/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.internal.RemoteCall;
import com.google.android.gms.common.util.BiConsumer;
import com.google.android.gms.tasks.TaskCompletionSource;

public final class zacu
implements RemoteCall {
    public final BiConsumer zaa;

    public /* synthetic */ zacu(BiConsumer biConsumer) {
        this.zaa = biConsumer;
    }

    public final void accept(Object object, Object object2) {
        this.zaa.accept((Api.AnyClient)object, (TaskCompletionSource)object2);
    }
}

