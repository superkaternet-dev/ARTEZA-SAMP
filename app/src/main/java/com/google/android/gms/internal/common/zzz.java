/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.internal.common;

import com.google.android.gms.internal.common.zzak;
import com.google.android.gms.internal.common.zzs;
import java.util.NoSuchElementException;

abstract class zzz<E>
extends zzak<E> {
    private final int zza;
    private int zzb;

    protected zzz(int n, int n2) {
        zzs.zzb(n2, n, "index");
        this.zza = n;
        this.zzb = n2;
    }

    @Override
    public final boolean hasNext() {
        return this.zzb < this.zza;
    }

    @Override
    public final boolean hasPrevious() {
        return this.zzb > 0;
    }

    @Override
    public final E next() {
        if (this.hasNext()) {
            int n = this.zzb;
            this.zzb = n + 1;
            return this.zza(n);
        }
        throw new NoSuchElementException();
    }

    @Override
    public final int nextIndex() {
        return this.zzb;
    }

    @Override
    public final E previous() {
        if (this.hasPrevious()) {
            int n;
            this.zzb = n = this.zzb - 1;
            return this.zza(n);
        }
        throw new NoSuchElementException();
    }

    @Override
    public final int previousIndex() {
        return this.zzb - 1;
    }

    protected abstract E zza(int var1);
}

