/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 */
package com.google.firebase;

import android.content.Context;
import com.google.firebase.FirebaseApp;
import com.google.firebase.inject.Provider;

public final class FirebaseApp$$ExternalSyntheticLambda1
implements Provider {
    public final FirebaseApp f$0;
    public final Context f$1;

    public /* synthetic */ FirebaseApp$$ExternalSyntheticLambda1(FirebaseApp firebaseApp, Context context) {
        this.f$0 = firebaseApp;
        this.f$1 = context;
    }

    public final Object get() {
        return this.f$0.lambda$new$0$com-google-firebase-FirebaseApp(this.f$1);
    }
}

