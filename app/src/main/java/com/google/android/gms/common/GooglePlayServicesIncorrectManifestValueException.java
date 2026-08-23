/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common;

import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.common.GooglePlayServicesManifestException;

public final class GooglePlayServicesIncorrectManifestValueException
extends GooglePlayServicesManifestException {
    public GooglePlayServicesIncorrectManifestValueException(int n) {
        int n2 = GoogleApiAvailabilityLight.GOOGLE_PLAY_SERVICES_VERSION_CODE;
        StringBuilder stringBuilder = new StringBuilder(320);
        stringBuilder.append("The meta-data tag in your app's AndroidManifest.xml does not have the right value.  Expected ");
        stringBuilder.append(n2);
        stringBuilder.append(" but found ");
        stringBuilder.append(n);
        stringBuilder.append(".  You must have the following declaration within the <application> element:     <meta-data android:name=\"com.google.android.gms.version\" android:value=\"@integer/google_play_services_version\" />");
        super(n, stringBuilder.toString());
    }
}

