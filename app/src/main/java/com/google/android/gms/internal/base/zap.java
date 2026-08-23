/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.internal.base;

import com.google.android.gms.internal.base.zam;
import com.google.android.gms.internal.base.zao;

public final class zap {
    private static final zam zaa;
    private static volatile zam zab;

    static {
        zao zao2 = new zao(null);
        zaa = zao2;
        zab = zao2;
    }

    public static zam zaa() {
        return zab;
    }
}

