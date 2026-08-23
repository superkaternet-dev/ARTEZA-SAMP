/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.filedownloader;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.message.MessageSnapshot;

interface IFileDownloadMessenger {
    public void discard();

    public boolean handoverDirectly();

    public void handoverMessage();

    public boolean isBlockingCompleted();

    public boolean notifyBegin();

    public void notifyBlockComplete(MessageSnapshot var1);

    public void notifyCompleted(MessageSnapshot var1);

    public void notifyConnected(MessageSnapshot var1);

    public void notifyError(MessageSnapshot var1);

    public void notifyPaused(MessageSnapshot var1);

    public void notifyPending(MessageSnapshot var1);

    public void notifyProgress(MessageSnapshot var1);

    public void notifyRetry(MessageSnapshot var1);

    public void notifyStarted(MessageSnapshot var1);

    public void notifyWarn(MessageSnapshot var1);

    public void reAppointment(BaseDownloadTask.IRunningTask var1, BaseDownloadTask.LifeCycleCallback var2);
}

