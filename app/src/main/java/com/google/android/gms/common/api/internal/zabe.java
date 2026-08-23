/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.os.Bundle
 *  android.os.Looper
 *  android.util.Log
 */
package com.google.android.gms.common.api.internal;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.BaseImplementation;
import com.google.android.gms.common.api.internal.BasePendingResult;
import com.google.android.gms.common.api.internal.LifecycleActivity;
import com.google.android.gms.common.api.internal.ListenerHolder;
import com.google.android.gms.common.api.internal.ListenerHolders;
import com.google.android.gms.common.api.internal.SignInConnectionListener;
import com.google.android.gms.common.api.internal.StatusPendingResult;
import com.google.android.gms.common.api.internal.zaaa;
import com.google.android.gms.common.api.internal.zaay;
import com.google.android.gms.common.api.internal.zaaz;
import com.google.android.gms.common.api.internal.zaba;
import com.google.android.gms.common.api.internal.zabb;
import com.google.android.gms.common.api.internal.zabc;
import com.google.android.gms.common.api.internal.zabd;
import com.google.android.gms.common.api.internal.zabi;
import com.google.android.gms.common.api.internal.zabx;
import com.google.android.gms.common.api.internal.zabz;
import com.google.android.gms.common.api.internal.zaca;
import com.google.android.gms.common.api.internal.zada;
import com.google.android.gms.common.api.internal.zadc;
import com.google.android.gms.common.api.internal.zak;
import com.google.android.gms.common.api.internal.zat;
import com.google.android.gms.common.internal.ClientSettings;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.service.Common;
import com.google.android.gms.common.internal.zaj;
import com.google.android.gms.common.util.ClientLibraryUtils;
import com.google.android.gms.signin.SignInOptions;
import com.google.android.gms.signin.zae;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;

public final class zabe
extends GoogleApiClient
implements zabz {
    final Queue<BaseImplementation.ApiMethodImpl<?, ?>> zaa = new LinkedList();
    zabx zab;
    final Map<Api.AnyClientKey<?>, Api.Client> zac;
    Set<Scope> zad;
    final ClientSettings zae;
    final Map<Api<?>, Boolean> zaf;
    final Api.AbstractClientBuilder<? extends zae, SignInOptions> zag;
    Set<zada> zah;
    final zadc zai;
    private final Lock zaj;
    private final com.google.android.gms.common.internal.zak zak;
    private zaca zal = null;
    private final int zam;
    private final Context zan;
    private final Looper zao;
    private volatile boolean zap;
    private long zaq;
    private long zar;
    private final zabc zas;
    private final GoogleApiAvailability zat;
    private final ListenerHolders zau;
    private final ArrayList<zat> zav;
    private Integer zaw;
    private final zaj zax;

    /*
     * WARNING - void declaration
     */
    public zabe(Context object2, Lock iterator2, Looper looper, ClientSettings clientSettings, GoogleApiAvailability googleApiAvailability, Api.AbstractClientBuilder<? extends zae, SignInOptions> abstractClientBuilder, Map<Api<?>, Boolean> map, List<GoogleApiClient.ConnectionCallbacks> list, List<GoogleApiClient.OnConnectionFailedListener> list2, Map<Api.AnyClientKey<?>, Api.Client> map2, int n, int n2, ArrayList<zat> arrayList) {
        void var6_10;
        void var4_8;
        void var9_13;
        void var8_12;
        void var13_17;
        void var10_14;
        void var7_11;
        void var11_15;
        void var5_9;
        void var3_7;
        Iterator iterator3;
        long l = true != ClientLibraryUtils.isPackageSide() ? 120000L : 10000L;
        this.zaq = l;
        this.zar = 5000L;
        this.zad = new HashSet<Scope>();
        this.zau = new ListenerHolders();
        this.zaw = null;
        this.zah = null;
        zaay zaay2 = new zaay(this);
        this.zax = zaay2;
        this.zan = object2;
        this.zaj = iterator3;
        this.zak = new com.google.android.gms.common.internal.zak((Looper)var3_7, zaay2);
        this.zao = var3_7;
        this.zas = new zabc(this, (Looper)var3_7);
        this.zat = var5_9;
        this.zam = var11_15;
        if (var11_15 >= 0) {
            void var12_16;
            this.zaw = (int)var12_16;
        }
        this.zaf = var7_11;
        this.zac = var10_14;
        this.zav = var13_17;
        this.zai = new zadc();
        for (GoogleApiClient.ConnectionCallbacks connectionCallbacks : var8_12) {
            this.zak.zaf(connectionCallbacks);
        }
        for (GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener : var9_13) {
            this.zak.zag(onConnectionFailedListener);
        }
        this.zae = var4_8;
        this.zag = var6_10;
    }

    public static int zad(Iterable<Api.Client> object, boolean bl) {
        object = object.iterator();
        boolean bl2 = false;
        boolean bl3 = false;
        while (object.hasNext()) {
            Api.Client client = (Api.Client)object.next();
            bl2 |= client.requiresSignIn();
            bl3 |= client.providesSignIn();
        }
        if (bl2) {
            if (bl3 && bl) {
                return 2;
            }
            return 1;
        }
        return 3;
    }

    static /* bridge */ /* synthetic */ Context zae(zabe zabe2) {
        return zabe2.zan;
    }

    static String zag(int n) {
        switch (n) {
            default: {
                return "UNKNOWN";
            }
            case 3: {
                return "SIGN_IN_MODE_NONE";
            }
            case 2: {
                return "SIGN_IN_MODE_OPTIONAL";
            }
            case 1: 
        }
        return "SIGN_IN_MODE_REQUIRED";
    }

    static /* bridge */ /* synthetic */ void zah(zabe zabe2, GoogleApiClient googleApiClient, StatusPendingResult statusPendingResult, boolean bl) {
        zabe2.zam(googleApiClient, statusPendingResult, true);
    }

    static /* bridge */ /* synthetic */ void zai(zabe zabe2) {
        zabe2.zaj.lock();
        try {
            if (zabe2.zap) {
                zabe2.zan();
            }
            return;
        }
        finally {
            zabe2.zaj.unlock();
        }
    }

    static /* bridge */ /* synthetic */ void zaj(zabe zabe2) {
        zabe2.zaj.lock();
        try {
            if (zabe2.zak()) {
                zabe2.zan();
            }
            return;
        }
        finally {
            zabe2.zaj.unlock();
        }
    }

    private final void zal(int n) {
        Object object;
        block12: {
            block11: {
                block10: {
                    object = this.zaw;
                    if (object != null) break block10;
                    this.zaw = n;
                    break block11;
                }
                if ((Integer)object != n) break block12;
            }
            if (this.zal != null) {
                return;
            }
            object = this.zac.values().iterator();
            boolean bl = false;
            n = 0;
            while (object.hasNext()) {
                Api.Client client = (Api.Client)object.next();
                bl |= client.requiresSignIn();
                n |= client.providesSignIn();
            }
            switch (this.zaw) {
                default: {
                    break;
                }
                case 2: {
                    if (!bl) break;
                    this.zal = zaaa.zag(this.zan, this, this.zaj, this.zao, this.zat, this.zac, this.zae, this.zaf, this.zag, this.zav);
                    return;
                }
                case 1: {
                    if (bl) {
                        if (n == 0) break;
                        throw new IllegalStateException("Cannot use SIGN_IN_MODE_REQUIRED with GOOGLE_SIGN_IN_API. Use connect(SIGN_IN_MODE_OPTIONAL) instead.");
                    }
                    throw new IllegalStateException("SIGN_IN_MODE_REQUIRED cannot be used on a GoogleApiClient that does not contain any authenticated APIs. Use connect() instead.");
                }
            }
            this.zal = new zabi(this.zan, this, this.zaj, this.zao, this.zat, this.zac, this.zae, this.zaf, this.zag, this.zav, this);
            return;
        }
        String string2 = zabe.zag(n);
        String string3 = zabe.zag(this.zaw);
        object = new StringBuilder(string2.length() + 51 + string3.length());
        ((StringBuilder)object).append("Cannot use sign-in mode: ");
        ((StringBuilder)object).append(string2);
        ((StringBuilder)object).append(". Mode was already set to ");
        ((StringBuilder)object).append(string3);
        object = new IllegalStateException(((StringBuilder)object).toString());
        throw object;
    }

    private final void zam(GoogleApiClient googleApiClient, StatusPendingResult statusPendingResult, boolean bl) {
        Common.zaa.zaa(googleApiClient).setResultCallback(new zabb(this, statusPendingResult, bl, googleApiClient));
    }

    private final void zan() {
        this.zak.zab();
        Preconditions.checkNotNull(this.zal).zaq();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public final ConnectionResult blockingConnect() {
        Looper looper = Looper.myLooper();
        Object object = Looper.getMainLooper();
        boolean bl = true;
        boolean bl2 = looper != object;
        Preconditions.checkState(bl2, "blockingConnect must not be called on the UI thread");
        this.zaj.lock();
        try {
            block9: {
                block7: {
                    block8: {
                        block6: {
                            if (this.zam < 0) break block6;
                            bl2 = this.zaw != null ? bl : false;
                            Preconditions.checkState(bl2, "Sign-in mode should have been set explicitly by auto-manage.");
                            break block7;
                        }
                        object = this.zaw;
                        if (object != null) break block8;
                        this.zaw = zabe.zad(this.zac.values(), false);
                        break block7;
                    }
                    if ((Integer)object == 2) break block9;
                }
                this.zal(Preconditions.checkNotNull(this.zaw));
                this.zak.zab();
                object = Preconditions.checkNotNull(this.zal).zab();
                return object;
            }
            object = new IllegalStateException("Cannot call blockingConnect() when sign-in mode is set to SIGN_IN_MODE_OPTIONAL. Call connect(SIGN_IN_MODE_OPTIONAL) instead.");
            throw object;
        }
        finally {
            this.zaj.unlock();
        }
    }

    @Override
    public final ConnectionResult blockingConnect(long l, TimeUnit object) {
        boolean bl = Looper.myLooper() != Looper.getMainLooper();
        Preconditions.checkState(bl, "blockingConnect must not be called on the UI thread");
        Preconditions.checkNotNull(object, "TimeUnit must not be null");
        this.zaj.lock();
        try {
            block9: {
                block8: {
                    Integer n;
                    block7: {
                        n = this.zaw;
                        if (n != null) break block7;
                        this.zaw = zabe.zad(this.zac.values(), false);
                        break block8;
                    }
                    if (n == 2) break block9;
                }
                this.zal(Preconditions.checkNotNull(this.zaw));
                this.zak.zab();
                object = Preconditions.checkNotNull(this.zal).zac(l, (TimeUnit)((Object)object));
                return object;
            }
            object = new IllegalStateException("Cannot call blockingConnect() when sign-in mode is set to SIGN_IN_MODE_OPTIONAL. Call connect(SIGN_IN_MODE_OPTIONAL) instead.");
            throw object;
        }
        finally {
            this.zaj.unlock();
        }
    }

    @Override
    public final PendingResult<Status> clearDefaultAccountAndReconnect() {
        Preconditions.checkState(this.isConnected(), "GoogleApiClient is not connected yet.");
        Serializable serializable = this.zaw;
        boolean bl = true;
        if (serializable != null && (Integer)serializable == 2) {
            bl = false;
        }
        Preconditions.checkState(bl, "Cannot use clearDefaultAccountAndReconnect with GOOGLE_SIGN_IN_API");
        StatusPendingResult statusPendingResult = new StatusPendingResult(this);
        if (this.zac.containsKey(Common.CLIENT_KEY)) {
            this.zam(this, statusPendingResult, false);
        } else {
            serializable = new AtomicReference();
            zaaz zaaz2 = new zaaz(this, (AtomicReference)serializable, statusPendingResult);
            zaba zaba2 = new zaba(this, statusPendingResult);
            Object object = new GoogleApiClient.Builder(this.zan);
            ((GoogleApiClient.Builder)object).addApi(Common.API);
            ((GoogleApiClient.Builder)object).addConnectionCallbacks(zaaz2);
            ((GoogleApiClient.Builder)object).addOnConnectionFailedListener(zaba2);
            ((GoogleApiClient.Builder)object).setHandler(this.zas);
            object = ((GoogleApiClient.Builder)object).build();
            ((AtomicReference)serializable).set(object);
            ((GoogleApiClient)object).connect();
        }
        return statusPendingResult;
    }

    /*
     * Unable to fully structure code
     */
    @Override
    public final void connect() {
        block13: {
            block14: {
                this.zaj.lock();
                var1_1 = this.zam;
                var2_2 = 2;
                var4_3 = false;
                if (var1_1 < 0) ** GOTO lbl15
                {
                    catch (Throwable var5_7) {
                        this.zaj.unlock();
                        throw var5_7;
                    }
                }
                var3_4 = this.zaw != null;
                Preconditions.checkState(var3_4, "Sign-in mode should have been set explicitly by auto-manage.");
                ** GOTO lbl21
lbl15:
                // 1 sources

                var5_5 = this.zaw;
                if (var5_5 != null) ** GOTO lbl20
                this.zaw = zabe.zad(this.zac.values(), false);
                break block14;
lbl20:
                // 1 sources

                if (var5_5.intValue() == 2) ** GOTO lbl-1000
            }
            var1_1 = Preconditions.checkNotNull(this.zaw);
            this.zaj.lock();
            if (var1_1 != 3 && var1_1 != 1) {
                if (var1_1 == 2) {
                    var3_4 = true;
                    var1_1 = var2_2;
                } else {
                    var3_4 = var4_3;
                }
                break block13;
            }
            var3_4 = true;
        }
        var5_5 = new StringBuilder(33);
        var5_5.append("Illegal sign-in mode: ");
        var5_5.append(var1_1);
        Preconditions.checkArgument(var3_4, var5_5.toString());
        this.zal(var1_1);
        this.zan();
        this.zaj.unlock();
        return;
lbl-1000:
        // 1 sources

        {
            var5_5 = new IllegalStateException("Cannot call connect() when SignInMode is set to SIGN_IN_MODE_OPTIONAL. Call connect(SIGN_IN_MODE_OPTIONAL) instead.");
            throw var5_5;
        }
        finally {
            this.zaj.unlock();
        }
    }

    @Override
    public final void connect(int n) {
        this.zaj.lock();
        boolean bl = true;
        if (n != 3 && n != 1) {
            if (n == 2) {
                n = 2;
            } else {
                bl = false;
            }
        }
        try {
            StringBuilder stringBuilder = new StringBuilder(33);
            stringBuilder.append("Illegal sign-in mode: ");
            stringBuilder.append(n);
            Preconditions.checkArgument(bl, stringBuilder.toString());
            this.zal(n);
            this.zan();
            return;
        }
        finally {
            this.zaj.unlock();
        }
    }

    /*
     * WARNING - void declaration
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public final void disconnect() {
        this.zaj.lock();
        try {
            void var1_6;
            this.zai.zab();
            zaca zaca2 = this.zal;
            if (zaca2 != null) {
                zaca2.zar();
            }
            this.zau.zab();
            for (BaseImplementation.ApiMethodImpl apiMethodImpl : this.zaa) {
                apiMethodImpl.zan(null);
                apiMethodImpl.cancel();
            }
            this.zaa.clear();
            zaca zaca3 = this.zal;
            if (zaca3 == null) {
                Lock lock = this.zaj;
            } else {
                this.zak();
                this.zak.zaa();
                Lock lock = this.zaj;
            }
            var1_6.unlock();
            return;
        }
        catch (Throwable throwable) {
            this.zaj.unlock();
            throw throwable;
        }
    }

    @Override
    public final void dump(String string2, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] stringArray) {
        printWriter.append(string2).append("mContext=").println(this.zan);
        printWriter.append(string2).append("mResuming=").print(this.zap);
        printWriter.append(" mWorkQueue.size()=").print(this.zaa.size());
        Object object = this.zai;
        printWriter.append(" mUnconsumedApiCalls.size()=").println(((zadc)object).zab.size());
        object = this.zal;
        if (object != null) {
            object.zas(string2, fileDescriptor, printWriter, stringArray);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public final <A extends Api.AnyClient, R extends Result, T extends BaseImplementation.ApiMethodImpl<R, A>> T enqueue(T object) {
        Object object2 = ((BaseImplementation.ApiMethodImpl)object).getApi();
        boolean bl = this.zac.containsKey(((BaseImplementation.ApiMethodImpl)object).getClientKey());
        object2 = object2 != null ? ((Api)object2).zad() : "the API";
        Object object3 = new StringBuilder(String.valueOf(object2).length() + 65);
        ((StringBuilder)object3).append("GoogleApiClient is not configured to use ");
        ((StringBuilder)object3).append((String)object2);
        ((StringBuilder)object3).append(" required for this call.");
        Preconditions.checkArgument(bl, ((StringBuilder)object3).toString());
        this.zaj.lock();
        try {
            object2 = this.zal;
            if (object2 == null) {
                this.zaa.add((BaseImplementation.ApiMethodImpl<?, ?>)object);
                object3 = this.zaj;
                object2 = object;
                object = object3;
            } else {
                object2 = object2.zae(object);
                object = this.zaj;
            }
            object.unlock();
        }
        catch (Throwable throwable) {
            this.zaj.unlock();
            throw throwable;
        }
        return (T)object2;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public final <A extends Api.AnyClient, T extends BaseImplementation.ApiMethodImpl<? extends Result, A>> T execute(T object) {
        block7: {
            Object object2 = ((BaseImplementation.ApiMethodImpl)object).getApi();
            boolean bl = this.zac.containsKey(((BaseImplementation.ApiMethodImpl)object).getClientKey());
            object2 = object2 != null ? ((Api)object2).zad() : "the API";
            Object object3 = new StringBuilder(String.valueOf(object2).length() + 65);
            ((StringBuilder)object3).append("GoogleApiClient is not configured to use ");
            ((StringBuilder)object3).append((String)object2);
            ((StringBuilder)object3).append(" required for this call.");
            Preconditions.checkArgument(bl, ((StringBuilder)object3).toString());
            this.zaj.lock();
            try {
                object2 = this.zal;
                if (object2 == null) break block7;
                if (this.zap) {
                    this.zaa.add((BaseImplementation.ApiMethodImpl<?, ?>)object);
                    while (!this.zaa.isEmpty()) {
                        object2 = this.zaa.remove();
                        this.zai.zaa((BasePendingResult<? extends Result>)object2);
                        ((BaseImplementation.ApiMethodImpl)object2).setFailedResult(Status.RESULT_INTERNAL_ERROR);
                    }
                    object3 = this.zaj;
                    object2 = object;
                    object = object3;
                } else {
                    object2 = object2.zaf(object);
                    object = this.zaj;
                }
                object.unlock();
            }
            catch (Throwable throwable) {
                this.zaj.unlock();
                throw throwable;
            }
            return (T)object2;
        }
        object = new IllegalStateException("GoogleApiClient is not connected yet.");
        throw object;
    }

    @Override
    public final <C extends Api.Client> C getClient(Api.AnyClientKey<C> object) {
        object = this.zac.get(object);
        Preconditions.checkNotNull(object, "Appropriate Api was not requested.");
        return (C)object;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public final ConnectionResult getConnectionResult(Api<?> object) {
        this.zaj.lock();
        try {
            if (!this.isConnected() && !this.zap) {
                object = new IllegalStateException("Cannot invoke getConnectionResult unless GoogleApiClient is connected");
                throw object;
            }
            if (this.zac.containsKey(((Api)object).zab())) {
                Object object2 = Preconditions.checkNotNull(this.zal).zad((Api<?>)object);
                if (object2 == null) {
                    if (this.zap) {
                        object = ConnectionResult.RESULT_SUCCESS;
                        object2 = this.zaj;
                    } else {
                        Log.w((String)"GoogleApiClientImpl", (String)this.zaf());
                        object = String.valueOf(((Api)object).zad()).concat(" requested in getConnectionResult is not connected but is not present in the failed  connections map");
                        object2 = new Exception();
                        Log.wtf((String)"GoogleApiClientImpl", (String)object, (Throwable)object2);
                        object = new ConnectionResult(8, null);
                        object2 = this.zaj;
                    }
                    object2.unlock();
                    return object;
                }
                this.zaj.unlock();
                return object2;
            }
            IllegalArgumentException illegalArgumentException = new IllegalArgumentException(String.valueOf(((Api)object).zad()).concat(" was never registered with GoogleApiClient"));
            throw illegalArgumentException;
        }
        catch (Throwable throwable) {
            this.zaj.unlock();
            throw throwable;
        }
    }

    @Override
    public final Context getContext() {
        return this.zan;
    }

    @Override
    public final Looper getLooper() {
        return this.zao;
    }

    @Override
    public final boolean hasApi(Api<?> api) {
        return this.zac.containsKey(api.zab());
    }

    @Override
    public final boolean hasConnectedApi(Api<?> object) {
        if (!this.isConnected()) {
            return false;
        }
        return (object = this.zac.get(((Api)object).zab())) != null && object.isConnected();
    }

    @Override
    public final boolean isConnected() {
        zaca zaca2 = this.zal;
        return zaca2 != null && zaca2.zaw();
    }

    @Override
    public final boolean isConnecting() {
        zaca zaca2 = this.zal;
        return zaca2 != null && zaca2.zax();
    }

    @Override
    public final boolean isConnectionCallbacksRegistered(GoogleApiClient.ConnectionCallbacks connectionCallbacks) {
        return this.zak.zaj(connectionCallbacks);
    }

    @Override
    public final boolean isConnectionFailedListenerRegistered(GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        return this.zak.zak(onConnectionFailedListener);
    }

    @Override
    public final boolean maybeSignIn(SignInConnectionListener signInConnectionListener) {
        zaca zaca2 = this.zal;
        return zaca2 != null && zaca2.zay(signInConnectionListener);
    }

    @Override
    public final void maybeSignOut() {
        zaca zaca2 = this.zal;
        if (zaca2 != null) {
            zaca2.zau();
        }
    }

    @Override
    public final void reconnect() {
        this.disconnect();
        this.connect();
    }

    @Override
    public final void registerConnectionCallbacks(GoogleApiClient.ConnectionCallbacks connectionCallbacks) {
        this.zak.zaf(connectionCallbacks);
    }

    @Override
    public final void registerConnectionFailedListener(GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        this.zak.zag(onConnectionFailedListener);
    }

    @Override
    public final <L> ListenerHolder<L> registerListener(L object) {
        this.zaj.lock();
        try {
            object = this.zau.zaa(object, this.zao, "NO_TYPE");
            return object;
        }
        finally {
            this.zaj.unlock();
        }
    }

    @Override
    public final void stopAutoManage(FragmentActivity object) {
        object = new LifecycleActivity((Activity)object);
        if (this.zam >= 0) {
            com.google.android.gms.common.api.internal.zak.zaa((LifecycleActivity)object).zae(this.zam);
            return;
        }
        throw new IllegalStateException("Called stopAutoManage but automatic lifecycle management is not enabled.");
    }

    @Override
    public final void unregisterConnectionCallbacks(GoogleApiClient.ConnectionCallbacks connectionCallbacks) {
        this.zak.zah(connectionCallbacks);
    }

    @Override
    public final void unregisterConnectionFailedListener(GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        this.zak.zai(onConnectionFailedListener);
    }

    @Override
    public final void zaa(ConnectionResult connectionResult) {
        if (!this.zat.isPlayServicesPossiblyUpdating(this.zan, connectionResult.getErrorCode())) {
            this.zak();
        }
        if (!this.zap) {
            this.zak.zac(connectionResult);
            this.zak.zaa();
        }
    }

    @Override
    public final void zab(Bundle bundle) {
        while (!this.zaa.isEmpty()) {
            ((GoogleApiClient)this).execute(this.zaa.remove());
        }
        this.zak.zad(bundle);
    }

    /*
     * Unable to fully structure code
     * Could not resolve type clashes
     */
    @Override
    public final void zac(int var1_1, boolean var2_2) {
        block7: {
            var3_3 = var1_1;
            if (var1_1 != 1) break block7;
            if (var2_2) ** GOTO lbl23
            if (this.zap) {
                var3_3 = 1;
            } else {
                this.zap = true;
                if (this.zab == null && !ClientLibraryUtils.isPackageSide()) {
                    try {
                        var7_4 = this.zat;
                        var5_5 /* !! */  = this.zan.getApplicationContext();
                        var6_7 = new zabd(this);
                        this.zab = var7_4.zac(var5_5 /* !! */ , var6_7);
                    }
                    catch (SecurityException var5_6) {
                        // empty catch block
                    }
                }
                var5_5 /* !! */  = this.zas;
                var5_5 /* !! */ .sendMessageDelayed(var5_5 /* !! */ .obtainMessage(1), this.zaq);
                var5_5 /* !! */  = this.zas;
                var5_5 /* !! */ .sendMessageDelayed(var5_5 /* !! */ .obtainMessage(2), this.zar);
lbl23:
                // 2 sources

                var3_3 = 1;
            }
        }
        var5_5 /* !! */  = this.zai.zab;
        var5_5 /* !! */  = var5_5 /* !! */ .toArray(new BasePendingResult[0]);
        var4_8 = ((BasePendingResult[])var5_5 /* !! */ ).length;
        for (var1_1 = 0; var1_1 < var4_8; ++var1_1) {
            var5_5 /* !! */ [var1_1].forceFailureUnlessReady(zadc.zaa);
        }
        this.zak.zae(var3_3);
        this.zak.zaa();
        if (var3_3 == 2) {
            this.zan();
        }
    }

    final String zaf() {
        StringWriter stringWriter = new StringWriter();
        this.dump("", null, new PrintWriter(stringWriter), null);
        return stringWriter.toString();
    }

    final boolean zak() {
        if (!this.zap) {
            return false;
        }
        this.zap = false;
        this.zas.removeMessages(2);
        this.zas.removeMessages(1);
        zabx zabx2 = this.zab;
        if (zabx2 != null) {
            zabx2.zab();
            this.zab = null;
        }
        return true;
    }

    @Override
    public final void zao(zada zada2) {
        this.zaj.lock();
        try {
            if (this.zah == null) {
                HashSet<zada> hashSet = new HashSet<zada>();
                this.zah = hashSet;
            }
            this.zah.add(zada2);
            return;
        }
        finally {
            this.zaj.unlock();
        }
    }

    /*
     * Unable to fully structure code
     */
    @Override
    public final void zap(zada var1_1) {
        block13: {
            block14: {
                this.zaj.lock();
                var3_4 = this.zah;
                if (var3_4 != null) ** GOTO lbl12
                {
                    catch (Throwable var1_3) {
                        throw var1_3;
                    }
                }
                var1_1 = new Exception();
                Log.wtf((String)"GoogleApiClientImpl", (String)"Attempted to remove pending transform when no transforms are registered.", (Throwable)var1_1);
                break block13;
lbl12:
                // 1 sources

                if (!var3_4.remove(var1_1)) {
                    var1_1 = new Exception();
                    Log.wtf((String)"GoogleApiClientImpl", (String)"Failed to remove pending transform - this may lead to memory leaks!", var1_1);
                    break block13;
                }
                this.zaj.lock();
                var1_1 = this.zah;
                if (var1_1 != null) break block14;
                this.zaj.unlock();
            }
            var2_5 = var1_1.isEmpty();
            this.zaj.unlock();
            if (var2_5 ^ true) break block13;
            var1_1 = this.zal;
            if (var1_1 == null) break block13;
            var1_1.zat();
        }
        return;
        catch (Throwable var1_2) {
            this.zaj.unlock();
            throw var1_2;
        }
        finally {
            this.zaj.unlock();
        }
    }
}

