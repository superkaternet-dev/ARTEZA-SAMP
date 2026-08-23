/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Intent
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 */
package com.google.android.gms.signin.internal;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.signin.internal.zab;

public final class zaa
extends AbstractSafeParcelable
implements Result {
    public static final Parcelable.Creator<zaa> CREATOR = new zab();
    final int zaa;
    private int zab;
    private Intent zac;

    public zaa() {
        this(2, 0, null);
    }

    zaa(int n, int n2, Intent intent) {
        this.zaa = n;
        this.zab = n2;
        this.zac = intent;
    }

    @Override
    public final Status getStatus() {
        if (this.zab == 0) {
            return Status.RESULT_SUCCESS;
        }
        return Status.RESULT_CANCELED;
    }

    public final void writeToParcel(Parcel parcel, int n) {
        int n2 = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.zaa);
        SafeParcelWriter.writeInt(parcel, 2, this.zab);
        SafeParcelWriter.writeParcelable(parcel, 3, (Parcelable)this.zac, n, false);
        SafeParcelWriter.finishObjectHeader(parcel, n2);
    }
}

