/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.res.Resources
 *  android.graphics.Bitmap
 *  android.graphics.Canvas
 *  android.graphics.ColorFilter
 *  android.graphics.Paint
 *  android.graphics.RadialGradient
 *  android.graphics.Shader
 *  android.graphics.Shader$TileMode
 *  android.graphics.SweepGradient
 *  android.graphics.drawable.BitmapDrawable
 */
package com.skydoves.colorpickerview;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.drawable.BitmapDrawable;

public class ColorHsvPalette
extends BitmapDrawable {
    private Paint huePaint = new Paint(1);
    private Paint saturationPaint = new Paint(1);

    public ColorHsvPalette(Resources resources, Bitmap bitmap) {
        super(resources, bitmap);
    }

    public void draw(Canvas canvas) {
        int n = this.getBounds().width();
        int n2 = this.getBounds().height();
        float f = (float)n * 0.5f;
        float f2 = (float)n2 * 0.5f;
        float f3 = 0.5f * (float)Math.min(n, n2);
        SweepGradient sweepGradient = new SweepGradient(f, f2, new int[]{-65536, -65281, -16776961, -16711681, -16711936, -256, -65536}, new float[]{0.0f, 0.166f, 0.333f, 0.499f, 0.666f, 0.833f, 0.999f});
        this.huePaint.setShader((Shader)sweepGradient);
        sweepGradient = new RadialGradient(f, f2, f3, -1, 0xFFFFFF, Shader.TileMode.CLAMP);
        this.saturationPaint.setShader((Shader)sweepGradient);
        canvas.drawCircle(f, f2, f3, this.huePaint);
        canvas.drawCircle(f, f2, f3, this.saturationPaint);
    }

    public int getOpacity() {
        return -1;
    }

    public void setAlpha(int n) {
        this.huePaint.setAlpha(n);
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.huePaint.setColorFilter(colorFilter);
    }
}

