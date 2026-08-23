/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 */
package com.google.android.gms.common.api.internal;

import android.os.Bundle;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.internal.zaar;
import com.google.android.gms.common.api.internal.zaas;
import com.google.android.gms.common.api.internal.zaaw;
import com.google.android.gms.common.internal.Preconditions;

final class zaat
implements GoogleApiClient.ConnectionCallbacks,
GoogleApiClient.OnConnectionFailedListener {
    final zaaw zaa;

    /* synthetic */ zaat(zaaw zaaw2, zaas zaas2) {
        this.zaa = zaaw2;
    }

    @Override
    public final void onConnected(Bundle object) {
        object = Preconditions.checkNotNull(zaaw.zal(this.zaa));
        Preconditions.checkNotNull(zaaw.zan(this.zaa)).zad(new zaar(this.zaa));
    }

    @Override
    public final void onConnectionFailed(ConnectionResult connectionResult) {
        zaaw.zap(this.zaa).lock();
        try {
            if (zaaw.zay(this.zaa, connectionResult)) {
                zaaw.zaq(this.zaa);
                zaaw.zau(this.zaa);
            } else {
                zaaw.zas(this.zaa, connectionResult);
            }
            return;
        }
        finally {
            zaaw.zap(this.zaa).unlock();
        }
    }

    @Override
    public final void onConnectionSuspended(int n) {
    }
}

