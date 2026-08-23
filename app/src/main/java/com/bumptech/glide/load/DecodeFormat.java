/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.load;

public final class DecodeFormat
extends Enum<DecodeFormat> {
    private static final DecodeFormat[] $VALUES;
    public static final DecodeFormat DEFAULT;
    public static final /* enum */ DecodeFormat PREFER_ARGB_8888;
    public static final /* enum */ DecodeFormat PREFER_RGB_565;

    static {
        DecodeFormat decodeFormat;
        DecodeFormat decodeFormat2;
        PREFER_ARGB_8888 = decodeFormat2 = new DecodeFormat();
        PREFER_RGB_565 = decodeFormat = new DecodeFormat();
        $VALUES = new DecodeFormat[]{decodeFormat2, decodeFormat};
        DEFAULT = decodeFormat2;
    }

    public static DecodeFormat valueOf(String string2) {
        return Enum.valueOf(DecodeFormat.class, string2);
    }

    public static DecodeFormat[] values() {
        return (DecodeFormat[])$VALUES.clone();
    }
}

