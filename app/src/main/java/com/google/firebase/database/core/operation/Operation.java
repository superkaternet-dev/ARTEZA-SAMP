/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core.operation;

import com.google.firebase.database.core.Path;
import com.google.firebase.database.core.operation.OperationSource;
import com.google.firebase.database.snapshot.ChildKey;

public abstract class Operation {
    protected final Path path;
    protected final OperationSource source;
    protected final OperationType type;

    protected Operation(OperationType operationType, OperationSource operationSource, Path path) {
        this.type = operationType;
        this.source = operationSource;
        this.path = path;
    }

    public Path getPath() {
        return this.path;
    }

    public OperationSource getSource() {
        return this.source;
    }

    public OperationType getType() {
        return this.type;
    }

    public abstract Operation operationForChild(ChildKey var1);

    public static final class OperationType
    extends Enum<OperationType> {
        private static final OperationType[] $VALUES;
        public static final /* enum */ OperationType AckUserWrite;
        public static final /* enum */ OperationType ListenComplete;
        public static final /* enum */ OperationType Merge;
        public static final /* enum */ OperationType Overwrite;

        static {
            OperationType operationType;
            OperationType operationType2;
            OperationType operationType3;
            OperationType operationType4;
            Overwrite = operationType4 = new OperationType();
            Merge = operationType3 = new OperationType();
            AckUserWrite = operationType2 = new OperationType();
            ListenComplete = operationType = new OperationType();
            $VALUES = new OperationType[]{operationType4, operationType3, operationType2, operationType};
        }

        public static OperationType valueOf(String string2) {
            return Enum.valueOf(OperationType.class, string2);
        }

        public static OperationType[] values() {
            return (OperationType[])$VALUES.clone();
        }
    }
}

