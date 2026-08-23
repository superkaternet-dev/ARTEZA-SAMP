/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.IBinder
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 */
package com.google.android.gms.common.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.zzh;
import com.google.android.gms.common.internal.zzi;

public final class BinderWrapper
implements Parcelable {
    public static final Parcelable.Creator<BinderWrapper> CREATOR = new zzh();
    private IBinder zza;

    public BinderWrapper(IBinder iBinder) {
        this.zza = iBinder;
    }

    /* synthetic */ BinderWrapper(Parcel parcel, zzi zzi2) {
        this.zza = parcel.readStrongBinder();
    }

    public final int describeContents() {
        return 0;
    }

    public final void writeToParcel(Parcel parcel, int n) {
        parcel.writeStrongBinder(this.zza);
    }
}

