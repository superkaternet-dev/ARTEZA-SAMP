/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.api;

import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.Preconditions;

public class BooleanResult
implements Result {
    private final Status zaa;
    private final boolean zab;

    public BooleanResult(Status status, boolean bl) {
        this.zaa = Preconditions.checkNotNull(status, "Status must not be null");
        this.zab = bl;
    }

    public final boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object == this) {
            return true;
        }
        if (!(object instanceof BooleanResult)) {
            return false;
        }
        object = (BooleanResult)object;
        return this.zaa.equals(((BooleanResult)object).zaa) && this.zab == ((BooleanResult)object).zab;
    }

    @Override
    public Status getStatus() {
        return this.zaa;
    }

    public boolean getValue() {
        return this.zab;
    }

    public final int hashCode() {
        return (this.zaa.hashCode() + 527) * 31 + this.zab;
    }
}

