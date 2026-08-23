/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.snapshot;

import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.core.Path;
import com.google.firebase.database.snapshot.DeferredValueNode;
import com.google.firebase.database.snapshot.DoubleNode;
import com.google.firebase.database.snapshot.EmptyNode;
import com.google.firebase.database.snapshot.LongNode;
import com.google.firebase.database.snapshot.Node;
import com.google.firebase.database.snapshot.NodeUtilities;
import com.google.firebase.database.snapshot.StringNode;

public class PriorityUtilities {
    public static Node NullPriority() {
        return EmptyNode.Empty();
    }

    public static boolean isValidPriority(Node node) {
        boolean bl = node.getPriority().isEmpty() && (node.isEmpty() || node instanceof DoubleNode || node instanceof StringNode || node instanceof DeferredValueNode);
        return bl;
    }

    public static Node parsePriority(Path object, Object object2) {
        Comparable<Node> comparable = NodeUtilities.NodeFromJSON(object2);
        object2 = comparable;
        if (comparable instanceof LongNode) {
            object2 = new DoubleNode((double)((Long)comparable.getValue()), PriorityUtilities.NullPriority());
        }
        if (!PriorityUtilities.isValidPriority((Node)object2)) {
            object2 = new StringBuilder();
            if (object != null) {
                comparable = new StringBuilder();
                ((StringBuilder)comparable).append("Path '");
                ((StringBuilder)comparable).append(object);
                ((StringBuilder)comparable).append("'");
                object = ((StringBuilder)comparable).toString();
            } else {
                object = "Node";
            }
            ((StringBuilder)object2).append((String)object);
            ((StringBuilder)object2).append(" contains invalid priority: Must be a string, double, ServerValue, or null");
            throw new DatabaseException(((StringBuilder)object2).toString());
        }
        return object2;
    }

    public static Node parsePriority(Object object) {
        return PriorityUtilities.parsePriority(null, object);
    }
}

