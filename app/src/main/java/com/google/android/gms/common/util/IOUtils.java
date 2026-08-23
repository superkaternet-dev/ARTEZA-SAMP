/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.ParcelFileDescriptor
 *  javax.annotation.Nullable
 */
package com.google.android.gms.common.util;

import android.os.ParcelFileDescriptor;
import com.google.android.gms.common.internal.Preconditions;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.annotation.Nullable;

@Deprecated
public final class IOUtils {
    private IOUtils() {
    }

    public static void closeQuietly(@Nullable ParcelFileDescriptor parcelFileDescriptor) {
        if (parcelFileDescriptor != null) {
            try {
                parcelFileDescriptor.close();
                return;
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
    }

    public static void closeQuietly(@Nullable Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
                return;
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
    }

    @Deprecated
    public static long copyStream(InputStream inputStream, OutputStream outputStream) throws IOException {
        return IOUtils.copyStream(inputStream, outputStream, false, 1024);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Deprecated
    public static long copyStream(InputStream inputStream, OutputStream outputStream, boolean bl, int n) throws IOException {
        byte[] byArray = new byte[n];
        long l = 0L;
        try {
            int n2;
            while ((n2 = inputStream.read(byArray, 0, n)) != -1) {
                l += (long)n2;
                outputStream.write(byArray, 0, n2);
            }
            if (!bl) return l;
        }
        catch (Throwable throwable) {
            if (!bl) {
                throw throwable;
            }
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
            throw throwable;
        }
        IOUtils.closeQuietly(inputStream);
        IOUtils.closeQuietly(outputStream);
        return l;
    }

    public static boolean isGzipByteBuffer(byte[] byArray) {
        byte by;
        return byArray.length > 1 && ((byArray[1] & 0xFF) << 8 | (by = byArray[0]) & 0xFF) == 35615;
    }

    @Deprecated
    public static byte[] readInputStreamFully(InputStream inputStream) throws IOException {
        return IOUtils.readInputStreamFully(inputStream, true);
    }

    @Deprecated
    public static byte[] readInputStreamFully(InputStream inputStream, boolean bl) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        IOUtils.copyStream(inputStream, byteArrayOutputStream, bl, 1024);
        return byteArrayOutputStream.toByteArray();
    }

    @Deprecated
    public static byte[] toByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Preconditions.checkNotNull(inputStream);
        Preconditions.checkNotNull(byteArrayOutputStream);
        byte[] byArray = new byte[4096];
        int n;
        while ((n = inputStream.read(byArray)) != -1) {
            ((OutputStream)byteArrayOutputStream).write(byArray, 0, n);
        }
        return byteArrayOutputStream.toByteArray();
    }
}

