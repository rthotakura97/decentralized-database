package com.decentralizeddatabase.utils;

public class FileBlock {

    private final long blockOrder;
    private final byte[] encryptedData;

    public FileBlock(final long blockOrder, final byte[] encryptedData) {
	this.blockOrder = blockOrder;
	this.encryptedData = encryptedData;
    }

    public long getBlockOrder() {
	return blockOrder;
    }

    public byte[] getData() {
	return encryptedData;
    }
}
