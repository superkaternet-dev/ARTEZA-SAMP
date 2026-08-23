/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.view.View
 *  android.view.animation.Animation
 *  android.view.animation.AnimationUtils
 */
package com.skydoves.colorpickerview;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.skydoves.colorpickerview.R;

public class FadeUtils {
    public static void fadeIn(View view) {
        Animation animation = AnimationUtils.loadAnimation((Context)view.getContext(), (int)R.anim.fade_in);
        animation.setFillAfter(true);
        view.startAnimation(animation);
    }

    public static void fadeOut(View view) {
        Animation animation = AnimationUtils.loadAnimation((Context)view.getContext(), (int)R.anim.fade_out);
        animation.setFillAfter(true);
        view.startAnimation(animation);
    }
}

