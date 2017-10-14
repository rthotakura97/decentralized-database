package com.decentralizeddatabase.reno;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Iterator;
import java.util.List;

import com.decentralizeddatabase.errors.EncryptionError;
import com.decentralizeddatabase.errors.FileNotFoundError;
import com.decentralizeddatabase.reno.crypto.CryptoBlock;
import com.decentralizeddatabase.reno.crypto.Hasher;
import com.decentralizeddatabase.utils.FileBlock;
import static com.decentralizeddatabase.utils.Constants.*;

public class DataManipulator {

    private final CryptoBlock cryptoBlock;

    public DataManipulator() throws EncryptionError {
        try {
            this.cryptoBlock = new CryptoBlock();
        } catch (Exception e) {
            throw new EncryptionError("There has been an error initializing our encryption scheme");
        }
    }

    /**
     *
     * @param file File represented as string
     * @return List of byte arrays representing the broken file
     */
    public static List<byte[]> breakFile(final String file){
	final List<byte[]> fileSegments = new ArrayList<>();

	final int length = file.length();
        final int sizeOfSegment = (int) Math.ceil(length / NUM_BLOCKS);

        int index = 0;
        while (index < length) {
            fileSegments.add((file.substring(index, Math.min(index+sizeOfSegment, length))).getBytes());
            index += sizeOfSegment;
        }

        return fileSegments;
    }

    /**
     * @param numberOfBlocks Int representing total number of blocks to make block orders for
     * @return a list of Longs w/ the block orders
     */

    public static List<Long> getBlockOrderValues(final int numberOfBlocks) {
        final List<Long> blockOrders = new ArrayList<Long>();
	long maxLong = Long.MAX_VALUE - 1_000_000_000;

        final Long first = ThreadLocalRandom.current().nextLong(Long.MIN_VALUE, maxLong);
        blockOrders.add(first);

        for (int i = 1; i < numberOfBlocks; i++) {
	    maxLong += (Long.MAX_VALUE - maxLong) / 2;

	    final Long blockOrder = ThreadLocalRandom.current().nextLong(blockOrders.get(i-1) + 1, maxLong);
            blockOrders.add(blockOrder);
        }

        return blockOrders;
    }

    private static class SortBlocks implements Comparator<FileBlock> {
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

    public static List<String> createKeys(final String secretKey, final String filename, final String user, final long fileSize) {
        final List<String> keys = new ArrayList<>();

        for (int blockNum  = 0; blockNum < fileSize; blockNum++) {
            final String key = Hasher.createBlockKey(secretKey, filename, blockNum);
            keys.add(key);
        }

        return keys;
    }

    public String makeFile(final List<FileBlock> blocks, final String secretKey) throws EncryptionError {
        Collections.sort(blocks, new SortBlocks());
        String fileString = "";

        for(FileBlock block : blocks) {
            final byte[] blockData = block.getData();
	    byte[] decryptedData;

            try {
                decryptedData = cryptoBlock.decrypt(blockData, secretKey);
            } catch (Exception e) {
		throw new EncryptionError("Could not decrypt your file");
            }

            fileString += new String(decryptedData);
        }

        return fileString;
    }

    public List<FileBlock> createBlocks(final String secretKey, final String file) throws EncryptionError {
        final List<FileBlock> blocks = new ArrayList<>();

        final List<byte[]> splitFiles = DataManipulator.breakFile(file);
        final int numBlocks = splitFiles.size();
        final List<Long> blockOrders = DataManipulator.getBlockOrderValues(numBlocks);

        final Iterator<byte[]> it1 = splitFiles.iterator();
        final Iterator<Long> it2 = blockOrders.iterator();

        while (it1.hasNext() && it2.hasNext()) {
            byte[] encrypted = null;
            try {
		encrypted = cryptoBlock.encrypt(it1.next(), secretKey);
            } catch (Exception e) {
		throw new EncryptionError("There has been an error encrypting your files");
            }
            final long blockOrder = it2.next();

            final FileBlock block = new FileBlock(blockOrder, encrypted);
            blocks.add(block);
        }

        return blocks;
    }
}
