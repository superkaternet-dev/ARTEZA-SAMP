/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.TypedArray
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.text.TextUtils
 *  android.util.AttributeSet
 *  android.view.View
 *  android.widget.TextView
 */
package androidx.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

public abstract class TwoStatePreference
extends Preference {
    protected boolean mChecked;
    private boolean mCheckedSet;
    private boolean mDisableDependentsState;
    private CharSequence mSummaryOff;
    private CharSequence mSummaryOn;

    public TwoStatePreference(Context context) {
        this(context, null);
    }

    public TwoStatePreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public TwoStatePreference(Context context, AttributeSet attributeSet, int n) {
        this(context, attributeSet, n, 0);
    }

    public TwoStatePreference(Context context, AttributeSet attributeSet, int n, int n2) {
        super(context, attributeSet, n, n2);
    }

    public boolean getDisableDependentsState() {
        return this.mDisableDependentsState;
    }

    public CharSequence getSummaryOff() {
        return this.mSummaryOff;
    }

    public CharSequence getSummaryOn() {
        return this.mSummaryOn;
    }

    public boolean isChecked() {
        return this.mChecked;
    }

    @Override
    protected void onClick() {
        super.onClick();
        boolean bl = this.isChecked() ^ true;
        if (this.callChangeListener(bl)) {
            this.setChecked(bl);
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray typedArray, int n) {
        return typedArray.getBoolean(n, false);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable object) {
        if (object != null && object.getClass().equals(SavedState.class)) {
            object = (SavedState)((Object)object);
            super.onRestoreInstanceState(object.getSuperState());
            this.setChecked(object.mChecked);
            return;
        }
        super.onRestoreInstanceState((Parcelable)object);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Object object = super.onSaveInstanceState();
        if (this.isPersistent()) {
            return object;
        }
        object = new SavedState((Parcelable)object);
        object.mChecked = this.isChecked();
        return object;
    }

    @Override
    protected void onSetInitialValue(Object object) {
        Object object2 = object;
        if (object == null) {
            object2 = false;
        }
        this.setChecked(this.getPersistedBoolean((Boolean)object2));
    }

    public void setChecked(boolean bl) {
        boolean bl2 = this.mChecked != bl;
        if (bl2 || !this.mCheckedSet) {
            this.mChecked = bl;
            this.mCheckedSet = true;
            this.persistBoolean(bl);
            if (bl2) {
                this.notifyDependencyChange(this.shouldDisableDependents());
                this.notifyChanged();
            }
        }
    }

    public void setDisableDependentsState(boolean bl) {
        this.mDisableDependentsState = bl;
    }

    public void setSummaryOff(int n) {
        this.setSummaryOff(this.getContext().getString(n));
    }

    public void setSummaryOff(CharSequence charSequence) {
        this.mSummaryOff = charSequence;
        if (!this.isChecked()) {
            this.notifyChanged();
        }
    }

    public void setSummaryOn(int n) {
        this.setSummaryOn(this.getContext().getString(n));
    }

    public void setSummaryOn(CharSequence charSequence) {
        this.mSummaryOn = charSequence;
        if (this.isChecked()) {
            this.notifyChanged();
        }
    }

    @Override
    public boolean shouldDisableDependents() {
        boolean bl = this.mDisableDependentsState;
        boolean bl2 = true;
        bl = bl ? this.mChecked : !this.mChecked;
        boolean bl3 = bl2;
        if (!bl) {
            bl3 = super.shouldDisableDependents() ? bl2 : false;
        }
        return bl3;
    }

    protected void syncSummaryView(View view) {
        int n;
        if (!(view instanceof TextView)) {
            return;
        }
        view = (TextView)view;
        int n2 = 1;
        if (this.mChecked && !TextUtils.isEmpty((CharSequence)this.mSummaryOn)) {
            view.setText(this.mSummaryOn);
            n = 0;
        } else {
            n = n2;
            if (!this.mChecked) {
                n = n2;
                if (!TextUtils.isEmpty((CharSequence)this.mSummaryOff)) {
                    view.setText(this.mSummaryOff);
                    n = 0;
                }
            }
        }
        n2 = n;
        if (n != 0) {
            CharSequence charSequence = this.getSummary();
            n2 = n;
            if (!TextUtils.isEmpty((CharSequence)charSequence)) {
                view.setText(charSequence);
                n2 = 0;
            }
        }
        n = 8;
        if (n2 == 0) {
            n = 0;
        }
        if (n != view.getVisibility()) {
            view.setVisibility(n);
        }
    }

    protected void syncSummaryView(PreferenceViewHolder preferenceViewHolder) {
        this.syncSummaryView(preferenceViewHolder.findViewById(0x1020010));
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
        boolean mChecked;

        SavedState(Parcel parcel) {
            super(parcel);
            int n = parcel.readInt();
            boolean bl = true;
            if (n != 1) {
                bl = false;
            }
            this.mChecked = bl;
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public void writeToParcel(Parcel parcel, int n) {
            super.writeToParcel(parcel, n);
            parcel.writeInt(this.mChecked ? 1 : 0);
        }
    }
}

