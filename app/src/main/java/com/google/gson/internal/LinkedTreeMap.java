/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson.internal;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public final class LinkedTreeMap<K, V>
extends AbstractMap<K, V>
implements Serializable {
    static final boolean $assertionsDisabled = false;
    private static final Comparator<Comparable> NATURAL_ORDER = new Comparator<Comparable>(){

        @Override
        public int compare(Comparable comparable, Comparable comparable2) {
            return comparable.compareTo(comparable2);
        }
    };
    Comparator<? super K> comparator;
    private EntrySet entrySet;
    final Node<K, V> header = new Node();
    private KeySet keySet;
    int modCount = 0;
    Node<K, V> root;
    int size = 0;

    public LinkedTreeMap() {
        this(NATURAL_ORDER);
    }

    public LinkedTreeMap(Comparator<? super K> comparator) {
        if (comparator == null) {
            comparator = NATURAL_ORDER;
        }
        this.comparator = comparator;
    }

    private boolean equal(Object object, Object object2) {
        boolean bl = object == object2 || object != null && object.equals(object2);
        return bl;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private void rebalance(Node<K, V> node, boolean bl) {
        while (node != null) {
            Node node2;
            int n;
            Node node3 = node.left;
            Node node4 = node.right;
            int n2 = 0;
            int n3 = 0;
            int n4 = node3 != null ? node3.height : 0;
            int n5 = n4 - (n = node4 != null ? node4.height : 0);
            if (n5 == -2) {
                node3 = node4.left;
                node2 = node4.right;
                n4 = node2 != null ? node2.height : 0;
                n = n3;
                if (node3 != null) {
                    n = node3.height;
                }
                if ((n4 = n - n4) != -1 && (n4 != 0 || bl)) {
                    if (n4 != 1) throw new AssertionError();
                    this.rotateRight(node4);
                    this.rotateLeft(node);
                } else {
                    this.rotateLeft(node);
                }
                if (bl) {
                    return;
                }
            } else if (n5 == 2) {
                node2 = node3.left;
                node4 = node3.right;
                n4 = node4 != null ? node4.height : 0;
                n = n2;
                if (node2 != null) {
                    n = node2.height;
                }
                if ((n4 = n - n4) != 1 && (n4 != 0 || bl)) {
                    if (n4 != -1) throw new AssertionError();
                    this.rotateLeft(node3);
                    this.rotateRight(node);
                } else {
                    this.rotateRight(node);
                }
                if (bl) {
                    return;
                }
            } else if (n5 == 0) {
                node.height = n4 + 1;
                if (bl) {
                    return;
                }
            } else {
                if (n5 != -1 && n5 != 1) {
                    throw new AssertionError();
                }
                node.height = Math.max(n4, n) + 1;
                if (!bl) return;
            }
            node = node.parent;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private void replaceInParent(Node<K, V> node, Node<K, V> node2) {
        Node node3 = node.parent;
        node.parent = null;
        if (node2 != null) {
            node2.parent = node3;
        }
        if (node3 != null) {
            if (node3.left == node) {
                node3.left = node2;
                return;
            } else {
                if (node3.right != node) throw new AssertionError();
                node3.right = node2;
            }
            return;
        } else {
            this.root = node2;
        }
    }

    private void rotateLeft(Node<K, V> node) {
        Node node2 = node.left;
        Node node3 = node.right;
        Node node4 = node3.left;
        Node node5 = node3.right;
        node.right = node4;
        if (node4 != null) {
            node4.parent = node;
        }
        this.replaceInParent(node, node3);
        node3.left = node;
        node.parent = node3;
        int n = 0;
        int n2 = node2 != null ? node2.height : 0;
        int n3 = node4 != null ? node4.height : 0;
        n3 = node.height = Math.max(n2, n3) + 1;
        n2 = n;
        if (node5 != null) {
            n2 = node5.height;
        }
        node3.height = Math.max(n3, n2) + 1;
    }

    private void rotateRight(Node<K, V> node) {
        Node node2 = node.left;
        Node node3 = node.right;
        Node node4 = node2.left;
        Node node5 = node2.right;
        node.left = node5;
        if (node5 != null) {
            node5.parent = node;
        }
        this.replaceInParent(node, node2);
        node2.right = node;
        node.parent = node2;
        int n = 0;
        int n2 = node3 != null ? node3.height : 0;
        int n3 = node5 != null ? node5.height : 0;
        n3 = node.height = Math.max(n2, n3) + 1;
        n2 = n;
        if (node4 != null) {
            n2 = node4.height;
        }
        node2.height = Math.max(n3, n2) + 1;
    }

    private Object writeReplace() throws ObjectStreamException {
        return new LinkedHashMap(this);
    }

    @Override
    public void clear() {
        this.root = null;
        this.size = 0;
        ++this.modCount;
        Node<K, V> node = this.header;
        node.prev = node;
        node.next = node;
    }

    @Override
    public boolean containsKey(Object object) {
        boolean bl = this.findByObject(object) != null;
        return bl;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        EntrySet entrySet = this.entrySet;
        if (entrySet == null) {
            this.entrySet = entrySet = new EntrySet(this);
        }
        return entrySet;
    }

    Node<K, V> find(K object, boolean bl) {
        Comparator<K> comparator = this.comparator;
        Node<K, V> node = this.root;
        int n = 0;
        Node node2 = node;
        if (node != null) {
            Comparable comparable = comparator == NATURAL_ORDER ? (Comparable)object : null;
            while (true) {
                if ((n = comparable != null ? comparable.compareTo(node.key) : comparator.compare(object, node.key)) == 0) {
                    return node;
                }
                node2 = n < 0 ? node.left : node.right;
                if (node2 == null) {
                    node2 = node;
                    break;
                }
                node = node2;
            }
        }
        if (!bl) {
            return null;
        }
        node = this.header;
        if (node2 == null) {
            if (comparator == NATURAL_ORDER && !(object instanceof Comparable)) {
                node = new StringBuilder();
                ((StringBuilder)((Object)node)).append(object.getClass().getName());
                ((StringBuilder)((Object)node)).append(" is not Comparable");
                throw new ClassCastException(((StringBuilder)((Object)node)).toString());
            }
            object = new Node<K, V>(node2, object, node, node.prev);
            this.root = object;
        } else {
            object = new Node<K, V>(node2, object, node, node.prev);
            if (n < 0) {
                node2.left = object;
            } else {
                node2.right = object;
            }
            this.rebalance(node2, true);
        }
        ++this.size;
        ++this.modCount;
        return object;
    }

    Node<K, V> findByEntry(Map.Entry<?, ?> entry) {
        Node<?, ?> node = this.findByObject(entry.getKey());
        boolean bl = node != null && this.equal(node.value, entry.getValue());
        entry = bl ? node : null;
        return entry;
    }

    Node<K, V> findByObject(Object object) {
        Node<Object, V> node = null;
        if (object != null) {
            try {
                node = this.find(object, false);
            }
            catch (ClassCastException classCastException) {
                return null;
            }
        }
        return node;
    }

    @Override
    public V get(Object node) {
        node = (node = this.findByObject(node)) != null ? node.value : null;
        return (V)node;
    }

    @Override
    public Set<K> keySet() {
        KeySet keySet = this.keySet;
        if (keySet == null) {
            this.keySet = keySet = new KeySet(this);
        }
        return keySet;
    }

    @Override
    public V put(K object, V v) {
        if (object != null) {
            Node<K, V> node = this.find(object, true);
            object = node.value;
            node.value = v;
            return (V)object;
        }
        throw new NullPointerException("key == null");
    }

    @Override
    public V remove(Object node) {
        node = (node = this.removeInternalByKey(node)) != null ? node.value : null;
        return (V)node;
    }

    void removeInternal(Node<K, V> node, boolean bl) {
        if (bl) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
        Node node2 = node.left;
        Node node3 = node.right;
        Node node4 = node.parent;
        if (node2 != null && node3 != null) {
            node2 = node2.height > node3.height ? node2.last() : node3.first();
            this.removeInternal(node2, false);
            int n = 0;
            node3 = node.left;
            if (node3 != null) {
                n = node3.height;
                node2.left = node3;
                node3.parent = node2;
                node.left = null;
            }
            int n2 = 0;
            node3 = node.right;
            if (node3 != null) {
                n2 = node3.height;
                node2.right = node3;
                node3.parent = node2;
                node.right = null;
            }
            node2.height = Math.max(n, n2) + 1;
            this.replaceInParent(node, node2);
            return;
        }
        if (node2 != null) {
            this.replaceInParent(node, node2);
            node.left = null;
        } else if (node3 != null) {
            this.replaceInParent(node, node3);
            node.right = null;
        } else {
            this.replaceInParent(node, null);
        }
        this.rebalance(node4, false);
        --this.size;
        ++this.modCount;
    }

    Node<K, V> removeInternalByKey(Object node) {
        if ((node = this.findByObject(node)) != null) {
            this.removeInternal(node, true);
        }
        return node;
    }

    @Override
    public int size() {
        return this.size;
    }

    class EntrySet
    extends AbstractSet<Map.Entry<K, V>> {
        final LinkedTreeMap this$0;

        EntrySet(LinkedTreeMap linkedTreeMap) {
            this.this$0 = linkedTreeMap;
        }

        @Override
        public void clear() {
            this.this$0.clear();
        }

        @Override
        public boolean contains(Object object) {
            boolean bl = object instanceof Map.Entry && this.this$0.findByEntry((Map.Entry)object) != null;
            return bl;
        }

        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new LinkedTreeMapIterator<Map.Entry<K, V>>(this){
                final EntrySet this$1;
                {
                    this.this$1 = entrySet;
                    super(entrySet.this$0);
                }

                @Override
                public Map.Entry<K, V> next() {
                    return this.nextNode();
                }
            };
        }

        @Override
        public boolean remove(Object node) {
            if (!(node instanceof Map.Entry)) {
                return false;
            }
            if ((node = this.this$0.findByEntry(node)) == null) {
                return false;
            }
            this.this$0.removeInternal(node, true);
            return true;
        }

        @Override
        public int size() {
            return this.this$0.size;
        }
    }

    final class KeySet
    extends AbstractSet<K> {
        final LinkedTreeMap this$0;

        KeySet(LinkedTreeMap linkedTreeMap) {
            this.this$0 = linkedTreeMap;
        }

        @Override
        public void clear() {
            this.this$0.clear();
        }

        @Override
        public boolean contains(Object object) {
            return this.this$0.containsKey(object);
        }

        @Override
        public Iterator<K> iterator() {
            return new LinkedTreeMapIterator<K>(this){
                final KeySet this$1;
                {
                    this.this$1 = keySet;
                    super(keySet.this$0);
                }

                @Override
                public K next() {
                    return this.nextNode().key;
                }
            };
        }

        @Override
        public boolean remove(Object object) {
            boolean bl = this.this$0.removeInternalByKey(object) != null;
            return bl;
        }

        @Override
        public int size() {
            return this.this$0.size;
        }
    }

    private abstract class LinkedTreeMapIterator<T>
    implements Iterator<T> {
        int expectedModCount;
        Node<K, V> lastReturned;
        Node<K, V> next;
        final LinkedTreeMap this$0;

        LinkedTreeMapIterator(LinkedTreeMap linkedTreeMap) {
            this.this$0 = linkedTreeMap;
            this.next = linkedTreeMap.header.next;
            this.lastReturned = null;
            this.expectedModCount = linkedTreeMap.modCount;
        }

        @Override
        public final boolean hasNext() {
            boolean bl = this.next != this.this$0.header;
            return bl;
        }

        final Node<K, V> nextNode() {
            Node node = this.next;
            if (node != this.this$0.header) {
                if (this.this$0.modCount == this.expectedModCount) {
                    this.next = node.next;
                    this.lastReturned = node;
                    return node;
                }
                throw new ConcurrentModificationException();
            }
            throw new NoSuchElementException();
        }

        @Override
        public final void remove() {
            Node node = this.lastReturned;
            if (node != null) {
                this.this$0.removeInternal(node, true);
                this.lastReturned = null;
                this.expectedModCount = this.this$0.modCount;
                return;
            }
            throw new IllegalStateException();
        }
    }

    static final class Node<K, V>
    implements Map.Entry<K, V> {
        int height;
        final K key;
        Node<K, V> left;
        Node<K, V> next;
        Node<K, V> parent;
        Node<K, V> prev;
        Node<K, V> right;
        V value;

        Node() {
            this.key = null;
            this.prev = this;
            this.next = this;
        }

        Node(Node<K, V> node, K k, Node<K, V> node2, Node<K, V> node3) {
            this.parent = node;
            this.key = k;
            this.height = 1;
            this.next = node2;
            this.prev = node3;
            node3.next = this;
            node2.prev = this;
        }

        @Override
        public boolean equals(Object object) {
            boolean bl = object instanceof Map.Entry;
            boolean bl2 = false;
            if (bl) {
                object = (Map.Entry)object;
                Object object2 = this.key;
                if ((object2 == null ? object.getKey() == null : object2.equals(object.getKey())) && ((object2 = this.value) == null ? object.getValue() == null : object2.equals(object.getValue()))) {
                    bl2 = true;
                }
                return bl2;
            }
            return false;
        }

        public Node<K, V> first() {
            Node<K, V> node = this;
            Node<K, V> node2 = node.left;
            while (node2 != null) {
                Node<K, V> node3 = node2.left;
                node = node2;
                node2 = node3;
            }
            return node;
        }

        @Override
        public K getKey() {
            return this.key;
        }

        @Override
        public V getValue() {
            return this.value;
        }

        @Override
        public int hashCode() {
            Object object = this.key;
            int n = 0;
            int n2 = object == null ? 0 : object.hashCode();
            object = this.value;
            if (object != null) {
                n = object.hashCode();
            }
            return n2 ^ n;
        }

        public Node<K, V> last() {
            Node<K, V> node = this;
            Node<K, V> node2 = node.right;
            while (node2 != null) {
                Node<K, V> node3 = node2.right;
                node = node2;
                node2 = node3;
            }
            return node;
        }

        @Override
        public V setValue(V v) {
            V v2 = this.value;
            this.value = v;
            return v2;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.key);
            stringBuilder.append("=");
            stringBuilder.append(this.value);
            return stringBuilder.toString();
        }
    }
}

