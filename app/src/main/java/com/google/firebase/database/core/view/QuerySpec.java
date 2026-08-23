/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core.view;

import com.google.firebase.database.core.Path;
import com.google.firebase.database.core.view.QueryParams;
import com.google.firebase.database.snapshot.Index;
import java.util.Map;

public final class QuerySpec {
    private final QueryParams params;
    private final Path path;

    public QuerySpec(Path path, QueryParams queryParams) {
        this.path = path;
        this.params = queryParams;
    }

    public static QuerySpec defaultQueryAtPath(Path path) {
        return new QuerySpec(path, QueryParams.DEFAULT_PARAMS);
    }

    public static QuerySpec fromPathAndQueryObject(Path path, Map<String, Object> map) {
        return new QuerySpec(path, QueryParams.fromQueryObject(map));
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object != null && this.getClass() == object.getClass()) {
            object = (QuerySpec)object;
            if (!this.path.equals(((QuerySpec)object).path)) {
                return false;
            }
            return this.params.equals(((QuerySpec)object).params);
        }
        return false;
    }

    public Index getIndex() {
        return this.params.getIndex();
    }

    public QueryParams getParams() {
        return this.params;
    }

    public Path getPath() {
        return this.path;
    }

    public int hashCode() {
        return this.path.hashCode() * 31 + this.params.hashCode();
    }

    public boolean isDefault() {
        return this.params.isDefault();
    }

    public boolean loadsAllData() {
        return this.params.loadsAllData();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.path);
        stringBuilder.append(":");
        stringBuilder.append(this.params);
        return stringBuilder.toString();
    }
}

