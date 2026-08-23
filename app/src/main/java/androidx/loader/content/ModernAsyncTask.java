/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Binder
 *  android.os.Handler
 *  android.os.Looper
 *  android.os.Message
 *  android.os.Process
 *  android.util.Log
 */
package androidx.loader.content;

import android.os.Binder;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

abstract class ModernAsyncTask<Params, Progress, Result> {
    private static final int CORE_POOL_SIZE = 5;
    private static final int KEEP_ALIVE = 1;
    private static final String LOG_TAG = "AsyncTask";
    private static final int MAXIMUM_POOL_SIZE = 128;
    private static final int MESSAGE_POST_PROGRESS = 2;
    private static final int MESSAGE_POST_RESULT = 1;
    public static final Executor THREAD_POOL_EXECUTOR;
    private static volatile Executor sDefaultExecutor;
    private static InternalHandler sHandler;
    private static final BlockingQueue<Runnable> sPoolWorkQueue;
    private static final ThreadFactory sThreadFactory;
    final AtomicBoolean mCancelled;
    private final FutureTask<Result> mFuture;
    private volatile Status mStatus = Status.PENDING;
    final AtomicBoolean mTaskInvoked;
    private final WorkerRunnable<Params, Result> mWorker;

    static {
        ThreadFactory threadFactory;
        sThreadFactory = threadFactory = new ThreadFactory(){
            private final AtomicInteger mCount = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable runnable) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("ModernAsyncTask #");
                stringBuilder.append(this.mCount.getAndIncrement());
                return new Thread(runnable, stringBuilder.toString());
            }
        };
        Object object = new LinkedBlockingQueue<Runnable>(10);
        sPoolWorkQueue = object;
        THREAD_POOL_EXECUTOR = object = new ThreadPoolExecutor(5, 128, 1L, TimeUnit.SECONDS, (BlockingQueue<Runnable>)object, threadFactory);
        sDefaultExecutor = object;
    }

    ModernAsyncTask() {
        WorkerRunnable workerRunnable;
        this.mCancelled = new AtomicBoolean();
        this.mTaskInvoked = new AtomicBoolean();
        this.mWorker = workerRunnable = new WorkerRunnable<Params, Result>(this){
            final ModernAsyncTask this$0;
            {
                this.this$0 = modernAsyncTask;
            }

            @Override
            public Result call() throws Exception {
                Object Result2;
                this.this$0.mTaskInvoked.set(true);
                Object Result3 = Result2 = null;
                try {
                    Process.setThreadPriority((int)10);
                    Result3 = Result2;
                }
                catch (Throwable throwable) {
                    try {
                        this.this$0.mCancelled.set(true);
                        throw throwable;
                    }
                    catch (Throwable throwable2) {
                        this.this$0.postResult(Result3);
                        throw throwable2;
                    }
                }
                Result3 = Result2 = (Object)this.this$0.doInBackground(this.mParams);
                Binder.flushPendingCommands();
                this.this$0.postResult(Result2);
                return Result2;
            }
        };
        this.mFuture = new FutureTask<Result>(this, workerRunnable){
            final ModernAsyncTask this$0;
            {
                this.this$0 = modernAsyncTask;
                super(callable);
            }

            @Override
            protected void done() {
                try {
                    Object v = this.get();
                    this.this$0.postResultIfNotInvoked(v);
                }
                catch (Throwable throwable) {
                    throw new RuntimeException("An error occurred while executing doInBackground()", throwable);
                }
                catch (CancellationException cancellationException) {
                    this.this$0.postResultIfNotInvoked(null);
                }
                catch (ExecutionException executionException) {
                    throw new RuntimeException("An error occurred while executing doInBackground()", executionException.getCause());
                }
                catch (InterruptedException interruptedException) {
                    Log.w((String)ModernAsyncTask.LOG_TAG, (Throwable)interruptedException);
                }
            }
        };
    }

    public static void execute(Runnable runnable) {
        sDefaultExecutor.execute(runnable);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static Handler getHandler() {
        synchronized (ModernAsyncTask.class) {
            InternalHandler internalHandler;
            if (sHandler != null) return sHandler;
            sHandler = internalHandler = new InternalHandler();
            return sHandler;
        }
    }

    public static void setDefaultExecutor(Executor executor) {
        sDefaultExecutor = executor;
    }

    public final boolean cancel(boolean bl) {
        this.mCancelled.set(true);
        return this.mFuture.cancel(bl);
    }

    protected abstract Result doInBackground(Params ... var1);

    public final ModernAsyncTask<Params, Progress, Result> execute(Params ... ParamsArray) {
        return this.executeOnExecutor(sDefaultExecutor, ParamsArray);
    }

    public final ModernAsyncTask<Params, Progress, Result> executeOnExecutor(Executor executor, Params ... ParamsArray) {
        if (this.mStatus != Status.PENDING) {
            switch (4.$SwitchMap$androidx$loader$content$ModernAsyncTask$Status[this.mStatus.ordinal()]) {
                default: {
                    throw new IllegalStateException("We should never reach this state");
                }
                case 2: {
                    throw new IllegalStateException("Cannot execute task: the task has already been executed (a task can be executed only once)");
                }
                case 1: 
            }
            throw new IllegalStateException("Cannot execute task: the task is already running.");
        }
        this.mStatus = Status.RUNNING;
        this.onPreExecute();
        this.mWorker.mParams = ParamsArray;
        executor.execute(this.mFuture);
        return this;
    }

    void finish(Result Result2) {
        if (this.isCancelled()) {
            this.onCancelled(Result2);
        } else {
            this.onPostExecute(Result2);
        }
        this.mStatus = Status.FINISHED;
    }

    public final Result get() throws InterruptedException, ExecutionException {
        return this.mFuture.get();
    }

    public final Result get(long l, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
        return this.mFuture.get(l, timeUnit);
    }

    public final Status getStatus() {
        return this.mStatus;
    }

    public final boolean isCancelled() {
        return this.mCancelled.get();
    }

    protected void onCancelled() {
    }

    protected void onCancelled(Result Result2) {
        this.onCancelled();
    }

    protected void onPostExecute(Result Result2) {
    }

    protected void onPreExecute() {
    }

    protected void onProgressUpdate(Progress ... ProgressArray) {
    }

    Result postResult(Result Result2) {
        ModernAsyncTask.getHandler().obtainMessage(1, new AsyncTaskResult<Object>(this, Result2)).sendToTarget();
        return Result2;
    }

    void postResultIfNotInvoked(Result Result2) {
        if (!this.mTaskInvoked.get()) {
            this.postResult(Result2);
        }
    }

    protected final void publishProgress(Progress ... ProgressArray) {
        if (!this.isCancelled()) {
            ModernAsyncTask.getHandler().obtainMessage(2, new AsyncTaskResult<Progress>(this, ProgressArray)).sendToTarget();
        }
    }

    private static class AsyncTaskResult<Data> {
        final Data[] mData;
        final ModernAsyncTask mTask;

        AsyncTaskResult(ModernAsyncTask modernAsyncTask, Data ... DataArray) {
            this.mTask = modernAsyncTask;
            this.mData = DataArray;
        }
    }

    private static class InternalHandler
    extends Handler {
        InternalHandler() {
            super(Looper.getMainLooper());
        }

        public void handleMessage(Message message) {
            AsyncTaskResult asyncTaskResult = (AsyncTaskResult)message.obj;
            switch (message.what) {
                default: {
                    break;
                }
                case 2: {
                    asyncTaskResult.mTask.onProgressUpdate(asyncTaskResult.mData);
                    break;
                }
                case 1: {
                    asyncTaskResult.mTask.finish(asyncTaskResult.mData[0]);
                }
            }
        }
    }

    public static final class Status
    extends Enum<Status> {
        private static final Status[] $VALUES;
        public static final /* enum */ Status FINISHED;
        public static final /* enum */ Status PENDING;
        public static final /* enum */ Status RUNNING;

        static {
            Status status;
            Status status2;
            Status status3;
            PENDING = status3 = new Status();
            RUNNING = status2 = new Status();
            FINISHED = status = new Status();
            $VALUES = new Status[]{status3, status2, status};
        }

        public static Status valueOf(String string2) {
            return Enum.valueOf(Status.class, string2);
        }

        public static Status[] values() {
            return (Status[])$VALUES.clone();
        }
    }

    private static abstract class WorkerRunnable<Params, Result>
    implements Callable<Result> {
        Params[] mParams;

        WorkerRunnable() {
        }
    }
}

