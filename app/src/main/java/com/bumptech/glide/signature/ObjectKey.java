/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.signature;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.util.Preconditions;
import java.security.MessageDigest;

public final class ObjectKey
implements Key {
    private final Object object;

    public ObjectKey(Object object) {
        this.object = Preconditions.checkNotNull(object);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof ObjectKey) {
            object = (ObjectKey)object;
            return this.object.equals(((ObjectKey)object).object);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.object.hashCode();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ObjectKey{object=");
        stringBuilder.append(this.object);
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update(this.object.toString().getBytes(CHARSET));
    }
}

