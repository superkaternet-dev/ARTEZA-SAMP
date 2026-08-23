/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.res.ColorStateList
 *  android.graphics.Color
 *  android.os.Build$VERSION
 *  android.util.StateSet
 */
package com.google.android.material.ripple;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.util.StateSet;
import androidx.core.graphics.ColorUtils;

public class RippleUtils {
    private static final int[] FOCUSED_STATE_SET;
    private static final int[] HOVERED_FOCUSED_STATE_SET;
    private static final int[] HOVERED_STATE_SET;
    private static final int[] PRESSED_STATE_SET;
    private static final int[] SELECTED_FOCUSED_STATE_SET;
    private static final int[] SELECTED_HOVERED_FOCUSED_STATE_SET;
    private static final int[] SELECTED_HOVERED_STATE_SET;
    private static final int[] SELECTED_PRESSED_STATE_SET;
    private static final int[] SELECTED_STATE_SET;
    public static final boolean USE_FRAMEWORK_RIPPLE;

    static {
        boolean bl = Build.VERSION.SDK_INT >= 21;
        USE_FRAMEWORK_RIPPLE = bl;
        PRESSED_STATE_SET = new int[]{16842919};
        HOVERED_FOCUSED_STATE_SET = new int[]{16843623, 16842908};
        FOCUSED_STATE_SET = new int[]{16842908};
        HOVERED_STATE_SET = new int[]{16843623};
        SELECTED_PRESSED_STATE_SET = new int[]{0x10100A1, 16842919};
        SELECTED_HOVERED_FOCUSED_STATE_SET = new int[]{0x10100A1, 16843623, 16842908};
        SELECTED_FOCUSED_STATE_SET = new int[]{0x10100A1, 16842908};
        SELECTED_HOVERED_STATE_SET = new int[]{0x10100A1, 16843623};
        SELECTED_STATE_SET = new int[]{0x10100A1};
    }

    private RippleUtils() {
    }

    public static ColorStateList convertToRippleDrawableColor(ColorStateList colorStateList) {
        if (USE_FRAMEWORK_RIPPLE) {
            int[][] nArrayArray = new int[2][];
            int[] nArray = new int[2];
            nArrayArray[0] = SELECTED_STATE_SET;
            nArray[0] = RippleUtils.getColorForState(colorStateList, SELECTED_PRESSED_STATE_SET);
            int n = 0 + 1;
            nArrayArray[n] = StateSet.NOTHING;
            nArray[n] = RippleUtils.getColorForState(colorStateList, PRESSED_STATE_SET);
            return new ColorStateList((int[][])nArrayArray, nArray);
        }
        int[][] nArrayArray = new int[10][];
        int[] nArray = new int[10];
        int[] nArray2 = SELECTED_PRESSED_STATE_SET;
        nArrayArray[0] = nArray2;
        nArray[0] = RippleUtils.getColorForState(colorStateList, nArray2);
        int n = 0 + 1;
        nArray2 = SELECTED_HOVERED_FOCUSED_STATE_SET;
        nArrayArray[n] = nArray2;
        nArray[n] = RippleUtils.getColorForState(colorStateList, nArray2);
        nArray2 = SELECTED_FOCUSED_STATE_SET;
        nArrayArray[++n] = nArray2;
        nArray[n] = RippleUtils.getColorForState(colorStateList, nArray2);
        nArray2 = SELECTED_HOVERED_STATE_SET;
        nArrayArray[++n] = nArray2;
        nArray[n] = RippleUtils.getColorForState(colorStateList, nArray2);
        nArrayArray[++n] = SELECTED_STATE_SET;
        nArray[n] = 0;
        nArray2 = PRESSED_STATE_SET;
        nArrayArray[++n] = nArray2;
        nArray[n] = RippleUtils.getColorForState(colorStateList, nArray2);
        nArray2 = HOVERED_FOCUSED_STATE_SET;
        nArrayArray[++n] = nArray2;
        nArray[n] = RippleUtils.getColorForState(colorStateList, nArray2);
        nArray2 = FOCUSED_STATE_SET;
        nArrayArray[++n] = nArray2;
        nArray[n] = RippleUtils.getColorForState(colorStateList, nArray2);
        nArray2 = HOVERED_STATE_SET;
        nArrayArray[++n] = nArray2;
        nArray[n] = RippleUtils.getColorForState(colorStateList, nArray2);
        nArrayArray[++n] = StateSet.NOTHING;
        nArray[n] = 0;
        return new ColorStateList((int[][])nArrayArray, nArray);
    }

    private static int doubleAlpha(int n) {
        return ColorUtils.setAlphaComponent(n, Math.min(Color.alpha((int)n) * 2, 255));
    }

    private static int getColorForState(ColorStateList colorStateList, int[] nArray) {
        int n;
        block0: {
            n = colorStateList != null ? colorStateList.getColorForState(nArray, colorStateList.getDefaultColor()) : 0;
            if (!USE_FRAMEWORK_RIPPLE) break block0;
            n = RippleUtils.doubleAlpha(n);
        }
        return n;
    }
}

