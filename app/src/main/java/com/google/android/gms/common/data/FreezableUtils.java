/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.data;

import com.google.android.gms.common.data.Freezable;
import java.util.ArrayList;

public final class FreezableUtils {
    public static <T, E extends Freezable<T>> ArrayList<T> freeze(ArrayList<E> arrayList) {
        ArrayList arrayList2 = new ArrayList(arrayList.size());
        int n = arrayList.size();
        for (int i = 0; i < n; ++i) {
            arrayList2.add(((Freezable)arrayList.get(i)).freeze());
        }
        return arrayList2;
    }

    public static <T, E extends Freezable<T>> ArrayList<T> freeze(E[] EArray) {
        ArrayList<T> arrayList = new ArrayList<T>(EArray.length);
        for (int i = 0; i < EArray.length; ++i) {
            arrayList.add(EArray[i].freeze());
        }
        return arrayList;
    }

    public static <T, E extends Freezable<T>> ArrayList<T> freezeIterable(Iterable<E> object) {
        ArrayList arrayList = new ArrayList();
        object = object.iterator();
        while (object.hasNext()) {
            arrayList.add(((Freezable)object.next()).freeze());
        }
        return arrayList;
    }
}

