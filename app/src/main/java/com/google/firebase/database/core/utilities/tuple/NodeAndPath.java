/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core.utilities.tuple;

import com.google.firebase.database.core.Path;
import com.google.firebase.database.snapshot.Node;

public class NodeAndPath {
    private Node node;
    private Path path;

    public NodeAndPath(Node node, Path path) {
        this.node = node;
        this.path = path;
    }

    public Node getNode() {
        return this.node;
    }

    public Path getPath() {
        return this.path;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public void setPath(Path path) {
        this.path = path;
    }
}

