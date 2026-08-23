/*
 * Decompiled with CFR 0.152.
 */
package androidx.lifecycle;

import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.lifecycle.LiveData;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class ComputableLiveData<T> {
    final AtomicBoolean mComputing;
    final Executor mExecutor;
    final AtomicBoolean mInvalid = new AtomicBoolean(true);
    final Runnable mInvalidationRunnable;
    final LiveData<T> mLiveData;
    final Runnable mRefreshRunnable;

    public ComputableLiveData() {
        this(ArchTaskExecutor.getIOThreadExecutor());
    }

    public ComputableLiveData(Executor executor) {
        this.mComputing = new AtomicBoolean(false);
        this.mRefreshRunnable = new Runnable(this){
            final ComputableLiveData this$0;
            {
                this.this$0 = computableLiveData;
            }

            @Override
            public void run() {
                boolean bl;
                do {
                    bl = false;
                    boolean bl2 = false;
                    if (!this.this$0.mComputing.compareAndSet(false, true)) continue;
                    Object t = null;
                    bl = bl2;
                    while (true) {
                        if (!this.this$0.mInvalid.compareAndSet(true, false)) break;
                        bl = true;
                        t = this.this$0.compute();
                        continue;
                        break;
                    }
                    if (!bl) continue;
                    try {
                        this.this$0.mLiveData.postValue(t);
                    }
                    catch (Throwable throwable) {
                        throw throwable;
                    }
                    finally {
                        this.this$0.mComputing.set(false);
                    }
                } while (bl && this.this$0.mInvalid.get());
            }
        };
        this.mInvalidationRunnable = new Runnable(this){
            final ComputableLiveData this$0;
            {
                this.this$0 = computableLiveData;
            }

            @Override
            public void run() {
                boolean bl = this.this$0.mLiveData.hasActiveObservers();
                if (this.this$0.mInvalid.compareAndSet(false, true) && bl) {
                    this.this$0.mExecutor.execute(this.this$0.mRefreshRunnable);
                }
            }
        };
        this.mExecutor = executor;
        this.mLiveData = new LiveData<T>(this){
            final ComputableLiveData this$0;
            {
                this.this$0 = computableLiveData;
            }

            @Override
            protected void onActive() {
                this.this$0.mExecutor.execute(this.this$0.mRefreshRunnable);
            }
        };
    }

    protected abstract T compute();

    public LiveData<T> getLiveData() {
        return this.mLiveData;
    }

    public void invalidate() {
        ArchTaskExecutor.getInstance().executeOnMainThread(this.mInvalidationRunnable);
    }
}

