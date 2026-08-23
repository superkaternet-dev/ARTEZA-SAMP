/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Bundle
 *  android.os.Handler
 *  android.os.Looper
 *  android.util.Log
 */
package com.google.android.gms.common.api.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.internal.zacq;
import com.google.android.gms.common.api.internal.zacr;
import com.google.android.gms.common.api.internal.zacs;
import com.google.android.gms.common.internal.ClientSettings;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.zav;
import com.google.android.gms.signin.SignInOptions;
import com.google.android.gms.signin.internal.zac;
import com.google.android.gms.signin.internal.zak;
import com.google.android.gms.signin.zad;
import com.google.android.gms.signin.zae;
import java.util.Set;

public final class zact
extends zac
implements GoogleApiClient.ConnectionCallbacks,
GoogleApiClient.OnConnectionFailedListener {
    private static final Api.AbstractClientBuilder<? extends zae, SignInOptions> zaa = com.google.android.gms.signin.zad.zac;
    private final Context zab;
    private final Handler zac;
    private final Api.AbstractClientBuilder<? extends zae, SignInOptions> zad;
    private final Set<Scope> zae;
    private final ClientSettings zaf;
    private zae zag;
    private zacs zah;

    public zact(Context context, Handler handler, ClientSettings clientSettings) {
        Api.AbstractClientBuilder<? extends zae, SignInOptions> abstractClientBuilder = zaa;
        this.zab = context;
        this.zac = handler;
        this.zaf = Preconditions.checkNotNull(clientSettings, "ClientSettings must not be null");
        this.zae = clientSettings.getRequiredScopes();
        this.zad = abstractClientBuilder;
    }

    static /* bridge */ /* synthetic */ zacs zac(zact zact2) {
        return zact2.zah;
    }

    static /* bridge */ /* synthetic */ void zad(zact zact2, zak abstractSafeParcelable) {
        Object object = ((zak)abstractSafeParcelable).zaa();
        if (((ConnectionResult)object).isSuccess()) {
            object = Preconditions.checkNotNull(((zak)abstractSafeParcelable).zab());
            if (!((ConnectionResult)(abstractSafeParcelable = ((zav)object).zaa())).isSuccess()) {
                object = String.valueOf(abstractSafeParcelable);
                String.valueOf(object).length();
                Exception exception = new Exception();
                Log.wtf((String)"SignInCoordinator", (String)"Sign-in succeeded with resolve account failure: ".concat(String.valueOf(object)), (Throwable)exception);
                zact2.zah.zae((ConnectionResult)abstractSafeParcelable);
                zact2.zag.disconnect();
                return;
            }
            zact2.zah.zaf(((zav)object).zab(), zact2.zae);
        } else {
            zact2.zah.zae((ConnectionResult)object);
        }
        zact2.zag.disconnect();
    }

    @Override
    public final void onConnected(Bundle bundle) {
        this.zag.zad(this);
    }

    @Override
    public final void onConnectionFailed(ConnectionResult connectionResult) {
        this.zah.zae(connectionResult);
    }

    @Override
    public final void onConnectionSuspended(int n) {
        this.zag.disconnect();
    }

    @Override
    public final void zab(zak zak2) {
        this.zac.post((Runnable)new zacr(this, zak2));
    }

    public final void zae(zacs object) {
        zae zae2 = this.zag;
        if (zae2 != null) {
            zae2.disconnect();
        }
        this.zaf.zae(System.identityHashCode(this));
        Api.AbstractClientBuilder<? extends zae, SignInOptions> abstractClientBuilder = this.zad;
        Context context = this.zab;
        zae2 = this.zac.getLooper();
        ClientSettings clientSettings = this.zaf;
        this.zag = abstractClientBuilder.buildClient(context, (Looper)zae2, clientSettings, clientSettings.zaa(), this, this);
        this.zah = object;
        object = this.zae;
        if (object != null && !object.isEmpty()) {
            this.zag.zab();
            return;
        }
        this.zac.post((Runnable)new zacq(this));
    }

    public final void zaf() {
        zae zae2 = this.zag;
        if (zae2 != null) {
            zae2.disconnect();
        }
    }
}

