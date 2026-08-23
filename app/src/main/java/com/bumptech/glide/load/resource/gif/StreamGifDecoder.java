/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package com.bumptech.glide.load.resource.gif;

import android.util.Log;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.ImageHeaderParserUtils;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.load.resource.gif.GifOptions;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

public class StreamGifDecoder
implements ResourceDecoder<InputStream, GifDrawable> {
    private static final String TAG = "StreamGifDecoder";
    private final ArrayPool byteArrayPool;
    private final ResourceDecoder<ByteBuffer, GifDrawable> byteBufferDecoder;
    private final List<ImageHeaderParser> parsers;

    public StreamGifDecoder(List<ImageHeaderParser> list, ResourceDecoder<ByteBuffer, GifDrawable> resourceDecoder, ArrayPool arrayPool) {
        this.parsers = list;
        this.byteBufferDecoder = resourceDecoder;
        this.byteArrayPool = arrayPool;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static byte[] inputStreamToBytes(InputStream inputStream) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(16384);
        try {
            byte[] byArray = new byte[16384];
            while (true) {
                int n;
                if ((n = inputStream.read(byArray)) == -1) {
                    byteArrayOutputStream.flush();
                    return byteArrayOutputStream.toByteArray();
                }
                byteArrayOutputStream.write(byArray, 0, n);
            }
        }
        catch (IOException iOException) {
            if (Log.isLoggable((String)TAG, (int)5)) {
                Log.w((String)TAG, (String)"Error reading data from stream", (Throwable)iOException);
            }
            return null;
        }
    }

    @Override
    public Resource<GifDrawable> decode(InputStream object, int n, int n2, Options options) throws IOException {
        if ((object = (Object)StreamGifDecoder.inputStreamToBytes((InputStream)object)) == null) {
            return null;
        }
        object = ByteBuffer.wrap((byte[])object);
        return this.byteBufferDecoder.decode((ByteBuffer)object, n, n2, options);
    }

    @Override
    public boolean handles(InputStream inputStream, Options options) throws IOException {
        boolean bl = options.get(GifOptions.DISABLE_ANIMATION) == false && ImageHeaderParserUtils.getType(this.parsers, inputStream, this.byteArrayPool) == ImageHeaderParser.ImageType.GIF;
        return bl;
    }
}

