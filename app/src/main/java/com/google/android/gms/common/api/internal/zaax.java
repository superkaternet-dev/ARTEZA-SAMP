/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  org.checkerframework.checker.initialization.qual.NotOnlyInitialized
 */
package com.google.android.gms.common.api.internal;

import android.os.Bundle;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.internal.BaseImplementation;
import com.google.android.gms.common.api.internal.zabf;
import com.google.android.gms.common.api.internal.zabi;
import java.util.Collections;
import java.util.Iterator;
import org.checkerframework.checker.initialization.qual.NotOnlyInitialized;

public final class zaax
implements zabf {
    @NotOnlyInitialized
    private final zabi zaa;

    public zaax(zabi zabi2) {
        this.zaa = zabi2;
    }

    @Override
    public final <A extends Api.AnyClient, R extends Result, T extends BaseImplementation.ApiMethodImpl<R, A>> T zaa(T t) {
        this.zaa.zag.zaa.add(t);
        return t;
    }

    @Override
    public final <A extends Api.AnyClient, T extends BaseImplementation.ApiMethodImpl<? extends Result, A>> T zab(T t) {
        throw new IllegalStateException("GoogleApiClient is not connected yet.");
    }

    @Override
    public final void zad() {
        Iterator<Api.Client> iterator2 = this.zaa.zaa.values().iterator();
        while (iterator2.hasNext()) {
            iterator2.next().disconnect();
        }
        this.zaa.zag.zad = Collections.emptySet();
    }

    @Override
    public final void zae() {
        this.zaa.zaj();
    }

    @Override
    public final void zag(Bundle bundle) {
    }

    @Override
    public final void zah(ConnectionResult connectionResult, Api<?> api, boolean bl) {
    }

    @Override
    public final void zai(int n) {
    }

    @Override
    public final boolean zaj() {
        return true;
    }
}

