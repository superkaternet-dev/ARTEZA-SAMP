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
import com.smarteist.autoimageslider.IndicatorView.animation.data.type.ColorAnimationValue;
import com.smarteist.autoimageslider.IndicatorView.draw.data.Indicator;
import com.smarteist.autoimageslider.IndicatorView.draw.drawer.type.BaseDrawer;

public class ColorDrawer
extends BaseDrawer {
    public ColorDrawer(Paint paint, Indicator indicator) {
        super(paint, indicator);
    }

    public void draw(Canvas canvas, Value value, int n, int n2, int n3) {
        if (!(value instanceof ColorAnimationValue)) {
            return;
        }
        value = (ColorAnimationValue)value;
        float f = this.indicator.getRadius();
        int n4 = this.indicator.getSelectedColor();
        int n5 = this.indicator.getSelectedPosition();
        int n6 = this.indicator.getSelectingPosition();
        int n7 = this.indicator.getLastSelectedPosition();
        if (this.indicator.isInteractiveAnimation()) {
            if (n == n6) {
                n4 = ((ColorAnimationValue)value).getColor();
            } else if (n == n5) {
                n4 = ((ColorAnimationValue)value).getColorReverse();
            }
        } else if (n == n5) {
            n4 = ((ColorAnimationValue)value).getColor();
        } else if (n == n7) {
            n4 = ((ColorAnimationValue)value).getColorReverse();
        }
        this.paint.setColor(n4);
        canvas.drawCircle((float)n2, (float)n3, f, this.paint);
    }
}

