/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 */
package com.bumptech.glide.load;

import android.content.Context;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.Resource;

public interface Transformation<T>
extends Key {
    public Resource<T> transform(Context var1, Resource<T> var2, int var3, int var4);
}

