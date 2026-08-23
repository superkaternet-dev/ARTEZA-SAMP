/*
 * Decompiled with CFR 0.152.
 */
package com.downloader.core;

import com.downloader.core.DefaultExecutorSupplier;
import com.downloader.core.ExecutorSupplier;

public class Core {
    private static Core instance = null;
    private final ExecutorSupplier executorSupplier = new DefaultExecutorSupplier();

    private Core() {
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static Core getInstance() {
        if (instance != null) return instance;
        synchronized (Core.class) {
            Core core;
            if (instance != null) return instance;
            instance = core = new Core();
            return instance;
        }
    }

    public static void shutDown() {
        if (instance != null) {
            instance = null;
        }
    }

    public ExecutorSupplier getExecutorSupplier() {
        return this.executorSupplier;
    }
}

