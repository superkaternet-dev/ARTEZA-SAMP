/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.internal.zabp;
import com.google.android.gms.common.api.internal.zabq;

final class zabo
implements Runnable {
    final zabp zaa;

    zabo(zabp zabp2) {
        this.zaa = zabp2;
    }

    @Override
    public final void run() {
        zabq zabq2 = this.zaa.zaa;
        zabq.zae(zabq2).disconnect(String.valueOf(zabq.zae(zabq2).getClass().getName()).concat(" disconnecting because it was signed out."));
    }
}

