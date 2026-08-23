/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  android.os.Parcelable
 *  android.util.Log
 *  android.view.View
 *  android.view.ViewGroup
 */
package androidx.fragment.app;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager.widget.PagerAdapter;
import java.util.ArrayList;

@Deprecated
public abstract class FragmentStatePagerAdapter
extends PagerAdapter {
    public static final int BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT = 1;
    @Deprecated
    public static final int BEHAVIOR_SET_USER_VISIBLE_HINT = 0;
    private static final boolean DEBUG = false;
    private static final String TAG = "FragmentStatePagerAdapt";
    private final int mBehavior;
    private FragmentTransaction mCurTransaction = null;
    private Fragment mCurrentPrimaryItem = null;
    private boolean mExecutingFinishUpdate;
    private final FragmentManager mFragmentManager;
    private ArrayList<Fragment> mFragments;
    private ArrayList<Fragment.SavedState> mSavedState = new ArrayList();

    @Deprecated
    public FragmentStatePagerAdapter(FragmentManager fragmentManager) {
        this(fragmentManager, 0);
    }

    public FragmentStatePagerAdapter(FragmentManager fragmentManager, int n) {
        this.mFragments = new ArrayList();
        this.mFragmentManager = fragmentManager;
        this.mBehavior = n;
    }

    @Override
    public void destroyItem(ViewGroup object, int n, Object object2) {
        object2 = (Fragment)object2;
        if (this.mCurTransaction == null) {
            this.mCurTransaction = this.mFragmentManager.beginTransaction();
        }
        while (this.mSavedState.size() <= n) {
            this.mSavedState.add(null);
        }
        ArrayList<Fragment.SavedState> arrayList = this.mSavedState;
        object = ((Fragment)object2).isAdded() ? this.mFragmentManager.saveFragmentInstanceState((Fragment)object2) : null;
        arrayList.set(n, (Fragment.SavedState)object);
        this.mFragments.set(n, null);
        this.mCurTransaction.remove((Fragment)object2);
        if (((Fragment)object2).equals(this.mCurrentPrimaryItem)) {
            this.mCurrentPrimaryItem = null;
        }
    }

    @Override
    public void finishUpdate(ViewGroup object) {
        object = this.mCurTransaction;
        if (object != null) {
            if (!this.mExecutingFinishUpdate) {
                try {
                    this.mExecutingFinishUpdate = true;
                    ((FragmentTransaction)object).commitNowAllowingStateLoss();
                }
                finally {
                    this.mExecutingFinishUpdate = false;
                }
            }
            this.mCurTransaction = null;
        }
    }

    public abstract Fragment getItem(int var1);

    @Override
    public Object instantiateItem(ViewGroup viewGroup, int n) {
        Object object;
        if (this.mFragments.size() > n && (object = this.mFragments.get(n)) != null) {
            return object;
        }
        if (this.mCurTransaction == null) {
            this.mCurTransaction = this.mFragmentManager.beginTransaction();
        }
        Fragment fragment = this.getItem(n);
        if (this.mSavedState.size() > n && (object = this.mSavedState.get(n)) != null) {
            fragment.setInitialSavedState((Fragment.SavedState)object);
        }
        while (this.mFragments.size() <= n) {
            this.mFragments.add(null);
        }
        fragment.setMenuVisibility(false);
        if (this.mBehavior == 0) {
            fragment.setUserVisibleHint(false);
        }
        this.mFragments.set(n, fragment);
        this.mCurTransaction.add(viewGroup.getId(), fragment);
        if (this.mBehavior == 1) {
            this.mCurTransaction.setMaxLifecycle(fragment, Lifecycle.State.STARTED);
        }
        return fragment;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        boolean bl = ((Fragment)object).getView() == view;
        return bl;
    }

    @Override
    public void restoreState(Parcelable parcelable, ClassLoader object2) {
        if (parcelable != null) {
            int n;
            parcelable = (Bundle)parcelable;
            parcelable.setClassLoader((ClassLoader)object2);
            object2 = parcelable.getParcelableArray("states");
            this.mSavedState.clear();
            this.mFragments.clear();
            if (object2 != null) {
                for (n = 0; n < ((Parcelable[])object2).length; ++n) {
                    this.mSavedState.add((Fragment.SavedState)object2[n]);
                }
            }
            for (Object object2 : parcelable.keySet()) {
                if (!((String)object2).startsWith("f")) continue;
                n = Integer.parseInt(((String)object2).substring(1));
                Object object3 = this.mFragmentManager.getFragment((Bundle)parcelable, (String)object2);
                if (object3 != null) {
                    while (this.mFragments.size() <= n) {
                        this.mFragments.add(null);
                    }
                    ((Fragment)object3).setMenuVisibility(false);
                    this.mFragments.set(n, (Fragment)object3);
                    continue;
                }
                object3 = new StringBuilder();
                ((StringBuilder)object3).append("Bad fragment at key ");
                ((StringBuilder)object3).append((String)object2);
                Log.w((String)TAG, (String)((StringBuilder)object3).toString());
            }
        }
    }

    @Override
    public Parcelable saveState() {
        Bundle bundle;
        Object object = null;
        if (this.mSavedState.size() > 0) {
            object = new Bundle();
            bundle = new Fragment.SavedState[this.mSavedState.size()];
            this.mSavedState.toArray((T[])bundle);
            object.putParcelableArray("states", (Parcelable[])bundle);
        }
        for (int i = 0; i < this.mFragments.size(); ++i) {
            Fragment fragment = this.mFragments.get(i);
            bundle = object;
            if (fragment != null) {
                bundle = object;
                if (fragment.isAdded()) {
                    bundle = object;
                    if (object == null) {
                        bundle = new Bundle();
                    }
                    object = new StringBuilder();
                    ((StringBuilder)object).append("f");
                    ((StringBuilder)object).append(i);
                    object = ((StringBuilder)object).toString();
                    this.mFragmentManager.putFragment(bundle, (String)object, fragment);
                }
            }
            object = bundle;
        }
        return object;
    }

    @Override
    public void setPrimaryItem(ViewGroup object, int n, Object object2) {
        object = (Fragment)object2;
        if (object != (object2 = this.mCurrentPrimaryItem)) {
            if (object2 != null) {
                ((Fragment)object2).setMenuVisibility(false);
                if (this.mBehavior == 1) {
                    if (this.mCurTransaction == null) {
                        this.mCurTransaction = this.mFragmentManager.beginTransaction();
                    }
                    this.mCurTransaction.setMaxLifecycle(this.mCurrentPrimaryItem, Lifecycle.State.STARTED);
                } else {
                    this.mCurrentPrimaryItem.setUserVisibleHint(false);
                }
            }
            ((Fragment)object).setMenuVisibility(true);
            if (this.mBehavior == 1) {
                if (this.mCurTransaction == null) {
                    this.mCurTransaction = this.mFragmentManager.beginTransaction();
                }
                this.mCurTransaction.setMaxLifecycle((Fragment)object, Lifecycle.State.RESUMED);
            } else {
                ((Fragment)object).setUserVisibleHint(true);
            }
            this.mCurrentPrimaryItem = object;
        }
    }

    @Override
    public void startUpdate(ViewGroup object) {
        if (object.getId() != -1) {
            return;
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("ViewPager with adapter ");
        ((StringBuilder)object).append(this);
        ((StringBuilder)object).append(" requires a view id");
        throw new IllegalStateException(((StringBuilder)object).toString());
    }
}

