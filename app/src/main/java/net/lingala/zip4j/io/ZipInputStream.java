/*
 * Decompiled with CFR 0.152.
 */
package net.lingala.zip4j.io;

import java.io.IOException;
import java.io.InputStream;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.BaseInputStream;

public class ZipInputStream
extends InputStream {
    private BaseInputStream is;

    public ZipInputStream(BaseInputStream baseInputStream) {
        this.is = baseInputStream;
    }

    @Override
    public int available() throws IOException {
        return this.is.available();
    }

    @Override
    public void close() throws IOException {
        this.close(false);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void close(boolean bl) throws IOException {
        try {
            this.is.close();
            if (bl) return;
        }
        catch (ZipException zipException) {
            throw new IOException(zipException.getMessage());
        }
        if (this.is.getUnzipEngine() == null) return;
        this.is.getUnzipEngine().checkCRC();
    }

    @Override
    public int read() throws IOException {
        int n = this.is.read();
        if (n != -1) {
            this.is.getUnzipEngine().updateCRC(n);
        }
        return n;
    }

    @Override
    public int read(byte[] byArray) throws IOException {
        return this.read(byArray, 0, byArray.length);
    }

    @Override
    public int read(byte[] byArray, int n, int n2) throws IOException {
        if ((n2 = this.is.read(byArray, n, n2)) > 0 && this.is.getUnzipEngine() != null) {
            this.is.getUnzipEngine().updateCRC(byArray, n, n2);
        }
        return n2;
    }

    @Override
    public long skip(long l) throws IOException {
        return this.is.skip(l);
    }
}

