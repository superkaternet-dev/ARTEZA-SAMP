/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common;

public final class GooglePlayServicesNotAvailableException
extends Exception {
    public final int errorCode;

    public GooglePlayServicesNotAvailableException(int n) {
        this.errorCode = n;
    }
}

