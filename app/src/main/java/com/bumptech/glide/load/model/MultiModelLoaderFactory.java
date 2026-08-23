/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.load.model;

import androidx.core.util.Pools;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoader;
import com.bumptech.glide.util.Preconditions;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MultiModelLoaderFactory {
    private static final Factory DEFAULT_FACTORY = new Factory();
    private static final ModelLoader<Object, Object> EMPTY_MODEL_LOADER = new EmptyModelLoader();
    private final Set<Entry<?, ?>> alreadyUsedEntries;
    private final List<Entry<?, ?>> entries = new ArrayList();
    private final Factory factory;
    private final Pools.Pool<List<Throwable>> throwableListPool;

    public MultiModelLoaderFactory(Pools.Pool<List<Throwable>> pool) {
        this(pool, DEFAULT_FACTORY);
    }

    MultiModelLoaderFactory(Pools.Pool<List<Throwable>> pool, Factory factory) {
        this.alreadyUsedEntries = new HashSet();
        this.throwableListPool = pool;
        this.factory = factory;
    }

    private <Model, Data> void add(Class<Model> object, Class<Data> object2, ModelLoaderFactory<? extends Model, ? extends Data> modelLoaderFactory, boolean bl) {
        object = new Entry<Model, Data>(object, object2, modelLoaderFactory);
        object2 = this.entries;
        int n = bl ? object2.size() : 0;
        object2.add(n, object);
    }

    private <Model, Data> ModelLoader<Model, Data> build(Entry<?, ?> entry) {
        return Preconditions.checkNotNull(entry.factory.build(this));
    }

    private static <Model, Data> ModelLoader<Model, Data> emptyModelLoader() {
        return EMPTY_MODEL_LOADER;
    }

    private <Model, Data> ModelLoaderFactory<Model, Data> getFactory(Entry<?, ?> entry) {
        return entry.factory;
    }

    <Model, Data> void append(Class<Model> clazz, Class<Data> clazz2, ModelLoaderFactory<? extends Model, ? extends Data> modelLoaderFactory) {
        synchronized (this) {
            this.add(clazz, clazz2, modelLoaderFactory, true);
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public <Model, Data> ModelLoader<Model, Data> build(Class<Model> modelLoader, Class<Data> clazz) {
        synchronized (this) {
            try {
                ArrayList arrayList = new ArrayList();
                boolean bl = false;
                for (Entry<Data, Data> entry : this.entries) {
                    if (this.alreadyUsedEntries.contains(entry)) {
                        bl = true;
                        continue;
                    }
                    if (!entry.handles((Class<?>)((Object)modelLoader), clazz)) continue;
                    this.alreadyUsedEntries.add(entry);
                    arrayList.add(this.build(entry));
                    this.alreadyUsedEntries.remove(entry);
                }
                if (arrayList.size() > 1) {
                    return this.factory.build(arrayList, this.throwableListPool);
                }
                if (arrayList.size() == 1) {
                    return (ModelLoader)arrayList.get(0);
                }
                if (bl) {
                    return MultiModelLoaderFactory.emptyModelLoader();
                }
                Registry.NoModelLoaderAvailableException noModelLoaderAvailableException = new Registry.NoModelLoaderAvailableException((Class<?>)((Object)modelLoader), clazz);
                throw noModelLoaderAvailableException;
            }
            catch (Throwable throwable) {
                this.alreadyUsedEntries.clear();
                throw throwable;
            }
        }
    }

    <Model> List<ModelLoader<Model, ?>> build(Class<Model> clazz) {
        synchronized (this) {
            try {
                ArrayList arrayList = new ArrayList();
                for (Entry<Model, Model> entry : this.entries) {
                    if (this.alreadyUsedEntries.contains(entry) || !entry.handles(clazz)) continue;
                    this.alreadyUsedEntries.add(entry);
                    arrayList.add(this.build(entry));
                    this.alreadyUsedEntries.remove(entry);
                }
                return arrayList;
            }
            catch (Throwable throwable) {
                try {
                    this.alreadyUsedEntries.clear();
                    throw throwable;
                }
                catch (Throwable throwable2) {
                    throw throwable2;
                }
            }
        }
    }

    List<Class<?>> getDataClasses(Class<?> clazz) {
        synchronized (this) {
            try {
                ArrayList arrayList = new ArrayList();
                for (Entry<?, ?> entry : this.entries) {
                    if (arrayList.contains(entry.dataClass) || !entry.handles(clazz)) continue;
                    arrayList.add(entry.dataClass);
                }
                return arrayList;
            }
            catch (Throwable throwable) {
                throw throwable;
            }
        }
    }

    <Model, Data> void prepend(Class<Model> clazz, Class<Data> clazz2, ModelLoaderFactory<? extends Model, ? extends Data> modelLoaderFactory) {
        synchronized (this) {
            this.add(clazz, clazz2, modelLoaderFactory, false);
            return;
        }
    }

    <Model, Data> List<ModelLoaderFactory<? extends Model, ? extends Data>> remove(Class<Model> clazz, Class<Data> clazz2) {
        synchronized (this) {
            try {
                ArrayList<ModelLoaderFactory<Model, Data>> arrayList = new ArrayList<ModelLoaderFactory<Model, Data>>();
                Iterator<Entry<?, ?>> iterator2 = this.entries.iterator();
                while (iterator2.hasNext()) {
                    Entry<Model, Model> entry = iterator2.next();
                    if (!entry.handles(clazz, clazz2)) continue;
                    iterator2.remove();
                    arrayList.add(this.getFactory(entry));
                }
                return arrayList;
            }
            catch (Throwable throwable) {
                throw throwable;
            }
        }
    }

    <Model, Data> List<ModelLoaderFactory<? extends Model, ? extends Data>> replace(Class<Model> clazz, Class<Data> clazz2, ModelLoaderFactory<? extends Model, ? extends Data> modelLoaderFactory) {
        synchronized (this) {
            List<ModelLoaderFactory<? extends Model, ? extends Data>> list = this.remove(clazz, clazz2);
            this.append(clazz, clazz2, modelLoaderFactory);
            return list;
        }
    }

    private static class EmptyModelLoader
    implements ModelLoader<Object, Object> {
        EmptyModelLoader() {
        }

        @Override
        public ModelLoader.LoadData<Object> buildLoadData(Object object, int n, int n2, Options options) {
            return null;
        }

        @Override
        public boolean handles(Object object) {
            return false;
        }
    }

    private static class Entry<Model, Data> {
        final Class<Data> dataClass;
        final ModelLoaderFactory<? extends Model, ? extends Data> factory;
        private final Class<Model> modelClass;

        public Entry(Class<Model> clazz, Class<Data> clazz2, ModelLoaderFactory<? extends Model, ? extends Data> modelLoaderFactory) {
            this.modelClass = clazz;
            this.dataClass = clazz2;
            this.factory = modelLoaderFactory;
        }

        public boolean handles(Class<?> clazz) {
            return this.modelClass.isAssignableFrom(clazz);
        }

        public boolean handles(Class<?> clazz, Class<?> clazz2) {
            boolean bl = this.handles(clazz) && this.dataClass.isAssignableFrom(clazz2);
            return bl;
        }
    }

    static class Factory {
        Factory() {
        }

        public <Model, Data> MultiModelLoader<Model, Data> build(List<ModelLoader<Model, Data>> list, Pools.Pool<List<Throwable>> pool) {
            return new MultiModelLoader<Model, Data>(list, pool);
        }
    }
}

