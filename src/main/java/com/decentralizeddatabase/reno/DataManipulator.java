package com.decentralizeddatabase.reno;

import java.util.ArrayList;
import java.util.List;

public class DataManipulator {

    /**
     *
     * @param file File represented as string
     * @return List of byte arrays representing the broken file
     */
    public List<byte[]> breakFile(String file){
        List<byte[]> fileSegments = new ArrayList<byte[]>();
        int sizeOfSegment = (int) Math.ceil(file.length()/4.0);
        int index = 0;
        while(index < file.length()){
            fileSegments.add((file.substring(index, Math.min(index+sizeOfSegment, file.length()))).getBytes());
            index+=sizeOfSegment;
        }
        return fileSegments;
    }

}
