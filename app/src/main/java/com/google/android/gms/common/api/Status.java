/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.app.PendingIntent
 *  android.content.IntentSender$SendIntentException
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 */
package com.google.android.gms.common.api;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.IntentSender;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.zzb;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

public final class Status
extends AbstractSafeParcelable
implements Result,
ReflectedParcelable {
    public static final Parcelable.Creator<Status> CREATOR;
    public static final Status RESULT_CANCELED;
    public static final Status RESULT_DEAD_CLIENT;
    public static final Status RESULT_INTERNAL_ERROR;
    public static final Status RESULT_INTERRUPTED;
    public static final Status RESULT_SUCCESS;
    public static final Status RESULT_TIMEOUT;
    public static final Status zza;
    final int zzb;
    private final int zzc;
    private final String zzd;
    private final PendingIntent zze;
    private final ConnectionResult zzf;

    static {
        RESULT_SUCCESS = new Status(0);
        RESULT_INTERRUPTED = new Status(14);
        RESULT_INTERNAL_ERROR = new Status(8);
        RESULT_TIMEOUT = new Status(15);
        RESULT_CANCELED = new Status(16);
        zza = new Status(17);
        RESULT_DEAD_CLIENT = new Status(18);
        CREATOR = new zzb();
    }

    public Status(int n) {
        this(n, null);
    }

    Status(int n, int n2, String string2, PendingIntent pendingIntent) {
        this(n, n2, string2, pendingIntent, null);
    }

    Status(int n, int n2, String string2, PendingIntent pendingIntent, ConnectionResult connectionResult) {
        this.zzb = n;
        this.zzc = n2;
        this.zzd = string2;
        this.zze = pendingIntent;
        this.zzf = connectionResult;
    }

    public Status(int n, String string2) {
        this(1, n, string2, null);
    }

    public Status(int n, String string2, PendingIntent pendingIntent) {
        this(1, n, string2, pendingIntent);
    }

    public Status(ConnectionResult connectionResult, String string2) {
        this(connectionResult, string2, 17);
    }

    @Deprecated
    public Status(ConnectionResult connectionResult, String string2, int n) {
        this(1, n, string2, connectionResult.getResolution(), connectionResult);
    }

    public boolean equals(Object object) {
        if (!(object instanceof Status)) {
            return false;
        }
        object = (Status)object;
        return this.zzb == ((Status)object).zzb && this.zzc == ((Status)object).zzc && Objects.equal(this.zzd, ((Status)object).zzd) && Objects.equal(this.zze, ((Status)object).zze) && Objects.equal(this.zzf, ((Status)object).zzf);
    }

    public ConnectionResult getConnectionResult() {
        return this.zzf;
    }

    public PendingIntent getResolution() {
        return this.zze;
    }

    @Override
    public Status getStatus() {
        return this;
    }

    public int getStatusCode() {
        return this.zzc;
    }

    public String getStatusMessage() {
        return this.zzd;
    }

    public boolean hasResolution() {
        return this.zze != null;
    }

    public int hashCode() {
        return Objects.hashCode(this.zzb, this.zzc, this.zzd, this.zze, this.zzf);
    }

    public boolean isCanceled() {
        return this.zzc == 16;
    }

    public boolean isInterrupted() {
        return this.zzc == 14;
    }

    public boolean isSuccess() {
        return this.zzc <= 0;
    }

    public void startResolutionForResult(Activity activity, int n) throws IntentSender.SendIntentException {
        if (!this.hasResolution()) {
            return;
        }
        PendingIntent pendingIntent = this.zze;
        Preconditions.checkNotNull(pendingIntent);
        activity.startIntentSenderForResult(pendingIntent.getIntentSender(), n, null, 0, 0, 0);
    }

    public String toString() {
        Objects.ToStringHelper toStringHelper = Objects.toStringHelper(this);
        toStringHelper.add("statusCode", this.zza());
        toStringHelper.add("resolution", this.zze);
        return toStringHelper.toString();
    }

    public void writeToParcel(Parcel parcel, int n) {
        int n2 = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.getStatusCode());
        SafeParcelWriter.writeString(parcel, 2, this.getStatusMessage(), false);
        SafeParcelWriter.writeParcelable(parcel, 3, (Parcelable)this.zze, n, false);
        SafeParcelWriter.writeParcelable(parcel, 4, this.getConnectionResult(), n, false);
        SafeParcelWriter.writeInt(parcel, 1000, this.zzb);
        SafeParcelWriter.finishObjectHeader(parcel, n2);
    }

    public final String zza() {
        String string2 = this.zzd;
        if (string2 != null) {
            return string2;
        }
        return CommonStatusCodes.getStatusCodeString(this.zzc);
    }
}

