/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.collection;

import com.google.firebase.database.collection.ImmutableSortedMap;
import com.google.firebase.database.collection.ImmutableSortedMapIterator;
import com.google.firebase.database.collection.LLRBBlackValueNode;
import com.google.firebase.database.collection.LLRBEmptyNode;
import com.google.firebase.database.collection.LLRBNode;
import com.google.firebase.database.collection.LLRBRedValueNode;
import com.google.firebase.database.collection.LLRBValueNode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RBTreeSortedMap<K, V>
extends ImmutableSortedMap<K, V> {
    private Comparator<K> comparator;
    private LLRBNode<K, V> root;

    private RBTreeSortedMap(LLRBNode<K, V> lLRBNode, Comparator<K> comparator) {
        this.root = lLRBNode;
        this.comparator = comparator;
    }

    RBTreeSortedMap(Comparator<K> comparator) {
        this.root = LLRBEmptyNode.getInstance();
        this.comparator = comparator;
    }

    public static <A, B, C> RBTreeSortedMap<A, C> buildFrom(List<A> list, Map<B, C> map, ImmutableSortedMap.Builder.KeyTranslator<A, B> keyTranslator, Comparator<A> comparator) {
        return Builder.buildFrom(list, map, keyTranslator, comparator);
    }

    public static <A, B> RBTreeSortedMap<A, B> fromMap(Map<A, B> map, Comparator<A> comparator) {
        return Builder.buildFrom(new ArrayList<A>(map.keySet()), map, ImmutableSortedMap.Builder.identityTranslator(), comparator);
    }

    private LLRBNode<K, V> getNode(K k) {
        LLRBNode<K, V> lLRBNode = this.root;
        while (!lLRBNode.isEmpty()) {
            int n = this.comparator.compare(k, lLRBNode.getKey());
            if (n < 0) {
                lLRBNode = lLRBNode.getLeft();
                continue;
            }
            if (n == 0) {
                return lLRBNode;
            }
            lLRBNode = lLRBNode.getRight();
        }
        return null;
    }

    @Override
    public boolean containsKey(K k) {
        boolean bl = this.getNode(k) != null;
        return bl;
    }

    @Override
    public V get(K object) {
        object = (object = this.getNode(object)) != null ? object.getValue() : null;
        return (V)object;
    }

    @Override
    public Comparator<K> getComparator() {
        return this.comparator;
    }

    @Override
    public K getMaxKey() {
        return this.root.getMax().getKey();
    }

    @Override
    public K getMinKey() {
        return this.root.getMin().getKey();
    }

    @Override
    public K getPredecessorKey(K object) {
        LLRBNode lLRBNode = this.root;
        LLRBNode lLRBNode2 = null;
        while (!lLRBNode.isEmpty()) {
            int n = this.comparator.compare(object, lLRBNode.getKey());
            if (n == 0) {
                if (!lLRBNode.getLeft().isEmpty()) {
                    object = lLRBNode.getLeft();
                    while (!object.getRight().isEmpty()) {
                        object = object.getRight();
                    }
                    return object.getKey();
                }
                if (lLRBNode2 != null) {
                    return lLRBNode2.getKey();
                }
                return null;
            }
            if (n < 0) {
                lLRBNode = lLRBNode.getLeft();
                continue;
            }
            lLRBNode2 = lLRBNode;
            lLRBNode = lLRBNode.getRight();
        }
        lLRBNode = new StringBuilder();
        ((StringBuilder)((Object)lLRBNode)).append("Couldn't find predecessor key of non-present key: ");
        ((StringBuilder)((Object)lLRBNode)).append(object);
        object = new IllegalArgumentException(((StringBuilder)((Object)lLRBNode)).toString());
        throw object;
    }

    LLRBNode<K, V> getRoot() {
        return this.root;
    }

    @Override
    public K getSuccessorKey(K object) {
        LLRBNode lLRBNode = this.root;
        LLRBNode lLRBNode2 = null;
        while (!lLRBNode.isEmpty()) {
            int n = this.comparator.compare(lLRBNode.getKey(), object);
            if (n == 0) {
                if (!lLRBNode.getRight().isEmpty()) {
                    object = lLRBNode.getRight();
                    while (!object.getLeft().isEmpty()) {
                        object = object.getLeft();
                    }
                    return object.getKey();
                }
                if (lLRBNode2 != null) {
                    return lLRBNode2.getKey();
                }
                return null;
            }
            if (n < 0) {
                lLRBNode = lLRBNode.getRight();
                continue;
            }
            lLRBNode2 = lLRBNode;
            lLRBNode = lLRBNode.getLeft();
        }
        lLRBNode = new StringBuilder();
        ((StringBuilder)((Object)lLRBNode)).append("Couldn't find successor key of non-present key: ");
        ((StringBuilder)((Object)lLRBNode)).append(object);
        object = new IllegalArgumentException(((StringBuilder)((Object)lLRBNode)).toString());
        throw object;
    }

    @Override
    public void inOrderTraversal(LLRBNode.NodeVisitor<K, V> nodeVisitor) {
        this.root.inOrderTraversal(nodeVisitor);
    }

    @Override
    public int indexOf(K k) {
        int n = 0;
        LLRBNode<K, V> lLRBNode = this.root;
        while (!lLRBNode.isEmpty()) {
            int n2 = this.comparator.compare(k, lLRBNode.getKey());
            if (n2 == 0) {
                return lLRBNode.getLeft().size() + n;
            }
            if (n2 < 0) {
                lLRBNode = lLRBNode.getLeft();
                continue;
            }
            n += lLRBNode.getLeft().size() + 1;
            lLRBNode = lLRBNode.getRight();
        }
        return -1;
    }

    @Override
    public ImmutableSortedMap<K, V> insert(K k, V v) {
        return new RBTreeSortedMap<Object, Object>(this.root.insert(k, v, this.comparator).copy(null, null, LLRBNode.Color.BLACK, null, null), this.comparator);
    }

    @Override
    public boolean isEmpty() {
        return this.root.isEmpty();
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        return new ImmutableSortedMapIterator<Object, V>(this.root, null, this.comparator, false);
    }

    @Override
    public Iterator<Map.Entry<K, V>> iteratorFrom(K k) {
        return new ImmutableSortedMapIterator<K, V>(this.root, k, this.comparator, false);
    }

    @Override
    public ImmutableSortedMap<K, V> remove(K k) {
        if (!this.containsKey(k)) {
            return this;
        }
        return new RBTreeSortedMap<Object, Object>(this.root.remove(k, this.comparator).copy(null, null, LLRBNode.Color.BLACK, null, null), this.comparator);
    }

    @Override
    public Iterator<Map.Entry<K, V>> reverseIterator() {
        return new ImmutableSortedMapIterator<Object, V>(this.root, null, this.comparator, true);
    }

    @Override
    public Iterator<Map.Entry<K, V>> reverseIteratorFrom(K k) {
        return new ImmutableSortedMapIterator<K, V>(this.root, k, this.comparator, true);
    }

    @Override
    public int size() {
        return this.root.size();
    }

    private static class Builder<A, B, C> {
        private final ImmutableSortedMap.Builder.KeyTranslator<A, B> keyTranslator;
        private final List<A> keys;
        private LLRBValueNode<A, C> leaf;
        private LLRBValueNode<A, C> root;
        private final Map<B, C> values;

        private Builder(List<A> list, Map<B, C> map, ImmutableSortedMap.Builder.KeyTranslator<A, B> keyTranslator) {
            this.keys = list;
            this.values = map;
            this.keyTranslator = keyTranslator;
        }

        private LLRBNode<A, C> buildBalancedTree(int n, int n2) {
            if (n2 == 0) {
                return LLRBEmptyNode.getInstance();
            }
            if (n2 == 1) {
                A a = this.keys.get(n);
                return new LLRBBlackValueNode<A, C>(a, this.getValue(a), null, null);
            }
            int n3 = n + (n2 /= 2);
            LLRBNode<A, C> lLRBNode = this.buildBalancedTree(n, n2);
            LLRBNode<A, C> lLRBNode2 = this.buildBalancedTree(n3 + 1, n2);
            A a = this.keys.get(n3);
            return new LLRBBlackValueNode<A, C>(a, this.getValue(a), lLRBNode, lLRBNode2);
        }

        public static <A, B, C> RBTreeSortedMap<A, C> buildFrom(List<A> lLRBNode, Map<B, C> lLRBValueNode, ImmutableSortedMap.Builder.KeyTranslator<A, B> object, Comparator<A> comparator) {
            object = new Builder<A, B, C>(lLRBNode, lLRBValueNode, object);
            Collections.sort(lLRBNode, comparator);
            lLRBValueNode = new Base1_2(lLRBNode.size()).iterator();
            int n = lLRBNode.size();
            while (lLRBValueNode.hasNext()) {
                lLRBNode = (BooleanChunk)lLRBValueNode.next();
                n -= ((BooleanChunk)((Object)lLRBNode)).chunkSize;
                if (((BooleanChunk)((Object)lLRBNode)).isOne) {
                    super.buildPennant(LLRBNode.Color.BLACK, ((BooleanChunk)((Object)lLRBNode)).chunkSize, n);
                    continue;
                }
                super.buildPennant(LLRBNode.Color.BLACK, ((BooleanChunk)((Object)lLRBNode)).chunkSize, n);
                super.buildPennant(LLRBNode.Color.RED, ((BooleanChunk)((Object)lLRBNode)).chunkSize, n -= ((BooleanChunk)((Object)lLRBNode)).chunkSize);
            }
            lLRBNode = lLRBValueNode = ((Builder)object).root;
            if (lLRBValueNode == null) {
                lLRBNode = LLRBEmptyNode.getInstance();
            }
            return new RBTreeSortedMap(lLRBNode, comparator);
        }

        private void buildPennant(LLRBNode.Color object, int n, int n2) {
            LLRBNode<A, C> lLRBNode = this.buildBalancedTree(n2 + 1, n - 1);
            A a = this.keys.get(n2);
            object = object == LLRBNode.Color.RED ? new LLRBRedValueNode<A, C>(a, this.getValue(a), null, lLRBNode) : new LLRBBlackValueNode<A, C>(a, this.getValue(a), null, lLRBNode);
            if (this.root == null) {
                this.root = object;
                this.leaf = object;
            } else {
                this.leaf.setLeft((LLRBNode<A, C>)object);
                this.leaf = object;
            }
        }

        private C getValue(A a) {
            return this.values.get(this.keyTranslator.translate(a));
        }

        static class Base1_2
        implements Iterable<BooleanChunk> {
            private final int length;
            private long value;

            public Base1_2(int n) {
                int n2 = n + 1;
                this.length = n = (int)Math.floor(Math.log(n2) / Math.log(2.0));
                long l = (long)Math.pow(2.0, n);
                this.value = (long)n2 & l - 1L;
            }

            @Override
            public Iterator<BooleanChunk> iterator() {
                return new Iterator<BooleanChunk>(this){
                    private int current;
                    final Base1_2 this$0;
                    {
                        this.this$0 = base1_2;
                        this.current = base1_2.length - 1;
                    }

                    @Override
                    public boolean hasNext() {
                        boolean bl = this.current >= 0;
                        return bl;
                    }

                    @Override
                    public BooleanChunk next() {
                        long l = this.this$0.value;
                        long l2 = 1 << this.current;
                        BooleanChunk booleanChunk = new BooleanChunk();
                        boolean bl = (l & l2) == 0L;
                        booleanChunk.isOne = bl;
                        booleanChunk.chunkSize = (int)Math.pow(2.0, this.current);
                        --this.current;
                        return booleanChunk;
                    }

                    @Override
                    public void remove() {
                    }
                };
            }
        }

        static class BooleanChunk {
            public int chunkSize;
            public boolean isOne;

            BooleanChunk() {
            }
        }
    }
}

