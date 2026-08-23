/*
 * Decompiled with CFR 0.152.
 */
package net.lingala.zip4j.io;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.Deflater;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.CipherOutputStream;
import net.lingala.zip4j.model.ZipModel;
import net.lingala.zip4j.model.ZipParameters;

public class DeflaterOutputStream
extends CipherOutputStream {
    private byte[] buff;
    protected Deflater deflater = new Deflater();
    private boolean firstBytesRead = false;

    public DeflaterOutputStream(OutputStream outputStream, ZipModel zipModel) {
        super(outputStream, zipModel);
        this.buff = new byte[4096];
    }

    private void deflate() throws IOException {
        Deflater deflater = this.deflater;
        byte[] byArray = this.buff;
        int n = deflater.deflate(byArray, 0, byArray.length);
        if (n > 0) {
            int n2 = n;
            if (this.deflater.finished()) {
                if (n == 4) {
                    return;
                }
                if (n < 4) {
                    this.decrementCompressedFileSize(4 - n);
                    return;
                }
                n2 = n - 4;
            }
            if (!this.firstBytesRead) {
                super.write(this.buff, 2, n2 - 2);
                this.firstBytesRead = true;
            } else {
                super.write(this.buff, 0, n2);
            }
        }
    }

    @Override
    public void closeEntry() throws IOException, ZipException {
        if (this.zipParameters.getCompressionMethod() == 8) {
            if (!this.deflater.finished()) {
                this.deflater.finish();
                while (!this.deflater.finished()) {
                    this.deflate();
                }
            }
            this.firstBytesRead = false;
        }
        super.closeEntry();
    }

    @Override
    public void finish() throws IOException, ZipException {
        super.finish();
    }

    @Override
    public void putNextEntry(File file, ZipParameters zipParameters) throws ZipException {
        super.putNextEntry(file, zipParameters);
        if (zipParameters.getCompressionMethod() == 8) {
            this.deflater.reset();
            if (zipParameters.getCompressionLevel() >= 0 && zipParameters.getCompressionLevel() <= 9 || zipParameters.getCompressionLevel() == -1) {
                this.deflater.setLevel(zipParameters.getCompressionLevel());
            } else {
                throw new ZipException("invalid compression level for deflater. compression level should be in the range of 0-9");
            }
        }
    }

    @Override
    public void write(int n) throws IOException {
        this.write(new byte[]{(byte)n}, 0, 1);
    }

    @Override
    public void write(byte[] byArray) throws IOException {
        this.write(byArray, 0, byArray.length);
    }

    @Override
    public void write(byte[] byArray, int n, int n2) throws IOException {
        if (this.zipParameters.getCompressionMethod() != 8) {
            super.write(byArray, n, n2);
        } else {
            this.deflater.setInput(byArray, n, n2);
            while (!this.deflater.needsInput()) {
                this.deflate();
            }
        }
    }
}

