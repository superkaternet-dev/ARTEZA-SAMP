/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.CheckForNull
 */
package com.google.android.gms.internal.common;

import com.google.android.gms.internal.common.zzp;
import javax.annotation.CheckForNull;

public final class zzr
extends zzp {
    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static boolean zza(@CheckForNull Object object, @CheckForNull Object object2) {
        boolean bl = false;
        if (object == object2) return true;
        if (object == null) return bl;
        if (!object.equals(object2)) return false;
        return true;
    }
}

