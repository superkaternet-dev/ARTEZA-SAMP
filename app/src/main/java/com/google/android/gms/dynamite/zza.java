/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Process
 */
package com.google.android.gms.dynamite;

import android.os.Process;

final class zza
extends Thread {
    zza(ThreadGroup threadGroup, String string2) {
        super(threadGroup, "GmsDynamite");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public final void run() {
        Process.setThreadPriority((int)19);
        synchronized (this) {
            try {
                try {
                    while (true) {
                        this.wait();
                    }
                }
                catch (InterruptedException interruptedException) {
                    return;
                }
            }
            catch (Throwable throwable) {}
            throw throwable;
        }
    }
}

