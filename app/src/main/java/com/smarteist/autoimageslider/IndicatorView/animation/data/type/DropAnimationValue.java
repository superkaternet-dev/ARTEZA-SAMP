/*
 * Decompiled with CFR 0.152.
 */
package com.smarteist.autoimageslider.IndicatorView.animation.data.type;

import com.smarteist.autoimageslider.IndicatorView.animation.data.Value;

public class DropAnimationValue
implements Value {
    private int height;
    private int radius;
    private int width;

    public int getHeight() {
        return this.height;
    }

    public int getRadius() {
        return this.radius;
    }

    public int getWidth() {
        return this.width;
    }

    public void setHeight(int n) {
        this.height = n;
    }

    public void setRadius(int n) {
        this.radius = n;
    }

    public void setWidth(int n) {
        this.width = n;
    }
}

