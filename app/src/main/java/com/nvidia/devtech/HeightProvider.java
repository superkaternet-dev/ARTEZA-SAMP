/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.graphics.Rect
 *  android.graphics.drawable.ColorDrawable
 *  android.graphics.drawable.Drawable
 *  android.view.View
 *  android.view.ViewTreeObserver$OnGlobalLayoutListener
 *  android.widget.PopupWindow
 */
package com.nvidia.devtech;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.PopupWindow;

public class HeightProvider
extends PopupWindow
implements ViewTreeObserver.OnGlobalLayoutListener {
    private int heightMaxHorizontal;
    private int heightMaxVertical;
    private HeightListener listener;
    private Activity mActivity;
    private View rootView;

    public HeightProvider(Activity activity) {
        super((Context)activity);
        this.mActivity = activity;
        activity = new View((Context)activity);
        this.rootView = activity;
        this.setContentView((View)activity);
        this.rootView.getViewTreeObserver().addOnGlobalLayoutListener((ViewTreeObserver.OnGlobalLayoutListener)this);
        this.setBackgroundDrawable((Drawable)new ColorDrawable(0));
        this.setWidth(0);
        this.setHeight(-1);
        this.setSoftInputMode(16);
        this.setInputMethodMode(1);
    }

    public HeightProvider init(View view) {
        if (!this.isShowing()) {
            view.post(new Runnable(this, view){
                final HeightProvider this$0;
                final View val$finalView;
                {
                    this.this$0 = heightProvider;
                    this.val$finalView = view;
                }

                @Override
                public void run() {
                    this.this$0.showAtLocation(this.val$finalView, 0, 0, 0);
                }
            });
        }
        return this;
    }

    public void onGlobalLayout() {
        int n;
        Object object = new Rect();
        this.rootView.getWindowVisibleDisplayFrame((Rect)object);
        if (((Rect)object).bottom > ((Rect)object).right) {
            if (((Rect)object).bottom > this.heightMaxVertical) {
                this.heightMaxVertical = ((Rect)object).bottom;
            }
            n = this.heightMaxVertical - ((Rect)object).bottom;
        } else {
            if (((Rect)object).bottom > this.heightMaxHorizontal) {
                this.heightMaxHorizontal = ((Rect)object).bottom;
            }
            n = this.heightMaxHorizontal - ((Rect)object).bottom;
        }
        object = this.listener;
        if (object != null) {
            object.onHeightChanged(this.mActivity.getResources().getConfiguration().orientation, n);
        }
    }

    public HeightProvider setHeightListener(HeightListener heightListener) {
        this.listener = heightListener;
        return this;
    }

    public static interface HeightListener {
        public void onHeightChanged(int var1, int var2);
    }
}

