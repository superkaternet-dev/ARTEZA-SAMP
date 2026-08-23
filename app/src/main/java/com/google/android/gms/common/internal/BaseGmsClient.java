/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.accounts.Account
 *  android.app.PendingIntent
 *  android.content.Context
 *  android.content.Intent
 *  android.content.ServiceConnection
 *  android.os.Bundle
 *  android.os.DeadObjectException
 *  android.os.Handler
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.Looper
 *  android.os.RemoteException
 *  android.text.TextUtils
 *  android.util.Log
 */
package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Feature;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.ConnectionTelemetryConfiguration;
import com.google.android.gms.common.internal.GetServiceRequest;
import com.google.android.gms.common.internal.GmsClientSupervisor;
import com.google.android.gms.common.internal.IAccountAccessor;
import com.google.android.gms.common.internal.IGmsCallbacks;
import com.google.android.gms.common.internal.IGmsServiceBroker;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.RootTelemetryConfigManager;
import com.google.android.gms.common.internal.RootTelemetryConfiguration;
import com.google.android.gms.common.internal.zzb;
import com.google.android.gms.common.internal.zzc;
import com.google.android.gms.common.internal.zzd;
import com.google.android.gms.common.internal.zze;
import com.google.android.gms.common.internal.zzf;
import com.google.android.gms.common.internal.zzg;
import com.google.android.gms.common.internal.zzj;
import com.google.android.gms.common.internal.zzn;
import com.google.android.gms.common.internal.zzu;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class BaseGmsClient<T extends IInterface> {
    public static final int CONNECT_STATE_CONNECTED = 4;
    public static final int CONNECT_STATE_DISCONNECTED = 1;
    public static final int CONNECT_STATE_DISCONNECTING = 5;
    public static final String DEFAULT_ACCOUNT = "<<default account>>";
    public static final String[] GOOGLE_PLUS_REQUIRED_FEATURES;
    public static final String KEY_PENDING_INTENT = "pendingIntent";
    private static final Feature[] zze;
    private volatile String zzA;
    private ConnectionResult zzB;
    private boolean zzC;
    private volatile zzj zzD;
    zzu zza;
    final Handler zzb;
    protected ConnectionProgressReportCallbacks zzc;
    protected AtomicInteger zzd;
    private int zzf;
    private long zzg;
    private long zzh;
    private int zzi;
    private long zzj;
    private volatile String zzk;
    private final Context zzl;
    private final Looper zzm;
    private final GmsClientSupervisor zzn;
    private final GoogleApiAvailabilityLight zzo;
    private final Object zzp;
    private final Object zzq;
    private IGmsServiceBroker zzr;
    private T zzs;
    private final ArrayList<zzc<?>> zzt;
    private zze zzu;
    private int zzv;
    private final BaseConnectionCallbacks zzw;
    private final BaseOnConnectionFailedListener zzx;
    private final int zzy;
    private final String zzz;

    static {
        zze = new Feature[0];
        GOOGLE_PLUS_REQUIRED_FEATURES = new String[]{"service_esmobile", "service_googleme"};
    }

    protected BaseGmsClient(Context context, Handler handler, GmsClientSupervisor gmsClientSupervisor, GoogleApiAvailabilityLight googleApiAvailabilityLight, int n, BaseConnectionCallbacks baseConnectionCallbacks, BaseOnConnectionFailedListener baseOnConnectionFailedListener) {
        this.zzk = null;
        this.zzp = new Object();
        this.zzq = new Object();
        this.zzt = new ArrayList();
        this.zzv = 1;
        this.zzB = null;
        this.zzC = false;
        this.zzD = null;
        this.zzd = new AtomicInteger(0);
        Preconditions.checkNotNull(context, "Context must not be null");
        this.zzl = context;
        Preconditions.checkNotNull(handler, "Handler must not be null");
        this.zzb = handler;
        this.zzm = handler.getLooper();
        Preconditions.checkNotNull(gmsClientSupervisor, "Supervisor must not be null");
        this.zzn = gmsClientSupervisor;
        Preconditions.checkNotNull(googleApiAvailabilityLight, "API availability must not be null");
        this.zzo = googleApiAvailabilityLight;
        this.zzy = n;
        this.zzw = baseConnectionCallbacks;
        this.zzx = baseOnConnectionFailedListener;
        this.zzz = null;
    }

    protected BaseGmsClient(Context context, Looper looper, int n, BaseConnectionCallbacks baseConnectionCallbacks, BaseOnConnectionFailedListener baseOnConnectionFailedListener, String string2) {
        GmsClientSupervisor gmsClientSupervisor = GmsClientSupervisor.getInstance(context);
        GoogleApiAvailabilityLight googleApiAvailabilityLight = GoogleApiAvailabilityLight.getInstance();
        Preconditions.checkNotNull(baseConnectionCallbacks);
        Preconditions.checkNotNull(baseOnConnectionFailedListener);
        this(context, looper, gmsClientSupervisor, googleApiAvailabilityLight, n, baseConnectionCallbacks, baseOnConnectionFailedListener, string2);
    }

    protected BaseGmsClient(Context context, Looper looper, GmsClientSupervisor gmsClientSupervisor, GoogleApiAvailabilityLight googleApiAvailabilityLight, int n, BaseConnectionCallbacks baseConnectionCallbacks, BaseOnConnectionFailedListener baseOnConnectionFailedListener, String string2) {
        this.zzk = null;
        this.zzp = new Object();
        this.zzq = new Object();
        this.zzt = new ArrayList();
        this.zzv = 1;
        this.zzB = null;
        this.zzC = false;
        this.zzD = null;
        this.zzd = new AtomicInteger(0);
        Preconditions.checkNotNull(context, "Context must not be null");
        this.zzl = context;
        Preconditions.checkNotNull(looper, "Looper must not be null");
        this.zzm = looper;
        Preconditions.checkNotNull(gmsClientSupervisor, "Supervisor must not be null");
        this.zzn = gmsClientSupervisor;
        Preconditions.checkNotNull(googleApiAvailabilityLight, "API availability must not be null");
        this.zzo = googleApiAvailabilityLight;
        this.zzb = new zzb(this, looper);
        this.zzy = n;
        this.zzw = baseConnectionCallbacks;
        this.zzx = baseOnConnectionFailedListener;
        this.zzz = string2;
    }

    static /* bridge */ /* synthetic */ ConnectionResult zza(BaseGmsClient baseGmsClient) {
        return baseGmsClient.zzB;
    }

    static /* bridge */ /* synthetic */ BaseConnectionCallbacks zzb(BaseGmsClient baseGmsClient) {
        return baseGmsClient.zzw;
    }

    static /* bridge */ /* synthetic */ Object zzd(BaseGmsClient baseGmsClient) {
        return baseGmsClient.zzq;
    }

    static /* bridge */ /* synthetic */ ArrayList zzf(BaseGmsClient baseGmsClient) {
        return baseGmsClient.zzt;
    }

    static /* bridge */ /* synthetic */ void zzg(BaseGmsClient baseGmsClient, ConnectionResult connectionResult) {
        baseGmsClient.zzB = connectionResult;
    }

    static /* bridge */ /* synthetic */ void zzh(BaseGmsClient baseGmsClient, IGmsServiceBroker iGmsServiceBroker) {
        baseGmsClient.zzr = iGmsServiceBroker;
    }

    static /* bridge */ /* synthetic */ void zzi(BaseGmsClient baseGmsClient, int n, IInterface iInterface) {
        baseGmsClient.zzp(n, null);
    }

    static /* bridge */ /* synthetic */ void zzj(BaseGmsClient object, zzj object2) {
        ((BaseGmsClient)object).zzD = object2;
        if (((BaseGmsClient)object).usesClientTelemetry()) {
            object = ((zzj)object2).zzd;
            object2 = RootTelemetryConfigManager.getInstance();
            object = object == null ? null : ((ConnectionTelemetryConfiguration)object).zza();
            ((RootTelemetryConfigManager)object2).zza((RootTelemetryConfiguration)object);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    static /* bridge */ /* synthetic */ void zzk(BaseGmsClient baseGmsClient, int n) {
        Object object = baseGmsClient.zzp;
        // MONITORENTER : object
        n = baseGmsClient.zzv;
        // MONITOREXIT : object
        if (n == 3) {
            baseGmsClient.zzC = true;
            n = 5;
        } else {
            n = 4;
        }
        object = baseGmsClient.zzb;
        object.sendMessage(object.obtainMessage(n, baseGmsClient.zzd.get(), 16));
    }

    static /* bridge */ /* synthetic */ boolean zzm(BaseGmsClient baseGmsClient) {
        return baseGmsClient.zzC;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    static /* bridge */ /* synthetic */ boolean zzn(BaseGmsClient baseGmsClient, int n, int n2, IInterface iInterface) {
        Object object = baseGmsClient.zzp;
        synchronized (object) {
            if (baseGmsClient.zzv != n) {
                return false;
            }
            baseGmsClient.zzp(n2, iInterface);
            return true;
        }
    }

    static /* bridge */ /* synthetic */ boolean zzo(BaseGmsClient baseGmsClient) {
        boolean bl = baseGmsClient.zzC;
        boolean bl2 = false;
        if (!(bl || TextUtils.isEmpty((CharSequence)baseGmsClient.getServiceDescriptor()) || TextUtils.isEmpty((CharSequence)baseGmsClient.getLocalStartServiceAction()))) {
            try {
                Class.forName(baseGmsClient.getServiceDescriptor());
                bl2 = true;
            }
            catch (ClassNotFoundException classNotFoundException) {
                // empty catch block
            }
        }
        return bl2;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private final void zzp(int n, T object) {
        int n2;
        boolean bl = false;
        int n3 = n != 4 ? 0 : 1;
        if (n3 == (n2 = object != null)) {
            bl = true;
        }
        Preconditions.checkArgument(bl);
        Object object2 = this.zzp;
        synchronized (object2) {
            this.zzv = n;
            this.zzs = object;
            switch (n) {
                default: {
                    break;
                }
                case 4: {
                    Preconditions.checkNotNull(object);
                    this.onConnectedLocked(object);
                    break;
                }
                case 2: 
                case 3: {
                    String string2;
                    Object object3;
                    Object object4;
                    object = this.zzu;
                    if (object != null && (object4 = this.zza) != null) {
                        object3 = ((zzu)object4).zzc();
                        string2 = ((zzu)object4).zzb();
                        n3 = String.valueOf(object3).length();
                        n = String.valueOf(string2).length();
                        object4 = new StringBuilder(n3 + 70 + n);
                        ((StringBuilder)object4).append("Calling connect() while still connected, missing disconnect() for ");
                        ((StringBuilder)object4).append((String)object3);
                        ((StringBuilder)object4).append(" on ");
                        ((StringBuilder)object4).append(string2);
                        Log.e((String)"GmsClient", (String)((StringBuilder)object4).toString());
                        object3 = this.zzn;
                        object4 = this.zza.zzc();
                        Preconditions.checkNotNull(object4);
                        ((GmsClientSupervisor)object3).zzb((String)object4, this.zza.zzb(), this.zza.zza(), (ServiceConnection)object, this.zze(), this.zza.zzd());
                        this.zzd.incrementAndGet();
                    }
                    this.zzu = object3 = new zze(this, this.zzd.get());
                    object = this.zzv == 3 && this.getLocalStartServiceAction() != null ? new zzu(this.getContext().getPackageName(), this.getLocalStartServiceAction(), true, GmsClientSupervisor.getDefaultBindFlags(), false) : new zzu(this.getStartServicePackage(), this.getStartServiceAction(), false, GmsClientSupervisor.getDefaultBindFlags(), this.getUseDynamicLookup());
                    this.zza = object;
                    if (((zzu)object).zzd() && this.getMinApkVersion() < 17895000) {
                        object = String.valueOf(this.zza.zzc());
                        object = ((String)object).length() != 0 ? "Internal Error, the minimum apk version of this BaseGmsClient is too low to support dynamic lookup. Start service action: ".concat((String)object) : new String("Internal Error, the minimum apk version of this BaseGmsClient is too low to support dynamic lookup. Start service action: ");
                        object3 = new IllegalStateException((String)object);
                        throw object3;
                    }
                    GmsClientSupervisor gmsClientSupervisor = this.zzn;
                    String string3 = this.zza.zzc();
                    Preconditions.checkNotNull(string3);
                    String string4 = this.zza.zzb();
                    n = this.zza.zza();
                    string2 = this.zze();
                    bl = this.zza.zzd();
                    object = this.getBindServiceExecutor();
                    object4 = new zzn(string3, string4, n, bl);
                    if (gmsClientSupervisor.zzc((zzn)object4, (ServiceConnection)object3, string2, (Executor)object)) break;
                    object = this.zza.zzc();
                    object4 = this.zza.zzb();
                    n3 = String.valueOf(object).length();
                    n = String.valueOf(object4).length();
                    object3 = new StringBuilder(n3 + 34 + n);
                    ((StringBuilder)object3).append("unable to connect to service: ");
                    ((StringBuilder)object3).append((String)object);
                    ((StringBuilder)object3).append(" on ");
                    ((StringBuilder)object3).append((String)object4);
                    Log.w((String)"GmsClient", (String)((StringBuilder)object3).toString());
                    this.zzl(16, null, this.zzd.get());
                    break;
                }
                case 1: {
                    zze zze2 = this.zzu;
                    if (zze2 == null) break;
                    object = this.zzn;
                    String string5 = this.zza.zzc();
                    Preconditions.checkNotNull(string5);
                    ((GmsClientSupervisor)object).zzb(string5, this.zza.zzb(), this.zza.zza(), zze2, this.zze(), this.zza.zzd());
                    this.zzu = null;
                }
            }
            return;
        }
    }

    public void checkAvailabilityAndConnect() {
        int n = this.zzo.isGooglePlayServicesAvailable(this.zzl, this.getMinApkVersion());
        if (n != 0) {
            this.zzp(1, null);
            this.triggerNotAvailable(new LegacyClientCallbackAdapter(this), n, null);
            return;
        }
        this.connect(new LegacyClientCallbackAdapter(this));
    }

    protected final void checkConnected() {
        if (this.isConnected()) {
            return;
        }
        throw new IllegalStateException("Not connected. Call connect() and wait for onConnected() to be called.");
    }

    public void connect(ConnectionProgressReportCallbacks connectionProgressReportCallbacks) {
        Preconditions.checkNotNull(connectionProgressReportCallbacks, "Connection progress callbacks cannot be null.");
        this.zzc = connectionProgressReportCallbacks;
        this.zzp(2, null);
    }

    protected abstract T createServiceInterface(IBinder var1);

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public void disconnect() {
        this.zzd.incrementAndGet();
        ArrayList<zzc<?>> arrayList = this.zzt;
        // MONITORENTER : arrayList
        int n = this.zzt.size();
        for (int i = 0; i < n; ++i) {
            this.zzt.get(i).zzf();
        }
        this.zzt.clear();
        // MONITOREXIT : arrayList
        Object object = this.zzq;
        this.zzr = null;
        this.zzp(1, null);
        return;
        finally {
            // MONITORENTER : object
        }
    }

    public void disconnect(String string2) {
        this.zzk = string2;
        this.disconnect();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void dump(String object, FileDescriptor object2, PrintWriter object3, String[] object4) {
        Object object5;
        long l;
        Object object6;
        int n;
        object2 = this.zzp;
        synchronized (object2) {
            n = this.zzv;
            object4 = this.zzs;
        }
        object2 = this.zzq;
        synchronized (object2) {
            object6 = this.zzr;
        }
        ((PrintWriter)object3).append((CharSequence)object).append("mConnectState=");
        switch (n) {
            default: {
                ((PrintWriter)object3).print("UNKNOWN");
                break;
            }
            case 5: {
                ((PrintWriter)object3).print("DISCONNECTING");
                break;
            }
            case 4: {
                ((PrintWriter)object3).print("CONNECTED");
                break;
            }
            case 3: {
                ((PrintWriter)object3).print("LOCAL_CONNECTING");
                break;
            }
            case 2: {
                ((PrintWriter)object3).print("REMOTE_CONNECTING");
                break;
            }
            case 1: {
                ((PrintWriter)object3).print("DISCONNECTED");
            }
        }
        ((PrintWriter)object3).append(" mService=");
        if (object4 == null) {
            ((PrintWriter)object3).append("null");
        } else {
            ((PrintWriter)object3).append(this.getServiceDescriptor()).append("@").append(Integer.toHexString(System.identityHashCode(object4.asBinder())));
        }
        ((PrintWriter)object3).append(" mServiceBroker=");
        if (object6 == null) {
            ((PrintWriter)object3).println("null");
        } else {
            ((PrintWriter)object3).append("IGmsServiceBroker@").println(Integer.toHexString(System.identityHashCode(object6.asBinder())));
        }
        object2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);
        if (this.zzh > 0L) {
            object4 = ((PrintWriter)object3).append((CharSequence)object).append("lastConnectedTime=");
            l = this.zzh;
            object5 = ((DateFormat)object2).format(new Date(l));
            object6 = new StringBuilder(String.valueOf(object5).length() + 21);
            ((StringBuilder)object6).append(l);
            ((StringBuilder)object6).append(" ");
            ((StringBuilder)object6).append((String)object5);
            ((PrintWriter)object4).println(((StringBuilder)object6).toString());
        }
        if (this.zzg > 0L) {
            ((PrintWriter)object3).append((CharSequence)object).append("lastSuspendedCause=");
            n = this.zzf;
            switch (n) {
                default: {
                    ((PrintWriter)object3).append(String.valueOf(n));
                    break;
                }
                case 3: {
                    ((PrintWriter)object3).append("CAUSE_DEAD_OBJECT_EXCEPTION");
                    break;
                }
                case 2: {
                    ((PrintWriter)object3).append("CAUSE_NETWORK_LOST");
                    break;
                }
                case 1: {
                    ((PrintWriter)object3).append("CAUSE_SERVICE_DISCONNECTED");
                }
            }
            object5 = ((PrintWriter)object3).append(" lastSuspendedTime=");
            l = this.zzg;
            object6 = ((DateFormat)object2).format(new Date(l));
            object4 = new StringBuilder(String.valueOf(object6).length() + 21);
            ((StringBuilder)object4).append(l);
            ((StringBuilder)object4).append(" ");
            ((StringBuilder)object4).append((String)object6);
            ((PrintWriter)object5).println(((StringBuilder)object4).toString());
        }
        if (this.zzj > 0L) {
            ((PrintWriter)object3).append((CharSequence)object).append("lastFailedStatus=").append(CommonStatusCodes.getStatusCodeString(this.zzi));
            object = ((PrintWriter)object3).append(" lastFailedTime=");
            l = this.zzj;
            object3 = ((DateFormat)object2).format(new Date(l));
            object2 = new StringBuilder(String.valueOf(object3).length() + 21);
            ((StringBuilder)object2).append(l);
            ((StringBuilder)object2).append(" ");
            ((StringBuilder)object2).append((String)object3);
            ((PrintWriter)object).println(((StringBuilder)object2).toString());
        }
    }

    protected boolean enableLocalFallback() {
        return false;
    }

    public Account getAccount() {
        return null;
    }

    public Feature[] getApiFeatures() {
        return zze;
    }

    public final Feature[] getAvailableFeatures() {
        zzj zzj2 = this.zzD;
        if (zzj2 == null) {
            return null;
        }
        return zzj2.zzb;
    }

    protected Executor getBindServiceExecutor() {
        return null;
    }

    public Bundle getConnectionHint() {
        return null;
    }

    public final Context getContext() {
        return this.zzl;
    }

    public String getEndpointPackageName() {
        zzu zzu2;
        if (this.isConnected() && (zzu2 = this.zza) != null) {
            return zzu2.zzb();
        }
        throw new RuntimeException("Failed to connect when checking package");
    }

    public int getGCoreServiceId() {
        return this.zzy;
    }

    protected Bundle getGetServiceRequestExtraArgs() {
        return new Bundle();
    }

    public String getLastDisconnectMessage() {
        return this.zzk;
    }

    protected String getLocalStartServiceAction() {
        return null;
    }

    public final Looper getLooper() {
        return this.zzm;
    }

    public int getMinApkVersion() {
        return GoogleApiAvailabilityLight.GOOGLE_PLAY_SERVICES_VERSION_CODE;
    }

    /*
     * WARNING - void declaration
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void getRemoteService(IAccountAccessor object, Set<Scope> object2) {
        void var1_4;
        block16: {
            Object object3 = this.getGetServiceRequestExtraArgs();
            GetServiceRequest getServiceRequest = new GetServiceRequest(this.zzy, this.zzA);
            getServiceRequest.zzd = this.zzl.getPackageName();
            getServiceRequest.zzg = object3;
            if (object2 != null) {
                getServiceRequest.zzf = object2.toArray(new Scope[object2.size()]);
            }
            if (this.requiresSignIn()) {
                object3 = this.getAccount();
                object2 = object3;
                if (object3 == null) {
                    object2 = new Account(DEFAULT_ACCOUNT, "com.google");
                }
                getServiceRequest.zzh = object2;
                if (object != null) {
                    getServiceRequest.zze = object.asBinder();
                }
            } else if (this.requiresAccount()) {
                getServiceRequest.zzh = this.getAccount();
            }
            getServiceRequest.zzi = zze;
            getServiceRequest.zzj = this.getApiFeatures();
            if (this.usesClientTelemetry()) {
                getServiceRequest.zzm = true;
            }
            try {
                object = this.zzq;
                synchronized (object) {
                    object3 = this.zzr;
                    if (object3 == null) break block15;
                }
            }
            catch (RuntimeException runtimeException) {
                break block16;
            }
            catch (RemoteException remoteException) {
                // empty catch block
                break block16;
            }
            catch (SecurityException securityException) {
                throw securityException;
            }
            catch (DeadObjectException deadObjectException) {
                Log.w((String)"GmsClient", (String)"IGmsServiceBroker.getService failed", (Throwable)deadObjectException);
                this.triggerConnectionSuspended(3);
                return;
            }
            {
                block17: {
                    block15: {
                        object2 = new zzd(this, this.zzd.get());
                        object3.getService((IGmsCallbacks)object2, getServiceRequest);
                        break block17;
                    }
                    Log.w((String)"GmsClient", (String)"mServiceBroker is null, client disconnected");
                }
                return;
            }
        }
        Log.w((String)"GmsClient", (String)"IGmsServiceBroker.getService failed", (Throwable)var1_4);
        this.onPostInitHandler(8, null, null, this.zzd.get());
    }

    protected Set<Scope> getScopes() {
        return Collections.emptySet();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final T getService() throws DeadObjectException {
        Object object = this.zzp;
        synchronized (object) {
            if (this.zzv != 5) {
                this.checkConnected();
                T t = this.zzs;
                Preconditions.checkNotNull(t, "Client is connected but service is null");
                return t;
            }
            DeadObjectException deadObjectException = new DeadObjectException();
            throw deadObjectException;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public IBinder getServiceBrokerBinder() {
        Object object = this.zzq;
        synchronized (object) {
            IGmsServiceBroker iGmsServiceBroker = this.zzr;
            if (iGmsServiceBroker != null) return iGmsServiceBroker.asBinder();
            return null;
        }
    }

    protected abstract String getServiceDescriptor();

    public Intent getSignInIntent() {
        throw new UnsupportedOperationException("Not a sign in API");
    }

    protected abstract String getStartServiceAction();

    protected String getStartServicePackage() {
        return "com.google.android.gms";
    }

    public ConnectionTelemetryConfiguration getTelemetryConfiguration() {
        zzj zzj2 = this.zzD;
        if (zzj2 == null) {
            return null;
        }
        return zzj2.zzd;
    }

    protected boolean getUseDynamicLookup() {
        return this.getMinApkVersion() >= 211700000;
    }

    public boolean hasConnectionInfo() {
        return this.zzD != null;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean isConnected() {
        Object object = this.zzp;
        synchronized (object) {
            if (this.zzv != 4) return false;
            return true;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean isConnecting() {
        Object object = this.zzp;
        synchronized (object) {
            boolean bl;
            int n = this.zzv;
            boolean bl2 = bl = true;
            if (n == 2) return bl2;
            if (n != 3) return false;
            return bl;
        }
    }

    protected void onConnectedLocked(T t) {
        this.zzh = System.currentTimeMillis();
    }

    protected void onConnectionFailed(ConnectionResult connectionResult) {
        this.zzi = connectionResult.getErrorCode();
        this.zzj = System.currentTimeMillis();
    }

    protected void onConnectionSuspended(int n) {
        this.zzf = n;
        this.zzg = System.currentTimeMillis();
    }

    protected void onPostInitHandler(int n, IBinder iBinder, Bundle bundle, int n2) {
        Handler handler = this.zzb;
        handler.sendMessage(handler.obtainMessage(1, n2, -1, (Object)new zzf(this, n, iBinder, bundle)));
    }

    public void onUserSignOut(SignOutCallbacks signOutCallbacks) {
        signOutCallbacks.onSignOutComplete();
    }

    public boolean providesSignIn() {
        return false;
    }

    public boolean requiresAccount() {
        return false;
    }

    public boolean requiresGooglePlayServices() {
        return true;
    }

    public boolean requiresSignIn() {
        return false;
    }

    public void setAttributionTag(String string2) {
        this.zzA = string2;
    }

    public void triggerConnectionSuspended(int n) {
        Handler handler = this.zzb;
        handler.sendMessage(handler.obtainMessage(6, this.zzd.get(), n));
    }

    protected void triggerNotAvailable(ConnectionProgressReportCallbacks connectionProgressReportCallbacks, int n, PendingIntent pendingIntent) {
        Preconditions.checkNotNull(connectionProgressReportCallbacks, "Connection progress callbacks cannot be null.");
        this.zzc = connectionProgressReportCallbacks;
        connectionProgressReportCallbacks = this.zzb;
        connectionProgressReportCallbacks.sendMessage(connectionProgressReportCallbacks.obtainMessage(3, this.zzd.get(), n, pendingIntent));
    }

    public boolean usesClientTelemetry() {
        return false;
    }

    protected final String zze() {
        String string2;
        String string3 = string2 = this.zzz;
        if (string2 == null) {
            string3 = this.zzl.getClass().getName();
        }
        return string3;
    }

    protected final void zzl(int n, Bundle bundle, int n2) {
        bundle = this.zzb;
        bundle.sendMessage(bundle.obtainMessage(7, n2, -1, (Object)new zzg(this, n, null)));
    }

    public static interface BaseConnectionCallbacks {
        public static final int CAUSE_DEAD_OBJECT_EXCEPTION = 3;
        public static final int CAUSE_SERVICE_DISCONNECTED = 1;

        public void onConnected(Bundle var1);

        public void onConnectionSuspended(int var1);
    }

    public static interface BaseOnConnectionFailedListener {
        public void onConnectionFailed(ConnectionResult var1);
    }

    public static interface ConnectionProgressReportCallbacks {
        public void onReportServiceBinding(ConnectionResult var1);
    }

    protected class LegacyClientCallbackAdapter
    implements ConnectionProgressReportCallbacks {
        final BaseGmsClient zza;

        public LegacyClientCallbackAdapter(BaseGmsClient baseGmsClient) {
            this.zza = baseGmsClient;
        }

        @Override
        public final void onReportServiceBinding(ConnectionResult object) {
            if (((ConnectionResult)object).isSuccess()) {
                object = this.zza;
                ((BaseGmsClient)object).getRemoteService(null, ((BaseGmsClient)object).getScopes());
                return;
            }
            if (this.zza.zzx != null) {
                this.zza.zzx.onConnectionFailed((ConnectionResult)object);
            }
        }
    }

    public static interface SignOutCallbacks {
        public void onSignOutComplete();
    }
}

