/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Bitmap
 *  android.graphics.drawable.Drawable
 *  android.net.Uri
 *  android.widget.ImageView
 */
package com.bumptech.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideContext;
import com.bumptech.glide.ModelTypes;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.ErrorRequestCoordinator;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestCoordinator;
import com.bumptech.glide.request.RequestFutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.SingleRequest;
import com.bumptech.glide.request.ThumbnailRequestCoordinator;
import com.bumptech.glide.request.target.PreloadTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.signature.AndroidResourceSignature;
import com.bumptech.glide.util.Executors;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;

public class RequestBuilder<TranscodeType>
extends BaseRequestOptions<RequestBuilder<TranscodeType>>
implements Cloneable,
ModelTypes<RequestBuilder<TranscodeType>> {
    protected static final RequestOptions DOWNLOAD_ONLY_OPTIONS = (RequestOptions)((RequestOptions)((RequestOptions)new RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA)).priority(Priority.LOW)).skipMemoryCache(true);
    private final Context context;
    private RequestBuilder<TranscodeType> errorBuilder;
    private final Glide glide;
    private final GlideContext glideContext;
    private boolean isDefaultTransitionOptionsSet = true;
    private boolean isModelSet;
    private boolean isThumbnailBuilt;
    private Object model;
    private List<RequestListener<TranscodeType>> requestListeners;
    private final RequestManager requestManager;
    private Float thumbSizeMultiplier;
    private RequestBuilder<TranscodeType> thumbnailBuilder;
    private final Class<TranscodeType> transcodeClass;
    private TransitionOptions<?, ? super TranscodeType> transitionOptions;

    protected RequestBuilder(Glide glide, RequestManager requestManager, Class<TranscodeType> clazz, Context context) {
        this.glide = glide;
        this.requestManager = requestManager;
        this.transcodeClass = clazz;
        this.context = context;
        this.transitionOptions = requestManager.getDefaultTransitionOptions(clazz);
        this.glideContext = glide.getGlideContext();
        this.initRequestListeners(requestManager.getDefaultRequestListeners());
        this.apply((BaseRequestOptions)requestManager.getDefaultRequestOptions());
    }

    protected RequestBuilder(Class<TranscodeType> clazz, RequestBuilder<?> requestBuilder) {
        this(requestBuilder.glide, requestBuilder.requestManager, clazz, requestBuilder.context);
        this.model = requestBuilder.model;
        this.isModelSet = requestBuilder.isModelSet;
        this.apply(requestBuilder);
    }

    private Request buildRequest(Target<TranscodeType> target, RequestListener<TranscodeType> requestListener, BaseRequestOptions<?> baseRequestOptions, Executor executor) {
        return this.buildRequestRecursive(new Object(), target, requestListener, null, this.transitionOptions, baseRequestOptions.getPriority(), baseRequestOptions.getOverrideWidth(), baseRequestOptions.getOverrideHeight(), baseRequestOptions, executor);
    }

    private Request buildRequestRecursive(Object object, Target<TranscodeType> target, RequestListener<TranscodeType> requestListener, RequestCoordinator requestCoordinator, TransitionOptions<?, ? super TranscodeType> object2, Priority object3, int n, int n2, BaseRequestOptions<?> baseRequestOptions, Executor executor) {
        RequestCoordinator requestCoordinator2;
        if (this.errorBuilder != null) {
            requestCoordinator = requestCoordinator2 = new ErrorRequestCoordinator(object, requestCoordinator);
        } else {
            Object var16_12 = null;
            requestCoordinator2 = requestCoordinator;
            requestCoordinator = var16_12;
        }
        object2 = this.buildThumbnailRequestRecursive(object, target, requestListener, requestCoordinator2, (TransitionOptions<?, ? super TranscodeType>)object2, (Priority)((Object)object3), n, n2, baseRequestOptions, executor);
        if (requestCoordinator == null) {
            return object2;
        }
        int n3 = this.errorBuilder.getOverrideWidth();
        int n4 = this.errorBuilder.getOverrideHeight();
        int n5 = n3;
        int n6 = n4;
        if (Util.isValidDimensions(n, n2)) {
            n5 = n3;
            n6 = n4;
            if (!this.errorBuilder.isValidOverride()) {
                n5 = baseRequestOptions.getOverrideWidth();
                n6 = baseRequestOptions.getOverrideHeight();
            }
        }
        object3 = this.errorBuilder;
        ((ErrorRequestCoordinator)requestCoordinator).setRequests((Request)object2, super.buildRequestRecursive(object, target, requestListener, requestCoordinator, ((RequestBuilder)object3).transitionOptions, ((BaseRequestOptions)object3).getPriority(), n5, n6, this.errorBuilder, executor));
        return requestCoordinator;
    }

    private Request buildThumbnailRequestRecursive(Object object, Target<TranscodeType> target, RequestListener<TranscodeType> requestListener, RequestCoordinator object2, TransitionOptions<?, ? super TranscodeType> object3, Priority object4, int n, int n2, BaseRequestOptions<?> baseRequestOptions, Executor executor) {
        Object object5 = this.thumbnailBuilder;
        if (object5 != null) {
            if (!this.isThumbnailBuilt) {
                TransitionOptions<?, ? super TranscodeType> transitionOptions = object5.transitionOptions;
                if (object5.isDefaultTransitionOptionsSet) {
                    transitionOptions = object3;
                }
                object5 = object5.isPrioritySet() ? this.thumbnailBuilder.getPriority() : this.getThumbnailPriority((Priority)((Object)object4));
                int n3 = this.thumbnailBuilder.getOverrideWidth();
                int n4 = this.thumbnailBuilder.getOverrideHeight();
                if (Util.isValidDimensions(n, n2) && !this.thumbnailBuilder.isValidOverride()) {
                    n3 = baseRequestOptions.getOverrideWidth();
                    n4 = baseRequestOptions.getOverrideHeight();
                }
                object2 = new ThumbnailRequestCoordinator(object, (RequestCoordinator)object2);
                object3 = this.obtainRequest(object, target, requestListener, baseRequestOptions, (RequestCoordinator)object2, (TransitionOptions<?, ? super TranscodeType>)object3, (Priority)((Object)object4), n, n2, executor);
                this.isThumbnailBuilt = true;
                object4 = this.thumbnailBuilder;
                object = super.buildRequestRecursive(object, target, requestListener, (RequestCoordinator)object2, transitionOptions, (Priority)((Object)object5), n3, n4, (BaseRequestOptions<?>)object4, executor);
                this.isThumbnailBuilt = false;
                ((ThumbnailRequestCoordinator)object2).setRequests((Request)object3, (Request)object);
                return object2;
            }
            throw new IllegalStateException("You cannot use a request as both the main request and a thumbnail, consider using clone() on the request(s) passed to thumbnail()");
        }
        if (this.thumbSizeMultiplier != null) {
            ThumbnailRequestCoordinator thumbnailRequestCoordinator = new ThumbnailRequestCoordinator(object, (RequestCoordinator)object2);
            object2 = this.obtainRequest(object, target, requestListener, baseRequestOptions, thumbnailRequestCoordinator, (TransitionOptions<?, ? super TranscodeType>)object3, (Priority)((Object)object4), n, n2, executor);
            baseRequestOptions = ((BaseRequestOptions)baseRequestOptions.clone()).sizeMultiplier(this.thumbSizeMultiplier.floatValue());
            thumbnailRequestCoordinator.setRequests((Request)object2, this.obtainRequest(object, target, requestListener, baseRequestOptions, thumbnailRequestCoordinator, (TransitionOptions<?, ? super TranscodeType>)object3, this.getThumbnailPriority((Priority)((Object)object4)), n, n2, executor));
            return thumbnailRequestCoordinator;
        }
        return this.obtainRequest(object, target, requestListener, baseRequestOptions, (RequestCoordinator)object2, (TransitionOptions<?, ? super TranscodeType>)object3, (Priority)((Object)object4), n, n2, executor);
    }

    private RequestBuilder<TranscodeType> cloneWithNullErrorAndThumbnail() {
        BaseRequestOptions baseRequestOptions = this.clone();
        RequestBuilder requestBuilder = null;
        return ((RequestBuilder)baseRequestOptions).error(requestBuilder).thumbnail((RequestBuilder<TranscodeType>)requestBuilder);
    }

    private Priority getThumbnailPriority(Priority object) {
        switch (1.$SwitchMap$com$bumptech$glide$Priority[((Enum)object).ordinal()]) {
            default: {
                object = new StringBuilder();
                ((StringBuilder)object).append("unknown priority: ");
                ((StringBuilder)object).append((Object)this.getPriority());
                throw new IllegalArgumentException(((StringBuilder)object).toString());
            }
            case 3: 
            case 4: {
                return Priority.IMMEDIATE;
            }
            case 2: {
                return Priority.HIGH;
            }
            case 1: 
        }
        return Priority.NORMAL;
    }

    private void initRequestListeners(List<RequestListener<Object>> object) {
        object = object.iterator();
        while (object.hasNext()) {
            this.addListener((RequestListener)object.next());
        }
    }

    private <Y extends Target<TranscodeType>> Y into(Y y, RequestListener<TranscodeType> object, BaseRequestOptions<?> baseRequestOptions, Executor object2) {
        Preconditions.checkNotNull(y);
        if (this.isModelSet) {
            if ((object = this.buildRequest(y, (RequestListener<TranscodeType>)object, baseRequestOptions, (Executor)object2)).isEquivalentTo((Request)(object2 = y.getRequest())) && !this.isSkipMemoryCacheWithCompletePreviousRequest(baseRequestOptions, (Request)object2)) {
                if (!((Request)Preconditions.checkNotNull(object2)).isRunning()) {
                    object2.begin();
                }
                return y;
            }
            this.requestManager.clear(y);
            y.setRequest((Request)object);
            this.requestManager.track(y, (Request)object);
            return y;
        }
        throw new IllegalArgumentException("You must call #load() before calling #into()");
    }

    private boolean isSkipMemoryCacheWithCompletePreviousRequest(BaseRequestOptions<?> baseRequestOptions, Request request) {
        boolean bl = !baseRequestOptions.isMemoryCacheable() && request.isComplete();
        return bl;
    }

    private RequestBuilder<TranscodeType> loadGeneric(Object object) {
        if (this.isAutoCloneEnabled()) {
            return super.loadGeneric(object);
        }
        this.model = object;
        this.isModelSet = true;
        return (RequestBuilder)this.selfOrThrowIfLocked();
    }

    private Request obtainRequest(Object object, Target<TranscodeType> target, RequestListener<TranscodeType> requestListener, BaseRequestOptions<?> baseRequestOptions, RequestCoordinator requestCoordinator, TransitionOptions<?, ? super TranscodeType> transitionOptions, Priority priority, int n, int n2, Executor executor) {
        Context context = this.context;
        GlideContext glideContext = this.glideContext;
        return SingleRequest.obtain(context, glideContext, object, this.model, this.transcodeClass, baseRequestOptions, n, n2, priority, target, requestListener, this.requestListeners, requestCoordinator, glideContext.getEngine(), transitionOptions.getTransitionFactory(), executor);
    }

    public RequestBuilder<TranscodeType> addListener(RequestListener<TranscodeType> requestListener) {
        if (this.isAutoCloneEnabled()) {
            return ((RequestBuilder)this.clone()).addListener(requestListener);
        }
        if (requestListener != null) {
            if (this.requestListeners == null) {
                this.requestListeners = new ArrayList<RequestListener<TranscodeType>>();
            }
            this.requestListeners.add(requestListener);
        }
        return (RequestBuilder)this.selfOrThrowIfLocked();
    }

    @Override
    public RequestBuilder<TranscodeType> apply(BaseRequestOptions<?> baseRequestOptions) {
        Preconditions.checkNotNull(baseRequestOptions);
        return (RequestBuilder)super.apply(baseRequestOptions);
    }

    @Override
    public RequestBuilder<TranscodeType> clone() {
        RequestBuilder<TranscodeType> requestBuilder;
        RequestBuilder requestBuilder2 = (RequestBuilder)super.clone();
        requestBuilder2.transitionOptions = requestBuilder2.transitionOptions.clone();
        if (requestBuilder2.requestListeners != null) {
            requestBuilder2.requestListeners = new ArrayList<RequestListener<TranscodeType>>(requestBuilder2.requestListeners);
        }
        if ((requestBuilder = requestBuilder2.thumbnailBuilder) != null) {
            requestBuilder2.thumbnailBuilder = requestBuilder.clone();
        }
        if ((requestBuilder = requestBuilder2.errorBuilder) != null) {
            requestBuilder2.errorBuilder = requestBuilder.clone();
        }
        return requestBuilder2;
    }

    @Deprecated
    public FutureTarget<File> downloadOnly(int n, int n2) {
        return this.getDownloadOnlyRequest().submit(n, n2);
    }

    @Deprecated
    public <Y extends Target<File>> Y downloadOnly(Y y) {
        return this.getDownloadOnlyRequest().into(y);
    }

    public RequestBuilder<TranscodeType> error(RequestBuilder<TranscodeType> requestBuilder) {
        if (this.isAutoCloneEnabled()) {
            return ((RequestBuilder)this.clone()).error(requestBuilder);
        }
        this.errorBuilder = requestBuilder;
        return (RequestBuilder)this.selfOrThrowIfLocked();
    }

    public RequestBuilder<TranscodeType> error(Object object) {
        if (object == null) {
            return this.error((RequestBuilder)null);
        }
        return this.error((RequestBuilder<TranscodeType>)this.cloneWithNullErrorAndThumbnail().load(object));
    }

    protected RequestBuilder<File> getDownloadOnlyRequest() {
        return new RequestBuilder<File>(File.class, this).apply((BaseRequestOptions)DOWNLOAD_ONLY_OPTIONS);
    }

    @Deprecated
    public FutureTarget<TranscodeType> into(int n, int n2) {
        return this.submit(n, n2);
    }

    public <Y extends Target<TranscodeType>> Y into(Y y) {
        return this.into(y, null, Executors.mainThreadExecutor());
    }

    <Y extends Target<TranscodeType>> Y into(Y y, RequestListener<TranscodeType> requestListener, Executor executor) {
        return this.into(y, requestListener, this, executor);
    }

    public ViewTarget<ImageView, TranscodeType> into(ImageView imageView) {
        RequestBuilder requestBuilder;
        Util.assertMainThread();
        Preconditions.checkNotNull(imageView);
        RequestBuilder<Object> requestBuilder2 = requestBuilder = this;
        if (!requestBuilder.isTransformationSet()) {
            requestBuilder2 = requestBuilder;
            if (requestBuilder.isTransformationAllowed()) {
                requestBuilder2 = requestBuilder;
                if (imageView.getScaleType() != null) {
                    switch (1.$SwitchMap$android$widget$ImageView$ScaleType[imageView.getScaleType().ordinal()]) {
                        default: {
                            requestBuilder2 = requestBuilder;
                            break;
                        }
                        case 6: {
                            requestBuilder2 = ((BaseRequestOptions)((BaseRequestOptions)requestBuilder).clone()).optionalCenterInside();
                            break;
                        }
                        case 3: 
                        case 4: 
                        case 5: {
                            requestBuilder2 = ((BaseRequestOptions)((BaseRequestOptions)requestBuilder).clone()).optionalFitCenter();
                            break;
                        }
                        case 2: {
                            requestBuilder2 = ((BaseRequestOptions)((BaseRequestOptions)requestBuilder).clone()).optionalCenterInside();
                            break;
                        }
                        case 1: {
                            requestBuilder2 = ((BaseRequestOptions)((BaseRequestOptions)requestBuilder).clone()).optionalCenterCrop();
                        }
                    }
                }
            }
        }
        return this.into(this.glideContext.buildImageViewTarget(imageView, this.transcodeClass), null, requestBuilder2, Executors.mainThreadExecutor());
    }

    public RequestBuilder<TranscodeType> listener(RequestListener<TranscodeType> requestListener) {
        if (this.isAutoCloneEnabled()) {
            return ((RequestBuilder)this.clone()).listener(requestListener);
        }
        this.requestListeners = null;
        return this.addListener(requestListener);
    }

    @Override
    public RequestBuilder<TranscodeType> load(Bitmap bitmap) {
        return this.loadGeneric(bitmap).apply((BaseRequestOptions)RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE));
    }

    @Override
    public RequestBuilder<TranscodeType> load(Drawable drawable2) {
        return this.loadGeneric(drawable2).apply((BaseRequestOptions)RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE));
    }

    @Override
    public RequestBuilder<TranscodeType> load(Uri uri) {
        return this.loadGeneric(uri);
    }

    @Override
    public RequestBuilder<TranscodeType> load(File file) {
        return this.loadGeneric(file);
    }

    @Override
    public RequestBuilder<TranscodeType> load(Integer n) {
        return this.loadGeneric(n).apply((BaseRequestOptions)RequestOptions.signatureOf(AndroidResourceSignature.obtain(this.context)));
    }

    @Override
    public RequestBuilder<TranscodeType> load(Object object) {
        return this.loadGeneric(object);
    }

    @Override
    public RequestBuilder<TranscodeType> load(String string2) {
        return this.loadGeneric(string2);
    }

    @Override
    @Deprecated
    public RequestBuilder<TranscodeType> load(URL uRL) {
        return this.loadGeneric(uRL);
    }

    @Override
    public RequestBuilder<TranscodeType> load(byte[] object) {
        Object object2 = this.loadGeneric(object);
        object = object2;
        if (!((BaseRequestOptions)object2).isDiskCacheStrategySet()) {
            object = ((RequestBuilder)object2).apply((BaseRequestOptions)RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE));
        }
        object2 = object;
        if (!((BaseRequestOptions)object).isSkipMemoryCacheSet()) {
            object2 = ((RequestBuilder)object).apply((BaseRequestOptions)RequestOptions.skipMemoryCacheOf(true));
        }
        return object2;
    }

    public Target<TranscodeType> preload() {
        return this.preload(Integer.MIN_VALUE, Integer.MIN_VALUE);
    }

    public Target<TranscodeType> preload(int n, int n2) {
        return this.into(PreloadTarget.obtain(this.requestManager, n, n2));
    }

    public FutureTarget<TranscodeType> submit() {
        return this.submit(Integer.MIN_VALUE, Integer.MIN_VALUE);
    }

    public FutureTarget<TranscodeType> submit(int n, int n2) {
        RequestFutureTarget requestFutureTarget = new RequestFutureTarget(n, n2);
        return this.into(requestFutureTarget, requestFutureTarget, Executors.directExecutor());
    }

    @Deprecated
    public RequestBuilder<TranscodeType> thumbnail(float f) {
        if (this.isAutoCloneEnabled()) {
            return ((RequestBuilder)this.clone()).thumbnail(f);
        }
        if (!(f < 0.0f) && !(f > 1.0f)) {
            this.thumbSizeMultiplier = Float.valueOf(f);
            return (RequestBuilder)this.selfOrThrowIfLocked();
        }
        throw new IllegalArgumentException("sizeMultiplier must be between 0 and 1");
    }

    public RequestBuilder<TranscodeType> thumbnail(RequestBuilder<TranscodeType> requestBuilder) {
        if (this.isAutoCloneEnabled()) {
            return ((RequestBuilder)this.clone()).thumbnail(requestBuilder);
        }
        this.thumbnailBuilder = requestBuilder;
        return (RequestBuilder)this.selfOrThrowIfLocked();
    }

    public RequestBuilder<TranscodeType> thumbnail(List<RequestBuilder<TranscodeType>> list) {
        if (list != null && !list.isEmpty()) {
            RequestBuilder<TranscodeType> requestBuilder = null;
            for (int i = list.size() - 1; i >= 0; --i) {
                RequestBuilder<TranscodeType> requestBuilder2 = list.get(i);
                if (requestBuilder2 == null) continue;
                requestBuilder = requestBuilder == null ? requestBuilder2 : requestBuilder2.thumbnail(requestBuilder);
            }
            return this.thumbnail(requestBuilder);
        }
        return this.thumbnail((RequestBuilder<TranscodeType>)null);
    }

    public RequestBuilder<TranscodeType> thumbnail(RequestBuilder<TranscodeType> ... requestBuilderArray) {
        if (requestBuilderArray != null && requestBuilderArray.length != 0) {
            return this.thumbnail(Arrays.asList(requestBuilderArray));
        }
        return this.thumbnail((RequestBuilder<TranscodeType>)null);
    }

    public RequestBuilder<TranscodeType> transition(TransitionOptions<?, ? super TranscodeType> transitionOptions) {
        if (this.isAutoCloneEnabled()) {
            return ((RequestBuilder)this.clone()).transition(transitionOptions);
        }
        this.transitionOptions = Preconditions.checkNotNull(transitionOptions);
        this.isDefaultTransitionOptionsSet = false;
        return (RequestBuilder)this.selfOrThrowIfLocked();
    }
}

