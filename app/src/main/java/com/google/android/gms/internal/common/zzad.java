/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.internal.common;

import com.google.android.gms.internal.common.zzaa;
import java.util.Iterator;

public final class zzad<E>
extends zzaa<E> {
    public zzad() {
        super(4);
    }

    zzad(int n) {
        super(4);
    }

    public final zzad<E> zzb(E e) {
        super.zza(e);
        return this;
    }

    public final zzad<E> zzc(Iterator<? extends E> iterator2) {
        while (iterator2.hasNext()) {
            super.zza(iterator2.next());
        }
        return this;
    }
}

