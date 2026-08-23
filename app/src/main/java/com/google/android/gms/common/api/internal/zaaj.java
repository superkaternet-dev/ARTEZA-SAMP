/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  android.os.DeadObjectException
 */
package com.google.android.gms.common.api.internal;

import android.os.Bundle;
import android.os.DeadObjectException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.BaseImplementation;
import com.google.android.gms.common.api.internal.zaah;
import com.google.android.gms.common.api.internal.zaai;
import com.google.android.gms.common.api.internal.zabe;
import com.google.android.gms.common.api.internal.zabf;
import com.google.android.gms.common.api.internal.zabi;
import com.google.android.gms.common.api.internal.zada;
import com.google.android.gms.common.internal.Preconditions;
import java.util.Set;

public final class zaaj
implements zabf {
    private final zabi zaa;
    private boolean zab = false;

    public zaaj(zabi zabi2) {
        this.zaa = zabi2;
    }

    static /* bridge */ /* synthetic */ zabi zac(zaaj zaaj2) {
        return zaaj2.zaa;
    }

    @Override
    public final <A extends Api.AnyClient, R extends Result, T extends BaseImplementation.ApiMethodImpl<R, A>> T zaa(T t) {
        this.zab(t);
        return t;
    }

    @Override
    public final <A extends Api.AnyClient, T extends BaseImplementation.ApiMethodImpl<? extends Result, A>> T zab(T t) {
        try {
            this.zaa.zag.zai.zaa(t);
            zabe zabe2 = this.zaa.zag;
            Object object = t.getClientKey();
            object = zabe2.zac.get(object);
            Preconditions.checkNotNull(object, "Appropriate Api was not requested.");
            if (!object.isConnected() && this.zaa.zab.containsKey(t.getClientKey())) {
                object = new Status(17);
                t.setFailedResult((Status)object);
            } else {
                t.run((Object)object);
            }
        }
        catch (DeadObjectException deadObjectException) {
            this.zaa.zal(new zaah(this, this));
        }
        return t;
    }

    @Override
    public final void zad() {
    }

    @Override
    public final void zae() {
        if (this.zab) {
            this.zab = false;
            this.zaa.zal(new zaai(this, this));
        }
    }

    final void zaf() {
        if (this.zab) {
            this.zab = false;
            this.zaa.zag.zai.zab();
            this.zaj();
        }
    }

    @Override
    public final void zag(Bundle bundle) {
    }

    @Override
    public final void zah(ConnectionResult connectionResult, Api<?> api, boolean bl) {
    }

    @Override
    public final void zai(int n) {
        this.zaa.zak(null);
        this.zaa.zah.zac(n, this.zab);
    }

    @Override
    public final boolean zaj() {
        if (this.zab) {
            return false;
        }
        Set<zada> set = this.zaa.zag.zah;
        if (set != null && !set.isEmpty()) {
            this.zab = true;
            set = set.iterator();
            while (set.hasNext()) {
                ((zada)set.next()).zah();
            }
            return false;
        }
        this.zaa.zak(null);
        return true;
    }
}

