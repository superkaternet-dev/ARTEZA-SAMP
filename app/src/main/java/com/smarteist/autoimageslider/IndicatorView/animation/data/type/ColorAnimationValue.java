/*
 * Decompiled with CFR 0.152.
 */
package com.smarteist.autoimageslider.IndicatorView.animation.data.type;

import com.smarteist.autoimageslider.IndicatorView.animation.data.Value;

public class ColorAnimationValue
implements Value {
    private int color;
    private int colorReverse;

    public int getColor() {
        return this.color;
    }

    public int getColorReverse() {
        return this.colorReverse;
    }

    public void setColor(int n) {
        this.color = n;
    }

    public void setColorReverse(int n) {
        this.colorReverse = n;
    }
}

