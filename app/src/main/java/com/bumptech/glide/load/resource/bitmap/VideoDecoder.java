/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.res.AssetFileDescriptor
 *  android.graphics.Bitmap
 *  android.media.MediaDataSource
 *  android.media.MediaMetadataRetriever
 *  android.os.Build$VERSION
 *  android.os.ParcelFileDescriptor
 *  android.util.Log
 */
package com.bumptech.glide.load.resource.bitmap;

import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.MediaDataSource;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

public class VideoDecoder<T>
implements ResourceDecoder<T, Bitmap> {
    private static final MediaMetadataRetrieverFactory DEFAULT_FACTORY;
    public static final long DEFAULT_FRAME = -1L;
    static final int DEFAULT_FRAME_OPTION = 2;
    public static final Option<Integer> FRAME_OPTION;
    private static final String TAG = "VideoDecoder";
    public static final Option<Long> TARGET_FRAME;
    private final BitmapPool bitmapPool;
    private final MediaMetadataRetrieverFactory factory;
    private final MediaMetadataRetrieverInitializer<T> initializer;

    static {
        TARGET_FRAME = Option.disk("com.bumptech.glide.load.resource.bitmap.VideoBitmapDecode.TargetFrame", -1L, new Option.CacheKeyUpdater<Long>(){
            private final ByteBuffer buffer = ByteBuffer.allocate(8);

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public void update(byte[] object, Long l, MessageDigest messageDigest) {
                messageDigest.update((byte[])object);
                object = this.buffer;
                synchronized (object) {
                    this.buffer.position(0);
                    messageDigest.update(this.buffer.putLong(l).array());
                    return;
                }
            }
        });
        FRAME_OPTION = Option.disk("com.bumptech.glide.load.resource.bitmap.VideoBitmapDecode.FrameOption", 2, new Option.CacheKeyUpdater<Integer>(){
            private final ByteBuffer buffer = ByteBuffer.allocate(4);

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public void update(byte[] object, Integer n, MessageDigest messageDigest) {
                if (n == null) {
                    return;
                }
                messageDigest.update((byte[])object);
                object = this.buffer;
                synchronized (object) {
                    this.buffer.position(0);
                    messageDigest.update(this.buffer.putInt(n).array());
                    return;
                }
            }
        });
        DEFAULT_FACTORY = new MediaMetadataRetrieverFactory();
    }

    VideoDecoder(BitmapPool bitmapPool, MediaMetadataRetrieverInitializer<T> mediaMetadataRetrieverInitializer) {
        this(bitmapPool, mediaMetadataRetrieverInitializer, DEFAULT_FACTORY);
    }

    VideoDecoder(BitmapPool bitmapPool, MediaMetadataRetrieverInitializer<T> mediaMetadataRetrieverInitializer, MediaMetadataRetrieverFactory mediaMetadataRetrieverFactory) {
        this.bitmapPool = bitmapPool;
        this.initializer = mediaMetadataRetrieverInitializer;
        this.factory = mediaMetadataRetrieverFactory;
    }

    public static ResourceDecoder<AssetFileDescriptor, Bitmap> asset(BitmapPool bitmapPool) {
        return new VideoDecoder<AssetFileDescriptor>(bitmapPool, new AssetFileDescriptorInitializer());
    }

    public static ResourceDecoder<ByteBuffer, Bitmap> byteBuffer(BitmapPool bitmapPool) {
        return new VideoDecoder<ByteBuffer>(bitmapPool, new ByteBufferInitializer());
    }

    private static Bitmap decodeFrame(MediaMetadataRetriever mediaMetadataRetriever, long l, int n, int n2, int n3, DownsampleStrategy downsampleStrategy) {
        Bitmap bitmap;
        Bitmap bitmap2 = bitmap = null;
        if (Build.VERSION.SDK_INT >= 27) {
            bitmap2 = bitmap;
            if (n2 != Integer.MIN_VALUE) {
                bitmap2 = bitmap;
                if (n3 != Integer.MIN_VALUE) {
                    bitmap2 = bitmap;
                    if (downsampleStrategy != DownsampleStrategy.NONE) {
                        bitmap2 = VideoDecoder.decodeScaledFrame(mediaMetadataRetriever, l, n, n2, n3, downsampleStrategy);
                    }
                }
            }
        }
        downsampleStrategy = bitmap2;
        if (bitmap2 == null) {
            downsampleStrategy = VideoDecoder.decodeOriginalFrame(mediaMetadataRetriever, l, n);
        }
        if (downsampleStrategy != null) {
            return downsampleStrategy;
        }
        throw new VideoDecoderException();
    }

    private static Bitmap decodeOriginalFrame(MediaMetadataRetriever mediaMetadataRetriever, long l, int n) {
        return mediaMetadataRetriever.getFrameAtTime(l, n);
    }

    /*
     * WARNING - void declaration
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static Bitmap decodeScaledFrame(MediaMetadataRetriever mediaMetadataRetriever, long l, int n, int n2, int n3, DownsampleStrategy downsampleStrategy) {
        void var0_3;
        block7: {
            int n4;
            int n5;
            try {
                n5 = Integer.parseInt(mediaMetadataRetriever.extractMetadata(18));
                int n6 = Integer.parseInt(mediaMetadataRetriever.extractMetadata(19));
                n4 = Integer.parseInt(mediaMetadataRetriever.extractMetadata(24));
                if (n4 != 90 && n4 != 270) {
                    n4 = n5;
                    n5 = n6;
                } else {
                    n4 = n6;
                }
            }
            catch (Throwable throwable) {
                // empty catch block
                break block7;
            }
            try {
                float f = downsampleStrategy.getScaleFactor(n4, n5, n2, n3);
                return mediaMetadataRetriever.getScaledFrameAtTime(l, n, Math.round((float)n4 * f), Math.round((float)n5 * f));
            }
            catch (Throwable throwable) {}
        }
        if (!Log.isLoggable((String)TAG, (int)3)) return null;
        Log.d((String)TAG, (String)"Exception trying to decode a scaled frame on oreo+, falling back to a fullsize frame", (Throwable)var0_3);
        return null;
    }

    public static ResourceDecoder<ParcelFileDescriptor, Bitmap> parcel(BitmapPool bitmapPool) {
        return new VideoDecoder<ParcelFileDescriptor>(bitmapPool, new ParcelFileDescriptorInitializer());
    }

    @Override
    public Resource<Bitmap> decode(T object, int n, int n2, Options object2) throws IOException {
        long l = ((Options)object2).get(TARGET_FRAME);
        if (l < 0L && l != -1L) {
            object = new StringBuilder();
            ((StringBuilder)object).append("Requested frame must be non-negative, or DEFAULT_FRAME, given: ");
            ((StringBuilder)object).append(l);
            throw new IllegalArgumentException(((StringBuilder)object).toString());
        }
        Integer n3 = ((Options)object2).get(FRAME_OPTION);
        if (n3 == null) {
            n3 = 2;
        }
        if ((object2 = ((Options)object2).get(DownsampleStrategy.OPTION)) == null) {
            object2 = DownsampleStrategy.DEFAULT;
        }
        MediaMetadataRetriever mediaMetadataRetriever = this.factory.build();
        try {
            this.initializer.initialize(mediaMetadataRetriever, object);
            object = VideoDecoder.decodeFrame(mediaMetadataRetriever, l, n3, n, n2, (DownsampleStrategy)object2);
            return BitmapResource.obtain((Bitmap)object, this.bitmapPool);
        }
        finally {
            if (Build.VERSION.SDK_INT >= 29) {
                mediaMetadataRetriever.close();
            } else {
                mediaMetadataRetriever.release();
            }
        }
    }

    @Override
    public boolean handles(T t, Options options) {
        return true;
    }

    private static final class AssetFileDescriptorInitializer
    implements MediaMetadataRetrieverInitializer<AssetFileDescriptor> {
        private AssetFileDescriptorInitializer() {
        }

        @Override
        public void initialize(MediaMetadataRetriever mediaMetadataRetriever, AssetFileDescriptor assetFileDescriptor) {
            mediaMetadataRetriever.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
        }
    }

    static final class ByteBufferInitializer
    implements MediaMetadataRetrieverInitializer<ByteBuffer> {
        ByteBufferInitializer() {
        }

        @Override
        public void initialize(MediaMetadataRetriever mediaMetadataRetriever, ByteBuffer byteBuffer) {
            mediaMetadataRetriever.setDataSource(new MediaDataSource(this, byteBuffer){
                final ByteBufferInitializer this$0;
                final ByteBuffer val$data;
                {
                    this.this$0 = byteBufferInitializer;
                    this.val$data = byteBuffer;
                }

                public void close() {
                }

                public long getSize() {
                    return this.val$data.limit();
                }

                public int readAt(long l, byte[] byArray, int n, int n2) {
                    if (l >= (long)this.val$data.limit()) {
                        return -1;
                    }
                    this.val$data.position((int)l);
                    n2 = Math.min(n2, this.val$data.remaining());
                    this.val$data.get(byArray, n, n2);
                    return n2;
                }
            });
        }
    }

    static class MediaMetadataRetrieverFactory {
        MediaMetadataRetrieverFactory() {
        }

        public MediaMetadataRetriever build() {
            return new MediaMetadataRetriever();
        }
    }

    static interface MediaMetadataRetrieverInitializer<T> {
        public void initialize(MediaMetadataRetriever var1, T var2);
    }

    static final class ParcelFileDescriptorInitializer
    implements MediaMetadataRetrieverInitializer<ParcelFileDescriptor> {
        ParcelFileDescriptorInitializer() {
        }

        @Override
        public void initialize(MediaMetadataRetriever mediaMetadataRetriever, ParcelFileDescriptor parcelFileDescriptor) {
            mediaMetadataRetriever.setDataSource(parcelFileDescriptor.getFileDescriptor());
        }
    }

    private static final class VideoDecoderException
    extends RuntimeException {
        private static final long serialVersionUID = -2556382523004027815L;

        VideoDecoderException() {
            super("MediaMetadataRetriever failed to retrieve a frame without throwing, check the adb logs for .*MetadataRetriever.* prior to this exception for details");
        }
    }
}

