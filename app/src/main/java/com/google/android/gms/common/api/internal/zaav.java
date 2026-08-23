/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.internal.zaau;
import com.google.android.gms.common.api.internal.zaaw;

abstract class zaav
implements Runnable {
    final zaaw zab;

    /* synthetic */ zaav(zaaw zaaw2, zaau zaau2) {
        this.zab = zaaw2;
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public final void run() {
        block7: {
            block8: {
                zaaw.zap(this.zab).lock();
                var1_1 = Thread.interrupted();
                if (var1_1) {
                }
                ** GOTO lbl-1000
                {
                    catch (Throwable var2_3) {
                        break block7;
                    }
                    catch (RuntimeException var2_4) {}
                    {
                        zaaw.zak(this.zab).zam(var2_4);
                    }
                    var2_2 = zaaw.zap(this.zab);
                    break block8;
                }
                var2_2 = zaaw.zap(this.zab);
            }
lbl15:
            // 2 sources

            while (true) {
                var2_2.unlock();
                return;
            }
lbl-1000:
            // 1 sources

            {
                this.zaa();
            }
            var2_2 = zaaw.zap(this.zab);
            ** while (true)
        }
        zaaw.zap(this.zab).unlock();
        throw var2_3;
    }

    protected abstract void zaa();
}

