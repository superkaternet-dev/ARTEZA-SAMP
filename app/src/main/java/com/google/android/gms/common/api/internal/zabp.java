/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.internal.GoogleApiManager;
import com.google.android.gms.common.api.internal.zabo;
import com.google.android.gms.common.api.internal.zabq;
import com.google.android.gms.common.internal.BaseGmsClient;

final class zabp
implements BaseGmsClient.SignOutCallbacks {
    final zabq zaa;

    zabp(zabq zabq2) {
        this.zaa = zabq2;
    }

    @Override
    public final void onSignOutComplete() {
        GoogleApiManager.zaf(this.zaa.zaa).post((Runnable)new zabo(this));
    }
}

