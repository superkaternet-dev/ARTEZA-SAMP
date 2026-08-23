/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.load.engine;

import androidx.core.util.Pools;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.pool.FactoryPools;
import com.bumptech.glide.util.pool.StateVerifier;

final class LockedResource<Z>
implements Resource<Z>,
FactoryPools.Poolable {
    private static final Pools.Pool<LockedResource<?>> POOL = FactoryPools.threadSafe(20, new FactoryPools.Factory<LockedResource<?>>(){

        @Override
        public LockedResource<?> create() {
            return new LockedResource();
        }
    });
    private boolean isLocked;
    private boolean isRecycled;
    private final StateVerifier stateVerifier = StateVerifier.newInstance();
    private Resource<Z> toWrap;

    LockedResource() {
    }

    private void init(Resource<Z> resource) {
        this.isRecycled = false;
        this.isLocked = true;
        this.toWrap = resource;
    }

    static <Z> LockedResource<Z> obtain(Resource<Z> resource) {
        LockedResource<?> lockedResource = Preconditions.checkNotNull(POOL.acquire());
        super.init(resource);
        return lockedResource;
    }

    private void release() {
        this.toWrap = null;
        POOL.release(this);
    }

    @Override
    public Z get() {
        return this.toWrap.get();
    }

    @Override
    public Class<Z> getResourceClass() {
        return this.toWrap.getResourceClass();
    }

    @Override
    public int getSize() {
        return this.toWrap.getSize();
    }

    @Override
    public StateVerifier getVerifier() {
        return this.stateVerifier;
    }

    @Override
    public void recycle() {
        synchronized (this) {
            this.stateVerifier.throwIfRecycled();
            this.isRecycled = true;
            if (!this.isLocked) {
                this.toWrap.recycle();
                this.release();
            }
            return;
        }
    }

    void unlock() {
        synchronized (this) {
            this.stateVerifier.throwIfRecycled();
            if (this.isLocked) {
                this.isLocked = false;
                if (this.isRecycled) {
                    this.recycle();
                }
                return;
            }
            IllegalStateException illegalStateException = new IllegalStateException("Already unlocked");
            throw illegalStateException;
        }
    }
}

