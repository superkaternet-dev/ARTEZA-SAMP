/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.util;

import androidx.collection.ArrayMap;
import androidx.collection.ArraySet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class CollectionUtils {
    private CollectionUtils() {
    }

    public static boolean isEmpty(Collection<?> collection) {
        if (collection == null) {
            return true;
        }
        return collection.isEmpty();
    }

    @Deprecated
    public static <T> List<T> listOf() {
        return Collections.emptyList();
    }

    @Deprecated
    public static <T> List<T> listOf(T t) {
        return Collections.singletonList(t);
    }

    @Deprecated
    public static <T> List<T> listOf(T ... TArray) {
        switch (TArray.length) {
            default: {
                return Collections.unmodifiableList(Arrays.asList(TArray));
            }
            case 1: {
                return CollectionUtils.listOf(TArray[0]);
            }
            case 0: 
        }
        return CollectionUtils.listOf();
    }

    public static <K, V> Map<K, V> mapOf(K k, V v, K k2, V v2, K k3, V v3) {
        Map<K, V> map = CollectionUtils.zza(3, false);
        map.put(k, v);
        map.put(k2, v2);
        map.put(k3, v3);
        return Collections.unmodifiableMap(map);
    }

    public static <K, V> Map<K, V> mapOf(K k, V v, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
        Map<K, V> map = CollectionUtils.zza(6, false);
        map.put(k, v);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        return Collections.unmodifiableMap(map);
    }

    public static <K, V> Map<K, V> mapOfKeyValueArrays(K[] object, V[] VArray) {
        int n = ((K[])object).length;
        int n2 = VArray.length;
        if (n == n2) {
            Map<Object, V> map;
            switch (n) {
                default: {
                    map = CollectionUtils.zza(n, false);
                    break;
                }
                case 1: {
                    return Collections.singletonMap(object[0], VArray[0]);
                }
                case 0: {
                    return Collections.emptyMap();
                }
            }
            for (n2 = 0; n2 < ((Object)object).length; ++n2) {
                map.put(object[n2], VArray[n2]);
            }
            return Collections.unmodifiableMap(map);
        }
        object = new StringBuilder(66);
        ((StringBuilder)object).append("Key and values array lengths not equal: ");
        ((StringBuilder)object).append(n);
        ((StringBuilder)object).append(" != ");
        ((StringBuilder)object).append(n2);
        object = new IllegalArgumentException(((StringBuilder)object).toString());
        throw object;
    }

    public static <T> Set<T> mutableSetOfWithSize(int n) {
        Set<Object> set = n == 0 ? new ArraySet() : CollectionUtils.zzb(n, true);
        return set;
    }

    @Deprecated
    public static <T> Set<T> setOf(T t, T t2, T t3) {
        Set<T> set = CollectionUtils.zzb(3, false);
        set.add(t);
        set.add(t2);
        set.add(t3);
        return Collections.unmodifiableSet(set);
    }

    @Deprecated
    public static <T> Set<T> setOf(T ... object) {
        int n = ((T[])object).length;
        switch (n) {
            default: {
                Set<T> set = CollectionUtils.zzb(n, false);
                Collections.addAll(set, object);
                return Collections.unmodifiableSet(set);
            }
            case 4: {
                T t = object[0];
                T t2 = object[1];
                T t3 = object[2];
                T t4 = object[3];
                object = CollectionUtils.zzb(4, false);
                object.add(t);
                object.add(t2);
                object.add(t3);
                object.add(t4);
                return Collections.unmodifiableSet(object);
            }
            case 3: {
                return CollectionUtils.setOf(object[0], object[1], object[2]);
            }
            case 2: {
                T t = object[0];
                object = object[1];
                Set<T> set = CollectionUtils.zzb(2, false);
                set.add(t);
                set.add(object);
                return Collections.unmodifiableSet(set);
            }
            case 1: {
                return Collections.singleton(object[0]);
            }
            case 0: 
        }
        return Collections.emptySet();
    }

    private static <K, V> Map<K, V> zza(int n, boolean bl) {
        Map map = n <= 256 ? new ArrayMap(n) : new HashMap(n, 1.0f);
        return map;
    }

    private static <T> Set<T> zzb(int n, boolean bl) {
        float f = true != bl ? 1.0f : 0.75f;
        int n2 = true != bl ? 256 : 128;
        Set set = n <= n2 ? new ArraySet(n) : new HashSet(n, f);
        return set;
    }
}

