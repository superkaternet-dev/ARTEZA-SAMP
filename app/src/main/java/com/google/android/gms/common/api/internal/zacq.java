/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.internal.zact;

final class zacq
implements Runnable {
    final zact zaa;

    zacq(zact zact2) {
        this.zaa = zact2;
    }

    @Override
    public final void run() {
        zact.zac(this.zaa).zae(new ConnectionResult(4));
    }
}

