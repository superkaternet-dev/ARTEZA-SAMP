/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core;

import com.google.firebase.database.core.EventTarget;
import com.google.firebase.database.core.ThreadInitializer;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class ThreadPoolEventTarget
implements EventTarget {
    private final ThreadPoolExecutor executor;

    public ThreadPoolEventTarget(ThreadFactory threadFactory, ThreadInitializer threadInitializer) {
        LinkedBlockingQueue<Runnable> linkedBlockingQueue = new LinkedBlockingQueue<Runnable>();
        this.executor = new ThreadPoolExecutor(1, 1, 3L, TimeUnit.SECONDS, linkedBlockingQueue, new ThreadFactory(this, threadFactory, threadInitializer){
            final ThreadPoolEventTarget this$0;
            final ThreadInitializer val$threadInitializer;
            final ThreadFactory val$wrappedFactory;
            {
                this.this$0 = threadPoolEventTarget;
                this.val$wrappedFactory = threadFactory;
                this.val$threadInitializer = threadInitializer;
            }

            @Override
            public Thread newThread(Runnable runnable) {
                runnable = this.val$wrappedFactory.newThread(runnable);
                this.val$threadInitializer.setName((Thread)runnable, "FirebaseDatabaseEventTarget");
                this.val$threadInitializer.setDaemon((Thread)runnable, true);
                return runnable;
            }
        });
    }

    @Override
    public void postEvent(Runnable runnable) {
        this.executor.execute(runnable);
    }

    @Override
    public void restart() {
        this.executor.setCorePoolSize(1);
    }

    @Override
    public void shutdown() {
        this.executor.setCorePoolSize(0);
    }
}

