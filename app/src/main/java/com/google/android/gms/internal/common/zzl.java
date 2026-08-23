/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.internal.common;

import com.google.android.gms.internal.common.zzk;

final class zzl
extends zzk {
    private final char zza;

    zzl(char c) {
        this.zza = c;
    }

    public final String toString() {
        Object object;
        int n = this.zza;
        char[] cArray = object = new char[6];
        object[0] = 92;
        cArray[1] = 117;
        cArray[2] = 0;
        cArray[3] = 0;
        cArray[4] = 0;
        cArray[5] = 0;
        for (int i = 0; i < 4; ++i) {
            object[5 - i] = "0123456789ABCDEF".charAt(n & 0xF);
            n >>= 4;
        }
        object = String.copyValueOf(object);
        StringBuilder stringBuilder = new StringBuilder(String.valueOf(object).length() + 18);
        stringBuilder.append("CharMatcher.is('");
        stringBuilder.append((String)object);
        stringBuilder.append("')");
        return stringBuilder.toString();
    }

    @Override
    public final boolean zza(char c) {
        return c == this.zza;
    }
}

