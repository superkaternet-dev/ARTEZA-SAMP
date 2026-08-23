/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.TypedArray
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.util.AttributeSet
 */
package androidx.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import androidx.core.content.res.TypedArrayUtils;
import androidx.preference.DialogPreference;
import androidx.preference.Preference;
import androidx.preference.R;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MultiSelectListPreference
extends DialogPreference {
    private CharSequence[] mEntries;
    private CharSequence[] mEntryValues;
    private Set<String> mValues = new HashSet<String>();

    public MultiSelectListPreference(Context context) {
        this(context, null);
    }

    public MultiSelectListPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, TypedArrayUtils.getAttr(context, R.attr.dialogPreferenceStyle, 0x1010091));
    }

    public MultiSelectListPreference(Context context, AttributeSet attributeSet, int n) {
        this(context, attributeSet, n, 0);
    }

    public MultiSelectListPreference(Context context, AttributeSet attributeSet, int n, int n2) {
        super(context, attributeSet, n, n2);
        context = context.obtainStyledAttributes(attributeSet, R.styleable.MultiSelectListPreference, n, n2);
        this.mEntries = TypedArrayUtils.getTextArray((TypedArray)context, R.styleable.MultiSelectListPreference_entries, R.styleable.MultiSelectListPreference_android_entries);
        this.mEntryValues = TypedArrayUtils.getTextArray((TypedArray)context, R.styleable.MultiSelectListPreference_entryValues, R.styleable.MultiSelectListPreference_android_entryValues);
        context.recycle();
    }

    public int findIndexOfValue(String string2) {
        CharSequence[] charSequenceArray;
        if (string2 != null && (charSequenceArray = this.mEntryValues) != null) {
            for (int i = charSequenceArray.length - 1; i >= 0; --i) {
                if (!this.mEntryValues[i].equals(string2)) continue;
                return i;
            }
        }
        return -1;
    }

    public CharSequence[] getEntries() {
        return this.mEntries;
    }

    public CharSequence[] getEntryValues() {
        return this.mEntryValues;
    }

    protected boolean[] getSelectedItems() {
        CharSequence[] charSequenceArray = this.mEntryValues;
        int n = charSequenceArray.length;
        Set<String> set = this.mValues;
        boolean[] blArray = new boolean[n];
        for (int i = 0; i < n; ++i) {
            blArray[i] = set.contains(charSequenceArray[i].toString());
        }
        return blArray;
    }

    public Set<String> getValues() {
        return this.mValues;
    }

    @Override
    protected Object onGetDefaultValue(TypedArray charSequenceArray, int n) {
        charSequenceArray = charSequenceArray.getTextArray(n);
        HashSet<String> hashSet = new HashSet<String>();
        int n2 = charSequenceArray.length;
        for (n = 0; n < n2; ++n) {
            hashSet.add(charSequenceArray[n].toString());
        }
        return hashSet;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable object) {
        if (object != null && object.getClass().equals(SavedState.class)) {
            object = (SavedState)((Object)object);
            super.onRestoreInstanceState(object.getSuperState());
            this.setValues(object.mValues);
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
        object.mValues = this.getValues();
        return object;
    }

    @Override
    protected void onSetInitialValue(Object object) {
        this.setValues(this.getPersistedStringSet((Set)object));
    }

    public void setEntries(int n) {
        this.setEntries(this.getContext().getResources().getTextArray(n));
    }

    public void setEntries(CharSequence[] charSequenceArray) {
        this.mEntries = charSequenceArray;
    }

    public void setEntryValues(int n) {
        this.setEntryValues(this.getContext().getResources().getTextArray(n));
    }

    public void setEntryValues(CharSequence[] charSequenceArray) {
        this.mEntryValues = charSequenceArray;
    }

    public void setValues(Set<String> set) {
        this.mValues.clear();
        this.mValues.addAll(set);
        this.persistStringSet(set);
        this.notifyChanged();
    }

    private static class SavedState
    extends Preference.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>(){

            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            public SavedState[] newArray(int n) {
                return new SavedState[n];
            }
        };
        Set<String> mValues;

        SavedState(Parcel parcel) {
            super(parcel);
            int n = parcel.readInt();
            this.mValues = new HashSet<String>();
            String[] stringArray = new String[n];
            parcel.readStringArray(stringArray);
            Collections.addAll(this.mValues, stringArray);
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public void writeToParcel(Parcel parcel, int n) {
            super.writeToParcel(parcel, n);
            parcel.writeInt(this.mValues.size());
            Set<String> set = this.mValues;
            parcel.writeStringArray(set.toArray(new String[set.size()]));
        }
    }
}

