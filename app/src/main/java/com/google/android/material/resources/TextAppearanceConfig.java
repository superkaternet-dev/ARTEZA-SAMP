/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.material.resources;

public class TextAppearanceConfig {
    private static boolean shouldLoadFontSynchronously;

    public static void setShouldLoadFontSynchronously(boolean bl) {
        shouldLoadFontSynchronously = bl;
    }

    public static boolean shouldLoadFontSynchronously() {
        return shouldLoadFontSynchronously;
    }
}

