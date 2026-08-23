/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package com.google.android.gms.common.api.internal;

import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.internal.GoogleApiManager;
import com.google.android.gms.common.api.internal.zabq;
import com.google.android.gms.common.api.internal.zabu;

final class zabt
implements Runnable {
    final ConnectionResult zaa;
    final zabu zab;

    zabt(zabu zabu2, ConnectionResult connectionResult) {
        this.zab = zabu2;
        this.zaa = connectionResult;
    }

    @Override
    public final void run() {
        Object object = this.zab;
        object = (zabq)GoogleApiManager.zat(((zabu)object).zaa).get(zabu.zab((zabu)object));
        if (object == null) {
            return;
        }
        if (this.zaa.isSuccess()) {
            zabu.zac(this.zab, true);
            if (zabu.zaa(this.zab).requiresSignIn()) {
                zabu.zad(this.zab);
                return;
            }
            try {
                zabu zabu2 = this.zab;
                zabu.zaa(zabu2).getRemoteService(null, zabu.zaa(zabu2).getScopesForConnectionlessNonSignIn());
                return;
            }
            catch (SecurityException securityException) {
                Log.e((String)"GoogleApiManager", (String)"Failed to get service from broker. ", (Throwable)securityException);
                zabu.zaa(this.zab).disconnect("Failed to get service from broker.");
                ((zabq)object).zar(new ConnectionResult(10), null);
                return;
            }
        }
        ((zabq)object).zar(this.zaa, null);
    }
}

