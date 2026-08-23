/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.res.AssetFileDescriptor
 *  android.content.res.Resources
 *  android.content.res.Resources$NotFoundException
 *  android.net.Uri
 *  android.os.ParcelFileDescriptor
 *  android.util.Log
 */
package com.bumptech.glide.load.model;

import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.load.model.UnitModelLoader;
import java.io.InputStream;

public class ResourceLoader<Data>
implements ModelLoader<Integer, Data> {
    private static final String TAG = "ResourceLoader";
    private final Resources resources;
    private final ModelLoader<Uri, Data> uriLoader;

    public ResourceLoader(Resources resources, ModelLoader<Uri, Data> modelLoader) {
        this.resources = resources;
        this.uriLoader = modelLoader;
    }

    private Uri getResourceUri(Integer n) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("android.resource://");
            stringBuilder.append(this.resources.getResourcePackageName(n.intValue()));
            stringBuilder.append('/');
            stringBuilder.append(this.resources.getResourceTypeName(n.intValue()));
            stringBuilder.append('/');
            stringBuilder.append(this.resources.getResourceEntryName(n.intValue()));
            stringBuilder = Uri.parse((String)stringBuilder.toString());
            return stringBuilder;
        }
        catch (Resources.NotFoundException notFoundException) {
            if (Log.isLoggable((String)TAG, (int)5)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Received invalid resource id: ");
                stringBuilder.append(n);
                Log.w((String)TAG, (String)stringBuilder.toString(), (Throwable)notFoundException);
            }
            return null;
        }
    }

    @Override
    public ModelLoader.LoadData<Data> buildLoadData(Integer object, int n, int n2, Options options) {
        object = (object = this.getResourceUri((Integer)object)) == null ? null : this.uriLoader.buildLoadData((Uri)object, n, n2, options);
        return object;
    }

    @Override
    public boolean handles(Integer n) {
        return true;
    }

    public static final class AssetFileDescriptorFactory
    implements ModelLoaderFactory<Integer, AssetFileDescriptor> {
        private final Resources resources;

        public AssetFileDescriptorFactory(Resources resources) {
            this.resources = resources;
        }

        @Override
        public ModelLoader<Integer, AssetFileDescriptor> build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new ResourceLoader<AssetFileDescriptor>(this.resources, multiModelLoaderFactory.build(Uri.class, AssetFileDescriptor.class));
        }

        @Override
        public void teardown() {
        }
    }

    public static class FileDescriptorFactory
    implements ModelLoaderFactory<Integer, ParcelFileDescriptor> {
        private final Resources resources;

        public FileDescriptorFactory(Resources resources) {
            this.resources = resources;
        }

        @Override
        public ModelLoader<Integer, ParcelFileDescriptor> build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new ResourceLoader<ParcelFileDescriptor>(this.resources, multiModelLoaderFactory.build(Uri.class, ParcelFileDescriptor.class));
        }

        @Override
        public void teardown() {
        }
    }

    public static class StreamFactory
    implements ModelLoaderFactory<Integer, InputStream> {
        private final Resources resources;

        public StreamFactory(Resources resources) {
            this.resources = resources;
        }

        @Override
        public ModelLoader<Integer, InputStream> build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new ResourceLoader<InputStream>(this.resources, multiModelLoaderFactory.build(Uri.class, InputStream.class));
        }

        @Override
        public void teardown() {
        }
    }

    public static class UriFactory
    implements ModelLoaderFactory<Integer, Uri> {
        private final Resources resources;

        public UriFactory(Resources resources) {
            this.resources = resources;
        }

        @Override
        public ModelLoader<Integer, Uri> build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new ResourceLoader<Uri>(this.resources, UnitModelLoader.getInstance());
        }

        @Override
        public void teardown() {
        }
    }
}

