/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core.utilities;

import com.google.firebase.database.core.utilities.Utilities;
import com.google.firebase.database.snapshot.BooleanNode;
import com.google.firebase.database.snapshot.ChildrenNode;
import com.google.firebase.database.snapshot.DoubleNode;
import com.google.firebase.database.snapshot.LeafNode;
import com.google.firebase.database.snapshot.LongNode;
import com.google.firebase.database.snapshot.NamedNode;
import com.google.firebase.database.snapshot.Node;
import com.google.firebase.database.snapshot.StringNode;

public class NodeSizeEstimator {
    private static final int LEAF_PRIORITY_OVERHEAD = 24;

    private static long estimateLeafNodeSize(LeafNode<?> leafNode) {
        block8: {
            long l;
            block5: {
                block7: {
                    block6: {
                        block4: {
                            if (!(leafNode instanceof DoubleNode)) break block4;
                            l = 8L;
                            break block5;
                        }
                        if (!(leafNode instanceof LongNode)) break block6;
                        l = 8L;
                        break block5;
                    }
                    if (!(leafNode instanceof BooleanNode)) break block7;
                    l = 4L;
                    break block5;
                }
                if (!(leafNode instanceof StringNode)) break block8;
                l = 2L + (long)((String)leafNode.getValue()).length();
            }
            if (leafNode.getPriority().isEmpty()) {
                return l;
            }
            return 24L + l + NodeSizeEstimator.estimateLeafNodeSize((LeafNode)leafNode.getPriority());
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unknown leaf node type: ");
        stringBuilder.append(leafNode.getClass());
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public static long estimateSerializedNodeSize(Node node) {
        if (node.isEmpty()) {
            return 4L;
        }
        if (node.isLeafNode()) {
            return NodeSizeEstimator.estimateLeafNodeSize((LeafNode)node);
        }
        boolean bl = node instanceof ChildrenNode;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unexpected node type: ");
        stringBuilder.append(node.getClass());
        Utilities.hardAssert(bl, stringBuilder.toString());
        long l = 1L;
        for (NamedNode namedNode : node) {
            l = l + (long)namedNode.getName().asString().length() + 4L + NodeSizeEstimator.estimateSerializedNodeSize(namedNode.getNode());
        }
        long l2 = l;
        if (!node.getPriority().isEmpty()) {
            l2 = l + 12L + NodeSizeEstimator.estimateLeafNodeSize((LeafNode)node.getPriority());
        }
        return l2;
    }

    public static int nodeCount(Node object) {
        if (object.isEmpty()) {
            return 0;
        }
        if (object.isLeafNode()) {
            return 1;
        }
        boolean bl = object instanceof ChildrenNode;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unexpected node type: ");
        stringBuilder.append(object.getClass());
        Utilities.hardAssert(bl, stringBuilder.toString());
        int n = 0;
        object = object.iterator();
        while (object.hasNext()) {
            n += NodeSizeEstimator.nodeCount(((NamedNode)object.next()).getNode());
        }
        return n;
    }
}

