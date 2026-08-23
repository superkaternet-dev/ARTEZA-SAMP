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
 *  android.util.Log
 */
package androidx.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import androidx.core.content.res.TypedArrayUtils;
import androidx.preference.DialogPreference;
import androidx.preference.Preference;
import androidx.preference.R;

public class ListPreference
extends DialogPreference {
    private static final String TAG = "ListPreference";
    private CharSequence[] mEntries;
    private CharSequence[] mEntryValues;
    private String mSummary;
    private String mValue;
    private boolean mValueSet;

    public ListPreference(Context context) {
        this(context, null);
    }

    public ListPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, TypedArrayUtils.getAttr(context, R.attr.dialogPreferenceStyle, 0x1010091));
    }

    public ListPreference(Context context, AttributeSet attributeSet, int n) {
        this(context, attributeSet, n, 0);
    }

    public ListPreference(Context context, AttributeSet attributeSet, int n, int n2) {
        super(context, attributeSet, n, n2);
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.ListPreference, n, n2);
        this.mEntries = TypedArrayUtils.getTextArray(typedArray, R.styleable.ListPreference_entries, R.styleable.ListPreference_android_entries);
        this.mEntryValues = TypedArrayUtils.getTextArray(typedArray, R.styleable.ListPreference_entryValues, R.styleable.ListPreference_android_entryValues);
        if (TypedArrayUtils.getBoolean(typedArray, R.styleable.ListPreference_useSimpleSummaryProvider, R.styleable.ListPreference_useSimpleSummaryProvider, false)) {
            this.setSummaryProvider(SimpleSummaryProvider.getInstance());
        }
        typedArray.recycle();
        context = context.obtainStyledAttributes(attributeSet, R.styleable.Preference, n, n2);
        this.mSummary = TypedArrayUtils.getString((TypedArray)context, R.styleable.Preference_summary, R.styleable.Preference_android_summary);
        context.recycle();
    }

    private int getValueIndex() {
        return this.findIndexOfValue(this.mValue);
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

    public CharSequence getEntry() {
        Object object;
        int n = this.getValueIndex();
        object = n >= 0 && (object = this.mEntries) != null ? object[n] : null;
        return object;
    }

    public CharSequence[] getEntryValues() {
        return this.mEntryValues;
    }

    @Override
    public CharSequence getSummary() {
        if (this.getSummaryProvider() != null) {
            return this.getSummaryProvider().provideSummary(this);
        }
        CharSequence charSequence = this.getEntry();
        CharSequence charSequence2 = super.getSummary();
        String string2 = this.mSummary;
        if (string2 == null) {
            return charSequence2;
        }
        if (charSequence == null) {
            charSequence = "";
        }
        if (TextUtils.equals((CharSequence)(charSequence = String.format(string2, charSequence)), (CharSequence)charSequence2)) {
            return charSequence2;
        }
        Log.w((String)TAG, (String)"Setting a summary with a String formatting marker is no longer supported. You should use a SummaryProvider instead.");
        return charSequence;
    }

    public String getValue() {
        return this.mValue;
    }

    @Override
    protected Object onGetDefaultValue(TypedArray typedArray, int n) {
        return typedArray.getString(n);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable object) {
        if (object != null && object.getClass().equals(SavedState.class)) {
            object = (SavedState)((Object)object);
            super.onRestoreInstanceState(object.getSuperState());
            this.setValue(object.mValue);
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
        object.mValue = this.getValue();
        return object;
    }

    @Override
    protected void onSetInitialValue(Object object) {
        this.setValue(this.getPersistedString((String)object));
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

    @Override
    public void setSummary(CharSequence charSequence) {
        super.setSummary(charSequence);
        if (charSequence == null && this.mSummary != null) {
            this.mSummary = null;
        } else if (charSequence != null && !charSequence.equals(this.mSummary)) {
            this.mSummary = charSequence.toString();
        }
    }

    public void setValue(String string2) {
        boolean bl = TextUtils.equals((CharSequence)this.mValue, (CharSequence)string2) ^ true;
        if (bl || !this.mValueSet) {
            this.mValue = string2;
            this.mValueSet = true;
            this.persistString(string2);
            if (bl) {
                this.notifyChanged();
            }
        }
    }

    public void setValueIndex(int n) {
        CharSequence[] charSequenceArray = this.mEntryValues;
        if (charSequenceArray != null) {
            this.setValue(charSequenceArray[n].toString());
        }
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
        String mValue;

        SavedState(Parcel parcel) {
            super(parcel);
            this.mValue = parcel.readString();
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public void writeToParcel(Parcel parcel, int n) {
            super.writeToParcel(parcel, n);
            parcel.writeString(this.mValue);
        }
    }

    public static final class SimpleSummaryProvider
    implements Preference.SummaryProvider<ListPreference> {
        private static SimpleSummaryProvider sSimpleSummaryProvider;

        private SimpleSummaryProvider() {
        }

        public static SimpleSummaryProvider getInstance() {
            if (sSimpleSummaryProvider == null) {
                sSimpleSummaryProvider = new SimpleSummaryProvider();
            }
            return sSimpleSummaryProvider;
        }

        @Override
        public CharSequence provideSummary(ListPreference listPreference) {
            if (TextUtils.isEmpty((CharSequence)listPreference.getEntry())) {
                return listPreference.getContext().getString(R.string.not_set);
            }
            return listPreference.getEntry();
        }
    }
}

