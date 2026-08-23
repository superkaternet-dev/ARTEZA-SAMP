/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.util.Preconditions;

class EngineResource<Z>
implements Resource<Z> {
    private int acquired;
    private final boolean isMemoryCacheable;
    private final boolean isRecyclable;
    private boolean isRecycled;
    private final Key key;
    private final ResourceListener listener;
    private final Resource<Z> resource;

    EngineResource(Resource<Z> resource, boolean bl, boolean bl2, Key key, ResourceListener resourceListener) {
        this.resource = Preconditions.checkNotNull(resource);
        this.isMemoryCacheable = bl;
        this.isRecyclable = bl2;
        this.key = key;
        this.listener = Preconditions.checkNotNull(resourceListener);
    }

    void acquire() {
        synchronized (this) {
            if (!this.isRecycled) {
                ++this.acquired;
                return;
            }
            IllegalStateException illegalStateException = new IllegalStateException("Cannot acquire a recycled resource");
            throw illegalStateException;
        }
    }

    @Override
    public Z get() {
        return this.resource.get();
    }

    Resource<Z> getResource() {
        return this.resource;
    }

    @Override
    public Class<Z> getResourceClass() {
        return this.resource.getResourceClass();
    }

    @Override
    public int getSize() {
        return this.resource.getSize();
    }

    boolean isMemoryCacheable() {
        return this.isMemoryCacheable;
    }

    @Override
    public void recycle() {
        synchronized (this) {
            if (this.acquired <= 0) {
                if (!this.isRecycled) {
                    this.isRecycled = true;
                    if (this.isRecyclable) {
                        this.resource.recycle();
                    }
                    return;
                }
                IllegalStateException illegalStateException = new IllegalStateException("Cannot recycle a resource that has already been recycled");
                throw illegalStateException;
            }
            IllegalStateException illegalStateException = new IllegalStateException("Cannot recycle a resource while it is still acquired");
            throw illegalStateException;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    void release() {
        boolean bl = false;
        // MONITORENTER : this
        int n = this.acquired;
        if (n <= 0) {
            IllegalStateException illegalStateException = new IllegalStateException("Cannot release a recycled or not yet acquired resource");
            throw illegalStateException;
        }
        this.acquired = --n;
        if (n == 0) {
            bl = true;
        }
        // MONITOREXIT : this
        if (!bl) return;
        this.listener.onResourceReleased(this.key, this);
    }

    public String toString() {
        synchronized (this) {
            CharSequence charSequence = new StringBuilder();
            charSequence.append("EngineResource{isMemoryCacheable=");
            charSequence.append(this.isMemoryCacheable);
            charSequence.append(", listener=");
            charSequence.append(this.listener);
            charSequence.append(", key=");
            charSequence.append(this.key);
            charSequence.append(", acquired=");
            charSequence.append(this.acquired);
            charSequence.append(", isRecycled=");
            charSequence.append(this.isRecycled);
            charSequence.append(", resource=");
            charSequence.append(this.resource);
            charSequence.append('}');
            charSequence = charSequence.toString();
            return charSequence;
        }
    }

    static interface ResourceListener {
        public void onResourceReleased(Key var1, EngineResource<?> var2);
    }
}

