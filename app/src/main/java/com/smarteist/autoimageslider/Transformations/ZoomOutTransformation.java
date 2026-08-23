/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.view.View
 */
package com.smarteist.autoimageslider.Transformations;

import android.view.View;
import com.smarteist.autoimageslider.SliderPager;

public class ZoomOutTransformation
implements SliderPager.PageTransformer {
    private static final float MIN_ALPHA = 0.3f;
    private static final float MIN_SCALE = 0.65f;

    @Override
    public void transformPage(View view, float f) {
        if (f < -1.0f) {
            view.setAlpha(0.0f);
        } else if (f <= 1.0f) {
            view.setScaleX(Math.max(0.65f, 1.0f - Math.abs(f)));
            view.setScaleY(Math.max(0.65f, 1.0f - Math.abs(f)));
            view.setAlpha(Math.max(0.3f, 1.0f - Math.abs(f)));
        } else {
            view.setAlpha(0.0f);
        }
    }
}

