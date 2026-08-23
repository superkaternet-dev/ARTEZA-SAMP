/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.internal.common;

import com.google.android.gms.internal.common.zzy;

public final class zzs {
    public static int zza(int n, int n2, String charSequence) {
        if (n >= 0 && n < n2) {
            return n;
        }
        if (n >= 0) {
            if (n2 < 0) {
                charSequence = new StringBuilder(26);
                ((StringBuilder)charSequence).append("negative size: ");
                ((StringBuilder)charSequence).append(n2);
                throw new IllegalArgumentException(((StringBuilder)charSequence).toString());
            }
            charSequence = zzy.zza("%s (%s) must be less than size (%s)", "index", n, n2);
        } else {
            charSequence = zzy.zza("%s (%s) must not be negative", "index", n);
        }
        throw new IndexOutOfBoundsException((String)charSequence);
    }

    public static int zzb(int n, int n2, String string2) {
        if (n >= 0 && n <= n2) {
            return n;
        }
        throw new IndexOutOfBoundsException(zzs.zzd(n, n2, "index"));
    }

    public static void zzc(int n, int n2, int n3) {
        if (n >= 0 && n2 >= n && n2 <= n3) {
            return;
        }
        String string2 = n >= 0 && n <= n3 ? (n2 >= 0 && n2 <= n3 ? zzy.zza("end index (%s) must not be less than start index (%s)", n2, n) : zzs.zzd(n2, n3, "end index")) : zzs.zzd(n, n3, "start index");
        throw new IndexOutOfBoundsException(string2);
    }

    private static String zzd(int n, int n2, String charSequence) {
        if (n < 0) {
            return zzy.zza("%s (%s) must not be negative", charSequence, n);
        }
        if (n2 >= 0) {
            return zzy.zza("%s (%s) must not be greater than size (%s)", charSequence, n, n2);
        }
        charSequence = new StringBuilder(26);
        ((StringBuilder)charSequence).append("negative size: ");
        ((StringBuilder)charSequence).append(n2);
        throw new IllegalArgumentException(((StringBuilder)charSequence).toString());
    }
}

