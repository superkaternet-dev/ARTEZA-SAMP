/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.CheckForNull
 */
package com.google.android.gms.internal.common;

import com.google.android.gms.internal.common.zzag;
import com.google.android.gms.internal.common.zzaj;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.Collection;
import javax.annotation.CheckForNull;

public abstract class zzac<E>
extends AbstractCollection<E>
implements Serializable {
    private static final Object[] zza = new Object[0];

    zzac() {
    }

    @Override
    @Deprecated
    public final boolean add(E e) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public final boolean addAll(Collection<? extends E> collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public final void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public final boolean remove(@CheckForNull Object object) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public final boolean removeAll(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public final boolean retainAll(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final Object[] toArray() {
        return this.toArray(zza);
    }

    /*
     * WARNING - void declaration
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public final <T> T[] toArray(T[] TArray) {
        void var4_8;
        if (TArray == null) throw null;
        int n = TArray.length;
        int n2 = this.size();
        if (n < n2) {
            Object[] objectArray = this.zzg();
            if (objectArray != null) return Arrays.copyOfRange(objectArray, this.zzc(), this.zzb(), TArray.getClass());
            Object[] objectArray2 = (Object[])Array.newInstance(TArray.getClass().getComponentType(), n2);
        } else {
            T[] TArray2 = TArray;
            if (n > n2) {
                TArray[n2] = null;
                T[] TArray3 = TArray;
            }
        }
        this.zza((Object[])var4_8, 0);
        return var4_8;
    }

    int zza(Object[] objectArray, int n) {
        throw null;
    }

    int zzb() {
        throw null;
    }

    int zzc() {
        throw null;
    }

    public zzag<E> zzd() {
        throw null;
    }

    public abstract zzaj<E> zze();

    abstract boolean zzf();

    @CheckForNull
    Object[] zzg() {
        throw null;
    }
}

