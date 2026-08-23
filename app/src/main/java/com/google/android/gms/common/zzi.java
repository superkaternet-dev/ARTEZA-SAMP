/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.RemoteException
 *  android.util.Log
 */
package com.google.android.gms.common;

import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.zzy;
import com.google.android.gms.common.internal.zzz;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.dynamic.ObjectWrapper;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

abstract class zzi
extends zzy {
    private final int zza;

    protected zzi(byte[] byArray) {
        boolean bl = byArray.length == 25;
        Preconditions.checkArgument(bl);
        this.zza = Arrays.hashCode(byArray);
    }

    protected static byte[] zze(String object) {
        try {
            object = ((String)object).getBytes("ISO-8859-1");
            return object;
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            throw new AssertionError((Object)unsupportedEncodingException);
        }
    }

    public final boolean equals(Object object) {
        if (object != null && object instanceof zzz) {
            block6: {
                block5: {
                    try {
                        object = (zzz)object;
                        if (object.zzc() == this.zza) break block5;
                        return false;
                    }
                    catch (RemoteException remoteException) {
                        Log.e((String)"GoogleCertificates", (String)"Failed to get Google certificates from remote", (Throwable)remoteException);
                        return false;
                    }
                }
                object = object.zzd();
                if (object != null) break block6;
                return false;
            }
            object = (byte[])ObjectWrapper.unwrap((IObjectWrapper)object);
            boolean bl = Arrays.equals(this.zzf(), (byte[])object);
            return bl;
        }
        return false;
    }

    public final int hashCode() {
        return this.zza;
    }

    @Override
    public final int zzc() {
        return this.zza;
    }

    @Override
    public final IObjectWrapper zzd() {
        return ObjectWrapper.wrap(this.zzf());
    }

    abstract byte[] zzf();
}

