/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Canvas
 *  android.graphics.Paint
 *  android.graphics.Paint$Style
 */
package com.tuyenmonkey.mkloader.model;

import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class GraphicObject {
    protected Paint paint;

    public GraphicObject() {
        Paint paint;
        this.paint = paint = new Paint();
        paint.setAntiAlias(true);
    }

    public abstract void draw(Canvas var1);

    public void setAlpha(int n) {
        this.paint.setAlpha(n);
    }

    public void setColor(int n) {
        this.paint.setColor(n);
    }

    public void setStyle(Paint.Style style2) {
        this.paint.setStyle(style2);
    }

    public void setWidth(float f) {
        this.paint.setStrokeWidth(f);
    }
}

