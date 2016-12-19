package com.example.myapplication;

import android.util.Base64;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

/**
 * Created by vinuth on 01/12/16.
 */

public class Signatures {

    public static String sign(RSA rsa, String message) {

        Signature sig = null;
        try {
            sig = Signature.getInstance("SHAwithDSA");
            sig.initSign((java.security.PrivateKey) rsa.privateKey);
            sig.update(message.getBytes());
            message = Base64.encodeToString(sig.sign(),Base64.DEFAULT);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        }
        return message;
    }

    static boolean verify(RSA rsa, String message) {
        Signature sig = null;
        try {
            sig = Signature.getInstance("SHAwithDSA");
            sig.initVerify((PublicKey) rsa.publickey2);
            sig.update(message.getBytes());
            return sig.verify(Base64.decode(message,Base64.DEFAULT));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return false;
    }
}

