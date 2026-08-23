/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 */
package com.google.android.gms.common.api.internal;

import android.app.Activity;
import androidx.collection.ArraySet;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.internal.ApiKey;
import com.google.android.gms.common.api.internal.GoogleApiManager;
import com.google.android.gms.common.api.internal.LifecycleFragment;
import com.google.android.gms.common.api.internal.zap;
import com.google.android.gms.common.internal.Preconditions;

public final class zaae
extends zap {
    private final ArraySet<ApiKey<?>> zad = new ArraySet();
    private final GoogleApiManager zae;

    zaae(LifecycleFragment lifecycleFragment, GoogleApiManager googleApiManager, GoogleApiAvailability googleApiAvailability) {
        super(lifecycleFragment, googleApiAvailability);
        this.zae = googleApiManager;
        this.mLifecycleFragment.addCallback("ConnectionlessLifecycleHelper", this);
    }

    public static void zad(Activity object, GoogleApiManager googleApiManager, ApiKey<?> apiKey) {
        LifecycleFragment lifecycleFragment = zaae.getFragment(object);
        if ((object = lifecycleFragment.getCallbackOrNull("ConnectionlessLifecycleHelper", zaae.class)) == null) {
            object = new zaae(lifecycleFragment, googleApiManager, GoogleApiAvailability.getInstance());
        }
        Preconditions.checkNotNull(apiKey, "ApiKey cannot be null");
        object.zad.add(apiKey);
        googleApiManager.zaC((zaae)object);
    }

    private final void zae() {
        if (!this.zad.isEmpty()) {
            this.zae.zaC(this);
        }
    }

    @Override
    public final void onResume() {
        super.onResume();
        this.zae();
    }

    @Override
    public final void onStart() {
        super.onStart();
        this.zae();
    }

    @Override
    public final void onStop() {
        super.onStop();
        this.zae.zaD(this);
    }

    final ArraySet<ApiKey<?>> zaa() {
        return this.zad;
    }

    @Override
    protected final void zab(ConnectionResult connectionResult, int n) {
        this.zae.zaz(connectionResult, n);
    }

    @Override
    protected final void zac() {
        this.zae.zaA();
    }
}

