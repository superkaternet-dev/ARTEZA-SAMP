/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.snapshot;

import com.google.firebase.database.core.Path;
import com.google.firebase.database.snapshot.ChildKey;
import com.google.firebase.database.snapshot.ChildrenNode;
import com.google.firebase.database.snapshot.EmptyNode;
import com.google.firebase.database.snapshot.NamedNode;
import java.util.Iterator;

public interface Node
extends Comparable<Node>,
Iterable<NamedNode> {
    public static final ChildrenNode MAX_NODE = new ChildrenNode(){

        @Override
        public int compareTo(Node node) {
            int n = node == this ? 0 : 1;
            return n;
        }

        @Override
        public boolean equals(Object object) {
            boolean bl = object == this;
            return bl;
        }

        @Override
        public Node getImmediateChild(ChildKey childKey) {
            if (childKey.isPriorityChildName()) {
                return this.getPriority();
            }
            return EmptyNode.Empty();
        }

        @Override
        public Node getPriority() {
            return this;
        }

        @Override
        public boolean hasChild(ChildKey childKey) {
            return false;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public String toString() {
            return "<Max Node>";
        }
    };

    public Node getChild(Path var1);

    public int getChildCount();

    public String getHash();

    public String getHashRepresentation(HashVersion var1);

    public Node getImmediateChild(ChildKey var1);

    public ChildKey getPredecessorChildKey(ChildKey var1);

    public Node getPriority();

    public ChildKey getSuccessorChildKey(ChildKey var1);

    public Object getValue();

    public Object getValue(boolean var1);

    public boolean hasChild(ChildKey var1);

    public boolean isEmpty();

    public boolean isLeafNode();

    public Iterator<NamedNode> reverseIterator();

    public Node updateChild(Path var1, Node var2);

    public Node updateImmediateChild(ChildKey var1, Node var2);

    public Node updatePriority(Node var1);

    public static final class HashVersion
    extends Enum<HashVersion> {
        private static final HashVersion[] $VALUES;
        public static final /* enum */ HashVersion V1;
        public static final /* enum */ HashVersion V2;

        static {
            HashVersion hashVersion;
            HashVersion hashVersion2;
            V1 = hashVersion2 = new HashVersion();
            V2 = hashVersion = new HashVersion();
            $VALUES = new HashVersion[]{hashVersion2, hashVersion};
        }

        public static HashVersion valueOf(String string2) {
            return Enum.valueOf(HashVersion.class, string2);
        }

        public static HashVersion[] values() {
            return (HashVersion[])$VALUES.clone();
        }
    }
}

