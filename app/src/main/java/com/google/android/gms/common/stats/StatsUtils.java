/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.PowerManager$WakeLock
 *  android.os.Process
 *  android.text.TextUtils
 */
package com.google.android.gms.common.stats;

import android.os.PowerManager;
import android.os.Process;
import android.text.TextUtils;

@Deprecated
public class StatsUtils {
    public static String getEventKey(PowerManager.WakeLock object, String string2) {
        object = String.valueOf(String.valueOf((long)Process.myPid() << 32 | (long)System.identityHashCode(object)));
        if (TextUtils.isEmpty((CharSequence)string2)) {
            string2 = "";
        }
        string2 = String.valueOf(string2);
        object = string2.length() != 0 ? ((String)object).concat(string2) : new String((String)object);
        return object;
    }
}

