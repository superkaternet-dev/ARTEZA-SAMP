/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 *  android.view.View
 */
package com.smarteist.autoimageslider.Transformations;

import android.util.Log;
import android.view.View;
import com.smarteist.autoimageslider.SliderPager;

public class HorizontalFlipTransformation
implements SliderPager.PageTransformer {
    @Override
    public void transformPage(View object, float f) {
        object.setTranslationX(-f * (float)object.getWidth());
        object.setCameraDistance(20000.0f);
        if ((double)f < 0.5 && (double)f > -0.5) {
            object.setVisibility(0);
        } else {
            object.setVisibility(4);
        }
        if (f < -1.0f) {
            object.setAlpha(0.0f);
        } else if (f <= 0.0f) {
            object.setAlpha(1.0f);
            object.setRotationX((1.0f - Math.abs(f) + 1.0f) * 180.0f);
            object = new StringBuilder();
            ((StringBuilder)object).append("position <= 0     ");
            ((StringBuilder)object).append((1.0f - Math.abs(f) + 1.0f) * 180.0f);
            Log.e((String)"HORIZONTAL", (String)((StringBuilder)object).toString());
        } else if (f <= 1.0f) {
            object.setAlpha(1.0f);
            object.setRotationX((1.0f - Math.abs(f) + 1.0f) * -180.0f);
            object = new StringBuilder();
            ((StringBuilder)object).append("position <= 1     ");
            ((StringBuilder)object).append((1.0f - Math.abs(f) + 1.0f) * -180.0f);
            Log.e((String)"HORIZONTAL", (String)((StringBuilder)object).toString());
        } else {
            object.setAlpha(0.0f);
        }
    }
}

