/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.IBinder
 *  android.os.Parcel
 *  android.os.Parcelable$Creator
 *  android.os.RemoteException
 *  android.util.Log
 *  javax.annotation.Nullable
 */
package com.google.android.gms.common;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.zzy;
import com.google.android.gms.common.zzi;
import com.google.android.gms.common.zzj;
import com.google.android.gms.common.zzt;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.dynamic.ObjectWrapper;
import javax.annotation.Nullable;

public final class zzs
extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzs> CREATOR = new zzt();
    private final String zza;
    @Nullable
    private final zzi zzb;
    private final boolean zzc;
    private final boolean zzd;

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    zzs(String object, @Nullable IBinder iBinder, boolean bl, boolean bl2) {
        block6: {
            this.zza = object;
            Object var5_6 = null;
            if (iBinder == null) {
                object = var5_6;
            } else {
                try {
                    object = zzy.zzg(iBinder).zzd();
                    object = object == null ? null : (Object)((byte[])ObjectWrapper.unwrap((IObjectWrapper)object));
                }
                catch (RemoteException remoteException) {
                    Log.e((String)"GoogleCertificatesQuery", (String)"Could not unwrap certificate", (Throwable)remoteException);
                    object = var5_6;
                    break block6;
                }
                if (object != null) {
                    object = new zzj((byte[])object);
                } else {
                    Log.e((String)"GoogleCertificatesQuery", (String)"Could not unwrap certificate");
                    object = var5_6;
                }
            }
        }
        this.zzb = object;
        this.zzc = bl;
        this.zzd = bl2;
    }

    zzs(String string2, @Nullable zzi zzi2, boolean bl, boolean bl2) {
        this.zza = string2;
        this.zzb = zzi2;
        this.zzc = bl;
        this.zzd = bl2;
    }

    public final void writeToParcel(Parcel parcel, int n) {
        zzi zzi2;
        n = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 1, this.zza, false);
        zzi zzi3 = zzi2 = this.zzb;
        if (zzi2 == null) {
            Log.w((String)"GoogleCertificatesQuery", (String)"certificate binder is null");
            zzi3 = null;
        }
        SafeParcelWriter.writeIBinder(parcel, 2, (IBinder)zzi3, false);
        SafeParcelWriter.writeBoolean(parcel, 3, this.zzc);
        SafeParcelWriter.writeBoolean(parcel, 4, this.zzd);
        SafeParcelWriter.finishObjectHeader(parcel, n);
    }
}

