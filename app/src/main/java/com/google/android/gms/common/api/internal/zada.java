/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Looper
 *  android.util.Log
 */
package com.google.android.gms.common.api.internal;

import android.os.Looper;
import android.util.Log;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Releasable;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.ResultCallbacks;
import com.google.android.gms.common.api.ResultTransform;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.TransformedResult;
import com.google.android.gms.common.api.internal.zaco;
import com.google.android.gms.common.api.internal.zacy;
import com.google.android.gms.common.api.internal.zacz;
import com.google.android.gms.common.internal.Preconditions;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;

public final class zada<R extends Result>
extends TransformedResult<R>
implements ResultCallback<R> {
    private ResultTransform<? super R, ? extends Result> zaa = null;
    private zada<? extends Result> zab = null;
    private volatile ResultCallbacks<? super R> zac = null;
    private PendingResult<R> zad = null;
    private final Object zae = new Object();
    private Status zaf = null;
    private final WeakReference<GoogleApiClient> zag;
    private final zacz zah;
    private boolean zai = false;

    public zada(WeakReference<GoogleApiClient> object) {
        Preconditions.checkNotNull(object, "GoogleApiClient reference must not be null");
        this.zag = object;
        object = (GoogleApiClient)((Reference)object).get();
        object = object != null ? ((GoogleApiClient)object).getLooper() : Looper.getMainLooper();
        this.zah = new zacz(this, (Looper)object);
    }

    static /* bridge */ /* synthetic */ ResultTransform zaa(zada zada2) {
        return zada2.zaa;
    }

    static /* bridge */ /* synthetic */ zacz zab(zada zada2) {
        return zada2.zah;
    }

    static /* bridge */ /* synthetic */ zada zac(zada zada2) {
        return zada2.zab;
    }

    static /* bridge */ /* synthetic */ Object zad(zada zada2) {
        return zada2.zae;
    }

    static /* bridge */ /* synthetic */ WeakReference zae(zada zada2) {
        return zada2.zag;
    }

    static /* bridge */ /* synthetic */ void zaf(zada zada2, Result result) {
        zada.zan(result);
    }

    static /* bridge */ /* synthetic */ void zag(zada zada2, Status status) {
        zada2.zaj(status);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private final void zaj(Status status) {
        Object object = this.zae;
        synchronized (object) {
            this.zaf = status;
            this.zal(status);
            return;
        }
    }

    private final void zak() {
        if (this.zaa == null && this.zac == null) {
            return;
        }
        PendingResult<R> pendingResult = (GoogleApiClient)this.zag.get();
        if (!this.zai && this.zaa != null && pendingResult != null) {
            ((GoogleApiClient)((Object)pendingResult)).zao(this);
            this.zai = true;
        }
        if ((pendingResult = this.zaf) != null) {
            this.zal((Status)((Object)pendingResult));
            return;
        }
        pendingResult = this.zad;
        if (pendingResult != null) {
            pendingResult.setResultCallback(this);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private final void zal(Status status) {
        Object object = this.zae;
        synchronized (object) {
            ResultTransform<R, Result> resultTransform = this.zaa;
            if (resultTransform != null) {
                status = Preconditions.checkNotNull(resultTransform.onFailure(status), "onFailure must not return null");
                super.zaj(status);
            } else if (this.zam()) {
                Preconditions.checkNotNull(this.zac).onFailure(status);
            }
            return;
        }
    }

    private final boolean zam() {
        GoogleApiClient googleApiClient = (GoogleApiClient)this.zag.get();
        return this.zac != null && googleApiClient != null;
    }

    private static final void zan(Result object) {
        if (object instanceof Releasable) {
            try {
                ((Releasable)object).release();
                return;
            }
            catch (RuntimeException runtimeException) {
                object = String.valueOf(object);
                String.valueOf(object).length();
                Log.w((String)"TransformedResultImpl", (String)"Unable to release ".concat(String.valueOf(object)), (Throwable)runtimeException);
                return;
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public final void andFinally(ResultCallbacks<? super R> resultCallbacks) {
        Object object = this.zae;
        synchronized (object) {
            ResultCallbacks<? super R> resultCallbacks2 = this.zac;
            boolean bl = true;
            boolean bl2 = resultCallbacks2 == null;
            Preconditions.checkState(bl2, "Cannot call andFinally() twice.");
            bl2 = this.zaa == null ? bl : false;
            Preconditions.checkState(bl2, "Cannot call then() and andFinally() on the same TransformedResult.");
            this.zac = resultCallbacks;
            this.zak();
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public final void onResult(R r) {
        Object object = this.zae;
        synchronized (object) {
            block6: {
                block5: {
                    if (!r.getStatus().isSuccess()) break block5;
                    if (this.zaa != null) {
                        ExecutorService executorService = zaco.zaa();
                        zacy zacy2 = new zacy(this, (Result)r);
                        executorService.submit(zacy2);
                        break block6;
                    } else if (this.zam()) {
                        Preconditions.checkNotNull(this.zac).onSuccess(r);
                    }
                    break block6;
                }
                this.zaj(r.getStatus());
                zada.zan(r);
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public final <S extends Result> TransformedResult<S> then(ResultTransform<? super R, ? extends S> zada2) {
        Object object = this.zae;
        synchronized (object) {
            ResultTransform<? super R, ? extends Result> resultTransform = this.zaa;
            boolean bl = true;
            boolean bl2 = resultTransform == null;
            Preconditions.checkState(bl2, "Cannot call then() twice.");
            bl2 = this.zac == null ? bl : false;
            Preconditions.checkState(bl2, "Cannot call then() and andFinally() on the same TransformedResult.");
            this.zaa = zada2;
            zada2 = new zada<R>(this.zag);
            this.zab = zada2;
            this.zak();
            return zada2;
        }
    }

    final void zah() {
        this.zac = null;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void zai(PendingResult<?> pendingResult) {
        Object object = this.zae;
        synchronized (object) {
            this.zad = pendingResult;
            this.zak();
            return;
        }
    }
}

