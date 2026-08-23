/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Bitmap
 *  android.graphics.ImageDecoder
 *  android.graphics.ImageDecoder$OnHeaderDecodedListener
 *  android.graphics.ImageDecoder$Source
 *  android.util.Log
 */
package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.util.Log;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPoolAdapter;
import com.bumptech.glide.load.resource.DefaultOnHeaderDecodedListener;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import java.io.IOException;

public final class BitmapImageDecoderResourceDecoder
implements ResourceDecoder<ImageDecoder.Source, Bitmap> {
    private static final String TAG = "BitmapImageDecoder";
    private final BitmapPool bitmapPool = new BitmapPoolAdapter();

    @Override
    public Resource<Bitmap> decode(ImageDecoder.Source source, int n, int n2, Options object) throws IOException {
        source = ImageDecoder.decodeBitmap((ImageDecoder.Source)source, (ImageDecoder.OnHeaderDecodedListener)new DefaultOnHeaderDecodedListener(n, n2, (Options)object));
        if (Log.isLoggable((String)TAG, (int)2)) {
            object = new StringBuilder();
            ((StringBuilder)object).append("Decoded [");
            ((StringBuilder)object).append(source.getWidth());
            ((StringBuilder)object).append("x");
            ((StringBuilder)object).append(source.getHeight());
            ((StringBuilder)object).append("] for [");
            ((StringBuilder)object).append(n);
            ((StringBuilder)object).append("x");
            ((StringBuilder)object).append(n2);
            ((StringBuilder)object).append("]");
            Log.v((String)TAG, (String)((StringBuilder)object).toString());
        }
        return new BitmapResource((Bitmap)source, this.bitmapPool);
    }

    @Override
    public boolean handles(ImageDecoder.Source source, Options options) throws IOException {
        return true;
    }
}

