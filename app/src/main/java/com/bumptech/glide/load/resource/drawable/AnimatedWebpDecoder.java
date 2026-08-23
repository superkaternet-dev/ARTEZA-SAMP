/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Bitmap$Config
 *  android.graphics.ImageDecoder
 *  android.graphics.ImageDecoder$OnHeaderDecodedListener
 *  android.graphics.ImageDecoder$Source
 *  android.graphics.drawable.AnimatedImageDrawable
 *  android.graphics.drawable.Drawable
 */
package com.bumptech.glide.load.resource.drawable;

import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.drawable.AnimatedImageDrawable;
import android.graphics.drawable.Drawable;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.ImageHeaderParserUtils;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.resource.DefaultOnHeaderDecodedListener;
import com.bumptech.glide.util.ByteBufferUtil;
import com.bumptech.glide.util.Util;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

public final class AnimatedWebpDecoder {
    private final ArrayPool arrayPool;
    private final List<ImageHeaderParser> imageHeaderParsers;

    private AnimatedWebpDecoder(List<ImageHeaderParser> list, ArrayPool arrayPool) {
        this.imageHeaderParsers = list;
        this.arrayPool = arrayPool;
    }

    public static ResourceDecoder<ByteBuffer, Drawable> byteBufferDecoder(List<ImageHeaderParser> list, ArrayPool arrayPool) {
        return new ByteBufferAnimatedWebpDecoder(new AnimatedWebpDecoder(list, arrayPool));
    }

    private boolean isHandled(ImageHeaderParser.ImageType imageType) {
        boolean bl = imageType == ImageHeaderParser.ImageType.ANIMATED_WEBP;
        return bl;
    }

    public static ResourceDecoder<InputStream, Drawable> streamDecoder(List<ImageHeaderParser> list, ArrayPool arrayPool) {
        return new StreamAnimatedWebpDecoder(new AnimatedWebpDecoder(list, arrayPool));
    }

    Resource<Drawable> decode(ImageDecoder.Source source, int n, int n2, Options object) throws IOException {
        if ((source = ImageDecoder.decodeDrawable((ImageDecoder.Source)source, (ImageDecoder.OnHeaderDecodedListener)new DefaultOnHeaderDecodedListener(n, n2, (Options)object))) instanceof AnimatedImageDrawable) {
            return new AnimatedImageDrawableResource((AnimatedImageDrawable)source);
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("Received unexpected drawable type for animated webp, failing: ");
        ((StringBuilder)object).append(source);
        throw new IOException(((StringBuilder)object).toString());
    }

    boolean handles(InputStream inputStream) throws IOException {
        return this.isHandled(ImageHeaderParserUtils.getType(this.imageHeaderParsers, inputStream, this.arrayPool));
    }

    boolean handles(ByteBuffer byteBuffer) throws IOException {
        return this.isHandled(ImageHeaderParserUtils.getType(this.imageHeaderParsers, byteBuffer));
    }

    private static final class AnimatedImageDrawableResource
    implements Resource<Drawable> {
        private static final int ESTIMATED_NUMBER_OF_FRAMES = 2;
        private final AnimatedImageDrawable imageDrawable;

        AnimatedImageDrawableResource(AnimatedImageDrawable animatedImageDrawable) {
            this.imageDrawable = animatedImageDrawable;
        }

        @Override
        public AnimatedImageDrawable get() {
            return this.imageDrawable;
        }

        @Override
        public Class<Drawable> getResourceClass() {
            return Drawable.class;
        }

        @Override
        public int getSize() {
            return this.imageDrawable.getIntrinsicWidth() * this.imageDrawable.getIntrinsicHeight() * Util.getBytesPerPixel(Bitmap.Config.ARGB_8888) * 2;
        }

        @Override
        public void recycle() {
            this.imageDrawable.stop();
            this.imageDrawable.clearAnimationCallbacks();
        }
    }

    private static final class ByteBufferAnimatedWebpDecoder
    implements ResourceDecoder<ByteBuffer, Drawable> {
        private final AnimatedWebpDecoder delegate;

        ByteBufferAnimatedWebpDecoder(AnimatedWebpDecoder animatedWebpDecoder) {
            this.delegate = animatedWebpDecoder;
        }

        @Override
        public Resource<Drawable> decode(ByteBuffer byteBuffer, int n, int n2, Options options) throws IOException {
            byteBuffer = ImageDecoder.createSource((ByteBuffer)byteBuffer);
            return this.delegate.decode((ImageDecoder.Source)byteBuffer, n, n2, options);
        }

        @Override
        public boolean handles(ByteBuffer byteBuffer, Options options) throws IOException {
            return this.delegate.handles(byteBuffer);
        }
    }

    private static final class StreamAnimatedWebpDecoder
    implements ResourceDecoder<InputStream, Drawable> {
        private final AnimatedWebpDecoder delegate;

        StreamAnimatedWebpDecoder(AnimatedWebpDecoder animatedWebpDecoder) {
            this.delegate = animatedWebpDecoder;
        }

        @Override
        public Resource<Drawable> decode(InputStream inputStream, int n, int n2, Options options) throws IOException {
            inputStream = ImageDecoder.createSource((ByteBuffer)ByteBufferUtil.fromStream(inputStream));
            return this.delegate.decode((ImageDecoder.Source)inputStream, n, n2, options);
        }

        @Override
        public boolean handles(InputStream inputStream, Options options) throws IOException {
            return this.delegate.handles(inputStream);
        }
    }
}

