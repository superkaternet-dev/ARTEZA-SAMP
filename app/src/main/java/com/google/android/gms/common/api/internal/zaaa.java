/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.PendingIntent
 *  android.content.Context
 *  android.os.Bundle
 *  android.os.Looper
 *  android.util.Log
 */
package com.google.android.gms.common.api.internal;

import android.app.PendingIntent;
import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import androidx.collection.ArrayMap;
import androidx.collection.SimpleArrayMap;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.BaseImplementation;
import com.google.android.gms.common.api.internal.SignInConnectionListener;
import com.google.android.gms.common.api.internal.zabe;
import com.google.android.gms.common.api.internal.zabi;
import com.google.android.gms.common.api.internal.zaca;
import com.google.android.gms.common.api.internal.zat;
import com.google.android.gms.common.api.internal.zav;
import com.google.android.gms.common.api.internal.zax;
import com.google.android.gms.common.api.internal.zaz;
import com.google.android.gms.common.internal.ClientSettings;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.base.zal;
import com.google.android.gms.internal.base.zaq;
import com.google.android.gms.signin.SignInOptions;
import com.google.android.gms.signin.zae;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

final class zaaa
implements zaca {
    private final Context zaa;
    private final zabe zab;
    private final Looper zac;
    private final zabi zad;
    private final zabi zae;
    private final Map<Api.AnyClientKey<?>, zabi> zaf;
    private final Set<SignInConnectionListener> zag = Collections.newSetFromMap(new WeakHashMap());
    private final Api.Client zah;
    private Bundle zai;
    private ConnectionResult zaj = null;
    private ConnectionResult zak = null;
    private boolean zal = false;
    private final Lock zam;
    private int zan = 0;

    private zaaa(Context object, zabe iterator2, Lock lock, Looper looper, GoogleApiAvailabilityLight googleApiAvailabilityLight, Map<Api.AnyClientKey<?>, Api.Client> map, Map<Api.AnyClientKey<?>, Api.Client> map2, ClientSettings clientSettings, Api.AbstractClientBuilder<? extends zae, SignInOptions> abstractClientBuilder, Api.Client client, ArrayList<zat> arrayList, ArrayList<zat> arrayList2, Map<Api<?>, Boolean> map3, Map<Api<?>, Boolean> map4) {
        this.zaa = object;
        this.zab = iterator2;
        this.zam = lock;
        this.zac = looper;
        this.zah = client;
        this.zad = new zabi((Context)object, (zabe)((Object)iterator2), lock, looper, googleApiAvailabilityLight, map2, null, map4, null, arrayList2, new zax(this, null));
        this.zae = new zabi((Context)object, (zabe)((Object)iterator2), lock, looper, googleApiAvailabilityLight, map, clientSettings, map3, abstractClientBuilder, arrayList, new zaz(this, null));
        object = new ArrayMap();
        iterator2 = map2.keySet().iterator();
        while (iterator2.hasNext()) {
            ((SimpleArrayMap)object).put((Api.AnyClientKey)iterator2.next(), this.zad);
        }
        iterator2 = map.keySet().iterator();
        while (iterator2.hasNext()) {
            ((SimpleArrayMap)object).put(iterator2.next(), this.zae);
        }
        this.zaf = Collections.unmodifiableMap(object);
    }

    private final void zaA(ConnectionResult connectionResult) {
        switch (this.zan) {
            default: {
                Log.wtf((String)"CompositeGAC", (String)"Attempted to call failure callbacks in CONNECTION_MODE_NONE. Callbacks should be disabled via GmsClientSupervisor", (Throwable)new Exception());
                break;
            }
            case 2: {
                this.zab.zaa(connectionResult);
            }
            case 1: {
                this.zaB();
            }
        }
        this.zan = 0;
    }

    private final void zaB() {
        Iterator<SignInConnectionListener> iterator2 = this.zag.iterator();
        while (iterator2.hasNext()) {
            iterator2.next().onComplete();
        }
        this.zag.clear();
    }

    private final boolean zaC() {
        ConnectionResult connectionResult = this.zak;
        return connectionResult != null && connectionResult.getErrorCode() == 4;
    }

    private final boolean zaD(BaseImplementation.ApiMethodImpl<? extends Result, ? extends Api.AnyClient> object) {
        object = ((BaseImplementation.ApiMethodImpl)object).getClientKey();
        object = this.zaf.get(object);
        Preconditions.checkNotNull(object, "GoogleApiClient is not configured to use the API required for this call.");
        return object.equals(this.zae);
    }

    private static boolean zaE(ConnectionResult connectionResult) {
        return connectionResult != null && connectionResult.isSuccess();
    }

    static /* bridge */ /* synthetic */ ConnectionResult zaa(zaaa zaaa2) {
        return zaaa2.zak;
    }

    public static zaaa zag(Context context, zabe zabe2, Lock lock, Looper looper, GoogleApiAvailabilityLight googleApiAvailabilityLight, Map<Api.AnyClientKey<?>, Api.Client> object, ClientSettings clientSettings, Map<Api<?>, Boolean> object2, Api.AbstractClientBuilder<? extends zae, SignInOptions> abstractClientBuilder, ArrayList<zat> arrayList) {
        Api.AnyClientKey<?> anyClientKey;
        Object object3;
        Api<?> api2;
        ArrayMap arrayMap = new ArrayMap();
        ArrayMap arrayMap2 = new ArrayMap();
        Object object4 = object.entrySet().iterator();
        object = null;
        while (object4.hasNext()) {
            api2 = object4.next();
            object3 = api2.getValue();
            if (object3.providesSignIn()) {
                object = object3;
            }
            if (object3.requiresSignIn()) {
                arrayMap.put(api2.getKey(), (Api.Client)object3);
                continue;
            }
            arrayMap2.put(api2.getKey(), (Api.Client)object3);
        }
        Preconditions.checkState(arrayMap.isEmpty() ^ true, "CompositeGoogleApiClient should not be used without any APIs that require sign-in.");
        object3 = new ArrayMap();
        object4 = new ArrayMap();
        for (Api<?> api2 : object2.keySet()) {
            anyClientKey = api2.zab();
            if (arrayMap.containsKey(anyClientKey)) {
                object3.put(api2, (Boolean)object2.get(api2));
                continue;
            }
            if (arrayMap2.containsKey(anyClientKey)) {
                object4.put(api2, (Boolean)object2.get(api2));
                continue;
            }
            throw new IllegalStateException("Each API in the isOptionalMap must have a corresponding client in the clients map.");
        }
        api2 = new ArrayList<zat>();
        object2 = new ArrayList();
        int n = arrayList.size();
        for (int i = 0; i < n; ++i) {
            anyClientKey = (zat)arrayList.get(i);
            if (object3.containsKey(((zat)((Object)anyClientKey)).zaa)) {
                ((ArrayList)((Object)api2)).add(anyClientKey);
                continue;
            }
            if (object4.containsKey(((zat)((Object)anyClientKey)).zaa)) {
                ((ArrayList)object2).add(anyClientKey);
                continue;
            }
            throw new IllegalStateException("Each ClientCallbacks must have a corresponding API in the isOptionalMap");
        }
        return new zaaa(context, zabe2, lock, looper, googleApiAvailabilityLight, arrayMap, arrayMap2, clientSettings, abstractClientBuilder, (Api.Client)object, (ArrayList<zat>)((Object)api2), (ArrayList<zat>)object2, (Map<Api<?>, Boolean>)object3, (Map<Api<?>, Boolean>)object4);
    }

    static /* bridge */ /* synthetic */ zabi zah(zaaa zaaa2) {
        return zaaa2.zad;
    }

    static /* bridge */ /* synthetic */ zabi zai(zaaa zaaa2) {
        return zaaa2.zae;
    }

    static /* bridge */ /* synthetic */ Lock zaj(zaaa zaaa2) {
        return zaaa2.zam;
    }

    static /* bridge */ /* synthetic */ void zak(zaaa zaaa2, ConnectionResult connectionResult) {
        zaaa2.zaj = connectionResult;
    }

    static /* bridge */ /* synthetic */ void zal(zaaa zaaa2, ConnectionResult connectionResult) {
        zaaa2.zak = connectionResult;
    }

    static /* bridge */ /* synthetic */ void zam(zaaa zaaa2, boolean bl) {
        zaaa2.zal = bl;
    }

    static /* bridge */ /* synthetic */ void zan(zaaa zaaa2, int n, boolean bl) {
        zaaa2.zab.zac(n, bl);
        zaaa2.zak = null;
        zaaa2.zaj = null;
    }

    static /* bridge */ /* synthetic */ void zao(zaaa zaaa2, Bundle bundle) {
        Bundle bundle2 = zaaa2.zai;
        if (bundle2 == null) {
            zaaa2.zai = bundle;
            return;
        }
        if (bundle != null) {
            bundle2.putAll(bundle);
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    static /* bridge */ /* synthetic */ void zap(zaaa zaaa2) {
        ConnectionResult connectionResult;
        if (zaaa.zaE(zaaa2.zaj)) {
            if (!zaaa.zaE(zaaa2.zak) && !zaaa2.zaC()) {
                ConnectionResult connectionResult2 = zaaa2.zak;
                if (connectionResult2 == null) return;
                if (zaaa2.zan == 1) {
                    zaaa2.zaB();
                    return;
                }
                zaaa2.zaA(connectionResult2);
                zaaa2.zad.zar();
                return;
            }
            switch (zaaa2.zan) {
                default: {
                    Log.wtf((String)"CompositeGAC", (String)"Attempted to call success callbacks in CONNECTION_MODE_NONE. Callbacks should be disabled via GmsClientSupervisor", (Throwable)((Object)new AssertionError()));
                    break;
                }
                case 2: {
                    Preconditions.checkNotNull(zaaa2.zab).zab(zaaa2.zai);
                }
                case 1: {
                    zaaa2.zaB();
                }
            }
            zaaa2.zan = 0;
            return;
        }
        if (zaaa2.zaj != null && zaaa.zaE(zaaa2.zak)) {
            zaaa2.zae.zar();
            zaaa2.zaA(Preconditions.checkNotNull(zaaa2.zaj));
            return;
        }
        ConnectionResult connectionResult3 = zaaa2.zaj;
        if (connectionResult3 == null || (connectionResult = zaaa2.zak) == null) return;
        if (zaaa2.zae.zaf < zaaa2.zad.zaf) {
            connectionResult3 = connectionResult;
        }
        zaaa2.zaA(connectionResult3);
    }

    static /* bridge */ /* synthetic */ boolean zav(zaaa zaaa2) {
        return zaaa2.zal;
    }

    private final PendingIntent zaz() {
        if (this.zah == null) {
            return null;
        }
        return com.google.android.gms.internal.base.zal.zaa(this.zaa, System.identityHashCode(this.zab), this.zah.getSignInIntent(), com.google.android.gms.internal.base.zal.zaa | 0x8000000);
    }

    @Override
    public final ConnectionResult zab() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final ConnectionResult zac(long l, TimeUnit timeUnit) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final ConnectionResult zad(Api<?> api) {
        if (Objects.equal(this.zaf.get(api.zab()), this.zae)) {
            if (this.zaC()) {
                return new ConnectionResult(4, this.zaz());
            }
            return this.zae.zad(api);
        }
        return this.zad.zad(api);
    }

    @Override
    public final <A extends Api.AnyClient, R extends Result, T extends BaseImplementation.ApiMethodImpl<R, A>> T zae(T t) {
        if (this.zaD(t)) {
            if (this.zaC()) {
                t.setFailedResult(new Status(4, null, this.zaz()));
                return t;
            }
            this.zae.zae(t);
            return t;
        }
        this.zad.zae(t);
        return t;
    }

    @Override
    public final <A extends Api.AnyClient, T extends BaseImplementation.ApiMethodImpl<? extends Result, A>> T zaf(T t) {
        if (this.zaD(t)) {
            if (this.zaC()) {
                t.setFailedResult(new Status(4, null, this.zaz()));
                return t;
            }
            return this.zae.zaf(t);
        }
        return this.zad.zaf(t);
    }

    @Override
    public final void zaq() {
        this.zan = 2;
        this.zal = false;
        this.zak = null;
        this.zaj = null;
        this.zad.zaq();
        this.zae.zaq();
    }

    @Override
    public final void zar() {
        this.zak = null;
        this.zaj = null;
        this.zan = 0;
        this.zad.zar();
        this.zae.zar();
        this.zaB();
    }

    @Override
    public final void zas(String string2, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] stringArray) {
        printWriter.append(string2).append("authClient").println(":");
        this.zae.zas(String.valueOf(string2).concat("  "), fileDescriptor, printWriter, stringArray);
        printWriter.append(string2).append("anonClient").println(":");
        this.zad.zas(String.valueOf(string2).concat("  "), fileDescriptor, printWriter, stringArray);
    }

    @Override
    public final void zat() {
        this.zad.zat();
        this.zae.zat();
    }

    @Override
    public final void zau() {
        this.zam.lock();
        try {
            boolean bl = this.zax();
            this.zae.zar();
            Object object = new ConnectionResult(4);
            this.zak = object;
            if (bl) {
                zaq zaq2 = new zaq(this.zac);
                object = new zav(this);
                zaq2.post((Runnable)object);
            } else {
                this.zaB();
            }
            return;
        }
        finally {
            this.zam.unlock();
        }
    }

    @Override
    public final boolean zaw() {
        boolean bl;
        block5: {
            this.zam.lock();
            try {
                boolean bl2 = this.zad.zaw();
                bl = false;
                if (!bl2) break block5;
            }
            catch (Throwable throwable) {
                this.zam.unlock();
                throw throwable;
            }
            if (!this.zae.zaw() && !this.zaC()) {
                int n = this.zan;
                if (n == 1) {
                    bl = true;
                }
                break block5;
            }
            bl = true;
        }
        this.zam.unlock();
        return bl;
    }

    @Override
    public final boolean zax() {
        this.zam.lock();
        try {
            int n = this.zan;
            boolean bl = n == 2;
            this.zam.unlock();
            return bl;
        }
        catch (Throwable throwable) {
            this.zam.unlock();
            throw throwable;
        }
    }

    @Override
    public final boolean zay(SignInConnectionListener signInConnectionListener) {
        this.zam.lock();
        try {
            if ((this.zax() || this.zaw()) && !this.zae.zaw()) {
                this.zag.add(signInConnectionListener);
                if (this.zan == 0) {
                    this.zan = 1;
                }
                this.zak = null;
                this.zae.zaq();
                return true;
            }
            return false;
        }
        finally {
            this.zam.unlock();
        }
    }
}

