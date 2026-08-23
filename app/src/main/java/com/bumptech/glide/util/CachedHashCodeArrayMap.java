/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.util;

import androidx.collection.ArrayMap;
import androidx.collection.SimpleArrayMap;

public final class CachedHashCodeArrayMap<K, V>
extends ArrayMap<K, V> {
    private int hashCode;

    @Override
    public void clear() {
        this.hashCode = 0;
        super.clear();
    }

    @Override
    public int hashCode() {
        if (this.hashCode == 0) {
            this.hashCode = super.hashCode();
        }
        return this.hashCode;
    }

    @Override
    public V put(K k, V v) {
        this.hashCode = 0;
        return super.put(k, v);
    }

    @Override
    public void putAll(SimpleArrayMap<? extends K, ? extends V> simpleArrayMap) {
        this.hashCode = 0;
        super.putAll(simpleArrayMap);
    }

    @Override
    public V removeAt(int n) {
        this.hashCode = 0;
        return super.removeAt(n);
    }

    @Override
    public V setValueAt(int n, V v) {
        this.hashCode = 0;
        return super.setValueAt(n, v);
    }
}

