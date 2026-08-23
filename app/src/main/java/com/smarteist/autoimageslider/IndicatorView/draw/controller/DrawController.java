/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Canvas
 *  android.view.MotionEvent
 */
package com.smarteist.autoimageslider.IndicatorView.draw.controller;

import android.graphics.Canvas;
import android.view.MotionEvent;
import com.smarteist.autoimageslider.IndicatorView.animation.data.Value;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.IndicatorView.draw.data.Indicator;
import com.smarteist.autoimageslider.IndicatorView.draw.drawer.Drawer;
import com.smarteist.autoimageslider.IndicatorView.utils.CoordinatesUtils;

public class DrawController {
    private Drawer drawer;
    private Indicator indicator;
    private ClickListener listener;
    private Value value;

    public DrawController(Indicator indicator) {
        this.indicator = indicator;
        this.drawer = new Drawer(indicator);
    }

    private void drawIndicator(Canvas canvas, int n, int n2, int n3) {
        boolean bl = this.indicator.isInteractiveAnimation();
        int n4 = this.indicator.getSelectedPosition();
        int n5 = this.indicator.getSelectingPosition();
        int n6 = this.indicator.getLastSelectedPosition();
        int n7 = 1;
        n6 = !bl && (n == n4 || n == n6) ? 1 : 0;
        if (!bl || n != n4 && n != n5) {
            n7 = 0;
        }
        boolean bl2 = n6 | n7;
        this.drawer.setup(n, n2, n3);
        if (this.value != null && bl2) {
            this.drawWithAnimation(canvas);
        } else {
            this.drawer.drawBasic(canvas, bl2);
        }
    }

    private void drawWithAnimation(Canvas canvas) {
        IndicatorAnimationType indicatorAnimationType = this.indicator.getAnimationType();
        switch (1.$SwitchMap$com$smarteist$autoimageslider$IndicatorView$animation$type$IndicatorAnimationType[indicatorAnimationType.ordinal()]) {
            default: {
                break;
            }
            case 10: {
                this.drawer.drawScaleDown(canvas, this.value);
                break;
            }
            case 9: {
                this.drawer.drawSwap(canvas, this.value);
                break;
            }
            case 8: {
                this.drawer.drawDrop(canvas, this.value);
                break;
            }
            case 7: {
                this.drawer.drawThinWorm(canvas, this.value);
                break;
            }
            case 6: {
                this.drawer.drawFill(canvas, this.value);
                break;
            }
            case 5: {
                this.drawer.drawSlide(canvas, this.value);
                break;
            }
            case 4: {
                this.drawer.drawWorm(canvas, this.value);
                break;
            }
            case 3: {
                this.drawer.drawScale(canvas, this.value);
                break;
            }
            case 2: {
                this.drawer.drawColor(canvas, this.value);
                break;
            }
            case 1: {
                this.drawer.drawBasic(canvas, true);
            }
        }
    }

    private void onIndicatorTouched(float f, float f2) {
        int n;
        if (this.listener != null && (n = CoordinatesUtils.getPosition(this.indicator, f, f2)) >= 0) {
            this.listener.onIndicatorClicked(n);
        }
    }

    public void draw(Canvas canvas) {
        int n = this.indicator.getCount();
        for (int i = 0; i < n; ++i) {
            this.drawIndicator(canvas, i, CoordinatesUtils.getXCoordinate(this.indicator, i), CoordinatesUtils.getYCoordinate(this.indicator, i));
        }
    }

    public void setClickListener(ClickListener clickListener) {
        this.listener = clickListener;
    }

    public void touch(MotionEvent motionEvent) {
        if (motionEvent == null) {
            return;
        }
        switch (motionEvent.getAction()) {
            default: {
                break;
            }
            case 1: {
                this.onIndicatorTouched(motionEvent.getX(), motionEvent.getY());
            }
        }
    }

    public void updateValue(Value value) {
        this.value = value;
    }

    public static interface ClickListener {
        public void onIndicatorClicked(int var1);
    }
}

