/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.res.AssetFileDescriptor
 *  android.content.res.AssetManager
 *  android.net.Uri
 */
package com.bumptech.glide.load.model;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.net.Uri;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.data.FileDescriptorAssetPathFetcher;
import com.bumptech.glide.load.data.StreamAssetPathFetcher;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.signature.ObjectKey;
import java.io.InputStream;

public class AssetUriLoader<Data>
implements ModelLoader<Uri, Data> {
    private static final String ASSET_PATH_SEGMENT = "android_asset";
    private static final String ASSET_PREFIX = "file:///android_asset/";
    private static final int ASSET_PREFIX_LENGTH = "file:///android_asset/".length();
    private final AssetManager assetManager;
    private final AssetFetcherFactory<Data> factory;

    public AssetUriLoader(AssetManager assetManager, AssetFetcherFactory<Data> assetFetcherFactory) {
        this.assetManager = assetManager;
        this.factory = assetFetcherFactory;
    }

    @Override
    public ModelLoader.LoadData<Data> buildLoadData(Uri uri, int n, int n2, Options object) {
        object = uri.toString().substring(ASSET_PREFIX_LENGTH);
        return new ModelLoader.LoadData<Data>(new ObjectKey(uri), this.factory.buildFetcher(this.assetManager, (String)object));
    }

    @Override
    public boolean handles(Uri uri) {
        boolean bl;
        block0: {
            boolean bl2 = "file".equals(uri.getScheme());
            bl = false;
            if (!bl2 || uri.getPathSegments().isEmpty() || !ASSET_PATH_SEGMENT.equals(uri.getPathSegments().get(0))) break block0;
            bl = true;
        }
        return bl;
    }

    public static interface AssetFetcherFactory<Data> {
        public DataFetcher<Data> buildFetcher(AssetManager var1, String var2);
    }

    public static class FileDescriptorFactory
    implements ModelLoaderFactory<Uri, AssetFileDescriptor>,
    AssetFetcherFactory<AssetFileDescriptor> {
        private final AssetManager assetManager;

        public FileDescriptorFactory(AssetManager assetManager) {
            this.assetManager = assetManager;
        }

        @Override
        public ModelLoader<Uri, AssetFileDescriptor> build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new AssetUriLoader<AssetFileDescriptor>(this.assetManager, this);
        }

        @Override
        public DataFetcher<AssetFileDescriptor> buildFetcher(AssetManager assetManager, String string2) {
            return new FileDescriptorAssetPathFetcher(assetManager, string2);
        }

        @Override
        public void teardown() {
        }
    }

    public static class StreamFactory
    implements ModelLoaderFactory<Uri, InputStream>,
    AssetFetcherFactory<InputStream> {
        private final AssetManager assetManager;

        public StreamFactory(AssetManager assetManager) {
            this.assetManager = assetManager;
        }

        @Override
        public ModelLoader<Uri, InputStream> build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new AssetUriLoader<InputStream>(this.assetManager, this);
        }

        @Override
        public DataFetcher<InputStream> buildFetcher(AssetManager assetManager, String string2) {
            return new StreamAssetPathFetcher(assetManager, string2);
        }

        @Override
        public void teardown() {
        }
    }
}

