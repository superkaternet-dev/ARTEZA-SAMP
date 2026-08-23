/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ContentResolver
 *  android.content.Context
 *  android.database.Cursor
 *  android.net.Uri
 *  android.provider.MediaStore$Images$Thumbnails
 *  android.provider.MediaStore$Video$Thumbnails
 *  android.util.Log
 */
package com.bumptech.glide.load.data.mediastore;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.data.ExifOrientationStream;
import com.bumptech.glide.load.data.mediastore.ThumbnailQuery;
import com.bumptech.glide.load.data.mediastore.ThumbnailStreamOpener;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ThumbFetcher
implements DataFetcher<InputStream> {
    private static final String TAG = "MediaStoreThumbFetcher";
    private InputStream inputStream;
    private final Uri mediaStoreImageUri;
    private final ThumbnailStreamOpener opener;

    ThumbFetcher(Uri uri, ThumbnailStreamOpener thumbnailStreamOpener) {
        this.mediaStoreImageUri = uri;
        this.opener = thumbnailStreamOpener;
    }

    private static ThumbFetcher build(Context context, Uri uri, ThumbnailQuery thumbnailQuery) {
        ArrayPool arrayPool = Glide.get(context).getArrayPool();
        return new ThumbFetcher(uri, new ThumbnailStreamOpener(Glide.get(context).getRegistry().getImageHeaderParsers(), thumbnailQuery, arrayPool, context.getContentResolver()));
    }

    public static ThumbFetcher buildImageFetcher(Context context, Uri uri) {
        return ThumbFetcher.build(context, uri, new ImageThumbnailQuery(context.getContentResolver()));
    }

    public static ThumbFetcher buildVideoFetcher(Context context, Uri uri) {
        return ThumbFetcher.build(context, uri, new VideoThumbnailQuery(context.getContentResolver()));
    }

    private InputStream openThumbInputStream() throws FileNotFoundException {
        InputStream inputStream = this.opener.open(this.mediaStoreImageUri);
        int n = -1;
        if (inputStream != null) {
            n = this.opener.getOrientation(this.mediaStoreImageUri);
        }
        InputStream inputStream2 = inputStream;
        if (n != -1) {
            inputStream2 = new ExifOrientationStream(inputStream, n);
        }
        return inputStream2;
    }

    @Override
    public void cancel() {
    }

    @Override
    public void cleanup() {
        InputStream inputStream = this.inputStream;
        if (inputStream != null) {
            try {
                inputStream.close();
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
    }

    @Override
    public Class<InputStream> getDataClass() {
        return InputStream.class;
    }

    @Override
    public DataSource getDataSource() {
        return DataSource.LOCAL;
    }

    @Override
    public void loadData(Priority object, DataFetcher.DataCallback<? super InputStream> dataCallback) {
        try {
            object = this.openThumbInputStream();
            this.inputStream = object;
            dataCallback.onDataReady((InputStream)object);
        }
        catch (FileNotFoundException fileNotFoundException) {
            if (Log.isLoggable((String)TAG, (int)3)) {
                Log.d((String)TAG, (String)"Failed to find thumbnail file", (Throwable)fileNotFoundException);
            }
            dataCallback.onLoadFailed(fileNotFoundException);
        }
    }

    static class ImageThumbnailQuery
    implements ThumbnailQuery {
        private static final String[] PATH_PROJECTION = new String[]{"_data"};
        private static final String PATH_SELECTION = "kind = 1 AND image_id = ?";
        private final ContentResolver contentResolver;

        ImageThumbnailQuery(ContentResolver contentResolver) {
            this.contentResolver = contentResolver;
        }

        @Override
        public Cursor query(Uri object) {
            object = object.getLastPathSegment();
            return this.contentResolver.query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, PATH_PROJECTION, PATH_SELECTION, new String[]{object}, null);
        }
    }

    static class VideoThumbnailQuery
    implements ThumbnailQuery {
        private static final String[] PATH_PROJECTION = new String[]{"_data"};
        private static final String PATH_SELECTION = "kind = 1 AND video_id = ?";
        private final ContentResolver contentResolver;

        VideoThumbnailQuery(ContentResolver contentResolver) {
            this.contentResolver = contentResolver;
        }

        @Override
        public Cursor query(Uri object) {
            object = object.getLastPathSegment();
            return this.contentResolver.query(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI, PATH_PROJECTION, PATH_SELECTION, new String[]{object}, null);
        }
    }
}

