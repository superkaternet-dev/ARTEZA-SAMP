/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core.utilities.tuple;

import com.google.firebase.database.snapshot.ChildKey;
import com.google.firebase.database.snapshot.Node;
import com.google.firebase.database.snapshot.NodeUtilities;

public class NameAndPriority
implements Comparable<NameAndPriority> {
    private ChildKey name;
    private Node priority;

    public NameAndPriority(ChildKey childKey, Node node) {
        this.name = childKey;
        this.priority = node;
    }

    @Override
    public int compareTo(NameAndPriority nameAndPriority) {
        return NodeUtilities.nameAndPriorityCompare(this.name, this.priority, nameAndPriority.name, nameAndPriority.priority);
    }

    public ChildKey getName() {
        return this.name;
    }

    public Node getPriority() {
        return this.priority;
    }
}

