/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.collection;

import java.util.Comparator;

public class StandardComparator<A extends Comparable<A>>
implements Comparator<A> {
    private static StandardComparator INSTANCE = new StandardComparator();

    private StandardComparator() {
    }

    public static <T extends Comparable<T>> StandardComparator<T> getComparator(Class<T> clazz) {
        return INSTANCE;
    }

    @Override
    public int compare(A a, A a2) {
        return a.compareTo(a2);
    }
}

