package com.example.myapplication;

import android.util.Base64;

import java.io.Serializable;

/**
 * Created by vinuth on 05/12/16.
 */

public class PrivateKey implements Serializable{
    String iv,salt,privateKey,publicKey1,publicKey2;
    byte[] privByte,pubByte1,pubByte2;

    public void setIv(String iv) {
        this.iv = iv;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
        this.privByte = Base64.decode(privateKey,Base64.DEFAULT);
    }

    public void setPublicKey1(String publicKey) {
        this.publicKey1 = publicKey;
        this.pubByte1 = Base64.decode(publicKey,Base64.DEFAULT);
    }

    public void setPublicKey2(String publicKey) {
        this.publicKey2 = publicKey;
        this.pubByte2 = Base64.decode(publicKey,Base64.DEFAULT);
    }

    public String getIv() {
        return iv;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public String getSalt() {
        return salt;
    }

    public String getPublicKey1() {
        return publicKey1;
    }

    public String getPublicKey2() {
        return publicKey2;
    }

    public byte[] getPrivByte() {
        return privByte;
    }

    public byte[] getPubByte1() {
        return pubByte1;
    }
    public byte[] getPubByte2() {
        return pubByte2;
    }
}
