/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.PendingIntent
 *  android.content.Context
 *  android.content.DialogInterface
 *  android.content.DialogInterface$OnCancelListener
 *  android.content.Intent
 *  android.os.Bundle
 *  android.os.Handler
 *  android.os.Looper
 *  android.os.Parcelable
 */
package com.google.android.gms.common.api.internal;

import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.internal.LifecycleCallback;
import com.google.android.gms.common.api.internal.LifecycleFragment;
import com.google.android.gms.common.api.internal.zam;
import com.google.android.gms.common.api.internal.zao;
import com.google.android.gms.internal.base.zaq;
import java.util.concurrent.atomic.AtomicReference;

public abstract class zap
extends LifecycleCallback
implements DialogInterface.OnCancelListener {
    protected volatile boolean zaa;
    protected final AtomicReference<zam> zab = new AtomicReference<Object>(null);
    protected final GoogleApiAvailability zac;
    private final Handler zad = new zaq(Looper.getMainLooper());

    zap(LifecycleFragment lifecycleFragment, GoogleApiAvailability googleApiAvailability) {
        super(lifecycleFragment);
        this.zac = googleApiAvailability;
    }

    private final void zaa(ConnectionResult connectionResult, int n) {
        this.zab.set(null);
        this.zab(connectionResult, n);
    }

    private final void zad() {
        this.zab.set(null);
        this.zac();
    }

    private static final int zae(zam zam2) {
        if (zam2 == null) {
            return -1;
        }
        return zam2.zaa();
    }

    static /* bridge */ /* synthetic */ void zaf(zap zap2, ConnectionResult connectionResult, int n) {
        zap2.zaa(connectionResult, n);
    }

    static /* bridge */ /* synthetic */ void zag(zap zap2) {
        zap2.zad();
    }

    @Override
    public final void onActivityResult(int n, int n2, Intent intent) {
        zam zam2 = this.zab.get();
        switch (n) {
            default: {
                break;
            }
            case 2: {
                n = this.zac.isGooglePlayServicesAvailable((Context)this.getActivity());
                if (n == 0) {
                    this.zad();
                    return;
                }
                if (zam2 == null) {
                    return;
                }
                if (zam2.zab().getErrorCode() != 18 || n != 18) break;
                return;
            }
            case 1: {
                if (n2 == -1) {
                    this.zad();
                    return;
                }
                if (n2 != 0) break;
                if (zam2 == null) {
                    return;
                }
                n = 13;
                if (intent != null) {
                    n = intent.getIntExtra("<<ResolutionFailureErrorDetail>>", 13);
                }
                this.zaa(new ConnectionResult(n, null, zam2.zab().toString()), zap.zae(zam2));
                return;
            }
        }
        if (zam2 != null) {
            this.zaa(zam2.zab(), zam2.zaa());
        }
    }

    public final void onCancel(DialogInterface dialogInterface) {
        this.zaa(new ConnectionResult(13, null), zap.zae(this.zab.get()));
    }

    @Override
    public final void onCreate(Bundle object) {
        super.onCreate((Bundle)object);
        if (object != null) {
            AtomicReference<zam> atomicReference = this.zab;
            object = object.getBoolean("resolving_error", false) ? new zam(new ConnectionResult(object.getInt("failed_status"), (PendingIntent)object.getParcelable("failed_resolution")), object.getInt("failed_client_id", -1)) : null;
            atomicReference.set((zam)object);
        }
    }

    @Override
    public final void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        zam zam2 = this.zab.get();
        if (zam2 == null) {
            return;
        }
        bundle.putBoolean("resolving_error", true);
        bundle.putInt("failed_client_id", zam2.zaa());
        bundle.putInt("failed_status", zam2.zab().getErrorCode());
        bundle.putParcelable("failed_resolution", (Parcelable)zam2.zab().getResolution());
    }

    @Override
    public void onStart() {
        super.onStart();
        this.zaa = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        this.zaa = false;
    }

    protected abstract void zab(ConnectionResult var1, int var2);

    protected abstract void zac();

    public final void zah(ConnectionResult object, int n) {
        if (this.zab.compareAndSet(null, (zam)(object = new zam((ConnectionResult)object, n)))) {
            this.zad.post((Runnable)new zao(this, (zam)object));
        }
    }
}

