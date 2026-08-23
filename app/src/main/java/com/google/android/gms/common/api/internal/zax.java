/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 */
package com.google.android.gms.common.api.internal;

import android.os.Bundle;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.internal.zaaa;
import com.google.android.gms.common.api.internal.zabz;
import com.google.android.gms.common.api.internal.zaw;

final class zax
implements zabz {
    final zaaa zaa;

    /* synthetic */ zax(zaaa zaaa2, zaw zaw2) {
        this.zaa = zaaa2;
    }

    @Override
    public final void zaa(ConnectionResult connectionResult) {
        zaaa.zaj(this.zaa).lock();
        try {
            zaaa.zak(this.zaa, connectionResult);
            zaaa.zap(this.zaa);
            return;
        }
        finally {
            zaaa.zaj(this.zaa).unlock();
        }
    }

    @Override
    public final void zab(Bundle bundle) {
        zaaa.zaj(this.zaa).lock();
        try {
            zaaa.zao(this.zaa, bundle);
            zaaa.zak(this.zaa, ConnectionResult.RESULT_SUCCESS);
            zaaa.zap(this.zaa);
            return;
        }
        finally {
            zaaa.zaj(this.zaa).unlock();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public final void zac(int n, boolean bl) {
        Object object;
        block4: {
            block3: {
                zaaa.zaj(this.zaa).lock();
                try {
                    object = this.zaa;
                    if (zaaa.zav((zaaa)object) || zaaa.zaa((zaaa)object) == null || !zaaa.zaa((zaaa)object).isSuccess()) break block3;
                    zaaa.zam(this.zaa, true);
                    zaaa.zai(this.zaa).onConnectionSuspended(n);
                }
                catch (Throwable throwable) {
                    zaaa.zaj(this.zaa).unlock();
                    throw throwable;
                }
                object = zaaa.zaj(this.zaa);
                break block4;
            }
            zaaa.zam(this.zaa, false);
            zaaa.zan(this.zaa, n, bl);
            object = zaaa.zaj(this.zaa);
        }
        object.unlock();
    }
}

