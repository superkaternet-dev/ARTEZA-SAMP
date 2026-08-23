/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 */
package com.google.android.gms.common.api.internal;

import android.os.Bundle;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.internal.StatusPendingResult;
import com.google.android.gms.common.api.internal.zabe;
import com.google.android.gms.common.internal.Preconditions;
import java.util.concurrent.atomic.AtomicReference;

final class zaaz
implements GoogleApiClient.ConnectionCallbacks {
    final AtomicReference zaa;
    final StatusPendingResult zab;
    final zabe zac;

    zaaz(zabe zabe2, AtomicReference atomicReference, StatusPendingResult statusPendingResult) {
        this.zac = zabe2;
        this.zaa = atomicReference;
        this.zab = statusPendingResult;
    }

    @Override
    public final void onConnected(Bundle bundle) {
        zabe.zah(this.zac, Preconditions.checkNotNull((GoogleApiClient)this.zaa.get()), this.zab, true);
    }

    @Override
    public final void onConnectionSuspended(int n) {
    }
}

