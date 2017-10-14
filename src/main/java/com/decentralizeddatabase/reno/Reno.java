package com.decentralizeddatabase.reno;

import com.decentralizeddatabase.errors.BadRequest;
import com.decentralizeddatabase.errors.EncryptionError;
import com.decentralizeddatabase.errors.FileNotFoundError;
import com.decentralizeddatabase.reno.crypto.Hasher;
import com.decentralizeddatabase.reno.filetable.FileData;
import com.decentralizeddatabase.reno.filetable.FileTable;
import com.decentralizeddatabase.utils.DecentralizedDBRequest;
import com.decentralizeddatabase.utils.DecentralizedDBResponse;
import com.decentralizeddatabase.utils.FileBlock;
import com.decentralizeddatabase.utils.Validations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.decentralizeddatabase.utils.Constants.*;

public class Reno {

    private final DataManipulator dataManipulator;
    private final FileTable fileTable;

    public Reno() throws EncryptionError {
	this.dataManipulator = new DataManipulator();
        this.fileTable = new FileTable();
    }

    public void listAll(final DecentralizedDBRequest request,
			final DecentralizedDBResponse response) throws BadRequest {
        final String user = Validations.validateUser(request.getUser());

        final Collection<FileData> fileList = fileTable.getFiles(user);
        final List<String> filenames = new ArrayList<>();

        for (FileData file : fileList) {
            filenames.add(file.getFilename());
        }

        response.setList(filenames);
    }

    public void read(final DecentralizedDBRequest request,
		     final DecentralizedDBResponse response) throws BadRequest,
								    EncryptionError, 
								    FileNotFoundError {
        final String filename = request.getFilename();
	final String user = Validations.validateUser(request.getUser());
	final String rawSecretKey = Validations.validateRawSecretKey(request.getSecretKey());
        final String secretKey = Hasher.createSecretKey(rawSecretKey);

	final long numBlocks = fileTable.getFile(user, filename).getFileSize();
        final List<String> keys = DataManipulator.createKeys(secretKey, filename, user, numBlocks);

        final List<FileBlock> blocks = retrieve(keys);
        final String file = dataManipulator.makeFile(blocks, secretKey);

        response.setData(file);
    }

    public void write(final DecentralizedDBRequest request,
		      final DecentralizedDBResponse response) throws BadRequest, 
								     EncryptionError {
        final String file = request.getFile();
        final String filename = request.getFilename();
	final String user = Validations.validateUser(request.getUser());
	final String rawSecretKey = Validations.validateRawSecretKey(request.getSecretKey());
        final String secretKey = Hasher.createSecretKey(rawSecretKey);

        final List<FileBlock> blocks = dataManipulator.createBlocks(secretKey, file);
        final List<String> keys = DataManipulator.createKeys(secretKey, filename, user, blocks.size());

	// This will need some looking into when we implement Jailcell
	// TODO: Design ?, How do we overwrite existing files?
	if (fileTable.containsFile(user, filename)) {
	    sendForDelete(keys);
	}
        sendForWrite(blocks, keys);

	fileTable.addFile(user, filename, blocks.size());
    }

    public void delete(final DecentralizedDBRequest request,
		       final DecentralizedDBResponse response) throws BadRequest, 
								      FileNotFoundError {
        final String filename = request.getFilename();
        final String user = Validations.validateUser(request.getUser());
	final String rawSecretKey = Validations.validateRawSecretKey(request.getSecretKey());
        final String secretKey = Hasher.createSecretKey(rawSecretKey);

	final long numBlocks = fileTable.getFile(user, filename).getFileSize();
        final List<String> blockKeys = DataManipulator.createKeys(secretKey, filename, user, numBlocks);

        sendForDelete(blockKeys);

	fileTable.removeFile(user, filename);
    }

    private void sendForWrite(final List<FileBlock> blocks, final List<String> keys) {
	//TODO
    }

    private void sendForDelete(final List<String> keys) {
	//TODO
    }

    private final List<FileBlock> retrieve(final List<String> keys){
	//TODO
        return null;
    }
}
