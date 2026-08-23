/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core;

import com.google.firebase.database.core.Path;
import com.google.firebase.database.snapshot.ChildKey;
import com.google.firebase.database.snapshot.ChildrenNode;
import com.google.firebase.database.snapshot.Node;
import java.util.HashMap;
import java.util.Map;

class SparseSnapshotTree {
    private Map<ChildKey, SparseSnapshotTree> children = null;
    private Node value = null;

    public void forEachChild(SparseSnapshotChildVisitor sparseSnapshotChildVisitor) {
        Map<ChildKey, SparseSnapshotTree> map2 = this.children;
        if (map2 != null) {
            for (Map.Entry<ChildKey, SparseSnapshotTree> entry : map2.entrySet()) {
                sparseSnapshotChildVisitor.visitChild(entry.getKey(), entry.getValue());
            }
        }
    }

    public void forEachTree(Path path, SparseSnapshotTreeVisitor sparseSnapshotTreeVisitor) {
        Node node = this.value;
        if (node != null) {
            sparseSnapshotTreeVisitor.visitTree(path, node);
        } else {
            this.forEachChild(new SparseSnapshotChildVisitor(this, path, sparseSnapshotTreeVisitor){
                final SparseSnapshotTree this$0;
                final Path val$prefixPath;
                final SparseSnapshotTreeVisitor val$visitor;
                {
                    this.this$0 = sparseSnapshotTree;
                    this.val$prefixPath = path;
                    this.val$visitor = sparseSnapshotTreeVisitor;
                }

                @Override
                public void visitChild(ChildKey childKey, SparseSnapshotTree sparseSnapshotTree) {
                    sparseSnapshotTree.forEachTree(this.val$prefixPath.child(childKey), this.val$visitor);
                }
            });
        }
    }

    public boolean forget(Path path) {
        if (path.isEmpty()) {
            this.value = null;
            this.children = null;
            return true;
        }
        Comparable<Node> comparable = this.value;
        if (comparable != null) {
            if (comparable.isLeafNode()) {
                return false;
            }
            comparable = (ChildrenNode)this.value;
            this.value = null;
            ((ChildrenNode)comparable).forEachChild(new ChildrenNode.ChildVisitor(this, path){
                final SparseSnapshotTree this$0;
                final Path val$path;
                {
                    this.this$0 = sparseSnapshotTree;
                    this.val$path = path;
                }

                @Override
                public void visitChild(ChildKey childKey, Node node) {
                    this.this$0.remember(this.val$path.child(childKey), node);
                }
            });
            return this.forget(path);
        }
        if (this.children != null) {
            comparable = path.getFront();
            path = path.popFront();
            if (this.children.containsKey(comparable) && this.children.get(comparable).forget(path)) {
                this.children.remove(comparable);
            }
            if (this.children.isEmpty()) {
                this.children = null;
                return true;
            }
            return false;
        }
        return true;
    }

    public void remember(Path path, Node node) {
        if (path.isEmpty()) {
            this.value = node;
            this.children = null;
        } else {
            Comparable<Node> comparable = this.value;
            if (comparable != null) {
                this.value = comparable.updateChild(path, node);
            } else {
                if (this.children == null) {
                    this.children = new HashMap<ChildKey, SparseSnapshotTree>();
                }
                if (!this.children.containsKey(comparable = path.getFront())) {
                    this.children.put((ChildKey)comparable, new SparseSnapshotTree());
                }
                this.children.get(comparable).remember(path.popFront(), node);
            }
        }
    }

    public static interface SparseSnapshotChildVisitor {
        public void visitChild(ChildKey var1, SparseSnapshotTree var2);
    }

    public static interface SparseSnapshotTreeVisitor {
        public void visitTree(Path var1, Node var2);
    }
}

