/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core.utilities;

import com.google.firebase.database.collection.ImmutableSortedMap;
import com.google.firebase.database.collection.StandardComparator;
import com.google.firebase.database.core.Path;
import com.google.firebase.database.core.utilities.Predicate;
import com.google.firebase.database.snapshot.ChildKey;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class ImmutableTree<T>
implements Iterable<Map.Entry<Path, T>> {
    private static final ImmutableTree EMPTY;
    private static final ImmutableSortedMap EMPTY_CHILDREN;
    private final ImmutableSortedMap<ChildKey, ImmutableTree<T>> children;
    private final T value;

    static {
        ImmutableSortedMap immutableSortedMap;
        EMPTY_CHILDREN = immutableSortedMap = ImmutableSortedMap.Builder.emptyMap(StandardComparator.getComparator(ChildKey.class));
        EMPTY = new ImmutableTree<Object>(null, immutableSortedMap);
    }

    public ImmutableTree(T t) {
        this(t, EMPTY_CHILDREN);
    }

    public ImmutableTree(T t, ImmutableSortedMap<ChildKey, ImmutableTree<T>> immutableSortedMap) {
        this.value = t;
        this.children = immutableSortedMap;
    }

    public static <V> ImmutableTree<V> emptyInstance() {
        return EMPTY;
    }

    private <R> R fold(Path path, TreeVisitor<? super T, R> treeVisitor, R r) {
        for (Map.Entry<ChildKey, ImmutableTree<T>> object2 : this.children) {
            r = super.fold(path.child(object2.getKey()), treeVisitor, r);
        }
        T t = this.value;
        Iterator<Map.Entry<ChildKey, ImmutableTree<Object>>> iterator2 = r;
        if (t != null) {
            iterator2 = treeVisitor.onNodeValue(path, t, r);
        }
        return (R)iterator2;
    }

    public boolean containsMatchingValue(Predicate<? super T> predicate) {
        Object object = this.value;
        if (object != null && predicate.evaluate(object)) {
            return true;
        }
        object = this.children.iterator();
        while (object.hasNext()) {
            if (!((ImmutableTree)((Map.Entry)object.next()).getValue()).containsMatchingValue(predicate)) continue;
            return true;
        }
        return false;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object != null && this.getClass() == object.getClass()) {
            object = (ImmutableTree)object;
            ImmutableSortedMap<ChildKey, ImmutableTree<Object>> immutableSortedMap = this.children;
            if (immutableSortedMap != null ? !immutableSortedMap.equals(((ImmutableTree)object).children) : ((ImmutableTree)object).children != null) {
                return false;
            }
            immutableSortedMap = this.value;
            return !(immutableSortedMap != null ? !((Object)immutableSortedMap).equals(((ImmutableTree)object).value) : ((ImmutableTree)object).value != null);
        }
        return false;
    }

    public Path findRootMostMatchingPath(Path path, Predicate<? super T> predicate) {
        Object object = this.value;
        if (object != null && predicate.evaluate(object)) {
            return Path.getEmptyPath();
        }
        if (path.isEmpty()) {
            return null;
        }
        object = path.getFront();
        ImmutableTree<T> immutableTree = this.children.get((ChildKey)object);
        if (immutableTree != null) {
            if ((path = immutableTree.findRootMostMatchingPath(path.popFront(), predicate)) != null) {
                return new Path(new ChildKey[]{object}).child(path);
            }
            return null;
        }
        return null;
    }

    public Path findRootMostPathWithValue(Path path) {
        return this.findRootMostMatchingPath(path, Predicate.TRUE);
    }

    public <R> R fold(R r, TreeVisitor<? super T, R> treeVisitor) {
        return this.fold(Path.getEmptyPath(), treeVisitor, r);
    }

    public void foreach(TreeVisitor<T, Void> treeVisitor) {
        this.fold(Path.getEmptyPath(), treeVisitor, null);
    }

    public T get(Path path) {
        if (path.isEmpty()) {
            return this.value;
        }
        Object object = path.getFront();
        if ((object = this.children.get((ChildKey)object)) != null) {
            return ((ImmutableTree)object).get(path.popFront());
        }
        return null;
    }

    public ImmutableTree<T> getChild(ChildKey object) {
        if ((object = this.children.get((ChildKey)object)) != null) {
            return object;
        }
        return ImmutableTree.emptyInstance();
    }

    public ImmutableSortedMap<ChildKey, ImmutableTree<T>> getChildren() {
        return this.children;
    }

    public T getValue() {
        return this.value;
    }

    public int hashCode() {
        Object object = this.value;
        int n = 0;
        int n2 = object != null ? object.hashCode() : 0;
        object = this.children;
        if (object != null) {
            n = ((ImmutableSortedMap)object).hashCode();
        }
        return n2 * 31 + n;
    }

    public boolean isEmpty() {
        boolean bl = this.value == null && this.children.isEmpty();
        return bl;
    }

    @Override
    public Iterator<Map.Entry<Path, T>> iterator() {
        ArrayList arrayList = new ArrayList();
        this.foreach(new TreeVisitor<T, Void>(this, arrayList){
            final ImmutableTree this$0;
            final List val$list;
            {
                this.this$0 = immutableTree;
                this.val$list = list;
            }

            @Override
            public Void onNodeValue(Path path, T t, Void void_) {
                this.val$list.add(new AbstractMap.SimpleImmutableEntry(path, t));
                return null;
            }
        });
        return arrayList.iterator();
    }

    public T leafMostValue(Path path) {
        return (T)this.leafMostValueMatching(path, Predicate.TRUE);
    }

    public T leafMostValueMatching(Path iterable, Predicate<? super T> predicate) {
        Object object = this.value;
        object = object != null && predicate.evaluate(object) ? this.value : null;
        ImmutableTree<T> immutableTree = this;
        Iterator<ChildKey> iterator2 = iterable.iterator();
        iterable = immutableTree;
        while (iterator2.hasNext()) {
            immutableTree = iterator2.next();
            if ((immutableTree = ((ImmutableTree)iterable).children.get((ChildKey)((Object)immutableTree))) == null) {
                return object;
            }
            T t = immutableTree.value;
            iterable = object;
            if (t != null) {
                iterable = object;
                if (predicate.evaluate(t)) {
                    iterable = immutableTree.value;
                }
            }
            object = iterable;
            iterable = immutableTree;
        }
        return object;
    }

    public ImmutableTree<T> remove(Path iterable) {
        if (((Path)iterable).isEmpty()) {
            if (this.children.isEmpty()) {
                return ImmutableTree.emptyInstance();
            }
            return new ImmutableTree<Object>(null, this.children);
        }
        ChildKey childKey = ((Path)iterable).getFront();
        ImmutableTree<T> immutableTree = this.children.get(childKey);
        if (immutableTree != null) {
            iterable = ((ImmutableTree)(iterable = immutableTree.remove(((Path)iterable).popFront()))).isEmpty() ? this.children.remove(childKey) : this.children.insert(childKey, (ImmutableTree<T>)iterable);
            if (this.value == null && ((ImmutableSortedMap)iterable).isEmpty()) {
                return ImmutableTree.emptyInstance();
            }
            return new ImmutableTree<T>(this.value, iterable);
        }
        return this;
    }

    public T rootMostValue(Path path) {
        return (T)this.rootMostValueMatching(path, Predicate.TRUE);
    }

    public T rootMostValueMatching(Path iterable, Predicate<? super T> predicate) {
        Object object = this.value;
        if (object != null && predicate.evaluate(object)) {
            return this.value;
        }
        object = this;
        Iterator<ChildKey> iterator2 = iterable.iterator();
        iterable = object;
        while (iterator2.hasNext()) {
            object = iterator2.next();
            iterable = ((ImmutableTree)iterable).children.get((ChildKey)object);
            if (iterable == null) {
                return null;
            }
            object = ((ImmutableTree)iterable).value;
            if (object == null || !predicate.evaluate(object)) continue;
            return ((ImmutableTree)iterable).value;
        }
        return null;
    }

    public ImmutableTree<T> set(Path iterable, T t) {
        ImmutableTree<T> immutableTree;
        if (iterable.isEmpty()) {
            return new ImmutableTree<T>(t, this.children);
        }
        ChildKey childKey = iterable.getFront();
        ImmutableTree<Object> immutableTree2 = immutableTree = this.children.get(childKey);
        if (immutableTree == null) {
            immutableTree2 = ImmutableTree.emptyInstance();
        }
        iterable = immutableTree2.set(iterable.popFront(), t);
        iterable = this.children.insert(childKey, (ImmutableTree<T>)iterable);
        return new ImmutableTree<T>(this.value, iterable);
    }

    public ImmutableTree<T> setTree(Path iterable, ImmutableTree<T> immutableTree) {
        ImmutableTree<T> immutableTree2;
        if (((Path)iterable).isEmpty()) {
            return immutableTree;
        }
        ChildKey childKey = ((Path)iterable).getFront();
        ImmutableTree<Object> immutableTree3 = immutableTree2 = this.children.get(childKey);
        if (immutableTree2 == null) {
            immutableTree3 = ImmutableTree.emptyInstance();
        }
        iterable = ((ImmutableTree)(iterable = immutableTree3.setTree(((Path)iterable).popFront(), immutableTree))).isEmpty() ? this.children.remove(childKey) : this.children.insert(childKey, (ImmutableTree<T>)iterable);
        return new ImmutableTree<T>(this.value, iterable);
    }

    public ImmutableTree<T> subtree(Path path) {
        if (path.isEmpty()) {
            return this;
        }
        Object object = path.getFront();
        if ((object = this.children.get((ChildKey)object)) != null) {
            return ((ImmutableTree)object).subtree(path.popFront());
        }
        return ImmutableTree.emptyInstance();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ImmutableTree { value=");
        stringBuilder.append(this.getValue());
        stringBuilder.append(", children={");
        for (Map.Entry<ChildKey, ImmutableTree<T>> entry : this.children) {
            stringBuilder.append(entry.getKey().asString());
            stringBuilder.append("=");
            stringBuilder.append(entry.getValue());
        }
        stringBuilder.append("} }");
        return stringBuilder.toString();
    }

    public Collection<T> values() {
        ArrayList arrayList = new ArrayList();
        this.foreach(new TreeVisitor<T, Void>(this, arrayList){
            final ImmutableTree this$0;
            final ArrayList val$list;
            {
                this.this$0 = immutableTree;
                this.val$list = arrayList;
            }

            @Override
            public Void onNodeValue(Path path, T t, Void void_) {
                this.val$list.add(t);
                return null;
            }
        });
        return arrayList;
    }

    public static interface TreeVisitor<T, R> {
        public R onNodeValue(Path var1, T var2, R var3);
    }
}

