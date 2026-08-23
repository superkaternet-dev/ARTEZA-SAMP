/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 */
package com.google.android.gms.tasks;

import android.app.Activity;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.DuplicateTaskCompletionException;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.RuntimeExecutionException;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.gms.tasks.zzd;
import com.google.android.gms.tasks.zzf;
import com.google.android.gms.tasks.zzh;
import com.google.android.gms.tasks.zzj;
import com.google.android.gms.tasks.zzl;
import com.google.android.gms.tasks.zzn;
import com.google.android.gms.tasks.zzp;
import com.google.android.gms.tasks.zzq;
import com.google.android.gms.tasks.zzr;
import com.google.android.gms.tasks.zzv;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Executor;

final class zzw<TResult>
extends Task<TResult> {
    private final Object zza = new Object();
    private final zzr<TResult> zzb = new zzr();
    private boolean zzc;
    private volatile boolean zzd;
    private TResult zze;
    private Exception zzf;

    zzw() {
    }

    private final void zzf() {
        Preconditions.checkState(this.zzc, "Task is not yet complete");
    }

    private final void zzg() {
        if (!this.zzd) {
            return;
        }
        throw new CancellationException("Task is already canceled.");
    }

    private final void zzh() {
        if (!this.zzc) {
            return;
        }
        throw DuplicateTaskCompletionException.of(this);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private final void zzi() {
        Object object = this.zza;
        synchronized (object) {
            if (!this.zzc) {
                return;
            }
        }
        this.zzb.zzb(this);
    }

    @Override
    public final Task<TResult> addOnCanceledListener(Activity activity, OnCanceledListener object) {
        object = new zzh(TaskExecutors.MAIN_THREAD, (OnCanceledListener)object);
        this.zzb.zza((zzq<TResult>)object);
        zzv.zza(activity).zzb(object);
        this.zzi();
        return this;
    }

    @Override
    public final Task<TResult> addOnCanceledListener(OnCanceledListener onCanceledListener) {
        ((Task)this).addOnCanceledListener(TaskExecutors.MAIN_THREAD, onCanceledListener);
        return this;
    }

    @Override
    public final Task<TResult> addOnCanceledListener(Executor executor, OnCanceledListener onCanceledListener) {
        this.zzb.zza(new zzh(executor, onCanceledListener));
        this.zzi();
        return this;
    }

    @Override
    public final Task<TResult> addOnCompleteListener(Activity activity, OnCompleteListener<TResult> object) {
        object = new zzj<TResult>(TaskExecutors.MAIN_THREAD, object);
        this.zzb.zza((zzq<TResult>)object);
        zzv.zza(activity).zzb(object);
        this.zzi();
        return this;
    }

    @Override
    public final Task<TResult> addOnCompleteListener(OnCompleteListener<TResult> onCompleteListener) {
        Executor executor = TaskExecutors.MAIN_THREAD;
        this.zzb.zza(new zzj<TResult>(executor, onCompleteListener));
        this.zzi();
        return this;
    }

    @Override
    public final Task<TResult> addOnCompleteListener(Executor executor, OnCompleteListener<TResult> onCompleteListener) {
        this.zzb.zza(new zzj<TResult>(executor, onCompleteListener));
        this.zzi();
        return this;
    }

    @Override
    public final Task<TResult> addOnFailureListener(Activity activity, OnFailureListener object) {
        object = new zzl(TaskExecutors.MAIN_THREAD, (OnFailureListener)object);
        this.zzb.zza((zzq<TResult>)object);
        zzv.zza(activity).zzb(object);
        this.zzi();
        return this;
    }

    @Override
    public final Task<TResult> addOnFailureListener(OnFailureListener onFailureListener) {
        this.addOnFailureListener(TaskExecutors.MAIN_THREAD, onFailureListener);
        return this;
    }

    @Override
    public final Task<TResult> addOnFailureListener(Executor executor, OnFailureListener onFailureListener) {
        this.zzb.zza(new zzl(executor, onFailureListener));
        this.zzi();
        return this;
    }

    @Override
    public final Task<TResult> addOnSuccessListener(Activity activity, OnSuccessListener<? super TResult> object) {
        object = new zzn<TResult>(TaskExecutors.MAIN_THREAD, object);
        this.zzb.zza((zzq<TResult>)object);
        zzv.zza(activity).zzb(object);
        this.zzi();
        return this;
    }

    @Override
    public final Task<TResult> addOnSuccessListener(OnSuccessListener<? super TResult> onSuccessListener) {
        this.addOnSuccessListener(TaskExecutors.MAIN_THREAD, onSuccessListener);
        return this;
    }

    @Override
    public final Task<TResult> addOnSuccessListener(Executor executor, OnSuccessListener<? super TResult> onSuccessListener) {
        this.zzb.zza(new zzn<TResult>(executor, onSuccessListener));
        this.zzi();
        return this;
    }

    @Override
    public final <TContinuationResult> Task<TContinuationResult> continueWith(Continuation<TResult, TContinuationResult> continuation) {
        return ((Task)this).continueWith(TaskExecutors.MAIN_THREAD, continuation);
    }

    @Override
    public final <TContinuationResult> Task<TContinuationResult> continueWith(Executor executor, Continuation<TResult, TContinuationResult> continuation) {
        zzw<TResult> zzw2 = new zzw<TResult>();
        this.zzb.zza(new zzd<TResult, TContinuationResult>(executor, continuation, zzw2));
        this.zzi();
        return zzw2;
    }

    @Override
    public final <TContinuationResult> Task<TContinuationResult> continueWithTask(Continuation<TResult, Task<TContinuationResult>> continuation) {
        return ((Task)this).continueWithTask(TaskExecutors.MAIN_THREAD, continuation);
    }

    @Override
    public final <TContinuationResult> Task<TContinuationResult> continueWithTask(Executor executor, Continuation<TResult, Task<TContinuationResult>> continuation) {
        zzw<TResult> zzw2 = new zzw<TResult>();
        this.zzb.zza(new zzf<TResult, TContinuationResult>(executor, continuation, zzw2));
        this.zzi();
        return zzw2;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public final Exception getException() {
        Object object = this.zza;
        synchronized (object) {
            return this.zzf;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public final TResult getResult() {
        Object object = this.zza;
        synchronized (object) {
            this.zzf();
            this.zzg();
            Exception exception = this.zzf;
            if (exception == null) {
                TResult TResult = this.zze;
                return TResult;
            }
            RuntimeExecutionException runtimeExecutionException = new RuntimeExecutionException(exception);
            throw runtimeExecutionException;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public final <X extends Throwable> TResult getResult(Class<X> serializable) throws X {
        Object object = this.zza;
        synchronized (object) {
            this.zzf();
            this.zzg();
            if (serializable.isInstance(this.zzf)) {
                throw (Throwable)serializable.cast(this.zzf);
            }
            serializable = this.zzf;
            if (serializable == null) {
                serializable = this.zze;
                return (TResult)serializable;
            }
            RuntimeExecutionException runtimeExecutionException = new RuntimeExecutionException((Throwable)serializable);
            throw runtimeExecutionException;
        }
    }

    @Override
    public final boolean isCanceled() {
        return this.zzd;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public final boolean isComplete() {
        Object object = this.zza;
        synchronized (object) {
            return this.zzc;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public final boolean isSuccessful() {
        Object object = this.zza;
        synchronized (object) {
            boolean bl;
            boolean bl2 = this.zzc;
            boolean bl3 = bl = false;
            if (!bl2) return bl3;
            bl3 = bl;
            if (this.zzd) return bl3;
            bl3 = bl;
            if (this.zzf != null) return bl3;
            return true;
        }
    }

    @Override
    public final <TContinuationResult> Task<TContinuationResult> onSuccessTask(SuccessContinuation<TResult, TContinuationResult> successContinuation) {
        Executor executor = TaskExecutors.MAIN_THREAD;
        zzw<TResult> zzw2 = new zzw<TResult>();
        this.zzb.zza(new zzp<TResult, TContinuationResult>(executor, successContinuation, zzw2));
        this.zzi();
        return zzw2;
    }

    @Override
    public final <TContinuationResult> Task<TContinuationResult> onSuccessTask(Executor executor, SuccessContinuation<TResult, TContinuationResult> successContinuation) {
        zzw<TResult> zzw2 = new zzw<TResult>();
        this.zzb.zza(new zzp<TResult, TContinuationResult>(executor, successContinuation, zzw2));
        this.zzi();
        return zzw2;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void zza(Exception exception) {
        Preconditions.checkNotNull(exception, "Exception must not be null");
        Object object = this.zza;
        synchronized (object) {
            this.zzh();
            this.zzc = true;
            this.zzf = exception;
        }
        this.zzb.zzb(this);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void zzb(TResult TResult) {
        Object object = this.zza;
        synchronized (object) {
            this.zzh();
            this.zzc = true;
            this.zze = TResult;
        }
        this.zzb.zzb(this);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final boolean zzc() {
        Object object = this.zza;
        synchronized (object) {
            if (this.zzc) {
                return false;
            }
            this.zzc = true;
            this.zzd = true;
        }
        this.zzb.zzb(this);
        return true;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final boolean zzd(Exception exception) {
        Preconditions.checkNotNull(exception, "Exception must not be null");
        Object object = this.zza;
        synchronized (object) {
            if (this.zzc) {
                return false;
            }
            this.zzc = true;
            this.zzf = exception;
        }
        this.zzb.zzb(this);
        return true;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final boolean zze(TResult TResult) {
        Object object = this.zza;
        synchronized (object) {
            if (this.zzc) {
                return false;
            }
            this.zzc = true;
            this.zze = TResult;
        }
        this.zzb.zzb(this);
        return true;
    }
}

