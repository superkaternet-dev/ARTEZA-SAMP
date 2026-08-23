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
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.zay;

public final class zax
extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zax> CREATOR = new zay();
    final int zaa;
    private final int zab;
    private final int zac;
    @Deprecated
    private final Scope[] zad;

    zax(int n, int n2, int n3, Scope[] scopeArray) {
        this.zaa = n;
        this.zab = n2;
        this.zac = n3;
        this.zad = scopeArray;
    }

    public final void writeToParcel(Parcel parcel, int n) {
        int n2 = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.zaa);
        SafeParcelWriter.writeInt(parcel, 2, this.zab);
        SafeParcelWriter.writeInt(parcel, 3, this.zac);
        SafeParcelWriter.writeTypedArray((Parcel)parcel, (int)4, (Parcelable[])this.zad, (int)n, (boolean)false);
        SafeParcelWriter.finishObjectHeader(parcel, n2);
    }
}

