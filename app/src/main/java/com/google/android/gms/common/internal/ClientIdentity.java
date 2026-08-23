/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable$Creator
 */
package com.google.android.gms.common.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.zaa;

public class ClientIdentity
extends AbstractSafeParcelable {
    public static final Parcelable.Creator<ClientIdentity> CREATOR = new zaa();
    public final String packageName;
    public final int uid;

    public ClientIdentity(int n, String string2) {
        this.uid = n;
        this.packageName = string2;
    }

    public final boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof ClientIdentity)) {
            return false;
        }
        object = (ClientIdentity)object;
        return ((ClientIdentity)object).uid == this.uid && Objects.equal(((ClientIdentity)object).packageName, this.packageName);
    }

    public final int hashCode() {
        return this.uid;
    }

    public final String toString() {
        int n = this.uid;
        String string2 = this.packageName;
        StringBuilder stringBuilder = new StringBuilder(String.valueOf(string2).length() + 12);
        stringBuilder.append(n);
        stringBuilder.append(":");
        stringBuilder.append(string2);
        return stringBuilder.toString();
    }

    public final void writeToParcel(Parcel parcel, int n) {
        n = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.uid);
        SafeParcelWriter.writeString(parcel, 2, this.packageName, false);
        SafeParcelWriter.finishObjectHeader(parcel, n);
    }
}

