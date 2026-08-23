/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package androidx.fragment.app;

import android.util.Log;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.LogWriter;
import androidx.lifecycle.Lifecycle;
import java.io.PrintWriter;
import java.util.ArrayList;

final class BackStackRecord
extends FragmentTransaction
implements FragmentManager.BackStackEntry,
FragmentManager.OpGenerator {
    private static final String TAG = "FragmentManager";
    boolean mCommitted;
    int mIndex;
    final FragmentManager mManager;

    BackStackRecord(FragmentManager fragmentManager) {
        FragmentFactory fragmentFactory = fragmentManager.getFragmentFactory();
        ClassLoader classLoader = fragmentManager.getHost() != null ? fragmentManager.getHost().getContext().getClassLoader() : null;
        super(fragmentFactory, classLoader);
        this.mIndex = -1;
        this.mManager = fragmentManager;
    }

    private static boolean isFragmentPostponed(FragmentTransaction.Op object) {
        object = ((FragmentTransaction.Op)object).mFragment;
        boolean bl = object != null && ((Fragment)object).mAdded && ((Fragment)object).mView != null && !((Fragment)object).mDetached && !((Fragment)object).mHidden && ((Fragment)object).isPostponed();
        return bl;
    }

    void bumpBackStackNesting(int n) {
        Object object;
        if (!this.mAddToBackStack) {
            return;
        }
        if (FragmentManager.isLoggingEnabled(2)) {
            object = new StringBuilder();
            ((StringBuilder)object).append("Bump nesting in ");
            ((StringBuilder)object).append(this);
            ((StringBuilder)object).append(" by ");
            ((StringBuilder)object).append(n);
            Log.v((String)TAG, (String)((StringBuilder)object).toString());
        }
        int n2 = this.mOps.size();
        for (int i = 0; i < n2; ++i) {
            object = (FragmentTransaction.Op)this.mOps.get(i);
            if (((FragmentTransaction.Op)object).mFragment == null) continue;
            Object object2 = ((FragmentTransaction.Op)object).mFragment;
            ((Fragment)object2).mBackStackNesting += n;
            if (!FragmentManager.isLoggingEnabled(2)) continue;
            object2 = new StringBuilder();
            ((StringBuilder)object2).append("Bump nesting of ");
            ((StringBuilder)object2).append(((FragmentTransaction.Op)object).mFragment);
            ((StringBuilder)object2).append(" to ");
            ((StringBuilder)object2).append(((FragmentTransaction.Op)object).mFragment.mBackStackNesting);
            Log.v((String)TAG, (String)((StringBuilder)object2).toString());
        }
    }

    @Override
    public int commit() {
        return this.commitInternal(false);
    }

    @Override
    public int commitAllowingStateLoss() {
        return this.commitInternal(true);
    }

    int commitInternal(boolean bl) {
        if (!this.mCommitted) {
            if (FragmentManager.isLoggingEnabled(2)) {
                Appendable appendable = new StringBuilder();
                ((StringBuilder)appendable).append("Commit: ");
                ((StringBuilder)appendable).append(this);
                Log.v((String)TAG, (String)((StringBuilder)appendable).toString());
                appendable = new PrintWriter(new LogWriter(TAG));
                this.dump("  ", (PrintWriter)appendable);
                ((PrintWriter)appendable).close();
            }
            this.mCommitted = true;
            this.mIndex = this.mAddToBackStack ? this.mManager.allocBackStackIndex() : -1;
            this.mManager.enqueueAction(this, bl);
            return this.mIndex;
        }
        throw new IllegalStateException("commit already called");
    }

    @Override
    public void commitNow() {
        this.disallowAddToBackStack();
        this.mManager.execSingleAction(this, false);
    }

    @Override
    public void commitNowAllowingStateLoss() {
        this.disallowAddToBackStack();
        this.mManager.execSingleAction(this, true);
    }

    @Override
    public FragmentTransaction detach(Fragment fragment) {
        if (fragment.mFragmentManager != null && fragment.mFragmentManager != this.mManager) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Cannot detach Fragment attached to a different FragmentManager. Fragment ");
            stringBuilder.append(fragment.toString());
            stringBuilder.append(" is already attached to a FragmentManager.");
            throw new IllegalStateException(stringBuilder.toString());
        }
        return super.detach(fragment);
    }

    @Override
    void doAddOp(int n, Fragment fragment, String string2, int n2) {
        super.doAddOp(n, fragment, string2, n2);
        fragment.mFragmentManager = this.mManager;
    }

    public void dump(String string2, PrintWriter printWriter) {
        this.dump(string2, printWriter, true);
    }

    public void dump(String string2, PrintWriter printWriter, boolean bl) {
        if (bl) {
            printWriter.print(string2);
            printWriter.print("mName=");
            printWriter.print(this.mName);
            printWriter.print(" mIndex=");
            printWriter.print(this.mIndex);
            printWriter.print(" mCommitted=");
            printWriter.println(this.mCommitted);
            if (this.mTransition != 0) {
                printWriter.print(string2);
                printWriter.print("mTransition=#");
                printWriter.print(Integer.toHexString(this.mTransition));
            }
            if (this.mEnterAnim != 0 || this.mExitAnim != 0) {
                printWriter.print(string2);
                printWriter.print("mEnterAnim=#");
                printWriter.print(Integer.toHexString(this.mEnterAnim));
                printWriter.print(" mExitAnim=#");
                printWriter.println(Integer.toHexString(this.mExitAnim));
            }
            if (this.mPopEnterAnim != 0 || this.mPopExitAnim != 0) {
                printWriter.print(string2);
                printWriter.print("mPopEnterAnim=#");
                printWriter.print(Integer.toHexString(this.mPopEnterAnim));
                printWriter.print(" mPopExitAnim=#");
                printWriter.println(Integer.toHexString(this.mPopExitAnim));
            }
            if (this.mBreadCrumbTitleRes != 0 || this.mBreadCrumbTitleText != null) {
                printWriter.print(string2);
                printWriter.print("mBreadCrumbTitleRes=#");
                printWriter.print(Integer.toHexString(this.mBreadCrumbTitleRes));
                printWriter.print(" mBreadCrumbTitleText=");
                printWriter.println(this.mBreadCrumbTitleText);
            }
            if (this.mBreadCrumbShortTitleRes != 0 || this.mBreadCrumbShortTitleText != null) {
                printWriter.print(string2);
                printWriter.print("mBreadCrumbShortTitleRes=#");
                printWriter.print(Integer.toHexString(this.mBreadCrumbShortTitleRes));
                printWriter.print(" mBreadCrumbShortTitleText=");
                printWriter.println(this.mBreadCrumbShortTitleText);
            }
        }
        if (!this.mOps.isEmpty()) {
            printWriter.print(string2);
            printWriter.println("Operations:");
            int n = this.mOps.size();
            for (int i = 0; i < n; ++i) {
                CharSequence charSequence;
                FragmentTransaction.Op op = (FragmentTransaction.Op)this.mOps.get(i);
                switch (op.mCmd) {
                    default: {
                        charSequence = new StringBuilder();
                        ((StringBuilder)charSequence).append("cmd=");
                        ((StringBuilder)charSequence).append(op.mCmd);
                        charSequence = ((StringBuilder)charSequence).toString();
                        break;
                    }
                    case 10: {
                        charSequence = "OP_SET_MAX_LIFECYCLE";
                        break;
                    }
                    case 9: {
                        charSequence = "UNSET_PRIMARY_NAV";
                        break;
                    }
                    case 8: {
                        charSequence = "SET_PRIMARY_NAV";
                        break;
                    }
                    case 7: {
                        charSequence = "ATTACH";
                        break;
                    }
                    case 6: {
                        charSequence = "DETACH";
                        break;
                    }
                    case 5: {
                        charSequence = "SHOW";
                        break;
                    }
                    case 4: {
                        charSequence = "HIDE";
                        break;
                    }
                    case 3: {
                        charSequence = "REMOVE";
                        break;
                    }
                    case 2: {
                        charSequence = "REPLACE";
                        break;
                    }
                    case 1: {
                        charSequence = "ADD";
                        break;
                    }
                    case 0: {
                        charSequence = "NULL";
                    }
                }
                printWriter.print(string2);
                printWriter.print("  Op #");
                printWriter.print(i);
                printWriter.print(": ");
                printWriter.print((String)charSequence);
                printWriter.print(" ");
                printWriter.println(op.mFragment);
                if (!bl) continue;
                if (op.mEnterAnim != 0 || op.mExitAnim != 0) {
                    printWriter.print(string2);
                    printWriter.print("enterAnim=#");
                    printWriter.print(Integer.toHexString(op.mEnterAnim));
                    printWriter.print(" exitAnim=#");
                    printWriter.println(Integer.toHexString(op.mExitAnim));
                }
                if (op.mPopEnterAnim == 0 && op.mPopExitAnim == 0) continue;
                printWriter.print(string2);
                printWriter.print("popEnterAnim=#");
                printWriter.print(Integer.toHexString(op.mPopEnterAnim));
                printWriter.print(" popExitAnim=#");
                printWriter.println(Integer.toHexString(op.mPopExitAnim));
            }
        }
    }

    void executeOps() {
        Object object;
        int n = this.mOps.size();
        for (int i = 0; i < n; ++i) {
            object = (FragmentTransaction.Op)this.mOps.get(i);
            Object object2 = ((FragmentTransaction.Op)object).mFragment;
            if (object2 != null) {
                ((Fragment)object2).setNextTransition(this.mTransition);
                ((Fragment)object2).setSharedElementNames(this.mSharedElementSourceNames, this.mSharedElementTargetNames);
            }
            switch (((FragmentTransaction.Op)object).mCmd) {
                default: {
                    object2 = new StringBuilder();
                    ((StringBuilder)object2).append("Unknown cmd: ");
                    ((StringBuilder)object2).append(((FragmentTransaction.Op)object).mCmd);
                    throw new IllegalArgumentException(((StringBuilder)object2).toString());
                }
                case 10: {
                    this.mManager.setMaxLifecycle((Fragment)object2, ((FragmentTransaction.Op)object).mCurrentMaxState);
                    break;
                }
                case 9: {
                    this.mManager.setPrimaryNavigationFragment(null);
                    break;
                }
                case 8: {
                    this.mManager.setPrimaryNavigationFragment((Fragment)object2);
                    break;
                }
                case 7: {
                    ((Fragment)object2).setNextAnim(((FragmentTransaction.Op)object).mEnterAnim);
                    this.mManager.setExitAnimationOrder((Fragment)object2, false);
                    this.mManager.attachFragment((Fragment)object2);
                    break;
                }
                case 6: {
                    ((Fragment)object2).setNextAnim(((FragmentTransaction.Op)object).mExitAnim);
                    this.mManager.detachFragment((Fragment)object2);
                    break;
                }
                case 5: {
                    ((Fragment)object2).setNextAnim(((FragmentTransaction.Op)object).mEnterAnim);
                    this.mManager.setExitAnimationOrder((Fragment)object2, false);
                    this.mManager.showFragment((Fragment)object2);
                    break;
                }
                case 4: {
                    ((Fragment)object2).setNextAnim(((FragmentTransaction.Op)object).mExitAnim);
                    this.mManager.hideFragment((Fragment)object2);
                    break;
                }
                case 3: {
                    ((Fragment)object2).setNextAnim(((FragmentTransaction.Op)object).mExitAnim);
                    this.mManager.removeFragment((Fragment)object2);
                    break;
                }
                case 1: {
                    ((Fragment)object2).setNextAnim(((FragmentTransaction.Op)object).mEnterAnim);
                    this.mManager.setExitAnimationOrder((Fragment)object2, false);
                    this.mManager.addFragment((Fragment)object2);
                }
            }
            if (this.mReorderingAllowed || ((FragmentTransaction.Op)object).mCmd == 1 || object2 == null || FragmentManager.USE_STATE_MANAGER) continue;
            this.mManager.moveFragmentToExpectedState((Fragment)object2);
        }
        if (!this.mReorderingAllowed && !FragmentManager.USE_STATE_MANAGER) {
            object = this.mManager;
            ((FragmentManager)object).moveToState(((FragmentManager)object).mCurState, true);
        }
    }

    void executePopOps(boolean bl) {
        Object object;
        for (int i = this.mOps.size() - 1; i >= 0; --i) {
            object = (FragmentTransaction.Op)this.mOps.get(i);
            Object object2 = ((FragmentTransaction.Op)object).mFragment;
            if (object2 != null) {
                ((Fragment)object2).setNextTransition(FragmentManager.reverseTransit(this.mTransition));
                ((Fragment)object2).setSharedElementNames(this.mSharedElementTargetNames, this.mSharedElementSourceNames);
            }
            switch (((FragmentTransaction.Op)object).mCmd) {
                default: {
                    object2 = new StringBuilder();
                    ((StringBuilder)object2).append("Unknown cmd: ");
                    ((StringBuilder)object2).append(((FragmentTransaction.Op)object).mCmd);
                    throw new IllegalArgumentException(((StringBuilder)object2).toString());
                }
                case 10: {
                    this.mManager.setMaxLifecycle((Fragment)object2, ((FragmentTransaction.Op)object).mOldMaxState);
                    break;
                }
                case 9: {
                    this.mManager.setPrimaryNavigationFragment((Fragment)object2);
                    break;
                }
                case 8: {
                    this.mManager.setPrimaryNavigationFragment(null);
                    break;
                }
                case 7: {
                    ((Fragment)object2).setNextAnim(((FragmentTransaction.Op)object).mPopExitAnim);
                    this.mManager.setExitAnimationOrder((Fragment)object2, true);
                    this.mManager.detachFragment((Fragment)object2);
                    break;
                }
                case 6: {
                    ((Fragment)object2).setNextAnim(((FragmentTransaction.Op)object).mPopEnterAnim);
                    this.mManager.attachFragment((Fragment)object2);
                    break;
                }
                case 5: {
                    ((Fragment)object2).setNextAnim(((FragmentTransaction.Op)object).mPopExitAnim);
                    this.mManager.setExitAnimationOrder((Fragment)object2, true);
                    this.mManager.hideFragment((Fragment)object2);
                    break;
                }
                case 4: {
                    ((Fragment)object2).setNextAnim(((FragmentTransaction.Op)object).mPopEnterAnim);
                    this.mManager.showFragment((Fragment)object2);
                    break;
                }
                case 3: {
                    ((Fragment)object2).setNextAnim(((FragmentTransaction.Op)object).mPopEnterAnim);
                    this.mManager.addFragment((Fragment)object2);
                    break;
                }
                case 1: {
                    ((Fragment)object2).setNextAnim(((FragmentTransaction.Op)object).mPopExitAnim);
                    this.mManager.setExitAnimationOrder((Fragment)object2, true);
                    this.mManager.removeFragment((Fragment)object2);
                }
            }
            if (this.mReorderingAllowed || ((FragmentTransaction.Op)object).mCmd == 3 || object2 == null || FragmentManager.USE_STATE_MANAGER) continue;
            this.mManager.moveFragmentToExpectedState((Fragment)object2);
        }
        if (!this.mReorderingAllowed && bl && !FragmentManager.USE_STATE_MANAGER) {
            object = this.mManager;
            ((FragmentManager)object).moveToState(((FragmentManager)object).mCurState, true);
        }
    }

    Fragment expandOps(ArrayList<Fragment> arrayList, Fragment object) {
        int n = 0;
        Fragment fragment = object;
        while (n < this.mOps.size()) {
            int n2;
            FragmentTransaction.Op op = (FragmentTransaction.Op)this.mOps.get(n);
            switch (op.mCmd) {
                default: {
                    n2 = n;
                    object = fragment;
                    break;
                }
                case 8: {
                    this.mOps.add(n, new FragmentTransaction.Op(9, fragment));
                    n2 = n + 1;
                    object = op.mFragment;
                    break;
                }
                case 3: 
                case 6: {
                    arrayList.remove(op.mFragment);
                    n2 = n;
                    object = fragment;
                    if (op.mFragment != fragment) break;
                    this.mOps.add(n, new FragmentTransaction.Op(9, op.mFragment));
                    n2 = n + 1;
                    object = null;
                    break;
                }
                case 2: {
                    Fragment fragment2 = op.mFragment;
                    int n3 = fragment2.mContainerId;
                    boolean bl = false;
                    object = fragment;
                    for (n2 = arrayList.size() - 1; n2 >= 0; --n2) {
                        Fragment fragment3 = arrayList.get(n2);
                        int n4 = n;
                        boolean bl2 = bl;
                        fragment = object;
                        if (fragment3.mContainerId == n3) {
                            if (fragment3 == fragment2) {
                                bl2 = true;
                                n4 = n;
                                fragment = object;
                            } else {
                                n4 = n;
                                fragment = object;
                                if (fragment3 == object) {
                                    this.mOps.add(n, new FragmentTransaction.Op(9, fragment3));
                                    n4 = n + 1;
                                    fragment = null;
                                }
                                object = new FragmentTransaction.Op(3, fragment3);
                                ((FragmentTransaction.Op)object).mEnterAnim = op.mEnterAnim;
                                ((FragmentTransaction.Op)object).mPopEnterAnim = op.mPopEnterAnim;
                                ((FragmentTransaction.Op)object).mExitAnim = op.mExitAnim;
                                ((FragmentTransaction.Op)object).mPopExitAnim = op.mPopExitAnim;
                                this.mOps.add(n4, object);
                                arrayList.remove(fragment3);
                                bl2 = bl;
                            }
                        }
                        n = ++n4;
                        bl = bl2;
                        object = fragment;
                    }
                    if (bl) {
                        this.mOps.remove(n);
                        --n;
                    } else {
                        op.mCmd = 1;
                        arrayList.add(fragment2);
                    }
                    n2 = n;
                    break;
                }
                case 1: 
                case 7: {
                    arrayList.add(op.mFragment);
                    object = fragment;
                    n2 = n;
                }
            }
            n = n2 + 1;
            fragment = object;
        }
        return fragment;
    }

    @Override
    public boolean generateOps(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2) {
        if (FragmentManager.isLoggingEnabled(2)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Run: ");
            stringBuilder.append(this);
            Log.v((String)TAG, (String)stringBuilder.toString());
        }
        arrayList.add(this);
        arrayList2.add(false);
        if (this.mAddToBackStack) {
            this.mManager.addBackStackState(this);
        }
        return true;
    }

    @Override
    public CharSequence getBreadCrumbShortTitle() {
        if (this.mBreadCrumbShortTitleRes != 0) {
            return this.mManager.getHost().getContext().getText(this.mBreadCrumbShortTitleRes);
        }
        return this.mBreadCrumbShortTitleText;
    }

    @Override
    public int getBreadCrumbShortTitleRes() {
        return this.mBreadCrumbShortTitleRes;
    }

    @Override
    public CharSequence getBreadCrumbTitle() {
        if (this.mBreadCrumbTitleRes != 0) {
            return this.mManager.getHost().getContext().getText(this.mBreadCrumbTitleRes);
        }
        return this.mBreadCrumbTitleText;
    }

    @Override
    public int getBreadCrumbTitleRes() {
        return this.mBreadCrumbTitleRes;
    }

    @Override
    public int getId() {
        return this.mIndex;
    }

    @Override
    public String getName() {
        return this.mName;
    }

    @Override
    public FragmentTransaction hide(Fragment fragment) {
        if (fragment.mFragmentManager != null && fragment.mFragmentManager != this.mManager) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Cannot hide Fragment attached to a different FragmentManager. Fragment ");
            stringBuilder.append(fragment.toString());
            stringBuilder.append(" is already attached to a FragmentManager.");
            throw new IllegalStateException(stringBuilder.toString());
        }
        return super.hide(fragment);
    }

    boolean interactsWith(int n) {
        int n2 = this.mOps.size();
        int n3 = 0;
        while (true) {
            int n4 = 0;
            if (n3 >= n2) break;
            FragmentTransaction.Op op = (FragmentTransaction.Op)this.mOps.get(n3);
            if (op.mFragment != null) {
                n4 = op.mFragment.mContainerId;
            }
            if (n4 != 0 && n4 == n) {
                return true;
            }
            ++n3;
        }
        return false;
    }

    boolean interactsWith(ArrayList<BackStackRecord> arrayList, int n, int n2) {
        if (n2 == n) {
            return false;
        }
        int n3 = this.mOps.size();
        int n4 = -1;
        for (int i = 0; i < n3; ++i) {
            FragmentTransaction.Op op = (FragmentTransaction.Op)this.mOps.get(i);
            int n5 = op.mFragment != null ? op.mFragment.mContainerId : 0;
            int n6 = n4;
            if (n5 != 0) {
                n6 = n4;
                if (n5 != n4) {
                    n4 = n5;
                    int n7 = n;
                    while (true) {
                        n6 = n4;
                        if (n7 >= n2) break;
                        BackStackRecord backStackRecord = arrayList.get(n7);
                        int n8 = backStackRecord.mOps.size();
                        for (n6 = 0; n6 < n8; ++n6) {
                            op = (FragmentTransaction.Op)backStackRecord.mOps.get(n6);
                            int n9 = op.mFragment != null ? op.mFragment.mContainerId : 0;
                            if (n9 != n5) continue;
                            return true;
                        }
                        ++n7;
                    }
                }
            }
            n4 = n6;
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return this.mOps.isEmpty();
    }

    boolean isPostponed() {
        for (int i = 0; i < this.mOps.size(); ++i) {
            if (!BackStackRecord.isFragmentPostponed((FragmentTransaction.Op)this.mOps.get(i))) continue;
            return true;
        }
        return false;
    }

    @Override
    public FragmentTransaction remove(Fragment fragment) {
        if (fragment.mFragmentManager != null && fragment.mFragmentManager != this.mManager) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Cannot remove Fragment attached to a different FragmentManager. Fragment ");
            stringBuilder.append(fragment.toString());
            stringBuilder.append(" is already attached to a FragmentManager.");
            throw new IllegalStateException(stringBuilder.toString());
        }
        return super.remove(fragment);
    }

    public void runOnCommitRunnables() {
        if (this.mCommitRunnables != null) {
            for (int i = 0; i < this.mCommitRunnables.size(); ++i) {
                ((Runnable)this.mCommitRunnables.get(i)).run();
            }
            this.mCommitRunnables = null;
        }
    }

    @Override
    public FragmentTransaction setMaxLifecycle(Fragment object, Lifecycle.State state) {
        if (((Fragment)object).mFragmentManager == this.mManager) {
            if (state == Lifecycle.State.INITIALIZED && ((Fragment)object).mState > -1) {
                object = new StringBuilder();
                ((StringBuilder)object).append("Cannot set maximum Lifecycle to ");
                ((StringBuilder)object).append((Object)state);
                ((StringBuilder)object).append(" after the Fragment has been created");
                throw new IllegalArgumentException(((StringBuilder)object).toString());
            }
            if (state != Lifecycle.State.DESTROYED) {
                return super.setMaxLifecycle((Fragment)object, state);
            }
            object = new StringBuilder();
            ((StringBuilder)object).append("Cannot set maximum Lifecycle to ");
            ((StringBuilder)object).append((Object)state);
            ((StringBuilder)object).append(". Use remove() to remove the fragment from the FragmentManager and trigger its destruction.");
            throw new IllegalArgumentException(((StringBuilder)object).toString());
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("Cannot setMaxLifecycle for Fragment not attached to FragmentManager ");
        ((StringBuilder)object).append(this.mManager);
        throw new IllegalArgumentException(((StringBuilder)object).toString());
    }

    void setOnStartPostponedListener(Fragment.OnStartEnterTransitionListener onStartEnterTransitionListener) {
        for (int i = 0; i < this.mOps.size(); ++i) {
            FragmentTransaction.Op op = (FragmentTransaction.Op)this.mOps.get(i);
            if (!BackStackRecord.isFragmentPostponed(op)) continue;
            op.mFragment.setOnStartEnterTransitionListener(onStartEnterTransitionListener);
        }
    }

    @Override
    public FragmentTransaction setPrimaryNavigationFragment(Fragment fragment) {
        if (fragment != null && fragment.mFragmentManager != null && fragment.mFragmentManager != this.mManager) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Cannot setPrimaryNavigation for Fragment attached to a different FragmentManager. Fragment ");
            stringBuilder.append(fragment.toString());
            stringBuilder.append(" is already attached to a FragmentManager.");
            throw new IllegalStateException(stringBuilder.toString());
        }
        return super.setPrimaryNavigationFragment(fragment);
    }

    @Override
    public FragmentTransaction show(Fragment fragment) {
        if (fragment.mFragmentManager != null && fragment.mFragmentManager != this.mManager) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Cannot show Fragment attached to a different FragmentManager. Fragment ");
            stringBuilder.append(fragment.toString());
            stringBuilder.append(" is already attached to a FragmentManager.");
            throw new IllegalStateException(stringBuilder.toString());
        }
        return super.show(fragment);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(128);
        stringBuilder.append("BackStackEntry{");
        stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
        if (this.mIndex >= 0) {
            stringBuilder.append(" #");
            stringBuilder.append(this.mIndex);
        }
        if (this.mName != null) {
            stringBuilder.append(" ");
            stringBuilder.append(this.mName);
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    Fragment trackAddedFragmentsInPop(ArrayList<Fragment> arrayList, Fragment fragment) {
        block7: for (int i = this.mOps.size() - 1; i >= 0; --i) {
            FragmentTransaction.Op op = (FragmentTransaction.Op)this.mOps.get(i);
            switch (op.mCmd) {
                default: {
                    continue block7;
                }
                case 10: {
                    op.mCurrentMaxState = op.mOldMaxState;
                    continue block7;
                }
                case 9: {
                    fragment = op.mFragment;
                    continue block7;
                }
                case 8: {
                    fragment = null;
                    continue block7;
                }
                case 3: 
                case 6: {
                    arrayList.add(op.mFragment);
                    continue block7;
                }
                case 1: 
                case 7: {
                    arrayList.remove(op.mFragment);
                }
            }
        }
        return fragment;
    }
}

