/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.internal.common;

import com.google.android.gms.internal.common.zzag;
import com.google.android.gms.internal.common.zzs;

final class zzai<E>
extends zzag<E> {
    static final zzag<Object> zza = new zzai<Object>(new Object[0], 0);
    final transient Object[] zzb;
    private final transient int zzc;

    zzai(Object[] objectArray, int n) {
        this.zzb = objectArray;
        this.zzc = n;
    }

    @Override
    public final E get(int n) {
        zzs.zza(n, this.zzc, "index");
        Object object = this.zzb[n];
        object.getClass();
        return (E)object;
    }

    @Override
    public final int size() {
        return this.zzc;
    }

    @Override
    final int zza(Object[] objectArray, int n) {
        System.arraycopy(this.zzb, 0, objectArray, 0, this.zzc);
        return this.zzc;
    }

    @Override
    final int zzb() {
        return this.zzc;
    }

    @Override
    final int zzc() {
        return 0;
    }

    @Override
    final boolean zzf() {
        return false;
    }

    @Override
    final Object[] zzg() {
        return this.zzb;
    }
}

