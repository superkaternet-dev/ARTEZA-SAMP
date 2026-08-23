/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.filedownloader.download;

import com.liulishuo.filedownloader.download.DownloadRunnable;

public interface ProcessCallback {
    public boolean isRetry(Exception var1);

    public void onCompleted(DownloadRunnable var1, long var2, long var4);

    public void onError(Exception var1);

    public void onProgress(long var1);

    public void onRetry(Exception var1);

    public void syncProgressFromCache();
}

