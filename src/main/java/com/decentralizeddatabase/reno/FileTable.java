package com.decentralizeddatabase.reno;

import java.util.Collection;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;

public class FileTable {

    private final Multimap<String,FileData> fileTable;

    public FileTable() {
	fileTable = MultimapBuilder.hashKeys().hashSetValues().build();
    }

    public boolean addFile(final String user, final String filename, final long fileSize) {
	final FileData file = new FileData(filename, fileSize);
	return fileTable.put(user, file);
    }

    public Collection<FileData> getFiles(final String user) {
	return fileTable.get(user);
    }

    public FileData getFile(final String user, final String filename) {
	final Collection<FileData> files = getFiles(user);

	for (FileData file : files) {
	    if (filename.equals(file.getFilename())) {
		return file;
	    }
	}

	return null;
    }

    public boolean removeFile(final String user, final String filename) {
	final FileData file = getFile(user, filename);

	return fileTable.remove(user, file);
    }

    public boolean removeUser(final String user) {
	return fileTable.removeAll(user) != null;
    }
}
