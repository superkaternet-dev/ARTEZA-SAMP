/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.snapshot;

import com.google.firebase.database.core.utilities.Utilities;
import com.google.firebase.database.snapshot.LeafNode;
import com.google.firebase.database.snapshot.Node;
import com.google.firebase.database.snapshot.PriorityUtilities;

public class DoubleNode
extends LeafNode<DoubleNode> {
    private final Double value;

    public DoubleNode(Double d, Node node) {
        super(node);
        this.value = d;
    }

    @Override
    protected int compareLeafValues(DoubleNode doubleNode) {
        return this.value.compareTo(doubleNode.value);
    }

    @Override
    public boolean equals(Object object) {
        boolean bl = object instanceof DoubleNode;
        boolean bl2 = false;
        if (!bl) {
            return false;
        }
        object = (DoubleNode)object;
        bl = bl2;
        if (this.value.equals(((DoubleNode)object).value)) {
            bl = bl2;
            if (this.priority.equals(((DoubleNode)object).priority)) {
                bl = true;
            }
        }
        return bl;
    }

    @Override
    public String getHashRepresentation(Node.HashVersion object) {
        object = this.getPriorityHash((Node.HashVersion)((Object)object));
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append((String)object);
        stringBuilder.append("number:");
        object = stringBuilder.toString();
        stringBuilder = new StringBuilder();
        stringBuilder.append((String)object);
        stringBuilder.append(Utilities.doubleToHashString(this.value));
        return stringBuilder.toString();
    }

    @Override
    protected LeafNode.LeafType getLeafType() {
        return LeafNode.LeafType.Number;
    }

    @Override
    public Object getValue() {
        return this.value;
    }

    @Override
    public int hashCode() {
        return this.value.hashCode() + this.priority.hashCode();
    }

    @Override
    public DoubleNode updatePriority(Node node) {
        Utilities.hardAssert(PriorityUtilities.isValidPriority(node));
        return new DoubleNode(this.value, node);
    }
}

