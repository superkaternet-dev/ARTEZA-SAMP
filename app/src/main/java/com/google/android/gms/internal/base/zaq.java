/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Handler
 *  android.os.Handler$Callback
 *  android.os.Looper
 */
package com.google.android.gms.internal.base;

import android.os.Handler;
import android.os.Looper;

public class zaq
extends Handler {
    public zaq() {
    }

    public zaq(Looper looper) {
        super(looper);
    }

    public zaq(Looper looper, Handler.Callback callback) {
        super(looper, callback);
    }
}

