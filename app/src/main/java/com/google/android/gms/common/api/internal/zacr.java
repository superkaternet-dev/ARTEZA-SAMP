/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.internal.zact;
import com.google.android.gms.signin.internal.zak;

final class zacr
implements Runnable {
    final zak zaa;
    final zact zab;

    zacr(zact zact2, zak zak2) {
        this.zab = zact2;
        this.zaa = zak2;
    }

    @Override
    public final void run() {
        zact.zad(this.zab, this.zaa);
    }
}

