/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.AnimatorSet
 *  android.animation.ValueAnimator
 */
package com.google.android.material.animation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import java.util.List;

public class AnimatorSetCompat {
    public static void playTogether(AnimatorSet animatorSet, List<Animator> list) {
        Animator animator2;
        long l = 0L;
        int n = list.size();
        for (int i = 0; i < n; ++i) {
            animator2 = list.get(i);
            l = Math.max(l, animator2.getStartDelay() + animator2.getDuration());
        }
        animator2 = ValueAnimator.ofInt((int[])new int[]{0, 0});
        animator2.setDuration(l);
        list.add(0, animator2);
        animatorSet.playTogether(list);
    }
}

