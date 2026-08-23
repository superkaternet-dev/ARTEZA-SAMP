/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.json.JSONObject
 */
package com.nvidia.devtech;

import com.nvidia.devtech.NvEventQueueActivity;
import org.json.JSONObject;

public final class NvEventQueueActivity$$ExternalSyntheticLambda14
implements Runnable {
    public final NvEventQueueActivity f$0;
    public final JSONObject f$1;

    public /* synthetic */ NvEventQueueActivity$$ExternalSyntheticLambda14(NvEventQueueActivity nvEventQueueActivity, JSONObject jSONObject) {
        this.f$0 = nvEventQueueActivity;
        this.f$1 = jSONObject;
    }

    @Override
    public final void run() {
        this.f$0.lambda$localShowNotification$15$com-nvidia-devtech-NvEventQueueActivity(this.f$1);
    }
}

