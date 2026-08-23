/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Build$VERSION
 *  android.os.SystemClock
 */
package com.bumptech.glide.util;

import android.os.Build;
import android.os.SystemClock;

public final class LogTime {
    private static final double MILLIS_MULTIPLIER;

    static {
        int n = Build.VERSION.SDK_INT;
        double d = 1.0;
        if (n >= 17) {
            d = 1.0 / Math.pow(10.0, 6.0);
        }
        MILLIS_MULTIPLIER = d;
    }

    private LogTime() {
    }

    public static double getElapsedMillis(long l) {
        double d = LogTime.getLogTime() - l;
        double d2 = MILLIS_MULTIPLIER;
        Double.isNaN(d);
        return d * d2;
    }

    public static long getLogTime() {
        if (Build.VERSION.SDK_INT >= 17) {
            return SystemClock.elapsedRealtimeNanos();
        }
        return SystemClock.uptimeMillis();
    }
}

