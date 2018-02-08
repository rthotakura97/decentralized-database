package com.distributeddb.reno.filetable;

public class FileData extends Object {

    private final String filename;
    private long fileSizeInBlocks;

    public FileData(final String filename, final long fileSizeInBlocks) {
        this.filename = filename;
        this.fileSizeInBlocks = fileSizeInBlocks;
    }

    public String getFilename() {
        return this.filename;
    }

    public long getFileSize() {
        return this.fileSizeInBlocks;
    }

    public void updateFileSize(final long fileSizeInBlocks) {
        this.fileSizeInBlocks = fileSizeInBlocks;
    }
}
