/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 *  android.os.SystemClock
 *  android.util.Log
 */
package com.google.android.gms.security;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtilLight;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.dynamite.DynamiteModule;
import com.google.android.gms.security.zza;
import java.lang.reflect.Method;

public class ProviderInstaller {
    public static final String PROVIDER_NAME = "GmsCore_OpenSSL";
    private static final GoogleApiAvailabilityLight zza = GoogleApiAvailabilityLight.getInstance();
    private static final Object zzb = new Object();
    private static Method zzc = null;
    private static Method zzd = null;

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void installIfNeeded(Context object) throws GooglePlayServicesRepairableException, GooglePlayServicesNotAvailableException {
        Preconditions.checkNotNull(object, "Context must not be null");
        zza.verifyGooglePlayServicesIsAvailable((Context)object, 11925000);
        Object object2 = zzb;
        synchronized (object2) {
            Object object3;
            long l = SystemClock.elapsedRealtime();
            try {
                object3 = DynamiteModule.load(object, DynamiteModule.PREFER_HIGHEST_OR_LOCAL_VERSION_NO_FORCE_STAGING, "com.google.android.gms.providerinstaller.dynamite").getModuleContext();
            }
            catch (DynamiteModule.LoadingException loadingException) {
                object3 = String.valueOf(loadingException.getMessage());
                object3 = ((String)object3).length() != 0 ? "Failed to load providerinstaller module: ".concat((String)object3) : new String("Failed to load providerinstaller module: ");
                Log.w((String)"ProviderInstaller", (String)object3);
                object3 = null;
            }
            if (object3 != null) {
                ProviderInstaller.zzc((Context)object3, object, "com.google.android.gms.providerinstaller.ProviderInstallerImpl");
                return;
            }
            long l2 = SystemClock.elapsedRealtime();
            Context context = GooglePlayServicesUtilLight.getRemoteContext(object);
            if (context != null) {
                try {
                    if (zzd == null) {
                        zzd = ProviderInstaller.zzb(context, "com.google.android.gms.common.security.ProviderInstallerImpl", "reportRequestStats", new Class[]{Context.class, Long.TYPE, Long.TYPE});
                    }
                    zzd.invoke(null, object, l, l2);
                }
                catch (Exception exception) {
                    String string2 = String.valueOf(exception.getMessage());
                    string2 = string2.length() != 0 ? "Failed to report request stats: ".concat(string2) : new String("Failed to report request stats: ");
                    Log.w((String)"ProviderInstaller", (String)string2);
                }
            }
            if (context != null) {
                ProviderInstaller.zzc(context, object, "com.google.android.gms.common.security.ProviderInstallerImpl");
                return;
            }
            Log.e((String)"ProviderInstaller", (String)"Failed to get remote context");
            object = new GooglePlayServicesNotAvailableException(8);
            throw object;
        }
    }

    public static void installIfNeededAsync(Context context, ProviderInstallListener providerInstallListener) {
        Preconditions.checkNotNull(context, "Context must not be null");
        Preconditions.checkNotNull(providerInstallListener, "Listener must not be null");
        Preconditions.checkMainThread("Must be called on the UI thread");
        new zza(context, providerInstallListener).execute(new Void[0]);
    }

    static /* bridge */ /* synthetic */ GoogleApiAvailabilityLight zza() {
        return zza;
    }

    private static Method zzb(Context context, String string2, String string3, Class[] classArray) throws ClassNotFoundException, NoSuchMethodException {
        return context.getClassLoader().loadClass(string2).getMethod(string3, classArray);
    }

    private static void zzc(Context context, Context object, String string2) throws GooglePlayServicesNotAvailableException {
        try {
            if (zzc == null) {
                zzc = ProviderInstaller.zzb(context, string2, "insertProvider", new Class[]{Context.class});
            }
            zzc.invoke(null, context);
            return;
        }
        catch (Exception exception) {
            object = exception.getCause();
            if (Log.isLoggable((String)"ProviderInstaller", (int)6)) {
                String string3 = object == null ? exception.getMessage() : ((Throwable)object).getMessage();
                string3 = String.valueOf(string3);
                string3 = string3.length() != 0 ? "Failed to install provider: ".concat(string3) : new String("Failed to install provider: ");
                Log.e((String)"ProviderInstaller", (String)string3);
            }
            throw new GooglePlayServicesNotAvailableException(8);
        }
    }

    public static interface ProviderInstallListener {
        public void onProviderInstallFailed(int var1, Intent var2);

        public void onProviderInstalled();
    }
}

