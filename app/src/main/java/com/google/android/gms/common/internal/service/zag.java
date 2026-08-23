/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.internal.service;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.internal.BaseImplementation;
import com.google.android.gms.common.internal.service.Common;
import com.google.android.gms.common.internal.service.zah;

abstract class zag<R extends Result>
extends BaseImplementation.ApiMethodImpl<R, zah> {
    public zag(GoogleApiClient googleApiClient) {
        super(Common.API, googleApiClient);
    }
}

