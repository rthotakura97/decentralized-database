package com.decentralizeddatabase.reno;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Iterator;
import java.util.List;

import com.decentralizeddatabase.errors.EncryptionError;
import com.decentralizeddatabase.reno.crypto.CryptoBlock;
import com.decentralizeddatabase.reno.crypto.Hasher;
import com.decentralizeddatabase.utils.FileBlock;
import static com.decentralizeddatabase.utils.Constants.*;

public class DataManipulator {

    private static List<String> breakFile(final String file) {
	final List<String> fileSegments = new ArrayList<>();

	final int length = file.length();
        final int sizeOfSegment = (int) Math.ceil(length / NUM_BLOCKS);

        int index = 0;
        while (index < length) {
	    final String chunk = file.substring(index, Math.min(index + sizeOfSegment, length));
            fileSegments.add(chunk);
            index += sizeOfSegment;
        }

        return fileSegments;
    }

    private static List<Long> getBlockOrderValues(final int numberOfBlocks) {
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

    public static String makeFile(final List<FileBlock> blocks, final String secretKey) throws EncryptionError {
        Collections.sort(blocks, new SortBlocks());
        String fileString = "";

        for(FileBlock block : blocks) {
            final String blockData = block.getData();
	    String decryptedData;

            try {
                decryptedData = CryptoBlock.decrypt(blockData, secretKey);
            } catch (Exception e) {
		throw new EncryptionError("Could not decrypt your file");
            }

            fileString += decryptedData;
        }

        return fileString;
    }

    public static List<FileBlock> createBlocks(final String secretKey, final String file) throws EncryptionError {
        final List<FileBlock> blocks = new ArrayList<>();

        final List<String> splitFiles = breakFile(file);
        final int numBlocks = splitFiles.size();
        final List<Long> blockOrders = getBlockOrderValues(numBlocks);

        final Iterator<String> it1 = splitFiles.iterator();
        final Iterator<Long> it2 = blockOrders.iterator();

        while (it1.hasNext() && it2.hasNext()) {
            String encrypted = null;
            try {
		encrypted = CryptoBlock.encrypt(it1.next(), secretKey);
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
