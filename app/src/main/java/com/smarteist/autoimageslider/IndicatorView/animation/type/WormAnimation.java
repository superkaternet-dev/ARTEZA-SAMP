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
import com.smarteist.autoimageslider.IndicatorView.animation.data.type.WormAnimationValue;
import com.smarteist.autoimageslider.IndicatorView.animation.type.BaseAnimation;
import java.util.Iterator;

public class WormAnimation
extends BaseAnimation<AnimatorSet> {
    int coordinateEnd;
    int coordinateStart;
    boolean isRightSide;
    int radius;
    int rectLeftEdge;
    int rectRightEdge;
    private WormAnimationValue value = new WormAnimationValue();

    public WormAnimation(ValueController.UpdateListener updateListener) {
        super(updateListener);
    }

    private void onAnimateUpdated(WormAnimationValue wormAnimationValue, ValueAnimator valueAnimator, boolean bl) {
        int n = (Integer)valueAnimator.getAnimatedValue();
        if (this.isRightSide) {
            if (!bl) {
                wormAnimationValue.setRectEnd(n);
            } else {
                wormAnimationValue.setRectStart(n);
            }
        } else if (!bl) {
            wormAnimationValue.setRectStart(n);
        } else {
            wormAnimationValue.setRectEnd(n);
        }
        if (this.listener != null) {
            this.listener.onValueUpdated(wormAnimationValue);
        }
    }

    @Override
    public AnimatorSet createAnimator() {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator((TimeInterpolator)new AccelerateDecelerateInterpolator());
        return animatorSet;
    }

    RectValues createRectValues(boolean bl) {
        int n;
        int n2;
        int n3;
        int n4;
        if (bl) {
            n4 = this.coordinateStart;
            int n5 = this.radius;
            n3 = n4 + n5;
            n2 = this.coordinateEnd;
            n = n2 + n5;
            n4 -= n5;
            n2 -= n5;
        } else {
            n4 = this.coordinateStart;
            n2 = this.radius;
            n3 = n4 - n2;
            int n6 = this.coordinateEnd;
            n = n6 - n2;
            n4 += n2;
            n2 = n6 + n2;
        }
        return new RectValues(this, n3, n, n4, n2);
    }

    ValueAnimator createWormAnimator(int n, int n2, long l, boolean bl, WormAnimationValue wormAnimationValue) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt((int[])new int[]{n, n2});
        valueAnimator.setInterpolator((TimeInterpolator)new AccelerateDecelerateInterpolator());
        valueAnimator.setDuration(l);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(this, wormAnimationValue, bl){
            final WormAnimation this$0;
            final boolean val$isReverse;
            final WormAnimationValue val$value;
            {
                this.this$0 = wormAnimation;
                this.val$value = wormAnimationValue;
                this.val$isReverse = bl;
            }

            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                this.this$0.onAnimateUpdated(this.val$value, valueAnimator, this.val$isReverse);
            }
        });
        return valueAnimator;
    }

    @Override
    public WormAnimation duration(long l) {
        super.duration(l);
        return this;
    }

    boolean hasChanges(int n, int n2, int n3, boolean bl) {
        if (this.coordinateStart != n) {
            return true;
        }
        if (this.coordinateEnd != n2) {
            return true;
        }
        if (this.radius != n3) {
            return true;
        }
        return this.isRightSide != bl;
    }

    @Override
    public WormAnimation progress(float f) {
        if (this.animator == null) {
            return this;
        }
        long l = (long)((float)this.animationDuration * f);
        Iterator iterator2 = ((AnimatorSet)this.animator).getChildAnimations().iterator();
        while (iterator2.hasNext()) {
            long l2;
            ValueAnimator valueAnimator = (ValueAnimator)((Animator)iterator2.next());
            long l3 = valueAnimator.getDuration();
            long l4 = l2 = l;
            if (l2 > l3) {
                l4 = l3;
            }
            valueAnimator.setCurrentPlayTime(l4);
            l -= l4;
        }
        return this;
    }

    public WormAnimation with(int n, int n2, int n3, boolean bl) {
        block0: {
            if (!this.hasChanges(n, n2, n3, bl)) break block0;
            this.animator = this.createAnimator();
            this.coordinateStart = n;
            this.coordinateEnd = n2;
            this.radius = n3;
            this.isRightSide = bl;
            this.rectLeftEdge = n2 = n - n3;
            this.rectRightEdge = n + n3;
            this.value.setRectStart(n2);
            this.value.setRectEnd(this.rectRightEdge);
            RectValues rectValues = this.createRectValues(bl);
            long l = this.animationDuration / 2L;
            ValueAnimator valueAnimator = this.createWormAnimator(rectValues.fromX, rectValues.toX, l, false, this.value);
            rectValues = this.createWormAnimator(rectValues.reverseFromX, rectValues.reverseToX, l, true, this.value);
            ((AnimatorSet)this.animator).playSequentially(new Animator[]{valueAnimator, rectValues});
        }
        return this;
    }

    class RectValues {
        final int fromX;
        final int reverseFromX;
        final int reverseToX;
        final WormAnimation this$0;
        final int toX;

        RectValues(WormAnimation wormAnimation, int n, int n2, int n3, int n4) {
            this.this$0 = wormAnimation;
            this.fromX = n;
            this.toX = n2;
            this.reverseFromX = n3;
            this.reverseToX = n4;
        }
    }
}

