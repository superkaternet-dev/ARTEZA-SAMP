/*
 * Decompiled with CFR 0.152.
 */
package com.downloader.core;

import com.downloader.core.DownloadFutureTask;
import com.downloader.internal.DownloadRunnable;
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DownloadExecutor
extends ThreadPoolExecutor {
    DownloadExecutor(int n, ThreadFactory threadFactory) {
        super(n, n, 0L, TimeUnit.MILLISECONDS, new PriorityBlockingQueue<Runnable>(), threadFactory);
    }

    @Override
    public Future<?> submit(Runnable runnable) {
        runnable = new DownloadFutureTask((DownloadRunnable)runnable);
        this.execute(runnable);
        return runnable;
    }
}

