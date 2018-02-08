package com.distributeddb.errors;

public class EncryptionError extends DecentralizedDBError {

    public EncryptionError(final String message) {
        this.errorCode = 500;
        this.message = message;
    }
}
