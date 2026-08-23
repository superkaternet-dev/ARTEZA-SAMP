/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Looper
 *  android.os.Message
 *  android.util.Log
 */
package com.google.android.gms.common.api.internal;

import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.zacp;
import com.google.android.gms.common.api.internal.zada;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.base.zaq;

final class zacz
extends zaq {
    final zada zaa;

    public zacz(zada zada2, Looper looper) {
        this.zaa = zada2;
        super(looper);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void handleMessage(Message object) {
        switch (((Message)object).what) {
            default: {
                int n = ((Message)object).what;
                object = new StringBuilder(70);
                ((StringBuilder)object).append("TransformationResultHandler received unknown message type: ");
                ((StringBuilder)object).append(n);
                Log.e((String)"TransformedResultImpl", (String)((StringBuilder)object).toString());
                return;
            }
            case 1: {
                RuntimeException runtimeException = (RuntimeException)((Message)object).obj;
                object = String.valueOf(runtimeException.getMessage());
                object = ((String)object).length() != 0 ? "Runtime exception on the transformation worker thread: ".concat((String)object) : new String("Runtime exception on the transformation worker thread: ");
                Log.e((String)"TransformedResultImpl", (String)object);
                throw runtimeException;
            }
            case 0: 
        }
        Object object2 = (PendingResult)((Message)object).obj;
        object = zada.zad(this.zaa);
        synchronized (object) {
            zada zada2 = Preconditions.checkNotNull(zada.zac(this.zaa));
            if (object2 == null) {
                object2 = new Status(13, "Transform returned null");
                zada.zag(zada2, (Status)object2);
            } else if (object2 instanceof zacp) {
                zada.zag(zada2, ((zacp)object2).zaa());
            } else {
                zada2.zai((PendingResult<?>)object2);
            }
            return;
        }
    }
}

