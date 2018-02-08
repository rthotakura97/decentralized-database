package com.distributeddb.errors;

public class JailCellServerError extends DecentralizedDBError {
    
    public JailCellServerError(final String message) {
        this.errorCode = 500;
        this.message = String.format("%s", message);
    }
}