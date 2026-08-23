/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Application
 *  android.content.Context
 *  android.os.Handler
 *  android.os.Handler$Callback
 *  android.os.Looper
 *  android.os.Message
 *  android.util.Log
 *  org.checkerframework.checker.initialization.qual.NotOnlyInitialized
 */
package com.google.android.gms.common.api.internal;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import androidx.collection.ArraySet;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.HasApiKey;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.ApiKey;
import com.google.android.gms.common.api.internal.BackgroundDetector;
import com.google.android.gms.common.api.internal.BaseImplementation;
import com.google.android.gms.common.api.internal.ListenerHolder;
import com.google.android.gms.common.api.internal.RegisterListenerMethod;
import com.google.android.gms.common.api.internal.StatusExceptionMapper;
import com.google.android.gms.common.api.internal.TaskApiCall;
import com.google.android.gms.common.api.internal.UnregisterListenerMethod;
import com.google.android.gms.common.api.internal.zaae;
import com.google.android.gms.common.api.internal.zaaf;
import com.google.android.gms.common.api.internal.zabk;
import com.google.android.gms.common.api.internal.zabl;
import com.google.android.gms.common.api.internal.zabq;
import com.google.android.gms.common.api.internal.zabs;
import com.google.android.gms.common.api.internal.zacd;
import com.google.android.gms.common.api.internal.zace;
import com.google.android.gms.common.api.internal.zach;
import com.google.android.gms.common.api.internal.zaci;
import com.google.android.gms.common.api.internal.zae;
import com.google.android.gms.common.api.internal.zaf;
import com.google.android.gms.common.api.internal.zag;
import com.google.android.gms.common.api.internal.zah;
import com.google.android.gms.common.api.internal.zai;
import com.google.android.gms.common.api.internal.zal;
import com.google.android.gms.common.internal.GmsClientSupervisor;
import com.google.android.gms.common.internal.MethodInvocation;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.RootTelemetryConfigManager;
import com.google.android.gms.common.internal.RootTelemetryConfiguration;
import com.google.android.gms.common.internal.TelemetryData;
import com.google.android.gms.common.internal.TelemetryLogging;
import com.google.android.gms.common.internal.TelemetryLoggingClient;
import com.google.android.gms.common.util.DeviceProperties;
import com.google.android.gms.internal.base.zaq;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.checkerframework.checker.initialization.qual.NotOnlyInitialized;

public class GoogleApiManager
implements Handler.Callback {
    public static final Status zaa = new Status(4, "Sign-out occurred while this API call was in progress.");
    private static final Status zab = new Status(4, "The user must be signed in to make this API call.");
    private static final Object zac = new Object();
    private static GoogleApiManager zad;
    private long zae = 5000L;
    private long zaf = 120000L;
    private long zag = 10000L;
    private boolean zah = false;
    private TelemetryData zai;
    private TelemetryLoggingClient zaj;
    private final Context zak;
    private final GoogleApiAvailability zal;
    private final com.google.android.gms.common.internal.zal zam;
    private final AtomicInteger zan = new AtomicInteger(1);
    private final AtomicInteger zao = new AtomicInteger(0);
    private final Map<ApiKey<?>, zabq<?>> zap = new ConcurrentHashMap(5, 0.75f, 1);
    private zaae zaq = null;
    private final Set<ApiKey<?>> zar = new ArraySet();
    private final Set<ApiKey<?>> zas = new ArraySet();
    @NotOnlyInitialized
    private final Handler zat;
    private volatile boolean zau = true;

    private GoogleApiManager(Context context, Looper object, GoogleApiAvailability googleApiAvailability) {
        this.zak = context;
        object = new zaq((Looper)object, this);
        this.zat = object;
        this.zal = googleApiAvailability;
        this.zam = new com.google.android.gms.common.internal.zal(googleApiAvailability);
        if (DeviceProperties.isAuto(context)) {
            this.zau = false;
        }
        object.sendMessage(object.obtainMessage(6));
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void reportSignOut() {
        Object object = zac;
        synchronized (object) {
            GoogleApiManager googleApiManager = zad;
            if (googleApiManager != null) {
                googleApiManager.zao.incrementAndGet();
                googleApiManager = googleApiManager.zat;
                googleApiManager.sendMessageAtFrontOfQueue(googleApiManager.obtainMessage(10));
            }
            return;
        }
    }

    static /* bridge */ /* synthetic */ boolean zaE(GoogleApiManager googleApiManager) {
        return googleApiManager.zau;
    }

    private static Status zaH(ApiKey<?> object, ConnectionResult connectionResult) {
        object = ((ApiKey)object).zab();
        String string2 = String.valueOf(connectionResult);
        StringBuilder stringBuilder = new StringBuilder(String.valueOf(object).length() + 63 + String.valueOf(string2).length());
        stringBuilder.append("API: ");
        stringBuilder.append((String)object);
        stringBuilder.append(" is not available on this device. Connection failed with: ");
        stringBuilder.append(string2);
        return new Status(connectionResult, stringBuilder.toString());
    }

    private final zabq<?> zaI(GoogleApi<?> zabq2) {
        ApiKey<?> apiKey = ((GoogleApi)((Object)zabq2)).getApiKey();
        zabq<?> zabq3 = this.zap.get(apiKey);
        if (zabq3 == null) {
            zabq2 = new zabq(this, (GoogleApi)((Object)zabq2));
            this.zap.put(apiKey, zabq2);
        } else {
            zabq2 = zabq3;
        }
        if (zabq2.zaz()) {
            this.zas.add(apiKey);
        }
        zabq2.zao();
        return zabq2;
    }

    private final TelemetryLoggingClient zaJ() {
        if (this.zaj == null) {
            this.zaj = TelemetryLogging.getClient(this.zak);
        }
        return this.zaj;
    }

    private final void zaK() {
        TelemetryData telemetryData = this.zai;
        if (telemetryData != null) {
            if (telemetryData.zaa() > 0 || this.zaF()) {
                this.zaJ().log(telemetryData);
            }
            this.zai = null;
        }
    }

    private final <T> void zaL(TaskCompletionSource<T> object, int n, GoogleApi object2) {
        if (n != 0 && (object2 = zacd.zaa(this, n, ((GoogleApi)object2).getApiKey())) != null) {
            object = ((TaskCompletionSource)object).getTask();
            Handler handler = this.zat;
            handler.getClass();
            ((Task)object).addOnCompleteListener(new zabk(handler), object2);
        }
    }

    static /* bridge */ /* synthetic */ long zab(GoogleApiManager googleApiManager) {
        return googleApiManager.zae;
    }

    static /* bridge */ /* synthetic */ long zac(GoogleApiManager googleApiManager) {
        return googleApiManager.zaf;
    }

    static /* bridge */ /* synthetic */ long zad(GoogleApiManager googleApiManager) {
        return googleApiManager.zag;
    }

    static /* bridge */ /* synthetic */ Context zae(GoogleApiManager googleApiManager) {
        return googleApiManager.zak;
    }

    static /* bridge */ /* synthetic */ Handler zaf(GoogleApiManager googleApiManager) {
        return googleApiManager.zat;
    }

    static /* bridge */ /* synthetic */ GoogleApiAvailability zag(GoogleApiManager googleApiManager) {
        return googleApiManager.zal;
    }

    static /* bridge */ /* synthetic */ Status zah() {
        return zab;
    }

    static /* bridge */ /* synthetic */ Status zai(ApiKey apiKey, ConnectionResult connectionResult) {
        return GoogleApiManager.zaH(apiKey, connectionResult);
    }

    static /* bridge */ /* synthetic */ zaae zaj(GoogleApiManager googleApiManager) {
        return googleApiManager.zaq;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static GoogleApiManager zal() {
        Object object = zac;
        synchronized (object) {
            Preconditions.checkNotNull(zad, "Must guarantee manager is non-null before using getInstance");
            return zad;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static GoogleApiManager zam(Context object) {
        Object object2 = zac;
        synchronized (object2) {
            GoogleApiManager googleApiManager;
            if (zad != null) return zad;
            Looper looper = GmsClientSupervisor.getOrStartHandlerThread().getLooper();
            zad = googleApiManager = new GoogleApiManager(object.getApplicationContext(), looper, GoogleApiAvailability.getInstance());
            return zad;
        }
    }

    static /* bridge */ /* synthetic */ com.google.android.gms.common.internal.zal zan(GoogleApiManager googleApiManager) {
        return googleApiManager.zam;
    }

    static /* bridge */ /* synthetic */ Object zas() {
        return zac;
    }

    static /* bridge */ /* synthetic */ Map zat(GoogleApiManager googleApiManager) {
        return googleApiManager.zap;
    }

    static /* bridge */ /* synthetic */ Set zau(GoogleApiManager googleApiManager) {
        return googleApiManager.zar;
    }

    static /* bridge */ /* synthetic */ void zav(GoogleApiManager googleApiManager, boolean bl) {
        googleApiManager.zah = true;
    }

    /*
     * Could not resolve type clashes
     * Unable to fully structure code
     */
    public final boolean handleMessage(Message var1_1) {
        var2_2 = var1_1.what;
        var3_3 = 300000L;
        var6_4 = null;
        block0 : switch (var2_2) {
            default: {
                var2_2 = var1_1.what;
                var1_1 = new StringBuilder(31);
                var1_1.append("Unknown message id: ");
                var1_1.append(var2_2);
                Log.w((String)"GoogleApiManager", (String)var1_1.toString());
                return false;
            }
            case 19: {
                this.zah = false;
                break;
            }
            case 18: {
                var1_1 = (zace)var1_1.obj;
                if (var1_1.zac == 0L) {
                    var1_1 = new TelemetryData(var1_1.zab, Arrays.asList(new MethodInvocation[]{var1_1.zaa}));
                    this.zaJ().log((TelemetryData)var1_1);
                    break;
                }
                var7_5 = this.zai;
                if (var7_5 != null) {
                    var6_4 = var7_5.zab();
                    if (var7_5.zaa() == var1_1.zab && (var6_4 == null || var6_4.size() < var1_1.zad)) {
                        this.zai.zac(var1_1.zaa);
                    } else {
                        this.zat.removeMessages(17);
                        this.zaK();
                    }
                }
                if (this.zai != null) break;
                var6_4 = new ArrayList<E>();
                var6_4.add(var1_1.zaa);
                this.zai = new TelemetryData(var1_1.zab, (List<MethodInvocation>)var6_4);
                var6_4 = this.zat;
                var6_4.sendMessageDelayed(var6_4.obtainMessage(17), var1_1.zac);
                break;
            }
            case 17: {
                this.zaK();
                break;
            }
            case 16: {
                var1_1 = (zabs)var1_1.obj;
                if (!this.zap.containsKey(zabs.zab((zabs)var1_1))) break;
                zabq.zam(this.zap.get(zabs.zab((zabs)var1_1)), (zabs)var1_1);
                break;
            }
            case 15: {
                var1_1 = (zabs)var1_1.obj;
                if (!this.zap.containsKey(zabs.zab((zabs)var1_1))) break;
                zabq.zal(this.zap.get(zabs.zab((zabs)var1_1)), (zabs)var1_1);
                break;
            }
            case 14: {
                var6_4 = (zaaf)var1_1.obj;
                var1_1 = var6_4.zaa();
                if (!this.zap.containsKey(var1_1)) {
                    var6_4.zab().setResult(false);
                    break;
                }
                var5_10 = zabq.zax(this.zap.get(var1_1), false);
                var6_4.zab().setResult(var5_10);
                break;
            }
            case 12: {
                if (!this.zap.containsKey(var1_1.obj)) break;
                this.zap.get(var1_1.obj).zaA();
                break;
            }
            case 11: {
                if (!this.zap.containsKey(var1_1.obj)) break;
                this.zap.get(var1_1.obj).zaw();
                break;
            }
            case 10: {
                for (Object var6_4 : this.zas) {
                    if ((var6_4 = this.zap.remove(var6_4)) == null) continue;
                    var6_4.zav();
                }
                this.zas.clear();
                break;
            }
            case 9: {
                if (!this.zap.containsKey(var1_1.obj)) break;
                this.zap.get(var1_1.obj).zau();
                break;
            }
            case 7: {
                this.zaI((GoogleApi)var1_1.obj);
                break;
            }
            case 6: {
                if (!(this.zak.getApplicationContext() instanceof Application)) break;
                BackgroundDetector.initialize((Application)this.zak.getApplicationContext());
                BackgroundDetector.getInstance().addListener(new zabl(this));
                if (BackgroundDetector.getInstance().readCurrentStateIfPossible(true)) break;
                this.zag = 300000L;
                break;
            }
            case 5: {
                var2_2 = var1_1.arg1;
                var7_6 = (ConnectionResult)var1_1.obj;
                for (Object var1_1 : this.zap.values()) {
                    if (var1_1.zab() != var2_2) continue;
                    ** GOTO lbl102
                }
                var1_1 = var6_4;
lbl102:
                // 2 sources

                if (var1_1 != null) {
                    if (var7_6.getErrorCode() == 13) {
                        var6_4 = this.zal.getErrorString(var7_6.getErrorCode());
                        var8_11 = var7_6.getErrorMessage();
                        var7_6 = new StringBuilder(String.valueOf(var6_4).length() + 69 + String.valueOf(var8_11).length());
                        var7_6.append("Error resolution was canceled by the user, original error message: ");
                        var7_6.append((String)var6_4);
                        var7_6.append(": ");
                        var7_6.append(var8_11);
                        zabq.zai((zabq)var1_1, new Status(17, var7_6.toString()));
                        break;
                    }
                    zabq.zai((zabq)var1_1, GoogleApiManager.zaH(zabq.zag((zabq)var1_1), (ConnectionResult)var7_6));
                    break;
                }
                var6_4 = new StringBuilder(76);
                var6_4.append("Could not find API instance ");
                var6_4.append(var2_2);
                var6_4.append(" while trying to fail enqueued calls.");
                var1_1 = new Exception();
                Log.wtf((String)"GoogleApiManager", (String)var6_4.toString(), (Throwable)var1_1);
                break;
            }
            case 4: 
            case 8: 
            case 13: {
                var7_7 = (zach)var1_1.obj;
                var1_1 = var6_4 = this.zap.get(var7_7.zac.getApiKey());
                if (var6_4 == null) {
                    var1_1 = this.zaI(var7_7.zac);
                }
                if (var1_1.zaz() && this.zao.get() != var7_7.zab) {
                    var7_7.zaa.zad(GoogleApiManager.zaa);
                    var1_1.zav();
                    break;
                }
                var1_1.zap(var7_7.zaa);
                break;
            }
            case 3: {
                for (Object var6_4 : this.zap.values()) {
                    var6_4.zan();
                    var6_4.zao();
                }
                break;
            }
            case 2: {
                var9_13 = (zal)var1_1.obj;
                for (ApiKey var7_8 : var9_13.zab()) {
                    var1_1 = this.zap.get(var7_8);
                    if (var1_1 == null) {
                        var9_13.zac(var7_8, new ConnectionResult(13), null);
                        break block0;
                    }
                    if (var1_1.zay()) {
                        var9_13.zac(var7_8, ConnectionResult.RESULT_SUCCESS, var1_1.zaf().getEndpointPackageName());
                        continue;
                    }
                    var8_12 = var1_1.zad();
                    if (var8_12 != null) {
                        var9_13.zac(var7_8, var8_12, null);
                        continue;
                    }
                    var1_1.zat(var9_13);
                    var1_1.zao();
                }
                break;
            }
            case 1: {
                if (((Boolean)var1_1.obj).booleanValue()) {
                    var3_3 = 10000L;
                }
                this.zag = var3_3;
                this.zat.removeMessages(12);
                for (Object var1_1 : this.zap.keySet()) {
                    var6_4 = this.zat;
                    var6_4.sendMessageDelayed(var6_4.obtainMessage(12, var1_1), this.zag);
                }
            }
        }
        return true;
    }

    public final void zaA() {
        Handler handler = this.zat;
        handler.sendMessage(handler.obtainMessage(3));
    }

    public final void zaB(GoogleApi<?> googleApi) {
        Handler handler = this.zat;
        handler.sendMessage(handler.obtainMessage(7, googleApi));
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void zaC(zaae zaae2) {
        Object object = zac;
        synchronized (object) {
            if (this.zaq != zaae2) {
                this.zaq = zaae2;
                this.zar.clear();
            }
            this.zar.addAll(zaae2.zaa());
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    final void zaD(zaae zaae2) {
        Object object = zac;
        synchronized (object) {
            if (this.zaq == zaae2) {
                this.zaq = null;
                this.zar.clear();
            }
            return;
        }
    }

    final boolean zaF() {
        if (this.zah) {
            return false;
        }
        RootTelemetryConfiguration rootTelemetryConfiguration = RootTelemetryConfigManager.getInstance().getConfig();
        if (rootTelemetryConfiguration != null && !rootTelemetryConfiguration.getMethodInvocationTelemetryEnabled()) {
            return false;
        }
        int n = this.zam.zaa(this.zak, 203400000);
        return n == -1 || n == 0;
        {
        }
    }

    final boolean zaG(ConnectionResult connectionResult, int n) {
        return this.zal.zah(this.zak, connectionResult, n);
    }

    public final int zaa() {
        return this.zan.getAndIncrement();
    }

    final zabq zak(ApiKey<?> apiKey) {
        return this.zap.get(apiKey);
    }

    public final Task<Map<ApiKey<?>, String>> zao(Iterable<? extends HasApiKey<?>> handler) {
        zal zal2 = new zal((Iterable<? extends HasApiKey<?>>)handler);
        handler = this.zat;
        handler.sendMessage(handler.obtainMessage(2, (Object)zal2));
        return zal2.zaa();
    }

    public final Task<Boolean> zap(GoogleApi<?> object) {
        object = new zaaf(((GoogleApi)object).getApiKey());
        Handler handler = this.zat;
        handler.sendMessage(handler.obtainMessage(14, object));
        return ((zaaf)object).zab().getTask();
    }

    public final <O extends Api.ApiOptions> Task<Void> zaq(GoogleApi<O> googleApi, RegisterListenerMethod<Api.AnyClient, ?> object, UnregisterListenerMethod<Api.AnyClient, ?> handler, Runnable runnable) {
        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<Void>();
        this.zaL(taskCompletionSource, ((RegisterListenerMethod)object).zaa(), googleApi);
        object = new zaf(new zaci((RegisterListenerMethod<Api.AnyClient, ?>)object, (UnregisterListenerMethod<Api.AnyClient, ?>)handler, runnable), taskCompletionSource);
        handler = this.zat;
        handler.sendMessage(handler.obtainMessage(8, (Object)new zach((zai)object, this.zao.get(), googleApi)));
        return taskCompletionSource.getTask();
    }

    public final <O extends Api.ApiOptions> Task<Boolean> zar(GoogleApi<O> googleApi, ListenerHolder.ListenerKey listenerKey, int n) {
        TaskCompletionSource<Boolean> taskCompletionSource = new TaskCompletionSource<Boolean>();
        this.zaL(taskCompletionSource, n, googleApi);
        zah zah2 = new zah(listenerKey, taskCompletionSource);
        listenerKey = this.zat;
        listenerKey.sendMessage(listenerKey.obtainMessage(13, new zach(zah2, this.zao.get(), googleApi)));
        return taskCompletionSource.getTask();
    }

    public final <O extends Api.ApiOptions> void zaw(GoogleApi<O> googleApi, int n, BaseImplementation.ApiMethodImpl<? extends Result, Api.AnyClient> object) {
        object = new zae<BaseImplementation.ApiMethodImpl<? extends Result, Api.AnyClient>>(n, (BaseImplementation.ApiMethodImpl<? extends Result, Api.AnyClient>)object);
        Handler handler = this.zat;
        handler.sendMessage(handler.obtainMessage(4, (Object)new zach((zai)object, this.zao.get(), googleApi)));
    }

    public final <O extends Api.ApiOptions, ResultT> void zax(GoogleApi<O> googleApi, int n, TaskApiCall<Api.AnyClient, ResultT> object, TaskCompletionSource<ResultT> handler, StatusExceptionMapper statusExceptionMapper) {
        this.zaL((TaskCompletionSource)handler, ((TaskApiCall)object).zaa(), googleApi);
        object = new zag<ResultT>(n, object, handler, statusExceptionMapper);
        handler = this.zat;
        handler.sendMessage(handler.obtainMessage(4, (Object)new zach((zai)object, this.zao.get(), googleApi)));
    }

    final void zay(MethodInvocation methodInvocation, int n, long l, int n2) {
        Handler handler = this.zat;
        handler.sendMessage(handler.obtainMessage(18, (Object)new zace(methodInvocation, n, l, n2)));
    }

    public final void zaz(ConnectionResult connectionResult, int n) {
        if (!this.zaG(connectionResult, n)) {
            Handler handler = this.zat;
            handler.sendMessage(handler.obtainMessage(5, n, 0, (Object)connectionResult));
        }
    }
}

