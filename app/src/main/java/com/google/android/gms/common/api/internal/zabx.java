/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.BroadcastReceiver
 *  android.content.Context
 *  android.content.Intent
 */
package com.google.android.gms.common.api.internal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.google.android.gms.common.api.internal.zabw;

public final class zabx
extends BroadcastReceiver {
    Context zaa;
    private final zabw zab;

    public zabx(zabw zabw2) {
        this.zab = zabw2;
    }

    public final void onReceive(Context object, Intent intent) {
        object = intent.getData();
        object = object != null ? object.getSchemeSpecificPart() : null;
        if ("com.google.android.gms".equals(object)) {
            this.zab.zaa();
            this.zab();
        }
    }

    public final void zaa(Context context) {
        this.zaa = context;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void zab() {
        synchronized (this) {
            Context context = this.zaa;
            if (context != null) {
                context.unregisterReceiver((BroadcastReceiver)this);
            }
            this.zaa = null;
            return;
        }
    }
}

