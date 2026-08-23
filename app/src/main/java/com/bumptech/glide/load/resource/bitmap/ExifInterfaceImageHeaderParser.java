/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.load.resource.bitmap;

import androidx.exifinterface.media.ExifInterface;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.util.ByteBufferUtil;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public final class ExifInterfaceImageHeaderParser
implements ImageHeaderParser {
    @Override
    public int getOrientation(InputStream object, ArrayPool arrayPool) throws IOException {
        int n = ((ExifInterface)(object = new ExifInterface((InputStream)object))).getAttributeInt("Orientation", 1);
        if (n == 0) {
            return -1;
        }
        return n;
    }

    @Override
    public int getOrientation(ByteBuffer byteBuffer, ArrayPool arrayPool) throws IOException {
        return this.getOrientation(ByteBufferUtil.toStream(byteBuffer), arrayPool);
    }

    @Override
    public ImageHeaderParser.ImageType getType(InputStream inputStream) {
        return ImageHeaderParser.ImageType.UNKNOWN;
    }

    @Override
    public ImageHeaderParser.ImageType getType(ByteBuffer byteBuffer) {
        return ImageHeaderParser.ImageType.UNKNOWN;
    }
}

