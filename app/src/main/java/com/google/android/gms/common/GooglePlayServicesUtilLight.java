/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.NotificationManager
 *  android.app.PendingIntent
 *  android.content.Context
 *  android.content.Intent
 *  android.content.pm.ApplicationInfo
 *  android.content.pm.PackageInfo
 *  android.content.pm.PackageInstaller$SessionInfo
 *  android.content.pm.PackageManager
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.content.res.Resources
 *  android.os.UserManager
 *  android.util.Log
 */
package com.google.android.gms.common;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.UserManager;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.common.GooglePlayServicesIncorrectManifestValueException;
import com.google.android.gms.common.GooglePlayServicesMissingManifestValueException;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GoogleSignatureVerifier;
import com.google.android.gms.common.R;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.zzag;
import com.google.android.gms.common.util.ClientLibraryUtils;
import com.google.android.gms.common.util.DeviceProperties;
import com.google.android.gms.common.util.PlatformVersion;
import com.google.android.gms.common.util.UidVerifier;
import com.google.android.gms.common.util.zza;
import com.google.android.gms.common.wrappers.Wrappers;
import java.util.concurrent.atomic.AtomicBoolean;

public class GooglePlayServicesUtilLight {
    static final int GMS_AVAILABILITY_NOTIFICATION_ID = 10436;
    static final int GMS_GENERAL_ERROR_NOTIFICATION_ID = 39789;
    public static final String GOOGLE_PLAY_GAMES_PACKAGE = "com.google.android.play.games";
    @Deprecated
    public static final String GOOGLE_PLAY_SERVICES_PACKAGE = "com.google.android.gms";
    @Deprecated
    public static final int GOOGLE_PLAY_SERVICES_VERSION_CODE = 12451000;
    public static final String GOOGLE_PLAY_STORE_PACKAGE = "com.android.vending";
    static final AtomicBoolean sCanceledAvailabilityNotification;
    static boolean zza;
    private static boolean zzb;
    private static final AtomicBoolean zzc;

    static {
        zzb = false;
        zza = false;
        sCanceledAvailabilityNotification = new AtomicBoolean();
        zzc = new AtomicBoolean();
    }

    GooglePlayServicesUtilLight() {
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Deprecated
    public static void cancelAvailabilityErrorNotifications(Context context) {
        if (sCanceledAvailabilityNotification.getAndSet(true)) {
            return;
        }
        try {
            context = (NotificationManager)context.getSystemService("notification");
            if (context == null) return;
        }
        catch (SecurityException securityException) {
            return;
        }
        context.cancel(10436);
    }

    public static void enableUsingApkIndependentContext() {
        zzc.set(true);
    }

    @Deprecated
    public static void ensurePlayServicesAvailable(Context object, int n) throws GooglePlayServicesRepairableException, GooglePlayServicesNotAvailableException {
        n = GoogleApiAvailabilityLight.getInstance().isGooglePlayServicesAvailable((Context)object, n);
        if (n != 0) {
            Intent intent = GoogleApiAvailabilityLight.getInstance().getErrorResolutionIntent((Context)object, n, "e");
            object = new StringBuilder(57);
            ((StringBuilder)object).append("GooglePlayServices not available due to error ");
            ((StringBuilder)object).append(n);
            Log.e((String)"GooglePlayServicesUtil", (String)((StringBuilder)object).toString());
            if (intent == null) {
                throw new GooglePlayServicesNotAvailableException(n);
            }
            throw new GooglePlayServicesRepairableException(n, "Google Play Services not available", intent);
        }
    }

    @Deprecated
    public static int getApkVersion(Context context) {
        try {
            context = context.getPackageManager().getPackageInfo(GOOGLE_PLAY_SERVICES_PACKAGE, 0);
            return context.versionCode;
        }
        catch (PackageManager.NameNotFoundException nameNotFoundException) {
            Log.w((String)"GooglePlayServicesUtil", (String)"Google Play services is missing.");
            return 0;
        }
    }

    @Deprecated
    public static int getClientVersion(Context context) {
        Preconditions.checkState(true);
        return ClientLibraryUtils.getClientVersion(context, context.getPackageName());
    }

    @Deprecated
    public static PendingIntent getErrorPendingIntent(int n, Context context, int n2) {
        return GoogleApiAvailabilityLight.getInstance().getErrorResolutionPendingIntent(context, n, n2);
    }

    @Deprecated
    public static String getErrorString(int n) {
        return ConnectionResult.zza(n);
    }

    @Deprecated
    public static Intent getGooglePlayServicesAvailabilityRecoveryIntent(int n) {
        return GoogleApiAvailabilityLight.getInstance().getErrorResolutionIntent(null, n, null);
    }

    public static Context getRemoteContext(Context context) {
        try {
            context = context.createPackageContext(GOOGLE_PLAY_SERVICES_PACKAGE, 3);
            return context;
        }
        catch (PackageManager.NameNotFoundException nameNotFoundException) {
            return null;
        }
    }

    public static Resources getRemoteResource(Context context) {
        try {
            context = context.getPackageManager().getResourcesForApplication(GOOGLE_PLAY_SERVICES_PACKAGE);
            return context;
        }
        catch (PackageManager.NameNotFoundException nameNotFoundException) {
            return null;
        }
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static boolean honorsDebugCertificates(Context context) {
        block5: {
            if (!zza) {
                Throwable throwable2222222;
                PackageInfo packageInfo = Wrappers.packageManager(context).getPackageInfo(GOOGLE_PLAY_SERVICES_PACKAGE, 64);
                GoogleSignatureVerifier.getInstance(context);
                zzb = packageInfo != null && !GoogleSignatureVerifier.zzb(packageInfo, false) && GoogleSignatureVerifier.zzb(packageInfo, true);
                zza = true;
                break block5;
                {
                    catch (Throwable throwable2222222) {
                    }
                    catch (PackageManager.NameNotFoundException nameNotFoundException) {}
                    {
                        Log.w((String)"GooglePlayServicesUtil", (String)"Cannot find Google Play services package name.", (Throwable)nameNotFoundException);
                        zza = true;
                        break block5;
                    }
                }
                zza = true;
                throw throwable2222222;
            }
        }
        if (zzb) return true;
        if (DeviceProperties.isUserBuild()) return false;
        return true;
    }

    @Deprecated
    public static int isGooglePlayServicesAvailable(Context context) {
        return GooglePlayServicesUtilLight.isGooglePlayServicesAvailable(context, GOOGLE_PLAY_SERVICES_VERSION_CODE);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Deprecated
    public static int isGooglePlayServicesAvailable(Context object, int n) {
        PackageInfo packageInfo;
        PackageInfo packageInfo2;
        int n2;
        try {
            object.getResources().getString(R.string.common_google_play_services_unknown_issue);
        }
        catch (Throwable throwable) {
            Log.e((String)"GooglePlayServicesUtil", (String)"The Google Play services resources were not found. Check your project configuration to ensure that the resources are included.");
        }
        if (!GOOGLE_PLAY_SERVICES_PACKAGE.equals(object.getPackageName()) && !zzc.get()) {
            n2 = zzag.zza((Context)object);
            if (n2 == 0) throw new GooglePlayServicesMissingManifestValueException();
            if (n2 != GOOGLE_PLAY_SERVICES_VERSION_CODE) throw new GooglePlayServicesIncorrectManifestValueException(n2);
        }
        boolean bl = DeviceProperties.isWearableWithoutPlayStore((Context)object);
        int n3 = 1;
        n2 = !bl && !DeviceProperties.zzb((Context)object) ? 1 : 0;
        bl = n >= 0;
        Preconditions.checkArgument(bl);
        String string2 = object.getPackageName();
        PackageManager packageManager = object.getPackageManager();
        if (n2 != 0) {
            try {
                packageInfo2 = packageManager.getPackageInfo(GOOGLE_PLAY_STORE_PACKAGE, 8256);
            }
            catch (PackageManager.NameNotFoundException nameNotFoundException) {
                Log.w((String)"GooglePlayServicesUtil", (String)String.valueOf(string2).concat(" requires the Google Play Store, but it is missing."));
                return 9;
            }
        } else {
            packageInfo2 = null;
        }
        try {
            packageInfo = packageManager.getPackageInfo(GOOGLE_PLAY_SERVICES_PACKAGE, 64);
        }
        catch (PackageManager.NameNotFoundException nameNotFoundException) {
            Log.w((String)"GooglePlayServicesUtil", (String)String.valueOf(string2).concat(" requires Google Play services, but they are missing."));
            return n3;
        }
        GoogleSignatureVerifier.getInstance((Context)object);
        if (!GoogleSignatureVerifier.zzb(packageInfo, true)) {
            Log.w((String)"GooglePlayServicesUtil", (String)String.valueOf(string2).concat(" requires Google Play services, but their signature is invalid."));
            return 9;
        }
        if (n2 != 0) {
            Preconditions.checkNotNull(packageInfo2);
            if (!GoogleSignatureVerifier.zzb(packageInfo2, true)) {
                Log.w((String)"GooglePlayServicesUtil", (String)String.valueOf(string2).concat(" requires Google Play Store, but its signature is invalid."));
                return 9;
            }
        }
        if (n2 != 0 && packageInfo2 != null && !packageInfo2.signatures[0].equals((Object)packageInfo.signatures[0])) {
            Log.w((String)"GooglePlayServicesUtil", (String)String.valueOf(string2).concat(" requires Google Play Store, but its signature doesn't match that of Google Play services."));
            return 9;
        }
        if (com.google.android.gms.common.util.zza.zza(packageInfo.versionCode) < com.google.android.gms.common.util.zza.zza(n)) {
            n2 = packageInfo.versionCode;
            object = new StringBuilder(String.valueOf(string2).length() + 82);
            ((StringBuilder)object).append("Google Play services out of date for ");
            ((StringBuilder)object).append(string2);
            ((StringBuilder)object).append(".  Requires ");
            ((StringBuilder)object).append(n);
            ((StringBuilder)object).append(" but found ");
            ((StringBuilder)object).append(n2);
            Log.w((String)"GooglePlayServicesUtil", (String)((StringBuilder)object).toString());
            return 2;
        }
        packageInfo2 = packageInfo.applicationInfo;
        object = packageInfo2;
        if (packageInfo2 == null) {
            try {
                object = packageManager.getApplicationInfo(GOOGLE_PLAY_SERVICES_PACKAGE, 0);
            }
            catch (PackageManager.NameNotFoundException nameNotFoundException) {
                Log.wtf((String)"GooglePlayServicesUtil", (String)String.valueOf(string2).concat(" requires Google Play services, but they're missing when getting application info."), (Throwable)nameNotFoundException);
                return n3;
            }
        }
        if (((ApplicationInfo)object).enabled) return 0;
        return 3;
    }

    @Deprecated
    public static boolean isGooglePlayServicesUid(Context context, int n) {
        return UidVerifier.isGooglePlayServicesUid(context, n);
    }

    @Deprecated
    public static boolean isPlayServicesPossiblyUpdating(Context context, int n) {
        if (n == 18) {
            return true;
        }
        if (n == 1) {
            return GooglePlayServicesUtilLight.zza(context, GOOGLE_PLAY_SERVICES_PACKAGE);
        }
        return false;
    }

    @Deprecated
    public static boolean isPlayStorePossiblyUpdating(Context context, int n) {
        if (n == 9) {
            return GooglePlayServicesUtilLight.zza(context, GOOGLE_PLAY_STORE_PACKAGE);
        }
        return false;
    }

    public static boolean isRestrictedUserProfile(Context context) {
        if (PlatformVersion.isAtLeastJellyBeanMR2()) {
            Object object = context.getSystemService("user");
            Preconditions.checkNotNull(object);
            context = ((UserManager)object).getApplicationRestrictions(context.getPackageName());
            if (context != null && "true".equals(context.getString("restricted_profile"))) {
                return true;
            }
        }
        return false;
    }

    @Deprecated
    public static boolean isSidewinderDevice(Context context) {
        return DeviceProperties.isSidewinder(context);
    }

    @Deprecated
    public static boolean isUserRecoverableError(int n) {
        switch (n) {
            default: {
                return false;
            }
            case 1: 
            case 2: 
            case 3: 
            case 9: 
        }
        return true;
    }

    @Deprecated
    public static boolean uidHasPackageName(Context context, int n, String string2) {
        return UidVerifier.uidHasPackageName(context, n, string2);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    static boolean zza(Context context, String string2) {
        Object object;
        boolean bl = string2.equals(GOOGLE_PLAY_SERVICES_PACKAGE);
        if (PlatformVersion.isAtLeastLollipop()) {
            try {
                object = context.getPackageManager().getPackageInstaller().getAllSessions();
                object = object.iterator();
            }
            catch (Exception exception) {
                return false;
            }
            while (object.hasNext()) {
                if (!string2.equals(((PackageInstaller.SessionInfo)object.next()).getAppPackageName())) continue;
                return true;
            }
        }
        object = context.getPackageManager();
        try {
            string2 = object.getApplicationInfo(string2, 8192);
            if (!bl) return ((ApplicationInfo)string2).enabled && !(bl = GooglePlayServicesUtilLight.isRestrictedUserProfile(context));
        }
        catch (PackageManager.NameNotFoundException nameNotFoundException) {
            return false;
        }
        return ((ApplicationInfo)string2).enabled;
    }
}

