/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.accounts.Account
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 */
package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.zau;

public final class zat
extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zat> CREATOR = new zau();
    final int zaa;
    private final Account zab;
    private final int zac;
    private final GoogleSignInAccount zad;

    zat(int n, Account account, int n2, GoogleSignInAccount googleSignInAccount) {
        this.zaa = n;
        this.zab = account;
        this.zac = n2;
        this.zad = googleSignInAccount;
    }

    public zat(Account account, int n, GoogleSignInAccount googleSignInAccount) {
        this(2, account, n, googleSignInAccount);
    }

    public final void writeToParcel(Parcel parcel, int n) {
        int n2 = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.zaa);
        SafeParcelWriter.writeParcelable(parcel, 2, (Parcelable)this.zab, n, false);
        SafeParcelWriter.writeInt(parcel, 3, this.zac);
        SafeParcelWriter.writeParcelable(parcel, 4, this.zad, n, false);
        SafeParcelWriter.finishObjectHeader(parcel, n2);
    }
}

