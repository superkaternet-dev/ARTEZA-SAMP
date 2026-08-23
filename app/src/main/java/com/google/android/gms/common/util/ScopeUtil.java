/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.util;

import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.Preconditions;
import java.util.Set;

public final class ScopeUtil {
    private ScopeUtil() {
    }

    public static String[] toScopeString(Set<Scope> stringArray) {
        Preconditions.checkNotNull(stringArray, "scopes can't be null.");
        Scope[] scopeArray = stringArray.toArray(new Scope[stringArray.size()]);
        Preconditions.checkNotNull(scopeArray, "scopes can't be null.");
        stringArray = new String[scopeArray.length];
        for (int i = 0; i < scopeArray.length; ++i) {
            stringArray[i] = scopeArray[i].getScopeUri();
        }
        return stringArray;
    }
}

