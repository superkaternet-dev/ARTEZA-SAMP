/*
 * Decompiled with CFR 0.152.
 */
package androidx.collection;

import androidx.collection.MapCollections;
import androidx.collection.SimpleArrayMap;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class ArrayMap<K, V>
extends SimpleArrayMap<K, V>
implements Map<K, V> {
    MapCollections<K, V> mCollections;

    public ArrayMap() {
    }

    public ArrayMap(int n) {
        super(n);
    }

    public ArrayMap(SimpleArrayMap simpleArrayMap) {
        super(simpleArrayMap);
    }

    private MapCollections<K, V> getCollection() {
        if (this.mCollections == null) {
            this.mCollections = new MapCollections<K, V>(this){
                final ArrayMap this$0;
                {
                    this.this$0 = arrayMap;
                }

                @Override
                protected void colClear() {
                    this.this$0.clear();
                }

                @Override
                protected Object colGetEntry(int n, int n2) {
                    return this.this$0.mArray[(n << 1) + n2];
                }

                @Override
                protected Map<K, V> colGetMap() {
                    return this.this$0;
                }

                @Override
                protected int colGetSize() {
                    return this.this$0.mSize;
                }

                @Override
                protected int colIndexOfKey(Object object) {
                    return this.this$0.indexOfKey(object);
                }

                @Override
                protected int colIndexOfValue(Object object) {
                    return this.this$0.indexOfValue(object);
                }

                @Override
                protected void colPut(K k, V v) {
                    this.this$0.put(k, v);
                }

                @Override
                protected void colRemoveAt(int n) {
                    this.this$0.removeAt(n);
                }

                @Override
                protected V colSetValue(int n, V v) {
                    return this.this$0.setValueAt(n, v);
                }
            };
        }
        return this.mCollections;
    }

    public boolean containsAll(Collection<?> collection) {
        return MapCollections.containsAllHelper(this, collection);
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return this.getCollection().getEntrySet();
    }

    @Override
    public Set<K> keySet() {
        return this.getCollection().getKeySet();
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> object2) {
        this.ensureCapacity(this.mSize + object2.size());
        for (Map.Entry entry : object2.entrySet()) {
            this.put(entry.getKey(), entry.getValue());
        }
    }

    public boolean removeAll(Collection<?> collection) {
        return MapCollections.removeAllHelper(this, collection);
    }

    public boolean retainAll(Collection<?> collection) {
        return MapCollections.retainAllHelper(this, collection);
    }

    @Override
    public Collection<V> values() {
        return this.getCollection().getValues();
    }
}

