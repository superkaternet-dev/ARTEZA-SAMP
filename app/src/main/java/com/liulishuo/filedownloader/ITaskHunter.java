/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.filedownloader;

import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.IDownloadSpeed;
import com.liulishuo.filedownloader.IFileDownloadMessenger;
import com.liulishuo.filedownloader.message.MessageSnapshot;

public interface ITaskHunter
extends IDownloadSpeed.Lookup {
    public void free();

    public Throwable getErrorCause();

    public String getEtag();

    public int getRetryingTimes();

    public long getSofarBytes();

    public byte getStatus();

    public long getTotalBytes();

    public void intoLaunchPool();

    public boolean isLargeFile();

    public boolean isResuming();

    public boolean isReusedOldFile();

    public boolean pause();

    public void reset();

    public static interface IMessageHandler {
        public IFileDownloadMessenger getMessenger();

        public MessageSnapshot prepareErrorMessage(Throwable var1);

        public boolean updateKeepAhead(MessageSnapshot var1);

        public boolean updateKeepFlow(MessageSnapshot var1);

        public boolean updateMoreLikelyCompleted(MessageSnapshot var1);

        public boolean updateSameFilePathTaskRunning(MessageSnapshot var1);
    }

    public static interface IStarter {
        public boolean equalListener(FileDownloadListener var1);

        public void start();
    }
}

