/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.TypedArray
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.accessibility.AccessibilityManager
 *  android.widget.Checkable
 *  android.widget.CompoundButton
 *  android.widget.CompoundButton$OnCheckedChangeListener
 */
package androidx.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Checkable;
import android.widget.CompoundButton;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.res.TypedArrayUtils;
import androidx.preference.PreferenceViewHolder;
import androidx.preference.R;
import androidx.preference.TwoStatePreference;

public class SwitchPreferenceCompat
extends TwoStatePreference {
    private final Listener mListener = new Listener(this);
    private CharSequence mSwitchOff;
    private CharSequence mSwitchOn;

    public SwitchPreferenceCompat(Context context) {
        this(context, null);
    }

    public SwitchPreferenceCompat(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.switchPreferenceCompatStyle);
    }

    public SwitchPreferenceCompat(Context context, AttributeSet attributeSet, int n) {
        this(context, attributeSet, n, 0);
    }

    public SwitchPreferenceCompat(Context context, AttributeSet attributeSet, int n, int n2) {
        super(context, attributeSet, n, n2);
        context = context.obtainStyledAttributes(attributeSet, R.styleable.SwitchPreferenceCompat, n, n2);
        this.setSummaryOn(TypedArrayUtils.getString((TypedArray)context, R.styleable.SwitchPreferenceCompat_summaryOn, R.styleable.SwitchPreferenceCompat_android_summaryOn));
        this.setSummaryOff(TypedArrayUtils.getString((TypedArray)context, R.styleable.SwitchPreferenceCompat_summaryOff, R.styleable.SwitchPreferenceCompat_android_summaryOff));
        this.setSwitchTextOn(TypedArrayUtils.getString((TypedArray)context, R.styleable.SwitchPreferenceCompat_switchTextOn, R.styleable.SwitchPreferenceCompat_android_switchTextOn));
        this.setSwitchTextOff(TypedArrayUtils.getString((TypedArray)context, R.styleable.SwitchPreferenceCompat_switchTextOff, R.styleable.SwitchPreferenceCompat_android_switchTextOff));
        this.setDisableDependentsState(TypedArrayUtils.getBoolean((TypedArray)context, R.styleable.SwitchPreferenceCompat_disableDependentsState, R.styleable.SwitchPreferenceCompat_android_disableDependentsState, false));
        context.recycle();
    }

    private void syncSwitchView(View object) {
        if (object instanceof SwitchCompat) {
            ((SwitchCompat)((Object)object)).setOnCheckedChangeListener(null);
        }
        if (object instanceof Checkable) {
            ((Checkable)object).setChecked(this.mChecked);
        }
        if (object instanceof SwitchCompat) {
            object = (SwitchCompat)((Object)object);
            ((SwitchCompat)((Object)object)).setTextOn(this.mSwitchOn);
            ((SwitchCompat)((Object)object)).setTextOff(this.mSwitchOff);
            object.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener)this.mListener);
        }
    }

    private void syncViewIfAccessibilityEnabled(View view) {
        if (!((AccessibilityManager)this.getContext().getSystemService("accessibility")).isEnabled()) {
            return;
        }
        this.syncSwitchView(view.findViewById(R.id.switchWidget));
        this.syncSummaryView(view.findViewById(0x1020010));
    }

    public CharSequence getSwitchTextOff() {
        return this.mSwitchOff;
    }

    public CharSequence getSwitchTextOn() {
        return this.mSwitchOn;
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        this.syncSwitchView(preferenceViewHolder.findViewById(R.id.switchWidget));
        this.syncSummaryView(preferenceViewHolder);
    }

    @Override
    protected void performClick(View view) {
        super.performClick(view);
        this.syncViewIfAccessibilityEnabled(view);
    }

    public void setSwitchTextOff(int n) {
        this.setSwitchTextOff(this.getContext().getString(n));
    }

    public void setSwitchTextOff(CharSequence charSequence) {
        this.mSwitchOff = charSequence;
        this.notifyChanged();
    }

    public void setSwitchTextOn(int n) {
        this.setSwitchTextOn(this.getContext().getString(n));
    }

    public void setSwitchTextOn(CharSequence charSequence) {
        this.mSwitchOn = charSequence;
        this.notifyChanged();
    }

    private class Listener
    implements CompoundButton.OnCheckedChangeListener {
        final SwitchPreferenceCompat this$0;

        Listener(SwitchPreferenceCompat switchPreferenceCompat) {
            this.this$0 = switchPreferenceCompat;
        }

        public void onCheckedChanged(CompoundButton compoundButton, boolean bl) {
            if (!this.this$0.callChangeListener(bl)) {
                compoundButton.setChecked(bl ^ true);
                return;
            }
            this.this$0.setChecked(bl);
        }
    }
}

