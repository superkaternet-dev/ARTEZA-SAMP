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
import com.bumptech.glide.load.resource.bitmap.VideoDecoder;
import com.bumptech.glide.signature.ObjectKey;
import java.io.InputStream;

public class MediaStoreVideoThumbLoader
implements ModelLoader<Uri, InputStream> {
    private final Context context;

    public MediaStoreVideoThumbLoader(Context context) {
        this.context = context.getApplicationContext();
    }

    private boolean isRequestingDefaultFrame(Options object) {
        boolean bl = (object = ((Options)object).get(VideoDecoder.TARGET_FRAME)) != null && (Long)object == -1L;
        return bl;
    }

    @Override
    public ModelLoader.LoadData<InputStream> buildLoadData(Uri uri, int n, int n2, Options options) {
        if (MediaStoreUtil.isThumbnailSize(n, n2) && this.isRequestingDefaultFrame(options)) {
            return new ModelLoader.LoadData<InputStream>(new ObjectKey(uri), ThumbFetcher.buildVideoFetcher(this.context, uri));
        }
        return null;
    }

    @Override
    public boolean handles(Uri uri) {
        return MediaStoreUtil.isMediaStoreVideoUri(uri);
    }

    public static class Factory
    implements ModelLoaderFactory<Uri, InputStream> {
        private final Context context;

        public Factory(Context context) {
            this.context = context;
        }

        @Override
        public ModelLoader<Uri, InputStream> build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new MediaStoreVideoThumbLoader(this.context);
        }

        @Override
        public void teardown() {
        }
    }
}

