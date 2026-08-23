/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Canvas
 *  android.graphics.RectF
 */
package com.tuyenmonkey.mkloader.model;

import android.graphics.Canvas;
import android.graphics.RectF;
import com.tuyenmonkey.mkloader.model.GraphicObject;

public class Arc
extends GraphicObject {
    private RectF oval;
    private float startAngle;
    private float sweepAngle;
    private boolean useCenter;

    @Override
    public void draw(Canvas canvas) {
        canvas.drawArc(this.oval, this.startAngle, this.sweepAngle, this.useCenter, this.paint);
    }

    public float getStartAngle() {
        return this.startAngle;
    }

    public void setOval(RectF rectF) {
        this.oval = rectF;
    }

    public void setStartAngle(float f) {
        this.startAngle = f;
    }

    public void setSweepAngle(float f) {
        this.sweepAngle = f;
    }

    public void setUseCenter(boolean bl) {
        this.useCenter = bl;
    }
}

