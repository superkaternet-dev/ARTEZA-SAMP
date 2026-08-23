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
 *  android.widget.Switch
 */
package androidx.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.Switch;
import androidx.core.content.res.TypedArrayUtils;
import androidx.preference.PreferenceViewHolder;
import androidx.preference.R;
import androidx.preference.TwoStatePreference;

public class SwitchPreference
extends TwoStatePreference {
    private final Listener mListener = new Listener(this);
    private CharSequence mSwitchOff;
    private CharSequence mSwitchOn;

    public SwitchPreference(Context context) {
        this(context, null);
    }

    public SwitchPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, TypedArrayUtils.getAttr(context, R.attr.switchPreferenceStyle, 16843629));
    }

    public SwitchPreference(Context context, AttributeSet attributeSet, int n) {
        this(context, attributeSet, n, 0);
    }

    public SwitchPreference(Context context, AttributeSet attributeSet, int n, int n2) {
        super(context, attributeSet, n, n2);
        context = context.obtainStyledAttributes(attributeSet, R.styleable.SwitchPreference, n, n2);
        this.setSummaryOn(TypedArrayUtils.getString((TypedArray)context, R.styleable.SwitchPreference_summaryOn, R.styleable.SwitchPreference_android_summaryOn));
        this.setSummaryOff(TypedArrayUtils.getString((TypedArray)context, R.styleable.SwitchPreference_summaryOff, R.styleable.SwitchPreference_android_summaryOff));
        this.setSwitchTextOn(TypedArrayUtils.getString((TypedArray)context, R.styleable.SwitchPreference_switchTextOn, R.styleable.SwitchPreference_android_switchTextOn));
        this.setSwitchTextOff(TypedArrayUtils.getString((TypedArray)context, R.styleable.SwitchPreference_switchTextOff, R.styleable.SwitchPreference_android_switchTextOff));
        this.setDisableDependentsState(TypedArrayUtils.getBoolean((TypedArray)context, R.styleable.SwitchPreference_disableDependentsState, R.styleable.SwitchPreference_android_disableDependentsState, false));
        context.recycle();
    }

    private void syncSwitchView(View view) {
        if (view instanceof Switch) {
            ((Switch)view).setOnCheckedChangeListener(null);
        }
        if (view instanceof Checkable) {
            ((Checkable)view).setChecked(this.mChecked);
        }
        if (view instanceof Switch) {
            view = (Switch)view;
            view.setTextOn(this.mSwitchOn);
            view.setTextOff(this.mSwitchOff);
            view.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener)this.mListener);
        }
    }

    private void syncViewIfAccessibilityEnabled(View view) {
        if (!((AccessibilityManager)this.getContext().getSystemService("accessibility")).isEnabled()) {
            return;
        }
        this.syncSwitchView(view.findViewById(16908352));
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
        this.syncSwitchView(preferenceViewHolder.findViewById(16908352));
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
        final SwitchPreference this$0;

        Listener(SwitchPreference switchPreference) {
            this.this$0 = switchPreference;
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

