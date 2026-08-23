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
import java.io.IOException;
import java.nio.ByteBuffer;

public final class ByteBufferBitmapImageDecoderResourceDecoder
implements ResourceDecoder<ByteBuffer, Bitmap> {
    private final BitmapImageDecoderResourceDecoder wrapped = new BitmapImageDecoderResourceDecoder();

    @Override
    public Resource<Bitmap> decode(ByteBuffer byteBuffer, int n, int n2, Options options) throws IOException {
        byteBuffer = ImageDecoder.createSource((ByteBuffer)byteBuffer);
        return this.wrapped.decode((ImageDecoder.Source)byteBuffer, n, n2, options);
    }

    @Override
    public boolean handles(ByteBuffer byteBuffer, Options options) throws IOException {
        return true;
    }
}

