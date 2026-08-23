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
import com.smarteist.autoimageslider.IndicatorView.animation.data.type.FillAnimationValue;
import com.smarteist.autoimageslider.IndicatorView.animation.type.ColorAnimation;

public class FillAnimation
extends ColorAnimation {
    private static final String ANIMATION_RADIUS = "ANIMATION_RADIUS";
    private static final String ANIMATION_RADIUS_REVERSE = "ANIMATION_RADIUS_REVERSE";
    private static final String ANIMATION_STROKE = "ANIMATION_STROKE";
    private static final String ANIMATION_STROKE_REVERSE = "ANIMATION_STROKE_REVERSE";
    public static final int DEFAULT_STROKE_DP = 1;
    private int radius;
    private int stroke;
    private FillAnimationValue value = new FillAnimationValue();

    public FillAnimation(ValueController.UpdateListener updateListener) {
        super(updateListener);
    }

    private PropertyValuesHolder createRadiusPropertyHolder(boolean bl) {
        int n;
        int n2;
        String string2;
        if (bl) {
            string2 = ANIMATION_RADIUS_REVERSE;
            n2 = this.radius / 2;
            n = this.radius;
        } else {
            string2 = ANIMATION_RADIUS;
            n2 = this.radius;
            n = this.radius / 2;
        }
        string2 = PropertyValuesHolder.ofInt((String)string2, (int[])new int[]{n2, n});
        string2.setEvaluator((TypeEvaluator)new IntEvaluator());
        return string2;
    }

    private PropertyValuesHolder createStrokePropertyHolder(boolean bl) {
        int n;
        int n2;
        String string2;
        if (bl) {
            string2 = ANIMATION_STROKE_REVERSE;
            n2 = this.radius;
            n = 0;
        } else {
            string2 = ANIMATION_STROKE;
            n2 = 0;
            n = this.radius;
        }
        string2 = PropertyValuesHolder.ofInt((String)string2, (int[])new int[]{n2, n});
        string2.setEvaluator((TypeEvaluator)new IntEvaluator());
        return string2;
    }

    private boolean hasChanges(int n, int n2, int n3, int n4) {
        if (this.colorStart != n) {
            return true;
        }
        if (this.colorEnd != n2) {
            return true;
        }
        if (this.radius != n3) {
            return true;
        }
        return this.stroke != n4;
    }

    private void onAnimateUpdated(ValueAnimator valueAnimator) {
        int n = (Integer)valueAnimator.getAnimatedValue("ANIMATION_COLOR");
        int n2 = (Integer)valueAnimator.getAnimatedValue("ANIMATION_COLOR_REVERSE");
        int n3 = (Integer)valueAnimator.getAnimatedValue(ANIMATION_RADIUS);
        int n4 = (Integer)valueAnimator.getAnimatedValue(ANIMATION_RADIUS_REVERSE);
        int n5 = (Integer)valueAnimator.getAnimatedValue(ANIMATION_STROKE);
        int n6 = (Integer)valueAnimator.getAnimatedValue(ANIMATION_STROKE_REVERSE);
        this.value.setColor(n);
        this.value.setColorReverse(n2);
        this.value.setRadius(n3);
        this.value.setRadiusReverse(n4);
        this.value.setStroke(n5);
        this.value.setStrokeReverse(n6);
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
            final FillAnimation this$0;
            {
                this.this$0 = fillAnimation;
            }

            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                this.this$0.onAnimateUpdated(valueAnimator);
            }
        });
        return valueAnimator;
    }

    public FillAnimation with(int n, int n2, int n3, int n4) {
        if (this.animator != null && this.hasChanges(n, n2, n3, n4)) {
            this.colorStart = n;
            this.colorEnd = n2;
            this.radius = n3;
            this.stroke = n4;
            PropertyValuesHolder propertyValuesHolder = this.createColorPropertyHolder(false);
            PropertyValuesHolder propertyValuesHolder2 = this.createColorPropertyHolder(true);
            PropertyValuesHolder propertyValuesHolder3 = this.createRadiusPropertyHolder(false);
            PropertyValuesHolder propertyValuesHolder4 = this.createRadiusPropertyHolder(true);
            PropertyValuesHolder propertyValuesHolder5 = this.createStrokePropertyHolder(false);
            PropertyValuesHolder propertyValuesHolder6 = this.createStrokePropertyHolder(true);
            ((ValueAnimator)this.animator).setValues(new PropertyValuesHolder[]{propertyValuesHolder, propertyValuesHolder2, propertyValuesHolder3, propertyValuesHolder4, propertyValuesHolder5, propertyValuesHolder6});
        }
        return this;
    }
}

