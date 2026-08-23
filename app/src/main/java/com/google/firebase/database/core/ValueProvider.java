/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core;

import com.google.firebase.database.core.Path;
import com.google.firebase.database.core.SyncTree;
import com.google.firebase.database.snapshot.ChildKey;
import com.google.firebase.database.snapshot.Node;
import java.util.ArrayList;

abstract class ValueProvider {
    ValueProvider() {
    }

    public abstract ValueProvider getImmediateChild(ChildKey var1);

    public abstract Node node();

    public static class DeferredValueProvider
    extends ValueProvider {
        private final Path path;
        private final SyncTree syncTree;

        DeferredValueProvider(SyncTree syncTree, Path path) {
            this.syncTree = syncTree;
            this.path = path;
        }

        @Override
        public ValueProvider getImmediateChild(ChildKey comparable) {
            comparable = this.path.child((ChildKey)comparable);
            return new DeferredValueProvider(this.syncTree, (Path)comparable);
        }

        @Override
        public Node node() {
            return this.syncTree.calcCompleteEventCache(this.path, new ArrayList<Long>());
        }
    }

    public static class ExistingValueProvider
    extends ValueProvider {
        private final Node node;

        ExistingValueProvider(Node node) {
            this.node = node;
        }

        @Override
        public ValueProvider getImmediateChild(ChildKey childKey) {
            return new ExistingValueProvider(this.node.getImmediateChild(childKey));
        }

        @Override
        public Node node() {
            return this.node;
        }
    }
}

