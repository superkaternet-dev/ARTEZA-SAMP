/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.SharedPreferences$Editor
 */
package com.google.android.gms.common.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtils {
    private SharedPreferencesUtils() {
    }

    @Deprecated
    public static void publishWorldReadableSharedPreferences(Context context, SharedPreferences.Editor editor, String string2) {
        throw new IllegalStateException("world-readable shared preferences should only be used by apk");
    }
}

