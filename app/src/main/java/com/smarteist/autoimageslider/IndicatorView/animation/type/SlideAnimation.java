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
import com.smarteist.autoimageslider.IndicatorView.animation.data.type.SlideAnimationValue;
import com.smarteist.autoimageslider.IndicatorView.animation.type.BaseAnimation;

public class SlideAnimation
extends BaseAnimation<ValueAnimator> {
    private static final String ANIMATION_COORDINATE = "ANIMATION_COORDINATE";
    private static final int COORDINATE_NONE = -1;
    private int coordinateEnd = -1;
    private int coordinateStart = -1;
    private SlideAnimationValue value = new SlideAnimationValue();

    public SlideAnimation(ValueController.UpdateListener updateListener) {
        super(updateListener);
    }

    private PropertyValuesHolder createSlidePropertyHolder() {
        PropertyValuesHolder propertyValuesHolder = PropertyValuesHolder.ofInt((String)ANIMATION_COORDINATE, (int[])new int[]{this.coordinateStart, this.coordinateEnd});
        propertyValuesHolder.setEvaluator((TypeEvaluator)new IntEvaluator());
        return propertyValuesHolder;
    }

    private boolean hasChanges(int n, int n2) {
        if (this.coordinateStart != n) {
            return true;
        }
        return this.coordinateEnd != n2;
    }

    private void onAnimateUpdated(ValueAnimator valueAnimator) {
        int n = (Integer)valueAnimator.getAnimatedValue(ANIMATION_COORDINATE);
        this.value.setCoordinate(n);
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
            final SlideAnimation this$0;
            {
                this.this$0 = slideAnimation;
            }

            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                this.this$0.onAnimateUpdated(valueAnimator);
            }
        });
        return valueAnimator;
    }

    @Override
    public SlideAnimation progress(float f) {
        if (this.animator != null) {
            long l = (long)((float)this.animationDuration * f);
            if (((ValueAnimator)this.animator).getValues() != null && ((ValueAnimator)this.animator).getValues().length > 0) {
                ((ValueAnimator)this.animator).setCurrentPlayTime(l);
            }
        }
        return this;
    }

    public SlideAnimation with(int n, int n2) {
        if (this.animator != null && this.hasChanges(n, n2)) {
            this.coordinateStart = n;
            this.coordinateEnd = n2;
            PropertyValuesHolder propertyValuesHolder = this.createSlidePropertyHolder();
            ((ValueAnimator)this.animator).setValues(new PropertyValuesHolder[]{propertyValuesHolder});
        }
        return this;
    }
}

