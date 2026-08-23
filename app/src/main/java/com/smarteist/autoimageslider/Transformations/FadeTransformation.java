/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.view.View
 */
package com.smarteist.autoimageslider.Transformations;

import android.view.View;
import com.smarteist.autoimageslider.SliderPager;

public class FadeTransformation
implements SliderPager.PageTransformer {
    @Override
    public void transformPage(View view, float f) {
        view.setTranslationX(-f * (float)view.getWidth());
        if (!(f < -1.0f) && !(f > 1.0f)) {
            if (!(f <= 0.0f) && !(f <= 1.0f)) {
                if (f == 0.0f) {
                    view.setAlpha(1.0f);
                }
            } else {
                f = f <= 0.0f ? 1.0f + f : 1.0f - f;
                view.setAlpha(f);
            }
        } else {
            view.setAlpha(0.0f);
        }
    }
}

