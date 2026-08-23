/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.load.engine.bitmap_recycle;

interface ArrayAdapterInterface<T> {
    public int getArrayLength(T var1);

    public int getElementSizeInBytes();

    public String getTag();

    public T newArray(int var1);
}

