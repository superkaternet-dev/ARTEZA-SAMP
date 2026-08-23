/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Build$VERSION
 *  android.util.Log
 */
package com.bumptech.glide.load.engine;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import androidx.core.util.Pools;
import com.bumptech.glide.GlideContext;
import com.bumptech.glide.Priority;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.EncodeStrategy;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.data.DataRewinder;
import com.bumptech.glide.load.engine.CallbackException;
import com.bumptech.glide.load.engine.DataCacheGenerator;
import com.bumptech.glide.load.engine.DataCacheKey;
import com.bumptech.glide.load.engine.DataCacheWriter;
import com.bumptech.glide.load.engine.DataFetcherGenerator;
import com.bumptech.glide.load.engine.DecodeHelper;
import com.bumptech.glide.load.engine.DecodePath;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.EngineKey;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.engine.Initializable;
import com.bumptech.glide.load.engine.LoadPath;
import com.bumptech.glide.load.engine.LockedResource;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.ResourceCacheGenerator;
import com.bumptech.glide.load.engine.ResourceCacheKey;
import com.bumptech.glide.load.engine.SourceGenerator;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.resource.bitmap.Downsampler;
import com.bumptech.glide.util.LogTime;
import com.bumptech.glide.util.pool.FactoryPools;
import com.bumptech.glide.util.pool.GlideTrace;
import com.bumptech.glide.util.pool.StateVerifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class DecodeJob<R>
implements DataFetcherGenerator.FetcherReadyCallback,
Runnable,
Comparable<DecodeJob<?>>,
FactoryPools.Poolable {
    private static final String TAG = "DecodeJob";
    private Callback<R> callback;
    private Key currentAttemptingKey;
    private Object currentData;
    private DataSource currentDataSource;
    private DataFetcher<?> currentFetcher;
    private volatile DataFetcherGenerator currentGenerator;
    private Key currentSourceKey;
    private Thread currentThread;
    private final DecodeHelper<R> decodeHelper = new DecodeHelper();
    private final DeferredEncodeManager<?> deferredEncodeManager;
    private final DiskCacheProvider diskCacheProvider;
    private DiskCacheStrategy diskCacheStrategy;
    private GlideContext glideContext;
    private int height;
    private volatile boolean isCallbackNotified;
    private volatile boolean isCancelled;
    private boolean isLoadingFromAlternateCacheKey;
    private EngineKey loadKey;
    private Object model;
    private boolean onlyRetrieveFromCache;
    private Options options;
    private int order;
    private final Pools.Pool<DecodeJob<?>> pool;
    private Priority priority;
    private final ReleaseManager releaseManager;
    private RunReason runReason;
    private Key signature;
    private Stage stage;
    private long startFetchTime;
    private final StateVerifier stateVerifier;
    private final List<Throwable> throwables = new ArrayList<Throwable>();
    private int width;

    DecodeJob(DiskCacheProvider diskCacheProvider, Pools.Pool<DecodeJob<?>> pool) {
        this.stateVerifier = StateVerifier.newInstance();
        this.deferredEncodeManager = new DeferredEncodeManager();
        this.releaseManager = new ReleaseManager();
        this.diskCacheProvider = diskCacheProvider;
        this.pool = pool;
    }

    private <Data> Resource<R> decodeFromData(DataFetcher<?> dataFetcher, Data object, DataSource object2) throws GlideException {
        if (object == null) {
            dataFetcher.cleanup();
            return null;
        }
        try {
            long l = LogTime.getLogTime();
            object2 = this.decodeFromFetcher((Data)object, (DataSource)((Object)object2));
            if (Log.isLoggable((String)TAG, (int)2)) {
                object = new StringBuilder();
                ((StringBuilder)object).append("Decoded result ");
                ((StringBuilder)object).append(object2);
                this.logWithTimeAndKey(((StringBuilder)object).toString(), l);
            }
            return object2;
        }
        finally {
            dataFetcher.cleanup();
        }
    }

    private <Data> Resource<R> decodeFromFetcher(Data Data, DataSource dataSource) throws GlideException {
        return this.runLoadPath(Data, dataSource, this.decodeHelper.getLoadPath(Data.getClass()));
    }

    private void decodeFromRetrievedData() {
        Object object;
        if (Log.isLoggable((String)TAG, (int)2)) {
            long l = this.startFetchTime;
            object = new StringBuilder();
            ((StringBuilder)object).append("data: ");
            ((StringBuilder)object).append(this.currentData);
            ((StringBuilder)object).append(", cache key: ");
            ((StringBuilder)object).append(this.currentSourceKey);
            ((StringBuilder)object).append(", fetcher: ");
            ((StringBuilder)object).append(this.currentFetcher);
            this.logWithTimeAndKey("Retrieved data", l, ((StringBuilder)object).toString());
        }
        object = null;
        try {
            Resource<R> resource = this.decodeFromData(this.currentFetcher, this.currentData, this.currentDataSource);
            object = resource;
        }
        catch (GlideException glideException) {
            glideException.setLoggingDetails(this.currentAttemptingKey, this.currentDataSource);
            this.throwables.add(glideException);
        }
        if (object != null) {
            this.notifyEncodeAndRelease((Resource<R>)object, this.currentDataSource, this.isLoadingFromAlternateCacheKey);
        } else {
            this.runGenerators();
        }
    }

    private DataFetcherGenerator getNextGenerator() {
        switch (1.$SwitchMap$com$bumptech$glide$load$engine$DecodeJob$Stage[this.stage.ordinal()]) {
            default: {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unrecognized stage: ");
                stringBuilder.append((Object)this.stage);
                throw new IllegalStateException(stringBuilder.toString());
            }
            case 4: {
                return null;
            }
            case 3: {
                return new SourceGenerator(this.decodeHelper, this);
            }
            case 2: {
                return new DataCacheGenerator(this.decodeHelper, this);
            }
            case 1: 
        }
        return new ResourceCacheGenerator(this.decodeHelper, this);
    }

    private Stage getNextStage(Stage stage) {
        switch (1.$SwitchMap$com$bumptech$glide$load$engine$DecodeJob$Stage[stage.ordinal()]) {
            default: {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unrecognized stage: ");
                stringBuilder.append((Object)stage);
                throw new IllegalArgumentException(stringBuilder.toString());
            }
            case 5: {
                stage = this.diskCacheStrategy.decodeCachedResource() ? Stage.RESOURCE_CACHE : this.getNextStage(Stage.RESOURCE_CACHE);
                return stage;
            }
            case 3: 
            case 4: {
                return Stage.FINISHED;
            }
            case 2: {
                stage = this.onlyRetrieveFromCache ? Stage.FINISHED : Stage.SOURCE;
                return stage;
            }
            case 1: 
        }
        stage = this.diskCacheStrategy.decodeCachedData() ? Stage.DATA_CACHE : this.getNextStage(Stage.DATA_CACHE);
        return stage;
    }

    private Options getOptionsWithHardwareConfig(DataSource object) {
        Options options = this.options;
        if (Build.VERSION.SDK_INT < 26) {
            return options;
        }
        boolean bl = object == DataSource.RESOURCE_DISK_CACHE || this.decodeHelper.isScaleOnlyOrNoTransform();
        object = options.get(Downsampler.ALLOW_HARDWARE_CONFIG);
        if (object != null && (!((Boolean)object).booleanValue() || bl)) {
            return options;
        }
        object = new Options();
        ((Options)object).putAll(this.options);
        ((Options)object).set(Downsampler.ALLOW_HARDWARE_CONFIG, bl);
        return object;
    }

    private int getPriority() {
        return this.priority.ordinal();
    }

    private void logWithTimeAndKey(String string2, long l) {
        this.logWithTimeAndKey(string2, l, null);
    }

    private void logWithTimeAndKey(String charSequence, long l, String string2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append((String)charSequence);
        stringBuilder.append(" in ");
        stringBuilder.append(LogTime.getElapsedMillis(l));
        stringBuilder.append(", load key: ");
        stringBuilder.append(this.loadKey);
        if (string2 != null) {
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append(", ");
            ((StringBuilder)charSequence).append(string2);
            charSequence = ((StringBuilder)charSequence).toString();
        } else {
            charSequence = "";
        }
        stringBuilder.append((String)charSequence);
        stringBuilder.append(", thread: ");
        stringBuilder.append(Thread.currentThread().getName());
        Log.v((String)TAG, (String)stringBuilder.toString());
    }

    private void notifyComplete(Resource<R> resource, DataSource dataSource, boolean bl) {
        this.setNotifiedOrThrow();
        this.callback.onResourceReady(resource, dataSource, bl);
    }

    /*
     * Unable to fully structure code
     */
    private void notifyEncodeAndRelease(Resource<R> var1_1, DataSource var2_4, boolean var3_5) {
        block11: {
            GlideTrace.beginSection("DecodeJob.notifyEncodeAndRelease");
            if (var1_1 instanceof Initializable) {
                ((Initializable)var1_1).initialize();
            }
            var5_6 = var1_1;
            var4_7 = null;
            {
                catch (Throwable var1_3) {
                    throw var1_3;
                }
            }
            if (!this.deferredEncodeManager.hasResourceToEncode()) break block11;
            var4_7 = LockedResource.obtain(var1_1);
            var5_6 = var4_7;
        }
        this.notifyComplete(var5_6, var2_4, var3_5);
        this.stage = Stage.ENCODE;
        try {
            if (this.deferredEncodeManager.hasResourceToEncode()) {
                this.deferredEncodeManager.encode(this.diskCacheProvider, this.options);
            }
            if (var4_7 == null) ** GOTO lbl28
        }
        catch (Throwable var1_2) {
            if (var4_7 == null) ** GOTO lbl25
            var4_7.unlock();
lbl25:
            // 2 sources

            throw var1_2;
        }
        try {
            var4_7.unlock();
lbl28:
            // 2 sources

            this.onEncodeComplete();
            return;
        }
        finally {
            GlideTrace.endSection();
        }
    }

    private void notifyFailed() {
        this.setNotifiedOrThrow();
        GlideException glideException = new GlideException("Failed to load resource", new ArrayList<Throwable>(this.throwables));
        this.callback.onLoadFailed(glideException);
        this.onLoadFailed();
    }

    private void onEncodeComplete() {
        if (this.releaseManager.onEncodeComplete()) {
            this.releaseInternal();
        }
    }

    private void onLoadFailed() {
        if (this.releaseManager.onFailed()) {
            this.releaseInternal();
        }
    }

    private void releaseInternal() {
        this.releaseManager.reset();
        this.deferredEncodeManager.clear();
        this.decodeHelper.clear();
        this.isCallbackNotified = false;
        this.glideContext = null;
        this.signature = null;
        this.options = null;
        this.priority = null;
        this.loadKey = null;
        this.callback = null;
        this.stage = null;
        this.currentGenerator = null;
        this.currentThread = null;
        this.currentSourceKey = null;
        this.currentData = null;
        this.currentDataSource = null;
        this.currentFetcher = null;
        this.startFetchTime = 0L;
        this.isCancelled = false;
        this.model = null;
        this.throwables.clear();
        this.pool.release(this);
    }

    private void runGenerators() {
        boolean bl;
        block2: {
            this.currentThread = Thread.currentThread();
            this.startFetchTime = LogTime.getLogTime();
            boolean bl2 = false;
            do {
                boolean bl3;
                bl = bl2;
                if (this.isCancelled) break block2;
                bl = bl2;
                if (this.currentGenerator == null) break block2;
                bl = bl2 = (bl3 = this.currentGenerator.startNext());
                if (bl3) break block2;
                this.stage = this.getNextStage(this.stage);
                this.currentGenerator = this.getNextGenerator();
            } while (this.stage != Stage.SOURCE);
            this.reschedule();
            return;
        }
        if ((this.stage == Stage.FINISHED || this.isCancelled) && !bl) {
            this.notifyFailed();
        }
    }

    private <Data, ResourceType> Resource<R> runLoadPath(Data object, DataSource object2, LoadPath<Data, ResourceType, R> loadPath) throws GlideException {
        Options options = this.getOptionsWithHardwareConfig((DataSource)((Object)object2));
        object = this.glideContext.getRegistry().getRewinder(object);
        try {
            int n = this.width;
            int n2 = this.height;
            DecodeCallback decodeCallback = new DecodeCallback(this, (DataSource)((Object)object2));
            object2 = loadPath.load((DataRewinder<Data>)object, options, n, n2, decodeCallback);
            return object2;
        }
        finally {
            object.cleanup();
        }
    }

    private void runWrapped() {
        switch (1.$SwitchMap$com$bumptech$glide$load$engine$DecodeJob$RunReason[this.runReason.ordinal()]) {
            default: {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unrecognized run reason: ");
                stringBuilder.append((Object)this.runReason);
                throw new IllegalStateException(stringBuilder.toString());
            }
            case 3: {
                this.decodeFromRetrievedData();
                break;
            }
            case 2: {
                this.runGenerators();
                break;
            }
            case 1: {
                this.stage = this.getNextStage(Stage.INITIALIZE);
                this.currentGenerator = this.getNextGenerator();
                this.runGenerators();
            }
        }
    }

    private void setNotifiedOrThrow() {
        this.stateVerifier.throwIfRecycled();
        if (this.isCallbackNotified) {
            Object object;
            if (this.throwables.isEmpty()) {
                object = null;
            } else {
                object = this.throwables;
                object = object.get(object.size() - 1);
            }
            throw new IllegalStateException("Already notified", (Throwable)object);
        }
        this.isCallbackNotified = true;
    }

    public void cancel() {
        this.isCancelled = true;
        DataFetcherGenerator dataFetcherGenerator = this.currentGenerator;
        if (dataFetcherGenerator != null) {
            dataFetcherGenerator.cancel();
        }
    }

    @Override
    public int compareTo(DecodeJob<?> decodeJob) {
        int n;
        int n2 = n = this.getPriority() - super.getPriority();
        if (n == 0) {
            n2 = this.order - decodeJob.order;
        }
        return n2;
    }

    @Override
    public StateVerifier getVerifier() {
        return this.stateVerifier;
    }

    DecodeJob<R> init(GlideContext glideContext, Object object, EngineKey engineKey, Key key, int n, int n2, Class<?> clazz, Class<R> clazz2, Priority priority, DiskCacheStrategy diskCacheStrategy, Map<Class<?>, Transformation<?>> map, boolean bl, boolean bl2, boolean bl3, Options options, Callback<R> callback, int n3) {
        this.decodeHelper.init(glideContext, object, key, n, n2, diskCacheStrategy, clazz, clazz2, priority, options, map, bl, bl2, this.diskCacheProvider);
        this.glideContext = glideContext;
        this.signature = key;
        this.priority = priority;
        this.loadKey = engineKey;
        this.width = n;
        this.height = n2;
        this.diskCacheStrategy = diskCacheStrategy;
        this.onlyRetrieveFromCache = bl3;
        this.options = options;
        this.callback = callback;
        this.order = n3;
        this.runReason = RunReason.INITIALIZE;
        this.model = object;
        return this;
    }

    @Override
    public void onDataFetcherFailed(Key key, Exception exception, DataFetcher<?> dataFetcher, DataSource dataSource) {
        dataFetcher.cleanup();
        exception = new GlideException("Fetching data failed", exception);
        ((GlideException)exception).setLoggingDetails(key, dataSource, dataFetcher.getDataClass());
        this.throwables.add(exception);
        if (Thread.currentThread() != this.currentThread) {
            this.runReason = RunReason.SWITCH_TO_SOURCE_SERVICE;
            this.callback.reschedule(this);
        } else {
            this.runGenerators();
        }
    }

    @Override
    public void onDataFetcherReady(Key key, Object list, DataFetcher<?> dataFetcher, DataSource dataSource, Key key2) {
        this.currentSourceKey = key;
        this.currentData = list;
        this.currentFetcher = dataFetcher;
        this.currentDataSource = dataSource;
        this.currentAttemptingKey = key2;
        list = this.decodeHelper.getCacheKeys();
        boolean bl = false;
        if (key != list.get(0)) {
            bl = true;
        }
        this.isLoadingFromAlternateCacheKey = bl;
        if (Thread.currentThread() != this.currentThread) {
            this.runReason = RunReason.DECODE_DATA;
            this.callback.reschedule(this);
        } else {
            GlideTrace.beginSection("DecodeJob.decodeFromRetrievedData");
            this.decodeFromRetrievedData();
        }
        return;
        finally {
            GlideTrace.endSection();
        }
    }

    <Z> Resource<Z> onResourceDecoded(DataSource object, Resource<Z> object2) {
        EncodeStrategy encodeStrategy;
        Resource<Object> resource;
        Transformation<?> transformation;
        Class<?> clazz = object2.get().getClass();
        if (object != DataSource.RESOURCE_DISK_CACHE) {
            transformation = this.decodeHelper.getTransformation(clazz);
            resource = transformation.transform((Context)this.glideContext, (Resource<?>)object2, this.width, this.height);
        } else {
            transformation = null;
            resource = object2;
        }
        if (!object2.equals(resource)) {
            object2.recycle();
        }
        if (this.decodeHelper.isResourceEncoderAvailable(resource)) {
            object2 = this.decodeHelper.getResultEncoder(resource);
            encodeStrategy = object2.getEncodeStrategy(this.options);
        } else {
            encodeStrategy = EncodeStrategy.NONE;
            object2 = null;
        }
        Resource<Object> resource2 = resource;
        boolean bl = this.decodeHelper.isSourceKey(this.currentSourceKey);
        if (this.diskCacheStrategy.isResourceCacheable(bl ^ true, (DataSource)((Object)object), encodeStrategy)) {
            if (object2 != null) {
                switch (1.$SwitchMap$com$bumptech$glide$load$EncodeStrategy[encodeStrategy.ordinal()]) {
                    default: {
                        object = new StringBuilder();
                        ((StringBuilder)object).append("Unknown strategy: ");
                        ((StringBuilder)object).append((Object)encodeStrategy);
                        throw new IllegalArgumentException(((StringBuilder)object).toString());
                    }
                    case 2: {
                        object = new ResourceCacheKey(this.decodeHelper.getArrayPool(), this.currentSourceKey, this.signature, this.width, this.height, transformation, clazz, this.options);
                        break;
                    }
                    case 1: {
                        object = new DataCacheKey(this.currentSourceKey, this.signature);
                    }
                }
                resource2 = LockedResource.obtain(resource);
                this.deferredEncodeManager.init((Key)object, object2, resource2);
            } else {
                throw new Registry.NoResultEncoderAvailableException(resource.get().getClass());
            }
        }
        return resource2;
    }

    void release(boolean bl) {
        if (this.releaseManager.release(bl)) {
            this.releaseInternal();
        }
    }

    @Override
    public void reschedule() {
        this.runReason = RunReason.SWITCH_TO_SOURCE_SERVICE;
        this.callback.reschedule(this);
    }

    /*
     * Loose catch block
     */
    @Override
    public void run() {
        block13: {
            DataFetcher<?> dataFetcher;
            block11: {
                block12: {
                    GlideTrace.beginSectionFormat("DecodeJob#run(reason=%s, model=%s)", (Object)this.runReason, this.model);
                    dataFetcher = this.currentFetcher;
                    if (!this.isCancelled) break block11;
                    this.notifyFailed();
                    if (dataFetcher == null) break block12;
                    dataFetcher.cleanup();
                }
                GlideTrace.endSection();
                return;
            }
            try {
                this.runWrapped();
                if (dataFetcher == null) break block13;
            }
            catch (Throwable throwable) {
                if (Log.isLoggable((String)TAG, (int)3)) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("DecodeJob threw unexpectedly, isCancelled: ");
                    stringBuilder.append(this.isCancelled);
                    stringBuilder.append(", stage: ");
                    stringBuilder.append((Object)this.stage);
                    Log.d((String)TAG, (String)stringBuilder.toString(), (Throwable)throwable);
                }
                if (this.stage != Stage.ENCODE) {
                    this.throwables.add(throwable);
                    this.notifyFailed();
                }
                if (!this.isCancelled) {
                    throw throwable;
                }
                throw throwable;
            }
            catch (CallbackException callbackException) {
                throw callbackException;
                {
                    catch (Throwable throwable2) {
                        if (dataFetcher != null) {
                            dataFetcher.cleanup();
                        }
                        GlideTrace.endSection();
                        throw throwable2;
                    }
                }
            }
            dataFetcher.cleanup();
        }
        GlideTrace.endSection();
        return;
    }

    boolean willDecodeFromCache() {
        Stage stage = this.getNextStage(Stage.INITIALIZE);
        boolean bl = stage == Stage.RESOURCE_CACHE || stage == Stage.DATA_CACHE;
        return bl;
    }

    static interface Callback<R> {
        public void onLoadFailed(GlideException var1);

        public void onResourceReady(Resource<R> var1, DataSource var2, boolean var3);

        public void reschedule(DecodeJob<?> var1);
    }

    private final class DecodeCallback<Z>
    implements DecodePath.DecodeCallback<Z> {
        private final DataSource dataSource;
        final DecodeJob this$0;

        DecodeCallback(DecodeJob decodeJob, DataSource dataSource) {
            this.this$0 = decodeJob;
            this.dataSource = dataSource;
        }

        @Override
        public Resource<Z> onResourceDecoded(Resource<Z> resource) {
            return this.this$0.onResourceDecoded(this.dataSource, resource);
        }
    }

    private static class DeferredEncodeManager<Z> {
        private ResourceEncoder<Z> encoder;
        private Key key;
        private LockedResource<Z> toEncode;

        DeferredEncodeManager() {
        }

        void clear() {
            this.key = null;
            this.encoder = null;
            this.toEncode = null;
        }

        void encode(DiskCacheProvider object, Options options) {
            GlideTrace.beginSection("DecodeJob.encode");
            try {
                object = object.getDiskCache();
                Key key = this.key;
                DataCacheWriter<LockedResource<Z>> dataCacheWriter = new DataCacheWriter<LockedResource<Z>>(this.encoder, this.toEncode, options);
                object.put(key, dataCacheWriter);
                return;
            }
            finally {
                this.toEncode.unlock();
                GlideTrace.endSection();
            }
        }

        boolean hasResourceToEncode() {
            boolean bl = this.toEncode != null;
            return bl;
        }

        <X> void init(Key key, ResourceEncoder<X> resourceEncoder, LockedResource<X> lockedResource) {
            this.key = key;
            this.encoder = resourceEncoder;
            this.toEncode = lockedResource;
        }
    }

    static interface DiskCacheProvider {
        public DiskCache getDiskCache();
    }

    private static class ReleaseManager {
        private boolean isEncodeComplete;
        private boolean isFailed;
        private boolean isReleased;

        ReleaseManager() {
        }

        private boolean isComplete(boolean bl) {
            bl = (this.isFailed || bl || this.isEncodeComplete) && this.isReleased;
            return bl;
        }

        boolean onEncodeComplete() {
            synchronized (this) {
                this.isEncodeComplete = true;
                boolean bl = this.isComplete(false);
                return bl;
            }
        }

        boolean onFailed() {
            synchronized (this) {
                this.isFailed = true;
                boolean bl = this.isComplete(false);
                return bl;
            }
        }

        boolean release(boolean bl) {
            synchronized (this) {
                this.isReleased = true;
                bl = this.isComplete(bl);
                return bl;
            }
        }

        void reset() {
            synchronized (this) {
                this.isEncodeComplete = false;
                this.isReleased = false;
                this.isFailed = false;
                return;
            }
        }
    }

    private static final class RunReason
    extends Enum<RunReason> {
        private static final RunReason[] $VALUES;
        public static final /* enum */ RunReason DECODE_DATA;
        public static final /* enum */ RunReason INITIALIZE;
        public static final /* enum */ RunReason SWITCH_TO_SOURCE_SERVICE;

        static {
            RunReason runReason;
            RunReason runReason2;
            RunReason runReason3;
            INITIALIZE = runReason3 = new RunReason();
            SWITCH_TO_SOURCE_SERVICE = runReason2 = new RunReason();
            DECODE_DATA = runReason = new RunReason();
            $VALUES = new RunReason[]{runReason3, runReason2, runReason};
        }

        public static RunReason valueOf(String string2) {
            return Enum.valueOf(RunReason.class, string2);
        }

        public static RunReason[] values() {
            return (RunReason[])$VALUES.clone();
        }
    }

    private static final class Stage
    extends Enum<Stage> {
        private static final Stage[] $VALUES;
        public static final /* enum */ Stage DATA_CACHE;
        public static final /* enum */ Stage ENCODE;
        public static final /* enum */ Stage FINISHED;
        public static final /* enum */ Stage INITIALIZE;
        public static final /* enum */ Stage RESOURCE_CACHE;
        public static final /* enum */ Stage SOURCE;

        static {
            Stage stage;
            Stage stage2;
            Stage stage3;
            Stage stage4;
            Stage stage5;
            Stage stage6;
            INITIALIZE = stage6 = new Stage();
            RESOURCE_CACHE = stage5 = new Stage();
            DATA_CACHE = stage4 = new Stage();
            SOURCE = stage3 = new Stage();
            ENCODE = stage2 = new Stage();
            FINISHED = stage = new Stage();
            $VALUES = new Stage[]{stage6, stage5, stage4, stage3, stage2, stage};
        }

        public static Stage valueOf(String string2) {
            return Enum.valueOf(Stage.class, string2);
        }

        public static Stage[] values() {
            return (Stage[])$VALUES.clone();
        }
    }
}

