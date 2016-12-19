package com.example.myapplication;


import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by vinuth on 06/12/16.
 */

public class RSA {

    Key privateKey,publicKey1,publickey2;
    private static final String KEYGEN_SPEC = "RSA";
    String pubExp = "10001";
    public RSA(PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException {
        //this.privateKey = new KeySpec(privateKey.getPrivByte(),0,privateKey.getPrivByte().length,"KEYGEN_SPEC");
        //this.publicKey = new KeySpec(privateKey.getPubByte(),0,privateKey.getPubByte().length,"KEYGEN_SPEC");
        this.setPrivateKey(privateKey.getPrivateKey());
        this.setPublicKey1(privateKey.getPublicKey1());
        this.setPublicKey2(privateKey.getPublicKey2());
    }

    private void setPublicKey1(String publicKeyStr) {

        BigInteger keyInt = new BigInteger(Base64.decode(publicKeyStr.getBytes(),Base64.DEFAULT));
        BigInteger exponentInt = new BigInteger(Base64.decode(publicKeyStr.getBytes(),Base64.DEFAULT));

        RSAPublicKeySpec keySpeck = new RSAPublicKeySpec(keyInt, exponentInt);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] byteKey = Base64.decode(publicKeyStr.getBytes(), Base64.DEFAULT);
            X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(byteKey);
           // publicKey1 = keyFactory.generatePublic(new X509EncodedKeySpec(Base64.decode(publicKeyStr.getBytes("utf-8"),Base64.DEFAULT)));
            publicKey1 = keyFactory.generatePublic(X509publicKey);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        //} catch (UnsupportedEncodingException e) {
          //  e.printStackTrace();
        }
    }

    private void setPublicKey2(String publicKeyStr) {

        BigInteger keyInt = new BigInteger(Base64.decode(publicKeyStr.getBytes(),Base64.DEFAULT));
        BigInteger exponentInt = new BigInteger(Base64.decode(publicKeyStr.getBytes(),Base64.DEFAULT));

        RSAPublicKeySpec keySpeck = new RSAPublicKeySpec(keyInt, exponentInt);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] byteKey = Base64.decode(publicKeyStr.getBytes(), Base64.DEFAULT);
            X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(byteKey);
            //publickey2 = keyFactory.generatePublic(new X509EncodedKeySpec(Base64.decode(publicKeyStr.getBytes("utf-8"),Base64.DEFAULT)));
            publickey2 = keyFactory.generatePublic(X509publicKey);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        //} catch (UnsupportedEncodingException e) {
          //  e.printStackTrace();
        }
    }

    private void setPrivateKey(String privateKeyStr) {
        //BigInteger keyInt = new BigInteger(privateKeyStr,16); // hex base
        //BigInteger exponentInt = new BigInteger(privateKeyStr,10); // decimal base
        BigInteger keyInt = new BigInteger(Base64.decode(privateKeyStr.getBytes(),Base64.DEFAULT));
        BigInteger exponentInt = new BigInteger(Base64.decode(privateKeyStr.getBytes(),Base64.DEFAULT));

        RSAPrivateKeySpec keySpeck = new RSAPrivateKeySpec(keyInt, exponentInt);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] byteKey = Base64.decode(privateKeyStr.getBytes(), Base64.DEFAULT);
            PKCS8EncodedKeySpec PKCS8PrivateKey = new PKCS8EncodedKeySpec(byteKey);
            //privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Base64.decode(privateKeyStr.getBytes("utf-8"),Base64.DEFAULT)));
            privateKey = keyFactory.generatePrivate(PKCS8PrivateKey);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
       // } catch (UnsupportedEncodingException e) {
         //   e.printStackTrace();
        }
    }


    Cipher rsa=Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");


    public String encrypt(String message) {
        byte[] encBytes = Base64.decode(message.getBytes(),Base64.DEFAULT);
        try {
            rsa.init(Cipher.ENCRYPT_MODE,privateKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        try {
            //message = rsa.doFinal(message.getBytes()).toString();
            encBytes = rsa.doFinal(encBytes);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        try {
            rsa.init(Cipher.ENCRYPT_MODE,publickey2);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        try {
            //message = rsa.doFinal(message.getBytes()).toString();
            encBytes = rsa.doFinal(encBytes);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        message = Base64.encodeToString(encBytes,Base64.DEFAULT);
        //message = encBytes.toString();
        return message;
    }

    public String decrypt1(String message) {
        byte[] decByte = Base64.decode(message.getBytes(),Base64.DEFAULT);
        try {
            rsa.init(Cipher.DECRYPT_MODE,privateKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        try {
            decByte = rsa.doFinal(decByte);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        try {
            rsa.init(Cipher.DECRYPT_MODE,publickey2);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        try {
            decByte = rsa.doFinal(decByte);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        message = Base64.encodeToString(decByte,Base64.DEFAULT);
        return message;
    }

    public String decrypt2(String message) {
        byte[] decByte = Base64.decode(message.getBytes(),Base64.DEFAULT);
        try {
            rsa.init(Cipher.DECRYPT_MODE,publicKey1);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        try {
            decByte = rsa.doFinal(decByte);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        try {
            rsa.init(Cipher.DECRYPT_MODE,publickey2);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        try {
            decByte = rsa.doFinal(decByte);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        message = Base64.encodeToString(decByte,Base64.DEFAULT);
        return message;
    }

    public RSA() throws NoSuchPaddingException, NoSuchAlgorithmException {
    }
}
