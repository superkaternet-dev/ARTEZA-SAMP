/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable$Creator
 */
package com.google.android.gms.signin.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.zav;
import com.google.android.gms.signin.internal.zal;

public final class zak
extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zak> CREATOR = new zal();
    final int zaa;
    private final ConnectionResult zab;
    private final zav zac;

    zak(int n, ConnectionResult connectionResult, zav zav2) {
        this.zaa = n;
        this.zab = connectionResult;
        this.zac = zav2;
    }

    public final void writeToParcel(Parcel parcel, int n) {
        int n2 = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.zaa);
        SafeParcelWriter.writeParcelable(parcel, 2, this.zab, n, false);
        SafeParcelWriter.writeParcelable(parcel, 3, this.zac, n, false);
        SafeParcelWriter.finishObjectHeader(parcel, n2);
    }

    public final ConnectionResult zaa() {
        return this.zab;
    }

    public final zav zab() {
        return this.zac;
    }
}

