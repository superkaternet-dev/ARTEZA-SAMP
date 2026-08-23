/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core;

import com.google.firebase.database.core.CompoundWrite;
import com.google.firebase.database.core.Path;
import com.google.firebase.database.snapshot.NamedNode;
import com.google.firebase.database.snapshot.Node;

public final class UserWriteRecord {
    private final CompoundWrite merge;
    private final Node overwrite;
    private final Path path;
    private final boolean visible;
    private final long writeId;

    public UserWriteRecord(long l, Path path, CompoundWrite compoundWrite) {
        this.writeId = l;
        this.path = path;
        this.overwrite = null;
        this.merge = compoundWrite;
        this.visible = true;
    }

    public UserWriteRecord(long l, Path path, Node node, boolean bl) {
        this.writeId = l;
        this.path = path;
        this.overwrite = node;
        this.merge = null;
        this.visible = bl;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object != null && this.getClass() == object.getClass()) {
            object = (UserWriteRecord)object;
            if (this.writeId != ((UserWriteRecord)object).writeId) {
                return false;
            }
            if (!this.path.equals(((UserWriteRecord)object).path)) {
                return false;
            }
            if (this.visible != ((UserWriteRecord)object).visible) {
                return false;
            }
            Iterable<NamedNode> iterable = this.overwrite;
            if (!(iterable == null ? ((UserWriteRecord)object).overwrite == null : iterable.equals(((UserWriteRecord)object).overwrite))) {
                return false;
            }
            iterable = this.merge;
            return iterable == null ? ((UserWriteRecord)object).merge == null : ((CompoundWrite)iterable).equals(((UserWriteRecord)object).merge);
        }
        return false;
    }

    public CompoundWrite getMerge() {
        CompoundWrite compoundWrite = this.merge;
        if (compoundWrite != null) {
            return compoundWrite;
        }
        throw new IllegalArgumentException("Can't access merge when write is an overwrite!");
    }

    public Node getOverwrite() {
        Node node = this.overwrite;
        if (node != null) {
            return node;
        }
        throw new IllegalArgumentException("Can't access overwrite when write is a merge!");
    }

    public Path getPath() {
        return this.path;
    }

    public long getWriteId() {
        return this.writeId;
    }

    public int hashCode() {
        int n = Long.valueOf(this.writeId).hashCode();
        int n2 = Boolean.valueOf(this.visible).hashCode();
        int n3 = this.path.hashCode();
        Iterable<NamedNode> iterable = this.overwrite;
        int n4 = 0;
        int n5 = iterable != null ? iterable.hashCode() : 0;
        iterable = this.merge;
        if (iterable != null) {
            n4 = ((CompoundWrite)iterable).hashCode();
        }
        return (((n * 31 + n2) * 31 + n3) * 31 + n5) * 31 + n4;
    }

    public boolean isMerge() {
        boolean bl = this.merge != null;
        return bl;
    }

    public boolean isOverwrite() {
        boolean bl = this.overwrite != null;
        return bl;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("UserWriteRecord{id=");
        stringBuilder.append(this.writeId);
        stringBuilder.append(" path=");
        stringBuilder.append(this.path);
        stringBuilder.append(" visible=");
        stringBuilder.append(this.visible);
        stringBuilder.append(" overwrite=");
        stringBuilder.append(this.overwrite);
        stringBuilder.append(" merge=");
        stringBuilder.append(this.merge);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}

