/*
 * Decompiled with CFR 0.152.
 */
package androidx.core.os;

public class OperationCanceledException
extends RuntimeException {
    public OperationCanceledException() {
        this((String)null);
    }

    public OperationCanceledException(String string2) {
        if (string2 == null) {
            string2 = "The operation has been canceled.";
        }
        super(string2);
    }
}

