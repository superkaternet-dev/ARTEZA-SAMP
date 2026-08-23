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
import com.smarteist.autoimageslider.IndicatorView.animation.data.type.SlideAnimationValue;
import com.smarteist.autoimageslider.IndicatorView.draw.data.Indicator;
import com.smarteist.autoimageslider.IndicatorView.draw.data.Orientation;
import com.smarteist.autoimageslider.IndicatorView.draw.drawer.type.BaseDrawer;

public class SlideDrawer
extends BaseDrawer {
    public SlideDrawer(Paint paint, Indicator indicator) {
        super(paint, indicator);
    }

    public void draw(Canvas canvas, Value value, int n, int n2) {
        if (!(value instanceof SlideAnimationValue)) {
            return;
        }
        int n3 = ((SlideAnimationValue)value).getCoordinate();
        int n4 = this.indicator.getUnselectedColor();
        int n5 = this.indicator.getSelectedColor();
        int n6 = this.indicator.getRadius();
        this.paint.setColor(n4);
        canvas.drawCircle((float)n, (float)n2, (float)n6, this.paint);
        this.paint.setColor(n5);
        if (this.indicator.getOrientation() == Orientation.HORIZONTAL) {
            canvas.drawCircle((float)n3, (float)n2, (float)n6, this.paint);
        } else {
            canvas.drawCircle((float)n, (float)n3, (float)n6, this.paint);
        }
    }
}

