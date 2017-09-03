package com.decentralizeddatabase.utils;

public class DecentralizedDBResponse {
    
    private boolean success;
    private byte[] data;

    public void setSuccess(final boolean success) {
	this.success = success;
    }

    public boolean getSuccess() {
	return success;
    }

    public void setData(final byte[] data) {
	this.data = data;
    }

    public byte[] getData() {
	return data;
    }
}
