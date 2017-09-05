package com.decentralizeddatabase.reno;

import java.io.File;
import java.util.*;

import com.decentralizeddatabase.errors.BadRequest;
import com.decentralizeddatabase.errors.EncryptionError;
import com.decentralizeddatabase.utils.DecentralizedDBRequest;
import com.decentralizeddatabase.utils.DecentralizedDBResponse;
import com.decentralizeddatabase.utils.FileBlock;

import java.util.ArrayList;
import java.util.List;

import static com.decentralizeddatabase.utils.Constants.*;

public class Reno {

    private final CryptoBlock cryptoBlock;

    public Reno() throws EncryptionError {
	try {
	    this.cryptoBlock = new CryptoBlock();
	} catch (Exception e) {
	    throw new EncryptionError(500, "There has been an error initializing our encryption scheme");
	}
    }

    public void listAll(final DecentralizedDBRequest request,
			final DecentralizedDBResponse response) throws BadRequest {

    }

    public void read(final DecentralizedDBRequest request,
		     final DecentralizedDBResponse response) throws BadRequest {
        final String filename = request.getFilename();
        final String user = request.getUser();
        final String secretKey = request.getSecretKey();

        final List<String> keys = createKeys(secretKey, filename, user, 4); //TODO: use multimap to get #blocks)
        List<FileBlock> blocks = retrieve(keys);
        String file = makeFile(blocks, secretKey);

        response.setData(file);
    }

    public void write(final DecentralizedDBRequest request,
		      final DecentralizedDBResponse response) throws BadRequest, EncryptionError {
        final String file = request.getFile();
        final String filename = request.getFilename();
        final String user = request.getUser();
        final String secretKey = request.getSecretKey();

        final List<FileBlock> blocks = createBlocks(secretKey, file);
        final int numBlocks = blocks.size();
        final List<String> keys = createKeys(secretKey, filename, user, numBlocks);

        send(blocks, keys);

        // TODO: After successful request, save new filename
    }

    public void delete(final DecentralizedDBRequest request,
		       final DecentralizedDBResponse response) throws BadRequest {
        final String filename = request.getFilename();
        final String user = request.getUser();
        final String secretKey = request.getSecretKey();

        final List<String> blockKeys = createKeys(secretKey, filename, user, (int) NUM_BLOCKS);

        //TODO: List of keys for blocks created, need to send to jailcell to retrieve blocks
        send(blockKeys);

    }

    private List<FileBlock> createBlocks(final String secretKey, final String file) throws EncryptionError {
        final List<FileBlock> blocks = new ArrayList<>();

        final List<byte[]> splitFiles = DataManipulator.breakFile(file);
        final int numBlocks = splitFiles.size();
        final List<Long> blockOrders = DataManipulator.getBlockOrderValues(numBlocks);

        final Iterator<byte[]> it1 = splitFiles.iterator();
        final Iterator<Long> it2 = blockOrders.iterator();

        while (it1.hasNext() && it2.hasNext()) {
            byte[] encrypted = null;
            try {
                //TODO: VALIDATE SECRET KEY AS 16 BYTES
            encrypted = cryptoBlock.encrypt(it1.next(), secretKey);
            } catch (Exception e) {
            throw new EncryptionError(500, "There has been an error encrypting your files");
            }
            final long blockOrder = it2.next();

            final FileBlock block = new FileBlock(blockOrder, encrypted);
            blocks.add(block);
        }

        return blocks;
    }

    private List<String> createKeys(final String secretKey, final String filename, final String user, final int fileSize) {
        final List<String> keys = new ArrayList<>();

        for (int blockNum  = 0; blockNum < fileSize; blockNum++) {
            final String key = Hasher.createBlockKey(secretKey, filename, blockNum);
            keys.add(key);
        }

        return keys;
    }

    private String makeFile(final List<FileBlock> blocks, final String secretKey){
        Collections.sort(blocks, new SortBlocks());
        String fileString = "";
        for(int i=0; i<blocks.size(); i++){
            byte[] blockData = blocks.get(i).getData();
            try {
                //TODO: VALIDATE SECRET KEY AS 16 BYTES
                blockData = cryptoBlock.decrypt(blockData, secretKey);
            } catch (Exception e) {
                //TODO: Fill in exception
            }
            fileString += blockData.toString();
        }
        return fileString;
    }

    private void send(final List<FileBlock> blocks, final List<String> keys) {

    }

    private void send(final List<String> keys) {

    }

    private final List<FileBlock> retrieve(final List<String> keys){
        return null;
    }

    private class SortBlocks implements Comparator<FileBlock> {
        @Override
        public int compare(FileBlock o1, FileBlock o2) {
            if(o1.getBlockOrder() > o2.getBlockOrder())
                return 1;
            else if(o1.getBlockOrder() < o2.getBlockOrder())
                return -1;
            else
                return 0;
        }
    }
}
