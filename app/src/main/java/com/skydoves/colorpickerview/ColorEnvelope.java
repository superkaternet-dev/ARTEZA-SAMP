/*
 * Decompiled with CFR 0.152.
 */
package com.skydoves.colorpickerview;

import com.skydoves.colorpickerview.ColorUtils;

public class ColorEnvelope {
    private int[] argb;
    private int color;
    private String hexCode;

    public ColorEnvelope(int n) {
        this.color = n;
        this.hexCode = ColorUtils.getHexCode(n);
        this.argb = ColorUtils.getColorARGB(n);
    }

    public int[] getArgb() {
        return this.argb;
    }

    public int getColor() {
        return this.color;
    }

    public String getHexCode() {
        return this.hexCode;
    }
}

