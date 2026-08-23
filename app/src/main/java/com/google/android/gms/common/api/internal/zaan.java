/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.internal.zaao;
import com.google.android.gms.common.api.internal.zabf;
import com.google.android.gms.common.api.internal.zabg;
import com.google.android.gms.common.internal.BaseGmsClient;

final class zaan
extends zabg {
    final BaseGmsClient.ConnectionProgressReportCallbacks zaa;

    zaan(zaao zaao2, zabf zabf2, BaseGmsClient.ConnectionProgressReportCallbacks connectionProgressReportCallbacks) {
        this.zaa = connectionProgressReportCallbacks;
        super(zabf2);
    }

    @Override
    public final void zaa() {
        this.zaa.onReportServiceBinding(new ConnectionResult(16, null));
    }
}

