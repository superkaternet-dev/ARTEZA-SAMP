/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.pm.PackageInfo
 *  android.content.pm.PackageManager
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.util.Log
 */
package com.google.android.gms.common.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import com.google.android.gms.common.GoogleSignatureVerifier;
import com.google.android.gms.common.wrappers.Wrappers;

public final class UidVerifier {
    private UidVerifier() {
    }

    public static boolean isGooglePlayServicesUid(Context context, int n) {
        if (!UidVerifier.uidHasPackageName(context, n, "com.google.android.gms")) {
            return false;
        }
        PackageManager packageManager = context.getPackageManager();
        try {
            packageManager = packageManager.getPackageInfo("com.google.android.gms", 64);
            return GoogleSignatureVerifier.getInstance(context).isGooglePublicSignedPackage((PackageInfo)packageManager);
        }
        catch (PackageManager.NameNotFoundException nameNotFoundException) {
            if (Log.isLoggable((String)"UidVerifier", (int)3)) {
                Log.d((String)"UidVerifier", (String)"Package manager can't find google play services package, defaulting to false");
            }
            return false;
        }
    }

    public static boolean uidHasPackageName(Context context, int n, String string2) {
        return Wrappers.packageManager(context).zza(n, string2);
    }
}

