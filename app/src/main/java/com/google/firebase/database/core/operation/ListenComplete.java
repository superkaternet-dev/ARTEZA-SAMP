/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core.operation;

import com.google.firebase.database.core.Path;
import com.google.firebase.database.core.operation.Operation;
import com.google.firebase.database.core.operation.OperationSource;
import com.google.firebase.database.core.utilities.Utilities;
import com.google.firebase.database.snapshot.ChildKey;

public class ListenComplete
extends Operation {
    public ListenComplete(OperationSource operationSource, Path path) {
        super(Operation.OperationType.ListenComplete, operationSource, path);
        Utilities.hardAssert(operationSource.isFromUser() ^ true, "Can't have a listen complete from a user source");
    }

    @Override
    public Operation operationForChild(ChildKey childKey) {
        if (this.path.isEmpty()) {
            return new ListenComplete(this.source, Path.getEmptyPath());
        }
        return new ListenComplete(this.source, this.path.popFront());
    }

    public String toString() {
        return String.format("ListenComplete { path=%s, source=%s }", this.getPath(), this.getSource());
    }
}

