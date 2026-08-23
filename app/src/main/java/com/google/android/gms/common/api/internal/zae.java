/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.DeadObjectException
 *  android.util.Log
 */
package com.google.android.gms.common.api.internal;

import android.os.DeadObjectException;
import android.util.Log;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.BaseImplementation;
import com.google.android.gms.common.api.internal.BasePendingResult;
import com.google.android.gms.common.api.internal.zaad;
import com.google.android.gms.common.api.internal.zabq;
import com.google.android.gms.common.api.internal.zai;
import com.google.android.gms.common.internal.Preconditions;

public final class zae<A extends BaseImplementation.ApiMethodImpl<? extends Result, Api.AnyClient>>
extends zai {
    protected final A zaa;

    public zae(int n, A a) {
        super(n);
        this.zaa = (BaseImplementation.ApiMethodImpl)Preconditions.checkNotNull(a, "Null methods are not runnable.");
    }

    @Override
    public final void zad(Status status) {
        try {
            ((BaseImplementation.ApiMethodImpl)this.zaa).setFailedResult(status);
            return;
        }
        catch (IllegalStateException illegalStateException) {
            Log.w((String)"ApiCallRunner", (String)"Exception reporting failure", (Throwable)illegalStateException);
            return;
        }
    }

    @Override
    public final void zae(Exception object) {
        String string2 = object.getClass().getSimpleName();
        object = ((Throwable)object).getLocalizedMessage();
        StringBuilder stringBuilder = new StringBuilder(String.valueOf(string2).length() + 2 + String.valueOf(object).length());
        stringBuilder.append(string2);
        stringBuilder.append(": ");
        stringBuilder.append((String)object);
        object = new Status(10, stringBuilder.toString());
        try {
            ((BaseImplementation.ApiMethodImpl)this.zaa).setFailedResult((Status)object);
            return;
        }
        catch (IllegalStateException illegalStateException) {
            Log.w((String)"ApiCallRunner", (String)"Exception reporting failure", (Throwable)illegalStateException);
            return;
        }
    }

    @Override
    public final void zaf(zabq<?> zabq2) throws DeadObjectException {
        try {
            ((BaseImplementation.ApiMethodImpl)((Object)this.zaa)).run((Api.Client)zabq2.zaf());
            return;
        }
        catch (RuntimeException runtimeException) {
            this.zae(runtimeException);
            return;
        }
    }

    @Override
    public final void zag(zaad zaad2, boolean bl) {
        zaad2.zac((BasePendingResult<? extends Result>)this.zaa, bl);
    }
}

