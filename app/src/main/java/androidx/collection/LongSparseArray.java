/*
 * Decompiled with CFR 0.152.
 */
package androidx.collection;

import androidx.collection.ContainerHelpers;

public class LongSparseArray<E>
implements Cloneable {
    private static final Object DELETED = new Object();
    private boolean mGarbage = false;
    private long[] mKeys;
    private int mSize;
    private Object[] mValues;

    public LongSparseArray() {
        this(10);
    }

    public LongSparseArray(int n) {
        if (n == 0) {
            this.mKeys = ContainerHelpers.EMPTY_LONGS;
            this.mValues = ContainerHelpers.EMPTY_OBJECTS;
        } else {
            n = ContainerHelpers.idealLongArraySize(n);
            this.mKeys = new long[n];
            this.mValues = new Object[n];
        }
    }

    private void gc() {
        int n = this.mSize;
        int n2 = 0;
        long[] lArray = this.mKeys;
        Object[] objectArray = this.mValues;
        for (int i = 0; i < n; ++i) {
            Object object = objectArray[i];
            int n3 = n2;
            if (object != DELETED) {
                if (i != n2) {
                    lArray[n2] = lArray[i];
                    objectArray[n2] = object;
                    objectArray[i] = null;
                }
                n3 = n2 + 1;
            }
            n2 = n3;
        }
        this.mGarbage = false;
        this.mSize = n2;
    }

    public void append(long l, E e) {
        int n = this.mSize;
        if (n != 0 && l <= this.mKeys[n - 1]) {
            this.put(l, e);
            return;
        }
        if (this.mGarbage && n >= this.mKeys.length) {
            this.gc();
        }
        if ((n = this.mSize) >= this.mKeys.length) {
            int n2 = ContainerHelpers.idealLongArraySize(n + 1);
            long[] lArray = new long[n2];
            Object[] objectArray = new Object[n2];
            Object[] objectArray2 = this.mKeys;
            System.arraycopy(objectArray2, 0, lArray, 0, objectArray2.length);
            objectArray2 = this.mValues;
            System.arraycopy(objectArray2, 0, objectArray, 0, objectArray2.length);
            this.mKeys = lArray;
            this.mValues = objectArray;
        }
        this.mKeys[n] = l;
        this.mValues[n] = e;
        this.mSize = n + 1;
    }

    public void clear() {
        int n = this.mSize;
        Object[] objectArray = this.mValues;
        for (int i = 0; i < n; ++i) {
            objectArray[i] = null;
        }
        this.mSize = 0;
        this.mGarbage = false;
    }

    public LongSparseArray<E> clone() {
        try {
            LongSparseArray longSparseArray = (LongSparseArray)super.clone();
            longSparseArray.mKeys = (long[])this.mKeys.clone();
            longSparseArray.mValues = (Object[])this.mValues.clone();
            return longSparseArray;
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            throw new AssertionError((Object)cloneNotSupportedException);
        }
    }

    public boolean containsKey(long l) {
        boolean bl = this.indexOfKey(l) >= 0;
        return bl;
    }

    public boolean containsValue(E e) {
        boolean bl = this.indexOfValue(e) >= 0;
        return bl;
    }

    @Deprecated
    public void delete(long l) {
        this.remove(l);
    }

    public E get(long l) {
        return this.get(l, null);
    }

    public E get(long l, E e) {
        Object[] objectArray;
        int n = ContainerHelpers.binarySearch(this.mKeys, this.mSize, l);
        if (n >= 0 && (objectArray = this.mValues)[n] != DELETED) {
            return (E)objectArray[n];
        }
        return e;
    }

    public int indexOfKey(long l) {
        if (this.mGarbage) {
            this.gc();
        }
        return ContainerHelpers.binarySearch(this.mKeys, this.mSize, l);
    }

    public int indexOfValue(E e) {
        if (this.mGarbage) {
            this.gc();
        }
        for (int i = 0; i < this.mSize; ++i) {
            if (this.mValues[i] != e) continue;
            return i;
        }
        return -1;
    }

    public boolean isEmpty() {
        boolean bl = this.size() == 0;
        return bl;
    }

    public long keyAt(int n) {
        if (this.mGarbage) {
            this.gc();
        }
        return this.mKeys[n];
    }

    public void put(long l, E e) {
        int n = ContainerHelpers.binarySearch(this.mKeys, this.mSize, l);
        if (n >= 0) {
            this.mValues[n] = e;
        } else {
            Object[] objectArray;
            int n2 = ~n;
            int n3 = this.mSize;
            if (n2 < n3 && (objectArray = this.mValues)[n2] == DELETED) {
                this.mKeys[n2] = l;
                objectArray[n2] = e;
                return;
            }
            n = n2;
            if (this.mGarbage) {
                n = n2;
                if (n3 >= this.mKeys.length) {
                    this.gc();
                    n = ~ContainerHelpers.binarySearch(this.mKeys, this.mSize, l);
                }
            }
            if ((n2 = this.mSize) >= this.mKeys.length) {
                n2 = ContainerHelpers.idealLongArraySize(n2 + 1);
                long[] lArray = new long[n2];
                objectArray = new Object[n2];
                Object[] objectArray2 = this.mKeys;
                System.arraycopy(objectArray2, 0, lArray, 0, objectArray2.length);
                objectArray2 = this.mValues;
                System.arraycopy(objectArray2, 0, objectArray, 0, objectArray2.length);
                this.mKeys = lArray;
                this.mValues = objectArray;
            }
            if ((n2 = this.mSize) - n != 0) {
                objectArray = this.mKeys;
                System.arraycopy(objectArray, n, objectArray, n + 1, n2 - n);
                objectArray = this.mValues;
                System.arraycopy(objectArray, n, objectArray, n + 1, this.mSize - n);
            }
            this.mKeys[n] = l;
            this.mValues[n] = e;
            ++this.mSize;
        }
    }

    public void putAll(LongSparseArray<? extends E> longSparseArray) {
        int n = longSparseArray.size();
        for (int i = 0; i < n; ++i) {
            this.put(longSparseArray.keyAt(i), longSparseArray.valueAt(i));
        }
    }

    public E putIfAbsent(long l, E e) {
        E e2 = this.get(l);
        if (e2 == null) {
            this.put(l, e);
        }
        return e2;
    }

    public void remove(long l) {
        Object object;
        Object[] objectArray;
        Object object2;
        int n = ContainerHelpers.binarySearch(this.mKeys, this.mSize, l);
        if (n >= 0 && (object2 = (objectArray = this.mValues)[n]) != (object = DELETED)) {
            objectArray[n] = object;
            this.mGarbage = true;
        }
    }

    public boolean remove(long l, Object object) {
        E e;
        int n = this.indexOfKey(l);
        if (n >= 0 && (object == (e = this.valueAt(n)) || object != null && object.equals(e))) {
            this.removeAt(n);
            return true;
        }
        return false;
    }

    public void removeAt(int n) {
        Object[] objectArray = this.mValues;
        Object object = objectArray[n];
        Object object2 = DELETED;
        if (object != object2) {
            objectArray[n] = object2;
            this.mGarbage = true;
        }
    }

    public E replace(long l, E e) {
        int n = this.indexOfKey(l);
        if (n >= 0) {
            Object[] objectArray = this.mValues;
            Object object = objectArray[n];
            objectArray[n] = e;
            return (E)object;
        }
        return null;
    }

    public boolean replace(long l, E e, E e2) {
        Object object;
        int n = this.indexOfKey(l);
        if (n >= 0 && ((object = this.mValues[n]) == e || e != null && e.equals(object))) {
            this.mValues[n] = e2;
            return true;
        }
        return false;
    }

    public void setValueAt(int n, E e) {
        if (this.mGarbage) {
            this.gc();
        }
        this.mValues[n] = e;
    }

    public int size() {
        if (this.mGarbage) {
            this.gc();
        }
        return this.mSize;
    }

    public String toString() {
        if (this.size() <= 0) {
            return "{}";
        }
        StringBuilder stringBuilder = new StringBuilder(this.mSize * 28);
        stringBuilder.append('{');
        for (int i = 0; i < this.mSize; ++i) {
            if (i > 0) {
                stringBuilder.append(", ");
            }
            stringBuilder.append(this.keyAt(i));
            stringBuilder.append('=');
            E e = this.valueAt(i);
            if (e != this) {
                stringBuilder.append(e);
                continue;
            }
            stringBuilder.append("(this Map)");
        }
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    public E valueAt(int n) {
        if (this.mGarbage) {
            this.gc();
        }
        return (E)this.mValues[n];
    }
}

