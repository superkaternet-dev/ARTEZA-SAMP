/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core.utilities;

import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.RunLoop;
import com.google.firebase.database.core.ThreadInitializer;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public abstract class DefaultRunLoop
implements RunLoop {
    private ScheduledThreadPoolExecutor executor;

    public DefaultRunLoop() {
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
        this.executor = scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(this, 1, new FirebaseThreadFactory(this)){
            final DefaultRunLoop this$0;
            {
                this.this$0 = defaultRunLoop;
                super(n, threadFactory);
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            protected void afterExecute(Runnable object, Throwable throwable) {
                Object object2;
                block8: {
                    super.afterExecute((Runnable)object, throwable);
                    object2 = throwable;
                    if (throwable == null) {
                        object2 = throwable;
                        if (object instanceof Future) {
                            object2 = (Future)object;
                            object = throwable;
                            try {
                                if (object2.isDone()) {
                                    object2.get();
                                    object = throwable;
                                }
                            }
                            catch (InterruptedException interruptedException) {
                                Thread.currentThread().interrupt();
                                object2 = throwable;
                                break block8;
                            }
                            catch (ExecutionException executionException) {
                                object = executionException.getCause();
                            }
                            catch (CancellationException cancellationException) {
                                object = throwable;
                            }
                            object2 = object;
                        }
                    }
                }
                if (object2 != null) {
                    this.this$0.handleException((Throwable)object2);
                }
            }
        };
        scheduledThreadPoolExecutor.setKeepAliveTime(3L, TimeUnit.SECONDS);
    }

    public static String messageForException(Throwable serializable) {
        if (serializable instanceof OutOfMemoryError) {
            return "Firebase Database encountered an OutOfMemoryError. You may need to reduce the amount of data you are syncing to the client (e.g. by using queries or syncing a deeper path). See https://firebase.google.com/docs/database/ios/structure-data#best_practices_for_data_structure and https://firebase.google.com/docs/database/android/retrieve-data#filtering_data";
        }
        if (serializable instanceof NoClassDefFoundError) {
            return "A symbol that the Firebase Database SDK depends on failed to load. This usually indicates that your project includes an incompatible version of another Firebase dependency. If updating your dependencies to the latest version does not resolve this issue, please file a report at https://github.com/firebase/firebase-android-sdk";
        }
        if (serializable instanceof DatabaseException) {
            return "";
        }
        serializable = new StringBuilder();
        ((StringBuilder)serializable).append("Uncaught exception in Firebase Database runloop (");
        ((StringBuilder)serializable).append(FirebaseDatabase.getSdkVersion());
        ((StringBuilder)serializable).append("). If you are not already on the latest version of the Firebase SDKs, try updating your dependencies. Should this problem persist, please file a report at https://github.com/firebase/firebase-android-sdk");
        return ((StringBuilder)serializable).toString();
    }

    public ScheduledExecutorService getExecutorService() {
        return this.executor;
    }

    protected ThreadFactory getThreadFactory() {
        return Executors.defaultThreadFactory();
    }

    protected ThreadInitializer getThreadInitializer() {
        return ThreadInitializer.defaultInstance;
    }

    public abstract void handleException(Throwable var1);

    @Override
    public void restart() {
        this.executor.setCorePoolSize(1);
    }

    @Override
    public ScheduledFuture schedule(Runnable runnable, long l) {
        return this.executor.schedule(runnable, l, TimeUnit.MILLISECONDS);
    }

    @Override
    public void scheduleNow(Runnable runnable) {
        this.executor.execute(runnable);
    }

    @Override
    public void shutdown() {
        this.executor.setCorePoolSize(0);
    }

    private class FirebaseThreadFactory
    implements ThreadFactory {
        final DefaultRunLoop this$0;

        private FirebaseThreadFactory(DefaultRunLoop defaultRunLoop) {
            this.this$0 = defaultRunLoop;
        }

        @Override
        public Thread newThread(Runnable runnable) {
            runnable = this.this$0.getThreadFactory().newThread(runnable);
            ThreadInitializer threadInitializer = this.this$0.getThreadInitializer();
            threadInitializer.setName((Thread)runnable, "FirebaseDatabaseWorker");
            threadInitializer.setDaemon((Thread)runnable, true);
            threadInitializer.setUncaughtExceptionHandler((Thread)runnable, new Thread.UncaughtExceptionHandler(this){
                final FirebaseThreadFactory this$1;
                {
                    this.this$1 = firebaseThreadFactory;
                }

                @Override
                public void uncaughtException(Thread thread2, Throwable throwable) {
                    this.this$1.this$0.handleException(throwable);
                }
            });
            return runnable;
        }
    }
}

