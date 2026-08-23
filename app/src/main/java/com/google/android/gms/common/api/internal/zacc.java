/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 */
package com.google.android.gms.common.api.internal;

import android.app.Activity;
import android.content.Context;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.LifecycleFragment;
import com.google.android.gms.common.api.internal.zap;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import java.util.concurrent.CancellationException;

public final class zacc
extends zap {
    private TaskCompletionSource<Void> zad = new TaskCompletionSource();

    private zacc(LifecycleFragment lifecycleFragment) {
        super(lifecycleFragment, GoogleApiAvailability.getInstance());
        this.mLifecycleFragment.addCallback("GmsAvailabilityHelper", this);
    }

    public static zacc zaa(Activity object) {
        zacc zacc2 = (object = zacc.getFragment((Activity)object)).getCallbackOrNull("GmsAvailabilityHelper", zacc.class);
        if (zacc2 != null) {
            if (zacc2.zad.getTask().isComplete()) {
                zacc2.zad = new TaskCompletionSource();
            }
            return zacc2;
        }
        return new zacc((LifecycleFragment)object);
    }

    @Override
    public final void onDestroy() {
        super.onDestroy();
        this.zad.trySetException(new CancellationException("Host activity was destroyed before Google Play services could be made available."));
    }

    @Override
    protected final void zab(ConnectionResult connectionResult, int n) {
        String string2;
        String string3 = string2 = connectionResult.getErrorMessage();
        if (string2 == null) {
            string3 = "Error connecting to Google Play services";
        }
        this.zad.setException(new ApiException(new Status(connectionResult, string3, connectionResult.getErrorCode())));
    }

    @Override
    protected final void zac() {
        Activity activity = this.mLifecycleFragment.getLifecycleActivity();
        if (activity == null) {
            this.zad.trySetException(new ApiException(new Status(8)));
            return;
        }
        int n = this.zac.isGooglePlayServicesAvailable((Context)activity);
        if (n == 0) {
            this.zad.trySetResult(null);
            return;
        }
        if (!this.zad.getTask().isComplete()) {
            this.zah(new ConnectionResult(n, null), 0);
        }
    }

    public final Task<Void> zad() {
        return this.zad.getTask();
    }
}

