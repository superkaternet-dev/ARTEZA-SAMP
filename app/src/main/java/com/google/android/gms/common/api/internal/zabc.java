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
import com.google.android.gms.common.api.internal.zabe;
import com.google.android.gms.internal.base.zaq;

final class zabc
extends zaq {
    final zabe zaa;

    zabc(zabe zabe2, Looper looper) {
        this.zaa = zabe2;
        super(looper);
    }

    public final void handleMessage(Message object) {
        switch (((Message)object).what) {
            default: {
                int n = ((Message)object).what;
                object = new StringBuilder(31);
                ((StringBuilder)object).append("Unknown message id: ");
                ((StringBuilder)object).append(n);
                Log.w((String)"GoogleApiClientImpl", (String)((StringBuilder)object).toString());
                return;
            }
            case 2: {
                zabe.zai(this.zaa);
                return;
            }
            case 1: 
        }
        zabe.zaj(this.zaa);
    }
}

