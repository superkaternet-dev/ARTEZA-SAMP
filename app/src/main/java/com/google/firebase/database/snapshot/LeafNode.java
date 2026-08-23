/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.snapshot;

import com.google.firebase.database.core.Path;
import com.google.firebase.database.core.utilities.Utilities;
import com.google.firebase.database.snapshot.ChildKey;
import com.google.firebase.database.snapshot.ChildrenNode;
import com.google.firebase.database.snapshot.DoubleNode;
import com.google.firebase.database.snapshot.EmptyNode;
import com.google.firebase.database.snapshot.LongNode;
import com.google.firebase.database.snapshot.NamedNode;
import com.google.firebase.database.snapshot.Node;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public abstract class LeafNode<T extends LeafNode>
implements Node {
    private String lazyHash;
    protected final Node priority;

    LeafNode(Node node) {
        this.priority = node;
    }

    private static int compareLongDoubleNodes(LongNode longNode, DoubleNode doubleNode) {
        return Double.valueOf(((Long)longNode.getValue()).longValue()).compareTo((Double)doubleNode.getValue());
    }

    protected abstract int compareLeafValues(T var1);

    @Override
    public int compareTo(Node node) {
        if (node.isEmpty()) {
            return 1;
        }
        if (node instanceof ChildrenNode) {
            return -1;
        }
        Utilities.hardAssert(node.isLeafNode(), "Node is not leaf node!");
        if (this instanceof LongNode && node instanceof DoubleNode) {
            return LeafNode.compareLongDoubleNodes((LongNode)this, (DoubleNode)node);
        }
        if (this instanceof DoubleNode && node instanceof LongNode) {
            return LeafNode.compareLongDoubleNodes((LongNode)node, (DoubleNode)this) * -1;
        }
        return this.leafCompare((LeafNode)node);
    }

    public abstract boolean equals(Object var1);

    @Override
    public Node getChild(Path path) {
        if (path.isEmpty()) {
            return this;
        }
        if (path.getFront().isPriorityChildName()) {
            return this.priority;
        }
        return EmptyNode.Empty();
    }

    @Override
    public int getChildCount() {
        return 0;
    }

    @Override
    public String getHash() {
        if (this.lazyHash == null) {
            this.lazyHash = Utilities.sha1HexDigest(this.getHashRepresentation(Node.HashVersion.V1));
        }
        return this.lazyHash;
    }

    @Override
    public Node getImmediateChild(ChildKey childKey) {
        if (childKey.isPriorityChildName()) {
            return this.priority;
        }
        return EmptyNode.Empty();
    }

    protected abstract LeafType getLeafType();

    @Override
    public ChildKey getPredecessorChildKey(ChildKey childKey) {
        return null;
    }

    @Override
    public Node getPriority() {
        return this.priority;
    }

    protected String getPriorityHash(Node.HashVersion hashVersion) {
        switch (1.$SwitchMap$com$google$firebase$database$snapshot$Node$HashVersion[hashVersion.ordinal()]) {
            default: {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unknown hash version: ");
                stringBuilder.append((Object)hashVersion);
                throw new IllegalArgumentException(stringBuilder.toString());
            }
            case 1: 
            case 2: 
        }
        if (this.priority.isEmpty()) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("priority:");
        stringBuilder.append(this.priority.getHashRepresentation(hashVersion));
        stringBuilder.append(":");
        return stringBuilder.toString();
    }

    @Override
    public ChildKey getSuccessorChildKey(ChildKey childKey) {
        return null;
    }

    @Override
    public Object getValue(boolean bl) {
        if (bl && !this.priority.isEmpty()) {
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put(".value", this.getValue());
            hashMap.put(".priority", this.priority.getValue());
            return hashMap;
        }
        return this.getValue();
    }

    @Override
    public boolean hasChild(ChildKey childKey) {
        return false;
    }

    public abstract int hashCode();

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean isLeafNode() {
        return true;
    }

    @Override
    public Iterator<NamedNode> iterator() {
        return Collections.emptyList().iterator();
    }

    protected int leafCompare(LeafNode<?> leafNode) {
        LeafType leafType;
        LeafType leafType2 = this.getLeafType();
        if (leafType2.equals((Object)(leafType = leafNode.getLeafType()))) {
            return this.compareLeafValues(leafNode);
        }
        return leafType2.compareTo(leafType);
    }

    @Override
    public Iterator<NamedNode> reverseIterator() {
        return Collections.emptyList().iterator();
    }

    public String toString() {
        String string2 = this.getValue(true).toString();
        if (string2.length() > 100) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(string2.substring(0, 100));
            stringBuilder.append("...");
            string2 = stringBuilder.toString();
        }
        return string2;
    }

    @Override
    public Node updateChild(Path path, Node node) {
        boolean bl;
        ChildKey childKey = path.getFront();
        if (childKey == null) {
            return node;
        }
        if (node.isEmpty() && !childKey.isPriorityChildName()) {
            return this;
        }
        boolean bl2 = path.getFront().isPriorityChildName();
        boolean bl3 = bl = true;
        if (bl2) {
            bl3 = path.size() == 1 ? bl : false;
        }
        Utilities.hardAssert(bl3);
        return this.updateImmediateChild(childKey, EmptyNode.Empty().updateChild(path.popFront(), node));
    }

    @Override
    public Node updateImmediateChild(ChildKey childKey, Node node) {
        if (childKey.isPriorityChildName()) {
            return this.updatePriority(node);
        }
        if (node.isEmpty()) {
            return this;
        }
        return EmptyNode.Empty().updateImmediateChild(childKey, node).updatePriority(this.priority);
    }

    protected static final class LeafType
    extends Enum<LeafType> {
        private static final LeafType[] $VALUES;
        public static final /* enum */ LeafType Boolean;
        public static final /* enum */ LeafType DeferredValue;
        public static final /* enum */ LeafType Number;
        public static final /* enum */ LeafType String;

        static {
            LeafType leafType;
            LeafType leafType2;
            LeafType leafType3;
            LeafType leafType4;
            DeferredValue = leafType4 = new LeafType();
            Boolean = leafType3 = new LeafType();
            Number = leafType2 = new LeafType();
            String = leafType = new LeafType();
            $VALUES = new LeafType[]{leafType4, leafType3, leafType2, leafType};
        }

        public static LeafType valueOf(String string2) {
            return Enum.valueOf(LeafType.class, string2);
        }

        public static LeafType[] values() {
            return (LeafType[])$VALUES.clone();
        }
    }
}

