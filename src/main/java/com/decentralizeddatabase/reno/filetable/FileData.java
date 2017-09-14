package com.decentralizeddatabase.reno.filetable;

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

    @Override
    public boolean equals(Object other) {
	final FileData otherFile = (FileData) other;
	return this.filename.equals(otherFile.getFilename());
    }
}
