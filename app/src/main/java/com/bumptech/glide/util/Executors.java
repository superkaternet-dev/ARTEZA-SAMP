/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.util;

import com.bumptech.glide.util.Util;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public final class Executors {
    private static final Executor DIRECT_EXECUTOR;
    private static final Executor MAIN_THREAD_EXECUTOR;

    static {
        MAIN_THREAD_EXECUTOR = new Executor(){

            @Override
            public void execute(Runnable runnable) {
                Util.postOnUiThread(runnable);
            }
        };
        DIRECT_EXECUTOR = new Executor(){

            @Override
            public void execute(Runnable runnable) {
                runnable.run();
            }
        };
    }

    private Executors() {
    }

    public static Executor directExecutor() {
        return DIRECT_EXECUTOR;
    }

    public static Executor mainThreadExecutor() {
        return MAIN_THREAD_EXECUTOR;
    }

    public static void shutdownAndAwaitTermination(ExecutorService executorService) {
        executorService.shutdownNow();
        try {
            if (!executorService.awaitTermination(5L, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
                if (!executorService.awaitTermination(5L, TimeUnit.SECONDS)) {
                    RuntimeException runtimeException = new RuntimeException("Failed to shutdown");
                    throw runtimeException;
                }
            }
            return;
        }
        catch (InterruptedException interruptedException) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
            throw new RuntimeException(interruptedException);
        }
    }
}

