/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.res.Resources
 *  android.util.DisplayMetrics
 *  android.util.TypedValue
 */
package com.smarteist.autoimageslider.IndicatorView.utils;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class DensityUtils {
    public static int dpToPx(int n) {
        return (int)TypedValue.applyDimension((int)1, (float)n, (DisplayMetrics)Resources.getSystem().getDisplayMetrics());
    }

    public static int pxToDp(float f) {
        return (int)TypedValue.applyDimension((int)0, (float)f, (DisplayMetrics)Resources.getSystem().getDisplayMetrics());
    }
}

