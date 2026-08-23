/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable$Creator
 */
package com.google.android.gms.common;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.zzc;

public class Feature
extends AbstractSafeParcelable {
    public static final Parcelable.Creator<Feature> CREATOR = new zzc();
    private final String zza;
    @Deprecated
    private final int zzb;
    private final long zzc;

    public Feature(String string2, int n, long l) {
        this.zza = string2;
        this.zzb = n;
        this.zzc = l;
    }

    public Feature(String string2, long l) {
        this.zza = string2;
        this.zzc = l;
        this.zzb = -1;
    }

    public final boolean equals(Object object) {
        if (object instanceof Feature) {
            object = (Feature)object;
            if ((this.getName() != null && this.getName().equals(((Feature)object).getName()) || this.getName() == null && ((Feature)object).getName() == null) && this.getVersion() == ((Feature)object).getVersion()) {
                return true;
            }
        }
        return false;
    }

    public String getName() {
        return this.zza;
    }

    public long getVersion() {
        long l;
        long l2 = l = this.zzc;
        if (l == -1L) {
            l2 = this.zzb;
        }
        return l2;
    }

    public final int hashCode() {
        return Objects.hashCode(this.getName(), this.getVersion());
    }

    public final String toString() {
        Objects.ToStringHelper toStringHelper = Objects.toStringHelper(this);
        toStringHelper.add("name", this.getName());
        toStringHelper.add("version", this.getVersion());
        return toStringHelper.toString();
    }

    public final void writeToParcel(Parcel parcel, int n) {
        n = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 1, this.getName(), false);
        SafeParcelWriter.writeInt(parcel, 2, this.zzb);
        SafeParcelWriter.writeLong(parcel, 3, this.getVersion());
        SafeParcelWriter.finishObjectHeader(parcel, n);
    }
}

