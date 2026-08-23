/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.accounts.Account
 *  android.os.Binder
 *  android.os.RemoteException
 *  android.util.Log
 */
package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.os.Binder;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.internal.IAccountAccessor;

public class AccountAccessor
extends IAccountAccessor.Stub {
    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static Account getAccountBinderSafe(IAccountAccessor iAccountAccessor) {
        Throwable throwable2;
        long l;
        block5: {
            Object var4_3 = null;
            Object var3_4 = null;
            if (iAccountAccessor == null) {
                return var4_3;
            }
            l = Binder.clearCallingIdentity();
            iAccountAccessor = iAccountAccessor.zzb();
            {
                catch (Throwable throwable2) {
                    break block5;
                }
                catch (RemoteException remoteException) {}
                {
                    Log.w((String)"AccountAccessor", (String)"Remote account accessor probably died");
                    iAccountAccessor = var3_4;
                }
            }
            Binder.restoreCallingIdentity((long)l);
            return iAccountAccessor;
        }
        Binder.restoreCallingIdentity((long)l);
        throw throwable2;
    }

    public final boolean equals(Object object) {
        throw null;
    }

    @Override
    public final Account zzb() {
        throw null;
    }
}

