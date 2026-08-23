/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.okdownload.core.exception;

import java.io.IOException;

public class ServerCanceledException
extends IOException {
    private final int responseCode;

    public ServerCanceledException(int n, long l) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Response code can't handled on internal ");
        stringBuilder.append(n);
        stringBuilder.append(" with current offset ");
        stringBuilder.append(l);
        super(stringBuilder.toString());
        this.responseCode = n;
    }

    public int getResponseCode() {
        return this.responseCode;
    }
}

