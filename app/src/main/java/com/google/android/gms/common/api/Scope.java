/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable$Creator
 */
package com.google.android.gms.common.api;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.api.zza;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

public final class Scope
extends AbstractSafeParcelable
implements ReflectedParcelable {
    public static final Parcelable.Creator<Scope> CREATOR = new zza();
    final int zza;
    private final String zzb;

    Scope(int n, String string2) {
        Preconditions.checkNotEmpty(string2, "scopeUri must not be null or empty");
        this.zza = n;
        this.zzb = string2;
    }

    public Scope(String string2) {
        this(1, string2);
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Scope)) {
            return false;
        }
        return this.zzb.equals(((Scope)object).zzb);
    }

    public String getScopeUri() {
        return this.zzb;
    }

    public int hashCode() {
        return this.zzb.hashCode();
    }

    public String toString() {
        return this.zzb;
    }

    public void writeToParcel(Parcel parcel, int n) {
        n = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.zza);
        SafeParcelWriter.writeString(parcel, 2, this.getScopeUri(), false);
        SafeParcelWriter.finishObjectHeader(parcel, n);
    }
}

