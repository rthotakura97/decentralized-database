package com.decentralizeddatabase.utils;

public class FileBlock {

    private final long blockOrder;
    private final String encryptedData;

    public FileBlock(final long blockOrder, final String encryptedData) {
        this.blockOrder = blockOrder;
        this.encryptedData = encryptedData;
    }

    public long getBlockOrder() {
        return blockOrder;
    }

    public String getData() {
        return encryptedData;
    }
}
