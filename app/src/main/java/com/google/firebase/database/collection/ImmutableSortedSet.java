/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.collection;

import com.google.firebase.database.collection.ImmutableSortedMap;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ImmutableSortedSet<T>
implements Iterable<T> {
    private final ImmutableSortedMap<T, Void> map;

    private ImmutableSortedSet(ImmutableSortedMap<T, Void> immutableSortedMap) {
        this.map = immutableSortedMap;
    }

    public ImmutableSortedSet(List<T> list, Comparator<T> comparator) {
        this.map = ImmutableSortedMap.Builder.buildFrom(list, Collections.emptyMap(), ImmutableSortedMap.Builder.identityTranslator(), comparator);
    }

    public boolean contains(T t) {
        return this.map.containsKey(t);
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof ImmutableSortedSet)) {
            return false;
        }
        object = (ImmutableSortedSet)object;
        return this.map.equals(((ImmutableSortedSet)object).map);
    }

    public T getMaxEntry() {
        return this.map.getMaxKey();
    }

    public T getMinEntry() {
        return this.map.getMinKey();
    }

    public T getPredecessorEntry(T t) {
        return this.map.getPredecessorKey(t);
    }

    public int hashCode() {
        return this.map.hashCode();
    }

    public int indexOf(T t) {
        return this.map.indexOf(t);
    }

    public ImmutableSortedSet<T> insert(T t) {
        return new ImmutableSortedSet<T>(this.map.insert(t, null));
    }

    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    @Override
    public Iterator<T> iterator() {
        return new WrappedEntryIterator<T>(this.map.iterator());
    }

    public Iterator<T> iteratorFrom(T t) {
        return new WrappedEntryIterator<T>(this.map.iteratorFrom(t));
    }

    public ImmutableSortedSet<T> remove(T object) {
        object = (object = this.map.remove(object)) == this.map ? this : new ImmutableSortedSet<T>(object);
        return object;
    }

    public Iterator<T> reverseIterator() {
        return new WrappedEntryIterator<T>(this.map.reverseIterator());
    }

    public Iterator<T> reverseIteratorFrom(T t) {
        return new WrappedEntryIterator<T>(this.map.reverseIteratorFrom(t));
    }

    public int size() {
        return this.map.size();
    }

    public ImmutableSortedSet<T> unionWith(ImmutableSortedSet<T> object) {
        ImmutableSortedSet immutableSortedSet;
        ImmutableSortedSet immutableSortedSet2 = immutableSortedSet = this;
        ImmutableSortedSet immutableSortedSet3 = object;
        if (immutableSortedSet.size() < ((ImmutableSortedSet)object).size()) {
            immutableSortedSet3 = this;
            immutableSortedSet2 = object;
        }
        object = immutableSortedSet3.iterator();
        while (object.hasNext()) {
            immutableSortedSet2 = immutableSortedSet2.insert(object.next());
        }
        return immutableSortedSet2;
    }

    private static class WrappedEntryIterator<T>
    implements Iterator<T> {
        final Iterator<Map.Entry<T, Void>> iterator;

        public WrappedEntryIterator(Iterator<Map.Entry<T, Void>> iterator2) {
            this.iterator = iterator2;
        }

        @Override
        public boolean hasNext() {
            return this.iterator.hasNext();
        }

        @Override
        public T next() {
            return this.iterator.next().getKey();
        }

        @Override
        public void remove() {
            this.iterator.remove();
        }
    }
}

