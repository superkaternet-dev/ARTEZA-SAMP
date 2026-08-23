/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.provider;

import androidx.collection.ArrayMap;
import com.bumptech.glide.util.MultiClassKey;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ModelToResourceClassCache {
    private final ArrayMap<MultiClassKey, List<Class<?>>> registeredResourceClassCache;
    private final AtomicReference<MultiClassKey> resourceClassKeyRef = new AtomicReference();

    public ModelToResourceClassCache() {
        this.registeredResourceClassCache = new ArrayMap();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void clear() {
        ArrayMap<MultiClassKey, List<Class<?>>> arrayMap = this.registeredResourceClassCache;
        synchronized (arrayMap) {
            this.registeredResourceClassCache.clear();
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public List<Class<?>> get(Class<?> object, Class<?> object2, Class<?> object3) {
        MultiClassKey multiClassKey = this.resourceClassKeyRef.getAndSet(null);
        if (multiClassKey == null) {
            object = new MultiClassKey((Class<?>)object, (Class<?>)object2, (Class<?>)object3);
        } else {
            multiClassKey.set((Class<?>)object, (Class<?>)object2, (Class<?>)object3);
            object = multiClassKey;
        }
        object2 = this.registeredResourceClassCache;
        synchronized (object2) {
            object3 = (List)this.registeredResourceClassCache.get(object);
        }
        this.resourceClassKeyRef.set((MultiClassKey)object);
        return object3;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void put(Class<?> clazz, Class<?> clazz2, Class<?> clazz3, List<Class<?>> list) {
        ArrayMap<MultiClassKey, List<Class<?>>> arrayMap = this.registeredResourceClassCache;
        synchronized (arrayMap) {
            ArrayMap<MultiClassKey, List<Class<?>>> arrayMap2 = this.registeredResourceClassCache;
            MultiClassKey multiClassKey = new MultiClassKey(clazz, clazz2, clazz3);
            arrayMap2.put(multiClassKey, list);
            return;
        }
    }
}

