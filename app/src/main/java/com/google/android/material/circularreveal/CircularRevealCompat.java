/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.Animator$AnimatorListener
 *  android.animation.AnimatorListenerAdapter
 *  android.animation.AnimatorSet
 *  android.animation.ObjectAnimator
 *  android.os.Build$VERSION
 *  android.view.View
 *  android.view.ViewAnimationUtils
 */
package com.google.android.material.circularreveal;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Build;
import android.view.View;
import android.view.ViewAnimationUtils;
import com.google.android.material.circularreveal.CircularRevealWidget;

public final class CircularRevealCompat {
    private CircularRevealCompat() {
    }

    public static Animator createCircularReveal(CircularRevealWidget circularRevealWidget, float f, float f2, float f3) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofObject((Object)circularRevealWidget, CircularRevealWidget.CircularRevealProperty.CIRCULAR_REVEAL, CircularRevealWidget.CircularRevealEvaluator.CIRCULAR_REVEAL, (Object[])new CircularRevealWidget.RevealInfo[]{new CircularRevealWidget.RevealInfo(f, f2, f3)});
        if (Build.VERSION.SDK_INT >= 21) {
            CircularRevealWidget.RevealInfo revealInfo = circularRevealWidget.getRevealInfo();
            if (revealInfo != null) {
                float f4 = revealInfo.radius;
                revealInfo = ViewAnimationUtils.createCircularReveal((View)((View)circularRevealWidget), (int)((int)f), (int)((int)f2), (float)f4, (float)f3);
                circularRevealWidget = new AnimatorSet();
                circularRevealWidget.playTogether(new Animator[]{objectAnimator, revealInfo});
                return circularRevealWidget;
            }
            throw new IllegalStateException("Caller must set a non-null RevealInfo before calling this.");
        }
        return objectAnimator;
    }

    public static Animator createCircularReveal(CircularRevealWidget circularRevealWidget, float f, float f2, float f3, float f4) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofObject((Object)circularRevealWidget, CircularRevealWidget.CircularRevealProperty.CIRCULAR_REVEAL, CircularRevealWidget.CircularRevealEvaluator.CIRCULAR_REVEAL, (Object[])new CircularRevealWidget.RevealInfo[]{new CircularRevealWidget.RevealInfo(f, f2, f3), new CircularRevealWidget.RevealInfo(f, f2, f4)});
        if (Build.VERSION.SDK_INT >= 21) {
            circularRevealWidget = ViewAnimationUtils.createCircularReveal((View)((View)circularRevealWidget), (int)((int)f), (int)((int)f2), (float)f3, (float)f4);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(new Animator[]{objectAnimator, circularRevealWidget});
            return animatorSet;
        }
        return objectAnimator;
    }

    public static Animator.AnimatorListener createCircularRevealListener(CircularRevealWidget circularRevealWidget) {
        return new AnimatorListenerAdapter(circularRevealWidget){
            final CircularRevealWidget val$view;
            {
                this.val$view = circularRevealWidget;
            }

            public void onAnimationEnd(Animator animator2) {
                this.val$view.destroyCircularRevealCache();
            }

            public void onAnimationStart(Animator animator2) {
                this.val$view.buildCircularRevealCache();
            }
        };
    }
}

