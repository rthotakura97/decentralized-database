package com.decentralizeddatabase.utils;

import java.math.BigInteger;

public class FileBlock {

    private final long blockOrder;
    private final String encryptedData;

    /**
     * @param blockOrder 
     * @param encryptedData
     * 
     * Builds a FileBlock using blockOrder and encryptedData
     */
    public FileBlock(final long blockOrder, final String encryptedData) {
        this.blockOrder = blockOrder;
        this.encryptedData = encryptedData;
    }

    /**
     * @param encodedData String returned by JailCell
     * 
     * Builds a FileBlock from raw data returned by JailCell
     */
    public FileBlock(final String encodedData) {
        final String order = encodedData.substring(0,16);
        blockOrder = new BigInteger(order, 16).longValue();
        encryptedData = encodedData.substring(16);
    }

    public long getBlockOrder() {
        return blockOrder;
    }

    public String getData() {
        return encryptedData;
    }

    /**
     * @return  A blob with the first 64 bits representing a signed long, the rest of the block represents
     *          the encrypted string
     * 
     * Constructs and returns a string with encoded blockOrder into encryptedData
     */
    public String encodeOrderIntoBlock() {
        final StringBuilder order = new StringBuilder(Long.toHexString(blockOrder));
        while (order.length() < 16) {
            order.insert(0, "0");
        }
        order.append(encryptedData);

        return order.toString();
    }
}
