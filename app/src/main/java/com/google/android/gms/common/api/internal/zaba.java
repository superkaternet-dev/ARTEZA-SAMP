/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.StatusPendingResult;
import com.google.android.gms.common.api.internal.zabe;

final class zaba
implements GoogleApiClient.OnConnectionFailedListener {
    final StatusPendingResult zaa;

    zaba(zabe zabe2, StatusPendingResult statusPendingResult) {
        this.zaa = statusPendingResult;
    }

    @Override
    public final void onConnectionFailed(ConnectionResult connectionResult) {
        this.zaa.setResult(new Status(8));
    }
}

