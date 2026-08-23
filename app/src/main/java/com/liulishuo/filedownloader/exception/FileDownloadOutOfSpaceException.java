/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.filedownloader.exception;

import com.liulishuo.filedownloader.util.FileDownloadUtils;
import java.io.IOException;

public class FileDownloadOutOfSpaceException
extends IOException {
    private long breakpointBytes;
    private long freeSpaceBytes;
    private long requiredSpaceBytes;

    public FileDownloadOutOfSpaceException(long l, long l2, long l3) {
        super(FileDownloadUtils.formatString("The file is too large to store, breakpoint in bytes:  %d, required space in bytes: %d, but free space in bytes: %d", l3, l2, l));
        this.init(l, l2, l3);
    }

    public FileDownloadOutOfSpaceException(long l, long l2, long l3, Throwable throwable) {
        super(FileDownloadUtils.formatString("The file is too large to store, breakpoint in bytes:  %d, required space in bytes: %d, but free space in bytes: %d", l3, l2, l), throwable);
        this.init(l, l2, l3);
    }

    private void init(long l, long l2, long l3) {
        this.freeSpaceBytes = l;
        this.requiredSpaceBytes = l2;
        this.breakpointBytes = l3;
    }

    public long getBreakpointBytes() {
        return this.breakpointBytes;
    }

    public long getFreeSpaceBytes() {
        return this.freeSpaceBytes;
    }

    public long getRequiredSpaceBytes() {
        return this.requiredSpaceBytes;
    }
}

