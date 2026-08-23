/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core.utilities;

import com.google.firebase.database.core.Path;
import com.google.firebase.database.core.utilities.TreeNode;
import com.google.firebase.database.core.utilities.Utilities;
import com.google.firebase.database.snapshot.ChildKey;
import java.util.Map;

public class Tree<T> {
    private ChildKey name;
    private TreeNode<T> node;
    private Tree<T> parent;

    public Tree() {
        this(null, null, new TreeNode());
    }

    public Tree(ChildKey childKey, Tree<T> tree, TreeNode<T> treeNode) {
        this.name = childKey;
        this.parent = tree;
        this.node = treeNode;
    }

    private void updateChild(ChildKey childKey, Tree<T> tree) {
        boolean bl = tree.isEmpty();
        boolean bl2 = this.node.children.containsKey(childKey);
        if (bl && bl2) {
            this.node.children.remove(childKey);
            this.updateParents();
        } else if (!bl && !bl2) {
            this.node.children.put(childKey, tree.node);
            this.updateParents();
        }
    }

    private void updateParents() {
        Tree<T> tree = this.parent;
        if (tree != null) {
            super.updateChild(this.name, this);
        }
    }

    public boolean forEachAncestor(TreeFilter<T> treeFilter) {
        return this.forEachAncestor(treeFilter, false);
    }

    public boolean forEachAncestor(TreeFilter<T> treeFilter, boolean bl) {
        Tree<T> tree = bl ? this : this.parent;
        while (tree != null) {
            if (treeFilter.filterTreeNode(tree)) {
                return true;
            }
            tree = tree.parent;
        }
        return false;
    }

    public void forEachChild(TreeVisitor<T> treeVisitor) {
        Object[] objectArray = this.node.children.entrySet().toArray();
        for (int i = 0; i < objectArray.length; ++i) {
            Map.Entry entry = (Map.Entry)objectArray[i];
            treeVisitor.visitTree(new Tree<T>((ChildKey)entry.getKey(), this, (TreeNode)entry.getValue()));
        }
    }

    public void forEachDescendant(TreeVisitor<T> treeVisitor) {
        this.forEachDescendant(treeVisitor, false, false);
    }

    public void forEachDescendant(TreeVisitor<T> treeVisitor, boolean bl) {
        this.forEachDescendant(treeVisitor, bl, false);
    }

    public void forEachDescendant(TreeVisitor<T> treeVisitor, boolean bl, boolean bl2) {
        if (bl && !bl2) {
            treeVisitor.visitTree(this);
        }
        this.forEachChild(new TreeVisitor<T>(this, treeVisitor, bl2){
            final Tree this$0;
            final boolean val$childrenFirst;
            final TreeVisitor val$visitor;
            {
                this.this$0 = tree;
                this.val$visitor = treeVisitor;
                this.val$childrenFirst = bl;
            }

            @Override
            public void visitTree(Tree<T> tree) {
                tree.forEachDescendant(this.val$visitor, true, this.val$childrenFirst);
            }
        });
        if (bl && bl2) {
            treeVisitor.visitTree(this);
        }
    }

    public ChildKey getName() {
        return this.name;
    }

    public Tree<T> getParent() {
        return this.parent;
    }

    public Path getPath() {
        Object object = this.parent;
        boolean bl = false;
        if (object != null) {
            if (this.name != null) {
                bl = true;
            }
            Utilities.hardAssert(bl);
            return this.parent.getPath().child(this.name);
        }
        object = this.name != null ? new Path(this.name) : Path.getEmptyPath();
        return object;
    }

    public T getValue() {
        return this.node.value;
    }

    public boolean hasChildren() {
        return this.node.children.isEmpty() ^ true;
    }

    public boolean isEmpty() {
        boolean bl = this.node.value == null && this.node.children.isEmpty();
        return bl;
    }

    public TreeNode<T> lastNodeOnPath(Path treeNode) {
        Object object = this.node;
        ChildKey childKey = ((Path)((Object)treeNode)).getFront();
        Path path = treeNode;
        treeNode = childKey;
        while (treeNode != null) {
            if ((treeNode = ((TreeNode)object).children.containsKey(treeNode) ? ((TreeNode)object).children.get(treeNode) : null) == null) {
                return object;
            }
            path = path.popFront();
            childKey = path.getFront();
            object = treeNode;
            treeNode = childKey;
        }
        return object;
    }

    public void setValue(T t) {
        this.node.value = t;
        this.updateParents();
    }

    public Tree<T> subTree(Path path) {
        Tree tree = this;
        ChildKey childKey = path.getFront();
        while (childKey != null) {
            TreeNode treeNode = tree.node.children.containsKey(childKey) ? tree.node.children.get(childKey) : new TreeNode();
            tree = new Tree(childKey, tree, treeNode);
            path = path.popFront();
            childKey = path.getFront();
        }
        return tree;
    }

    public String toString() {
        return this.toString("");
    }

    String toString(String string2) {
        Object object = this.name;
        object = object == null ? "<anon>" : ((ChildKey)object).asString();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(string2);
        stringBuilder.append((String)object);
        stringBuilder.append("\n");
        object = this.node;
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(string2);
        stringBuilder2.append("\t");
        stringBuilder.append(((TreeNode)object).toString(stringBuilder2.toString()));
        return stringBuilder.toString();
    }

    public static interface TreeFilter<T> {
        public boolean filterTreeNode(Tree<T> var1);
    }

    public static interface TreeVisitor<T> {
        public void visitTree(Tree<T> var1);
    }
}

