/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.load.model;

import androidx.core.util.Pools;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.util.Preconditions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

class MultiModelLoader<Model, Data>
implements ModelLoader<Model, Data> {
    private final Pools.Pool<List<Throwable>> exceptionListPool;
    private final List<ModelLoader<Model, Data>> modelLoaders;

    MultiModelLoader(List<ModelLoader<Model, Data>> list, Pools.Pool<List<Throwable>> pool) {
        this.modelLoaders = list;
        this.exceptionListPool = pool;
    }

    @Override
    public ModelLoader.LoadData<Data> buildLoadData(Model object, int n, int n2, Options options) {
        Key key = null;
        int n3 = this.modelLoaders.size();
        ArrayList arrayList = new ArrayList(n3);
        for (int i = 0; i < n3; ++i) {
            Object object2 = this.modelLoaders.get(i);
            Key key2 = key;
            if (object2.handles(object)) {
                object2 = object2.buildLoadData(object, n, n2, options);
                key2 = key;
                if (object2 != null) {
                    key2 = ((ModelLoader.LoadData)object2).sourceKey;
                    arrayList.add(((ModelLoader.LoadData)object2).fetcher);
                }
            }
            key = key2;
        }
        object = !arrayList.isEmpty() && key != null ? new ModelLoader.LoadData(key, new MultiFetcher(arrayList, this.exceptionListPool)) : null;
        return object;
    }

    @Override
    public boolean handles(Model Model2) {
        Iterator<ModelLoader<Model, Data>> iterator2 = this.modelLoaders.iterator();
        while (iterator2.hasNext()) {
            if (!iterator2.next().handles(Model2)) continue;
            return true;
        }
        return false;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("MultiModelLoader{modelLoaders=");
        stringBuilder.append(Arrays.toString(this.modelLoaders.toArray()));
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    static class MultiFetcher<Data>
    implements DataFetcher<Data>,
    DataFetcher.DataCallback<Data> {
        private DataFetcher.DataCallback<? super Data> callback;
        private int currentIndex;
        private List<Throwable> exceptions;
        private final List<DataFetcher<Data>> fetchers;
        private boolean isCancelled;
        private Priority priority;
        private final Pools.Pool<List<Throwable>> throwableListPool;

        MultiFetcher(List<DataFetcher<Data>> list, Pools.Pool<List<Throwable>> pool) {
            this.throwableListPool = pool;
            Preconditions.checkNotEmpty(list);
            this.fetchers = list;
            this.currentIndex = 0;
        }

        private void startNextOrFail() {
            if (this.isCancelled) {
                return;
            }
            if (this.currentIndex < this.fetchers.size() - 1) {
                ++this.currentIndex;
                this.loadData(this.priority, this.callback);
            } else {
                Preconditions.checkNotNull(this.exceptions);
                this.callback.onLoadFailed(new GlideException("Fetch failed", new ArrayList<Throwable>(this.exceptions)));
            }
        }

        @Override
        public void cancel() {
            this.isCancelled = true;
            Iterator<DataFetcher<Data>> iterator2 = this.fetchers.iterator();
            while (iterator2.hasNext()) {
                iterator2.next().cancel();
            }
        }

        @Override
        public void cleanup() {
            List<Throwable> list = this.exceptions;
            if (list != null) {
                this.throwableListPool.release(list);
            }
            this.exceptions = null;
            list = this.fetchers.iterator();
            while (list.hasNext()) {
                ((DataFetcher)list.next()).cleanup();
            }
        }

        @Override
        public Class<Data> getDataClass() {
            return this.fetchers.get(0).getDataClass();
        }

        @Override
        public DataSource getDataSource() {
            return this.fetchers.get(0).getDataSource();
        }

        @Override
        public void loadData(Priority priority, DataFetcher.DataCallback<? super Data> dataCallback) {
            this.priority = priority;
            this.callback = dataCallback;
            this.exceptions = this.throwableListPool.acquire();
            this.fetchers.get(this.currentIndex).loadData(priority, this);
            if (this.isCancelled) {
                this.cancel();
            }
        }

        @Override
        public void onDataReady(Data Data) {
            if (Data != null) {
                this.callback.onDataReady(Data);
            } else {
                this.startNextOrFail();
            }
        }

        @Override
        public void onLoadFailed(Exception exception) {
            Preconditions.checkNotNull(this.exceptions).add(exception);
            this.startNextOrFail();
        }
    }
}

