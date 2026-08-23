/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.accounts.Account
 *  android.app.Activity
 *  android.content.Context
 *  android.os.Handler
 *  android.os.Looper
 *  org.checkerframework.checker.initialization.qual.NotOnlyInitialized
 */
package com.google.android.gms.common.api;

import android.accounts.Account;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiActivity;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.HasApiKey;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.internal.ApiExceptionMapper;
import com.google.android.gms.common.api.internal.ApiKey;
import com.google.android.gms.common.api.internal.BaseImplementation;
import com.google.android.gms.common.api.internal.GoogleApiManager;
import com.google.android.gms.common.api.internal.ListenerHolder;
import com.google.android.gms.common.api.internal.ListenerHolders;
import com.google.android.gms.common.api.internal.NonGmsServiceBrokerClient;
import com.google.android.gms.common.api.internal.RegisterListenerMethod;
import com.google.android.gms.common.api.internal.RegistrationMethods;
import com.google.android.gms.common.api.internal.StatusExceptionMapper;
import com.google.android.gms.common.api.internal.TaskApiCall;
import com.google.android.gms.common.api.internal.UnregisterListenerMethod;
import com.google.android.gms.common.api.internal.zaae;
import com.google.android.gms.common.api.internal.zabq;
import com.google.android.gms.common.api.internal.zabv;
import com.google.android.gms.common.api.internal.zact;
import com.google.android.gms.common.api.zad;
import com.google.android.gms.common.api.zae;
import com.google.android.gms.common.internal.BaseGmsClient;
import com.google.android.gms.common.internal.ClientSettings;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.PlatformVersion;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import org.checkerframework.checker.initialization.qual.NotOnlyInitialized;

public abstract class GoogleApi<O extends Api.ApiOptions>
implements HasApiKey<O> {
    protected final GoogleApiManager zaa;
    private final Context zab;
    private final String zac;
    private final Api<O> zad;
    private final O zae;
    private final ApiKey<O> zaf;
    private final Looper zag;
    private final int zah;
    @NotOnlyInitialized
    private final GoogleApiClient zai;
    private final StatusExceptionMapper zaj;

    public GoogleApi(Activity activity, Api<O> api, O o, Settings settings) {
        this((Context)activity, activity, api, o, settings);
    }

    @Deprecated
    public GoogleApi(Activity activity, Api<O> api, O o, StatusExceptionMapper statusExceptionMapper) {
        Settings.Builder builder = new Settings.Builder();
        builder.setMapper(statusExceptionMapper);
        builder.setLooper(activity.getMainLooper());
        this(activity, api, o, builder.build());
    }

    private GoogleApi(Context object, Activity activity, Api<O> object2, O o, Settings settings) {
        block6: {
            Preconditions.checkNotNull(object, "Null context is not permitted.");
            Preconditions.checkNotNull(object2, "Api must not be null.");
            Preconditions.checkNotNull(settings, "Settings must not be null; use Settings.DEFAULT_SETTINGS instead.");
            this.zab = object.getApplicationContext();
            boolean bl = PlatformVersion.isAtLeastR();
            Object var7_10 = null;
            if (bl) {
                try {
                    object = (String)Context.class.getMethod("getAttributionTag", new Class[0]).invoke(object, new Object[0]);
                    break block6;
                }
                catch (InvocationTargetException invocationTargetException) {
                }
                catch (IllegalAccessException illegalAccessException) {
                }
                catch (NoSuchMethodException noSuchMethodException) {
                    // empty catch block
                }
            }
            object = var7_10;
        }
        this.zac = object;
        this.zad = object2;
        this.zae = o;
        this.zag = settings.zab;
        this.zaf = object2 = ApiKey.zaa(object2, o, (String)object);
        this.zai = new zabv(this);
        object = GoogleApiManager.zam(this.zab);
        this.zaa = object;
        this.zah = ((GoogleApiManager)object).zaa();
        this.zaj = settings.zaa;
        if (activity != null && !(activity instanceof GoogleApiActivity) && Looper.myLooper() == Looper.getMainLooper()) {
            zaae.zad(activity, (GoogleApiManager)object, object2);
        }
        ((GoogleApiManager)object).zaB(this);
    }

    @Deprecated
    public GoogleApi(Context context, Api<O> api, O o, Looper looper, StatusExceptionMapper statusExceptionMapper) {
        Settings.Builder builder = new Settings.Builder();
        builder.setLooper(looper);
        builder.setMapper(statusExceptionMapper);
        this(context, api, o, builder.build());
    }

    public GoogleApi(Context context, Api<O> api, O o, Settings settings) {
        this(context, null, api, o, settings);
    }

    @Deprecated
    public GoogleApi(Context context, Api<O> api, O o, StatusExceptionMapper statusExceptionMapper) {
        Settings.Builder builder = new Settings.Builder();
        builder.setMapper(statusExceptionMapper);
        this(context, api, o, builder.build());
    }

    private final <A extends Api.AnyClient, T extends BaseImplementation.ApiMethodImpl<? extends Result, A>> T zad(int n, T t) {
        t.zak();
        this.zaa.zaw(this, n, t);
        return t;
    }

    private final <TResult, A extends Api.AnyClient> Task<TResult> zae(int n, TaskApiCall<A, TResult> taskApiCall) {
        TaskCompletionSource taskCompletionSource = new TaskCompletionSource();
        this.zaa.zax(this, n, taskApiCall, taskCompletionSource, this.zaj);
        return taskCompletionSource.getTask();
    }

    public GoogleApiClient asGoogleApiClient() {
        return this.zai;
    }

    protected ClientSettings.Builder createClientSettingsBuilder() {
        ClientSettings.Builder builder = new ClientSettings.Builder();
        Object object = this.zae;
        object = object instanceof Api.ApiOptions.HasGoogleSignInAccountOptions && (object = ((Api.ApiOptions.HasGoogleSignInAccountOptions)object).getGoogleSignInAccount()) != null ? ((GoogleSignInAccount)object).getAccount() : ((object = this.zae) instanceof Api.ApiOptions.HasAccountOptions ? ((Api.ApiOptions.HasAccountOptions)object).getAccount() : null);
        builder.zab((Account)object);
        object = this.zae;
        object = object instanceof Api.ApiOptions.HasGoogleSignInAccountOptions ? ((object = ((Api.ApiOptions.HasGoogleSignInAccountOptions)object).getGoogleSignInAccount()) == null ? Collections.emptySet() : ((GoogleSignInAccount)object).getRequestedScopes()) : Collections.emptySet();
        builder.zaa((Collection<Scope>)object);
        builder.zac(this.zab.getClass().getName());
        builder.setRealClientPackageName(this.zab.getPackageName());
        return builder;
    }

    protected Task<Boolean> disconnectService() {
        return this.zaa.zap(this);
    }

    public <A extends Api.AnyClient, T extends BaseImplementation.ApiMethodImpl<? extends Result, A>> T doBestEffortWrite(T t) {
        this.zad(2, t);
        return t;
    }

    public <TResult, A extends Api.AnyClient> Task<TResult> doBestEffortWrite(TaskApiCall<A, TResult> taskApiCall) {
        return this.zae(2, taskApiCall);
    }

    public <A extends Api.AnyClient, T extends BaseImplementation.ApiMethodImpl<? extends Result, A>> T doRead(T t) {
        this.zad(0, t);
        return t;
    }

    public <TResult, A extends Api.AnyClient> Task<TResult> doRead(TaskApiCall<A, TResult> taskApiCall) {
        return this.zae(0, taskApiCall);
    }

    @Deprecated
    public <A extends Api.AnyClient, T extends RegisterListenerMethod<A, ?>, U extends UnregisterListenerMethod<A, ?>> Task<Void> doRegisterEventListener(T t, U u) {
        Preconditions.checkNotNull(t);
        Preconditions.checkNotNull(u);
        Preconditions.checkNotNull(t.getListenerKey(), "Listener has already been released.");
        Preconditions.checkNotNull(u.getListenerKey(), "Listener has already been released.");
        Preconditions.checkArgument(Objects.equal(t.getListenerKey(), u.getListenerKey()), "Listener registration and unregistration methods must be constructed with the same ListenerHolder.");
        return this.zaa.zaq(this, t, u, com.google.android.gms.common.api.zad.zaa);
    }

    public <A extends Api.AnyClient> Task<Void> doRegisterEventListener(RegistrationMethods<A, ?> registrationMethods) {
        Preconditions.checkNotNull(registrationMethods);
        Preconditions.checkNotNull(registrationMethods.register.getListenerKey(), "Listener has already been released.");
        Preconditions.checkNotNull(registrationMethods.zaa.getListenerKey(), "Listener has already been released.");
        return this.zaa.zaq(this, registrationMethods.register, registrationMethods.zaa, registrationMethods.zab);
    }

    public Task<Boolean> doUnregisterEventListener(ListenerHolder.ListenerKey<?> listenerKey) {
        return this.doUnregisterEventListener(listenerKey, 0);
    }

    public Task<Boolean> doUnregisterEventListener(ListenerHolder.ListenerKey<?> listenerKey, int n) {
        Preconditions.checkNotNull(listenerKey, "Listener key cannot be null.");
        return this.zaa.zar(this, listenerKey, n);
    }

    public <A extends Api.AnyClient, T extends BaseImplementation.ApiMethodImpl<? extends Result, A>> T doWrite(T t) {
        this.zad(1, t);
        return t;
    }

    public <TResult, A extends Api.AnyClient> Task<TResult> doWrite(TaskApiCall<A, TResult> taskApiCall) {
        return this.zae(1, taskApiCall);
    }

    @Override
    public final ApiKey<O> getApiKey() {
        return this.zaf;
    }

    public O getApiOptions() {
        return this.zae;
    }

    public Context getApplicationContext() {
        return this.zab;
    }

    protected String getContextAttributionTag() {
        return this.zac;
    }

    @Deprecated
    protected String getContextFeatureId() {
        return this.zac;
    }

    public Looper getLooper() {
        return this.zag;
    }

    public <L> ListenerHolder<L> registerListener(L l, String string2) {
        return ListenerHolders.createListenerHolder(l, this.zag, string2);
    }

    public final int zaa() {
        return this.zah;
    }

    public final Api.Client zab(Looper object, zabq<O> zabq2) {
        ClientSettings clientSettings = this.createClientSettingsBuilder().build();
        zabq2 = Preconditions.checkNotNull(this.zad.zaa()).buildClient(this.zab, (Looper)object, clientSettings, this.zae, zabq2, zabq2);
        object = this.getContextAttributionTag();
        if (object != null && zabq2 instanceof BaseGmsClient) {
            ((BaseGmsClient)((Object)zabq2)).setAttributionTag((String)object);
        }
        if (object != null && zabq2 instanceof NonGmsServiceBrokerClient) {
            ((NonGmsServiceBrokerClient)((Object)zabq2)).zac((String)object);
        }
        return zabq2;
    }

    public final zact zac(Context context, Handler handler) {
        return new zact(context, handler, this.createClientSettingsBuilder().build());
    }

    public static class Settings {
        public static final Settings DEFAULT_SETTINGS = new Builder().build();
        public final StatusExceptionMapper zaa;
        public final Looper zab;

        private Settings(StatusExceptionMapper statusExceptionMapper, Account account, Looper looper) {
            this.zaa = statusExceptionMapper;
            this.zab = looper;
        }

        /* synthetic */ Settings(StatusExceptionMapper statusExceptionMapper, Account account, Looper looper, zae zae2) {
            this(statusExceptionMapper, null, looper);
        }

        public static class Builder {
            private StatusExceptionMapper zaa;
            private Looper zab;

            public Settings build() {
                if (this.zaa == null) {
                    this.zaa = new ApiExceptionMapper();
                }
                if (this.zab == null) {
                    this.zab = Looper.getMainLooper();
                }
                return new Settings(this.zaa, null, this.zab, null);
            }

            public Builder setLooper(Looper looper) {
                Preconditions.checkNotNull(looper, "Looper must not be null.");
                this.zab = looper;
                return this;
            }

            public Builder setMapper(StatusExceptionMapper statusExceptionMapper) {
                Preconditions.checkNotNull(statusExceptionMapper, "StatusExceptionMapper must not be null.");
                this.zaa = statusExceptionMapper;
                return this;
            }
        }
    }
}

