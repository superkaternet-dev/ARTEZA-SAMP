/*
 * Decompiled with CFR 0.152.
 */
package androidx.collection;

import androidx.collection.ContainerHelpers;
import androidx.collection.MapCollections;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public final class ArraySet<E>
implements Collection<E>,
Set<E> {
    private static final int BASE_SIZE = 4;
    private static final int CACHE_SIZE = 10;
    private static final boolean DEBUG = false;
    private static final int[] INT = new int[0];
    private static final Object[] OBJECT = new Object[0];
    private static final String TAG = "ArraySet";
    private static Object[] sBaseCache;
    private static int sBaseCacheSize;
    private static Object[] sTwiceBaseCache;
    private static int sTwiceBaseCacheSize;
    Object[] mArray;
    private MapCollections<E, E> mCollections;
    private int[] mHashes;
    int mSize;

    public ArraySet() {
        this(0);
    }

    public ArraySet(int n) {
        if (n == 0) {
            this.mHashes = INT;
            this.mArray = OBJECT;
        } else {
            this.allocArrays(n);
        }
        this.mSize = 0;
    }

    public ArraySet(ArraySet<E> arraySet) {
        this();
        if (arraySet != null) {
            this.addAll(arraySet);
        }
    }

    public ArraySet(Collection<E> collection) {
        this();
        if (collection != null) {
            this.addAll(collection);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void allocArrays(int n) {
        if (n == 8) {
            synchronized (ArraySet.class) {
                Object[] objectArray = sTwiceBaseCache;
                if (objectArray != null) {
                    this.mArray = objectArray;
                    sTwiceBaseCache = (Object[])objectArray[0];
                    this.mHashes = (int[])objectArray[1];
                    objectArray[1] = null;
                    objectArray[0] = null;
                    --sTwiceBaseCacheSize;
                    return;
                }
            }
        } else if (n == 4) {
            synchronized (ArraySet.class) {
                Object[] objectArray = sBaseCache;
                if (objectArray != null) {
                    this.mArray = objectArray;
                    sBaseCache = (Object[])objectArray[0];
                    this.mHashes = (int[])objectArray[1];
                    objectArray[1] = null;
                    objectArray[0] = null;
                    --sBaseCacheSize;
                    return;
                }
            }
        }
        this.mHashes = new int[n];
        this.mArray = new Object[n];
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static void freeArrays(int[] nArray, Object[] objectArray, int n) {
        if (nArray.length == 8) {
            synchronized (ArraySet.class) {
                if (sTwiceBaseCacheSize >= 10) return;
                objectArray[0] = sTwiceBaseCache;
                objectArray[1] = nArray;
                --n;
                while (true) {
                    if (n < 2) {
                        sTwiceBaseCache = objectArray;
                        ++sTwiceBaseCacheSize;
                        return;
                    }
                    objectArray[n] = null;
                    --n;
                }
            }
        }
        if (nArray.length != 4) return;
        synchronized (ArraySet.class) {
            if (sBaseCacheSize >= 10) return;
            objectArray[0] = sBaseCache;
            objectArray[1] = nArray;
            --n;
            while (true) {
                if (n < 2) {
                    sBaseCache = objectArray;
                    ++sBaseCacheSize;
                    return;
                }
                objectArray[n] = null;
                --n;
            }
        }
    }

    private MapCollections<E, E> getCollection() {
        if (this.mCollections == null) {
            this.mCollections = new MapCollections<E, E>(this){
                final ArraySet this$0;
                {
                    this.this$0 = arraySet;
                }

                @Override
                protected void colClear() {
                    this.this$0.clear();
                }

                @Override
                protected Object colGetEntry(int n, int n2) {
                    return this.this$0.mArray[n];
                }

                @Override
                protected Map<E, E> colGetMap() {
                    throw new UnsupportedOperationException("not a map");
                }

                @Override
                protected int colGetSize() {
                    return this.this$0.mSize;
                }

                @Override
                protected int colIndexOfKey(Object object) {
                    return this.this$0.indexOf(object);
                }

                @Override
                protected int colIndexOfValue(Object object) {
                    return this.this$0.indexOf(object);
                }

                @Override
                protected void colPut(E e, E e2) {
                    this.this$0.add(e);
                }

                @Override
                protected void colRemoveAt(int n) {
                    this.this$0.removeAt(n);
                }

                @Override
                protected E colSetValue(int n, E e) {
                    throw new UnsupportedOperationException("not a map");
                }
            };
        }
        return this.mCollections;
    }

    private int indexOf(Object object, int n) {
        int n2;
        int n3 = this.mSize;
        if (n3 == 0) {
            return -1;
        }
        int n4 = ContainerHelpers.binarySearch(this.mHashes, n3, n);
        if (n4 < 0) {
            return n4;
        }
        if (object.equals(this.mArray[n4])) {
            return n4;
        }
        for (n2 = n4 + 1; n2 < n3 && this.mHashes[n2] == n; ++n2) {
            if (!object.equals(this.mArray[n2])) continue;
            return n2;
        }
        for (n3 = n4 - 1; n3 >= 0 && this.mHashes[n3] == n; --n3) {
            if (!object.equals(this.mArray[n3])) continue;
            return n3;
        }
        return ~n2;
    }

    private int indexOfNull() {
        int n;
        int n2 = this.mSize;
        if (n2 == 0) {
            return -1;
        }
        int n3 = ContainerHelpers.binarySearch(this.mHashes, n2, 0);
        if (n3 < 0) {
            return n3;
        }
        if (this.mArray[n3] == null) {
            return n3;
        }
        for (n = n3 + 1; n < n2 && this.mHashes[n] == 0; ++n) {
            if (this.mArray[n] != null) continue;
            return n;
        }
        for (n2 = n3 - 1; n2 >= 0 && this.mHashes[n2] == 0; --n2) {
            if (this.mArray[n2] != null) continue;
            return n2;
        }
        return ~n;
    }

    @Override
    public boolean add(E e) {
        Object[] objectArray;
        int n;
        int n2;
        if (e == null) {
            n2 = 0;
            n = this.indexOfNull();
        } else {
            n2 = e.hashCode();
            n = this.indexOf(e, n2);
        }
        if (n >= 0) {
            return false;
        }
        int n3 = ~n;
        int n4 = this.mSize;
        if (n4 >= this.mHashes.length) {
            n = 4;
            if (n4 >= 8) {
                n = (n4 >> 1) + n4;
            } else if (n4 >= 4) {
                n = 8;
            }
            objectArray = this.mHashes;
            Object[] objectArray2 = this.mArray;
            this.allocArrays(n);
            int[] nArray = this.mHashes;
            if (nArray.length > 0) {
                System.arraycopy(objectArray, 0, nArray, 0, objectArray.length);
                System.arraycopy(objectArray2, 0, this.mArray, 0, objectArray2.length);
            }
            ArraySet.freeArrays(objectArray, objectArray2, this.mSize);
        }
        if (n3 < (n = this.mSize)) {
            objectArray = this.mHashes;
            System.arraycopy(objectArray, n3, objectArray, n3 + 1, n - n3);
            objectArray = this.mArray;
            System.arraycopy(objectArray, n3, objectArray, n3 + 1, this.mSize - n3);
        }
        this.mHashes[n3] = n2;
        this.mArray[n3] = e;
        ++this.mSize;
        return true;
    }

    public void addAll(ArraySet<? extends E> arraySet) {
        int n = arraySet.mSize;
        this.ensureCapacity(this.mSize + n);
        if (this.mSize == 0) {
            if (n > 0) {
                System.arraycopy(arraySet.mHashes, 0, this.mHashes, 0, n);
                System.arraycopy(arraySet.mArray, 0, this.mArray, 0, n);
                this.mSize = n;
            }
        } else {
            for (int i = 0; i < n; ++i) {
                this.add(arraySet.valueAt(i));
            }
        }
    }

    @Override
    public boolean addAll(Collection<? extends E> object) {
        this.ensureCapacity(this.mSize + object.size());
        boolean bl = false;
        object = object.iterator();
        while (object.hasNext()) {
            bl |= this.add(object.next());
        }
        return bl;
    }

    @Override
    public void clear() {
        int n = this.mSize;
        if (n != 0) {
            ArraySet.freeArrays(this.mHashes, this.mArray, n);
            this.mHashes = INT;
            this.mArray = OBJECT;
            this.mSize = 0;
        }
    }

    @Override
    public boolean contains(Object object) {
        boolean bl = this.indexOf(object) >= 0;
        return bl;
    }

    @Override
    public boolean containsAll(Collection<?> object) {
        object = object.iterator();
        while (object.hasNext()) {
            if (this.contains(object.next())) continue;
            return false;
        }
        return true;
    }

    public void ensureCapacity(int n) {
        if (this.mHashes.length < n) {
            int[] nArray = this.mHashes;
            Object[] objectArray = this.mArray;
            this.allocArrays(n);
            n = this.mSize;
            if (n > 0) {
                System.arraycopy(nArray, 0, this.mHashes, 0, n);
                System.arraycopy(objectArray, 0, this.mArray, 0, this.mSize);
            }
            ArraySet.freeArrays(nArray, objectArray, this.mSize);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Set)) {
            return false;
        }
        object = (Set)object;
        if (this.size() != object.size()) {
            return false;
        }
        int n = 0;
        try {
            while (true) {
                if (n >= this.mSize) {
                    return true;
                }
                boolean bl = object.contains(this.valueAt(n));
                if (!bl) {
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

    @Override
    public int hashCode() {
        int[] nArray = this.mHashes;
        int n = 0;
        int n2 = this.mSize;
        for (int i = 0; i < n2; ++i) {
            n += nArray[i];
        }
        return n;
    }

    public int indexOf(Object object) {
        int n = object == null ? this.indexOfNull() : this.indexOf(object, object.hashCode());
        return n;
    }

    @Override
    public boolean isEmpty() {
        boolean bl = this.mSize <= 0;
        return bl;
    }

    @Override
    public Iterator<E> iterator() {
        return this.getCollection().getKeySet().iterator();
    }

    @Override
    public boolean remove(Object object) {
        int n = this.indexOf(object);
        if (n >= 0) {
            this.removeAt(n);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeAll(ArraySet<? extends E> arraySet) {
        int n = arraySet.mSize;
        int n2 = this.mSize;
        for (int i = 0; i < n; ++i) {
            this.remove(arraySet.valueAt(i));
        }
        boolean bl = n2 != this.mSize;
        return bl;
    }

    @Override
    public boolean removeAll(Collection<?> object) {
        boolean bl = false;
        object = object.iterator();
        while (object.hasNext()) {
            bl |= this.remove(object.next());
        }
        return bl;
    }

    public E removeAt(int n) {
        Object[] objectArray = this.mArray;
        Object object = objectArray[n];
        int n2 = this.mSize;
        if (n2 <= 1) {
            ArraySet.freeArrays(this.mHashes, objectArray, n2);
            this.mHashes = INT;
            this.mArray = OBJECT;
            this.mSize = 0;
        } else {
            objectArray = this.mHashes;
            int n3 = objectArray.length;
            int n4 = 8;
            if (n3 > 8 && n2 < objectArray.length / 3) {
                if (n2 > 8) {
                    n4 = n2 + (n2 >> 1);
                }
                int[] nArray = this.mHashes;
                objectArray = this.mArray;
                this.allocArrays(n4);
                --this.mSize;
                if (n > 0) {
                    System.arraycopy(nArray, 0, this.mHashes, 0, n);
                    System.arraycopy(objectArray, 0, this.mArray, 0, n);
                }
                if (n < (n4 = this.mSize)) {
                    System.arraycopy(nArray, n + 1, this.mHashes, n, n4 - n);
                    System.arraycopy(objectArray, n + 1, this.mArray, n, this.mSize - n);
                }
            } else {
                this.mSize = n4 = n2 - 1;
                if (n < n4) {
                    System.arraycopy(objectArray, n + 1, objectArray, n, n4 - n);
                    objectArray = this.mArray;
                    System.arraycopy(objectArray, n + 1, objectArray, n, this.mSize - n);
                }
                this.mArray[this.mSize] = null;
            }
        }
        return (E)object;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        boolean bl = false;
        for (int i = this.mSize - 1; i >= 0; --i) {
            if (collection.contains(this.mArray[i])) continue;
            this.removeAt(i);
            bl = true;
        }
        return bl;
    }

    @Override
    public int size() {
        return this.mSize;
    }

    @Override
    public Object[] toArray() {
        int n = this.mSize;
        Object[] objectArray = new Object[n];
        System.arraycopy(this.mArray, 0, objectArray, 0, n);
        return objectArray;
    }

    @Override
    public <T> T[] toArray(T[] TArray) {
        Object[] objectArray = TArray;
        if (TArray.length < this.mSize) {
            objectArray = (Object[])Array.newInstance(TArray.getClass().getComponentType(), this.mSize);
        }
        System.arraycopy(this.mArray, 0, objectArray, 0, this.mSize);
        int n = objectArray.length;
        int n2 = this.mSize;
        if (n > n2) {
            objectArray[n2] = null;
        }
        return objectArray;
    }

    public String toString() {
        if (this.isEmpty()) {
            return "{}";
        }
        StringBuilder stringBuilder = new StringBuilder(this.mSize * 14);
        stringBuilder.append('{');
        for (int i = 0; i < this.mSize; ++i) {
            E e;
            if (i > 0) {
                stringBuilder.append(", ");
            }
            if ((e = this.valueAt(i)) != this) {
                stringBuilder.append(e);
                continue;
            }
            stringBuilder.append("(this Set)");
        }
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    public E valueAt(int n) {
        return (E)this.mArray[n];
    }
}

