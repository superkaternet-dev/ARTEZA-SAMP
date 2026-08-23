/*
 * Decompiled with CFR 0.152.
 */
package androidx.collection;

import androidx.collection.ContainerHelpers;
import java.util.ConcurrentModificationException;
import java.util.Map;

public class SimpleArrayMap<K, V> {
    private static final int BASE_SIZE = 4;
    private static final int CACHE_SIZE = 10;
    private static final boolean CONCURRENT_MODIFICATION_EXCEPTIONS = true;
    private static final boolean DEBUG = false;
    private static final String TAG = "ArrayMap";
    static Object[] mBaseCache;
    static int mBaseCacheSize;
    static Object[] mTwiceBaseCache;
    static int mTwiceBaseCacheSize;
    Object[] mArray;
    int[] mHashes;
    int mSize;

    public SimpleArrayMap() {
        this.mHashes = ContainerHelpers.EMPTY_INTS;
        this.mArray = ContainerHelpers.EMPTY_OBJECTS;
        this.mSize = 0;
    }

    public SimpleArrayMap(int n) {
        if (n == 0) {
            this.mHashes = ContainerHelpers.EMPTY_INTS;
            this.mArray = ContainerHelpers.EMPTY_OBJECTS;
        } else {
            this.allocArrays(n);
        }
        this.mSize = 0;
    }

    public SimpleArrayMap(SimpleArrayMap<K, V> simpleArrayMap) {
        this();
        if (simpleArrayMap != null) {
            this.putAll(simpleArrayMap);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void allocArrays(int n) {
        if (n == 8) {
            synchronized (SimpleArrayMap.class) {
                Object[] objectArray = mTwiceBaseCache;
                if (objectArray != null) {
                    this.mArray = objectArray;
                    mTwiceBaseCache = (Object[])objectArray[0];
                    this.mHashes = (int[])objectArray[1];
                    objectArray[1] = null;
                    objectArray[0] = null;
                    --mTwiceBaseCacheSize;
                    return;
                }
            }
        } else if (n == 4) {
            synchronized (SimpleArrayMap.class) {
                Object[] objectArray = mBaseCache;
                if (objectArray != null) {
                    this.mArray = objectArray;
                    mBaseCache = (Object[])objectArray[0];
                    this.mHashes = (int[])objectArray[1];
                    objectArray[1] = null;
                    objectArray[0] = null;
                    --mBaseCacheSize;
                    return;
                }
            }
        }
        this.mHashes = new int[n];
        this.mArray = new Object[n << 1];
    }

    private static int binarySearchHashes(int[] nArray, int n, int n2) {
        try {
            n = ContainerHelpers.binarySearch(nArray, n, n2);
            return n;
        }
        catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
            throw new ConcurrentModificationException();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static void freeArrays(int[] nArray, Object[] objectArray, int n) {
        if (nArray.length == 8) {
            synchronized (SimpleArrayMap.class) {
                if (mTwiceBaseCacheSize >= 10) return;
                objectArray[0] = mTwiceBaseCache;
                objectArray[1] = nArray;
                n = (n << 1) - 1;
                while (true) {
                    if (n < 2) {
                        mTwiceBaseCache = objectArray;
                        ++mTwiceBaseCacheSize;
                        return;
                    }
                    objectArray[n] = null;
                    --n;
                }
            }
        }
        if (nArray.length != 4) return;
        synchronized (SimpleArrayMap.class) {
            if (mBaseCacheSize >= 10) return;
            objectArray[0] = mBaseCache;
            objectArray[1] = nArray;
            n = (n << 1) - 1;
            while (true) {
                if (n < 2) {
                    mBaseCache = objectArray;
                    ++mBaseCacheSize;
                    return;
                }
                objectArray[n] = null;
                --n;
            }
        }
    }

    public void clear() {
        if (this.mSize > 0) {
            int[] nArray = this.mHashes;
            Object[] objectArray = this.mArray;
            int n = this.mSize;
            this.mHashes = ContainerHelpers.EMPTY_INTS;
            this.mArray = ContainerHelpers.EMPTY_OBJECTS;
            this.mSize = 0;
            SimpleArrayMap.freeArrays(nArray, objectArray, n);
        }
        if (this.mSize <= 0) {
            return;
        }
        throw new ConcurrentModificationException();
    }

    public boolean containsKey(Object object) {
        boolean bl = this.indexOfKey(object) >= 0;
        return bl;
    }

    public boolean containsValue(Object object) {
        boolean bl = this.indexOfValue(object) >= 0;
        return bl;
    }

    public void ensureCapacity(int n) {
        int n2 = this.mSize;
        if (this.mHashes.length < n) {
            int[] nArray = this.mHashes;
            Object[] objectArray = this.mArray;
            this.allocArrays(n);
            if (this.mSize > 0) {
                System.arraycopy(nArray, 0, this.mHashes, 0, n2);
                System.arraycopy(objectArray, 0, this.mArray, 0, n2 << 1);
            }
            SimpleArrayMap.freeArrays(nArray, objectArray, n2);
        }
        if (this.mSize == n2) {
            return;
        }
        throw new ConcurrentModificationException();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof SimpleArrayMap)) {
            if (!(object instanceof Map)) {
                return false;
            }
            object = (Map)object;
            if (this.size() != object.size()) {
                return false;
            }
        } else {
            object = (SimpleArrayMap)object;
            if (this.size() != ((SimpleArrayMap)object).size()) {
                return false;
            }
            int n = 0;
            try {
                while (true) {
                    boolean bl;
                    if (n >= this.mSize) {
                        return true;
                    }
                    K k = this.keyAt(n);
                    V v2 = this.valueAt(n);
                    V v = ((SimpleArrayMap)object).get(k);
                    if (v2 == null ? v != null || !((SimpleArrayMap)object).containsKey(k) : !(bl = v2.equals(v))) {
                        return false;
                    }
                    ++n;
                }
            }
            catch (ClassCastException classCastException) {
                return false;
            }
            catch (NullPointerException nullPointerException) {
                return false;
            }
        }
        int n = 0;
        try {
            while (true) {
                boolean bl;
                if (n >= this.mSize) {
                    return true;
                }
                K k = this.keyAt(n);
                V v3 = this.valueAt(n);
                Object v = object.get(k);
                if (v3 == null ? v != null || !object.containsKey(k) : !(bl = v3.equals(v))) {
                    return false;
                }
                ++n;
            }
        }
        catch (ClassCastException classCastException) {
            return false;
        }
        catch (NullPointerException nullPointerException) {
            return false;
        }
    }

    public V get(Object object) {
        return this.getOrDefault(object, null);
    }

    public V getOrDefault(Object object, V v) {
        int n = this.indexOfKey(object);
        object = n >= 0 ? this.mArray[(n << 1) + 1] : v;
        return (V)object;
    }

    public int hashCode() {
        int[] nArray = this.mHashes;
        Object[] objectArray = this.mArray;
        int n = 0;
        int n2 = 0;
        int n3 = 1;
        int n4 = this.mSize;
        while (n2 < n4) {
            Object object = objectArray[n3];
            int n5 = nArray[n2];
            int n6 = object == null ? 0 : object.hashCode();
            n += n5 ^ n6;
            ++n2;
            n3 += 2;
        }
        return n;
    }

    int indexOf(Object object, int n) {
        int n2;
        int n3 = this.mSize;
        if (n3 == 0) {
            return -1;
        }
        int n4 = SimpleArrayMap.binarySearchHashes(this.mHashes, n3, n);
        if (n4 < 0) {
            return n4;
        }
        if (object.equals(this.mArray[n4 << 1])) {
            return n4;
        }
        for (n2 = n4 + 1; n2 < n3 && this.mHashes[n2] == n; ++n2) {
            if (!object.equals(this.mArray[n2 << 1])) continue;
            return n2;
        }
        for (n3 = n4 - 1; n3 >= 0 && this.mHashes[n3] == n; --n3) {
            if (!object.equals(this.mArray[n3 << 1])) continue;
            return n3;
        }
        return ~n2;
    }

    public int indexOfKey(Object object) {
        int n = object == null ? this.indexOfNull() : this.indexOf(object, object.hashCode());
        return n;
    }

    int indexOfNull() {
        int n;
        int n2 = this.mSize;
        if (n2 == 0) {
            return -1;
        }
        int n3 = SimpleArrayMap.binarySearchHashes(this.mHashes, n2, 0);
        if (n3 < 0) {
            return n3;
        }
        if (this.mArray[n3 << 1] == null) {
            return n3;
        }
        for (n = n3 + 1; n < n2 && this.mHashes[n] == 0; ++n) {
            if (this.mArray[n << 1] != null) continue;
            return n;
        }
        --n3;
        while (n3 >= 0 && this.mHashes[n3] == 0) {
            if (this.mArray[n3 << 1] == null) {
                return n3;
            }
            --n3;
        }
        return ~n;
    }

    int indexOfValue(Object object) {
        int n = this.mSize * 2;
        Object[] objectArray = this.mArray;
        if (object == null) {
            for (int i = 1; i < n; i += 2) {
                if (objectArray[i] != null) continue;
                return i >> 1;
            }
        } else {
            for (int i = 1; i < n; i += 2) {
                if (!object.equals(objectArray[i])) continue;
                return i >> 1;
            }
        }
        return -1;
    }

    public boolean isEmpty() {
        boolean bl = this.mSize <= 0;
        return bl;
    }

    public K keyAt(int n) {
        return (K)this.mArray[n << 1];
    }

    public V put(K object, V v) {
        Object[] objectArray;
        int n;
        int n2;
        int n3 = this.mSize;
        if (object == null) {
            n2 = 0;
            n = this.indexOfNull();
        } else {
            n2 = object.hashCode();
            n = this.indexOf(object, n2);
        }
        if (n >= 0) {
            n = (n << 1) + 1;
            object = this.mArray;
            K k = object[n];
            object[n] = v;
            return (V)k;
        }
        int n4 = ~n;
        if (n3 >= this.mHashes.length) {
            n = 4;
            if (n3 >= 8) {
                n = (n3 >> 1) + n3;
            } else if (n3 >= 4) {
                n = 8;
            }
            objectArray = this.mHashes;
            Object[] objectArray2 = this.mArray;
            this.allocArrays(n);
            if (n3 == this.mSize) {
                int[] nArray = this.mHashes;
                if (nArray.length > 0) {
                    System.arraycopy(objectArray, 0, nArray, 0, objectArray.length);
                    System.arraycopy(objectArray2, 0, this.mArray, 0, objectArray2.length);
                }
                SimpleArrayMap.freeArrays(objectArray, objectArray2, n3);
            } else {
                throw new ConcurrentModificationException();
            }
        }
        if (n4 < n3) {
            objectArray = this.mHashes;
            System.arraycopy(objectArray, n4, objectArray, n4 + 1, n3 - n4);
            objectArray = this.mArray;
            System.arraycopy(objectArray, n4 << 1, objectArray, n4 + 1 << 1, this.mSize - n4 << 1);
        }
        if (n3 == (n = this.mSize) && n4 < (objectArray = this.mHashes).length) {
            objectArray[n4] = n2;
            objectArray = this.mArray;
            objectArray[n4 << 1] = (int)object;
            objectArray[(n4 << 1) + 1] = (int)v;
            this.mSize = n + 1;
            return null;
        }
        throw new ConcurrentModificationException();
    }

    public void putAll(SimpleArrayMap<? extends K, ? extends V> simpleArrayMap) {
        int n = simpleArrayMap.mSize;
        this.ensureCapacity(this.mSize + n);
        if (this.mSize == 0) {
            if (n > 0) {
                System.arraycopy(simpleArrayMap.mHashes, 0, this.mHashes, 0, n);
                System.arraycopy(simpleArrayMap.mArray, 0, this.mArray, 0, n << 1);
                this.mSize = n;
            }
        } else {
            for (int i = 0; i < n; ++i) {
                this.put(simpleArrayMap.keyAt(i), simpleArrayMap.valueAt(i));
            }
        }
    }

    public V putIfAbsent(K k, V v) {
        V v2;
        V v3 = v2 = this.get(k);
        if (v2 == null) {
            v3 = this.put(k, v);
        }
        return v3;
    }

    public V remove(Object object) {
        int n = this.indexOfKey(object);
        if (n >= 0) {
            return this.removeAt(n);
        }
        return null;
    }

    public boolean remove(Object object, Object object2) {
        int n = this.indexOfKey(object);
        if (n >= 0 && (object2 == (object = this.valueAt(n)) || object2 != null && object2.equals(object))) {
            this.removeAt(n);
            return true;
        }
        return false;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public V removeAt(int n) {
        Object[] objectArray = this.mArray;
        Object object = objectArray[(n << 1) + 1];
        int n2 = this.mSize;
        if (n2 <= 1) {
            SimpleArrayMap.freeArrays(this.mHashes, objectArray, n2);
            this.mHashes = ContainerHelpers.EMPTY_INTS;
            this.mArray = ContainerHelpers.EMPTY_OBJECTS;
            n = 0;
        } else {
            int n3 = n2 - 1;
            int[] nArray = this.mHashes;
            int n4 = nArray.length;
            int n5 = 8;
            if (n4 > 8 && this.mSize < nArray.length / 3) {
                if (n2 > 8) {
                    n5 = n2 + (n2 >> 1);
                }
                int[] nArray2 = this.mHashes;
                Object[] objectArray2 = this.mArray;
                this.allocArrays(n5);
                if (n2 != this.mSize) throw new ConcurrentModificationException();
                if (n > 0) {
                    System.arraycopy(nArray2, 0, this.mHashes, 0, n);
                    System.arraycopy(objectArray2, 0, this.mArray, 0, n << 1);
                }
                if (n < n3) {
                    System.arraycopy(nArray2, n + 1, this.mHashes, n, n3 - n);
                    System.arraycopy(objectArray2, n + 1 << 1, this.mArray, n << 1, n3 - n << 1);
                }
                n = n3;
            } else {
                if (n < n3) {
                    System.arraycopy(nArray, n + 1, nArray, n, n3 - n);
                    Object[] objectArray3 = this.mArray;
                    System.arraycopy(objectArray3, n + 1 << 1, objectArray3, n << 1, n3 - n << 1);
                }
                Object[] objectArray4 = this.mArray;
                objectArray4[n3 << 1] = null;
                objectArray4[(n3 << 1) + 1] = null;
                n = n3;
            }
        }
        if (n2 != this.mSize) throw new ConcurrentModificationException();
        this.mSize = n;
        return (V)object;
    }

    public V replace(K k, V v) {
        int n = this.indexOfKey(k);
        if (n >= 0) {
            return this.setValueAt(n, v);
        }
        return null;
    }

    public boolean replace(K object, V v, V v2) {
        int n = this.indexOfKey(object);
        if (n >= 0 && ((object = this.valueAt(n)) == v || v != null && v.equals(object))) {
            this.setValueAt(n, v2);
            return true;
        }
        return false;
    }

    public V setValueAt(int n, V v) {
        n = (n << 1) + 1;
        Object[] objectArray = this.mArray;
        Object object = objectArray[n];
        objectArray[n] = v;
        return (V)object;
    }

    public int size() {
        return this.mSize;
    }

    public String toString() {
        if (this.isEmpty()) {
            return "{}";
        }
        StringBuilder stringBuilder = new StringBuilder(this.mSize * 28);
        stringBuilder.append('{');
        for (int i = 0; i < this.mSize; ++i) {
            Object object;
            if (i > 0) {
                stringBuilder.append(", ");
            }
            if ((object = this.keyAt(i)) != this) {
                stringBuilder.append(object);
            } else {
                stringBuilder.append("(this Map)");
            }
            stringBuilder.append('=');
            object = this.valueAt(i);
            if (object != this) {
                stringBuilder.append(object);
                continue;
            }
            stringBuilder.append("(this Map)");
        }
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    public V valueAt(int n) {
        return (V)this.mArray[(n << 1) + 1];
    }
}

