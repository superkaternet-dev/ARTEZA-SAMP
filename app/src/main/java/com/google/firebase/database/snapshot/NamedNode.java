/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.snapshot;

import com.google.firebase.database.snapshot.ChildKey;
import com.google.firebase.database.snapshot.EmptyNode;
import com.google.firebase.database.snapshot.Node;

public final class NamedNode {
    private static final NamedNode MAX_NODE;
    private static final NamedNode MIN_NODE;
    private final ChildKey name;
    private final Node node;

    static {
        MIN_NODE = new NamedNode(ChildKey.getMinName(), EmptyNode.Empty());
        MAX_NODE = new NamedNode(ChildKey.getMaxName(), Node.MAX_NODE);
    }

    public NamedNode(ChildKey childKey, Node node) {
        this.name = childKey;
        this.node = node;
    }

    public static NamedNode getMaxNode() {
        return MAX_NODE;
    }

    public static NamedNode getMinNode() {
        return MIN_NODE;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object != null && this.getClass() == object.getClass()) {
            object = (NamedNode)object;
            if (!this.name.equals(((NamedNode)object).name)) {
                return false;
            }
            return this.node.equals(((NamedNode)object).node);
        }
        return false;
    }

    public ChildKey getName() {
        return this.name;
    }

    public Node getNode() {
        return this.node;
    }

    public int hashCode() {
        return this.name.hashCode() * 31 + this.node.hashCode();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("NamedNode{name=");
        stringBuilder.append(this.name);
        stringBuilder.append(", node=");
        stringBuilder.append(this.node);
        stringBuilder.append('}');
        return stringBuilder.toString();
    }
}

