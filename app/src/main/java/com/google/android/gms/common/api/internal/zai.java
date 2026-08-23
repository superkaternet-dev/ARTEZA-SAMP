/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.DeadObjectException
 *  android.os.RemoteException
 */
package com.google.android.gms.common.api.internal;

import android.os.DeadObjectException;
import android.os.RemoteException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.zaad;
import com.google.android.gms.common.api.internal.zabq;

public abstract class zai {
    public final int zac;

    public zai(int n) {
        this.zac = n;
    }

    static /* bridge */ /* synthetic */ Status zah(RemoteException remoteException) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(((Object)((Object)remoteException)).getClass().getSimpleName());
        stringBuilder.append(": ");
        stringBuilder.append(remoteException.getLocalizedMessage());
        return new Status(19, stringBuilder.toString());
    }

    public abstract void zad(Status var1);

    public abstract void zae(Exception var1);

    public abstract void zaf(zabq<?> var1) throws DeadObjectException;

    public abstract void zag(zaad var1, boolean var2);
}

