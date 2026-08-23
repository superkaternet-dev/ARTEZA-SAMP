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
import com.smarteist.autoimageslider.IndicatorView.animation.data.type.DropAnimationValue;
import com.smarteist.autoimageslider.IndicatorView.draw.data.Indicator;
import com.smarteist.autoimageslider.IndicatorView.draw.data.Orientation;
import com.smarteist.autoimageslider.IndicatorView.draw.drawer.type.BaseDrawer;

public class DropDrawer
extends BaseDrawer {
    public DropDrawer(Paint paint, Indicator indicator) {
        super(paint, indicator);
    }

    public void draw(Canvas canvas, Value value, int n, int n2) {
        if (!(value instanceof DropAnimationValue)) {
            return;
        }
        value = (DropAnimationValue)value;
        int n3 = this.indicator.getUnselectedColor();
        int n4 = this.indicator.getSelectedColor();
        float f = this.indicator.getRadius();
        this.paint.setColor(n3);
        canvas.drawCircle((float)n, (float)n2, f, this.paint);
        this.paint.setColor(n4);
        if (this.indicator.getOrientation() == Orientation.HORIZONTAL) {
            canvas.drawCircle((float)((DropAnimationValue)value).getWidth(), (float)((DropAnimationValue)value).getHeight(), (float)((DropAnimationValue)value).getRadius(), this.paint);
        } else {
            canvas.drawCircle((float)((DropAnimationValue)value).getHeight(), (float)((DropAnimationValue)value).getWidth(), (float)((DropAnimationValue)value).getRadius(), this.paint);
        }
    }
}

