/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Looper
 *  android.os.Message
 *  android.os.RemoteException
 *  android.util.Log
 *  android.util.Pair
 */
package com.google.android.gms.common.api.internal;

import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.util.Pair;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Releasable;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.ResultTransform;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.TransformedResult;
import com.google.android.gms.common.api.internal.zada;
import com.google.android.gms.common.api.internal.zadb;
import com.google.android.gms.common.api.internal.zaq;
import com.google.android.gms.common.api.internal.zas;
import com.google.android.gms.common.internal.ICancelToken;
import com.google.android.gms.common.internal.Preconditions;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public abstract class BasePendingResult<R extends Result>
extends PendingResult<R> {
    static final ThreadLocal<Boolean> zaa = new zaq();
    public static final int zad = 0;
    private zas mResultGuardian;
    protected final CallbackHandler<R> zab;
    protected final WeakReference<GoogleApiClient> zac;
    private final Object zae = new Object();
    private final CountDownLatch zaf = new CountDownLatch(1);
    private final ArrayList<PendingResult.StatusListener> zag = new ArrayList();
    private ResultCallback<? super R> zah;
    private final AtomicReference<zadb> zai = new AtomicReference();
    private R zaj;
    private Status zak;
    private volatile boolean zal;
    private boolean zam;
    private boolean zan;
    private ICancelToken zao;
    private volatile zada<R> zap;
    private boolean zaq = false;

    @Deprecated
    BasePendingResult() {
        this.zab = new CallbackHandler(Looper.getMainLooper());
        this.zac = new WeakReference<Object>(null);
    }

    @Deprecated
    protected BasePendingResult(Looper looper) {
        this.zab = new CallbackHandler(looper);
        this.zac = new WeakReference<Object>(null);
    }

    protected BasePendingResult(GoogleApiClient googleApiClient) {
        Looper looper = googleApiClient != null ? googleApiClient.getLooper() : Looper.getMainLooper();
        this.zab = new CallbackHandler(looper);
        this.zac = new WeakReference<GoogleApiClient>(googleApiClient);
    }

    protected BasePendingResult(CallbackHandler<R> callbackHandler) {
        this.zab = Preconditions.checkNotNull(callbackHandler, "CallbackHandler must not be null");
        this.zac = new WeakReference<Object>(null);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private final R zaa() {
        R r;
        Object object = this.zae;
        synchronized (object) {
            Preconditions.checkState(this.zal ^ true, "Result has already been consumed.");
            Preconditions.checkState(this.isReady(), "Result is not ready.");
            r = this.zaj;
            this.zaj = null;
            this.zah = null;
            this.zal = true;
        }
        object = this.zai.getAndSet(null);
        if (object != null) {
            ((zadb)object).zaa.zab.remove(this);
        }
        return (R)((Result)Preconditions.checkNotNull(r));
    }

    private final void zab(R object) {
        this.zaj = object;
        this.zak = object.getStatus();
        this.zao = null;
        this.zaf.countDown();
        if (this.zam) {
            this.zah = null;
        } else {
            object = this.zah;
            if (object == null) {
                if (this.zaj instanceof Releasable) {
                    this.mResultGuardian = new zas(this, null);
                }
            } else {
                this.zab.removeMessages(2);
                this.zab.zaa((ResultCallback<R>)object, this.zaa());
            }
        }
        object = this.zag;
        int n = object.size();
        for (int i = 0; i < n; ++i) {
            ((PendingResult.StatusListener)object.get(i)).onComplete(this.zak);
        }
        this.zag.clear();
    }

    static /* bridge */ /* synthetic */ Result zaj(BasePendingResult basePendingResult) {
        return basePendingResult.zaj;
    }

    public static void zal(Result object) {
        if (object instanceof Releasable) {
            try {
                ((Releasable)object).release();
                return;
            }
            catch (RuntimeException runtimeException) {
                object = String.valueOf(object);
                String.valueOf(object).length();
                Log.w((String)"BasePendingResult", (String)"Unable to release ".concat(String.valueOf(object)), (Throwable)runtimeException);
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
    public final void addStatusListener(PendingResult.StatusListener statusListener) {
        boolean bl = statusListener != null;
        Preconditions.checkArgument(bl, "Callback cannot be null.");
        Object object = this.zae;
        synchronized (object) {
            if (this.isReady()) {
                statusListener.onComplete(this.zak);
            } else {
                this.zag.add(statusListener);
            }
            return;
        }
    }

    @Override
    public final R await() {
        Preconditions.checkNotMainThread("await must not be called on the UI thread");
        boolean bl = this.zal;
        boolean bl2 = true;
        Preconditions.checkState(bl ^ true, "Result has already been consumed");
        if (this.zap != null) {
            bl2 = false;
        }
        Preconditions.checkState(bl2, "Cannot await if then() has been called.");
        try {
            this.zaf.await();
        }
        catch (InterruptedException interruptedException) {
            this.forceFailureUnlessReady(Status.RESULT_INTERRUPTED);
        }
        Preconditions.checkState(this.isReady(), "Result is not ready.");
        return this.zaa();
    }

    @Override
    public final R await(long l, TimeUnit timeUnit) {
        if (l > 0L) {
            Preconditions.checkNotMainThread("await must not be called on the UI thread when time is greater than zero.");
        }
        boolean bl = this.zal;
        boolean bl2 = true;
        Preconditions.checkState(bl ^ true, "Result has already been consumed.");
        if (this.zap != null) {
            bl2 = false;
        }
        Preconditions.checkState(bl2, "Cannot await if then() has been called.");
        try {
            if (!this.zaf.await(l, timeUnit)) {
                this.forceFailureUnlessReady(Status.RESULT_TIMEOUT);
            }
        }
        catch (InterruptedException interruptedException) {
            this.forceFailureUnlessReady(Status.RESULT_INTERRUPTED);
        }
        Preconditions.checkState(this.isReady(), "Result is not ready.");
        return this.zaa();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void cancel() {
        Object object = this.zae;
        synchronized (object) {
            if (!this.zam && !this.zal) {
                ICancelToken iCancelToken = this.zao;
                if (iCancelToken != null) {
                    try {
                        iCancelToken.cancel();
                    }
                    catch (RemoteException remoteException) {
                        // empty catch block
                    }
                }
                BasePendingResult.zal(this.zaj);
                this.zam = true;
                this.zab(this.createFailedResult(Status.RESULT_CANCELED));
                return;
            }
            return;
        }
    }

    protected abstract R createFailedResult(Status var1);

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Deprecated
    public final void forceFailureUnlessReady(Status status) {
        Object object = this.zae;
        synchronized (object) {
            if (!this.isReady()) {
                this.setResult(this.createFailedResult(status));
                this.zan = true;
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
    public final boolean isCanceled() {
        Object object = this.zae;
        synchronized (object) {
            return this.zam;
        }
    }

    public final boolean isReady() {
        return this.zaf.getCount() == 0L;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected final void setCancelToken(ICancelToken iCancelToken) {
        Object object = this.zae;
        synchronized (object) {
            this.zao = iCancelToken;
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void setResult(R r) {
        Object object = this.zae;
        synchronized (object) {
            if (!this.zan && !this.zam) {
                this.isReady();
                Preconditions.checkState(this.isReady() ^ true, "Results have already been set");
                Preconditions.checkState(this.zal ^ true, "Result has already been consumed");
                this.zab(r);
                return;
            }
            BasePendingResult.zal(r);
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public final void setResultCallback(ResultCallback<? super R> resultCallback) {
        Object object = this.zae;
        synchronized (object) {
            if (resultCallback == null) {
                this.zah = null;
                return;
            }
            boolean bl = this.zal;
            boolean bl2 = true;
            Preconditions.checkState(bl ^ true, "Result has already been consumed.");
            if (this.zap != null) {
                bl2 = false;
            }
            Preconditions.checkState(bl2, "Cannot set callbacks if then() has been called.");
            if (this.isCanceled()) {
                return;
            }
            if (this.isReady()) {
                this.zab.zaa(resultCallback, (R)this.zaa());
            } else {
                this.zah = resultCallback;
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
    public final void setResultCallback(ResultCallback<? super R> object, long l, TimeUnit timeUnit) {
        Object object2 = this.zae;
        synchronized (object2) {
            if (object == null) {
                this.zah = null;
                return;
            }
            boolean bl = this.zal;
            boolean bl2 = true;
            Preconditions.checkState(bl ^ true, "Result has already been consumed.");
            if (this.zap != null) {
                bl2 = false;
            }
            Preconditions.checkState(bl2, "Cannot set callbacks if then() has been called.");
            if (this.isCanceled()) {
                return;
            }
            if (this.isReady()) {
                this.zab.zaa((ResultCallback<? super R>)object, (R)this.zaa());
            } else {
                this.zah = object;
                object = this.zab;
                l = timeUnit.toMillis(l);
                object.sendMessageDelayed(object.obtainMessage(2, this), l);
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
    public final <S extends Result> TransformedResult<S> then(ResultTransform<? super R, ? extends S> object) {
        Preconditions.checkState(this.zal ^ true, "Result has already been consumed.");
        Object object2 = this.zae;
        synchronized (object2) {
            zada zada2 = this.zap;
            boolean bl = false;
            boolean bl2 = zada2 == null;
            Preconditions.checkState(bl2, "Cannot call then() twice.");
            bl2 = this.zah == null ? true : bl;
            Preconditions.checkState(bl2, "Cannot call then() if callbacks are set.");
            Preconditions.checkState(this.zam ^ true, "Cannot call then() if result was canceled.");
            this.zaq = true;
            zada2 = new zada(this.zac);
            this.zap = zada2;
            object = this.zap.then(object);
            if (this.isReady()) {
                this.zab.zaa(this.zap, this.zaa());
            } else {
                this.zah = this.zap;
            }
            return object;
        }
    }

    public final void zak() {
        boolean bl;
        boolean bl2 = this.zaq;
        boolean bl3 = bl = true;
        if (!bl2) {
            bl3 = zaa.get() != false ? bl : false;
        }
        this.zaq = bl3;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final boolean zam() {
        Object object = this.zae;
        synchronized (object) {
            if ((GoogleApiClient)this.zac.get() != null) {
                if (this.zaq) return this.isCanceled();
            }
            this.cancel();
            return this.isCanceled();
        }
    }

    public final void zan(zadb zadb2) {
        this.zai.set(zadb2);
    }

    public static class CallbackHandler<R extends Result>
    extends com.google.android.gms.internal.base.zaq {
        public CallbackHandler() {
            super(Looper.getMainLooper());
        }

        public CallbackHandler(Looper looper) {
            super(looper);
        }

        public final void handleMessage(Message object) {
            switch (((Message)object).what) {
                default: {
                    int n = ((Message)object).what;
                    object = new StringBuilder(45);
                    ((StringBuilder)object).append("Don't know how to handle message: ");
                    ((StringBuilder)object).append(n);
                    Exception exception = new Exception();
                    Log.wtf((String)"BasePendingResult", (String)((StringBuilder)object).toString(), (Throwable)exception);
                    return;
                }
                case 2: {
                    ((BasePendingResult)((Message)object).obj).forceFailureUnlessReady(Status.RESULT_TIMEOUT);
                    return;
                }
                case 1: 
            }
            Object object2 = (Pair)((Message)object).obj;
            object = (ResultCallback)object2.first;
            object2 = (Result)object2.second;
            try {
                object.onResult(object2);
                return;
            }
            catch (RuntimeException runtimeException) {
                BasePendingResult.zal((Result)object2);
                throw runtimeException;
            }
        }

        public final void zaa(ResultCallback<? super R> resultCallback, R r) {
            int n = zad;
            this.sendMessage(this.obtainMessage(1, new Pair(Preconditions.checkNotNull(resultCallback), r)));
        }
    }
}

