/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.snapshot;

import com.google.firebase.database.snapshot.ChildKey;
import com.google.firebase.database.snapshot.Index;
import com.google.firebase.database.snapshot.NamedNode;
import com.google.firebase.database.snapshot.Node;
import com.google.firebase.database.snapshot.NodeUtilities;
import com.google.firebase.database.snapshot.StringNode;

public class PriorityIndex
extends Index {
    private static final PriorityIndex INSTANCE = new PriorityIndex();

    private PriorityIndex() {
    }

    public static PriorityIndex getInstance() {
        return INSTANCE;
    }

    @Override
    public int compare(NamedNode namedNode, NamedNode namedNode2) {
        Node node = namedNode.getNode().getPriority();
        Node node2 = namedNode2.getNode().getPriority();
        return NodeUtilities.nameAndPriorityCompare(namedNode.getName(), node, namedNode2.getName(), node2);
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof PriorityIndex;
    }

    @Override
    public String getQueryDefinition() {
        throw new IllegalArgumentException("Can't get query definition on priority index!");
    }

    public int hashCode() {
        return 3155577;
    }

    @Override
    public boolean isDefinedOn(Node node) {
        return node.getPriority().isEmpty() ^ true;
    }

    @Override
    public NamedNode makePost(ChildKey childKey, Node node) {
        return new NamedNode(childKey, new StringNode("[PRIORITY-POST]", node));
    }

    @Override
    public NamedNode maxPost() {
        return this.makePost(ChildKey.getMaxName(), Node.MAX_NODE);
    }

    public String toString() {
        return "PriorityIndex";
    }
}

