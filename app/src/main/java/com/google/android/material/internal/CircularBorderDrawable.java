/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.res.ColorStateList
 *  android.graphics.Canvas
 *  android.graphics.ColorFilter
 *  android.graphics.LinearGradient
 *  android.graphics.Paint
 *  android.graphics.Paint$Style
 *  android.graphics.Rect
 *  android.graphics.RectF
 *  android.graphics.Shader
 *  android.graphics.Shader$TileMode
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.Drawable$ConstantState
 */
package com.google.android.material.internal;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import androidx.core.graphics.ColorUtils;

public class CircularBorderDrawable
extends Drawable {
    private static final float DRAW_STROKE_WIDTH_MULTIPLE = 1.3333f;
    private ColorStateList borderTint;
    float borderWidth;
    private int bottomInnerStrokeColor;
    private int bottomOuterStrokeColor;
    private int currentBorderTintColor;
    private boolean invalidateShader = true;
    final Paint paint;
    final Rect rect = new Rect();
    final RectF rectF = new RectF();
    private float rotation;
    final CircularBorderState state = new CircularBorderState(this);
    private int topInnerStrokeColor;
    private int topOuterStrokeColor;

    public CircularBorderDrawable() {
        Paint paint;
        this.paint = paint = new Paint(1);
        paint.setStyle(Paint.Style.STROKE);
    }

    private Shader createGradientShader() {
        Rect rect = this.rect;
        this.copyBounds(rect);
        float f = this.borderWidth / (float)rect.height();
        int n = ColorUtils.compositeColors(this.topOuterStrokeColor, this.currentBorderTintColor);
        int n2 = ColorUtils.compositeColors(this.topInnerStrokeColor, this.currentBorderTintColor);
        int n3 = ColorUtils.compositeColors(ColorUtils.setAlphaComponent(this.topInnerStrokeColor, 0), this.currentBorderTintColor);
        int n4 = ColorUtils.compositeColors(ColorUtils.setAlphaComponent(this.bottomInnerStrokeColor, 0), this.currentBorderTintColor);
        int n5 = ColorUtils.compositeColors(this.bottomInnerStrokeColor, this.currentBorderTintColor);
        int n6 = ColorUtils.compositeColors(this.bottomOuterStrokeColor, this.currentBorderTintColor);
        float f2 = rect.top;
        float f3 = rect.bottom;
        rect = Shader.TileMode.CLAMP;
        return new LinearGradient(0.0f, f2, 0.0f, f3, new int[]{n, n2, n3, n4, n5, n6}, new float[]{0.0f, f, 0.5f, 0.5f, 1.0f - f, 1.0f}, (Shader.TileMode)rect);
    }

    public void draw(Canvas canvas) {
        if (this.invalidateShader) {
            this.paint.setShader(this.createGradientShader());
            this.invalidateShader = false;
        }
        float f = this.paint.getStrokeWidth() / 2.0f;
        RectF rectF = this.rectF;
        this.copyBounds(this.rect);
        rectF.set(this.rect);
        rectF.left += f;
        rectF.top += f;
        rectF.right -= f;
        rectF.bottom -= f;
        canvas.save();
        canvas.rotate(this.rotation, rectF.centerX(), rectF.centerY());
        canvas.drawOval(rectF, this.paint);
        canvas.restore();
    }

    public Drawable.ConstantState getConstantState() {
        return this.state;
    }

    public int getOpacity() {
        int n = this.borderWidth > 0.0f ? -3 : -2;
        return n;
    }

    public boolean getPadding(Rect rect) {
        int n = Math.round(this.borderWidth);
        rect.set(n, n, n, n);
        return true;
    }

    public boolean isStateful() {
        ColorStateList colorStateList = this.borderTint;
        boolean bl = colorStateList != null && colorStateList.isStateful() || super.isStateful();
        return bl;
    }

    protected void onBoundsChange(Rect rect) {
        this.invalidateShader = true;
    }

    protected boolean onStateChange(int[] nArray) {
        int n;
        ColorStateList colorStateList = this.borderTint;
        if (colorStateList != null && (n = colorStateList.getColorForState(nArray, this.currentBorderTintColor)) != this.currentBorderTintColor) {
            this.invalidateShader = true;
            this.currentBorderTintColor = n;
        }
        if (this.invalidateShader) {
            this.invalidateSelf();
        }
        return this.invalidateShader;
    }

    public void setAlpha(int n) {
        this.paint.setAlpha(n);
        this.invalidateSelf();
    }

    public void setBorderTint(ColorStateList colorStateList) {
        if (colorStateList != null) {
            this.currentBorderTintColor = colorStateList.getColorForState(this.getState(), this.currentBorderTintColor);
        }
        this.borderTint = colorStateList;
        this.invalidateShader = true;
        this.invalidateSelf();
    }

    public void setBorderWidth(float f) {
        if (this.borderWidth != f) {
            this.borderWidth = f;
            this.paint.setStrokeWidth(1.3333f * f);
            this.invalidateShader = true;
            this.invalidateSelf();
        }
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.paint.setColorFilter(colorFilter);
        this.invalidateSelf();
    }

    public void setGradientColors(int n, int n2, int n3, int n4) {
        this.topOuterStrokeColor = n;
        this.topInnerStrokeColor = n2;
        this.bottomOuterStrokeColor = n3;
        this.bottomInnerStrokeColor = n4;
    }

    public final void setRotation(float f) {
        if (f != this.rotation) {
            this.rotation = f;
            this.invalidateSelf();
        }
    }

    private class CircularBorderState
    extends Drawable.ConstantState {
        final CircularBorderDrawable this$0;

        private CircularBorderState(CircularBorderDrawable circularBorderDrawable) {
            this.this$0 = circularBorderDrawable;
        }

        public int getChangingConfigurations() {
            return 0;
        }

        public Drawable newDrawable() {
            return this.this$0;
        }
    }
}

