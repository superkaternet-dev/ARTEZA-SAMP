/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core.utilities.tuple;

import com.google.firebase.database.core.Path;

public class PathAndId {
    private long id;
    private Path path;

    public PathAndId(Path path, long l) {
        this.path = path;
        this.id = l;
    }

    public long getId() {
        return this.id;
    }

    public Path getPath() {
        return this.path;
    }
}

