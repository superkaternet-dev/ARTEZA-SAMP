/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 */
package com.google.android.gms.dynamite;

import android.content.Context;
import com.google.android.gms.dynamite.DynamiteModule;

final class zzj
implements DynamiteModule.VersionPolicy {
    zzj() {
    }

    @Override
    public final DynamiteModule.VersionPolicy.SelectionResult selectModule(Context context, String string2, DynamiteModule.VersionPolicy.IVersions iVersions) throws DynamiteModule.LoadingException {
        DynamiteModule.VersionPolicy.SelectionResult selectionResult;
        block2: {
            int n;
            int n2;
            block1: {
                int n3;
                block0: {
                    selectionResult = new DynamiteModule.VersionPolicy.SelectionResult();
                    selectionResult.localVersion = n2 = iVersions.zza(context, string2);
                    n = 0;
                    selectionResult.remoteVersion = n2 != 0 ? (n2 = iVersions.zzb(context, string2, false)) : (n2 = iVersions.zzb(context, string2, true));
                    n3 = selectionResult.localVersion;
                    if (n3 != 0) break block0;
                    if (n2 != 0) break block1;
                    selectionResult.selection = 0;
                    break block2;
                }
                n = n3;
            }
            selectionResult.selection = n >= n2 ? -1 : 1;
        }
        return selectionResult;
    }
}

