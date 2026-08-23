/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.internal.common;

import com.google.android.gms.internal.common.zzq;
import com.google.android.gms.internal.common.zzx;
import java.io.IOException;
import java.util.Iterator;

final class zzv
implements Iterable<String> {
    final CharSequence zza;
    final zzx zzb;

    zzv(zzx zzx2, CharSequence charSequence) {
        this.zzb = zzx2;
        this.zza = charSequence;
    }

    @Override
    public final Iterator<String> iterator() {
        return zzx.zze(this.zzb, this.zza);
    }

    public final String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('[');
        Iterator iterator2 = this.iterator();
        try {
            if (iterator2.hasNext()) {
                stringBuilder.append(zzq.zza(iterator2.next(), ", "));
                while (iterator2.hasNext()) {
                    stringBuilder.append((CharSequence)", ");
                    stringBuilder.append(zzq.zza(iterator2.next(), ", "));
                }
            }
            stringBuilder.append(']');
        }
        catch (IOException iOException) {
            AssertionError assertionError = new AssertionError((Object)iOException);
            throw assertionError;
        }
        return stringBuilder.toString();
    }
}

