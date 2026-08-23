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
import androidx.core.content.res.TypedArrayUtils;
import androidx.preference.PreferenceViewHolder;
import androidx.preference.R;
import androidx.preference.TwoStatePreference;

public class CheckBoxPreference
extends TwoStatePreference {
    private final Listener mListener = new Listener(this);

    public CheckBoxPreference(Context context) {
        this(context, null);
    }

    public CheckBoxPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, TypedArrayUtils.getAttr(context, R.attr.checkBoxPreferenceStyle, 16842895));
    }

    public CheckBoxPreference(Context context, AttributeSet attributeSet, int n) {
        this(context, attributeSet, n, 0);
    }

    public CheckBoxPreference(Context context, AttributeSet attributeSet, int n, int n2) {
        super(context, attributeSet, n, n2);
        context = context.obtainStyledAttributes(attributeSet, R.styleable.CheckBoxPreference, n, n2);
        this.setSummaryOn(TypedArrayUtils.getString((TypedArray)context, R.styleable.CheckBoxPreference_summaryOn, R.styleable.CheckBoxPreference_android_summaryOn));
        this.setSummaryOff(TypedArrayUtils.getString((TypedArray)context, R.styleable.CheckBoxPreference_summaryOff, R.styleable.CheckBoxPreference_android_summaryOff));
        this.setDisableDependentsState(TypedArrayUtils.getBoolean((TypedArray)context, R.styleable.CheckBoxPreference_disableDependentsState, R.styleable.CheckBoxPreference_android_disableDependentsState, false));
        context.recycle();
    }

    private void syncCheckboxView(View view) {
        if (view instanceof CompoundButton) {
            ((CompoundButton)view).setOnCheckedChangeListener(null);
        }
        if (view instanceof Checkable) {
            ((Checkable)view).setChecked(this.mChecked);
        }
        if (view instanceof CompoundButton) {
            ((CompoundButton)view).setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener)this.mListener);
        }
    }

    private void syncViewIfAccessibilityEnabled(View view) {
        if (!((AccessibilityManager)this.getContext().getSystemService("accessibility")).isEnabled()) {
            return;
        }
        this.syncCheckboxView(view.findViewById(0x1020001));
        this.syncSummaryView(view.findViewById(0x1020010));
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        this.syncCheckboxView(preferenceViewHolder.findViewById(0x1020001));
        this.syncSummaryView(preferenceViewHolder);
    }

    @Override
    protected void performClick(View view) {
        super.performClick(view);
        this.syncViewIfAccessibilityEnabled(view);
    }

    private class Listener
    implements CompoundButton.OnCheckedChangeListener {
        final CheckBoxPreference this$0;

        Listener(CheckBoxPreference checkBoxPreference) {
            this.this$0 = checkBoxPreference;
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

