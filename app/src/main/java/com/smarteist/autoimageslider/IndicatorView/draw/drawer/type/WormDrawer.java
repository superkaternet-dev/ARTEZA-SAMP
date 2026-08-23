/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Canvas
 *  android.graphics.Paint
 *  android.graphics.RectF
 */
package com.smarteist.autoimageslider.IndicatorView.draw.drawer.type;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import com.smarteist.autoimageslider.IndicatorView.animation.data.Value;
import com.smarteist.autoimageslider.IndicatorView.animation.data.type.WormAnimationValue;
import com.smarteist.autoimageslider.IndicatorView.draw.data.Indicator;
import com.smarteist.autoimageslider.IndicatorView.draw.data.Orientation;
import com.smarteist.autoimageslider.IndicatorView.draw.drawer.type.BaseDrawer;

public class WormDrawer
extends BaseDrawer {
    public RectF rect = new RectF();

    public WormDrawer(Paint paint, Indicator indicator) {
        super(paint, indicator);
    }

    public void draw(Canvas canvas, Value value, int n, int n2) {
        if (!(value instanceof WormAnimationValue)) {
            return;
        }
        value = (WormAnimationValue)value;
        int n3 = ((WormAnimationValue)value).getRectStart();
        int n4 = ((WormAnimationValue)value).getRectEnd();
        int n5 = this.indicator.getRadius();
        int n6 = this.indicator.getUnselectedColor();
        int n7 = this.indicator.getSelectedColor();
        if (this.indicator.getOrientation() == Orientation.HORIZONTAL) {
            this.rect.left = n3;
            this.rect.right = n4;
            this.rect.top = n2 - n5;
            this.rect.bottom = n2 + n5;
        } else {
            this.rect.left = n - n5;
            this.rect.right = n + n5;
            this.rect.top = n3;
            this.rect.bottom = n4;
        }
        this.paint.setColor(n6);
        canvas.drawCircle((float)n, (float)n2, (float)n5, this.paint);
        this.paint.setColor(n7);
        canvas.drawRoundRect(this.rect, (float)n5, (float)n5, this.paint);
    }
}

