/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide;

public final class Priority
extends Enum<Priority> {
    private static final Priority[] $VALUES;
    public static final /* enum */ Priority HIGH;
    public static final /* enum */ Priority IMMEDIATE;
    public static final /* enum */ Priority LOW;
    public static final /* enum */ Priority NORMAL;

    static {
        Priority priority;
        Priority priority2;
        Priority priority3;
        Priority priority4;
        IMMEDIATE = priority4 = new Priority();
        HIGH = priority3 = new Priority();
        NORMAL = priority2 = new Priority();
        LOW = priority = new Priority();
        $VALUES = new Priority[]{priority4, priority3, priority2, priority};
    }

    public static Priority valueOf(String string2) {
        return Enum.valueOf(Priority.class, string2);
    }

    public static Priority[] values() {
        return (Priority[])$VALUES.clone();
    }
}

