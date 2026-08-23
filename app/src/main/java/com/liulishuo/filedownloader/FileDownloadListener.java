/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.filedownloader;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.util.FileDownloadLog;

public abstract class FileDownloadListener {
    public FileDownloadListener() {
    }

    public FileDownloadListener(int n) {
        FileDownloadLog.w(this, "not handle priority any more", new Object[0]);
    }

    protected void blockComplete(BaseDownloadTask baseDownloadTask) throws Throwable {
    }

    protected abstract void completed(BaseDownloadTask var1);

    protected void connected(BaseDownloadTask baseDownloadTask, String string2, boolean bl, int n, int n2) {
    }

    protected abstract void error(BaseDownloadTask var1, Throwable var2);

    protected boolean isInvalid() {
        return false;
    }

    protected abstract void paused(BaseDownloadTask var1, int var2, int var3);

    protected abstract void pending(BaseDownloadTask var1, int var2, int var3);

    protected abstract void progress(BaseDownloadTask var1, int var2, int var3);

    protected void retry(BaseDownloadTask baseDownloadTask, Throwable throwable, int n, int n2) {
    }

    protected void started(BaseDownloadTask baseDownloadTask) {
    }

    protected abstract void warn(BaseDownloadTask var1);
}

