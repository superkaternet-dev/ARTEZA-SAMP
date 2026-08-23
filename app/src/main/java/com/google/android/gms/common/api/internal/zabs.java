/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.Feature;
import com.google.android.gms.common.api.internal.ApiKey;
import com.google.android.gms.common.api.internal.zabr;
import com.google.android.gms.common.internal.Objects;

final class zabs {
    private final ApiKey<?> zaa;
    private final Feature zab;

    /* synthetic */ zabs(ApiKey apiKey, Feature feature, zabr zabr2) {
        this.zaa = apiKey;
        this.zab = feature;
    }

    static /* bridge */ /* synthetic */ Feature zaa(zabs zabs2) {
        return zabs2.zab;
    }

    static /* bridge */ /* synthetic */ ApiKey zab(zabs zabs2) {
        return zabs2.zaa;
    }

    public final boolean equals(Object object) {
        if (object != null && object instanceof zabs) {
            object = (zabs)object;
            if (Objects.equal(this.zaa, ((zabs)object).zaa) && Objects.equal(this.zab, ((zabs)object).zab)) {
                return true;
            }
        }
        return false;
    }

    public final int hashCode() {
        return Objects.hashCode(this.zaa, this.zab);
    }

    public final String toString() {
        return Objects.toStringHelper(this).add("key", this.zaa).add("feature", this.zab).toString();
    }
}

