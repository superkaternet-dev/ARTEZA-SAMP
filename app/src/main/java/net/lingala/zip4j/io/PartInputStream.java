/*
 * Decompiled with CFR 0.152.
 */
package net.lingala.zip4j.io;

import java.io.IOException;
import java.io.RandomAccessFile;
import net.lingala.zip4j.crypto.AESDecrypter;
import net.lingala.zip4j.crypto.IDecrypter;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.BaseInputStream;
import net.lingala.zip4j.unzip.UnzipEngine;

public class PartInputStream
extends BaseInputStream {
    private byte[] aesBlockByte;
    private int aesBytesReturned;
    private long bytesRead;
    private int count;
    private IDecrypter decrypter;
    private boolean isAESEncryptedFile;
    private long length;
    private byte[] oneByteBuff;
    private RandomAccessFile raf;
    private UnzipEngine unzipEngine;

    public PartInputStream(RandomAccessFile randomAccessFile, long l, long l2, UnzipEngine unzipEngine) {
        boolean bl = true;
        this.oneByteBuff = new byte[1];
        this.aesBlockByte = new byte[16];
        this.aesBytesReturned = 0;
        this.isAESEncryptedFile = false;
        this.count = -1;
        this.raf = randomAccessFile;
        this.unzipEngine = unzipEngine;
        this.decrypter = unzipEngine.getDecrypter();
        this.bytesRead = 0L;
        this.length = l2;
        if (!unzipEngine.getFileHeader().isEncrypted() || unzipEngine.getFileHeader().getEncryptionMethod() != 99) {
            bl = false;
        }
        this.isAESEncryptedFile = bl;
    }

    @Override
    public int available() {
        long l = this.length - this.bytesRead;
        if (l > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        return (int)l;
    }

    protected void checkAndReadAESMacBytes() throws IOException {
        Object object;
        if (this.isAESEncryptedFile && (object = this.decrypter) != null && object instanceof AESDecrypter) {
            if (((AESDecrypter)object).getStoredMac() != null) {
                return;
            }
            object = new byte[10];
            int n = this.raf.read((byte[])object);
            if (n != 10) {
                if (this.unzipEngine.getZipModel().isSplitArchive()) {
                    RandomAccessFile randomAccessFile;
                    this.raf.close();
                    this.raf = randomAccessFile = this.unzipEngine.startNextSplitFile();
                    randomAccessFile.read((byte[])object, n, 10 - n);
                } else {
                    throw new IOException("Error occured while reading stored AES authentication bytes");
                }
            }
            ((AESDecrypter)this.unzipEngine.getDecrypter()).setStoredMac((byte[])object);
        }
    }

    @Override
    public void close() throws IOException {
        this.raf.close();
    }

    @Override
    public UnzipEngine getUnzipEngine() {
        return this.unzipEngine;
    }

    @Override
    public int read() throws IOException {
        long l = this.bytesRead;
        long l2 = this.length;
        int n = -1;
        if (l >= l2) {
            return -1;
        }
        if (this.isAESEncryptedFile) {
            n = this.aesBytesReturned;
            if (n == 0 || n == 16) {
                if (this.read(this.aesBlockByte) == -1) {
                    return -1;
                }
                this.aesBytesReturned = 0;
            }
            byte[] byArray = this.aesBlockByte;
            n = this.aesBytesReturned;
            this.aesBytesReturned = n + 1;
            return byArray[n] & 0xFF;
        }
        if (this.read(this.oneByteBuff, 0, 1) != -1) {
            n = this.oneByteBuff[0] & 0xFF;
        }
        return n;
    }

    @Override
    public int read(byte[] byArray) throws IOException {
        return this.read(byArray, 0, byArray.length);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public int read(byte[] byArray, int n, int n2) throws IOException {
        int n3;
        long l = n2;
        long l2 = this.length;
        long l3 = this.bytesRead;
        if (l > l2 - l3) {
            n2 = n3 = (int)(l2 - l3);
            if (n3 == 0) {
                this.checkAndReadAESMacBytes();
                return -1;
            }
        }
        n3 = n2;
        if (this.unzipEngine.getDecrypter() instanceof AESDecrypter) {
            n3 = n2;
            if (this.bytesRead + (long)n2 < this.length) {
                n3 = n2;
                if (n2 % 16 != 0) {
                    n3 = n2 - n2 % 16;
                }
            }
        }
        Object object = this.raf;
        synchronized (object) {
            this.count = n2 = this.raf.read(byArray, n, n3);
            if (n2 < n3 && this.unzipEngine.getZipModel().isSplitArchive()) {
                RandomAccessFile randomAccessFile;
                this.raf.close();
                this.raf = randomAccessFile = this.unzipEngine.startNextSplitFile();
                if (this.count < 0) {
                    this.count = 0;
                }
                n2 = this.count;
                if ((n2 = randomAccessFile.read(byArray, n2, n3 - n2)) > 0) {
                    this.count += n2;
                }
            }
        }
        n2 = this.count;
        if (n2 > 0) {
            object = this.decrypter;
            if (object != null) {
                try {
                    object.decryptData(byArray, n, n2);
                }
                catch (ZipException zipException) {
                    throw new IOException(zipException.getMessage());
                }
            }
            this.bytesRead += (long)this.count;
        }
        if (this.bytesRead >= this.length) {
            this.checkAndReadAESMacBytes();
        }
        return this.count;
    }

    @Override
    public void seek(long l) throws IOException {
        this.raf.seek(l);
    }

    @Override
    public long skip(long l) throws IOException {
        if (l >= 0L) {
            long l2 = this.length;
            long l3 = this.bytesRead;
            long l4 = l;
            if (l > l2 - l3) {
                l4 = l2 - l3;
            }
            this.bytesRead = l3 + l4;
            return l4;
        }
        throw new IllegalArgumentException();
    }
}

