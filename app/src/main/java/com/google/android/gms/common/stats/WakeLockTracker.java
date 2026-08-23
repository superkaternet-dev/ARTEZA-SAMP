/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 */
package com.google.android.gms.common.stats;

import android.content.Context;
import android.content.Intent;
import java.util.List;

@Deprecated
public class WakeLockTracker {
    private static WakeLockTracker zza = new WakeLockTracker();

    public static WakeLockTracker getInstance() {
        return zza;
    }

    public void registerAcquireEvent(Context context, Intent intent, String string2, String string3, String string4, int n, String string5) {
    }

    public void registerDeadlineEvent(Context context, String string2, String string3, String string4, int n, List<String> list, boolean bl, long l) {
    }

    public void registerEvent(Context context, String string2, int n, String string3, String string4, String string5, int n2, List<String> list) {
    }

    public void registerEvent(Context context, String string2, int n, String string3, String string4, String string5, int n2, List<String> list, long l) {
    }

    public void registerReleaseEvent(Context context, Intent intent) {
    }
}

