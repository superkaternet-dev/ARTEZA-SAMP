/*
 * Decompiled with CFR 0.152.
 */
package androidx.lifecycle;

import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.arch.core.internal.SafeIterableMap;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import java.util.Map;

public abstract class LiveData<T> {
    static final Object NOT_SET = new Object();
    static final int START_VERSION = -1;
    int mActiveCount = 0;
    private boolean mChangingActiveState;
    private volatile Object mData;
    final Object mDataLock = new Object();
    private boolean mDispatchInvalidated;
    private boolean mDispatchingValue;
    private SafeIterableMap<Observer<? super T>, ObserverWrapper> mObservers = new SafeIterableMap();
    volatile Object mPendingData;
    private final Runnable mPostValueRunnable;
    private int mVersion;

    public LiveData() {
        Object object;
        this.mPendingData = object = NOT_SET;
        this.mPostValueRunnable = new Runnable(this){
            final LiveData this$0;
            {
                this.this$0 = liveData;
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public void run() {
                Object object;
                Object object2 = this.this$0.mDataLock;
                synchronized (object2) {
                    object = this.this$0.mPendingData;
                    this.this$0.mPendingData = NOT_SET;
                }
                this.this$0.setValue(object);
            }
        };
        this.mData = object;
        this.mVersion = -1;
    }

    public LiveData(T t) {
        this.mPendingData = NOT_SET;
        this.mPostValueRunnable = new /* invalid duplicate definition of identical inner class */;
        this.mData = t;
        this.mVersion = 0;
    }

    static void assertMainThread(String string2) {
        if (ArchTaskExecutor.getInstance().isMainThread()) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Cannot invoke ");
        stringBuilder.append(string2);
        stringBuilder.append(" on a background thread");
        throw new IllegalStateException(stringBuilder.toString());
    }

    private void considerNotify(ObserverWrapper observerWrapper) {
        if (!observerWrapper.mActive) {
            return;
        }
        if (!observerWrapper.shouldBeActive()) {
            observerWrapper.activeStateChanged(false);
            return;
        }
        int n = observerWrapper.mLastVersion;
        int n2 = this.mVersion;
        if (n >= n2) {
            return;
        }
        observerWrapper.mLastVersion = n2;
        observerWrapper.mObserver.onChanged(this.mData);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void changeActiveCounter(int n) {
        int n2 = this.mActiveCount;
        this.mActiveCount += n;
        if (this.mChangingActiveState) {
            return;
        }
        this.mChangingActiveState = true;
        try {
            while (true) {
                int n3;
                if (n2 == (n3 = this.mActiveCount)) {
                    this.mChangingActiveState = false;
                    return;
                }
                n = n2 == 0 && n3 > 0 ? 1 : 0;
                n2 = n2 > 0 && n3 == 0 ? 1 : 0;
                if (n != 0) {
                    this.onActive();
                } else if (n2 != 0) {
                    this.onInactive();
                }
                n2 = n3;
            }
        }
        catch (Throwable throwable) {
            this.mChangingActiveState = false;
            throw throwable;
        }
    }

    void dispatchingValue(ObserverWrapper observerWrapper) {
        if (this.mDispatchingValue) {
            this.mDispatchInvalidated = true;
            return;
        }
        this.mDispatchingValue = true;
        while (true) {
            ObserverWrapper observerWrapper2;
            block6: {
                this.mDispatchInvalidated = false;
                if (observerWrapper != null) {
                    this.considerNotify(observerWrapper);
                    observerWrapper2 = null;
                } else {
                    SafeIterableMap.IteratorWithAdditions iteratorWithAdditions = this.mObservers.iteratorWithAdditions();
                    do {
                        observerWrapper2 = observerWrapper;
                        if (!iteratorWithAdditions.hasNext()) break block6;
                        this.considerNotify((ObserverWrapper)((Map.Entry)iteratorWithAdditions.next()).getValue());
                    } while (!this.mDispatchInvalidated);
                    observerWrapper2 = observerWrapper;
                }
            }
            if (!this.mDispatchInvalidated) {
                this.mDispatchingValue = false;
                return;
            }
            observerWrapper = observerWrapper2;
        }
    }

    public T getValue() {
        Object object = this.mData;
        if (object != NOT_SET) {
            return (T)object;
        }
        return null;
    }

    int getVersion() {
        return this.mVersion;
    }

    public boolean hasActiveObservers() {
        boolean bl = this.mActiveCount > 0;
        return bl;
    }

    public boolean hasObservers() {
        boolean bl = this.mObservers.size() > 0;
        return bl;
    }

    public void observe(LifecycleOwner lifecycleOwner, Observer<? super T> object) {
        LiveData.assertMainThread("observe");
        if (lifecycleOwner.getLifecycle().getCurrentState() == Lifecycle.State.DESTROYED) {
            return;
        }
        LifecycleBoundObserver lifecycleBoundObserver = new LifecycleBoundObserver(this, lifecycleOwner, object);
        if ((object = this.mObservers.putIfAbsent((Observer<T>)object, lifecycleBoundObserver)) != null && !((ObserverWrapper)object).isAttachedTo(lifecycleOwner)) {
            throw new IllegalArgumentException("Cannot add the same observer with different lifecycles");
        }
        if (object != null) {
            return;
        }
        lifecycleOwner.getLifecycle().addObserver(lifecycleBoundObserver);
    }

    public void observeForever(Observer<? super T> object) {
        LiveData.assertMainThread("observeForever");
        AlwaysActiveObserver alwaysActiveObserver = new AlwaysActiveObserver(this, object);
        object = this.mObservers.putIfAbsent((Observer<T>)object, alwaysActiveObserver);
        if (!(object instanceof LifecycleBoundObserver)) {
            if (object != null) {
                return;
            }
            alwaysActiveObserver.activeStateChanged(true);
            return;
        }
        throw new IllegalArgumentException("Cannot add the same observer with different lifecycles");
    }

    protected void onActive() {
    }

    protected void onInactive() {
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    protected void postValue(T t) {
        Object object = this.mDataLock;
        // MONITORENTER : object
        boolean bl = this.mPendingData == NOT_SET;
        this.mPendingData = t;
        // MONITOREXIT : object
        if (!bl) {
            return;
        }
        ArchTaskExecutor.getInstance().postToMainThread(this.mPostValueRunnable);
    }

    public void removeObserver(Observer<? super T> object) {
        LiveData.assertMainThread("removeObserver");
        object = this.mObservers.remove((Observer<T>)object);
        if (object == null) {
            return;
        }
        ((ObserverWrapper)object).detachObserver();
        ((ObserverWrapper)object).activeStateChanged(false);
    }

    public void removeObservers(LifecycleOwner lifecycleOwner) {
        LiveData.assertMainThread("removeObservers");
        for (Map.Entry<Observer<T>, ObserverWrapper> entry : this.mObservers) {
            if (!entry.getValue().isAttachedTo(lifecycleOwner)) continue;
            this.removeObserver(entry.getKey());
        }
    }

    protected void setValue(T t) {
        LiveData.assertMainThread("setValue");
        ++this.mVersion;
        this.mData = t;
        this.dispatchingValue(null);
    }

    private class AlwaysActiveObserver
    extends ObserverWrapper {
        final LiveData this$0;

        AlwaysActiveObserver(LiveData liveData, Observer<? super T> observer) {
            this.this$0 = liveData;
            super(liveData, observer);
        }

        @Override
        boolean shouldBeActive() {
            return true;
        }
    }

    class LifecycleBoundObserver
    extends ObserverWrapper
    implements LifecycleEventObserver {
        final LifecycleOwner mOwner;
        final LiveData this$0;

        LifecycleBoundObserver(LiveData liveData, LifecycleOwner lifecycleOwner, Observer<? super T> observer) {
            this.this$0 = liveData;
            super(liveData, observer);
            this.mOwner = lifecycleOwner;
        }

        @Override
        void detachObserver() {
            this.mOwner.getLifecycle().removeObserver(this);
        }

        @Override
        boolean isAttachedTo(LifecycleOwner lifecycleOwner) {
            boolean bl = this.mOwner == lifecycleOwner;
            return bl;
        }

        @Override
        public void onStateChanged(LifecycleOwner object, Lifecycle.Event object2) {
            object = this.mOwner.getLifecycle().getCurrentState();
            if (object == Lifecycle.State.DESTROYED) {
                this.this$0.removeObserver(this.mObserver);
                return;
            }
            object2 = null;
            while (object2 != object) {
                object2 = object;
                this.activeStateChanged(this.shouldBeActive());
                object = this.mOwner.getLifecycle().getCurrentState();
            }
        }

        @Override
        boolean shouldBeActive() {
            return this.mOwner.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED);
        }
    }

    private abstract class ObserverWrapper {
        boolean mActive;
        int mLastVersion;
        final Observer<? super T> mObserver;
        final LiveData this$0;

        ObserverWrapper(LiveData liveData, Observer<? super T> observer) {
            this.this$0 = liveData;
            this.mLastVersion = -1;
            this.mObserver = observer;
        }

        void activeStateChanged(boolean bl) {
            if (bl == this.mActive) {
                return;
            }
            this.mActive = bl;
            LiveData liveData = this.this$0;
            int n = bl ? 1 : -1;
            liveData.changeActiveCounter(n);
            if (this.mActive) {
                this.this$0.dispatchingValue(this);
            }
        }

        void detachObserver() {
        }

        boolean isAttachedTo(LifecycleOwner lifecycleOwner) {
            return false;
        }

        abstract boolean shouldBeActive();
    }
}

