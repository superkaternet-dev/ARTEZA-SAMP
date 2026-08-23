/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core.utilities;

import com.google.firebase.database.core.Path;
import com.google.firebase.database.core.RepoInfo;

public final class ParsedUrl {
    public Path path;
    public RepoInfo repoInfo;

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object != null && this.getClass() == object.getClass()) {
            object = (ParsedUrl)object;
            if (!this.repoInfo.equals(((ParsedUrl)object).repoInfo)) {
                return false;
            }
            return this.path.equals(((ParsedUrl)object).path);
        }
        return false;
    }

    public int hashCode() {
        return this.repoInfo.hashCode() * 31 + this.path.hashCode();
    }
}

