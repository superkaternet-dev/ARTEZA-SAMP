/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package com.google.android.gms.common.api;

import android.util.Log;
import com.google.android.gms.common.api.Releasable;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public abstract class ResultCallbacks<R extends Result>
implements ResultCallback<R> {
    public abstract void onFailure(Status var1);

    @Override
    public final void onResult(R object) {
        Status status = object.getStatus();
        if (status.isSuccess()) {
            this.onSuccess(object);
            return;
        }
        this.onFailure(status);
        if (object instanceof Releasable) {
            try {
                ((Releasable)object).release();
                return;
            }
            catch (RuntimeException runtimeException) {
                object = String.valueOf(object);
                String.valueOf(object).length();
                Log.w((String)"ResultCallbacks", (String)"Unable to release ".concat(String.valueOf(object)), (Throwable)runtimeException);
                return;
            }
        }
    }

    public abstract void onSuccess(R var1);
}

