package com.decentralizeddatabase.reno;

public class FileData extends Object {

    private final String filename;
    private final long fileSize;

    public FileData(final String filename, final long fileSize) {
	this.filename = filename;
	this.fileSize = fileSize;
    }

    public String getFilename() {
	return this.filename;
    }

    public long getFileSize() {
	return this.fileSize;
    }
}
