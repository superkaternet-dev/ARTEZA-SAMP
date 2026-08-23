/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.StatusPendingResult;
import com.google.android.gms.common.api.internal.zabe;

final class zabb
implements ResultCallback<Status> {
    final StatusPendingResult zaa;
    final boolean zab;
    final GoogleApiClient zac;
    final zabe zad;

    zabb(zabe zabe2, StatusPendingResult statusPendingResult, boolean bl, GoogleApiClient googleApiClient) {
        this.zad = zabe2;
        this.zaa = statusPendingResult;
        this.zab = bl;
        this.zac = googleApiClient;
    }
}

