/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 */
package com.google.firebase.platforminfo;

import com.google.firebase.platforminfo.LibraryVersion;
import javax.annotation.Nonnull;

final class AutoValue_LibraryVersion
extends LibraryVersion {
    private final String libraryName;
    private final String version;

    AutoValue_LibraryVersion(String string2, String string3) {
        if (string2 != null) {
            this.libraryName = string2;
            if (string3 != null) {
                this.version = string3;
                return;
            }
            throw new NullPointerException("Null version");
        }
        throw new NullPointerException("Null libraryName");
    }

    public boolean equals(Object object) {
        boolean bl = true;
        if (object == this) {
            return true;
        }
        if (object instanceof LibraryVersion) {
            if (!this.libraryName.equals(((LibraryVersion)(object = (LibraryVersion)object)).getLibraryName()) || !this.version.equals(((LibraryVersion)object).getVersion())) {
                bl = false;
            }
            return bl;
        }
        return false;
    }

    @Override
    @Nonnull
    public String getLibraryName() {
        return this.libraryName;
    }

    @Override
    @Nonnull
    public String getVersion() {
        return this.version;
    }

    public int hashCode() {
        return (1 * 1000003 ^ this.libraryName.hashCode()) * 1000003 ^ this.version.hashCode();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("LibraryVersion{libraryName=");
        stringBuilder.append(this.libraryName);
        stringBuilder.append(", version=");
        stringBuilder.append(this.version);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}

