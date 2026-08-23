/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.snapshot;

import com.google.firebase.database.core.utilities.Utilities;
import com.google.firebase.database.snapshot.LeafNode;
import com.google.firebase.database.snapshot.Node;

public class StringNode
extends LeafNode<StringNode> {
    private final String value;

    public StringNode(String string2, Node node) {
        super(node);
        this.value = string2;
    }

    @Override
    protected int compareLeafValues(StringNode stringNode) {
        return this.value.compareTo(stringNode.value);
    }

    @Override
    public boolean equals(Object object) {
        boolean bl = object instanceof StringNode;
        boolean bl2 = false;
        if (!bl) {
            return false;
        }
        object = (StringNode)object;
        bl = bl2;
        if (this.value.equals(((StringNode)object).value)) {
            bl = bl2;
            if (this.priority.equals(((StringNode)object).priority)) {
                bl = true;
            }
        }
        return bl;
    }

    @Override
    public String getHashRepresentation(Node.HashVersion hashVersion) {
        switch (1.$SwitchMap$com$google$firebase$database$snapshot$Node$HashVersion[hashVersion.ordinal()]) {
            default: {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Invalid hash version for string node: ");
                stringBuilder.append((Object)hashVersion);
                throw new IllegalArgumentException(stringBuilder.toString());
            }
            case 2: {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(this.getPriorityHash(hashVersion));
                stringBuilder.append("string:");
                stringBuilder.append(Utilities.stringHashV2Representation(this.value));
                return stringBuilder.toString();
            }
            case 1: 
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.getPriorityHash(hashVersion));
        stringBuilder.append("string:");
        stringBuilder.append(this.value);
        return stringBuilder.toString();
    }

    @Override
    protected LeafNode.LeafType getLeafType() {
        return LeafNode.LeafType.String;
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
    public StringNode updatePriority(Node node) {
        return new StringNode(this.value, node);
    }
}

