/*
 * Decompiled with CFR 0.152.
 */
package androidx.activity;

import androidx.activity.Cancellable;
import androidx.activity.OnBackPressedCallback;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import java.util.ArrayDeque;
import java.util.Iterator;

public final class OnBackPressedDispatcher {
    private final Runnable mFallbackOnBackPressed;
    final ArrayDeque<OnBackPressedCallback> mOnBackPressedCallbacks = new ArrayDeque();

    public OnBackPressedDispatcher() {
        this(null);
    }

    public OnBackPressedDispatcher(Runnable runnable) {
        this.mFallbackOnBackPressed = runnable;
    }

    public void addCallback(OnBackPressedCallback onBackPressedCallback) {
        this.addCancellableCallback(onBackPressedCallback);
    }

    public void addCallback(LifecycleOwner object, OnBackPressedCallback onBackPressedCallback) {
        if (((Lifecycle)(object = object.getLifecycle())).getCurrentState() == Lifecycle.State.DESTROYED) {
            return;
        }
        onBackPressedCallback.addCancellable(new LifecycleOnBackPressedCancellable(this, (Lifecycle)object, onBackPressedCallback));
    }

    Cancellable addCancellableCallback(OnBackPressedCallback onBackPressedCallback) {
        this.mOnBackPressedCallbacks.add(onBackPressedCallback);
        OnBackPressedCancellable onBackPressedCancellable = new OnBackPressedCancellable(this, onBackPressedCallback);
        onBackPressedCallback.addCancellable(onBackPressedCancellable);
        return onBackPressedCancellable;
    }

    public boolean hasEnabledCallbacks() {
        Iterator<OnBackPressedCallback> iterator2 = this.mOnBackPressedCallbacks.descendingIterator();
        while (iterator2.hasNext()) {
            if (!iterator2.next().isEnabled()) continue;
            return true;
        }
        return false;
    }

    public void onBackPressed() {
        Object object = this.mOnBackPressedCallbacks.descendingIterator();
        while (object.hasNext()) {
            OnBackPressedCallback onBackPressedCallback = object.next();
            if (!onBackPressedCallback.isEnabled()) continue;
            onBackPressedCallback.handleOnBackPressed();
            return;
        }
        object = this.mFallbackOnBackPressed;
        if (object != null) {
            object.run();
        }
    }

    private class LifecycleOnBackPressedCancellable
    implements LifecycleEventObserver,
    Cancellable {
        private Cancellable mCurrentCancellable;
        private final Lifecycle mLifecycle;
        private final OnBackPressedCallback mOnBackPressedCallback;
        final OnBackPressedDispatcher this$0;

        LifecycleOnBackPressedCancellable(OnBackPressedDispatcher onBackPressedDispatcher, Lifecycle lifecycle, OnBackPressedCallback onBackPressedCallback) {
            this.this$0 = onBackPressedDispatcher;
            this.mLifecycle = lifecycle;
            this.mOnBackPressedCallback = onBackPressedCallback;
            lifecycle.addObserver(this);
        }

        @Override
        public void cancel() {
            this.mLifecycle.removeObserver(this);
            this.mOnBackPressedCallback.removeCancellable(this);
            Cancellable cancellable = this.mCurrentCancellable;
            if (cancellable != null) {
                cancellable.cancel();
                this.mCurrentCancellable = null;
            }
        }

        @Override
        public void onStateChanged(LifecycleOwner object, Lifecycle.Event event) {
            if (event == Lifecycle.Event.ON_START) {
                this.mCurrentCancellable = this.this$0.addCancellableCallback(this.mOnBackPressedCallback);
            } else if (event == Lifecycle.Event.ON_STOP) {
                object = this.mCurrentCancellable;
                if (object != null) {
                    object.cancel();
                }
            } else if (event == Lifecycle.Event.ON_DESTROY) {
                this.cancel();
            }
        }
    }

    private class OnBackPressedCancellable
    implements Cancellable {
        private final OnBackPressedCallback mOnBackPressedCallback;
        final OnBackPressedDispatcher this$0;

        OnBackPressedCancellable(OnBackPressedDispatcher onBackPressedDispatcher, OnBackPressedCallback onBackPressedCallback) {
            this.this$0 = onBackPressedDispatcher;
            this.mOnBackPressedCallback = onBackPressedCallback;
        }

        @Override
        public void cancel() {
            this.this$0.mOnBackPressedCallbacks.remove(this.mOnBackPressedCallback);
            this.mOnBackPressedCallback.removeCancellable(this);
        }
    }
}

