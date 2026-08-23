/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 */
package com.google.android.gms.common.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.GetServiceRequest;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

public final class zzm
implements Parcelable.Creator<GetServiceRequest> {
    static void zza(GetServiceRequest getServiceRequest, Parcel parcel, int n) {
        int n2 = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, getServiceRequest.zza);
        SafeParcelWriter.writeInt(parcel, 2, getServiceRequest.zzb);
        SafeParcelWriter.writeInt(parcel, 3, getServiceRequest.zzc);
        SafeParcelWriter.writeString(parcel, 4, getServiceRequest.zzd, false);
        SafeParcelWriter.writeIBinder(parcel, 5, getServiceRequest.zze, false);
        SafeParcelWriter.writeTypedArray((Parcel)parcel, (int)6, (Parcelable[])getServiceRequest.zzf, (int)n, (boolean)false);
        SafeParcelWriter.writeBundle(parcel, 7, getServiceRequest.zzg, false);
        SafeParcelWriter.writeParcelable(parcel, 8, (Parcelable)getServiceRequest.zzh, n, false);
        SafeParcelWriter.writeTypedArray((Parcel)parcel, (int)10, (Parcelable[])getServiceRequest.zzi, (int)n, (boolean)false);
        SafeParcelWriter.writeTypedArray((Parcel)parcel, (int)11, (Parcelable[])getServiceRequest.zzj, (int)n, (boolean)false);
        SafeParcelWriter.writeBoolean(parcel, 12, getServiceRequest.zzk);
        SafeParcelWriter.writeInt(parcel, 13, getServiceRequest.zzl);
        SafeParcelWriter.writeBoolean(parcel, 14, getServiceRequest.zzm);
        SafeParcelWriter.writeString(parcel, 15, getServiceRequest.zza(), false);
        SafeParcelWriter.finishObjectHeader(parcel, n2);
    }

    public final /* synthetic */ Object[] newArray(int n) {
        return new GetServiceRequest[n];
    }
}

