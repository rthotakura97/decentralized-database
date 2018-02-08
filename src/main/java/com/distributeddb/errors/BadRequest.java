package com.distributeddb.errors;

public final class BadRequest extends DistributedDBError {

    public BadRequest(final String message) {
        this.errorCode = 400;
        this.message = String.format("BadRequest: %s", message);
    }
}
