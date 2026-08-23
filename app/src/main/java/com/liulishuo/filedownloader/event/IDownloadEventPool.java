/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.filedownloader.event;

import com.liulishuo.filedownloader.event.IDownloadEvent;
import com.liulishuo.filedownloader.event.IDownloadListener;

interface IDownloadEventPool {
    public boolean addListener(String var1, IDownloadListener var2);

    public void asyncPublishInNewThread(IDownloadEvent var1);

    public boolean publish(IDownloadEvent var1);

    public boolean removeListener(String var1, IDownloadListener var2);
}

