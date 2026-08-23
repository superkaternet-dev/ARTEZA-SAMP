/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Canvas
 *  android.graphics.PointF
 */
package com.tuyenmonkey.mkloader.model;

import android.graphics.Canvas;
import android.graphics.PointF;
import com.tuyenmonkey.mkloader.model.GraphicObject;

public class Line
extends GraphicObject {
    private PointF point1;
    private PointF point2;

    @Override
    public void draw(Canvas canvas) {
        canvas.drawLine(this.point1.x, this.point1.y, this.point2.x, this.point2.y, this.paint);
    }

    public PointF getPoint1() {
        return this.point1;
    }

    public PointF getPoint2() {
        return this.point2;
    }

    public void setPoint1(PointF pointF) {
        this.point1 = pointF;
    }

    public void setPoint2(PointF pointF) {
        this.point2 = pointF;
    }
}

