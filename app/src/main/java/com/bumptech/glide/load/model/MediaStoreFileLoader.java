/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.database.Cursor
 *  android.net.Uri
 *  android.text.TextUtils
 */
package com.bumptech.glide.load.model;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.data.mediastore.MediaStoreUtil;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.signature.ObjectKey;
import java.io.File;
import java.io.FileNotFoundException;

public final class MediaStoreFileLoader
implements ModelLoader<Uri, File> {
    private final Context context;

    public MediaStoreFileLoader(Context context) {
        this.context = context;
    }

    @Override
    public ModelLoader.LoadData<File> buildLoadData(Uri uri, int n, int n2, Options options) {
        return new ModelLoader.LoadData<File>(new ObjectKey(uri), new FilePathFetcher(this.context, uri));
    }

    @Override
    public boolean handles(Uri uri) {
        return MediaStoreUtil.isMediaStoreUri(uri);
    }

    public static final class Factory
    implements ModelLoaderFactory<Uri, File> {
        private final Context context;

        public Factory(Context context) {
            this.context = context;
        }

        @Override
        public ModelLoader<Uri, File> build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new MediaStoreFileLoader(this.context);
        }

        @Override
        public void teardown() {
        }
    }

    private static class FilePathFetcher
    implements DataFetcher<File> {
        private static final String[] PROJECTION = new String[]{"_data"};
        private final Context context;
        private final Uri uri;

        FilePathFetcher(Context context, Uri uri) {
            this.context = context;
            this.uri = uri;
        }

        @Override
        public void cancel() {
        }

        @Override
        public void cleanup() {
        }

        @Override
        public Class<File> getDataClass() {
            return File.class;
        }

        @Override
        public DataSource getDataSource() {
            return DataSource.LOCAL;
        }

        @Override
        public void loadData(Priority object, DataFetcher.DataCallback<? super File> dataCallback) {
            Cursor cursor = this.context.getContentResolver().query(this.uri, PROJECTION, null, null, null);
            object = null;
            Object var3_5 = null;
            if (cursor != null) {
                object = var3_5;
                try {
                    if (cursor.moveToFirst()) {
                        object = cursor.getString(cursor.getColumnIndexOrThrow("_data"));
                    }
                }
                finally {
                    cursor.close();
                }
            }
            if (TextUtils.isEmpty((CharSequence)object)) {
                object = new StringBuilder();
                ((StringBuilder)object).append("Failed to find file path for: ");
                ((StringBuilder)object).append(this.uri);
                dataCallback.onLoadFailed(new FileNotFoundException(((StringBuilder)object).toString()));
            } else {
                dataCallback.onDataReady(new File((String)object));
            }
        }
    }
}

