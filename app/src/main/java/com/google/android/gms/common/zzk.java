/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common;

import com.google.android.gms.common.zzi;
import java.lang.ref.WeakReference;

abstract class zzk
extends zzi {
    private static final WeakReference<byte[]> zza = new WeakReference<Object>(null);
    private WeakReference<byte[]> zzb = zza;

    zzk(byte[] byArray) {
        super(byArray);
    }

    protected abstract byte[] zzb();

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    final byte[] zzf() {
        synchronized (this) {
            Object object;
            byte[] byArray = object = (byte[])this.zzb.get();
            if (object == null) {
                byArray = this.zzb();
                object = new WeakReference;
                object(byArray);
                this.zzb = object;
            }
            return byArray;
        }
    }
}

