package com.decentralizeddatabase.reno.crypto;

import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class CryptoBlock {

    private static final String IV = "0987654321123456";

    /**
     *  @param block String data block 
     *  @param key String 16 byte secret key
     *  @return String  encrypted data block
     */
    public static String encrypt(final String block, final String key) throws Exception {
        final Key aesKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
	final IvParameterSpec iv = new IvParameterSpec(IV.getBytes("UTF-8"));

	final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey, iv);

        final byte[] encrypted = cipher.doFinal(block.getBytes("UTF-8"));

        return Base64.encodeBase64String(encrypted);
    }

    /**
     *   @param block String encrypted data block
     *   @param key String 16 byte secret key
     *   @return String decrypted data block
     */
    public static String decrypt(final String block, final String key) throws Exception {
        final Key aesKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
	final IvParameterSpec iv = new IvParameterSpec(IV.getBytes("UTF-8"));

	final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, aesKey, iv);

        final byte[] decrypted = cipher.doFinal(Base64.decodeBase64(block));

        return new String(decrypted);
    }
}
