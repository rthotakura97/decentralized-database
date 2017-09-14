package com.decentralizeddatabase.reno.crypto;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class CryptoBlock {

    private final Cipher cipher;

    public CryptoBlock() throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.cipher = Cipher.getInstance("AES");
    }

    /**
     *  @param block byte[] data block 
     *  @param key String 16 byte secret key
     *  @return byte[] encrypted data block
     */
    public byte[] encrypt(final byte[] block, final String key) throws NoSuchPaddingException, 
							   NoSuchAlgorithmException, 
							   InvalidKeyException, 
							   BadPaddingException, 
							   IllegalBlockSizeException {
        final Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        final byte[] encrypted = cipher.doFinal(block);
        return encrypted;
    }

    /**
     *   @param block byte[] encrypted data block
     *   @param key String 16 byte secret key
     *   @return byte[] decrypted data block
     */
    public byte[] decrypt(final byte[] block, final String key) throws InvalidKeyException, 
							   BadPaddingException, 
							   IllegalBlockSizeException {
        final Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        final byte[] decrypted = cipher.doFinal(block);
        return decrypted;
    }
}
