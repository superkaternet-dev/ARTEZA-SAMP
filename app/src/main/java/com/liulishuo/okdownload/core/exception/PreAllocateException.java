/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.okdownload.core.exception;

import java.io.IOException;

public class PreAllocateException
extends IOException {
    private final long freeSpace;
    private final long requireSpace;

    public PreAllocateException(long l, long l2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("There is Free space less than Require space: ");
        stringBuilder.append(l2);
        stringBuilder.append(" < ");
        stringBuilder.append(l);
        super(stringBuilder.toString());
        this.requireSpace = l;
        this.freeSpace = l2;
    }

    public long getFreeSpace() {
        return this.freeSpace;
    }

    public long getRequireSpace() {
        return this.requireSpace;
    }
}

