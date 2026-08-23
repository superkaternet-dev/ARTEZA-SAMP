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
import com.google.android.gms.common.api.internal.zabg;
import com.google.android.gms.common.api.internal.zabi;
import com.google.android.gms.internal.base.zaq;

final class zabh
extends zaq {
    final zabi zaa;

    zabh(zabi zabi2, Looper looper) {
        this.zaa = zabi2;
        super(looper);
    }

    public final void handleMessage(Message object) {
        switch (((Message)object).what) {
            default: {
                int n = ((Message)object).what;
                object = new StringBuilder(31);
                ((StringBuilder)object).append("Unknown message id: ");
                ((StringBuilder)object).append(n);
                Log.w((String)"GACStateManager", (String)((StringBuilder)object).toString());
                return;
            }
            case 2: {
                throw (RuntimeException)((Message)object).obj;
            }
            case 1: 
        }
        ((zabg)((Message)object).obj).zab(this.zaa);
    }
}

