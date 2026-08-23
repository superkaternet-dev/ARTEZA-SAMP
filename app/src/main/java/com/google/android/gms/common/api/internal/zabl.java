/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.internal.BackgroundDetector;
import com.google.android.gms.common.api.internal.GoogleApiManager;

final class zabl
implements BackgroundDetector.BackgroundStateChangeListener {
    final GoogleApiManager zaa;

    zabl(GoogleApiManager googleApiManager) {
        this.zaa = googleApiManager;
    }

    @Override
    public final void onBackgroundStateChanged(boolean bl) {
        GoogleApiManager googleApiManager = this.zaa;
        GoogleApiManager.zaf(googleApiManager).sendMessage(GoogleApiManager.zaf(googleApiManager).obtainMessage(1, (Object)bl));
    }
}

