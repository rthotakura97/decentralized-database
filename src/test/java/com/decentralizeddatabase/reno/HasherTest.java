package com.decentralizeddatabase.reno;

import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import org.junit.Assert;
import org.junit.Test;

public class HasherTest {

    private static final String TEST_SECRET_KEY = "Secret Key";
    private static final String TEST_FILENAME = "Some file";

    private String hashKey(final String secretKey, final String filename, final int blockNumber) {
	return Hashing.sha1().hashString(secretKey+filename+Integer.toString(blockNumber), Charsets.UTF_8).toString();	
    }

    @Test
    public void testCreateBlockKey() {
	final String expected = hashKey(TEST_SECRET_KEY, TEST_FILENAME, 0);
	final String actual = Hasher.createBlockKey(TEST_SECRET_KEY, TEST_FILENAME, 0);

	Assert.assertEquals(expected, actual);
    }
}
