/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.load.resource.bytes;

import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.util.Preconditions;

public class BytesResource
implements Resource<byte[]> {
    private final byte[] bytes;

    public BytesResource(byte[] byArray) {
        this.bytes = Preconditions.checkNotNull(byArray);
    }

    @Override
    public byte[] get() {
        return this.bytes;
    }

    @Override
    public Class<byte[]> getResourceClass() {
        return byte[].class;
    }

    @Override
    public int getSize() {
        return this.bytes.length;
    }

    @Override
    public void recycle() {
    }
}

