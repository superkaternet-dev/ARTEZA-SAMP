/*
 * Decompiled with CFR 0.152.
 */
package com.downloader;

import java.io.Serializable;

public class Progress
implements Serializable {
    public long currentBytes;
    public long totalBytes;

    public Progress(long l, long l2) {
        this.currentBytes = l;
        this.totalBytes = l2;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Progress{currentBytes=");
        stringBuilder.append(this.currentBytes);
        stringBuilder.append(", totalBytes=");
        stringBuilder.append(this.totalBytes);
        stringBuilder.append('}');
        return stringBuilder.toString();
    }
}

