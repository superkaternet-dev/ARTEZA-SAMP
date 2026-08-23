/*
 * Decompiled with CFR 0.152.
 */
package com.downloader.core;

import com.downloader.core.DownloadExecutor;
import java.util.concurrent.Executor;

public interface ExecutorSupplier {
    public Executor forBackgroundTasks();

    public DownloadExecutor forDownloadTasks();

    public Executor forMainThreadTasks();
}

