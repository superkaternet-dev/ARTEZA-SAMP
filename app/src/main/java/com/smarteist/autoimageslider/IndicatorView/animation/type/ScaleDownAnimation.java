/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.IntEvaluator
 *  android.animation.PropertyValuesHolder
 *  android.animation.TypeEvaluator
 */
package com.smarteist.autoimageslider.IndicatorView.animation.type;

import android.animation.IntEvaluator;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import com.smarteist.autoimageslider.IndicatorView.animation.controller.ValueController;
import com.smarteist.autoimageslider.IndicatorView.animation.type.ScaleAnimation;

public class ScaleDownAnimation
extends ScaleAnimation {
    public ScaleDownAnimation(ValueController.UpdateListener updateListener) {
        super(updateListener);
    }

    @Override
    protected PropertyValuesHolder createScalePropertyHolder(boolean bl) {
        int n;
        int n2;
        String string2;
        if (bl) {
            string2 = "ANIMATION_SCALE_REVERSE";
            n2 = (int)((float)this.radius * this.scaleFactor);
            n = this.radius;
        } else {
            string2 = "ANIMATION_SCALE";
            n2 = this.radius;
            n = (int)((float)this.radius * this.scaleFactor);
        }
        string2 = PropertyValuesHolder.ofInt((String)string2, (int[])new int[]{n2, n});
        string2.setEvaluator((TypeEvaluator)new IntEvaluator());
        return string2;
    }
}

