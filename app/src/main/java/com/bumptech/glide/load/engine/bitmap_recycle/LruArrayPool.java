/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package com.bumptech.glide.load.engine.bitmap_recycle;

import android.util.Log;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayAdapterInterface;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BaseKeyPool;
import com.bumptech.glide.load.engine.bitmap_recycle.ByteArrayAdapter;
import com.bumptech.glide.load.engine.bitmap_recycle.GroupedLinkedMap;
import com.bumptech.glide.load.engine.bitmap_recycle.IntegerArrayAdapter;
import com.bumptech.glide.load.engine.bitmap_recycle.Poolable;
import com.bumptech.glide.util.Preconditions;
import java.lang.constant.Constable;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public final class LruArrayPool
implements ArrayPool {
    private static final int DEFAULT_SIZE = 0x400000;
    static final int MAX_OVER_SIZE_MULTIPLE = 8;
    private static final int SINGLE_ARRAY_MAX_SIZE_DIVISOR = 2;
    private final Map<Class<?>, ArrayAdapterInterface<?>> adapters;
    private int currentSize;
    private final GroupedLinkedMap<Key, Object> groupedMap = new GroupedLinkedMap();
    private final KeyPool keyPool = new KeyPool();
    private final int maxSize;
    private final Map<Class<?>, NavigableMap<Integer, Integer>> sortedSizes = new HashMap();

    public LruArrayPool() {
        this.adapters = new HashMap();
        this.maxSize = 0x400000;
    }

    public LruArrayPool(int n) {
        this.adapters = new HashMap();
        this.maxSize = n;
    }

    private void decrementArrayOfSize(int n, Class<?> object) {
        Integer n2 = (Integer)(object = this.getSizesForAdapter((Class<?>)object)).get(n);
        if (n2 != null) {
            if (n2 == 1) {
                object.remove(n);
            } else {
                object.put(n, n2 - 1);
            }
            return;
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("Tried to decrement empty size, size: ");
        ((StringBuilder)object).append(n);
        ((StringBuilder)object).append(", this: ");
        ((StringBuilder)object).append(this);
        throw new NullPointerException(((StringBuilder)object).toString());
    }

    private void evict() {
        this.evictToSize(this.maxSize);
    }

    private void evictToSize(int n) {
        while (this.currentSize > n) {
            Object object = this.groupedMap.removeLast();
            Preconditions.checkNotNull(object);
            ArrayAdapterInterface<Object> arrayAdapterInterface = this.getAdapterFromObject(object);
            this.currentSize -= arrayAdapterInterface.getArrayLength(object) * arrayAdapterInterface.getElementSizeInBytes();
            this.decrementArrayOfSize(arrayAdapterInterface.getArrayLength(object), object.getClass());
            if (!Log.isLoggable((String)arrayAdapterInterface.getTag(), (int)2)) continue;
            String string2 = arrayAdapterInterface.getTag();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("evicted: ");
            stringBuilder.append(arrayAdapterInterface.getArrayLength(object));
            Log.v((String)string2, (String)stringBuilder.toString());
        }
    }

    private <T> ArrayAdapterInterface<T> getAdapterFromObject(T t) {
        return this.getAdapterFromType(t.getClass());
    }

    private <T> ArrayAdapterInterface<T> getAdapterFromType(Class<T> clazz) {
        Object object;
        block4: {
            IntegerArrayAdapter integerArrayAdapter = this.adapters.get(clazz);
            object = integerArrayAdapter;
            if (integerArrayAdapter != null) break block4;
            if (clazz.equals(int[].class)) {
                object = new IntegerArrayAdapter();
            } else {
                if (!clazz.equals(byte[].class)) {
                    object = new StringBuilder();
                    ((StringBuilder)object).append("No array pool found for: ");
                    ((StringBuilder)object).append(clazz.getSimpleName());
                    throw new IllegalArgumentException(((StringBuilder)object).toString());
                }
                object = new ByteArrayAdapter();
            }
            this.adapters.put(clazz, (ArrayAdapterInterface<?>)object);
        }
        return object;
    }

    private <T> T getArrayForKey(Key key) {
        return (T)this.groupedMap.get(key);
    }

    private <T> T getForKey(Key key, Class<T> serializable) {
        ArrayAdapterInterface<T> arrayAdapterInterface = this.getAdapterFromType((Class<T>)serializable);
        Object object = this.getArrayForKey(key);
        if (object != null) {
            this.currentSize -= arrayAdapterInterface.getArrayLength(object) * arrayAdapterInterface.getElementSizeInBytes();
            this.decrementArrayOfSize(arrayAdapterInterface.getArrayLength(object), (Class<?>)serializable);
        }
        serializable = object;
        if (object == null) {
            if (Log.isLoggable((String)arrayAdapterInterface.getTag(), (int)2)) {
                object = arrayAdapterInterface.getTag();
                serializable = new StringBuilder();
                ((StringBuilder)serializable).append("Allocated ");
                ((StringBuilder)serializable).append(key.size);
                ((StringBuilder)serializable).append(" bytes");
                Log.v(object, (String)((StringBuilder)serializable).toString());
            }
            serializable = arrayAdapterInterface.newArray(key.size);
        }
        return (T)serializable;
    }

    private NavigableMap<Integer, Integer> getSizesForAdapter(Class<?> clazz) {
        NavigableMap<Integer, Integer> navigableMap;
        NavigableMap<Integer, Integer> navigableMap2 = navigableMap = this.sortedSizes.get(clazz);
        if (navigableMap == null) {
            navigableMap2 = new TreeMap<Integer, Integer>();
            this.sortedSizes.put(clazz, navigableMap2);
        }
        return navigableMap2;
    }

    private boolean isNoMoreThanHalfFull() {
        int n = this.currentSize;
        boolean bl = n == 0 || this.maxSize / n >= 2;
        return bl;
    }

    private boolean isSmallEnoughForReuse(int n) {
        boolean bl = n <= this.maxSize / 2;
        return bl;
    }

    private boolean mayFillRequest(int n, Integer n2) {
        boolean bl = n2 != null && (this.isNoMoreThanHalfFull() || n2 <= n * 8);
        return bl;
    }

    @Override
    public void clearMemory() {
        synchronized (this) {
            this.evictToSize(0);
            return;
        }
    }

    @Override
    public <T> T get(int n, Class<T> clazz) {
        synchronized (this) {
            Object object = this.getSizesForAdapter(clazz).ceilingKey(n);
            object = this.mayFillRequest(n, (Integer)object) ? this.keyPool.get((Integer)object, clazz) : this.keyPool.get(n, clazz);
            clazz = this.getForKey((Key)object, clazz);
            return (T)clazz;
        }
    }

    int getCurrentSize() {
        int n = 0;
        for (Class<?> clazz : this.sortedSizes.keySet()) {
            for (Integer n2 : this.sortedSizes.get(clazz).keySet()) {
                ArrayAdapterInterface<?> arrayAdapterInterface = this.getAdapterFromType(clazz);
                n += n2 * (Integer)this.sortedSizes.get(clazz).get(n2) * arrayAdapterInterface.getElementSizeInBytes();
            }
        }
        return n;
    }

    @Override
    public <T> T getExact(int n, Class<T> clazz) {
        synchronized (this) {
            clazz = this.getForKey(this.keyPool.get(n, clazz), clazz);
            return (T)clazz;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public <T> void put(T object) {
        synchronized (this) {
            Constable constable = object.getClass();
            Object object2 = this.getAdapterFromType((Class<T>)constable);
            int n = object2.getArrayLength(object);
            int n2 = object2.getElementSizeInBytes() * n;
            boolean bl = this.isSmallEnoughForReuse(n2);
            if (!bl) {
                return;
            }
            object2 = this.keyPool.get(n, (Class<?>)constable);
            this.groupedMap.put((Key)object2, object);
            object = this.getSizesForAdapter((Class<?>)constable);
            constable = (Integer)object.get(((Key)object2).size);
            int n3 = ((Key)object2).size;
            n = 1;
            if (constable != null) {
                n = 1 + (Integer)constable;
            }
            object.put(n3, n);
            this.currentSize += n2;
            this.evict();
            return;
        }
    }

    @Override
    @Deprecated
    public <T> void put(T t, Class<T> clazz) {
        this.put(t);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void trimMemory(int n) {
        synchronized (this) {
            Throwable throwable2;
            block6: {
                block5: {
                    if (n >= 40) {
                        try {
                            this.clearMemory();
                            break block5;
                        }
                        catch (Throwable throwable2) {
                            break block6;
                        }
                    }
                    if (n >= 20 || n == 15) {
                        this.evictToSize(this.maxSize / 2);
                    }
                }
                return;
            }
            throw throwable2;
        }
    }

    private static final class Key
    implements Poolable {
        private Class<?> arrayClass;
        private final KeyPool pool;
        int size;

        Key(KeyPool keyPool) {
            this.pool = keyPool;
        }

        public boolean equals(Object object) {
            boolean bl = object instanceof Key;
            boolean bl2 = false;
            if (bl) {
                object = (Key)object;
                bl = bl2;
                if (this.size == ((Key)object).size) {
                    bl = bl2;
                    if (this.arrayClass == ((Key)object).arrayClass) {
                        bl = true;
                    }
                }
                return bl;
            }
            return false;
        }

        public int hashCode() {
            int n = this.size;
            Class<?> clazz = this.arrayClass;
            int n2 = clazz != null ? clazz.hashCode() : 0;
            return n * 31 + n2;
        }

        void init(int n, Class<?> clazz) {
            this.size = n;
            this.arrayClass = clazz;
        }

        @Override
        public void offer() {
            this.pool.offer(this);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Key{size=");
            stringBuilder.append(this.size);
            stringBuilder.append("array=");
            stringBuilder.append(this.arrayClass);
            stringBuilder.append('}');
            return stringBuilder.toString();
        }
    }

    private static final class KeyPool
    extends BaseKeyPool<Key> {
        KeyPool() {
        }

        @Override
        protected Key create() {
            return new Key(this);
        }

        Key get(int n, Class<?> clazz) {
            Key key = (Key)this.get();
            key.init(n, clazz);
            return key;
        }
    }
}

