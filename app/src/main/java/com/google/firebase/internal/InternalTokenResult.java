/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.internal;

import com.google.android.gms.common.internal.Objects;

public class InternalTokenResult {
    private String zza;

    public InternalTokenResult(String string2) {
        this.zza = string2;
    }

    public boolean equals(Object object) {
        if (!(object instanceof InternalTokenResult)) {
            return false;
        }
        object = (InternalTokenResult)object;
        return Objects.equal(this.zza, ((InternalTokenResult)object).zza);
    }

    public String getToken() {
        return this.zza;
    }

    public int hashCode() {
        return Objects.hashCode(this.zza);
    }

    public String toString() {
        return Objects.toStringHelper(this).add("token", this.zza).toString();
    }
}

