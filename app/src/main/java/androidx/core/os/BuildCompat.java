/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Build$VERSION
 */
package androidx.core.os;

import android.os.Build;

public class BuildCompat {
    private BuildCompat() {
    }

    @Deprecated
    public static boolean isAtLeastN() {
        boolean bl = Build.VERSION.SDK_INT >= 24;
        return bl;
    }

    @Deprecated
    public static boolean isAtLeastNMR1() {
        boolean bl = Build.VERSION.SDK_INT >= 25;
        return bl;
    }

    @Deprecated
    public static boolean isAtLeastO() {
        boolean bl = Build.VERSION.SDK_INT >= 26;
        return bl;
    }

    @Deprecated
    public static boolean isAtLeastOMR1() {
        boolean bl = Build.VERSION.SDK_INT >= 27;
        return bl;
    }

    @Deprecated
    public static boolean isAtLeastP() {
        boolean bl = Build.VERSION.SDK_INT >= 28;
        return bl;
    }

    @Deprecated
    public static boolean isAtLeastQ() {
        boolean bl = Build.VERSION.SDK_INT >= 29;
        return bl;
    }

    public static boolean isAtLeastR() {
        int n = Build.VERSION.CODENAME.length();
        boolean bl = true;
        if (n != 1 || Build.VERSION.CODENAME.charAt(0) < 'R' || Build.VERSION.CODENAME.charAt(0) > 'Z') {
            bl = false;
        }
        return bl;
    }
}

