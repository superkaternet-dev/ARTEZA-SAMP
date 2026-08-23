/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.internal.zaar;
import com.google.android.gms.common.api.internal.zaaw;
import com.google.android.gms.common.api.internal.zabf;
import com.google.android.gms.common.api.internal.zabg;
import com.google.android.gms.signin.internal.zak;

final class zaaq
extends zabg {
    final zaaw zaa;
    final zak zab;

    zaaq(zaar zaar2, zabf zabf2, zaaw zaaw2, zak zak2) {
        this.zaa = zaaw2;
        this.zab = zak2;
        super(zabf2);
    }

    @Override
    public final void zaa() {
        zaaw.zar(this.zaa, this.zab);
    }
}

