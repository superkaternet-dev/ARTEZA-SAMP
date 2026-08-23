/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.snapshot;

import com.google.firebase.database.core.utilities.Utilities;
import com.google.firebase.database.snapshot.ChildKey;
import com.google.firebase.database.snapshot.EmptyNode;
import com.google.firebase.database.snapshot.Index;
import com.google.firebase.database.snapshot.NamedNode;
import com.google.firebase.database.snapshot.Node;
import com.google.firebase.database.snapshot.StringNode;

public class KeyIndex
extends Index {
    private static final KeyIndex INSTANCE = new KeyIndex();

    private KeyIndex() {
    }

    public static KeyIndex getInstance() {
        return INSTANCE;
    }

    @Override
    public int compare(NamedNode namedNode, NamedNode namedNode2) {
        return namedNode.getName().compareTo(namedNode2.getName());
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof KeyIndex;
    }

    @Override
    public String getQueryDefinition() {
        return ".key";
    }

    public int hashCode() {
        return 37;
    }

    @Override
    public boolean isDefinedOn(Node node) {
        return true;
    }

    @Override
    public NamedNode makePost(ChildKey childKey, Node node) {
        Utilities.hardAssert(node instanceof StringNode);
        return new NamedNode(ChildKey.fromString((String)node.getValue()), EmptyNode.Empty());
    }

    @Override
    public NamedNode maxPost() {
        return NamedNode.getMaxNode();
    }

    public String toString() {
        return "KeyIndex";
    }
}

