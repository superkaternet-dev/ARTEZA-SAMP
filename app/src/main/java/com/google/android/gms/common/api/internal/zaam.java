/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.internal.zaao;
import com.google.android.gms.common.api.internal.zaaw;
import com.google.android.gms.common.api.internal.zabf;
import com.google.android.gms.common.api.internal.zabg;

final class zaam
extends zabg {
    final ConnectionResult zaa;
    final zaao zab;

    zaam(zaao zaao2, zabf zabf2, ConnectionResult connectionResult) {
        this.zab = zaao2;
        this.zaa = connectionResult;
        super(zabf2);
    }

    @Override
    public final void zaa() {
        zaaw.zas(this.zab.zaa, this.zaa);
    }
}

