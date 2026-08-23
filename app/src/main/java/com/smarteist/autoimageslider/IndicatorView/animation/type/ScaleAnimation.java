/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.IntEvaluator
 *  android.animation.PropertyValuesHolder
 *  android.animation.TimeInterpolator
 *  android.animation.TypeEvaluator
 *  android.animation.ValueAnimator
 *  android.animation.ValueAnimator$AnimatorUpdateListener
 *  android.view.animation.AccelerateDecelerateInterpolator
 */
package com.smarteist.autoimageslider.IndicatorView.animation.type;

import android.animation.IntEvaluator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import com.smarteist.autoimageslider.IndicatorView.animation.controller.ValueController;
import com.smarteist.autoimageslider.IndicatorView.animation.data.type.ScaleAnimationValue;
import com.smarteist.autoimageslider.IndicatorView.animation.type.ColorAnimation;

public class ScaleAnimation
extends ColorAnimation {
    static final String ANIMATION_SCALE = "ANIMATION_SCALE";
    static final String ANIMATION_SCALE_REVERSE = "ANIMATION_SCALE_REVERSE";
    public static final float DEFAULT_SCALE_FACTOR = 0.7f;
    public static final float MAX_SCALE_FACTOR = 1.0f;
    public static final float MIN_SCALE_FACTOR = 0.3f;
    int radius;
    float scaleFactor;
    private ScaleAnimationValue value = new ScaleAnimationValue();

    public ScaleAnimation(ValueController.UpdateListener updateListener) {
        super(updateListener);
    }

    private boolean hasChanges(int n, int n2, int n3, float f) {
        if (this.colorStart != n) {
            return true;
        }
        if (this.colorEnd != n2) {
            return true;
        }
        if (this.radius != n3) {
            return true;
        }
        return this.scaleFactor != f;
    }

    private void onAnimateUpdated(ValueAnimator valueAnimator) {
        int n = (Integer)valueAnimator.getAnimatedValue("ANIMATION_COLOR");
        int n2 = (Integer)valueAnimator.getAnimatedValue("ANIMATION_COLOR_REVERSE");
        int n3 = (Integer)valueAnimator.getAnimatedValue(ANIMATION_SCALE);
        int n4 = (Integer)valueAnimator.getAnimatedValue(ANIMATION_SCALE_REVERSE);
        this.value.setColor(n);
        this.value.setColorReverse(n2);
        this.value.setRadius(n3);
        this.value.setRadiusReverse(n4);
        if (this.listener != null) {
            this.listener.onValueUpdated(this.value);
        }
    }

    @Override
    public ValueAnimator createAnimator() {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(350L);
        valueAnimator.setInterpolator((TimeInterpolator)new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(this){
            final ScaleAnimation this$0;
            {
                this.this$0 = scaleAnimation;
            }

            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                this.this$0.onAnimateUpdated(valueAnimator);
            }
        });
        return valueAnimator;
    }

    protected PropertyValuesHolder createScalePropertyHolder(boolean bl) {
        int n;
        int n2;
        String string2;
        if (bl) {
            string2 = ANIMATION_SCALE_REVERSE;
            n2 = this.radius;
            n = (int)((float)this.radius * this.scaleFactor);
        } else {
            string2 = ANIMATION_SCALE;
            n2 = (int)((float)this.radius * this.scaleFactor);
            n = this.radius;
        }
        string2 = PropertyValuesHolder.ofInt((String)string2, (int[])new int[]{n2, n});
        string2.setEvaluator((TypeEvaluator)new IntEvaluator());
        return string2;
    }

    public ScaleAnimation with(int n, int n2, int n3, float f) {
        if (this.animator != null && this.hasChanges(n, n2, n3, f)) {
            this.colorStart = n;
            this.colorEnd = n2;
            this.radius = n3;
            this.scaleFactor = f;
            PropertyValuesHolder propertyValuesHolder = this.createColorPropertyHolder(false);
            PropertyValuesHolder propertyValuesHolder2 = this.createColorPropertyHolder(true);
            PropertyValuesHolder propertyValuesHolder3 = this.createScalePropertyHolder(false);
            PropertyValuesHolder propertyValuesHolder4 = this.createScalePropertyHolder(true);
            ((ValueAnimator)this.animator).setValues(new PropertyValuesHolder[]{propertyValuesHolder, propertyValuesHolder2, propertyValuesHolder3, propertyValuesHolder4});
        }
        return this;
    }
}

