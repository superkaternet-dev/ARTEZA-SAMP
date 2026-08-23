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

public class Circle
extends GraphicObject {
    private PointF center = new PointF();
    private float radius;

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(this.center.x, this.center.y, this.radius, this.paint);
    }

    public void setCenter(float f, float f2) {
        this.center.set(f, f2);
    }

    public void setRadius(float f) {
        this.radius = f;
    }
}

