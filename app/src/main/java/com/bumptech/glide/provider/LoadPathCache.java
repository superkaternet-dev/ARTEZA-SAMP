/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.provider;

import androidx.collection.ArrayMap;
import com.bumptech.glide.load.engine.DecodePath;
import com.bumptech.glide.load.engine.LoadPath;
import com.bumptech.glide.load.resource.transcode.UnitTranscoder;
import com.bumptech.glide.util.MultiClassKey;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;

public class LoadPathCache {
    private static final LoadPath<?, ?, ?> NO_PATHS_SIGNAL = new LoadPath<Object, Object, Object>(Object.class, Object.class, Object.class, Collections.singletonList(new DecodePath<Object, Object, Object>(Object.class, Object.class, Object.class, Collections.emptyList(), new UnitTranscoder(), null)), null);
    private final ArrayMap<MultiClassKey, LoadPath<?, ?, ?>> cache = new ArrayMap();
    private final AtomicReference<MultiClassKey> keyRef = new AtomicReference();

    private MultiClassKey getKey(Class<?> clazz, Class<?> clazz2, Class<?> clazz3) {
        MultiClassKey multiClassKey;
        MultiClassKey multiClassKey2 = multiClassKey = (MultiClassKey)this.keyRef.getAndSet(null);
        if (multiClassKey == null) {
            multiClassKey2 = new MultiClassKey();
        }
        multiClassKey2.set(clazz, clazz2, clazz3);
        return multiClassKey2;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public <Data, TResource, Transcode> LoadPath<Data, TResource, Transcode> get(Class<Data> object, Class<TResource> object2, Class<Transcode> object3) {
        object3 = this.getKey((Class<?>)object, (Class<?>)object2, (Class<?>)object3);
        object = this.cache;
        synchronized (object) {
            object2 = (LoadPath)this.cache.get(object3);
        }
        this.keyRef.set((MultiClassKey)object3);
        return object2;
    }

    public boolean isEmptyLoadPath(LoadPath<?, ?, ?> loadPath) {
        return NO_PATHS_SIGNAL.equals(loadPath);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void put(Class<?> loadPath, Class<?> clazz, Class<?> clazz2, LoadPath<?, ?, ?> loadPath2) {
        ArrayMap<MultiClassKey, LoadPath<?, ?, ?>> arrayMap = this.cache;
        synchronized (arrayMap) {
            ArrayMap<MultiClassKey, LoadPath<?, ?, ?>> arrayMap2 = this.cache;
            MultiClassKey multiClassKey = new MultiClassKey((Class<?>)((Object)loadPath), clazz, clazz2);
            loadPath = loadPath2 != null ? loadPath2 : NO_PATHS_SIGNAL;
            arrayMap2.put(multiClassKey, loadPath);
            return;
        }
    }
}

