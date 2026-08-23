/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.load.engine.cache;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.cache.MemoryCache;
import com.bumptech.glide.util.LruCache;

public class LruResourceCache
extends LruCache<Key, Resource<?>>
implements MemoryCache {
    private MemoryCache.ResourceRemovedListener listener;

    public LruResourceCache(long l) {
        super(l);
    }

    @Override
    protected int getSize(Resource<?> resource) {
        if (resource == null) {
            return super.getSize(null);
        }
        return resource.getSize();
    }

    @Override
    protected void onItemEvicted(Key object, Resource<?> resource) {
        object = this.listener;
        if (object != null && resource != null) {
            object.onResourceRemoved(resource);
        }
    }

    @Override
    public void setResourceRemovedListener(MemoryCache.ResourceRemovedListener resourceRemovedListener) {
        this.listener = resourceRemovedListener;
    }

    @Override
    public void trimMemory(int n) {
        if (n >= 40) {
            this.clearMemory();
        } else if (n >= 20 || n == 15) {
            this.trimToSize(this.getMaxSize() / 2L);
        }
    }
}

