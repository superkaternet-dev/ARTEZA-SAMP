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
import com.smarteist.autoimageslider.IndicatorView.animation.data.type.ThinWormAnimationValue;
import com.smarteist.autoimageslider.IndicatorView.animation.data.type.WormAnimationValue;
import com.smarteist.autoimageslider.IndicatorView.draw.data.Indicator;
import com.smarteist.autoimageslider.IndicatorView.draw.data.Orientation;
import com.smarteist.autoimageslider.IndicatorView.draw.drawer.type.WormDrawer;

public class ThinWormDrawer
extends WormDrawer {
    public ThinWormDrawer(Paint paint, Indicator indicator) {
        super(paint, indicator);
    }

    @Override
    public void draw(Canvas canvas, Value value, int n, int n2) {
        if (!(value instanceof ThinWormAnimationValue)) {
            return;
        }
        value = (ThinWormAnimationValue)value;
        int n3 = ((WormAnimationValue)value).getRectStart();
        int n4 = ((WormAnimationValue)value).getRectEnd();
        int n5 = ((ThinWormAnimationValue)value).getHeight() / 2;
        int n6 = this.indicator.getRadius();
        int n7 = this.indicator.getUnselectedColor();
        int n8 = this.indicator.getSelectedColor();
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
        this.paint.setColor(n7);
        canvas.drawCircle((float)n, (float)n2, (float)n6, this.paint);
        this.paint.setColor(n8);
        canvas.drawRoundRect(this.rect, (float)n6, (float)n6, this.paint);
    }
}

