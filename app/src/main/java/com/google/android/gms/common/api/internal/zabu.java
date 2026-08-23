/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package com.google.android.gms.common.api.internal;

import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.internal.ApiKey;
import com.google.android.gms.common.api.internal.GoogleApiManager;
import com.google.android.gms.common.api.internal.zabq;
import com.google.android.gms.common.api.internal.zabt;
import com.google.android.gms.common.api.internal.zacs;
import com.google.android.gms.common.internal.BaseGmsClient;
import com.google.android.gms.common.internal.IAccountAccessor;
import java.util.Set;

final class zabu
implements BaseGmsClient.ConnectionProgressReportCallbacks,
zacs {
    final GoogleApiManager zaa;
    private final Api.Client zab;
    private final ApiKey<?> zac;
    private IAccountAccessor zad;
    private Set<Scope> zae;
    private boolean zaf;

    /*
     * Ignored method signature, as it can't be verified against descriptor
     */
    public zabu(GoogleApiManager googleApiManager, Api.Client client, ApiKey apiKey) {
        this.zaa = googleApiManager;
        this.zad = null;
        this.zae = null;
        this.zaf = false;
        this.zab = client;
        this.zac = apiKey;
    }

    static /* bridge */ /* synthetic */ Api.Client zaa(zabu zabu2) {
        return zabu2.zab;
    }

    static /* bridge */ /* synthetic */ ApiKey zab(zabu zabu2) {
        return zabu2.zac;
    }

    static /* bridge */ /* synthetic */ void zac(zabu zabu2, boolean bl) {
        zabu2.zaf = true;
    }

    static /* bridge */ /* synthetic */ void zad(zabu zabu2) {
        zabu2.zag();
    }

    private final void zag() {
        IAccountAccessor iAccountAccessor;
        if (this.zaf && (iAccountAccessor = this.zad) != null) {
            this.zab.getRemoteService(iAccountAccessor, this.zae);
        }
    }

    @Override
    public final void onReportServiceBinding(ConnectionResult connectionResult) {
        GoogleApiManager.zaf(this.zaa).post((Runnable)new zabt(this, connectionResult));
    }

    @Override
    public final void zae(ConnectionResult connectionResult) {
        zabq zabq2 = (zabq)GoogleApiManager.zat(this.zaa).get(this.zac);
        if (zabq2 != null) {
            zabq2.zas(connectionResult);
        }
    }

    @Override
    public final void zaf(IAccountAccessor iAccountAccessor, Set<Scope> set) {
        if (iAccountAccessor != null && set != null) {
            this.zad = iAccountAccessor;
            this.zae = set;
            this.zag();
            return;
        }
        Log.wtf((String)"GoogleApiManager", (String)"Received null response from onSignInSuccess", (Throwable)new Exception());
        this.zae(new ConnectionResult(4));
    }
}

