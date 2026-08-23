/*
 * Decompiled with CFR 0.152.
 */
package com.smarteist.autoimageslider.IndicatorView.animation.data.type;

import com.smarteist.autoimageslider.IndicatorView.animation.data.Value;

public class WormAnimationValue
implements Value {
    private int rectEnd;
    private int rectStart;

    public int getRectEnd() {
        return this.rectEnd;
    }

    public int getRectStart() {
        return this.rectStart;
    }

    public void setRectEnd(int n) {
        this.rectEnd = n;
    }

    public void setRectStart(int n) {
        this.rectStart = n;
    }
}

