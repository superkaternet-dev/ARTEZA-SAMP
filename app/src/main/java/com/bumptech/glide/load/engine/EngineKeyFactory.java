/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.EngineKey;
import java.util.Map;

class EngineKeyFactory {
    EngineKeyFactory() {
    }

    EngineKey buildKey(Object object, Key key, int n, int n2, Map<Class<?>, Transformation<?>> map, Class<?> clazz, Class<?> clazz2, Options options) {
        return new EngineKey(object, key, n, n2, map, clazz, clazz2, options);
    }
}

