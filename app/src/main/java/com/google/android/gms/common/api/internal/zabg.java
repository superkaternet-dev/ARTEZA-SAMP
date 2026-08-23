/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.internal.zabf;
import com.google.android.gms.common.api.internal.zabi;

abstract class zabg {
    private final zabf zaa;

    protected zabg(zabf zabf2) {
        this.zaa = zabf2;
    }

    protected abstract void zaa();

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void zab(zabi object) {
        block4: {
            block3: {
                zabi.zah((zabi)object).lock();
                try {
                    zabf zabf2 = zabi.zag((zabi)object);
                    zabf zabf3 = this.zaa;
                    if (zabf2 == zabf3) break block3;
                }
                catch (Throwable throwable) {
                    zabi.zah((zabi)object).unlock();
                    throw throwable;
                }
                object = zabi.zah((zabi)object);
                break block4;
            }
            this.zaa();
            object = zabi.zah((zabi)object);
        }
        object.unlock();
    }
}

