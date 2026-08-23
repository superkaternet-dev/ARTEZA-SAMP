/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Canvas
 *  android.graphics.Paint
 *  android.graphics.Paint$Style
 */
package com.smarteist.autoimageslider.IndicatorView.draw.drawer.type;

import android.graphics.Canvas;
import android.graphics.Paint;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.IndicatorView.draw.data.Indicator;
import com.smarteist.autoimageslider.IndicatorView.draw.drawer.type.BaseDrawer;

public class BasicDrawer
extends BaseDrawer {
    private Paint strokePaint;

    public BasicDrawer(Paint paint, Indicator indicator) {
        super(paint, indicator);
        this.strokePaint = paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        this.strokePaint.setAntiAlias(true);
        this.strokePaint.setStrokeWidth((float)indicator.getStroke());
    }

    public void draw(Canvas canvas, int n, boolean bl, int n2, int n3) {
        float f;
        float f2 = this.indicator.getRadius();
        int n4 = this.indicator.getStroke();
        float f3 = this.indicator.getScaleFactor();
        int n5 = this.indicator.getSelectedColor();
        int n6 = this.indicator.getUnselectedColor();
        int n7 = this.indicator.getSelectedPosition();
        IndicatorAnimationType indicatorAnimationType = this.indicator.getAnimationType();
        if (indicatorAnimationType == IndicatorAnimationType.SCALE && !bl) {
            f = f2 * f3;
        } else {
            f = f2;
            if (indicatorAnimationType == IndicatorAnimationType.SCALE_DOWN) {
                f = f2;
                if (bl) {
                    f = f2 * f3;
                }
            }
        }
        if (n == n7) {
            n6 = n5;
        }
        if (indicatorAnimationType == IndicatorAnimationType.FILL && n != n7) {
            indicatorAnimationType = this.strokePaint;
            indicatorAnimationType.setStrokeWidth(n4);
        } else {
            indicatorAnimationType = this.paint;
        }
        indicatorAnimationType.setColor(n6);
        canvas.drawCircle((float)n2, (float)n3, f, (Paint)indicatorAnimationType);
    }
}

