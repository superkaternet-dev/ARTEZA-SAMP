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
import com.google.android.gms.common.api.internal.zay;

final class zaz
implements zabz {
    final zaaa zaa;

    /* synthetic */ zaz(zaaa zaaa2, zay zay2) {
        this.zaa = zaaa2;
    }

    @Override
    public final void zaa(ConnectionResult connectionResult) {
        zaaa.zaj(this.zaa).lock();
        try {
            zaaa.zal(this.zaa, connectionResult);
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
            zaaa.zal(this.zaa, ConnectionResult.RESULT_SUCCESS);
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
                    if (!zaaa.zav((zaaa)object)) break block3;
                    zaaa.zam((zaaa)object, false);
                    zaaa.zan(this.zaa, n, bl);
                }
                catch (Throwable throwable) {
                    zaaa.zaj(this.zaa).unlock();
                    throw throwable;
                }
                object = zaaa.zaj(this.zaa);
                break block4;
            }
            zaaa.zam((zaaa)object, true);
            zaaa.zah(this.zaa).onConnectionSuspended(n);
            object = zaaa.zaj(this.zaa);
        }
        object.unlock();
    }
}

