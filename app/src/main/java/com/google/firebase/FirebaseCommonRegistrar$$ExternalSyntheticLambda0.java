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

public final class FirebaseCommonRegistrar$$ExternalSyntheticLambda0
implements LibraryVersionComponent.VersionExtractor {
    public static final FirebaseCommonRegistrar$$ExternalSyntheticLambda0 INSTANCE = new FirebaseCommonRegistrar$$ExternalSyntheticLambda0();

    private /* synthetic */ FirebaseCommonRegistrar$$ExternalSyntheticLambda0() {
    }

    public final String extract(Object object) {
        return FirebaseCommonRegistrar.lambda$getComponents$0((Context)object);
    }
}

