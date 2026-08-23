/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 */
package com.google.android.gms.common.internal;

import android.os.Bundle;
import com.google.android.gms.common.api.internal.ConnectionCallbacks;
import com.google.android.gms.common.internal.BaseGmsClient;

final class zah
implements BaseGmsClient.BaseConnectionCallbacks {
    final ConnectionCallbacks zaa;

    zah(ConnectionCallbacks connectionCallbacks) {
        this.zaa = connectionCallbacks;
    }

    @Override
    public final void onConnected(Bundle bundle) {
        this.zaa.onConnected(bundle);
    }

    @Override
    public final void onConnectionSuspended(int n) {
        this.zaa.onConnectionSuspended(n);
    }
}

