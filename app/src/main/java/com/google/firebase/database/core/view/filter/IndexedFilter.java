/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core.view.filter;

import com.google.firebase.database.core.Path;
import com.google.firebase.database.core.utilities.Utilities;
import com.google.firebase.database.core.view.Change;
import com.google.firebase.database.core.view.filter.ChildChangeAccumulator;
import com.google.firebase.database.core.view.filter.NodeFilter;
import com.google.firebase.database.snapshot.ChildKey;
import com.google.firebase.database.snapshot.Index;
import com.google.firebase.database.snapshot.IndexedNode;
import com.google.firebase.database.snapshot.NamedNode;
import com.google.firebase.database.snapshot.Node;

public class IndexedFilter
implements NodeFilter {
    private final Index index;

    public IndexedFilter(Index index) {
        this.index = index;
    }

    @Override
    public boolean filtersNodes() {
        return false;
    }

    @Override
    public Index getIndex() {
        return this.index;
    }

    @Override
    public NodeFilter getIndexedFilter() {
        return this;
    }

    @Override
    public IndexedNode updateChild(IndexedNode indexedNode, ChildKey childKey, Node node, Path path, NodeFilter.CompleteChildSource object, ChildChangeAccumulator childChangeAccumulator) {
        Utilities.hardAssert(indexedNode.hasIndex(this.index), "The index must match the filter");
        object = indexedNode.getNode();
        Node node2 = object.getImmediateChild(childKey);
        if (node2.getChild(path).equals(node.getChild(path)) && node2.isEmpty() == node.isEmpty()) {
            return indexedNode;
        }
        if (childChangeAccumulator != null) {
            if (node.isEmpty()) {
                if (object.hasChild(childKey)) {
                    childChangeAccumulator.trackChildChange(Change.childRemovedChange(childKey, node2));
                } else {
                    Utilities.hardAssert(object.isLeafNode(), "A child remove without an old child only makes sense on a leaf node");
                }
            } else if (node2.isEmpty()) {
                childChangeAccumulator.trackChildChange(Change.childAddedChange(childKey, node));
            } else {
                childChangeAccumulator.trackChildChange(Change.childChangedChange(childKey, node, node2));
            }
        }
        if (object.isLeafNode() && node.isEmpty()) {
            return indexedNode;
        }
        return indexedNode.updateChild(childKey, node);
    }

    @Override
    public IndexedNode updateFullNode(IndexedNode indexedNode, IndexedNode indexedNode2, ChildChangeAccumulator childChangeAccumulator) {
        Utilities.hardAssert(indexedNode2.hasIndex(this.index), "Can't use IndexedNode that doesn't have filter's index");
        if (childChangeAccumulator != null) {
            for (NamedNode namedNode : indexedNode.getNode()) {
                if (indexedNode2.getNode().hasChild(namedNode.getName())) continue;
                childChangeAccumulator.trackChildChange(Change.childRemovedChange(namedNode.getName(), namedNode.getNode()));
            }
            if (!indexedNode2.getNode().isLeafNode()) {
                for (NamedNode namedNode : indexedNode2.getNode()) {
                    if (indexedNode.getNode().hasChild(namedNode.getName())) {
                        Node node = indexedNode.getNode().getImmediateChild(namedNode.getName());
                        if (node.equals(namedNode.getNode())) continue;
                        childChangeAccumulator.trackChildChange(Change.childChangedChange(namedNode.getName(), namedNode.getNode(), node));
                        continue;
                    }
                    childChangeAccumulator.trackChildChange(Change.childAddedChange(namedNode.getName(), namedNode.getNode()));
                }
            }
        }
        return indexedNode2;
    }

    @Override
    public IndexedNode updatePriority(IndexedNode indexedNode, Node node) {
        if (indexedNode.getNode().isEmpty()) {
            return indexedNode;
        }
        return indexedNode.updatePriority(node);
    }
}

