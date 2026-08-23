/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.load.engine.bitmap_recycle;

import com.bumptech.glide.load.engine.bitmap_recycle.ArrayAdapterInterface;

public final class IntegerArrayAdapter
implements ArrayAdapterInterface<int[]> {
    private static final String TAG = "IntegerArrayPool";

    @Override
    public int getArrayLength(int[] nArray) {
        return nArray.length;
    }

    @Override
    public int getElementSizeInBytes() {
        return 4;
    }

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public int[] newArray(int n) {
        return new int[n];
    }
}

