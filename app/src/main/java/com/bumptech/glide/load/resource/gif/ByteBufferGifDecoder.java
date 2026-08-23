/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$Config
 *  android.util.Log
 */
package com.bumptech.glide.load.resource.gif;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import com.bumptech.glide.Glide;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.gifdecoder.GifHeader;
import com.bumptech.glide.gifdecoder.GifHeaderParser;
import com.bumptech.glide.gifdecoder.StandardGifDecoder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.ImageHeaderParserUtils;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.UnitTransformation;
import com.bumptech.glide.load.resource.gif.GifBitmapProvider;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawableResource;
import com.bumptech.glide.load.resource.gif.GifOptions;
import com.bumptech.glide.util.LogTime;
import com.bumptech.glide.util.Util;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Queue;

public class ByteBufferGifDecoder
implements ResourceDecoder<ByteBuffer, GifDrawable> {
    private static final GifDecoderFactory GIF_DECODER_FACTORY = new GifDecoderFactory();
    private static final GifHeaderParserPool PARSER_POOL = new GifHeaderParserPool();
    private static final String TAG = "BufferGifDecoder";
    private final Context context;
    private final GifDecoderFactory gifDecoderFactory;
    private final GifHeaderParserPool parserPool;
    private final List<ImageHeaderParser> parsers;
    private final GifBitmapProvider provider;

    public ByteBufferGifDecoder(Context context) {
        this(context, Glide.get(context).getRegistry().getImageHeaderParsers(), Glide.get(context).getBitmapPool(), Glide.get(context).getArrayPool());
    }

    public ByteBufferGifDecoder(Context context, List<ImageHeaderParser> list, BitmapPool bitmapPool, ArrayPool arrayPool) {
        this(context, list, bitmapPool, arrayPool, PARSER_POOL, GIF_DECODER_FACTORY);
    }

    ByteBufferGifDecoder(Context context, List<ImageHeaderParser> list, BitmapPool bitmapPool, ArrayPool arrayPool, GifHeaderParserPool gifHeaderParserPool, GifDecoderFactory gifDecoderFactory) {
        this.context = context.getApplicationContext();
        this.parsers = list;
        this.gifDecoderFactory = gifDecoderFactory;
        this.provider = new GifBitmapProvider(bitmapPool, arrayPool);
        this.parserPool = gifHeaderParserPool;
    }

    private GifDrawableResource decode(ByteBuffer object, int n, int n2, GifHeaderParser object2, Options options) {
        long l;
        block7: {
            Object object3;
            block8: {
                l = LogTime.getLogTime();
                object3 = ((GifHeaderParser)object2).parseHeader();
                if (((GifHeader)object3).getNumFrames() <= 0 || ((GifHeader)object3).getStatus() != 0) break block7;
                object2 = options.get(GifOptions.DECODE_FORMAT) == DecodeFormat.PREFER_RGB_565 ? Bitmap.Config.RGB_565 : Bitmap.Config.ARGB_8888;
                int n3 = ByteBufferGifDecoder.getSampleSize((GifHeader)object3, n, n2);
                object = this.gifDecoderFactory.build(this.provider, (GifHeader)object3, (ByteBuffer)object, n3);
                object.setDefaultBitmapConfig((Bitmap.Config)object2);
                object.advance();
                options = object.getNextFrame();
                if (options != null) break block8;
                if (Log.isLoggable((String)TAG, (int)2)) {
                    object = new StringBuilder();
                    ((StringBuilder)object).append("Decoded GIF from stream in ");
                    ((StringBuilder)object).append(LogTime.getElapsedMillis(l));
                    Log.v((String)TAG, (String)((StringBuilder)object).toString());
                }
                return null;
            }
            object3 = UnitTransformation.get();
            object2 = new GifDrawable(this.context, (GifDecoder)object, (Transformation<Bitmap>)object3, n, n2, (Bitmap)options);
            object = new GifDrawableResource((GifDrawable)object2);
            return object;
        }
        if (Log.isLoggable((String)TAG, (int)2)) {
            object = new StringBuilder();
            ((StringBuilder)object).append("Decoded GIF from stream in ");
            ((StringBuilder)object).append(LogTime.getElapsedMillis(l));
            Log.v((String)TAG, (String)((StringBuilder)object).toString());
        }
        return null;
        finally {
            if (Log.isLoggable((String)TAG, (int)2)) {
                object2 = new StringBuilder();
                ((StringBuilder)object2).append("Decoded GIF from stream in ");
                ((StringBuilder)object2).append(LogTime.getElapsedMillis(l));
                Log.v((String)TAG, (String)((StringBuilder)object2).toString());
            }
        }
    }

    private static int getSampleSize(GifHeader gifHeader, int n, int n2) {
        int n3 = Math.min(gifHeader.getHeight() / n2, gifHeader.getWidth() / n);
        n3 = n3 == 0 ? 0 : Integer.highestOneBit(n3);
        n3 = Math.max(1, n3);
        if (Log.isLoggable((String)TAG, (int)2) && n3 > 1) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Downsampling GIF, sampleSize: ");
            stringBuilder.append(n3);
            stringBuilder.append(", target dimens: [");
            stringBuilder.append(n);
            stringBuilder.append("x");
            stringBuilder.append(n2);
            stringBuilder.append("], actual dimens: [");
            stringBuilder.append(gifHeader.getWidth());
            stringBuilder.append("x");
            stringBuilder.append(gifHeader.getHeight());
            stringBuilder.append("]");
            Log.v((String)TAG, (String)stringBuilder.toString());
        }
        return n3;
    }

    public GifDrawableResource decode(ByteBuffer object, int n, int n2, Options options) {
        GifHeaderParser gifHeaderParser = this.parserPool.obtain((ByteBuffer)object);
        try {
            object = this.decode((ByteBuffer)object, n, n2, gifHeaderParser, options);
            return object;
        }
        finally {
            this.parserPool.release(gifHeaderParser);
        }
    }

    @Override
    public boolean handles(ByteBuffer byteBuffer, Options options) throws IOException {
        boolean bl = options.get(GifOptions.DISABLE_ANIMATION) == false && ImageHeaderParserUtils.getType(this.parsers, byteBuffer) == ImageHeaderParser.ImageType.GIF;
        return bl;
    }

    static class GifDecoderFactory {
        GifDecoderFactory() {
        }

        GifDecoder build(GifDecoder.BitmapProvider bitmapProvider, GifHeader gifHeader, ByteBuffer byteBuffer, int n) {
            return new StandardGifDecoder(bitmapProvider, gifHeader, byteBuffer, n);
        }
    }

    static class GifHeaderParserPool {
        private final Queue<GifHeaderParser> pool = Util.createQueue(0);

        GifHeaderParserPool() {
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        GifHeaderParser obtain(ByteBuffer object) {
            synchronized (this) {
                GifHeaderParser gifHeaderParser;
                GifHeaderParser gifHeaderParser2 = gifHeaderParser = this.pool.poll();
                if (gifHeaderParser != null) return gifHeaderParser2.setData((ByteBuffer)object);
                gifHeaderParser2 = new GifHeaderParser();
                return gifHeaderParser2.setData((ByteBuffer)object);
            }
        }

        void release(GifHeaderParser gifHeaderParser) {
            synchronized (this) {
                gifHeaderParser.clear();
                this.pool.offer(gifHeaderParser);
                return;
            }
        }
    }
}

