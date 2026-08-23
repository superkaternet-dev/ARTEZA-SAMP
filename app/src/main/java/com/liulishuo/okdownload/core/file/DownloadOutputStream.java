/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.net.Uri
 */
package com.liulishuo.okdownload.core.file;

import android.content.Context;
import android.net.Uri;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface DownloadOutputStream {
    public void close() throws IOException;

    public void flushAndSync() throws IOException;

    public void seek(long var1) throws IOException;

    public void setLength(long var1) throws IOException;

    public void write(byte[] var1, int var2, int var3) throws IOException;

    public static interface Factory {
        public DownloadOutputStream create(Context var1, Uri var2, int var3) throws FileNotFoundException;

        public DownloadOutputStream create(Context var1, File var2, int var3) throws FileNotFoundException;

        public boolean supportSeek();
    }
}

