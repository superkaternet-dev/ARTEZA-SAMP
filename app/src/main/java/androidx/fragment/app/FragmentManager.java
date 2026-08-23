/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.Animator$AnimatorListener
 *  android.animation.AnimatorListenerAdapter
 *  android.content.Context
 *  android.content.ContextWrapper
 *  android.content.Intent
 *  android.content.IntentSender
 *  android.content.IntentSender$SendIntentException
 *  android.content.res.Configuration
 *  android.os.Bundle
 *  android.os.Looper
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.util.Log
 *  android.view.LayoutInflater$Factory2
 *  android.view.Menu
 *  android.view.MenuInflater
 *  android.view.MenuItem
 *  android.view.View
 *  android.view.ViewGroup
 */
package androidx.fragment.app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.ActivityResultRegistryOwner;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.collection.ArraySet;
import androidx.core.os.CancellationSignal;
import androidx.fragment.R;
import androidx.fragment.app.BackStackRecord;
import androidx.fragment.app.BackStackState;
import androidx.fragment.app.DefaultSpecialEffectsController;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentAnim;
import androidx.fragment.app.FragmentContainer;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.FragmentHostCallback;
import androidx.fragment.app.FragmentLayoutInflaterFactory;
import androidx.fragment.app.FragmentLifecycleCallbacksDispatcher;
import androidx.fragment.app.FragmentManagerNonConfig;
import androidx.fragment.app.FragmentManagerState;
import androidx.fragment.app.FragmentManagerViewModel;
import androidx.fragment.app.FragmentOnAttachListener;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentResultOwner;
import androidx.fragment.app.FragmentState;
import androidx.fragment.app.FragmentStateManager;
import androidx.fragment.app.FragmentStore;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentTransition;
import androidx.fragment.app.LogWriter;
import androidx.fragment.app.SpecialEffectsController;
import androidx.fragment.app.SpecialEffectsControllerFactory;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class FragmentManager
implements FragmentResultOwner {
    private static boolean DEBUG = false;
    private static final String EXTRA_CREATED_FILLIN_INTENT = "androidx.fragment.extra.ACTIVITY_OPTIONS_BUNDLE";
    public static final int POP_BACK_STACK_INCLUSIVE = 1;
    static final String TAG = "FragmentManager";
    static boolean USE_STATE_MANAGER;
    ArrayList<BackStackRecord> mBackStack;
    private ArrayList<OnBackStackChangedListener> mBackStackChangeListeners;
    private final AtomicInteger mBackStackIndex;
    private FragmentContainer mContainer;
    private ArrayList<Fragment> mCreatedMenus;
    int mCurState = -1;
    private SpecialEffectsControllerFactory mDefaultSpecialEffectsControllerFactory;
    private boolean mDestroyed;
    private Runnable mExecCommit;
    private boolean mExecutingActions;
    private Map<Fragment, HashSet<CancellationSignal>> mExitAnimationCancellationSignals;
    private FragmentFactory mFragmentFactory = null;
    private final FragmentStore mFragmentStore;
    private final FragmentTransition.Callback mFragmentTransitionCallback;
    private boolean mHavePendingDeferredStart;
    private FragmentHostCallback<?> mHost;
    private FragmentFactory mHostFragmentFactory;
    ArrayDeque<LaunchedFragmentInfo> mLaunchedFragments;
    private final FragmentLayoutInflaterFactory mLayoutInflaterFactory;
    private final FragmentLifecycleCallbacksDispatcher mLifecycleCallbacksDispatcher;
    private boolean mNeedMenuInvalidate;
    private FragmentManagerViewModel mNonConfig;
    private final CopyOnWriteArrayList<FragmentOnAttachListener> mOnAttachListeners;
    private final OnBackPressedCallback mOnBackPressedCallback;
    private OnBackPressedDispatcher mOnBackPressedDispatcher;
    private Fragment mParent;
    private final ArrayList<OpGenerator> mPendingActions = new ArrayList();
    private ArrayList<StartEnterTransitionListener> mPostponedTransactions;
    Fragment mPrimaryNav;
    private ActivityResultLauncher<String[]> mRequestPermissions;
    private final Map<String, LifecycleAwareResultListener> mResultListeners;
    private final Map<String, Bundle> mResults;
    private SpecialEffectsControllerFactory mSpecialEffectsControllerFactory = null;
    private ActivityResultLauncher<Intent> mStartActivityForResult;
    private ActivityResultLauncher<IntentSenderRequest> mStartIntentSenderForResult;
    private boolean mStateSaved;
    private boolean mStopped;
    private ArrayList<Fragment> mTmpAddedFragments;
    private ArrayList<Boolean> mTmpIsPop;
    private ArrayList<BackStackRecord> mTmpRecords;

    static {
        DEBUG = false;
        USE_STATE_MANAGER = true;
    }

    public FragmentManager() {
        this.mFragmentStore = new FragmentStore();
        this.mLayoutInflaterFactory = new FragmentLayoutInflaterFactory(this);
        this.mOnBackPressedCallback = new OnBackPressedCallback(this, false){
            final FragmentManager this$0;
            {
                this.this$0 = fragmentManager;
                super(bl);
            }

            @Override
            public void handleOnBackPressed() {
                this.this$0.handleOnBackPressed();
            }
        };
        this.mBackStackIndex = new AtomicInteger();
        this.mResults = Collections.synchronizedMap(new HashMap());
        this.mResultListeners = Collections.synchronizedMap(new HashMap());
        this.mExitAnimationCancellationSignals = Collections.synchronizedMap(new HashMap());
        this.mFragmentTransitionCallback = new FragmentTransition.Callback(this){
            final FragmentManager this$0;
            {
                this.this$0 = fragmentManager;
            }

            @Override
            public void onComplete(Fragment fragment, CancellationSignal cancellationSignal) {
                if (!cancellationSignal.isCanceled()) {
                    this.this$0.removeCancellationSignal(fragment, cancellationSignal);
                }
            }

            @Override
            public void onStart(Fragment fragment, CancellationSignal cancellationSignal) {
                this.this$0.addCancellationSignal(fragment, cancellationSignal);
            }
        };
        this.mLifecycleCallbacksDispatcher = new FragmentLifecycleCallbacksDispatcher(this);
        this.mOnAttachListeners = new CopyOnWriteArrayList();
        this.mHostFragmentFactory = new FragmentFactory(this){
            final FragmentManager this$0;
            {
                this.this$0 = fragmentManager;
            }

            @Override
            public Fragment instantiate(ClassLoader classLoader, String string2) {
                return this.this$0.getHost().instantiate(this.this$0.getHost().getContext(), string2, null);
            }
        };
        this.mDefaultSpecialEffectsControllerFactory = new SpecialEffectsControllerFactory(this){
            final FragmentManager this$0;
            {
                this.this$0 = fragmentManager;
            }

            @Override
            public SpecialEffectsController createController(ViewGroup viewGroup) {
                return new DefaultSpecialEffectsController(viewGroup);
            }
        };
        this.mLaunchedFragments = new ArrayDeque();
        this.mExecCommit = new Runnable(this){
            final FragmentManager this$0;
            {
                this.this$0 = fragmentManager;
            }

            @Override
            public void run() {
                this.this$0.execPendingActions(true);
            }
        };
    }

    private void addAddedFragments(ArraySet<Fragment> arraySet) {
        int n = this.mCurState;
        if (n < 1) {
            return;
        }
        n = Math.min(n, 5);
        for (Fragment fragment : this.mFragmentStore.getFragments()) {
            if (fragment.mState >= n) continue;
            this.moveToState(fragment, n);
            if (fragment.mView == null || fragment.mHidden || !fragment.mIsNewlyAdded) continue;
            arraySet.add(fragment);
        }
    }

    private void cancelExitAnimation(Fragment fragment) {
        HashSet<CancellationSignal> hashSet = this.mExitAnimationCancellationSignals.get(fragment);
        if (hashSet != null) {
            Iterator<CancellationSignal> iterator2 = hashSet.iterator();
            while (iterator2.hasNext()) {
                iterator2.next().cancel();
            }
            hashSet.clear();
            this.destroyFragmentView(fragment);
            this.mExitAnimationCancellationSignals.remove(fragment);
        }
    }

    private void checkStateLoss() {
        if (!this.isStateSaved()) {
            return;
        }
        throw new IllegalStateException("Can not perform this action after onSaveInstanceState");
    }

    private void cleanupExec() {
        this.mExecutingActions = false;
        this.mTmpIsPop.clear();
        this.mTmpRecords.clear();
    }

    private Set<SpecialEffectsController> collectAllSpecialEffectsController() {
        HashSet<SpecialEffectsController> hashSet = new HashSet<SpecialEffectsController>();
        Iterator<FragmentStateManager> iterator2 = this.mFragmentStore.getActiveFragmentStateManagers().iterator();
        while (iterator2.hasNext()) {
            ViewGroup viewGroup = iterator2.next().getFragment().mContainer;
            if (viewGroup == null) continue;
            hashSet.add(SpecialEffectsController.getOrCreateController(viewGroup, this.getSpecialEffectsControllerFactory()));
        }
        return hashSet;
    }

    private Set<SpecialEffectsController> collectChangedControllers(ArrayList<BackStackRecord> arrayList, int n, int n2) {
        HashSet<SpecialEffectsController> hashSet = new HashSet<SpecialEffectsController>();
        while (n < n2) {
            Iterator iterator2 = arrayList.get((int)n).mOps.iterator();
            while (iterator2.hasNext()) {
                Fragment fragment = ((FragmentTransaction.Op)iterator2.next()).mFragment;
                if (fragment == null || (fragment = fragment.mContainer) == null) continue;
                hashSet.add(SpecialEffectsController.getOrCreateController((ViewGroup)fragment, this));
            }
            ++n;
        }
        return hashSet;
    }

    private void completeShowHideFragment(Fragment fragment) {
        if (fragment.mView != null) {
            FragmentAnim.AnimationOrAnimator animationOrAnimator = FragmentAnim.loadAnimation(this.mHost.getContext(), fragment, fragment.mHidden ^ true);
            if (animationOrAnimator != null && animationOrAnimator.animator != null) {
                animationOrAnimator.animator.setTarget((Object)fragment.mView);
                if (fragment.mHidden) {
                    if (fragment.isHideReplaced()) {
                        fragment.setHideReplaced(false);
                    } else {
                        ViewGroup viewGroup = fragment.mContainer;
                        View view = fragment.mView;
                        viewGroup.startViewTransition(view);
                        animationOrAnimator.animator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter(this, viewGroup, view, fragment){
                            final FragmentManager this$0;
                            final View val$animatingView;
                            final ViewGroup val$container;
                            final Fragment val$fragment;
                            {
                                this.this$0 = fragmentManager;
                                this.val$container = viewGroup;
                                this.val$animatingView = view;
                                this.val$fragment = fragment;
                            }

                            public void onAnimationEnd(Animator animator2) {
                                this.val$container.endViewTransition(this.val$animatingView);
                                animator2.removeListener((Animator.AnimatorListener)this);
                                if (this.val$fragment.mView != null && this.val$fragment.mHidden) {
                                    this.val$fragment.mView.setVisibility(8);
                                }
                            }
                        });
                    }
                } else {
                    fragment.mView.setVisibility(0);
                }
                animationOrAnimator.animator.start();
            } else {
                if (animationOrAnimator != null) {
                    fragment.mView.startAnimation(animationOrAnimator.animation);
                    animationOrAnimator.animation.start();
                }
                int n = fragment.mHidden && !fragment.isHideReplaced() ? 8 : 0;
                fragment.mView.setVisibility(n);
                if (fragment.isHideReplaced()) {
                    fragment.setHideReplaced(false);
                }
            }
        }
        this.invalidateMenuForFragment(fragment);
        fragment.mHiddenChanged = false;
        fragment.onHiddenChanged(fragment.mHidden);
    }

    private void destroyFragmentView(Fragment fragment) {
        fragment.performDestroyView();
        this.mLifecycleCallbacksDispatcher.dispatchOnFragmentViewDestroyed(fragment, false);
        fragment.mContainer = null;
        fragment.mView = null;
        fragment.mViewLifecycleOwner = null;
        fragment.mViewLifecycleOwnerLiveData.setValue(null);
        fragment.mInLayout = false;
    }

    private void dispatchParentPrimaryNavigationFragmentChanged(Fragment fragment) {
        if (fragment != null && fragment.equals(this.findActiveFragment(fragment.mWho))) {
            fragment.performPrimaryNavigationFragmentChanged();
        }
    }

    private void dispatchStateChange(int n) {
        try {
            this.mExecutingActions = true;
            this.mFragmentStore.dispatchStateChange(n);
            this.moveToState(n, false);
            if (USE_STATE_MANAGER) {
                Iterator<SpecialEffectsController> iterator2 = this.collectAllSpecialEffectsController().iterator();
                while (iterator2.hasNext()) {
                    iterator2.next().forceCompleteAllOperations();
                }
            }
            this.mExecutingActions = false;
            this.execPendingActions(true);
        }
        catch (Throwable throwable) {
            this.mExecutingActions = false;
            throw throwable;
        }
    }

    private void doPendingDeferredStart() {
        if (this.mHavePendingDeferredStart) {
            this.mHavePendingDeferredStart = false;
            this.startPendingDeferredFragments();
        }
    }

    @Deprecated
    public static void enableDebugLogging(boolean bl) {
        DEBUG = bl;
    }

    public static void enableNewStateManager(boolean bl) {
        USE_STATE_MANAGER = bl;
    }

    private void endAnimatingAwayFragments() {
        block3: {
            block2: {
                if (!USE_STATE_MANAGER) break block2;
                Iterator<SpecialEffectsController> iterator2 = this.collectAllSpecialEffectsController().iterator();
                while (iterator2.hasNext()) {
                    iterator2.next().forceCompleteAllOperations();
                }
                break block3;
            }
            if (this.mExitAnimationCancellationSignals.isEmpty()) break block3;
            for (Fragment fragment : this.mExitAnimationCancellationSignals.keySet()) {
                this.cancelExitAnimation(fragment);
                this.moveToState(fragment);
            }
        }
    }

    private void ensureExecReady(boolean bl) {
        if (!this.mExecutingActions) {
            if (this.mHost == null) {
                if (this.mDestroyed) {
                    throw new IllegalStateException("FragmentManager has been destroyed");
                }
                throw new IllegalStateException("FragmentManager has not been attached to a host.");
            }
            if (Looper.myLooper() == this.mHost.getHandler().getLooper()) {
                if (!bl) {
                    this.checkStateLoss();
                }
                if (this.mTmpRecords == null) {
                    this.mTmpRecords = new ArrayList();
                    this.mTmpIsPop = new ArrayList();
                }
                this.mExecutingActions = true;
                try {
                    this.executePostponedTransaction(null, null);
                    return;
                }
                finally {
                    this.mExecutingActions = false;
                }
            }
            throw new IllegalStateException("Must be called from main thread of fragment host");
        }
        throw new IllegalStateException("FragmentManager is already executing transactions");
    }

    private static void executeOps(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2, int n, int n2) {
        while (n < n2) {
            BackStackRecord backStackRecord = arrayList.get(n);
            boolean bl = arrayList2.get(n);
            boolean bl2 = true;
            if (bl) {
                backStackRecord.bumpBackStackNesting(-1);
                if (n != n2 - 1) {
                    bl2 = false;
                }
                backStackRecord.executePopOps(bl2);
            } else {
                backStackRecord.bumpBackStackNesting(1);
                backStackRecord.executeOps();
            }
            ++n;
        }
    }

    /*
     * WARNING - void declaration
     */
    private void executeOpsTogether(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2, int n, int n2) {
        int n3;
        Object object;
        boolean bl = arrayList.get((int)n).mReorderingAllowed;
        ArrayList<Fragment> object22 = this.mTmpAddedFragments;
        if (object22 == null) {
            this.mTmpAddedFragments = new ArrayList();
        } else {
            object22.clear();
        }
        this.mTmpAddedFragments.addAll(this.mFragmentStore.getFragments());
        Fragment fragment = this.getPrimaryNavigationFragment();
        int n4 = n;
        int n5 = 0;
        while (true) {
            void var10_8;
            int n6 = 1;
            if (n4 >= n2) break;
            object = arrayList.get(n4);
            if (!arrayList2.get(n4).booleanValue()) {
                Fragment fragment2 = ((BackStackRecord)object).expandOps(this.mTmpAddedFragments, (Fragment)var10_8);
            } else {
                Fragment fragment3 = ((BackStackRecord)object).trackAddedFragmentsInPop(this.mTmpAddedFragments, (Fragment)var10_8);
            }
            n3 = n6;
            if (n5 == 0) {
                n3 = ((BackStackRecord)object).mAddToBackStack ? n6 : 0;
            }
            ++n4;
            n5 = n3;
        }
        this.mTmpAddedFragments.clear();
        if (!bl && this.mCurState >= 1) {
            if (USE_STATE_MANAGER) {
                for (n3 = n; n3 < n2; ++n3) {
                    Iterator iterator2 = arrayList.get((int)n3).mOps.iterator();
                    while (iterator2.hasNext()) {
                        object = ((FragmentTransaction.Op)iterator2.next()).mFragment;
                        if (object == null || ((Fragment)object).mFragmentManager == null) continue;
                        object = this.createOrGetFragmentStateManager((Fragment)object);
                        this.mFragmentStore.makeActive((FragmentStateManager)object);
                    }
                }
            } else {
                FragmentTransition.startTransitions(this.mHost.getContext(), this.mContainer, arrayList, arrayList2, n, n2, false, this.mFragmentTransitionCallback);
            }
        }
        FragmentManager.executeOps(arrayList, arrayList2, n, n2);
        if (USE_STATE_MANAGER) {
            bl = arrayList2.get(n2 - 1);
            for (n3 = n; n3 < n2; ++n3) {
                object = arrayList.get(n3);
                if (bl) {
                    for (n4 = ((BackStackRecord)object).mOps.size() - 1; n4 >= 0; --n4) {
                        Fragment fragment4 = ((FragmentTransaction.Op)((BackStackRecord)object).mOps.get((int)n4)).mFragment;
                        if (fragment4 == null) continue;
                        this.createOrGetFragmentStateManager(fragment4).moveToExpectedState();
                    }
                    continue;
                }
                Iterator iterator3 = ((BackStackRecord)object).mOps.iterator();
                while (iterator3.hasNext()) {
                    object = ((FragmentTransaction.Op)iterator3.next()).mFragment;
                    if (object == null) continue;
                    this.createOrGetFragmentStateManager((Fragment)object).moveToExpectedState();
                }
            }
            this.moveToState(this.mCurState, true);
            for (SpecialEffectsController specialEffectsController : this.collectChangedControllers(arrayList, n, n2)) {
                specialEffectsController.updateOperationDirection(bl);
                specialEffectsController.markPostponedState();
                specialEffectsController.executePendingOperations();
            }
            n3 = n2;
            ArrayList<Boolean> arrayList3 = arrayList2;
        } else {
            if (bl) {
                ArraySet<Fragment> arraySet = new ArraySet<Fragment>();
                this.addAddedFragments(arraySet);
                n3 = this.postponePostponableTransactions(arrayList, arrayList2, n, n2, arraySet);
                this.makeRemovedFragmentsInvisible(arraySet);
            } else {
                n3 = n2;
            }
            if (n3 != n && bl) {
                if (this.mCurState >= 1) {
                    FragmentTransition.startTransitions(this.mHost.getContext(), this.mContainer, arrayList, arrayList2, n, n3, true, this.mFragmentTransitionCallback);
                }
                n3 = n2;
                ArrayList<Boolean> arrayList4 = arrayList2;
                this.moveToState(this.mCurState, true);
            } else {
                ArrayList<Boolean> arrayList5 = arrayList2;
                n3 = n2;
            }
        }
        while (n < n2) {
            BackStackRecord backStackRecord = arrayList.get(n);
            if (arrayList2.get(n).booleanValue() && backStackRecord.mIndex >= 0) {
                backStackRecord.mIndex = -1;
            }
            backStackRecord.runOnCommitRunnables();
            ++n;
        }
        if (n5 != 0) {
            this.reportBackStackChanged();
        }
    }

    private void executePostponedTransaction(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2) {
        ArrayList<StartEnterTransitionListener> arrayList3 = this.mPostponedTransactions;
        int n = arrayList3 == null ? 0 : arrayList3.size();
        int n2 = 0;
        int n3 = n;
        while (n2 < n3) {
            int n4;
            block7: {
                block8: {
                    block6: {
                        arrayList3 = this.mPostponedTransactions.get(n2);
                        if (arrayList == null || ((StartEnterTransitionListener)((Object)arrayList3)).mIsBack || (n = arrayList.indexOf(((StartEnterTransitionListener)((Object)arrayList3)).mRecord)) == -1 || arrayList2 == null || !arrayList2.get(n).booleanValue()) break block6;
                        this.mPostponedTransactions.remove(n2);
                        n4 = n2 - 1;
                        n = n3 - 1;
                        ((StartEnterTransitionListener)((Object)arrayList3)).cancelTransaction();
                        break block7;
                    }
                    if (((StartEnterTransitionListener)((Object)arrayList3)).isReady()) break block8;
                    n = n3;
                    n4 = n2;
                    if (arrayList == null) break block7;
                    n = n3;
                    n4 = n2;
                    if (!((StartEnterTransitionListener)((Object)arrayList3)).mRecord.interactsWith(arrayList, 0, arrayList.size())) break block7;
                }
                this.mPostponedTransactions.remove(n2);
                n4 = n2 - 1;
                n = n3 - 1;
                if (arrayList != null && !((StartEnterTransitionListener)((Object)arrayList3)).mIsBack && (n2 = arrayList.indexOf(((StartEnterTransitionListener)((Object)arrayList3)).mRecord)) != -1 && arrayList2 != null && arrayList2.get(n2).booleanValue()) {
                    ((StartEnterTransitionListener)((Object)arrayList3)).cancelTransaction();
                } else {
                    ((StartEnterTransitionListener)((Object)arrayList3)).completeTransaction();
                }
            }
            n2 = n4 + 1;
            n3 = n;
        }
    }

    public static <F extends Fragment> F findFragment(View view) {
        Object object = FragmentManager.findViewFragment(view);
        if (object != null) {
            return (F)object;
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("View ");
        ((StringBuilder)object).append(view);
        ((StringBuilder)object).append(" does not have a Fragment set");
        throw new IllegalStateException(((StringBuilder)object).toString());
    }

    static FragmentManager findFragmentManager(View object) {
        Object object2;
        block8: {
            block7: {
                Fragment fragment;
                block5: {
                    block6: {
                        fragment = FragmentManager.findViewFragment(object);
                        if (fragment == null) break block5;
                        if (!fragment.isAdded()) break block6;
                        object = fragment.getChildFragmentManager();
                        break block7;
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("The Fragment ");
                    stringBuilder.append(fragment);
                    stringBuilder.append(" that owns View ");
                    stringBuilder.append(object);
                    stringBuilder.append(" has already been destroyed. Nested fragments should always use the child FragmentManager.");
                    throw new IllegalStateException(stringBuilder.toString());
                }
                fragment = object.getContext();
                FragmentActivity fragmentActivity = null;
                while (true) {
                    object2 = fragmentActivity;
                    if (!(fragment instanceof ContextWrapper)) break;
                    if (fragment instanceof FragmentActivity) {
                        object2 = (FragmentActivity)((Object)fragment);
                        break;
                    }
                    fragment = ((ContextWrapper)fragment).getBaseContext();
                }
                if (object2 == null) break block8;
                object = ((FragmentActivity)object2).getSupportFragmentManager();
            }
            return object;
        }
        object2 = new StringBuilder();
        ((StringBuilder)object2).append("View ");
        ((StringBuilder)object2).append(object);
        ((StringBuilder)object2).append(" is not within a subclass of FragmentActivity.");
        object = new IllegalStateException(((StringBuilder)object2).toString());
        throw object;
    }

    private static Fragment findViewFragment(View view) {
        while (true) {
            Object var1_1 = null;
            if (view == null) break;
            Fragment fragment = FragmentManager.getViewFragment(view);
            if (fragment != null) {
                return fragment;
            }
            fragment = view.getParent();
            view = var1_1;
            if (!(fragment instanceof View)) continue;
            view = (View)fragment;
        }
        return null;
    }

    private void forcePostponedTransactions() {
        block3: {
            block2: {
                if (!USE_STATE_MANAGER) break block2;
                Iterator<SpecialEffectsController> iterator2 = this.collectAllSpecialEffectsController().iterator();
                while (iterator2.hasNext()) {
                    iterator2.next().forcePostponedExecutePendingOperations();
                }
                break block3;
            }
            if (this.mPostponedTransactions == null) break block3;
            while (!this.mPostponedTransactions.isEmpty()) {
                this.mPostponedTransactions.remove(0).completeTransaction();
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private boolean generateOpsForPendingActions(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2) {
        boolean bl = false;
        ArrayList<OpGenerator> arrayList3 = this.mPendingActions;
        synchronized (arrayList3) {
            if (this.mPendingActions.isEmpty()) {
                return false;
            }
            int n = this.mPendingActions.size();
            for (int i = 0; i < n; bl |= this.mPendingActions.get(i).generateOps(arrayList, arrayList2), ++i) {
            }
            this.mPendingActions.clear();
            this.mHost.getHandler().removeCallbacks(this.mExecCommit);
            return bl;
        }
    }

    private FragmentManagerViewModel getChildNonConfig(Fragment fragment) {
        return this.mNonConfig.getChildNonConfig(fragment);
    }

    private ViewGroup getFragmentContainer(Fragment fragment) {
        if (fragment.mContainer != null) {
            return fragment.mContainer;
        }
        if (fragment.mContainerId <= 0) {
            return null;
        }
        if (this.mContainer.onHasView() && (fragment = this.mContainer.onFindViewById(fragment.mContainerId)) instanceof ViewGroup) {
            return (ViewGroup)fragment;
        }
        return null;
    }

    static Fragment getViewFragment(View object) {
        if ((object = object.getTag(R.id.fragment_container_view_tag)) instanceof Fragment) {
            return (Fragment)object;
        }
        return null;
    }

    static boolean isLoggingEnabled(int n) {
        boolean bl = DEBUG || Log.isLoggable((String)TAG, (int)n);
        return bl;
    }

    private boolean isMenuAvailable(Fragment fragment) {
        boolean bl = fragment.mHasMenu && fragment.mMenuVisible || fragment.mChildFragmentManager.checkForMenus();
        return bl;
    }

    private void makeRemovedFragmentsInvisible(ArraySet<Fragment> arraySet) {
        int n = arraySet.size();
        for (int i = 0; i < n; ++i) {
            Fragment fragment = arraySet.valueAt(i);
            if (fragment.mAdded) continue;
            View view = fragment.requireView();
            fragment.mPostponedAlpha = view.getAlpha();
            view.setAlpha(0.0f);
        }
    }

    private boolean popBackStackImmediate(String string2, int n, int n2) {
        this.execPendingActions(false);
        this.ensureExecReady(true);
        Fragment fragment = this.mPrimaryNav;
        if (fragment != null && n < 0 && string2 == null && fragment.getChildFragmentManager().popBackStackImmediate()) {
            return true;
        }
        boolean bl = this.popBackStackState(this.mTmpRecords, this.mTmpIsPop, string2, n, n2);
        if (bl) {
            this.mExecutingActions = true;
            try {
                this.removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
            }
            finally {
                this.cleanupExec();
            }
        }
        this.updateOnBackPressedCallbackEnabled();
        this.doPendingDeferredStart();
        this.mFragmentStore.burpActive();
        return bl;
    }

    private int postponePostponableTransactions(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2, int n, int n2, ArraySet<Fragment> arraySet) {
        int n3 = n2;
        for (int i = n2 - 1; i >= n; --i) {
            BackStackRecord backStackRecord = arrayList.get(i);
            boolean bl = arrayList2.get(i);
            boolean bl2 = backStackRecord.isPostponed() && !backStackRecord.interactsWith(arrayList, i + 1, n2);
            int n4 = n3;
            if (bl2) {
                if (this.mPostponedTransactions == null) {
                    this.mPostponedTransactions = new ArrayList();
                }
                StartEnterTransitionListener startEnterTransitionListener = new StartEnterTransitionListener(backStackRecord, bl);
                this.mPostponedTransactions.add(startEnterTransitionListener);
                backStackRecord.setOnStartPostponedListener(startEnterTransitionListener);
                if (bl) {
                    backStackRecord.executeOps();
                } else {
                    backStackRecord.executePopOps(false);
                }
                n4 = n3 - 1;
                if (i != n4) {
                    arrayList.remove(i);
                    arrayList.add(n4, backStackRecord);
                }
                this.addAddedFragments(arraySet);
            }
            n3 = n4;
        }
        return n3;
    }

    private void removeRedundantOperationsAndExecute(ArrayList<BackStackRecord> serializable, ArrayList<Boolean> arrayList) {
        if (((ArrayList)serializable).isEmpty()) {
            return;
        }
        if (((ArrayList)serializable).size() == arrayList.size()) {
            this.executePostponedTransaction((ArrayList<BackStackRecord>)serializable, arrayList);
            int n = ((ArrayList)serializable).size();
            int n2 = 0;
            int n3 = 0;
            while (n3 < n) {
                int n4 = n2;
                int n5 = n3;
                if (!((BackStackRecord)((ArrayList)serializable).get((int)n3)).mReorderingAllowed) {
                    if (n2 != n3) {
                        this.executeOpsTogether((ArrayList<BackStackRecord>)serializable, arrayList, n2, n3);
                    }
                    n2 = n4 = n3 + 1;
                    if (arrayList.get(n3).booleanValue()) {
                        while (true) {
                            n2 = n4;
                            if (n4 >= n) break;
                            n2 = n4;
                            if (!arrayList.get(n4).booleanValue()) break;
                            n2 = n4;
                            if (((BackStackRecord)((ArrayList)serializable).get((int)n4)).mReorderingAllowed) break;
                            ++n4;
                        }
                    }
                    this.executeOpsTogether((ArrayList<BackStackRecord>)serializable, arrayList, n3, n2);
                    n4 = n2;
                    n5 = n2 - 1;
                }
                n3 = n5 + 1;
                n2 = n4;
            }
            if (n2 != n) {
                this.executeOpsTogether((ArrayList<BackStackRecord>)serializable, arrayList, n2, n);
            }
            return;
        }
        serializable = new IllegalStateException("Internal error with the back stack records");
        throw serializable;
    }

    private void reportBackStackChanged() {
        if (this.mBackStackChangeListeners != null) {
            for (int i = 0; i < this.mBackStackChangeListeners.size(); ++i) {
                this.mBackStackChangeListeners.get(i).onBackStackChanged();
            }
        }
    }

    static int reverseTransit(int n) {
        int n2 = 0;
        switch (n) {
            default: {
                n = n2;
                break;
            }
            case 8194: {
                n = 4097;
                break;
            }
            case 4099: {
                n = 4099;
                break;
            }
            case 4097: {
                n = 8194;
            }
        }
        return n;
    }

    private void setVisibleRemovingFragment(Fragment fragment) {
        ViewGroup viewGroup = this.getFragmentContainer(fragment);
        if (viewGroup != null && fragment.getNextAnim() > 0) {
            if (viewGroup.getTag(R.id.visible_removing_fragment_view_tag) == null) {
                viewGroup.setTag(R.id.visible_removing_fragment_view_tag, (Object)fragment);
            }
            ((Fragment)viewGroup.getTag(R.id.visible_removing_fragment_view_tag)).setNextAnim(fragment.getNextAnim());
        }
    }

    private void startPendingDeferredFragments() {
        Iterator<FragmentStateManager> iterator2 = this.mFragmentStore.getActiveFragmentStateManagers().iterator();
        while (iterator2.hasNext()) {
            this.performPendingDeferredStart(iterator2.next());
        }
    }

    private void throwException(RuntimeException runtimeException) {
        Log.e((String)TAG, (String)runtimeException.getMessage());
        Log.e((String)TAG, (String)"Activity state:");
        PrintWriter printWriter = new PrintWriter(new LogWriter(TAG));
        FragmentHostCallback<?> fragmentHostCallback = this.mHost;
        if (fragmentHostCallback != null) {
            try {
                fragmentHostCallback.onDump("  ", null, printWriter, new String[0]);
            }
            catch (Exception exception) {
                Log.e((String)TAG, (String)"Failed dumping state", (Throwable)exception);
            }
        } else {
            try {
                this.dump("  ", null, printWriter, new String[0]);
            }
            catch (Exception exception) {
                Log.e((String)TAG, (String)"Failed dumping state", (Throwable)exception);
            }
        }
        throw runtimeException;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void updateOnBackPressedCallbackEnabled() {
        Object object = this.mPendingActions;
        synchronized (object) {
            boolean bl = this.mPendingActions.isEmpty();
            boolean bl2 = true;
            if (!bl) {
                this.mOnBackPressedCallback.setEnabled(true);
                return;
            }
            object = this.mOnBackPressedCallback;
            if (this.getBackStackEntryCount() <= 0 || !this.isPrimaryNavigation(this.mParent)) {
                bl2 = false;
            }
            ((OnBackPressedCallback)object).setEnabled(bl2);
            return;
        }
    }

    void addBackStackState(BackStackRecord backStackRecord) {
        if (this.mBackStack == null) {
            this.mBackStack = new ArrayList();
        }
        this.mBackStack.add(backStackRecord);
    }

    void addCancellationSignal(Fragment fragment, CancellationSignal cancellationSignal) {
        if (this.mExitAnimationCancellationSignals.get(fragment) == null) {
            this.mExitAnimationCancellationSignals.put(fragment, new HashSet());
        }
        this.mExitAnimationCancellationSignals.get(fragment).add(cancellationSignal);
    }

    FragmentStateManager addFragment(Fragment fragment) {
        Object object;
        if (FragmentManager.isLoggingEnabled(2)) {
            object = new StringBuilder();
            ((StringBuilder)object).append("add: ");
            ((StringBuilder)object).append(fragment);
            Log.v((String)TAG, (String)((StringBuilder)object).toString());
        }
        object = this.createOrGetFragmentStateManager(fragment);
        fragment.mFragmentManager = this;
        this.mFragmentStore.makeActive((FragmentStateManager)object);
        if (!fragment.mDetached) {
            this.mFragmentStore.addFragment(fragment);
            fragment.mRemoving = false;
            if (fragment.mView == null) {
                fragment.mHiddenChanged = false;
            }
            if (this.isMenuAvailable(fragment)) {
                this.mNeedMenuInvalidate = true;
            }
        }
        return object;
    }

    public void addFragmentOnAttachListener(FragmentOnAttachListener fragmentOnAttachListener) {
        this.mOnAttachListeners.add(fragmentOnAttachListener);
    }

    public void addOnBackStackChangedListener(OnBackStackChangedListener onBackStackChangedListener) {
        if (this.mBackStackChangeListeners == null) {
            this.mBackStackChangeListeners = new ArrayList();
        }
        this.mBackStackChangeListeners.add(onBackStackChangedListener);
    }

    void addRetainedFragment(Fragment fragment) {
        this.mNonConfig.addRetainedFragment(fragment);
    }

    int allocBackStackIndex() {
        return this.mBackStackIndex.getAndIncrement();
    }

    void attachController(FragmentHostCallback<?> object, FragmentContainer object2, Fragment object3) {
        if (this.mHost == null) {
            this.mHost = object;
            this.mContainer = object2;
            this.mParent = object3;
            if (object3 != null) {
                this.addFragmentOnAttachListener(new FragmentOnAttachListener(this, (Fragment)object3){
                    final FragmentManager this$0;
                    final Fragment val$parent;
                    {
                        this.this$0 = fragmentManager;
                        this.val$parent = fragment;
                    }

                    @Override
                    public void onAttachFragment(FragmentManager fragmentManager, Fragment fragment) {
                        this.val$parent.onAttachFragment(fragment);
                    }
                });
            } else if (object instanceof FragmentOnAttachListener) {
                this.addFragmentOnAttachListener((FragmentOnAttachListener)object);
            }
            if (this.mParent != null) {
                this.updateOnBackPressedCallbackEnabled();
            }
            if (object instanceof OnBackPressedDispatcherOwner) {
                OnBackPressedDispatcher onBackPressedDispatcher;
                object2 = (OnBackPressedDispatcherOwner)object;
                this.mOnBackPressedDispatcher = onBackPressedDispatcher = object2.getOnBackPressedDispatcher();
                if (object3 != null) {
                    object2 = object3;
                }
                onBackPressedDispatcher.addCallback((LifecycleOwner)object2, this.mOnBackPressedCallback);
            }
            this.mNonConfig = object3 != null ? ((Fragment)object3).mFragmentManager.getChildNonConfig((Fragment)object3) : (object instanceof ViewModelStoreOwner ? FragmentManagerViewModel.getInstance(((ViewModelStoreOwner)object).getViewModelStore()) : new FragmentManagerViewModel(false));
            this.mNonConfig.setIsStateSaved(this.isStateSaved());
            this.mFragmentStore.setNonConfig(this.mNonConfig);
            object = this.mHost;
            if (object instanceof ActivityResultRegistryOwner) {
                object2 = ((ActivityResultRegistryOwner)object).getActivityResultRegistry();
                if (object3 != null) {
                    object = new StringBuilder();
                    ((StringBuilder)object).append(((Fragment)object3).mWho);
                    ((StringBuilder)object).append(":");
                    object = ((StringBuilder)object).toString();
                } else {
                    object = "";
                }
                object3 = new StringBuilder();
                ((StringBuilder)object3).append("FragmentManager:");
                ((StringBuilder)object3).append((String)object);
                object = ((StringBuilder)object3).toString();
                object3 = new StringBuilder();
                ((StringBuilder)object3).append((String)object);
                ((StringBuilder)object3).append("StartActivityForResult");
                this.mStartActivityForResult = ((ActivityResultRegistry)object2).register(((StringBuilder)object3).toString(), new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>(this){
                    final FragmentManager this$0;
                    {
                        this.this$0 = fragmentManager;
                    }

                    @Override
                    public void onActivityResult(ActivityResult object) {
                        Object object2 = this.this$0.mLaunchedFragments.pollFirst();
                        if (object2 == null) {
                            object = new StringBuilder();
                            ((StringBuilder)object).append("No Activities were started for result for ");
                            ((StringBuilder)object).append(this);
                            Log.w((String)FragmentManager.TAG, (String)((StringBuilder)object).toString());
                            return;
                        }
                        String string2 = ((LaunchedFragmentInfo)object2).mWho;
                        int n = ((LaunchedFragmentInfo)object2).mRequestCode;
                        object2 = this.this$0.mFragmentStore.findFragmentByWho(string2);
                        if (object2 == null) {
                            object = new StringBuilder();
                            ((StringBuilder)object).append("Activity result delivered for unknown Fragment ");
                            ((StringBuilder)object).append(string2);
                            Log.w((String)FragmentManager.TAG, (String)((StringBuilder)object).toString());
                            return;
                        }
                        ((Fragment)object2).onActivityResult(n, ((ActivityResult)object).getResultCode(), ((ActivityResult)object).getData());
                    }
                });
                object3 = new StringBuilder();
                ((StringBuilder)object3).append((String)object);
                ((StringBuilder)object3).append("StartIntentSenderForResult");
                this.mStartIntentSenderForResult = ((ActivityResultRegistry)object2).register(((StringBuilder)object3).toString(), new FragmentIntentSenderContract(), new ActivityResultCallback<ActivityResult>(this){
                    final FragmentManager this$0;
                    {
                        this.this$0 = fragmentManager;
                    }

                    @Override
                    public void onActivityResult(ActivityResult object) {
                        Object object2 = this.this$0.mLaunchedFragments.pollFirst();
                        if (object2 == null) {
                            object = new StringBuilder();
                            ((StringBuilder)object).append("No IntentSenders were started for ");
                            ((StringBuilder)object).append(this);
                            Log.w((String)FragmentManager.TAG, (String)((StringBuilder)object).toString());
                            return;
                        }
                        String string2 = ((LaunchedFragmentInfo)object2).mWho;
                        int n = ((LaunchedFragmentInfo)object2).mRequestCode;
                        object2 = this.this$0.mFragmentStore.findFragmentByWho(string2);
                        if (object2 == null) {
                            object = new StringBuilder();
                            ((StringBuilder)object).append("Intent Sender result delivered for unknown Fragment ");
                            ((StringBuilder)object).append(string2);
                            Log.w((String)FragmentManager.TAG, (String)((StringBuilder)object).toString());
                            return;
                        }
                        ((Fragment)object2).onActivityResult(n, ((ActivityResult)object).getResultCode(), ((ActivityResult)object).getData());
                    }
                });
                object3 = new StringBuilder();
                ((StringBuilder)object3).append((String)object);
                ((StringBuilder)object3).append("RequestPermissions");
                this.mRequestPermissions = ((ActivityResultRegistry)object2).register(((StringBuilder)object3).toString(), new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>(this){
                    final FragmentManager this$0;
                    {
                        this.this$0 = fragmentManager;
                    }

                    @Override
                    public void onActivityResult(Map<String, Boolean> object) {
                        int n;
                        Object object2 = object.keySet().toArray(new String[0]);
                        object = new ArrayList<Boolean>(object.values());
                        int[] nArray = new int[((ArrayList)object).size()];
                        for (n = 0; n < ((ArrayList)object).size(); ++n) {
                            int n2 = (Boolean)((ArrayList)object).get(n) != false ? 0 : -1;
                            nArray[n] = n2;
                        }
                        Object object3 = this.this$0.mLaunchedFragments.pollFirst();
                        if (object3 == null) {
                            object = new StringBuilder();
                            ((StringBuilder)object).append("No permissions were requested for ");
                            ((StringBuilder)object).append(this);
                            Log.w((String)FragmentManager.TAG, (String)((StringBuilder)object).toString());
                            return;
                        }
                        object = ((LaunchedFragmentInfo)object3).mWho;
                        n = ((LaunchedFragmentInfo)object3).mRequestCode;
                        object3 = this.this$0.mFragmentStore.findFragmentByWho((String)object);
                        if (object3 == null) {
                            object2 = new StringBuilder();
                            ((StringBuilder)object2).append("Permission request result delivered for unknown Fragment ");
                            ((StringBuilder)object2).append((String)object);
                            Log.w((String)FragmentManager.TAG, (String)((StringBuilder)object2).toString());
                            return;
                        }
                        ((Fragment)object3).onRequestPermissionsResult(n, (String[])object2, nArray);
                    }
                });
            }
            return;
        }
        throw new IllegalStateException("Already attached");
    }

    void attachFragment(Fragment fragment) {
        StringBuilder stringBuilder;
        if (FragmentManager.isLoggingEnabled(2)) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("attach: ");
            stringBuilder.append(fragment);
            Log.v((String)TAG, (String)stringBuilder.toString());
        }
        if (fragment.mDetached) {
            fragment.mDetached = false;
            if (!fragment.mAdded) {
                this.mFragmentStore.addFragment(fragment);
                if (FragmentManager.isLoggingEnabled(2)) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("add from attach: ");
                    stringBuilder.append(fragment);
                    Log.v((String)TAG, (String)stringBuilder.toString());
                }
                if (this.isMenuAvailable(fragment)) {
                    this.mNeedMenuInvalidate = true;
                }
            }
        }
    }

    public FragmentTransaction beginTransaction() {
        return new BackStackRecord(this);
    }

    boolean checkForMenus() {
        boolean bl = false;
        for (Fragment fragment : this.mFragmentStore.getActiveFragments()) {
            if (fragment != null) {
                bl = this.isMenuAvailable(fragment);
            }
            if (!bl) continue;
            return true;
        }
        return false;
    }

    @Override
    public final void clearFragmentResult(String string2) {
        this.mResults.remove(string2);
    }

    @Override
    public final void clearFragmentResultListener(String object) {
        if ((object = this.mResultListeners.remove(object)) != null) {
            ((LifecycleAwareResultListener)object).removeObserver();
        }
    }

    void completeExecute(BackStackRecord backStackRecord, boolean bl, boolean bl2, boolean bl3) {
        if (bl) {
            backStackRecord.executePopOps(bl3);
        } else {
            backStackRecord.executeOps();
        }
        ArrayList<BackStackRecord> object2 = new ArrayList<BackStackRecord>(1);
        ArrayList<Boolean> arrayList = new ArrayList<Boolean>(1);
        object2.add(backStackRecord);
        arrayList.add(bl);
        if (bl2 && this.mCurState >= 1) {
            FragmentTransition.startTransitions(this.mHost.getContext(), this.mContainer, object2, arrayList, 0, 1, true, this.mFragmentTransitionCallback);
        }
        if (bl3) {
            this.moveToState(this.mCurState, true);
        }
        for (Fragment fragment : this.mFragmentStore.getActiveFragments()) {
            if (fragment == null || fragment.mView == null || !fragment.mIsNewlyAdded || !backStackRecord.interactsWith(fragment.mContainerId)) continue;
            if (fragment.mPostponedAlpha > 0.0f) {
                fragment.mView.setAlpha(fragment.mPostponedAlpha);
            }
            if (bl3) {
                fragment.mPostponedAlpha = 0.0f;
                continue;
            }
            fragment.mPostponedAlpha = -1.0f;
            fragment.mIsNewlyAdded = false;
        }
    }

    FragmentStateManager createOrGetFragmentStateManager(Fragment object) {
        FragmentStateManager fragmentStateManager = this.mFragmentStore.getFragmentStateManager(((Fragment)object).mWho);
        if (fragmentStateManager != null) {
            return fragmentStateManager;
        }
        object = new FragmentStateManager(this.mLifecycleCallbacksDispatcher, this.mFragmentStore, (Fragment)object);
        ((FragmentStateManager)object).restoreState(this.mHost.getContext().getClassLoader());
        ((FragmentStateManager)object).setFragmentManagerState(this.mCurState);
        return object;
    }

    void detachFragment(Fragment fragment) {
        StringBuilder stringBuilder;
        if (FragmentManager.isLoggingEnabled(2)) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("detach: ");
            stringBuilder.append(fragment);
            Log.v((String)TAG, (String)stringBuilder.toString());
        }
        if (!fragment.mDetached) {
            fragment.mDetached = true;
            if (fragment.mAdded) {
                if (FragmentManager.isLoggingEnabled(2)) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("remove from detach: ");
                    stringBuilder.append(fragment);
                    Log.v((String)TAG, (String)stringBuilder.toString());
                }
                this.mFragmentStore.removeFragment(fragment);
                if (this.isMenuAvailable(fragment)) {
                    this.mNeedMenuInvalidate = true;
                }
                this.setVisibleRemovingFragment(fragment);
            }
        }
    }

    void dispatchActivityCreated() {
        this.mStateSaved = false;
        this.mStopped = false;
        this.mNonConfig.setIsStateSaved(false);
        this.dispatchStateChange(4);
    }

    void dispatchAttach() {
        this.mStateSaved = false;
        this.mStopped = false;
        this.mNonConfig.setIsStateSaved(false);
        this.dispatchStateChange(0);
    }

    void dispatchConfigurationChanged(Configuration configuration) {
        for (Fragment fragment : this.mFragmentStore.getFragments()) {
            if (fragment == null) continue;
            fragment.performConfigurationChanged(configuration);
        }
    }

    boolean dispatchContextItemSelected(MenuItem menuItem) {
        if (this.mCurState < 1) {
            return false;
        }
        for (Fragment fragment : this.mFragmentStore.getFragments()) {
            if (fragment == null || !fragment.performContextItemSelected(menuItem)) continue;
            return true;
        }
        return false;
    }

    void dispatchCreate() {
        this.mStateSaved = false;
        this.mStopped = false;
        this.mNonConfig.setIsStateSaved(false);
        this.dispatchStateChange(1);
    }

    boolean dispatchCreateOptionsMenu(Menu object, MenuInflater menuInflater) {
        if (this.mCurState < 1) {
            return false;
        }
        boolean bl = false;
        ArrayList<Fragment> arrayList = null;
        for (Fragment fragment : this.mFragmentStore.getFragments()) {
            boolean bl2 = bl;
            ArrayList<Fragment> arrayList2 = arrayList;
            if (fragment != null) {
                bl2 = bl;
                arrayList2 = arrayList;
                if (this.isParentMenuVisible(fragment)) {
                    bl2 = bl;
                    arrayList2 = arrayList;
                    if (fragment.performCreateOptionsMenu((Menu)object, menuInflater)) {
                        bl2 = true;
                        arrayList2 = arrayList;
                        if (arrayList == null) {
                            arrayList2 = new ArrayList<Fragment>();
                        }
                        arrayList2.add(fragment);
                    }
                }
            }
            bl = bl2;
            arrayList = arrayList2;
        }
        if (this.mCreatedMenus != null) {
            for (int i = 0; i < this.mCreatedMenus.size(); ++i) {
                object = this.mCreatedMenus.get(i);
                if (arrayList != null && arrayList.contains(object)) continue;
                ((Fragment)object).onDestroyOptionsMenu();
            }
        }
        this.mCreatedMenus = arrayList;
        return bl;
    }

    void dispatchDestroy() {
        ActivityResultLauncher<Intent> activityResultLauncher;
        this.mDestroyed = true;
        this.execPendingActions(true);
        this.endAnimatingAwayFragments();
        this.dispatchStateChange(-1);
        this.mHost = null;
        this.mContainer = null;
        this.mParent = null;
        if (this.mOnBackPressedDispatcher != null) {
            this.mOnBackPressedCallback.remove();
            this.mOnBackPressedDispatcher = null;
        }
        if ((activityResultLauncher = this.mStartActivityForResult) != null) {
            activityResultLauncher.unregister();
            this.mStartIntentSenderForResult.unregister();
            this.mRequestPermissions.unregister();
        }
    }

    void dispatchDestroyView() {
        this.dispatchStateChange(1);
    }

    void dispatchLowMemory() {
        for (Fragment fragment : this.mFragmentStore.getFragments()) {
            if (fragment == null) continue;
            fragment.performLowMemory();
        }
    }

    void dispatchMultiWindowModeChanged(boolean bl) {
        for (Fragment fragment : this.mFragmentStore.getFragments()) {
            if (fragment == null) continue;
            fragment.performMultiWindowModeChanged(bl);
        }
    }

    void dispatchOnAttachFragment(Fragment fragment) {
        Iterator<FragmentOnAttachListener> iterator2 = this.mOnAttachListeners.iterator();
        while (iterator2.hasNext()) {
            iterator2.next().onAttachFragment(this, fragment);
        }
    }

    boolean dispatchOptionsItemSelected(MenuItem menuItem) {
        if (this.mCurState < 1) {
            return false;
        }
        for (Fragment fragment : this.mFragmentStore.getFragments()) {
            if (fragment == null || !fragment.performOptionsItemSelected(menuItem)) continue;
            return true;
        }
        return false;
    }

    void dispatchOptionsMenuClosed(Menu menu) {
        if (this.mCurState < 1) {
            return;
        }
        for (Fragment fragment : this.mFragmentStore.getFragments()) {
            if (fragment == null) continue;
            fragment.performOptionsMenuClosed(menu);
        }
    }

    void dispatchPause() {
        this.dispatchStateChange(5);
    }

    void dispatchPictureInPictureModeChanged(boolean bl) {
        for (Fragment fragment : this.mFragmentStore.getFragments()) {
            if (fragment == null) continue;
            fragment.performPictureInPictureModeChanged(bl);
        }
    }

    boolean dispatchPrepareOptionsMenu(Menu menu) {
        if (this.mCurState < 1) {
            return false;
        }
        boolean bl = false;
        for (Fragment fragment : this.mFragmentStore.getFragments()) {
            boolean bl2 = bl;
            if (fragment != null) {
                bl2 = bl;
                if (this.isParentMenuVisible(fragment)) {
                    bl2 = bl;
                    if (fragment.performPrepareOptionsMenu(menu)) {
                        bl2 = true;
                    }
                }
            }
            bl = bl2;
        }
        return bl;
    }

    void dispatchPrimaryNavigationFragmentChanged() {
        this.updateOnBackPressedCallbackEnabled();
        this.dispatchParentPrimaryNavigationFragmentChanged(this.mPrimaryNav);
    }

    void dispatchResume() {
        this.mStateSaved = false;
        this.mStopped = false;
        this.mNonConfig.setIsStateSaved(false);
        this.dispatchStateChange(7);
    }

    void dispatchStart() {
        this.mStateSaved = false;
        this.mStopped = false;
        this.mNonConfig.setIsStateSaved(false);
        this.dispatchStateChange(5);
    }

    void dispatchStop() {
        this.mStopped = true;
        this.mNonConfig.setIsStateSaved(true);
        this.dispatchStateChange(4);
    }

    void dispatchViewCreated() {
        this.dispatchStateChange(2);
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void dump(String string2, FileDescriptor arrayList, PrintWriter printWriter, String[] object) {
        int n;
        int n2;
        CharSequence charSequence = new StringBuilder();
        charSequence.append(string2);
        charSequence.append("    ");
        charSequence = charSequence.toString();
        this.mFragmentStore.dump(string2, (FileDescriptor)((Object)arrayList), printWriter, (String[])object);
        arrayList = this.mCreatedMenus;
        if (arrayList != null && (n2 = arrayList.size()) > 0) {
            printWriter.print(string2);
            printWriter.println("Fragments Created Menus:");
            for (n = 0; n < n2; ++n) {
                arrayList = this.mCreatedMenus.get(n);
                printWriter.print(string2);
                printWriter.print("  #");
                printWriter.print(n);
                printWriter.print(": ");
                printWriter.println(((Fragment)((Object)arrayList)).toString());
            }
        }
        if ((arrayList = this.mBackStack) != null && (n2 = arrayList.size()) > 0) {
            printWriter.print(string2);
            printWriter.println("Back Stack:");
            for (n = 0; n < n2; ++n) {
                arrayList = this.mBackStack.get(n);
                printWriter.print(string2);
                printWriter.print("  #");
                printWriter.print(n);
                printWriter.print(": ");
                printWriter.println(((BackStackRecord)((Object)arrayList)).toString());
                ((BackStackRecord)((Object)arrayList)).dump((String)charSequence, printWriter);
            }
        }
        printWriter.print(string2);
        arrayList = new StringBuilder();
        ((StringBuilder)((Object)arrayList)).append("Back Stack Index: ");
        ((StringBuilder)((Object)arrayList)).append(this.mBackStackIndex.get());
        printWriter.println(((StringBuilder)((Object)arrayList)).toString());
        arrayList = this.mPendingActions;
        synchronized (arrayList) {
            n2 = this.mPendingActions.size();
            if (n2 > 0) {
                printWriter.print(string2);
                printWriter.println("Pending Actions:");
                for (n = 0; n < n2; ++n) {
                    object = this.mPendingActions.get(n);
                    printWriter.print(string2);
                    printWriter.print("  #");
                    printWriter.print(n);
                    printWriter.print(": ");
                    printWriter.println(object);
                }
            }
            {
                printWriter.print(string2);
                printWriter.println("FragmentManager misc state:");
                printWriter.print(string2);
                printWriter.print("  mHost=");
                printWriter.println(this.mHost);
                printWriter.print(string2);
                printWriter.print("  mContainer=");
                printWriter.println(this.mContainer);
                if (this.mParent != null) {
                    printWriter.print(string2);
                    printWriter.print("  mParent=");
                    printWriter.println(this.mParent);
                }
                printWriter.print(string2);
                printWriter.print("  mCurState=");
                printWriter.print(this.mCurState);
                printWriter.print(" mStateSaved=");
                printWriter.print(this.mStateSaved);
                printWriter.print(" mStopped=");
                printWriter.print(this.mStopped);
                printWriter.print(" mDestroyed=");
                printWriter.println(this.mDestroyed);
                if (!this.mNeedMenuInvalidate) return;
                printWriter.print(string2);
                printWriter.print("  mNeedMenuInvalidate=");
                printWriter.println(this.mNeedMenuInvalidate);
                return;
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void enqueueAction(OpGenerator object, boolean bl) {
        if (!bl) {
            if (this.mHost == null) {
                if (this.mDestroyed) {
                    throw new IllegalStateException("FragmentManager has been destroyed");
                }
                throw new IllegalStateException("FragmentManager has not been attached to a host.");
            }
            this.checkStateLoss();
        }
        ArrayList<OpGenerator> arrayList = this.mPendingActions;
        synchronized (arrayList) {
            if (this.mHost != null) {
                this.mPendingActions.add((OpGenerator)object);
                this.scheduleCommit();
                return;
            }
            if (bl) {
                return;
            }
            object = new IllegalStateException("Activity has been destroyed");
            throw object;
        }
    }

    boolean execPendingActions(boolean bl) {
        this.ensureExecReady(bl);
        bl = false;
        while (this.generateOpsForPendingActions(this.mTmpRecords, this.mTmpIsPop)) {
            this.mExecutingActions = true;
            try {
                this.removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
                bl = true;
            }
            finally {
                this.cleanupExec();
            }
        }
        this.updateOnBackPressedCallbackEnabled();
        this.doPendingDeferredStart();
        this.mFragmentStore.burpActive();
        return bl;
    }

    void execSingleAction(OpGenerator opGenerator, boolean bl) {
        if (bl && (this.mHost == null || this.mDestroyed)) {
            return;
        }
        this.ensureExecReady(bl);
        if (opGenerator.generateOps(this.mTmpRecords, this.mTmpIsPop)) {
            this.mExecutingActions = true;
            try {
                this.removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
            }
            finally {
                this.cleanupExec();
            }
        }
        this.updateOnBackPressedCallbackEnabled();
        this.doPendingDeferredStart();
        this.mFragmentStore.burpActive();
    }

    public boolean executePendingTransactions() {
        boolean bl = this.execPendingActions(true);
        this.forcePostponedTransactions();
        return bl;
    }

    Fragment findActiveFragment(String string2) {
        return this.mFragmentStore.findActiveFragment(string2);
    }

    public Fragment findFragmentById(int n) {
        return this.mFragmentStore.findFragmentById(n);
    }

    public Fragment findFragmentByTag(String string2) {
        return this.mFragmentStore.findFragmentByTag(string2);
    }

    Fragment findFragmentByWho(String string2) {
        return this.mFragmentStore.findFragmentByWho(string2);
    }

    int getActiveFragmentCount() {
        return this.mFragmentStore.getActiveFragmentCount();
    }

    List<Fragment> getActiveFragments() {
        return this.mFragmentStore.getActiveFragments();
    }

    public BackStackEntry getBackStackEntryAt(int n) {
        return this.mBackStack.get(n);
    }

    public int getBackStackEntryCount() {
        ArrayList<BackStackRecord> arrayList = this.mBackStack;
        int n = arrayList != null ? arrayList.size() : 0;
        return n;
    }

    FragmentContainer getContainer() {
        return this.mContainer;
    }

    public Fragment getFragment(Bundle object, String string2) {
        String string3 = object.getString(string2);
        if (string3 == null) {
            return null;
        }
        object = this.findActiveFragment(string3);
        if (object == null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Fragment no longer exists for key ");
            stringBuilder.append(string2);
            stringBuilder.append(": unique id ");
            stringBuilder.append(string3);
            this.throwException(new IllegalStateException(stringBuilder.toString()));
        }
        return object;
    }

    public FragmentFactory getFragmentFactory() {
        Object object = this.mFragmentFactory;
        if (object != null) {
            return object;
        }
        object = this.mParent;
        if (object != null) {
            return ((Fragment)object).mFragmentManager.getFragmentFactory();
        }
        return this.mHostFragmentFactory;
    }

    FragmentStore getFragmentStore() {
        return this.mFragmentStore;
    }

    public List<Fragment> getFragments() {
        return this.mFragmentStore.getFragments();
    }

    FragmentHostCallback<?> getHost() {
        return this.mHost;
    }

    LayoutInflater.Factory2 getLayoutInflaterFactory() {
        return this.mLayoutInflaterFactory;
    }

    FragmentLifecycleCallbacksDispatcher getLifecycleCallbacksDispatcher() {
        return this.mLifecycleCallbacksDispatcher;
    }

    Fragment getParent() {
        return this.mParent;
    }

    public Fragment getPrimaryNavigationFragment() {
        return this.mPrimaryNav;
    }

    SpecialEffectsControllerFactory getSpecialEffectsControllerFactory() {
        Object object = this.mSpecialEffectsControllerFactory;
        if (object != null) {
            return object;
        }
        object = this.mParent;
        if (object != null) {
            return ((Fragment)object).mFragmentManager.getSpecialEffectsControllerFactory();
        }
        return this.mDefaultSpecialEffectsControllerFactory;
    }

    ViewModelStore getViewModelStore(Fragment fragment) {
        return this.mNonConfig.getViewModelStore(fragment);
    }

    void handleOnBackPressed() {
        this.execPendingActions(true);
        if (this.mOnBackPressedCallback.isEnabled()) {
            this.popBackStackImmediate();
        } else {
            this.mOnBackPressedDispatcher.onBackPressed();
        }
    }

    void hideFragment(Fragment fragment) {
        if (FragmentManager.isLoggingEnabled(2)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("hide: ");
            stringBuilder.append(fragment);
            Log.v((String)TAG, (String)stringBuilder.toString());
        }
        if (!fragment.mHidden) {
            fragment.mHidden = true;
            fragment.mHiddenChanged = true ^ fragment.mHiddenChanged;
            this.setVisibleRemovingFragment(fragment);
        }
    }

    void invalidateMenuForFragment(Fragment fragment) {
        if (fragment.mAdded && this.isMenuAvailable(fragment)) {
            this.mNeedMenuInvalidate = true;
        }
    }

    public boolean isDestroyed() {
        return this.mDestroyed;
    }

    boolean isParentMenuVisible(Fragment fragment) {
        if (fragment == null) {
            return true;
        }
        return fragment.isMenuVisible();
    }

    boolean isPrimaryNavigation(Fragment fragment) {
        boolean bl = true;
        if (fragment == null) {
            return true;
        }
        FragmentManager fragmentManager = fragment.mFragmentManager;
        if (!fragment.equals(fragmentManager.getPrimaryNavigationFragment()) || !this.isPrimaryNavigation(fragmentManager.mParent)) {
            bl = false;
        }
        return bl;
    }

    boolean isStateAtLeast(int n) {
        boolean bl = this.mCurState >= n;
        return bl;
    }

    public boolean isStateSaved() {
        boolean bl = this.mStateSaved || this.mStopped;
        return bl;
    }

    void launchRequestPermissions(Fragment object, String[] stringArray, int n) {
        if (this.mRequestPermissions != null) {
            object = new LaunchedFragmentInfo(((Fragment)object).mWho, n);
            this.mLaunchedFragments.addLast((LaunchedFragmentInfo)object);
            this.mRequestPermissions.launch(stringArray);
        } else {
            this.mHost.onRequestPermissionsFromFragment((Fragment)object, stringArray, n);
        }
    }

    void launchStartActivityForResult(Fragment object, Intent intent, int n, Bundle bundle) {
        if (this.mStartActivityForResult != null) {
            object = new LaunchedFragmentInfo(((Fragment)object).mWho, n);
            this.mLaunchedFragments.addLast((LaunchedFragmentInfo)object);
            if (intent != null && bundle != null) {
                intent.putExtra("androidx.activity.result.contract.extra.ACTIVITY_OPTIONS_BUNDLE", bundle);
            }
            this.mStartActivityForResult.launch(intent);
        } else {
            this.mHost.onStartActivityFromFragment((Fragment)object, intent, n, bundle);
        }
    }

    void launchStartIntentSenderForResult(Fragment fragment, IntentSender object, int n, Intent object2, int n2, int n3, int n4, Bundle bundle) throws IntentSender.SendIntentException {
        if (this.mStartIntentSenderForResult != null) {
            if (bundle != null) {
                if (object2 == null) {
                    object2 = new Intent();
                    object2.putExtra(EXTRA_CREATED_FILLIN_INTENT, true);
                }
                if (FragmentManager.isLoggingEnabled(2)) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("ActivityOptions ");
                    stringBuilder.append(bundle);
                    stringBuilder.append(" were added to fillInIntent ");
                    stringBuilder.append(object2);
                    stringBuilder.append(" for fragment ");
                    stringBuilder.append(fragment);
                    Log.v((String)TAG, (String)stringBuilder.toString());
                }
                object2.putExtra("androidx.activity.result.contract.extra.ACTIVITY_OPTIONS_BUNDLE", bundle);
            }
            object = new IntentSenderRequest.Builder((IntentSender)object).setFillInIntent((Intent)object2).setFlags(n3, n2).build();
            object2 = new LaunchedFragmentInfo(fragment.mWho, n);
            this.mLaunchedFragments.addLast((LaunchedFragmentInfo)object2);
            if (FragmentManager.isLoggingEnabled(2)) {
                object2 = new StringBuilder();
                ((StringBuilder)object2).append("Fragment ");
                ((StringBuilder)object2).append(fragment);
                ((StringBuilder)object2).append("is launching an IntentSender for result ");
                Log.v((String)TAG, (String)((StringBuilder)object2).toString());
            }
            this.mStartIntentSenderForResult.launch((IntentSenderRequest)object);
        } else {
            this.mHost.onStartIntentSenderFromFragment(fragment, (IntentSender)object, n, (Intent)object2, n2, n3, n4, bundle);
        }
    }

    void moveFragmentToExpectedState(Fragment fragment) {
        if (!this.mFragmentStore.containsActiveFragment(fragment.mWho)) {
            if (FragmentManager.isLoggingEnabled(3)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Ignoring moving ");
                stringBuilder.append(fragment);
                stringBuilder.append(" to state ");
                stringBuilder.append(this.mCurState);
                stringBuilder.append("since it is not added to ");
                stringBuilder.append(this);
                Log.d((String)TAG, (String)stringBuilder.toString());
            }
            return;
        }
        this.moveToState(fragment);
        if (fragment.mView != null && fragment.mIsNewlyAdded && fragment.mContainer != null) {
            if (fragment.mPostponedAlpha > 0.0f) {
                fragment.mView.setAlpha(fragment.mPostponedAlpha);
            }
            fragment.mPostponedAlpha = 0.0f;
            fragment.mIsNewlyAdded = false;
            FragmentAnim.AnimationOrAnimator animationOrAnimator = FragmentAnim.loadAnimation(this.mHost.getContext(), fragment, true);
            if (animationOrAnimator != null) {
                if (animationOrAnimator.animation != null) {
                    fragment.mView.startAnimation(animationOrAnimator.animation);
                } else {
                    animationOrAnimator.animator.setTarget((Object)fragment.mView);
                    animationOrAnimator.animator.start();
                }
            }
        }
        if (fragment.mHiddenChanged) {
            this.completeShowHideFragment(fragment);
        }
    }

    void moveToState(int n, boolean bl) {
        Object object;
        if (this.mHost == null && n != -1) {
            throw new IllegalStateException("No activity");
        }
        if (!bl && n == this.mCurState) {
            return;
        }
        this.mCurState = n;
        if (USE_STATE_MANAGER) {
            this.mFragmentStore.moveToExpectedState();
        } else {
            object = this.mFragmentStore.getFragments().iterator();
            while (object.hasNext()) {
                this.moveFragmentToExpectedState(object.next());
            }
            for (FragmentStateManager fragmentStateManager : this.mFragmentStore.getActiveFragmentStateManagers()) {
                object = fragmentStateManager.getFragment();
                if (!((Fragment)object).mIsNewlyAdded) {
                    this.moveFragmentToExpectedState((Fragment)object);
                }
                if ((n = ((Fragment)object).mRemoving && !((Fragment)object).isInBackStack() ? 1 : 0) == 0) continue;
                this.mFragmentStore.makeInactive(fragmentStateManager);
            }
        }
        this.startPendingDeferredFragments();
        if (this.mNeedMenuInvalidate && (object = this.mHost) != null && this.mCurState == 7) {
            ((FragmentHostCallback)object).onSupportInvalidateOptionsMenu();
            this.mNeedMenuInvalidate = false;
        }
    }

    void moveToState(Fragment fragment) {
        this.moveToState(fragment, this.mCurState);
    }

    void moveToState(Fragment fragment, int n) {
        int n2;
        Object object;
        Object object2 = object = this.mFragmentStore.getFragmentStateManager(fragment.mWho);
        if (object == null) {
            object2 = new FragmentStateManager(this.mLifecycleCallbacksDispatcher, this.mFragmentStore, fragment);
            ((FragmentStateManager)object2).setFragmentManagerState(1);
        }
        int n3 = n;
        if (fragment.mFromLayout) {
            n3 = n;
            if (fragment.mInLayout) {
                n3 = n;
                if (fragment.mState == 2) {
                    n3 = Math.max(n, 2);
                }
            }
        }
        if (fragment.mState <= (n2 = Math.min(n3, ((FragmentStateManager)object2).computeExpectedState()))) {
            if (fragment.mState < n2 && !this.mExitAnimationCancellationSignals.isEmpty()) {
                this.cancelExitAnimation(fragment);
            }
            switch (fragment.mState) {
                default: {
                    break;
                }
                case -1: {
                    if (n2 > -1) {
                        ((FragmentStateManager)object2).attach();
                    }
                }
                case 0: {
                    if (n2 > 0) {
                        ((FragmentStateManager)object2).create();
                    }
                }
                case 1: {
                    if (n2 > -1) {
                        ((FragmentStateManager)object2).ensureInflatedView();
                    }
                    if (n2 > 1) {
                        ((FragmentStateManager)object2).createView();
                    }
                }
                case 2: {
                    if (n2 > 2) {
                        ((FragmentStateManager)object2).activityCreated();
                    }
                }
                case 4: {
                    if (n2 > 4) {
                        ((FragmentStateManager)object2).start();
                    }
                }
                case 5: {
                    if (n2 <= 5) break;
                    ((FragmentStateManager)object2).resume();
                }
            }
            n3 = n2;
        } else {
            n3 = n2;
            if (fragment.mState > n2) {
                n = n2;
                switch (fragment.mState) {
                    default: {
                        n3 = n2;
                        break;
                    }
                    case 7: {
                        if (n2 < 7) {
                            ((FragmentStateManager)object2).pause();
                        }
                    }
                    case 5: {
                        if (n2 < 5) {
                            ((FragmentStateManager)object2).stop();
                        }
                    }
                    case 4: {
                        if (n2 < 4) {
                            if (FragmentManager.isLoggingEnabled(3)) {
                                object = new StringBuilder();
                                ((StringBuilder)object).append("movefrom ACTIVITY_CREATED: ");
                                ((StringBuilder)object).append(fragment);
                                Log.d((String)TAG, (String)((StringBuilder)object).toString());
                            }
                            if (fragment.mView != null && this.mHost.onShouldSaveFragmentState(fragment) && fragment.mSavedViewState == null) {
                                ((FragmentStateManager)object2).saveViewState();
                            }
                        }
                    }
                    case 2: {
                        if (n2 < 2) {
                            ViewGroup viewGroup = null;
                            if (fragment.mView != null && fragment.mContainer != null) {
                                fragment.mContainer.endViewTransition(fragment.mView);
                                fragment.mView.clearAnimation();
                                if (!fragment.isRemovingParent()) {
                                    object = viewGroup;
                                    if (this.mCurState > -1) {
                                        object = viewGroup;
                                        if (!this.mDestroyed) {
                                            object = viewGroup;
                                            if (fragment.mView.getVisibility() == 0) {
                                                object = viewGroup;
                                                if (fragment.mPostponedAlpha >= 0.0f) {
                                                    object = FragmentAnim.loadAnimation(this.mHost.getContext(), fragment, false);
                                                }
                                            }
                                        }
                                    }
                                    fragment.mPostponedAlpha = 0.0f;
                                    viewGroup = fragment.mContainer;
                                    View view = fragment.mView;
                                    if (object != null) {
                                        FragmentAnim.animateRemoveFragment(fragment, (FragmentAnim.AnimationOrAnimator)object, this.mFragmentTransitionCallback);
                                    }
                                    viewGroup.removeView(view);
                                    if (FragmentManager.isLoggingEnabled(2)) {
                                        object = new StringBuilder();
                                        ((StringBuilder)object).append("Removing view ");
                                        ((StringBuilder)object).append(view);
                                        ((StringBuilder)object).append(" for fragment ");
                                        ((StringBuilder)object).append(fragment);
                                        ((StringBuilder)object).append(" from container ");
                                        ((StringBuilder)object).append(viewGroup);
                                        Log.v((String)TAG, (String)((StringBuilder)object).toString());
                                    }
                                    if (viewGroup != fragment.mContainer) {
                                        return;
                                    }
                                }
                            }
                            if (this.mExitAnimationCancellationSignals.get(fragment) == null) {
                                ((FragmentStateManager)object2).destroyFragmentView();
                            }
                        }
                    }
                    case 1: {
                        n = n2;
                        if (n2 < 1) {
                            if (this.mExitAnimationCancellationSignals.get(fragment) != null) {
                                n = 1;
                            } else {
                                ((FragmentStateManager)object2).destroy();
                                n = n2;
                            }
                        }
                    }
                    case 0: {
                        n3 = n;
                        if (n >= 0) break;
                        ((FragmentStateManager)object2).detach();
                        n3 = n;
                    }
                }
            }
        }
        if (fragment.mState != n3) {
            if (FragmentManager.isLoggingEnabled(3)) {
                object2 = new StringBuilder();
                ((StringBuilder)object2).append("moveToState: Fragment state for ");
                ((StringBuilder)object2).append(fragment);
                ((StringBuilder)object2).append(" not updated inline; expected state ");
                ((StringBuilder)object2).append(n3);
                ((StringBuilder)object2).append(" found ");
                ((StringBuilder)object2).append(fragment.mState);
                Log.d((String)TAG, (String)((StringBuilder)object2).toString());
            }
            fragment.mState = n3;
        }
    }

    void noteStateNotSaved() {
        if (this.mHost == null) {
            return;
        }
        this.mStateSaved = false;
        this.mStopped = false;
        this.mNonConfig.setIsStateSaved(false);
        for (Fragment fragment : this.mFragmentStore.getFragments()) {
            if (fragment == null) continue;
            fragment.noteStateNotSaved();
        }
    }

    void onContainerAvailable(FragmentContainerView fragmentContainerView) {
        for (FragmentStateManager fragmentStateManager : this.mFragmentStore.getActiveFragmentStateManagers()) {
            Fragment fragment = fragmentStateManager.getFragment();
            if (fragment.mContainerId != fragmentContainerView.getId() || fragment.mView == null || fragment.mView.getParent() != null) continue;
            fragment.mContainer = fragmentContainerView;
            fragmentStateManager.addViewToContainer();
        }
    }

    @Deprecated
    public FragmentTransaction openTransaction() {
        return this.beginTransaction();
    }

    void performPendingDeferredStart(FragmentStateManager fragmentStateManager) {
        Fragment fragment = fragmentStateManager.getFragment();
        if (fragment.mDeferStart) {
            if (this.mExecutingActions) {
                this.mHavePendingDeferredStart = true;
                return;
            }
            fragment.mDeferStart = false;
            if (USE_STATE_MANAGER) {
                fragmentStateManager.moveToExpectedState();
            } else {
                this.moveToState(fragment);
            }
        }
    }

    public void popBackStack() {
        this.enqueueAction(new PopBackStackState(this, null, -1, 0), false);
    }

    public void popBackStack(int n, int n2) {
        if (n >= 0) {
            this.enqueueAction(new PopBackStackState(this, null, n, n2), false);
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Bad id: ");
        stringBuilder.append(n);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public void popBackStack(String string2, int n) {
        this.enqueueAction(new PopBackStackState(this, string2, -1, n), false);
    }

    public boolean popBackStackImmediate() {
        return this.popBackStackImmediate(null, -1, 0);
    }

    public boolean popBackStackImmediate(int n, int n2) {
        if (n >= 0) {
            return this.popBackStackImmediate(null, n, n2);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Bad id: ");
        stringBuilder.append(n);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public boolean popBackStackImmediate(String string2, int n) {
        return this.popBackStackImmediate(string2, -1, n);
    }

    boolean popBackStackState(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2, String string2, int n, int n2) {
        ArrayList<BackStackRecord> arrayList3 = this.mBackStack;
        if (arrayList3 == null) {
            return false;
        }
        if (string2 == null && n < 0 && (n2 & 1) == 0) {
            n = arrayList3.size() - 1;
            if (n < 0) {
                return false;
            }
            arrayList.add(this.mBackStack.remove(n));
            arrayList2.add(true);
        } else {
            int n3 = -1;
            if (string2 != null || n >= 0) {
                int n4;
                for (n4 = arrayList3.size() - 1; n4 >= 0; --n4) {
                    arrayList3 = this.mBackStack.get(n4);
                    if (string2 != null && string2.equals(((BackStackRecord)((Object)arrayList3)).getName()) || n >= 0 && n == ((BackStackRecord)((Object)arrayList3)).mIndex) break;
                }
                if (n4 < 0) {
                    return false;
                }
                n3 = n4;
                if ((n2 & 1) != 0) {
                    n2 = n4 - 1;
                    while (true) {
                        n3 = --n2;
                        if (n2 < 0) break;
                        arrayList3 = this.mBackStack.get(n2);
                        if (string2 != null && string2.equals(((BackStackRecord)((Object)arrayList3)).getName())) continue;
                        n3 = n2;
                        if (n < 0) break;
                        n3 = n2;
                        if (n != ((BackStackRecord)((Object)arrayList3)).mIndex) break;
                    }
                }
            }
            if (n3 == this.mBackStack.size() - 1) {
                return false;
            }
            for (n = this.mBackStack.size() - 1; n > n3; --n) {
                arrayList.add(this.mBackStack.remove(n));
                arrayList2.add(true);
            }
        }
        return true;
    }

    public void putFragment(Bundle bundle, String string2, Fragment fragment) {
        if (fragment.mFragmentManager != this) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Fragment ");
            stringBuilder.append(fragment);
            stringBuilder.append(" is not currently in the FragmentManager");
            this.throwException(new IllegalStateException(stringBuilder.toString()));
        }
        bundle.putString(string2, fragment.mWho);
    }

    public void registerFragmentLifecycleCallbacks(FragmentLifecycleCallbacks fragmentLifecycleCallbacks, boolean bl) {
        this.mLifecycleCallbacksDispatcher.registerFragmentLifecycleCallbacks(fragmentLifecycleCallbacks, bl);
    }

    void removeCancellationSignal(Fragment fragment, CancellationSignal cancellationSignal) {
        HashSet<CancellationSignal> hashSet = this.mExitAnimationCancellationSignals.get(fragment);
        if (hashSet != null && hashSet.remove(cancellationSignal) && hashSet.isEmpty()) {
            this.mExitAnimationCancellationSignals.remove(fragment);
            if (fragment.mState < 5) {
                this.destroyFragmentView(fragment);
                this.moveToState(fragment);
            }
        }
    }

    void removeFragment(Fragment fragment) {
        if (FragmentManager.isLoggingEnabled(2)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("remove: ");
            stringBuilder.append(fragment);
            stringBuilder.append(" nesting=");
            stringBuilder.append(fragment.mBackStackNesting);
            Log.v((String)TAG, (String)stringBuilder.toString());
        }
        boolean bl = fragment.isInBackStack();
        if (!fragment.mDetached || bl ^ true) {
            this.mFragmentStore.removeFragment(fragment);
            if (this.isMenuAvailable(fragment)) {
                this.mNeedMenuInvalidate = true;
            }
            fragment.mRemoving = true;
            this.setVisibleRemovingFragment(fragment);
        }
    }

    public void removeFragmentOnAttachListener(FragmentOnAttachListener fragmentOnAttachListener) {
        this.mOnAttachListeners.remove(fragmentOnAttachListener);
    }

    public void removeOnBackStackChangedListener(OnBackStackChangedListener onBackStackChangedListener) {
        ArrayList<OnBackStackChangedListener> arrayList = this.mBackStackChangeListeners;
        if (arrayList != null) {
            arrayList.remove(onBackStackChangedListener);
        }
    }

    void removeRetainedFragment(Fragment fragment) {
        this.mNonConfig.removeRetainedFragment(fragment);
    }

    void restoreAllState(Parcelable parcelable, FragmentManagerNonConfig fragmentManagerNonConfig) {
        if (this.mHost instanceof ViewModelStoreOwner) {
            this.throwException(new IllegalStateException("You must use restoreSaveState when your FragmentHostCallback implements ViewModelStoreOwner"));
        }
        this.mNonConfig.restoreFromSnapshot(fragmentManagerNonConfig);
        this.restoreSaveState(parcelable);
    }

    void restoreSaveState(Parcelable object) {
        int n;
        Object object2;
        Object object3;
        if (object == null) {
            return;
        }
        FragmentManagerState fragmentManagerState = (FragmentManagerState)object;
        if (fragmentManagerState.mActive == null) {
            return;
        }
        this.mFragmentStore.resetActiveFragments();
        for (FragmentState fragmentState : fragmentManagerState.mActive) {
            if (fragmentState == null) continue;
            object = this.mNonConfig.findRetainedFragmentByWho(fragmentState.mWho);
            if (object != null) {
                if (FragmentManager.isLoggingEnabled(2)) {
                    object3 = new StringBuilder();
                    ((StringBuilder)object3).append("restoreSaveState: re-attaching retained ");
                    ((StringBuilder)object3).append(object);
                    Log.v((String)TAG, (String)((StringBuilder)object3).toString());
                }
                object = new FragmentStateManager(this.mLifecycleCallbacksDispatcher, this.mFragmentStore, (Fragment)object, fragmentState);
            } else {
                object = new FragmentStateManager(this.mLifecycleCallbacksDispatcher, this.mFragmentStore, this.mHost.getContext().getClassLoader(), this.getFragmentFactory(), fragmentState);
            }
            Fragment fragment = ((FragmentStateManager)object).getFragment();
            fragment.mFragmentManager = this;
            if (FragmentManager.isLoggingEnabled(2)) {
                object3 = new StringBuilder();
                ((StringBuilder)object3).append("restoreSaveState: active (");
                ((StringBuilder)object3).append(fragment.mWho);
                ((StringBuilder)object3).append("): ");
                ((StringBuilder)object3).append(fragment);
                Log.v((String)TAG, (String)((StringBuilder)object3).toString());
            }
            ((FragmentStateManager)object).restoreState(this.mHost.getContext().getClassLoader());
            this.mFragmentStore.makeActive((FragmentStateManager)object);
            ((FragmentStateManager)object).setFragmentManagerState(this.mCurState);
        }
        object = this.mNonConfig.getRetainedFragments().iterator();
        while (object.hasNext()) {
            object2 = (Fragment)object.next();
            if (this.mFragmentStore.containsActiveFragment(((Fragment)object2).mWho)) continue;
            if (FragmentManager.isLoggingEnabled(2)) {
                object3 = new StringBuilder();
                ((StringBuilder)object3).append("Discarding retained Fragment ");
                ((StringBuilder)object3).append(object2);
                ((StringBuilder)object3).append(" that was not found in the set of active Fragments ");
                ((StringBuilder)object3).append(fragmentManagerState.mActive);
                Log.v((String)TAG, (String)((StringBuilder)object3).toString());
            }
            this.mNonConfig.removeRetainedFragment((Fragment)object2);
            ((Fragment)object2).mFragmentManager = this;
            object3 = new FragmentStateManager(this.mLifecycleCallbacksDispatcher, this.mFragmentStore, (Fragment)object2);
            ((FragmentStateManager)object3).setFragmentManagerState(1);
            ((FragmentStateManager)object3).moveToExpectedState();
            ((Fragment)object2).mRemoving = true;
            ((FragmentStateManager)object3).moveToExpectedState();
        }
        this.mFragmentStore.restoreAddedFragments(fragmentManagerState.mAdded);
        if (fragmentManagerState.mBackStack != null) {
            this.mBackStack = new ArrayList(fragmentManagerState.mBackStack.length);
            for (n = 0; n < fragmentManagerState.mBackStack.length; ++n) {
                object = fragmentManagerState.mBackStack[n].instantiate(this);
                if (FragmentManager.isLoggingEnabled(2)) {
                    object2 = new StringBuilder();
                    ((StringBuilder)object2).append("restoreAllState: back stack #");
                    ((StringBuilder)object2).append(n);
                    ((StringBuilder)object2).append(" (index ");
                    ((StringBuilder)object2).append(((BackStackRecord)object).mIndex);
                    ((StringBuilder)object2).append("): ");
                    ((StringBuilder)object2).append(object);
                    Log.v((String)TAG, (String)((StringBuilder)object2).toString());
                    object2 = new PrintWriter(new LogWriter(TAG));
                    ((BackStackRecord)object).dump("  ", (PrintWriter)object2, false);
                    ((PrintWriter)object2).close();
                }
                this.mBackStack.add((BackStackRecord)object);
            }
        } else {
            this.mBackStack = null;
        }
        this.mBackStackIndex.set(fragmentManagerState.mBackStackIndex);
        if (fragmentManagerState.mPrimaryNavActiveWho != null) {
            this.mPrimaryNav = object = this.findActiveFragment(fragmentManagerState.mPrimaryNavActiveWho);
            this.dispatchParentPrimaryNavigationFragmentChanged((Fragment)object);
        }
        if ((object = fragmentManagerState.mResultKeys) != null) {
            for (n = 0; n < ((ArrayList)object).size(); ++n) {
                this.mResults.put((String)((ArrayList)object).get(n), fragmentManagerState.mResults.get(n));
            }
        }
        this.mLaunchedFragments = new ArrayDeque<LaunchedFragmentInfo>(fragmentManagerState.mLaunchedFragments);
    }

    @Deprecated
    FragmentManagerNonConfig retainNonConfig() {
        if (this.mHost instanceof ViewModelStoreOwner) {
            this.throwException(new IllegalStateException("You cannot use retainNonConfig when your FragmentHostCallback implements ViewModelStoreOwner."));
        }
        return this.mNonConfig.getSnapshot();
    }

    Parcelable saveAllState() {
        this.forcePostponedTransactions();
        this.endAnimatingAwayFragments();
        this.execPendingActions(true);
        this.mStateSaved = true;
        this.mNonConfig.setIsStateSaved(true);
        ArrayList<FragmentState> arrayList = this.mFragmentStore.saveActiveFragments();
        if (arrayList.isEmpty()) {
            if (FragmentManager.isLoggingEnabled(2)) {
                Log.v((String)TAG, (String)"saveAllState: no fragments!");
            }
            return null;
        }
        ArrayList<String> arrayList2 = this.mFragmentStore.saveAddedFragments();
        BackStackState[] backStackStateArray = null;
        ArrayList<BackStackRecord> arrayList3 = this.mBackStack;
        Object object = backStackStateArray;
        if (arrayList3 != null) {
            int n = arrayList3.size();
            object = backStackStateArray;
            if (n > 0) {
                backStackStateArray = new BackStackState[n];
                int n2 = 0;
                while (true) {
                    object = backStackStateArray;
                    if (n2 >= n) break;
                    backStackStateArray[n2] = new BackStackState(this.mBackStack.get(n2));
                    if (FragmentManager.isLoggingEnabled(2)) {
                        object = new StringBuilder();
                        ((StringBuilder)object).append("saveAllState: adding back stack #");
                        ((StringBuilder)object).append(n2);
                        ((StringBuilder)object).append(": ");
                        ((StringBuilder)object).append(this.mBackStack.get(n2));
                        Log.v((String)TAG, (String)((StringBuilder)object).toString());
                    }
                    ++n2;
                }
            }
        }
        backStackStateArray = new FragmentManagerState();
        backStackStateArray.mActive = arrayList;
        backStackStateArray.mAdded = arrayList2;
        backStackStateArray.mBackStack = object;
        backStackStateArray.mBackStackIndex = this.mBackStackIndex.get();
        object = this.mPrimaryNav;
        if (object != null) {
            backStackStateArray.mPrimaryNavActiveWho = ((Fragment)object).mWho;
        }
        backStackStateArray.mResultKeys.addAll(this.mResults.keySet());
        backStackStateArray.mResults.addAll(this.mResults.values());
        backStackStateArray.mLaunchedFragments = new ArrayList<LaunchedFragmentInfo>(this.mLaunchedFragments);
        return backStackStateArray;
    }

    public Fragment.SavedState saveFragmentInstanceState(Fragment fragment) {
        FragmentStateManager fragmentStateManager = this.mFragmentStore.getFragmentStateManager(fragment.mWho);
        if (fragmentStateManager == null || !fragmentStateManager.getFragment().equals(fragment)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Fragment ");
            stringBuilder.append(fragment);
            stringBuilder.append(" is not currently in the FragmentManager");
            this.throwException(new IllegalStateException(stringBuilder.toString()));
        }
        return fragmentStateManager.saveInstanceState();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void scheduleCommit() {
        ArrayList<OpGenerator> arrayList = this.mPendingActions;
        synchronized (arrayList) {
            ArrayList<StartEnterTransitionListener> arrayList2 = this.mPostponedTransactions;
            boolean bl = false;
            boolean bl2 = arrayList2 != null && !arrayList2.isEmpty();
            if (this.mPendingActions.size() == 1) {
                bl = true;
            }
            if (bl2 || bl) {
                this.mHost.getHandler().removeCallbacks(this.mExecCommit);
                this.mHost.getHandler().post(this.mExecCommit);
                this.updateOnBackPressedCallbackEnabled();
            }
            return;
        }
    }

    void setExitAnimationOrder(Fragment fragment, boolean bl) {
        if ((fragment = this.getFragmentContainer(fragment)) != null && fragment instanceof FragmentContainerView) {
            ((FragmentContainerView)((Object)fragment)).setDrawDisappearingViewsLast(bl ^ true);
        }
    }

    public void setFragmentFactory(FragmentFactory fragmentFactory) {
        this.mFragmentFactory = fragmentFactory;
    }

    @Override
    public final void setFragmentResult(String string2, Bundle bundle) {
        LifecycleAwareResultListener lifecycleAwareResultListener = this.mResultListeners.get(string2);
        if (lifecycleAwareResultListener != null && lifecycleAwareResultListener.isAtLeast(Lifecycle.State.STARTED)) {
            lifecycleAwareResultListener.onFragmentResult(string2, bundle);
        } else {
            this.mResults.put(string2, bundle);
        }
    }

    @Override
    public final void setFragmentResultListener(String object, LifecycleOwner object2, FragmentResultListener fragmentResultListener) {
        if (((Lifecycle)(object2 = object2.getLifecycle())).getCurrentState() == Lifecycle.State.DESTROYED) {
            return;
        }
        LifecycleEventObserver lifecycleEventObserver = new LifecycleEventObserver(this, (String)object, fragmentResultListener, (Lifecycle)object2){
            final FragmentManager this$0;
            final Lifecycle val$lifecycle;
            final FragmentResultListener val$listener;
            final String val$requestKey;
            {
                this.this$0 = fragmentManager;
                this.val$requestKey = string2;
                this.val$listener = fragmentResultListener;
                this.val$lifecycle = lifecycle;
            }

            @Override
            public void onStateChanged(LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
                if (event == Lifecycle.Event.ON_START && (lifecycleOwner = (Bundle)this.this$0.mResults.get(this.val$requestKey)) != null) {
                    this.val$listener.onFragmentResult(this.val$requestKey, (Bundle)lifecycleOwner);
                    this.this$0.clearFragmentResult(this.val$requestKey);
                }
                if (event == Lifecycle.Event.ON_DESTROY) {
                    this.val$lifecycle.removeObserver(this);
                    this.this$0.mResultListeners.remove(this.val$requestKey);
                }
            }
        };
        ((Lifecycle)object2).addObserver(lifecycleEventObserver);
        object = this.mResultListeners.put((String)object, new LifecycleAwareResultListener((Lifecycle)object2, fragmentResultListener, lifecycleEventObserver));
        if (object != null) {
            ((LifecycleAwareResultListener)object).removeObserver();
        }
    }

    void setMaxLifecycle(Fragment fragment, Lifecycle.State object) {
        if (fragment.equals(this.findActiveFragment(fragment.mWho)) && (fragment.mHost == null || fragment.mFragmentManager == this)) {
            fragment.mMaxState = object;
            return;
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("Fragment ");
        ((StringBuilder)object).append(fragment);
        ((StringBuilder)object).append(" is not an active fragment of FragmentManager ");
        ((StringBuilder)object).append(this);
        throw new IllegalArgumentException(((StringBuilder)object).toString());
    }

    void setPrimaryNavigationFragment(Fragment fragment) {
        if (fragment != null && (!fragment.equals(this.findActiveFragment(fragment.mWho)) || fragment.mHost != null && fragment.mFragmentManager != this)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Fragment ");
            stringBuilder.append(fragment);
            stringBuilder.append(" is not an active fragment of FragmentManager ");
            stringBuilder.append(this);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        Fragment fragment2 = this.mPrimaryNav;
        this.mPrimaryNav = fragment;
        this.dispatchParentPrimaryNavigationFragmentChanged(fragment2);
        this.dispatchParentPrimaryNavigationFragmentChanged(this.mPrimaryNav);
    }

    void setSpecialEffectsControllerFactory(SpecialEffectsControllerFactory specialEffectsControllerFactory) {
        this.mSpecialEffectsControllerFactory = specialEffectsControllerFactory;
    }

    void showFragment(Fragment fragment) {
        if (FragmentManager.isLoggingEnabled(2)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("show: ");
            stringBuilder.append(fragment);
            Log.v((String)TAG, (String)stringBuilder.toString());
        }
        if (fragment.mHidden) {
            fragment.mHidden = false;
            fragment.mHiddenChanged ^= true;
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(128);
        stringBuilder.append("FragmentManager{");
        stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
        stringBuilder.append(" in ");
        FragmentHostCallback<?> fragmentHostCallback = this.mParent;
        if (fragmentHostCallback != null) {
            stringBuilder.append(fragmentHostCallback.getClass().getSimpleName());
            stringBuilder.append("{");
            stringBuilder.append(Integer.toHexString(System.identityHashCode(this.mParent)));
            stringBuilder.append("}");
        } else {
            fragmentHostCallback = this.mHost;
            if (fragmentHostCallback != null) {
                stringBuilder.append(fragmentHostCallback.getClass().getSimpleName());
                stringBuilder.append("{");
                stringBuilder.append(Integer.toHexString(System.identityHashCode(this.mHost)));
                stringBuilder.append("}");
            } else {
                stringBuilder.append("null");
            }
        }
        stringBuilder.append("}}");
        return stringBuilder.toString();
    }

    public void unregisterFragmentLifecycleCallbacks(FragmentLifecycleCallbacks fragmentLifecycleCallbacks) {
        this.mLifecycleCallbacksDispatcher.unregisterFragmentLifecycleCallbacks(fragmentLifecycleCallbacks);
    }

    public static interface BackStackEntry {
        @Deprecated
        public CharSequence getBreadCrumbShortTitle();

        @Deprecated
        public int getBreadCrumbShortTitleRes();

        @Deprecated
        public CharSequence getBreadCrumbTitle();

        @Deprecated
        public int getBreadCrumbTitleRes();

        public int getId();

        public String getName();
    }

    static class FragmentIntentSenderContract
    extends ActivityResultContract<IntentSenderRequest, ActivityResult> {
        FragmentIntentSenderContract() {
        }

        @Override
        public Intent createIntent(Context object, IntentSenderRequest intentSenderRequest) {
            Intent intent = new Intent("androidx.activity.result.contract.action.INTENT_SENDER_REQUEST");
            Intent intent2 = intentSenderRequest.getFillInIntent();
            object = intentSenderRequest;
            if (intent2 != null) {
                Bundle bundle = intent2.getBundleExtra("androidx.activity.result.contract.extra.ACTIVITY_OPTIONS_BUNDLE");
                object = intentSenderRequest;
                if (bundle != null) {
                    intent.putExtra("androidx.activity.result.contract.extra.ACTIVITY_OPTIONS_BUNDLE", bundle);
                    intent2.removeExtra("androidx.activity.result.contract.extra.ACTIVITY_OPTIONS_BUNDLE");
                    object = intentSenderRequest;
                    if (intent2.getBooleanExtra(FragmentManager.EXTRA_CREATED_FILLIN_INTENT, false)) {
                        object = new IntentSenderRequest.Builder(intentSenderRequest.getIntentSender()).setFillInIntent(null).setFlags(intentSenderRequest.getFlagsValues(), intentSenderRequest.getFlagsMask()).build();
                    }
                }
            }
            intent.putExtra("androidx.activity.result.contract.extra.INTENT_SENDER_REQUEST", (Parcelable)object);
            if (FragmentManager.isLoggingEnabled(2)) {
                object = new StringBuilder();
                ((StringBuilder)object).append("CreateIntent created the following intent: ");
                ((StringBuilder)object).append(intent);
                Log.v((String)FragmentManager.TAG, (String)((StringBuilder)object).toString());
            }
            return intent;
        }

        @Override
        public ActivityResult parseResult(int n, Intent intent) {
            return new ActivityResult(n, intent);
        }
    }

    public static abstract class FragmentLifecycleCallbacks {
        @Deprecated
        public void onFragmentActivityCreated(FragmentManager fragmentManager, Fragment fragment, Bundle bundle) {
        }

        public void onFragmentAttached(FragmentManager fragmentManager, Fragment fragment, Context context) {
        }

        public void onFragmentCreated(FragmentManager fragmentManager, Fragment fragment, Bundle bundle) {
        }

        public void onFragmentDestroyed(FragmentManager fragmentManager, Fragment fragment) {
        }

        public void onFragmentDetached(FragmentManager fragmentManager, Fragment fragment) {
        }

        public void onFragmentPaused(FragmentManager fragmentManager, Fragment fragment) {
        }

        public void onFragmentPreAttached(FragmentManager fragmentManager, Fragment fragment, Context context) {
        }

        public void onFragmentPreCreated(FragmentManager fragmentManager, Fragment fragment, Bundle bundle) {
        }

        public void onFragmentResumed(FragmentManager fragmentManager, Fragment fragment) {
        }

        public void onFragmentSaveInstanceState(FragmentManager fragmentManager, Fragment fragment, Bundle bundle) {
        }

        public void onFragmentStarted(FragmentManager fragmentManager, Fragment fragment) {
        }

        public void onFragmentStopped(FragmentManager fragmentManager, Fragment fragment) {
        }

        public void onFragmentViewCreated(FragmentManager fragmentManager, Fragment fragment, View view, Bundle bundle) {
        }

        public void onFragmentViewDestroyed(FragmentManager fragmentManager, Fragment fragment) {
        }
    }

    static class LaunchedFragmentInfo
    implements Parcelable {
        public static final Parcelable.Creator<LaunchedFragmentInfo> CREATOR = new Parcelable.Creator<LaunchedFragmentInfo>(){

            public LaunchedFragmentInfo createFromParcel(Parcel parcel) {
                return new LaunchedFragmentInfo(parcel);
            }

            public LaunchedFragmentInfo[] newArray(int n) {
                return new LaunchedFragmentInfo[n];
            }
        };
        int mRequestCode;
        String mWho;

        LaunchedFragmentInfo(Parcel parcel) {
            this.mWho = parcel.readString();
            this.mRequestCode = parcel.readInt();
        }

        LaunchedFragmentInfo(String string2, int n) {
            this.mWho = string2;
            this.mRequestCode = n;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel parcel, int n) {
            parcel.writeString(this.mWho);
            parcel.writeInt(this.mRequestCode);
        }
    }

    private static class LifecycleAwareResultListener
    implements FragmentResultListener {
        private final Lifecycle mLifecycle;
        private final FragmentResultListener mListener;
        private final LifecycleEventObserver mObserver;

        LifecycleAwareResultListener(Lifecycle lifecycle, FragmentResultListener fragmentResultListener, LifecycleEventObserver lifecycleEventObserver) {
            this.mLifecycle = lifecycle;
            this.mListener = fragmentResultListener;
            this.mObserver = lifecycleEventObserver;
        }

        public boolean isAtLeast(Lifecycle.State state) {
            return this.mLifecycle.getCurrentState().isAtLeast(state);
        }

        @Override
        public void onFragmentResult(String string2, Bundle bundle) {
            this.mListener.onFragmentResult(string2, bundle);
        }

        public void removeObserver() {
            this.mLifecycle.removeObserver(this.mObserver);
        }
    }

    public static interface OnBackStackChangedListener {
        public void onBackStackChanged();
    }

    static interface OpGenerator {
        public boolean generateOps(ArrayList<BackStackRecord> var1, ArrayList<Boolean> var2);
    }

    private class PopBackStackState
    implements OpGenerator {
        final int mFlags;
        final int mId;
        final String mName;
        final FragmentManager this$0;

        PopBackStackState(FragmentManager fragmentManager, String string2, int n, int n2) {
            this.this$0 = fragmentManager;
            this.mName = string2;
            this.mId = n;
            this.mFlags = n2;
        }

        @Override
        public boolean generateOps(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2) {
            if (this.this$0.mPrimaryNav != null && this.mId < 0 && this.mName == null && this.this$0.mPrimaryNav.getChildFragmentManager().popBackStackImmediate()) {
                return false;
            }
            return this.this$0.popBackStackState(arrayList, arrayList2, this.mName, this.mId, this.mFlags);
        }
    }

    static class StartEnterTransitionListener
    implements Fragment.OnStartEnterTransitionListener {
        final boolean mIsBack;
        private int mNumPostponed;
        final BackStackRecord mRecord;

        StartEnterTransitionListener(BackStackRecord backStackRecord, boolean bl) {
            this.mIsBack = bl;
            this.mRecord = backStackRecord;
        }

        void cancelTransaction() {
            this.mRecord.mManager.completeExecute(this.mRecord, this.mIsBack, false, false);
        }

        void completeTransaction() {
            int n = this.mNumPostponed;
            boolean bl = false;
            n = n > 0 ? 1 : 0;
            for (Fragment object2 : this.mRecord.mManager.getFragments()) {
                object2.setOnStartEnterTransitionListener(null);
                if (n == 0 || !object2.isPostponed()) continue;
                object2.startPostponedEnterTransition();
            }
            FragmentManager fragmentManager = this.mRecord.mManager;
            BackStackRecord backStackRecord = this.mRecord;
            boolean bl2 = this.mIsBack;
            if (n == 0) {
                bl = true;
            }
            fragmentManager.completeExecute(backStackRecord, bl2, bl, true);
        }

        public boolean isReady() {
            boolean bl = this.mNumPostponed == 0;
            return bl;
        }

        @Override
        public void onStartEnterTransition() {
            int n;
            this.mNumPostponed = n = this.mNumPostponed - 1;
            if (n != 0) {
                return;
            }
            this.mRecord.mManager.scheduleCommit();
        }

        @Override
        public void startListening() {
            ++this.mNumPostponed;
        }
    }
}

