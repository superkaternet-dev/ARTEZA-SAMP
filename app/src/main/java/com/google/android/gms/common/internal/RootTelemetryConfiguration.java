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
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.zzai;

public class RootTelemetryConfiguration
extends AbstractSafeParcelable {
    public static final Parcelable.Creator<RootTelemetryConfiguration> CREATOR = new zzai();
    private final int zza;
    private final boolean zzb;
    private final boolean zzc;
    private final int zzd;
    private final int zze;

    public RootTelemetryConfiguration(int n, boolean bl, boolean bl2, int n2, int n3) {
        this.zza = n;
        this.zzb = bl;
        this.zzc = bl2;
        this.zzd = n2;
        this.zze = n3;
    }

    public int getBatchPeriodMillis() {
        return this.zzd;
    }

    public int getMaxMethodInvocationsInBatch() {
        return this.zze;
    }

    public boolean getMethodInvocationTelemetryEnabled() {
        return this.zzb;
    }

    public boolean getMethodTimingTelemetryEnabled() {
        return this.zzc;
    }

    public int getVersion() {
        return this.zza;
    }

    public final void writeToParcel(Parcel parcel, int n) {
        n = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.getVersion());
        SafeParcelWriter.writeBoolean(parcel, 2, this.getMethodInvocationTelemetryEnabled());
        SafeParcelWriter.writeBoolean(parcel, 3, this.getMethodTimingTelemetryEnabled());
        SafeParcelWriter.writeInt(parcel, 4, this.getBatchPeriodMillis());
        SafeParcelWriter.writeInt(parcel, 5, this.getMaxMethodInvocationsInBatch());
        SafeParcelWriter.finishObjectHeader(parcel, n);
    }
}

