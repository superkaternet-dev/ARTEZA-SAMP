/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 */
package com.google.android.gms.common.internal;

import android.content.Context;
import android.content.res.Resources;
import com.google.android.gms.common.R;
import com.google.android.gms.common.internal.Preconditions;

public class StringResourceValueReader {
    private final Resources zza;
    private final String zzb;

    public StringResourceValueReader(Context context) {
        Preconditions.checkNotNull(context);
        context = context.getResources();
        this.zza = context;
        this.zzb = context.getResourcePackageName(R.string.common_google_play_services_unknown_issue);
    }

    public String getString(String string2) {
        int n = this.zza.getIdentifier(string2, "string", this.zzb);
        if (n == 0) {
            return null;
        }
        return this.zza.getString(n);
    }
}

