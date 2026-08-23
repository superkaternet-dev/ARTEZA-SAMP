/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.auth;

import com.google.android.gms.common.internal.Preconditions;
import com.google.firebase.FirebaseException;

public class FirebaseAuthException
extends FirebaseException {
    private final String zza;

    public FirebaseAuthException(String string2, String string3) {
        super(string3);
        this.zza = Preconditions.checkNotEmpty(string2);
    }

    public String getErrorCode() {
        return this.zza;
    }
}

