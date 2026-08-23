/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.load.resource.transcode;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;

public class UnitTranscoder<Z>
implements ResourceTranscoder<Z, Z> {
    private static final UnitTranscoder<?> UNIT_TRANSCODER = new UnitTranscoder();

    public static <Z> ResourceTranscoder<Z, Z> get() {
        return UNIT_TRANSCODER;
    }

    @Override
    public Resource<Z> transcode(Resource<Z> resource, Options options) {
        return resource;
    }
}

