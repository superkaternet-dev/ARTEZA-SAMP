/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.internal.zaaq;
import com.google.android.gms.common.api.internal.zaaw;
import com.google.android.gms.signin.internal.zac;
import com.google.android.gms.signin.internal.zak;
import java.lang.ref.WeakReference;

final class zaar
extends zac {
    private final WeakReference<zaaw> zaa;

    zaar(zaaw zaaw2) {
        this.zaa = new WeakReference<zaaw>(zaaw2);
    }

    @Override
    public final void zab(zak zak2) {
        zaaw zaaw2 = (zaaw)this.zaa.get();
        if (zaaw2 == null) {
            return;
        }
        zaaw.zak(zaaw2).zal(new zaaq(this, zaaw2, zaaw2, zak2));
    }
}

