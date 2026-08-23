/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core.operation;

import com.google.firebase.database.core.CompoundWrite;
import com.google.firebase.database.core.Path;
import com.google.firebase.database.core.operation.Operation;
import com.google.firebase.database.core.operation.OperationSource;
import com.google.firebase.database.core.operation.Overwrite;
import com.google.firebase.database.snapshot.ChildKey;

public class Merge
extends Operation {
    private final CompoundWrite children;

    public Merge(OperationSource operationSource, Path path, CompoundWrite compoundWrite) {
        super(Operation.OperationType.Merge, operationSource, path);
        this.children = compoundWrite;
    }

    public CompoundWrite getChildren() {
        return this.children;
    }

    @Override
    public Operation operationForChild(ChildKey object) {
        if (this.path.isEmpty()) {
            if (((CompoundWrite)(object = this.children.childCompoundWrite(new Path(new ChildKey[]{object})))).isEmpty()) {
                return null;
            }
            if (((CompoundWrite)object).rootWrite() != null) {
                return new Overwrite(this.source, Path.getEmptyPath(), ((CompoundWrite)object).rootWrite());
            }
            return new Merge(this.source, Path.getEmptyPath(), (CompoundWrite)object);
        }
        if (this.path.getFront().equals(object)) {
            return new Merge(this.source, this.path.popFront(), this.children);
        }
        return null;
    }

    public String toString() {
        return String.format("Merge { path=%s, source=%s, children=%s }", this.getPath(), this.getSource(), this.children);
    }
}

