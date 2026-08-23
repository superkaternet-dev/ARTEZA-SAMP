/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.filedownloader.event;

import com.liulishuo.filedownloader.util.FileDownloadLog;

public abstract class IDownloadEvent {
    public Runnable callback = null;
    protected final String id;

    public IDownloadEvent(String string2) {
        this.id = string2;
    }

    public IDownloadEvent(String string2, boolean bl) {
        this.id = string2;
        if (bl) {
            FileDownloadLog.w(this, "do not handle ORDER any more, %s", string2);
        }
    }

    public final String getId() {
        return this.id;
    }
}

