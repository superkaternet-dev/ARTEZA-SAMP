/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.load;

public final class PreferredColorSpace
extends Enum<PreferredColorSpace> {
    private static final PreferredColorSpace[] $VALUES;
    public static final /* enum */ PreferredColorSpace DISPLAY_P3;
    public static final /* enum */ PreferredColorSpace SRGB;

    static {
        PreferredColorSpace preferredColorSpace;
        PreferredColorSpace preferredColorSpace2;
        SRGB = preferredColorSpace2 = new PreferredColorSpace();
        DISPLAY_P3 = preferredColorSpace = new PreferredColorSpace();
        $VALUES = new PreferredColorSpace[]{preferredColorSpace2, preferredColorSpace};
    }

    public static PreferredColorSpace valueOf(String string2) {
        return Enum.valueOf(PreferredColorSpace.class, string2);
    }

    public static PreferredColorSpace[] values() {
        return (PreferredColorSpace[])$VALUES.clone();
    }
}

