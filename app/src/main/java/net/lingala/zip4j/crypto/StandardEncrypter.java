/*
 * Decompiled with CFR 0.152.
 */
package net.lingala.zip4j.crypto;

import java.util.Random;
import net.lingala.zip4j.crypto.IEncrypter;
import net.lingala.zip4j.crypto.engine.ZipCryptoEngine;
import net.lingala.zip4j.exception.ZipException;

public class StandardEncrypter
implements IEncrypter {
    private byte[] headerBytes;
    private ZipCryptoEngine zipCryptoEngine;

    public StandardEncrypter(char[] cArray, int n) throws ZipException {
        if (cArray != null && cArray.length > 0) {
            this.zipCryptoEngine = new ZipCryptoEngine();
            this.headerBytes = new byte[12];
            this.init(cArray, n);
            return;
        }
        throw new ZipException("input password is null or empty in standard encrpyter constructor");
    }

    private void init(char[] objectArray, int n) throws ZipException {
        if (objectArray != null && objectArray.length > 0) {
            this.zipCryptoEngine.initKeys((char[])objectArray);
            this.headerBytes = this.generateRandomBytes(12);
            this.zipCryptoEngine.initKeys((char[])objectArray);
            objectArray = this.headerBytes;
            objectArray[11] = (char)(n >>> 24);
            objectArray[10] = (char)(n >>> 16);
            if (objectArray.length >= 12) {
                this.encryptData((byte[])objectArray);
                return;
            }
            throw new ZipException("invalid header bytes generated, cannot perform standard encryption");
        }
        throw new ZipException("input password is null or empty, cannot initialize standard encrypter");
    }

    protected byte encryptByte(byte by) {
        byte by2 = (byte)(this.zipCryptoEngine.decryptByte() & 0xFF ^ by);
        this.zipCryptoEngine.updateKeys(by);
        return by2;
    }

    @Override
    public int encryptData(byte[] byArray) throws ZipException {
        if (byArray != null) {
            return this.encryptData(byArray, 0, byArray.length);
        }
        throw new NullPointerException();
    }

    @Override
    public int encryptData(byte[] object, int n, int n2) throws ZipException {
        if (n2 >= 0) {
            for (int i = n; i < n + n2; ++i) {
                try {
                    object[i] = this.encryptByte(object[i]);
                    continue;
                }
                catch (Exception exception) {
                    throw new ZipException(exception);
                }
            }
            return n2;
        }
        object = new ZipException("invalid length specified to decrpyt data");
        throw object;
    }

    protected byte[] generateRandomBytes(int n) throws ZipException {
        if (n > 0) {
            byte[] byArray = new byte[n];
            Random random = new Random();
            for (n = 0; n < byArray.length; ++n) {
                byArray[n] = this.encryptByte((byte)random.nextInt(256));
            }
            return byArray;
        }
        ZipException zipException = new ZipException("size is either 0 or less than 0, cannot generate header for standard encryptor");
        throw zipException;
    }

    public byte[] getHeaderBytes() {
        return this.headerBytes;
    }
}

