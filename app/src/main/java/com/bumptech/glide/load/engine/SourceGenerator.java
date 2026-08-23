/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package com.bumptech.glide.load.engine;

import android.util.Log;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.data.DataRewinder;
import com.bumptech.glide.load.engine.DataCacheGenerator;
import com.bumptech.glide.load.engine.DataCacheKey;
import com.bumptech.glide.load.engine.DataCacheWriter;
import com.bumptech.glide.load.engine.DataFetcherGenerator;
import com.bumptech.glide.load.engine.DecodeHelper;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.util.LogTime;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

class SourceGenerator
implements DataFetcherGenerator,
DataFetcherGenerator.FetcherReadyCallback {
    private static final String TAG = "SourceGenerator";
    private final DataFetcherGenerator.FetcherReadyCallback cb;
    private volatile Object dataToCache;
    private final DecodeHelper<?> helper;
    private volatile ModelLoader.LoadData<?> loadData;
    private volatile int loadDataListIndex;
    private volatile DataCacheKey originalKey;
    private volatile DataCacheGenerator sourceCacheGenerator;

    SourceGenerator(DecodeHelper<?> decodeHelper, DataFetcherGenerator.FetcherReadyCallback fetcherReadyCallback) {
        this.helper = decodeHelper;
        this.cb = fetcherReadyCallback;
    }

    private boolean cacheData(Object object) throws IOException {
        DataRewinder<Object> dataRewinder;
        boolean bl;
        block42: {
            Object object2;
            boolean bl2;
            block41: {
                DataCacheKey dataCacheKey;
                Object object3;
                block40: {
                    long l = LogTime.getLogTime();
                    bl = bl2 = false;
                    dataRewinder = this.helper.getRewinder(object);
                    bl = bl2;
                    object3 = dataRewinder.rewindAndGet();
                    bl = bl2;
                    object2 = this.helper.getSourceEncoder(object3);
                    bl = bl2;
                    bl = bl2;
                    Object object4 = new DataCacheWriter(object2, object3, this.helper.getOptions());
                    bl = bl2;
                    bl = bl2;
                    dataCacheKey = new DataCacheKey(this.loadData.sourceKey, this.helper.getSignature());
                    bl = bl2;
                    object3 = this.helper.getDiskCache();
                    bl = bl2;
                    object3.put(dataCacheKey, (DiskCache.Writer)object4);
                    bl = bl2;
                    boolean bl3 = Log.isLoggable((String)TAG, (int)2);
                    if (!bl3) break block40;
                    bl = bl2;
                    bl = bl2;
                    object4 = new StringBuilder();
                    bl = bl2;
                    ((StringBuilder)object4).append("Finished encoding source to cache, key: ");
                    bl = bl2;
                    ((StringBuilder)object4).append(dataCacheKey);
                    bl = bl2;
                    ((StringBuilder)object4).append(", data: ");
                    bl = bl2;
                    ((StringBuilder)object4).append(object);
                    bl = bl2;
                    ((StringBuilder)object4).append(", encoder: ");
                    bl = bl2;
                    ((StringBuilder)object4).append(object2);
                    bl = bl2;
                    ((StringBuilder)object4).append(", duration: ");
                    bl = bl2;
                    ((StringBuilder)object4).append(LogTime.getElapsedMillis(l));
                    bl = bl2;
                    Log.v((String)TAG, (String)((StringBuilder)object4).toString());
                }
                bl = bl2;
                if (object3.get(dataCacheKey) == null) break block41;
                bl = bl2;
                this.originalKey = dataCacheKey;
                bl = bl2;
                bl = bl2;
                object = new DataCacheGenerator(Collections.singletonList(this.loadData.sourceKey), this.helper, this);
                bl = bl2;
                this.sourceCacheGenerator = object;
                return true;
            }
            bl = bl2;
            if (!Log.isLoggable((String)TAG, (int)3)) break block42;
            bl = bl2;
            bl = bl2;
            object2 = new StringBuilder();
            bl = bl2;
            ((StringBuilder)object2).append("Attempt to write: ");
            bl = bl2;
            ((StringBuilder)object2).append(this.originalKey);
            bl = bl2;
            ((StringBuilder)object2).append(", data: ");
            bl = bl2;
            ((StringBuilder)object2).append(object);
            bl = bl2;
            ((StringBuilder)object2).append(" to the disk cache failed, maybe the disk cache is disabled? Trying to decode the data directly...");
            bl = bl2;
            Log.d((String)TAG, (String)((StringBuilder)object2).toString());
        }
        bl = true;
        try {
            this.cb.onDataFetcherReady(this.loadData.sourceKey, dataRewinder.rewindAndGet(), this.loadData.fetcher, this.loadData.fetcher.getDataSource(), this.loadData.sourceKey);
            if (!true) {
                this.loadData.fetcher.cleanup();
            }
            return false;
        }
        finally {
            if (!bl) {
                this.loadData.fetcher.cleanup();
            }
        }
    }

    private boolean hasNextModelLoader() {
        boolean bl = this.loadDataListIndex < this.helper.getLoadData().size();
        return bl;
    }

    private void startNextLoad(ModelLoader.LoadData<?> loadData) {
        this.loadData.fetcher.loadData(this.helper.getPriority(), new DataFetcher.DataCallback<Object>(this, loadData){
            final SourceGenerator this$0;
            final ModelLoader.LoadData val$toStart;
            {
                this.this$0 = sourceGenerator;
                this.val$toStart = loadData;
            }

            @Override
            public void onDataReady(Object object) {
                if (this.this$0.isCurrentRequest(this.val$toStart)) {
                    this.this$0.onDataReadyInternal(this.val$toStart, object);
                }
            }

            @Override
            public void onLoadFailed(Exception exception) {
                if (this.this$0.isCurrentRequest(this.val$toStart)) {
                    this.this$0.onLoadFailedInternal(this.val$toStart, exception);
                }
            }
        });
    }

    @Override
    public void cancel() {
        ModelLoader.LoadData<?> loadData = this.loadData;
        if (loadData != null) {
            loadData.fetcher.cancel();
        }
    }

    boolean isCurrentRequest(ModelLoader.LoadData<?> loadData) {
        ModelLoader.LoadData<?> loadData2 = this.loadData;
        boolean bl = loadData2 != null && loadData2 == loadData;
        return bl;
    }

    @Override
    public void onDataFetcherFailed(Key key, Exception exception, DataFetcher<?> dataFetcher, DataSource dataSource) {
        this.cb.onDataFetcherFailed(key, exception, dataFetcher, this.loadData.fetcher.getDataSource());
    }

    @Override
    public void onDataFetcherReady(Key key, Object object, DataFetcher<?> dataFetcher, DataSource dataSource, Key key2) {
        this.cb.onDataFetcherReady(key, object, dataFetcher, this.loadData.fetcher.getDataSource(), key);
    }

    void onDataReadyInternal(ModelLoader.LoadData<?> loadData, Object object) {
        DiskCacheStrategy diskCacheStrategy = this.helper.getDiskCacheStrategy();
        if (object != null && diskCacheStrategy.isDataCacheable(loadData.fetcher.getDataSource())) {
            this.dataToCache = object;
            this.cb.reschedule();
        } else {
            this.cb.onDataFetcherReady(loadData.sourceKey, object, loadData.fetcher, loadData.fetcher.getDataSource(), this.originalKey);
        }
    }

    void onLoadFailedInternal(ModelLoader.LoadData<?> loadData, Exception exception) {
        this.cb.onDataFetcherFailed(this.originalKey, exception, loadData.fetcher, loadData.fetcher.getDataSource());
    }

    @Override
    public void reschedule() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean startNext() {
        boolean bl;
        List<ModelLoader.LoadData<?>> list;
        block6: {
            if (this.dataToCache != null) {
                list = this.dataToCache;
                this.dataToCache = null;
                try {
                    bl = this.cacheData(list);
                    if (!bl) {
                        return true;
                    }
                }
                catch (IOException iOException) {
                    if (!Log.isLoggable((String)TAG, (int)3)) break block6;
                    Log.d((String)TAG, (String)"Failed to properly rewind or write data to cache", (Throwable)iOException);
                }
            }
        }
        if (this.sourceCacheGenerator != null && this.sourceCacheGenerator.startNext()) {
            return true;
        }
        this.sourceCacheGenerator = null;
        this.loadData = null;
        bl = false;
        while (!bl && this.hasNextModelLoader()) {
            list = this.helper.getLoadData();
            int n = this.loadDataListIndex;
            this.loadDataListIndex = n + 1;
            this.loadData = list.get(n);
            if (this.loadData == null || !this.helper.getDiskCacheStrategy().isDataCacheable(this.loadData.fetcher.getDataSource()) && !this.helper.hasLoadPath(this.loadData.fetcher.getDataClass())) continue;
            bl = true;
            this.startNextLoad(this.loadData);
        }
        return bl;
    }
}

