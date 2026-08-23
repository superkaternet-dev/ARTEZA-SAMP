/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.DeadObjectException
 *  android.os.RemoteException
 */
package com.google.android.gms.common.api.internal;

import android.os.DeadObjectException;
import android.os.RemoteException;
import com.google.android.gms.common.Feature;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.StatusExceptionMapper;
import com.google.android.gms.common.api.internal.TaskApiCall;
import com.google.android.gms.common.api.internal.zaad;
import com.google.android.gms.common.api.internal.zabq;
import com.google.android.gms.common.api.internal.zac;
import com.google.android.gms.common.api.internal.zai;
import com.google.android.gms.tasks.TaskCompletionSource;

public final class zag<ResultT>
extends zac {
    private final TaskApiCall<Api.AnyClient, ResultT> zaa;
    private final TaskCompletionSource<ResultT> zab;
    private final StatusExceptionMapper zad;

    public zag(int n, TaskApiCall<Api.AnyClient, ResultT> taskApiCall, TaskCompletionSource<ResultT> taskCompletionSource, StatusExceptionMapper statusExceptionMapper) {
        super(n);
        this.zab = taskCompletionSource;
        this.zaa = taskApiCall;
        this.zad = statusExceptionMapper;
        if (n == 2 && taskApiCall.shouldAutoResolveMissingFeatures()) {
            throw new IllegalArgumentException("Best-effort write calls cannot pass methods that should auto-resolve missing features.");
        }
    }

    @Override
    public final boolean zaa(zabq<?> zabq2) {
        return this.zaa.shouldAutoResolveMissingFeatures();
    }

    @Override
    public final Feature[] zab(zabq<?> zabq2) {
        return this.zaa.zab();
    }

    @Override
    public final void zad(Status status) {
        this.zab.trySetException(this.zad.getException(status));
    }

    @Override
    public final void zae(Exception exception) {
        this.zab.trySetException(exception);
    }

    @Override
    public final void zaf(zabq<?> zabq2) throws DeadObjectException {
        try {
            this.zaa.doExecute(zabq2.zaf(), this.zab);
            return;
        }
        catch (RuntimeException runtimeException) {
            this.zab.trySetException(runtimeException);
            return;
        }
        catch (RemoteException remoteException) {
            this.zad(zai.zah(remoteException));
            return;
        }
        catch (DeadObjectException deadObjectException) {
            throw deadObjectException;
        }
    }

    @Override
    public final void zag(zaad zaad2, boolean bl) {
        zaad2.zad(this.zab, bl);
    }
}

