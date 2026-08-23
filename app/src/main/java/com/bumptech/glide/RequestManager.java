/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ComponentCallbacks2
 *  android.content.Context
 *  android.content.res.Configuration
 *  android.graphics.Bitmap
 *  android.graphics.drawable.Drawable
 *  android.net.Uri
 *  android.view.View
 */
package com.bumptech.glide;

import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import com.bumptech.glide.Glide;
import com.bumptech.glide.ModelTypes;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.manager.ConnectivityMonitor;
import com.bumptech.glide.manager.ConnectivityMonitorFactory;
import com.bumptech.glide.manager.Lifecycle;
import com.bumptech.glide.manager.LifecycleListener;
import com.bumptech.glide.manager.RequestManagerTreeNode;
import com.bumptech.glide.manager.RequestTracker;
import com.bumptech.glide.manager.TargetTracker;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.util.Util;
import java.io.File;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class RequestManager
implements ComponentCallbacks2,
LifecycleListener,
ModelTypes<RequestBuilder<Drawable>> {
    private static final RequestOptions DECODE_TYPE_BITMAP = (RequestOptions)RequestOptions.decodeTypeOf(Bitmap.class).lock();
    private static final RequestOptions DECODE_TYPE_GIF = (RequestOptions)RequestOptions.decodeTypeOf(GifDrawable.class).lock();
    private static final RequestOptions DOWNLOAD_ONLY_OPTIONS = (RequestOptions)((RequestOptions)RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.DATA).priority(Priority.LOW)).skipMemoryCache(true);
    private final Runnable addSelfToLifecycle;
    private final ConnectivityMonitor connectivityMonitor;
    protected final Context context;
    private final CopyOnWriteArrayList<RequestListener<Object>> defaultRequestListeners;
    protected final Glide glide;
    final Lifecycle lifecycle;
    private boolean pauseAllRequestsOnTrimMemoryModerate;
    private RequestOptions requestOptions;
    private final RequestTracker requestTracker;
    private final TargetTracker targetTracker = new TargetTracker();
    private final RequestManagerTreeNode treeNode;

    public RequestManager(Glide glide, Lifecycle lifecycle, RequestManagerTreeNode requestManagerTreeNode, Context context) {
        this(glide, lifecycle, requestManagerTreeNode, new RequestTracker(), glide.getConnectivityMonitorFactory(), context);
    }

    RequestManager(Glide glide, Lifecycle lifecycle, RequestManagerTreeNode object, RequestTracker requestTracker, ConnectivityMonitorFactory connectivityMonitorFactory, Context context) {
        Runnable runnable;
        this.addSelfToLifecycle = runnable = new Runnable(this){
            final RequestManager this$0;
            {
                this.this$0 = requestManager;
            }

            @Override
            public void run() {
                this.this$0.lifecycle.addListener(this.this$0);
            }
        };
        this.glide = glide;
        this.lifecycle = lifecycle;
        this.treeNode = object;
        this.requestTracker = requestTracker;
        this.context = context;
        this.connectivityMonitor = object = connectivityMonitorFactory.build(context.getApplicationContext(), new RequestManagerConnectivityListener(this, requestTracker));
        if (Util.isOnBackgroundThread()) {
            Util.postOnUiThread(runnable);
        } else {
            lifecycle.addListener(this);
        }
        lifecycle.addListener((LifecycleListener)object);
        this.defaultRequestListeners = new CopyOnWriteArrayList<RequestListener<Object>>(glide.getGlideContext().getDefaultRequestListeners());
        this.setRequestOptions(glide.getGlideContext().getDefaultRequestOptions());
        glide.registerRequestManager(this);
    }

    private void untrackOrDelegate(Target<?> target) {
        boolean bl = this.untrack(target);
        Request request = target.getRequest();
        if (!bl && !this.glide.removeFromManagers(target) && request != null) {
            target.setRequest(null);
            request.clear();
        }
    }

    private void updateRequestOptions(RequestOptions requestOptions) {
        synchronized (this) {
            this.requestOptions = (RequestOptions)this.requestOptions.apply(requestOptions);
            return;
        }
    }

    public RequestManager addDefaultRequestListener(RequestListener<Object> requestListener) {
        this.defaultRequestListeners.add(requestListener);
        return this;
    }

    public RequestManager applyDefaultRequestOptions(RequestOptions requestOptions) {
        synchronized (this) {
            this.updateRequestOptions(requestOptions);
            return this;
        }
    }

    public <ResourceType> RequestBuilder<ResourceType> as(Class<ResourceType> clazz) {
        return new RequestBuilder<ResourceType>(this.glide, this, clazz, this.context);
    }

    public RequestBuilder<Bitmap> asBitmap() {
        return this.as(Bitmap.class).apply((BaseRequestOptions)DECODE_TYPE_BITMAP);
    }

    public RequestBuilder<Drawable> asDrawable() {
        return this.as(Drawable.class);
    }

    public RequestBuilder<File> asFile() {
        return this.as(File.class).apply((BaseRequestOptions)RequestOptions.skipMemoryCacheOf(true));
    }

    public RequestBuilder<GifDrawable> asGif() {
        return this.as(GifDrawable.class).apply((BaseRequestOptions)DECODE_TYPE_GIF);
    }

    public void clear(View view) {
        this.clear(new ClearTarget(view));
    }

    public void clear(Target<?> target) {
        if (target == null) {
            return;
        }
        this.untrackOrDelegate(target);
    }

    public RequestBuilder<File> download(Object object) {
        return this.downloadOnly().load(object);
    }

    public RequestBuilder<File> downloadOnly() {
        return this.as(File.class).apply((BaseRequestOptions)DOWNLOAD_ONLY_OPTIONS);
    }

    List<RequestListener<Object>> getDefaultRequestListeners() {
        return this.defaultRequestListeners;
    }

    RequestOptions getDefaultRequestOptions() {
        synchronized (this) {
            RequestOptions requestOptions = this.requestOptions;
            return requestOptions;
        }
    }

    <T> TransitionOptions<?, T> getDefaultTransitionOptions(Class<T> clazz) {
        return this.glide.getGlideContext().getDefaultTransitionOptions(clazz);
    }

    public boolean isPaused() {
        synchronized (this) {
            boolean bl = this.requestTracker.isPaused();
            return bl;
        }
    }

    @Override
    public RequestBuilder<Drawable> load(Bitmap bitmap) {
        return this.asDrawable().load(bitmap);
    }

    @Override
    public RequestBuilder<Drawable> load(Drawable drawable2) {
        return this.asDrawable().load(drawable2);
    }

    @Override
    public RequestBuilder<Drawable> load(Uri uri) {
        return this.asDrawable().load(uri);
    }

    @Override
    public RequestBuilder<Drawable> load(File file) {
        return this.asDrawable().load(file);
    }

    @Override
    public RequestBuilder<Drawable> load(Integer n) {
        return this.asDrawable().load(n);
    }

    @Override
    public RequestBuilder<Drawable> load(Object object) {
        return this.asDrawable().load(object);
    }

    @Override
    public RequestBuilder<Drawable> load(String string2) {
        return this.asDrawable().load(string2);
    }

    @Override
    @Deprecated
    public RequestBuilder<Drawable> load(URL uRL) {
        return this.asDrawable().load(uRL);
    }

    @Override
    public RequestBuilder<Drawable> load(byte[] byArray) {
        return this.asDrawable().load(byArray);
    }

    public void onConfigurationChanged(Configuration configuration) {
    }

    @Override
    public void onDestroy() {
        synchronized (this) {
            try {
                this.targetTracker.onDestroy();
                Iterator<Target<?>> iterator2 = this.targetTracker.getAll().iterator();
                while (iterator2.hasNext()) {
                    this.clear(iterator2.next());
                }
                this.targetTracker.clear();
                this.requestTracker.clearRequests();
                this.lifecycle.removeListener(this);
                this.lifecycle.removeListener(this.connectivityMonitor);
                Util.removeCallbacksOnUiThread(this.addSelfToLifecycle);
                this.glide.unregisterRequestManager(this);
                return;
            }
            catch (Throwable throwable) {
                throw throwable;
            }
        }
    }

    public void onLowMemory() {
    }

    @Override
    public void onStart() {
        synchronized (this) {
            this.resumeRequests();
            this.targetTracker.onStart();
            return;
        }
    }

    @Override
    public void onStop() {
        synchronized (this) {
            this.pauseRequests();
            this.targetTracker.onStop();
            return;
        }
    }

    public void onTrimMemory(int n) {
        if (n == 60 && this.pauseAllRequestsOnTrimMemoryModerate) {
            this.pauseAllRequestsRecursive();
        }
    }

    public void pauseAllRequests() {
        synchronized (this) {
            this.requestTracker.pauseAllRequests();
            return;
        }
    }

    public void pauseAllRequestsRecursive() {
        synchronized (this) {
            try {
                this.pauseAllRequests();
                Iterator<RequestManager> iterator2 = this.treeNode.getDescendants().iterator();
                while (iterator2.hasNext()) {
                    iterator2.next().pauseAllRequests();
                }
                return;
            }
            catch (Throwable throwable) {
                throw throwable;
            }
        }
    }

    public void pauseRequests() {
        synchronized (this) {
            this.requestTracker.pauseRequests();
            return;
        }
    }

    public void pauseRequestsRecursive() {
        synchronized (this) {
            try {
                this.pauseRequests();
                Iterator<RequestManager> iterator2 = this.treeNode.getDescendants().iterator();
                while (iterator2.hasNext()) {
                    iterator2.next().pauseRequests();
                }
                return;
            }
            catch (Throwable throwable) {
                throw throwable;
            }
        }
    }

    public void resumeRequests() {
        synchronized (this) {
            this.requestTracker.resumeRequests();
            return;
        }
    }

    public void resumeRequestsRecursive() {
        synchronized (this) {
            try {
                Util.assertMainThread();
                this.resumeRequests();
                Iterator<RequestManager> iterator2 = this.treeNode.getDescendants().iterator();
                while (iterator2.hasNext()) {
                    iterator2.next().resumeRequests();
                }
                return;
            }
            catch (Throwable throwable) {
                throw throwable;
            }
        }
    }

    public RequestManager setDefaultRequestOptions(RequestOptions requestOptions) {
        synchronized (this) {
            this.setRequestOptions(requestOptions);
            return this;
        }
    }

    public void setPauseAllRequestsOnTrimMemoryModerate(boolean bl) {
        this.pauseAllRequestsOnTrimMemoryModerate = bl;
    }

    protected void setRequestOptions(RequestOptions requestOptions) {
        synchronized (this) {
            this.requestOptions = (RequestOptions)((RequestOptions)requestOptions.clone()).autoClone();
            return;
        }
    }

    public String toString() {
        synchronized (this) {
            CharSequence charSequence = new StringBuilder();
            charSequence.append(super.toString());
            charSequence.append("{tracker=");
            charSequence.append(this.requestTracker);
            charSequence.append(", treeNode=");
            charSequence.append(this.treeNode);
            charSequence.append("}");
            charSequence = charSequence.toString();
            return charSequence;
        }
    }

    void track(Target<?> target, Request request) {
        synchronized (this) {
            this.targetTracker.track(target);
            this.requestTracker.runRequest(request);
            return;
        }
    }

    boolean untrack(Target<?> target) {
        synchronized (this) {
            Request request;
            block6: {
                request = target.getRequest();
                if (request != null) break block6;
                return true;
            }
            if (this.requestTracker.clearAndRemove(request)) {
                this.targetTracker.untrack(target);
                target.setRequest(null);
                return true;
            }
            return false;
        }
    }

    private static class ClearTarget
    extends CustomViewTarget<View, Object> {
        ClearTarget(View view) {
            super(view);
        }

        @Override
        public void onLoadFailed(Drawable drawable2) {
        }

        @Override
        protected void onResourceCleared(Drawable drawable2) {
        }

        @Override
        public void onResourceReady(Object object, Transition<? super Object> transition) {
        }
    }

    private class RequestManagerConnectivityListener
    implements ConnectivityMonitor.ConnectivityListener {
        private final RequestTracker requestTracker;
        final RequestManager this$0;

        RequestManagerConnectivityListener(RequestManager requestManager, RequestTracker requestTracker) {
            this.this$0 = requestManager;
            this.requestTracker = requestTracker;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void onConnectivityChanged(boolean bl) {
            if (!bl) return;
            RequestManager requestManager = this.this$0;
            synchronized (requestManager) {
                this.requestTracker.restartRequests();
                return;
            }
        }
    }
}

