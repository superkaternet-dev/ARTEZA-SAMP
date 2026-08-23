/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.load.data;

import com.bumptech.glide.load.data.DataRewinder;
import com.bumptech.glide.util.Preconditions;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DataRewinderRegistry {
    private static final DataRewinder.Factory<?> DEFAULT_FACTORY = new DataRewinder.Factory<Object>(){

        @Override
        public DataRewinder<Object> build(Object object) {
            return new DefaultRewinder(object);
        }

        @Override
        public Class<Object> getDataClass() {
            throw new UnsupportedOperationException("Not implemented");
        }
    };
    private final Map<Class<?>, DataRewinder.Factory<?>> rewinders = new HashMap();

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public <T> DataRewinder<T> build(T object) {
        synchronized (this) {
            DataRewinder.Factory<?> factory;
            DataRewinder.Factory<?> factory2;
            block7: {
                Preconditions.checkNotNull(object);
                factory = factory2 = this.rewinders.get(object.getClass());
                if (factory2 != null) break block7;
                Iterator<DataRewinder.Factory<?>> iterator2 = this.rewinders.values().iterator();
                while (true) {
                    factory = factory2;
                    if (iterator2.hasNext() && !(factory = iterator2.next()).getDataClass().isAssignableFrom(object.getClass())) continue;
                    break;
                }
            }
            factory2 = factory;
            if (factory != null) return factory2.build(object);
            factory2 = DEFAULT_FACTORY;
            return factory2.build(object);
        }
    }

    public void register(DataRewinder.Factory<?> factory) {
        synchronized (this) {
            this.rewinders.put(factory.getDataClass(), factory);
            return;
        }
    }

    private static final class DefaultRewinder
    implements DataRewinder<Object> {
        private final Object data;

        DefaultRewinder(Object object) {
            this.data = object;
        }

        @Override
        public void cleanup() {
        }

        @Override
        public Object rewindAndGet() {
            return this.data;
        }
    }
}

