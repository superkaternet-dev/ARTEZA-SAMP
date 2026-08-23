/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.filedownloader;

import com.liulishuo.filedownloader.model.FileDownloadModel;

public interface IThreadPoolMonitor {
    public int findRunningTaskIdBySameTempPath(String var1, int var2);

    public boolean isDownloading(FileDownloadModel var1);
}

