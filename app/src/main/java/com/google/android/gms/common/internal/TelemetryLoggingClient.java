/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.internal;

import com.google.android.gms.common.api.HasApiKey;
import com.google.android.gms.common.internal.TelemetryData;
import com.google.android.gms.common.internal.TelemetryLoggingOptions;
import com.google.android.gms.tasks.Task;

public interface TelemetryLoggingClient
extends HasApiKey<TelemetryLoggingOptions> {
    public Task<Void> log(TelemetryData var1);
}

