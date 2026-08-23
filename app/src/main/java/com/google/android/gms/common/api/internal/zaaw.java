/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Bundle
 *  android.os.Looper
 *  android.util.Log
 */
package com.google.android.gms.common.api.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.internal.BaseImplementation;
import com.google.android.gms.common.api.internal.zaak;
import com.google.android.gms.common.api.internal.zaal;
import com.google.android.gms.common.api.internal.zaao;
import com.google.android.gms.common.api.internal.zaap;
import com.google.android.gms.common.api.internal.zaat;
import com.google.android.gms.common.api.internal.zabf;
import com.google.android.gms.common.api.internal.zabi;
import com.google.android.gms.common.api.internal.zabj;
import com.google.android.gms.common.internal.ClientSettings;
import com.google.android.gms.common.internal.IAccountAccessor;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.zab;
import com.google.android.gms.common.internal.zav;
import com.google.android.gms.signin.SignInOptions;
import com.google.android.gms.signin.internal.zak;
import com.google.android.gms.signin.zae;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;

public final class zaaw
implements zabf {
    private final zabi zaa;
    private final Lock zab;
    private final Context zac;
    private final GoogleApiAvailabilityLight zad;
    private ConnectionResult zae;
    private int zaf;
    private int zag = 0;
    private int zah;
    private final Bundle zai = new Bundle();
    private final Set<Api.AnyClientKey> zaj = new HashSet<Api.AnyClientKey>();
    private zae zak;
    private boolean zal;
    private boolean zam;
    private boolean zan;
    private IAccountAccessor zao;
    private boolean zap;
    private boolean zaq;
    private final ClientSettings zar;
    private final Map<Api<?>, Boolean> zas;
    private final Api.AbstractClientBuilder<? extends zae, SignInOptions> zat;
    private final ArrayList<Future<?>> zau = new ArrayList();

    public zaaw(zabi zabi2, ClientSettings clientSettings, Map<Api<?>, Boolean> map, GoogleApiAvailabilityLight googleApiAvailabilityLight, Api.AbstractClientBuilder<? extends zae, SignInOptions> abstractClientBuilder, Lock lock, Context context) {
        this.zaa = zabi2;
        this.zar = clientSettings;
        this.zas = map;
        this.zad = googleApiAvailabilityLight;
        this.zat = abstractClientBuilder;
        this.zab = lock;
        this.zac = context;
    }

    private final void zaA() {
        this.zam = false;
        this.zaa.zag.zad = Collections.emptySet();
        for (Api.AnyClientKey anyClientKey : this.zaj) {
            if (this.zaa.zab.containsKey(anyClientKey)) continue;
            this.zaa.zab.put(anyClientKey, new ConnectionResult(17, null));
        }
    }

    private final void zaB(boolean bl) {
        Object object = this.zak;
        if (object != null) {
            if (object.isConnected() && bl) {
                object.zaa();
            }
            object.disconnect();
            object = Preconditions.checkNotNull(this.zar);
            this.zao = null;
        }
    }

    /*
     * WARNING - void declaration
     */
    private final void zaC() {
        void var1_6;
        this.zaa.zai();
        zabj.zaa().execute(new zaak(this));
        zae object2 = this.zak;
        if (object2 != null) {
            if (this.zap) {
                object2.zac(Preconditions.checkNotNull(this.zao), this.zaq);
            }
            this.zaB(false);
        }
        for (Api.AnyClientKey<?> anyClientKey : this.zaa.zab.keySet()) {
            Preconditions.checkNotNull(this.zaa.zaa.get(anyClientKey)).disconnect();
        }
        if (this.zai.isEmpty()) {
            Object var1_4 = null;
        } else {
            Bundle bundle = this.zai;
        }
        this.zaa.zah.zab((Bundle)var1_6);
    }

    private final void zaD(ConnectionResult connectionResult) {
        this.zaz();
        this.zaB(connectionResult.hasResolution() ^ true);
        this.zaa.zak(connectionResult);
        this.zaa.zah.zaa(connectionResult);
    }

    private final void zaE(ConnectionResult connectionResult, Api<?> api, boolean bl) {
        int n = api.zac().getPriority();
        if (!(bl && !connectionResult.hasResolution() && this.zad.getErrorResolutionIntent(connectionResult.getErrorCode()) == null || this.zae != null && n >= this.zaf)) {
            this.zae = connectionResult;
            this.zaf = n;
        }
        this.zaa.zab.put(api.zab(), connectionResult);
    }

    private final void zaF() {
        if (this.zah != 0) {
            return;
        }
        if (!this.zam || this.zan) {
            ArrayList<Api.Client> arrayList = new ArrayList<Api.Client>();
            this.zag = 1;
            this.zah = this.zaa.zaa.size();
            for (Api.AnyClientKey<?> anyClientKey : this.zaa.zaa.keySet()) {
                if (this.zaa.zab.containsKey(anyClientKey)) {
                    if (!this.zaH()) continue;
                    this.zaC();
                    continue;
                }
                arrayList.add(this.zaa.zaa.get(anyClientKey));
            }
            if (!arrayList.isEmpty()) {
                this.zau.add(zabj.zaa().submit(new zaap(this, arrayList)));
            }
        }
    }

    private final boolean zaG(int n) {
        if (this.zag != n) {
            Log.w((String)"GACConnecting", (String)this.zaa.zag.zaf());
            Log.w((String)"GACConnecting", (String)"Unexpected callback in ".concat(this.toString()));
            int n2 = this.zah;
            StringBuilder stringBuilder = new StringBuilder(33);
            stringBuilder.append("mRemainingConnections=");
            stringBuilder.append(n2);
            Log.w((String)"GACConnecting", (String)stringBuilder.toString());
            Object object = zaaw.zaJ(this.zag);
            String string2 = zaaw.zaJ(n);
            stringBuilder = new StringBuilder(((String)object).length() + 70 + string2.length());
            stringBuilder.append("GoogleApiClient connecting is in step ");
            stringBuilder.append((String)object);
            stringBuilder.append(" but received callback for step ");
            stringBuilder.append(string2);
            object = new Exception();
            Log.e((String)"GACConnecting", (String)stringBuilder.toString(), (Throwable)object);
            this.zaD(new ConnectionResult(8, null));
            return false;
        }
        return true;
    }

    private final boolean zaH() {
        int n;
        this.zah = n = this.zah - 1;
        if (n > 0) {
            return false;
        }
        if (n < 0) {
            Log.w((String)"GACConnecting", (String)this.zaa.zag.zaf());
            Log.wtf((String)"GACConnecting", (String)"GoogleApiClient received too many callbacks for the given step. Clients may be in an unexpected state; GoogleApiClient will now disconnect.", (Throwable)new Exception());
            this.zaD(new ConnectionResult(8, null));
            return false;
        }
        ConnectionResult connectionResult = this.zae;
        if (connectionResult != null) {
            this.zaa.zaf = this.zaf;
            this.zaD(connectionResult);
            return false;
        }
        return true;
    }

    private final boolean zaI(ConnectionResult connectionResult) {
        return this.zal && !connectionResult.hasResolution();
    }

    private static final String zaJ(int n) {
        switch (n) {
            default: {
                return "STEP_GETTING_REMOTE_SERVICE";
            }
            case 0: 
        }
        return "STEP_SERVICE_BINDINGS_AND_SIGN_IN";
    }

    static /* bridge */ /* synthetic */ Context zac(zaaw zaaw2) {
        return zaaw2.zac;
    }

    static /* bridge */ /* synthetic */ GoogleApiAvailabilityLight zaf(zaaw zaaw2) {
        return zaaw2.zad;
    }

    static /* bridge */ /* synthetic */ zabi zak(zaaw zaaw2) {
        return zaaw2.zaa;
    }

    static /* bridge */ /* synthetic */ ClientSettings zal(zaaw zaaw2) {
        return zaaw2.zar;
    }

    static /* bridge */ /* synthetic */ IAccountAccessor zam(zaaw zaaw2) {
        return zaaw2.zao;
    }

    static /* bridge */ /* synthetic */ zae zan(zaaw zaaw2) {
        return zaaw2.zak;
    }

    static /* bridge */ /* synthetic */ Set zao(zaaw object) {
        Object object2 = ((zaaw)object).zar;
        if (object2 == null) {
            object = Collections.emptySet();
        } else {
            object2 = new HashSet<Scope>(((ClientSettings)object2).getRequiredScopes());
            Map<Api<?>, zab> map = ((zaaw)object).zar.zad();
            for (Api<?> api : map.keySet()) {
                if (((zaaw)object).zaa.zab.containsKey(api.zab())) continue;
                object2.addAll(map.get(api).zaa);
            }
            object = object2;
        }
        return object;
    }

    static /* bridge */ /* synthetic */ Lock zap(zaaw zaaw2) {
        return zaaw2.zab;
    }

    static /* bridge */ /* synthetic */ void zaq(zaaw zaaw2) {
        zaaw2.zaA();
    }

    static /* bridge */ /* synthetic */ void zar(zaaw zaaw2, zak abstractSafeParcelable) {
        if (!zaaw2.zaG(0)) {
            return;
        }
        Object object = ((zak)abstractSafeParcelable).zaa();
        if (((ConnectionResult)object).isSuccess()) {
            object = Preconditions.checkNotNull(((zak)abstractSafeParcelable).zab());
            if (!((ConnectionResult)(abstractSafeParcelable = ((zav)object).zaa())).isSuccess()) {
                object = String.valueOf(abstractSafeParcelable);
                String.valueOf(object).length();
                Exception exception = new Exception();
                Log.wtf((String)"GACConnecting", (String)"Sign-in succeeded with resolve account failure: ".concat(String.valueOf(object)), (Throwable)exception);
                zaaw2.zaD((ConnectionResult)abstractSafeParcelable);
                return;
            }
            zaaw2.zan = true;
            zaaw2.zao = Preconditions.checkNotNull(((zav)object).zab());
            zaaw2.zap = ((zav)object).zac();
            zaaw2.zaq = ((zav)object).zad();
            zaaw2.zaF();
            return;
        }
        if (zaaw2.zaI((ConnectionResult)object)) {
            zaaw2.zaA();
            zaaw2.zaF();
            return;
        }
        zaaw2.zaD((ConnectionResult)object);
    }

    static /* bridge */ /* synthetic */ void zas(zaaw zaaw2, ConnectionResult connectionResult) {
        zaaw2.zaD(connectionResult);
    }

    static /* bridge */ /* synthetic */ void zat(zaaw zaaw2, ConnectionResult connectionResult, Api api, boolean bl) {
        zaaw2.zaE(connectionResult, api, bl);
    }

    static /* bridge */ /* synthetic */ void zau(zaaw zaaw2) {
        zaaw2.zaF();
    }

    static /* bridge */ /* synthetic */ boolean zav(zaaw zaaw2) {
        return zaaw2.zam;
    }

    static /* bridge */ /* synthetic */ boolean zaw(zaaw zaaw2, int n) {
        return zaaw2.zaG(0);
    }

    static /* bridge */ /* synthetic */ boolean zax(zaaw zaaw2) {
        return zaaw2.zaH();
    }

    static /* bridge */ /* synthetic */ boolean zay(zaaw zaaw2, ConnectionResult connectionResult) {
        return zaaw2.zaI(connectionResult);
    }

    private final void zaz() {
        ArrayList<Future<?>> arrayList = this.zau;
        int n = arrayList.size();
        for (int i = 0; i < n; ++i) {
            ((Future)arrayList.get(i)).cancel(true);
        }
        this.zau.clear();
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
        Object object;
        Looper looper;
        this.zaa.zab.clear();
        this.zam = false;
        this.zae = null;
        this.zag = 0;
        this.zal = true;
        this.zan = false;
        this.zap = false;
        HashMap<Object, zaal> hashMap = new HashMap<Object, zaal>();
        Context context = this.zas.keySet().iterator();
        boolean bl = false;
        while (context.hasNext()) {
            looper = context.next();
            object = Preconditions.checkNotNull(this.zaa.zaa.get(looper.zab()));
            boolean bl2 = looper.zac().getPriority() == 1;
            bl |= bl2;
            boolean bl3 = this.zas.get(looper);
            if (object.requiresSignIn()) {
                this.zam = true;
                if (bl3) {
                    this.zaj.add(looper.zab());
                } else {
                    this.zal = false;
                }
            }
            hashMap.put(object, new zaal(this, (Api<?>)looper, bl3));
        }
        if (bl) {
            this.zam = false;
        }
        if (this.zam) {
            Preconditions.checkNotNull(this.zar);
            Preconditions.checkNotNull(this.zat);
            this.zar.zae(System.identityHashCode(this.zaa.zag));
            object = new zaat(this, null);
            Api.AbstractClientBuilder<? extends zae, SignInOptions> abstractClientBuilder = this.zat;
            context = this.zac;
            looper = ((GoogleApiClient)this.zaa.zag).getLooper();
            ClientSettings clientSettings = this.zar;
            this.zak = abstractClientBuilder.buildClient(context, looper, clientSettings, clientSettings.zaa(), (GoogleApiClient.ConnectionCallbacks)object, (GoogleApiClient.OnConnectionFailedListener)object);
        }
        this.zah = this.zaa.zaa.size();
        this.zau.add(zabj.zaa().submit(new zaao(this, hashMap)));
    }

    @Override
    public final void zae() {
    }

    @Override
    public final void zag(Bundle bundle) {
        if (!this.zaG(1)) {
            return;
        }
        if (bundle != null) {
            this.zai.putAll(bundle);
        }
        if (this.zaH()) {
            this.zaC();
        }
    }

    @Override
    public final void zah(ConnectionResult connectionResult, Api<?> api, boolean bl) {
        if (!this.zaG(1)) {
            return;
        }
        this.zaE(connectionResult, api, bl);
        if (this.zaH()) {
            this.zaC();
        }
    }

    @Override
    public final void zai(int n) {
        this.zaD(new ConnectionResult(8, null));
    }

    @Override
    public final boolean zaj() {
        this.zaz();
        this.zaB(true);
        this.zaa.zak(null);
        return true;
    }
}

