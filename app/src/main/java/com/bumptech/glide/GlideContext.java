/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.ContextWrapper
 *  android.widget.ImageView
 */
package com.bumptech.glide;

import android.content.Context;
import android.content.ContextWrapper;
import android.widget.ImageView;
import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideExperiments;
import com.bumptech.glide.Registry;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.load.engine.Engine;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTargetFactory;
import com.bumptech.glide.request.target.ViewTarget;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GlideContext
extends ContextWrapper {
    static final TransitionOptions<?, ?> DEFAULT_TRANSITION_OPTIONS = new GenericTransitionOptions();
    private final ArrayPool arrayPool;
    private final List<RequestListener<Object>> defaultRequestListeners;
    private RequestOptions defaultRequestOptions;
    private final Glide.RequestOptionsFactory defaultRequestOptionsFactory;
    private final Map<Class<?>, TransitionOptions<?, ?>> defaultTransitionOptions;
    private final Engine engine;
    private final GlideExperiments experiments;
    private final ImageViewTargetFactory imageViewTargetFactory;
    private final int logLevel;
    private final Registry registry;

    public GlideContext(Context context, ArrayPool arrayPool, Registry registry, ImageViewTargetFactory imageViewTargetFactory, Glide.RequestOptionsFactory requestOptionsFactory, Map<Class<?>, TransitionOptions<?, ?>> map, List<RequestListener<Object>> list, Engine engine, GlideExperiments glideExperiments, int n) {
        super(context.getApplicationContext());
        this.arrayPool = arrayPool;
        this.registry = registry;
        this.imageViewTargetFactory = imageViewTargetFactory;
        this.defaultRequestOptionsFactory = requestOptionsFactory;
        this.defaultRequestListeners = list;
        this.defaultTransitionOptions = map;
        this.engine = engine;
        this.experiments = glideExperiments;
        this.logLevel = n;
    }

    public <X> ViewTarget<ImageView, X> buildImageViewTarget(ImageView imageView, Class<X> clazz) {
        return this.imageViewTargetFactory.buildTarget(imageView, clazz);
    }

    public ArrayPool getArrayPool() {
        return this.arrayPool;
    }

    public List<RequestListener<Object>> getDefaultRequestListeners() {
        return this.defaultRequestListeners;
    }

    public RequestOptions getDefaultRequestOptions() {
        synchronized (this) {
            if (this.defaultRequestOptions == null) {
                this.defaultRequestOptions = (RequestOptions)this.defaultRequestOptionsFactory.build().lock();
            }
            RequestOptions requestOptions = this.defaultRequestOptions;
            return requestOptions;
        }
    }

    public <T> TransitionOptions<?, T> getDefaultTransitionOptions(Class<T> transitionOptions) {
        TransitionOptions transitionOptions2 = this.defaultTransitionOptions.get(transitionOptions);
        Map.Entry<Class<?>, TransitionOptions<?, ?>> entry = transitionOptions2;
        if (transitionOptions2 == null) {
            Iterator<Map.Entry<Class<?>, TransitionOptions<?, ?>>> iterator2 = this.defaultTransitionOptions.entrySet().iterator();
            while (true) {
                entry = transitionOptions2;
                if (!iterator2.hasNext()) break;
                entry = iterator2.next();
                if (!((Class)entry.getKey()).isAssignableFrom((Class<?>)((Object)transitionOptions))) continue;
                transitionOptions2 = (TransitionOptions)entry.getValue();
            }
        }
        transitionOptions = entry;
        if (entry == null) {
            transitionOptions = DEFAULT_TRANSITION_OPTIONS;
        }
        return transitionOptions;
    }

    public Engine getEngine() {
        return this.engine;
    }

    public GlideExperiments getExperiments() {
        return this.experiments;
    }

    public int getLogLevel() {
        return this.logLevel;
    }

    public Registry getRegistry() {
        return this.registry;
    }
}

