/*
 * Decompiled with CFR 0.152.
 */
package com.smarteist.autoimageslider.IndicatorView.animation.data.type;

import com.smarteist.autoimageslider.IndicatorView.animation.data.Value;
import com.smarteist.autoimageslider.IndicatorView.animation.data.type.ColorAnimationValue;

public class FillAnimationValue
extends ColorAnimationValue
implements Value {
    private int radius;
    private int radiusReverse;
    private int stroke;
    private int strokeReverse;

    public int getRadius() {
        return this.radius;
    }

    public int getRadiusReverse() {
        return this.radiusReverse;
    }

    public int getStroke() {
        return this.stroke;
    }

    public int getStrokeReverse() {
        return this.strokeReverse;
    }

    public void setRadius(int n) {
        this.radius = n;
    }

    public void setRadiusReverse(int n) {
        this.radiusReverse = n;
    }

    public void setStroke(int n) {
        this.stroke = n;
    }

    public void setStrokeReverse(int n) {
        this.strokeReverse = n;
    }
}

