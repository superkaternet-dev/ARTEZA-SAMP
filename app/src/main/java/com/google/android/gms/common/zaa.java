/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import java.util.Map;

public final class zaa
implements SuccessContinuation {
    public static final zaa zaa = new zaa();

    private /* synthetic */ zaa() {
    }

    public final Task then(Object object) {
        object = (Map)object;
        int n = GoogleApiAvailability.GOOGLE_PLAY_SERVICES_VERSION_CODE;
        return Tasks.forResult(null);
    }
}

