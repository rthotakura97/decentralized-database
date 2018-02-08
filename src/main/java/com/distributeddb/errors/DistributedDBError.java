package com.distributeddb.errors;

public class DistributedDBError extends Exception {
    
    protected String message;
    protected int errorCode;

    public String getMessage() {
        return this.message;
    }

    public int getErrorCode() {
        return this.errorCode;
    }
}
