package com.decentralizeddatabase.reno;

import java.util.ArrayList;
import java.util.List;

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

}
