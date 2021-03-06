package com.distributeddb.reno.crypto;

import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public class Hasher {

    private static final String SALT = "lijsl9j932l*((@3))";

    /**
     * @param secretKey String
     * @param filename String
     * @param blockNumber int
     * @return String (20byte SHA-1 hash)
     */
    public static String createBlockKey(final String secretKey, final String filename, final long blockNumber){
        final String hashed = Hashing.sha1()
            .hashString(secretKey + filename + Long.toString(blockNumber), Charsets.UTF_8)
            .toString();

        return hashed;
    }

    /**
     * @param userSecretKey secret key as the user knows it
     * @return 16 byte string needed to encrypt all data
     */
    public static String createSecretKey(final String userSecretKey) {
        final String key = Hashing.sha1()
            .hashString(userSecretKey + SALT, Charsets.UTF_8)
            .toString()
            .substring(0, 16);

        return key;
    }
}
