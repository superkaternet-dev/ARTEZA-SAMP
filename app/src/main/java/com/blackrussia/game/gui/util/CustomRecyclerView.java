/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.AttributeSet
 *  android.view.MotionEvent
 */
package com.blackrussia.game.gui.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.recyclerview.widget.RecyclerView;

public class CustomRecyclerView
extends RecyclerView {
    private boolean mEnableScrolling = true;

    public CustomRecyclerView(Context context) {
        super(context);
    }

    public CustomRecyclerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public CustomRecyclerView(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
    }

    public int getScrollForRecycler() {
        return this.computeVerticalScrollOffset();
    }

    public boolean isEnableScrolling() {
        return this.mEnableScrolling;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (this.isEnableScrolling()) {
            return CustomRecyclerView.super.onInterceptTouchEvent(motionEvent);
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.isEnableScrolling()) {
            return CustomRecyclerView.super.onTouchEvent(motionEvent);
        }
        return false;
    }

    public void setEnableScrolling(boolean bl) {
        this.mEnableScrolling = bl;
    }
}

