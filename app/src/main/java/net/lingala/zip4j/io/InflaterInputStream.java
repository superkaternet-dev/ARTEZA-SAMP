/*
 * Decompiled with CFR 0.152.
 */
package net.lingala.zip4j.io;

import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import net.lingala.zip4j.io.PartInputStream;
import net.lingala.zip4j.unzip.UnzipEngine;

public class InflaterInputStream
extends PartInputStream {
    private byte[] buff;
    private long bytesWritten;
    private Inflater inflater;
    private byte[] oneByteBuff = new byte[1];
    private long uncompressedSize;
    private UnzipEngine unzipEngine;

    public InflaterInputStream(RandomAccessFile randomAccessFile, long l, long l2, UnzipEngine unzipEngine) {
        super(randomAccessFile, l, l2, unzipEngine);
        this.inflater = new Inflater(true);
        this.buff = new byte[4096];
        this.unzipEngine = unzipEngine;
        this.bytesWritten = 0L;
        this.uncompressedSize = unzipEngine.getFileHeader().getUncompressedSize();
    }

    private void fill() throws IOException {
        byte[] byArray = this.buff;
        int n = super.read(byArray, 0, byArray.length);
        if (n != -1) {
            this.inflater.setInput(this.buff, 0, n);
            return;
        }
        throw new EOFException("Unexpected end of ZLIB input stream");
    }

    private void finishInflating() throws IOException {
        byte[] byArray = new byte[1024];
        while (super.read(byArray, 0, 1024) != -1) {
        }
        this.checkAndReadAESMacBytes();
    }

    @Override
    public int available() {
        return this.inflater.finished() ^ 1;
    }

    @Override
    public void close() throws IOException {
        this.inflater.end();
        super.close();
    }

    @Override
    public UnzipEngine getUnzipEngine() {
        return super.getUnzipEngine();
    }

    @Override
    public int read() throws IOException {
        int n = this.read(this.oneByteBuff, 0, 1);
        int n2 = -1;
        if (n != -1) {
            n2 = this.oneByteBuff[0] & 0xFF;
        }
        return n2;
    }

    @Override
    public int read(byte[] byArray) throws IOException {
        if (byArray != null) {
            return this.read(byArray, 0, byArray.length);
        }
        throw new NullPointerException("input buffer is null");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public int read(byte[] object, int n, int n2) throws IOException {
        if (object == null) {
            object = new NullPointerException("input buffer is null");
            throw object;
        }
        if (n < 0) throw new IndexOutOfBoundsException();
        if (n2 < 0) throw new IndexOutOfBoundsException();
        if (n2 > ((byte[])object).length - n) throw new IndexOutOfBoundsException();
        if (n2 == 0) {
            return 0;
        }
        try {
            if (this.bytesWritten >= this.uncompressedSize) {
                this.finishInflating();
                return -1;
            }
            while (true) {
                int n3;
                if ((n3 = this.inflater.inflate((byte[])object, n, n2)) != 0) {
                    this.bytesWritten += (long)n3;
                    return n3;
                }
                if (this.inflater.finished() || this.inflater.needsDictionary()) break;
                if (!this.inflater.needsInput()) continue;
                this.fill();
            }
            this.finishInflating();
            return -1;
        }
        catch (DataFormatException dataFormatException) {
            object = "Invalid ZLIB data format";
            if (dataFormatException.getMessage() != null) {
                object = dataFormatException.getMessage();
            }
            UnzipEngine unzipEngine = this.unzipEngine;
            Object object2 = object;
            if (unzipEngine == null) throw new IOException((String)object2);
            object2 = object;
            if (!unzipEngine.getLocalFileHeader().isEncrypted()) throw new IOException((String)object2);
            object2 = object;
            if (this.unzipEngine.getLocalFileHeader().getEncryptionMethod() != 0) throw new IOException((String)object2);
            object2 = new StringBuilder();
            ((StringBuilder)object2).append((String)object);
            ((StringBuilder)object2).append(" - Wrong Password?");
            object2 = ((StringBuilder)object2).toString();
            throw new IOException((String)object2);
        }
    }

    @Override
    public void seek(long l) throws IOException {
        super.seek(l);
    }

    @Override
    public long skip(long l) throws IOException {
        if (l >= 0L) {
            int n;
            int n2;
            int n3 = (int)Math.min(l, Integer.MAX_VALUE);
            byte[] byArray = new byte[512];
            for (n = 0; n < n3; n += n2) {
                int n4;
                n2 = n4 = n3 - n;
                if (n4 > byArray.length) {
                    n2 = byArray.length;
                }
                if ((n2 = this.read(byArray, 0, n2)) == -1) break;
            }
            return n;
        }
        IllegalArgumentException illegalArgumentException = new IllegalArgumentException("negative skip length");
        throw illegalArgumentException;
    }
}

