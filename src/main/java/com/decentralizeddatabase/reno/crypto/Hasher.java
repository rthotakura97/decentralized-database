package com.decentralizeddatabase.reno.crypto;

import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public class Hasher {

    /**
     * @param secretKey String
     * @param filename String
     * @param blockNumber int
     * @return String (20byte SHA-1 hash)
     */
    public static String createBlockKey(final String secretKey, final String filename, final int blockNumber){
         final String hashed = Hashing.sha1()
                 .hashString(secretKey+filename+Integer.toString(blockNumber), Charsets.UTF_8)
                 .toString();
         return hashed;
    }

}
