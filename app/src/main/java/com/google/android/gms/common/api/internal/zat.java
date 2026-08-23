/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 */
package com.google.android.gms.common.api.internal;

import android.os.Bundle;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.internal.zau;
import com.google.android.gms.common.internal.Preconditions;

public final class zat
implements GoogleApiClient.ConnectionCallbacks,
GoogleApiClient.OnConnectionFailedListener {
    public final Api<?> zaa;
    private final boolean zab;
    private zau zac;

    public zat(Api<?> api, boolean bl) {
        this.zaa = api;
        this.zab = bl;
    }

    private final zau zab() {
        Preconditions.checkNotNull(this.zac, "Callbacks must be attached to a ClientConnectionHelper instance before connecting the client.");
        return this.zac;
    }

    @Override
    public final void onConnected(Bundle bundle) {
        this.zab().onConnected(bundle);
    }

    @Override
    public final void onConnectionFailed(ConnectionResult connectionResult) {
        this.zab().zaa(connectionResult, this.zaa, this.zab);
    }

    @Override
    public final void onConnectionSuspended(int n) {
        this.zab().onConnectionSuspended(n);
    }

    public final void zaa(zau zau2) {
        this.zac = zau2;
    }
}

