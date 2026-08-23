/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.snapshot;

import com.google.firebase.database.collection.ImmutableSortedMap;
import com.google.firebase.database.collection.LLRBNode;
import com.google.firebase.database.core.Path;
import com.google.firebase.database.core.utilities.Utilities;
import com.google.firebase.database.snapshot.ChildKey;
import com.google.firebase.database.snapshot.EmptyNode;
import com.google.firebase.database.snapshot.NamedNode;
import com.google.firebase.database.snapshot.Node;
import com.google.firebase.database.snapshot.PriorityIndex;
import com.google.firebase.database.snapshot.PriorityUtilities;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChildrenNode
implements Node {
    public static Comparator<ChildKey> NAME_ONLY_COMPARATOR = new Comparator<ChildKey>(){

        @Override
        public int compare(ChildKey childKey, ChildKey childKey2) {
            return childKey.compareTo(childKey2);
        }
    };
    private final ImmutableSortedMap<ChildKey, Node> children;
    private String lazyHash = null;
    private final Node priority;

    protected ChildrenNode() {
        this.children = ImmutableSortedMap.Builder.emptyMap(NAME_ONLY_COMPARATOR);
        this.priority = PriorityUtilities.NullPriority();
    }

    protected ChildrenNode(ImmutableSortedMap<ChildKey, Node> immutableSortedMap, Node node) {
        if (immutableSortedMap.isEmpty() && !node.isEmpty()) {
            throw new IllegalArgumentException("Can't create empty ChildrenNode with priority!");
        }
        this.priority = node;
        this.children = immutableSortedMap;
    }

    private static void addIndentation(StringBuilder stringBuilder, int n) {
        for (int i = 0; i < n; ++i) {
            stringBuilder.append(" ");
        }
    }

    private void toString(StringBuilder stringBuilder, int n) {
        if (this.children.isEmpty() && this.priority.isEmpty()) {
            stringBuilder.append("{ }");
        } else {
            stringBuilder.append("{\n");
            for (Map.Entry<ChildKey, Node> entry : this.children) {
                ChildrenNode.addIndentation(stringBuilder, n + 2);
                stringBuilder.append(entry.getKey().asString());
                stringBuilder.append("=");
                if (entry.getValue() instanceof ChildrenNode) {
                    ((ChildrenNode)entry.getValue()).toString(stringBuilder, n + 2);
                } else {
                    stringBuilder.append(entry.getValue().toString());
                }
                stringBuilder.append("\n");
            }
            if (!this.priority.isEmpty()) {
                ChildrenNode.addIndentation(stringBuilder, n + 2);
                stringBuilder.append(".priority=");
                stringBuilder.append(this.priority.toString());
                stringBuilder.append("\n");
            }
            ChildrenNode.addIndentation(stringBuilder, n);
            stringBuilder.append("}");
        }
    }

    @Override
    public int compareTo(Node node) {
        if (this.isEmpty()) {
            if (node.isEmpty()) {
                return 0;
            }
            return -1;
        }
        if (node.isLeafNode()) {
            return 1;
        }
        if (node.isEmpty()) {
            return 1;
        }
        if (node == Node.MAX_NODE) {
            return -1;
        }
        return 0;
    }

    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object == this) {
            return true;
        }
        if (!(object instanceof ChildrenNode)) {
            return false;
        }
        Object object2 = (ChildrenNode)object;
        if (!this.getPriority().equals(((ChildrenNode)object2).getPriority())) {
            return false;
        }
        if (this.children.size() != ((ChildrenNode)object2).children.size()) {
            return false;
        }
        object = this.children.iterator();
        Iterator<Map.Entry<ChildKey, Node>> iterator2 = ((ChildrenNode)object2).children.iterator();
        while (object.hasNext() && iterator2.hasNext()) {
            object2 = object.next();
            Map.Entry<ChildKey, Node> entry = iterator2.next();
            if (((ChildKey)object2.getKey()).equals(entry.getKey()) && ((Node)object2.getValue()).equals(entry.getValue())) continue;
            return false;
        }
        if (!object.hasNext() && !iterator2.hasNext()) {
            return true;
        }
        object = new IllegalStateException("Something went wrong internally.");
        throw object;
    }

    public void forEachChild(ChildVisitor childVisitor) {
        this.forEachChild(childVisitor, false);
    }

    public void forEachChild(ChildVisitor childVisitor, boolean bl) {
        if (bl && !this.getPriority().isEmpty()) {
            this.children.inOrderTraversal(new LLRBNode.NodeVisitor<ChildKey, Node>(this, childVisitor){
                boolean passedPriorityKey;
                final ChildrenNode this$0;
                final ChildVisitor val$visitor;
                {
                    this.this$0 = childrenNode;
                    this.val$visitor = childVisitor;
                    this.passedPriorityKey = false;
                }

                @Override
                public void visitEntry(ChildKey childKey, Node node) {
                    if (!this.passedPriorityKey && childKey.compareTo(ChildKey.getPriorityKey()) > 0) {
                        this.passedPriorityKey = true;
                        this.val$visitor.visitChild(ChildKey.getPriorityKey(), this.this$0.getPriority());
                    }
                    this.val$visitor.visitChild(childKey, node);
                }
            });
        } else {
            this.children.inOrderTraversal(childVisitor);
        }
    }

    @Override
    public Node getChild(Path path) {
        ChildKey childKey = path.getFront();
        if (childKey == null) {
            return this;
        }
        return this.getImmediateChild(childKey).getChild(path.popFront());
    }

    @Override
    public int getChildCount() {
        return this.children.size();
    }

    public ChildKey getFirstChildKey() {
        return this.children.getMinKey();
    }

    @Override
    public String getHash() {
        if (this.lazyHash == null) {
            String string2 = this.getHashRepresentation(Node.HashVersion.V1);
            string2 = string2.isEmpty() ? "" : Utilities.sha1HexDigest(string2);
            this.lazyHash = string2;
        }
        return this.lazyHash;
    }

    @Override
    public String getHashRepresentation(Node.HashVersion object) {
        if (object == Node.HashVersion.V1) {
            Object object22;
            object = new StringBuilder();
            if (!this.priority.isEmpty()) {
                ((StringBuilder)object).append("priority:");
                ((StringBuilder)object).append(this.priority.getHashRepresentation(Node.HashVersion.V1));
                ((StringBuilder)object).append(":");
            }
            Object object3 = new ArrayList<Object>();
            boolean bl = false;
            for (Object object22 : this) {
                object3.add(object22);
                if (!bl && ((NamedNode)object22).getNode().getPriority().isEmpty()) {
                    bl = false;
                    continue;
                }
                bl = true;
            }
            if (bl) {
                Collections.sort(object3, PriorityIndex.getInstance());
            }
            object22 = object3.iterator();
            while (object22.hasNext()) {
                NamedNode namedNode = (NamedNode)object22.next();
                object3 = namedNode.getNode().getHash();
                if (((String)object3).equals("")) continue;
                ((StringBuilder)object).append(":");
                ((StringBuilder)object).append(namedNode.getName().asString());
                ((StringBuilder)object).append(":");
                ((StringBuilder)object).append((String)object3);
            }
            return ((StringBuilder)object).toString();
        }
        object = new IllegalArgumentException("Hashes on children nodes only supported for V1");
        throw object;
    }

    @Override
    public Node getImmediateChild(ChildKey childKey) {
        if (childKey.isPriorityChildName() && !this.priority.isEmpty()) {
            return this.priority;
        }
        if (this.children.containsKey(childKey)) {
            return this.children.get(childKey);
        }
        return EmptyNode.Empty();
    }

    public ChildKey getLastChildKey() {
        return this.children.getMaxKey();
    }

    @Override
    public ChildKey getPredecessorChildKey(ChildKey childKey) {
        return this.children.getPredecessorKey(childKey);
    }

    @Override
    public Node getPriority() {
        return this.priority;
    }

    @Override
    public ChildKey getSuccessorChildKey(ChildKey childKey) {
        return this.children.getSuccessorKey(childKey);
    }

    @Override
    public Object getValue() {
        return this.getValue(false);
    }

    @Override
    public Object getValue(boolean bl) {
        int n;
        if (this.isEmpty()) {
            return null;
        }
        int n2 = 0;
        int n3 = 0;
        int n4 = 1;
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        for (Map.Entry<ChildKey, Node> entry : this.children) {
            String string2 = entry.getKey().asString();
            hashMap.put(string2, entry.getValue().getValue(bl));
            ++n2;
            int n5 = n3;
            n = n4;
            if (n4 != 0) {
                if (string2.length() > 1 && string2.charAt(0) == '0') {
                    n = 0;
                    n5 = n3;
                } else {
                    Integer n6 = Utilities.tryParseInt(string2);
                    if (n6 != null && n6 >= 0) {
                        n5 = n3;
                        n = n4;
                        if (n6 > n3) {
                            n5 = n6;
                            n = n4;
                        }
                    } else {
                        n = 0;
                        n5 = n3;
                    }
                }
            }
            n3 = n5;
            n4 = n;
        }
        if (!bl && n4 != 0 && n3 < n2 * 2) {
            ArrayList arrayList = new ArrayList(n3 + 1);
            for (n = 0; n <= n3; ++n) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("");
                stringBuilder.append(n);
                arrayList.add(hashMap.get(stringBuilder.toString()));
            }
            return arrayList;
        }
        if (bl && !this.priority.isEmpty()) {
            hashMap.put(".priority", this.priority.getValue());
        }
        return hashMap;
    }

    @Override
    public boolean hasChild(ChildKey childKey) {
        return this.getImmediateChild(childKey).isEmpty() ^ true;
    }

    public int hashCode() {
        int n = 0;
        for (NamedNode namedNode : this) {
            n = (n * 31 + namedNode.getName().hashCode()) * 17 + namedNode.getNode().hashCode();
        }
        return n;
    }

    @Override
    public boolean isEmpty() {
        return this.children.isEmpty();
    }

    @Override
    public boolean isLeafNode() {
        return false;
    }

    @Override
    public Iterator<NamedNode> iterator() {
        return new NamedNodeIterator(this.children.iterator());
    }

    @Override
    public Iterator<NamedNode> reverseIterator() {
        return new NamedNodeIterator(this.children.reverseIterator());
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        this.toString(stringBuilder, 0);
        return stringBuilder.toString();
    }

    @Override
    public Node updateChild(Path path, Node node) {
        ChildKey childKey = path.getFront();
        if (childKey == null) {
            return node;
        }
        if (childKey.isPriorityChildName()) {
            Utilities.hardAssert(PriorityUtilities.isValidPriority(node));
            return this.updatePriority(node);
        }
        return this.updateImmediateChild(childKey, this.getImmediateChild(childKey).updateChild(path.popFront(), node));
    }

    @Override
    public Node updateImmediateChild(ChildKey childKey, Node node) {
        ImmutableSortedMap<ChildKey, Node> immutableSortedMap;
        if (childKey.isPriorityChildName()) {
            return this.updatePriority(node);
        }
        ImmutableSortedMap<ChildKey, Node> immutableSortedMap2 = immutableSortedMap = this.children;
        if (immutableSortedMap.containsKey(childKey)) {
            immutableSortedMap2 = immutableSortedMap.remove(childKey);
        }
        immutableSortedMap = immutableSortedMap2;
        if (!node.isEmpty()) {
            immutableSortedMap = immutableSortedMap2.insert(childKey, node);
        }
        if (immutableSortedMap.isEmpty()) {
            return EmptyNode.Empty();
        }
        return new ChildrenNode(immutableSortedMap, this.priority);
    }

    @Override
    public Node updatePriority(Node node) {
        if (this.children.isEmpty()) {
            return EmptyNode.Empty();
        }
        return new ChildrenNode(this.children, node);
    }

    public static abstract class ChildVisitor
    extends LLRBNode.NodeVisitor<ChildKey, Node> {
        public abstract void visitChild(ChildKey var1, Node var2);

        @Override
        public void visitEntry(ChildKey childKey, Node node) {
            this.visitChild(childKey, node);
        }
    }

    private static class NamedNodeIterator
    implements Iterator<NamedNode> {
        private final Iterator<Map.Entry<ChildKey, Node>> iterator;

        public NamedNodeIterator(Iterator<Map.Entry<ChildKey, Node>> iterator2) {
            this.iterator = iterator2;
        }

        @Override
        public boolean hasNext() {
            return this.iterator.hasNext();
        }

        @Override
        public NamedNode next() {
            Map.Entry<ChildKey, Node> entry = this.iterator.next();
            return new NamedNode(entry.getKey(), entry.getValue());
        }

        @Override
        public void remove() {
            this.iterator.remove();
        }
    }
}

