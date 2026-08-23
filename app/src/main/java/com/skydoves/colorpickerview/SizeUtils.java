/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 */
package com.skydoves.colorpickerview;

import android.content.Context;

class SizeUtils {
    SizeUtils() {
    }

    protected static int dp2Px(Context context, int n) {
        float f = context.getResources().getDisplayMetrics().density;
        return (int)((float)n * f + 0.5f);
    }
}

