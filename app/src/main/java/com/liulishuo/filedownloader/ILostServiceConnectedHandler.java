/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.filedownloader;

import com.liulishuo.filedownloader.BaseDownloadTask;

public interface ILostServiceConnectedHandler {
    public boolean dispatchTaskStart(BaseDownloadTask.IRunningTask var1);

    public boolean isInWaitingList(BaseDownloadTask.IRunningTask var1);

    public void taskWorkFine(BaseDownloadTask.IRunningTask var1);
}

