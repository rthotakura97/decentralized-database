package com.distributeddb.errors;

public class EncryptionError extends DistributedDBError {

    public EncryptionError(final String message) {
        this.errorCode = 500;
        this.message = message;
    }
}
