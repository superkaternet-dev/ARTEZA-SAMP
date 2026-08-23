/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package com.bumptech.glide.load.engine;

import android.util.Log;
import androidx.core.util.Pools;
import com.bumptech.glide.GlideContext;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.ActiveResources;
import com.bumptech.glide.load.engine.DecodeJob;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.EngineJob;
import com.bumptech.glide.load.engine.EngineJobListener;
import com.bumptech.glide.load.engine.EngineKey;
import com.bumptech.glide.load.engine.EngineKeyFactory;
import com.bumptech.glide.load.engine.EngineResource;
import com.bumptech.glide.load.engine.Jobs;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.ResourceRecycler;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskCacheAdapter;
import com.bumptech.glide.load.engine.cache.MemoryCache;
import com.bumptech.glide.load.engine.executor.GlideExecutor;
import com.bumptech.glide.request.ResourceCallback;
import com.bumptech.glide.util.Executors;
import com.bumptech.glide.util.LogTime;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.pool.FactoryPools;
import java.util.Map;
import java.util.concurrent.Executor;

public class Engine
implements EngineJobListener,
MemoryCache.ResourceRemovedListener,
EngineResource.ResourceListener {
    private static final int JOB_POOL_SIZE = 150;
    private static final String TAG = "Engine";
    private static final boolean VERBOSE_IS_LOGGABLE = Log.isLoggable((String)"Engine", (int)2);
    private final ActiveResources activeResources;
    private final MemoryCache cache;
    private final DecodeJobFactory decodeJobFactory;
    private final LazyDiskCacheProvider diskCacheProvider;
    private final EngineJobFactory engineJobFactory;
    private final Jobs jobs;
    private final EngineKeyFactory keyFactory;
    private final ResourceRecycler resourceRecycler;

    Engine(MemoryCache memoryCache, DiskCache.Factory object, GlideExecutor glideExecutor, GlideExecutor glideExecutor2, GlideExecutor glideExecutor3, GlideExecutor glideExecutor4, Jobs jobs, EngineKeyFactory engineKeyFactory, ActiveResources activeResources, EngineJobFactory engineJobFactory, DecodeJobFactory decodeJobFactory, ResourceRecycler resourceRecycler, boolean bl) {
        LazyDiskCacheProvider lazyDiskCacheProvider;
        this.cache = memoryCache;
        this.diskCacheProvider = lazyDiskCacheProvider = new LazyDiskCacheProvider((DiskCache.Factory)object);
        object = activeResources == null ? new ActiveResources(bl) : activeResources;
        this.activeResources = object;
        ((ActiveResources)object).setListener(this);
        if (engineKeyFactory == null) {
            engineKeyFactory = new EngineKeyFactory();
        }
        this.keyFactory = engineKeyFactory;
        object = jobs == null ? new Jobs() : jobs;
        this.jobs = object;
        if (engineJobFactory == null) {
            engineJobFactory = new EngineJobFactory(glideExecutor, glideExecutor2, glideExecutor3, glideExecutor4, this, this);
        }
        this.engineJobFactory = engineJobFactory;
        object = decodeJobFactory == null ? new DecodeJobFactory(lazyDiskCacheProvider) : decodeJobFactory;
        this.decodeJobFactory = object;
        object = resourceRecycler == null ? new ResourceRecycler() : resourceRecycler;
        this.resourceRecycler = object;
        memoryCache.setResourceRemovedListener(this);
    }

    public Engine(MemoryCache memoryCache, DiskCache.Factory factory, GlideExecutor glideExecutor, GlideExecutor glideExecutor2, GlideExecutor glideExecutor3, GlideExecutor glideExecutor4, boolean bl) {
        this(memoryCache, factory, glideExecutor, glideExecutor2, glideExecutor3, glideExecutor4, null, null, null, null, null, null, bl);
    }

    private EngineResource<?> getEngineResourceFromCache(Key engineResource) {
        Resource<?> resource = this.cache.remove((Key)((Object)engineResource));
        engineResource = resource == null ? null : (resource instanceof EngineResource ? (EngineResource)resource : new EngineResource(resource, true, true, (Key)((Object)engineResource), this));
        return engineResource;
    }

    private EngineResource<?> loadFromActiveResources(Key object) {
        if ((object = this.activeResources.get((Key)object)) != null) {
            ((EngineResource)object).acquire();
        }
        return object;
    }

    private EngineResource<?> loadFromCache(Key key) {
        EngineResource<?> engineResource = this.getEngineResourceFromCache(key);
        if (engineResource != null) {
            engineResource.acquire();
            this.activeResources.activate(key, engineResource);
        }
        return engineResource;
    }

    private EngineResource<?> loadFromMemory(EngineKey engineKey, boolean bl, long l) {
        if (!bl) {
            return null;
        }
        EngineResource<?> engineResource = this.loadFromActiveResources(engineKey);
        if (engineResource != null) {
            if (VERBOSE_IS_LOGGABLE) {
                Engine.logWithTimeAndKey("Loaded resource from active resources", l, engineKey);
            }
            return engineResource;
        }
        engineResource = this.loadFromCache(engineKey);
        if (engineResource != null) {
            if (VERBOSE_IS_LOGGABLE) {
                Engine.logWithTimeAndKey("Loaded resource from cache", l, engineKey);
            }
            return engineResource;
        }
        return null;
    }

    private static void logWithTimeAndKey(String string2, long l, Key key) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(string2);
        stringBuilder.append(" in ");
        stringBuilder.append(LogTime.getElapsedMillis(l));
        stringBuilder.append("ms, key: ");
        stringBuilder.append(key);
        Log.v((String)TAG, (String)stringBuilder.toString());
    }

    private <R> LoadStatus waitForExistingOrStartNewJob(GlideContext object, Object object2, Key key, int n, int n2, Class<?> clazz, Class<R> clazz2, Priority priority, DiskCacheStrategy diskCacheStrategy, Map<Class<?>, Transformation<?>> map, boolean bl, boolean bl2, Options options, boolean bl3, boolean bl4, boolean bl5, boolean bl6, ResourceCallback resourceCallback, Executor executor, EngineKey engineKey, long l) {
        EngineJob<Object> engineJob = this.jobs.get(engineKey, bl6);
        if (engineJob != null) {
            engineJob.addCallback(resourceCallback, executor);
            if (VERBOSE_IS_LOGGABLE) {
                Engine.logWithTimeAndKey("Added to existing load", l, engineKey);
            }
            return new LoadStatus(this, resourceCallback, engineJob);
        }
        engineJob = this.engineJobFactory.build(engineKey, bl3, bl4, bl5, bl6);
        object = this.decodeJobFactory.build((GlideContext)((Object)object), object2, engineKey, key, n, n2, clazz, clazz2, priority, diskCacheStrategy, map, bl, bl2, bl6, options, engineJob);
        this.jobs.put(engineKey, engineJob);
        engineJob.addCallback(resourceCallback, executor);
        engineJob.start((DecodeJob<Object>)object);
        if (VERBOSE_IS_LOGGABLE) {
            Engine.logWithTimeAndKey("Started new load", l, engineKey);
        }
        return new LoadStatus(this, resourceCallback, engineJob);
    }

    public void clearDiskCache() {
        this.diskCacheProvider.getDiskCache().clear();
    }

    /*
     * WARNING - void declaration
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public <R> LoadStatus load(GlideContext object, Object object2, Key key, int n, int n2, Class<?> clazz, Class<R> clazz2, Priority priority, DiskCacheStrategy diskCacheStrategy, Map<Class<?>, Transformation<?>> map, boolean bl, boolean bl2, Options options, boolean bl3, boolean bl4, boolean bl5, boolean bl6, ResourceCallback resourceCallback, Executor executor) {
        void var18_21;
        void var14_17;
        void var13_16;
        void var7_10;
        void var6_9;
        void var10_13;
        void var5_8;
        void var4_7;
        void var3_6;
        void var2_5;
        long l = VERBOSE_IS_LOGGABLE ? LogTime.getLogTime() : 0L;
        EngineKey engineKey = this.keyFactory.buildKey(var2_5, (Key)var3_6, (int)var4_7, (int)var5_8, (Map<Class<?>, Transformation<?>>)var10_13, (Class<?>)var6_9, (Class<?>)var7_10, (Options)var13_16);
        // MONITORENTER : this
        EngineResource<?> engineResource = this.loadFromMemory(engineKey, (boolean)var14_17, l);
        if (engineResource != null) {
            var18_21.onResourceReady(engineResource, DataSource.MEMORY_CACHE, false);
            return null;
        }
        try {
            void var19_22;
            void var17_20;
            void var16_19;
            void var15_18;
            void var12_15;
            void var11_14;
            void var9_12;
            void var8_11;
            object = this.waitForExistingOrStartNewJob((GlideContext)((Object)object), var2_5, (Key)var3_6, (int)var4_7, (int)var5_8, (Class<?>)var6_9, (Class<R>)var7_10, (Priority)var8_11, (DiskCacheStrategy)var9_12, (Map<Class<?>, Transformation<?>>)var10_13, (boolean)var11_14, (boolean)var12_15, (Options)var13_16, (boolean)var14_17, (boolean)var15_18, (boolean)var16_19, (boolean)var17_20, (ResourceCallback)var18_21, (Executor)var19_22, engineKey, l);
            // MONITOREXIT : this
            return object;
        }
        catch (Throwable throwable) {
            throw throwable;
        }
    }

    @Override
    public void onEngineJobCancelled(EngineJob<?> engineJob, Key key) {
        synchronized (this) {
            this.jobs.removeIfCurrent(key, engineJob);
            return;
        }
    }

    /*
     * WARNING - void declaration
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void onEngineJobComplete(EngineJob<?> engineJob, Key key, EngineResource<?> engineResource) {
        synchronized (this) {
            void var2_2;
            void var3_3;
            if (var3_3 != null && var3_3.isMemoryCacheable()) {
                this.activeResources.activate((Key)var2_2, (EngineResource<?>)var3_3);
            }
            this.jobs.removeIfCurrent((Key)var2_2, engineJob);
            return;
        }
    }

    @Override
    public void onResourceReleased(Key key, EngineResource<?> engineResource) {
        this.activeResources.deactivate(key);
        if (engineResource.isMemoryCacheable()) {
            this.cache.put(key, engineResource);
        } else {
            this.resourceRecycler.recycle(engineResource, false);
        }
    }

    @Override
    public void onResourceRemoved(Resource<?> resource) {
        this.resourceRecycler.recycle(resource, true);
    }

    public void release(Resource<?> resource) {
        if (resource instanceof EngineResource) {
            ((EngineResource)resource).release();
            return;
        }
        throw new IllegalArgumentException("Cannot release anything but an EngineResource");
    }

    public void shutdown() {
        this.engineJobFactory.shutdown();
        this.diskCacheProvider.clearDiskCacheIfCreated();
        this.activeResources.shutdown();
    }

    static class DecodeJobFactory {
        private int creationOrder;
        final DecodeJob.DiskCacheProvider diskCacheProvider;
        final Pools.Pool<DecodeJob<?>> pool = FactoryPools.threadSafe(150, new FactoryPools.Factory<DecodeJob<?>>(this){
            final DecodeJobFactory this$0;
            {
                this.this$0 = decodeJobFactory;
            }

            @Override
            public DecodeJob<?> create() {
                return new DecodeJob(this.this$0.diskCacheProvider, this.this$0.pool);
            }
        });

        DecodeJobFactory(DecodeJob.DiskCacheProvider diskCacheProvider) {
            this.diskCacheProvider = diskCacheProvider;
        }

        <R> DecodeJob<R> build(GlideContext glideContext, Object object, EngineKey engineKey, Key key, int n, int n2, Class<?> clazz, Class<R> clazz2, Priority priority, DiskCacheStrategy diskCacheStrategy, Map<Class<?>, Transformation<?>> map, boolean bl, boolean bl2, boolean bl3, Options options, DecodeJob.Callback<R> callback) {
            DecodeJob<?> decodeJob = Preconditions.checkNotNull(this.pool.acquire());
            int n3 = this.creationOrder;
            this.creationOrder = n3 + 1;
            return decodeJob.init(glideContext, object, engineKey, key, n, n2, clazz, clazz2, priority, diskCacheStrategy, map, bl, bl2, bl3, options, callback, n3);
        }
    }

    static class EngineJobFactory {
        final GlideExecutor animationExecutor;
        final GlideExecutor diskCacheExecutor;
        final EngineJobListener engineJobListener;
        final Pools.Pool<EngineJob<?>> pool = FactoryPools.threadSafe(150, new FactoryPools.Factory<EngineJob<?>>(this){
            final EngineJobFactory this$0;
            {
                this.this$0 = engineJobFactory;
            }

            @Override
            public EngineJob<?> create() {
                return new EngineJob(this.this$0.diskCacheExecutor, this.this$0.sourceExecutor, this.this$0.sourceUnlimitedExecutor, this.this$0.animationExecutor, this.this$0.engineJobListener, this.this$0.resourceListener, this.this$0.pool);
            }
        });
        final EngineResource.ResourceListener resourceListener;
        final GlideExecutor sourceExecutor;
        final GlideExecutor sourceUnlimitedExecutor;

        EngineJobFactory(GlideExecutor glideExecutor, GlideExecutor glideExecutor2, GlideExecutor glideExecutor3, GlideExecutor glideExecutor4, EngineJobListener engineJobListener, EngineResource.ResourceListener resourceListener) {
            this.diskCacheExecutor = glideExecutor;
            this.sourceExecutor = glideExecutor2;
            this.sourceUnlimitedExecutor = glideExecutor3;
            this.animationExecutor = glideExecutor4;
            this.engineJobListener = engineJobListener;
            this.resourceListener = resourceListener;
        }

        <R> EngineJob<R> build(Key key, boolean bl, boolean bl2, boolean bl3, boolean bl4) {
            return Preconditions.checkNotNull(this.pool.acquire()).init(key, bl, bl2, bl3, bl4);
        }

        void shutdown() {
            Executors.shutdownAndAwaitTermination(this.diskCacheExecutor);
            Executors.shutdownAndAwaitTermination(this.sourceExecutor);
            Executors.shutdownAndAwaitTermination(this.sourceUnlimitedExecutor);
            Executors.shutdownAndAwaitTermination(this.animationExecutor);
        }
    }

    private static class LazyDiskCacheProvider
    implements DecodeJob.DiskCacheProvider {
        private volatile DiskCache diskCache;
        private final DiskCache.Factory factory;

        LazyDiskCacheProvider(DiskCache.Factory factory) {
            this.factory = factory;
        }

        void clearDiskCacheIfCreated() {
            synchronized (this) {
                block4: {
                    DiskCache diskCache = this.diskCache;
                    if (diskCache != null) break block4;
                    return;
                }
                this.diskCache.clear();
                return;
            }
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public DiskCache getDiskCache() {
            if (this.diskCache != null) return this.diskCache;
            synchronized (this) {
                if (this.diskCache == null) {
                    this.diskCache = this.factory.build();
                }
                if (this.diskCache != null) return this.diskCache;
                DiskCacheAdapter diskCacheAdapter = new DiskCacheAdapter();
                this.diskCache = diskCacheAdapter;
                return this.diskCache;
            }
        }
    }

    public class LoadStatus {
        private final ResourceCallback cb;
        private final EngineJob<?> engineJob;
        final Engine this$0;

        LoadStatus(Engine engine, ResourceCallback resourceCallback, EngineJob<?> engineJob) {
            this.this$0 = engine;
            this.cb = resourceCallback;
            this.engineJob = engineJob;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        public void cancel() {
            Engine engine = this.this$0;
            synchronized (engine) {
                this.engineJob.removeCallback(this.cb);
                return;
            }
        }
    }
}

