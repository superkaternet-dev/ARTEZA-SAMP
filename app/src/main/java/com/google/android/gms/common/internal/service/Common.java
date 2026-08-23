/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.internal.service;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.internal.service.zab;
import com.google.android.gms.common.internal.service.zae;
import com.google.android.gms.common.internal.service.zah;

public final class Common {
    public static final Api<Api.ApiOptions.NoOptions> API;
    public static final Api.ClientKey<zah> CLIENT_KEY;
    public static final zae zaa;
    private static final Api.AbstractClientBuilder<zah, Api.ApiOptions.NoOptions> zab;

    static {
        zab zab2;
        Api.ClientKey clientKey = new Api.ClientKey();
        CLIENT_KEY = clientKey;
        zab = zab2 = new zab();
        API = new Api<Api.ApiOptions.NoOptions>("Common.API", zab2, clientKey);
        zaa = new zae();
    }
}

