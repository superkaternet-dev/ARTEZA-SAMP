/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.filedownloader.util;

import com.liulishuo.filedownloader.util.FileDownloadUtils;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class FileDownloadExecutors {
    private static final int DEFAULT_IDLE_SECOND = 15;

    public static ThreadPoolExecutor newDefaultThreadPool(int n, String string2) {
        return FileDownloadExecutors.newDefaultThreadPool(n, new LinkedBlockingQueue<Runnable>(), string2);
    }

    public static ThreadPoolExecutor newDefaultThreadPool(int n, LinkedBlockingQueue<Runnable> object, String string2) {
        object = new ThreadPoolExecutor(n, n, 15L, TimeUnit.SECONDS, (BlockingQueue<Runnable>)object, new FileDownloadThreadFactory(string2));
        ((ThreadPoolExecutor)object).allowCoreThreadTimeOut(true);
        return object;
    }

    public static ThreadPoolExecutor newFixedThreadPool(String string2) {
        return new ThreadPoolExecutor(0, Integer.MAX_VALUE, 15L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), new FileDownloadThreadFactory(string2));
    }

    static class FileDownloadThreadFactory
    implements ThreadFactory {
        private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);
        private final ThreadGroup group;
        private final String namePrefix;
        private final AtomicInteger threadNumber = new AtomicInteger(1);

        FileDownloadThreadFactory(String string2) {
            this.group = Thread.currentThread().getThreadGroup();
            this.namePrefix = FileDownloadUtils.getThreadPoolName(string2);
        }

        @Override
        public Thread newThread(Runnable runnable) {
            ThreadGroup threadGroup = this.group;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.namePrefix);
            stringBuilder.append(this.threadNumber.getAndIncrement());
            runnable = new Thread(threadGroup, runnable, stringBuilder.toString(), 0L);
            if (((Thread)runnable).isDaemon()) {
                ((Thread)runnable).setDaemon(false);
            }
            if (((Thread)runnable).getPriority() != 5) {
                ((Thread)runnable).setPriority(5);
            }
            return runnable;
        }
    }
}

