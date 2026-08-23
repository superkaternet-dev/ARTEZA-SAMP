/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.ValueAnimator
 *  android.animation.ValueAnimator$AnimatorUpdateListener
 *  android.view.ViewGroup
 *  android.widget.TextView
 */
package com.google.android.material.internal;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.transition.Transition;
import androidx.transition.TransitionValues;

public class TextScale
extends Transition {
    private static final String PROPNAME_SCALE = "android:textscale:scale";

    private void captureValues(TransitionValues transitionValues) {
        if (transitionValues.view instanceof TextView) {
            TextView textView = (TextView)transitionValues.view;
            transitionValues.values.put(PROPNAME_SCALE, Float.valueOf(textView.getScaleX()));
        }
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        this.captureValues(transitionValues);
    }

    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        this.captureValues(transitionValues);
    }

    @Override
    public Animator createAnimator(ViewGroup viewGroup, TransitionValues object, TransitionValues object2) {
        if (object != null && object2 != null && ((TransitionValues)object).view instanceof TextView && ((TransitionValues)object2).view instanceof TextView) {
            viewGroup = (TextView)((TransitionValues)object2).view;
            object = ((TransitionValues)object).values;
            object2 = ((TransitionValues)object2).values;
            Object v = object.get(PROPNAME_SCALE);
            float f = 1.0f;
            float f2 = v != null ? ((Float)object.get(PROPNAME_SCALE)).floatValue() : 1.0f;
            if (object2.get(PROPNAME_SCALE) != null) {
                f = ((Float)object2.get(PROPNAME_SCALE)).floatValue();
            }
            if (f2 == f) {
                return null;
            }
            object = ValueAnimator.ofFloat((float[])new float[]{f2, f});
            object.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(this, (TextView)viewGroup){
                final TextScale this$0;
                final TextView val$view;
                {
                    this.this$0 = textScale;
                    this.val$view = textView;
                }

                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float f = ((Float)valueAnimator.getAnimatedValue()).floatValue();
                    this.val$view.setScaleX(f);
                    this.val$view.setScaleY(f);
                }
            });
            return object;
        }
        return null;
    }
}

