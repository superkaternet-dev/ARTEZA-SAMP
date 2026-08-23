/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.load.model.stream;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import java.io.InputStream;
import java.net.URL;

public class UrlLoader
implements ModelLoader<URL, InputStream> {
    private final ModelLoader<GlideUrl, InputStream> glideUrlLoader;

    public UrlLoader(ModelLoader<GlideUrl, InputStream> modelLoader) {
        this.glideUrlLoader = modelLoader;
    }

    @Override
    public ModelLoader.LoadData<InputStream> buildLoadData(URL uRL, int n, int n2, Options options) {
        return this.glideUrlLoader.buildLoadData(new GlideUrl(uRL), n, n2, options);
    }

    @Override
    public boolean handles(URL uRL) {
        return true;
    }

    public static class StreamFactory
    implements ModelLoaderFactory<URL, InputStream> {
        @Override
        public ModelLoader<URL, InputStream> build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new UrlLoader(multiModelLoaderFactory.build(GlideUrl.class, InputStream.class));
        }

        @Override
        public void teardown() {
        }
    }
}

