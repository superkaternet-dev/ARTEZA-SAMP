/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.load.engine;

public interface Resource<Z> {
    public Z get();

    public Class<Z> getResourceClass();

    public int getSize();

    public void recycle();
}

