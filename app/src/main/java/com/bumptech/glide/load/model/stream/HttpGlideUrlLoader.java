/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.load.model.stream;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.HttpUrlFetcher;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelCache;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import java.io.InputStream;

public class HttpGlideUrlLoader
implements ModelLoader<GlideUrl, InputStream> {
    public static final Option<Integer> TIMEOUT = Option.memory("com.bumptech.glide.load.model.stream.HttpGlideUrlLoader.Timeout", 2500);
    private final ModelCache<GlideUrl, GlideUrl> modelCache;

    public HttpGlideUrlLoader() {
        this(null);
    }

    public HttpGlideUrlLoader(ModelCache<GlideUrl, GlideUrl> modelCache) {
        this.modelCache = modelCache;
    }

    @Override
    public ModelLoader.LoadData<InputStream> buildLoadData(GlideUrl glideUrl, int n, int n2, Options options) {
        Object object = glideUrl;
        ModelCache<GlideUrl, GlideUrl> modelCache = this.modelCache;
        if (modelCache != null) {
            object = modelCache = modelCache.get(glideUrl, 0, 0);
            if (modelCache == null) {
                this.modelCache.put(glideUrl, 0, 0, glideUrl);
                object = glideUrl;
            }
        }
        return new ModelLoader.LoadData<InputStream>((Key)object, new HttpUrlFetcher((GlideUrl)object, options.get(TIMEOUT)));
    }

    @Override
    public boolean handles(GlideUrl glideUrl) {
        return true;
    }

    public static class Factory
    implements ModelLoaderFactory<GlideUrl, InputStream> {
        private final ModelCache<GlideUrl, GlideUrl> modelCache = new ModelCache(500L);

        @Override
        public ModelLoader<GlideUrl, InputStream> build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new HttpGlideUrlLoader(this.modelCache);
        }

        @Override
        public void teardown() {
        }
    }
}

