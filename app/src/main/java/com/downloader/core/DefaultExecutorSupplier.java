/*
 * Decompiled with CFR 0.152.
 */
package com.downloader.core;

import com.downloader.core.DownloadExecutor;
import com.downloader.core.ExecutorSupplier;
import com.downloader.core.MainThreadExecutor;
import com.downloader.core.PriorityThreadFactory;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DefaultExecutorSupplier
implements ExecutorSupplier {
    private static final int DEFAULT_MAX_NUM_THREADS = Runtime.getRuntime().availableProcessors() * 2 + 1;
    private final Executor backgroundExecutor;
    private final Executor mainThreadExecutor;
    private final DownloadExecutor networkExecutor;

    DefaultExecutorSupplier() {
        PriorityThreadFactory priorityThreadFactory = new PriorityThreadFactory(10);
        this.networkExecutor = new DownloadExecutor(DEFAULT_MAX_NUM_THREADS, priorityThreadFactory);
        this.backgroundExecutor = Executors.newSingleThreadExecutor();
        this.mainThreadExecutor = new MainThreadExecutor();
    }

    @Override
    public Executor forBackgroundTasks() {
        return this.backgroundExecutor;
    }

    @Override
    public DownloadExecutor forDownloadTasks() {
        return this.networkExecutor;
    }

    @Override
    public Executor forMainThreadTasks() {
        return this.mainThreadExecutor;
    }
}

