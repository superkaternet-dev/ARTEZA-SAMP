/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.engine.DataCacheKey;
import com.bumptech.glide.load.engine.DataFetcherGenerator;
import com.bumptech.glide.load.engine.DecodeHelper;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.util.pool.GlideTrace;
import java.io.File;
import java.util.List;

class DataCacheGenerator
implements DataFetcherGenerator,
DataFetcher.DataCallback<Object> {
    private File cacheFile;
    private final List<Key> cacheKeys;
    private final DataFetcherGenerator.FetcherReadyCallback cb;
    private final DecodeHelper<?> helper;
    private volatile ModelLoader.LoadData<?> loadData;
    private int modelLoaderIndex;
    private List<ModelLoader<File, ?>> modelLoaders;
    private int sourceIdIndex = -1;
    private Key sourceKey;

    DataCacheGenerator(DecodeHelper<?> decodeHelper, DataFetcherGenerator.FetcherReadyCallback fetcherReadyCallback) {
        this(decodeHelper.getCacheKeys(), decodeHelper, fetcherReadyCallback);
    }

    DataCacheGenerator(List<Key> list, DecodeHelper<?> decodeHelper, DataFetcherGenerator.FetcherReadyCallback fetcherReadyCallback) {
        this.cacheKeys = list;
        this.helper = decodeHelper;
        this.cb = fetcherReadyCallback;
    }

    private boolean hasNextModelLoader() {
        boolean bl = this.modelLoaderIndex < this.modelLoaders.size();
        return bl;
    }

    @Override
    public void cancel() {
        ModelLoader.LoadData<?> loadData = this.loadData;
        if (loadData != null) {
            loadData.fetcher.cancel();
        }
    }

    @Override
    public void onDataReady(Object object) {
        this.cb.onDataFetcherReady(this.sourceKey, object, this.loadData.fetcher, DataSource.DATA_DISK_CACHE, this.sourceKey);
    }

    @Override
    public void onLoadFailed(Exception exception) {
        this.cb.onDataFetcherFailed(this.sourceKey, exception, this.loadData.fetcher, DataSource.DATA_DISK_CACHE);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public boolean startNext() {
        GlideTrace.beginSection("DataCacheGenerator.startNext");
        try {
            while (true) {
                int n;
                Object object;
                if (this.modelLoaders != null && this.hasNextModelLoader()) {
                    this.loadData = null;
                    boolean bl = false;
                    while (!bl && this.hasNextModelLoader()) {
                        object = this.modelLoaders;
                        n = this.modelLoaderIndex;
                        this.modelLoaderIndex = n + 1;
                        this.loadData = ((ModelLoader)object.get(n)).buildLoadData(this.cacheFile, this.helper.getWidth(), this.helper.getHeight(), this.helper.getOptions());
                        boolean bl2 = bl;
                        if (this.loadData != null) {
                            bl2 = bl;
                            if (this.helper.hasLoadPath(this.loadData.fetcher.getDataClass())) {
                                bl2 = true;
                                this.loadData.fetcher.loadData(this.helper.getPriority(), this);
                            }
                        }
                        bl = bl2;
                    }
                    return bl;
                }
                this.sourceIdIndex = n = this.sourceIdIndex + 1;
                int n2 = this.cacheKeys.size();
                if (n >= n2) {
                    return false;
                }
                object = this.cacheKeys.get(this.sourceIdIndex);
                Object object2 = new DataCacheKey((Key)object, this.helper.getSignature());
                this.cacheFile = object2 = this.helper.getDiskCache().get((Key)object2);
                if (object2 == null) continue;
                this.sourceKey = object;
                this.modelLoaders = this.helper.getModelLoaders((File)object2);
                this.modelLoaderIndex = 0;
            }
        }
        finally {
            GlideTrace.endSection();
        }
    }
}

