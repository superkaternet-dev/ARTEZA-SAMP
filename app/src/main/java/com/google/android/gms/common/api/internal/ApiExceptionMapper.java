/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.StatusExceptionMapper;
import com.google.android.gms.common.internal.ApiExceptionUtil;

public class ApiExceptionMapper
implements StatusExceptionMapper {
    @Override
    public final Exception getException(Status status) {
        return ApiExceptionUtil.fromStatus(status);
    }
}

