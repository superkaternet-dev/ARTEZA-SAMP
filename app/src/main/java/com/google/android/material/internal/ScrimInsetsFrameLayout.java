/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Canvas
 *  android.graphics.Rect
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.Drawable$Callback
 *  android.util.AttributeSet
 *  android.view.View
 *  android.widget.FrameLayout
 */
package com.google.android.material.internal;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.R;
import com.google.android.material.internal.ThemeEnforcement;

public class ScrimInsetsFrameLayout
extends FrameLayout {
    Drawable insetForeground;
    Rect insets;
    private Rect tempRect = new Rect();

    public ScrimInsetsFrameLayout(Context context) {
        this(context, null);
    }

    public ScrimInsetsFrameLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ScrimInsetsFrameLayout(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        context = ThemeEnforcement.obtainStyledAttributes(context, attributeSet, R.styleable.ScrimInsetsFrameLayout, n, R.style.Widget_Design_ScrimInsetsFrameLayout, new int[0]);
        this.insetForeground = context.getDrawable(R.styleable.ScrimInsetsFrameLayout_insetForeground);
        context.recycle();
        this.setWillNotDraw(true);
        ViewCompat.setOnApplyWindowInsetsListener((View)this, new OnApplyWindowInsetsListener(this){
            final ScrimInsetsFrameLayout this$0;
            {
                this.this$0 = scrimInsetsFrameLayout;
            }

            @Override
            public WindowInsetsCompat onApplyWindowInsets(View object, WindowInsetsCompat windowInsetsCompat) {
                if (this.this$0.insets == null) {
                    this.this$0.insets = new Rect();
                }
                this.this$0.insets.set(windowInsetsCompat.getSystemWindowInsetLeft(), windowInsetsCompat.getSystemWindowInsetTop(), windowInsetsCompat.getSystemWindowInsetRight(), windowInsetsCompat.getSystemWindowInsetBottom());
                this.this$0.onInsetsChanged(windowInsetsCompat);
                object = this.this$0;
                boolean bl = !windowInsetsCompat.hasSystemWindowInsets() || this.this$0.insetForeground == null;
                object.setWillNotDraw(bl);
                ViewCompat.postInvalidateOnAnimation((View)this.this$0);
                return windowInsetsCompat.consumeSystemWindowInsets();
            }
        });
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        int n = this.getWidth();
        int n2 = this.getHeight();
        if (this.insets != null && this.insetForeground != null) {
            int n3 = canvas.save();
            canvas.translate((float)this.getScrollX(), (float)this.getScrollY());
            this.tempRect.set(0, 0, n, this.insets.top);
            this.insetForeground.setBounds(this.tempRect);
            this.insetForeground.draw(canvas);
            this.tempRect.set(0, n2 - this.insets.bottom, n, n2);
            this.insetForeground.setBounds(this.tempRect);
            this.insetForeground.draw(canvas);
            this.tempRect.set(0, this.insets.top, this.insets.left, n2 - this.insets.bottom);
            this.insetForeground.setBounds(this.tempRect);
            this.insetForeground.draw(canvas);
            this.tempRect.set(n - this.insets.right, this.insets.top, n, n2 - this.insets.bottom);
            this.insetForeground.setBounds(this.tempRect);
            this.insetForeground.draw(canvas);
            canvas.restoreToCount(n3);
        }
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Drawable drawable2 = this.insetForeground;
        if (drawable2 != null) {
            drawable2.setCallback((Drawable.Callback)this);
        }
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Drawable drawable2 = this.insetForeground;
        if (drawable2 != null) {
            drawable2.setCallback(null);
        }
    }

    protected void onInsetsChanged(WindowInsetsCompat windowInsetsCompat) {
    }
}

