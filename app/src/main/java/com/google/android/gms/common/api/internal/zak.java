/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 *  android.util.SparseArray
 */
package com.google.android.gms.common.api.internal;

import android.util.Log;
import android.util.SparseArray;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.internal.LifecycleActivity;
import com.google.android.gms.common.api.internal.LifecycleFragment;
import com.google.android.gms.common.api.internal.zaj;
import com.google.android.gms.common.api.internal.zam;
import com.google.android.gms.common.api.internal.zap;
import com.google.android.gms.common.internal.Preconditions;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public final class zak
extends zap {
    private final SparseArray<zaj> zad = new SparseArray();

    private zak(LifecycleFragment lifecycleFragment) {
        super(lifecycleFragment, GoogleApiAvailability.getInstance());
        this.mLifecycleFragment.addCallback("AutoManageHelper", this);
    }

    public static zak zaa(LifecycleActivity object) {
        zak zak2 = (object = zak.getFragment((LifecycleActivity)object)).getCallbackOrNull("AutoManageHelper", zak.class);
        if (zak2 != null) {
            return zak2;
        }
        return new zak((LifecycleFragment)object);
    }

    private final zaj zai(int n) {
        if (this.zad.size() <= n) {
            return null;
        }
        SparseArray<zaj> sparseArray = this.zad;
        return (zaj)sparseArray.get(sparseArray.keyAt(n));
    }

    @Override
    public final void dump(String string2, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] stringArray) {
        for (int i = 0; i < this.zad.size(); ++i) {
            zaj zaj2 = this.zai(i);
            if (zaj2 == null) continue;
            printWriter.append(string2).append("GoogleApiClient #").print(zaj2.zaa);
            printWriter.println(":");
            zaj2.zab.dump(String.valueOf(string2).concat("  "), fileDescriptor, printWriter, stringArray);
        }
    }

    @Override
    public final void onStart() {
        super.onStart();
        boolean bl = this.zaa;
        String string2 = String.valueOf(this.zad);
        Object object = new StringBuilder(String.valueOf(string2).length() + 14);
        ((StringBuilder)object).append("onStart ");
        ((StringBuilder)object).append(bl);
        ((StringBuilder)object).append(" ");
        ((StringBuilder)object).append(string2);
        Log.d((String)"AutoManageHelper", (String)((StringBuilder)object).toString());
        if (this.zab.get() == null) {
            for (int i = 0; i < this.zad.size(); ++i) {
                object = this.zai(i);
                if (object == null) continue;
                ((zaj)object).zab.connect();
            }
        }
    }

    @Override
    public final void onStop() {
        super.onStop();
        for (int i = 0; i < this.zad.size(); ++i) {
            zaj zaj2 = this.zai(i);
            if (zaj2 == null) continue;
            zaj2.zab.disconnect();
        }
    }

    @Override
    protected final void zab(ConnectionResult connectionResult, int n) {
        Log.w((String)"AutoManageHelper", (String)"Unresolved error while connecting client. Stopping auto-manage.");
        if (n < 0) {
            Log.wtf((String)"AutoManageHelper", (String)"AutoManageLifecycleHelper received onErrorResolutionFailed callback but no failing client ID is set", (Throwable)new Exception());
            return;
        }
        GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener = (zaj)this.zad.get(n);
        if (onConnectionFailedListener != null) {
            this.zae(n);
            onConnectionFailedListener = ((zaj)onConnectionFailedListener).zac;
            if (onConnectionFailedListener != null) {
                onConnectionFailedListener.onConnectionFailed(connectionResult);
            }
        }
    }

    @Override
    protected final void zac() {
        for (int i = 0; i < this.zad.size(); ++i) {
            zaj zaj2 = this.zai(i);
            if (zaj2 == null) continue;
            zaj2.zab.connect();
        }
    }

    public final void zad(int n, GoogleApiClient googleApiClient, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        Preconditions.checkNotNull(googleApiClient, "GoogleApiClient instance cannot be null");
        boolean bl = this.zad.indexOfKey(n) < 0;
        Object object = new StringBuilder(54);
        ((StringBuilder)object).append("Already managing a GoogleApiClient with id ");
        ((StringBuilder)object).append(n);
        Preconditions.checkState(bl, ((StringBuilder)object).toString());
        object = (zam)this.zab.get();
        bl = this.zaa;
        String string2 = String.valueOf(object);
        StringBuilder stringBuilder = new StringBuilder(String.valueOf(string2).length() + 49);
        stringBuilder.append("starting AutoManage for client ");
        stringBuilder.append(n);
        stringBuilder.append(" ");
        stringBuilder.append(bl);
        stringBuilder.append(" ");
        stringBuilder.append(string2);
        Log.d((String)"AutoManageHelper", (String)stringBuilder.toString());
        onConnectionFailedListener = new zaj(this, n, googleApiClient, onConnectionFailedListener);
        googleApiClient.registerConnectionFailedListener(onConnectionFailedListener);
        this.zad.put(n, (Object)onConnectionFailedListener);
        if (this.zaa && object == null) {
            Log.d((String)"AutoManageHelper", (String)"connecting ".concat(googleApiClient.toString()));
            googleApiClient.connect();
        }
    }

    public final void zae(int n) {
        zaj zaj2 = (zaj)this.zad.get(n);
        this.zad.remove(n);
        if (zaj2 != null) {
            zaj2.zab.unregisterConnectionFailedListener(zaj2);
            zaj2.zab.disconnect();
        }
    }
}

