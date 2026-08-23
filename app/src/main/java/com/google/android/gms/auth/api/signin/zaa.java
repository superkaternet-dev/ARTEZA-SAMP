/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcelable$Creator
 */
package com.google.android.gms.auth.api.signin;

import android.os.Parcelable;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.Scope;
import java.util.Comparator;

public final class zaa
implements Comparator {
    public static final zaa zaa = new zaa();

    private /* synthetic */ zaa() {
    }

    public final int compare(Object object, Object object2) {
        object = (Scope)object;
        object2 = (Scope)object2;
        Parcelable.Creator<GoogleSignInAccount> creator = GoogleSignInAccount.CREATOR;
        return ((Scope)object).getScopeUri().compareTo(((Scope)object2).getScopeUri());
    }
}

