/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Intent
 */
package com.google.android.gms.common.internal;

import android.app.Activity;
import android.content.Intent;
import com.google.android.gms.common.internal.zag;

final class zad
extends zag {
    final Intent zaa;
    final Activity zab;
    final int zac;

    zad(Intent intent, Activity activity, int n) {
        this.zaa = intent;
        this.zab = activity;
        this.zac = n;
    }

    @Override
    public final void zaa() {
        Intent intent = this.zaa;
        if (intent != null) {
            this.zab.startActivityForResult(intent, this.zac);
        }
    }
}

