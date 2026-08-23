/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.signin;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.signin.zaf;

public final class SignInOptions
implements Api.ApiOptions.Optional {
    public static final SignInOptions zaa = new SignInOptions(false, false, null, false, null, null, false, null, null, null);
    private final boolean zab;
    private final boolean zac;
    private final String zad;
    private final boolean zae;
    private final String zaf;
    private final String zag;
    private final boolean zah;
    private final Long zai = null;
    private final Long zaj = null;

    /* synthetic */ SignInOptions(boolean bl, boolean bl2, String string2, boolean bl3, String string3, String string4, boolean bl4, Long l, Long l2, zaf zaf2) {
        this.zab = false;
        this.zac = false;
        this.zad = null;
        this.zae = false;
        this.zah = false;
        this.zaf = null;
        this.zag = null;
    }

    public final boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof SignInOptions)) {
            return false;
        }
        object = (SignInOptions)object;
        boolean bl = ((SignInOptions)object).zab;
        bl = ((SignInOptions)object).zac;
        Object object2 = ((SignInOptions)object).zad;
        if (Objects.equal(null, null)) {
            bl = ((SignInOptions)object).zae;
            bl = ((SignInOptions)object).zah;
            object2 = ((SignInOptions)object).zaf;
            if (Objects.equal(null, null)) {
                object2 = ((SignInOptions)object).zag;
                if (Objects.equal(null, null)) {
                    object2 = ((SignInOptions)object).zai;
                    if (Objects.equal(null, null)) {
                        object = ((SignInOptions)object).zaj;
                        if (Objects.equal(null, null)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public final int hashCode() {
        Boolean bl = false;
        return Objects.hashCode(bl, bl, null, bl, bl, null, null, null, null);
    }
}

