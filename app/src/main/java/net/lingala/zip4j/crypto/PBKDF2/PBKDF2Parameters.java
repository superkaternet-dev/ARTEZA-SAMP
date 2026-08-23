/*
 * Decompiled with CFR 0.152.
 */
package net.lingala.zip4j.crypto.PBKDF2;

public class PBKDF2Parameters {
    protected byte[] derivedKey;
    protected String hashAlgorithm;
    protected String hashCharset;
    protected int iterationCount;
    protected byte[] salt;

    public PBKDF2Parameters() {
        this.hashAlgorithm = null;
        this.hashCharset = "UTF-8";
        this.salt = null;
        this.iterationCount = 1000;
        this.derivedKey = null;
    }

    public PBKDF2Parameters(String string2, String string3, byte[] byArray, int n) {
        this.hashAlgorithm = string2;
        this.hashCharset = string3;
        this.salt = byArray;
        this.iterationCount = n;
        this.derivedKey = null;
    }

    public PBKDF2Parameters(String string2, String string3, byte[] byArray, int n, byte[] byArray2) {
        this.hashAlgorithm = string2;
        this.hashCharset = string3;
        this.salt = byArray;
        this.iterationCount = n;
        this.derivedKey = byArray2;
    }

    public byte[] getDerivedKey() {
        return this.derivedKey;
    }

    public String getHashAlgorithm() {
        return this.hashAlgorithm;
    }

    public String getHashCharset() {
        return this.hashCharset;
    }

    public int getIterationCount() {
        return this.iterationCount;
    }

    public byte[] getSalt() {
        return this.salt;
    }

    public void setDerivedKey(byte[] byArray) {
        this.derivedKey = byArray;
    }

    public void setHashAlgorithm(String string2) {
        this.hashAlgorithm = string2;
    }

    public void setHashCharset(String string2) {
        this.hashCharset = string2;
    }

    public void setIterationCount(int n) {
        this.iterationCount = n;
    }

    public void setSalt(byte[] byArray) {
        this.salt = byArray;
    }
}

