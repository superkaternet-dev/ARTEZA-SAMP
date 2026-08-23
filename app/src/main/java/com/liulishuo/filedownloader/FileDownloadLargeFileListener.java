/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.filedownloader;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;

public abstract class FileDownloadLargeFileListener
extends FileDownloadListener {
    public FileDownloadLargeFileListener() {
    }

    public FileDownloadLargeFileListener(int n) {
        super(n);
    }

    @Override
    protected void connected(BaseDownloadTask baseDownloadTask, String string2, boolean bl, int n, int n2) {
    }

    protected void connected(BaseDownloadTask baseDownloadTask, String string2, boolean bl, long l, long l2) {
    }

    @Override
    protected void paused(BaseDownloadTask baseDownloadTask, int n, int n2) {
    }

    protected abstract void paused(BaseDownloadTask var1, long var2, long var4);

    @Override
    protected void pending(BaseDownloadTask baseDownloadTask, int n, int n2) {
    }

    protected abstract void pending(BaseDownloadTask var1, long var2, long var4);

    @Override
    protected void progress(BaseDownloadTask baseDownloadTask, int n, int n2) {
    }

    protected abstract void progress(BaseDownloadTask var1, long var2, long var4);

    @Override
    protected void retry(BaseDownloadTask baseDownloadTask, Throwable throwable, int n, int n2) {
    }

    protected void retry(BaseDownloadTask baseDownloadTask, Throwable throwable, int n, long l) {
    }
}

