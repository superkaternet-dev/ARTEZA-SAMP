/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.PorterDuff$Mode
 *  android.view.View
 */
package com.google.android.material.internal;

import android.graphics.PorterDuff;
import android.view.View;
import androidx.core.view.ViewCompat;

public class ViewUtils {
    public static boolean isLayoutRtl(View view) {
        int n = ViewCompat.getLayoutDirection(view);
        boolean bl = true;
        if (n != 1) {
            bl = false;
        }
        return bl;
    }

    public static PorterDuff.Mode parseTintMode(int n, PorterDuff.Mode mode) {
        switch (n) {
            default: {
                return mode;
            }
            case 16: {
                return PorterDuff.Mode.ADD;
            }
            case 15: {
                return PorterDuff.Mode.SCREEN;
            }
            case 14: {
                return PorterDuff.Mode.MULTIPLY;
            }
            case 9: {
                return PorterDuff.Mode.SRC_ATOP;
            }
            case 5: {
                return PorterDuff.Mode.SRC_IN;
            }
            case 3: 
        }
        return PorterDuff.Mode.SRC_OVER;
    }
}

