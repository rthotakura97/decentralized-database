package com.distributeddb.reno.crypto;

import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import org.junit.Assert;
import org.junit.Test;

public class HasherTest {

    private static final String TEST_SECRET_KEY = "Secret Key";
    private static final String TEST_FILENAME = "Some file";
    private static final String SALT = "lijsl9j932l*((@3))";

    private String hashKey(final String secretKey, final String filename, final int blockNumber) {
        return Hashing.sha1().hashString(secretKey + filename + Integer.toString(blockNumber), Charsets.UTF_8).toString();	
    }

    private String hashKey(final String userSecretKey) {
        return Hashing.sha1()
            .hashString(userSecretKey + SALT, Charsets.UTF_8)
            .toString()
            .substring(0, 16);
    }

    @Test
    public void testCreateBlockKey() {
        final String expected = hashKey(TEST_SECRET_KEY, TEST_FILENAME, 0);
        final String actual = Hasher.createBlockKey(TEST_SECRET_KEY, TEST_FILENAME, 0);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCreateSecretKey() {
        final String expected = hashKey(TEST_SECRET_KEY);
        final String actual = Hasher.createSecretKey(TEST_SECRET_KEY);

        Assert.assertEquals(16, actual.length());
        Assert.assertEquals(expected, actual);
    }
}