package com.decentralizeddatabase.reno;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

/*Initialize and use this class to deal with block encryption and decryption*/
public class CryptoBlock {

    private Cipher cipher;

    public CryptoBlock() throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.cipher = Cipher.getInstance("AES");
    }

    /*
        Inputs: String (data block), String (16 byte secret key)
        Outputs: byte[] (encrypted data block)
     */
    public byte[] encrypt(String block, String key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] encrypted = cipher.doFinal(block.getBytes());
        return encrypted;
    }

    /*
        Inputs: byte[] (encrypted data block), String (16 byte secret key)
        Outputs: String (decrypted data block)
     */
    public String decrypt(byte[] block, String key) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        String decrypted = new String(cipher.doFinal(block));
        return decrypted;
    }
}
