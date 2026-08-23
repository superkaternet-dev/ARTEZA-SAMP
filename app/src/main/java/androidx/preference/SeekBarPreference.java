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
 *  android.util.Log
 *  android.view.KeyEvent
 *  android.view.View
 *  android.view.View$OnKeyListener
 *  android.widget.SeekBar
 *  android.widget.SeekBar$OnSeekBarChangeListener
 *  android.widget.TextView
 */
package androidx.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import androidx.preference.R;

public class SeekBarPreference
extends Preference {
    private static final String TAG = "SeekBarPreference";
    boolean mAdjustable;
    private int mMax;
    int mMin;
    SeekBar mSeekBar;
    private SeekBar.OnSeekBarChangeListener mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener(this){
        final SeekBarPreference this$0;
        {
            this.this$0 = seekBarPreference;
        }

        public void onProgressChanged(SeekBar object, int n, boolean bl) {
            if (bl && (this.this$0.mUpdatesContinuously || !this.this$0.mTrackingTouch)) {
                this.this$0.syncValueInternal((SeekBar)object);
            } else {
                object = this.this$0;
                ((SeekBarPreference)object).updateLabelValue(((SeekBarPreference)object).mMin + n);
            }
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
            this.this$0.mTrackingTouch = true;
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            this.this$0.mTrackingTouch = false;
            if (seekBar.getProgress() + this.this$0.mMin != this.this$0.mSeekBarValue) {
                this.this$0.syncValueInternal(seekBar);
            }
        }
    };
    private int mSeekBarIncrement;
    private View.OnKeyListener mSeekBarKeyListener = new View.OnKeyListener(this){
        final SeekBarPreference this$0;
        {
            this.this$0 = seekBarPreference;
        }

        public boolean onKey(View view, int n, KeyEvent keyEvent) {
            if (keyEvent.getAction() != 0) {
                return false;
            }
            if (!(this.this$0.mAdjustable || n != 21 && n != 22)) {
                return false;
            }
            if (n != 23 && n != 66) {
                if (this.this$0.mSeekBar == null) {
                    Log.e((String)SeekBarPreference.TAG, (String)"SeekBar view is null and hence cannot be adjusted.");
                    return false;
                }
                return this.this$0.mSeekBar.onKeyDown(n, keyEvent);
            }
            return false;
        }
    };
    int mSeekBarValue;
    private TextView mSeekBarValueTextView;
    private boolean mShowSeekBarValue;
    boolean mTrackingTouch;
    boolean mUpdatesContinuously;

    public SeekBarPreference(Context context) {
        this(context, null);
    }

    public SeekBarPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.seekBarPreferenceStyle);
    }

    public SeekBarPreference(Context context, AttributeSet attributeSet, int n) {
        this(context, attributeSet, n, 0);
    }

    public SeekBarPreference(Context context, AttributeSet attributeSet, int n, int n2) {
        super(context, attributeSet, n, n2);
        context = context.obtainStyledAttributes(attributeSet, R.styleable.SeekBarPreference, n, n2);
        this.mMin = context.getInt(R.styleable.SeekBarPreference_min, 0);
        this.setMax(context.getInt(R.styleable.SeekBarPreference_android_max, 100));
        this.setSeekBarIncrement(context.getInt(R.styleable.SeekBarPreference_seekBarIncrement, 0));
        this.mAdjustable = context.getBoolean(R.styleable.SeekBarPreference_adjustable, true);
        this.mShowSeekBarValue = context.getBoolean(R.styleable.SeekBarPreference_showSeekBarValue, false);
        this.mUpdatesContinuously = context.getBoolean(R.styleable.SeekBarPreference_updatesContinuously, false);
        context.recycle();
    }

    private void setValueInternal(int n, boolean bl) {
        int n2 = n;
        if (n < this.mMin) {
            n2 = this.mMin;
        }
        n = n2;
        if (n2 > this.mMax) {
            n = this.mMax;
        }
        if (n != this.mSeekBarValue) {
            this.mSeekBarValue = n;
            this.updateLabelValue(n);
            this.persistInt(n);
            if (bl) {
                this.notifyChanged();
            }
        }
    }

    public int getMax() {
        return this.mMax;
    }

    public int getMin() {
        return this.mMin;
    }

    public final int getSeekBarIncrement() {
        return this.mSeekBarIncrement;
    }

    public boolean getShowSeekBarValue() {
        return this.mShowSeekBarValue;
    }

    public boolean getUpdatesContinuously() {
        return this.mUpdatesContinuously;
    }

    public int getValue() {
        return this.mSeekBarValue;
    }

    public boolean isAdjustable() {
        return this.mAdjustable;
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        preferenceViewHolder.itemView.setOnKeyListener(this.mSeekBarKeyListener);
        this.mSeekBar = (SeekBar)preferenceViewHolder.findViewById(R.id.seekbar);
        preferenceViewHolder = (TextView)preferenceViewHolder.findViewById(R.id.seekbar_value);
        this.mSeekBarValueTextView = preferenceViewHolder;
        if (this.mShowSeekBarValue) {
            preferenceViewHolder.setVisibility(0);
        } else {
            preferenceViewHolder.setVisibility(8);
            this.mSeekBarValueTextView = null;
        }
        preferenceViewHolder = this.mSeekBar;
        if (preferenceViewHolder == null) {
            Log.e((String)TAG, (String)"SeekBar view is null in onBindViewHolder.");
            return;
        }
        preferenceViewHolder.setOnSeekBarChangeListener(this.mSeekBarChangeListener);
        this.mSeekBar.setMax(this.mMax - this.mMin);
        int n = this.mSeekBarIncrement;
        if (n != 0) {
            this.mSeekBar.setKeyProgressIncrement(n);
        } else {
            this.mSeekBarIncrement = this.mSeekBar.getKeyProgressIncrement();
        }
        this.mSeekBar.setProgress(this.mSeekBarValue - this.mMin);
        this.updateLabelValue(this.mSeekBarValue);
        this.mSeekBar.setEnabled(this.isEnabled());
    }

    @Override
    protected Object onGetDefaultValue(TypedArray typedArray, int n) {
        return typedArray.getInt(n, 0);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable object) {
        if (!object.getClass().equals(SavedState.class)) {
            super.onRestoreInstanceState((Parcelable)object);
            return;
        }
        object = (SavedState)((Object)object);
        super.onRestoreInstanceState(object.getSuperState());
        this.mSeekBarValue = object.mSeekBarValue;
        this.mMin = object.mMin;
        this.mMax = object.mMax;
        this.notifyChanged();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Object object = super.onSaveInstanceState();
        if (this.isPersistent()) {
            return object;
        }
        object = new SavedState((Parcelable)object);
        object.mSeekBarValue = this.mSeekBarValue;
        object.mMin = this.mMin;
        object.mMax = this.mMax;
        return object;
    }

    @Override
    protected void onSetInitialValue(Object object) {
        Object object2 = object;
        if (object == null) {
            object2 = 0;
        }
        this.setValue(this.getPersistedInt((Integer)object2));
    }

    public void setAdjustable(boolean bl) {
        this.mAdjustable = bl;
    }

    public final void setMax(int n) {
        int n2 = n;
        if (n < this.mMin) {
            n2 = this.mMin;
        }
        if (n2 != this.mMax) {
            this.mMax = n2;
            this.notifyChanged();
        }
    }

    public void setMin(int n) {
        int n2 = n;
        if (n > this.mMax) {
            n2 = this.mMax;
        }
        if (n2 != this.mMin) {
            this.mMin = n2;
            this.notifyChanged();
        }
    }

    public final void setSeekBarIncrement(int n) {
        if (n != this.mSeekBarIncrement) {
            this.mSeekBarIncrement = Math.min(this.mMax - this.mMin, Math.abs(n));
            this.notifyChanged();
        }
    }

    public void setShowSeekBarValue(boolean bl) {
        this.mShowSeekBarValue = bl;
        this.notifyChanged();
    }

    public void setUpdatesContinuously(boolean bl) {
        this.mUpdatesContinuously = bl;
    }

    public void setValue(int n) {
        this.setValueInternal(n, true);
    }

    void syncValueInternal(SeekBar seekBar) {
        int n = this.mMin + seekBar.getProgress();
        if (n != this.mSeekBarValue) {
            if (this.callChangeListener(n)) {
                this.setValueInternal(n, false);
            } else {
                seekBar.setProgress(this.mSeekBarValue - this.mMin);
                this.updateLabelValue(this.mSeekBarValue);
            }
        }
    }

    void updateLabelValue(int n) {
        TextView textView = this.mSeekBarValueTextView;
        if (textView != null) {
            textView.setText((CharSequence)String.valueOf(n));
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
        int mMax;
        int mMin;
        int mSeekBarValue;

        SavedState(Parcel parcel) {
            super(parcel);
            this.mSeekBarValue = parcel.readInt();
            this.mMin = parcel.readInt();
            this.mMax = parcel.readInt();
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public void writeToParcel(Parcel parcel, int n) {
            super.writeToParcel(parcel, n);
            parcel.writeInt(this.mSeekBarValue);
            parcel.writeInt(this.mMin);
            parcel.writeInt(this.mMax);
        }
    }
}

