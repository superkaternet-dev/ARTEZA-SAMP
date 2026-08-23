/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcelable
 *  android.view.View
 *  android.view.ViewGroup
 */
package androidx.fragment.app;

import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager.widget.PagerAdapter;

@Deprecated
public abstract class FragmentPagerAdapter
extends PagerAdapter {
    public static final int BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT = 1;
    @Deprecated
    public static final int BEHAVIOR_SET_USER_VISIBLE_HINT = 0;
    private static final boolean DEBUG = false;
    private static final String TAG = "FragmentPagerAdapter";
    private final int mBehavior;
    private FragmentTransaction mCurTransaction = null;
    private Fragment mCurrentPrimaryItem = null;
    private boolean mExecutingFinishUpdate;
    private final FragmentManager mFragmentManager;

    @Deprecated
    public FragmentPagerAdapter(FragmentManager fragmentManager) {
        this(fragmentManager, 0);
    }

    public FragmentPagerAdapter(FragmentManager fragmentManager, int n) {
        this.mFragmentManager = fragmentManager;
        this.mBehavior = n;
    }

    private static String makeFragmentName(int n, long l) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("android:switcher:");
        stringBuilder.append(n);
        stringBuilder.append(":");
        stringBuilder.append(l);
        return stringBuilder.toString();
    }

    @Override
    public void destroyItem(ViewGroup object, int n, Object object2) {
        object = (Fragment)object2;
        if (this.mCurTransaction == null) {
            this.mCurTransaction = this.mFragmentManager.beginTransaction();
        }
        this.mCurTransaction.detach((Fragment)object);
        if (((Fragment)object).equals(this.mCurrentPrimaryItem)) {
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

    public long getItemId(int n) {
        return n;
    }

    @Override
    public Object instantiateItem(ViewGroup object, int n) {
        if (this.mCurTransaction == null) {
            this.mCurTransaction = this.mFragmentManager.beginTransaction();
        }
        long l = this.getItemId(n);
        Object object2 = FragmentPagerAdapter.makeFragmentName(object.getId(), l);
        if ((object2 = this.mFragmentManager.findFragmentByTag((String)object2)) != null) {
            this.mCurTransaction.attach((Fragment)object2);
            object = object2;
        } else {
            object2 = this.getItem(n);
            this.mCurTransaction.add(object.getId(), (Fragment)object2, FragmentPagerAdapter.makeFragmentName(object.getId(), l));
            object = object2;
        }
        if (object != this.mCurrentPrimaryItem) {
            ((Fragment)object).setMenuVisibility(false);
            if (this.mBehavior == 1) {
                this.mCurTransaction.setMaxLifecycle((Fragment)object, Lifecycle.State.STARTED);
            } else {
                ((Fragment)object).setUserVisibleHint(false);
            }
        }
        return object;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        boolean bl = ((Fragment)object).getView() == view;
        return bl;
    }

    @Override
    public void restoreState(Parcelable parcelable, ClassLoader classLoader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void setPrimaryItem(ViewGroup object, int n, Object object2) {
        object = this.mCurrentPrimaryItem;
        if ((object2 = (Fragment)object2) != object) {
            if (object != null) {
                ((Fragment)object).setMenuVisibility(false);
                if (this.mBehavior == 1) {
                    if (this.mCurTransaction == null) {
                        this.mCurTransaction = this.mFragmentManager.beginTransaction();
                    }
                    this.mCurTransaction.setMaxLifecycle(this.mCurrentPrimaryItem, Lifecycle.State.STARTED);
                } else {
                    this.mCurrentPrimaryItem.setUserVisibleHint(false);
                }
            }
            ((Fragment)object2).setMenuVisibility(true);
            if (this.mBehavior == 1) {
                if (this.mCurTransaction == null) {
                    this.mCurTransaction = this.mFragmentManager.beginTransaction();
                }
                this.mCurTransaction.setMaxLifecycle((Fragment)object2, Lifecycle.State.RESUMED);
            } else {
                ((Fragment)object2).setUserVisibleHint(true);
            }
            this.mCurrentPrimaryItem = object2;
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

