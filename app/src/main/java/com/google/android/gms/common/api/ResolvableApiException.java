/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.app.PendingIntent
 *  android.content.IntentSender$SendIntentException
 */
package com.google.android.gms.common.api;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.IntentSender;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;

public class ResolvableApiException
extends ApiException {
    public ResolvableApiException(Status status) {
        super(status);
    }

    public PendingIntent getResolution() {
        return this.getStatus().getResolution();
    }

    public void startResolutionForResult(Activity activity, int n) throws IntentSender.SendIntentException {
        this.getStatus().startResolutionForResult(activity, n);
    }
}

