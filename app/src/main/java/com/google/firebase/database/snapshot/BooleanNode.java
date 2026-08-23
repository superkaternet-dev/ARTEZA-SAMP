/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.snapshot;

import com.google.firebase.database.snapshot.LeafNode;
import com.google.firebase.database.snapshot.Node;

public class BooleanNode
extends LeafNode<BooleanNode> {
    private final boolean value;

    public BooleanNode(Boolean bl, Node node) {
        super(node);
        this.value = bl;
    }

    @Override
    protected int compareLeafValues(BooleanNode booleanNode) {
        boolean bl = this.value;
        int n = bl == booleanNode.value ? 0 : (bl ? 1 : -1);
        return n;
    }

    @Override
    public boolean equals(Object object) {
        boolean bl = object instanceof BooleanNode;
        boolean bl2 = false;
        if (!bl) {
            return false;
        }
        object = (BooleanNode)object;
        bl = bl2;
        if (this.value == ((BooleanNode)object).value) {
            bl = bl2;
            if (this.priority.equals(((BooleanNode)object).priority)) {
                bl = true;
            }
        }
        return bl;
    }

    @Override
    public String getHashRepresentation(Node.HashVersion hashVersion) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.getPriorityHash(hashVersion));
        stringBuilder.append("boolean:");
        stringBuilder.append(this.value);
        return stringBuilder.toString();
    }

    @Override
    protected LeafNode.LeafType getLeafType() {
        return LeafNode.LeafType.Boolean;
    }

    @Override
    public Object getValue() {
        return this.value;
    }

    @Override
    public int hashCode() {
        return this.value + this.priority.hashCode();
    }

    @Override
    public BooleanNode updatePriority(Node node) {
        return new BooleanNode(this.value, node);
    }
}

