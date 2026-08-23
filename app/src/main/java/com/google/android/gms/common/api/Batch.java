/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.api;

import com.google.android.gms.common.api.BatchResult;
import com.google.android.gms.common.api.BatchResultToken;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.BasePendingResult;
import com.google.android.gms.common.api.zab;
import com.google.android.gms.common.api.zac;
import java.util.ArrayList;
import java.util.List;

public final class Batch
extends BasePendingResult<BatchResult> {
    private int zae;
    private boolean zaf;
    private boolean zag;
    private final PendingResult<?>[] zah;
    private final Object zai = new Object();

    /* synthetic */ Batch(List list, GoogleApiClient object, zac zac2) {
        super((GoogleApiClient)object);
        int n;
        this.zae = n = list.size();
        object = new PendingResult[n];
        this.zah = object;
        if (!list.isEmpty()) {
            for (n = 0; n < list.size(); ++n) {
                this.zah[n] = object = (PendingResult)list.get(n);
                ((PendingResult)object).addStatusListener(new zab(this));
            }
            return;
        }
        this.setResult(new BatchResult(Status.RESULT_SUCCESS, (PendingResult<?>[])object));
    }

    static /* bridge */ /* synthetic */ int zaa(Batch batch) {
        return batch.zae;
    }

    static /* bridge */ /* synthetic */ Object zab(Batch batch) {
        return batch.zai;
    }

    static /* bridge */ /* synthetic */ void zac(Batch batch, boolean bl) {
        batch.zaf = true;
    }

    static /* bridge */ /* synthetic */ void zad(Batch batch, boolean bl) {
        batch.zag = true;
    }

    static /* bridge */ /* synthetic */ void zae(Batch batch, int n) {
        batch.zae = n;
    }

    static /* synthetic */ void zaf(Batch batch) {
        super.cancel();
    }

    static /* bridge */ /* synthetic */ boolean zag(Batch batch) {
        return batch.zaf;
    }

    static /* bridge */ /* synthetic */ boolean zah(Batch batch) {
        return batch.zag;
    }

    static /* bridge */ /* synthetic */ PendingResult[] zai(Batch batch) {
        return batch.zah;
    }

    @Override
    public void cancel() {
        super.cancel();
        PendingResult<?>[] pendingResultArray = this.zah;
        int n = pendingResultArray.length;
        for (int i = 0; i < n; ++i) {
            pendingResultArray[i].cancel();
        }
    }

    @Override
    public BatchResult createFailedResult(Status status) {
        return new BatchResult(status, this.zah);
    }

    public static final class Builder {
        private List<PendingResult<?>> zaa = new ArrayList();
        private GoogleApiClient zab;

        public Builder(GoogleApiClient googleApiClient) {
            this.zab = googleApiClient;
        }

        public <R extends Result> BatchResultToken<R> add(PendingResult<R> pendingResult) {
            BatchResultToken batchResultToken = new BatchResultToken(this.zaa.size());
            this.zaa.add(pendingResult);
            return batchResultToken;
        }

        public Batch build() {
            return new Batch(this.zaa, this.zab, null);
        }
    }
}

