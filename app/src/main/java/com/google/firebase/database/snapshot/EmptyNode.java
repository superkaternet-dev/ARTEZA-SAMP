/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.snapshot;

import com.google.firebase.database.core.Path;
import com.google.firebase.database.snapshot.ChildKey;
import com.google.firebase.database.snapshot.ChildrenNode;
import com.google.firebase.database.snapshot.NamedNode;
import com.google.firebase.database.snapshot.Node;
import java.util.Collections;
import java.util.Iterator;

public class EmptyNode
extends ChildrenNode
implements Node {
    private static final EmptyNode empty = new EmptyNode();

    private EmptyNode() {
    }

    public static EmptyNode Empty() {
        return empty;
    }

    @Override
    public int compareTo(Node node) {
        int n = node.isEmpty() ? 0 : -1;
        return n;
    }

    @Override
    public boolean equals(Object object) {
        boolean bl = object instanceof EmptyNode;
        boolean bl2 = true;
        if (bl) {
            return true;
        }
        if (!(object instanceof Node && ((Node)object).isEmpty() && this.getPriority().equals(((Node)object).getPriority()))) {
            bl2 = false;
        }
        return bl2;
    }

    @Override
    public Node getChild(Path path) {
        return this;
    }

    @Override
    public int getChildCount() {
        return 0;
    }

    @Override
    public String getHash() {
        return "";
    }

    @Override
    public String getHashRepresentation(Node.HashVersion hashVersion) {
        return "";
    }

    @Override
    public Node getImmediateChild(ChildKey childKey) {
        return this;
    }

    @Override
    public ChildKey getPredecessorChildKey(ChildKey childKey) {
        return null;
    }

    @Override
    public Node getPriority() {
        return this;
    }

    @Override
    public ChildKey getSuccessorChildKey(ChildKey childKey) {
        return null;
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public Object getValue(boolean bl) {
        return null;
    }

    @Override
    public boolean hasChild(ChildKey childKey) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public boolean isLeafNode() {
        return false;
    }

    @Override
    public Iterator<NamedNode> iterator() {
        return Collections.emptyList().iterator();
    }

    @Override
    public Iterator<NamedNode> reverseIterator() {
        return Collections.emptyList().iterator();
    }

    @Override
    public String toString() {
        return "<Empty Node>";
    }

    @Override
    public Node updateChild(Path path, Node node) {
        if (path.isEmpty()) {
            return node;
        }
        ChildKey childKey = path.getFront();
        return this.updateImmediateChild(childKey, this.getImmediateChild(childKey).updateChild(path.popFront(), node));
    }

    @Override
    public Node updateImmediateChild(ChildKey childKey, Node node) {
        if (node.isEmpty()) {
            return this;
        }
        if (childKey.isPriorityChildName()) {
            return this;
        }
        return new ChildrenNode().updateImmediateChild(childKey, node);
    }

    @Override
    public EmptyNode updatePriority(Node node) {
        return this;
    }
}

