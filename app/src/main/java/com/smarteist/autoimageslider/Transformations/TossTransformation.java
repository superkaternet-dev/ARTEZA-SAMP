/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.view.View
 */
package com.smarteist.autoimageslider.Transformations;

import android.view.View;
import com.smarteist.autoimageslider.SliderPager;

public class TossTransformation
implements SliderPager.PageTransformer {
    @Override
    public void transformPage(View view, float f) {
        view.setTranslationX(-f * (float)view.getWidth());
        view.setCameraDistance(20000.0f);
        if ((double)f < 0.5 && (double)f > -0.5) {
            view.setVisibility(0);
        } else {
            view.setVisibility(4);
        }
        if (f < -1.0f) {
            view.setAlpha(0.0f);
        } else if (f <= 0.0f) {
            view.setAlpha(1.0f);
            view.setScaleX(Math.max(0.4f, 1.0f - Math.abs(f)));
            view.setScaleY(Math.max(0.4f, 1.0f - Math.abs(f)));
            view.setRotationX((1.0f - Math.abs(f) + 1.0f) * 1080.0f);
            view.setTranslationY(Math.abs(f) * -1000.0f);
        } else if (f <= 1.0f) {
            view.setAlpha(1.0f);
            view.setScaleX(Math.max(0.4f, 1.0f - Math.abs(f)));
            view.setScaleY(Math.max(0.4f, 1.0f - Math.abs(f)));
            view.setRotationX((1.0f - Math.abs(f) + 1.0f) * -1080.0f);
            view.setTranslationY(Math.abs(f) * -1000.0f);
        } else {
            view.setAlpha(0.0f);
        }
    }
}

