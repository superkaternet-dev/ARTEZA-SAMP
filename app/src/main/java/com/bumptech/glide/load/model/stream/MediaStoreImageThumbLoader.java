/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.net.Uri
 */
package com.bumptech.glide.load.model.stream;

import android.content.Context;
import android.net.Uri;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.mediastore.MediaStoreUtil;
import com.bumptech.glide.load.data.mediastore.ThumbFetcher;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.signature.ObjectKey;
import java.io.InputStream;

public class MediaStoreImageThumbLoader
implements ModelLoader<Uri, InputStream> {
    private final Context context;

    public MediaStoreImageThumbLoader(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public ModelLoader.LoadData<InputStream> buildLoadData(Uri uri, int n, int n2, Options options) {
        if (MediaStoreUtil.isThumbnailSize(n, n2)) {
            return new ModelLoader.LoadData<InputStream>(new ObjectKey(uri), ThumbFetcher.buildImageFetcher(this.context, uri));
        }
        return null;
    }

    @Override
    public boolean handles(Uri uri) {
        return MediaStoreUtil.isMediaStoreImageUri(uri);
    }

    public static class Factory
    implements ModelLoaderFactory<Uri, InputStream> {
        private final Context context;

        public Factory(Context context) {
            this.context = context;
        }

        @Override
        public ModelLoader<Uri, InputStream> build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new MediaStoreImageThumbLoader(this.context);
        }

        @Override
        public void teardown() {
        }
    }
}

