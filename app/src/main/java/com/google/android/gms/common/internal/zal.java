/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.SparseIntArray
 */
package com.google.android.gms.common.internal;

import android.content.Context;
import android.util.SparseIntArray;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.internal.Preconditions;

public final class zal {
    private final SparseIntArray zaa = new SparseIntArray();
    private GoogleApiAvailabilityLight zab;

    public zal() {
        this(GoogleApiAvailability.getInstance());
    }

    public zal(GoogleApiAvailabilityLight googleApiAvailabilityLight) {
        Preconditions.checkNotNull(googleApiAvailabilityLight);
        this.zab = googleApiAvailabilityLight;
    }

    public final int zaa(Context context, int n) {
        return this.zaa.get(n, -1);
    }

    public final int zab(Context context, Api.Client client) {
        Preconditions.checkNotNull(context);
        Preconditions.checkNotNull(client);
        boolean bl = client.requiresGooglePlayServices();
        int n = 0;
        if (!bl) {
            return 0;
        }
        int n2 = client.getMinApkVersion();
        int n3 = this.zaa(context, n2);
        if (n3 == -1) {
            block4: {
                for (n3 = 0; n3 < this.zaa.size(); ++n3) {
                    int n4 = this.zaa.keyAt(n3);
                    if (n4 <= n2 || this.zaa.get(n4) != 0) continue;
                    n3 = n;
                    break block4;
                }
                n3 = -1;
            }
            if (n3 == -1) {
                n3 = this.zab.isGooglePlayServicesAvailable(context, n2);
            }
            this.zaa.put(n2, n3);
        }
        return n3;
    }

    public final void zac() {
        this.zaa.clear();
    }
}

