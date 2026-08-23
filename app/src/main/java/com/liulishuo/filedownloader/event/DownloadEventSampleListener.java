/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.filedownloader.event;

import com.liulishuo.filedownloader.event.IDownloadEvent;
import com.liulishuo.filedownloader.event.IDownloadListener;

public class DownloadEventSampleListener
extends IDownloadListener {
    private final IEventListener i;

    public DownloadEventSampleListener(IEventListener iEventListener) {
        this.i = iEventListener;
    }

    @Override
    public boolean callback(IDownloadEvent iDownloadEvent) {
        IEventListener iEventListener = this.i;
        boolean bl = iEventListener != null && iEventListener.callback(iDownloadEvent);
        return bl;
    }

    public static interface IEventListener {
        public boolean callback(IDownloadEvent var1);
    }
}

