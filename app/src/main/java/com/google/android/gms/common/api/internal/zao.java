/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Dialog
 *  android.content.Context
 */
package com.google.android.gms.common.api.internal;

import android.app.Dialog;
import android.content.Context;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiActivity;
import com.google.android.gms.common.api.internal.LifecycleCallback;
import com.google.android.gms.common.api.internal.zam;
import com.google.android.gms.common.api.internal.zan;
import com.google.android.gms.common.api.internal.zap;
import com.google.android.gms.common.internal.Preconditions;

final class zao
implements Runnable {
    final zap zaa;
    private final zam zab;

    zao(zap zap2, zam zam2) {
        this.zaa = zap2;
        this.zab = zam2;
    }

    @Override
    public final void run() {
        if (!this.zaa.zaa) {
            return;
        }
        Object object = this.zab.zab();
        if (((ConnectionResult)object).hasResolution()) {
            this.zaa.mLifecycleFragment.startActivityForResult(GoogleApiActivity.zaa((Context)this.zaa.getActivity(), Preconditions.checkNotNull(((ConnectionResult)object).getResolution()), this.zab.zaa(), false), 1);
            return;
        }
        zap zap2 = this.zaa;
        if (zap2.zac.getErrorResolutionIntent((Context)zap2.getActivity(), ((ConnectionResult)object).getErrorCode(), null) != null) {
            zap2 = this.zaa;
            zap2.zac.zag(zap2.getActivity(), this.zaa.mLifecycleFragment, ((ConnectionResult)object).getErrorCode(), 2, this.zaa);
            return;
        }
        if (((ConnectionResult)object).getErrorCode() == 18) {
            object = this.zaa;
            zap2 = ((zap)object).zac.zab(((LifecycleCallback)object).getActivity(), this.zaa);
            object = this.zaa;
            ((zap)object).zac.zac(((LifecycleCallback)object).getActivity().getApplicationContext(), new zan(this, (Dialog)zap2));
            return;
        }
        zap.zaf(this.zaa, (ConnectionResult)object, this.zab.zaa());
    }
}

