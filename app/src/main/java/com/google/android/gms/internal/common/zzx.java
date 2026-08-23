/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.internal.common;

import com.google.android.gms.internal.common.zzn;
import com.google.android.gms.internal.common.zzo;
import com.google.android.gms.internal.common.zzt;
import com.google.android.gms.internal.common.zzu;
import com.google.android.gms.internal.common.zzv;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class zzx {
    private final zzo zza;
    private final boolean zzb;
    private final zzu zzc;

    private zzx(zzu zzu2, boolean bl, zzo zzo2, int n, byte[] byArray) {
        this.zzc = zzu2;
        this.zzb = bl;
        this.zza = zzo2;
    }

    static /* synthetic */ zzo zza(zzx zzx2) {
        return zzx2.zza;
    }

    public static zzx zzc(zzo zzo2) {
        return new zzx(new zzu(zzo2), false, zzn.zza, Integer.MAX_VALUE, null);
    }

    static /* synthetic */ Iterator zze(zzx zzx2, CharSequence charSequence) {
        return zzx2.zzh(charSequence);
    }

    static /* synthetic */ boolean zzg(zzx zzx2) {
        return zzx2.zzb;
    }

    private final Iterator<String> zzh(CharSequence charSequence) {
        return new zzt(this.zzc, this, charSequence);
    }

    public final zzx zzb() {
        return new zzx(this.zzc, true, this.zza, Integer.MAX_VALUE, null);
    }

    public final Iterable<String> zzd(CharSequence charSequence) {
        return new zzv(this, charSequence);
    }

    public final List<String> zzf(CharSequence object) {
        if (object != null) {
            Iterator<String> iterator2 = this.zzh((CharSequence)object);
            object = new ArrayList();
            while (iterator2.hasNext()) {
                object.add(iterator2.next());
            }
            return Collections.unmodifiableList(object);
        }
        throw null;
    }
}

