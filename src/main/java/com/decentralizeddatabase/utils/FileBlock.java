package com.decentralizeddatabase.utils;

public class FileBlock {

    private final long blockNumber;
    private final byte[] encryptedData;

    public FileBlock(final long blockNumber, final byte[] encryptedData) {
	this.blockNumber = blockNumber;
	this.encryptedData = encryptedData;
    }

    public long getBlockNumber() {
	return blockNumber;
    }

    public byte[] getData() {
	return encryptedData;
    }
}
