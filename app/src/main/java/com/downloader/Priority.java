/*
 * Decompiled with CFR 0.152.
 */
package com.downloader;

public final class Priority
extends Enum<Priority> {
    private static final Priority[] $VALUES;
    public static final /* enum */ Priority HIGH;
    public static final /* enum */ Priority IMMEDIATE;
    public static final /* enum */ Priority LOW;
    public static final /* enum */ Priority MEDIUM;

    static {
        Priority priority;
        Priority priority2;
        Priority priority3;
        Priority priority4;
        LOW = priority4 = new Priority();
        MEDIUM = priority3 = new Priority();
        HIGH = priority2 = new Priority();
        IMMEDIATE = priority = new Priority();
        $VALUES = new Priority[]{priority4, priority3, priority2, priority};
    }

    public static Priority valueOf(String string2) {
        return Enum.valueOf(Priority.class, string2);
    }

    public static Priority[] values() {
        return (Priority[])$VALUES.clone();
    }
}

