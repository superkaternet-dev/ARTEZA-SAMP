/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core;

import com.google.firebase.database.core.Path;
import com.google.firebase.database.snapshot.EmptyNode;
import com.google.firebase.database.snapshot.Node;

public class SnapshotHolder {
    private Node rootNode;

    SnapshotHolder() {
        this.rootNode = EmptyNode.Empty();
    }

    public SnapshotHolder(Node node) {
        this.rootNode = node;
    }

    public Node getNode(Path path) {
        return this.rootNode.getChild(path);
    }

    public Node getRootNode() {
        return this.rootNode;
    }

    public void update(Path path, Node node) {
        this.rootNode = this.rootNode.updateChild(path, node);
    }
}

