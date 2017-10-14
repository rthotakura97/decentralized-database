package com.decentralizeddatabase.reno.filetable;

import java.util.Collection;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;

import com.decentralizeddatabase.errors.FileNotFoundError;

public class FileTable {

    private final Multimap<String,FileData> fileTable;

    public FileTable() {
	fileTable = MultimapBuilder.hashKeys().hashSetValues().build();
    }

    /**
     * @param String user - username of account
     * @param String filename - name of file to add
     * @param long fileSize - how large the file is in blocks
     * @return boolean - If the put operation was successful or not
     *
     * Duplicates are updated with the new fileSize
     */
    public boolean addFile(final String user, final String filename, final long fileSize) {
	FileData file;

	try {
	    file = getFile(user, filename);
	    file.updateFileSize(fileSize);
	} catch (FileNotFoundError e) {
	    file = new FileData(filename, fileSize);
	}

	return fileTable.put(user, file);
    }

    /**
     * @param String user - username of account
     * @return Collection of FileData that corresponds to the user, returns an empty collection if user does not exist
     */
    public Collection<FileData> getFiles(final String user) {
	return fileTable.get(user);
    }

    /**
     * @param String user
     * @param String filename
     * @return FileData block of the file
     * @throws FileNotFoundError if file does not exist for that user
     *
     * Retrieves a singular file
     */
    public FileData getFile(final String user, final String filename) throws FileNotFoundError {
	final Collection<FileData> files = getFiles(user);

	for (FileData file : files) {
	    if (filename.equals(file.getFilename())) {
		return file;
	    }
	}

	throw new FileNotFoundError(String.format("The file %s was not found", filename));
    }

    /**
     * @param String user
     * @param String filename
     * @throws FileNotFoundError if file does not exist for that user
     */
    public boolean removeFile(final String user, final String filename) throws FileNotFoundError {
	final FileData file = getFile(user, filename);

	return fileTable.remove(user, file);
    }

    /**
     * @param String user
     * @return boolean if user was successfully removed
     *
     * Removes user and all their files from FileTable
     */
    public boolean removeUser(final String user) {
	return fileTable.removeAll(user) != null;
    }

    /**
     * @param String user
     * @param String filename
     * @return Boolean if the file exists or not
     */
    public boolean containsFile(final String user, final String filename) {
	try {
	    getFile(user, filename);
	} catch (FileNotFoundError e) {
	    return false;
	}

	return true;
    }
}
