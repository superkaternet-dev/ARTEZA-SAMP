/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Bitmap
 *  android.graphics.BitmapFactory
 *  android.graphics.BitmapFactory$Options
 *  android.os.ParcelFileDescriptor
 */
package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.ParcelFileDescriptor;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.ImageHeaderParserUtils;
import com.bumptech.glide.load.data.InputStreamRewinder;
import com.bumptech.glide.load.data.ParcelFileDescriptorRewinder;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.resource.bitmap.RecyclableBufferedInputStream;
import com.bumptech.glide.util.ByteBufferUtil;
import com.bumptech.glide.util.Preconditions;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

interface ImageReader {
    public Bitmap decodeBitmap(BitmapFactory.Options var1) throws IOException;

    public int getImageOrientation() throws IOException;

    public ImageHeaderParser.ImageType getImageType() throws IOException;

    public void stopGrowingBuffers();

    public static final class ByteArrayReader
    implements ImageReader {
        private final ArrayPool byteArrayPool;
        private final byte[] bytes;
        private final List<ImageHeaderParser> parsers;

        ByteArrayReader(byte[] byArray, List<ImageHeaderParser> list, ArrayPool arrayPool) {
            this.bytes = byArray;
            this.parsers = list;
            this.byteArrayPool = arrayPool;
        }

        @Override
        public Bitmap decodeBitmap(BitmapFactory.Options options) {
            byte[] byArray = this.bytes;
            return BitmapFactory.decodeByteArray((byte[])byArray, (int)0, (int)byArray.length, (BitmapFactory.Options)options);
        }

        @Override
        public int getImageOrientation() throws IOException {
            return ImageHeaderParserUtils.getOrientation(this.parsers, ByteBuffer.wrap(this.bytes), this.byteArrayPool);
        }

        @Override
        public ImageHeaderParser.ImageType getImageType() throws IOException {
            return ImageHeaderParserUtils.getType(this.parsers, ByteBuffer.wrap(this.bytes));
        }

        @Override
        public void stopGrowingBuffers() {
        }
    }

    public static final class ByteBufferReader
    implements ImageReader {
        private final ByteBuffer buffer;
        private final ArrayPool byteArrayPool;
        private final List<ImageHeaderParser> parsers;

        ByteBufferReader(ByteBuffer byteBuffer, List<ImageHeaderParser> list, ArrayPool arrayPool) {
            this.buffer = byteBuffer;
            this.parsers = list;
            this.byteArrayPool = arrayPool;
        }

        private InputStream stream() {
            return ByteBufferUtil.toStream(ByteBufferUtil.rewind(this.buffer));
        }

        @Override
        public Bitmap decodeBitmap(BitmapFactory.Options options) {
            return BitmapFactory.decodeStream((InputStream)this.stream(), null, (BitmapFactory.Options)options);
        }

        @Override
        public int getImageOrientation() throws IOException {
            return ImageHeaderParserUtils.getOrientation(this.parsers, ByteBufferUtil.rewind(this.buffer), this.byteArrayPool);
        }

        @Override
        public ImageHeaderParser.ImageType getImageType() throws IOException {
            return ImageHeaderParserUtils.getType(this.parsers, ByteBufferUtil.rewind(this.buffer));
        }

        @Override
        public void stopGrowingBuffers() {
        }
    }

    public static final class FileReader
    implements ImageReader {
        private final ArrayPool byteArrayPool;
        private final File file;
        private final List<ImageHeaderParser> parsers;

        FileReader(File file, List<ImageHeaderParser> list, ArrayPool arrayPool) {
            this.file = file;
            this.parsers = list;
            this.byteArrayPool = arrayPool;
        }

        @Override
        public Bitmap decodeBitmap(BitmapFactory.Options options) throws FileNotFoundException {
            FileInputStream fileInputStream;
            InputStream inputStream;
            InputStream inputStream2 = inputStream = null;
            inputStream2 = inputStream;
            inputStream2 = inputStream;
            try {
                fileInputStream = new FileInputStream(this.file);
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
                throw throwable;
            }
            RecyclableBufferedInputStream recyclableBufferedInputStream = new RecyclableBufferedInputStream(fileInputStream, this.byteArrayPool);
            inputStream2 = inputStream = recyclableBufferedInputStream;
            options = BitmapFactory.decodeStream((InputStream)inputStream, null, (BitmapFactory.Options)options);
            try {
                inputStream.close();
            }
            catch (IOException iOException) {
                // empty catch block
            }
            return options;
        }

        @Override
        public int getImageOrientation() throws IOException {
            FileInputStream fileInputStream;
            InputStream inputStream;
            InputStream inputStream2 = inputStream = null;
            inputStream2 = inputStream;
            inputStream2 = inputStream;
            try {
                fileInputStream = new FileInputStream(this.file);
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
                throw throwable;
            }
            RecyclableBufferedInputStream recyclableBufferedInputStream = new RecyclableBufferedInputStream(fileInputStream, this.byteArrayPool);
            inputStream2 = inputStream = recyclableBufferedInputStream;
            int n = ImageHeaderParserUtils.getOrientation(this.parsers, inputStream, this.byteArrayPool);
            try {
                inputStream.close();
            }
            catch (IOException iOException) {
                // empty catch block
            }
            return n;
        }

        @Override
        public ImageHeaderParser.ImageType getImageType() throws IOException {
            FileInputStream fileInputStream;
            Object object;
            InputStream inputStream = object = null;
            inputStream = object;
            inputStream = object;
            try {
                fileInputStream = new FileInputStream(this.file);
                inputStream = object;
            }
            catch (Throwable throwable) {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    }
                    catch (IOException iOException) {
                        // empty catch block
                    }
                }
                throw throwable;
            }
            RecyclableBufferedInputStream recyclableBufferedInputStream = new RecyclableBufferedInputStream(fileInputStream, this.byteArrayPool);
            inputStream = recyclableBufferedInputStream;
            object = ImageHeaderParserUtils.getType(this.parsers, recyclableBufferedInputStream, this.byteArrayPool);
            try {
                ((InputStream)recyclableBufferedInputStream).close();
            }
            catch (IOException iOException) {
                // empty catch block
            }
            return object;
        }

        @Override
        public void stopGrowingBuffers() {
        }
    }

    public static final class InputStreamImageReader
    implements ImageReader {
        private final ArrayPool byteArrayPool;
        private final InputStreamRewinder dataRewinder;
        private final List<ImageHeaderParser> parsers;

        InputStreamImageReader(InputStream inputStream, List<ImageHeaderParser> list, ArrayPool arrayPool) {
            this.byteArrayPool = Preconditions.checkNotNull(arrayPool);
            this.parsers = Preconditions.checkNotNull(list);
            this.dataRewinder = new InputStreamRewinder(inputStream, arrayPool);
        }

        @Override
        public Bitmap decodeBitmap(BitmapFactory.Options options) throws IOException {
            return BitmapFactory.decodeStream((InputStream)this.dataRewinder.rewindAndGet(), null, (BitmapFactory.Options)options);
        }

        @Override
        public int getImageOrientation() throws IOException {
            return ImageHeaderParserUtils.getOrientation(this.parsers, this.dataRewinder.rewindAndGet(), this.byteArrayPool);
        }

        @Override
        public ImageHeaderParser.ImageType getImageType() throws IOException {
            return ImageHeaderParserUtils.getType(this.parsers, this.dataRewinder.rewindAndGet(), this.byteArrayPool);
        }

        @Override
        public void stopGrowingBuffers() {
            this.dataRewinder.fixMarkLimits();
        }
    }

    public static final class ParcelFileDescriptorImageReader
    implements ImageReader {
        private final ArrayPool byteArrayPool;
        private final ParcelFileDescriptorRewinder dataRewinder;
        private final List<ImageHeaderParser> parsers;

        ParcelFileDescriptorImageReader(ParcelFileDescriptor parcelFileDescriptor, List<ImageHeaderParser> list, ArrayPool arrayPool) {
            this.byteArrayPool = Preconditions.checkNotNull(arrayPool);
            this.parsers = Preconditions.checkNotNull(list);
            this.dataRewinder = new ParcelFileDescriptorRewinder(parcelFileDescriptor);
        }

        @Override
        public Bitmap decodeBitmap(BitmapFactory.Options options) throws IOException {
            return BitmapFactory.decodeFileDescriptor((FileDescriptor)this.dataRewinder.rewindAndGet().getFileDescriptor(), null, (BitmapFactory.Options)options);
        }

        @Override
        public int getImageOrientation() throws IOException {
            return ImageHeaderParserUtils.getOrientation(this.parsers, this.dataRewinder, this.byteArrayPool);
        }

        @Override
        public ImageHeaderParser.ImageType getImageType() throws IOException {
            return ImageHeaderParserUtils.getType(this.parsers, this.dataRewinder, this.byteArrayPool);
        }

        @Override
        public void stopGrowingBuffers() {
        }
    }
}

