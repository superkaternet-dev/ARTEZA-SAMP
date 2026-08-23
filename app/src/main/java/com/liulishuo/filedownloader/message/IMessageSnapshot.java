/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.filedownloader.message;

interface IMessageSnapshot {
    public String getEtag();

    public String getFileName();

    public int getId();

    public long getLargeSofarBytes();

    public long getLargeTotalBytes();

    public int getRetryingTimes();

    public int getSmallSofarBytes();

    public int getSmallTotalBytes();

    public byte getStatus();

    public Throwable getThrowable();

    public boolean isLargeFile();

    public boolean isResuming();

    public boolean isReusedDownloadedFile();
}

