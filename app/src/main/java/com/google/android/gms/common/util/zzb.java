/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Looper
 */
package com.google.android.gms.common.util;

import android.os.Looper;

public final class zzb {
    public static boolean zza() {
        return Looper.getMainLooper() == Looper.myLooper();
    }
}

