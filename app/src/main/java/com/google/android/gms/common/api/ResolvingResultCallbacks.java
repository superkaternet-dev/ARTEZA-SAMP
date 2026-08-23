/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.IntentSender$SendIntentException
 *  android.util.Log
 */
package com.google.android.gms.common.api;

import android.app.Activity;
import android.content.IntentSender;
import android.util.Log;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallbacks;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.Preconditions;

public abstract class ResolvingResultCallbacks<R extends Result>
extends ResultCallbacks<R> {
    private final Activity zza;
    private final int zzb;

    protected ResolvingResultCallbacks(Activity activity, int n) {
        Preconditions.checkNotNull(activity, "Activity must not be null");
        this.zza = activity;
        this.zzb = n;
    }

    @Override
    public final void onFailure(Status status) {
        if (status.hasResolution()) {
            try {
                status.startResolutionForResult(this.zza, this.zzb);
                return;
            }
            catch (IntentSender.SendIntentException sendIntentException) {
                Log.e((String)"ResolvingResultCallback", (String)"Failed to start resolution", (Throwable)sendIntentException);
                this.onUnresolvableFailure(new Status(8));
                return;
            }
        }
        this.onUnresolvableFailure(status);
    }

    @Override
    public abstract void onSuccess(R var1);

    public abstract void onUnresolvableFailure(Status var1);
}

