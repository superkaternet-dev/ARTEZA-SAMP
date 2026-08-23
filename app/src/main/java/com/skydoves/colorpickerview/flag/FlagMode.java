/*
 * Decompiled with CFR 0.152.
 */
package com.skydoves.colorpickerview.flag;

public final class FlagMode
extends Enum<FlagMode> {
    private static final FlagMode[] $VALUES;
    public static final /* enum */ FlagMode ALWAYS;
    public static final /* enum */ FlagMode FADE;
    public static final /* enum */ FlagMode LAST;

    static {
        FlagMode flagMode;
        FlagMode flagMode2;
        FlagMode flagMode3;
        ALWAYS = flagMode3 = new FlagMode();
        LAST = flagMode2 = new FlagMode();
        FADE = flagMode = new FlagMode();
        $VALUES = new FlagMode[]{flagMode3, flagMode2, flagMode};
    }

    public static FlagMode valueOf(String string2) {
        return Enum.valueOf(FlagMode.class, string2);
    }

    public static FlagMode[] values() {
        return (FlagMode[])$VALUES.clone();
    }
}

