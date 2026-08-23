/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  android.os.DeadObjectException
 *  android.os.Handler
 *  android.os.Looper
 *  android.os.Message
 *  android.os.RemoteException
 *  android.util.Log
 *  org.checkerframework.checker.initialization.qual.NotOnlyInitialized
 */
package com.google.android.gms.common.api.internal;

import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import androidx.collection.ArrayMap;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Feature;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.UnsupportedApiCallException;
import com.google.android.gms.common.api.internal.ApiKey;
import com.google.android.gms.common.api.internal.GoogleApiManager;
import com.google.android.gms.common.api.internal.ListenerHolder;
import com.google.android.gms.common.api.internal.RegisterListenerMethod;
import com.google.android.gms.common.api.internal.zaad;
import com.google.android.gms.common.api.internal.zabm;
import com.google.android.gms.common.api.internal.zabn;
import com.google.android.gms.common.api.internal.zabp;
import com.google.android.gms.common.api.internal.zabs;
import com.google.android.gms.common.api.internal.zabu;
import com.google.android.gms.common.api.internal.zac;
import com.google.android.gms.common.api.internal.zaci;
import com.google.android.gms.common.api.internal.zacs;
import com.google.android.gms.common.api.internal.zact;
import com.google.android.gms.common.api.internal.zah;
import com.google.android.gms.common.api.internal.zai;
import com.google.android.gms.common.api.internal.zal;
import com.google.android.gms.common.api.internal.zau;
import com.google.android.gms.common.internal.BaseGmsClient;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.service.zap;
import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.gms.tasks.TaskCompletionSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import org.checkerframework.checker.initialization.qual.NotOnlyInitialized;

public final class zabq<O extends Api.ApiOptions>
implements GoogleApiClient.ConnectionCallbacks,
GoogleApiClient.OnConnectionFailedListener,
zau {
    final GoogleApiManager zaa;
    private final Queue<zai> zab;
    @NotOnlyInitialized
    private final Api.Client zac;
    private final ApiKey<O> zad;
    private final zaad zae;
    private final Set<zal> zaf;
    private final Map<ListenerHolder.ListenerKey<?>, zaci> zag;
    private final int zah;
    private final zact zai;
    private boolean zaj;
    private final List<zabs> zak;
    private ConnectionResult zal;
    private int zam;

    /*
     * Ignored method signature, as it can't be verified against descriptor
     */
    public zabq(GoogleApiManager googleApiManager, GoogleApi googleApi) {
        Api.Client client;
        this.zaa = googleApiManager;
        this.zab = new LinkedList<zai>();
        this.zaf = new HashSet<zal>();
        this.zag = new HashMap();
        this.zak = new ArrayList<zabs>();
        this.zal = null;
        this.zam = 0;
        this.zac = client = googleApi.zab(GoogleApiManager.zaf(googleApiManager).getLooper(), this);
        this.zad = googleApi.getApiKey();
        this.zae = new zaad();
        this.zah = googleApi.zaa();
        if (client.requiresSignIn()) {
            this.zai = googleApi.zac(GoogleApiManager.zae(googleApiManager), GoogleApiManager.zaf(googleApiManager));
            return;
        }
        this.zai = null;
    }

    private final Feature zaB(Feature[] featureArray) {
        if (featureArray != null && featureArray.length != 0) {
            Feature feature;
            int n;
            Object object = this.zac.getAvailableFeatures();
            int n2 = 0;
            Object object2 = object;
            if (object == null) {
                object2 = new Feature[]{};
            }
            int n3 = ((Feature[])object2).length;
            object = new ArrayMap(n3);
            for (n = 0; n < n3; ++n) {
                feature = object2[n];
                object.put(feature.getName(), feature.getVersion());
            }
            n3 = featureArray.length;
            for (n = n2; n < n3; ++n) {
                feature = featureArray[n];
                object2 = (Long)object.get(feature.getName());
                if (object2 != null && (Long)object2 >= feature.getVersion()) {
                    continue;
                }
                return feature;
            }
            return null;
        }
        return null;
    }

    private final void zaC(ConnectionResult connectionResult) {
        for (zal zal2 : this.zaf) {
            String string2 = Objects.equal(connectionResult, ConnectionResult.RESULT_SUCCESS) ? this.zac.getEndpointPackageName() : null;
            zal2.zac(this.zad, connectionResult, string2);
        }
        this.zaf.clear();
    }

    private final void zaD(Status status) {
        Preconditions.checkHandlerThread(GoogleApiManager.zaf(this.zaa));
        this.zaE(status, null, false);
    }

    private final void zaE(Status object, Exception exception, boolean bl) {
        Preconditions.checkHandlerThread(GoogleApiManager.zaf(this.zaa));
        boolean bl2 = false;
        boolean bl3 = object == null;
        if (exception == null) {
            bl2 = true;
        }
        if (bl3 != bl2) {
            Iterator iterator2 = this.zab.iterator();
            while (iterator2.hasNext()) {
                zai zai2 = (zai)iterator2.next();
                if (bl && zai2.zac != 2) continue;
                if (object != null) {
                    zai2.zad((Status)object);
                } else {
                    zai2.zae(exception);
                }
                iterator2.remove();
            }
            return;
        }
        object = new IllegalArgumentException("Status XOR exception should be null");
        throw object;
    }

    private final void zaF() {
        ArrayList<zai> arrayList = new ArrayList<zai>(this.zab);
        int n = arrayList.size();
        for (int i = 0; i < n; ++i) {
            zai zai2 = (zai)arrayList.get(i);
            if (!this.zac.isConnected()) break;
            if (!this.zaL(zai2)) continue;
            this.zab.remove(zai2);
        }
    }

    private final void zaG() {
        this.zan();
        this.zaC(ConnectionResult.RESULT_SUCCESS);
        this.zaK();
        Iterator<zaci> iterator2 = this.zag.values().iterator();
        while (iterator2.hasNext()) {
            Object object = iterator2.next();
            if (this.zaB(((zaci)object).zaa.getRequiredFeatures()) != null) {
                iterator2.remove();
                continue;
            }
            try {
                RegisterListenerMethod<Api.AnyClient, ?> registerListenerMethod = ((zaci)object).zaa;
                object = this.zac;
                TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<Void>();
                registerListenerMethod.registerListener((Api.AnyClient)object, taskCompletionSource);
            }
            catch (RemoteException remoteException) {
                iterator2.remove();
            }
            catch (DeadObjectException deadObjectException) {
                this.onConnectionSuspended(3);
                this.zac.disconnect("DeadObjectException thrown while calling register listener method.");
                break;
            }
        }
        this.zaF();
        this.zaI();
    }

    private final void zaH(int n) {
        this.zan();
        this.zaj = true;
        this.zae.zae(n, this.zac.getLastDisconnectMessage());
        Object object = this.zaa;
        GoogleApiManager.zaf((GoogleApiManager)object).sendMessageDelayed(Message.obtain((Handler)GoogleApiManager.zaf((GoogleApiManager)object), (int)9, this.zad), GoogleApiManager.zab(this.zaa));
        object = this.zaa;
        GoogleApiManager.zaf((GoogleApiManager)object).sendMessageDelayed(Message.obtain((Handler)GoogleApiManager.zaf((GoogleApiManager)object), (int)11, this.zad), GoogleApiManager.zac(this.zaa));
        GoogleApiManager.zan(this.zaa).zac();
        object = this.zag.values().iterator();
        while (object.hasNext()) {
            ((zaci)object.next()).zac.run();
        }
    }

    private final void zaI() {
        GoogleApiManager.zaf(this.zaa).removeMessages(12, this.zad);
        GoogleApiManager googleApiManager = this.zaa;
        GoogleApiManager.zaf(googleApiManager).sendMessageDelayed(GoogleApiManager.zaf(googleApiManager).obtainMessage(12, this.zad), GoogleApiManager.zad(this.zaa));
    }

    private final void zaJ(zai zai2) {
        zai2.zag(this.zae, this.zaz());
        try {
            zai2.zaf(this);
            return;
        }
        catch (DeadObjectException deadObjectException) {
            this.onConnectionSuspended(1);
            this.zac.disconnect("DeadObjectException thrown while running ApiCallRunner.");
            return;
        }
    }

    private final void zaK() {
        if (this.zaj) {
            GoogleApiManager.zaf(this.zaa).removeMessages(11, this.zad);
            GoogleApiManager.zaf(this.zaa).removeMessages(9, this.zad);
            this.zaj = false;
        }
    }

    private final boolean zaL(zai object) {
        if (!(object instanceof zac)) {
            this.zaJ((zai)object);
            return true;
        }
        Object object2 = (zac)object;
        Feature feature = this.zaB(((zac)object2).zab(this));
        if (feature == null) {
            this.zaJ((zai)object);
            return true;
        }
        String string2 = this.zac.getClass().getName();
        String string3 = feature.getName();
        long l = feature.getVersion();
        object = new StringBuilder(String.valueOf(string2).length() + 77 + String.valueOf(string3).length());
        ((StringBuilder)object).append(string2);
        ((StringBuilder)object).append(" could not execute call because it requires feature (");
        ((StringBuilder)object).append(string3);
        ((StringBuilder)object).append(", ");
        ((StringBuilder)object).append(l);
        ((StringBuilder)object).append(").");
        Log.w((String)"GoogleApiManager", (String)((StringBuilder)object).toString());
        if (GoogleApiManager.zaE(this.zaa) && ((zac)object2).zaa(this)) {
            object = new zabs(this.zad, feature, null);
            int n = this.zak.indexOf(object);
            if (n >= 0) {
                object2 = this.zak.get(n);
                GoogleApiManager.zaf(this.zaa).removeMessages(15, object2);
                object = this.zaa;
                GoogleApiManager.zaf((GoogleApiManager)object).sendMessageDelayed(Message.obtain((Handler)GoogleApiManager.zaf((GoogleApiManager)object), (int)15, (Object)object2), GoogleApiManager.zab(this.zaa));
            } else {
                this.zak.add((zabs)object);
                object2 = this.zaa;
                GoogleApiManager.zaf((GoogleApiManager)object2).sendMessageDelayed(Message.obtain((Handler)GoogleApiManager.zaf((GoogleApiManager)object2), (int)15, (Object)object), GoogleApiManager.zab(this.zaa));
                object2 = this.zaa;
                GoogleApiManager.zaf((GoogleApiManager)object2).sendMessageDelayed(Message.obtain((Handler)GoogleApiManager.zaf((GoogleApiManager)object2), (int)16, (Object)object), GoogleApiManager.zac(this.zaa));
                object = new ConnectionResult(2, null);
                if (!this.zaM((ConnectionResult)object)) {
                    this.zaa.zaG((ConnectionResult)object, this.zah);
                }
            }
            return false;
        }
        ((zai)object2).zae(new UnsupportedApiCallException(feature));
        return true;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private final boolean zaM(ConnectionResult connectionResult) {
        Object object = GoogleApiManager.zas();
        synchronized (object) {
            GoogleApiManager googleApiManager = this.zaa;
            if (GoogleApiManager.zaj(googleApiManager) != null && GoogleApiManager.zau(googleApiManager).contains(this.zad)) {
                GoogleApiManager.zaj(this.zaa).zah(connectionResult, this.zah);
                return true;
            }
            return false;
        }
    }

    private final boolean zaN(boolean bl) {
        Preconditions.checkHandlerThread(GoogleApiManager.zaf(this.zaa));
        if (this.zac.isConnected() && this.zag.size() == 0) {
            if (this.zae.zag()) {
                if (bl) {
                    this.zaI();
                }
                return false;
            }
            this.zac.disconnect("Timing out service connection.");
            return true;
        }
        return false;
    }

    static /* bridge */ /* synthetic */ Api.Client zae(zabq zabq2) {
        return zabq2.zac;
    }

    static /* bridge */ /* synthetic */ ApiKey zag(zabq zabq2) {
        return zabq2.zad;
    }

    static /* bridge */ /* synthetic */ void zai(zabq zabq2, Status status) {
        zabq2.zaD(status);
    }

    static /* bridge */ /* synthetic */ void zaj(zabq zabq2) {
        zabq2.zaG();
    }

    static /* bridge */ /* synthetic */ void zak(zabq zabq2, int n) {
        zabq2.zaH(n);
    }

    static /* bridge */ /* synthetic */ void zal(zabq zabq2, zabs zabs2) {
        if (zabq2.zak.contains(zabs2) && !zabq2.zaj) {
            if (!zabq2.zac.isConnected()) {
                zabq2.zao();
                return;
            }
            zabq2.zaF();
            return;
        }
    }

    static /* bridge */ /* synthetic */ void zam(zabq zabq2, zabs object) {
        if (zabq2.zak.remove(object)) {
            GoogleApiManager.zaf(zabq2.zaa).removeMessages(15, object);
            GoogleApiManager.zaf(zabq2.zaa).removeMessages(16, object);
            object = zabs.zaa((zabs)object);
            ArrayList<zai> arrayList = new ArrayList<zai>(zabq2.zab.size());
            for (zai zai2 : zabq2.zab) {
                Feature[] featureArray;
                if (!(zai2 instanceof zac) || (featureArray = ((zac)zai2).zab(zabq2)) == null || !ArrayUtils.contains(featureArray, object)) continue;
                arrayList.add(zai2);
            }
            int n = arrayList.size();
            for (int i = 0; i < n; ++i) {
                zai zai3 = (zai)arrayList.get(i);
                zabq2.zab.remove(zai3);
                zai3.zae(new UnsupportedApiCallException((Feature)object));
            }
        }
    }

    static /* bridge */ /* synthetic */ boolean zax(zabq zabq2, boolean bl) {
        return zabq2.zaN(false);
    }

    @Override
    public final void onConnected(Bundle bundle) {
        if (Looper.myLooper() == GoogleApiManager.zaf(this.zaa).getLooper()) {
            this.zaG();
            return;
        }
        GoogleApiManager.zaf(this.zaa).post((Runnable)new zabm(this));
    }

    @Override
    public final void onConnectionFailed(ConnectionResult connectionResult) {
        this.zar(connectionResult, null);
    }

    @Override
    public final void onConnectionSuspended(int n) {
        if (Looper.myLooper() == GoogleApiManager.zaf(this.zaa).getLooper()) {
            this.zaH(n);
            return;
        }
        GoogleApiManager.zaf(this.zaa).post((Runnable)new zabn(this, n));
    }

    public final boolean zaA() {
        return this.zaN(true);
    }

    @Override
    public final void zaa(ConnectionResult connectionResult, Api<?> api, boolean bl) {
        throw null;
    }

    public final int zab() {
        return this.zah;
    }

    final int zac() {
        return this.zam;
    }

    public final ConnectionResult zad() {
        Preconditions.checkHandlerThread(GoogleApiManager.zaf(this.zaa));
        return this.zal;
    }

    public final Api.Client zaf() {
        return this.zac;
    }

    public final Map<ListenerHolder.ListenerKey<?>, zaci> zah() {
        return this.zag;
    }

    public final void zan() {
        Preconditions.checkHandlerThread(GoogleApiManager.zaf(this.zaa));
        this.zal = null;
    }

    public final void zao() {
        Preconditions.checkHandlerThread(GoogleApiManager.zaf(this.zaa));
        if (!this.zac.isConnected() && !this.zac.isConnecting()) {
            Object object;
            block7: {
                int n;
                try {
                    object = this.zaa;
                    n = GoogleApiManager.zan((GoogleApiManager)object).zab(GoogleApiManager.zae((GoogleApiManager)object), this.zac);
                    if (n == 0) break block7;
                }
                catch (IllegalStateException illegalStateException) {
                    this.zar(new ConnectionResult(10), illegalStateException);
                    return;
                }
                ConnectionResult connectionResult = new ConnectionResult(n, null);
                String string2 = this.zac.getClass().getName();
                String string3 = ((Object)connectionResult).toString();
                n = String.valueOf(string2).length();
                int n2 = string3.length();
                object = new StringBuilder(n + 35 + n2);
                ((StringBuilder)object).append("The service for ");
                ((StringBuilder)object).append(string2);
                ((StringBuilder)object).append(" is not available: ");
                ((StringBuilder)object).append(string3);
                Log.w((String)"GoogleApiManager", (String)((StringBuilder)object).toString());
                this.zar(connectionResult, null);
                return;
            }
            Object object2 = this.zaa;
            object = this.zac;
            object2 = new zabu((GoogleApiManager)object2, (Api.Client)object, this.zad);
            if (object.requiresSignIn()) {
                Preconditions.checkNotNull(this.zai).zae((zacs)object2);
            }
            try {
                this.zac.connect((BaseGmsClient.ConnectionProgressReportCallbacks)object2);
                return;
            }
            catch (SecurityException securityException) {
                this.zar(new ConnectionResult(10), securityException);
                return;
            }
        }
    }

    public final void zap(zai object) {
        Preconditions.checkHandlerThread(GoogleApiManager.zaf(this.zaa));
        if (this.zac.isConnected()) {
            if (this.zaL((zai)object)) {
                this.zaI();
                return;
            }
            this.zab.add((zai)object);
            return;
        }
        this.zab.add((zai)object);
        object = this.zal;
        if (object != null && ((ConnectionResult)object).hasResolution()) {
            this.zar(this.zal, null);
            return;
        }
        this.zao();
    }

    final void zaq() {
        ++this.zam;
    }

    public final void zar(ConnectionResult object, Exception exception) {
        Preconditions.checkHandlerThread(GoogleApiManager.zaf(this.zaa));
        Object object2 = this.zai;
        if (object2 != null) {
            ((zact)object2).zaf();
        }
        this.zan();
        GoogleApiManager.zan(this.zaa).zac();
        this.zaC((ConnectionResult)object);
        if (this.zac instanceof zap && ((ConnectionResult)object).getErrorCode() != 24) {
            GoogleApiManager.zav(this.zaa, true);
            object2 = this.zaa;
            GoogleApiManager.zaf((GoogleApiManager)object2).sendMessageDelayed(GoogleApiManager.zaf((GoogleApiManager)object2).obtainMessage(19), 300000L);
        }
        if (((ConnectionResult)object).getErrorCode() == 4) {
            this.zaD(GoogleApiManager.zah());
            return;
        }
        if (this.zab.isEmpty()) {
            this.zal = object;
            return;
        }
        if (exception != null) {
            Preconditions.checkHandlerThread(GoogleApiManager.zaf(this.zaa));
            this.zaE(null, exception, false);
            return;
        }
        if (GoogleApiManager.zaE(this.zaa)) {
            this.zaE(GoogleApiManager.zai(this.zad, (ConnectionResult)object), null, true);
            if (this.zab.isEmpty()) {
                return;
            }
            if (this.zaM((ConnectionResult)object)) {
                return;
            }
            if (!this.zaa.zaG((ConnectionResult)object, this.zah)) {
                if (((ConnectionResult)object).getErrorCode() == 18) {
                    this.zaj = true;
                }
                if (this.zaj) {
                    object = this.zaa;
                    GoogleApiManager.zaf((GoogleApiManager)object).sendMessageDelayed(Message.obtain((Handler)GoogleApiManager.zaf((GoogleApiManager)object), (int)9, this.zad), GoogleApiManager.zab(this.zaa));
                    return;
                }
                this.zaD(GoogleApiManager.zai(this.zad, (ConnectionResult)object));
            }
            return;
        }
        this.zaD(GoogleApiManager.zai(this.zad, (ConnectionResult)object));
    }

    public final void zas(ConnectionResult connectionResult) {
        Preconditions.checkHandlerThread(GoogleApiManager.zaf(this.zaa));
        Api.Client client = this.zac;
        String string2 = client.getClass().getName();
        String string3 = String.valueOf(connectionResult);
        StringBuilder stringBuilder = new StringBuilder(String.valueOf(string2).length() + 25 + String.valueOf(string3).length());
        stringBuilder.append("onSignInFailed for ");
        stringBuilder.append(string2);
        stringBuilder.append(" with ");
        stringBuilder.append(string3);
        client.disconnect(stringBuilder.toString());
        this.zar(connectionResult, null);
    }

    public final void zat(zal zal2) {
        Preconditions.checkHandlerThread(GoogleApiManager.zaf(this.zaa));
        this.zaf.add(zal2);
    }

    public final void zau() {
        Preconditions.checkHandlerThread(GoogleApiManager.zaf(this.zaa));
        if (this.zaj) {
            this.zao();
        }
    }

    public final void zav() {
        Preconditions.checkHandlerThread(GoogleApiManager.zaf(this.zaa));
        this.zaD(GoogleApiManager.zaa);
        this.zae.zaf();
        ListenerHolder.ListenerKey[] listenerKeyArray = this.zag.keySet();
        listenerKeyArray = listenerKeyArray.toArray(new ListenerHolder.ListenerKey[0]);
        int n = listenerKeyArray.length;
        for (int i = 0; i < n; ++i) {
            this.zap(new zah(listenerKeyArray[i], new TaskCompletionSource<Boolean>()));
        }
        this.zaC(new ConnectionResult(4));
        if (this.zac.isConnected()) {
            this.zac.onUserSignOut(new zabp(this));
        }
    }

    public final void zaw() {
        Preconditions.checkHandlerThread(GoogleApiManager.zaf(this.zaa));
        if (this.zaj) {
            this.zaK();
            Object object = this.zaa;
            object = GoogleApiManager.zag((GoogleApiManager)object).isGooglePlayServicesAvailable(GoogleApiManager.zae((GoogleApiManager)object)) == 18 ? new Status(21, "Connection timed out waiting for Google Play services update to complete.") : new Status(22, "API failed to connect while resuming due to an unknown error.");
            this.zaD((Status)object);
            this.zac.disconnect("Timing out connection while resuming.");
            return;
        }
    }

    final boolean zay() {
        return this.zac.isConnected();
    }

    public final boolean zaz() {
        return this.zac.requiresSignIn();
    }
}

