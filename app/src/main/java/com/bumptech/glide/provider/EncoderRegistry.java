/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.provider;

import com.bumptech.glide.load.Encoder;
import java.util.ArrayList;
import java.util.List;

public class EncoderRegistry {
    private final List<Entry<?>> encoders = new ArrayList();

    public <T> void append(Class<T> clazz, Encoder<T> encoder) {
        synchronized (this) {
            List<Entry<?>> list = this.encoders;
            Entry<T> entry = new Entry<T>(clazz, encoder);
            list.add(entry);
            return;
        }
    }

    public <T> Encoder<T> getEncoder(Class<T> object) {
        synchronized (this) {
            try {
                for (Entry entry : this.encoders) {
                    if (!entry.handles((Class<?>)((Object)object))) continue;
                    object = entry.encoder;
                    return object;
                }
            }
            catch (Throwable throwable) {}
            {
                throw throwable;
            }
            return null;
        }
    }

    public <T> void prepend(Class<T> clazz, Encoder<T> encoder) {
        synchronized (this) {
            List<Entry<?>> list = this.encoders;
            Entry<T> entry = new Entry<T>(clazz, encoder);
            list.add(0, entry);
            return;
        }
    }

    private static final class Entry<T> {
        private final Class<T> dataClass;
        final Encoder<T> encoder;

        Entry(Class<T> clazz, Encoder<T> encoder) {
            this.dataClass = clazz;
            this.encoder = encoder;
        }

        boolean handles(Class<?> clazz) {
            return this.dataClass.isAssignableFrom(clazz);
        }
    }
}

