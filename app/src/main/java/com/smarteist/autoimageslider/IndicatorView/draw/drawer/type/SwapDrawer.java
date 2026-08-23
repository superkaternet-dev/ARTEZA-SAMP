/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Canvas
 *  android.graphics.Paint
 */
package com.smarteist.autoimageslider.IndicatorView.draw.drawer.type;

import android.graphics.Canvas;
import android.graphics.Paint;
import com.smarteist.autoimageslider.IndicatorView.animation.data.Value;
import com.smarteist.autoimageslider.IndicatorView.animation.data.type.SwapAnimationValue;
import com.smarteist.autoimageslider.IndicatorView.draw.data.Indicator;
import com.smarteist.autoimageslider.IndicatorView.draw.data.Orientation;
import com.smarteist.autoimageslider.IndicatorView.draw.drawer.type.BaseDrawer;

public class SwapDrawer
extends BaseDrawer {
    public SwapDrawer(Paint paint, Indicator indicator) {
        super(paint, indicator);
    }

    public void draw(Canvas canvas, Value value, int n, int n2, int n3) {
        if (!(value instanceof SwapAnimationValue)) {
            return;
        }
        value = (SwapAnimationValue)value;
        int n4 = this.indicator.getSelectedColor();
        int n5 = this.indicator.getUnselectedColor();
        int n6 = this.indicator.getRadius();
        int n7 = this.indicator.getSelectedPosition();
        int n8 = this.indicator.getSelectingPosition();
        int n9 = this.indicator.getLastSelectedPosition();
        int n10 = ((SwapAnimationValue)value).getCoordinate();
        int n11 = n5;
        if (this.indicator.isInteractiveAnimation()) {
            if (n == n8) {
                n = ((SwapAnimationValue)value).getCoordinate();
                n11 = n4;
                n4 = n;
            } else {
                n4 = n10;
                if (n == n7) {
                    n4 = ((SwapAnimationValue)value).getCoordinateReverse();
                    n11 = n5;
                }
            }
        } else if (n == n9) {
            n = ((SwapAnimationValue)value).getCoordinate();
            n11 = n4;
            n4 = n;
        } else {
            n4 = n10;
            if (n == n7) {
                n4 = ((SwapAnimationValue)value).getCoordinateReverse();
                n11 = n5;
            }
        }
        this.paint.setColor(n11);
        if (this.indicator.getOrientation() == Orientation.HORIZONTAL) {
            canvas.drawCircle((float)n4, (float)n3, (float)n6, this.paint);
        } else {
            canvas.drawCircle((float)n2, (float)n4, (float)n6, this.paint);
        }
    }
}

