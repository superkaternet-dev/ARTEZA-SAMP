/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.okdownload;

import com.liulishuo.okdownload.DownloadContext;
import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.core.cause.EndCause;

public interface DownloadContextListener {
    public void queueEnd(DownloadContext var1);

    public void taskEnd(DownloadContext var1, DownloadTask var2, EndCause var3, Exception var4, int var5);
}

