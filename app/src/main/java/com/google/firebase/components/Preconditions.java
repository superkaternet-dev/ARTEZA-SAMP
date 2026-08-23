/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.components;

public final class Preconditions {
    public static void checkArgument(boolean bl, String string2) {
        if (bl) {
            return;
        }
        throw new IllegalArgumentException(string2);
    }

    public static <T> T checkNotNull(T t) {
        if (t != null) {
            return t;
        }
        throw new NullPointerException();
    }

    public static <T> T checkNotNull(T t, String string2) {
        if (t != null) {
            return t;
        }
        throw new NullPointerException(string2);
    }

    public static void checkState(boolean bl, String string2) {
        if (bl) {
            return;
        }
        throw new IllegalStateException(string2);
    }
}

