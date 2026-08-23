/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 */
package com.google.firebase.platforminfo;

import com.google.firebase.platforminfo.AutoValue_LibraryVersion;
import javax.annotation.Nonnull;

abstract class LibraryVersion {
    LibraryVersion() {
    }

    static LibraryVersion create(String string2, String string3) {
        return new AutoValue_LibraryVersion(string2, string3);
    }

    @Nonnull
    public abstract String getLibraryName();

    @Nonnull
    public abstract String getVersion();
}

