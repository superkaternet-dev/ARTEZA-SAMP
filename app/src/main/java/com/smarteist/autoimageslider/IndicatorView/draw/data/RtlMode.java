/*
 * Decompiled with CFR 0.152.
 */
package com.smarteist.autoimageslider.IndicatorView.draw.data;

public final class RtlMode
extends Enum<RtlMode> {
    private static final RtlMode[] $VALUES;
    public static final /* enum */ RtlMode Auto;
    public static final /* enum */ RtlMode Off;
    public static final /* enum */ RtlMode On;

    static {
        RtlMode rtlMode;
        RtlMode rtlMode2;
        RtlMode rtlMode3;
        On = rtlMode3 = new RtlMode();
        Off = rtlMode2 = new RtlMode();
        Auto = rtlMode = new RtlMode();
        $VALUES = new RtlMode[]{rtlMode3, rtlMode2, rtlMode};
    }

    public static RtlMode valueOf(String string2) {
        return Enum.valueOf(RtlMode.class, string2);
    }

    public static RtlMode[] values() {
        return (RtlMode[])$VALUES.clone();
    }
}

