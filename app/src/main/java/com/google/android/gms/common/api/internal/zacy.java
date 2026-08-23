/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.internal.BasePendingResult;
import com.google.android.gms.common.api.internal.zada;
import com.google.android.gms.common.internal.Preconditions;

final class zacy
implements Runnable {
    final Result zaa;
    final zada zab;

    zacy(zada zada2, Result result) {
        this.zab = zada2;
        this.zaa = result;
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public final void run() {
        Throwable throwable2;
        block5: {
            Object object;
            block6: {
                BasePendingResult.zaa.set(true);
                object = Preconditions.checkNotNull(zada.zaa(this.zab)).onSuccess(this.zaa);
                zada zada2 = this.zab;
                zada.zab(zada2).sendMessage(zada.zab(zada2).obtainMessage(0, object));
                {
                    catch (Throwable throwable2) {
                        break block5;
                    }
                    catch (RuntimeException runtimeException) {}
                    {
                        object = this.zab;
                        zada.zab((zada)object).sendMessage(zada.zab((zada)object).obtainMessage(1, runtimeException));
                        BasePendingResult.zaa.set(false);
                    }
                    zada.zaf(this.zab, this.zaa);
                    object = (GoogleApiClient)zada.zae(this.zab).get();
                    if (object == null) return;
                    break block6;
                }
                BasePendingResult.zaa.set(false);
                zada.zaf(this.zab, this.zaa);
                object = (GoogleApiClient)zada.zae(this.zab).get();
                if (object == null) return;
            }
            ((GoogleApiClient)object).zap(this.zab);
            return;
        }
        BasePendingResult.zaa.set(false);
        zada.zaf(this.zab, this.zaa);
        GoogleApiClient googleApiClient = (GoogleApiClient)zada.zae(this.zab).get();
        if (googleApiClient == null) {
            throw throwable2;
        }
        googleApiClient.zap(this.zab);
        throw throwable2;
    }
}

