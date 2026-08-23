/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ContentResolver
 *  android.content.res.AssetFileDescriptor
 *  android.net.Uri
 *  android.os.ParcelFileDescriptor
 */
package com.bumptech.glide.load.model;

import android.content.ContentResolver;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.AssetFileDescriptorLocalUriFetcher;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.data.FileDescriptorLocalUriFetcher;
import com.bumptech.glide.load.data.StreamLocalUriFetcher;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.signature.ObjectKey;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class UriLoader<Data>
implements ModelLoader<Uri, Data> {
    private static final Set<String> SCHEMES = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList("file", "android.resource", "content")));
    private final LocalUriFetcherFactory<Data> factory;

    public UriLoader(LocalUriFetcherFactory<Data> localUriFetcherFactory) {
        this.factory = localUriFetcherFactory;
    }

    @Override
    public ModelLoader.LoadData<Data> buildLoadData(Uri uri, int n, int n2, Options options) {
        return new ModelLoader.LoadData<Data>(new ObjectKey(uri), this.factory.build(uri));
    }

    @Override
    public boolean handles(Uri uri) {
        return SCHEMES.contains(uri.getScheme());
    }

    public static final class AssetFileDescriptorFactory
    implements ModelLoaderFactory<Uri, AssetFileDescriptor>,
    LocalUriFetcherFactory<AssetFileDescriptor> {
        private final ContentResolver contentResolver;

        public AssetFileDescriptorFactory(ContentResolver contentResolver) {
            this.contentResolver = contentResolver;
        }

        @Override
        public DataFetcher<AssetFileDescriptor> build(Uri uri) {
            return new AssetFileDescriptorLocalUriFetcher(this.contentResolver, uri);
        }

        @Override
        public ModelLoader<Uri, AssetFileDescriptor> build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new UriLoader<AssetFileDescriptor>(this);
        }

        @Override
        public void teardown() {
        }
    }

    public static class FileDescriptorFactory
    implements ModelLoaderFactory<Uri, ParcelFileDescriptor>,
    LocalUriFetcherFactory<ParcelFileDescriptor> {
        private final ContentResolver contentResolver;

        public FileDescriptorFactory(ContentResolver contentResolver) {
            this.contentResolver = contentResolver;
        }

        @Override
        public DataFetcher<ParcelFileDescriptor> build(Uri uri) {
            return new FileDescriptorLocalUriFetcher(this.contentResolver, uri);
        }

        @Override
        public ModelLoader<Uri, ParcelFileDescriptor> build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new UriLoader<ParcelFileDescriptor>(this);
        }

        @Override
        public void teardown() {
        }
    }

    public static interface LocalUriFetcherFactory<Data> {
        public DataFetcher<Data> build(Uri var1);
    }

    public static class StreamFactory
    implements ModelLoaderFactory<Uri, InputStream>,
    LocalUriFetcherFactory<InputStream> {
        private final ContentResolver contentResolver;

        public StreamFactory(ContentResolver contentResolver) {
            this.contentResolver = contentResolver;
        }

        @Override
        public DataFetcher<InputStream> build(Uri uri) {
            return new StreamLocalUriFetcher(this.contentResolver, uri);
        }

        @Override
        public ModelLoader<Uri, InputStream> build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new UriLoader<InputStream>(this);
        }

        @Override
        public void teardown() {
        }
    }
}

