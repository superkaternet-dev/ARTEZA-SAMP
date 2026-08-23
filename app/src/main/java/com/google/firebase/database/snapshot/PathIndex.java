/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.snapshot;

import com.google.firebase.database.core.Path;
import com.google.firebase.database.snapshot.ChildKey;
import com.google.firebase.database.snapshot.EmptyNode;
import com.google.firebase.database.snapshot.Index;
import com.google.firebase.database.snapshot.NamedNode;
import com.google.firebase.database.snapshot.Node;

public final class PathIndex
extends Index {
    private final Path indexPath;

    public PathIndex(Path path) {
        if (path.size() == 1 && path.getFront().isPriorityChildName()) {
            throw new IllegalArgumentException("Can't create PathIndex with '.priority' as key. Please use PriorityIndex instead!");
        }
        this.indexPath = path;
    }

    @Override
    public int compare(NamedNode namedNode, NamedNode namedNode2) {
        int n = namedNode.getNode().getChild(this.indexPath).compareTo(namedNode2.getNode().getChild(this.indexPath));
        if (n == 0) {
            return namedNode.getName().compareTo(namedNode2.getName());
        }
        return n;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object != null && this.getClass() == object.getClass()) {
            object = (PathIndex)object;
            return this.indexPath.equals(((PathIndex)object).indexPath);
        }
        return false;
    }

    @Override
    public String getQueryDefinition() {
        return this.indexPath.wireFormat();
    }

    public int hashCode() {
        return this.indexPath.hashCode();
    }

    @Override
    public boolean isDefinedOn(Node node) {
        return node.getChild(this.indexPath).isEmpty() ^ true;
    }

    @Override
    public NamedNode makePost(ChildKey childKey, Node node) {
        return new NamedNode(childKey, EmptyNode.Empty().updateChild(this.indexPath, node));
    }

    @Override
    public NamedNode maxPost() {
        Node node = EmptyNode.Empty().updateChild(this.indexPath, Node.MAX_NODE);
        return new NamedNode(ChildKey.getMaxName(), node);
    }
}

