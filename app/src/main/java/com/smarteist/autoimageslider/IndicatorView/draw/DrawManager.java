/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Canvas
 *  android.util.AttributeSet
 *  android.util.Pair
 *  android.view.MotionEvent
 */
package com.smarteist.autoimageslider.IndicatorView.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import com.smarteist.autoimageslider.IndicatorView.animation.data.Value;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.AttributeController;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.MeasureController;
import com.smarteist.autoimageslider.IndicatorView.draw.data.Indicator;

public class DrawManager {
    private AttributeController attributeController;
    private DrawController drawController;
    private Indicator indicator;
    private MeasureController measureController;

    public DrawManager() {
        Indicator indicator;
        this.indicator = indicator = new Indicator();
        this.drawController = new DrawController(indicator);
        this.measureController = new MeasureController();
        this.attributeController = new AttributeController(this.indicator);
    }

    public void draw(Canvas canvas) {
        this.drawController.draw(canvas);
    }

    public Indicator indicator() {
        if (this.indicator == null) {
            this.indicator = new Indicator();
        }
        return this.indicator;
    }

    public void initAttributes(Context context, AttributeSet attributeSet) {
        this.attributeController.init(context, attributeSet);
    }

    public Pair<Integer, Integer> measureViewSize(int n, int n2) {
        return this.measureController.measureViewSize(this.indicator, n, n2);
    }

    public void setClickListener(DrawController.ClickListener clickListener) {
        this.drawController.setClickListener(clickListener);
    }

    public void touch(MotionEvent motionEvent) {
        this.drawController.touch(motionEvent);
    }

    public void updateValue(Value value) {
        this.drawController.updateValue(value);
    }
}

