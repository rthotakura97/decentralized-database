package com.decentralizeddatabase.errors;

public class EncryptionError extends DecentralizedDBError {

    public EncryptionError(final int errorCode, final String message) {
	this.errorCode = errorCode;
	this.message = message;
    }
}
