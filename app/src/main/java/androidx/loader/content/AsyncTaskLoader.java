/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Handler
 *  android.os.SystemClock
 */
package androidx.loader.content;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import androidx.core.os.OperationCanceledException;
import androidx.core.util.TimeUtils;
import androidx.loader.content.Loader;
import androidx.loader.content.ModernAsyncTask;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

public abstract class AsyncTaskLoader<D>
extends Loader<D> {
    static final boolean DEBUG = false;
    static final String TAG = "AsyncTaskLoader";
    volatile LoadTask mCancellingTask;
    private final Executor mExecutor;
    Handler mHandler;
    long mLastLoadCompleteTime = -10000L;
    volatile LoadTask mTask;
    long mUpdateThrottle;

    public AsyncTaskLoader(Context context) {
        this(context, ModernAsyncTask.THREAD_POOL_EXECUTOR);
    }

    private AsyncTaskLoader(Context context, Executor executor) {
        super(context);
        this.mExecutor = executor;
    }

    public void cancelLoadInBackground() {
    }

    void dispatchOnCancelled(LoadTask loadTask, D d) {
        this.onCanceled(d);
        if (this.mCancellingTask == loadTask) {
            this.rollbackContentChanged();
            this.mLastLoadCompleteTime = SystemClock.uptimeMillis();
            this.mCancellingTask = null;
            this.deliverCancellation();
            this.executePendingTask();
        }
    }

    void dispatchOnLoadComplete(LoadTask loadTask, D d) {
        if (this.mTask != loadTask) {
            this.dispatchOnCancelled(loadTask, d);
        } else if (this.isAbandoned()) {
            this.onCanceled(d);
        } else {
            this.commitContentChanged();
            this.mLastLoadCompleteTime = SystemClock.uptimeMillis();
            this.mTask = null;
            this.deliverResult(d);
        }
    }

    @Override
    @Deprecated
    public void dump(String string2, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] stringArray) {
        super.dump(string2, fileDescriptor, printWriter, stringArray);
        if (this.mTask != null) {
            printWriter.print(string2);
            printWriter.print("mTask=");
            printWriter.print(this.mTask);
            printWriter.print(" waiting=");
            printWriter.println(this.mTask.waiting);
        }
        if (this.mCancellingTask != null) {
            printWriter.print(string2);
            printWriter.print("mCancellingTask=");
            printWriter.print(this.mCancellingTask);
            printWriter.print(" waiting=");
            printWriter.println(this.mCancellingTask.waiting);
        }
        if (this.mUpdateThrottle != 0L) {
            printWriter.print(string2);
            printWriter.print("mUpdateThrottle=");
            TimeUtils.formatDuration(this.mUpdateThrottle, printWriter);
            printWriter.print(" mLastLoadCompleteTime=");
            TimeUtils.formatDuration(this.mLastLoadCompleteTime, SystemClock.uptimeMillis(), printWriter);
            printWriter.println();
        }
    }

    void executePendingTask() {
        if (this.mCancellingTask == null && this.mTask != null) {
            if (this.mTask.waiting) {
                this.mTask.waiting = false;
                this.mHandler.removeCallbacks((Runnable)this.mTask);
            }
            if (this.mUpdateThrottle > 0L && SystemClock.uptimeMillis() < this.mLastLoadCompleteTime + this.mUpdateThrottle) {
                this.mTask.waiting = true;
                this.mHandler.postAtTime((Runnable)this.mTask, this.mLastLoadCompleteTime + this.mUpdateThrottle);
                return;
            }
            this.mTask.executeOnExecutor(this.mExecutor, null);
        }
    }

    public boolean isLoadInBackgroundCanceled() {
        boolean bl = this.mCancellingTask != null;
        return bl;
    }

    public abstract D loadInBackground();

    @Override
    protected boolean onCancelLoad() {
        if (this.mTask != null) {
            if (!this.mStarted) {
                this.mContentChanged = true;
            }
            if (this.mCancellingTask != null) {
                if (this.mTask.waiting) {
                    this.mTask.waiting = false;
                    this.mHandler.removeCallbacks((Runnable)this.mTask);
                }
                this.mTask = null;
                return false;
            }
            if (this.mTask.waiting) {
                this.mTask.waiting = false;
                this.mHandler.removeCallbacks((Runnable)this.mTask);
                this.mTask = null;
                return false;
            }
            boolean bl = this.mTask.cancel(false);
            if (bl) {
                this.mCancellingTask = this.mTask;
                this.cancelLoadInBackground();
            }
            this.mTask = null;
            return bl;
        }
        return false;
    }

    public void onCanceled(D d) {
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
        this.cancelLoad();
        this.mTask = new LoadTask(this);
        this.executePendingTask();
    }

    protected D onLoadInBackground() {
        return this.loadInBackground();
    }

    public void setUpdateThrottle(long l) {
        this.mUpdateThrottle = l;
        if (l != 0L) {
            this.mHandler = new Handler();
        }
    }

    public void waitForLoader() {
        LoadTask loadTask = this.mTask;
        if (loadTask != null) {
            loadTask.waitForLoader();
        }
    }

    final class LoadTask
    extends ModernAsyncTask<Void, Void, D>
    implements Runnable {
        private final CountDownLatch mDone;
        final AsyncTaskLoader this$0;
        boolean waiting;

        LoadTask(AsyncTaskLoader asyncTaskLoader) {
            this.this$0 = asyncTaskLoader;
            this.mDone = new CountDownLatch(1);
        }

        protected D doInBackground(Void ... object) {
            try {
                object = this.this$0.onLoadInBackground();
            }
            catch (OperationCanceledException operationCanceledException) {
                if (this.isCancelled()) {
                    return null;
                }
                throw operationCanceledException;
            }
            return object;
        }

        @Override
        protected void onCancelled(D d) {
            try {
                this.this$0.dispatchOnCancelled(this, d);
                return;
            }
            finally {
                this.mDone.countDown();
            }
        }

        @Override
        protected void onPostExecute(D d) {
            try {
                this.this$0.dispatchOnLoadComplete(this, d);
                return;
            }
            finally {
                this.mDone.countDown();
            }
        }

        @Override
        public void run() {
            this.waiting = false;
            this.this$0.executePendingTask();
        }

        public void waitForLoader() {
            try {
                this.mDone.await();
            }
            catch (InterruptedException interruptedException) {
                // empty catch block
            }
        }
    }
}

