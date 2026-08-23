/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.util.LruCache;
import com.bumptech.glide.util.Util;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

final class ResourceCacheKey
implements Key {
    private static final LruCache<Class<?>, byte[]> RESOURCE_CLASS_BYTES = new LruCache(50L);
    private final ArrayPool arrayPool;
    private final Class<?> decodedResourceClass;
    private final int height;
    private final Options options;
    private final Key signature;
    private final Key sourceKey;
    private final Transformation<?> transformation;
    private final int width;

    ResourceCacheKey(ArrayPool arrayPool, Key key, Key key2, int n, int n2, Transformation<?> transformation, Class<?> clazz, Options options) {
        this.arrayPool = arrayPool;
        this.sourceKey = key;
        this.signature = key2;
        this.width = n;
        this.height = n2;
        this.transformation = transformation;
        this.decodedResourceClass = clazz;
        this.options = options;
    }

    private byte[] getResourceClassBytes() {
        byte[] byArray;
        LruCache<Class<?>, byte[]> lruCache = RESOURCE_CLASS_BYTES;
        byte[] byArray2 = byArray = lruCache.get(this.decodedResourceClass);
        if (byArray == null) {
            byArray2 = this.decodedResourceClass.getName().getBytes(CHARSET);
            lruCache.put(this.decodedResourceClass, byArray2);
        }
        return byArray2;
    }

    @Override
    public boolean equals(Object object) {
        boolean bl = object instanceof ResourceCacheKey;
        boolean bl2 = false;
        if (bl) {
            object = (ResourceCacheKey)object;
            if (this.height == ((ResourceCacheKey)object).height && this.width == ((ResourceCacheKey)object).width && Util.bothNullOrEqual(this.transformation, ((ResourceCacheKey)object).transformation) && this.decodedResourceClass.equals(((ResourceCacheKey)object).decodedResourceClass) && this.sourceKey.equals(((ResourceCacheKey)object).sourceKey) && this.signature.equals(((ResourceCacheKey)object).signature) && this.options.equals(((ResourceCacheKey)object).options)) {
                bl2 = true;
            }
            return bl2;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int n = ((this.sourceKey.hashCode() * 31 + this.signature.hashCode()) * 31 + this.width) * 31 + this.height;
        Transformation<?> transformation = this.transformation;
        int n2 = n;
        if (transformation != null) {
            n2 = n * 31 + transformation.hashCode();
        }
        return (n2 * 31 + this.decodedResourceClass.hashCode()) * 31 + this.options.hashCode();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ResourceCacheKey{sourceKey=");
        stringBuilder.append(this.sourceKey);
        stringBuilder.append(", signature=");
        stringBuilder.append(this.signature);
        stringBuilder.append(", width=");
        stringBuilder.append(this.width);
        stringBuilder.append(", height=");
        stringBuilder.append(this.height);
        stringBuilder.append(", decodedResourceClass=");
        stringBuilder.append(this.decodedResourceClass);
        stringBuilder.append(", transformation='");
        stringBuilder.append(this.transformation);
        stringBuilder.append('\'');
        stringBuilder.append(", options=");
        stringBuilder.append(this.options);
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        byte[] byArray = this.arrayPool.getExact(8, byte[].class);
        ByteBuffer.wrap(byArray).putInt(this.width).putInt(this.height).array();
        this.signature.updateDiskCacheKey(messageDigest);
        this.sourceKey.updateDiskCacheKey(messageDigest);
        messageDigest.update(byArray);
        Transformation<?> transformation = this.transformation;
        if (transformation != null) {
            transformation.updateDiskCacheKey(messageDigest);
        }
        this.options.updateDiskCacheKey(messageDigest);
        messageDigest.update(this.getResourceClassBytes());
        this.arrayPool.put(byArray);
    }
}

