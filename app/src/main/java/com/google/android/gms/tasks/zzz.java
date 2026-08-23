/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.tasks;

import com.google.android.gms.tasks.zzw;
import java.util.concurrent.Callable;

final class zzz
implements Runnable {
    final zzw zza;
    final Callable zzb;

    zzz(zzw zzw2, Callable callable) {
        this.zza = zzw2;
        this.zzb = callable;
    }

    @Override
    public final void run() {
        try {
            this.zza.zzb(this.zzb.call());
            return;
        }
        catch (Throwable throwable) {
            this.zza.zza(new RuntimeException(throwable));
            return;
        }
        catch (Exception exception) {
            this.zza.zza(exception);
            return;
        }
    }
}

