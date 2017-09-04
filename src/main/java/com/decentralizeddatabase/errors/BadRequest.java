package com.decentralizeddatabase.errors;

public final class BadRequest extends DecentralizedDBError {

    public BadRequest(final int errorCode, final String message) {
	this.errorCode = errorCode;
	this.message = message;
    }
}
