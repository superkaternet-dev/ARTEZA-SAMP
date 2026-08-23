/*
 * Decompiled with CFR 0.152.
 */
package com.smarteist.autoimageslider.IndicatorView.animation.controller;

import com.smarteist.autoimageslider.IndicatorView.animation.controller.ValueController;
import com.smarteist.autoimageslider.IndicatorView.animation.type.BaseAnimation;
import com.smarteist.autoimageslider.IndicatorView.animation.type.DropAnimation;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.IndicatorView.draw.data.Indicator;
import com.smarteist.autoimageslider.IndicatorView.draw.data.Orientation;
import com.smarteist.autoimageslider.IndicatorView.utils.CoordinatesUtils;

public class AnimationController {
    private Indicator indicator;
    private boolean isInteractive;
    private ValueController.UpdateListener listener;
    private float progress;
    private BaseAnimation runningAnimation;
    private ValueController valueController;

    public AnimationController(Indicator indicator, ValueController.UpdateListener updateListener) {
        this.valueController = new ValueController(updateListener);
        this.listener = updateListener;
        this.indicator = indicator;
    }

    private void animate() {
        IndicatorAnimationType indicatorAnimationType = this.indicator.getAnimationType();
        switch (1.$SwitchMap$com$smarteist$autoimageslider$IndicatorView$animation$type$IndicatorAnimationType[indicatorAnimationType.ordinal()]) {
            default: {
                break;
            }
            case 10: {
                this.scaleDownAnimation();
                break;
            }
            case 9: {
                this.swapAnimation();
                break;
            }
            case 8: {
                this.dropAnimation();
                break;
            }
            case 7: {
                this.thinWormAnimation();
                break;
            }
            case 6: {
                this.slideAnimation();
                break;
            }
            case 5: {
                this.fillAnimation();
                break;
            }
            case 4: {
                this.wormAnimation();
                break;
            }
            case 3: {
                this.scaleAnimation();
                break;
            }
            case 2: {
                this.colorAnimation();
                break;
            }
            case 1: {
                this.listener.onValueUpdated(null);
            }
        }
    }

    private void colorAnimation() {
        int n = this.indicator.getSelectedColor();
        int n2 = this.indicator.getUnselectedColor();
        long l = this.indicator.getAnimationDuration();
        BaseAnimation baseAnimation = this.valueController.color().with(n2, n).duration(l);
        if (this.isInteractive) {
            baseAnimation.progress(this.progress);
        } else {
            baseAnimation.start();
        }
        this.runningAnimation = baseAnimation;
    }

    private void dropAnimation() {
        int n = this.indicator.isInteractiveAnimation() ? this.indicator.getSelectedPosition() : this.indicator.getLastSelectedPosition();
        int n2 = this.indicator.isInteractiveAnimation() ? this.indicator.getSelectingPosition() : this.indicator.getSelectedPosition();
        int n3 = CoordinatesUtils.getCoordinate(this.indicator, n);
        int n4 = CoordinatesUtils.getCoordinate(this.indicator, n2);
        n = this.indicator.getPaddingTop();
        n2 = this.indicator.getPaddingLeft();
        if (this.indicator.getOrientation() != Orientation.HORIZONTAL) {
            n = n2;
        }
        n2 = this.indicator.getRadius();
        long l = this.indicator.getAnimationDuration();
        DropAnimation dropAnimation = ((DropAnimation)this.valueController.drop().duration(l)).with(n3, n4, n2 * 3 + n, n2 + n, n2);
        if (this.isInteractive) {
            ((BaseAnimation)dropAnimation).progress(this.progress);
        } else {
            dropAnimation.start();
        }
        this.runningAnimation = dropAnimation;
    }

    private void fillAnimation() {
        int n = this.indicator.getSelectedColor();
        int n2 = this.indicator.getUnselectedColor();
        int n3 = this.indicator.getRadius();
        int n4 = this.indicator.getStroke();
        long l = this.indicator.getAnimationDuration();
        BaseAnimation baseAnimation = this.valueController.fill().with(n2, n, n3, n4).duration(l);
        if (this.isInteractive) {
            baseAnimation.progress(this.progress);
        } else {
            baseAnimation.start();
        }
        this.runningAnimation = baseAnimation;
    }

    private void scaleAnimation() {
        int n = this.indicator.getSelectedColor();
        int n2 = this.indicator.getUnselectedColor();
        int n3 = this.indicator.getRadius();
        float f = this.indicator.getScaleFactor();
        long l = this.indicator.getAnimationDuration();
        BaseAnimation baseAnimation = this.valueController.scale().with(n2, n, n3, f).duration(l);
        if (this.isInteractive) {
            baseAnimation.progress(this.progress);
        } else {
            baseAnimation.start();
        }
        this.runningAnimation = baseAnimation;
    }

    private void scaleDownAnimation() {
        int n = this.indicator.getSelectedColor();
        int n2 = this.indicator.getUnselectedColor();
        int n3 = this.indicator.getRadius();
        float f = this.indicator.getScaleFactor();
        long l = this.indicator.getAnimationDuration();
        BaseAnimation baseAnimation = this.valueController.scaleDown().with(n2, n, n3, f).duration(l);
        if (this.isInteractive) {
            baseAnimation.progress(this.progress);
        } else {
            baseAnimation.start();
        }
        this.runningAnimation = baseAnimation;
    }

    private void slideAnimation() {
        int n = this.indicator.isInteractiveAnimation() ? this.indicator.getSelectedPosition() : this.indicator.getLastSelectedPosition();
        int n2 = this.indicator.isInteractiveAnimation() ? this.indicator.getSelectingPosition() : this.indicator.getSelectedPosition();
        n = CoordinatesUtils.getCoordinate(this.indicator, n);
        n2 = CoordinatesUtils.getCoordinate(this.indicator, n2);
        long l = this.indicator.getAnimationDuration();
        BaseAnimation baseAnimation = this.valueController.slide().with(n, n2).duration(l);
        if (this.isInteractive) {
            baseAnimation.progress(this.progress);
        } else {
            baseAnimation.start();
        }
        this.runningAnimation = baseAnimation;
    }

    private void swapAnimation() {
        int n = this.indicator.isInteractiveAnimation() ? this.indicator.getSelectedPosition() : this.indicator.getLastSelectedPosition();
        int n2 = this.indicator.isInteractiveAnimation() ? this.indicator.getSelectingPosition() : this.indicator.getSelectedPosition();
        n = CoordinatesUtils.getCoordinate(this.indicator, n);
        n2 = CoordinatesUtils.getCoordinate(this.indicator, n2);
        long l = this.indicator.getAnimationDuration();
        BaseAnimation baseAnimation = this.valueController.swap().with(n, n2).duration(l);
        if (this.isInteractive) {
            baseAnimation.progress(this.progress);
        } else {
            baseAnimation.start();
        }
        this.runningAnimation = baseAnimation;
    }

    private void thinWormAnimation() {
        int n = this.indicator.isInteractiveAnimation() ? this.indicator.getSelectedPosition() : this.indicator.getLastSelectedPosition();
        int n2 = this.indicator.isInteractiveAnimation() ? this.indicator.getSelectingPosition() : this.indicator.getSelectedPosition();
        int n3 = CoordinatesUtils.getCoordinate(this.indicator, n);
        int n4 = CoordinatesUtils.getCoordinate(this.indicator, n2);
        boolean bl = n2 > n;
        n = this.indicator.getRadius();
        long l = this.indicator.getAnimationDuration();
        BaseAnimation baseAnimation = this.valueController.thinWorm().with(n3, n4, n, bl).duration(l);
        if (this.isInteractive) {
            baseAnimation.progress(this.progress);
        } else {
            baseAnimation.start();
        }
        this.runningAnimation = baseAnimation;
    }

    private void wormAnimation() {
        int n = this.indicator.isInteractiveAnimation() ? this.indicator.getSelectedPosition() : this.indicator.getLastSelectedPosition();
        int n2 = this.indicator.isInteractiveAnimation() ? this.indicator.getSelectingPosition() : this.indicator.getSelectedPosition();
        int n3 = CoordinatesUtils.getCoordinate(this.indicator, n);
        int n4 = CoordinatesUtils.getCoordinate(this.indicator, n2);
        boolean bl = n2 > n;
        n = this.indicator.getRadius();
        long l = this.indicator.getAnimationDuration();
        BaseAnimation baseAnimation = this.valueController.worm().with(n3, n4, n, bl).duration(l);
        if (this.isInteractive) {
            baseAnimation.progress(this.progress);
        } else {
            baseAnimation.start();
        }
        this.runningAnimation = baseAnimation;
    }

    public void basic() {
        this.isInteractive = false;
        this.progress = 0.0f;
        this.animate();
    }

    public void end() {
        BaseAnimation baseAnimation = this.runningAnimation;
        if (baseAnimation != null) {
            baseAnimation.end();
        }
    }

    public void interactive(float f) {
        this.isInteractive = true;
        this.progress = f;
        this.animate();
    }
}

