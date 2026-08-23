/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.RemoteException
 */
package com.google.android.gms.common.internal;

import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import com.google.android.gms.common.internal.zzj;

public interface IGmsCallbacks
extends IInterface {
    public void onPostInitComplete(int var1, IBinder var2, Bundle var3) throws RemoteException;

    public void zzb(int var1, Bundle var2) throws RemoteException;

    public void zzc(int var1, IBinder var2, zzj var3) throws RemoteException;
}

