/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.content.ServiceConnection
 *  android.os.Bundle
 *  android.os.Handler
 *  android.os.IBinder
 *  android.os.Looper
 */
package com.google.android.gms.common.api.internal;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Feature;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.internal.ConnectionCallbacks;
import com.google.android.gms.common.api.internal.OnConnectionFailedListener;
import com.google.android.gms.common.api.internal.zacf;
import com.google.android.gms.common.api.internal.zacg;
import com.google.android.gms.common.internal.BaseGmsClient;
import com.google.android.gms.common.internal.GmsClientSupervisor;
import com.google.android.gms.common.internal.IAccountAccessor;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.base.zaq;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Set;

public final class NonGmsServiceBrokerClient
implements Api.Client,
ServiceConnection {
    private static final String zaa = NonGmsServiceBrokerClient.class.getSimpleName();
    private final String zab;
    private final String zac;
    private final ComponentName zad;
    private final Context zae;
    private final ConnectionCallbacks zaf;
    private final Handler zag;
    private final OnConnectionFailedListener zah;
    private IBinder zai;
    private boolean zaj;
    private String zak;
    private String zal;

    public NonGmsServiceBrokerClient(Context context, Looper looper, ComponentName componentName, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        this(context, looper, null, null, componentName, connectionCallbacks, onConnectionFailedListener);
    }

    private NonGmsServiceBrokerClient(Context context, Looper looper, String string2, String string3, ComponentName componentName, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        block3: {
            block4: {
                block2: {
                    this.zaj = false;
                    this.zak = null;
                    this.zae = context;
                    this.zag = new zaq(looper);
                    this.zaf = connectionCallbacks;
                    this.zah = onConnectionFailedListener;
                    if (string2 == null || string3 == null) break block2;
                    if (componentName != null) break block3;
                    componentName = null;
                    break block4;
                }
                if (componentName == null) break block3;
            }
            this.zab = string2;
            this.zac = string3;
            this.zad = componentName;
            return;
        }
        throw new AssertionError((Object)"Must specify either package or component, but not both");
    }

    public NonGmsServiceBrokerClient(Context context, Looper looper, String string2, String string3, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        this(context, looper, string2, string3, null, connectionCallbacks, onConnectionFailedListener);
    }

    private final void zad() {
        if (Thread.currentThread() == this.zag.getLooper().getThread()) {
            return;
        }
        throw new IllegalStateException("This method should only run on the NonGmsServiceBrokerClient's handler thread.");
    }

    private final void zae(String string2) {
        String.valueOf(String.valueOf(this.zai)).length();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public final void connect(BaseGmsClient.ConnectionProgressReportCallbacks connectionProgressReportCallbacks) {
        block7: {
            this.zad();
            this.zae("Connect started.");
            if (this.isConnected()) {
                try {
                    this.disconnect("connect() called when already connected");
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
            try {
                boolean bl;
                Intent intent = new Intent();
                connectionProgressReportCallbacks = this.zad;
                if (connectionProgressReportCallbacks != null) {
                    intent.setComponent((ComponentName)connectionProgressReportCallbacks);
                } else {
                    intent.setPackage(this.zab).setAction(this.zac);
                }
                this.zaj = bl = this.zae.bindService(intent, (ServiceConnection)this, GmsClientSupervisor.getDefaultBindFlags());
                if (bl) break block7;
                this.zai = null;
            }
            catch (SecurityException securityException) {
                this.zaj = false;
                this.zai = null;
                throw securityException;
            }
            this.zah.onConnectionFailed(new ConnectionResult(16));
        }
        this.zae("Finished connect.");
    }

    @Override
    public final void disconnect() {
        this.zad();
        this.zae("Disconnect called.");
        try {
            this.zae.unbindService((ServiceConnection)this);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            // empty catch block
        }
        this.zaj = false;
        this.zai = null;
    }

    @Override
    public final void disconnect(String string2) {
        this.zad();
        this.zak = string2;
        this.disconnect();
    }

    @Override
    public final void dump(String string2, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] stringArray) {
    }

    @Override
    public final Feature[] getAvailableFeatures() {
        return new Feature[0];
    }

    public IBinder getBinder() {
        this.zad();
        return this.zai;
    }

    @Override
    public final String getEndpointPackageName() {
        String string2 = this.zab;
        if (string2 != null) {
            return string2;
        }
        Preconditions.checkNotNull(this.zad);
        return this.zad.getPackageName();
    }

    @Override
    public final String getLastDisconnectMessage() {
        return this.zak;
    }

    @Override
    public final int getMinApkVersion() {
        return 0;
    }

    @Override
    public final void getRemoteService(IAccountAccessor iAccountAccessor, Set<Scope> set) {
    }

    @Override
    public final Feature[] getRequiredFeatures() {
        return new Feature[0];
    }

    @Override
    public final Set<Scope> getScopesForConnectionlessNonSignIn() {
        return Collections.emptySet();
    }

    @Override
    public final IBinder getServiceBrokerBinder() {
        return null;
    }

    @Override
    public final Intent getSignInIntent() {
        return new Intent();
    }

    @Override
    public final boolean isConnected() {
        this.zad();
        return this.zai != null;
    }

    @Override
    public final boolean isConnecting() {
        this.zad();
        return this.zaj;
    }

    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        this.zag.post((Runnable)new zacg(this, iBinder));
    }

    public final void onServiceDisconnected(ComponentName componentName) {
        this.zag.post((Runnable)new zacf(this));
    }

    @Override
    public final void onUserSignOut(BaseGmsClient.SignOutCallbacks signOutCallbacks) {
    }

    @Override
    public final boolean providesSignIn() {
        return false;
    }

    @Override
    public final boolean requiresAccount() {
        return false;
    }

    @Override
    public final boolean requiresGooglePlayServices() {
        return false;
    }

    @Override
    public final boolean requiresSignIn() {
        return false;
    }

    final /* synthetic */ void zaa(IBinder iBinder) {
        this.zaj = false;
        this.zai = iBinder;
        this.zae("Connected.");
        this.zaf.onConnected(new Bundle());
    }

    final /* synthetic */ void zab() {
        this.zaj = false;
        this.zai = null;
        this.zae("Disconnected.");
        this.zaf.onConnectionSuspended(1);
    }

    public final void zac(String string2) {
        this.zal = string2;
    }
}

