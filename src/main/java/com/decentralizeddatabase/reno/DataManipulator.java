package com.decentralizeddatabase.reno;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static com.decentralizeddatabase.utils.Constants.*;

public class DataManipulator {

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

}
