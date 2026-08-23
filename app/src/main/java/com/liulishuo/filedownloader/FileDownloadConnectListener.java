/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.filedownloader;

import com.liulishuo.filedownloader.event.DownloadServiceConnectChangedEvent;
import com.liulishuo.filedownloader.event.IDownloadEvent;
import com.liulishuo.filedownloader.event.IDownloadListener;

public abstract class FileDownloadConnectListener
extends IDownloadListener {
    private DownloadServiceConnectChangedEvent.ConnectStatus mConnectStatus;

    @Override
    public boolean callback(IDownloadEvent object) {
        if (object instanceof DownloadServiceConnectChangedEvent) {
            object = ((DownloadServiceConnectChangedEvent)object).getStatus();
            this.mConnectStatus = object;
            if (object == DownloadServiceConnectChangedEvent.ConnectStatus.connected) {
                this.connected();
            } else {
                this.disconnected();
            }
        }
        return false;
    }

    public abstract void connected();

    public abstract void disconnected();

    public DownloadServiceConnectChangedEvent.ConnectStatus getConnectStatus() {
        return this.mConnectStatus;
    }
}

