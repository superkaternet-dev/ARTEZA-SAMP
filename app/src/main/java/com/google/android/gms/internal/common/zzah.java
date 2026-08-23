/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.internal.common;

public final class zzah {
    static Object[] zza(Object[] object, int n) {
        for (int i = 0; i < n; ++i) {
            if (object[i] != null) {
                continue;
            }
            object = new StringBuilder(20);
            ((StringBuilder)object).append("at index ");
            ((StringBuilder)object).append(i);
            throw new NullPointerException(((StringBuilder)object).toString());
        }
        return object;
    }
}

