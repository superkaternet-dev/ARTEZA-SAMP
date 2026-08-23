/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core.view;

import com.google.firebase.database.core.Path;
import com.google.firebase.database.snapshot.ChildKey;
import com.google.firebase.database.snapshot.IndexedNode;
import com.google.firebase.database.snapshot.Node;

public class CacheNode {
    private final boolean filtered;
    private final boolean fullyInitialized;
    private final IndexedNode indexedNode;

    public CacheNode(IndexedNode indexedNode, boolean bl, boolean bl2) {
        this.indexedNode = indexedNode;
        this.fullyInitialized = bl;
        this.filtered = bl2;
    }

    public IndexedNode getIndexedNode() {
        return this.indexedNode;
    }

    public Node getNode() {
        return this.indexedNode.getNode();
    }

    public boolean isCompleteForChild(ChildKey childKey) {
        boolean bl = this.isFullyInitialized() && !this.filtered || this.indexedNode.getNode().hasChild(childKey);
        return bl;
    }

    public boolean isCompleteForPath(Path path) {
        if (path.isEmpty()) {
            boolean bl = this.isFullyInitialized() && !this.filtered;
            return bl;
        }
        return this.isCompleteForChild(path.getFront());
    }

    public boolean isFiltered() {
        return this.filtered;
    }

    public boolean isFullyInitialized() {
        return this.fullyInitialized;
    }
}

