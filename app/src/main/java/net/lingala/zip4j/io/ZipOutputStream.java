/*
 * Decompiled with CFR 0.152.
 */
package net.lingala.zip4j.io;

import java.io.IOException;
import java.io.OutputStream;
import net.lingala.zip4j.io.DeflaterOutputStream;
import net.lingala.zip4j.model.ZipModel;

public class ZipOutputStream
extends DeflaterOutputStream {
    public ZipOutputStream(OutputStream outputStream) {
        this(outputStream, null);
    }

    public ZipOutputStream(OutputStream outputStream, ZipModel zipModel) {
        super(outputStream, zipModel);
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
        this.crc.update(byArray, n, n2);
        this.updateTotalBytesRead(n2);
        super.write(byArray, n, n2);
    }
}

