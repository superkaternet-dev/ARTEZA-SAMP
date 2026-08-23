/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.api;

import com.google.android.gms.common.api.Batch;
import com.google.android.gms.common.api.BatchResult;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;

final class zab
implements PendingResult.StatusListener {
    final Batch zaa;

    zab(Batch batch) {
        this.zaa = batch;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public final void onComplete(Status object) {
        Object object2 = Batch.zab(this.zaa);
        synchronized (object2) {
            if (this.zaa.isCanceled()) {
                return;
            }
            if (((Status)object).isCanceled()) {
                Batch.zad(this.zaa, true);
            } else if (!((Status)object).isSuccess()) {
                Batch.zac(this.zaa, true);
            }
            object = this.zaa;
            Batch.zae((Batch)object, Batch.zaa((Batch)object) - 1);
            object = this.zaa;
            if (Batch.zaa((Batch)object) == 0) {
                if (Batch.zah((Batch)object)) {
                    Batch.zaf((Batch)object);
                } else {
                    object = Batch.zag((Batch)object) ? new Status(13) : Status.RESULT_SUCCESS;
                    Batch batch = this.zaa;
                    BatchResult batchResult = new BatchResult((Status)object, Batch.zai(batch));
                    batch.setResult(batchResult);
                }
            }
            return;
        }
    }
}

