/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.provider;

import com.bumptech.glide.load.ResourceEncoder;
import java.util.ArrayList;
import java.util.List;

public class ResourceEncoderRegistry {
    private final List<Entry<?>> encoders = new ArrayList();

    public <Z> void append(Class<Z> clazz, ResourceEncoder<Z> resourceEncoder) {
        synchronized (this) {
            List<Entry<?>> list = this.encoders;
            Entry<Z> entry = new Entry<Z>(clazz, resourceEncoder);
            list.add(entry);
            return;
        }
    }

    public <Z> ResourceEncoder<Z> get(Class<Z> object) {
        synchronized (this) {
            int n;
            try {
                n = this.encoders.size();
            }
            catch (Throwable throwable) {
                throw throwable;
            }
            for (int i = 0; i < n; ++i) {
                Entry<Z> entry = this.encoders.get(i);
                if (!entry.handles((Class<?>)object)) continue;
                object = entry.encoder;
                return object;
            }
            return null;
        }
    }

    public <Z> void prepend(Class<Z> clazz, ResourceEncoder<Z> resourceEncoder) {
        synchronized (this) {
            List<Entry<?>> list = this.encoders;
            Entry<Z> entry = new Entry<Z>(clazz, resourceEncoder);
            list.add(0, entry);
            return;
        }
    }

    private static final class Entry<T> {
        final ResourceEncoder<T> encoder;
        private final Class<T> resourceClass;

        Entry(Class<T> clazz, ResourceEncoder<T> resourceEncoder) {
            this.resourceClass = clazz;
            this.encoder = resourceEncoder;
        }

        boolean handles(Class<?> clazz) {
            return this.resourceClass.isAssignableFrom(clazz);
        }
    }
}

