package com.example.myapplication;

import android.os.Message;
import android.util.Base64;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by vinuth on 01/12/16.
 */

public class AESMessage {
    final static String SECRET = "hello";
    final Key key = new SecretKeySpec(SECRET.getBytes(), "AES");
    final static int keylength = 256;
    // AES specification - changing will break existing encrypted streams!
    private static final String CIPHER_SPEC = "AES";

    // Key derivation specification - changing will break existing streams!
    private static final String KEYGEN_SPEC = "PBKDF2WithHmacSHA1";
    private static final int SALT_LENGTH = 16; // in bytes
    private static final int AUTH_KEY_LENGTH = 8; // in bytes
    private static final int ITERATIONS = 32768;

    public static String encrypt(String message){
        Keys keys = keygen(keylength,SECRET.toCharArray());
        Cipher encrypt = null;
        try {
            encrypt = Cipher.getInstance(CIPHER_SPEC);
            encrypt.init(Cipher.ENCRYPT_MODE, keys.encryption);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException impossible) { } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        byte[] buffer = Base64.decode(message,Base64.DEFAULT);
        int numRead;
        byte[] encrypted = null;
        try {
            encrypted = encrypt.doFinal(buffer);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        if (encrypted != null) {
            message = Base64.encodeToString(encrypted,Base64.DEFAULT);
        }
        return message;
    }

    public static String decrypt(String message)
            throws AES.InvalidPasswordException, AES.InvalidAESStreamException, IOException,
            AES.StrongEncryptionNotAvailableException {




        Keys keys = keygen(keylength,SECRET.toCharArray());
        byte[] authRead = new byte[AUTH_KEY_LENGTH];

        //if (!Arrays.equals(keys.authentication.getEncoded(), authRead)) {
        //  throw new InvalidPasswordException();
        //}

        // initialize AES decryption
        Cipher decrypt = null;
        try {
            decrypt = Cipher.getInstance(CIPHER_SPEC);
            decrypt.init(Cipher.DECRYPT_MODE, keys.encryption);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException impossible) { }
        catch (InvalidKeyException e) { // 192 or 256-bit AES not available
            throw new AES.StrongEncryptionNotAvailableException(keylength);
        }

        // read data from input into buffer, decrypt and write to output
        byte[] buffer = Base64.decode(message,Base64.DEFAULT);
        byte[] decrypted = new byte[0];
        try {
            decrypted = decrypt.doFinal(buffer);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        if (decrypted != null) {
            message = Base64.encodeToString(decrypted,Base64.DEFAULT);
        }

       return message;
    }

    private static AESMessage.Keys keygen(int keyLength, char[] password ) {
        SecretKeyFactory factory;
        try {
            factory = SecretKeyFactory.getInstance(KEYGEN_SPEC);
        } catch (NoSuchAlgorithmException impossible) { return null; }
        // derive a longer key, then split into AES key and authentication key
        KeySpec spec = new PBEKeySpec(password, SECRET.getBytes(), ITERATIONS, keyLength + AUTH_KEY_LENGTH * 8);
        SecretKey tmp = null;
        try {
            tmp = factory.generateSecret(spec);
        } catch (InvalidKeySpecException impossible) { }
        byte[] fullKey = tmp.getEncoded();
        SecretKey authKey = new SecretKeySpec( // key for password authentication
                Arrays.copyOfRange(fullKey, 0, AUTH_KEY_LENGTH), "AES");
        SecretKey encKey = new SecretKeySpec( // key for AES encryption
                Arrays.copyOfRange(fullKey, AUTH_KEY_LENGTH, fullKey.length), "AES");
        return new AESMessage.Keys(encKey, authKey);
    }

    private static class Keys {
        public final SecretKey encryption, authentication;
        public Keys(SecretKey encryption, SecretKey authentication) {
            this.encryption = encryption;
            this.authentication = authentication;
        }
    }


    //******** EXCEPTIONS thrown by encrypt and decrypt ********

    /**
     * Thrown if an attempt is made to decrypt a stream with an incorrect password.
     */
    public static class InvalidPasswordException extends Exception { }

    /**
     * Thrown if an attempt is made to encrypt a stream with an invalid AES key length.
     */
    public static class InvalidKeyLengthException extends Exception {
        InvalidKeyLengthException(int length) {
            super("Invalid AES key length: " + length);
        }
    }

    /**
     * Thrown if 192- or 256-bit AES encryption or decryption is attempted,
     * but not available on the particular Java platform.
     */
    public static class StrongEncryptionNotAvailableException extends Exception {
        public StrongEncryptionNotAvailableException(int keySize) {
            super(keySize + "-bit AES encryption is not available on this Java platform.");
        }
    }

    /**
     * Thrown if an attempt is made to decrypt an invalid AES stream.
     */
    public static class InvalidAESStreamException extends Exception {
        public InvalidAESStreamException() { super(); };
        public InvalidAESStreamException(Exception e) { super(e); }
    }
}
