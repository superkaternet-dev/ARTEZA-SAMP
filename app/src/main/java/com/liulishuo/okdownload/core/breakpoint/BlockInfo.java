/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.okdownload.core.breakpoint;

import java.util.concurrent.atomic.AtomicLong;

public class BlockInfo {
    private final long contentLength;
    private final AtomicLong currentOffset;
    private final long startOffset;

    public BlockInfo(long l, long l2) {
        this(l, l2, 0L);
    }

    public BlockInfo(long l, long l2, long l3) {
        if (l >= 0L && (l2 >= 0L || l2 == -1L) && l3 >= 0L) {
            this.startOffset = l;
            this.contentLength = l2;
            this.currentOffset = new AtomicLong(l3);
            return;
        }
        throw new IllegalArgumentException();
    }

    public BlockInfo copy() {
        return new BlockInfo(this.startOffset, this.contentLength, this.currentOffset.get());
    }

    public long getContentLength() {
        return this.contentLength;
    }

    public long getCurrentOffset() {
        return this.currentOffset.get();
    }

    public long getRangeLeft() {
        return this.startOffset + this.currentOffset.get();
    }

    public long getRangeRight() {
        return this.startOffset + this.contentLength - 1L;
    }

    public long getStartOffset() {
        return this.startOffset;
    }

    public void increaseCurrentOffset(long l) {
        this.currentOffset.addAndGet(l);
    }

    public void resetBlock() {
        this.currentOffset.set(0L);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        stringBuilder.append(this.startOffset);
        stringBuilder.append(", ");
        stringBuilder.append(this.getRangeRight());
        stringBuilder.append(")");
        stringBuilder.append("-current:");
        stringBuilder.append(this.currentOffset);
        return stringBuilder.toString();
    }
}

