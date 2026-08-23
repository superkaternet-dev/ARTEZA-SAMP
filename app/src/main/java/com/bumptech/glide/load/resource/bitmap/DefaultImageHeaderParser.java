/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package com.bumptech.glide.load.resource.bitmap;

import android.util.Log;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.util.Preconditions;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

public final class DefaultImageHeaderParser
implements ImageHeaderParser {
    private static final int AVIF_BRAND = 1635150182;
    private static final int AVIS_BRAND = 1635150195;
    private static final int[] BYTES_PER_FORMAT;
    static final int EXIF_MAGIC_NUMBER = 65496;
    static final int EXIF_SEGMENT_TYPE = 225;
    private static final int FTYP_HEADER = 1718909296;
    private static final int GIF_HEADER = 4671814;
    private static final int INTEL_TIFF_MAGIC_NUMBER = 18761;
    private static final String JPEG_EXIF_SEGMENT_PREAMBLE = "Exif\u0000\u0000";
    static final byte[] JPEG_EXIF_SEGMENT_PREAMBLE_BYTES;
    private static final int MARKER_EOI = 217;
    private static final int MOTOROLA_TIFF_MAGIC_NUMBER = 19789;
    private static final int ORIENTATION_TAG_TYPE = 274;
    private static final int PNG_HEADER = -1991225785;
    private static final int RIFF_HEADER = 1380533830;
    private static final int SEGMENT_SOS = 218;
    static final int SEGMENT_START_ID = 255;
    private static final String TAG = "DfltImageHeaderParser";
    private static final int VP8_HEADER = 1448097792;
    private static final int VP8_HEADER_MASK = -256;
    private static final int VP8_HEADER_TYPE_EXTENDED = 88;
    private static final int VP8_HEADER_TYPE_LOSSLESS = 76;
    private static final int VP8_HEADER_TYPE_MASK = 255;
    private static final int WEBP_EXTENDED_ALPHA_FLAG = 16;
    private static final int WEBP_EXTENDED_ANIMATION_FLAG = 2;
    private static final int WEBP_HEADER = 1464156752;
    private static final int WEBP_LOSSLESS_ALPHA_FLAG = 8;

    static {
        JPEG_EXIF_SEGMENT_PREAMBLE_BYTES = JPEG_EXIF_SEGMENT_PREAMBLE.getBytes(Charset.forName("UTF-8"));
        BYTES_PER_FORMAT = new int[]{0, 1, 1, 2, 4, 8, 1, 1, 2, 4, 8, 4, 8};
    }

    private static int calcTagOffset(int n, int n2) {
        return n + 2 + n2 * 12;
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private int getOrientation(Reader object, ArrayPool arrayPool) throws IOException {
        byte[] byArray;
        int n;
        try {
            n = object.getUInt16();
            boolean bl = DefaultImageHeaderParser.handles(n);
            if (!bl) {
                if (!Log.isLoggable((String)TAG, (int)3)) return -1;
                object = new StringBuilder();
                ((StringBuilder)object).append("Parser doesn't handle magic number: ");
                ((StringBuilder)object).append(n);
                Log.d((String)TAG, (String)((StringBuilder)object).toString());
                return -1;
            }
            n = this.moveToExifSegmentAndGetLength((Reader)object);
            if (n == -1) {
                if (!Log.isLoggable((String)TAG, (int)3)) return -1;
                Log.d((String)TAG, (String)"Failed to parse exif segment length, or exif segment not found");
                return -1;
            }
            byArray = arrayPool.get(n, byte[].class);
        }
        catch (Reader.EndOfFileException endOfFileException) {
            return -1;
        }
        try {
            n = this.parseExifSegment((Reader)object, byArray, n);
            return n;
        }
        finally {
            arrayPool.put(byArray);
        }
    }

    /*
     * Unable to fully structure code
     * Could not resolve type clashes
     */
    private ImageHeaderParser.ImageType getType(Reader var1_1) throws IOException {
        block22: {
            block21: {
                block20: {
                    block19: {
                        var2_4 = var1_1.getUInt16();
                        if (var2_4 != 65496) ** GOTO lbl8
                        {
                            catch (Reader.EndOfFileException var1_3) {
                                return ImageHeaderParser.ImageType.UNKNOWN;
                            }
                        }
                        return ImageHeaderParser.ImageType.JPEG;
lbl8:
                        // 1 sources

                        if ((var2_4 = var2_4 << 8 | var1_1.getUInt8()) != 4671814) ** GOTO lbl11
                        return ImageHeaderParser.ImageType.GIF;
lbl11:
                        // 1 sources

                        if ((var2_4 = var2_4 << 8 | var1_1.getUInt8()) != -1991225785) break block19;
                        var1_1.skip(21L);
                        try {
                            var1_1 = var1_1.getUInt8() >= 3 ? ImageHeaderParser.ImageType.PNG_A : ImageHeaderParser.ImageType.PNG;
                            return var1_1;
                        }
                        catch (Reader.EndOfFileException var1_2) {
                            return ImageHeaderParser.ImageType.PNG;
                        }
                    }
                    if (var2_4 != 1380533830) {
                        var1_1 = this.sniffAvif(var1_1, var2_4) != false ? ImageHeaderParser.ImageType.AVIF : ImageHeaderParser.ImageType.UNKNOWN;
                        return var1_1;
                    }
                    var1_1.skip(4L);
                    if ((var1_1.getUInt16() << 16 | var1_1.getUInt16()) != 1464156752) {
                        return ImageHeaderParser.ImageType.UNKNOWN;
                    }
                    var2_4 = var1_1.getUInt16() << 16 | var1_1.getUInt16();
                    if ((var2_4 & -256) == 1448097792) break block20;
                    return ImageHeaderParser.ImageType.UNKNOWN;
                }
                if ((var2_4 & 255) != 88) break block22;
                var1_1.skip(4L);
                var2_4 = var1_1.getUInt8();
                if ((var2_4 & 2) == 0) break block21;
                return ImageHeaderParser.ImageType.ANIMATED_WEBP;
            }
            if ((var2_4 & 16) == 0) ** GOTO lbl48
            return ImageHeaderParser.ImageType.WEBP_A;
lbl48:
            // 1 sources

            return ImageHeaderParser.ImageType.WEBP;
        }
        if ((var2_4 & 255) == 76) {
            var1_1.skip(4L);
            var1_1 = (var1_1.getUInt8() & 8) != 0 ? ImageHeaderParser.ImageType.WEBP_A : ImageHeaderParser.ImageType.WEBP;
            return var1_1;
        }
        var1_1 = ImageHeaderParser.ImageType.WEBP;
        return var1_1;
    }

    private static boolean handles(int n) {
        boolean bl = (n & 0xFFD8) == 65496 || n == 19789 || n == 18761;
        return bl;
    }

    private boolean hasJpegExifPreamble(byte[] byArray, int n) {
        boolean bl = byArray != null && n > JPEG_EXIF_SEGMENT_PREAMBLE_BYTES.length;
        boolean bl2 = bl;
        if (bl) {
            n = 0;
            while (true) {
                byte[] byArray2 = JPEG_EXIF_SEGMENT_PREAMBLE_BYTES;
                bl2 = bl;
                if (n >= byArray2.length) break;
                if (byArray[n] != byArray2[n]) {
                    bl2 = false;
                    break;
                }
                ++n;
            }
        }
        return bl2;
    }

    private int moveToExifSegmentAndGetLength(Reader object) throws IOException {
        int n;
        block7: {
            short s;
            long l;
            do {
                if ((s = object.getUInt8()) != 255) {
                    if (Log.isLoggable((String)TAG, (int)3)) {
                        object = new StringBuilder();
                        ((StringBuilder)object).append("Unknown segmentId=");
                        ((StringBuilder)object).append(s);
                        Log.d((String)TAG, (String)((StringBuilder)object).toString());
                    }
                    return -1;
                }
                s = object.getUInt8();
                if (s == 218) {
                    return -1;
                }
                if (s == 217) {
                    if (Log.isLoggable((String)TAG, (int)3)) {
                        Log.d((String)TAG, (String)"Found MARKER_EOI in exif segment");
                    }
                    return -1;
                }
                n = object.getUInt16() - 2;
                if (s == 225) break block7;
            } while ((l = object.skip(n)) == (long)n);
            if (Log.isLoggable((String)TAG, (int)3)) {
                object = new StringBuilder();
                ((StringBuilder)object).append("Unable to skip enough data, type: ");
                ((StringBuilder)object).append(s);
                ((StringBuilder)object).append(", wanted to skip: ");
                ((StringBuilder)object).append(n);
                ((StringBuilder)object).append(", but actually skipped: ");
                ((StringBuilder)object).append(l);
                Log.d((String)TAG, (String)((StringBuilder)object).toString());
            }
            return -1;
        }
        return n;
    }

    private static int parseExifSegment(RandomAccessReader randomAccessReader) {
        Object object;
        int n;
        int n2;
        block11: {
            n2 = JPEG_EXIF_SEGMENT_PREAMBLE.length();
            n = randomAccessReader.getInt16(n2);
            switch (n) {
                default: {
                    if (!Log.isLoggable((String)TAG, (int)3)) break;
                    object = new StringBuilder();
                    ((StringBuilder)object).append("Unknown endianness = ");
                    ((StringBuilder)object).append(n);
                    Log.d((String)TAG, (String)((StringBuilder)object).toString());
                    break;
                }
                case 19789: {
                    object = ByteOrder.BIG_ENDIAN;
                    break block11;
                }
                case 18761: {
                    object = ByteOrder.LITTLE_ENDIAN;
                    break block11;
                }
            }
            object = ByteOrder.BIG_ENDIAN;
        }
        randomAccessReader.order((ByteOrder)object);
        int n3 = randomAccessReader.getInt32(n2 + 4) + n2;
        n2 = randomAccessReader.getInt16(n3);
        for (n = 0; n < n2; ++n) {
            int n4 = DefaultImageHeaderParser.calcTagOffset(n3, n);
            short s = randomAccessReader.getInt16(n4);
            if (s != 274) continue;
            int n5 = randomAccessReader.getInt16(n4 + 2);
            if (n5 >= 1 && n5 <= 12) {
                int n6 = randomAccessReader.getInt32(n4 + 4);
                if (n6 < 0) {
                    if (!Log.isLoggable((String)TAG, (int)3)) continue;
                    Log.d((String)TAG, (String)"Negative tiff component count");
                    continue;
                }
                if (Log.isLoggable((String)TAG, (int)3)) {
                    object = new StringBuilder();
                    ((StringBuilder)object).append("Got tagIndex=");
                    ((StringBuilder)object).append(n);
                    ((StringBuilder)object).append(" tagType=");
                    ((StringBuilder)object).append(s);
                    ((StringBuilder)object).append(" formatCode=");
                    ((StringBuilder)object).append(n5);
                    ((StringBuilder)object).append(" componentCount=");
                    ((StringBuilder)object).append(n6);
                    Log.d((String)TAG, (String)((StringBuilder)object).toString());
                }
                if ((n6 = BYTES_PER_FORMAT[n5] + n6) > 4) {
                    if (!Log.isLoggable((String)TAG, (int)3)) continue;
                    object = new StringBuilder();
                    ((StringBuilder)object).append("Got byte count > 4, not orientation, continuing, formatCode=");
                    ((StringBuilder)object).append(n5);
                    Log.d((String)TAG, (String)((StringBuilder)object).toString());
                    continue;
                }
                n5 = n4 + 8;
                if (n5 >= 0 && n5 <= randomAccessReader.length()) {
                    if (n6 >= 0 && n5 + n6 <= randomAccessReader.length()) {
                        return randomAccessReader.getInt16(n5);
                    }
                    if (!Log.isLoggable((String)TAG, (int)3)) continue;
                    object = new StringBuilder();
                    ((StringBuilder)object).append("Illegal number of bytes for TI tag data tagType=");
                    ((StringBuilder)object).append(s);
                    Log.d((String)TAG, (String)((StringBuilder)object).toString());
                    continue;
                }
                if (!Log.isLoggable((String)TAG, (int)3)) continue;
                object = new StringBuilder();
                ((StringBuilder)object).append("Illegal tagValueOffset=");
                ((StringBuilder)object).append(n5);
                ((StringBuilder)object).append(" tagType=");
                ((StringBuilder)object).append(s);
                Log.d((String)TAG, (String)((StringBuilder)object).toString());
                continue;
            }
            if (!Log.isLoggable((String)TAG, (int)3)) continue;
            object = new StringBuilder();
            ((StringBuilder)object).append("Got invalid format code = ");
            ((StringBuilder)object).append(n5);
            Log.d((String)TAG, (String)((StringBuilder)object).toString());
        }
        return -1;
    }

    private int parseExifSegment(Reader object, byte[] byArray, int n) throws IOException {
        int n2 = object.read(byArray, n);
        if (n2 != n) {
            if (Log.isLoggable((String)TAG, (int)3)) {
                object = new StringBuilder();
                ((StringBuilder)object).append("Unable to read exif segment data, length: ");
                ((StringBuilder)object).append(n);
                ((StringBuilder)object).append(", actually read: ");
                ((StringBuilder)object).append(n2);
                Log.d((String)TAG, (String)((StringBuilder)object).toString());
            }
            return -1;
        }
        if (this.hasJpegExifPreamble(byArray, n)) {
            return DefaultImageHeaderParser.parseExifSegment(new RandomAccessReader(byArray, n));
        }
        if (Log.isLoggable((String)TAG, (int)3)) {
            Log.d((String)TAG, (String)"Missing jpeg exif preamble");
        }
        return -1;
    }

    private boolean sniffAvif(Reader reader, int n) throws IOException {
        if ((reader.getUInt16() << 16 | reader.getUInt16()) != 1718909296) {
            return false;
        }
        int n2 = reader.getUInt16() << 16 | reader.getUInt16();
        if (n2 != 1635150182 && n2 != 1635150195) {
            reader.skip(4L);
            n2 = n - 16;
            if (n2 % 4 != 0) {
                return false;
            }
            for (n = 0; n < 5 && n2 > 0; ++n, n2 -= 4) {
                int n3 = reader.getUInt16() << 16 | reader.getUInt16();
                if (n3 != 1635150182 && n3 != 1635150195) {
                    continue;
                }
                return true;
            }
            return false;
        }
        return true;
    }

    @Override
    public int getOrientation(InputStream inputStream, ArrayPool arrayPool) throws IOException {
        return this.getOrientation(new StreamReader(Preconditions.checkNotNull(inputStream)), Preconditions.checkNotNull(arrayPool));
    }

    @Override
    public int getOrientation(ByteBuffer byteBuffer, ArrayPool arrayPool) throws IOException {
        return this.getOrientation(new ByteBufferReader(Preconditions.checkNotNull(byteBuffer)), Preconditions.checkNotNull(arrayPool));
    }

    @Override
    public ImageHeaderParser.ImageType getType(InputStream inputStream) throws IOException {
        return this.getType(new StreamReader(Preconditions.checkNotNull(inputStream)));
    }

    @Override
    public ImageHeaderParser.ImageType getType(ByteBuffer byteBuffer) throws IOException {
        return this.getType(new ByteBufferReader(Preconditions.checkNotNull(byteBuffer)));
    }

    private static final class ByteBufferReader
    implements Reader {
        private final ByteBuffer byteBuffer;

        ByteBufferReader(ByteBuffer byteBuffer) {
            this.byteBuffer = byteBuffer;
            byteBuffer.order(ByteOrder.BIG_ENDIAN);
        }

        @Override
        public int getUInt16() throws Reader.EndOfFileException {
            return this.getUInt8() << 8 | this.getUInt8();
        }

        @Override
        public short getUInt8() throws Reader.EndOfFileException {
            if (this.byteBuffer.remaining() >= 1) {
                return (short)(this.byteBuffer.get() & 0xFF);
            }
            throw new Reader.EndOfFileException();
        }

        @Override
        public int read(byte[] byArray, int n) {
            if ((n = Math.min(n, this.byteBuffer.remaining())) == 0) {
                return -1;
            }
            this.byteBuffer.get(byArray, 0, n);
            return n;
        }

        @Override
        public long skip(long l) {
            int n = (int)Math.min((long)this.byteBuffer.remaining(), l);
            ByteBuffer byteBuffer = this.byteBuffer;
            byteBuffer.position(byteBuffer.position() + n);
            return n;
        }
    }

    private static final class RandomAccessReader {
        private final ByteBuffer data;

        RandomAccessReader(byte[] byArray, int n) {
            this.data = (ByteBuffer)ByteBuffer.wrap(byArray).order(ByteOrder.BIG_ENDIAN).limit(n);
        }

        private boolean isAvailable(int n, int n2) {
            boolean bl = this.data.remaining() - n >= n2;
            return bl;
        }

        short getInt16(int n) {
            short s = this.isAvailable(n, 2) ? this.data.getShort(n) : (short)-1;
            return s;
        }

        int getInt32(int n) {
            n = this.isAvailable(n, 4) ? this.data.getInt(n) : -1;
            return n;
        }

        int length() {
            return this.data.remaining();
        }

        void order(ByteOrder byteOrder) {
            this.data.order(byteOrder);
        }
    }

    private static interface Reader {
        public int getUInt16() throws IOException;

        public short getUInt8() throws IOException;

        public int read(byte[] var1, int var2) throws IOException;

        public long skip(long var1) throws IOException;

        public static final class EndOfFileException
        extends IOException {
            private static final long serialVersionUID = 1L;

            EndOfFileException() {
                super("Unexpectedly reached end of a file");
            }
        }
    }

    private static final class StreamReader
    implements Reader {
        private final InputStream is;

        StreamReader(InputStream inputStream) {
            this.is = inputStream;
        }

        @Override
        public int getUInt16() throws IOException {
            return this.getUInt8() << 8 | this.getUInt8();
        }

        @Override
        public short getUInt8() throws IOException {
            int n = this.is.read();
            if (n != -1) {
                return (short)n;
            }
            throw new Reader.EndOfFileException();
        }

        @Override
        public int read(byte[] byArray, int n) throws IOException {
            int n2;
            int n3 = 0;
            int n4 = 0;
            while (true) {
                int n5;
                n2 = n4;
                if (n3 >= n) break;
                n2 = n4 = (n5 = this.is.read(byArray, n3, n - n3));
                if (n5 == -1) break;
                n3 += n4;
            }
            if (n3 == 0 && n2 == -1) {
                throw new Reader.EndOfFileException();
            }
            return n3;
        }

        @Override
        public long skip(long l) throws IOException {
            if (l < 0L) {
                return 0L;
            }
            long l2 = l;
            while (l2 > 0L) {
                long l3 = this.is.skip(l2);
                if (l3 > 0L) {
                    l2 -= l3;
                    continue;
                }
                if (this.is.read() == -1) break;
                --l2;
            }
            return l - l2;
        }
    }
}

