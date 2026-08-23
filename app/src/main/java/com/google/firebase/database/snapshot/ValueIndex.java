/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.snapshot;

import com.google.firebase.database.snapshot.ChildKey;
import com.google.firebase.database.snapshot.Index;
import com.google.firebase.database.snapshot.NamedNode;
import com.google.firebase.database.snapshot.Node;

public class ValueIndex
extends Index {
    private static final ValueIndex INSTANCE = new ValueIndex();

    private ValueIndex() {
    }

    public static ValueIndex getInstance() {
        return INSTANCE;
    }

    @Override
    public int compare(NamedNode namedNode, NamedNode namedNode2) {
        int n = namedNode.getNode().compareTo(namedNode2.getNode());
        if (n == 0) {
            return namedNode.getName().compareTo(namedNode2.getName());
        }
        return n;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof ValueIndex;
    }

    @Override
    public String getQueryDefinition() {
        return ".value";
    }

    public int hashCode() {
        return 4;
    }

    @Override
    public boolean isDefinedOn(Node node) {
        return true;
    }

    @Override
    public NamedNode makePost(ChildKey childKey, Node node) {
        return new NamedNode(childKey, node);
    }

    @Override
    public NamedNode maxPost() {
        return new NamedNode(ChildKey.getMaxName(), Node.MAX_NODE);
    }

    public String toString() {
        return "ValueIndex";
    }
}

