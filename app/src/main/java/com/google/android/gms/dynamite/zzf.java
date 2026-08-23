/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 */
package com.google.android.gms.dynamite;

import android.content.Context;
import com.google.android.gms.dynamite.DynamiteModule;

final class zzf
implements DynamiteModule.VersionPolicy {
    zzf() {
    }

    @Override
    public final DynamiteModule.VersionPolicy.SelectionResult selectModule(Context context, String string2, DynamiteModule.VersionPolicy.IVersions iVersions) throws DynamiteModule.LoadingException {
        int n;
        DynamiteModule.VersionPolicy.SelectionResult selectionResult = new DynamiteModule.VersionPolicy.SelectionResult();
        selectionResult.remoteVersion = n = iVersions.zzb(context, string2, true);
        if (n != 0) {
            selectionResult.selection = 1;
        } else {
            selectionResult.localVersion = n = iVersions.zza(context, string2);
            if (n != 0) {
                selectionResult.selection = -1;
            }
        }
        return selectionResult;
    }
}

