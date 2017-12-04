package com.decentralizeddatabase.reno;

import com.decentralizeddatabase.errors.BadRequest;
import com.decentralizeddatabase.errors.EncryptionError;
import com.decentralizeddatabase.errors.FileNotFoundError;
import com.decentralizeddatabase.reno.crypto.Hasher;
import com.decentralizeddatabase.reno.filetable.FileData;
import com.decentralizeddatabase.reno.filetable.FileTable;
import com.decentralizeddatabase.utils.DecentralizedDBRequest;
import com.decentralizeddatabase.utils.DecentralizedDBResponse;
import com.decentralizeddatabase.utils.FileBlock;
import com.decentralizeddatabase.utils.Validations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Reno {

    private final FileTable fileTable;
    private static final Logger LOGGER = LoggerFactory.getLogger(Reno.class);

    public Reno() {
        this.fileTable = new FileTable();
    }

    public void listAll(final DecentralizedDBRequest request,
                        final DecentralizedDBResponse response) throws BadRequest {
        LOGGER.info("Processing listAll");
        final String user = Validations.validateUser(request.getUser());

        final Collection<FileData> fileList = fileTable.getFiles(user);
        final List<String> filenames = new ArrayList<>();

        for (FileData file : fileList) {
            filenames.add(file.getFilename());
        }

        response.setList(filenames);
    }

    public void read(final DecentralizedDBRequest request,
                     final DecentralizedDBResponse response) throws BadRequest,
                                                                    EncryptionError, 
                                                                    FileNotFoundError {
        final String filename = request.getFilename();
        final String user = Validations.validateUser(request.getUser());
        final String rawSecretKey = Validations.validateRawSecretKey(request.getSecretKey());
        final String secretKey = Hasher.createSecretKey(rawSecretKey);

        final long numBlocks = fileTable.getFile(user, filename).getFileSize();
        final List<String> keys = DataManipulator.createKeys(secretKey, filename, user, numBlocks);

        final List<FileBlock> blocks = retrieve(keys);
        final String file = DataManipulator.makeFile(blocks, secretKey);

        response.setData(file);
    }

    public void write(final DecentralizedDBRequest request,
                      final DecentralizedDBResponse response) throws BadRequest, 
                                                                     EncryptionError,
                                                                     FileNotFoundError {
        final String file = request.getFile();
        final String filename = request.getFilename();
        final String user = Validations.validateUser(request.getUser());
        final String rawSecretKey = Validations.validateRawSecretKey(request.getSecretKey());
        final String secretKey = Hasher.createSecretKey(rawSecretKey);
        LOGGER.info("Processing write request for {}", filename);

        final List<FileBlock> blocks = DataManipulator.createBlocks(secretKey, file);
        final List<String> keys = DataManipulator.createKeys(secretKey, filename, user, blocks.size());

        // This will need some looking into when we implement Jailcell
        // TODO: Design ?, How do we overwrite existing files?
        if (fileTable.containsFile(user, filename)) {
            LOGGER.info("{} already exists, overwriting...", filename);
            final long fileSize = fileTable.getFile(user, filename).getFileSize();
            final List<String> oldKeys = DataManipulator.createKeys(secretKey, filename, user, fileSize);
            sendForDelete(oldKeys);
        }
        sendForWrite(blocks, keys);

        fileTable.addFile(user, filename, blocks.size());
    }

    public void delete(final DecentralizedDBRequest request,
                       final DecentralizedDBResponse response) throws BadRequest, 
                                                                      FileNotFoundError {
        final String filename = request.getFilename();
        final String user = Validations.validateUser(request.getUser());
        final String rawSecretKey = Validations.validateRawSecretKey(request.getSecretKey());
        final String secretKey = Hasher.createSecretKey(rawSecretKey);

        final long numBlocks = fileTable.getFile(user, filename).getFileSize();
        final List<String> blockKeys = DataManipulator.createKeys(secretKey, filename, user, numBlocks);

        sendForDelete(blockKeys);

        fileTable.removeFile(user, filename);
    }

    private void sendForWrite(final List<FileBlock> blocks, final List<String> keys) {
        //TODO
        // break up keys and blocks
        // post blocks and keys as JSON
    }

    private void sendForDelete(final List<String> keys) {
        //TODO
        // send all keys to every node
        // post keys as JSON
    }

    private List<FileBlock> retrieve(final List<String> keys) {
        //TODO
        // send all keys to every node
        // compile returned files into a single list
        return null;
    }
}
