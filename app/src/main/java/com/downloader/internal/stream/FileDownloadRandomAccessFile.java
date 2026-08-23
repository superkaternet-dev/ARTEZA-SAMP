/*
 * Decompiled with CFR 0.152.
 */
package com.downloader.internal.stream;

import com.downloader.internal.stream.FileDownloadOutputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileDownloadRandomAccessFile
implements FileDownloadOutputStream {
    private final FileDescriptor fd;
    private final BufferedOutputStream out;
    private final RandomAccessFile randomAccess;

    private FileDownloadRandomAccessFile(File object) throws IOException {
        this.randomAccess = object = new RandomAccessFile((File)object, "rw");
        this.fd = ((RandomAccessFile)object).getFD();
        this.out = new BufferedOutputStream(new FileOutputStream(((RandomAccessFile)object).getFD()));
    }

    public static FileDownloadOutputStream create(File file) throws IOException {
        return new FileDownloadRandomAccessFile(file);
    }

    @Override
    public void close() throws IOException {
        this.out.close();
        this.randomAccess.close();
    }

    @Override
    public void flushAndSync() throws IOException {
        this.out.flush();
        this.fd.sync();
    }

    @Override
    public void seek(long l) throws IOException {
        this.randomAccess.seek(l);
    }

    @Override
    public void setLength(long l) throws IOException {
        this.randomAccess.setLength(l);
    }

    @Override
    public void write(byte[] byArray, int n, int n2) throws IOException {
        this.out.write(byArray, n, n2);
    }
}

