/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.os.Build
 */
package com.google.android.gms.common.util;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import com.google.android.gms.common.GooglePlayServicesUtilLight;
import com.google.android.gms.common.util.PlatformVersion;

public final class DeviceProperties {
    private static Boolean zza;
    private static Boolean zzb;
    private static Boolean zzc;
    private static Boolean zzd;
    private static Boolean zze;
    private static Boolean zzf;
    private static Boolean zzg;
    private static Boolean zzh;
    private static Boolean zzi;
    private static Boolean zzj;
    private static Boolean zzk;
    private static Boolean zzl;

    private DeviceProperties() {
    }

    public static boolean isAuto(Context context) {
        context = context.getPackageManager();
        if (zzi == null) {
            boolean bl = PlatformVersion.isAtLeastO();
            boolean bl2 = false;
            if (bl && context.hasSystemFeature("android.hardware.type.automotive")) {
                bl2 = true;
            }
            zzi = bl2;
        }
        return zzi;
    }

    public static boolean isBstar(Context context) {
        if (zzl == null) {
            boolean bl = PlatformVersion.isAtLeastR();
            boolean bl2 = false;
            if (bl && context.getPackageManager().hasSystemFeature("com.google.android.play.feature.HPE_EXPERIENCE")) {
                bl2 = true;
            }
            zzl = bl2;
        }
        return zzl;
    }

    public static boolean isLatchsky(Context context) {
        if (zzf == null) {
            context = context.getPackageManager();
            boolean bl = context.hasSystemFeature("com.google.android.feature.services_updater");
            boolean bl2 = false;
            if (bl && context.hasSystemFeature("cn.google.services")) {
                bl2 = true;
            }
            zzf = bl2;
        }
        return zzf;
    }

    public static boolean isPhone(Context context) {
        if (zza == null) {
            boolean bl = DeviceProperties.isTablet(context);
            boolean bl2 = false;
            if (!(bl || DeviceProperties.isWearable(context) || DeviceProperties.zzb(context))) {
                if (zzh == null) {
                    zzh = context.getPackageManager().hasSystemFeature("org.chromium.arc");
                }
                if (!(zzh.booleanValue() || DeviceProperties.isAuto(context) || DeviceProperties.isTv(context))) {
                    if (zzk == null) {
                        zzk = context.getPackageManager().hasSystemFeature("com.google.android.feature.AMATI_EXPERIENCE");
                    }
                    if (!zzk.booleanValue() && !DeviceProperties.isBstar(context)) {
                        bl2 = true;
                    }
                }
            }
            zza = bl2;
        }
        return zza;
    }

    public static boolean isSevenInchTablet(Context context) {
        return DeviceProperties.zzc(context.getResources());
    }

    public static boolean isSidewinder(Context context) {
        return DeviceProperties.zza(context);
    }

    public static boolean isTablet(Context context) {
        return DeviceProperties.isTablet(context.getResources());
    }

    public static boolean isTablet(Resources resources) {
        boolean bl = false;
        if (resources == null) {
            return false;
        }
        if (zzb == null) {
            if ((resources.getConfiguration().screenLayout & 0xF) > 3) {
                bl = true;
            } else if (DeviceProperties.zzc(resources)) {
                bl = true;
            }
            zzb = bl;
        }
        return zzb;
    }

    public static boolean isTv(Context context) {
        context = context.getPackageManager();
        if (zzj == null) {
            boolean bl = context.hasSystemFeature("com.google.android.tv");
            boolean bl2 = true;
            if (!(bl || context.hasSystemFeature("android.hardware.type.television") || context.hasSystemFeature("android.software.leanback"))) {
                bl2 = false;
            }
            zzj = bl2;
        }
        return zzj;
    }

    public static boolean isUserBuild() {
        int n = GooglePlayServicesUtilLight.GOOGLE_PLAY_SERVICES_VERSION_CODE;
        return "user".equals(Build.TYPE);
    }

    public static boolean isWearable(Context context) {
        context = context.getPackageManager();
        if (zzd == null) {
            boolean bl = PlatformVersion.isAtLeastKitKatWatch();
            boolean bl2 = false;
            if (bl && context.hasSystemFeature("android.hardware.type.watch")) {
                bl2 = true;
            }
            zzd = bl2;
        }
        return zzd;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static boolean isWearableWithoutPlayStore(Context context) {
        boolean bl = DeviceProperties.isWearable(context);
        boolean bl2 = true;
        if (!bl) return false;
        if (!PlatformVersion.isAtLeastN()) return bl2;
        if (!DeviceProperties.zza(context)) return false;
        if (!PlatformVersion.isAtLeastO()) return true;
        return false;
    }

    public static boolean zza(Context context) {
        if (zze == null) {
            boolean bl = PlatformVersion.isAtLeastLollipop();
            boolean bl2 = false;
            if (bl && context.getPackageManager().hasSystemFeature("cn.google")) {
                bl2 = true;
            }
            zze = bl2;
        }
        return zze;
    }

    public static boolean zzb(Context context) {
        if (zzg == null) {
            boolean bl = context.getPackageManager().hasSystemFeature("android.hardware.type.iot");
            boolean bl2 = true;
            if (!bl && !context.getPackageManager().hasSystemFeature("android.hardware.type.embedded")) {
                bl2 = false;
            }
            zzg = bl2;
        }
        return zzg;
    }

    public static boolean zzc(Resources resources) {
        boolean bl = false;
        if (resources == null) {
            return false;
        }
        if (zzc == null) {
            resources = resources.getConfiguration();
            if ((resources.screenLayout & 0xF) <= 3 && resources.smallestScreenWidthDp >= 600) {
                bl = true;
            }
            zzc = bl;
        }
        return zzc;
    }
}

