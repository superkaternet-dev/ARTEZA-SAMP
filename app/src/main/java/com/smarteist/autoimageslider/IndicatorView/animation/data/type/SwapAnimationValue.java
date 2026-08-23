/*
 * Decompiled with CFR 0.152.
 */
package com.smarteist.autoimageslider.IndicatorView.animation.data.type;

import com.smarteist.autoimageslider.IndicatorView.animation.data.Value;

public class SwapAnimationValue
implements Value {
    private int coordinate;
    private int coordinateReverse;

    public int getCoordinate() {
        return this.coordinate;
    }

    public int getCoordinateReverse() {
        return this.coordinateReverse;
    }

    public void setCoordinate(int n) {
        this.coordinate = n;
    }

    public void setCoordinateReverse(int n) {
        this.coordinateReverse = n;
    }
}

