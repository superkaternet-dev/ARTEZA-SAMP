/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.internal.Objects;

public final class ApiKey<O extends Api.ApiOptions> {
    private final int zaa;
    private final Api<O> zab;
    private final O zac;
    private final String zad;

    private ApiKey(Api<O> api, O o, String string2) {
        this.zab = api;
        this.zac = o;
        this.zad = string2;
        this.zaa = Objects.hashCode(api, o, string2);
    }

    public static <O extends Api.ApiOptions> ApiKey<O> zaa(Api<O> api, O o, String string2) {
        return new ApiKey<O>(api, o, string2);
    }

    public final boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object == this) {
            return true;
        }
        if (!(object instanceof ApiKey)) {
            return false;
        }
        object = (ApiKey)object;
        return Objects.equal(this.zab, ((ApiKey)object).zab) && Objects.equal(this.zac, ((ApiKey)object).zac) && Objects.equal(this.zad, ((ApiKey)object).zad);
    }

    public final int hashCode() {
        return this.zaa;
    }

    public final String zab() {
        return this.zab.zad();
    }
}

