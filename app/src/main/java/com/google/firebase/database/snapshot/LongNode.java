/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.snapshot;

import com.google.firebase.database.core.utilities.Utilities;
import com.google.firebase.database.snapshot.LeafNode;
import com.google.firebase.database.snapshot.Node;

public class LongNode
extends LeafNode<LongNode> {
    private final long value;

    public LongNode(Long l, Node node) {
        super(node);
        this.value = l;
    }

    @Override
    protected int compareLeafValues(LongNode longNode) {
        return Utilities.compareLongs(this.value, longNode.value);
    }

    @Override
    public boolean equals(Object object) {
        boolean bl = object instanceof LongNode;
        boolean bl2 = false;
        if (!bl) {
            return false;
        }
        object = (LongNode)object;
        bl = bl2;
        if (this.value == ((LongNode)object).value) {
            bl = bl2;
            if (this.priority.equals(((LongNode)object).priority)) {
                bl = true;
            }
        }
        return bl;
    }

    @Override
    public String getHashRepresentation(Node.HashVersion object) {
        object = this.getPriorityHash((Node.HashVersion)((Object)object));
        CharSequence charSequence = new StringBuilder();
        charSequence.append((String)object);
        charSequence.append("number:");
        charSequence = charSequence.toString();
        object = new StringBuilder();
        ((StringBuilder)object).append((String)charSequence);
        ((StringBuilder)object).append(Utilities.doubleToHashString(this.value));
        return ((StringBuilder)object).toString();
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
        long l = this.value;
        return (int)(l ^ l >>> 32) + this.priority.hashCode();
    }

    @Override
    public LongNode updatePriority(Node node) {
        return new LongNode(this.value, node);
    }
}

