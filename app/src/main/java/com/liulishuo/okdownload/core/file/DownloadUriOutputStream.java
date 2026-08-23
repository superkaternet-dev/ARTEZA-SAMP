/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.net.Uri
 *  android.os.Build$VERSION
 *  android.os.ParcelFileDescriptor
 *  android.system.Os
 */
package com.liulishuo.okdownload.core.file;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.system.Os;
import com.liulishuo.okdownload.core.Util;
import com.liulishuo.okdownload.core.file.DownloadOutputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

public class DownloadUriOutputStream
implements DownloadOutputStream {
    private final FileChannel channel;
    final FileOutputStream fos;
    final BufferedOutputStream out;
    final ParcelFileDescriptor pdf;

    public DownloadUriOutputStream(Context object, Uri uri, int n) throws FileNotFoundException {
        object = object.getContentResolver().openFileDescriptor(uri, "rw");
        if (object != null) {
            this.pdf = object;
            this.fos = object = new FileOutputStream(object.getFileDescriptor());
            this.channel = ((FileOutputStream)object).getChannel();
            this.out = new BufferedOutputStream((OutputStream)object, n);
            return;
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("result of ");
        ((StringBuilder)object).append(uri);
        ((StringBuilder)object).append(" is null!");
        throw new FileNotFoundException(((StringBuilder)object).toString());
    }

    DownloadUriOutputStream(FileChannel fileChannel, ParcelFileDescriptor parcelFileDescriptor, FileOutputStream fileOutputStream, BufferedOutputStream bufferedOutputStream) {
        this.channel = fileChannel;
        this.pdf = parcelFileDescriptor;
        this.fos = fileOutputStream;
        this.out = bufferedOutputStream;
    }

    @Override
    public void close() throws IOException {
        this.out.close();
        this.fos.close();
    }

    @Override
    public void flushAndSync() throws IOException {
        this.out.flush();
        this.pdf.getFileDescriptor().sync();
    }

    @Override
    public void seek(long l) throws IOException {
        this.channel.position(l);
    }

    @Override
    public void setLength(long l) {
        if (Build.VERSION.SDK_INT >= 21) {
            try {
                Os.ftruncate((FileDescriptor)this.pdf.getFileDescriptor(), (long)l);
            }
            catch (Throwable throwable) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("It can't pre-allocate length(");
                stringBuilder.append(l);
                stringBuilder.append(") on the sdk");
                stringBuilder.append(" version(");
                stringBuilder.append(Build.VERSION.SDK_INT);
                stringBuilder.append("), because of ");
                stringBuilder.append(throwable);
                Util.w("DownloadUriOutputStream", stringBuilder.toString());
            }
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("It can't pre-allocate length(");
            stringBuilder.append(l);
            stringBuilder.append(") on the sdk ");
            stringBuilder.append("version(");
            stringBuilder.append(Build.VERSION.SDK_INT);
            stringBuilder.append(")");
            Util.w("DownloadUriOutputStream", stringBuilder.toString());
        }
    }

    @Override
    public void write(byte[] byArray, int n, int n2) throws IOException {
        this.out.write(byArray, n, n2);
    }

    public static class Factory
    implements DownloadOutputStream.Factory {
        @Override
        public DownloadOutputStream create(Context context, Uri uri, int n) throws FileNotFoundException {
            return new DownloadUriOutputStream(context, uri, n);
        }

        @Override
        public DownloadOutputStream create(Context context, File file, int n) throws FileNotFoundException {
            return new DownloadUriOutputStream(context, Uri.fromFile((File)file), n);
        }

        @Override
        public boolean supportSeek() {
            return true;
        }
    }
}

