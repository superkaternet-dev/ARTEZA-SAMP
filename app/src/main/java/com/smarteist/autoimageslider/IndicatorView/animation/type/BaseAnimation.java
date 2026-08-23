/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.ValueAnimator
 */
package com.smarteist.autoimageslider.IndicatorView.animation.type;

import android.animation.Animator;
import android.animation.ValueAnimator;
import com.smarteist.autoimageslider.IndicatorView.animation.controller.ValueController;

public abstract class BaseAnimation<T extends Animator> {
    public static final int DEFAULT_ANIMATION_TIME = 350;
    protected long animationDuration = 350L;
    protected T animator;
    protected ValueController.UpdateListener listener;

    public BaseAnimation(ValueController.UpdateListener updateListener) {
        this.listener = updateListener;
        this.animator = this.createAnimator();
    }

    public abstract T createAnimator();

    public BaseAnimation duration(long l) {
        this.animationDuration = l;
        T t = this.animator;
        if (t instanceof ValueAnimator) {
            t.setDuration(l);
        }
        return this;
    }

    public void end() {
        T t = this.animator;
        if (t != null && t.isStarted()) {
            this.animator.end();
        }
    }

    public abstract BaseAnimation progress(float var1);

    public void start() {
        T t = this.animator;
        if (t != null && !t.isRunning()) {
            this.animator.start();
        }
    }
}

