/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable$Creator
 */
package com.google.android.gms.common.server.response;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.server.response.FastJsonResponse;
import com.google.android.gms.common.server.response.zak;

public final class zam
extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zam> CREATOR = new zak();
    final int zaa;
    final String zab;
    final FastJsonResponse.Field<?, ?> zac;

    zam(int n, String string2, FastJsonResponse.Field<?, ?> field) {
        this.zaa = n;
        this.zab = string2;
        this.zac = field;
    }

    zam(String string2, FastJsonResponse.Field<?, ?> field) {
        this.zaa = 1;
        this.zab = string2;
        this.zac = field;
    }

    public final void writeToParcel(Parcel parcel, int n) {
        int n2 = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.zaa);
        SafeParcelWriter.writeString(parcel, 2, this.zab, false);
        SafeParcelWriter.writeParcelable(parcel, 3, this.zac, n, false);
        SafeParcelWriter.finishObjectHeader(parcel, n2);
    }
}

