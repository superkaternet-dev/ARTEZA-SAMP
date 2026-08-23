/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Dialog
 */
package com.google.android.gms.common.api.internal;

import android.app.Dialog;
import com.google.android.gms.common.api.internal.zabw;
import com.google.android.gms.common.api.internal.zao;
import com.google.android.gms.common.api.internal.zap;

final class zan
extends zabw {
    final Dialog zaa;
    final zao zab;

    zan(zao zao2, Dialog dialog) {
        this.zab = zao2;
        this.zaa = dialog;
    }

    @Override
    public final void zaa() {
        zap.zag(this.zab.zaa);
        if (this.zaa.isShowing()) {
            this.zaa.dismiss();
        }
    }
}

