package com.distributeddb.errors;

public class FileNotFoundError extends DistributedDBError {
    
    public FileNotFoundError(final String message) {
        this.errorCode = 404;
        this.message = String.format("FileNotFound: %s", message);
    }
}
