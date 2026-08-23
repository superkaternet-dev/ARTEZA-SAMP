/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.util.concurrent;

import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.concurrent.zza;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class NamedThreadFactory
implements ThreadFactory {
    private final String zza;
    private final ThreadFactory zzb = Executors.defaultThreadFactory();

    public NamedThreadFactory(String string2) {
        Preconditions.checkNotNull(string2, "Name must not be null");
        this.zza = string2;
    }

    @Override
    public final Thread newThread(Runnable runnable) {
        runnable = this.zzb.newThread(new zza(runnable, 0));
        ((Thread)runnable).setName(this.zza);
        return runnable;
    }
}

