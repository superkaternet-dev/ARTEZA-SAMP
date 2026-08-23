/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.res.AssetFileDescriptor
 *  android.net.Uri
 *  android.os.ParcelFileDescriptor
 *  android.text.TextUtils
 */
package com.bumptech.glide.load.model;

import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import java.io.File;
import java.io.InputStream;

public class StringLoader<Data>
implements ModelLoader<String, Data> {
    private final ModelLoader<Uri, Data> uriLoader;

    public StringLoader(ModelLoader<Uri, Data> modelLoader) {
        this.uriLoader = modelLoader;
    }

    private static Uri parseUri(String string2) {
        Uri uri;
        if (TextUtils.isEmpty((CharSequence)string2)) {
            return null;
        }
        if (string2.charAt(0) == '/') {
            uri = StringLoader.toFileUri(string2);
        } else {
            Uri uri2;
            uri = uri2 = Uri.parse((String)string2);
            if (uri2.getScheme() == null) {
                uri = StringLoader.toFileUri(string2);
            }
        }
        return uri;
    }

    private static Uri toFileUri(String string2) {
        return Uri.fromFile((File)new File(string2));
    }

    @Override
    public ModelLoader.LoadData<Data> buildLoadData(String string2, int n, int n2, Options options) {
        if ((string2 = StringLoader.parseUri(string2)) != null && this.uriLoader.handles((Uri)string2)) {
            return this.uriLoader.buildLoadData((Uri)string2, n, n2, options);
        }
        return null;
    }

    @Override
    public boolean handles(String string2) {
        return true;
    }

    public static final class AssetFileDescriptorFactory
    implements ModelLoaderFactory<String, AssetFileDescriptor> {
        @Override
        public ModelLoader<String, AssetFileDescriptor> build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new StringLoader<AssetFileDescriptor>(multiModelLoaderFactory.build(Uri.class, AssetFileDescriptor.class));
        }

        @Override
        public void teardown() {
        }
    }

    public static class FileDescriptorFactory
    implements ModelLoaderFactory<String, ParcelFileDescriptor> {
        @Override
        public ModelLoader<String, ParcelFileDescriptor> build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new StringLoader<ParcelFileDescriptor>(multiModelLoaderFactory.build(Uri.class, ParcelFileDescriptor.class));
        }

        @Override
        public void teardown() {
        }
    }

    public static class StreamFactory
    implements ModelLoaderFactory<String, InputStream> {
        @Override
        public ModelLoader<String, InputStream> build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new StringLoader<InputStream>(multiModelLoaderFactory.build(Uri.class, InputStream.class));
        }

        @Override
        public void teardown() {
        }
    }
}

