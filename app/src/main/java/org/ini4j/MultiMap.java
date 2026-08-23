/*
 * Decompiled with CFR 0.152.
 */
package org.ini4j;

import java.util.List;
import java.util.Map;

public interface MultiMap<K, V>
extends Map<K, V> {
    public void add(K var1, V var2);

    public void add(K var1, V var2, int var3);

    public V get(Object var1, int var2);

    public List<V> getAll(Object var1);

    public int length(Object var1);

    public V put(K var1, V var2, int var3);

    public List<V> putAll(K var1, List<V> var2);

    public V remove(Object var1, int var2);
}

