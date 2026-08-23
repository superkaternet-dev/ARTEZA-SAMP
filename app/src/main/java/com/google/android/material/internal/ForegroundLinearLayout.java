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
 *  android.view.Gravity
 */
package com.google.android.material.internal;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import androidx.appcompat.widget.LinearLayoutCompat;
import com.google.android.material.R;
import com.google.android.material.internal.ThemeEnforcement;

public class ForegroundLinearLayout
extends LinearLayoutCompat {
    private Drawable foreground;
    boolean foregroundBoundsChanged = false;
    private int foregroundGravity = 119;
    protected boolean mForegroundInPadding = true;
    private final Rect overlayBounds;
    private final Rect selfBounds = new Rect();

    public ForegroundLinearLayout(Context context) {
        this(context, null);
    }

    public ForegroundLinearLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ForegroundLinearLayout(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        this.overlayBounds = new Rect();
        context = ThemeEnforcement.obtainStyledAttributes(context, attributeSet, R.styleable.ForegroundLinearLayout, n, 0, new int[0]);
        this.foregroundGravity = context.getInt(R.styleable.ForegroundLinearLayout_android_foregroundGravity, this.foregroundGravity);
        attributeSet = context.getDrawable(R.styleable.ForegroundLinearLayout_android_foreground);
        if (attributeSet != null) {
            this.setForeground((Drawable)attributeSet);
        }
        this.mForegroundInPadding = context.getBoolean(R.styleable.ForegroundLinearLayout_foregroundInsidePadding, true);
        context.recycle();
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (this.foreground != null) {
            Drawable drawable2 = this.foreground;
            if (this.foregroundBoundsChanged) {
                this.foregroundBoundsChanged = false;
                Rect rect = this.selfBounds;
                Rect rect2 = this.overlayBounds;
                int n = this.getRight() - this.getLeft();
                int n2 = this.getBottom() - this.getTop();
                if (this.mForegroundInPadding) {
                    rect.set(0, 0, n, n2);
                } else {
                    rect.set(this.getPaddingLeft(), this.getPaddingTop(), n - this.getPaddingRight(), n2 - this.getPaddingBottom());
                }
                Gravity.apply((int)this.foregroundGravity, (int)drawable2.getIntrinsicWidth(), (int)drawable2.getIntrinsicHeight(), (Rect)rect, (Rect)rect2);
                drawable2.setBounds(rect2);
            }
            drawable2.draw(canvas);
        }
    }

    public void drawableHotspotChanged(float f, float f2) {
        super.drawableHotspotChanged(f, f2);
        Drawable drawable2 = this.foreground;
        if (drawable2 != null) {
            drawable2.setHotspot(f, f2);
        }
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable drawable2 = this.foreground;
        if (drawable2 != null && drawable2.isStateful()) {
            this.foreground.setState(this.getDrawableState());
        }
    }

    public Drawable getForeground() {
        return this.foreground;
    }

    public int getForegroundGravity() {
        return this.foregroundGravity;
    }

    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        Drawable drawable2 = this.foreground;
        if (drawable2 != null) {
            drawable2.jumpToCurrentState();
        }
    }

    @Override
    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        super.onLayout(bl, n, n2, n3, n4);
        this.foregroundBoundsChanged |= bl;
    }

    protected void onSizeChanged(int n, int n2, int n3, int n4) {
        super.onSizeChanged(n, n2, n3, n4);
        this.foregroundBoundsChanged = true;
    }

    public void setForeground(Drawable drawable2) {
        Drawable drawable3 = this.foreground;
        if (drawable3 != drawable2) {
            if (drawable3 != null) {
                drawable3.setCallback(null);
                this.unscheduleDrawable(this.foreground);
            }
            this.foreground = drawable2;
            if (drawable2 != null) {
                this.setWillNotDraw(false);
                drawable2.setCallback((Drawable.Callback)this);
                if (drawable2.isStateful()) {
                    drawable2.setState(this.getDrawableState());
                }
                if (this.foregroundGravity == 119) {
                    drawable2.getPadding(new Rect());
                }
            } else {
                this.setWillNotDraw(true);
            }
            this.requestLayout();
            this.invalidate();
        }
    }

    public void setForegroundGravity(int n) {
        if (this.foregroundGravity != n) {
            int n2 = n;
            if ((0x800007 & n) == 0) {
                n2 = n | 0x800003;
            }
            n = n2;
            if ((n2 & 0x70) == 0) {
                n = n2 | 0x30;
            }
            this.foregroundGravity = n;
            if (n == 119 && this.foreground != null) {
                Rect rect = new Rect();
                this.foreground.getPadding(rect);
            }
            this.requestLayout();
        }
    }

    protected boolean verifyDrawable(Drawable drawable2) {
        boolean bl = super.verifyDrawable(drawable2) || drawable2 == this.foreground;
        return bl;
    }
}

