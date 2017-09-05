package com.decentralizeddatabase.reno;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class DataManipulator {

    private static final double NUM_BLOCKS = 4.0;

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

    public List<Long> getBlockOrderValues(int numberOfBlocks){
        List<Long> blockOrders = new ArrayList<Long>();

        Long first = ThreadLocalRandom.current().nextLong(Long.MIN_VALUE, Long.MAX_VALUE-numberOfBlocks);
        blockOrders.add(first);

        for(int i=1; i<numberOfBlocks; i++){
            blockOrders.add(ThreadLocalRandom.current().nextLong(blockOrders.get(i-1), Long.MAX_VALUE));
        }

        return blockOrders;
    }

}
