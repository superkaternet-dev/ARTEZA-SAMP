/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.util.Preconditions;
import java.security.MessageDigest;
import java.util.Map;

class EngineKey
implements Key {
    private int hashCode;
    private final int height;
    private final Object model;
    private final Options options;
    private final Class<?> resourceClass;
    private final Key signature;
    private final Class<?> transcodeClass;
    private final Map<Class<?>, Transformation<?>> transformations;
    private final int width;

    EngineKey(Object object, Key key, int n, int n2, Map<Class<?>, Transformation<?>> map, Class<?> clazz, Class<?> clazz2, Options options) {
        this.model = Preconditions.checkNotNull(object);
        this.signature = Preconditions.checkNotNull(key, "Signature must not be null");
        this.width = n;
        this.height = n2;
        this.transformations = Preconditions.checkNotNull(map);
        this.resourceClass = Preconditions.checkNotNull(clazz, "Resource class must not be null");
        this.transcodeClass = Preconditions.checkNotNull(clazz2, "Transcode class must not be null");
        this.options = Preconditions.checkNotNull(options);
    }

    @Override
    public boolean equals(Object object) {
        boolean bl = object instanceof EngineKey;
        boolean bl2 = false;
        if (bl) {
            object = (EngineKey)object;
            if (this.model.equals(((EngineKey)object).model) && this.signature.equals(((EngineKey)object).signature) && this.height == ((EngineKey)object).height && this.width == ((EngineKey)object).width && this.transformations.equals(((EngineKey)object).transformations) && this.resourceClass.equals(((EngineKey)object).resourceClass) && this.transcodeClass.equals(((EngineKey)object).transcodeClass) && this.options.equals(((EngineKey)object).options)) {
                bl2 = true;
            }
            return bl2;
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (this.hashCode == 0) {
            int n;
            this.hashCode = n = this.model.hashCode();
            this.hashCode = n = n * 31 + this.signature.hashCode();
            this.hashCode = n = n * 31 + this.width;
            this.hashCode = n = n * 31 + this.height;
            this.hashCode = n = n * 31 + this.transformations.hashCode();
            this.hashCode = n = n * 31 + this.resourceClass.hashCode();
            this.hashCode = n = n * 31 + this.transcodeClass.hashCode();
            this.hashCode = n * 31 + this.options.hashCode();
        }
        return this.hashCode;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("EngineKey{model=");
        stringBuilder.append(this.model);
        stringBuilder.append(", width=");
        stringBuilder.append(this.width);
        stringBuilder.append(", height=");
        stringBuilder.append(this.height);
        stringBuilder.append(", resourceClass=");
        stringBuilder.append(this.resourceClass);
        stringBuilder.append(", transcodeClass=");
        stringBuilder.append(this.transcodeClass);
        stringBuilder.append(", signature=");
        stringBuilder.append(this.signature);
        stringBuilder.append(", hashCode=");
        stringBuilder.append(this.hashCode);
        stringBuilder.append(", transformations=");
        stringBuilder.append(this.transformations);
        stringBuilder.append(", options=");
        stringBuilder.append(this.options);
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        throw new UnsupportedOperationException();
    }
}

