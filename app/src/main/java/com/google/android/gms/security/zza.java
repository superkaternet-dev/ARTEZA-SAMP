/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.AsyncTask
 */
package com.google.android.gms.security;

import android.content.Context;
import android.os.AsyncTask;
import com.google.android.gms.security.ProviderInstaller;

final class zza
extends AsyncTask<Void, Void, Integer> {
    final Context zza;
    final ProviderInstaller.ProviderInstallListener zzb;

    zza(Context context, ProviderInstaller.ProviderInstallListener providerInstallListener) {
        this.zza = context;
        this.zzb = providerInstallListener;
    }
}

