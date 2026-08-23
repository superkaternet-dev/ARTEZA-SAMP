/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.signin;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.signin.SignInOptions;
import com.google.android.gms.signin.internal.SignInClientImpl;
import com.google.android.gms.signin.zaa;
import com.google.android.gms.signin.zab;
import com.google.android.gms.signin.zac;

public final class zad {
    public static final Api.ClientKey<SignInClientImpl> zaa;
    public static final Api.ClientKey<SignInClientImpl> zab;
    public static final Api.AbstractClientBuilder<SignInClientImpl, SignInOptions> zac;
    static final Api.AbstractClientBuilder<SignInClientImpl, zac> zad;
    public static final Scope zae;
    public static final Scope zaf;
    public static final Api<SignInOptions> zag;
    public static final Api<zac> zah;

    static {
        zab zab2;
        zaa zaa2;
        Api.ClientKey clientKey = new Api.ClientKey();
        zaa = clientKey;
        Api.ClientKey clientKey2 = new Api.ClientKey();
        zab = clientKey2;
        zac = zaa2 = new zaa();
        zad = zab2 = new zab();
        zae = new Scope("profile");
        zaf = new Scope("email");
        zag = new Api<SignInOptions>("SignIn.API", zaa2, clientKey);
        zah = new Api<zac>("SignIn.INTERNAL_API", zab2, clientKey2);
    }
}

