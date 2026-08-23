/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.collection;

import com.google.firebase.database.collection.ImmutableSortedMap;
import com.google.firebase.database.collection.LLRBNode;
import com.google.firebase.database.collection.RBTreeSortedMap;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ArraySortedMap<K, V>
extends ImmutableSortedMap<K, V> {
    private final Comparator<K> comparator;
    private final K[] keys;
    private final V[] values;

    public ArraySortedMap(Comparator<K> comparator) {
        this.keys = new Object[0];
        this.values = new Object[0];
        this.comparator = comparator;
    }

    private ArraySortedMap(Comparator<K> comparator, K[] KArray, V[] VArray) {
        this.keys = KArray;
        this.values = VArray;
        this.comparator = comparator;
    }

    private static <T> T[] addToArray(T[] TArray, int n, T t) {
        int n2 = TArray.length + 1;
        Object[] objectArray = new Object[n2];
        System.arraycopy(TArray, 0, objectArray, 0, n);
        objectArray[n] = t;
        System.arraycopy(TArray, n, objectArray, n + 1, n2 - n - 1);
        return objectArray;
    }

    public static <A, B, C> ArraySortedMap<A, C> buildFrom(List<A> object, Map<B, C> map, ImmutableSortedMap.Builder.KeyTranslator<A, B> keyTranslator, Comparator<A> comparator) {
        Collections.sort(object, comparator);
        int n = object.size();
        Object[] objectArray = new Object[n];
        Object[] objectArray2 = new Object[n];
        n = 0;
        object = object.iterator();
        while (object.hasNext()) {
            Object e = object.next();
            objectArray[n] = e;
            objectArray2[n] = map.get(keyTranslator.translate(e));
            ++n;
        }
        return new ArraySortedMap<Object, Object>(comparator, objectArray, objectArray2);
    }

    private int findKey(K k) {
        int n = 0;
        for (K k2 : this.keys) {
            if (this.comparator.compare(k, k2) == 0) {
                return n;
            }
            ++n;
        }
        return -1;
    }

    private int findKeyOrInsertPosition(K k) {
        K[] KArray;
        int n;
        for (n = 0; n < (KArray = this.keys).length && this.comparator.compare(KArray[n], k) < 0; ++n) {
        }
        return n;
    }

    public static <K, V> ArraySortedMap<K, V> fromMap(Map<K, V> map, Comparator<K> comparator) {
        return ArraySortedMap.buildFrom(new ArrayList<K>(map.keySet()), map, ImmutableSortedMap.Builder.identityTranslator(), comparator);
    }

    private Iterator<Map.Entry<K, V>> iterator(int n, boolean bl) {
        return new Iterator<Map.Entry<K, V>>(this, n, bl){
            int currentPos;
            final ArraySortedMap this$0;
            final int val$pos;
            final boolean val$reverse;
            {
                this.this$0 = arraySortedMap;
                this.val$pos = n;
                this.val$reverse = bl;
                this.currentPos = n;
            }

            @Override
            public boolean hasNext() {
                boolean bl = this.val$reverse;
                boolean bl2 = true;
                if (!(bl ? this.currentPos >= 0 : this.currentPos < this.this$0.keys.length)) {
                    bl2 = false;
                }
                return bl2;
            }

            @Override
            public Map.Entry<K, V> next() {
                Object object = this.this$0.keys[this.currentPos];
                Object object2 = this.this$0.values;
                int n = this.currentPos;
                object2 = object2[n];
                n = this.val$reverse ? --n : ++n;
                this.currentPos = n;
                return new AbstractMap.SimpleImmutableEntry<Object, Object[]>(object, (Object[])object2);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Can't remove elements from ImmutableSortedMap");
            }
        };
    }

    private static <T> T[] removeFromArray(T[] TArray, int n) {
        int n2 = TArray.length - 1;
        Object[] objectArray = new Object[n2];
        System.arraycopy(TArray, 0, objectArray, 0, n);
        System.arraycopy(TArray, n + 1, objectArray, n, n2 - n);
        return objectArray;
    }

    private static <T> T[] replaceInArray(T[] TArray, int n, T t) {
        int n2 = TArray.length;
        Object[] objectArray = new Object[n2];
        System.arraycopy(TArray, 0, objectArray, 0, n2);
        objectArray[n] = t;
        return objectArray;
    }

    @Override
    public boolean containsKey(K k) {
        boolean bl = this.findKey(k) != -1;
        return bl;
    }

    @Override
    public V get(K object) {
        int n = this.findKey(object);
        object = n != -1 ? this.values[n] : null;
        return (V)object;
    }

    @Override
    public Comparator<K> getComparator() {
        return this.comparator;
    }

    @Override
    public K getMaxKey() {
        Object object = this.keys;
        object = ((K[])object).length > 0 ? object[((K[])object).length - 1] : null;
        return (K)object;
    }

    @Override
    public K getMinKey() {
        Object object = this.keys;
        object = ((K[])object).length > 0 ? object[0] : null;
        return (K)object;
    }

    @Override
    public K getPredecessorKey(K object) {
        int n = this.findKey(object);
        if (n != -1) {
            object = n > 0 ? this.keys[n - 1] : null;
            return object;
        }
        throw new IllegalArgumentException("Can't find predecessor of nonexistent key");
    }

    @Override
    public K getSuccessorKey(K object) {
        int n = this.findKey(object);
        if (n != -1) {
            object = this.keys;
            object = n < ((K)object).length - 1 ? object[n + 1] : null;
            return object;
        }
        throw new IllegalArgumentException("Can't find successor of nonexistent key");
    }

    @Override
    public void inOrderTraversal(LLRBNode.NodeVisitor<K, V> nodeVisitor) {
        K[] KArray;
        for (int i = 0; i < (KArray = this.keys).length; ++i) {
            nodeVisitor.visitEntry(KArray[i], this.values[i]);
        }
    }

    @Override
    public int indexOf(K k) {
        return this.findKey(k);
    }

    @Override
    public ImmutableSortedMap<K, V> insert(K object, V object2) {
        int n = this.findKey(object);
        if (n != -1) {
            K[] KArray = this.keys;
            if (KArray[n] == object && this.values[n] == object2) {
                return this;
            }
            object = ArraySortedMap.replaceInArray(KArray, n, object);
            object2 = ArraySortedMap.replaceInArray(this.values, n, object2);
            return new ArraySortedMap<K, V>(this.comparator, (K[])object, (V[])object2);
        }
        if (this.keys.length > 25) {
            K[] KArray;
            HashMap<K, V> hashMap = new HashMap<K, V>(this.keys.length + 1);
            for (n = 0; n < (KArray = this.keys).length; ++n) {
                hashMap.put(KArray[n], this.values[n]);
            }
            hashMap.put(object, object2);
            return RBTreeSortedMap.fromMap(hashMap, this.comparator);
        }
        n = this.findKeyOrInsertPosition(object);
        object = ArraySortedMap.addToArray(this.keys, n, object);
        object2 = ArraySortedMap.addToArray(this.values, n, object2);
        return new ArraySortedMap<K, V>(this.comparator, (K[])object, (V[])object2);
    }

    @Override
    public boolean isEmpty() {
        boolean bl = this.keys.length == 0;
        return bl;
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        return this.iterator(0, false);
    }

    @Override
    public Iterator<Map.Entry<K, V>> iteratorFrom(K k) {
        return this.iterator(this.findKeyOrInsertPosition(k), false);
    }

    @Override
    public ImmutableSortedMap<K, V> remove(K object) {
        int n = this.findKey(object);
        if (n == -1) {
            return this;
        }
        object = ArraySortedMap.removeFromArray(this.keys, n);
        V[] VArray = ArraySortedMap.removeFromArray(this.values, n);
        return new ArraySortedMap<K, V>(this.comparator, (K[])object, VArray);
    }

    @Override
    public Iterator<Map.Entry<K, V>> reverseIterator() {
        return this.iterator(this.keys.length - 1, true);
    }

    @Override
    public Iterator<Map.Entry<K, V>> reverseIteratorFrom(K k) {
        K[] KArray;
        int n = this.findKeyOrInsertPosition(k);
        if (n < (KArray = this.keys).length && this.comparator.compare(KArray[n], k) == 0) {
            return this.iterator(n, true);
        }
        return this.iterator(n - 1, true);
    }

    @Override
    public int size() {
        return this.keys.length;
    }
}

