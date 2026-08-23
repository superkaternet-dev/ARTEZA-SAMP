/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.accessibility.AccessibilityEvent
 *  android.widget.Checkable
 */
package com.google.android.material.internal;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Checkable;
import androidx.appcompat.R;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

public class CheckableImageButton
extends AppCompatImageButton
implements Checkable {
    private static final int[] DRAWABLE_STATE_CHECKED = new int[]{0x10100A0};
    private boolean checked;

    public CheckableImageButton(Context context) {
        this(context, null);
    }

    public CheckableImageButton(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.imageButtonStyle);
    }

    public CheckableImageButton(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        ViewCompat.setAccessibilityDelegate((View)this, new AccessibilityDelegateCompat(this){
            final CheckableImageButton this$0;
            {
                this.this$0 = checkableImageButton;
            }

            @Override
            public void onInitializeAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
                super.onInitializeAccessibilityEvent(view, accessibilityEvent);
                accessibilityEvent.setChecked(this.this$0.isChecked());
            }

            @Override
            public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
                accessibilityNodeInfoCompat.setCheckable(true);
                accessibilityNodeInfoCompat.setChecked(this.this$0.isChecked());
            }
        });
    }

    public boolean isChecked() {
        return this.checked;
    }

    public int[] onCreateDrawableState(int n) {
        if (this.checked) {
            int[] nArray = DRAWABLE_STATE_CHECKED;
            return CheckableImageButton.mergeDrawableStates((int[])super.onCreateDrawableState(nArray.length + n), (int[])nArray);
        }
        return super.onCreateDrawableState(n);
    }

    public void setChecked(boolean bl) {
        if (this.checked != bl) {
            this.checked = bl;
            this.refreshDrawableState();
            this.sendAccessibilityEvent(2048);
        }
    }

    public void toggle() {
        this.setChecked(this.checked ^ true);
    }
}

