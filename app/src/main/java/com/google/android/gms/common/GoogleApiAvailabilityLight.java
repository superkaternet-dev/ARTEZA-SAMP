/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.PendingIntent
 *  android.content.Context
 *  android.content.Intent
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.text.TextUtils
 */
package com.google.android.gms.common;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtilLight;
import com.google.android.gms.common.internal.zzt;
import com.google.android.gms.common.util.DeviceProperties;
import com.google.android.gms.common.wrappers.Wrappers;
import com.google.android.gms.internal.common.zzd;

public class GoogleApiAvailabilityLight {
    public static final String GOOGLE_PLAY_SERVICES_PACKAGE = "com.google.android.gms";
    public static final int GOOGLE_PLAY_SERVICES_VERSION_CODE = GooglePlayServicesUtilLight.GOOGLE_PLAY_SERVICES_VERSION_CODE;
    public static final String GOOGLE_PLAY_STORE_PACKAGE = "com.android.vending";
    static final String TRACKING_SOURCE_DIALOG = "d";
    static final String TRACKING_SOURCE_NOTIFICATION = "n";
    private static final GoogleApiAvailabilityLight zza = new GoogleApiAvailabilityLight();

    GoogleApiAvailabilityLight() {
    }

    public static GoogleApiAvailabilityLight getInstance() {
        return zza;
    }

    public void cancelAvailabilityErrorNotifications(Context context) {
        GooglePlayServicesUtilLight.cancelAvailabilityErrorNotifications(context);
    }

    public int getApkVersion(Context context) {
        return GooglePlayServicesUtilLight.getApkVersion(context);
    }

    public int getClientVersion(Context context) {
        return GooglePlayServicesUtilLight.getClientVersion(context);
    }

    @Deprecated
    public Intent getErrorResolutionIntent(int n) {
        return this.getErrorResolutionIntent(null, n, null);
    }

    public Intent getErrorResolutionIntent(Context context, int n, String string2) {
        switch (n) {
            default: {
                return null;
            }
            case 3: {
                return zzt.zzc(GOOGLE_PLAY_SERVICES_PACKAGE);
            }
            case 1: 
            case 2: 
        }
        if (context != null && DeviceProperties.isWearableWithoutPlayStore(context)) {
            return zzt.zza();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("gcore_");
        stringBuilder.append(GOOGLE_PLAY_SERVICES_VERSION_CODE);
        stringBuilder.append("-");
        if (!TextUtils.isEmpty((CharSequence)string2)) {
            stringBuilder.append(string2);
        }
        stringBuilder.append("-");
        if (context != null) {
            stringBuilder.append(context.getPackageName());
        }
        stringBuilder.append("-");
        if (context != null) {
            try {
                stringBuilder.append(Wrappers.packageManager((Context)context).getPackageInfo((String)context.getPackageName(), (int)0).versionCode);
            }
            catch (PackageManager.NameNotFoundException nameNotFoundException) {
                // empty catch block
            }
        }
        return zzt.zzb(GOOGLE_PLAY_SERVICES_PACKAGE, stringBuilder.toString());
    }

    public PendingIntent getErrorResolutionPendingIntent(Context context, int n, int n2) {
        return this.getErrorResolutionPendingIntent(context, n, n2, null);
    }

    public PendingIntent getErrorResolutionPendingIntent(Context context, int n, int n2, String string2) {
        if ((string2 = this.getErrorResolutionIntent(context, n, string2)) == null) {
            return null;
        }
        return zzd.zza(context, n2, (Intent)string2, zzd.zza | 0x8000000);
    }

    public String getErrorString(int n) {
        return GooglePlayServicesUtilLight.getErrorString(n);
    }

    public int isGooglePlayServicesAvailable(Context context) {
        return this.isGooglePlayServicesAvailable(context, GOOGLE_PLAY_SERVICES_VERSION_CODE);
    }

    public int isGooglePlayServicesAvailable(Context context, int n) {
        if (GooglePlayServicesUtilLight.isPlayServicesPossiblyUpdating(context, n = GooglePlayServicesUtilLight.isGooglePlayServicesAvailable(context, n))) {
            return 18;
        }
        return n;
    }

    public boolean isPlayServicesPossiblyUpdating(Context context, int n) {
        return GooglePlayServicesUtilLight.isPlayServicesPossiblyUpdating(context, n);
    }

    public boolean isPlayStorePossiblyUpdating(Context context, int n) {
        return GooglePlayServicesUtilLight.isPlayStorePossiblyUpdating(context, n);
    }

    public boolean isUninstalledAppPossiblyUpdating(Context context, String string2) {
        return GooglePlayServicesUtilLight.zza(context, string2);
    }

    public boolean isUserResolvableError(int n) {
        return GooglePlayServicesUtilLight.isUserRecoverableError(n);
    }

    public void verifyGooglePlayServicesIsAvailable(Context context, int n) throws GooglePlayServicesRepairableException, GooglePlayServicesNotAvailableException {
        GooglePlayServicesUtilLight.ensurePlayServicesAvailable(context, n);
    }
}

