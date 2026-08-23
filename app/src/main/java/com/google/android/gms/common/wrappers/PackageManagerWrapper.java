/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.AppOpsManager
 *  android.content.Context
 *  android.content.pm.ApplicationInfo
 *  android.content.pm.PackageInfo
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.graphics.drawable.Drawable
 *  android.os.Binder
 *  android.os.Process
 */
package com.google.android.gms.common.wrappers;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Binder;
import android.os.Process;
import androidx.core.util.Pair;
import com.google.android.gms.common.util.PlatformVersion;
import com.google.android.gms.common.wrappers.InstantApps;

public class PackageManagerWrapper {
    protected final Context zza;

    public PackageManagerWrapper(Context context) {
        this.zza = context;
    }

    public int checkCallingOrSelfPermission(String string2) {
        return this.zza.checkCallingOrSelfPermission(string2);
    }

    public int checkPermission(String string2, String string3) {
        return this.zza.getPackageManager().checkPermission(string2, string3);
    }

    public ApplicationInfo getApplicationInfo(String string2, int n) throws PackageManager.NameNotFoundException {
        return this.zza.getPackageManager().getApplicationInfo(string2, n);
    }

    public CharSequence getApplicationLabel(String string2) throws PackageManager.NameNotFoundException {
        return this.zza.getPackageManager().getApplicationLabel(this.zza.getPackageManager().getApplicationInfo(string2, 0));
    }

    public Pair<CharSequence, Drawable> getApplicationLabelAndIcon(String string2) throws PackageManager.NameNotFoundException {
        string2 = this.zza.getPackageManager().getApplicationInfo(string2, 0);
        return Pair.create(this.zza.getPackageManager().getApplicationLabel((ApplicationInfo)string2), this.zza.getPackageManager().getApplicationIcon((ApplicationInfo)string2));
    }

    public PackageInfo getPackageInfo(String string2, int n) throws PackageManager.NameNotFoundException {
        return this.zza.getPackageManager().getPackageInfo(string2, n);
    }

    public boolean isCallerInstantApp() {
        String string2;
        if (Binder.getCallingUid() == Process.myUid()) {
            return InstantApps.isInstantApp(this.zza);
        }
        if (PlatformVersion.isAtLeastO() && (string2 = this.zza.getPackageManager().getNameForUid(Binder.getCallingUid())) != null) {
            return this.zza.getPackageManager().isInstantApp(string2);
        }
        return false;
    }

    public final boolean zza(int n, String object) {
        if (PlatformVersion.isAtLeastKitKat()) {
            block7: {
                AppOpsManager appOpsManager;
                try {
                    appOpsManager = (AppOpsManager)this.zza.getSystemService("appops");
                    if (appOpsManager == null) break block7;
                }
                catch (SecurityException securityException) {
                    return false;
                }
                appOpsManager.checkPackage(n, (String)object);
                return true;
            }
            object = new NullPointerException("context.getSystemService(Context.APP_OPS_SERVICE) is null");
            throw object;
        }
        String[] stringArray = this.zza.getPackageManager().getPackagesForUid(n);
        if (object != null && stringArray != null) {
            for (n = 0; n < stringArray.length; ++n) {
                if (!((String)object).equals(stringArray[n])) continue;
                return true;
            }
        }
        return false;
    }
}

