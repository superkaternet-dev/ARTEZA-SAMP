/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide;

public final class MemoryCategory
extends Enum<MemoryCategory> {
    private static final MemoryCategory[] $VALUES;
    public static final /* enum */ MemoryCategory HIGH;
    public static final /* enum */ MemoryCategory LOW;
    public static final /* enum */ MemoryCategory NORMAL;
    private final float multiplier;

    static {
        MemoryCategory memoryCategory;
        MemoryCategory memoryCategory2;
        MemoryCategory memoryCategory3;
        LOW = memoryCategory3 = new MemoryCategory(0.5f);
        NORMAL = memoryCategory2 = new MemoryCategory(1.0f);
        HIGH = memoryCategory = new MemoryCategory(1.5f);
        $VALUES = new MemoryCategory[]{memoryCategory3, memoryCategory2, memoryCategory};
    }

    private MemoryCategory(float f) {
        this.multiplier = f;
    }

    public static MemoryCategory valueOf(String string2) {
        return Enum.valueOf(MemoryCategory.class, string2);
    }

    public static MemoryCategory[] values() {
        return (MemoryCategory[])$VALUES.clone();
    }

    public float getMultiplier() {
        return this.multiplier;
    }
}

