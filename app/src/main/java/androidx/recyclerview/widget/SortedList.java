/*
 * Decompiled with CFR 0.152.
 */
package androidx.recyclerview.widget;

import androidx.recyclerview.widget.BatchingListUpdateCallback;
import androidx.recyclerview.widget.ListUpdateCallback;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

public class SortedList<T> {
    private static final int CAPACITY_GROWTH = 10;
    private static final int DELETION = 2;
    private static final int INSERTION = 1;
    public static final int INVALID_POSITION = -1;
    private static final int LOOKUP = 4;
    private static final int MIN_CAPACITY = 10;
    private BatchedCallback mBatchedCallback;
    private Callback mCallback;
    T[] mData;
    private int mNewDataStart;
    private T[] mOldData;
    private int mOldDataSize;
    private int mOldDataStart;
    private int mSize;
    private final Class<T> mTClass;

    public SortedList(Class<T> clazz, Callback<T> callback) {
        this(clazz, callback, 10);
    }

    public SortedList(Class<T> clazz, Callback<T> callback, int n) {
        this.mTClass = clazz;
        this.mData = (Object[])Array.newInstance(clazz, n);
        this.mCallback = callback;
        this.mSize = 0;
    }

    private int add(T t, boolean bl) {
        int n;
        int n2 = this.findIndexOf(t, this.mData, 0, this.mSize, 1);
        if (n2 == -1) {
            n = 0;
        } else {
            n = n2;
            if (n2 < this.mSize) {
                T t2 = this.mData[n2];
                n = n2;
                if (this.mCallback.areItemsTheSame(t2, t)) {
                    if (this.mCallback.areContentsTheSame(t2, t)) {
                        this.mData[n2] = t;
                        return n2;
                    }
                    this.mData[n2] = t;
                    Callback callback = this.mCallback;
                    callback.onChanged(n2, 1, callback.getChangePayload(t2, t));
                    return n2;
                }
            }
        }
        this.addToData(n, t);
        if (bl) {
            this.mCallback.onInserted(n, 1);
        }
        return n;
    }

    private void addAllInternal(T[] TArray) {
        if (TArray.length < 1) {
            return;
        }
        int n = this.sortAndDedup(TArray);
        if (this.mSize == 0) {
            this.mData = TArray;
            this.mSize = n;
            this.mCallback.onInserted(0, n);
        } else {
            this.merge(TArray, n);
        }
    }

    private void addToData(int n, T object) {
        int n2 = this.mSize;
        if (n <= n2) {
            Object[] objectArray = this.mData;
            if (n2 == objectArray.length) {
                objectArray = (Object[])Array.newInstance(this.mTClass, objectArray.length + 10);
                System.arraycopy(this.mData, 0, objectArray, 0, n);
                objectArray[n] = object;
                System.arraycopy(this.mData, n, objectArray, n + 1, this.mSize - n);
                this.mData = objectArray;
            } else {
                System.arraycopy(objectArray, n, objectArray, n + 1, n2 - n);
                this.mData[n] = object;
            }
            ++this.mSize;
            return;
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("cannot add item to ");
        ((StringBuilder)object).append(n);
        ((StringBuilder)object).append(" because size is ");
        ((StringBuilder)object).append(this.mSize);
        throw new IndexOutOfBoundsException(((StringBuilder)object).toString());
    }

    private T[] copyArray(T[] TArray) {
        Object[] objectArray = (Object[])Array.newInstance(this.mTClass, TArray.length);
        System.arraycopy(TArray, 0, objectArray, 0, TArray.length);
        return objectArray;
    }

    private int findIndexOf(T t, T[] TArray, int n, int n2, int n3) {
        int n4;
        while (true) {
            n4 = -1;
            if (n >= n2) break;
            n4 = (n + n2) / 2;
            T t2 = TArray[n4];
            int n5 = this.mCallback.compare(t2, t);
            if (n5 < 0) {
                n = n4 + 1;
                continue;
            }
            if (n5 == 0) {
                if (this.mCallback.areItemsTheSame(t2, t)) {
                    return n4;
                }
                n = this.linearEqualitySearch(t, n4, n, n2);
                if (n3 == 1) {
                    if (n == -1) {
                        n = n4;
                    }
                    return n;
                }
                return n;
            }
            n2 = n4;
        }
        n2 = n4;
        if (n3 == 1) {
            n2 = n;
        }
        return n2;
    }

    private int findSameItem(T t, T[] TArray, int n, int n2) {
        while (n < n2) {
            if (this.mCallback.areItemsTheSame(TArray[n], t)) {
                return n;
            }
            ++n;
        }
        return -1;
    }

    private int linearEqualitySearch(T t, int n, int n2, int n3) {
        T t2;
        for (int i = n - 1; i >= n2 && this.mCallback.compare(t2 = this.mData[i], t) == 0; --i) {
            if (!this.mCallback.areItemsTheSame(t2, t)) continue;
            return i;
        }
        ++n;
        while (n < n3 && this.mCallback.compare(t2 = this.mData[n], t) == 0) {
            if (this.mCallback.areItemsTheSame(t2, t)) {
                return n;
            }
            ++n;
        }
        return -1;
    }

    private void merge(T[] TArray, int n) {
        int n2;
        boolean bl = this.mCallback instanceof BatchedCallback ^ true;
        if (bl) {
            this.beginBatchedUpdates();
        }
        this.mOldData = this.mData;
        this.mOldDataStart = 0;
        this.mOldDataSize = n2 = this.mSize;
        this.mData = (Object[])Array.newInstance(this.mTClass, n2 + n + 10);
        this.mNewDataStart = 0;
        n2 = 0;
        while (true) {
            int n3;
            int n4;
            block10: {
                block8: {
                    block9: {
                        if ((n4 = this.mOldDataStart) >= (n3 = this.mOldDataSize) && n2 >= n) break block8;
                        if (n4 != n3) break block9;
                        System.arraycopy(TArray, n2, this.mData, this.mNewDataStart, n -= n2);
                        this.mNewDataStart = n2 = this.mNewDataStart + n;
                        this.mSize += n;
                        this.mCallback.onInserted(n2 - n, n);
                        break block8;
                    }
                    if (n2 != n) break block10;
                    n = n3 - n4;
                    System.arraycopy(this.mOldData, n4, this.mData, this.mNewDataStart, n);
                    this.mNewDataStart += n;
                }
                this.mOldData = null;
                if (bl) {
                    this.endBatchedUpdates();
                }
                return;
            }
            Object object = this.mOldData[n4];
            Object object2 = TArray[n2];
            if ((n4 = this.mCallback.compare(object, object2)) > 0) {
                object = this.mData;
                n3 = this.mNewDataStart;
                this.mNewDataStart = n4 = n3 + 1;
                object[n3] = object2;
                ++this.mSize;
                ++n2;
                this.mCallback.onInserted(n4 - 1, 1);
                continue;
            }
            if (n4 == 0 && this.mCallback.areItemsTheSame(object, object2)) {
                Object object3 = this.mData;
                n4 = this.mNewDataStart;
                this.mNewDataStart = n4 + 1;
                object3[n4] = object2;
                n4 = n2 + 1;
                ++this.mOldDataStart;
                n2 = n4;
                if (this.mCallback.areContentsTheSame(object, object2)) continue;
                object3 = this.mCallback;
                ((Callback)object3).onChanged(this.mNewDataStart - 1, 1, ((Callback)object3).getChangePayload(object, object2));
                n2 = n4;
                continue;
            }
            object2 = this.mData;
            n4 = this.mNewDataStart;
            this.mNewDataStart = n4 + 1;
            object2[n4] = object;
            ++this.mOldDataStart;
        }
    }

    private boolean remove(T t, boolean bl) {
        int n = this.findIndexOf(t, this.mData, 0, this.mSize, 2);
        if (n == -1) {
            return false;
        }
        this.removeItemAtIndex(n, bl);
        return true;
    }

    private void removeItemAtIndex(int n, boolean bl) {
        int n2;
        T[] TArray = this.mData;
        System.arraycopy(TArray, n + 1, TArray, n, this.mSize - n - 1);
        this.mSize = n2 = this.mSize - 1;
        this.mData[n2] = null;
        if (bl) {
            this.mCallback.onRemoved(n, 1);
        }
    }

    private void replaceAllInsert(T t) {
        T[] TArray = this.mData;
        int n = this.mNewDataStart;
        TArray[n] = t;
        this.mNewDataStart = ++n;
        ++this.mSize;
        this.mCallback.onInserted(n - 1, 1);
    }

    private void replaceAllInternal(T[] TArray) {
        boolean bl = this.mCallback instanceof BatchedCallback ^ true;
        if (bl) {
            this.beginBatchedUpdates();
        }
        this.mOldDataStart = 0;
        this.mOldDataSize = this.mSize;
        this.mOldData = this.mData;
        this.mNewDataStart = 0;
        int n = this.sortAndDedup(TArray);
        this.mData = (Object[])Array.newInstance(this.mTClass, n);
        while (true) {
            int n2;
            int n3;
            block11: {
                block9: {
                    int n4;
                    block10: {
                        if ((n3 = this.mNewDataStart) >= n && this.mOldDataStart >= this.mOldDataSize) break block9;
                        n4 = this.mOldDataSize;
                        if ((n2 = this.mOldDataStart++) < n4) break block10;
                        n2 = this.mNewDataStart;
                        System.arraycopy(TArray, n2, this.mData, n2, n -= n3);
                        this.mNewDataStart += n;
                        this.mSize += n;
                        this.mCallback.onInserted(n2, n);
                        break block9;
                    }
                    if (n3 < n) break block11;
                    n = n4 - n2;
                    this.mSize -= n;
                    this.mCallback.onRemoved(n3, n);
                }
                this.mOldData = null;
                if (bl) {
                    this.endBatchedUpdates();
                }
                return;
            }
            T t = this.mOldData[n2];
            T t2 = TArray[n3];
            if ((n3 = this.mCallback.compare(t, t2)) < 0) {
                this.replaceAllRemove();
                continue;
            }
            if (n3 > 0) {
                this.replaceAllInsert(t2);
                continue;
            }
            if (!this.mCallback.areItemsTheSame(t, t2)) {
                this.replaceAllRemove();
                this.replaceAllInsert(t2);
                continue;
            }
            Object object = this.mData;
            n3 = this.mNewDataStart;
            object[n3] = t2;
            this.mNewDataStart = n3 + 1;
            if (this.mCallback.areContentsTheSame(t, t2)) continue;
            object = this.mCallback;
            ((Callback)object).onChanged(this.mNewDataStart - 1, 1, ((Callback)object).getChangePayload(t, t2));
        }
    }

    private void replaceAllRemove() {
        --this.mSize;
        ++this.mOldDataStart;
        this.mCallback.onRemoved(this.mNewDataStart, 1);
    }

    private int sortAndDedup(T[] TArray) {
        if (TArray.length == 0) {
            return 0;
        }
        Arrays.sort(TArray, this.mCallback);
        int n = 0;
        int n2 = 1;
        for (int i = 1; i < TArray.length; ++i) {
            T t = TArray[i];
            if (this.mCallback.compare(TArray[n], t) == 0) {
                int n3 = this.findSameItem(t, TArray, n, n2);
                if (n3 != -1) {
                    TArray[n3] = t;
                    continue;
                }
                if (n2 != i) {
                    TArray[n2] = t;
                }
                ++n2;
                continue;
            }
            if (n2 != i) {
                TArray[n2] = t;
            }
            n = n2++;
        }
        return n2;
    }

    private void throwIfInMutationOperation() {
        if (this.mOldData == null) {
            return;
        }
        throw new IllegalStateException("Data cannot be mutated in the middle of a batch update operation such as addAll or replaceAll.");
    }

    public int add(T t) {
        this.throwIfInMutationOperation();
        return this.add(t, true);
    }

    public void addAll(Collection<T> collection) {
        this.addAll(collection.toArray((Object[])Array.newInstance(this.mTClass, collection.size())), true);
    }

    public void addAll(T ... TArray) {
        this.addAll(TArray, false);
    }

    public void addAll(T[] TArray, boolean bl) {
        this.throwIfInMutationOperation();
        if (TArray.length == 0) {
            return;
        }
        if (bl) {
            this.addAllInternal(TArray);
        } else {
            this.addAllInternal(this.copyArray(TArray));
        }
    }

    public void beginBatchedUpdates() {
        this.throwIfInMutationOperation();
        Callback callback = this.mCallback;
        if (callback instanceof BatchedCallback) {
            return;
        }
        if (this.mBatchedCallback == null) {
            this.mBatchedCallback = new BatchedCallback(callback);
        }
        this.mCallback = this.mBatchedCallback;
    }

    public void clear() {
        this.throwIfInMutationOperation();
        if (this.mSize == 0) {
            return;
        }
        int n = this.mSize;
        Arrays.fill(this.mData, 0, n, null);
        this.mSize = 0;
        this.mCallback.onRemoved(0, n);
    }

    public void endBatchedUpdates() {
        Callback callback;
        this.throwIfInMutationOperation();
        Callback callback2 = this.mCallback;
        if (callback2 instanceof BatchedCallback) {
            ((BatchedCallback)callback2).dispatchLastEvent();
        }
        if ((callback = this.mCallback) == (callback2 = this.mBatchedCallback)) {
            this.mCallback = ((BatchedCallback)callback2).mWrappedCallback;
        }
    }

    public T get(int n) throws IndexOutOfBoundsException {
        if (n < this.mSize && n >= 0) {
            int n2;
            T[] TArray = this.mOldData;
            if (TArray != null && n >= (n2 = this.mNewDataStart)) {
                return TArray[n - n2 + this.mOldDataStart];
            }
            return this.mData[n];
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Asked to get item at ");
        stringBuilder.append(n);
        stringBuilder.append(" but size is ");
        stringBuilder.append(this.mSize);
        throw new IndexOutOfBoundsException(stringBuilder.toString());
    }

    public int indexOf(T t) {
        if (this.mOldData != null) {
            int n = this.findIndexOf(t, this.mData, 0, this.mNewDataStart, 4);
            if (n != -1) {
                return n;
            }
            n = this.findIndexOf(t, this.mOldData, this.mOldDataStart, this.mOldDataSize, 4);
            if (n != -1) {
                return n - this.mOldDataStart + this.mNewDataStart;
            }
            return -1;
        }
        return this.findIndexOf(t, this.mData, 0, this.mSize, 4);
    }

    public void recalculatePositionOfItemAt(int n) {
        this.throwIfInMutationOperation();
        T t = this.get(n);
        this.removeItemAtIndex(n, false);
        int n2 = this.add(t, false);
        if (n != n2) {
            this.mCallback.onMoved(n, n2);
        }
    }

    public boolean remove(T t) {
        this.throwIfInMutationOperation();
        return this.remove(t, true);
    }

    public T removeItemAt(int n) {
        this.throwIfInMutationOperation();
        T t = this.get(n);
        this.removeItemAtIndex(n, true);
        return t;
    }

    public void replaceAll(Collection<T> collection) {
        this.replaceAll(collection.toArray((Object[])Array.newInstance(this.mTClass, collection.size())), true);
    }

    public void replaceAll(T ... TArray) {
        this.replaceAll(TArray, false);
    }

    public void replaceAll(T[] TArray, boolean bl) {
        this.throwIfInMutationOperation();
        if (bl) {
            this.replaceAllInternal(TArray);
        } else {
            this.replaceAllInternal(this.copyArray(TArray));
        }
    }

    public int size() {
        return this.mSize;
    }

    public void updateItemAt(int n, T t) {
        this.throwIfInMutationOperation();
        T t2 = this.get(n);
        int n2 = t2 != t && this.mCallback.areContentsTheSame(t2, t) ? 0 : 1;
        if (t2 != t && this.mCallback.compare(t2, t) == 0) {
            this.mData[n] = t;
            if (n2 != 0) {
                Callback callback = this.mCallback;
                callback.onChanged(n, 1, callback.getChangePayload(t2, t));
            }
            return;
        }
        if (n2 != 0) {
            Callback callback = this.mCallback;
            callback.onChanged(n, 1, callback.getChangePayload(t2, t));
        }
        this.removeItemAtIndex(n, false);
        n2 = this.add(t, false);
        if (n != n2) {
            this.mCallback.onMoved(n, n2);
        }
    }

    public static class BatchedCallback<T2>
    extends Callback<T2> {
        private final BatchingListUpdateCallback mBatchingListUpdateCallback;
        final Callback<T2> mWrappedCallback;

        public BatchedCallback(Callback<T2> callback) {
            this.mWrappedCallback = callback;
            this.mBatchingListUpdateCallback = new BatchingListUpdateCallback(callback);
        }

        @Override
        public boolean areContentsTheSame(T2 T2, T2 T22) {
            return this.mWrappedCallback.areContentsTheSame(T2, T22);
        }

        @Override
        public boolean areItemsTheSame(T2 T2, T2 T22) {
            return this.mWrappedCallback.areItemsTheSame(T2, T22);
        }

        @Override
        public int compare(T2 T2, T2 T22) {
            return this.mWrappedCallback.compare(T2, T22);
        }

        public void dispatchLastEvent() {
            this.mBatchingListUpdateCallback.dispatchLastEvent();
        }

        @Override
        public Object getChangePayload(T2 T2, T2 T22) {
            return this.mWrappedCallback.getChangePayload(T2, T22);
        }

        @Override
        public void onChanged(int n, int n2) {
            this.mBatchingListUpdateCallback.onChanged(n, n2, null);
        }

        @Override
        public void onChanged(int n, int n2, Object object) {
            this.mBatchingListUpdateCallback.onChanged(n, n2, object);
        }

        @Override
        public void onInserted(int n, int n2) {
            this.mBatchingListUpdateCallback.onInserted(n, n2);
        }

        @Override
        public void onMoved(int n, int n2) {
            this.mBatchingListUpdateCallback.onMoved(n, n2);
        }

        @Override
        public void onRemoved(int n, int n2) {
            this.mBatchingListUpdateCallback.onRemoved(n, n2);
        }
    }

    public static abstract class Callback<T2>
    implements Comparator<T2>,
    ListUpdateCallback {
        public abstract boolean areContentsTheSame(T2 var1, T2 var2);

        public abstract boolean areItemsTheSame(T2 var1, T2 var2);

        @Override
        public abstract int compare(T2 var1, T2 var2);

        public Object getChangePayload(T2 T2, T2 T22) {
            return null;
        }

        public abstract void onChanged(int var1, int var2);

        @Override
        public void onChanged(int n, int n2, Object object) {
            this.onChanged(n, n2);
        }
    }
}

