/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.snapshot;

import com.google.firebase.database.core.utilities.Utilities;
import com.google.firebase.database.snapshot.LeafNode;
import com.google.firebase.database.snapshot.Node;
import com.google.firebase.database.snapshot.PriorityUtilities;
import java.util.Map;

public class DeferredValueNode
extends LeafNode<DeferredValueNode> {
    private Map<Object, Object> value;

    public DeferredValueNode(Map<Object, Object> map, Node node) {
        super(node);
        this.value = map;
    }

    @Override
    protected int compareLeafValues(DeferredValueNode deferredValueNode) {
        return 0;
    }

    @Override
    public boolean equals(Object object) {
        boolean bl;
        block1: {
            boolean bl2 = object instanceof DeferredValueNode;
            bl = false;
            if (!bl2) {
                return false;
            }
            object = (DeferredValueNode)object;
            if (!this.value.equals(((DeferredValueNode)object).value) || !this.priority.equals(((DeferredValueNode)object).priority)) break block1;
            bl = true;
        }
        return bl;
    }

    @Override
    public String getHashRepresentation(Node.HashVersion hashVersion) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.getPriorityHash(hashVersion));
        stringBuilder.append("deferredValue:");
        stringBuilder.append(this.value);
        return stringBuilder.toString();
    }

    @Override
    protected LeafNode.LeafType getLeafType() {
        return LeafNode.LeafType.DeferredValue;
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
    public DeferredValueNode updatePriority(Node node) {
        Utilities.hardAssert(PriorityUtilities.isValidPriority(node));
        return new DeferredValueNode(this.value, node);
    }
}

