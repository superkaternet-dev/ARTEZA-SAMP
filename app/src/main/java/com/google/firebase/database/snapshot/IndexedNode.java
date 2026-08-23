/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.snapshot;

import com.google.android.gms.common.internal.Objects;
import com.google.firebase.database.collection.ImmutableSortedSet;
import com.google.firebase.database.snapshot.ChildKey;
import com.google.firebase.database.snapshot.ChildrenNode;
import com.google.firebase.database.snapshot.Index;
import com.google.firebase.database.snapshot.KeyIndex;
import com.google.firebase.database.snapshot.NamedNode;
import com.google.firebase.database.snapshot.Node;
import com.google.firebase.database.snapshot.PriorityIndex;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class IndexedNode
implements Iterable<NamedNode> {
    private static final ImmutableSortedSet<NamedNode> FALLBACK_INDEX = new ImmutableSortedSet(Collections.emptyList(), null);
    private final Index index;
    private ImmutableSortedSet<NamedNode> indexed;
    private final Node node;

    private IndexedNode(Node node, Index index) {
        this.index = index;
        this.node = node;
        this.indexed = null;
    }

    private IndexedNode(Node node, Index index, ImmutableSortedSet<NamedNode> immutableSortedSet) {
        this.index = index;
        this.node = node;
        this.indexed = immutableSortedSet;
    }

    private void ensureIndexed() {
        if (this.indexed == null) {
            if (this.index.equals(KeyIndex.getInstance())) {
                this.indexed = FALLBACK_INDEX;
            } else {
                ArrayList<NamedNode> arrayList = new ArrayList<NamedNode>();
                boolean bl = false;
                for (NamedNode namedNode : this.node) {
                    bl = bl || this.index.isDefinedOn(namedNode.getNode());
                    arrayList.add(new NamedNode(namedNode.getName(), namedNode.getNode()));
                }
                this.indexed = bl ? new ImmutableSortedSet<NamedNode>(arrayList, this.index) : FALLBACK_INDEX;
            }
        }
    }

    public static IndexedNode from(Node node) {
        return new IndexedNode(node, PriorityIndex.getInstance());
    }

    public static IndexedNode from(Node node, Index index) {
        return new IndexedNode(node, index);
    }

    public NamedNode getFirstChild() {
        if (!(this.node instanceof ChildrenNode)) {
            return null;
        }
        this.ensureIndexed();
        if (Objects.equal(this.indexed, FALLBACK_INDEX)) {
            ChildKey childKey = ((ChildrenNode)this.node).getFirstChildKey();
            return new NamedNode(childKey, this.node.getImmediateChild(childKey));
        }
        return this.indexed.getMinEntry();
    }

    public NamedNode getLastChild() {
        if (!(this.node instanceof ChildrenNode)) {
            return null;
        }
        this.ensureIndexed();
        if (Objects.equal(this.indexed, FALLBACK_INDEX)) {
            ChildKey childKey = ((ChildrenNode)this.node).getLastChildKey();
            return new NamedNode(childKey, this.node.getImmediateChild(childKey));
        }
        return this.indexed.getMaxEntry();
    }

    public Node getNode() {
        return this.node;
    }

    public ChildKey getPredecessorChildName(ChildKey object, Node node, Index index) {
        if (!this.index.equals(KeyIndex.getInstance()) && !this.index.equals(index)) {
            throw new IllegalArgumentException("Index not available in IndexedNode!");
        }
        this.ensureIndexed();
        if (Objects.equal(this.indexed, FALLBACK_INDEX)) {
            return this.node.getPredecessorChildKey((ChildKey)object);
        }
        object = (object = this.indexed.getPredecessorEntry(new NamedNode((ChildKey)object, node))) != null ? ((NamedNode)object).getName() : null;
        return object;
    }

    public boolean hasIndex(Index index) {
        boolean bl = this.index == index;
        return bl;
    }

    @Override
    public Iterator<NamedNode> iterator() {
        this.ensureIndexed();
        if (Objects.equal(this.indexed, FALLBACK_INDEX)) {
            return this.node.iterator();
        }
        return this.indexed.iterator();
    }

    public Iterator<NamedNode> reverseIterator() {
        this.ensureIndexed();
        if (Objects.equal(this.indexed, FALLBACK_INDEX)) {
            return this.node.reverseIterator();
        }
        return this.indexed.reverseIterator();
    }

    public IndexedNode updateChild(ChildKey childKey, Node node) {
        Node node2 = this.node.updateImmediateChild(childKey, node);
        ImmutableSortedSet<NamedNode> immutableSortedSet = this.indexed;
        Iterable<NamedNode> iterable = FALLBACK_INDEX;
        if (Objects.equal(immutableSortedSet, iterable) && !this.index.isDefinedOn(node)) {
            return new IndexedNode(node2, this.index, (ImmutableSortedSet<NamedNode>)iterable);
        }
        immutableSortedSet = this.indexed;
        if (immutableSortedSet != null && !Objects.equal(immutableSortedSet, iterable)) {
            iterable = this.node.getImmediateChild(childKey);
            immutableSortedSet = this.indexed.remove(new NamedNode(childKey, (Node)iterable));
            iterable = immutableSortedSet;
            if (!node.isEmpty()) {
                iterable = immutableSortedSet.insert(new NamedNode(childKey, node));
            }
            return new IndexedNode(node2, this.index, (ImmutableSortedSet<NamedNode>)iterable);
        }
        return new IndexedNode(node2, this.index, null);
    }

    public IndexedNode updatePriority(Node node) {
        return new IndexedNode(this.node.updatePriority(node), this.index, this.indexed);
    }
}

