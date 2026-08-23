/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.os.IBinder
 *  android.os.RemoteException
 *  android.os.StrictMode
 *  android.os.StrictMode$ThreadPolicy
 *  android.util.Log
 */
package com.google.android.gms.common;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.StrictMode;
import android.util.Log;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.zzae;
import com.google.android.gms.common.internal.zzaf;
import com.google.android.gms.common.util.AndroidUtilsLight;
import com.google.android.gms.common.util.Hex;
import com.google.android.gms.common.zzd;
import com.google.android.gms.common.zze;
import com.google.android.gms.common.zzf;
import com.google.android.gms.common.zzg;
import com.google.android.gms.common.zzh;
import com.google.android.gms.common.zzi;
import com.google.android.gms.common.zzk;
import com.google.android.gms.common.zzn;
import com.google.android.gms.common.zzq;
import com.google.android.gms.common.zzs;
import com.google.android.gms.common.zzv;
import com.google.android.gms.common.zzw;
import com.google.android.gms.dynamic.ObjectWrapper;
import com.google.android.gms.dynamite.DynamiteModule;
import java.security.MessageDigest;

final class zzm {
    static final zzk zza = new zze(zzi.zze("0\u0082\u0005\u00c80\u0082\u0003\u00b0\u00a0\u0003\u0002\u0001\u0002\u0002\u0014\u0010\u008ae\bs\u00f9/\u008eQ\u00ed"));
    static final zzk zzb = new zzf(zzi.zze("0\u0082\u0006\u00040\u0082\u0003\u00ec\u00a0\u0003\u0002\u0001\u0002\u0002\u0014\u0003\u00a3\u00b2\u00ad\u00d7\u00e1r\u00cak\u00ec"));
    static final zzk zzc = new zzg(zzi.zze("0\u0082\u0004C0\u0082\u0003+\u00a0\u0003\u0002\u0001\u0002\u0002\t\u0000\u00c2\u00e0\u0087FdJ0\u008d0"));
    static final zzk zzd = new zzh(zzi.zze("0\u0082\u0004\u00a80\u0082\u0003\u0090\u00a0\u0003\u0002\u0001\u0002\u0002\t\u0000\u00d5\u0085\u00b8l}\u00d3N\u00f50"));
    private static volatile zzaf zze;
    private static final Object zzf;
    private static Context zzg;

    static {
        zzf = new Object();
    }

    static zzw zza(String object, zzi zzi2, boolean bl, boolean bl2) {
        StrictMode.ThreadPolicy threadPolicy = StrictMode.allowThreadDiskReads();
        try {
            object = zzm.zzf((String)object, zzi2, bl, bl2);
            return object;
        }
        finally {
            StrictMode.setThreadPolicy((StrictMode.ThreadPolicy)threadPolicy);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    static zzw zzb(String object, boolean bl, boolean bl2, boolean bl3) {
        StrictMode.ThreadPolicy threadPolicy = StrictMode.allowThreadDiskReads();
        try {
            zzq zzq2;
            Object object2;
            block9: {
                Preconditions.checkNotNull(zzg);
                try {
                    zzm.zzg();
                    object2 = new zzn((String)object, bl, false, (IBinder)ObjectWrapper.wrap(zzg), false);
                }
                catch (DynamiteModule.LoadingException loadingException) {
                    Log.e((String)"GoogleCertificates", (String)"Failed to get Google certificates from remote", (Throwable)loadingException);
                    object = String.valueOf(loadingException.getMessage());
                    object = ((String)object).length() != 0 ? "module init: ".concat((String)object) : new String("module init: ");
                    object = zzw.zzd((String)object, loadingException);
                    return object;
                }
                try {
                    zzq2 = zze.zze((zzn)object2);
                    if (!zzq2.zzb()) break block9;
                }
                catch (RemoteException remoteException) {
                    Log.e((String)"GoogleCertificates", (String)"Failed to get Google certificates from remote", (Throwable)remoteException);
                    object = zzw.zzd("module call", remoteException);
                    return object;
                }
                object = zzw.zzb();
                return object;
            }
            object = object2 = zzq2.zza();
            if (object2 == null) {
                object = "error checking package certificate";
            }
            if (zzq2.zzc() == 4) {
                object2 = new PackageManager.NameNotFoundException();
                object = zzw.zzd((String)object, (Throwable)object2);
                return object;
            }
            object = zzw.zzc((String)object);
            return object;
        }
        finally {
            StrictMode.setThreadPolicy((StrictMode.ThreadPolicy)threadPolicy);
        }
    }

    static /* synthetic */ String zzc(boolean bl, String string2, zzi zzi2) throws Exception {
        boolean bl2 = !bl && zzm.zzf((String)string2, (zzi)zzi2, (boolean)true, (boolean)false).zza;
        String string3 = true != bl2 ? "not allowed" : "debug cert rejected";
        MessageDigest messageDigest = AndroidUtilsLight.zza("SHA-1");
        Preconditions.checkNotNull(messageDigest);
        return String.format("%s: pkg=%s, sha1=%s, atk=%s, ver=%s", string3, string2, Hex.bytesToStringLowercase(messageDigest.digest(zzi2.zzf())), bl, "12451000.false");
    }

    static void zzd(Context context) {
        synchronized (zzm.class) {
            block6: {
                block7: {
                    if (zzg != null) break block6;
                    if (context == null) break block7;
                    zzg = context.getApplicationContext();
                }
                return;
            }
            Log.w((String)"GoogleCertificates", (String)"GoogleCertificates has been initialized already");
            return;
            finally {
            }
        }
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * WARNING - void declaration
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    static boolean zze() {
        Throwable throwable2;
        StrictMode.ThreadPolicy threadPolicy;
        block5: {
            boolean bl;
            threadPolicy = StrictMode.allowThreadDiskReads();
            try {
                zzm.zzg();
                bl = zze.zzg();
            }
            catch (Throwable throwable2) {
                break block5;
            }
            catch (RemoteException remoteException) {
            }
            catch (DynamiteModule.LoadingException loadingException) {
                // empty catch block
            }
            StrictMode.setThreadPolicy((StrictMode.ThreadPolicy)threadPolicy);
            return bl;
            {
                void var1_5;
                Log.e((String)"GoogleCertificates", (String)"Failed to get Google certificates from remote", (Throwable)var1_5);
            }
            StrictMode.setThreadPolicy((StrictMode.ThreadPolicy)threadPolicy);
            return false;
        }
        StrictMode.setThreadPolicy((StrictMode.ThreadPolicy)threadPolicy);
        throw throwable2;
    }

    private static zzw zzf(String string2, zzi zzi2, boolean bl, boolean bl2) {
        block4: {
            try {
                zzm.zzg();
            }
            catch (DynamiteModule.LoadingException loadingException) {
                Log.e((String)"GoogleCertificates", (String)"Failed to get Google certificates from remote", (Throwable)loadingException);
                string2 = String.valueOf(loadingException.getMessage());
                string2 = string2.length() != 0 ? "module init: ".concat(string2) : new String("module init: ");
                return zzw.zzd(string2, loadingException);
            }
            Preconditions.checkNotNull(zzg);
            zzs zzs2 = new zzs(string2, zzi2, bl, bl2);
            try {
                bl2 = zze.zzf(zzs2, ObjectWrapper.wrap(zzg.getPackageManager()));
                if (!bl2) break block4;
            }
            catch (RemoteException remoteException) {
                Log.e((String)"GoogleCertificates", (String)"Failed to get Google certificates from remote", (Throwable)remoteException);
                return zzw.zzd("module call", remoteException);
            }
            return zzw.zzb();
        }
        return new zzv(new zzd(bl, string2, zzi2), null);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static void zzg() throws DynamiteModule.LoadingException {
        if (zze != null) {
            return;
        }
        Preconditions.checkNotNull(zzg);
        Object object = zzf;
        synchronized (object) {
            if (zze == null) {
                zze = zzae.zzb(DynamiteModule.load(zzg, DynamiteModule.PREFER_HIGHEST_OR_LOCAL_VERSION_NO_FORCE_STAGING, "com.google.android.gms.googlecertificates").instantiate("com.google.android.gms.common.GoogleCertificatesImpl"));
            }
            return;
        }
    }
}

