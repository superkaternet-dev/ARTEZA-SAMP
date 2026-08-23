/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.load;

import java.io.IOException;

public final class HttpException
extends IOException {
    public static final int UNKNOWN = -1;
    private static final long serialVersionUID = 1L;
    private final int statusCode;

    public HttpException(int n) {
        this("Http request failed", n);
    }

    @Deprecated
    public HttpException(String string2) {
        this(string2, -1);
    }

    public HttpException(String string2, int n) {
        this(string2, n, null);
    }

    public HttpException(String string2, int n, Throwable throwable) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(string2);
        stringBuilder.append(", status code: ");
        stringBuilder.append(n);
        super(stringBuilder.toString(), throwable);
        this.statusCode = n;
    }

    public int getStatusCode() {
        return this.statusCode;
    }
}

