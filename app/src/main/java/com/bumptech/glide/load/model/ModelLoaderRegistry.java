/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.load.model;

import androidx.core.util.Pools;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelLoaderRegistry {
    private final ModelLoaderCache cache = new ModelLoaderCache();
    private final MultiModelLoaderFactory multiModelLoaderFactory;

    public ModelLoaderRegistry(Pools.Pool<List<Throwable>> pool) {
        this(new MultiModelLoaderFactory(pool));
    }

    private ModelLoaderRegistry(MultiModelLoaderFactory multiModelLoaderFactory) {
        this.multiModelLoaderFactory = multiModelLoaderFactory;
    }

    private static <A> Class<A> getClass(A a) {
        return a.getClass();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private <A> List<ModelLoader<A, ?>> getModelLoadersForClass(Class<A> clazz) {
        synchronized (this) {
            List list;
            List list2 = list = this.cache.get(clazz);
            if (list == null) {
                list2 = Collections.unmodifiableList(this.multiModelLoaderFactory.build(clazz));
                this.cache.put(clazz, list2);
            }
            return list2;
        }
    }

    private <Model, Data> void tearDown(List<ModelLoaderFactory<? extends Model, ? extends Data>> object) {
        object = object.iterator();
        while (object.hasNext()) {
            ((ModelLoaderFactory)object.next()).teardown();
        }
    }

    public <Model, Data> void append(Class<Model> clazz, Class<Data> clazz2, ModelLoaderFactory<? extends Model, ? extends Data> modelLoaderFactory) {
        synchronized (this) {
            this.multiModelLoaderFactory.append(clazz, clazz2, modelLoaderFactory);
            this.cache.clear();
            return;
        }
    }

    public <Model, Data> ModelLoader<Model, Data> build(Class<Model> object, Class<Data> clazz) {
        synchronized (this) {
            object = this.multiModelLoaderFactory.build(object, clazz);
            return object;
        }
    }

    public List<Class<?>> getDataClasses(Class<?> object) {
        synchronized (this) {
            object = this.multiModelLoaderFactory.getDataClasses((Class<?>)object);
            return object;
        }
    }

    public <A> List<ModelLoader<A, ?>> getModelLoaders(A object) {
        List list = this.getModelLoadersForClass(ModelLoaderRegistry.getClass(object));
        if (!list.isEmpty()) {
            int n = list.size();
            boolean bl = true;
            List<ModelLoader<A, ?>> list2 = Collections.emptyList();
            for (int i = 0; i < n; ++i) {
                ModelLoader<A, ?> modelLoader = list.get(i);
                boolean bl2 = bl;
                List<ModelLoader<A, ?>> list3 = list2;
                if (modelLoader.handles(object)) {
                    bl2 = bl;
                    if (bl) {
                        list2 = new ArrayList(n - i);
                        bl2 = false;
                    }
                    list2.add(modelLoader);
                    list3 = list2;
                }
                bl = bl2;
                list2 = list3;
            }
            if (!list2.isEmpty()) {
                return list2;
            }
            throw new Registry.NoModelLoaderAvailableException(object, list);
        }
        object = new Registry.NoModelLoaderAvailableException(object);
        throw object;
    }

    public <Model, Data> void prepend(Class<Model> clazz, Class<Data> clazz2, ModelLoaderFactory<? extends Model, ? extends Data> modelLoaderFactory) {
        synchronized (this) {
            this.multiModelLoaderFactory.prepend(clazz, clazz2, modelLoaderFactory);
            this.cache.clear();
            return;
        }
    }

    public <Model, Data> void remove(Class<Model> clazz, Class<Data> clazz2) {
        synchronized (this) {
            this.tearDown(this.multiModelLoaderFactory.remove(clazz, clazz2));
            this.cache.clear();
            return;
        }
    }

    public <Model, Data> void replace(Class<Model> clazz, Class<Data> clazz2, ModelLoaderFactory<? extends Model, ? extends Data> modelLoaderFactory) {
        synchronized (this) {
            this.tearDown(this.multiModelLoaderFactory.replace(clazz, clazz2, modelLoaderFactory));
            this.cache.clear();
            return;
        }
    }

    private static class ModelLoaderCache {
        private final Map<Class<?>, Entry<?>> cachedModelLoaders = new HashMap();

        ModelLoaderCache() {
        }

        public void clear() {
            this.cachedModelLoaders.clear();
        }

        public <Model> List<ModelLoader<Model, ?>> get(Class<Model> list) {
            list = (list = this.cachedModelLoaders.get(list)) == null ? null : ((Entry)((Object)list)).loaders;
            return list;
        }

        public <Model> void put(Class<Model> clazz, List<ModelLoader<Model, ?>> object) {
            if (this.cachedModelLoaders.put(clazz, new Entry<Model>(object)) == null) {
                return;
            }
            object = new StringBuilder();
            ((StringBuilder)object).append("Already cached loaders for model: ");
            ((StringBuilder)object).append(clazz);
            throw new IllegalStateException(((StringBuilder)object).toString());
        }

        private static class Entry<Model> {
            final List<ModelLoader<Model, ?>> loaders;

            public Entry(List<ModelLoader<Model, ?>> list) {
                this.loaders = list;
            }
        }
    }
}

