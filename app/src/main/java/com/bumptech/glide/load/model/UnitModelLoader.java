/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.load.model;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.signature.ObjectKey;

public class UnitModelLoader<Model>
implements ModelLoader<Model, Model> {
    private static final UnitModelLoader<?> INSTANCE = new UnitModelLoader();

    @Deprecated
    public UnitModelLoader() {
    }

    public static <T> UnitModelLoader<T> getInstance() {
        return INSTANCE;
    }

    @Override
    public ModelLoader.LoadData<Model> buildLoadData(Model Model2, int n, int n2, Options options) {
        return new ModelLoader.LoadData<Model>(new ObjectKey(Model2), new UnitFetcher<Model>(Model2));
    }

    @Override
    public boolean handles(Model Model2) {
        return true;
    }

    public static class Factory<Model>
    implements ModelLoaderFactory<Model, Model> {
        private static final Factory<?> FACTORY = new Factory();

        @Deprecated
        public Factory() {
        }

        public static <T> Factory<T> getInstance() {
            return FACTORY;
        }

        @Override
        public ModelLoader<Model, Model> build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return UnitModelLoader.getInstance();
        }

        @Override
        public void teardown() {
        }
    }

    private static class UnitFetcher<Model>
    implements DataFetcher<Model> {
        private final Model resource;

        UnitFetcher(Model Model2) {
            this.resource = Model2;
        }

        @Override
        public void cancel() {
        }

        @Override
        public void cleanup() {
        }

        @Override
        public Class<Model> getDataClass() {
            return this.resource.getClass();
        }

        @Override
        public DataSource getDataSource() {
            return DataSource.LOCAL;
        }

        @Override
        public void loadData(Priority priority, DataFetcher.DataCallback<? super Model> dataCallback) {
            dataCallback.onDataReady(this.resource);
        }
    }
}

