/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 */
package com.google.android.gms.dynamite;

import android.content.Context;
import com.google.android.gms.dynamite.DynamiteModule;

final class zzi
implements DynamiteModule.VersionPolicy {
    zzi() {
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public final DynamiteModule.VersionPolicy.SelectionResult selectModule(Context context, String string2, DynamiteModule.VersionPolicy.IVersions iVersions) throws DynamiteModule.LoadingException {
        int n;
        int n2;
        DynamiteModule.VersionPolicy.SelectionResult selectionResult = new DynamiteModule.VersionPolicy.SelectionResult();
        selectionResult.localVersion = iVersions.zza(context, string2);
        selectionResult.remoteVersion = n2 = iVersions.zzb(context, string2, true);
        int n3 = n = selectionResult.localVersion;
        if (n == 0) {
            if (n2 == 0) {
                selectionResult.selection = 0;
                return selectionResult;
            }
            n3 = 0;
        }
        if (n3 >= n2) {
            selectionResult.selection = -1;
            return selectionResult;
        }
        selectionResult.selection = 1;
        return selectionResult;
    }
}

