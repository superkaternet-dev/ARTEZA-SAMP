/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Looper
 */
package com.google.android.gms.common.api.internal;

import android.os.Looper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.internal.zaaw;
import com.google.android.gms.common.internal.BaseGmsClient;
import com.google.android.gms.common.internal.Preconditions;
import java.lang.ref.WeakReference;

final class zaal
implements BaseGmsClient.ConnectionProgressReportCallbacks {
    private final WeakReference<zaaw> zaa;
    private final Api<?> zab;
    private final boolean zac;

    public zaal(zaaw zaaw2, Api<?> api, boolean bl) {
        this.zaa = new WeakReference<zaaw>(zaaw2);
        this.zab = api;
        this.zac = bl;
    }

    static /* bridge */ /* synthetic */ boolean zaa(zaal zaal2) {
        return zaal2.zac;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public final void onReportServiceBinding(ConnectionResult object) {
        block7: {
            zaaw zaaw2;
            block6: {
                block5: {
                    zaaw2 = (zaaw)this.zaa.get();
                    if (zaaw2 == null) {
                        return;
                    }
                    boolean bl = Looper.myLooper() == ((GoogleApiClient)zaaw.zak((zaaw)zaaw2).zag).getLooper();
                    Preconditions.checkState(bl, "onReportServiceBinding must be called on the GoogleApiClient handler thread");
                    zaaw.zap(zaaw2).lock();
                    try {
                        bl = zaaw.zaw(zaaw2, 0);
                        if (bl) break block5;
                    }
                    catch (Throwable throwable) {
                        zaaw.zap(zaaw2).unlock();
                        throw throwable;
                    }
                    object = zaaw.zap(zaaw2);
                    break block7;
                }
                if (!((ConnectionResult)object).isSuccess()) {
                    zaaw.zat(zaaw2, (ConnectionResult)object, this.zab, this.zac);
                }
                if (!zaaw.zax(zaaw2)) break block6;
                zaaw.zau(zaaw2);
            }
            object = zaaw.zap(zaaw2);
        }
        object.unlock();
    }
}

