/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.pm.PackageInfo
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.util.Log
 *  javax.annotation.Nullable
 */
package com.google.android.gms.common;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import com.google.android.gms.common.GooglePlayServicesUtilLight;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.zzi;
import com.google.android.gms.common.zzj;
import com.google.android.gms.common.zzl;
import com.google.android.gms.common.zzm;
import com.google.android.gms.common.zzw;
import javax.annotation.Nullable;

public class GoogleSignatureVerifier {
    @Nullable
    private static GoogleSignatureVerifier zza;
    private final Context zzb;
    private volatile String zzc;

    public GoogleSignatureVerifier(Context context) {
        this.zzb = context.getApplicationContext();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static GoogleSignatureVerifier getInstance(Context context) {
        Preconditions.checkNotNull(context);
        synchronized (GoogleSignatureVerifier.class) {
            if (zza == null) {
                GoogleSignatureVerifier googleSignatureVerifier;
                zzm.zzd(context);
                zza = googleSignatureVerifier = new GoogleSignatureVerifier(context);
            }
            return zza;
        }
    }

    @Nullable
    static final zzi zza(PackageInfo object, zzi ... zziArray) {
        if (object.signatures == null) {
            return null;
        }
        if (object.signatures.length != 1) {
            Log.w((String)"GoogleSignatureVerifier", (String)"Package has more than one signature.");
            return null;
        }
        object = object.signatures;
        object = new zzj(object[0].toByteArray());
        for (int i = 0; i < zziArray.length; ++i) {
            if (!zziArray[i].equals(object)) continue;
            return zziArray[i];
        }
        return null;
    }

    public static final boolean zzb(PackageInfo object, boolean bl) {
        return object != null && object.signatures != null && (object = bl ? GoogleSignatureVerifier.zza(object, zzl.zza) : GoogleSignatureVerifier.zza(object, new zzi[]{zzl.zza[0]})) != null;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private final zzw zzc(String string2, boolean bl, boolean bl2) {
        zzw zzw2;
        if (string2 == null) {
            return zzw.zzc("null pkg");
        }
        if (string2.equals(this.zzc)) return zzw.zzb();
        if (zzm.zze()) {
            zzw2 = zzm.zzb(string2, GooglePlayServicesUtilLight.honorsDebugCertificates(this.zzb), false, false);
        } else {
            PackageInfo packageInfo;
            try {
                packageInfo = this.zzb.getPackageManager().getPackageInfo(string2, 64);
            }
            catch (PackageManager.NameNotFoundException nameNotFoundException) {
                if (string2.length() != 0) {
                    string2 = "no pkg ".concat(string2);
                    return zzw.zzd(string2, nameNotFoundException);
                }
                string2 = new String("no pkg ");
                return zzw.zzd(string2, nameNotFoundException);
            }
            bl = GooglePlayServicesUtilLight.honorsDebugCertificates(this.zzb);
            if (packageInfo == null) {
                zzw2 = zzw.zzc("null pkg");
            } else if (packageInfo.signatures != null && packageInfo.signatures.length == 1) {
                zzj zzj2 = new zzj(packageInfo.signatures[0].toByteArray());
                String string3 = packageInfo.packageName;
                zzw2 = zzm.zza(string3, zzj2, bl, false);
                if (zzw2.zza && packageInfo.applicationInfo != null && (packageInfo.applicationInfo.flags & 2) != 0 && zzm.zza((String)string3, (zzi)zzj2, (boolean)false, (boolean)true).zza) {
                    zzw2 = zzw.zzc("debuggable release cert app rejected");
                }
            } else {
                zzw2 = zzw.zzc("single cert required");
            }
        }
        if (!zzw2.zza) return zzw2;
        this.zzc = string2;
        return zzw2;
    }

    public boolean isGooglePublicSignedPackage(PackageInfo packageInfo) {
        if (packageInfo == null) {
            return false;
        }
        if (GoogleSignatureVerifier.zzb(packageInfo, false)) {
            return true;
        }
        if (GoogleSignatureVerifier.zzb(packageInfo, true)) {
            if (GooglePlayServicesUtilLight.honorsDebugCertificates(this.zzb)) {
                return true;
            }
            Log.w((String)"GoogleSignatureVerifier", (String)"Test-keys aren't accepted on this build.");
        }
        return false;
    }

    public boolean isPackageGoogleSigned(String object) {
        object = this.zzc((String)object, false, false);
        ((zzw)object).zze();
        return ((zzw)object).zza;
    }

    public boolean isUidGoogleSigned(int n) {
        zzw zzw2;
        block4: {
            int n2;
            String[] stringArray = this.zzb.getPackageManager().getPackagesForUid(n);
            if (stringArray != null && (n2 = stringArray.length) != 0) {
                zzw2 = null;
                for (n = 0; n < n2; ++n) {
                    zzw2 = this.zzc(stringArray[n], false, false);
                    if (!zzw2.zza) {
                        continue;
                    }
                    break block4;
                }
                Preconditions.checkNotNull(zzw2);
            } else {
                zzw2 = zzw.zzc("no pkgs");
            }
        }
        zzw2.zze();
        return zzw2.zza;
    }
}

