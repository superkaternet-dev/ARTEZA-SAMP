/*
 * Decompiled with CFR 0.152.
 */
package com.smarteist.autoimageslider.IndicatorView.draw.data;

public final class Orientation
extends Enum<Orientation> {
    private static final Orientation[] $VALUES;
    public static final /* enum */ Orientation HORIZONTAL;
    public static final /* enum */ Orientation VERTICAL;

    static {
        Orientation orientation;
        Orientation orientation2;
        HORIZONTAL = orientation2 = new Orientation();
        VERTICAL = orientation = new Orientation();
        $VALUES = new Orientation[]{orientation2, orientation};
    }

    public static Orientation valueOf(String string2) {
        return Enum.valueOf(Orientation.class, string2);
    }

    public static Orientation[] values() {
        return (Orientation[])$VALUES.clone();
    }
}

