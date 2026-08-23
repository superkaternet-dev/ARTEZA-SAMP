/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Build$VERSION
 */
package com.bumptech.glide;

import android.content.Context;
import android.os.Build;
import androidx.collection.ArrayMap;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideExperiments;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.load.engine.Engine;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPoolAdapter;
import com.bumptech.glide.load.engine.bitmap_recycle.LruArrayPool;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemoryCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.load.engine.executor.GlideExecutor;
import com.bumptech.glide.manager.ConnectivityMonitorFactory;
import com.bumptech.glide.manager.DefaultConnectivityMonitorFactory;
import com.bumptech.glide.manager.RequestManagerRetriever;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.util.Preconditions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class GlideBuilder {
    private GlideExecutor animationExecutor;
    private ArrayPool arrayPool;
    private BitmapPool bitmapPool;
    private ConnectivityMonitorFactory connectivityMonitorFactory;
    private List<RequestListener<Object>> defaultRequestListeners;
    private Glide.RequestOptionsFactory defaultRequestOptionsFactory;
    private final Map<Class<?>, TransitionOptions<?, ?>> defaultTransitionOptions = new ArrayMap();
    private GlideExecutor diskCacheExecutor;
    private DiskCache.Factory diskCacheFactory;
    private Engine engine;
    private final GlideExperiments.Builder glideExperimentsBuilder = new GlideExperiments.Builder();
    private boolean isActiveResourceRetentionAllowed;
    private int logLevel = 4;
    private MemoryCache memoryCache;
    private MemorySizeCalculator memorySizeCalculator;
    private RequestManagerRetriever.RequestManagerFactory requestManagerFactory;
    private GlideExecutor sourceExecutor;

    public GlideBuilder() {
        this.defaultRequestOptionsFactory = new Glide.RequestOptionsFactory(this){
            final GlideBuilder this$0;
            {
                this.this$0 = glideBuilder;
            }

            @Override
            public RequestOptions build() {
                return new RequestOptions();
            }
        };
    }

    public GlideBuilder addGlobalRequestListener(RequestListener<Object> requestListener) {
        if (this.defaultRequestListeners == null) {
            this.defaultRequestListeners = new ArrayList<RequestListener<Object>>();
        }
        this.defaultRequestListeners.add(requestListener);
        return this;
    }

    Glide build(Context context) {
        Object object;
        if (this.sourceExecutor == null) {
            this.sourceExecutor = GlideExecutor.newSourceExecutor();
        }
        if (this.diskCacheExecutor == null) {
            this.diskCacheExecutor = GlideExecutor.newDiskCacheExecutor();
        }
        if (this.animationExecutor == null) {
            this.animationExecutor = GlideExecutor.newAnimationExecutor();
        }
        if (this.memorySizeCalculator == null) {
            this.memorySizeCalculator = new MemorySizeCalculator.Builder(context).build();
        }
        if (this.connectivityMonitorFactory == null) {
            this.connectivityMonitorFactory = new DefaultConnectivityMonitorFactory();
        }
        if (this.bitmapPool == null) {
            int n = this.memorySizeCalculator.getBitmapPoolSize();
            this.bitmapPool = n > 0 ? new LruBitmapPool(n) : new BitmapPoolAdapter();
        }
        if (this.arrayPool == null) {
            this.arrayPool = new LruArrayPool(this.memorySizeCalculator.getArrayPoolSizeInBytes());
        }
        if (this.memoryCache == null) {
            this.memoryCache = new LruResourceCache(this.memorySizeCalculator.getMemoryCacheSize());
        }
        if (this.diskCacheFactory == null) {
            this.diskCacheFactory = new InternalCacheDiskCacheFactory(context);
        }
        if (this.engine == null) {
            this.engine = new Engine(this.memoryCache, this.diskCacheFactory, this.diskCacheExecutor, this.sourceExecutor, GlideExecutor.newUnlimitedSourceExecutor(), this.animationExecutor, this.isActiveResourceRetentionAllowed);
        }
        this.defaultRequestListeners = (object = this.defaultRequestListeners) == null ? Collections.emptyList() : Collections.unmodifiableList(object);
        object = this.glideExperimentsBuilder.build();
        RequestManagerRetriever requestManagerRetriever = new RequestManagerRetriever(this.requestManagerFactory, (GlideExperiments)object);
        return new Glide(context, this.engine, this.memoryCache, this.bitmapPool, this.arrayPool, requestManagerRetriever, this.connectivityMonitorFactory, this.logLevel, this.defaultRequestOptionsFactory, this.defaultTransitionOptions, this.defaultRequestListeners, (GlideExperiments)object);
    }

    public GlideBuilder setAnimationExecutor(GlideExecutor glideExecutor) {
        this.animationExecutor = glideExecutor;
        return this;
    }

    public GlideBuilder setArrayPool(ArrayPool arrayPool) {
        this.arrayPool = arrayPool;
        return this;
    }

    public GlideBuilder setBitmapPool(BitmapPool bitmapPool) {
        this.bitmapPool = bitmapPool;
        return this;
    }

    public GlideBuilder setConnectivityMonitorFactory(ConnectivityMonitorFactory connectivityMonitorFactory) {
        this.connectivityMonitorFactory = connectivityMonitorFactory;
        return this;
    }

    public GlideBuilder setDefaultRequestOptions(Glide.RequestOptionsFactory requestOptionsFactory) {
        this.defaultRequestOptionsFactory = Preconditions.checkNotNull(requestOptionsFactory);
        return this;
    }

    public GlideBuilder setDefaultRequestOptions(RequestOptions requestOptions) {
        return this.setDefaultRequestOptions(new Glide.RequestOptionsFactory(this, requestOptions){
            final GlideBuilder this$0;
            final RequestOptions val$requestOptions;
            {
                this.this$0 = glideBuilder;
                this.val$requestOptions = requestOptions;
            }

            @Override
            public RequestOptions build() {
                RequestOptions requestOptions = this.val$requestOptions;
                if (requestOptions == null) {
                    requestOptions = new RequestOptions();
                }
                return requestOptions;
            }
        });
    }

    public <T> GlideBuilder setDefaultTransitionOptions(Class<T> clazz, TransitionOptions<?, T> transitionOptions) {
        this.defaultTransitionOptions.put(clazz, transitionOptions);
        return this;
    }

    public GlideBuilder setDiskCache(DiskCache.Factory factory) {
        this.diskCacheFactory = factory;
        return this;
    }

    public GlideBuilder setDiskCacheExecutor(GlideExecutor glideExecutor) {
        this.diskCacheExecutor = glideExecutor;
        return this;
    }

    public GlideBuilder setEnableImageDecoderForAnimatedWebp(boolean bl) {
        this.glideExperimentsBuilder.update(new EnableImageDecoderForAnimatedWebp(), bl);
        return this;
    }

    GlideBuilder setEngine(Engine engine) {
        this.engine = engine;
        return this;
    }

    public GlideBuilder setImageDecoderEnabledForBitmaps(boolean bl) {
        GlideExperiments.Builder builder = this.glideExperimentsBuilder;
        EnableImageDecoderForBitmaps enableImageDecoderForBitmaps = new EnableImageDecoderForBitmaps();
        bl = bl && Build.VERSION.SDK_INT >= 29;
        builder.update(enableImageDecoderForBitmaps, bl);
        return this;
    }

    public GlideBuilder setIsActiveResourceRetentionAllowed(boolean bl) {
        this.isActiveResourceRetentionAllowed = bl;
        return this;
    }

    public GlideBuilder setLogLevel(int n) {
        if (n >= 2 && n <= 6) {
            this.logLevel = n;
            return this;
        }
        throw new IllegalArgumentException("Log level must be one of Log.VERBOSE, Log.DEBUG, Log.INFO, Log.WARN, or Log.ERROR");
    }

    public GlideBuilder setLogRequestOrigins(boolean bl) {
        this.glideExperimentsBuilder.update(new LogRequestOrigins(), bl);
        return this;
    }

    public GlideBuilder setMemoryCache(MemoryCache memoryCache) {
        this.memoryCache = memoryCache;
        return this;
    }

    public GlideBuilder setMemorySizeCalculator(MemorySizeCalculator.Builder builder) {
        return this.setMemorySizeCalculator(builder.build());
    }

    public GlideBuilder setMemorySizeCalculator(MemorySizeCalculator memorySizeCalculator) {
        this.memorySizeCalculator = memorySizeCalculator;
        return this;
    }

    void setRequestManagerFactory(RequestManagerRetriever.RequestManagerFactory requestManagerFactory) {
        this.requestManagerFactory = requestManagerFactory;
    }

    @Deprecated
    public GlideBuilder setResizeExecutor(GlideExecutor glideExecutor) {
        return this.setSourceExecutor(glideExecutor);
    }

    public GlideBuilder setSourceExecutor(GlideExecutor glideExecutor) {
        this.sourceExecutor = glideExecutor;
        return this;
    }

    static final class EnableImageDecoderForAnimatedWebp
    implements GlideExperiments.Experiment {
        EnableImageDecoderForAnimatedWebp() {
        }
    }

    static final class EnableImageDecoderForBitmaps
    implements GlideExperiments.Experiment {
        EnableImageDecoderForBitmaps() {
        }
    }

    public static final class LogRequestOrigins
    implements GlideExperiments.Experiment {
    }

    static final class ManualOverrideHardwareBitmapMaxFdCount
    implements GlideExperiments.Experiment {
        final int fdCount;

        ManualOverrideHardwareBitmapMaxFdCount(int n) {
            this.fdCount = n;
        }
    }

    public static final class WaitForFramesAfterTrimMemory
    implements GlideExperiments.Experiment {
        private WaitForFramesAfterTrimMemory() {
        }
    }
}

