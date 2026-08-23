/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 *  android.view.View
 *  android.view.ViewGroup
 */
package androidx.fragment.app;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.os.CancellationSignal;
import androidx.core.view.ViewCompat;
import androidx.fragment.R;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStateManager;
import androidx.fragment.app.SpecialEffectsControllerFactory;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

abstract class SpecialEffectsController {
    private final ViewGroup mContainer;
    boolean mIsContainerPostponed = false;
    boolean mOperationDirectionIsPop = false;
    final ArrayList<Operation> mPendingOperations = new ArrayList();
    final ArrayList<Operation> mRunningOperations = new ArrayList();

    SpecialEffectsController(ViewGroup viewGroup) {
        this.mContainer = viewGroup;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void enqueue(Operation.State object, Operation.LifecycleImpact lifecycleImpact, FragmentStateManager fragmentStateManager) {
        ArrayList<Operation> arrayList = this.mPendingOperations;
        synchronized (arrayList) {
            CancellationSignal cancellationSignal = new CancellationSignal();
            Operation operation = this.findPendingOperation(fragmentStateManager.getFragment());
            if (operation != null) {
                operation.mergeWith((Operation.State)((Object)object), lifecycleImpact);
                return;
            }
            operation = new FragmentStateManagerOperation((Operation.State)((Object)object), lifecycleImpact, fragmentStateManager, cancellationSignal);
            this.mPendingOperations.add(operation);
            object = new Runnable(this, (FragmentStateManagerOperation)operation){
                final SpecialEffectsController this$0;
                final FragmentStateManagerOperation val$operation;
                {
                    this.this$0 = specialEffectsController;
                    this.val$operation = fragmentStateManagerOperation;
                }

                @Override
                public void run() {
                    if (this.this$0.mPendingOperations.contains(this.val$operation)) {
                        this.val$operation.getFinalState().applyState(this.val$operation.getFragment().mView);
                    }
                }
            };
            operation.addCompletionListener((Runnable)object);
            object = new Runnable(this, (FragmentStateManagerOperation)operation){
                final SpecialEffectsController this$0;
                final FragmentStateManagerOperation val$operation;
                {
                    this.this$0 = specialEffectsController;
                    this.val$operation = fragmentStateManagerOperation;
                }

                @Override
                public void run() {
                    this.this$0.mPendingOperations.remove(this.val$operation);
                    this.this$0.mRunningOperations.remove(this.val$operation);
                }
            };
            operation.addCompletionListener((Runnable)object);
            return;
        }
    }

    private Operation findPendingOperation(Fragment fragment) {
        for (Operation operation : this.mPendingOperations) {
            if (!operation.getFragment().equals(fragment) || operation.isCanceled()) continue;
            return operation;
        }
        return null;
    }

    private Operation findRunningOperation(Fragment fragment) {
        for (Operation operation : this.mRunningOperations) {
            if (!operation.getFragment().equals(fragment) || operation.isCanceled()) continue;
            return operation;
        }
        return null;
    }

    static SpecialEffectsController getOrCreateController(ViewGroup viewGroup, FragmentManager fragmentManager) {
        return SpecialEffectsController.getOrCreateController(viewGroup, fragmentManager.getSpecialEffectsControllerFactory());
    }

    static SpecialEffectsController getOrCreateController(ViewGroup viewGroup, SpecialEffectsControllerFactory object) {
        Object object2 = viewGroup.getTag(R.id.special_effects_controller_view_tag);
        if (object2 instanceof SpecialEffectsController) {
            return (SpecialEffectsController)object2;
        }
        object = object.createController(viewGroup);
        viewGroup.setTag(R.id.special_effects_controller_view_tag, object);
        return object;
    }

    private void updateFinalState() {
        for (Operation operation : this.mPendingOperations) {
            if (operation.getLifecycleImpact() != Operation.LifecycleImpact.ADDING) continue;
            operation.mergeWith(Operation.State.from(operation.getFragment().requireView().getVisibility()), Operation.LifecycleImpact.NONE);
        }
    }

    void enqueueAdd(Operation.State state, FragmentStateManager fragmentStateManager) {
        if (FragmentManager.isLoggingEnabled(2)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SpecialEffectsController: Enqueuing add operation for fragment ");
            stringBuilder.append(fragmentStateManager.getFragment());
            Log.v((String)"FragmentManager", (String)stringBuilder.toString());
        }
        this.enqueue(state, Operation.LifecycleImpact.ADDING, fragmentStateManager);
    }

    void enqueueHide(FragmentStateManager fragmentStateManager) {
        if (FragmentManager.isLoggingEnabled(2)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SpecialEffectsController: Enqueuing hide operation for fragment ");
            stringBuilder.append(fragmentStateManager.getFragment());
            Log.v((String)"FragmentManager", (String)stringBuilder.toString());
        }
        this.enqueue(Operation.State.GONE, Operation.LifecycleImpact.NONE, fragmentStateManager);
    }

    void enqueueRemove(FragmentStateManager fragmentStateManager) {
        if (FragmentManager.isLoggingEnabled(2)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SpecialEffectsController: Enqueuing remove operation for fragment ");
            stringBuilder.append(fragmentStateManager.getFragment());
            Log.v((String)"FragmentManager", (String)stringBuilder.toString());
        }
        this.enqueue(Operation.State.REMOVED, Operation.LifecycleImpact.REMOVING, fragmentStateManager);
    }

    void enqueueShow(FragmentStateManager fragmentStateManager) {
        if (FragmentManager.isLoggingEnabled(2)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SpecialEffectsController: Enqueuing show operation for fragment ");
            stringBuilder.append(fragmentStateManager.getFragment());
            Log.v((String)"FragmentManager", (String)stringBuilder.toString());
        }
        this.enqueue(Operation.State.VISIBLE, Operation.LifecycleImpact.NONE, fragmentStateManager);
    }

    abstract void executeOperations(List<Operation> var1, boolean var2);

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void executePendingOperations() {
        if (this.mIsContainerPostponed) {
            return;
        }
        if (!ViewCompat.isAttachedToWindow((View)this.mContainer)) {
            this.forceCompleteAllOperations();
            this.mOperationDirectionIsPop = false;
            return;
        }
        ArrayList<Operation> arrayList = this.mPendingOperations;
        synchronized (arrayList) {
            ArrayList<Operation> arrayList2;
            if (this.mPendingOperations.isEmpty()) return;
            Iterator<Operation> iterator2 = new Iterator<Operation>(this.mRunningOperations);
            this.mRunningOperations.clear();
            iterator2 = ((ArrayList)((Object)iterator2)).iterator();
            while (iterator2.hasNext()) {
                arrayList2 = (Operation)iterator2.next();
                if (FragmentManager.isLoggingEnabled(2)) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("SpecialEffectsController: Cancelling operation ");
                    stringBuilder.append(arrayList2);
                    Log.v((String)"FragmentManager", (String)stringBuilder.toString());
                }
                ((Operation)((Object)arrayList2)).cancel();
                if (((Operation)((Object)arrayList2)).isComplete()) continue;
                this.mRunningOperations.add((Operation)((Object)arrayList2));
            }
            this.updateFinalState();
            arrayList2 = new ArrayList<Operation>(this.mPendingOperations);
            this.mPendingOperations.clear();
            this.mRunningOperations.addAll(arrayList2);
            iterator2 = arrayList2.iterator();
            while (true) {
                if (!iterator2.hasNext()) {
                    this.executeOperations(arrayList2, this.mOperationDirectionIsPop);
                    this.mOperationDirectionIsPop = false;
                    return;
                }
                iterator2.next().onStart();
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void forceCompleteAllOperations() {
        boolean bl = ViewCompat.isAttachedToWindow((View)this.mContainer);
        ArrayList<Operation> arrayList = this.mPendingOperations;
        synchronized (arrayList) {
            StringBuilder stringBuilder;
            Object object;
            this.updateFinalState();
            Object object2 = this.mPendingOperations.iterator();
            while (object2.hasNext()) {
                object2.next().onStart();
            }
            object2 = new ArrayList(this.mRunningOperations);
            Object object3 = ((ArrayList)object2).iterator();
            while (object3.hasNext()) {
                object = object3.next();
                if (FragmentManager.isLoggingEnabled(2)) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("SpecialEffectsController: ");
                    if (bl) {
                        object2 = "";
                    } else {
                        object2 = new StringBuilder();
                        ((StringBuilder)object2).append("Container ");
                        ((StringBuilder)object2).append(this.mContainer);
                        ((StringBuilder)object2).append(" is not attached to window. ");
                        object2 = ((StringBuilder)object2).toString();
                    }
                    stringBuilder.append((String)object2);
                    stringBuilder.append("Cancelling running operation ");
                    stringBuilder.append(object);
                    Log.v((String)"FragmentManager", (String)stringBuilder.toString());
                }
                ((Operation)object).cancel();
            }
            object2 = new ArrayList(this.mPendingOperations);
            object = ((ArrayList)object2).iterator();
            while (object.hasNext()) {
                object3 = (Operation)object.next();
                if (FragmentManager.isLoggingEnabled(2)) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("SpecialEffectsController: ");
                    if (bl) {
                        object2 = "";
                    } else {
                        object2 = new StringBuilder();
                        ((StringBuilder)object2).append("Container ");
                        ((StringBuilder)object2).append(this.mContainer);
                        ((StringBuilder)object2).append(" is not attached to window. ");
                        object2 = ((StringBuilder)object2).toString();
                    }
                    stringBuilder.append((String)object2);
                    stringBuilder.append("Cancelling pending operation ");
                    stringBuilder.append(object3);
                    Log.v((String)"FragmentManager", (String)stringBuilder.toString());
                }
                ((Operation)object3).cancel();
            }
            return;
        }
    }

    void forcePostponedExecutePendingOperations() {
        if (this.mIsContainerPostponed) {
            this.mIsContainerPostponed = false;
            this.executePendingOperations();
        }
    }

    Operation.LifecycleImpact getAwaitingCompletionLifecycleImpact(FragmentStateManager object) {
        Operation operation = this.findPendingOperation(((FragmentStateManager)object).getFragment());
        if (operation != null) {
            return operation.getLifecycleImpact();
        }
        if ((object = this.findRunningOperation(((FragmentStateManager)object).getFragment())) != null) {
            return ((Operation)object).getLifecycleImpact();
        }
        return null;
    }

    public ViewGroup getContainer() {
        return this.mContainer;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void markPostponedState() {
        ArrayList<Operation> arrayList = this.mPendingOperations;
        synchronized (arrayList) {
            this.updateFinalState();
            this.mIsContainerPostponed = false;
            for (int i = this.mPendingOperations.size() - 1; i >= 0; --i) {
                Operation operation = this.mPendingOperations.get(i);
                Operation.State state = Operation.State.from(operation.getFragment().mView);
                if (operation.getFinalState() != Operation.State.VISIBLE || state == Operation.State.VISIBLE) continue;
                this.mIsContainerPostponed = operation.getFragment().isPostponed();
                return;
            }
            return;
        }
    }

    void updateOperationDirection(boolean bl) {
        this.mOperationDirectionIsPop = bl;
    }

    private static class FragmentStateManagerOperation
    extends Operation {
        private final FragmentStateManager mFragmentStateManager;

        FragmentStateManagerOperation(Operation.State state, Operation.LifecycleImpact lifecycleImpact, FragmentStateManager fragmentStateManager, CancellationSignal cancellationSignal) {
            super(state, lifecycleImpact, fragmentStateManager.getFragment(), cancellationSignal);
            this.mFragmentStateManager = fragmentStateManager;
        }

        @Override
        public void complete() {
            super.complete();
            this.mFragmentStateManager.moveToExpectedState();
        }

        @Override
        void onStart() {
            if (this.getLifecycleImpact() == Operation.LifecycleImpact.ADDING) {
                Object object;
                Fragment fragment = this.mFragmentStateManager.getFragment();
                View view = fragment.mView.findFocus();
                if (view != null) {
                    fragment.setFocusedView(view);
                    if (FragmentManager.isLoggingEnabled(2)) {
                        object = new StringBuilder();
                        ((StringBuilder)object).append("requestFocus: Saved focused view ");
                        ((StringBuilder)object).append(view);
                        ((StringBuilder)object).append(" for Fragment ");
                        ((StringBuilder)object).append(fragment);
                        Log.v((String)"FragmentManager", (String)((StringBuilder)object).toString());
                    }
                }
                if ((object = this.getFragment().requireView()).getParent() == null) {
                    this.mFragmentStateManager.addViewToContainer();
                    object.setAlpha(0.0f);
                }
                if (object.getAlpha() == 0.0f && object.getVisibility() == 0) {
                    object.setVisibility(4);
                }
                object.setAlpha(fragment.getPostOnViewCreatedAlpha());
            }
        }
    }

    static class Operation {
        private final List<Runnable> mCompletionListeners = new ArrayList<Runnable>();
        private State mFinalState;
        private final Fragment mFragment;
        private boolean mIsCanceled = false;
        private boolean mIsComplete = false;
        private LifecycleImpact mLifecycleImpact;
        private final HashSet<CancellationSignal> mSpecialEffectsSignals = new HashSet();

        Operation(State state, LifecycleImpact lifecycleImpact, Fragment fragment, CancellationSignal cancellationSignal) {
            this.mFinalState = state;
            this.mLifecycleImpact = lifecycleImpact;
            this.mFragment = fragment;
            cancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener(this){
                final Operation this$0;
                {
                    this.this$0 = operation;
                }

                @Override
                public void onCancel() {
                    this.this$0.cancel();
                }
            });
        }

        final void addCompletionListener(Runnable runnable) {
            this.mCompletionListeners.add(runnable);
        }

        final void cancel() {
            if (this.isCanceled()) {
                return;
            }
            this.mIsCanceled = true;
            if (this.mSpecialEffectsSignals.isEmpty()) {
                this.complete();
            } else {
                Iterator<CancellationSignal> iterator2 = new ArrayList<CancellationSignal>(this.mSpecialEffectsSignals).iterator();
                while (iterator2.hasNext()) {
                    iterator2.next().cancel();
                }
            }
        }

        public void complete() {
            Object object;
            if (this.mIsComplete) {
                return;
            }
            if (FragmentManager.isLoggingEnabled(2)) {
                object = new StringBuilder();
                ((StringBuilder)object).append("SpecialEffectsController: ");
                ((StringBuilder)object).append(this);
                ((StringBuilder)object).append(" has called complete.");
                Log.v((String)"FragmentManager", (String)((StringBuilder)object).toString());
            }
            this.mIsComplete = true;
            object = this.mCompletionListeners.iterator();
            while (object.hasNext()) {
                ((Runnable)object.next()).run();
            }
        }

        public final void completeSpecialEffect(CancellationSignal cancellationSignal) {
            if (this.mSpecialEffectsSignals.remove(cancellationSignal) && this.mSpecialEffectsSignals.isEmpty()) {
                this.complete();
            }
        }

        public State getFinalState() {
            return this.mFinalState;
        }

        public final Fragment getFragment() {
            return this.mFragment;
        }

        LifecycleImpact getLifecycleImpact() {
            return this.mLifecycleImpact;
        }

        final boolean isCanceled() {
            return this.mIsCanceled;
        }

        final boolean isComplete() {
            return this.mIsComplete;
        }

        public final void markStartedSpecialEffect(CancellationSignal cancellationSignal) {
            this.onStart();
            this.mSpecialEffectsSignals.add(cancellationSignal);
        }

        final void mergeWith(State object, LifecycleImpact object2) {
            switch (3.$SwitchMap$androidx$fragment$app$SpecialEffectsController$Operation$LifecycleImpact[((Enum)object2).ordinal()]) {
                default: {
                    break;
                }
                case 3: {
                    if (this.mFinalState == State.REMOVED) break;
                    if (FragmentManager.isLoggingEnabled(2)) {
                        object2 = new StringBuilder();
                        ((StringBuilder)object2).append("SpecialEffectsController: For fragment ");
                        ((StringBuilder)object2).append(this.mFragment);
                        ((StringBuilder)object2).append(" mFinalState = ");
                        ((StringBuilder)object2).append((Object)this.mFinalState);
                        ((StringBuilder)object2).append(" -> ");
                        ((StringBuilder)object2).append(object);
                        ((StringBuilder)object2).append(". ");
                        Log.v((String)"FragmentManager", (String)((StringBuilder)object2).toString());
                    }
                    this.mFinalState = object;
                    break;
                }
                case 2: {
                    if (FragmentManager.isLoggingEnabled(2)) {
                        object = new StringBuilder();
                        ((StringBuilder)object).append("SpecialEffectsController: For fragment ");
                        ((StringBuilder)object).append(this.mFragment);
                        ((StringBuilder)object).append(" mFinalState = ");
                        ((StringBuilder)object).append((Object)this.mFinalState);
                        ((StringBuilder)object).append(" -> REMOVED. mLifecycleImpact  = ");
                        ((StringBuilder)object).append((Object)this.mLifecycleImpact);
                        ((StringBuilder)object).append(" to REMOVING.");
                        Log.v((String)"FragmentManager", (String)((StringBuilder)object).toString());
                    }
                    this.mFinalState = State.REMOVED;
                    this.mLifecycleImpact = LifecycleImpact.REMOVING;
                    break;
                }
                case 1: {
                    if (this.mFinalState != State.REMOVED) break;
                    if (FragmentManager.isLoggingEnabled(2)) {
                        object = new StringBuilder();
                        ((StringBuilder)object).append("SpecialEffectsController: For fragment ");
                        ((StringBuilder)object).append(this.mFragment);
                        ((StringBuilder)object).append(" mFinalState = REMOVED -> VISIBLE. mLifecycleImpact = ");
                        ((StringBuilder)object).append((Object)this.mLifecycleImpact);
                        ((StringBuilder)object).append(" to ADDING.");
                        Log.v((String)"FragmentManager", (String)((StringBuilder)object).toString());
                    }
                    this.mFinalState = State.VISIBLE;
                    this.mLifecycleImpact = LifecycleImpact.ADDING;
                }
            }
        }

        void onStart() {
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Operation ");
            stringBuilder.append("{");
            stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
            stringBuilder.append("} ");
            stringBuilder.append("{");
            stringBuilder.append("mFinalState = ");
            stringBuilder.append((Object)this.mFinalState);
            stringBuilder.append("} ");
            stringBuilder.append("{");
            stringBuilder.append("mLifecycleImpact = ");
            stringBuilder.append((Object)this.mLifecycleImpact);
            stringBuilder.append("} ");
            stringBuilder.append("{");
            stringBuilder.append("mFragment = ");
            stringBuilder.append(this.mFragment);
            stringBuilder.append("}");
            return stringBuilder.toString();
        }

        static final class LifecycleImpact
        extends Enum<LifecycleImpact> {
            private static final LifecycleImpact[] $VALUES;
            public static final /* enum */ LifecycleImpact ADDING;
            public static final /* enum */ LifecycleImpact NONE;
            public static final /* enum */ LifecycleImpact REMOVING;

            static {
                LifecycleImpact lifecycleImpact;
                LifecycleImpact lifecycleImpact2;
                LifecycleImpact lifecycleImpact3;
                NONE = lifecycleImpact3 = new LifecycleImpact();
                ADDING = lifecycleImpact2 = new LifecycleImpact();
                REMOVING = lifecycleImpact = new LifecycleImpact();
                $VALUES = new LifecycleImpact[]{lifecycleImpact3, lifecycleImpact2, lifecycleImpact};
            }

            public static LifecycleImpact valueOf(String string2) {
                return Enum.valueOf(LifecycleImpact.class, string2);
            }

            public static LifecycleImpact[] values() {
                return (LifecycleImpact[])$VALUES.clone();
            }
        }

        static final class State
        extends Enum<State> {
            private static final State[] $VALUES;
            public static final /* enum */ State GONE;
            public static final /* enum */ State INVISIBLE;
            public static final /* enum */ State REMOVED;
            public static final /* enum */ State VISIBLE;

            static {
                State state;
                State state2;
                State state3;
                State state4;
                REMOVED = state4 = new State();
                VISIBLE = state3 = new State();
                GONE = state2 = new State();
                INVISIBLE = state = new State();
                $VALUES = new State[]{state4, state3, state2, state};
            }

            static State from(int n) {
                switch (n) {
                    default: {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Unknown visibility ");
                        stringBuilder.append(n);
                        throw new IllegalArgumentException(stringBuilder.toString());
                    }
                    case 8: {
                        return GONE;
                    }
                    case 4: {
                        return INVISIBLE;
                    }
                    case 0: 
                }
                return VISIBLE;
            }

            static State from(View view) {
                if (view.getAlpha() == 0.0f && view.getVisibility() == 0) {
                    return INVISIBLE;
                }
                return State.from(view.getVisibility());
            }

            public static State valueOf(String string2) {
                return Enum.valueOf(State.class, string2);
            }

            public static State[] values() {
                return (State[])$VALUES.clone();
            }

            void applyState(View view) {
                switch (3.$SwitchMap$androidx$fragment$app$SpecialEffectsController$Operation$State[this.ordinal()]) {
                    default: {
                        break;
                    }
                    case 4: {
                        if (FragmentManager.isLoggingEnabled(2)) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("SpecialEffectsController: Setting view ");
                            stringBuilder.append(view);
                            stringBuilder.append(" to INVISIBLE");
                            Log.v((String)"FragmentManager", (String)stringBuilder.toString());
                        }
                        view.setVisibility(4);
                        break;
                    }
                    case 3: {
                        if (FragmentManager.isLoggingEnabled(2)) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("SpecialEffectsController: Setting view ");
                            stringBuilder.append(view);
                            stringBuilder.append(" to GONE");
                            Log.v((String)"FragmentManager", (String)stringBuilder.toString());
                        }
                        view.setVisibility(8);
                        break;
                    }
                    case 2: {
                        if (FragmentManager.isLoggingEnabled(2)) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("SpecialEffectsController: Setting view ");
                            stringBuilder.append(view);
                            stringBuilder.append(" to VISIBLE");
                            Log.v((String)"FragmentManager", (String)stringBuilder.toString());
                        }
                        view.setVisibility(0);
                        break;
                    }
                    case 1: {
                        ViewGroup viewGroup = (ViewGroup)view.getParent();
                        if (viewGroup == null) break;
                        if (FragmentManager.isLoggingEnabled(2)) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("SpecialEffectsController: Removing view ");
                            stringBuilder.append(view);
                            stringBuilder.append(" from container ");
                            stringBuilder.append(viewGroup);
                            Log.v((String)"FragmentManager", (String)stringBuilder.toString());
                        }
                        viewGroup.removeView(view);
                    }
                }
            }
        }
    }
}

