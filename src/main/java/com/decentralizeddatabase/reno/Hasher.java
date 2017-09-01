package com.decentralizeddatabase.reno;
import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public class Hasher {

    public String createBlockKey(String secretKey, String filename, int blockNumber){
         String hashed = Hashing.sha1()
                 .hashString(secretKey+filename+Integer.toString(blockNumber), Charsets.UTF_8)
                 .toString();
         return hashed;
    }

}
