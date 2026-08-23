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
import com.smarteist.autoimageslider.IndicatorView.animation.data.type.DropAnimationValue;
import com.smarteist.autoimageslider.IndicatorView.animation.type.BaseAnimation;
import java.util.Iterator;

public class DropAnimation
extends BaseAnimation<AnimatorSet> {
    private int heightEnd;
    private int heightStart;
    private int radius;
    private DropAnimationValue value = new DropAnimationValue();
    private int widthEnd;
    private int widthStart;

    public DropAnimation(ValueController.UpdateListener updateListener) {
        super(updateListener);
    }

    private ValueAnimator createValueAnimation(int n, int n2, long l, AnimationType animationType) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt((int[])new int[]{n, n2});
        valueAnimator.setInterpolator((TimeInterpolator)new AccelerateDecelerateInterpolator());
        valueAnimator.setDuration(l);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(this, animationType){
            final DropAnimation this$0;
            final AnimationType val$type;
            {
                this.this$0 = dropAnimation;
                this.val$type = animationType;
            }

            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                this.this$0.onAnimatorUpdate(valueAnimator, this.val$type);
            }
        });
        return valueAnimator;
    }

    private boolean hasChanges(int n, int n2, int n3, int n4, int n5) {
        if (this.widthStart != n) {
            return true;
        }
        if (this.widthEnd != n2) {
            return true;
        }
        if (this.heightStart != n3) {
            return true;
        }
        if (this.heightEnd != n4) {
            return true;
        }
        return this.radius != n5;
    }

    private void onAnimatorUpdate(ValueAnimator valueAnimator, AnimationType animationType) {
        int n = (Integer)valueAnimator.getAnimatedValue();
        switch (2.$SwitchMap$com$smarteist$autoimageslider$IndicatorView$animation$type$DropAnimation$AnimationType[animationType.ordinal()]) {
            default: {
                break;
            }
            case 3: {
                this.value.setRadius(n);
                break;
            }
            case 2: {
                this.value.setHeight(n);
                break;
            }
            case 1: {
                this.value.setWidth(n);
            }
        }
        if (this.listener != null) {
            this.listener.onValueUpdated(this.value);
        }
    }

    @Override
    public AnimatorSet createAnimator() {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator((TimeInterpolator)new AccelerateDecelerateInterpolator());
        return animatorSet;
    }

    @Override
    public DropAnimation duration(long l) {
        super.duration(l);
        return this;
    }

    @Override
    public DropAnimation progress(float f) {
        if (this.animator != null) {
            long l = (long)((float)this.animationDuration * f);
            boolean bl = false;
            Iterator iterator2 = ((AnimatorSet)this.animator).getChildAnimations().iterator();
            while (iterator2.hasNext()) {
                long l2;
                ValueAnimator valueAnimator = (ValueAnimator)((Animator)iterator2.next());
                long l3 = valueAnimator.getDuration();
                long l4 = l2 = l;
                if (bl) {
                    l4 = l2 - l3;
                }
                if (l4 < 0L) continue;
                l2 = l4;
                if (l4 >= l3) {
                    l2 = l3;
                }
                if (valueAnimator.getValues() != null && valueAnimator.getValues().length > 0) {
                    valueAnimator.setCurrentPlayTime(l2);
                }
                boolean bl2 = bl;
                if (!bl) {
                    bl2 = bl;
                    if (l3 >= this.animationDuration) {
                        bl2 = true;
                    }
                }
                bl = bl2;
            }
        }
        return this;
    }

    public DropAnimation with(int n, int n2, int n3, int n4, int n5) {
        block0: {
            if (!this.hasChanges(n, n2, n3, n4, n5)) break block0;
            this.animator = this.createAnimator();
            this.widthStart = n;
            this.widthEnd = n2;
            this.heightStart = n3;
            this.heightEnd = n4;
            this.radius = n5;
            double d = n5;
            Double.isNaN(d);
            int n6 = (int)(d / 1.5);
            long l = this.animationDuration / 2L;
            ValueAnimator valueAnimator = this.createValueAnimation(n, n2, this.animationDuration, AnimationType.Width);
            ValueAnimator valueAnimator2 = this.createValueAnimation(n3, n4, l, AnimationType.Height);
            ValueAnimator valueAnimator3 = this.createValueAnimation(n5, n6, l, AnimationType.Radius);
            ValueAnimator valueAnimator4 = this.createValueAnimation(n4, n3, l, AnimationType.Height);
            ValueAnimator valueAnimator5 = this.createValueAnimation(n6, n5, l, AnimationType.Radius);
            ((AnimatorSet)this.animator).play((Animator)valueAnimator2).with((Animator)valueAnimator3).with((Animator)valueAnimator).before((Animator)valueAnimator4).before((Animator)valueAnimator5);
        }
        return this;
    }

    private static final class AnimationType
    extends Enum<AnimationType> {
        private static final AnimationType[] $VALUES;
        public static final /* enum */ AnimationType Height;
        public static final /* enum */ AnimationType Radius;
        public static final /* enum */ AnimationType Width;

        static {
            AnimationType animationType;
            AnimationType animationType2;
            AnimationType animationType3;
            Width = animationType3 = new AnimationType();
            Height = animationType2 = new AnimationType();
            Radius = animationType = new AnimationType();
            $VALUES = new AnimationType[]{animationType3, animationType2, animationType};
        }

        public static AnimationType valueOf(String string2) {
            return Enum.valueOf(AnimationType.class, string2);
        }

        public static AnimationType[] values() {
            return (AnimationType[])$VALUES.clone();
        }
    }
}

