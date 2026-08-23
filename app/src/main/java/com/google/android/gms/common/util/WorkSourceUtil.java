/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.pm.ApplicationInfo
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.os.Process
 *  android.os.WorkSource
 *  android.util.Log
 */
package com.google.android.gms.common.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Process;
import android.os.WorkSource;
import android.util.Log;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.PlatformVersion;
import com.google.android.gms.common.util.Strings;
import com.google.android.gms.common.wrappers.Wrappers;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class WorkSourceUtil {
    private static final int zza;
    private static final Method zzb;
    private static final Method zzc;
    private static final Method zzd;
    private static final Method zze;
    private static final Method zzf;
    private static final Method zzg;
    private static final Method zzh;
    private static final Method zzi;

    static {
        Method method;
        Method method2;
        Object var2;
        block25: {
            block24: {
                zza = Process.myUid();
                var2 = null;
                method2 = null;
                try {
                    method = WorkSource.class.getMethod("add", Integer.TYPE);
                }
                catch (Exception exception) {
                    method = null;
                }
                zzb = method;
                if (PlatformVersion.isAtLeastJellyBeanMR2()) {
                    try {
                        method = WorkSource.class.getMethod("add", Integer.TYPE, String.class);
                        break block24;
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                }
                method = null;
            }
            zzc = method;
            try {
                method = WorkSource.class.getMethod("size", new Class[0]);
            }
            catch (Exception exception) {
                method = null;
            }
            zzd = method;
            try {
                method = WorkSource.class.getMethod("get", Integer.TYPE);
            }
            catch (Exception exception) {
                method = null;
            }
            zze = method;
            if (PlatformVersion.isAtLeastJellyBeanMR2()) {
                try {
                    method = WorkSource.class.getMethod("getName", Integer.TYPE);
                    break block25;
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
            method = null;
        }
        zzf = method;
        if (PlatformVersion.isAtLeastP()) {
            try {
                method = WorkSource.class.getMethod("createWorkChain", new Class[0]);
            }
            catch (Exception exception) {
                Log.w((String)"WorkSourceUtil", (String)"Missing WorkChain API createWorkChain", (Throwable)exception);
                method = null;
            }
        } else {
            method = null;
        }
        zzg = method;
        if (PlatformVersion.isAtLeastP()) {
            try {
                method = Class.forName("android.os.WorkSource$WorkChain").getMethod("addNode", Integer.TYPE, String.class);
            }
            catch (Exception exception) {
                Log.w((String)"WorkSourceUtil", (String)"Missing WorkChain class", (Throwable)exception);
                method = null;
            }
        } else {
            method = null;
        }
        zzh = method;
        method = var2;
        if (PlatformVersion.isAtLeastP()) {
            method = method2;
            method = method2 = WorkSource.class.getMethod("isEmpty", new Class[0]);
            try {
                method2.setAccessible(true);
                method = method2;
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        zzi = method;
    }

    private WorkSourceUtil() {
    }

    public static void add(WorkSource workSource, int n, String object) {
        Method method = zzc;
        if (method != null) {
            String string2 = object;
            if (object == null) {
                string2 = "";
            }
            try {
                method.invoke((Object)workSource, n, string2);
                return;
            }
            catch (Exception exception) {
                Log.wtf((String)"WorkSourceUtil", (String)"Unable to assign blame through WorkSource", (Throwable)exception);
                return;
            }
        }
        object = zzb;
        if (object != null) {
            try {
                ((Method)object).invoke((Object)workSource, n);
                return;
            }
            catch (Exception exception) {
                Log.wtf((String)"WorkSourceUtil", (String)"Unable to assign blame through WorkSource", (Throwable)exception);
                return;
            }
        }
    }

    public static WorkSource fromPackage(Context object, String string2) {
        if (object != null && object.getPackageManager() != null && string2 != null) {
            block3: {
                try {
                    object = Wrappers.packageManager(object).getApplicationInfo(string2, 0);
                    if (object != null) break block3;
                    object = string2.length() != 0 ? "Could not get applicationInfo from package: ".concat(string2) : new String("Could not get applicationInfo from package: ");
                }
                catch (PackageManager.NameNotFoundException nameNotFoundException) {
                    String string3 = string2.length() != 0 ? "Could not find package: ".concat(string2) : new String("Could not find package: ");
                    Log.e((String)"WorkSourceUtil", (String)string3);
                    return null;
                }
                Log.e((String)"WorkSourceUtil", (String)object);
                return null;
            }
            int n = object.uid;
            object = new WorkSource();
            WorkSourceUtil.add((WorkSource)object, n, string2);
            return object;
        }
        return null;
    }

    /*
     * WARNING - void declaration
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static WorkSource fromPackageAndModuleExperimentalPi(Context object, String string2, String string3) {
        block14: {
            Method method;
            int n;
            void var1_11;
            void var2_13;
            block13: {
                ApplicationInfo applicationInfo;
                block11: {
                    void var0_4;
                    if (object == null || object.getPackageManager() == null || var2_13 == null || var1_11 == null) break block14;
                    n = -1;
                    try {
                        applicationInfo = Wrappers.packageManager(object).getApplicationInfo((String)var1_11, 0);
                        if (applicationInfo != null) break block11;
                        if (var1_11.length() != 0) {
                            String string4 = "Could not get applicationInfo from package: ".concat((String)var1_11);
                        } else {
                            String string5 = new String("Could not get applicationInfo from package: ");
                        }
                    }
                    catch (PackageManager.NameNotFoundException nameNotFoundException) {
                        void var0_8;
                        if (var1_11.length() != 0) {
                            String string6 = "Could not find package: ".concat((String)var1_11);
                        } else {
                            String string7 = new String("Could not find package: ");
                        }
                        Log.e((String)"WorkSourceUtil", (String)var0_8);
                        break block13;
                    }
                    Log.e((String)"WorkSourceUtil", (String)var0_4);
                    break block13;
                }
                n = applicationInfo.uid;
            }
            if (n < 0) {
                return null;
            }
            WorkSource workSource = new WorkSource();
            Object object2 = zzg;
            if (object2 != null && (method = zzh) != null) {
                try {
                    object2 = ((Method)object2).invoke((Object)workSource, new Object[0]);
                    int n2 = zza;
                    if (n != n2) {
                        method.invoke(object2, n, var1_11);
                    }
                    method.invoke(object2, n2, var2_13);
                    return workSource;
                }
                catch (Exception exception) {
                    Log.w((String)"WorkSourceUtil", (String)"Unable to assign chained blame through WorkSource", (Throwable)exception);
                    return workSource;
                }
            }
            WorkSourceUtil.add(workSource, n, (String)var1_11);
            return workSource;
        }
        Log.w((String)"WorkSourceUtil", (String)"Unexpected null arguments");
        return null;
    }

    public static List<String> getNames(WorkSource workSource) {
        ArrayList<String> arrayList = new ArrayList<String>();
        int n = workSource == null ? 0 : WorkSourceUtil.zza(workSource);
        if (n != 0) {
            for (int i = 0; i < n; ++i) {
                Object object = zzf;
                Object object2 = null;
                if (object != null) {
                    try {
                        object2 = object = (String)((Method)object).invoke((Object)workSource, i);
                    }
                    catch (Exception exception) {
                        Log.wtf((String)"WorkSourceUtil", (String)"Unable to assign blame through WorkSource", (Throwable)exception);
                    }
                }
                if (Strings.isEmptyOrWhitespace(object2)) continue;
                Preconditions.checkNotNull(object2);
                arrayList.add((String)object2);
            }
        }
        return arrayList;
    }

    public static boolean hasWorkSourcePermission(Context context) {
        if (context == null) {
            return false;
        }
        if (context.getPackageManager() == null) {
            return false;
        }
        return Wrappers.packageManager(context).checkPermission("android.permission.UPDATE_DEVICE_STATS", context.getPackageName()) == 0;
    }

    public static boolean isEmpty(WorkSource workSource) {
        Object object = zzi;
        if (object != null) {
            try {
                object = ((Method)object).invoke((Object)workSource, new Object[0]);
                Preconditions.checkNotNull(object);
                boolean bl = (Boolean)object;
                return bl;
            }
            catch (Exception exception) {
                Log.e((String)"WorkSourceUtil", (String)"Unable to check WorkSource emptiness", (Throwable)exception);
            }
        }
        return WorkSourceUtil.zza(workSource) == 0;
    }

    public static int zza(WorkSource object) {
        Method method = zzd;
        if (method != null) {
            try {
                object = method.invoke(object, new Object[0]);
                Preconditions.checkNotNull(object);
                int n = (Integer)object;
                return n;
            }
            catch (Exception exception) {
                Log.wtf((String)"WorkSourceUtil", (String)"Unable to assign blame through WorkSource", (Throwable)exception);
            }
        }
        return 0;
    }
}

