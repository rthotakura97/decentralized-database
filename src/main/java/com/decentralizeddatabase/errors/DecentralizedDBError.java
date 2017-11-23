package com.decentralizeddatabase.errors;

public class DecentralizedDBError extends Exception {
    
    protected String message;
    protected int errorCode;

    public String getMessage() {
        return this.message;
    }

    public int getErrorCode() {
        return this.errorCode;
    }
}
