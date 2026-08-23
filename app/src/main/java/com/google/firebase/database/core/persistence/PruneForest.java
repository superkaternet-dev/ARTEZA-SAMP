/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core.persistence;

import com.google.firebase.database.collection.ImmutableSortedMap;
import com.google.firebase.database.core.Path;
import com.google.firebase.database.core.utilities.ImmutableTree;
import com.google.firebase.database.core.utilities.Predicate;
import com.google.firebase.database.snapshot.ChildKey;
import java.util.Iterator;
import java.util.Set;

public class PruneForest {
    private static final Predicate<Boolean> KEEP_PREDICATE = new Predicate<Boolean>(){

        @Override
        public boolean evaluate(Boolean bl) {
            return bl ^ true;
        }
    };
    private static final ImmutableTree<Boolean> KEEP_TREE;
    private static final Predicate<Boolean> PRUNE_PREDICATE;
    private static final ImmutableTree<Boolean> PRUNE_TREE;
    private final ImmutableTree<Boolean> pruneForest;

    static {
        PRUNE_PREDICATE = new Predicate<Boolean>(){

            @Override
            public boolean evaluate(Boolean bl) {
                return bl;
            }
        };
        PRUNE_TREE = new ImmutableTree<Boolean>(true);
        KEEP_TREE = new ImmutableTree<Boolean>(false);
    }

    public PruneForest() {
        this.pruneForest = ImmutableTree.emptyInstance();
    }

    private PruneForest(ImmutableTree<Boolean> immutableTree) {
        this.pruneForest = immutableTree;
    }

    private PruneForest doAll(Path path, Set<ChildKey> iterable, ImmutableTree<Boolean> immutableTree) {
        ImmutableTree<Boolean> immutableTree2 = this.pruneForest.subtree(path);
        ImmutableSortedMap<ChildKey, ImmutableTree<Boolean>> immutableSortedMap = immutableTree2.getChildren();
        Iterator<ChildKey> iterator2 = iterable.iterator();
        iterable = immutableSortedMap;
        while (iterator2.hasNext()) {
            iterable = ((ImmutableSortedMap)iterable).insert(iterator2.next(), immutableTree);
        }
        return new PruneForest(this.pruneForest.setTree(path, new ImmutableTree<Boolean>(immutableTree2.getValue(), (ImmutableSortedMap<ChildKey, ImmutableTree<Boolean>>)iterable)));
    }

    public boolean affectsPath(Path path) {
        boolean bl = this.pruneForest.rootMostValue(path) != null || !this.pruneForest.subtree(path).isEmpty();
        return bl;
    }

    public PruneForest child(Path path) {
        if (path.isEmpty()) {
            return this;
        }
        return this.child(path.getFront()).child(path.popFront());
    }

    public PruneForest child(ChildKey immutableTree) {
        ImmutableTree<Boolean> immutableTree2 = this.pruneForest.getChild((ChildKey)((Object)immutableTree));
        if (immutableTree2 == null) {
            immutableTree = new ImmutableTree<Boolean>(this.pruneForest.getValue());
        } else {
            immutableTree = immutableTree2;
            if (immutableTree2.getValue() == null) {
                immutableTree = immutableTree2;
                if (this.pruneForest.getValue() != null) {
                    immutableTree = immutableTree2.set(Path.getEmptyPath(), this.pruneForest.getValue());
                }
            }
        }
        return new PruneForest(immutableTree);
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof PruneForest)) {
            return false;
        }
        object = (PruneForest)object;
        return this.pruneForest.equals(((PruneForest)object).pruneForest);
    }

    public <T> T foldKeptNodes(T t, ImmutableTree.TreeVisitor<Void, T> treeVisitor) {
        return this.pruneForest.fold(t, new ImmutableTree.TreeVisitor<Boolean, T>(this, treeVisitor){
            final PruneForest this$0;
            final ImmutableTree.TreeVisitor val$treeVisitor;
            {
                this.this$0 = pruneForest;
                this.val$treeVisitor = treeVisitor;
            }

            @Override
            public T onNodeValue(Path path, Boolean bl, T t) {
                if (!bl.booleanValue()) {
                    return this.val$treeVisitor.onNodeValue(path, null, t);
                }
                return t;
            }
        });
    }

    public int hashCode() {
        return this.pruneForest.hashCode();
    }

    public PruneForest keep(Path path) {
        if (this.pruneForest.rootMostValueMatching(path, KEEP_PREDICATE) != null) {
            return this;
        }
        return new PruneForest(this.pruneForest.setTree(path, KEEP_TREE));
    }

    public PruneForest keepAll(Path path, Set<ChildKey> set) {
        if (this.pruneForest.rootMostValueMatching(path, KEEP_PREDICATE) != null) {
            return this;
        }
        return this.doAll(path, set, KEEP_TREE);
    }

    public PruneForest prune(Path path) {
        if (this.pruneForest.rootMostValueMatching(path, KEEP_PREDICATE) == null) {
            if (this.pruneForest.rootMostValueMatching(path, PRUNE_PREDICATE) != null) {
                return this;
            }
            return new PruneForest(this.pruneForest.setTree(path, PRUNE_TREE));
        }
        throw new IllegalArgumentException("Can't prune path that was kept previously!");
    }

    public PruneForest pruneAll(Path path, Set<ChildKey> set) {
        if (this.pruneForest.rootMostValueMatching(path, KEEP_PREDICATE) == null) {
            if (this.pruneForest.rootMostValueMatching(path, PRUNE_PREDICATE) != null) {
                return this;
            }
            return this.doAll(path, set, PRUNE_TREE);
        }
        throw new IllegalArgumentException("Can't prune path that was kept previously!");
    }

    public boolean prunesAnything() {
        return this.pruneForest.containsMatchingValue(PRUNE_PREDICATE);
    }

    public boolean shouldKeep(Path comparable) {
        boolean bl = (comparable = this.pruneForest.leafMostValue((Path)comparable)) != null && !((Boolean)comparable).booleanValue();
        return bl;
    }

    public boolean shouldPruneUnkeptDescendants(Path comparable) {
        boolean bl = (comparable = this.pruneForest.leafMostValue((Path)comparable)) != null && ((Boolean)comparable).booleanValue();
        return bl;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{PruneForest:");
        stringBuilder.append(this.pruneForest.toString());
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}

