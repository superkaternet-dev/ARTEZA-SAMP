/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 */
package com.google.android.gms.common.internal.service;

import android.content.Context;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.internal.TaskApiCall;
import com.google.android.gms.common.internal.TelemetryData;
import com.google.android.gms.common.internal.TelemetryLoggingClient;
import com.google.android.gms.common.internal.TelemetryLoggingOptions;
import com.google.android.gms.common.internal.service.zam;
import com.google.android.gms.common.internal.service.zan;
import com.google.android.gms.common.internal.service.zap;
import com.google.android.gms.internal.base.zad;
import com.google.android.gms.tasks.Task;

public final class zao
extends GoogleApi<TelemetryLoggingOptions>
implements TelemetryLoggingClient {
    public static final int zab = 0;
    private static final Api.ClientKey<zap> zac;
    private static final Api.AbstractClientBuilder<zap, TelemetryLoggingOptions> zad;
    private static final Api<TelemetryLoggingOptions> zae;

    static {
        zan zan2;
        Api.ClientKey clientKey = new Api.ClientKey();
        zac = clientKey;
        zad = zan2 = new zan();
        zae = new Api<TelemetryLoggingOptions>("ClientTelemetry.API", zan2, clientKey);
    }

    public zao(Context context, TelemetryLoggingOptions telemetryLoggingOptions) {
        super(context, zae, telemetryLoggingOptions, GoogleApi.Settings.DEFAULT_SETTINGS);
    }

    @Override
    public final Task<Void> log(TelemetryData telemetryData) {
        TaskApiCall.Builder builder = TaskApiCall.builder();
        builder.setFeatures(com.google.android.gms.internal.base.zad.zaa);
        builder.setAutoResolveMissingFeatures(false);
        builder.run(new zam(telemetryData));
        return this.doBestEffortWrite(builder.build());
    }
}

