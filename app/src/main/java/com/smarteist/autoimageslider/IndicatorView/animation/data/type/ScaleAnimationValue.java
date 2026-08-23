/*
 * Decompiled with CFR 0.152.
 */
package com.smarteist.autoimageslider.IndicatorView.animation.data.type;

import com.smarteist.autoimageslider.IndicatorView.animation.data.Value;
import com.smarteist.autoimageslider.IndicatorView.animation.data.type.ColorAnimationValue;

public class ScaleAnimationValue
extends ColorAnimationValue
implements Value {
    private int radius;
    private int radiusReverse;

    public int getRadius() {
        return this.radius;
    }

    public int getRadiusReverse() {
        return this.radiusReverse;
    }

    public void setRadius(int n) {
        this.radius = n;
    }

    public void setRadiusReverse(int n) {
        this.radiusReverse = n;
    }
}

