/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 */
package com.google.android.gms.common.internal;

import android.os.Bundle;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.zaac;
import com.google.android.gms.common.internal.zaad;

public class TelemetryLoggingOptions
implements Api.ApiOptions.Optional {
    public static final TelemetryLoggingOptions zaa = TelemetryLoggingOptions.builder().build();
    private final String zab;

    /* synthetic */ TelemetryLoggingOptions(String string2, zaad zaad2) {
        this.zab = string2;
    }

    public static Builder builder() {
        return new Builder(null);
    }

    public final boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof TelemetryLoggingOptions)) {
            return false;
        }
        object = (TelemetryLoggingOptions)object;
        return Objects.equal(this.zab, ((TelemetryLoggingOptions)object).zab);
    }

    public final int hashCode() {
        return Objects.hashCode(this.zab);
    }

    public final Bundle zaa() {
        Bundle bundle = new Bundle();
        String string2 = this.zab;
        if (string2 != null) {
            bundle.putString("api", string2);
        }
        return bundle;
    }

    public static class Builder {
        private String zaa;

        private Builder() {
        }

        /* synthetic */ Builder(zaac zaac2) {
        }

        public TelemetryLoggingOptions build() {
            return new TelemetryLoggingOptions(this.zaa, null);
        }

        public Builder setApi(String string2) {
            this.zaa = string2;
            return this;
        }
    }
}

