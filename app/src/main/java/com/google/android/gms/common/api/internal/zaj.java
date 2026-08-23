/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package com.google.android.gms.common.api.internal;

import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.internal.zak;

final class zaj
implements GoogleApiClient.OnConnectionFailedListener {
    public final int zaa;
    public final GoogleApiClient zab;
    public final GoogleApiClient.OnConnectionFailedListener zac;
    final zak zad;

    public zaj(zak zak2, int n, GoogleApiClient googleApiClient, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        this.zad = zak2;
        this.zaa = n;
        this.zab = googleApiClient;
        this.zac = onConnectionFailedListener;
    }

    @Override
    public final void onConnectionFailed(ConnectionResult connectionResult) {
        String string2 = String.valueOf(connectionResult);
        String.valueOf(string2).length();
        Log.d((String)"AutoManageHelper", (String)"beginFailureResolution for ".concat(String.valueOf(string2)));
        this.zad.zah(connectionResult, this.zaa);
    }
}

