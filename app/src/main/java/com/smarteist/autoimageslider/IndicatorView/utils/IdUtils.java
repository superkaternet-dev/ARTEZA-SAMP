/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Build$VERSION
 *  android.view.View
 */
package com.smarteist.autoimageslider.IndicatorView.utils;

import android.os.Build;
import android.view.View;
import java.util.concurrent.atomic.AtomicInteger;

public class IdUtils {
    private static final AtomicInteger nextGeneratedId = new AtomicInteger(1);

    private static int generateId() {
        int n;
        int n2;
        AtomicInteger atomicInteger;
        do {
            int n3;
            atomicInteger = nextGeneratedId;
            n2 = atomicInteger.get();
            n = n3 = n2 + 1;
            if (n3 <= 0xFFFFFF) continue;
            n = 1;
        } while (!atomicInteger.compareAndSet(n2, n));
        return n2;
    }

    public static int generateViewId() {
        if (Build.VERSION.SDK_INT < 17) {
            return IdUtils.generateId();
        }
        return View.generateViewId();
    }
}

