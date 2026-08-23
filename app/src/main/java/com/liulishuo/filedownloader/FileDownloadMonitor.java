/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.filedownloader;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;

public class FileDownloadMonitor {
    private static IMonitor monitor;

    public static IMonitor getMonitor() {
        return monitor;
    }

    public static boolean isValid() {
        boolean bl = FileDownloadMonitor.getMonitor() != null;
        return bl;
    }

    public static void releaseGlobalMonitor() {
        monitor = null;
    }

    public static void setGlobalMonitor(IMonitor iMonitor) {
        monitor = iMonitor;
    }

    public static interface IMonitor {
        public void onRequestStart(int var1, boolean var2, FileDownloadListener var3);

        public void onRequestStart(BaseDownloadTask var1);

        public void onTaskBegin(BaseDownloadTask var1);

        public void onTaskOver(BaseDownloadTask var1);

        public void onTaskStarted(BaseDownloadTask var1);
    }
}

