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
import com.smarteist.autoimageslider.IndicatorView.animation.data.type.SwapAnimationValue;
import com.smarteist.autoimageslider.IndicatorView.animation.type.BaseAnimation;

public class SwapAnimation
extends BaseAnimation<ValueAnimator> {
    private static final String ANIMATION_COORDINATE = "ANIMATION_COORDINATE";
    private static final String ANIMATION_COORDINATE_REVERSE = "ANIMATION_COORDINATE_REVERSE";
    private static final int COORDINATE_NONE = -1;
    private int coordinateEnd = -1;
    private int coordinateStart = -1;
    private SwapAnimationValue value = new SwapAnimationValue();

    public SwapAnimation(ValueController.UpdateListener updateListener) {
        super(updateListener);
    }

    private PropertyValuesHolder createColorPropertyHolder(String string2, int n, int n2) {
        string2 = PropertyValuesHolder.ofInt((String)string2, (int[])new int[]{n, n2});
        string2.setEvaluator((TypeEvaluator)new IntEvaluator());
        return string2;
    }

    private boolean hasChanges(int n, int n2) {
        if (this.coordinateStart != n) {
            return true;
        }
        return this.coordinateEnd != n2;
    }

    private void onAnimateUpdated(ValueAnimator valueAnimator) {
        int n = (Integer)valueAnimator.getAnimatedValue(ANIMATION_COORDINATE);
        int n2 = (Integer)valueAnimator.getAnimatedValue(ANIMATION_COORDINATE_REVERSE);
        this.value.setCoordinate(n);
        this.value.setCoordinateReverse(n2);
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
            final SwapAnimation this$0;
            {
                this.this$0 = swapAnimation;
            }

            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                this.this$0.onAnimateUpdated(valueAnimator);
            }
        });
        return valueAnimator;
    }

    @Override
    public SwapAnimation progress(float f) {
        if (this.animator != null) {
            long l = (long)((float)this.animationDuration * f);
            if (((ValueAnimator)this.animator).getValues() != null && ((ValueAnimator)this.animator).getValues().length > 0) {
                ((ValueAnimator)this.animator).setCurrentPlayTime(l);
            }
        }
        return this;
    }

    public SwapAnimation with(int n, int n2) {
        if (this.animator != null && this.hasChanges(n, n2)) {
            this.coordinateStart = n;
            this.coordinateEnd = n2;
            PropertyValuesHolder propertyValuesHolder = this.createColorPropertyHolder(ANIMATION_COORDINATE, n, n2);
            PropertyValuesHolder propertyValuesHolder2 = this.createColorPropertyHolder(ANIMATION_COORDINATE_REVERSE, n2, n);
            ((ValueAnimator)this.animator).setValues(new PropertyValuesHolder[]{propertyValuesHolder, propertyValuesHolder2});
        }
        return this;
    }
}

