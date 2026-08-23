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
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.zaad;
import com.google.android.gms.common.api.internal.zabq;
import com.google.android.gms.common.api.internal.zac;
import com.google.android.gms.common.api.internal.zai;
import com.google.android.gms.tasks.TaskCompletionSource;

abstract class zad<T>
extends zac {
    protected final TaskCompletionSource<T> zaa;

    public zad(int n, TaskCompletionSource<T> taskCompletionSource) {
        super(n);
        this.zaa = taskCompletionSource;
    }

    protected abstract void zac(zabq<?> var1) throws RemoteException;

    @Override
    public final void zad(Status status) {
        this.zaa.trySetException(new ApiException(status));
    }

    @Override
    public final void zae(Exception exception) {
        this.zaa.trySetException(exception);
    }

    @Override
    public final void zaf(zabq<?> zabq2) throws DeadObjectException {
        try {
            this.zac(zabq2);
            return;
        }
        catch (RuntimeException runtimeException) {
            this.zaa.trySetException(runtimeException);
            return;
        }
        catch (RemoteException remoteException) {
            this.zad(zai.zah(remoteException));
            return;
        }
        catch (DeadObjectException deadObjectException) {
            this.zad(zai.zah((RemoteException)((Object)deadObjectException)));
            throw deadObjectException;
        }
    }

    @Override
    public void zag(zaad zaad2, boolean bl) {
    }
}

