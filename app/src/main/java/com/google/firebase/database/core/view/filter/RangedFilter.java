/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core.view.filter;

import com.google.firebase.database.core.Path;
import com.google.firebase.database.core.view.QueryParams;
import com.google.firebase.database.core.view.filter.ChildChangeAccumulator;
import com.google.firebase.database.core.view.filter.IndexedFilter;
import com.google.firebase.database.core.view.filter.NodeFilter;
import com.google.firebase.database.snapshot.ChildKey;
import com.google.firebase.database.snapshot.EmptyNode;
import com.google.firebase.database.snapshot.Index;
import com.google.firebase.database.snapshot.IndexedNode;
import com.google.firebase.database.snapshot.NamedNode;
import com.google.firebase.database.snapshot.Node;
import com.google.firebase.database.snapshot.PriorityUtilities;
import java.util.Iterator;

public class RangedFilter
implements NodeFilter {
    private final NamedNode endPost;
    private final Index index;
    private final IndexedFilter indexedFilter;
    private final NamedNode startPost;

    public RangedFilter(QueryParams queryParams) {
        this.indexedFilter = new IndexedFilter(queryParams.getIndex());
        this.index = queryParams.getIndex();
        this.startPost = RangedFilter.getStartPost(queryParams);
        this.endPost = RangedFilter.getEndPost(queryParams);
    }

    private static NamedNode getEndPost(QueryParams queryParams) {
        if (queryParams.hasEnd()) {
            ChildKey childKey = queryParams.getIndexEndName();
            return queryParams.getIndex().makePost(childKey, queryParams.getIndexEndValue());
        }
        return queryParams.getIndex().maxPost();
    }

    private static NamedNode getStartPost(QueryParams queryParams) {
        if (queryParams.hasStart()) {
            ChildKey childKey = queryParams.getIndexStartName();
            return queryParams.getIndex().makePost(childKey, queryParams.getIndexStartValue());
        }
        return queryParams.getIndex().minPost();
    }

    @Override
    public boolean filtersNodes() {
        return true;
    }

    public NamedNode getEndPost() {
        return this.endPost;
    }

    @Override
    public Index getIndex() {
        return this.index;
    }

    @Override
    public NodeFilter getIndexedFilter() {
        return this.indexedFilter;
    }

    public NamedNode getStartPost() {
        return this.startPost;
    }

    public boolean matches(NamedNode namedNode) {
        return this.index.compare(this.getStartPost(), namedNode) <= 0 && this.index.compare(namedNode, this.getEndPost()) <= 0;
    }

    @Override
    public IndexedNode updateChild(IndexedNode indexedNode, ChildKey childKey, Node node, Path path, NodeFilter.CompleteChildSource completeChildSource, ChildChangeAccumulator childChangeAccumulator) {
        Node node2 = node;
        if (!this.matches(new NamedNode(childKey, node))) {
            node2 = EmptyNode.Empty();
        }
        return this.indexedFilter.updateChild(indexedNode, childKey, node2, path, completeChildSource, childChangeAccumulator);
    }

    @Override
    public IndexedNode updateFullNode(IndexedNode indexedNode, IndexedNode indexedNode2, ChildChangeAccumulator childChangeAccumulator) {
        IndexedNode indexedNode3;
        if (indexedNode2.getNode().isLeafNode()) {
            indexedNode3 = IndexedNode.from(EmptyNode.Empty(), this.index);
        } else {
            indexedNode3 = indexedNode2.updatePriority(PriorityUtilities.NullPriority());
            Iterator<NamedNode> iterator2 = indexedNode2.iterator();
            indexedNode2 = indexedNode3;
            while (true) {
                indexedNode3 = indexedNode2;
                if (!iterator2.hasNext()) break;
                NamedNode namedNode = iterator2.next();
                indexedNode3 = indexedNode2;
                if (!this.matches(namedNode)) {
                    indexedNode3 = indexedNode2.updateChild(namedNode.getName(), EmptyNode.Empty());
                }
                indexedNode2 = indexedNode3;
            }
        }
        return this.indexedFilter.updateFullNode(indexedNode, indexedNode3, childChangeAccumulator);
    }

    @Override
    public IndexedNode updatePriority(IndexedNode indexedNode, Node node) {
        return indexedNode;
    }
}

