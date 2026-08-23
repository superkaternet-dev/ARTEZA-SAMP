/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Canvas
 *  android.graphics.Paint
 *  android.graphics.Paint$Style
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.PorterDuffXfermode
 *  android.graphics.RectF
 *  android.graphics.Xfermode
 *  android.graphics.drawable.Drawable$Callback
 *  android.graphics.drawable.GradientDrawable
 *  android.os.Build$VERSION
 *  android.view.View
 */
package com.google.android.material.textfield;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.View;

class CutoutDrawable
extends GradientDrawable {
    private final RectF cutoutBounds;
    private final Paint cutoutPaint = new Paint(1);
    private int savedLayer;

    CutoutDrawable() {
        this.setPaintStyles();
        this.cutoutBounds = new RectF();
    }

    private void postDraw(Canvas canvas) {
        if (!this.useHardwareLayer(this.getCallback())) {
            canvas.restoreToCount(this.savedLayer);
        }
    }

    private void preDraw(Canvas canvas) {
        Drawable.Callback callback = this.getCallback();
        if (this.useHardwareLayer(callback)) {
            ((View)callback).setLayerType(2, null);
        } else {
            this.saveCanvasLayer(canvas);
        }
    }

    private void saveCanvasLayer(Canvas canvas) {
        this.savedLayer = Build.VERSION.SDK_INT >= 21 ? canvas.saveLayer(0.0f, 0.0f, (float)canvas.getWidth(), (float)canvas.getHeight(), null) : canvas.saveLayer(0.0f, 0.0f, (float)canvas.getWidth(), (float)canvas.getHeight(), null, 31);
    }

    private void setPaintStyles() {
        this.cutoutPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        this.cutoutPaint.setColor(-1);
        this.cutoutPaint.setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
    }

    private boolean useHardwareLayer(Drawable.Callback callback) {
        return callback instanceof View;
    }

    public void draw(Canvas canvas) {
        this.preDraw(canvas);
        super.draw(canvas);
        canvas.drawRect(this.cutoutBounds, this.cutoutPaint);
        this.postDraw(canvas);
    }

    boolean hasCutout() {
        return this.cutoutBounds.isEmpty() ^ true;
    }

    void removeCutout() {
        this.setCutout(0.0f, 0.0f, 0.0f, 0.0f);
    }

    void setCutout(float f, float f2, float f3, float f4) {
        if (f != this.cutoutBounds.left || f2 != this.cutoutBounds.top || f3 != this.cutoutBounds.right || f4 != this.cutoutBounds.bottom) {
            this.cutoutBounds.set(f, f2, f3, f4);
            this.invalidateSelf();
        }
    }

    void setCutout(RectF rectF) {
        this.setCutout(rectF.left, rectF.top, rectF.right, rectF.bottom);
    }
}

