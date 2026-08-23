/*
 * Decompiled with CFR 0.152.
 */
package androidx.arch.core.executor;

import androidx.arch.core.executor.DefaultTaskExecutor;
import androidx.arch.core.executor.TaskExecutor;
import java.util.concurrent.Executor;

public class ArchTaskExecutor
extends TaskExecutor {
    private static final Executor sIOThreadExecutor;
    private static volatile ArchTaskExecutor sInstance;
    private static final Executor sMainThreadExecutor;
    private TaskExecutor mDefaultTaskExecutor;
    private TaskExecutor mDelegate;

    static {
        sMainThreadExecutor = new Executor(){

            @Override
            public void execute(Runnable runnable) {
                ArchTaskExecutor.getInstance().postToMainThread(runnable);
            }
        };
        sIOThreadExecutor = new Executor(){

            @Override
            public void execute(Runnable runnable) {
                ArchTaskExecutor.getInstance().executeOnDiskIO(runnable);
            }
        };
    }

    private ArchTaskExecutor() {
        DefaultTaskExecutor defaultTaskExecutor = new DefaultTaskExecutor();
        this.mDefaultTaskExecutor = defaultTaskExecutor;
        this.mDelegate = defaultTaskExecutor;
    }

    public static Executor getIOThreadExecutor() {
        return sIOThreadExecutor;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static ArchTaskExecutor getInstance() {
        if (sInstance != null) {
            return sInstance;
        }
        synchronized (ArchTaskExecutor.class) {
            if (sInstance == null) {
                ArchTaskExecutor archTaskExecutor;
                sInstance = archTaskExecutor = new ArchTaskExecutor();
            }
            return sInstance;
        }
    }

    public static Executor getMainThreadExecutor() {
        return sMainThreadExecutor;
    }

    @Override
    public void executeOnDiskIO(Runnable runnable) {
        this.mDelegate.executeOnDiskIO(runnable);
    }

    @Override
    public boolean isMainThread() {
        return this.mDelegate.isMainThread();
    }

    @Override
    public void postToMainThread(Runnable runnable) {
        this.mDelegate.postToMainThread(runnable);
    }

    public void setDelegate(TaskExecutor taskExecutor) {
        if (taskExecutor == null) {
            taskExecutor = this.mDefaultTaskExecutor;
        }
        this.mDelegate = taskExecutor;
    }
}

