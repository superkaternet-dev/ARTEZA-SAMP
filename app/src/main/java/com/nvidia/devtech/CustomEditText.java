/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.AttributeSet
 *  android.view.KeyEvent
 *  android.widget.EditText
 */
package com.nvidia.devtech;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;
import com.nvidia.devtech.NvEventQueueActivity;

public class CustomEditText
extends EditText {
    private Context mContext = null;

    public CustomEditText(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mContext = context;
    }

    public void SetBackListener(Context context) {
        this.mContext = context;
    }

    public boolean onKeyPreIme(int n, KeyEvent keyEvent) {
        if (n == 4) {
            ((NvEventQueueActivity)this.mContext).onEventBackPressed();
            return true;
        }
        return false;
    }
}

