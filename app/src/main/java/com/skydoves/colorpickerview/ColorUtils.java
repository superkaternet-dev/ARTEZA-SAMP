/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Color
 */
package com.skydoves.colorpickerview;

import android.graphics.Color;
import java.util.Locale;

class ColorUtils {
    ColorUtils() {
    }

    public static int[] getColorARGB(int n) {
        return new int[]{Color.alpha((int)n), Color.red((int)n), Color.green((int)n), Color.blue((int)n)};
    }

    public static String getHexCode(int n) {
        int n2 = Color.alpha((int)n);
        int n3 = Color.red((int)n);
        int n4 = Color.green((int)n);
        n = Color.blue((int)n);
        return String.format(Locale.getDefault(), "%02X%02X%02X%02X", n2, n3, n4, n);
    }
}

