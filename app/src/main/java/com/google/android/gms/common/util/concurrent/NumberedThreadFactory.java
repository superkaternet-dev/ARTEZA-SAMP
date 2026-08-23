/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.util.concurrent;

import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.concurrent.zza;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NumberedThreadFactory
implements ThreadFactory {
    private final String zza;
    private final AtomicInteger zzb = new AtomicInteger();
    private final ThreadFactory zzc = Executors.defaultThreadFactory();

    public NumberedThreadFactory(String string2) {
        Preconditions.checkNotNull(string2, "Name must not be null");
        this.zza = string2;
    }

    @Override
    public final Thread newThread(Runnable object) {
        Thread thread2 = this.zzc.newThread(new zza((Runnable)object, 0));
        object = this.zza;
        int n = this.zzb.getAndIncrement();
        StringBuilder stringBuilder = new StringBuilder(((String)object).length() + 13);
        stringBuilder.append((String)object);
        stringBuilder.append("[");
        stringBuilder.append(n);
        stringBuilder.append("]");
        thread2.setName(stringBuilder.toString());
        return thread2;
    }
}

