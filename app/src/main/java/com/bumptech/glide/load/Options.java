/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.load;

import androidx.collection.ArrayMap;
import androidx.collection.SimpleArrayMap;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.util.CachedHashCodeArrayMap;
import java.security.MessageDigest;

public final class Options
implements Key {
    private final ArrayMap<Option<?>, Object> values = new CachedHashCodeArrayMap();

    private static <T> void updateDiskCacheKey(Option<T> option, Object object, MessageDigest messageDigest) {
        option.update(object, messageDigest);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Options) {
            object = (Options)object;
            return this.values.equals(((Options)object).values);
        }
        return false;
    }

    public <T> T get(Option<T> option) {
        option = this.values.containsKey(option) ? this.values.get(option) : option.getDefaultValue();
        return (T)option;
    }

    @Override
    public int hashCode() {
        return this.values.hashCode();
    }

    public void putAll(Options options) {
        ((SimpleArrayMap)this.values).putAll((SimpleArrayMap<Option<?>, Object>)options.values);
    }

    public <T> Options set(Option<T> option, T t) {
        this.values.put(option, t);
        return this;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Options{values=");
        stringBuilder.append(this.values);
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        for (int i = 0; i < this.values.size(); ++i) {
            Options.updateDiskCacheKey((Option)this.values.keyAt(i), this.values.valueAt(i), messageDigest);
        }
    }
}

