/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.filedownloader;

import com.liulishuo.filedownloader.event.DownloadEventPoolImpl;

public class FileDownloadEventPool
extends DownloadEventPoolImpl {
    private FileDownloadEventPool() {
    }

    public static FileDownloadEventPool getImpl() {
        return HolderClass.INSTANCE;
    }

    private static class HolderClass {
        private static final FileDownloadEventPool INSTANCE = new FileDownloadEventPool();

        private HolderClass() {
        }
    }
}

