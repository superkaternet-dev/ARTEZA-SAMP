/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.RemoteException
 */
package com.google.android.gms.signin.internal;

import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.internal.base.zab;
import com.google.android.gms.internal.base.zac;
import com.google.android.gms.signin.internal.zaa;
import com.google.android.gms.signin.internal.zae;
import com.google.android.gms.signin.internal.zag;
import com.google.android.gms.signin.internal.zak;

public abstract class zad
extends zab
implements zae {
    public zad() {
        super("com.google.android.gms.signin.internal.ISignInCallbacks");
    }

    @Override
    protected final boolean zaa(int n, Parcel object, Parcel parcel, int n2) throws RemoteException {
        switch (n) {
            default: {
                return false;
            }
            case 9: {
                object = zac.zaa(object, zag.CREATOR);
                break;
            }
            case 8: {
                this.zab(zac.zaa(object, zak.CREATOR));
                break;
            }
            case 7: {
                Status status = zac.zaa(object, Status.CREATOR);
                object = zac.zaa(object, GoogleSignInAccount.CREATOR);
                break;
            }
            case 6: {
                object = zac.zaa(object, Status.CREATOR);
                break;
            }
            case 4: {
                object = zac.zaa(object, Status.CREATOR);
                break;
            }
            case 3: {
                ConnectionResult connectionResult = zac.zaa(object, ConnectionResult.CREATOR);
                object = zac.zaa(object, zaa.CREATOR);
            }
        }
        parcel.writeNoException();
        return true;
    }
}

