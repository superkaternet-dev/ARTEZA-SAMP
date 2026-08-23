/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.ArgbEvaluator
 *  android.animation.PropertyValuesHolder
 *  android.animation.TimeInterpolator
 *  android.animation.TypeEvaluator
 *  android.animation.ValueAnimator
 *  android.animation.ValueAnimator$AnimatorUpdateListener
 *  android.view.animation.AccelerateDecelerateInterpolator
 */
package com.smarteist.autoimageslider.IndicatorView.animation.type;

import android.animation.ArgbEvaluator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import com.smarteist.autoimageslider.IndicatorView.animation.controller.ValueController;
import com.smarteist.autoimageslider.IndicatorView.animation.data.type.ColorAnimationValue;
import com.smarteist.autoimageslider.IndicatorView.animation.type.BaseAnimation;

public class ColorAnimation
extends BaseAnimation<ValueAnimator> {
    static final String ANIMATION_COLOR = "ANIMATION_COLOR";
    static final String ANIMATION_COLOR_REVERSE = "ANIMATION_COLOR_REVERSE";
    public static final String DEFAULT_SELECTED_COLOR = "#ffffff";
    public static final String DEFAULT_UNSELECTED_COLOR = "#33ffffff";
    int colorEnd;
    int colorStart;
    private ColorAnimationValue value = new ColorAnimationValue();

    public ColorAnimation(ValueController.UpdateListener updateListener) {
        super(updateListener);
    }

    private boolean hasChanges(int n, int n2) {
        if (this.colorStart != n) {
            return true;
        }
        return this.colorEnd != n2;
    }

    private void onAnimateUpdated(ValueAnimator valueAnimator) {
        int n = (Integer)valueAnimator.getAnimatedValue(ANIMATION_COLOR);
        int n2 = (Integer)valueAnimator.getAnimatedValue(ANIMATION_COLOR_REVERSE);
        this.value.setColor(n);
        this.value.setColorReverse(n2);
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
            final ColorAnimation this$0;
            {
                this.this$0 = colorAnimation;
            }

            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                this.this$0.onAnimateUpdated(valueAnimator);
            }
        });
        return valueAnimator;
    }

    PropertyValuesHolder createColorPropertyHolder(boolean bl) {
        int n;
        int n2;
        String string2;
        if (bl) {
            string2 = ANIMATION_COLOR_REVERSE;
            n2 = this.colorEnd;
            n = this.colorStart;
        } else {
            string2 = ANIMATION_COLOR;
            n2 = this.colorStart;
            n = this.colorEnd;
        }
        string2 = PropertyValuesHolder.ofInt((String)string2, (int[])new int[]{n2, n});
        string2.setEvaluator((TypeEvaluator)new ArgbEvaluator());
        return string2;
    }

    @Override
    public ColorAnimation progress(float f) {
        if (this.animator != null) {
            long l = (long)((float)this.animationDuration * f);
            if (((ValueAnimator)this.animator).getValues() != null && ((ValueAnimator)this.animator).getValues().length > 0) {
                ((ValueAnimator)this.animator).setCurrentPlayTime(l);
            }
        }
        return this;
    }

    public ColorAnimation with(int n, int n2) {
        if (this.animator != null && this.hasChanges(n, n2)) {
            this.colorStart = n;
            this.colorEnd = n2;
            PropertyValuesHolder propertyValuesHolder = this.createColorPropertyHolder(false);
            PropertyValuesHolder propertyValuesHolder2 = this.createColorPropertyHolder(true);
            ((ValueAnimator)this.animator).setValues(new PropertyValuesHolder[]{propertyValuesHolder, propertyValuesHolder2});
        }
        return this;
    }
}

