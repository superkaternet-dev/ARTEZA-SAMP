/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.collection;

import com.google.firebase.database.collection.ArraySortedMap;
import com.google.firebase.database.collection.ImmutableSortedMap$Builder$$ExternalSyntheticLambda0;
import com.google.firebase.database.collection.LLRBNode;
import com.google.firebase.database.collection.RBTreeSortedMap;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class ImmutableSortedMap<K, V>
implements Iterable<Map.Entry<K, V>> {
    public abstract boolean containsKey(K var1);

    public boolean equals(Object iterator2) {
        if (this == iterator2) {
            return true;
        }
        if (!(iterator2 instanceof ImmutableSortedMap)) {
            return false;
        }
        Object object = (ImmutableSortedMap)((Object)iterator2);
        if (!this.getComparator().equals(((ImmutableSortedMap)object).getComparator())) {
            return false;
        }
        if (this.size() != ((ImmutableSortedMap)object).size()) {
            return false;
        }
        iterator2 = this.iterator();
        object = ((ImmutableSortedMap)object).iterator();
        while (iterator2.hasNext()) {
            if (iterator2.next().equals(object.next())) continue;
            return false;
        }
        return true;
    }

    public abstract V get(K var1);

    public abstract Comparator<K> getComparator();

    public abstract K getMaxKey();

    public abstract K getMinKey();

    public abstract K getPredecessorKey(K var1);

    public abstract K getSuccessorKey(K var1);

    public int hashCode() {
        int n = this.getComparator().hashCode();
        Iterator<Map.Entry<K, V>> iterator2 = this.iterator();
        while (iterator2.hasNext()) {
            n = n * 31 + iterator2.next().hashCode();
        }
        return n;
    }

    public abstract void inOrderTraversal(LLRBNode.NodeVisitor<K, V> var1);

    public abstract int indexOf(K var1);

    public abstract ImmutableSortedMap<K, V> insert(K var1, V var2);

    public abstract boolean isEmpty();

    @Override
    public abstract Iterator<Map.Entry<K, V>> iterator();

    public abstract Iterator<Map.Entry<K, V>> iteratorFrom(K var1);

    public abstract ImmutableSortedMap<K, V> remove(K var1);

    public abstract Iterator<Map.Entry<K, V>> reverseIterator();

    public abstract Iterator<Map.Entry<K, V>> reverseIteratorFrom(K var1);

    public abstract int size();

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.getClass().getSimpleName());
        stringBuilder.append("{");
        boolean bl = true;
        for (Map.Entry<K, V> entry : this) {
            if (bl) {
                bl = false;
            } else {
                stringBuilder.append(", ");
            }
            stringBuilder.append("(");
            stringBuilder.append(entry.getKey());
            stringBuilder.append("=>");
            stringBuilder.append(entry.getValue());
            stringBuilder.append(")");
        }
        stringBuilder.append("};");
        return stringBuilder.toString();
    }

    public static class Builder {
        static final int ARRAY_TO_RB_TREE_SIZE_THRESHOLD = 25;
        private static final KeyTranslator IDENTITY_TRANSLATOR = ImmutableSortedMap$Builder$$ExternalSyntheticLambda0.INSTANCE;

        public static <A, B, C> ImmutableSortedMap<A, C> buildFrom(List<A> list, Map<B, C> map, KeyTranslator<A, B> keyTranslator, Comparator<A> comparator) {
            if (list.size() < 25) {
                return ArraySortedMap.buildFrom(list, map, keyTranslator, comparator);
            }
            return RBTreeSortedMap.buildFrom(list, map, keyTranslator, comparator);
        }

        public static <K, V> ImmutableSortedMap<K, V> emptyMap(Comparator<K> comparator) {
            return new ArraySortedMap(comparator);
        }

        public static <A, B> ImmutableSortedMap<A, B> fromMap(Map<A, B> map, Comparator<A> comparator) {
            if (map.size() < 25) {
                return ArraySortedMap.fromMap(map, comparator);
            }
            return RBTreeSortedMap.fromMap(map, comparator);
        }

        public static <A> KeyTranslator<A, A> identityTranslator() {
            return IDENTITY_TRANSLATOR;
        }

        static /* synthetic */ Object lambda$static$0(Object object) {
            return object;
        }

        public static interface KeyTranslator<C, D> {
            public D translate(C var1);
        }
    }
}

