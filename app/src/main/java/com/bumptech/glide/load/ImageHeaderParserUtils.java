/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.load;

import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.data.ParcelFileDescriptorRewinder;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.resource.bitmap.RecyclableBufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

public final class ImageHeaderParserUtils {
    private static final int MARK_READ_LIMIT = 0x500000;

    private ImageHeaderParserUtils() {
    }

    public static int getOrientation(List<ImageHeaderParser> list, ParcelFileDescriptorRewinder parcelFileDescriptorRewinder, ArrayPool arrayPool) throws IOException {
        return ImageHeaderParserUtils.getOrientationInternal(list, new OrientationReader(parcelFileDescriptorRewinder, arrayPool){
            final ArrayPool val$byteArrayPool;
            final ParcelFileDescriptorRewinder val$parcelFileDescriptorRewinder;
            {
                this.val$parcelFileDescriptorRewinder = parcelFileDescriptorRewinder;
                this.val$byteArrayPool = arrayPool;
            }

            @Override
            public int getOrientation(ImageHeaderParser imageHeaderParser) throws IOException {
                FileInputStream fileInputStream;
                InputStream inputStream;
                InputStream inputStream2 = inputStream = null;
                inputStream2 = inputStream;
                inputStream2 = inputStream;
                try {
                    fileInputStream = new FileInputStream(this.val$parcelFileDescriptorRewinder.rewindAndGet().getFileDescriptor());
                    inputStream2 = inputStream;
                }
                catch (Throwable throwable) {
                    if (inputStream2 != null) {
                        try {
                            inputStream2.close();
                        }
                        catch (IOException iOException) {
                            // empty catch block
                        }
                    }
                    this.val$parcelFileDescriptorRewinder.rewindAndGet();
                    throw throwable;
                }
                RecyclableBufferedInputStream recyclableBufferedInputStream = new RecyclableBufferedInputStream(fileInputStream, this.val$byteArrayPool);
                inputStream2 = recyclableBufferedInputStream;
                int n = imageHeaderParser.getOrientation(recyclableBufferedInputStream, this.val$byteArrayPool);
                try {
                    ((InputStream)recyclableBufferedInputStream).close();
                }
                catch (IOException iOException) {
                    // empty catch block
                }
                this.val$parcelFileDescriptorRewinder.rewindAndGet();
                return n;
            }
        });
    }

    public static int getOrientation(List<ImageHeaderParser> list, InputStream inputStream, ArrayPool arrayPool) throws IOException {
        if (inputStream == null) {
            return -1;
        }
        InputStream inputStream2 = inputStream;
        if (!inputStream.markSupported()) {
            inputStream2 = new RecyclableBufferedInputStream(inputStream, arrayPool);
        }
        inputStream2.mark(0x500000);
        return ImageHeaderParserUtils.getOrientationInternal(list, new OrientationReader(inputStream2, arrayPool){
            final ArrayPool val$byteArrayPool;
            final InputStream val$finalIs;
            {
                this.val$finalIs = inputStream;
                this.val$byteArrayPool = arrayPool;
            }

            @Override
            public int getOrientation(ImageHeaderParser imageHeaderParser) throws IOException {
                try {
                    int n = imageHeaderParser.getOrientation(this.val$finalIs, this.val$byteArrayPool);
                    return n;
                }
                finally {
                    this.val$finalIs.reset();
                }
            }
        });
    }

    public static int getOrientation(List<ImageHeaderParser> list, ByteBuffer byteBuffer, ArrayPool arrayPool) throws IOException {
        if (byteBuffer == null) {
            return -1;
        }
        return ImageHeaderParserUtils.getOrientationInternal(list, new OrientationReader(byteBuffer, arrayPool){
            final ArrayPool val$arrayPool;
            final ByteBuffer val$buffer;
            {
                this.val$buffer = byteBuffer;
                this.val$arrayPool = arrayPool;
            }

            @Override
            public int getOrientation(ImageHeaderParser imageHeaderParser) throws IOException {
                return imageHeaderParser.getOrientation(this.val$buffer, this.val$arrayPool);
            }
        });
    }

    private static int getOrientationInternal(List<ImageHeaderParser> list, OrientationReader orientationReader) throws IOException {
        int n = list.size();
        for (int i = 0; i < n; ++i) {
            int n2 = orientationReader.getOrientation(list.get(i));
            if (n2 == -1) continue;
            return n2;
        }
        return -1;
    }

    public static ImageHeaderParser.ImageType getType(List<ImageHeaderParser> list, ParcelFileDescriptorRewinder parcelFileDescriptorRewinder, ArrayPool arrayPool) throws IOException {
        return ImageHeaderParserUtils.getTypeInternal(list, new TypeReader(parcelFileDescriptorRewinder, arrayPool){
            final ArrayPool val$byteArrayPool;
            final ParcelFileDescriptorRewinder val$parcelFileDescriptorRewinder;
            {
                this.val$parcelFileDescriptorRewinder = parcelFileDescriptorRewinder;
                this.val$byteArrayPool = arrayPool;
            }

            @Override
            public ImageHeaderParser.ImageType getType(ImageHeaderParser object) throws IOException {
                FileInputStream fileInputStream;
                InputStream inputStream;
                InputStream inputStream2 = inputStream = null;
                inputStream2 = inputStream;
                inputStream2 = inputStream;
                try {
                    fileInputStream = new FileInputStream(this.val$parcelFileDescriptorRewinder.rewindAndGet().getFileDescriptor());
                    inputStream2 = inputStream;
                }
                catch (Throwable throwable) {
                    if (inputStream2 != null) {
                        try {
                            inputStream2.close();
                        }
                        catch (IOException iOException) {
                            // empty catch block
                        }
                    }
                    this.val$parcelFileDescriptorRewinder.rewindAndGet();
                    throw throwable;
                }
                RecyclableBufferedInputStream recyclableBufferedInputStream = new RecyclableBufferedInputStream(fileInputStream, this.val$byteArrayPool);
                inputStream2 = recyclableBufferedInputStream;
                object = object.getType(recyclableBufferedInputStream);
                try {
                    ((InputStream)recyclableBufferedInputStream).close();
                }
                catch (IOException iOException) {
                    // empty catch block
                }
                this.val$parcelFileDescriptorRewinder.rewindAndGet();
                return object;
            }
        });
    }

    public static ImageHeaderParser.ImageType getType(List<ImageHeaderParser> list, InputStream inputStream, ArrayPool arrayPool) throws IOException {
        if (inputStream == null) {
            return ImageHeaderParser.ImageType.UNKNOWN;
        }
        InputStream inputStream2 = inputStream;
        if (!inputStream.markSupported()) {
            inputStream2 = new RecyclableBufferedInputStream(inputStream, arrayPool);
        }
        inputStream2.mark(0x500000);
        return ImageHeaderParserUtils.getTypeInternal(list, new TypeReader(inputStream2){
            final InputStream val$finalIs;
            {
                this.val$finalIs = inputStream;
            }

            @Override
            public ImageHeaderParser.ImageType getType(ImageHeaderParser object) throws IOException {
                try {
                    object = object.getType(this.val$finalIs);
                    return object;
                }
                finally {
                    this.val$finalIs.reset();
                }
            }
        });
    }

    public static ImageHeaderParser.ImageType getType(List<ImageHeaderParser> list, ByteBuffer byteBuffer) throws IOException {
        if (byteBuffer == null) {
            return ImageHeaderParser.ImageType.UNKNOWN;
        }
        return ImageHeaderParserUtils.getTypeInternal(list, new TypeReader(byteBuffer){
            final ByteBuffer val$buffer;
            {
                this.val$buffer = byteBuffer;
            }

            @Override
            public ImageHeaderParser.ImageType getType(ImageHeaderParser imageHeaderParser) throws IOException {
                return imageHeaderParser.getType(this.val$buffer);
            }
        });
    }

    private static ImageHeaderParser.ImageType getTypeInternal(List<ImageHeaderParser> list, TypeReader typeReader) throws IOException {
        int n = list.size();
        for (int i = 0; i < n; ++i) {
            ImageHeaderParser.ImageType imageType = typeReader.getType(list.get(i));
            if (imageType == ImageHeaderParser.ImageType.UNKNOWN) continue;
            return imageType;
        }
        return ImageHeaderParser.ImageType.UNKNOWN;
    }

    private static interface OrientationReader {
        public int getOrientation(ImageHeaderParser var1) throws IOException;
    }

    private static interface TypeReader {
        public ImageHeaderParser.ImageType getType(ImageHeaderParser var1) throws IOException;
    }
}

