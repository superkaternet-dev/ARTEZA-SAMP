/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database;

import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.interop.InternalAppCheckTokenProvider;
import com.google.firebase.auth.internal.InternalAuthProvider;
import com.google.firebase.components.Component;
import com.google.firebase.components.ComponentContainer;
import com.google.firebase.components.ComponentRegistrar;
import com.google.firebase.components.Dependency;
import com.google.firebase.database.DatabaseRegistrar$$ExternalSyntheticLambda0;
import com.google.firebase.database.FirebaseDatabaseComponent;
import com.google.firebase.platforminfo.LibraryVersionComponent;
import java.util.Arrays;
import java.util.List;

public class DatabaseRegistrar
implements ComponentRegistrar {
    static /* synthetic */ FirebaseDatabaseComponent lambda$getComponents$0(ComponentContainer componentContainer) {
        return new FirebaseDatabaseComponent(componentContainer.get(FirebaseApp.class), componentContainer.getDeferred(InternalAuthProvider.class), componentContainer.getDeferred(InternalAppCheckTokenProvider.class));
    }

    @Override
    public List<Component<?>> getComponents() {
        return Arrays.asList(Component.builder(FirebaseDatabaseComponent.class).add(Dependency.required(FirebaseApp.class)).add(Dependency.deferred(InternalAuthProvider.class)).add(Dependency.deferred(InternalAppCheckTokenProvider.class)).factory(DatabaseRegistrar$$ExternalSyntheticLambda0.INSTANCE).build(), LibraryVersionComponent.create("fire-rtdb", "20.0.4"));
    }
}

