package com.distributeddb.reno.crypto;

import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Before;
import org.junit.rules.ExpectedException;
import org.junit.Rule;
import org.junit.Test;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;

public final class CryptoBlockTest {

    private static final String KEY = "0123456789123456";
    private static final String IV = "0987654321123456";
    private static final String BLOCK = "Block";
    
    private String encryptHelper(final String block, final String key) throws Exception {
        final Key aesKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        final IvParameterSpec iv = new IvParameterSpec(IV.getBytes("UTF-8"));

        final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey, iv);

        final byte[] encrypted = cipher.doFinal(block.getBytes("UTF-8"));

        return Base64.encodeBase64String(encrypted);
    }

    private String decryptHelper(final String block, final String key) throws Exception {
        final Key aesKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        final IvParameterSpec iv = new IvParameterSpec(IV.getBytes("UTF-8"));

        final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, aesKey, iv);

        final byte[] decrypted = cipher.doFinal(Base64.decodeBase64(block));

        return new String(decrypted);
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testEncryptWithValidKey() throws Exception {
        final String expected = encryptHelper(BLOCK, KEY);	
        final String actual = CryptoBlock.encrypt(BLOCK, KEY);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testEncryptWithInvalidKey() throws Exception {
        thrown.expect(InvalidKeyException.class);
        CryptoBlock.encrypt(BLOCK, "invalid");
        
        Assert.fail("No InvalidKeyException thrown");
    }

    @Test
    public void testDecryptWithValidKey() throws Exception {
        final String encryptedBlock = encryptHelper(BLOCK, KEY);

        final String expected = decryptHelper(encryptedBlock, KEY);
        final String actual = CryptoBlock.decrypt(encryptedBlock, KEY);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDecryptWithInvalidKey() throws Exception {
        thrown.expect(InvalidKeyException.class);
        CryptoBlock.decrypt(BLOCK, "invalid");
        
        Assert.fail("No InvalidKeyException thrown");
    }
}