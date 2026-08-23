/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 */
package com.google.android.gms.dynamite;

import android.content.Context;
import com.google.android.gms.dynamite.DynamiteModule;

final class zzh
implements DynamiteModule.VersionPolicy {
    zzh() {
    }

    @Override
    public final DynamiteModule.VersionPolicy.SelectionResult selectModule(Context context, String string2, DynamiteModule.VersionPolicy.IVersions iVersions) throws DynamiteModule.LoadingException {
        int n;
        DynamiteModule.VersionPolicy.SelectionResult selectionResult = new DynamiteModule.VersionPolicy.SelectionResult();
        selectionResult.remoteVersion = n = iVersions.zzb(context, string2, false);
        selectionResult.selection = n == 0 ? 0 : 1;
        return selectionResult;
    }
}

