/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.load.engine;

import androidx.core.util.Pools;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.CallbackException;
import com.bumptech.glide.load.engine.DecodeJob;
import com.bumptech.glide.load.engine.EngineJobListener;
import com.bumptech.glide.load.engine.EngineResource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.executor.GlideExecutor;
import com.bumptech.glide.request.ResourceCallback;
import com.bumptech.glide.util.Executors;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.pool.FactoryPools;
import com.bumptech.glide.util.pool.StateVerifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

class EngineJob<R>
implements DecodeJob.Callback<R>,
FactoryPools.Poolable {
    private static final EngineResourceFactory DEFAULT_FACTORY = new EngineResourceFactory();
    private final GlideExecutor animationExecutor;
    final ResourceCallbacksAndExecutors cbs = new ResourceCallbacksAndExecutors();
    DataSource dataSource;
    private DecodeJob<R> decodeJob;
    private final GlideExecutor diskCacheExecutor;
    private final EngineJobListener engineJobListener;
    EngineResource<?> engineResource;
    private final EngineResourceFactory engineResourceFactory;
    GlideException exception;
    private boolean hasLoadFailed;
    private boolean hasResource;
    private boolean isCacheable;
    private volatile boolean isCancelled;
    private boolean isLoadedFromAlternateCacheKey;
    private Key key;
    private boolean onlyRetrieveFromCache;
    private final AtomicInteger pendingCallbacks;
    private final Pools.Pool<EngineJob<?>> pool;
    private Resource<?> resource;
    private final EngineResource.ResourceListener resourceListener;
    private final GlideExecutor sourceExecutor;
    private final GlideExecutor sourceUnlimitedExecutor;
    private final StateVerifier stateVerifier = StateVerifier.newInstance();
    private boolean useAnimationPool;
    private boolean useUnlimitedSourceGeneratorPool;

    EngineJob(GlideExecutor glideExecutor, GlideExecutor glideExecutor2, GlideExecutor glideExecutor3, GlideExecutor glideExecutor4, EngineJobListener engineJobListener, EngineResource.ResourceListener resourceListener, Pools.Pool<EngineJob<?>> pool) {
        this(glideExecutor, glideExecutor2, glideExecutor3, glideExecutor4, engineJobListener, resourceListener, pool, DEFAULT_FACTORY);
    }

    EngineJob(GlideExecutor glideExecutor, GlideExecutor glideExecutor2, GlideExecutor glideExecutor3, GlideExecutor glideExecutor4, EngineJobListener engineJobListener, EngineResource.ResourceListener resourceListener, Pools.Pool<EngineJob<?>> pool, EngineResourceFactory engineResourceFactory) {
        this.pendingCallbacks = new AtomicInteger();
        this.diskCacheExecutor = glideExecutor;
        this.sourceExecutor = glideExecutor2;
        this.sourceUnlimitedExecutor = glideExecutor3;
        this.animationExecutor = glideExecutor4;
        this.engineJobListener = engineJobListener;
        this.resourceListener = resourceListener;
        this.pool = pool;
        this.engineResourceFactory = engineResourceFactory;
    }

    private GlideExecutor getActiveSourceExecutor() {
        GlideExecutor glideExecutor = this.useUnlimitedSourceGeneratorPool ? this.sourceUnlimitedExecutor : (this.useAnimationPool ? this.animationExecutor : this.sourceExecutor);
        return glideExecutor;
    }

    private boolean isDone() {
        boolean bl = this.hasLoadFailed || this.hasResource || this.isCancelled;
        return bl;
    }

    private void release() {
        synchronized (this) {
            if (this.key != null) {
                this.cbs.clear();
                this.key = null;
                this.engineResource = null;
                this.resource = null;
                this.hasLoadFailed = false;
                this.isCancelled = false;
                this.hasResource = false;
                this.isLoadedFromAlternateCacheKey = false;
                this.decodeJob.release(false);
                this.decodeJob = null;
                this.exception = null;
                this.dataSource = null;
                this.pool.release(this);
                return;
            }
            IllegalArgumentException illegalArgumentException = new IllegalArgumentException();
            throw illegalArgumentException;
        }
    }

    /*
     * WARNING - void declaration
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void addCallback(ResourceCallback resourceCallback, Executor executor) {
        synchronized (this) {
            void var2_2;
            this.stateVerifier.throwIfRecycled();
            this.cbs.add(resourceCallback, (Executor)var2_2);
            boolean bl = this.hasResource;
            boolean bl2 = true;
            if (bl) {
                this.incrementPendingCallbacks(1);
                CallResourceReady callResourceReady = new CallResourceReady(this, resourceCallback);
                var2_2.execute(callResourceReady);
            } else if (this.hasLoadFailed) {
                this.incrementPendingCallbacks(1);
                CallLoadFailed callLoadFailed = new CallLoadFailed(this, resourceCallback);
                var2_2.execute(callLoadFailed);
            } else {
                if (this.isCancelled) {
                    bl2 = false;
                }
                Preconditions.checkArgument(bl2, "Cannot add callbacks to a cancelled EngineJob");
            }
            return;
        }
    }

    void callCallbackOnLoadFailed(ResourceCallback resourceCallback) {
        try {
            resourceCallback.onLoadFailed(this.exception);
            return;
        }
        catch (Throwable throwable) {
            throw new CallbackException(throwable);
        }
    }

    void callCallbackOnResourceReady(ResourceCallback resourceCallback) {
        try {
            resourceCallback.onResourceReady(this.engineResource, this.dataSource, this.isLoadedFromAlternateCacheKey);
            return;
        }
        catch (Throwable throwable) {
            throw new CallbackException(throwable);
        }
    }

    void cancel() {
        if (this.isDone()) {
            return;
        }
        this.isCancelled = true;
        this.decodeJob.cancel();
        this.engineJobListener.onEngineJobCancelled(this, this.key);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    void decrementPendingCallbacks() {
        EngineResource<?> engineResource = null;
        // MONITORENTER : this
        this.stateVerifier.throwIfRecycled();
        Preconditions.checkArgument(this.isDone(), "Not yet complete!");
        int n = this.pendingCallbacks.decrementAndGet();
        boolean bl = n >= 0;
        Preconditions.checkArgument(bl, "Can't decrement below 0");
        if (n == 0) {
            engineResource = this.engineResource;
            this.release();
        }
        // MONITOREXIT : this
        if (engineResource == null) return;
        engineResource.release();
    }

    @Override
    public StateVerifier getVerifier() {
        return this.stateVerifier;
    }

    void incrementPendingCallbacks(int n) {
        synchronized (this) {
            EngineResource<?> engineResource;
            Preconditions.checkArgument(this.isDone(), "Not yet complete!");
            if (this.pendingCallbacks.getAndAdd(n) == 0 && (engineResource = this.engineResource) != null) {
                engineResource.acquire();
            }
            return;
        }
    }

    EngineJob<R> init(Key key, boolean bl, boolean bl2, boolean bl3, boolean bl4) {
        synchronized (this) {
            this.key = key;
            this.isCacheable = bl;
            this.useUnlimitedSourceGeneratorPool = bl2;
            this.useAnimationPool = bl3;
            this.onlyRetrieveFromCache = bl4;
            return this;
        }
    }

    boolean isCancelled() {
        synchronized (this) {
            boolean bl = this.isCancelled;
            return bl;
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void notifyCallbacksOfException() {
        synchronized (this) {
            this.stateVerifier.throwIfRecycled();
            if (this.isCancelled) {
                this.release();
                return;
            }
            if (this.cbs.isEmpty()) break block7;
            if (this.hasLoadFailed) ** break block8
            this.hasLoadFailed = true;
            var2_1 = this.key;
            var1_2 = this.cbs.copy();
            this.incrementPendingCallbacks(var1_2.size() + 1);
            // MONITOREXIT @DISABLED, blocks:[0, 2] lbl14 : MonitorExitStatement: MONITOREXIT : this
            this.engineJobListener.onEngineJobComplete(this, (Key)var2_1, null);
            var1_2 = var1_2.iterator();
        }
        while (true) {
            if (!var1_2.hasNext()) {
                this.decrementPendingCallbacks();
                return;
            }
            var2_1 = (ResourceCallbackAndExecutor)var1_2.next();
            var2_1.executor.execute(new CallLoadFailed(this, var2_1.cb));
        }
        {
            block7: {
                var1_3 = new IllegalStateException("Already failed once");
                throw var1_3;
            }
            var1_4 = new IllegalStateException("Received an exception without any callbacks to notify");
            throw var1_4;
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void notifyCallbacksOfResult() {
        synchronized (this) {
            this.stateVerifier.throwIfRecycled();
            if (this.isCancelled) {
                this.resource.recycle();
                this.release();
                return;
            }
            if (this.cbs.isEmpty()) break block7;
            if (this.hasResource) ** break block8
            this.engineResource = this.engineResourceFactory.build(this.resource, this.isCacheable, this.key, this.resourceListener);
            this.hasResource = true;
            var3_1 = this.cbs.copy();
            this.incrementPendingCallbacks(var3_1.size() + 1);
            var1_2 = this.key;
            var2_6 = this.engineResource;
            // MONITOREXIT @DISABLED, blocks:[0, 2] lbl17 : MonitorExitStatement: MONITOREXIT : this
            this.engineJobListener.onEngineJobComplete(this, (Key)var1_2, (EngineResource<?>)var2_6);
            var2_6 = var3_1.iterator();
        }
        while (true) {
            if (!var2_6.hasNext()) {
                this.decrementPendingCallbacks();
                return;
            }
            var1_2 = (ResourceCallbackAndExecutor)var2_6.next();
            var1_2.executor.execute(new CallResourceReady(this, var1_2.cb));
        }
        {
            block7: {
                var1_3 = new IllegalStateException("Already have resource");
                throw var1_3;
            }
            var1_4 = new IllegalStateException("Received a resource without any callbacks to notify");
            throw var1_4;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void onLoadFailed(GlideException glideException) {
        synchronized (this) {
            this.exception = glideException;
        }
        this.notifyCallbacksOfException();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void onResourceReady(Resource<R> resource, DataSource dataSource, boolean bl) {
        synchronized (this) {
            this.resource = resource;
            this.dataSource = dataSource;
            this.isLoadedFromAlternateCacheKey = bl;
        }
        this.notifyCallbacksOfResult();
    }

    boolean onlyRetrieveFromCache() {
        return this.onlyRetrieveFromCache;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    void removeCallback(ResourceCallback resourceCallback) {
        synchronized (this) {
            this.stateVerifier.throwIfRecycled();
            this.cbs.remove(resourceCallback);
            if (!this.cbs.isEmpty()) return;
            this.cancel();
            if (!this.hasResource) {
                if (!this.hasLoadFailed) return;
            }
            boolean bl = true;
            if (!bl) return;
            if (this.pendingCallbacks.get() != 0) return;
            this.release();
            return;
        }
    }

    @Override
    public void reschedule(DecodeJob<?> decodeJob) {
        this.getActiveSourceExecutor().execute(decodeJob);
    }

    public void start(DecodeJob<R> decodeJob) {
        synchronized (this) {
            this.decodeJob = decodeJob;
            GlideExecutor glideExecutor = decodeJob.willDecodeFromCache() ? this.diskCacheExecutor : this.getActiveSourceExecutor();
            glideExecutor.execute(decodeJob);
            return;
        }
    }

    private class CallLoadFailed
    implements Runnable {
        private final ResourceCallback cb;
        final EngineJob this$0;

        CallLoadFailed(EngineJob engineJob, ResourceCallback resourceCallback) {
            this.this$0 = engineJob;
            this.cb = resourceCallback;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void run() {
            Object object = this.cb.getLock();
            synchronized (object) {
                EngineJob engineJob = this.this$0;
                synchronized (engineJob) {
                    if (this.this$0.cbs.contains(this.cb)) {
                        this.this$0.callCallbackOnLoadFailed(this.cb);
                    }
                    this.this$0.decrementPendingCallbacks();
                    return;
                }
            }
        }
    }

    private class CallResourceReady
    implements Runnable {
        private final ResourceCallback cb;
        final EngineJob this$0;

        CallResourceReady(EngineJob engineJob, ResourceCallback resourceCallback) {
            this.this$0 = engineJob;
            this.cb = resourceCallback;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void run() {
            Object object = this.cb.getLock();
            synchronized (object) {
                EngineJob engineJob = this.this$0;
                synchronized (engineJob) {
                    if (this.this$0.cbs.contains(this.cb)) {
                        this.this$0.engineResource.acquire();
                        this.this$0.callCallbackOnResourceReady(this.cb);
                        this.this$0.removeCallback(this.cb);
                    }
                    this.this$0.decrementPendingCallbacks();
                    return;
                }
            }
        }
    }

    static class EngineResourceFactory {
        EngineResourceFactory() {
        }

        public <R> EngineResource<R> build(Resource<R> resource, boolean bl, Key key, EngineResource.ResourceListener resourceListener) {
            return new EngineResource<R>(resource, bl, true, key, resourceListener);
        }
    }

    static final class ResourceCallbackAndExecutor {
        final ResourceCallback cb;
        final Executor executor;

        ResourceCallbackAndExecutor(ResourceCallback resourceCallback, Executor executor) {
            this.cb = resourceCallback;
            this.executor = executor;
        }

        public boolean equals(Object object) {
            if (object instanceof ResourceCallbackAndExecutor) {
                object = (ResourceCallbackAndExecutor)object;
                return this.cb.equals(((ResourceCallbackAndExecutor)object).cb);
            }
            return false;
        }

        public int hashCode() {
            return this.cb.hashCode();
        }
    }

    static final class ResourceCallbacksAndExecutors
    implements Iterable<ResourceCallbackAndExecutor> {
        private final List<ResourceCallbackAndExecutor> callbacksAndExecutors;

        ResourceCallbacksAndExecutors() {
            this(new ArrayList<ResourceCallbackAndExecutor>(2));
        }

        ResourceCallbacksAndExecutors(List<ResourceCallbackAndExecutor> list) {
            this.callbacksAndExecutors = list;
        }

        private static ResourceCallbackAndExecutor defaultCallbackAndExecutor(ResourceCallback resourceCallback) {
            return new ResourceCallbackAndExecutor(resourceCallback, Executors.directExecutor());
        }

        void add(ResourceCallback resourceCallback, Executor executor) {
            this.callbacksAndExecutors.add(new ResourceCallbackAndExecutor(resourceCallback, executor));
        }

        void clear() {
            this.callbacksAndExecutors.clear();
        }

        boolean contains(ResourceCallback resourceCallback) {
            return this.callbacksAndExecutors.contains(ResourceCallbacksAndExecutors.defaultCallbackAndExecutor(resourceCallback));
        }

        ResourceCallbacksAndExecutors copy() {
            return new ResourceCallbacksAndExecutors(new ArrayList<ResourceCallbackAndExecutor>(this.callbacksAndExecutors));
        }

        boolean isEmpty() {
            return this.callbacksAndExecutors.isEmpty();
        }

        @Override
        public Iterator<ResourceCallbackAndExecutor> iterator() {
            return this.callbacksAndExecutors.iterator();
        }

        void remove(ResourceCallback resourceCallback) {
            this.callbacksAndExecutors.remove(ResourceCallbacksAndExecutors.defaultCallbackAndExecutor(resourceCallback));
        }

        int size() {
            return this.callbacksAndExecutors.size();
        }
    }
}

