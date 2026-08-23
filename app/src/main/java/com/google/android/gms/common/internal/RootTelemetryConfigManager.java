/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.internal;

import com.google.android.gms.common.internal.RootTelemetryConfiguration;

public final class RootTelemetryConfigManager {
    private static RootTelemetryConfigManager zza = null;
    private static final RootTelemetryConfiguration zzb = new RootTelemetryConfiguration(0, false, false, 0, 0);
    private RootTelemetryConfiguration zzc;

    private RootTelemetryConfigManager() {
    }

    public static RootTelemetryConfigManager getInstance() {
        synchronized (RootTelemetryConfigManager.class) {
            RootTelemetryConfigManager rootTelemetryConfigManager;
            if (zza == null) {
                zza = rootTelemetryConfigManager = new RootTelemetryConfigManager();
            }
            rootTelemetryConfigManager = zza;
            return rootTelemetryConfigManager;
        }
    }

    public RootTelemetryConfiguration getConfig() {
        return this.zzc;
    }

    /*
     * Unable to fully structure code
     */
    public final void zza(RootTelemetryConfiguration var1_1) {
        synchronized (this) {
            if (var1_1 != null) ** GOTO lbl9
            var1_1 = RootTelemetryConfigManager.zzb;
lbl5:
            // 3 sources

            while (true) {
                this.zzc = var1_1;
                return;
            }
lbl9:
            // 1 sources

            var4_3 = this.zzc;
            if (var4_3 == null) ** GOTO lbl5
            try {
                if ((var2_4 = var4_3.getVersion()) >= (var3_5 = var1_1.getVersion())) ** continue;
                ** continue;
            }
            catch (Throwable var1_2) {
                throw var1_2;
            }
        }
    }
}

