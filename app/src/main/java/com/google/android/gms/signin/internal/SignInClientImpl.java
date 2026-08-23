/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.accounts.Account
 *  android.content.Context
 *  android.os.Bundle
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.Looper
 *  android.os.Parcelable
 *  android.os.RemoteException
 *  android.util.Log
 */
package com.google.android.gms.signin.internal;

import android.accounts.Account;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.Parcelable;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.internal.BaseGmsClient;
import com.google.android.gms.common.internal.ClientSettings;
import com.google.android.gms.common.internal.GmsClient;
import com.google.android.gms.common.internal.IAccountAccessor;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.zat;
import com.google.android.gms.signin.internal.zae;
import com.google.android.gms.signin.internal.zaf;
import com.google.android.gms.signin.internal.zai;
import com.google.android.gms.signin.internal.zak;

public class SignInClientImpl
extends GmsClient<zaf>
implements com.google.android.gms.signin.zae {
    public static final int zaa = 0;
    private final boolean zab;
    private final ClientSettings zac;
    private final Bundle zad;
    private final Integer zae;

    public SignInClientImpl(Context context, Looper looper, boolean bl, ClientSettings clientSettings, Bundle bundle, GoogleApiClient.ConnectionCallbacks connectionCallbacks, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        super(context, looper, 44, clientSettings, connectionCallbacks, onConnectionFailedListener);
        this.zab = true;
        this.zac = clientSettings;
        this.zad = bundle;
        this.zae = clientSettings.zab();
    }

    public static Bundle createBundleFromClientSettings(ClientSettings clientSettings) {
        clientSettings.zaa();
        Integer n = clientSettings.zab();
        Bundle bundle = new Bundle();
        bundle.putParcelable("com.google.android.gms.signin.internal.clientRequestedAccount", (Parcelable)clientSettings.getAccount());
        if (n != null) {
            bundle.putInt("com.google.android.gms.common.internal.ClientSettings.sessionId", n.intValue());
        }
        bundle.putBoolean("com.google.android.gms.signin.internal.offlineAccessRequested", false);
        bundle.putBoolean("com.google.android.gms.signin.internal.idTokenRequested", false);
        bundle.putString("com.google.android.gms.signin.internal.serverClientId", null);
        bundle.putBoolean("com.google.android.gms.signin.internal.usePromptModeForAuthCode", true);
        bundle.putBoolean("com.google.android.gms.signin.internal.forceCodeForRefreshToken", false);
        bundle.putString("com.google.android.gms.signin.internal.hostedDomain", null);
        bundle.putString("com.google.android.gms.signin.internal.logSessionId", null);
        bundle.putBoolean("com.google.android.gms.signin.internal.waitForAccessTokenRefresh", false);
        return bundle;
    }

    @Override
    protected final /* synthetic */ IInterface createServiceInterface(IBinder object) {
        IInterface iInterface;
        object = object == null ? null : ((iInterface = object.queryLocalInterface("com.google.android.gms.signin.internal.ISignInService")) instanceof zaf ? (zaf)iInterface : new zaf((IBinder)object));
        return object;
    }

    @Override
    protected final Bundle getGetServiceRequestExtraArgs() {
        String string2 = this.zac.getRealClientPackageName();
        if (!this.getContext().getPackageName().equals(string2)) {
            this.zad.putString("com.google.android.gms.signin.internal.realClientPackageName", this.zac.getRealClientPackageName());
        }
        return this.zad;
    }

    @Override
    public final int getMinApkVersion() {
        return 12451000;
    }

    @Override
    protected final String getServiceDescriptor() {
        return "com.google.android.gms.signin.internal.ISignInService";
    }

    @Override
    protected final String getStartServiceAction() {
        return "com.google.android.gms.signin.service.START";
    }

    @Override
    public final boolean requiresSignIn() {
        return this.zab;
    }

    @Override
    public final void zaa() {
        try {
            ((zaf)this.getService()).zae(Preconditions.checkNotNull(this.zae));
            return;
        }
        catch (RemoteException remoteException) {
            Log.w((String)"SignInClientImpl", (String)"Remote service probably died when clearAccountFromSessionStore is called");
            return;
        }
    }

    @Override
    public final void zab() {
        this.connect(new BaseGmsClient.LegacyClientCallbackAdapter(this));
    }

    @Override
    public final void zac(IAccountAccessor iAccountAccessor, boolean bl) {
        try {
            ((zaf)this.getService()).zaf(iAccountAccessor, Preconditions.checkNotNull(this.zae), bl);
            return;
        }
        catch (RemoteException remoteException) {
            Log.w((String)"SignInClientImpl", (String)"Remote service probably died when saveDefaultAccount is called");
            return;
        }
    }

    @Override
    public final void zad(zae zae2) {
        Object object;
        Object object2;
        Preconditions.checkNotNull(zae2, "Expecting a valid ISignInCallbacks");
        try {
            object2 = this.zac.getAccountOrDefault();
            object = "<<default account>>".equals(object2.name) ? Storage.getInstance(this.getContext()).getSavedDefaultGoogleSignInAccount() : null;
        }
        catch (RemoteException remoteException) {
            Log.w((String)"SignInClientImpl", (String)"Remote service probably died when signIn is called");
            try {
                ConnectionResult connectionResult = new ConnectionResult(8, null);
                object2 = new zak(1, connectionResult, null);
                zae2.zab((zak)object2);
                return;
            }
            catch (RemoteException remoteException2) {
                Log.wtf((String)"SignInClientImpl", (String)"ISignInCallbacks#onSignInComplete should be executed from the same process, unexpected RemoteException.", (Throwable)remoteException);
                return;
            }
        }
        zat zat2 = new zat((Account)object2, Preconditions.checkNotNull(this.zae), (GoogleSignInAccount)object);
        object = (zaf)this.getService();
        object2 = new zai(1, zat2);
        ((zaf)object).zag((zai)object2, zae2);
    }
}

