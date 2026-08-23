/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.accounts.Account
 *  android.app.Activity
 *  android.content.Context
 *  android.os.Handler
 *  android.os.Looper
 *  android.view.View
 */
package com.google.android.gms.common.api;

import android.accounts.Account;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import androidx.collection.ArrayMap;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.BaseImplementation;
import com.google.android.gms.common.api.internal.LifecycleActivity;
import com.google.android.gms.common.api.internal.ListenerHolder;
import com.google.android.gms.common.api.internal.SignInConnectionListener;
import com.google.android.gms.common.api.internal.zabe;
import com.google.android.gms.common.api.internal.zada;
import com.google.android.gms.common.api.internal.zak;
import com.google.android.gms.common.api.internal.zat;
import com.google.android.gms.common.internal.ClientSettings;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.zab;
import com.google.android.gms.signin.SignInOptions;
import com.google.android.gms.signin.zad;
import com.google.android.gms.signin.zae;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Deprecated
public abstract class GoogleApiClient {
    public static final String DEFAULT_ACCOUNT = "<<default account>>";
    public static final int SIGN_IN_MODE_OPTIONAL = 2;
    public static final int SIGN_IN_MODE_REQUIRED = 1;
    private static final Set<GoogleApiClient> zaa = Collections.newSetFromMap(new WeakHashMap());

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void dumpAll(String string2, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] stringArray) {
        Set<GoogleApiClient> set = zaa;
        synchronized (set) {
            String string3 = String.valueOf(string2).concat("  ");
            Iterator<GoogleApiClient> iterator2 = set.iterator();
            int n = 0;
            while (iterator2.hasNext()) {
                GoogleApiClient googleApiClient = iterator2.next();
                printWriter.append(string2).append("GoogleApiClient#").println(n);
                googleApiClient.dump(string3, fileDescriptor, printWriter, stringArray);
                ++n;
            }
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
    public static Set<GoogleApiClient> getAllClients() {
        Set<GoogleApiClient> set = zaa;
        // MONITORENTER : set
        // MONITOREXIT : set
        return set;
    }

    public abstract ConnectionResult blockingConnect();

    public abstract ConnectionResult blockingConnect(long var1, TimeUnit var3);

    public abstract PendingResult<Status> clearDefaultAccountAndReconnect();

    public abstract void connect();

    public void connect(int n) {
        throw new UnsupportedOperationException();
    }

    public abstract void disconnect();

    public abstract void dump(String var1, FileDescriptor var2, PrintWriter var3, String[] var4);

    public <A extends Api.AnyClient, R extends Result, T extends BaseImplementation.ApiMethodImpl<R, A>> T enqueue(T t) {
        throw new UnsupportedOperationException();
    }

    public <A extends Api.AnyClient, T extends BaseImplementation.ApiMethodImpl<? extends Result, A>> T execute(T t) {
        throw new UnsupportedOperationException();
    }

    public <C extends Api.Client> C getClient(Api.AnyClientKey<C> anyClientKey) {
        throw new UnsupportedOperationException();
    }

    public abstract ConnectionResult getConnectionResult(Api<?> var1);

    public Context getContext() {
        throw new UnsupportedOperationException();
    }

    public Looper getLooper() {
        throw new UnsupportedOperationException();
    }

    public boolean hasApi(Api<?> api) {
        throw new UnsupportedOperationException();
    }

    public abstract boolean hasConnectedApi(Api<?> var1);

    public abstract boolean isConnected();

    public abstract boolean isConnecting();

    public abstract boolean isConnectionCallbacksRegistered(ConnectionCallbacks var1);

    public abstract boolean isConnectionFailedListenerRegistered(OnConnectionFailedListener var1);

    public boolean maybeSignIn(SignInConnectionListener signInConnectionListener) {
        throw new UnsupportedOperationException();
    }

    public void maybeSignOut() {
        throw new UnsupportedOperationException();
    }

    public abstract void reconnect();

    public abstract void registerConnectionCallbacks(ConnectionCallbacks var1);

    public abstract void registerConnectionFailedListener(OnConnectionFailedListener var1);

    public <L> ListenerHolder<L> registerListener(L l) {
        throw new UnsupportedOperationException();
    }

    public abstract void stopAutoManage(FragmentActivity var1);

    public abstract void unregisterConnectionCallbacks(ConnectionCallbacks var1);

    public abstract void unregisterConnectionFailedListener(OnConnectionFailedListener var1);

    public void zao(zada zada2) {
        throw new UnsupportedOperationException();
    }

    public void zap(zada zada2) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public static final class Builder {
        private Account zaa;
        private final Set<Scope> zab = new HashSet<Scope>();
        private final Set<Scope> zac = new HashSet<Scope>();
        private int zad;
        private View zae;
        private String zaf;
        private String zag;
        private final Map<Api<?>, zab> zah = new ArrayMap();
        private final Context zai;
        private final Map<Api<?>, Api.ApiOptions> zaj = new ArrayMap();
        private LifecycleActivity zak;
        private int zal = -1;
        private OnConnectionFailedListener zam;
        private Looper zan;
        private GoogleApiAvailability zao = GoogleApiAvailability.getInstance();
        private Api.AbstractClientBuilder<? extends zae, SignInOptions> zap = com.google.android.gms.signin.zad.zac;
        private final ArrayList<ConnectionCallbacks> zaq = new ArrayList();
        private final ArrayList<OnConnectionFailedListener> zar = new ArrayList();

        public Builder(Context context) {
            this.zai = context;
            this.zan = context.getMainLooper();
            this.zaf = context.getPackageName();
            this.zag = context.getClass().getName();
        }

        public Builder(Context context, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
            this(context);
            Preconditions.checkNotNull(connectionCallbacks, "Must provide a connected listener");
            this.zaq.add(connectionCallbacks);
            Preconditions.checkNotNull(onConnectionFailedListener, "Must provide a connection failed listener");
            this.zar.add(onConnectionFailedListener);
        }

        private final <O extends Api.ApiOptions> void zab(Api<O> api, O object, Scope ... scopeArray) {
            object = new HashSet<Scope>(Preconditions.checkNotNull(api.zac(), "Base client builder must not be null").getImpliedScopes(object));
            int n = scopeArray.length;
            for (int i = 0; i < n; ++i) {
                object.add((Scope)scopeArray[i]);
            }
            this.zah.put(api, new zab((Set<Scope>)object));
        }

        public Builder addApi(Api<? extends Api.ApiOptions.NotRequiredOptions> object) {
            Preconditions.checkNotNull(object, "Api must not be null");
            this.zaj.put((Api<?>)object, (Api.ApiOptions)null);
            object = Preconditions.checkNotNull(((Api)object).zac(), "Base client builder must not be null").getImpliedScopes(null);
            this.zac.addAll((Collection<Scope>)object);
            this.zab.addAll((Collection<Scope>)object);
            return this;
        }

        public <O extends Api.ApiOptions.HasOptions> Builder addApi(Api<O> object, O o) {
            Preconditions.checkNotNull(object, "Api must not be null");
            Preconditions.checkNotNull(o, "Null options are not permitted for this Api");
            this.zaj.put((Api<?>)object, o);
            object = Preconditions.checkNotNull(((Api)object).zac(), "Base client builder must not be null").getImpliedScopes(o);
            this.zac.addAll((Collection<Scope>)object);
            this.zab.addAll((Collection<Scope>)object);
            return this;
        }

        public <O extends Api.ApiOptions.HasOptions> Builder addApiIfAvailable(Api<O> api, O o, Scope ... scopeArray) {
            Preconditions.checkNotNull(api, "Api must not be null");
            Preconditions.checkNotNull(o, "Null options are not permitted for this Api");
            this.zaj.put(api, o);
            this.zab(api, o, scopeArray);
            return this;
        }

        public <T extends Api.ApiOptions.NotRequiredOptions> Builder addApiIfAvailable(Api<? extends Api.ApiOptions.NotRequiredOptions> api, Scope ... scopeArray) {
            Preconditions.checkNotNull(api, "Api must not be null");
            this.zaj.put(api, null);
            this.zab(api, null, scopeArray);
            return this;
        }

        public Builder addConnectionCallbacks(ConnectionCallbacks connectionCallbacks) {
            Preconditions.checkNotNull(connectionCallbacks, "Listener must not be null");
            this.zaq.add(connectionCallbacks);
            return this;
        }

        public Builder addOnConnectionFailedListener(OnConnectionFailedListener onConnectionFailedListener) {
            Preconditions.checkNotNull(onConnectionFailedListener, "Listener must not be null");
            this.zar.add(onConnectionFailedListener);
            return this;
        }

        public Builder addScope(Scope scope) {
            Preconditions.checkNotNull(scope, "Scope must not be null");
            this.zab.add(scope);
            return this;
        }

        /*
         * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
         * Loose catch block
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        public GoogleApiClient build() {
            int n;
            boolean bl;
            Object object;
            Preconditions.checkArgument(this.zaj.isEmpty() ^ true, "must call addApi() to add at least one API");
            ClientSettings clientSettings = this.zaa();
            Map<Api<?>, zab> map = clientSettings.zad();
            Object object2 = new ArrayMap();
            ArrayMap arrayMap = new ArrayMap();
            ArrayList<zat> arrayList = new ArrayList<zat>();
            Iterator<Api<?>> iterator2 = this.zaj.keySet().iterator();
            Object object3 = null;
            int n2 = 0;
            while (iterator2.hasNext()) {
                object = iterator2.next();
                Api.ApiOptions apiOptions = this.zaj.get(object);
                bl = map.get(object) != null;
                object2.put(object, bl);
                zat zat2 = new zat((Api<?>)object, bl);
                arrayList.add(zat2);
                Api.AbstractClientBuilder<?, Api.ApiOptions> abstractClientBuilder = Preconditions.checkNotNull(((Api)object).zaa());
                zat2 = abstractClientBuilder.buildClient(this.zai, this.zan, clientSettings, apiOptions, zat2, zat2);
                arrayMap.put(((Api)object).zab(), (Api.Client)((Object)zat2));
                n = n2;
                if (abstractClientBuilder.getPriority() == 1) {
                    n = apiOptions != null ? 1 : 0;
                }
                n2 = n;
                if (!zat2.providesSignIn()) continue;
                if (object3 != null) {
                    object = ((Api)object).zad();
                    object2 = ((Api)object3).zad();
                    object3 = new StringBuilder(String.valueOf(object).length() + 21 + String.valueOf(object2).length());
                    ((StringBuilder)object3).append((String)object);
                    ((StringBuilder)object3).append(" cannot be used with ");
                    ((StringBuilder)object3).append((String)object2);
                    throw new IllegalStateException(((StringBuilder)object3).toString());
                }
                object3 = object;
                n2 = n;
            }
            if (object3 != null) {
                if (n2 != 0) {
                    object = ((Api)object3).zad();
                    object3 = new StringBuilder(String.valueOf(object).length() + 82);
                    ((StringBuilder)object3).append("With using ");
                    ((StringBuilder)object3).append((String)object);
                    ((StringBuilder)object3).append(", GamesOptions can only be specified within GoogleSignInOptions.Builder");
                    throw new IllegalStateException(((StringBuilder)object3).toString());
                }
                bl = this.zaa == null;
                Preconditions.checkState(bl, "Must not set an account in GoogleApiClient.Builder when using %s. Set account in GoogleSignInOptions.Builder instead", ((Api)object3).zad());
                Preconditions.checkState(this.zab.equals(this.zac), "Must not set scopes in GoogleApiClient.Builder when using %s. Set account in GoogleSignInOptions.Builder instead.", ((Api)object3).zad());
            }
            n = zabe.zad(arrayMap.values(), true);
            object = new zabe(this.zai, new ReentrantLock(), this.zan, clientSettings, this.zao, this.zap, (Map<Api<?>, Boolean>)object2, (List<ConnectionCallbacks>)this.zaq, (List<OnConnectionFailedListener>)this.zar, arrayMap, this.zal, n, arrayList);
            object3 = zaa;
            synchronized (object3) {
                zaa.add(object);
                // MONITOREXIT @DISABLED, blocks:[0, 2] lbl63 : MonitorExitStatement: MONITOREXIT : var4_7
                if (this.zal < 0) return object;
                com.google.android.gms.common.api.internal.zak.zaa(this.zak).zad(this.zal, (GoogleApiClient)object, this.zam);
                return object;
                {
                    catch (Throwable throwable) {}
                    {
                        throw throwable;
                    }
                }
            }
        }

        public Builder enableAutoManage(FragmentActivity object, int n, OnConnectionFailedListener onConnectionFailedListener) {
            object = new LifecycleActivity((Activity)object);
            boolean bl = n >= 0;
            Preconditions.checkArgument(bl, "clientId must be non-negative");
            this.zal = n;
            this.zam = onConnectionFailedListener;
            this.zak = object;
            return this;
        }

        public Builder enableAutoManage(FragmentActivity fragmentActivity, OnConnectionFailedListener onConnectionFailedListener) {
            this.enableAutoManage(fragmentActivity, 0, onConnectionFailedListener);
            return this;
        }

        public Builder setAccountName(String string2) {
            string2 = string2 == null ? null : new Account(string2, "com.google");
            this.zaa = string2;
            return this;
        }

        public Builder setGravityForPopups(int n) {
            this.zad = n;
            return this;
        }

        public Builder setHandler(Handler handler) {
            Preconditions.checkNotNull(handler, "Handler must not be null");
            this.zan = handler.getLooper();
            return this;
        }

        public Builder setViewForPopups(View view) {
            Preconditions.checkNotNull(view, "View must not be null");
            this.zae = view;
            return this;
        }

        public Builder useDefaultAccount() {
            this.setAccountName(GoogleApiClient.DEFAULT_ACCOUNT);
            return this;
        }

        public final ClientSettings zaa() {
            SignInOptions signInOptions = SignInOptions.zaa;
            if (this.zaj.containsKey(com.google.android.gms.signin.zad.zag)) {
                signInOptions = (SignInOptions)this.zaj.get(com.google.android.gms.signin.zad.zag);
            }
            return new ClientSettings(this.zaa, this.zab, this.zah, this.zad, this.zae, this.zaf, this.zag, signInOptions, false);
        }
    }

    @Deprecated
    public static interface ConnectionCallbacks
    extends com.google.android.gms.common.api.internal.ConnectionCallbacks {
        public static final int CAUSE_NETWORK_LOST = 2;
        public static final int CAUSE_SERVICE_DISCONNECTED = 1;
    }

    @Deprecated
    public static interface OnConnectionFailedListener
    extends com.google.android.gms.common.api.internal.OnConnectionFailedListener {
    }
}

