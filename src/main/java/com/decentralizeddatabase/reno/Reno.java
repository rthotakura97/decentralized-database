package com.decentralizeddatabase.reno;

import com.decentralizeddatabase.errors.*;
import com.decentralizeddatabase.reno.crypto.Hasher;
import com.decentralizeddatabase.reno.filetable.*;
import com.decentralizeddatabase.reno.jailcellaccess.*;
import com.decentralizeddatabase.utils.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.json.JSONObject;

public class Reno {

    private static final Logger LOGGER = LoggerFactory.getLogger(Reno.class);

    public static void listAll(final DecentralizedDBRequest request,
                        final DecentralizedDBResponse response) throws BadRequest {
        LOGGER.info("Processing listAll");
        final String user = Validations.validateUser(request.getUser());

        final Collection<FileData> fileList = FileTable.getFiles(user);
        final List<String> filenames = new ArrayList<>();

        for (FileData file : fileList) {
            filenames.add(file.getFilename());
        }

        response.setList(filenames);
    }

    public static void read(final DecentralizedDBRequest request,
                     final DecentralizedDBResponse response) throws BadRequest,
                                                                    EncryptionError, 
                                                                    FileNotFoundError,
                                                                    JailCellServerError {
        final String filename = request.getFilename();
        final String user = Validations.validateUser(request.getUser());
        final String rawSecretKey = Validations.validateRawSecretKey(request.getSecretKey());
        final String secretKey = Hasher.createSecretKey(rawSecretKey);

        final long numBlocks = FileTable.getFile(user, filename).getFileSize();
        final List<String> keys = DataManipulator.createKeys(secretKey, filename, user, numBlocks);
        List<FileBlock> blocks = retrieve(keys);
        final String file = DataManipulator.makeFile(blocks, secretKey);

        response.setData(file);
    }

    public static void write(final DecentralizedDBRequest request,
                      final DecentralizedDBResponse response) throws BadRequest, 
                                                                     EncryptionError,
                                                                     FileNotFoundError,
                                                                     JailCellServerError {
        final String file = request.getFile();
        final String filename = request.getFilename();
        final String user = Validations.validateUser(request.getUser());
        final String rawSecretKey = Validations.validateRawSecretKey(request.getSecretKey());
        final String secretKey = Hasher.createSecretKey(rawSecretKey);
        LOGGER.info("Processing write request for {}", filename);

        final List<FileBlock> blocks = DataManipulator.createBlocks(secretKey, file);
        final List<String> keys = DataManipulator.createKeys(secretKey, filename, user, blocks.size());

        if (FileTable.containsFile(user, filename)) {
            LOGGER.info("{} already exists, overwriting...", filename);
            final long fileSize = FileTable.getFile(user, filename).getFileSize();
            final List<String> oldKeys = DataManipulator.createKeys(secretKey, filename, user, fileSize);
            sendForDelete(oldKeys);
        }
        sendForWrite(blocks, keys);

        FileTable.addFile(user, filename, blocks.size());
    }

    public static void delete(final DecentralizedDBRequest request,
                              final DecentralizedDBResponse response) throws BadRequest, 
                                                                             FileNotFoundError,
                                                                             JailCellServerError {
        final String filename = request.getFilename();
        final String user = Validations.validateUser(request.getUser());
        final String rawSecretKey = Validations.validateRawSecretKey(request.getSecretKey());
        final String secretKey = Hasher.createSecretKey(rawSecretKey);

        final long numBlocks = FileTable.getFile(user, filename).getFileSize();
        final List<String> blockKeys = DataManipulator.createKeys(secretKey, filename, user, numBlocks);

        sendForDelete(blockKeys);

        FileTable.removeFile(user, filename);
    }

    //Refactor and break up after confirmed working
    //TODO: Magic numbers (strings??)
    private static void sendForWrite(final List<FileBlock> blocks, final List<String> keys) throws JailCellServerError {
        final List<JailCellInfo> jailCells = JailCellAccess.getJailCells();
        final int numCells = jailCells.size();
        final List<List<String>> brokenUpBlocks = new ArrayList<>(Collections.nCopies(numCells, new ArrayList<>()));

        int keyIdx = 0;
        final List<List<String>> brokenUpKeys = new ArrayList<>(Collections.nCopies(numCells, new ArrayList<>()));

        final Random rnd = new Random();//todo set seed?
        
        for (FileBlock block : blocks) {
            final String encoded = block.encodeOrderIntoBlock();
            final String currKey = keys.get(keyIdx++);
            final int idx = rnd.nextInt(numCells);

            brokenUpBlocks.get(idx).add(encoded);
            brokenUpKeys.get(idx).add(currKey);
        }

        //TODO: Thread this
        for (int i = 0; i < numCells; i++) {
            final String jailCellUrl = jailCells.get(i).url;
            final List<String> currKeys = brokenUpKeys.get(i);
            final List<String> currBlocks = brokenUpBlocks.get(i);

            final JSONObject toPost = new JSONObject();
            toPost.put("method", "write");
            toPost.put("keys", currKeys);
            toPost.put("blocks", currBlocks);

            HttpUtility.postToJailCell(toPost, jailCellUrl);
        }
    }

    private static void sendForDelete(final List<String> keys) throws JailCellServerError {
        final List<JailCellInfo> jailCells = JailCellAccess.getJailCells();

        //Thread this
        for (JailCellInfo jailCell : jailCells) {
            final String jailCellUrl = jailCell.url;
            final JSONObject toPost = new JSONObject();
            toPost.put("method", "delete");
            toPost.put("keys", keys);

            HttpUtility.postToJailCell(toPost, jailCellUrl);
        }
    }

    private static List<FileBlock> retrieve(final List<String> keys) throws JailCellServerError {
        final List<JailCellInfo> jailCells = JailCellAccess.getJailCells();
        final List<String> encodedFileStrings = new ArrayList<>();//Needs to be threadsafe

        //Thread this
        for (JailCellInfo jailCell : jailCells) {
            final String jailCellUrl = jailCell.url;
            JSONObject toPost = new JSONObject();
            toPost.put("method", "read");
            toPost.put("keys", keys);
            JSONObject jsonObj = HttpUtility.postToJailCellWithResponse(toPost, jailCellUrl);
            List<String> blocks = (List<String>)(jsonObj.get("blocks"));
            encodedFileStrings.addAll(blocks);
        }

        //TODO: Streams?
        final List<FileBlock> fileBlocks = new ArrayList<>();
        for (String encodedString : encodedFileStrings) {
            fileBlocks.add(new FileBlock(encodedString));
        }

        return fileBlocks;
    }
}
