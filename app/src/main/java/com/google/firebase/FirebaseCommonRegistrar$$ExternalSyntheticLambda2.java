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

public final class FirebaseCommonRegistrar$$ExternalSyntheticLambda2
implements LibraryVersionComponent.VersionExtractor {
    public static final FirebaseCommonRegistrar$$ExternalSyntheticLambda2 INSTANCE = new FirebaseCommonRegistrar$$ExternalSyntheticLambda2();

    private /* synthetic */ FirebaseCommonRegistrar$$ExternalSyntheticLambda2() {
    }

    public final String extract(Object object) {
        return FirebaseCommonRegistrar.lambda$getComponents$2((Context)object);
    }
}

