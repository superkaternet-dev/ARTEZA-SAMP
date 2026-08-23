/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.load;

public final class EncodeStrategy
extends Enum<EncodeStrategy> {
    private static final EncodeStrategy[] $VALUES;
    public static final /* enum */ EncodeStrategy NONE;
    public static final /* enum */ EncodeStrategy SOURCE;
    public static final /* enum */ EncodeStrategy TRANSFORMED;

    static {
        EncodeStrategy encodeStrategy;
        EncodeStrategy encodeStrategy2;
        EncodeStrategy encodeStrategy3;
        SOURCE = encodeStrategy3 = new EncodeStrategy();
        TRANSFORMED = encodeStrategy2 = new EncodeStrategy();
        NONE = encodeStrategy = new EncodeStrategy();
        $VALUES = new EncodeStrategy[]{encodeStrategy3, encodeStrategy2, encodeStrategy};
    }

    public static EncodeStrategy valueOf(String string2) {
        return Enum.valueOf(EncodeStrategy.class, string2);
    }

    public static EncodeStrategy[] values() {
        return (EncodeStrategy[])$VALUES.clone();
    }
}

