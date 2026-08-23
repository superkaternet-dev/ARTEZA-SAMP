/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Bitmap
 *  android.graphics.ImageDecoder
 *  android.graphics.ImageDecoder$Source
 */
package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.bitmap.BitmapImageDecoderResourceDecoder;
import com.bumptech.glide.util.ByteBufferUtil;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public final class InputStreamBitmapImageDecoderResourceDecoder
implements ResourceDecoder<InputStream, Bitmap> {
    private final BitmapImageDecoderResourceDecoder wrapped = new BitmapImageDecoderResourceDecoder();

    @Override
    public Resource<Bitmap> decode(InputStream inputStream, int n, int n2, Options options) throws IOException {
        inputStream = ImageDecoder.createSource((ByteBuffer)ByteBufferUtil.fromStream(inputStream));
        return this.wrapped.decode((ImageDecoder.Source)inputStream, n, n2, options);
    }

    @Override
    public boolean handles(InputStream inputStream, Options options) throws IOException {
        return true;
    }
}

