/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Handler
 *  android.os.Handler$Callback
 *  android.os.Looper
 */
package com.google.android.gms.internal.common;

import android.os.Handler;
import android.os.Looper;

public class zzi
extends Handler {
    public zzi() {
    }

    public zzi(Looper looper) {
        super(looper);
    }

    public zzi(Looper looper, Handler.Callback callback) {
        super(looper, callback);
    }
}

