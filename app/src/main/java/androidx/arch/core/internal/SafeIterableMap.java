/*
 * Decompiled with CFR 0.152.
 */
package androidx.arch.core.internal;

import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

public class SafeIterableMap<K, V>
implements Iterable<Map.Entry<K, V>> {
    private Entry<K, V> mEnd;
    private WeakHashMap<SupportRemove<K, V>, Boolean> mIterators = new WeakHashMap();
    private int mSize = 0;
    Entry<K, V> mStart;

    public Iterator<Map.Entry<K, V>> descendingIterator() {
        DescendingIterator<K, V> descendingIterator = new DescendingIterator<K, V>(this.mEnd, this.mStart);
        this.mIterators.put(descendingIterator, false);
        return descendingIterator;
    }

    public Map.Entry<K, V> eldest() {
        return this.mStart;
    }

    public boolean equals(Object iterator2) {
        boolean bl = true;
        if (iterator2 == this) {
            return true;
        }
        if (!(iterator2 instanceof SafeIterableMap)) {
            return false;
        }
        Object object = (SafeIterableMap)((Object)iterator2);
        if (this.size() != ((SafeIterableMap)object).size()) {
            return false;
        }
        iterator2 = this.iterator();
        object = ((SafeIterableMap)object).iterator();
        while (iterator2.hasNext() && object.hasNext()) {
            Map.Entry<K, V> entry = iterator2.next();
            Object e = object.next();
            if ((entry != null || e == null) && (entry == null || entry.equals(e))) continue;
            return false;
        }
        if (iterator2.hasNext() || object.hasNext()) {
            bl = false;
        }
        return bl;
    }

    protected Entry<K, V> get(K k) {
        Entry<K, V> entry = this.mStart;
        while (entry != null && !entry.mKey.equals(k)) {
            entry = entry.mNext;
        }
        return entry;
    }

    public int hashCode() {
        int n = 0;
        Iterator<Map.Entry<K, V>> iterator2 = this.iterator();
        while (iterator2.hasNext()) {
            n += iterator2.next().hashCode();
        }
        return n;
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        AscendingIterator<K, V> ascendingIterator = new AscendingIterator<K, V>(this.mStart, this.mEnd);
        this.mIterators.put(ascendingIterator, false);
        return ascendingIterator;
    }

    public IteratorWithAdditions iteratorWithAdditions() {
        IteratorWithAdditions iteratorWithAdditions = new IteratorWithAdditions(this);
        this.mIterators.put(iteratorWithAdditions, false);
        return iteratorWithAdditions;
    }

    public Map.Entry<K, V> newest() {
        return this.mEnd;
    }

    protected Entry<K, V> put(K object, V object2) {
        object = new Entry<K, V>(object, object2);
        ++this.mSize;
        object2 = this.mEnd;
        if (object2 == null) {
            this.mStart = object;
            this.mEnd = object;
            return object;
        }
        ((Entry)object2).mNext = object;
        ((Entry)object).mPrevious = this.mEnd;
        this.mEnd = object;
        return object;
    }

    public V putIfAbsent(K k, V v) {
        Entry<K, V> entry = this.get(k);
        if (entry != null) {
            return entry.mValue;
        }
        this.put(k, v);
        return null;
    }

    public V remove(K object) {
        if ((object = this.get(object)) == null) {
            return null;
        }
        --this.mSize;
        if (!this.mIterators.isEmpty()) {
            Iterator<SupportRemove<K, V>> iterator2 = this.mIterators.keySet().iterator();
            while (iterator2.hasNext()) {
                iterator2.next().supportRemove((Entry<K, V>)object);
            }
        }
        if (((Entry)object).mPrevious != null) {
            ((Entry)object).mPrevious.mNext = ((Entry)object).mNext;
        } else {
            this.mStart = ((Entry)object).mNext;
        }
        if (((Entry)object).mNext != null) {
            ((Entry)object).mNext.mPrevious = ((Entry)object).mPrevious;
        } else {
            this.mEnd = ((Entry)object).mPrevious;
        }
        ((Entry)object).mNext = null;
        ((Entry)object).mPrevious = null;
        return ((Entry)object).mValue;
    }

    public int size() {
        return this.mSize;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        Iterator<Map.Entry<K, V>> iterator2 = this.iterator();
        while (iterator2.hasNext()) {
            stringBuilder.append(iterator2.next().toString());
            if (!iterator2.hasNext()) continue;
            stringBuilder.append(", ");
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    static class AscendingIterator<K, V>
    extends ListIterator<K, V> {
        AscendingIterator(Entry<K, V> entry, Entry<K, V> entry2) {
            super(entry, entry2);
        }

        @Override
        Entry<K, V> backward(Entry<K, V> entry) {
            return entry.mPrevious;
        }

        @Override
        Entry<K, V> forward(Entry<K, V> entry) {
            return entry.mNext;
        }
    }

    private static class DescendingIterator<K, V>
    extends ListIterator<K, V> {
        DescendingIterator(Entry<K, V> entry, Entry<K, V> entry2) {
            super(entry, entry2);
        }

        @Override
        Entry<K, V> backward(Entry<K, V> entry) {
            return entry.mNext;
        }

        @Override
        Entry<K, V> forward(Entry<K, V> entry) {
            return entry.mPrevious;
        }
    }

    static class Entry<K, V>
    implements Map.Entry<K, V> {
        final K mKey;
        Entry<K, V> mNext;
        Entry<K, V> mPrevious;
        final V mValue;

        Entry(K k, V v) {
            this.mKey = k;
            this.mValue = v;
        }

        @Override
        public boolean equals(Object object) {
            boolean bl = true;
            if (object == this) {
                return true;
            }
            if (!(object instanceof Entry)) {
                return false;
            }
            object = (Entry)object;
            if (!this.mKey.equals(((Entry)object).mKey) || !this.mValue.equals(((Entry)object).mValue)) {
                bl = false;
            }
            return bl;
        }

        @Override
        public K getKey() {
            return this.mKey;
        }

        @Override
        public V getValue() {
            return this.mValue;
        }

        @Override
        public int hashCode() {
            return this.mKey.hashCode() ^ this.mValue.hashCode();
        }

        @Override
        public V setValue(V v) {
            throw new UnsupportedOperationException("An entry modification is not supported");
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.mKey);
            stringBuilder.append("=");
            stringBuilder.append(this.mValue);
            return stringBuilder.toString();
        }
    }

    private class IteratorWithAdditions
    implements Iterator<Map.Entry<K, V>>,
    SupportRemove<K, V> {
        private boolean mBeforeStart;
        private Entry<K, V> mCurrent;
        final SafeIterableMap this$0;

        IteratorWithAdditions(SafeIterableMap safeIterableMap) {
            this.this$0 = safeIterableMap;
            this.mBeforeStart = true;
        }

        @Override
        public boolean hasNext() {
            boolean bl = this.mBeforeStart;
            boolean bl2 = true;
            boolean bl3 = true;
            if (bl) {
                if (this.this$0.mStart == null) {
                    bl3 = false;
                }
                return bl3;
            }
            Entry entry = this.mCurrent;
            bl3 = entry != null && entry.mNext != null ? bl2 : false;
            return bl3;
        }

        @Override
        public Map.Entry<K, V> next() {
            if (this.mBeforeStart) {
                this.mBeforeStart = false;
                this.mCurrent = this.this$0.mStart;
            } else {
                Entry entry = this.mCurrent;
                entry = entry != null ? entry.mNext : null;
                this.mCurrent = entry;
            }
            return this.mCurrent;
        }

        @Override
        public void supportRemove(Entry<K, V> entry) {
            Entry entry2 = this.mCurrent;
            if (entry == entry2) {
                entry = entry2.mPrevious;
                this.mCurrent = entry;
                boolean bl = entry == null;
                this.mBeforeStart = bl;
            }
        }
    }

    private static abstract class ListIterator<K, V>
    implements Iterator<Map.Entry<K, V>>,
    SupportRemove<K, V> {
        Entry<K, V> mExpectedEnd;
        Entry<K, V> mNext;

        ListIterator(Entry<K, V> entry, Entry<K, V> entry2) {
            this.mExpectedEnd = entry2;
            this.mNext = entry;
        }

        private Entry<K, V> nextNode() {
            Entry<K, V> entry = this.mNext;
            Entry<K, V> entry2 = this.mExpectedEnd;
            if (entry != entry2 && entry2 != null) {
                return this.forward(entry);
            }
            return null;
        }

        abstract Entry<K, V> backward(Entry<K, V> var1);

        abstract Entry<K, V> forward(Entry<K, V> var1);

        @Override
        public boolean hasNext() {
            boolean bl = this.mNext != null;
            return bl;
        }

        @Override
        public Map.Entry<K, V> next() {
            Entry<K, V> entry = this.mNext;
            this.mNext = this.nextNode();
            return entry;
        }

        @Override
        public void supportRemove(Entry<K, V> entry) {
            Entry<K, V> entry2;
            if (this.mExpectedEnd == entry && entry == this.mNext) {
                this.mNext = null;
                this.mExpectedEnd = null;
            }
            if ((entry2 = this.mExpectedEnd) == entry) {
                this.mExpectedEnd = this.backward(entry2);
            }
            if (this.mNext == entry) {
                this.mNext = this.nextNode();
            }
        }
    }

    static interface SupportRemove<K, V> {
        public void supportRemove(Entry<K, V> var1);
    }
}

