/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Looper
 *  android.util.Log
 */
package com.google.android.gms.dynamite;

import android.os.Looper;
import android.util.Log;
import com.google.android.gms.dynamite.zza;

public final class zzb {
    private static volatile ClassLoader zza = null;
    private static volatile Thread zzb = null;

    public static ClassLoader zza() {
        synchronized (zzb.class) {
            if (zza == null) {
                zza = com.google.android.gms.dynamite.zzb.zzb();
            }
            ClassLoader classLoader = zza;
            return classLoader;
        }
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    private static ClassLoader zzb() {
        Throwable throwable22;
        block11: {
            Object object;
            Object var1_1;
            Thread thread2;
            block10: {
                // MONITORENTER : com.google.android.gms.dynamite.zzb.class
                thread2 = zzb;
                var1_1 = null;
                object = null;
                if (thread2 != null || (thread2 = (zzb = com.google.android.gms.dynamite.zzb.zzc())) != null) break block10;
                return object;
            }
            thread2 = zzb;
            // MONITORENTER : thread2
            try {
                return zzb.getContextClassLoader();
            }
            catch (Throwable throwable22) {
                break block11;
            }
            catch (SecurityException securityException) {
                object = String.valueOf(securityException.getMessage());
                object = ((String)object).length() != 0 ? "Failed to get thread context classloader ".concat((String)object) : new String("Failed to get thread context classloader ");
                Log.w((String)"DynamiteLoaderV2CL", (String)object);
                object = var1_1;
            }
            // MONITOREXIT : thread2
            return object;
        }
        // MONITOREXIT : thread2
        throw throwable22;
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static Thread zzc() {
        synchronized (zzb.class) {
            block26: {
                block22: {
                    var6 = Looper.getMainLooper().getThread().getThreadGroup();
                    if (var6 != null) break block22;
                    return null;
                }
                ** synchronized (Void.class)
lbl-1000:
                // 1 sources

                {
                    block25: {
                        block24: {
                            block23: {
                                var2_1 = var6.activeGroupCount();
                                var5_2 = new ThreadGroup[var2_1];
                                var6.enumerate((ThreadGroup[])var5_2);
                                var1_7 = 0;
                                for (var0_8 = 0; var0_8 < var2_1; ++var0_8) {
                                    var4_9 = var5_2[var0_8];
                                    if (!"dynamiteLoader".equals(var4_9.getName())) {
                                        continue;
                                    }
                                    break block23;
                                }
                                var4_9 = null;
                            }
                            var5_2 = var4_9;
                            if (var4_9 != null) ** GOTO lbl27
                            var5_2 = new ThreadGroup((ThreadGroup)var6, "dynamiteLoader");
lbl27:
                            // 2 sources

                            var2_1 = var5_2.activeCount();
                            var6 = new Thread[var2_1];
                            var5_2.enumerate((Thread[])var6);
                            for (var0_8 = var1_7; var0_8 < var2_1; ++var0_8) {
                                var4_9 = var6[var0_8];
                                var3_12 = "GmsDynamite".equals(var4_9.getName());
                                if (!var3_12) {
                                    continue;
                                }
                                break block24;
                            }
                            var4_9 = null;
                        }
                        if (var4_9 != null) return var4_9;
                        try {
                            try {
                                var6 = new zza((ThreadGroup)var5_2, "GmsDynamite");
                            }
                            catch (SecurityException var5_4) {
                                break block25;
                            }
                            try {
                                var6.setContextClassLoader(null);
                                var6.start();
                                return var6;
                            }
                            catch (SecurityException var5_3) {
                                var4_9 = var6;
                                break block25;
                            }
                        }
                        catch (Throwable var4_10) {
                            break block26;
                        }
                        catch (SecurityException var5_5) {
                            var4_9 = null;
                        }
                    }
                    var5_6 = String.valueOf(var5_6.getMessage());
                    var5_6 = var5_6.length() != 0 ? "Failed to enumerate thread/threadgroup ".concat(var5_6) : new String("Failed to enumerate thread/threadgroup ");
                    Log.w((String)"DynamiteLoaderV2CL", (String)var5_6);
                    return var4_9;
                }
            }
            throw var4_10;
        }
    }
}

