/*
 * Decompiled with CFR 0.152.
 */
package com.smarteist.autoimageslider.IndicatorView.animation.type;

public final class IndicatorAnimationType
extends Enum<IndicatorAnimationType> {
    private static final IndicatorAnimationType[] $VALUES;
    public static final /* enum */ IndicatorAnimationType COLOR;
    public static final /* enum */ IndicatorAnimationType DROP;
    public static final /* enum */ IndicatorAnimationType FILL;
    public static final /* enum */ IndicatorAnimationType NONE;
    public static final /* enum */ IndicatorAnimationType SCALE;
    public static final /* enum */ IndicatorAnimationType SCALE_DOWN;
    public static final /* enum */ IndicatorAnimationType SLIDE;
    public static final /* enum */ IndicatorAnimationType SWAP;
    public static final /* enum */ IndicatorAnimationType THIN_WORM;
    public static final /* enum */ IndicatorAnimationType WORM;

    static {
        IndicatorAnimationType indicatorAnimationType;
        IndicatorAnimationType indicatorAnimationType2;
        IndicatorAnimationType indicatorAnimationType3;
        IndicatorAnimationType indicatorAnimationType4;
        IndicatorAnimationType indicatorAnimationType5;
        IndicatorAnimationType indicatorAnimationType6;
        IndicatorAnimationType indicatorAnimationType7;
        IndicatorAnimationType indicatorAnimationType8;
        IndicatorAnimationType indicatorAnimationType9;
        IndicatorAnimationType indicatorAnimationType10;
        NONE = indicatorAnimationType10 = new IndicatorAnimationType();
        COLOR = indicatorAnimationType9 = new IndicatorAnimationType();
        SCALE = indicatorAnimationType8 = new IndicatorAnimationType();
        WORM = indicatorAnimationType7 = new IndicatorAnimationType();
        SLIDE = indicatorAnimationType6 = new IndicatorAnimationType();
        FILL = indicatorAnimationType5 = new IndicatorAnimationType();
        THIN_WORM = indicatorAnimationType4 = new IndicatorAnimationType();
        DROP = indicatorAnimationType3 = new IndicatorAnimationType();
        SWAP = indicatorAnimationType2 = new IndicatorAnimationType();
        SCALE_DOWN = indicatorAnimationType = new IndicatorAnimationType();
        $VALUES = new IndicatorAnimationType[]{indicatorAnimationType10, indicatorAnimationType9, indicatorAnimationType8, indicatorAnimationType7, indicatorAnimationType6, indicatorAnimationType5, indicatorAnimationType4, indicatorAnimationType3, indicatorAnimationType2, indicatorAnimationType};
    }

    public static IndicatorAnimationType valueOf(String string2) {
        return Enum.valueOf(IndicatorAnimationType.class, string2);
    }

    public static IndicatorAnimationType[] values() {
        return (IndicatorAnimationType[])$VALUES.clone();
    }
}

