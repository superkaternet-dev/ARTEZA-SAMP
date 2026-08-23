/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.pm.PackageInfo
 *  android.content.pm.PackageManager$NameNotFoundException
 */
package com.google.android.gms.common.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import com.google.android.gms.common.wrappers.Wrappers;

public class ClientLibraryUtils {
    private ClientLibraryUtils() {
    }

    public static int getClientVersion(Context context, String string2) {
        if ((context = ClientLibraryUtils.getPackageInfo(context, string2)) != null && context.applicationInfo != null && (context = context.applicationInfo.metaData) != null) {
            return context.getInt("com.google.android.gms.version", -1);
        }
        return -1;
    }

    public static PackageInfo getPackageInfo(Context context, String string2) {
        try {
            context = Wrappers.packageManager(context).getPackageInfo(string2, 128);
            return context;
        }
        catch (PackageManager.NameNotFoundException nameNotFoundException) {
            return null;
        }
    }

    public static boolean isPackageSide() {
        return false;
    }
}

