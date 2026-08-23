/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Bundle
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.Looper
 */
package com.google.android.gms.common.internal.service;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import com.google.android.gms.common.Feature;
import com.google.android.gms.common.api.internal.ConnectionCallbacks;
import com.google.android.gms.common.api.internal.OnConnectionFailedListener;
import com.google.android.gms.common.internal.ClientSettings;
import com.google.android.gms.common.internal.GmsClient;
import com.google.android.gms.common.internal.TelemetryLoggingOptions;
import com.google.android.gms.common.internal.service.zai;
import com.google.android.gms.internal.base.zad;

public final class zap
extends GmsClient<zai> {
    private final TelemetryLoggingOptions zaa;

    public zap(Context context, Looper looper, ClientSettings clientSettings, TelemetryLoggingOptions telemetryLoggingOptions, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        super(context, looper, 270, clientSettings, connectionCallbacks, onConnectionFailedListener);
        this.zaa = telemetryLoggingOptions;
    }

    @Override
    protected final /* synthetic */ IInterface createServiceInterface(IBinder object) {
        IInterface iInterface;
        object = object == null ? null : ((iInterface = object.queryLocalInterface("com.google.android.gms.common.internal.service.IClientTelemetryService")) instanceof zai ? (zai)iInterface : new zai((IBinder)object));
        return object;
    }

    @Override
    public final Feature[] getApiFeatures() {
        return zad.zab;
    }

    @Override
    protected final Bundle getGetServiceRequestExtraArgs() {
        return this.zaa.zaa();
    }

    @Override
    public final int getMinApkVersion() {
        return 203400000;
    }

    @Override
    protected final String getServiceDescriptor() {
        return "com.google.android.gms.common.internal.service.IClientTelemetryService";
    }

    @Override
    protected final String getStartServiceAction() {
        return "com.google.android.gms.common.telemetry.service.START";
    }

    @Override
    protected final boolean getUseDynamicLookup() {
        return true;
    }
}

