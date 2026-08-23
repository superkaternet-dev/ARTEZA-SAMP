/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.Key;
import java.security.MessageDigest;

final class DataCacheKey
implements Key {
    private final Key signature;
    private final Key sourceKey;

    DataCacheKey(Key key, Key key2) {
        this.sourceKey = key;
        this.signature = key2;
    }

    @Override
    public boolean equals(Object object) {
        boolean bl = object instanceof DataCacheKey;
        boolean bl2 = false;
        if (bl) {
            object = (DataCacheKey)object;
            bl = bl2;
            if (this.sourceKey.equals(((DataCacheKey)object).sourceKey)) {
                bl = bl2;
                if (this.signature.equals(((DataCacheKey)object).signature)) {
                    bl = true;
                }
            }
            return bl;
        }
        return false;
    }

    Key getSourceKey() {
        return this.sourceKey;
    }

    @Override
    public int hashCode() {
        return this.sourceKey.hashCode() * 31 + this.signature.hashCode();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DataCacheKey{sourceKey=");
        stringBuilder.append(this.sourceKey);
        stringBuilder.append(", signature=");
        stringBuilder.append(this.signature);
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        this.sourceKey.updateDiskCacheKey(messageDigest);
        this.signature.updateDiskCacheKey(messageDigest);
    }
}

