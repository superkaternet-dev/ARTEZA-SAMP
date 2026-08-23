/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.load.engine.bitmap_recycle;

import com.bumptech.glide.load.engine.bitmap_recycle.ArrayAdapterInterface;

public final class ByteArrayAdapter
implements ArrayAdapterInterface<byte[]> {
    private static final String TAG = "ByteArrayPool";

    @Override
    public int getArrayLength(byte[] byArray) {
        return byArray.length;
    }

    @Override
    public int getElementSizeInBytes() {
        return 1;
    }

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public byte[] newArray(int n) {
        return new byte[n];
    }
}

