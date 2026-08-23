/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core;

import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.core.utilities.Utilities;
import com.google.firebase.database.snapshot.ChildKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class Path
implements Iterable<ChildKey>,
Comparable<Path> {
    private static final Path EMPTY_PATH = new Path("");
    private final int end;
    private final ChildKey[] pieces;
    private final int start;

    public Path(String stringArray) {
        int n;
        stringArray = stringArray.split("/", -1);
        int n2 = 0;
        int n3 = stringArray.length;
        for (int i = 0; i < n3; ++i) {
            n = n2;
            if (stringArray[i].length() > 0) {
                n = n2 + 1;
            }
            n2 = n;
        }
        this.pieces = new ChildKey[n2];
        n = 0;
        for (String string2 : stringArray) {
            n2 = n;
            if (string2.length() > 0) {
                this.pieces[n] = ChildKey.fromString(string2);
                n2 = n + 1;
            }
            n = n2;
        }
        this.start = 0;
        this.end = this.pieces.length;
    }

    public Path(List<String> list) {
        this.pieces = new ChildKey[list.size()];
        int n = 0;
        for (String string2 : list) {
            this.pieces[n] = ChildKey.fromString(string2);
            ++n;
        }
        this.start = 0;
        this.end = list.size();
    }

    public Path(ChildKey ... childKeyArray) {
        this.pieces = Arrays.copyOf(childKeyArray, childKeyArray.length);
        this.start = 0;
        this.end = childKeyArray.length;
        int n = childKeyArray.length;
        for (int i = 0; i < n; ++i) {
            boolean bl = childKeyArray[i] != null;
            Utilities.hardAssert(bl, "Can't construct a path with a null value!");
        }
    }

    private Path(ChildKey[] childKeyArray, int n, int n2) {
        this.pieces = childKeyArray;
        this.start = n;
        this.end = n2;
    }

    public static Path getEmptyPath() {
        return EMPTY_PATH;
    }

    public static Path getRelative(Path path, Path path2) {
        Comparable<ChildKey> comparable = path.getFront();
        ChildKey childKey = path2.getFront();
        if (comparable == null) {
            return path2;
        }
        if (((ChildKey)comparable).equals(childKey)) {
            return Path.getRelative(path.popFront(), path2.popFront());
        }
        comparable = new StringBuilder();
        ((StringBuilder)comparable).append("INTERNAL ERROR: ");
        ((StringBuilder)comparable).append(path2);
        ((StringBuilder)comparable).append(" is not contained in ");
        ((StringBuilder)comparable).append(path);
        throw new DatabaseException(((StringBuilder)comparable).toString());
    }

    public List<String> asList() {
        ArrayList<String> arrayList = new ArrayList<String>(this.size());
        Iterator<ChildKey> iterator2 = this.iterator();
        while (iterator2.hasNext()) {
            arrayList.add(iterator2.next().asString());
        }
        return arrayList;
    }

    public Path child(Path path) {
        int n = this.size() + path.size();
        ChildKey[] childKeyArray = new ChildKey[n];
        System.arraycopy(this.pieces, this.start, childKeyArray, 0, this.size());
        System.arraycopy(path.pieces, path.start, childKeyArray, this.size(), path.size());
        return new Path(childKeyArray, 0, n);
    }

    public Path child(ChildKey childKey) {
        int n = this.size();
        ChildKey[] childKeyArray = new ChildKey[n + 1];
        System.arraycopy(this.pieces, this.start, childKeyArray, 0, n);
        childKeyArray[n] = childKey;
        return new Path(childKeyArray, 0, n + 1);
    }

    @Override
    public int compareTo(Path path) {
        int n;
        int n2;
        int n3 = this.start;
        for (n2 = path.start; n3 < (n = this.end) && n2 < path.end; ++n3, ++n2) {
            n = this.pieces[n3].compareTo(path.pieces[n2]);
            if (n == 0) continue;
            return n;
        }
        if (n3 == n && n2 == path.end) {
            return 0;
        }
        if (n3 == n) {
            return -1;
        }
        return 1;
    }

    public boolean contains(Path path) {
        if (this.size() > path.size()) {
            return false;
        }
        int n = this.start;
        int n2 = path.start;
        while (n < this.end) {
            if (!this.pieces[n].equals(path.pieces[n2])) {
                return false;
            }
            ++n;
            ++n2;
        }
        return true;
    }

    public boolean equals(Object object) {
        if (!(object instanceof Path)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        object = (Path)object;
        if (this.size() != ((Path)object).size()) {
            return false;
        }
        int n = this.start;
        for (int i = ((Path)object).start; n < this.end && i < ((Path)object).end; ++n, ++i) {
            if (this.pieces[n].equals(((Path)object).pieces[i])) continue;
            return false;
        }
        return true;
    }

    public ChildKey getBack() {
        if (!this.isEmpty()) {
            return this.pieces[this.end - 1];
        }
        return null;
    }

    public ChildKey getFront() {
        if (this.isEmpty()) {
            return null;
        }
        return this.pieces[this.start];
    }

    public Path getParent() {
        if (this.isEmpty()) {
            return null;
        }
        return new Path(this.pieces, this.start, this.end - 1);
    }

    public int hashCode() {
        int n = 0;
        for (int i = this.start; i < this.end; ++i) {
            n = n * 37 + this.pieces[i].hashCode();
        }
        return n;
    }

    public boolean isEmpty() {
        boolean bl = this.start >= this.end;
        return bl;
    }

    @Override
    public Iterator<ChildKey> iterator() {
        return new Iterator<ChildKey>(this){
            int offset;
            final Path this$0;
            {
                this.this$0 = path;
                this.offset = path.start;
            }

            @Override
            public boolean hasNext() {
                boolean bl = this.offset < this.this$0.end;
                return bl;
            }

            @Override
            public ChildKey next() {
                if (this.hasNext()) {
                    Object object = this.this$0.pieces;
                    int n = this.offset;
                    object = object[n];
                    this.offset = n + 1;
                    return object;
                }
                throw new NoSuchElementException("No more elements.");
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Can't remove component from immutable Path!");
            }
        };
    }

    public Path popFront() {
        int n;
        int n2 = n = this.start;
        if (!this.isEmpty()) {
            n2 = n + 1;
        }
        return new Path(this.pieces, n2, this.end);
    }

    public int size() {
        return this.end - this.start;
    }

    public String toString() {
        if (this.isEmpty()) {
            return "/";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = this.start; i < this.end; ++i) {
            stringBuilder.append("/");
            stringBuilder.append(this.pieces[i].asString());
        }
        return stringBuilder.toString();
    }

    public String wireFormat() {
        if (this.isEmpty()) {
            return "/";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = this.start; i < this.end; ++i) {
            if (i > this.start) {
                stringBuilder.append("/");
            }
            stringBuilder.append(this.pieces[i].asString());
        }
        return stringBuilder.toString();
    }
}

