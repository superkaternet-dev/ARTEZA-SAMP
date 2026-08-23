/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core.persistence;

import com.google.firebase.database.core.view.QuerySpec;

public final class TrackedQuery {
    public final boolean active;
    public final boolean complete;
    public final long id;
    public final long lastUse;
    public final QuerySpec querySpec;

    public TrackedQuery(long l, QuerySpec querySpec, long l2, boolean bl, boolean bl2) {
        this.id = l;
        if (querySpec.loadsAllData() && !querySpec.isDefault()) {
            throw new IllegalArgumentException("Can't create TrackedQuery for a non-default query that loads all data");
        }
        this.querySpec = querySpec;
        this.lastUse = l2;
        this.complete = bl;
        this.active = bl2;
    }

    public boolean equals(Object object) {
        boolean bl = true;
        if (object == this) {
            return true;
        }
        if (object != null && object.getClass() == this.getClass()) {
            object = (TrackedQuery)object;
            if (this.id != ((TrackedQuery)object).id || !this.querySpec.equals(((TrackedQuery)object).querySpec) || this.lastUse != ((TrackedQuery)object).lastUse || this.complete != ((TrackedQuery)object).complete || this.active != ((TrackedQuery)object).active) {
                bl = false;
            }
            return bl;
        }
        return false;
    }

    public int hashCode() {
        return (((Long.valueOf(this.id).hashCode() * 31 + this.querySpec.hashCode()) * 31 + Long.valueOf(this.lastUse).hashCode()) * 31 + Boolean.valueOf(this.complete).hashCode()) * 31 + Boolean.valueOf(this.active).hashCode();
    }

    public TrackedQuery setActiveState(boolean bl) {
        return new TrackedQuery(this.id, this.querySpec, this.lastUse, this.complete, bl);
    }

    public TrackedQuery setComplete() {
        return new TrackedQuery(this.id, this.querySpec, this.lastUse, true, this.active);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("TrackedQuery{id=");
        stringBuilder.append(this.id);
        stringBuilder.append(", querySpec=");
        stringBuilder.append(this.querySpec);
        stringBuilder.append(", lastUse=");
        stringBuilder.append(this.lastUse);
        stringBuilder.append(", complete=");
        stringBuilder.append(this.complete);
        stringBuilder.append(", active=");
        stringBuilder.append(this.active);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public TrackedQuery updateLastUse(long l) {
        return new TrackedQuery(this.id, this.querySpec, l, this.complete, this.active);
    }
}

