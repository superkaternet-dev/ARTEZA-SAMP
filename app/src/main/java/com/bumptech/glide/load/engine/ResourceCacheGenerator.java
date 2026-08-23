/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.engine.DataFetcherGenerator;
import com.bumptech.glide.load.engine.DecodeHelper;
import com.bumptech.glide.load.engine.ResourceCacheKey;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.util.pool.GlideTrace;
import java.io.File;
import java.io.Serializable;
import java.util.List;

class ResourceCacheGenerator
implements DataFetcherGenerator,
DataFetcher.DataCallback<Object> {
    private File cacheFile;
    private final DataFetcherGenerator.FetcherReadyCallback cb;
    private ResourceCacheKey currentKey;
    private final DecodeHelper<?> helper;
    private volatile ModelLoader.LoadData<?> loadData;
    private int modelLoaderIndex;
    private List<ModelLoader<File, ?>> modelLoaders;
    private int resourceClassIndex = -1;
    private int sourceIdIndex;
    private Key sourceKey;

    ResourceCacheGenerator(DecodeHelper<?> decodeHelper, DataFetcherGenerator.FetcherReadyCallback fetcherReadyCallback) {
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
        this.cb.onDataFetcherReady(this.sourceKey, object, this.loadData.fetcher, DataSource.RESOURCE_DISK_CACHE, this.currentKey);
    }

    @Override
    public void onLoadFailed(Exception exception) {
        this.cb.onDataFetcherFailed(this.currentKey, exception, this.loadData.fetcher, DataSource.RESOURCE_DISK_CACHE);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public boolean startNext() {
        GlideTrace.beginSection("ResourceCacheGenerator.startNext");
        try {
            List list = this.helper.getCacheKeys();
            boolean bl = list.isEmpty();
            if (bl) {
                return false;
            }
            List<Class<?>> list2 = this.helper.getRegisteredResourceClasses();
            if (list2.isEmpty()) {
                bl = File.class.equals(this.helper.getTranscodeClass());
                if (bl) {
                    return false;
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to find any load path from ");
                stringBuilder.append(this.helper.getModelClass());
                stringBuilder.append(" to ");
                stringBuilder.append(this.helper.getTranscodeClass());
                list = new List(stringBuilder.toString());
                throw list;
            }
            while (true) {
                ResourceCacheKey resourceCacheKey;
                int n;
                if (this.modelLoaders != null && this.hasNextModelLoader()) {
                    this.loadData = null;
                    bl = false;
                    while (!bl && this.hasNextModelLoader()) {
                        list = this.modelLoaders;
                        n = this.modelLoaderIndex;
                        this.modelLoaderIndex = n + 1;
                        this.loadData = list.get(n).buildLoadData(this.cacheFile, this.helper.getWidth(), this.helper.getHeight(), this.helper.getOptions());
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
                this.resourceClassIndex = n = this.resourceClassIndex + 1;
                if (n >= list2.size()) {
                    this.sourceIdIndex = n = this.sourceIdIndex + 1;
                    int n2 = list.size();
                    if (n >= n2) {
                        return false;
                    }
                    this.resourceClassIndex = 0;
                }
                Key key = (Key)((Object)list.get(this.sourceIdIndex));
                Serializable serializable = list2.get(this.resourceClassIndex);
                Transformation<?> transformation = this.helper.getTransformation(serializable);
                this.currentKey = resourceCacheKey = new ResourceCacheKey(this.helper.getArrayPool(), key, this.helper.getSignature(), this.helper.getWidth(), this.helper.getHeight(), transformation, (Class<?>)serializable, this.helper.getOptions());
                serializable = this.helper.getDiskCache().get(this.currentKey);
                this.cacheFile = serializable;
                if (serializable == null) continue;
                this.sourceKey = key;
                this.modelLoaders = this.helper.getModelLoaders((File)serializable);
                this.modelLoaderIndex = 0;
            }
        }
        finally {
            GlideTrace.endSection();
        }
    }
}

