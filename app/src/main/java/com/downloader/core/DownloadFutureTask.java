/*
 * Decompiled with CFR 0.152.
 */
package com.downloader.core;

import com.downloader.Priority;
import com.downloader.internal.DownloadRunnable;
import java.util.concurrent.FutureTask;

public class DownloadFutureTask
extends FutureTask<DownloadRunnable>
implements Comparable<DownloadFutureTask> {
    private final DownloadRunnable runnable;

    DownloadFutureTask(DownloadRunnable downloadRunnable) {
        super(downloadRunnable, null);
        this.runnable = downloadRunnable;
    }

    @Override
    public int compareTo(DownloadFutureTask downloadFutureTask) {
        int n;
        int n2;
        Priority priority = this.runnable.priority;
        Priority priority2 = downloadFutureTask.runnable.priority;
        if (priority == priority2) {
            n2 = this.runnable.sequence;
            n = downloadFutureTask.runnable.sequence;
        } else {
            n2 = priority2.ordinal();
            n = priority.ordinal();
        }
        return n2 - n;
    }
}

