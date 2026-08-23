/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.stats;

import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;

@Deprecated
public abstract class StatsEvent
extends AbstractSafeParcelable
implements ReflectedParcelable {
    public final String toString() {
        long l = this.zzc();
        int n = this.zza();
        long l2 = this.zzb();
        String string2 = this.zzd();
        StringBuilder stringBuilder = new StringBuilder(string2.length() + 53);
        stringBuilder.append(l);
        stringBuilder.append("\t");
        stringBuilder.append(n);
        stringBuilder.append("\t");
        stringBuilder.append(l2);
        stringBuilder.append(string2);
        return stringBuilder.toString();
    }

    public abstract int zza();

    public abstract long zzb();

    public abstract long zzc();

    public abstract String zzd();

    public static interface Types {
        public static final int EVENT_TYPE_ACQUIRE_WAKE_LOCK = 7;
        public static final int EVENT_TYPE_RELEASE_WAKE_LOCK = 8;
    }
}

