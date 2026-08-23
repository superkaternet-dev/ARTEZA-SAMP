/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.AnimatorSet
 *  android.animation.TimeInterpolator
 *  android.animation.ValueAnimator
 *  android.animation.ValueAnimator$AnimatorUpdateListener
 *  android.view.animation.AccelerateDecelerateInterpolator
 */
package com.smarteist.autoimageslider.IndicatorView.animation.type;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import com.smarteist.autoimageslider.IndicatorView.animation.controller.ValueController;
import com.smarteist.autoimageslider.IndicatorView.animation.data.type.ThinWormAnimationValue;
import com.smarteist.autoimageslider.IndicatorView.animation.type.WormAnimation;

public class ThinWormAnimation
extends WormAnimation {
    private ThinWormAnimationValue value = new ThinWormAnimationValue();

    public ThinWormAnimation(ValueController.UpdateListener updateListener) {
        super(updateListener);
    }

    private ValueAnimator createHeightAnimator(int n, int n2, long l) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt((int[])new int[]{n, n2});
        valueAnimator.setInterpolator((TimeInterpolator)new AccelerateDecelerateInterpolator());
        valueAnimator.setDuration(l);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(this){
            final ThinWormAnimation this$0;
            {
                this.this$0 = thinWormAnimation;
            }

            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                this.this$0.onAnimateUpdated(valueAnimator);
            }
        });
        return valueAnimator;
    }

    private void onAnimateUpdated(ValueAnimator valueAnimator) {
        this.value.setHeight((Integer)valueAnimator.getAnimatedValue());
        if (this.listener != null) {
            this.listener.onValueUpdated(this.value);
        }
    }

    @Override
    public ThinWormAnimation duration(long l) {
        super.duration(l);
        return this;
    }

    @Override
    public ThinWormAnimation progress(float f) {
        if (this.animator != null) {
            long l = (long)((float)this.animationDuration * f);
            int n = ((AnimatorSet)this.animator).getChildAnimations().size();
            for (int i = 0; i < n; ++i) {
                long l2;
                ValueAnimator valueAnimator = (ValueAnimator)((AnimatorSet)this.animator).getChildAnimations().get(i);
                long l3 = l - valueAnimator.getStartDelay();
                if (l3 <= (l2 = valueAnimator.getDuration())) {
                    l2 = l3;
                    if (l3 < 0L) {
                        l2 = 0L;
                    }
                }
                if (i == n - 1 && l2 <= 0L || valueAnimator.getValues() == null || valueAnimator.getValues().length <= 0) continue;
                valueAnimator.setCurrentPlayTime(l2);
            }
        }
        return this;
    }

    @Override
    public WormAnimation with(int n, int n2, int n3, boolean bl) {
        if (this.hasChanges(n, n2, n3, bl)) {
            this.animator = this.createAnimator();
            this.coordinateStart = n;
            this.coordinateEnd = n2;
            this.radius = n3;
            this.isRightSide = bl;
            n2 = n3 * 2;
            this.rectLeftEdge = n - n3;
            this.rectRightEdge = n + n3;
            this.value.setRectStart(this.rectLeftEdge);
            this.value.setRectEnd(this.rectRightEdge);
            this.value.setHeight(n2);
            WormAnimation.RectValues rectValues = this.createRectValues(bl);
            double d = this.animationDuration;
            Double.isNaN(d);
            long l = (long)(d * 0.8);
            d = this.animationDuration;
            Double.isNaN(d);
            long l2 = (long)(d * 0.2);
            d = this.animationDuration;
            Double.isNaN(d);
            long l3 = (long)(d * 0.5);
            d = this.animationDuration;
            Double.isNaN(d);
            long l4 = (long)(d * 0.5);
            ValueAnimator valueAnimator = this.createWormAnimator(rectValues.fromX, rectValues.toX, l, false, this.value);
            ValueAnimator valueAnimator2 = this.createWormAnimator(rectValues.reverseFromX, rectValues.reverseToX, l, true, this.value);
            valueAnimator2.setStartDelay(l2);
            ValueAnimator valueAnimator3 = this.createHeightAnimator(n2, n3, l3);
            rectValues = this.createHeightAnimator(n3, n2, l3);
            rectValues.setStartDelay(l4);
            ((AnimatorSet)this.animator).playTogether(new Animator[]{valueAnimator, valueAnimator2, valueAnimator3, rectValues});
        }
        return this;
    }
}

