/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable$Creator
 *  android.text.TextUtils
 *  javax.annotation.Nullable
 */
package com.google.android.gms.common.stats;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.stats.StatsEvent;
import com.google.android.gms.common.stats.zza;
import java.util.List;
import javax.annotation.Nullable;

@Deprecated
public final class WakeLockEvent
extends StatsEvent {
    public static final Parcelable.Creator<WakeLockEvent> CREATOR = new zza();
    final int zza;
    private final long zzb;
    private int zzc;
    private final String zzd;
    private final String zze;
    private final String zzf;
    private final int zzg;
    @Nullable
    private final List<String> zzh;
    private final String zzi;
    private final long zzj;
    private int zzk;
    private final String zzl;
    private final float zzm;
    private final long zzn;
    private final boolean zzo;
    private long zzp;

    WakeLockEvent(int n, long l, int n2, String string2, int n3, @Nullable List<String> list, String string3, long l2, int n4, String string4, String string5, float f, long l3, String string6, boolean bl) {
        this.zza = n;
        this.zzb = l;
        this.zzc = n2;
        this.zzd = string2;
        this.zze = string4;
        this.zzf = string6;
        this.zzg = n3;
        this.zzp = -1L;
        this.zzh = list;
        this.zzi = string3;
        this.zzj = l2;
        this.zzk = n4;
        this.zzl = string5;
        this.zzm = f;
        this.zzn = l3;
        this.zzo = bl;
    }

    public final void writeToParcel(Parcel parcel, int n) {
        n = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.zza);
        SafeParcelWriter.writeLong(parcel, 2, this.zzb);
        SafeParcelWriter.writeString(parcel, 4, this.zzd, false);
        SafeParcelWriter.writeInt(parcel, 5, this.zzg);
        SafeParcelWriter.writeStringList(parcel, 6, this.zzh, false);
        SafeParcelWriter.writeLong(parcel, 8, this.zzj);
        SafeParcelWriter.writeString(parcel, 10, this.zze, false);
        SafeParcelWriter.writeInt(parcel, 11, this.zzc);
        SafeParcelWriter.writeString(parcel, 12, this.zzi, false);
        SafeParcelWriter.writeString(parcel, 13, this.zzl, false);
        SafeParcelWriter.writeInt(parcel, 14, this.zzk);
        SafeParcelWriter.writeFloat(parcel, 15, this.zzm);
        SafeParcelWriter.writeLong(parcel, 16, this.zzn);
        SafeParcelWriter.writeString(parcel, 17, this.zzf, false);
        SafeParcelWriter.writeBoolean(parcel, 18, this.zzo);
        SafeParcelWriter.finishObjectHeader(parcel, n);
    }

    @Override
    public final int zza() {
        return this.zzc;
    }

    @Override
    public final long zzb() {
        return this.zzp;
    }

    @Override
    public final long zzc() {
        return this.zzb;
    }

    @Override
    public final String zzd() {
        String string2;
        Object object = this.zzh;
        String string3 = this.zzd;
        int n = this.zzg;
        String string4 = "";
        object = object == null ? "" : TextUtils.join((CharSequence)",", (Iterable)object);
        int n2 = this.zzk;
        String string5 = string2 = this.zze;
        if (string2 == null) {
            string5 = "";
        }
        CharSequence charSequence = this.zzl;
        string2 = charSequence;
        if (charSequence == null) {
            string2 = "";
        }
        float f = this.zzm;
        charSequence = this.zzf;
        if (charSequence != null) {
            string4 = charSequence;
        }
        boolean bl = this.zzo;
        charSequence = new StringBuilder(String.valueOf(string3).length() + 51 + String.valueOf(object).length() + string5.length() + string2.length() + string4.length());
        ((StringBuilder)charSequence).append("\t");
        ((StringBuilder)charSequence).append(string3);
        ((StringBuilder)charSequence).append("\t");
        ((StringBuilder)charSequence).append(n);
        ((StringBuilder)charSequence).append("\t");
        ((StringBuilder)charSequence).append((String)object);
        ((StringBuilder)charSequence).append("\t");
        ((StringBuilder)charSequence).append(n2);
        ((StringBuilder)charSequence).append("\t");
        ((StringBuilder)charSequence).append(string5);
        ((StringBuilder)charSequence).append("\t");
        ((StringBuilder)charSequence).append(string2);
        ((StringBuilder)charSequence).append("\t");
        ((StringBuilder)charSequence).append(f);
        ((StringBuilder)charSequence).append("\t");
        ((StringBuilder)charSequence).append(string4);
        ((StringBuilder)charSequence).append("\t");
        ((StringBuilder)charSequence).append(bl);
        return ((StringBuilder)charSequence).toString();
    }
}

