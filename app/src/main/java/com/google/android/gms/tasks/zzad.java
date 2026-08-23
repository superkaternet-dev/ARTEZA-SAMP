/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.tasks;

import com.google.android.gms.tasks.zzac;
import com.google.android.gms.tasks.zzae;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

final class zzad
implements zzae {
    private final CountDownLatch zza = new CountDownLatch(1);

    private zzad() {
    }

    /* synthetic */ zzad(zzac zzac2) {
    }

    @Override
    public final void onCanceled() {
        this.zza.countDown();
    }

    @Override
    public final void onFailure(Exception exception) {
        this.zza.countDown();
    }

    @Override
    public final void onSuccess(Object object) {
        this.zza.countDown();
    }

    public final void zza() throws InterruptedException {
        this.zza.await();
    }

    public final boolean zzb(long l, TimeUnit timeUnit) throws InterruptedException {
        return this.zza.await(l, timeUnit);
    }
}

