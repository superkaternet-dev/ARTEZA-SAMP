/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 */
package com.google.firebase.platforminfo;

import android.content.Context;
import com.google.firebase.components.Component;
import com.google.firebase.components.ComponentContainer;
import com.google.firebase.components.Dependency;
import com.google.firebase.platforminfo.LibraryVersion;
import com.google.firebase.platforminfo.LibraryVersionComponent$$ExternalSyntheticLambda0;

public class LibraryVersionComponent {
    private LibraryVersionComponent() {
    }

    public static Component<?> create(String string2, String string3) {
        return Component.intoSet(LibraryVersion.create(string2, string3), LibraryVersion.class);
    }

    public static Component<?> fromContext(String string2, VersionExtractor<Context> versionExtractor) {
        return Component.intoSetBuilder(LibraryVersion.class).add(Dependency.required(Context.class)).factory(new LibraryVersionComponent$$ExternalSyntheticLambda0(string2, versionExtractor)).build();
    }

    static /* synthetic */ LibraryVersion lambda$fromContext$0(String string2, VersionExtractor versionExtractor, ComponentContainer componentContainer) {
        return LibraryVersion.create(string2, versionExtractor.extract(componentContainer.get(Context.class)));
    }

    public static interface VersionExtractor<T> {
        public String extract(T var1);
    }
}

