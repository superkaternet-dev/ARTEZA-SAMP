/*
 * Decompiled with CFR 0.152.
 */
package com.downloader.internal.stream;

import java.io.IOException;

public interface FileDownloadOutputStream {
    public void close() throws IOException;

    public void flushAndSync() throws IOException;

    public void seek(long var1) throws IOException, IllegalAccessException;

    public void setLength(long var1) throws IOException, IllegalAccessException;

    public void write(byte[] var1, int var2, int var3) throws IOException;
}

