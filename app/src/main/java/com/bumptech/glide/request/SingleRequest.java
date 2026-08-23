/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources$Theme
 *  android.graphics.drawable.Drawable
 *  android.util.Log
 */
package com.bumptech.glide.request;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.GlideContext;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.Engine;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.drawable.DrawableDecoderCompat;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.ExperimentalRequestListener;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestCoordinator;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.ResourceCallback;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.request.transition.TransitionFactory;
import com.bumptech.glide.util.LogTime;
import com.bumptech.glide.util.Util;
import com.bumptech.glide.util.pool.GlideTrace;
import com.bumptech.glide.util.pool.StateVerifier;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

public final class SingleRequest<R>
implements Request,
SizeReadyCallback,
ResourceCallback {
    private static final String GLIDE_TAG = "Glide";
    private static final boolean IS_VERBOSE_LOGGABLE = Log.isLoggable((String)"GlideRequest", (int)2);
    private static final String TAG = "GlideRequest";
    private final TransitionFactory<? super R> animationFactory;
    private final Executor callbackExecutor;
    private final Context context;
    private int cookie;
    private volatile Engine engine;
    private Drawable errorDrawable;
    private Drawable fallbackDrawable;
    private final GlideContext glideContext;
    private int height;
    private boolean isCallingCallbacks;
    private Engine.LoadStatus loadStatus;
    private final Object model;
    private final int overrideHeight;
    private final int overrideWidth;
    private Drawable placeholderDrawable;
    private final Priority priority;
    private final RequestCoordinator requestCoordinator;
    private final List<RequestListener<R>> requestListeners;
    private final Object requestLock;
    private final BaseRequestOptions<?> requestOptions;
    private RuntimeException requestOrigin;
    private Resource<R> resource;
    private long startTime;
    private final StateVerifier stateVerifier;
    private Status status;
    private final String tag;
    private final Target<R> target;
    private final RequestListener<R> targetListener;
    private final Class<R> transcodeClass;
    private int width;

    private SingleRequest(Context context, GlideContext glideContext, Object object, Object object2, Class<R> clazz, BaseRequestOptions<?> baseRequestOptions, int n, int n2, Priority priority, Target<R> target, RequestListener<R> requestListener, List<RequestListener<R>> list, RequestCoordinator requestCoordinator, Engine engine, TransitionFactory<? super R> transitionFactory, Executor executor) {
        String string2 = IS_VERBOSE_LOGGABLE ? String.valueOf(super.hashCode()) : null;
        this.tag = string2;
        this.stateVerifier = StateVerifier.newInstance();
        this.requestLock = object;
        this.context = context;
        this.glideContext = glideContext;
        this.model = object2;
        this.transcodeClass = clazz;
        this.requestOptions = baseRequestOptions;
        this.overrideWidth = n;
        this.overrideHeight = n2;
        this.priority = priority;
        this.target = target;
        this.targetListener = requestListener;
        this.requestListeners = list;
        this.requestCoordinator = requestCoordinator;
        this.engine = engine;
        this.animationFactory = transitionFactory;
        this.callbackExecutor = executor;
        this.status = Status.PENDING;
        if (this.requestOrigin == null && glideContext.getExperiments().isEnabled(GlideBuilder.LogRequestOrigins.class)) {
            this.requestOrigin = new RuntimeException("Glide request origin trace");
        }
    }

    private void assertNotCallingCallbacks() {
        if (!this.isCallingCallbacks) {
            return;
        }
        throw new IllegalStateException("You can't start or clear loads in RequestListener or Target callbacks. If you're trying to start a fallback request when a load fails, use RequestBuilder#error(RequestBuilder). Otherwise consider posting your into() or clear() calls to the main thread using a Handler instead.");
    }

    private boolean canNotifyCleared() {
        RequestCoordinator requestCoordinator = this.requestCoordinator;
        boolean bl = requestCoordinator == null || requestCoordinator.canNotifyCleared(this);
        return bl;
    }

    private boolean canNotifyStatusChanged() {
        RequestCoordinator requestCoordinator = this.requestCoordinator;
        boolean bl = requestCoordinator == null || requestCoordinator.canNotifyStatusChanged(this);
        return bl;
    }

    private boolean canSetResource() {
        RequestCoordinator requestCoordinator = this.requestCoordinator;
        boolean bl = requestCoordinator == null || requestCoordinator.canSetImage(this);
        return bl;
    }

    private void cancel() {
        this.assertNotCallingCallbacks();
        this.stateVerifier.throwIfRecycled();
        this.target.removeCallback(this);
        Engine.LoadStatus loadStatus = this.loadStatus;
        if (loadStatus != null) {
            loadStatus.cancel();
            this.loadStatus = null;
        }
    }

    private void experimentalNotifyRequestStarted(Object object) {
        List<RequestListener<R>> list = this.requestListeners;
        if (list == null) {
            return;
        }
        for (RequestListener requestListener : list) {
            if (!(requestListener instanceof ExperimentalRequestListener)) continue;
            ((ExperimentalRequestListener)requestListener).onRequestStarted(object);
        }
    }

    private Drawable getErrorDrawable() {
        if (this.errorDrawable == null) {
            Drawable drawable2;
            this.errorDrawable = drawable2 = this.requestOptions.getErrorPlaceholder();
            if (drawable2 == null && this.requestOptions.getErrorId() > 0) {
                this.errorDrawable = this.loadDrawable(this.requestOptions.getErrorId());
            }
        }
        return this.errorDrawable;
    }

    private Drawable getFallbackDrawable() {
        if (this.fallbackDrawable == null) {
            Drawable drawable2;
            this.fallbackDrawable = drawable2 = this.requestOptions.getFallbackDrawable();
            if (drawable2 == null && this.requestOptions.getFallbackId() > 0) {
                this.fallbackDrawable = this.loadDrawable(this.requestOptions.getFallbackId());
            }
        }
        return this.fallbackDrawable;
    }

    private Drawable getPlaceholderDrawable() {
        if (this.placeholderDrawable == null) {
            Drawable drawable2;
            this.placeholderDrawable = drawable2 = this.requestOptions.getPlaceholderDrawable();
            if (drawable2 == null && this.requestOptions.getPlaceholderId() > 0) {
                this.placeholderDrawable = this.loadDrawable(this.requestOptions.getPlaceholderId());
            }
        }
        return this.placeholderDrawable;
    }

    private boolean isFirstReadyResource() {
        RequestCoordinator requestCoordinator = this.requestCoordinator;
        boolean bl = requestCoordinator == null || !requestCoordinator.getRoot().isAnyResourceSet();
        return bl;
    }

    private Drawable loadDrawable(int n) {
        Resources.Theme theme = this.requestOptions.getTheme() != null ? this.requestOptions.getTheme() : this.context.getTheme();
        return DrawableDecoderCompat.getDrawable((Context)this.glideContext, n, theme);
    }

    private void logV(String string2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(string2);
        stringBuilder.append(" this: ");
        stringBuilder.append(this.tag);
        Log.v((String)TAG, (String)stringBuilder.toString());
    }

    private static int maybeApplySizeMultiplier(int n, float f) {
        if (n != Integer.MIN_VALUE) {
            n = Math.round((float)n * f);
        }
        return n;
    }

    private void notifyLoadFailed() {
        RequestCoordinator requestCoordinator = this.requestCoordinator;
        if (requestCoordinator != null) {
            requestCoordinator.onRequestFailed(this);
        }
    }

    private void notifyLoadSuccess() {
        RequestCoordinator requestCoordinator = this.requestCoordinator;
        if (requestCoordinator != null) {
            requestCoordinator.onRequestSuccess(this);
        }
    }

    public static <R> SingleRequest<R> obtain(Context context, GlideContext glideContext, Object object, Object object2, Class<R> clazz, BaseRequestOptions<?> baseRequestOptions, int n, int n2, Priority priority, Target<R> target, RequestListener<R> requestListener, List<RequestListener<R>> list, RequestCoordinator requestCoordinator, Engine engine, TransitionFactory<? super R> transitionFactory, Executor executor) {
        return new SingleRequest<R>(context, glideContext, object, object2, clazz, baseRequestOptions, n, n2, priority, target, requestListener, list, requestCoordinator, engine, transitionFactory, executor);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void onLoadFailed(GlideException glideException, int n) {
        this.stateVerifier.throwIfRecycled();
        Object object = this.requestLock;
        synchronized (object) {
            Object object2;
            glideException.setOrigin(this.requestOrigin);
            int n2 = this.glideContext.getLogLevel();
            if (n2 <= n) {
                object2 = new StringBuilder();
                ((StringBuilder)object2).append("Load failed for ");
                ((StringBuilder)object2).append(this.model);
                ((StringBuilder)object2).append(" with size [");
                ((StringBuilder)object2).append(this.width);
                ((StringBuilder)object2).append("x");
                ((StringBuilder)object2).append(this.height);
                ((StringBuilder)object2).append("]");
                Log.w((String)GLIDE_TAG, (String)((StringBuilder)object2).toString(), (Throwable)glideException);
                if (n2 <= 4) {
                    glideException.logRootCauses(GLIDE_TAG);
                }
            }
            this.loadStatus = null;
            this.status = Status.FAILED;
            int n3 = 1;
            this.isCallingCallbacks = true;
            n2 = 0;
            n = 0;
            try {
                object2 = this.requestListeners;
                if (object2 != null) {
                    object2 = object2.iterator();
                    while (true) {
                        n2 = n;
                        if (!object2.hasNext()) break;
                        n |= ((RequestListener)object2.next()).onLoadFailed(glideException, this.model, this.target, this.isFirstReadyResource());
                    }
                }
                if (((n = (object2 = this.targetListener) != null && object2.onLoadFailed(glideException, this.model, this.target, this.isFirstReadyResource()) ? n3 : 0) | n2) == 0) {
                    this.setErrorPlaceholder();
                }
            }
            finally {
                this.isCallingCallbacks = false;
            }
            this.notifyLoadFailed();
            GlideTrace.endSectionAsync(TAG, this.cookie);
            return;
        }
    }

    private void onResourceReady(Resource<R> object, R r, DataSource dataSource, boolean bl) {
        boolean bl2;
        boolean bl3;
        block14: {
            block13: {
                boolean bl4;
                block12: {
                    block11: {
                        bl = this.isFirstReadyResource();
                        this.status = Status.COMPLETE;
                        this.resource = object;
                        if (this.glideContext.getLogLevel() <= 3) {
                            object = new StringBuilder();
                            ((StringBuilder)object).append("Finished loading ");
                            ((StringBuilder)object).append(r.getClass().getSimpleName());
                            ((StringBuilder)object).append(" from ");
                            ((StringBuilder)object).append((Object)dataSource);
                            ((StringBuilder)object).append(" for ");
                            ((StringBuilder)object).append(this.model);
                            ((StringBuilder)object).append(" with size [");
                            ((StringBuilder)object).append(this.width);
                            ((StringBuilder)object).append("x");
                            ((StringBuilder)object).append(this.height);
                            ((StringBuilder)object).append("] in ");
                            ((StringBuilder)object).append(LogTime.getElapsedMillis(this.startTime));
                            ((StringBuilder)object).append(" ms");
                            Log.d((String)GLIDE_TAG, (String)((StringBuilder)object).toString());
                        }
                        bl4 = true;
                        this.isCallingCallbacks = true;
                        object = this.requestListeners;
                        if (object == null) break block11;
                        object = object.iterator();
                        bl3 = false;
                        while (true) {
                            bl2 = bl3;
                            if (object.hasNext()) {
                                bl3 |= ((RequestListener)object.next()).onResourceReady(r, this.model, this.target, dataSource, bl);
                                continue;
                            }
                            break block12;
                            break;
                        }
                    }
                    bl2 = false;
                }
                try {
                    object = this.targetListener;
                    if (object == null) break block13;
                }
                catch (Throwable throwable) {
                    this.isCallingCallbacks = false;
                    throw throwable;
                }
                if (!object.onResourceReady(r, this.model, this.target, dataSource, bl)) break block13;
                bl3 = bl4;
                break block14;
            }
            bl3 = false;
        }
        if (!(bl2 | bl3)) {
            object = this.animationFactory.build(dataSource, bl);
            this.target.onResourceReady(r, (Transition<R>)object);
        }
        this.isCallingCallbacks = false;
        this.notifyLoadSuccess();
        GlideTrace.endSectionAsync(TAG, this.cookie);
    }

    private void setErrorPlaceholder() {
        if (!this.canNotifyStatusChanged()) {
            return;
        }
        Drawable drawable2 = null;
        if (this.model == null) {
            drawable2 = this.getFallbackDrawable();
        }
        Drawable drawable3 = drawable2;
        if (drawable2 == null) {
            drawable3 = this.getErrorDrawable();
        }
        drawable2 = drawable3;
        if (drawable3 == null) {
            drawable2 = this.getPlaceholderDrawable();
        }
        this.target.onLoadFailed(drawable2);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void begin() {
        Object object = this.requestLock;
        synchronized (object) {
            this.assertNotCallingCallbacks();
            this.stateVerifier.throwIfRecycled();
            this.startTime = LogTime.getLogTime();
            if (this.model == null) {
                if (Util.isValidDimensions(this.overrideWidth, this.overrideHeight)) {
                    this.width = this.overrideWidth;
                    this.height = this.overrideHeight;
                }
                int n = this.getFallbackDrawable() == null ? 5 : 3;
                GlideException glideException = new GlideException("Received null model");
                this.onLoadFailed(glideException, n);
                return;
            }
            if (this.status == Status.RUNNING) {
                IllegalArgumentException illegalArgumentException = new IllegalArgumentException("Cannot restart a running request");
                throw illegalArgumentException;
            }
            if (this.status == Status.COMPLETE) {
                this.onResourceReady(this.resource, DataSource.MEMORY_CACHE, false);
                return;
            }
            this.experimentalNotifyRequestStarted(this.model);
            this.cookie = GlideTrace.beginSectionAsync(TAG);
            this.status = Status.WAITING_FOR_SIZE;
            if (Util.isValidDimensions(this.overrideWidth, this.overrideHeight)) {
                this.onSizeReady(this.overrideWidth, this.overrideHeight);
            } else {
                this.target.getSize(this);
            }
            if ((this.status == Status.RUNNING || this.status == Status.WAITING_FOR_SIZE) && this.canNotifyStatusChanged()) {
                this.target.onLoadStarted(this.getPlaceholderDrawable());
            }
            if (IS_VERBOSE_LOGGABLE) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("finished run method in ");
                stringBuilder.append(LogTime.getElapsedMillis(this.startTime));
                this.logV(stringBuilder.toString());
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    @Override
    public void clear() {
        Resource<R> resource = null;
        Object object = this.requestLock;
        // MONITORENTER : object
        this.assertNotCallingCallbacks();
        this.stateVerifier.throwIfRecycled();
        if (this.status == Status.CLEARED) {
            // MONITOREXIT : object
            return;
        }
        this.cancel();
        Resource<R> resource2 = this.resource;
        if (resource2 != null) {
            resource = resource2;
            this.resource = null;
        }
        if (this.canNotifyCleared()) {
            this.target.onLoadCleared(this.getPlaceholderDrawable());
        }
        GlideTrace.endSectionAsync(TAG, this.cookie);
        this.status = Status.CLEARED;
        // MONITOREXIT : object
        if (resource == null) return;
        this.engine.release(resource);
    }

    @Override
    public Object getLock() {
        this.stateVerifier.throwIfRecycled();
        return this.requestLock;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public boolean isAnyResourceSet() {
        Object object = this.requestLock;
        synchronized (object) {
            if (this.status != Status.COMPLETE) return false;
            return true;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public boolean isCleared() {
        Object object = this.requestLock;
        synchronized (object) {
            if (this.status != Status.CLEARED) return false;
            return true;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public boolean isComplete() {
        Object object = this.requestLock;
        synchronized (object) {
            if (this.status != Status.COMPLETE) return false;
            return true;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public boolean isEquivalentTo(Request object) {
        int n;
        BaseRequestOptions<?> baseRequestOptions;
        Class<R> clazz;
        int n2;
        int n3;
        int n4;
        Object object2;
        Priority priority;
        BaseRequestOptions<?> baseRequestOptions2;
        Class<R> clazz2;
        Object object3;
        int n5;
        int n6;
        if (!(object instanceof SingleRequest)) {
            return false;
        }
        Object object4 = this.requestLock;
        synchronized (object4) {
            n6 = this.overrideWidth;
            n5 = this.overrideHeight;
            object3 = this.model;
            clazz2 = this.transcodeClass;
            baseRequestOptions2 = this.requestOptions;
            priority = this.priority;
            object2 = this.requestListeners;
            n4 = object2 != null ? object2.size() : 0;
        }
        Object object5 = (SingleRequest)object;
        object = ((SingleRequest)object5).requestLock;
        synchronized (object) {
            n3 = ((SingleRequest)object5).overrideWidth;
            n2 = ((SingleRequest)object5).overrideHeight;
            object2 = ((SingleRequest)object5).model;
            clazz = ((SingleRequest)object5).transcodeClass;
            baseRequestOptions = ((SingleRequest)object5).requestOptions;
            object4 = ((SingleRequest)object5).priority;
            object5 = ((SingleRequest)object5).requestListeners;
            n = object5 != null ? object5.size() : 0;
        }
        if (n6 != n3) return false;
        if (n5 != n2) return false;
        if (!Util.bothModelsNullEquivalentOrEquals(object3, object2)) return false;
        if (!clazz2.equals(clazz)) return false;
        if (!baseRequestOptions2.equals(baseRequestOptions)) return false;
        if (priority != object4) return false;
        if (n4 != n) return false;
        return true;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public boolean isRunning() {
        Object object = this.requestLock;
        synchronized (object) {
            if (this.status == Status.RUNNING) return true;
            if (this.status != Status.WAITING_FOR_SIZE) return false;
            return true;
        }
    }

    @Override
    public void onLoadFailed(GlideException glideException) {
        this.onLoadFailed(glideException, 5);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    @Override
    public void onResourceReady(Resource<?> object, DataSource object2, boolean bl) {
        Object object3;
        Object object4;
        Object object5;
        block14: {
            this.stateVerifier.throwIfRecycled();
            object5 = null;
            object4 = null;
            object3 = object5;
            try {
                Object object6 = this.requestLock;
                object3 = object5;
                // MONITORENTER : object6
                object3 = object4;
                this.loadStatus = null;
                if (object != null) break block14;
                object3 = object4;
                object3 = object4;
                object3 = object4;
                object = new StringBuilder();
                object3 = object4;
                ((StringBuilder)object).append("Expected to receive a Resource<R> with an object of ");
            }
            catch (Throwable throwable) {
                if (object3 == null) throw throwable;
                this.engine.release((Resource<?>)object3);
                throw throwable;
            }
            object3 = object4;
            ((StringBuilder)object).append(this.transcodeClass);
            object3 = object4;
            ((StringBuilder)object).append(" inside, but instead got null.");
            object3 = object4;
            object2 = new GlideException(((StringBuilder)object).toString());
            object3 = object4;
            this.onLoadFailed((GlideException)object2);
            object3 = object4;
            // MONITOREXIT : object6
            return;
        }
        object3 = object4;
        Object z = object.get();
        if (z != null) {
            object3 = object4;
            if (this.transcodeClass.isAssignableFrom(z.getClass())) {
                object3 = object4;
                if (!this.canSetResource()) {
                    object3 = object;
                    this.resource = null;
                    object3 = object;
                    this.status = Status.COMPLETE;
                    object3 = object;
                    GlideTrace.endSectionAsync(TAG, this.cookie);
                    object3 = object;
                    // MONITOREXIT : object6
                    if (object == null) return;
                    this.engine.release((Resource<?>)object);
                    return;
                }
                object3 = object4;
                this.onResourceReady((Resource<R>)object, (R)z, (DataSource)((Object)object2), bl);
                object3 = object4;
                // MONITOREXIT : object6
                return;
            }
        }
        object3 = object2 = object;
        this.resource = null;
        object3 = object2;
        object3 = object2;
        object3 = object2;
        StringBuilder stringBuilder = new StringBuilder();
        object3 = object2;
        stringBuilder.append("Expected to receive an object of ");
        object3 = object2;
        stringBuilder.append(this.transcodeClass);
        object3 = object2;
        stringBuilder.append(" but instead got ");
        if (z != null) {
            object3 = object2;
            object4 = z.getClass();
        } else {
            object4 = "";
        }
        object3 = object2;
        stringBuilder.append(object4);
        object3 = object2;
        stringBuilder.append("{");
        object3 = object2;
        stringBuilder.append(z);
        object3 = object2;
        stringBuilder.append("} inside Resource{");
        object3 = object2;
        stringBuilder.append(object);
        object3 = object2;
        stringBuilder.append("}.");
        object = z != null ? "" : " To indicate failure return a null Resource object, rather than a Resource object containing null data.";
        object3 = object2;
        stringBuilder.append((String)object);
        object3 = object2;
        object5 = new GlideException(stringBuilder.toString());
        object3 = object2;
        this.onLoadFailed((GlideException)object5);
        object3 = object2;
        // MONITOREXIT : object6
        if (object2 == null) return;
        this.engine.release((Resource<?>)object2);
    }

    /*
     * Loose catch block
     * WARNING - void declaration
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void onSizeReady(int n, int n2) {
        this.stateVerifier.throwIfRecycled();
        Object object = this.requestLock;
        synchronized (object) {
            void var12_8;
            Object object2;
            block12: {
                Object object3;
                boolean bl = IS_VERBOSE_LOGGABLE;
                if (bl) {
                    object3 = new StringBuilder();
                    ((StringBuilder)object3).append("Got onSizeReady in ");
                    ((StringBuilder)object3).append(LogTime.getElapsedMillis(this.startTime));
                    this.logV(((StringBuilder)object3).toString());
                }
                if (this.status != Status.WAITING_FOR_SIZE) {
                    return;
                }
                this.status = Status.RUNNING;
                float f = this.requestOptions.getSizeMultiplier();
                this.width = SingleRequest.maybeApplySizeMultiplier(n, f);
                this.height = SingleRequest.maybeApplySizeMultiplier(n2, f);
                if (bl) {
                    object3 = new StringBuilder();
                    ((StringBuilder)object3).append("finished setup for calling load in ");
                    ((StringBuilder)object3).append(LogTime.getElapsedMillis(this.startTime));
                    this.logV(((StringBuilder)object3).toString());
                }
                Engine engine = this.engine;
                GlideContext glideContext = this.glideContext;
                object3 = this.model;
                Key key = this.requestOptions.getSignature();
                n = this.width;
                n2 = this.height;
                Class<?> clazz = this.requestOptions.getResourceClass();
                Class<R> clazz2 = this.transcodeClass;
                Priority priority = this.priority;
                DiskCacheStrategy diskCacheStrategy = this.requestOptions.getDiskCacheStrategy();
                Map<Class<?>, Transformation<?>> map = this.requestOptions.getTransformations();
                boolean bl2 = this.requestOptions.isTransformationRequired();
                boolean bl3 = this.requestOptions.isScaleOnlyOrNoTransform();
                object2 = this.requestOptions.getOptions();
                boolean bl4 = this.requestOptions.isMemoryCacheable();
                boolean bl5 = this.requestOptions.getUseUnlimitedSourceGeneratorsPool();
                boolean bl6 = this.requestOptions.getUseAnimationPool();
                boolean bl7 = this.requestOptions.getOnlyRetrieveFromCache();
                Executor executor = this.callbackExecutor;
                object3 = engine.load(glideContext, object3, key, n, n2, clazz, clazz2, priority, diskCacheStrategy, map, bl2, bl3, (Options)object2, bl4, bl5, bl6, bl7, this, executor);
                object2 = object;
                {
                    catch (Throwable throwable) {}
                }
                try {
                    this.loadStatus = object3;
                    object2 = object;
                    if (this.status != Status.RUNNING) {
                        object2 = object;
                        this.loadStatus = null;
                    }
                    if (bl) {
                        object2 = object;
                        object2 = object;
                        object3 = new StringBuilder();
                        object2 = object;
                        ((StringBuilder)object3).append("finished onSizeReady in ");
                        object2 = object;
                        ((StringBuilder)object3).append(LogTime.getElapsedMillis(this.startTime));
                        object2 = object;
                        this.logV(((StringBuilder)object3).toString());
                    }
                    object2 = object;
                    return;
                    break block12;
                    catch (Throwable throwable) {
                        // empty catch block
                    }
                }
                catch (Throwable throwable) {
                    object = object2;
                }
            }
            object2 = object;
            throw var12_8;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void pause() {
        Object object = this.requestLock;
        synchronized (object) {
            if (this.isRunning()) {
                this.clear();
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public String toString() {
        Class<R> clazz;
        Object object;
        Object object2 = this.requestLock;
        synchronized (object2) {
            object = this.model;
            clazz = this.transcodeClass;
        }
        object2 = new StringBuilder();
        ((StringBuilder)object2).append(super.toString());
        ((StringBuilder)object2).append("[model=");
        ((StringBuilder)object2).append(object);
        ((StringBuilder)object2).append(", transcodeClass=");
        ((StringBuilder)object2).append(clazz);
        ((StringBuilder)object2).append("]");
        return ((StringBuilder)object2).toString();
    }

    private static final class Status
    extends Enum<Status> {
        private static final Status[] $VALUES;
        public static final /* enum */ Status CLEARED;
        public static final /* enum */ Status COMPLETE;
        public static final /* enum */ Status FAILED;
        public static final /* enum */ Status PENDING;
        public static final /* enum */ Status RUNNING;
        public static final /* enum */ Status WAITING_FOR_SIZE;

        static {
            Status status;
            Status status2;
            Status status3;
            Status status4;
            Status status5;
            Status status6;
            PENDING = status6 = new Status();
            RUNNING = status5 = new Status();
            WAITING_FOR_SIZE = status4 = new Status();
            COMPLETE = status3 = new Status();
            FAILED = status2 = new Status();
            CLEARED = status = new Status();
            $VALUES = new Status[]{status6, status5, status4, status3, status2, status};
        }

        public static Status valueOf(String string2) {
            return Enum.valueOf(Status.class, string2);
        }

        public static Status[] values() {
            return (Status[])$VALUES.clone();
        }
    }
}

