/*
 * Decompiled with CFR 0.152.
 */
package org.ini4j;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.ini4j.MultiMap;

public class BasicMultiMap<K, V>
implements MultiMap<K, V>,
Serializable {
    private static final long serialVersionUID = 4716749660560043989L;
    private final Map<K, List<V>> _impl;

    public BasicMultiMap() {
        this(new LinkedHashMap());
    }

    public BasicMultiMap(Map<K, List<V>> map) {
        this._impl = map;
    }

    private List<V> getList(Object object, boolean bl) {
        List<V> list;
        List<V> list2 = list = this._impl.get(object);
        if (list == null) {
            list2 = list;
            if (bl) {
                list2 = new ArrayList<V>();
                this._impl.put(object, list2);
            }
        }
        return list2;
    }

    @Override
    public void add(K k, V v) {
        this.getList(k, true).add(v);
    }

    @Override
    public void add(K k, V v, int n) {
        this.getList(k, true).add(n, v);
    }

    @Override
    public void clear() {
        this._impl.clear();
    }

    @Override
    public boolean containsKey(Object object) {
        return this._impl.containsKey(object);
    }

    @Override
    public boolean containsValue(Object object) {
        boolean bl;
        block1: {
            boolean bl2 = false;
            Iterator<List<V>> iterator2 = this._impl.values().iterator();
            do {
                bl = bl2;
                if (!iterator2.hasNext()) break block1;
            } while (!iterator2.next().contains(object));
            bl = true;
        }
        return bl;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        HashSet<Map.Entry<K, V>> hashSet = new HashSet<Map.Entry<K, V>>();
        Iterator<K> iterator2 = this.keySet().iterator();
        while (iterator2.hasNext()) {
            hashSet.add(new ShadowEntry(this, iterator2.next()));
        }
        return hashSet;
    }

    @Override
    public V get(Object list) {
        list = (list = this.getList(list, false)) == null ? null : list.get(list.size() - 1);
        return (V)list;
    }

    @Override
    public V get(Object list, int n) {
        list = (list = this.getList(list, false)) == null ? null : list.get(n);
        return (V)list;
    }

    @Override
    public List<V> getAll(Object object) {
        return this._impl.get(object);
    }

    @Override
    public boolean isEmpty() {
        return this._impl.isEmpty();
    }

    @Override
    public Set<K> keySet() {
        return this._impl.keySet();
    }

    @Override
    public int length(Object list) {
        int n = 0;
        if ((list = this.getList(list, false)) != null) {
            n = list.size();
        }
        return n;
    }

    @Override
    public V put(K object, V v) {
        Object var3_3 = null;
        if ((object = this.getList(object, true)).isEmpty()) {
            object.add(v);
            object = var3_3;
        } else {
            object = object.set(object.size() - 1, v);
        }
        return (V)object;
    }

    @Override
    public V put(K k, V v, int n) {
        return this.getList(k, false).set(n, v);
    }

    @Override
    public List<V> putAll(K k, List<V> list) {
        List<V> list2 = this._impl.get(k);
        this._impl.put(k, new ArrayList<V>(list));
        return list2;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> object) {
        if (object instanceof MultiMap) {
            MultiMap multiMap = (MultiMap)object;
            for (Object e : multiMap.keySet()) {
                this.putAll(e, multiMap.getAll(e));
            }
        } else {
            for (K k : object.keySet()) {
                this.put(k, object.get(k));
            }
        }
    }

    @Override
    public V remove(Object list) {
        list = (list = this._impl.remove(list)) == null ? null : list.get(0);
        return (V)list;
    }

    @Override
    public V remove(Object object, int n) {
        V v = null;
        List<V> list = this.getList(object, false);
        if (list != null) {
            V v2 = list.remove(n);
            v = v2;
            if (list.isEmpty()) {
                this._impl.remove(object);
                v = v2;
            }
        }
        return v;
    }

    @Override
    public int size() {
        return this._impl.size();
    }

    public String toString() {
        return this._impl.toString();
    }

    @Override
    public Collection<V> values() {
        ArrayList<V> arrayList = new ArrayList<V>(this._impl.size());
        Iterator<List<V>> iterator2 = this._impl.values().iterator();
        while (iterator2.hasNext()) {
            arrayList.addAll(iterator2.next());
        }
        return arrayList;
    }

    class ShadowEntry
    implements Map.Entry<K, V> {
        private final K _key;
        final BasicMultiMap this$0;

        ShadowEntry(BasicMultiMap basicMultiMap, K k) {
            this.this$0 = basicMultiMap;
            this._key = k;
        }

        @Override
        public K getKey() {
            return this._key;
        }

        @Override
        public V getValue() {
            return this.this$0.get(this._key);
        }

        @Override
        public V setValue(V v) {
            return this.this$0.put(this._key, v);
        }
    }
}

