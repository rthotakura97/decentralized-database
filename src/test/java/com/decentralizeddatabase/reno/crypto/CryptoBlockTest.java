package com.decentralizeddatabase.reno.crypto;

import org.junit.Assert;
import org.junit.Before;
import org.junit.rules.ExpectedException;
import org.junit.Rule;
import org.junit.Test;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;

public final class CryptoBlockTest {

    private Cipher cipher;
    private CryptoBlock cryptoBlock;

    private static final String KEY = "0123456789123456";
    private static final byte[] BLOCK = "Block".getBytes();
    
    private byte[] encryptHelper(final byte[] block, final String key) throws Exception {
        final Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        final byte[] encrypted = cipher.doFinal(block);
        return encrypted;
    }

    private byte[] decryptHelper(final byte[] block, final String key) throws Exception {
        final Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        final byte[] decrypted = cipher.doFinal(block);
        return decrypted;
    }

    @Before
    public void setup() throws Exception {
	cipher = Cipher.getInstance("AES");
	cryptoBlock = new CryptoBlock();
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testEncryptWithValidKey() throws Exception {
	final byte[] expected = encryptHelper(BLOCK, KEY);	
	final byte[] actual = cryptoBlock.encrypt(BLOCK, KEY);

	final int length = expected.length;
	for (int idx = 0; idx < length; idx++) {
	    Assert.assertEquals(expected[idx], actual[idx]);
	}
    }

    @Test
    public void testEncryptWithInvalidKey() throws Exception {
	thrown.expect(InvalidKeyException.class);
	cryptoBlock.encrypt(BLOCK, "invalid");
	
	Assert.fail("No InvalidKeyException thrown");
    }

    @Test
    public void testDecryptWithValidKey() throws Exception {
	final byte[] encryptedBlock = encryptHelper(BLOCK, KEY);

	final byte[] expected = decryptHelper(encryptedBlock, KEY);
	final byte[] actual = cryptoBlock.decrypt(encryptedBlock, KEY);

	final int length = expected.length;
	for (int idx = 0; idx < length; idx++) {
	    Assert.assertEquals(expected[idx], actual[idx]);
	}
    }

    @Test
    public void testDecryptWithInvalidKey() throws Exception {
	thrown.expect(InvalidKeyException.class);
	cryptoBlock.decrypt(BLOCK, "invalid");
	
	Assert.fail("No InvalidKeyException thrown");
    }
}
