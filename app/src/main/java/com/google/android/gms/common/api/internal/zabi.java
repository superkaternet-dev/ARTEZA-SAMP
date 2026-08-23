/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Bundle
 *  android.os.Looper
 *  android.os.Message
 *  org.checkerframework.checker.initialization.qual.NotOnlyInitialized
 */
package com.google.android.gms.common.api.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.internal.BaseImplementation;
import com.google.android.gms.common.api.internal.SignInConnectionListener;
import com.google.android.gms.common.api.internal.zaaj;
import com.google.android.gms.common.api.internal.zaaw;
import com.google.android.gms.common.api.internal.zaax;
import com.google.android.gms.common.api.internal.zabe;
import com.google.android.gms.common.api.internal.zabf;
import com.google.android.gms.common.api.internal.zabg;
import com.google.android.gms.common.api.internal.zabh;
import com.google.android.gms.common.api.internal.zabz;
import com.google.android.gms.common.api.internal.zaca;
import com.google.android.gms.common.api.internal.zat;
import com.google.android.gms.common.api.internal.zau;
import com.google.android.gms.common.internal.ClientSettings;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.signin.SignInOptions;
import com.google.android.gms.signin.zae;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import org.checkerframework.checker.initialization.qual.NotOnlyInitialized;

public final class zabi
implements zaca,
zau {
    final Map<Api.AnyClientKey<?>, Api.Client> zaa;
    final Map<Api.AnyClientKey<?>, ConnectionResult> zab = new HashMap();
    final ClientSettings zac;
    final Map<Api<?>, Boolean> zad;
    final Api.AbstractClientBuilder<? extends zae, SignInOptions> zae;
    int zaf;
    final zabe zag;
    final zabz zah;
    private final Lock zai;
    private final Condition zaj;
    private final Context zak;
    private final GoogleApiAvailabilityLight zal;
    private final zabh zam;
    @NotOnlyInitialized
    private volatile zabf zan;
    private ConnectionResult zao = null;

    public zabi(Context context, zabe zabe2, Lock lock, Looper looper, GoogleApiAvailabilityLight googleApiAvailabilityLight, Map<Api.AnyClientKey<?>, Api.Client> map, ClientSettings clientSettings, Map<Api<?>, Boolean> map2, Api.AbstractClientBuilder<? extends zae, SignInOptions> abstractClientBuilder, ArrayList<zat> arrayList, zabz zabz2) {
        this.zak = context;
        this.zai = lock;
        this.zal = googleApiAvailabilityLight;
        this.zaa = map;
        this.zac = clientSettings;
        this.zad = map2;
        this.zae = abstractClientBuilder;
        this.zag = zabe2;
        this.zah = zabz2;
        int n = arrayList.size();
        for (int i = 0; i < n; ++i) {
            ((zat)arrayList.get(i)).zaa(this);
        }
        this.zam = new zabh(this, looper);
        this.zaj = lock.newCondition();
        this.zan = new zaax(this);
    }

    static /* bridge */ /* synthetic */ zabf zag(zabi zabi2) {
        return zabi2.zan;
    }

    static /* bridge */ /* synthetic */ Lock zah(zabi zabi2) {
        return zabi2.zai;
    }

    @Override
    public final void onConnected(Bundle bundle) {
        this.zai.lock();
        try {
            this.zan.zag(bundle);
            return;
        }
        finally {
            this.zai.unlock();
        }
    }

    @Override
    public final void onConnectionSuspended(int n) {
        this.zai.lock();
        try {
            this.zan.zai(n);
            return;
        }
        finally {
            this.zai.unlock();
        }
    }

    @Override
    public final void zaa(ConnectionResult connectionResult, Api<?> api, boolean bl) {
        this.zai.lock();
        try {
            this.zan.zah(connectionResult, api, bl);
            return;
        }
        finally {
            this.zai.unlock();
        }
    }

    @Override
    public final ConnectionResult zab() {
        this.zaq();
        while (this.zan instanceof zaaw) {
            try {
                this.zaj.await();
            }
            catch (InterruptedException interruptedException) {
                Thread.currentThread().interrupt();
                return new ConnectionResult(15, null);
            }
        }
        if (this.zan instanceof zaaj) {
            return ConnectionResult.RESULT_SUCCESS;
        }
        ConnectionResult connectionResult = this.zao;
        if (connectionResult != null) {
            return connectionResult;
        }
        return new ConnectionResult(13, null);
    }

    /*
     * Unable to fully structure code
     * Could not resolve type clashes
     */
    @Override
    public final ConnectionResult zac(long var1_1, TimeUnit var3_2) {
        this.zaq();
        var1_1 = var3_2.toNanos(var1_1);
        while (this.zan instanceof zaaw) {
            if (var1_1 > 0L) ** GOTO lbl8
            try {
                this.zar();
                return new ConnectionResult(14, null);
lbl8:
                // 1 sources

                var1_1 = this.zaj.awaitNanos(var1_1);
            }
            catch (InterruptedException var3_3) {
                Thread.currentThread().interrupt();
                return new ConnectionResult(15, null);
            }
        }
        if (this.zan instanceof zaaj) {
            return ConnectionResult.RESULT_SUCCESS;
        }
        var3_2 = this.zao;
        if (var3_2 != null) {
            return var3_2;
        }
        return new ConnectionResult(13, null);
    }

    @Override
    public final ConnectionResult zad(Api<?> object) {
        if (this.zaa.containsKey(object = ((Api)object).zab())) {
            if (this.zaa.get(object).isConnected()) {
                return ConnectionResult.RESULT_SUCCESS;
            }
            if (this.zab.containsKey(object)) {
                return this.zab.get(object);
            }
        }
        return null;
    }

    @Override
    public final <A extends Api.AnyClient, R extends Result, T extends BaseImplementation.ApiMethodImpl<R, A>> T zae(T t) {
        t.zak();
        this.zan.zaa(t);
        return t;
    }

    @Override
    public final <A extends Api.AnyClient, T extends BaseImplementation.ApiMethodImpl<? extends Result, A>> T zaf(T t) {
        t.zak();
        return this.zan.zab(t);
    }

    final void zai() {
        this.zai.lock();
        try {
            this.zag.zak();
            zaaj zaaj2 = new zaaj(this);
            this.zan = zaaj2;
            this.zan.zad();
            this.zaj.signalAll();
            return;
        }
        finally {
            this.zai.unlock();
        }
    }

    final void zaj() {
        this.zai.lock();
        try {
            zaaw zaaw2 = new zaaw(this, this.zac, this.zad, this.zal, this.zae, this.zai, this.zak);
            this.zan = zaaw2;
            this.zan.zad();
            this.zaj.signalAll();
            return;
        }
        finally {
            this.zai.unlock();
        }
    }

    final void zak(ConnectionResult object) {
        this.zai.lock();
        try {
            this.zao = object;
            this.zan = object = new zaax(this);
            this.zan.zad();
            this.zaj.signalAll();
            return;
        }
        finally {
            this.zai.unlock();
        }
    }

    final void zal(zabg zabg2) {
        zabg2 = this.zam.obtainMessage(1, zabg2);
        this.zam.sendMessage((Message)zabg2);
    }

    final void zam(RuntimeException runtimeException) {
        runtimeException = this.zam.obtainMessage(2, runtimeException);
        this.zam.sendMessage((Message)runtimeException);
    }

    @Override
    public final void zaq() {
        this.zan.zae();
    }

    @Override
    public final void zar() {
        if (this.zan.zaj()) {
            this.zab.clear();
        }
    }

    @Override
    public final void zas(String string2, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] stringArray) {
        String string3 = String.valueOf(string2).concat("  ");
        printWriter.append(string2).append("mState=").println(this.zan);
        for (Api<?> api : this.zad.keySet()) {
            printWriter.append(string2).append(api.zad()).println(":");
            Preconditions.checkNotNull(this.zaa.get(api.zab())).dump(string3, fileDescriptor, printWriter, stringArray);
        }
    }

    @Override
    public final void zat() {
        if (this.zan instanceof zaaj) {
            ((zaaj)this.zan).zaf();
        }
    }

    @Override
    public final void zau() {
    }

    @Override
    public final boolean zaw() {
        return this.zan instanceof zaaj;
    }

    @Override
    public final boolean zax() {
        return this.zan instanceof zaaw;
    }

    @Override
    public final boolean zay(SignInConnectionListener signInConnectionListener) {
        return false;
    }
}

