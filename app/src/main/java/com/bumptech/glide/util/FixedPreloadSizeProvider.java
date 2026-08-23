/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.util;

import com.bumptech.glide.ListPreloader;

public class FixedPreloadSizeProvider<T>
implements ListPreloader.PreloadSizeProvider<T> {
    private final int[] size;

    public FixedPreloadSizeProvider(int n, int n2) {
        this.size = new int[]{n, n2};
    }

    @Override
    public int[] getPreloadSize(T t, int n, int n2) {
        return this.size;
    }
}

