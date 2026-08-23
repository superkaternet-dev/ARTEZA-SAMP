/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 *  android.util.Log
 */
package com.bumptech.glide.util;

import android.text.TextUtils;
import android.util.Log;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class ContentLengthInputStream
extends FilterInputStream {
    private static final String TAG = "ContentLengthStream";
    private static final int UNKNOWN = -1;
    private final long contentLength;
    private int readSoFar;

    private ContentLengthInputStream(InputStream inputStream, long l) {
        super(inputStream);
        this.contentLength = l;
    }

    private int checkReadSoFarOrThrow(int n) throws IOException {
        block4: {
            block3: {
                block2: {
                    if (n < 0) break block2;
                    this.readSoFar += n;
                    break block3;
                }
                if (this.contentLength - (long)this.readSoFar > 0L) break block4;
            }
            return n;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Failed to read all expected data, expected: ");
        stringBuilder.append(this.contentLength);
        stringBuilder.append(", but read: ");
        stringBuilder.append(this.readSoFar);
        throw new IOException(stringBuilder.toString());
    }

    public static InputStream obtain(InputStream inputStream, long l) {
        return new ContentLengthInputStream(inputStream, l);
    }

    public static InputStream obtain(InputStream inputStream, String string2) {
        return ContentLengthInputStream.obtain(inputStream, ContentLengthInputStream.parseContentLength(string2));
    }

    private static int parseContentLength(String string2) {
        int n;
        block3: {
            int n2;
            n = n2 = -1;
            if (!TextUtils.isEmpty((CharSequence)string2)) {
                try {
                    n = Integer.parseInt(string2);
                }
                catch (NumberFormatException numberFormatException) {
                    n = n2;
                    if (!Log.isLoggable((String)TAG, (int)3)) break block3;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("failed to parse content length header: ");
                    stringBuilder.append(string2);
                    Log.d((String)TAG, (String)stringBuilder.toString(), (Throwable)numberFormatException);
                    n = n2;
                }
            }
        }
        return n;
    }

    @Override
    public int available() throws IOException {
        synchronized (this) {
            long l = Math.max(this.contentLength - (long)this.readSoFar, (long)this.in.available());
            int n = (int)l;
            return n;
        }
    }

    @Override
    public int read() throws IOException {
        synchronized (this) {
            int n = super.read();
            int n2 = n >= 0 ? 1 : -1;
            this.checkReadSoFarOrThrow(n2);
            return n;
        }
    }

    @Override
    public int read(byte[] byArray) throws IOException {
        return this.read(byArray, 0, byArray.length);
    }

    @Override
    public int read(byte[] byArray, int n, int n2) throws IOException {
        synchronized (this) {
            n = this.checkReadSoFarOrThrow(super.read(byArray, n, n2));
            return n;
        }
    }
}

