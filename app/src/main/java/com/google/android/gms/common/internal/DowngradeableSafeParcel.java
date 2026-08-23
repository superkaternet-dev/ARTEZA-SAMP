/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.internal;

import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;

public abstract class DowngradeableSafeParcel
extends AbstractSafeParcelable
implements ReflectedParcelable {
    private static final Object zza = new Object();
    private boolean zzb = false;

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    protected static boolean canUnparcelSafely(String string2) {
        Object object = zza;
        // MONITORENTER : object
        // MONITOREXIT : object
        return true;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    protected static Integer getUnparcelClientVersion() {
        Object object = zza;
        // MONITORENTER : object
        // MONITOREXIT : object
        return null;
    }

    protected abstract boolean prepareForClientVersion(int var1);

    public void setShouldDowngrade(boolean bl) {
        this.zzb = bl;
    }

    protected boolean shouldDowngrade() {
        return this.zzb;
    }
}

