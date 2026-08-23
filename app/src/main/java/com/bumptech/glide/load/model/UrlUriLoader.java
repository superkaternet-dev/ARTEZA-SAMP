/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.net.Uri
 */
package com.bumptech.glide.load.model;

import android.net.Uri;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class UrlUriLoader<Data>
implements ModelLoader<Uri, Data> {
    private static final Set<String> SCHEMES = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList("http", "https")));
    private final ModelLoader<GlideUrl, Data> urlLoader;

    public UrlUriLoader(ModelLoader<GlideUrl, Data> modelLoader) {
        this.urlLoader = modelLoader;
    }

    @Override
    public ModelLoader.LoadData<Data> buildLoadData(Uri object, int n, int n2, Options options) {
        object = new GlideUrl(object.toString());
        return this.urlLoader.buildLoadData((GlideUrl)object, n, n2, options);
    }

    @Override
    public boolean handles(Uri uri) {
        return SCHEMES.contains(uri.getScheme());
    }

    public static class StreamFactory
    implements ModelLoaderFactory<Uri, InputStream> {
        @Override
        public ModelLoader<Uri, InputStream> build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new UrlUriLoader<InputStream>(multiModelLoaderFactory.build(GlideUrl.class, InputStream.class));
        }

        @Override
        public void teardown() {
        }
    }
}

