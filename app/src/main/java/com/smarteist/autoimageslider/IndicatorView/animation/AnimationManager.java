/*
 * Decompiled with CFR 0.152.
 */
package com.smarteist.autoimageslider.IndicatorView.animation;

import com.smarteist.autoimageslider.IndicatorView.animation.controller.AnimationController;
import com.smarteist.autoimageslider.IndicatorView.animation.controller.ValueController;
import com.smarteist.autoimageslider.IndicatorView.draw.data.Indicator;

public class AnimationManager {
    private AnimationController animationController;

    public AnimationManager(Indicator indicator, ValueController.UpdateListener updateListener) {
        this.animationController = new AnimationController(indicator, updateListener);
    }

    public void basic() {
        AnimationController animationController = this.animationController;
        if (animationController != null) {
            animationController.end();
            this.animationController.basic();
        }
    }

    public void end() {
        AnimationController animationController = this.animationController;
        if (animationController != null) {
            animationController.end();
        }
    }

    public void interactive(float f) {
        AnimationController animationController = this.animationController;
        if (animationController != null) {
            animationController.interactive(f);
        }
    }
}

