/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.accounts.Account
 *  android.content.Context
 *  android.os.Handler
 *  android.os.IInterface
 *  android.os.Looper
 */
package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.content.Context;
import android.os.Handler;
import android.os.IInterface;
import android.os.Looper;
import com.google.android.gms.common.Feature;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.internal.ConnectionCallbacks;
import com.google.android.gms.common.api.internal.OnConnectionFailedListener;
import com.google.android.gms.common.internal.BaseGmsClient;
import com.google.android.gms.common.internal.ClientSettings;
import com.google.android.gms.common.internal.GmsClientSupervisor;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.zah;
import com.google.android.gms.common.internal.zai;
import com.google.android.gms.common.internal.zaj;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executor;

public abstract class GmsClient<T extends IInterface>
extends BaseGmsClient<T>
implements Api.Client,
zaj {
    private static volatile Executor zaa;
    private final ClientSettings zab;
    private final Set<Scope> zac;
    private final Account zad;

    protected GmsClient(Context context, Handler handler, int n, ClientSettings clientSettings) {
        super(context, handler, GmsClientSupervisor.getInstance(context), GoogleApiAvailability.getInstance(), n, null, null);
        this.zab = Preconditions.checkNotNull(clientSettings);
        this.zad = clientSettings.getAccount();
        this.zac = this.zaa(clientSettings.getAllRequestedScopes());
    }

    protected GmsClient(Context context, Looper looper, int n, ClientSettings clientSettings) {
        this(context, looper, GmsClientSupervisor.getInstance(context), GoogleApiAvailability.getInstance(), n, clientSettings, null, null);
    }

    @Deprecated
    protected GmsClient(Context context, Looper looper, int n, ClientSettings clientSettings, GoogleApiClient.ConnectionCallbacks connectionCallbacks, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        this(context, looper, n, clientSettings, (ConnectionCallbacks)connectionCallbacks, (OnConnectionFailedListener)onConnectionFailedListener);
    }

    protected GmsClient(Context context, Looper looper, int n, ClientSettings clientSettings, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        this(context, looper, GmsClientSupervisor.getInstance(context), GoogleApiAvailability.getInstance(), n, clientSettings, Preconditions.checkNotNull(connectionCallbacks), Preconditions.checkNotNull(onConnectionFailedListener));
    }

    protected GmsClient(Context context, Looper looper, GmsClientSupervisor gmsClientSupervisor, GoogleApiAvailability googleApiAvailability, int n, ClientSettings clientSettings, ConnectionCallbacks object, OnConnectionFailedListener object2) {
        object = object == null ? null : new zah((ConnectionCallbacks)object);
        object2 = object2 == null ? null : new zai((OnConnectionFailedListener)object2);
        super(context, looper, gmsClientSupervisor, googleApiAvailability, n, (BaseGmsClient.BaseConnectionCallbacks)object, (BaseGmsClient.BaseOnConnectionFailedListener)object2, clientSettings.zac());
        this.zab = clientSettings;
        this.zad = clientSettings.getAccount();
        this.zac = this.zaa(clientSettings.getAllRequestedScopes());
    }

    private final Set<Scope> zaa(Set<Scope> set) {
        Set<Scope> set2 = this.validateScopes(set);
        Iterator<Scope> iterator2 = set2.iterator();
        while (iterator2.hasNext()) {
            if (set.contains(iterator2.next())) continue;
            throw new IllegalStateException("Expanding scopes is not permitted, use implied scopes instead");
        }
        return set2;
    }

    @Override
    public final Account getAccount() {
        return this.zad;
    }

    @Override
    protected final Executor getBindServiceExecutor() {
        return null;
    }

    protected final ClientSettings getClientSettings() {
        return this.zab;
    }

    @Override
    public Feature[] getRequiredFeatures() {
        return new Feature[0];
    }

    @Override
    protected final Set<Scope> getScopes() {
        return this.zac;
    }

    @Override
    public Set<Scope> getScopesForConnectionlessNonSignIn() {
        Set<Scope> set = this.requiresSignIn() ? this.zac : Collections.emptySet();
        return set;
    }

    protected Set<Scope> validateScopes(Set<Scope> set) {
        return set;
    }
}

