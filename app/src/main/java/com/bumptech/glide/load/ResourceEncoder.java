/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.load;

import com.bumptech.glide.load.EncodeStrategy;
import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.engine.Resource;

public interface ResourceEncoder<T>
extends Encoder<Resource<T>> {
    public EncodeStrategy getEncodeStrategy(Options var1);
}

