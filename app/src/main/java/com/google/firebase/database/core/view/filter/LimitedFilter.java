/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core.view.filter;

import com.google.firebase.database.core.Path;
import com.google.firebase.database.core.utilities.Utilities;
import com.google.firebase.database.core.view.Change;
import com.google.firebase.database.core.view.QueryParams;
import com.google.firebase.database.core.view.filter.ChildChangeAccumulator;
import com.google.firebase.database.core.view.filter.NodeFilter;
import com.google.firebase.database.core.view.filter.RangedFilter;
import com.google.firebase.database.snapshot.ChildKey;
import com.google.firebase.database.snapshot.EmptyNode;
import com.google.firebase.database.snapshot.Index;
import com.google.firebase.database.snapshot.IndexedNode;
import com.google.firebase.database.snapshot.NamedNode;
import com.google.firebase.database.snapshot.Node;
import com.google.firebase.database.snapshot.PriorityUtilities;
import java.util.Iterator;

public class LimitedFilter
implements NodeFilter {
    private final Index index;
    private final int limit;
    private final RangedFilter rangedFilter;
    private final boolean reverse;

    public LimitedFilter(QueryParams queryParams) {
        this.rangedFilter = new RangedFilter(queryParams);
        this.index = queryParams.getIndex();
        this.limit = queryParams.getLimit();
        this.reverse = queryParams.isViewFromLeft() ^ true;
    }

    private IndexedNode fullLimitUpdateChild(IndexedNode indexedNode, ChildKey childKey, Node node, NodeFilter.CompleteChildSource completeChildSource, ChildChangeAccumulator childChangeAccumulator) {
        boolean bl = indexedNode.getNode().getChildCount() == this.limit;
        Utilities.hardAssert(bl);
        NamedNode namedNode = new NamedNode(childKey, node);
        NamedNode namedNode2 = this.reverse ? indexedNode.getFirstChild() : indexedNode.getLastChild();
        bl = this.rangedFilter.matches(namedNode);
        if (indexedNode.getNode().hasChild(childKey)) {
            Node node2 = indexedNode.getNode().getImmediateChild(childKey);
            namedNode2 = completeChildSource.getChildAfterChild(this.index, namedNode2, this.reverse);
            while (namedNode2 != null && (namedNode2.getName().equals(childKey) || indexedNode.getNode().hasChild(namedNode2.getName()))) {
                namedNode2 = completeChildSource.getChildAfterChild(this.index, namedNode2, this.reverse);
            }
            int n = namedNode2 == null ? 1 : this.index.compare(namedNode2, namedNode, this.reverse);
            n = bl && !node.isEmpty() && n >= 0 ? 1 : 0;
            if (n != 0) {
                if (childChangeAccumulator != null) {
                    childChangeAccumulator.trackChildChange(Change.childChangedChange(childKey, node, node2));
                }
                return indexedNode.updateChild(childKey, node);
            }
            if (childChangeAccumulator != null) {
                childChangeAccumulator.trackChildChange(Change.childRemovedChange(childKey, node2));
            }
            indexedNode = indexedNode.updateChild(childKey, EmptyNode.Empty());
            n = namedNode2 != null && this.rangedFilter.matches(namedNode2) ? 1 : 0;
            if (n != 0) {
                if (childChangeAccumulator != null) {
                    childChangeAccumulator.trackChildChange(Change.childAddedChange(namedNode2.getName(), namedNode2.getNode()));
                }
                return indexedNode.updateChild(namedNode2.getName(), namedNode2.getNode());
            }
            return indexedNode;
        }
        if (node.isEmpty()) {
            return indexedNode;
        }
        if (bl) {
            if (this.index.compare(namedNode2, namedNode, this.reverse) >= 0) {
                if (childChangeAccumulator != null) {
                    childChangeAccumulator.trackChildChange(Change.childRemovedChange(namedNode2.getName(), namedNode2.getNode()));
                    childChangeAccumulator.trackChildChange(Change.childAddedChange(childKey, node));
                }
                return indexedNode.updateChild(childKey, node).updateChild(namedNode2.getName(), EmptyNode.Empty());
            }
            return indexedNode;
        }
        return indexedNode;
    }

    @Override
    public boolean filtersNodes() {
        return true;
    }

    @Override
    public Index getIndex() {
        return this.index;
    }

    @Override
    public NodeFilter getIndexedFilter() {
        return this.rangedFilter.getIndexedFilter();
    }

    @Override
    public IndexedNode updateChild(IndexedNode indexedNode, ChildKey childKey, Node node, Path path, NodeFilter.CompleteChildSource completeChildSource, ChildChangeAccumulator childChangeAccumulator) {
        Node node2 = node;
        if (!this.rangedFilter.matches(new NamedNode(childKey, node))) {
            node2 = EmptyNode.Empty();
        }
        if (indexedNode.getNode().getImmediateChild(childKey).equals(node2)) {
            return indexedNode;
        }
        if (indexedNode.getNode().getChildCount() < this.limit) {
            return this.rangedFilter.getIndexedFilter().updateChild(indexedNode, childKey, node2, path, completeChildSource, childChangeAccumulator);
        }
        return this.fullLimitUpdateChild(indexedNode, childKey, node2, completeChildSource, childChangeAccumulator);
    }

    @Override
    public IndexedNode updateFullNode(IndexedNode indexedNode, IndexedNode indexedNode2, ChildChangeAccumulator childChangeAccumulator) {
        Object object;
        if (!indexedNode2.getNode().isLeafNode() && !indexedNode2.getNode().isEmpty()) {
            int n;
            NamedNode namedNode;
            NamedNode namedNode2;
            Iterator<NamedNode> iterator2;
            object = indexedNode2.updatePriority(PriorityUtilities.NullPriority());
            if (this.reverse) {
                iterator2 = indexedNode2.reverseIterator();
                namedNode2 = this.rangedFilter.getEndPost();
                namedNode = this.rangedFilter.getStartPost();
                n = -1;
            } else {
                iterator2 = indexedNode2.iterator();
                namedNode2 = this.rangedFilter.getStartPost();
                namedNode = this.rangedFilter.getEndPost();
                n = 1;
            }
            int n2 = 0;
            boolean bl = false;
            indexedNode2 = object;
            while (true) {
                object = indexedNode2;
                if (iterator2.hasNext()) {
                    object = iterator2.next();
                    boolean bl2 = bl;
                    if (!bl) {
                        bl2 = bl;
                        if (this.index.compare(namedNode2, object) * n <= 0) {
                            bl2 = true;
                        }
                    }
                    if (bl = bl2 && n2 < this.limit && this.index.compare(object, namedNode) * n <= 0) {
                        ++n2;
                    } else {
                        indexedNode2 = indexedNode2.updateChild(((NamedNode)object).getName(), EmptyNode.Empty());
                    }
                    bl = bl2;
                    continue;
                }
                break;
            }
        } else {
            object = IndexedNode.from(EmptyNode.Empty(), this.index);
        }
        return this.rangedFilter.getIndexedFilter().updateFullNode(indexedNode, (IndexedNode)object, childChangeAccumulator);
    }

    @Override
    public IndexedNode updatePriority(IndexedNode indexedNode, Node node) {
        return indexedNode;
    }
}

