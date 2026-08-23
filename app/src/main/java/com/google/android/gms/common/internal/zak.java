/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  android.os.Handler
 *  android.os.Handler$Callback
 *  android.os.Looper
 *  android.os.Message
 *  android.util.Log
 *  org.checkerframework.checker.initialization.qual.NotOnlyInitialized
 */
package com.google.android.gms.common.internal;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.zaj;
import com.google.android.gms.internal.base.zaq;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import org.checkerframework.checker.initialization.qual.NotOnlyInitialized;

public final class zak
implements Handler.Callback {
    final ArrayList<GoogleApiClient.ConnectionCallbacks> zaa;
    @NotOnlyInitialized
    private final zaj zab;
    private final ArrayList<GoogleApiClient.ConnectionCallbacks> zac = new ArrayList();
    private final ArrayList<GoogleApiClient.OnConnectionFailedListener> zad;
    private volatile boolean zae = false;
    private final AtomicInteger zaf;
    private boolean zag = false;
    private final Handler zah;
    private final Object zai;

    public zak(Looper looper, zaj zaj2) {
        this.zaa = new ArrayList();
        this.zad = new ArrayList();
        this.zaf = new AtomicInteger(0);
        this.zai = new Object();
        this.zab = zaj2;
        this.zah = new zaq(looper, this);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final boolean handleMessage(Message object) {
        if (object.what != 1) {
            int n = object.what;
            StringBuilder stringBuilder = new StringBuilder(45);
            stringBuilder.append("Don't know how to handle message: ");
            stringBuilder.append(n);
            object = new Exception();
            Log.wtf((String)"GmsClientEvents", (String)stringBuilder.toString(), (Throwable)object);
            return false;
        }
        GoogleApiClient.ConnectionCallbacks connectionCallbacks = (GoogleApiClient.ConnectionCallbacks)object.obj;
        object = this.zai;
        synchronized (object) {
            if (this.zae && this.zab.isConnected() && this.zac.contains(connectionCallbacks)) {
                connectionCallbacks.onConnected(null);
            }
            return true;
        }
    }

    public final void zaa() {
        this.zae = false;
        this.zaf.incrementAndGet();
    }

    public final void zab() {
        this.zae = true;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void zac(ConnectionResult connectionResult) {
        Preconditions.checkHandlerThread(this.zah, "onConnectionFailure must only be called on the Handler thread");
        this.zah.removeMessages(1);
        Object object = this.zai;
        synchronized (object) {
            Object object2 = new ArrayList(this.zad);
            int n = this.zaf.get();
            Iterator<GoogleApiClient.OnConnectionFailedListener> iterator2 = ((ArrayList)object2).iterator();
            while (iterator2.hasNext()) {
                object2 = iterator2.next();
                if (!this.zae) return;
                if (this.zaf.get() != n) {
                    return;
                }
                if (!this.zad.contains(object2)) continue;
                object2.onConnectionFailed(connectionResult);
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void zad(Bundle bundle) {
        Preconditions.checkHandlerThread(this.zah, "onConnectionSuccess must only be called on the Handler thread");
        Object object = this.zai;
        synchronized (object) {
            Preconditions.checkState(this.zag ^ true);
            this.zah.removeMessages(1);
            this.zag = true;
            Preconditions.checkState(this.zaa.isEmpty());
            Object object2 = new ArrayList(this.zac);
            int n = this.zaf.get();
            Iterator<GoogleApiClient.ConnectionCallbacks> iterator2 = ((ArrayList)object2).iterator();
            while (iterator2.hasNext()) {
                object2 = iterator2.next();
                if (!this.zae || !this.zab.isConnected() || this.zaf.get() != n) break;
                if (this.zaa.contains(object2)) continue;
                object2.onConnected(bundle);
            }
            this.zaa.clear();
            this.zag = false;
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void zae(int n) {
        Preconditions.checkHandlerThread(this.zah, "onUnintentionalDisconnection must only be called on the Handler thread");
        this.zah.removeMessages(1);
        Object object = this.zai;
        synchronized (object) {
            this.zag = true;
            Object object2 = new ArrayList(this.zac);
            int n2 = this.zaf.get();
            object2 = ((ArrayList)object2).iterator();
            while (object2.hasNext()) {
                GoogleApiClient.ConnectionCallbacks connectionCallbacks = (GoogleApiClient.ConnectionCallbacks)object2.next();
                if (!this.zae || this.zaf.get() != n2) break;
                if (!this.zac.contains(connectionCallbacks)) continue;
                connectionCallbacks.onConnectionSuspended(n);
            }
            this.zaa.clear();
            this.zag = false;
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public final void zaf(GoogleApiClient.ConnectionCallbacks connectionCallbacks) {
        Preconditions.checkNotNull(connectionCallbacks);
        Object object = this.zai;
        // MONITORENTER : object
        if (this.zac.contains(connectionCallbacks)) {
            String string2 = String.valueOf(connectionCallbacks);
            int n = String.valueOf(string2).length();
            StringBuilder stringBuilder = new StringBuilder(n + 62);
            stringBuilder.append("registerConnectionCallbacks(): listener ");
            stringBuilder.append(string2);
            stringBuilder.append(" is already registered");
            Log.w((String)"GmsClientEvents", (String)stringBuilder.toString());
        } else {
            this.zac.add(connectionCallbacks);
        }
        // MONITOREXIT : object
        if (!this.zab.isConnected()) return;
        object = this.zah;
        object.sendMessage(object.obtainMessage(1, (Object)connectionCallbacks));
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void zag(GoogleApiClient.OnConnectionFailedListener object) {
        Preconditions.checkNotNull(object);
        Object object2 = this.zai;
        synchronized (object2) {
            if (this.zad.contains(object)) {
                String string2 = String.valueOf(object);
                int n = String.valueOf(string2).length();
                object = new StringBuilder(n + 67);
                ((StringBuilder)object).append("registerConnectionFailedListener(): listener ");
                ((StringBuilder)object).append(string2);
                ((StringBuilder)object).append(" is already registered");
                Log.w((String)"GmsClientEvents", (String)((StringBuilder)object).toString());
            } else {
                this.zad.add((GoogleApiClient.OnConnectionFailedListener)object);
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void zah(GoogleApiClient.ConnectionCallbacks object) {
        Preconditions.checkNotNull(object);
        Object object2 = this.zai;
        synchronized (object2) {
            if (!this.zac.remove(object)) {
                object = String.valueOf(object);
                int n = String.valueOf(object).length();
                StringBuilder stringBuilder = new StringBuilder(n + 52);
                stringBuilder.append("unregisterConnectionCallbacks(): listener ");
                stringBuilder.append((String)object);
                stringBuilder.append(" not found");
                Log.w((String)"GmsClientEvents", (String)stringBuilder.toString());
            } else if (this.zag) {
                this.zaa.add((GoogleApiClient.ConnectionCallbacks)object);
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void zai(GoogleApiClient.OnConnectionFailedListener object) {
        Preconditions.checkNotNull(object);
        Object object2 = this.zai;
        synchronized (object2) {
            if (!this.zad.remove(object)) {
                object = String.valueOf(object);
                int n = String.valueOf(object).length();
                StringBuilder stringBuilder = new StringBuilder(n + 57);
                stringBuilder.append("unregisterConnectionFailedListener(): listener ");
                stringBuilder.append((String)object);
                stringBuilder.append(" not found");
                Log.w((String)"GmsClientEvents", (String)stringBuilder.toString());
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final boolean zaj(GoogleApiClient.ConnectionCallbacks connectionCallbacks) {
        Preconditions.checkNotNull(connectionCallbacks);
        Object object = this.zai;
        synchronized (object) {
            return this.zac.contains(connectionCallbacks);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final boolean zak(GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        Preconditions.checkNotNull(onConnectionFailedListener);
        Object object = this.zai;
        synchronized (object) {
            return this.zad.contains(onConnectionFailedListener);
        }
    }
}

