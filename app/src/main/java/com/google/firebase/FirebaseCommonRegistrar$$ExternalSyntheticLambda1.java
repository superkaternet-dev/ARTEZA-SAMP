/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 */
package com.google.firebase;

import android.content.Context;
import com.google.firebase.FirebaseCommonRegistrar;
import com.google.firebase.platforminfo.LibraryVersionComponent;

public final class FirebaseCommonRegistrar$$ExternalSyntheticLambda1
implements LibraryVersionComponent.VersionExtractor {
    public static final FirebaseCommonRegistrar$$ExternalSyntheticLambda1 INSTANCE = new FirebaseCommonRegistrar$$ExternalSyntheticLambda1();

    private /* synthetic */ FirebaseCommonRegistrar$$ExternalSyntheticLambda1() {
    }

    public final String extract(Object object) {
        return FirebaseCommonRegistrar.lambda$getComponents$1((Context)object);
    }
}

