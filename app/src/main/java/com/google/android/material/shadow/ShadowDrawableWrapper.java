/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Canvas
 *  android.graphics.LinearGradient
 *  android.graphics.Paint
 *  android.graphics.Paint$Style
 *  android.graphics.Path
 *  android.graphics.Path$FillType
 *  android.graphics.RadialGradient
 *  android.graphics.Rect
 *  android.graphics.RectF
 *  android.graphics.Shader
 *  android.graphics.Shader$TileMode
 *  android.graphics.drawable.Drawable
 */
package com.google.android.material.shadow;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import androidx.appcompat.graphics.drawable.DrawableWrapper;
import androidx.core.content.ContextCompat;
import com.google.android.material.R;

public class ShadowDrawableWrapper
extends DrawableWrapper {
    static final double COS_45 = Math.cos(Math.toRadians(45.0));
    static final float SHADOW_BOTTOM_SCALE = 1.0f;
    static final float SHADOW_HORIZ_SCALE = 0.5f;
    static final float SHADOW_MULTIPLIER = 1.5f;
    static final float SHADOW_TOP_SCALE = 0.25f;
    private boolean addPaddingForCorners = true;
    final RectF contentBounds;
    float cornerRadius;
    final Paint cornerShadowPaint;
    Path cornerShadowPath;
    private boolean dirty = true;
    final Paint edgeShadowPaint;
    float maxShadowSize;
    private boolean printedShadowClipWarning = false;
    float rawMaxShadowSize;
    float rawShadowSize;
    private float rotation;
    private final int shadowEndColor;
    private final int shadowMiddleColor;
    float shadowSize;
    private final int shadowStartColor;

    public ShadowDrawableWrapper(Context context, Drawable drawable2, float f, float f2, float f3) {
        super(drawable2);
        this.shadowStartColor = ContextCompat.getColor(context, R.color.design_fab_shadow_start_color);
        this.shadowMiddleColor = ContextCompat.getColor(context, R.color.design_fab_shadow_mid_color);
        this.shadowEndColor = ContextCompat.getColor(context, R.color.design_fab_shadow_end_color);
        context = new Paint(5);
        this.cornerShadowPaint = context;
        context.setStyle(Paint.Style.FILL);
        this.cornerRadius = Math.round(f);
        this.contentBounds = new RectF();
        context = new Paint((Paint)context);
        this.edgeShadowPaint = context;
        context.setAntiAlias(false);
        this.setShadowSize(f2, f3);
    }

    private void buildComponents(Rect rect) {
        float f = this.rawMaxShadowSize * 1.5f;
        this.contentBounds.set((float)rect.left + this.rawMaxShadowSize, (float)rect.top + f, (float)rect.right - this.rawMaxShadowSize, (float)rect.bottom - f);
        this.getWrappedDrawable().setBounds((int)this.contentBounds.left, (int)this.contentBounds.top, (int)this.contentBounds.right, (int)this.contentBounds.bottom);
        this.buildShadowCorners();
    }

    private void buildShadowCorners() {
        int n;
        int n2;
        int n3;
        float f = this.cornerRadius;
        RectF rectF = new RectF(-f, -f, f, f);
        RectF rectF2 = new RectF(rectF);
        f = this.shadowSize;
        rectF2.inset(-f, -f);
        Path path = this.cornerShadowPath;
        if (path == null) {
            this.cornerShadowPath = new Path();
        } else {
            path.reset();
        }
        this.cornerShadowPath.setFillType(Path.FillType.EVEN_ODD);
        this.cornerShadowPath.moveTo(-this.cornerRadius, 0.0f);
        this.cornerShadowPath.rLineTo(-this.shadowSize, 0.0f);
        this.cornerShadowPath.arcTo(rectF2, 180.0f, 90.0f, false);
        this.cornerShadowPath.arcTo(rectF, 270.0f, -90.0f, false);
        this.cornerShadowPath.close();
        float f2 = -rectF2.top;
        if (f2 > 0.0f) {
            float f3 = this.cornerRadius / f2;
            f = (1.0f - f3) / 2.0f;
            Paint paint = this.cornerShadowPaint;
            n3 = this.shadowStartColor;
            n2 = this.shadowMiddleColor;
            n = this.shadowEndColor;
            path = Shader.TileMode.CLAMP;
            paint.setShader((Shader)new RadialGradient(0.0f, 0.0f, f2, new int[]{0, n3, n2, n}, new float[]{0.0f, f3, f3 + f, 1.0f}, (Shader.TileMode)path));
        }
        path = this.edgeShadowPaint;
        f2 = rectF.top;
        f = rectF2.top;
        n = this.shadowStartColor;
        n2 = this.shadowMiddleColor;
        n3 = this.shadowEndColor;
        rectF = Shader.TileMode.CLAMP;
        path.setShader((Shader)new LinearGradient(0.0f, f2, 0.0f, f, new int[]{n, n2, n3}, new float[]{0.0f, 0.5f, 1.0f}, (Shader.TileMode)rectF));
        this.edgeShadowPaint.setAntiAlias(false);
    }

    public static float calculateHorizontalPadding(float f, float f2, boolean bl) {
        if (bl) {
            double d = f;
            double d2 = COS_45;
            double d3 = f2;
            Double.isNaN(d3);
            Double.isNaN(d);
            return (float)(d + (1.0 - d2) * d3);
        }
        return f;
    }

    public static float calculateVerticalPadding(float f, float f2, boolean bl) {
        if (bl) {
            double d = 1.5f * f;
            double d2 = COS_45;
            double d3 = f2;
            Double.isNaN(d3);
            Double.isNaN(d);
            return (float)(d + (1.0 - d2) * d3);
        }
        return 1.5f * f;
    }

    private void drawShadow(Canvas canvas) {
        int n = canvas.save();
        canvas.rotate(this.rotation, this.contentBounds.centerX(), this.contentBounds.centerY());
        float f = -this.cornerRadius - this.shadowSize;
        float f2 = this.cornerRadius;
        float f3 = this.contentBounds.width();
        boolean bl = true;
        int n2 = f3 - f2 * 2.0f > 0.0f ? 1 : 0;
        if (!(this.contentBounds.height() - f2 * 2.0f > 0.0f)) {
            bl = false;
        }
        float f4 = this.rawShadowSize;
        f3 = f2 / (f2 + (f4 - 0.5f * f4));
        float f5 = f2 / (f2 + (f4 - 0.25f * f4));
        f4 = f2 / (f2 + (f4 - f4 * 1.0f));
        int n3 = canvas.save();
        canvas.translate(this.contentBounds.left + f2, this.contentBounds.top + f2);
        canvas.scale(f3, f5);
        canvas.drawPath(this.cornerShadowPath, this.cornerShadowPaint);
        if (n2 != 0) {
            canvas.scale(1.0f / f3, 1.0f);
            canvas.drawRect(0.0f, f, this.contentBounds.width() - f2 * 2.0f, -this.cornerRadius, this.edgeShadowPaint);
        }
        canvas.restoreToCount(n3);
        n3 = canvas.save();
        canvas.translate(this.contentBounds.right - f2, this.contentBounds.bottom - f2);
        canvas.scale(f3, f4);
        canvas.rotate(180.0f);
        canvas.drawPath(this.cornerShadowPath, this.cornerShadowPaint);
        if (n2 != 0) {
            canvas.scale(1.0f / f3, 1.0f);
            canvas.drawRect(0.0f, f, this.contentBounds.width() - f2 * 2.0f, -this.cornerRadius + this.shadowSize, this.edgeShadowPaint);
        }
        canvas.restoreToCount(n3);
        n2 = canvas.save();
        canvas.translate(this.contentBounds.left + f2, this.contentBounds.bottom - f2);
        canvas.scale(f3, f4);
        canvas.rotate(270.0f);
        canvas.drawPath(this.cornerShadowPath, this.cornerShadowPaint);
        if (bl) {
            canvas.scale(1.0f / f4, 1.0f);
            canvas.drawRect(0.0f, f, this.contentBounds.height() - f2 * 2.0f, -this.cornerRadius, this.edgeShadowPaint);
        }
        canvas.restoreToCount(n2);
        n2 = canvas.save();
        canvas.translate(this.contentBounds.right - f2, this.contentBounds.top + f2);
        canvas.scale(f3, f5);
        canvas.rotate(90.0f);
        canvas.drawPath(this.cornerShadowPath, this.cornerShadowPaint);
        if (bl) {
            canvas.scale(1.0f / f5, 1.0f);
            canvas.drawRect(0.0f, f, this.contentBounds.height() - 2.0f * f2, -this.cornerRadius, this.edgeShadowPaint);
        }
        canvas.restoreToCount(n2);
        canvas.restoreToCount(n);
    }

    private static int toEven(float f) {
        int n;
        block0: {
            n = Math.round(f);
            if (n % 2 != 1) break block0;
            --n;
        }
        return n;
    }

    @Override
    public void draw(Canvas canvas) {
        if (this.dirty) {
            this.buildComponents(this.getBounds());
            this.dirty = false;
        }
        this.drawShadow(canvas);
        super.draw(canvas);
    }

    public float getCornerRadius() {
        return this.cornerRadius;
    }

    public float getMaxShadowSize() {
        return this.rawMaxShadowSize;
    }

    public float getMinHeight() {
        float f = this.rawMaxShadowSize;
        f = Math.max(f, this.cornerRadius + f * 1.5f / 2.0f);
        return this.rawMaxShadowSize * 1.5f * 2.0f + f * 2.0f;
    }

    public float getMinWidth() {
        float f = this.rawMaxShadowSize;
        f = Math.max(f, this.cornerRadius + f / 2.0f);
        return this.rawMaxShadowSize * 2.0f + f * 2.0f;
    }

    @Override
    public int getOpacity() {
        return -3;
    }

    @Override
    public boolean getPadding(Rect rect) {
        int n = (int)Math.ceil(ShadowDrawableWrapper.calculateVerticalPadding(this.rawMaxShadowSize, this.cornerRadius, this.addPaddingForCorners));
        int n2 = (int)Math.ceil(ShadowDrawableWrapper.calculateHorizontalPadding(this.rawMaxShadowSize, this.cornerRadius, this.addPaddingForCorners));
        rect.set(n2, n, n2, n);
        return true;
    }

    public float getShadowSize() {
        return this.rawShadowSize;
    }

    @Override
    protected void onBoundsChange(Rect rect) {
        this.dirty = true;
    }

    public void setAddPaddingForCorners(boolean bl) {
        this.addPaddingForCorners = bl;
        this.invalidateSelf();
    }

    @Override
    public void setAlpha(int n) {
        super.setAlpha(n);
        this.cornerShadowPaint.setAlpha(n);
        this.edgeShadowPaint.setAlpha(n);
    }

    public void setCornerRadius(float f) {
        if (this.cornerRadius == (f = (float)Math.round(f))) {
            return;
        }
        this.cornerRadius = f;
        this.dirty = true;
        this.invalidateSelf();
    }

    public void setMaxShadowSize(float f) {
        this.setShadowSize(this.rawShadowSize, f);
    }

    public final void setRotation(float f) {
        if (this.rotation != f) {
            this.rotation = f;
            this.invalidateSelf();
        }
    }

    public void setShadowSize(float f) {
        this.setShadowSize(f, this.rawMaxShadowSize);
    }

    public void setShadowSize(float f, float f2) {
        if (!(f < 0.0f) && !(f2 < 0.0f)) {
            float f3 = ShadowDrawableWrapper.toEven(f);
            float f4 = ShadowDrawableWrapper.toEven(f2);
            f = f3;
            if (f3 > f4) {
                f = f2 = f4;
                if (!this.printedShadowClipWarning) {
                    this.printedShadowClipWarning = true;
                    f = f2;
                }
            }
            if (this.rawShadowSize == f && this.rawMaxShadowSize == f4) {
                return;
            }
            this.rawShadowSize = f;
            this.rawMaxShadowSize = f4;
            this.shadowSize = Math.round(1.5f * f);
            this.maxShadowSize = f4;
            this.dirty = true;
            this.invalidateSelf();
            return;
        }
        throw new IllegalArgumentException("invalid shadow size");
    }
}

