/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.filedownloader;

import com.liulishuo.filedownloader.FileDownloadListener;
import java.util.List;

public interface IQueuesHandler {
    public boolean contain(int var1);

    public void freezeAllSerialQueues();

    public int serialQueueSize();

    public boolean startQueueParallel(FileDownloadListener var1);

    public boolean startQueueSerial(FileDownloadListener var1);

    public void unFreezeSerialQueues(List<Integer> var1);
}

