/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.internal.common;

import com.google.android.gms.internal.common.zzag;
import com.google.android.gms.internal.common.zzz;

final class zzae<E>
extends zzz<E> {
    private final zzag<E> zza;

    zzae(zzag<E> zzag2, int n) {
        super(zzag2.size(), n);
        this.zza = zzag2;
    }

    @Override
    protected final E zza(int n) {
        return this.zza.get(n);
    }
}

