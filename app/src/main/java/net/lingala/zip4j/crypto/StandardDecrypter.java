/*
 * Decompiled with CFR 0.152.
 */
package net.lingala.zip4j.crypto;

import net.lingala.zip4j.crypto.IDecrypter;
import net.lingala.zip4j.crypto.engine.ZipCryptoEngine;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

public class StandardDecrypter
implements IDecrypter {
    private byte[] crc = new byte[4];
    private FileHeader fileHeader;
    private ZipCryptoEngine zipCryptoEngine;

    public StandardDecrypter(FileHeader fileHeader, byte[] byArray) throws ZipException {
        if (fileHeader != null) {
            this.fileHeader = fileHeader;
            this.zipCryptoEngine = new ZipCryptoEngine();
            this.init(byArray);
            return;
        }
        throw new ZipException("one of more of the input parameters were null in StandardDecryptor");
    }

    @Override
    public int decryptData(byte[] byArray) throws ZipException {
        return this.decryptData(byArray, 0, byArray.length);
    }

    @Override
    public int decryptData(byte[] object, int n, int n2) throws ZipException {
        if (n >= 0 && n2 >= 0) {
            for (int i = n; i < n + n2; ++i) {
                int n3 = object[i];
                try {
                    n3 = (this.zipCryptoEngine.decryptByte() ^ n3 & 0xFF) & 0xFF;
                    this.zipCryptoEngine.updateKeys((byte)n3);
                }
                catch (Exception exception) {
                    throw new ZipException(exception);
                }
                object[i] = (byte)n3;
            }
            return n2;
        }
        object = new ZipException("one of the input parameters were null in standard decrpyt data");
        throw object;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void init(byte[] object) throws ZipException {
        Object object2 = this.fileHeader.getCrcBuff();
        byte[] byArray = this.crc;
        byArray[3] = (byte)(object2[3] & 0xFF);
        byArray[2] = (byte)(object2[3] >> 8 & 0xFF);
        byArray[1] = (byte)(object2[3] >> 16 & 0xFF);
        byArray[0] = (byte)(object2[3] >> 24 & 0xFF);
        if (byArray[2] <= 0 && byArray[1] <= 0 && byArray[0] <= 0) {
            if (this.fileHeader.getPassword() == null) throw new ZipException("Wrong password!", 5);
            if (this.fileHeader.getPassword().length <= 0) throw new ZipException("Wrong password!", 5);
            this.zipCryptoEngine.initKeys(this.fileHeader.getPassword());
            Object by = object[0];
            int i = 0;
            while (i < 12) {
                block4: {
                    try {
                        object2 = this.zipCryptoEngine;
                        ((ZipCryptoEngine)object2).updateKeys((byte)(((ZipCryptoEngine)object2).decryptByte() ^ by));
                        if (i + 1 == 12) break block4;
                    }
                    catch (Exception exception) {
                        throw new ZipException(exception);
                    }
                    by = object[i + 1];
                }
                ++i;
            }
            return;
        }
        object = new IllegalStateException("Invalid CRC in File Header");
        throw object;
    }
}

