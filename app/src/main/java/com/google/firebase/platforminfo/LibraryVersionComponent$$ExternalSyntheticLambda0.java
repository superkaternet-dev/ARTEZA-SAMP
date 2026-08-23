/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.platforminfo;

import com.google.firebase.components.ComponentContainer;
import com.google.firebase.components.ComponentFactory;
import com.google.firebase.platforminfo.LibraryVersionComponent;

public final class LibraryVersionComponent$$ExternalSyntheticLambda0
implements ComponentFactory {
    public final String f$0;
    public final LibraryVersionComponent.VersionExtractor f$1;

    public /* synthetic */ LibraryVersionComponent$$ExternalSyntheticLambda0(String string2, LibraryVersionComponent.VersionExtractor versionExtractor) {
        this.f$0 = string2;
        this.f$1 = versionExtractor;
    }

    public final Object create(ComponentContainer componentContainer) {
        return LibraryVersionComponent.lambda$fromContext$0(this.f$0, this.f$1, componentContainer);
    }
}

