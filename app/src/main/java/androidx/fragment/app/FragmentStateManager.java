/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.res.Resources$NotFoundException
 *  android.os.Bundle
 *  android.util.Log
 *  android.util.SparseArray
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.View$OnAttachStateChangeListener
 *  android.view.ViewGroup
 */
package androidx.fragment.app;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.view.ViewCompat;
import androidx.fragment.R;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.FragmentHostCallback;
import androidx.fragment.app.FragmentLifecycleCallbacksDispatcher;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentState;
import androidx.fragment.app.FragmentStore;
import androidx.fragment.app.SpecialEffectsController;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelStoreOwner;

class FragmentStateManager {
    private static final String TAG = "FragmentManager";
    private static final String TARGET_REQUEST_CODE_STATE_TAG = "android:target_req_state";
    private static final String TARGET_STATE_TAG = "android:target_state";
    private static final String USER_VISIBLE_HINT_TAG = "android:user_visible_hint";
    private static final String VIEW_REGISTRY_STATE_TAG = "android:view_registry_state";
    private static final String VIEW_STATE_TAG = "android:view_state";
    private final FragmentLifecycleCallbacksDispatcher mDispatcher;
    private final Fragment mFragment;
    private int mFragmentManagerState = -1;
    private final FragmentStore mFragmentStore;
    private boolean mMovingToState = false;

    FragmentStateManager(FragmentLifecycleCallbacksDispatcher fragmentLifecycleCallbacksDispatcher, FragmentStore fragmentStore, Fragment fragment) {
        this.mDispatcher = fragmentLifecycleCallbacksDispatcher;
        this.mFragmentStore = fragmentStore;
        this.mFragment = fragment;
    }

    FragmentStateManager(FragmentLifecycleCallbacksDispatcher object, FragmentStore fragmentStore, Fragment fragment, FragmentState fragmentState) {
        this.mDispatcher = object;
        this.mFragmentStore = fragmentStore;
        this.mFragment = fragment;
        fragment.mSavedViewState = null;
        fragment.mSavedViewRegistryState = null;
        fragment.mBackStackNesting = 0;
        fragment.mInLayout = false;
        fragment.mAdded = false;
        object = fragment.mTarget != null ? fragment.mTarget.mWho : null;
        fragment.mTargetWho = object;
        fragment.mTarget = null;
        fragment.mSavedFragmentState = fragmentState.mSavedFragmentState != null ? fragmentState.mSavedFragmentState : new Bundle();
    }

    FragmentStateManager(FragmentLifecycleCallbacksDispatcher object, FragmentStore object2, ClassLoader classLoader, FragmentFactory fragmentFactory, FragmentState fragmentState) {
        this.mDispatcher = object;
        this.mFragmentStore = object2;
        this.mFragment = object = fragmentFactory.instantiate(classLoader, fragmentState.mClassName);
        if (fragmentState.mArguments != null) {
            fragmentState.mArguments.setClassLoader(classLoader);
        }
        ((Fragment)object).setArguments(fragmentState.mArguments);
        ((Fragment)object).mWho = fragmentState.mWho;
        ((Fragment)object).mFromLayout = fragmentState.mFromLayout;
        ((Fragment)object).mRestored = true;
        ((Fragment)object).mFragmentId = fragmentState.mFragmentId;
        ((Fragment)object).mContainerId = fragmentState.mContainerId;
        ((Fragment)object).mTag = fragmentState.mTag;
        ((Fragment)object).mRetainInstance = fragmentState.mRetainInstance;
        ((Fragment)object).mRemoving = fragmentState.mRemoving;
        ((Fragment)object).mDetached = fragmentState.mDetached;
        ((Fragment)object).mHidden = fragmentState.mHidden;
        ((Fragment)object).mMaxState = Lifecycle.State.values()[fragmentState.mMaxLifecycleState];
        ((Fragment)object).mSavedFragmentState = fragmentState.mSavedFragmentState != null ? fragmentState.mSavedFragmentState : new Bundle();
        if (FragmentManager.isLoggingEnabled(2)) {
            object2 = new StringBuilder();
            ((StringBuilder)object2).append("Instantiated fragment ");
            ((StringBuilder)object2).append(object);
            Log.v((String)TAG, (String)((StringBuilder)object2).toString());
        }
    }

    private boolean isFragmentViewChild(View view) {
        if (view == this.mFragment.mView) {
            return true;
        }
        for (view = view.getParent(); view != null; view = view.getParent()) {
            if (view != this.mFragment.mView) continue;
            return true;
        }
        return false;
    }

    private Bundle saveBasicState() {
        Bundle bundle = new Bundle();
        this.mFragment.performSaveInstanceState(bundle);
        this.mDispatcher.dispatchOnFragmentSaveInstanceState(this.mFragment, bundle, false);
        Bundle bundle2 = bundle;
        if (bundle.isEmpty()) {
            bundle2 = null;
        }
        if (this.mFragment.mView != null) {
            this.saveViewState();
        }
        bundle = bundle2;
        if (this.mFragment.mSavedViewState != null) {
            bundle = bundle2;
            if (bundle2 == null) {
                bundle = new Bundle();
            }
            bundle.putSparseParcelableArray(VIEW_STATE_TAG, this.mFragment.mSavedViewState);
        }
        bundle2 = bundle;
        if (this.mFragment.mSavedViewRegistryState != null) {
            bundle2 = bundle;
            if (bundle == null) {
                bundle2 = new Bundle();
            }
            bundle2.putBundle(VIEW_REGISTRY_STATE_TAG, this.mFragment.mSavedViewRegistryState);
        }
        bundle = bundle2;
        if (!this.mFragment.mUserVisibleHint) {
            bundle = bundle2;
            if (bundle2 == null) {
                bundle = new Bundle();
            }
            bundle.putBoolean(USER_VISIBLE_HINT_TAG, this.mFragment.mUserVisibleHint);
        }
        return bundle;
    }

    void activityCreated() {
        Object object;
        if (FragmentManager.isLoggingEnabled(3)) {
            object = new StringBuilder();
            ((StringBuilder)object).append("moveto ACTIVITY_CREATED: ");
            ((StringBuilder)object).append(this.mFragment);
            Log.d((String)TAG, (String)((StringBuilder)object).toString());
        }
        object = this.mFragment;
        ((Fragment)object).performActivityCreated(((Fragment)object).mSavedFragmentState);
        FragmentLifecycleCallbacksDispatcher fragmentLifecycleCallbacksDispatcher = this.mDispatcher;
        object = this.mFragment;
        fragmentLifecycleCallbacksDispatcher.dispatchOnFragmentActivityCreated((Fragment)object, ((Fragment)object).mSavedFragmentState, false);
    }

    void addViewToContainer() {
        int n = this.mFragmentStore.findFragmentIndexInContainer(this.mFragment);
        this.mFragment.mContainer.addView(this.mFragment.mView, n);
    }

    /*
     * Enabled aggressive block sorting
     */
    void attach() {
        Object object;
        if (FragmentManager.isLoggingEnabled(3)) {
            object = new StringBuilder();
            ((StringBuilder)object).append("moveto ATTACHED: ");
            ((StringBuilder)object).append(this.mFragment);
            Log.d((String)TAG, (String)((StringBuilder)object).toString());
        }
        if (this.mFragment.mTarget != null) {
            object = this.mFragmentStore.getFragmentStateManager(this.mFragment.mTarget.mWho);
            if (object == null) {
                object = new StringBuilder();
                ((StringBuilder)object).append("Fragment ");
                ((StringBuilder)object).append(this.mFragment);
                ((StringBuilder)object).append(" declared target fragment ");
                ((StringBuilder)object).append(this.mFragment.mTarget);
                ((StringBuilder)object).append(" that does not belong to this FragmentManager!");
                throw new IllegalStateException(((StringBuilder)object).toString());
            }
            Fragment fragment = this.mFragment;
            fragment.mTargetWho = fragment.mTarget.mWho;
            this.mFragment.mTarget = null;
        } else if (this.mFragment.mTargetWho != null) {
            object = this.mFragmentStore.getFragmentStateManager(this.mFragment.mTargetWho);
            if (object == null) {
                object = new StringBuilder();
                ((StringBuilder)object).append("Fragment ");
                ((StringBuilder)object).append(this.mFragment);
                ((StringBuilder)object).append(" declared target fragment ");
                ((StringBuilder)object).append(this.mFragment.mTargetWho);
                ((StringBuilder)object).append(" that does not belong to this FragmentManager!");
                throw new IllegalStateException(((StringBuilder)object).toString());
            }
        } else {
            object = null;
        }
        if (object != null && (FragmentManager.USE_STATE_MANAGER || ((FragmentStateManager)object).getFragment().mState < 1)) {
            ((FragmentStateManager)object).moveToExpectedState();
        }
        object = this.mFragment;
        ((Fragment)object).mHost = ((Fragment)object).mFragmentManager.getHost();
        object = this.mFragment;
        ((Fragment)object).mParentFragment = ((Fragment)object).mFragmentManager.getParent();
        this.mDispatcher.dispatchOnFragmentPreAttached(this.mFragment, false);
        this.mFragment.performAttach();
        this.mDispatcher.dispatchOnFragmentAttached(this.mFragment, false);
    }

    int computeExpectedState() {
        if (this.mFragment.mFragmentManager == null) {
            return this.mFragment.mState;
        }
        int n = this.mFragmentManagerState;
        switch (2.$SwitchMap$androidx$lifecycle$Lifecycle$State[this.mFragment.mMaxState.ordinal()]) {
            default: {
                n = Math.min(n, -1);
                break;
            }
            case 4: {
                n = Math.min(n, 0);
                break;
            }
            case 3: {
                n = Math.min(n, 1);
                break;
            }
            case 2: {
                n = Math.min(n, 5);
            }
            case 1: 
        }
        int n2 = n;
        if (this.mFragment.mFromLayout) {
            if (this.mFragment.mInLayout) {
                n2 = n = Math.max(this.mFragmentManagerState, 2);
                if (this.mFragment.mView != null) {
                    n2 = n;
                    if (this.mFragment.mView.getParent() == null) {
                        n2 = Math.min(n, 2);
                    }
                }
            } else {
                n2 = this.mFragmentManagerState < 4 ? Math.min(n, this.mFragment.mState) : Math.min(n, 1);
            }
        }
        n = n2;
        if (!this.mFragment.mAdded) {
            n = Math.min(n2, 1);
        }
        SpecialEffectsController.Operation.LifecycleImpact lifecycleImpact = null;
        Object object = lifecycleImpact;
        if (FragmentManager.USE_STATE_MANAGER) {
            object = lifecycleImpact;
            if (this.mFragment.mContainer != null) {
                object = SpecialEffectsController.getOrCreateController(this.mFragment.mContainer, this.mFragment.getParentFragmentManager()).getAwaitingCompletionLifecycleImpact(this);
            }
        }
        if (object == SpecialEffectsController.Operation.LifecycleImpact.ADDING) {
            n2 = Math.min(n, 6);
        } else if (object == SpecialEffectsController.Operation.LifecycleImpact.REMOVING) {
            n2 = Math.max(n, 3);
        } else {
            n2 = n;
            if (this.mFragment.mRemoving) {
                n2 = this.mFragment.isInBackStack() ? Math.min(n, 1) : Math.min(n, -1);
            }
        }
        n = n2;
        if (this.mFragment.mDeferStart) {
            n = n2;
            if (this.mFragment.mState < 5) {
                n = Math.min(n2, 4);
            }
        }
        if (FragmentManager.isLoggingEnabled(2)) {
            object = new StringBuilder();
            ((StringBuilder)object).append("computeExpectedState() of ");
            ((StringBuilder)object).append(n);
            ((StringBuilder)object).append(" for ");
            ((StringBuilder)object).append(this.mFragment);
            Log.v((String)TAG, (String)((StringBuilder)object).toString());
        }
        return n;
    }

    void create() {
        Object object;
        if (FragmentManager.isLoggingEnabled(3)) {
            object = new StringBuilder();
            ((StringBuilder)object).append("moveto CREATED: ");
            ((StringBuilder)object).append(this.mFragment);
            Log.d((String)TAG, (String)((StringBuilder)object).toString());
        }
        if (!this.mFragment.mIsCreated) {
            object = this.mDispatcher;
            Object object2 = this.mFragment;
            ((FragmentLifecycleCallbacksDispatcher)object).dispatchOnFragmentPreCreated((Fragment)object2, ((Fragment)object2).mSavedFragmentState, false);
            object = this.mFragment;
            ((Fragment)object).performCreate(((Fragment)object).mSavedFragmentState);
            object2 = this.mDispatcher;
            object = this.mFragment;
            ((FragmentLifecycleCallbacksDispatcher)object2).dispatchOnFragmentCreated((Fragment)object, ((Fragment)object).mSavedFragmentState, false);
        } else {
            object = this.mFragment;
            ((Fragment)object).restoreChildFragmentState(((Fragment)object).mSavedFragmentState);
            this.mFragment.mState = 1;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void createView() {
        Object object;
        Object object2;
        if (this.mFragment.mFromLayout) {
            return;
        }
        if (FragmentManager.isLoggingEnabled(3)) {
            object2 = new StringBuilder();
            ((StringBuilder)object2).append("moveto CREATE_VIEW: ");
            ((StringBuilder)object2).append(this.mFragment);
            Log.d((String)TAG, (String)((StringBuilder)object2).toString());
        }
        object2 = this.mFragment;
        LayoutInflater layoutInflater = ((Fragment)object2).performGetLayoutInflater(((Fragment)object2).mSavedFragmentState);
        object2 = null;
        if (this.mFragment.mContainer != null) {
            object2 = this.mFragment.mContainer;
        } else if (this.mFragment.mContainerId != 0) {
            if (this.mFragment.mContainerId == -1) {
                object2 = new StringBuilder();
                ((StringBuilder)object2).append("Cannot create fragment ");
                ((StringBuilder)object2).append(this.mFragment);
                ((StringBuilder)object2).append(" for a container view with no id");
                throw new IllegalArgumentException(((StringBuilder)object2).toString());
            }
            object2 = object = (ViewGroup)this.mFragment.mFragmentManager.getContainer().onFindViewById(this.mFragment.mContainerId);
            if (object == null) {
                if (this.mFragment.mRestored) {
                    object2 = object;
                } else {
                    try {
                        object2 = this.mFragment.getResources().getResourceName(this.mFragment.mContainerId);
                    }
                    catch (Resources.NotFoundException notFoundException) {
                        object2 = "unknown";
                    }
                    object = new StringBuilder();
                    ((StringBuilder)object).append("No view found for id 0x");
                    ((StringBuilder)object).append(Integer.toHexString(this.mFragment.mContainerId));
                    ((StringBuilder)object).append(" (");
                    ((StringBuilder)object).append((String)object2);
                    ((StringBuilder)object).append(") for fragment ");
                    ((StringBuilder)object).append(this.mFragment);
                    throw new IllegalArgumentException(((StringBuilder)object).toString());
                }
            }
        }
        this.mFragment.mContainer = object2;
        object = this.mFragment;
        ((Fragment)object).performCreateView(layoutInflater, (ViewGroup)object2, ((Fragment)object).mSavedFragmentState);
        if (this.mFragment.mView != null) {
            object = this.mFragment.mView;
            boolean bl = false;
            object.setSaveFromParentEnabled(false);
            this.mFragment.mView.setTag(R.id.fragment_container_view_tag, (Object)this.mFragment);
            if (object2 != null) {
                this.addViewToContainer();
            }
            if (this.mFragment.mHidden) {
                this.mFragment.mView.setVisibility(8);
            }
            if (ViewCompat.isAttachedToWindow(this.mFragment.mView)) {
                ViewCompat.requestApplyInsets(this.mFragment.mView);
            } else {
                object2 = this.mFragment.mView;
                object2.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener(this, (View)object2){
                    final FragmentStateManager this$0;
                    final View val$fragmentView;
                    {
                        this.this$0 = fragmentStateManager;
                        this.val$fragmentView = view;
                    }

                    public void onViewAttachedToWindow(View view) {
                        this.val$fragmentView.removeOnAttachStateChangeListener((View.OnAttachStateChangeListener)this);
                        ViewCompat.requestApplyInsets(this.val$fragmentView);
                    }

                    public void onViewDetachedFromWindow(View view) {
                    }
                });
            }
            this.mFragment.performViewCreated();
            object = this.mDispatcher;
            object2 = this.mFragment;
            ((FragmentLifecycleCallbacksDispatcher)object).dispatchOnFragmentViewCreated((Fragment)object2, ((Fragment)object2).mView, this.mFragment.mSavedFragmentState, false);
            int n = this.mFragment.mView.getVisibility();
            float f = this.mFragment.mView.getAlpha();
            if (FragmentManager.USE_STATE_MANAGER) {
                this.mFragment.setPostOnViewCreatedAlpha(f);
                if (this.mFragment.mContainer != null && n == 0) {
                    object2 = this.mFragment.mView.findFocus();
                    if (object2 != null) {
                        this.mFragment.setFocusedView((View)object2);
                        if (FragmentManager.isLoggingEnabled(2)) {
                            object = new StringBuilder();
                            ((StringBuilder)object).append("requestFocus: Saved focused view ");
                            ((StringBuilder)object).append(object2);
                            ((StringBuilder)object).append(" for Fragment ");
                            ((StringBuilder)object).append(this.mFragment);
                            Log.v((String)TAG, (String)((StringBuilder)object).toString());
                        }
                    }
                    this.mFragment.mView.setAlpha(0.0f);
                }
            } else {
                object2 = this.mFragment;
                boolean bl2 = bl;
                if (n == 0) {
                    bl2 = bl;
                    if (((Fragment)object2).mContainer != null) {
                        bl2 = true;
                    }
                }
                ((Fragment)object2).mIsNewlyAdded = bl2;
            }
        }
        this.mFragment.mState = 2;
    }

    void destroy() {
        boolean bl;
        FragmentHostCallback<?> fragmentHostCallback;
        if (FragmentManager.isLoggingEnabled(3)) {
            fragmentHostCallback = new StringBuilder();
            ((StringBuilder)((Object)fragmentHostCallback)).append("movefrom CREATED: ");
            ((StringBuilder)((Object)fragmentHostCallback)).append(this.mFragment);
            Log.d((String)TAG, (String)((StringBuilder)((Object)fragmentHostCallback)).toString());
        }
        boolean bl2 = (bl = this.mFragment.mRemoving && !this.mFragment.isInBackStack()) || this.mFragmentStore.getNonConfig().shouldDestroy(this.mFragment);
        if (bl2) {
            fragmentHostCallback = this.mFragment.mHost;
            boolean bl3 = fragmentHostCallback instanceof ViewModelStoreOwner ? this.mFragmentStore.getNonConfig().isCleared() : (fragmentHostCallback.getContext() instanceof Activity ? true ^ ((Activity)fragmentHostCallback.getContext()).isChangingConfigurations() : true);
            if (bl || bl3) {
                this.mFragmentStore.getNonConfig().clearNonConfigState(this.mFragment);
            }
            this.mFragment.performDestroy();
            this.mDispatcher.dispatchOnFragmentDestroyed(this.mFragment, false);
            for (FragmentStateManager fragmentStateManager : this.mFragmentStore.getActiveFragmentStateManagers()) {
                if (fragmentStateManager == null) continue;
                Fragment fragment = fragmentStateManager.getFragment();
                if (!this.mFragment.mWho.equals(fragment.mTargetWho)) continue;
                fragment.mTarget = this.mFragment;
                fragment.mTargetWho = null;
            }
            if (this.mFragment.mTargetWho != null) {
                fragmentHostCallback = this.mFragment;
                ((Fragment)((Object)fragmentHostCallback)).mTarget = this.mFragmentStore.findActiveFragment(((Fragment)((Object)fragmentHostCallback)).mTargetWho);
            }
            this.mFragmentStore.makeInactive(this);
        } else {
            if (this.mFragment.mTargetWho != null && (fragmentHostCallback = this.mFragmentStore.findActiveFragment(this.mFragment.mTargetWho)) != null && ((Fragment)((Object)fragmentHostCallback)).mRetainInstance) {
                this.mFragment.mTarget = fragmentHostCallback;
            }
            this.mFragment.mState = 0;
        }
    }

    void destroyFragmentView() {
        if (FragmentManager.isLoggingEnabled(3)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("movefrom CREATE_VIEW: ");
            stringBuilder.append(this.mFragment);
            Log.d((String)TAG, (String)stringBuilder.toString());
        }
        if (this.mFragment.mContainer != null && this.mFragment.mView != null) {
            this.mFragment.mContainer.removeView(this.mFragment.mView);
        }
        this.mFragment.performDestroyView();
        this.mDispatcher.dispatchOnFragmentViewDestroyed(this.mFragment, false);
        this.mFragment.mContainer = null;
        this.mFragment.mView = null;
        this.mFragment.mViewLifecycleOwner = null;
        this.mFragment.mViewLifecycleOwnerLiveData.setValue(null);
        this.mFragment.mInLayout = false;
    }

    void detach() {
        Object object;
        if (FragmentManager.isLoggingEnabled(3)) {
            object = new StringBuilder();
            ((StringBuilder)object).append("movefrom ATTACHED: ");
            ((StringBuilder)object).append(this.mFragment);
            Log.d((String)TAG, (String)((StringBuilder)object).toString());
        }
        this.mFragment.performDetach();
        FragmentLifecycleCallbacksDispatcher fragmentLifecycleCallbacksDispatcher = this.mDispatcher;
        object = this.mFragment;
        boolean bl = false;
        fragmentLifecycleCallbacksDispatcher.dispatchOnFragmentDetached((Fragment)object, false);
        this.mFragment.mState = -1;
        this.mFragment.mHost = null;
        this.mFragment.mParentFragment = null;
        this.mFragment.mFragmentManager = null;
        boolean bl2 = bl;
        if (this.mFragment.mRemoving) {
            bl2 = bl;
            if (!this.mFragment.isInBackStack()) {
                bl2 = true;
            }
        }
        if (bl2 || this.mFragmentStore.getNonConfig().shouldDestroy(this.mFragment)) {
            if (FragmentManager.isLoggingEnabled(3)) {
                object = new StringBuilder();
                ((StringBuilder)object).append("initState called for fragment: ");
                ((StringBuilder)object).append(this.mFragment);
                Log.d((String)TAG, (String)((StringBuilder)object).toString());
            }
            this.mFragment.initState();
        }
    }

    void ensureInflatedView() {
        if (this.mFragment.mFromLayout && this.mFragment.mInLayout && !this.mFragment.mPerformedCreateView) {
            Object object;
            if (FragmentManager.isLoggingEnabled(3)) {
                object = new StringBuilder();
                ((StringBuilder)object).append("moveto CREATE_VIEW: ");
                ((StringBuilder)object).append(this.mFragment);
                Log.d((String)TAG, (String)((StringBuilder)object).toString());
            }
            object = this.mFragment;
            ((Fragment)object).performCreateView(((Fragment)object).performGetLayoutInflater(((Fragment)object).mSavedFragmentState), null, this.mFragment.mSavedFragmentState);
            if (this.mFragment.mView != null) {
                this.mFragment.mView.setSaveFromParentEnabled(false);
                this.mFragment.mView.setTag(R.id.fragment_container_view_tag, (Object)this.mFragment);
                if (this.mFragment.mHidden) {
                    this.mFragment.mView.setVisibility(8);
                }
                this.mFragment.performViewCreated();
                object = this.mDispatcher;
                Fragment fragment = this.mFragment;
                ((FragmentLifecycleCallbacksDispatcher)object).dispatchOnFragmentViewCreated(fragment, fragment.mView, this.mFragment.mSavedFragmentState, false);
                this.mFragment.mState = 2;
            }
        }
    }

    Fragment getFragment() {
        return this.mFragment;
    }

    void moveToExpectedState() {
        if (this.mMovingToState) {
            if (FragmentManager.isLoggingEnabled(2)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Ignoring re-entrant call to moveToExpectedState() for ");
                stringBuilder.append(this.getFragment());
                Log.v((String)TAG, (String)stringBuilder.toString());
            }
            return;
        }
        try {
            Object object;
            int n;
            this.mMovingToState = true;
            block22: while ((n = this.computeExpectedState()) != this.mFragment.mState) {
                if (n > this.mFragment.mState) {
                    switch (this.mFragment.mState + 1) {
                        default: {
                            continue block22;
                        }
                        case 7: {
                            this.resume();
                            continue block22;
                        }
                        case 6: {
                            this.mFragment.mState = 6;
                            continue block22;
                        }
                        case 5: {
                            this.start();
                            continue block22;
                        }
                        case 4: {
                            if (this.mFragment.mView != null && this.mFragment.mContainer != null) {
                                object = SpecialEffectsController.getOrCreateController(this.mFragment.mContainer, this.mFragment.getParentFragmentManager());
                                n = this.mFragment.mView.getVisibility();
                                ((SpecialEffectsController)object).enqueueAdd(SpecialEffectsController.Operation.State.from(n), this);
                            }
                            this.mFragment.mState = 4;
                            continue block22;
                        }
                        case 3: {
                            this.activityCreated();
                            continue block22;
                        }
                        case 2: {
                            this.ensureInflatedView();
                            this.createView();
                            continue block22;
                        }
                        case 1: {
                            this.create();
                            continue block22;
                        }
                        case 0: 
                    }
                    this.attach();
                    continue;
                }
                switch (this.mFragment.mState - 1) {
                    default: {
                        continue block22;
                    }
                    case 6: {
                        this.pause();
                        continue block22;
                    }
                    case 5: {
                        this.mFragment.mState = 5;
                        continue block22;
                    }
                    case 4: {
                        this.stop();
                        continue block22;
                    }
                    case 3: {
                        if (FragmentManager.isLoggingEnabled(3)) {
                            object = new StringBuilder();
                            ((StringBuilder)object).append("movefrom ACTIVITY_CREATED: ");
                            ((StringBuilder)object).append(this.mFragment);
                            Log.d((String)TAG, (String)((StringBuilder)object).toString());
                        }
                        if (this.mFragment.mView != null && this.mFragment.mSavedViewState == null) {
                            this.saveViewState();
                        }
                        if (this.mFragment.mView != null && this.mFragment.mContainer != null) {
                            SpecialEffectsController.getOrCreateController(this.mFragment.mContainer, this.mFragment.getParentFragmentManager()).enqueueRemove(this);
                        }
                        this.mFragment.mState = 3;
                        continue block22;
                    }
                    case 2: {
                        this.mFragment.mInLayout = false;
                        this.mFragment.mState = 2;
                        continue block22;
                    }
                    case 1: {
                        this.destroyFragmentView();
                        this.mFragment.mState = 1;
                        continue block22;
                    }
                    case 0: {
                        this.destroy();
                        continue block22;
                    }
                    case -1: 
                }
                this.detach();
            }
            if (FragmentManager.USE_STATE_MANAGER && this.mFragment.mHiddenChanged) {
                if (this.mFragment.mView != null && this.mFragment.mContainer != null) {
                    object = SpecialEffectsController.getOrCreateController(this.mFragment.mContainer, this.mFragment.getParentFragmentManager());
                    if (this.mFragment.mHidden) {
                        ((SpecialEffectsController)object).enqueueHide(this);
                    } else {
                        ((SpecialEffectsController)object).enqueueShow(this);
                    }
                }
                if (this.mFragment.mFragmentManager != null) {
                    this.mFragment.mFragmentManager.invalidateMenuForFragment(this.mFragment);
                }
                this.mFragment.mHiddenChanged = false;
                object = this.mFragment;
                ((Fragment)object).onHiddenChanged(((Fragment)object).mHidden);
            }
            this.mMovingToState = false;
            return;
        }
        catch (Throwable throwable) {
            this.mMovingToState = false;
            throw throwable;
        }
    }

    void pause() {
        if (FragmentManager.isLoggingEnabled(3)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("movefrom RESUMED: ");
            stringBuilder.append(this.mFragment);
            Log.d((String)TAG, (String)stringBuilder.toString());
        }
        this.mFragment.performPause();
        this.mDispatcher.dispatchOnFragmentPaused(this.mFragment, false);
    }

    void restoreState(ClassLoader object) {
        if (this.mFragment.mSavedFragmentState == null) {
            return;
        }
        this.mFragment.mSavedFragmentState.setClassLoader((ClassLoader)object);
        object = this.mFragment;
        ((Fragment)object).mSavedViewState = ((Fragment)object).mSavedFragmentState.getSparseParcelableArray(VIEW_STATE_TAG);
        object = this.mFragment;
        ((Fragment)object).mSavedViewRegistryState = ((Fragment)object).mSavedFragmentState.getBundle(VIEW_REGISTRY_STATE_TAG);
        object = this.mFragment;
        ((Fragment)object).mTargetWho = ((Fragment)object).mSavedFragmentState.getString(TARGET_STATE_TAG);
        if (this.mFragment.mTargetWho != null) {
            object = this.mFragment;
            ((Fragment)object).mTargetRequestCode = ((Fragment)object).mSavedFragmentState.getInt(TARGET_REQUEST_CODE_STATE_TAG, 0);
        }
        if (this.mFragment.mSavedUserVisibleHint != null) {
            object = this.mFragment;
            ((Fragment)object).mUserVisibleHint = ((Fragment)object).mSavedUserVisibleHint;
            this.mFragment.mSavedUserVisibleHint = null;
        } else {
            object = this.mFragment;
            ((Fragment)object).mUserVisibleHint = ((Fragment)object).mSavedFragmentState.getBoolean(USER_VISIBLE_HINT_TAG, true);
        }
        if (!this.mFragment.mUserVisibleHint) {
            this.mFragment.mDeferStart = true;
        }
    }

    void resume() {
        CharSequence charSequence;
        if (FragmentManager.isLoggingEnabled(3)) {
            charSequence = new StringBuilder();
            charSequence.append("moveto RESUMED: ");
            charSequence.append(this.mFragment);
            Log.d((String)TAG, (String)charSequence.toString());
        }
        if ((charSequence = this.mFragment.getFocusedView()) != null && this.isFragmentViewChild((View)charSequence)) {
            boolean bl = charSequence.requestFocus();
            if (FragmentManager.isLoggingEnabled(2)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("requestFocus: Restoring focused view ");
                stringBuilder.append((Object)charSequence);
                stringBuilder.append(" ");
                charSequence = bl ? "succeeded" : "failed";
                stringBuilder.append((String)charSequence);
                stringBuilder.append(" on Fragment ");
                stringBuilder.append(this.mFragment);
                stringBuilder.append(" resulting in focused view ");
                stringBuilder.append(this.mFragment.mView.findFocus());
                Log.v((String)TAG, (String)stringBuilder.toString());
            }
        }
        this.mFragment.setFocusedView(null);
        this.mFragment.performResume();
        this.mDispatcher.dispatchOnFragmentResumed(this.mFragment, false);
        this.mFragment.mSavedFragmentState = null;
        this.mFragment.mSavedViewState = null;
        this.mFragment.mSavedViewRegistryState = null;
    }

    Fragment.SavedState saveInstanceState() {
        int n = this.mFragment.mState;
        Fragment.SavedState savedState = null;
        if (n > -1) {
            Bundle bundle = this.saveBasicState();
            if (bundle != null) {
                savedState = new Fragment.SavedState(bundle);
            }
            return savedState;
        }
        return null;
    }

    FragmentState saveState() {
        FragmentState fragmentState = new FragmentState(this.mFragment);
        if (this.mFragment.mState > -1 && fragmentState.mSavedFragmentState == null) {
            fragmentState.mSavedFragmentState = this.saveBasicState();
            if (this.mFragment.mTargetWho != null) {
                if (fragmentState.mSavedFragmentState == null) {
                    fragmentState.mSavedFragmentState = new Bundle();
                }
                fragmentState.mSavedFragmentState.putString(TARGET_STATE_TAG, this.mFragment.mTargetWho);
                if (this.mFragment.mTargetRequestCode != 0) {
                    fragmentState.mSavedFragmentState.putInt(TARGET_REQUEST_CODE_STATE_TAG, this.mFragment.mTargetRequestCode);
                }
            }
        } else {
            fragmentState.mSavedFragmentState = this.mFragment.mSavedFragmentState;
        }
        return fragmentState;
    }

    void saveViewState() {
        if (this.mFragment.mView == null) {
            return;
        }
        SparseArray sparseArray = new SparseArray();
        this.mFragment.mView.saveHierarchyState(sparseArray);
        if (sparseArray.size() > 0) {
            this.mFragment.mSavedViewState = sparseArray;
        }
        sparseArray = new Bundle();
        this.mFragment.mViewLifecycleOwner.performSave((Bundle)sparseArray);
        if (!sparseArray.isEmpty()) {
            this.mFragment.mSavedViewRegistryState = sparseArray;
        }
    }

    void setFragmentManagerState(int n) {
        this.mFragmentManagerState = n;
    }

    void start() {
        if (FragmentManager.isLoggingEnabled(3)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("moveto STARTED: ");
            stringBuilder.append(this.mFragment);
            Log.d((String)TAG, (String)stringBuilder.toString());
        }
        this.mFragment.performStart();
        this.mDispatcher.dispatchOnFragmentStarted(this.mFragment, false);
    }

    void stop() {
        if (FragmentManager.isLoggingEnabled(3)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("movefrom STARTED: ");
            stringBuilder.append(this.mFragment);
            Log.d((String)TAG, (String)stringBuilder.toString());
        }
        this.mFragment.performStop();
        this.mDispatcher.dispatchOnFragmentStopped(this.mFragment, false);
    }
}

