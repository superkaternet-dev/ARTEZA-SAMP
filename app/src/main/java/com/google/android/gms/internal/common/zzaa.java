/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.internal.common;

import com.google.android.gms.internal.common.zzab;
import java.util.Arrays;

class zzaa<E>
extends zzab<E> {
    Object[] zza = new Object[4];
    int zzb = 0;
    boolean zzc;

    zzaa(int n) {
    }

    private final void zzb(int n) {
        Object[] objectArray = this.zza;
        int n2 = objectArray.length;
        if (n2 < n) {
            if ((n2 = n2 + (n2 >> 1) + 1) < n) {
                n = Integer.highestOneBit(n - 1);
                n += n;
            } else {
                n = n2;
            }
            n2 = n;
            if (n < 0) {
                n2 = Integer.MAX_VALUE;
            }
            this.zza = Arrays.copyOf(objectArray, n2);
            this.zzc = false;
            return;
        }
        if (this.zzc) {
            this.zza = (Object[])objectArray.clone();
            this.zzc = false;
        }
    }

    public final zzaa<E> zza(E e) {
        if (e != null) {
            this.zzb(this.zzb + 1);
            Object[] objectArray = this.zza;
            int n = this.zzb;
            this.zzb = n + 1;
            objectArray[n] = e;
            return this;
        }
        throw null;
    }
}

