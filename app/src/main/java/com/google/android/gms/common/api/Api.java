/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.accounts.Account
 *  android.content.Context
 *  android.content.Intent
 *  android.os.IBinder
 *  android.os.Looper
 */
package com.google.android.gms.common.api;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.Feature;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.internal.ConnectionCallbacks;
import com.google.android.gms.common.api.internal.OnConnectionFailedListener;
import com.google.android.gms.common.api.zaa;
import com.google.android.gms.common.internal.BaseGmsClient;
import com.google.android.gms.common.internal.ClientSettings;
import com.google.android.gms.common.internal.IAccountAccessor;
import com.google.android.gms.common.internal.Preconditions;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public final class Api<O extends ApiOptions> {
    private final AbstractClientBuilder<?, O> zaa;
    private final ClientKey<?> zab;
    private final String zac;

    public <C extends Client> Api(String string2, AbstractClientBuilder<C, O> abstractClientBuilder, ClientKey<C> clientKey) {
        Preconditions.checkNotNull(abstractClientBuilder, "Cannot construct an Api with a null ClientBuilder");
        Preconditions.checkNotNull(clientKey, "Cannot construct an Api with a null ClientKey");
        this.zac = string2;
        this.zaa = abstractClientBuilder;
        this.zab = clientKey;
    }

    public final AbstractClientBuilder<?, O> zaa() {
        return this.zaa;
    }

    public final AnyClientKey<?> zab() {
        return this.zab;
    }

    public final BaseClientBuilder<?, O> zac() {
        return this.zaa;
    }

    public final String zad() {
        return this.zac;
    }

    public static abstract class AbstractClientBuilder<T extends Client, O>
    extends BaseClientBuilder<T, O> {
        @Deprecated
        public T buildClient(Context context, Looper looper, ClientSettings clientSettings, O o, GoogleApiClient.ConnectionCallbacks connectionCallbacks, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
            return this.buildClient(context, looper, clientSettings, o, (ConnectionCallbacks)connectionCallbacks, (OnConnectionFailedListener)onConnectionFailedListener);
        }

        public T buildClient(Context context, Looper looper, ClientSettings clientSettings, O o, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
            throw new UnsupportedOperationException("buildClient must be implemented");
        }
    }

    public static interface AnyClient {
    }

    public static class AnyClientKey<C extends AnyClient> {
    }

    public static interface ApiOptions {
        public static final NoOptions NO_OPTIONS = new NoOptions(null);

        public static interface HasAccountOptions
        extends HasOptions,
        NotRequiredOptions {
            public Account getAccount();
        }

        public static interface HasGoogleSignInAccountOptions
        extends HasOptions {
            public GoogleSignInAccount getGoogleSignInAccount();
        }

        public static interface HasOptions
        extends ApiOptions {
        }

        public static final class NoOptions
        implements NotRequiredOptions {
            private NoOptions() {
            }

            /* synthetic */ NoOptions(zaa zaa2) {
            }
        }

        public static interface NotRequiredOptions
        extends ApiOptions {
        }

        public static interface Optional
        extends HasOptions,
        NotRequiredOptions {
        }
    }

    public static abstract class BaseClientBuilder<T extends AnyClient, O> {
        public static final int API_PRIORITY_GAMES = 1;
        public static final int API_PRIORITY_OTHER = Integer.MAX_VALUE;
        public static final int API_PRIORITY_PLUS = 2;

        public List<Scope> getImpliedScopes(O o) {
            return Collections.emptyList();
        }

        public int getPriority() {
            return Integer.MAX_VALUE;
        }
    }

    public static interface Client
    extends AnyClient {
        public void connect(BaseGmsClient.ConnectionProgressReportCallbacks var1);

        public void disconnect();

        public void disconnect(String var1);

        public void dump(String var1, FileDescriptor var2, PrintWriter var3, String[] var4);

        public Feature[] getAvailableFeatures();

        public String getEndpointPackageName();

        public String getLastDisconnectMessage();

        public int getMinApkVersion();

        public void getRemoteService(IAccountAccessor var1, Set<Scope> var2);

        public Feature[] getRequiredFeatures();

        public Set<Scope> getScopesForConnectionlessNonSignIn();

        public IBinder getServiceBrokerBinder();

        public Intent getSignInIntent();

        public boolean isConnected();

        public boolean isConnecting();

        public void onUserSignOut(BaseGmsClient.SignOutCallbacks var1);

        public boolean providesSignIn();

        public boolean requiresAccount();

        public boolean requiresGooglePlayServices();

        public boolean requiresSignIn();
    }

    public static final class ClientKey<C extends Client>
    extends AnyClientKey<C> {
    }
}

