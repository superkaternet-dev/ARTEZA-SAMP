/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.api;

import com.google.android.gms.common.api.Status;

public class ApiException
extends Exception {
    @Deprecated
    protected final Status mStatus;

    public ApiException(Status status) {
        int n = status.getStatusCode();
        String string2 = status.getStatusMessage() != null ? status.getStatusMessage() : "";
        StringBuilder stringBuilder = new StringBuilder(String.valueOf(string2).length() + 13);
        stringBuilder.append(n);
        stringBuilder.append(": ");
        stringBuilder.append(string2);
        super(stringBuilder.toString());
        this.mStatus = status;
    }

    public Status getStatus() {
        return this.mStatus;
    }

    public int getStatusCode() {
        return this.mStatus.getStatusCode();
    }

    @Deprecated
    public String getStatusMessage() {
        return this.mStatus.getStatusMessage();
    }
}

