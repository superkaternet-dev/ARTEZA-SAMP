/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Build$VERSION
 *  android.os.Handler
 *  android.os.Handler$Callback
 *  android.os.Looper
 */
package androidx.arch.core.executor;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import androidx.arch.core.executor.TaskExecutor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultTaskExecutor
extends TaskExecutor {
    private final ExecutorService mDiskIO;
    private final Object mLock = new Object();
    private volatile Handler mMainHandler;

    public DefaultTaskExecutor() {
        this.mDiskIO = Executors.newFixedThreadPool(4, new ThreadFactory(this){
            private static final String THREAD_NAME_STEM = "arch_disk_io_%d";
            private final AtomicInteger mThreadId;
            final DefaultTaskExecutor this$0;
            {
                this.this$0 = defaultTaskExecutor;
                this.mThreadId = new AtomicInteger(0);
            }

            @Override
            public Thread newThread(Runnable runnable) {
                runnable = new Thread(runnable);
                ((Thread)runnable).setName(String.format(THREAD_NAME_STEM, this.mThreadId.getAndIncrement()));
                return runnable;
            }
        });
    }

    private static Handler createAsync(Looper looper) {
        if (Build.VERSION.SDK_INT >= 28) {
            return Handler.createAsync((Looper)looper);
        }
        if (Build.VERSION.SDK_INT >= 16) {
            try {
                Handler handler = (Handler)Handler.class.getDeclaredConstructor(Looper.class, Handler.Callback.class, Boolean.TYPE).newInstance(looper, null, true);
                return handler;
            }
            catch (InvocationTargetException invocationTargetException) {
                return new Handler(looper);
            }
            catch (NoSuchMethodException noSuchMethodException) {
            }
            catch (InstantiationException instantiationException) {
            }
            catch (IllegalAccessException illegalAccessException) {
                // empty catch block
            }
        }
        return new Handler(looper);
    }

    @Override
    public void executeOnDiskIO(Runnable runnable) {
        this.mDiskIO.execute(runnable);
    }

    @Override
    public boolean isMainThread() {
        boolean bl = Looper.getMainLooper().getThread() == Thread.currentThread();
        return bl;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void postToMainThread(Runnable runnable) {
        if (this.mMainHandler == null) {
            Object object = this.mLock;
            synchronized (object) {
                if (this.mMainHandler == null) {
                    this.mMainHandler = DefaultTaskExecutor.createAsync(Looper.getMainLooper());
                }
            }
        }
        this.mMainHandler.post(runnable);
    }
}

