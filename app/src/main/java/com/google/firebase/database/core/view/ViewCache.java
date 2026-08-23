/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core.view;

import com.google.firebase.database.core.view.CacheNode;
import com.google.firebase.database.snapshot.IndexedNode;
import com.google.firebase.database.snapshot.Node;

public class ViewCache {
    private final CacheNode eventSnap;
    private final CacheNode serverSnap;

    public ViewCache(CacheNode cacheNode, CacheNode cacheNode2) {
        this.eventSnap = cacheNode;
        this.serverSnap = cacheNode2;
    }

    public Node getCompleteEventSnap() {
        Node node = this.eventSnap.isFullyInitialized() ? this.eventSnap.getNode() : null;
        return node;
    }

    public Node getCompleteServerSnap() {
        Node node = this.serverSnap.isFullyInitialized() ? this.serverSnap.getNode() : null;
        return node;
    }

    public CacheNode getEventCache() {
        return this.eventSnap;
    }

    public CacheNode getServerCache() {
        return this.serverSnap;
    }

    public ViewCache updateEventSnap(IndexedNode indexedNode, boolean bl, boolean bl2) {
        return new ViewCache(new CacheNode(indexedNode, bl, bl2), this.serverSnap);
    }

    public ViewCache updateServerSnap(IndexedNode indexedNode, boolean bl, boolean bl2) {
        return new ViewCache(this.eventSnap, new CacheNode(indexedNode, bl, bl2));
    }
}

