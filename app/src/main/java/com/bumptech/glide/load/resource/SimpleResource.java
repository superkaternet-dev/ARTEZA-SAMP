/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.load.resource;

import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.util.Preconditions;

public class SimpleResource<T>
implements Resource<T> {
    protected final T data;

    public SimpleResource(T t) {
        this.data = Preconditions.checkNotNull(t);
    }

    @Override
    public final T get() {
        return this.data;
    }

    @Override
    public Class<T> getResourceClass() {
        return this.data.getClass();
    }

    @Override
    public final int getSize() {
        return 1;
    }

    @Override
    public void recycle() {
    }
}

