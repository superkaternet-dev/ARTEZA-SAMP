/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core.operation;

import com.google.firebase.database.core.Path;
import com.google.firebase.database.core.operation.Operation;
import com.google.firebase.database.core.operation.OperationSource;
import com.google.firebase.database.core.utilities.ImmutableTree;
import com.google.firebase.database.core.utilities.Utilities;
import com.google.firebase.database.snapshot.ChildKey;

public class AckUserWrite
extends Operation {
    private final ImmutableTree<Boolean> affectedTree;
    private final boolean revert;

    public AckUserWrite(Path path, ImmutableTree<Boolean> immutableTree, boolean bl) {
        super(Operation.OperationType.AckUserWrite, OperationSource.USER, path);
        this.affectedTree = immutableTree;
        this.revert = bl;
    }

    public ImmutableTree<Boolean> getAffectedTree() {
        return this.affectedTree;
    }

    public boolean isRevert() {
        return this.revert;
    }

    @Override
    public Operation operationForChild(ChildKey object) {
        if (!this.path.isEmpty()) {
            Utilities.hardAssert(this.path.getFront().equals(object), "operationForChild called for unrelated child.");
            return new AckUserWrite(this.path.popFront(), this.affectedTree, this.revert);
        }
        if (this.affectedTree.getValue() != null) {
            Utilities.hardAssert(this.affectedTree.getChildren().isEmpty(), "affectedTree should not have overlapping affected paths.");
            return this;
        }
        object = this.affectedTree.subtree(new Path(new ChildKey[]{object}));
        return new AckUserWrite(Path.getEmptyPath(), (ImmutableTree<Boolean>)object, this.revert);
    }

    public String toString() {
        return String.format("AckUserWrite { path=%s, revert=%s, affectedTree=%s }", this.getPath(), this.revert, this.affectedTree);
    }
}

