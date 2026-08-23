/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.CheckForNull
 */
package com.google.android.gms.internal.common;

import java.util.Iterator;
import java.util.NoSuchElementException;
import javax.annotation.CheckForNull;

abstract class zzj<T>
implements Iterator<T> {
    @CheckForNull
    private T zza;
    private int zzb = 2;

    protected zzj() {
    }

    @Override
    public final boolean hasNext() {
        int n = this.zzb;
        if (n != 4) {
            if (n != 0) {
                switch (n - 1) {
                    default: {
                        this.zzb = 4;
                        this.zza = this.zza();
                        if (this.zzb == 3) break;
                        this.zzb = 1;
                        return true;
                    }
                    case 2: {
                        return false;
                    }
                    case 0: {
                        return true;
                    }
                }
                return false;
            }
            throw null;
        }
        throw new IllegalStateException();
    }

    @Override
    public final T next() {
        if (this.hasNext()) {
            this.zzb = 2;
            T t = this.zza;
            this.zza = null;
            return t;
        }
        throw new NoSuchElementException();
    }

    @Override
    public final void remove() {
        throw new UnsupportedOperationException();
    }

    @CheckForNull
    protected abstract T zza();

    @CheckForNull
    protected final T zzb() {
        this.zzb = 3;
        return null;
    }
}

