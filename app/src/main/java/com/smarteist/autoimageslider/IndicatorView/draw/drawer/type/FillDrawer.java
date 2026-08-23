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
import com.smarteist.autoimageslider.IndicatorView.animation.data.Value;
import com.smarteist.autoimageslider.IndicatorView.animation.data.type.ColorAnimationValue;
import com.smarteist.autoimageslider.IndicatorView.animation.data.type.FillAnimationValue;
import com.smarteist.autoimageslider.IndicatorView.draw.data.Indicator;
import com.smarteist.autoimageslider.IndicatorView.draw.drawer.type.BaseDrawer;

public class FillDrawer
extends BaseDrawer {
    private Paint strokePaint;

    public FillDrawer(Paint paint, Indicator indicator) {
        super(paint, indicator);
        this.strokePaint = paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        this.strokePaint.setAntiAlias(true);
    }

    public void draw(Canvas canvas, Value value, int n, int n2, int n3) {
        if (!(value instanceof FillAnimationValue)) {
            return;
        }
        value = (FillAnimationValue)value;
        int n4 = this.indicator.getUnselectedColor();
        float f = this.indicator.getRadius();
        int n5 = this.indicator.getStroke();
        int n6 = this.indicator.getSelectedPosition();
        int n7 = this.indicator.getSelectingPosition();
        int n8 = this.indicator.getLastSelectedPosition();
        if (this.indicator.isInteractiveAnimation()) {
            if (n == n7) {
                n4 = ((ColorAnimationValue)value).getColor();
                f = ((FillAnimationValue)value).getRadius();
                n5 = ((FillAnimationValue)value).getStroke();
            } else if (n == n6) {
                n4 = ((ColorAnimationValue)value).getColorReverse();
                f = ((FillAnimationValue)value).getRadiusReverse();
                n5 = ((FillAnimationValue)value).getStrokeReverse();
            }
        } else if (n == n6) {
            n4 = ((ColorAnimationValue)value).getColor();
            f = ((FillAnimationValue)value).getRadius();
            n5 = ((FillAnimationValue)value).getStroke();
        } else if (n == n8) {
            n4 = ((ColorAnimationValue)value).getColorReverse();
            f = ((FillAnimationValue)value).getRadiusReverse();
            n5 = ((FillAnimationValue)value).getStrokeReverse();
        }
        this.strokePaint.setColor(n4);
        this.strokePaint.setStrokeWidth((float)this.indicator.getStroke());
        canvas.drawCircle((float)n2, (float)n3, (float)this.indicator.getRadius(), this.strokePaint);
        this.strokePaint.setStrokeWidth((float)n5);
        canvas.drawCircle((float)n2, (float)n3, f, this.strokePaint);
    }
}

