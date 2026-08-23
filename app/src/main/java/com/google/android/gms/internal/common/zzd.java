/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.PendingIntent
 *  android.content.Context
 *  android.content.Intent
 *  android.os.Build$VERSION
 */
package com.google.android.gms.internal.common;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public final class zzd {
    public static final int zza;

    static {
        int n = Build.VERSION.SDK_INT;
        int n2 = 0x2000000;
        if (n < 31 && (Build.VERSION.SDK_INT < 30 || Build.VERSION.CODENAME.length() != 1 || Build.VERSION.CODENAME.charAt(0) < 'S' || Build.VERSION.CODENAME.charAt(0) > 'Z')) {
            n2 = 0;
        }
        zza = n2;
    }

    public static PendingIntent zza(Context context, int n, Intent intent, int n2) {
        return PendingIntent.getActivity((Context)context, (int)n, (Intent)intent, (int)n2);
    }
}

