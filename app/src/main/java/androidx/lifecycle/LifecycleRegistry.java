/*
 * Decompiled with CFR 0.152.
 */
package androidx.lifecycle;

import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.arch.core.internal.FastSafeIterableMap;
import androidx.arch.core.internal.SafeIterableMap;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Lifecycling;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class LifecycleRegistry
extends Lifecycle {
    private int mAddingObserverCounter = 0;
    private final boolean mEnforceMainThread;
    private boolean mHandlingEvent = false;
    private final WeakReference<LifecycleOwner> mLifecycleOwner;
    private boolean mNewEventOccurred = false;
    private FastSafeIterableMap<LifecycleObserver, ObserverWithState> mObserverMap = new FastSafeIterableMap();
    private ArrayList<Lifecycle.State> mParentStates = new ArrayList();
    private Lifecycle.State mState;

    public LifecycleRegistry(LifecycleOwner lifecycleOwner) {
        this(lifecycleOwner, true);
    }

    private LifecycleRegistry(LifecycleOwner lifecycleOwner, boolean bl) {
        this.mLifecycleOwner = new WeakReference<LifecycleOwner>(lifecycleOwner);
        this.mState = Lifecycle.State.INITIALIZED;
        this.mEnforceMainThread = bl;
    }

    private void backwardPass(LifecycleOwner object) {
        Iterator iterator2 = this.mObserverMap.descendingIterator();
        while (iterator2.hasNext() && !this.mNewEventOccurred) {
            Map.Entry entry = iterator2.next();
            ObserverWithState observerWithState = (ObserverWithState)entry.getValue();
            while (observerWithState.mState.compareTo(this.mState) > 0 && !this.mNewEventOccurred && this.mObserverMap.contains((LifecycleObserver)entry.getKey())) {
                Lifecycle.Event event = Lifecycle.Event.downFrom(observerWithState.mState);
                if (event != null) {
                    this.pushParentState(event.getTargetState());
                    observerWithState.dispatchEvent((LifecycleOwner)object, event);
                    this.popParentState();
                    continue;
                }
                object = new StringBuilder();
                ((StringBuilder)object).append("no event down from ");
                ((StringBuilder)object).append((Object)observerWithState.mState);
                throw new IllegalStateException(((StringBuilder)object).toString());
            }
        }
    }

    private Lifecycle.State calculateTargetState(LifecycleObserver object) {
        Object object2;
        block0: {
            object = this.mObserverMap.ceil((LifecycleObserver)object);
            object2 = null;
            object = object != null ? ((ObserverWithState)object.getValue()).mState : null;
            if (this.mParentStates.isEmpty()) break block0;
            object2 = this.mParentStates;
            object2 = object2.get(object2.size() - 1);
        }
        return LifecycleRegistry.min(LifecycleRegistry.min(this.mState, (Lifecycle.State)((Object)object)), object2);
    }

    public static LifecycleRegistry createUnsafe(LifecycleOwner lifecycleOwner) {
        return new LifecycleRegistry(lifecycleOwner, false);
    }

    private void enforceMainThreadIfNeeded(String string2) {
        if (this.mEnforceMainThread && !ArchTaskExecutor.getInstance().isMainThread()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Method ");
            stringBuilder.append(string2);
            stringBuilder.append(" must be called on the main thread");
            throw new IllegalStateException(stringBuilder.toString());
        }
    }

    private void forwardPass(LifecycleOwner object) {
        SafeIterableMap.IteratorWithAdditions iteratorWithAdditions = this.mObserverMap.iteratorWithAdditions();
        while (iteratorWithAdditions.hasNext() && !this.mNewEventOccurred) {
            Map.Entry entry = (Map.Entry)iteratorWithAdditions.next();
            ObserverWithState observerWithState = (ObserverWithState)entry.getValue();
            while (observerWithState.mState.compareTo(this.mState) < 0 && !this.mNewEventOccurred && this.mObserverMap.contains((LifecycleObserver)entry.getKey())) {
                this.pushParentState(observerWithState.mState);
                Lifecycle.Event event = Lifecycle.Event.upFrom(observerWithState.mState);
                if (event != null) {
                    observerWithState.dispatchEvent((LifecycleOwner)object, event);
                    this.popParentState();
                    continue;
                }
                object = new StringBuilder();
                ((StringBuilder)object).append("no event up from ");
                ((StringBuilder)object).append((Object)observerWithState.mState);
                throw new IllegalStateException(((StringBuilder)object).toString());
            }
        }
    }

    private boolean isSynced() {
        int n = this.mObserverMap.size();
        boolean bl = true;
        if (n == 0) {
            return true;
        }
        Lifecycle.State state = ((ObserverWithState)this.mObserverMap.eldest().getValue()).mState;
        Lifecycle.State state2 = ((ObserverWithState)this.mObserverMap.newest().getValue()).mState;
        if (state != state2 || this.mState != state2) {
            bl = false;
        }
        return bl;
    }

    static Lifecycle.State min(Lifecycle.State state, Lifecycle.State state2) {
        block0: {
            if (state2 == null || state2.compareTo(state) >= 0) break block0;
            state = state2;
        }
        return state;
    }

    private void moveToState(Lifecycle.State state) {
        if (this.mState == state) {
            return;
        }
        this.mState = state;
        if (!this.mHandlingEvent && this.mAddingObserverCounter == 0) {
            this.mHandlingEvent = true;
            this.sync();
            this.mHandlingEvent = false;
            return;
        }
        this.mNewEventOccurred = true;
    }

    private void popParentState() {
        ArrayList<Lifecycle.State> arrayList = this.mParentStates;
        arrayList.remove(arrayList.size() - 1);
    }

    private void pushParentState(Lifecycle.State state) {
        this.mParentStates.add(state);
    }

    private void sync() {
        Object object = (LifecycleOwner)this.mLifecycleOwner.get();
        if (object != null) {
            while (!this.isSynced()) {
                this.mNewEventOccurred = false;
                if (this.mState.compareTo(((ObserverWithState)this.mObserverMap.eldest().getValue()).mState) < 0) {
                    this.backwardPass((LifecycleOwner)object);
                }
                Map.Entry entry = this.mObserverMap.newest();
                if (this.mNewEventOccurred || entry == null || this.mState.compareTo(((ObserverWithState)entry.getValue()).mState) <= 0) continue;
                this.forwardPass((LifecycleOwner)object);
            }
            this.mNewEventOccurred = false;
            return;
        }
        object = new IllegalStateException("LifecycleOwner of this LifecycleRegistry is alreadygarbage collected. It is too late to change lifecycle state.");
        throw object;
    }

    @Override
    public void addObserver(LifecycleObserver object) {
        this.enforceMainThreadIfNeeded("addObserver");
        Enum enum_ = this.mState == Lifecycle.State.DESTROYED ? Lifecycle.State.DESTROYED : Lifecycle.State.INITIALIZED;
        ObserverWithState observerWithState = new ObserverWithState((LifecycleObserver)object, (Lifecycle.State)enum_);
        if (this.mObserverMap.putIfAbsent((LifecycleObserver)object, observerWithState) != null) {
            return;
        }
        LifecycleOwner lifecycleOwner = (LifecycleOwner)this.mLifecycleOwner.get();
        if (lifecycleOwner == null) {
            return;
        }
        boolean bl = this.mAddingObserverCounter != 0 || this.mHandlingEvent;
        enum_ = this.calculateTargetState((LifecycleObserver)object);
        ++this.mAddingObserverCounter;
        while (observerWithState.mState.compareTo(enum_) < 0 && this.mObserverMap.contains((LifecycleObserver)object)) {
            this.pushParentState(observerWithState.mState);
            enum_ = Lifecycle.Event.upFrom(observerWithState.mState);
            if (enum_ != null) {
                observerWithState.dispatchEvent(lifecycleOwner, (Lifecycle.Event)enum_);
                this.popParentState();
                enum_ = this.calculateTargetState((LifecycleObserver)object);
                continue;
            }
            object = new StringBuilder();
            ((StringBuilder)object).append("no event up from ");
            ((StringBuilder)object).append((Object)observerWithState.mState);
            throw new IllegalStateException(((StringBuilder)object).toString());
        }
        if (!bl) {
            this.sync();
        }
        --this.mAddingObserverCounter;
    }

    @Override
    public Lifecycle.State getCurrentState() {
        return this.mState;
    }

    public int getObserverCount() {
        this.enforceMainThreadIfNeeded("getObserverCount");
        return this.mObserverMap.size();
    }

    public void handleLifecycleEvent(Lifecycle.Event event) {
        this.enforceMainThreadIfNeeded("handleLifecycleEvent");
        this.moveToState(event.getTargetState());
    }

    @Deprecated
    public void markState(Lifecycle.State state) {
        this.enforceMainThreadIfNeeded("markState");
        this.setCurrentState(state);
    }

    @Override
    public void removeObserver(LifecycleObserver lifecycleObserver) {
        this.enforceMainThreadIfNeeded("removeObserver");
        this.mObserverMap.remove(lifecycleObserver);
    }

    public void setCurrentState(Lifecycle.State state) {
        this.enforceMainThreadIfNeeded("setCurrentState");
        this.moveToState(state);
    }

    static class ObserverWithState {
        LifecycleEventObserver mLifecycleObserver;
        Lifecycle.State mState;

        ObserverWithState(LifecycleObserver lifecycleObserver, Lifecycle.State state) {
            this.mLifecycleObserver = Lifecycling.lifecycleEventObserver(lifecycleObserver);
            this.mState = state;
        }

        void dispatchEvent(LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
            Lifecycle.State state = event.getTargetState();
            this.mState = LifecycleRegistry.min(this.mState, state);
            this.mLifecycleObserver.onStateChanged(lifecycleOwner, event);
            this.mState = state;
        }
    }
}

