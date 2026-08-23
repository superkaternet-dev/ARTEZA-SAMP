/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Paint
 */
package com.smarteist.autoimageslider.IndicatorView.draw.drawer.type;

import android.graphics.Paint;
import com.smarteist.autoimageslider.IndicatorView.draw.data.Indicator;

class BaseDrawer {
    Indicator indicator;
    Paint paint;

    BaseDrawer(Paint paint, Indicator indicator) {
        this.paint = paint;
        this.indicator = indicator;
    }
}

