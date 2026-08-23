/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.TypedArray
 *  android.os.Bundle
 *  android.os.Handler
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.text.TextUtils
 *  android.util.AttributeSet
 *  android.util.Log
 */
package androidx.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import androidx.collection.SimpleArrayMap;
import androidx.core.content.res.TypedArrayUtils;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;
import androidx.preference.R;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class PreferenceGroup
extends Preference {
    private static final String TAG = "PreferenceGroup";
    private boolean mAttachedToHierarchy = false;
    private final Runnable mClearRecycleCacheRunnable;
    private int mCurrentPreferenceOrder = 0;
    private final Handler mHandler;
    final SimpleArrayMap<String, Long> mIdRecycleCache = new SimpleArrayMap();
    private int mInitialExpandedChildrenCount;
    private OnExpandButtonClickListener mOnExpandButtonClickListener = null;
    private boolean mOrderingAsAdded = true;
    private List<Preference> mPreferences;

    public PreferenceGroup(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public PreferenceGroup(Context context, AttributeSet attributeSet, int n) {
        this(context, attributeSet, n, 0);
    }

    public PreferenceGroup(Context context, AttributeSet attributeSet, int n, int n2) {
        super(context, attributeSet, n, n2);
        this.mHandler = new Handler();
        this.mInitialExpandedChildrenCount = Integer.MAX_VALUE;
        this.mClearRecycleCacheRunnable = new Runnable(this){
            final PreferenceGroup this$0;
            {
                this.this$0 = preferenceGroup;
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public void run() {
                synchronized (this) {
                    this.this$0.mIdRecycleCache.clear();
                    return;
                }
            }
        };
        this.mPreferences = new ArrayList<Preference>();
        context = context.obtainStyledAttributes(attributeSet, R.styleable.PreferenceGroup, n, n2);
        this.mOrderingAsAdded = TypedArrayUtils.getBoolean((TypedArray)context, R.styleable.PreferenceGroup_orderingFromXml, R.styleable.PreferenceGroup_orderingFromXml, true);
        if (context.hasValue(R.styleable.PreferenceGroup_initialExpandedChildrenCount)) {
            this.setInitialExpandedChildrenCount(TypedArrayUtils.getInt((TypedArray)context, R.styleable.PreferenceGroup_initialExpandedChildrenCount, R.styleable.PreferenceGroup_initialExpandedChildrenCount, Integer.MAX_VALUE));
        }
        context.recycle();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private boolean removePreferenceInt(Preference preference) {
        synchronized (this) {
            boolean bl;
            preference.onPrepareForRemoval();
            if (preference.getParent() == this) {
                preference.assignParent(null);
            }
            if (bl = this.mPreferences.remove(preference)) {
                String string2 = preference.getKey();
                if (string2 != null) {
                    this.mIdRecycleCache.put(string2, preference.getId());
                    this.mHandler.removeCallbacks(this.mClearRecycleCacheRunnable);
                    this.mHandler.post(this.mClearRecycleCacheRunnable);
                }
                if (this.mAttachedToHierarchy) {
                    preference.onDetached();
                }
            }
            return bl;
        }
    }

    public void addItemFromInflater(Preference preference) {
        this.addPreference(preference);
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean addPreference(Preference preference) {
        long l;
        int n;
        int n2;
        String string2;
        Object object;
        if (this.mPreferences.contains(preference)) {
            return true;
        }
        if (preference.getKey() != null) {
            object = this;
            while (((Preference)object).getParent() != null) {
                object = ((Preference)object).getParent();
            }
            string2 = preference.getKey();
            if (((PreferenceGroup)object).findPreference(string2) != null) {
                object = new StringBuilder();
                ((StringBuilder)object).append("Found duplicated key: \"");
                ((StringBuilder)object).append(string2);
                ((StringBuilder)object).append("\". This can cause unintended behaviour, please use unique keys for every preference.");
                Log.e((String)TAG, (String)((StringBuilder)object).toString());
            }
        }
        if (preference.getOrder() == Integer.MAX_VALUE) {
            if (this.mOrderingAsAdded) {
                n2 = this.mCurrentPreferenceOrder;
                this.mCurrentPreferenceOrder = n2 + 1;
                preference.setOrder(n2);
            }
            if (preference instanceof PreferenceGroup) {
                ((PreferenceGroup)preference).setOrderingAsAdded(this.mOrderingAsAdded);
            }
        }
        n2 = n = Collections.binarySearch(this.mPreferences, preference);
        if (n < 0) {
            n2 = n * -1 - 1;
        }
        if (!this.onPrepareAddPreference(preference)) {
            return false;
        }
        synchronized (this) {
            this.mPreferences.add(n2, preference);
            {
                catch (Throwable throwable) {}
                {
                    throw throwable;
                }
            }
        }
        object = this.getPreferenceManager();
        string2 = preference.getKey();
        if (string2 != null && this.mIdRecycleCache.containsKey(string2)) {
            l = this.mIdRecycleCache.get(string2);
            this.mIdRecycleCache.remove(string2);
        } else {
            l = ((PreferenceManager)object).getNextId();
        }
        preference.onAttachedToHierarchy((PreferenceManager)object, l);
        preference.assignParent(this);
        if (this.mAttachedToHierarchy) {
            preference.onAttached();
        }
        this.notifyHierarchyChanged();
        return true;
    }

    @Override
    protected void dispatchRestoreInstanceState(Bundle bundle) {
        super.dispatchRestoreInstanceState(bundle);
        int n = this.getPreferenceCount();
        for (int i = 0; i < n; ++i) {
            this.getPreference(i).dispatchRestoreInstanceState(bundle);
        }
    }

    @Override
    protected void dispatchSaveInstanceState(Bundle bundle) {
        super.dispatchSaveInstanceState(bundle);
        int n = this.getPreferenceCount();
        for (int i = 0; i < n; ++i) {
            this.getPreference(i).dispatchSaveInstanceState(bundle);
        }
    }

    public <T extends Preference> T findPreference(CharSequence object) {
        if (object != null) {
            if (TextUtils.equals((CharSequence)this.getKey(), (CharSequence)object)) {
                return (T)this;
            }
            int n = this.getPreferenceCount();
            for (int i = 0; i < n; ++i) {
                Preference preference = this.getPreference(i);
                if (TextUtils.equals((CharSequence)preference.getKey(), (CharSequence)object)) {
                    return (T)preference;
                }
                if (!(preference instanceof PreferenceGroup) || (preference = ((PreferenceGroup)preference).findPreference((CharSequence)object)) == null) continue;
                return (T)preference;
            }
            return null;
        }
        object = new IllegalArgumentException("Key cannot be null");
        throw object;
    }

    public int getInitialExpandedChildrenCount() {
        return this.mInitialExpandedChildrenCount;
    }

    public OnExpandButtonClickListener getOnExpandButtonClickListener() {
        return this.mOnExpandButtonClickListener;
    }

    public Preference getPreference(int n) {
        return this.mPreferences.get(n);
    }

    public int getPreferenceCount() {
        return this.mPreferences.size();
    }

    public boolean isAttached() {
        return this.mAttachedToHierarchy;
    }

    protected boolean isOnSameScreenAsChildren() {
        return true;
    }

    public boolean isOrderingAsAdded() {
        return this.mOrderingAsAdded;
    }

    @Override
    public void notifyDependencyChange(boolean bl) {
        super.notifyDependencyChange(bl);
        int n = this.getPreferenceCount();
        for (int i = 0; i < n; ++i) {
            this.getPreference(i).onParentChanged(this, bl);
        }
    }

    @Override
    public void onAttached() {
        super.onAttached();
        this.mAttachedToHierarchy = true;
        int n = this.getPreferenceCount();
        for (int i = 0; i < n; ++i) {
            this.getPreference(i).onAttached();
        }
    }

    @Override
    public void onDetached() {
        super.onDetached();
        this.mAttachedToHierarchy = false;
        int n = this.getPreferenceCount();
        for (int i = 0; i < n; ++i) {
            this.getPreference(i).onDetached();
        }
    }

    protected boolean onPrepareAddPreference(Preference preference) {
        preference.onParentChanged(this, this.shouldDisableDependents());
        return true;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable object) {
        if (object != null && object.getClass().equals(SavedState.class)) {
            object = (SavedState)((Object)object);
            this.mInitialExpandedChildrenCount = object.mInitialExpandedChildrenCount;
            super.onRestoreInstanceState(object.getSuperState());
            return;
        }
        super.onRestoreInstanceState((Parcelable)object);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        return new SavedState(super.onSaveInstanceState(), this.mInitialExpandedChildrenCount);
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public void removeAll() {
        // MONITORENTER : this
        List<Preference> list = this.mPreferences;
        for (int i = list.size() - 1; i >= 0; --i) {
            this.removePreferenceInt(list.get(0));
        }
        {
            catch (Throwable throwable) {}
            {
                // MONITOREXIT : this
                throw throwable;
            }
        }
        this.notifyHierarchyChanged();
    }

    public boolean removePreference(Preference preference) {
        boolean bl = this.removePreferenceInt(preference);
        this.notifyHierarchyChanged();
        return bl;
    }

    public boolean removePreferenceRecursively(CharSequence charSequence) {
        if ((charSequence = this.findPreference(charSequence)) == null) {
            return false;
        }
        return ((Preference)((Object)charSequence)).getParent().removePreference((Preference)((Object)charSequence));
    }

    public void setInitialExpandedChildrenCount(int n) {
        if (n != Integer.MAX_VALUE && !this.hasKey()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.getClass().getSimpleName());
            stringBuilder.append(" should have a key defined if it contains an expandable preference");
            Log.e((String)TAG, (String)stringBuilder.toString());
        }
        this.mInitialExpandedChildrenCount = n;
    }

    public void setOnExpandButtonClickListener(OnExpandButtonClickListener onExpandButtonClickListener) {
        this.mOnExpandButtonClickListener = onExpandButtonClickListener;
    }

    public void setOrderingAsAdded(boolean bl) {
        this.mOrderingAsAdded = bl;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void sortPreferences() {
        synchronized (this) {
            Collections.sort(this.mPreferences);
            return;
        }
    }

    public static interface OnExpandButtonClickListener {
        public void onExpandButtonClick();
    }

    public static interface PreferencePositionCallback {
        public int getPreferenceAdapterPosition(Preference var1);

        public int getPreferenceAdapterPosition(String var1);
    }

    static class SavedState
    extends Preference.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>(){

            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            public SavedState[] newArray(int n) {
                return new SavedState[n];
            }
        };
        int mInitialExpandedChildrenCount;

        SavedState(Parcel parcel) {
            super(parcel);
            this.mInitialExpandedChildrenCount = parcel.readInt();
        }

        SavedState(Parcelable parcelable, int n) {
            super(parcelable);
            this.mInitialExpandedChildrenCount = n;
        }

        public void writeToParcel(Parcel parcel, int n) {
            super.writeToParcel(parcel, n);
            parcel.writeInt(this.mInitialExpandedChildrenCount);
        }
    }
}

