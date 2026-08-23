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
 *  android.widget.EditText
 */
package androidx.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.EditText;
import androidx.core.content.res.TypedArrayUtils;
import androidx.preference.DialogPreference;
import androidx.preference.Preference;
import androidx.preference.R;

public class EditTextPreference
extends DialogPreference {
    private OnBindEditTextListener mOnBindEditTextListener;
    private String mText;

    public EditTextPreference(Context context) {
        this(context, null);
    }

    public EditTextPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, TypedArrayUtils.getAttr(context, R.attr.editTextPreferenceStyle, 16842898));
    }

    public EditTextPreference(Context context, AttributeSet attributeSet, int n) {
        this(context, attributeSet, n, 0);
    }

    public EditTextPreference(Context context, AttributeSet attributeSet, int n, int n2) {
        super(context, attributeSet, n, n2);
        context = context.obtainStyledAttributes(attributeSet, R.styleable.EditTextPreference, n, n2);
        if (TypedArrayUtils.getBoolean((TypedArray)context, R.styleable.EditTextPreference_useSimpleSummaryProvider, R.styleable.EditTextPreference_useSimpleSummaryProvider, false)) {
            this.setSummaryProvider(SimpleSummaryProvider.getInstance());
        }
        context.recycle();
    }

    OnBindEditTextListener getOnBindEditTextListener() {
        return this.mOnBindEditTextListener;
    }

    public String getText() {
        return this.mText;
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
            this.setText(object.mText);
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
        object.mText = this.getText();
        return object;
    }

    @Override
    protected void onSetInitialValue(Object object) {
        this.setText(this.getPersistedString((String)object));
    }

    public void setOnBindEditTextListener(OnBindEditTextListener onBindEditTextListener) {
        this.mOnBindEditTextListener = onBindEditTextListener;
    }

    public void setText(String string2) {
        boolean bl = this.shouldDisableDependents();
        this.mText = string2;
        this.persistString(string2);
        boolean bl2 = this.shouldDisableDependents();
        if (bl2 != bl) {
            this.notifyDependencyChange(bl2);
        }
        this.notifyChanged();
    }

    @Override
    public boolean shouldDisableDependents() {
        boolean bl = TextUtils.isEmpty((CharSequence)this.mText) || super.shouldDisableDependents();
        return bl;
    }

    public static interface OnBindEditTextListener {
        public void onBindEditText(EditText var1);
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
        String mText;

        SavedState(Parcel parcel) {
            super(parcel);
            this.mText = parcel.readString();
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public void writeToParcel(Parcel parcel, int n) {
            super.writeToParcel(parcel, n);
            parcel.writeString(this.mText);
        }
    }

    public static final class SimpleSummaryProvider
    implements Preference.SummaryProvider<EditTextPreference> {
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
        public CharSequence provideSummary(EditTextPreference editTextPreference) {
            if (TextUtils.isEmpty((CharSequence)editTextPreference.getText())) {
                return editTextPreference.getContext().getString(R.string.not_set);
            }
            return editTextPreference.getText();
        }
    }
}

