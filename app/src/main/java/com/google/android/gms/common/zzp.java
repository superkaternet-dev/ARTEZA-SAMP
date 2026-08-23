/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common;

final class zzp {
    static int zza(int n) {
        for (int i = 0; i < 6; ++i) {
            int n2 = (new int[]{1, 2, 3, 4, 5, 6})[i];
            if (n2 != 0) {
                if (n2 - 1 != n) continue;
                return n2;
            }
            throw null;
        }
        return 1;
    }
}

