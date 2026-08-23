/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$Config
 *  android.util.Log
 */
package com.bumptech.glide.gifdecoder;

import android.graphics.Bitmap;
import android.util.Log;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.gifdecoder.GifFrame;
import com.bumptech.glide.gifdecoder.GifHeader;
import com.bumptech.glide.gifdecoder.GifHeaderParser;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class StandardGifDecoder
implements GifDecoder {
    private static final int BYTES_PER_INTEGER = 4;
    private static final int COLOR_TRANSPARENT_BLACK = 0;
    private static final int INITIAL_FRAME_POINTER = -1;
    private static final int MASK_INT_LOWEST_BYTE = 255;
    private static final int MAX_STACK_SIZE = 4096;
    private static final int NULL_CODE = -1;
    private static final String TAG = StandardGifDecoder.class.getSimpleName();
    private int[] act;
    private Bitmap.Config bitmapConfig;
    private final GifDecoder.BitmapProvider bitmapProvider;
    private byte[] block;
    private int downsampledHeight;
    private int downsampledWidth;
    private int framePointer;
    private GifHeader header;
    private Boolean isFirstFrameTransparent;
    private byte[] mainPixels;
    private int[] mainScratch;
    private GifHeaderParser parser;
    private final int[] pct = new int[256];
    private byte[] pixelStack;
    private short[] prefix;
    private Bitmap previousImage;
    private ByteBuffer rawData;
    private int sampleSize;
    private boolean savePrevious;
    private int status;
    private byte[] suffix;

    public StandardGifDecoder(GifDecoder.BitmapProvider bitmapProvider) {
        this.bitmapConfig = Bitmap.Config.ARGB_8888;
        this.bitmapProvider = bitmapProvider;
        this.header = new GifHeader();
    }

    public StandardGifDecoder(GifDecoder.BitmapProvider bitmapProvider, GifHeader gifHeader, ByteBuffer byteBuffer) {
        this(bitmapProvider, gifHeader, byteBuffer, 1);
    }

    public StandardGifDecoder(GifDecoder.BitmapProvider bitmapProvider, GifHeader gifHeader, ByteBuffer byteBuffer, int n) {
        this(bitmapProvider);
        this.setData(gifHeader, byteBuffer, n);
    }

    private int averageColorsNear(int n, int n2, int n3) {
        int n4;
        int n5;
        int n6;
        int n7;
        int n8;
        int n9;
        byte[] byArray;
        int n10;
        int n11 = 0;
        int n12 = 0;
        int n13 = 0;
        int n14 = 0;
        int n15 = 0;
        for (n10 = n; n10 < this.sampleSize + n && n10 < (byArray = this.mainPixels).length && n10 < n2; ++n10) {
            n9 = byArray[n10];
            n8 = this.act[n9 & 0xFF];
            n7 = n11;
            n6 = n12;
            n5 = n13;
            n4 = n14;
            n9 = n15;
            if (n8 != 0) {
                n7 = n11 + (n8 >> 24 & 0xFF);
                n6 = n12 + (n8 >> 16 & 0xFF);
                n5 = n13 + (n8 >> 8 & 0xFF);
                n4 = n14 + (n8 & 0xFF);
                n9 = n15 + 1;
            }
            n11 = n7;
            n12 = n6;
            n13 = n5;
            n14 = n4;
            n15 = n9;
        }
        n9 = n12;
        n6 = n11;
        for (n10 = n + n3; n10 < n + n3 + this.sampleSize && n10 < (byArray = this.mainPixels).length && n10 < n2; ++n10) {
            n12 = byArray[n10];
            n8 = this.act[n12 & 0xFF];
            n7 = n6;
            n5 = n9;
            n4 = n13;
            n11 = n14;
            n12 = n15;
            if (n8 != 0) {
                n7 = n6 + (n8 >> 24 & 0xFF);
                n5 = n9 + (n8 >> 16 & 0xFF);
                n4 = n13 + (n8 >> 8 & 0xFF);
                n11 = n14 + (n8 & 0xFF);
                n12 = n15 + 1;
            }
            n6 = n7;
            n9 = n5;
            n13 = n4;
            n14 = n11;
            n15 = n12;
        }
        if (n15 == 0) {
            return 0;
        }
        return n6 / n15 << 24 | n9 / n15 << 16 | n13 / n15 << 8 | n14 / n15;
    }

    private void copyCopyIntoScratchRobust(GifFrame gifFrame) {
        int[] nArray = this.mainScratch;
        int n = gifFrame.ih / this.sampleSize;
        int n2 = gifFrame.iy / this.sampleSize;
        int n3 = gifFrame.iw / this.sampleSize;
        int n4 = gifFrame.ix / this.sampleSize;
        int n5 = 0;
        boolean bl = this.framePointer == 0;
        int n6 = this.sampleSize;
        int n7 = this.downsampledWidth;
        int n8 = this.downsampledHeight;
        byte[] byArray = this.mainPixels;
        int[] nArray2 = this.act;
        int n9 = 1;
        Boolean bl2 = this.isFirstFrameTransparent;
        int n10 = 8;
        for (int i = 0; i < n; ++i) {
            int n11;
            int n12 = i;
            int n13 = n5;
            int n14 = n9;
            int n15 = n10;
            if (gifFrame.interlace) {
                n12 = n5;
                n14 = n9;
                n11 = n10;
                if (n5 >= n) {
                    n14 = n9 + 1;
                    switch (n14) {
                        default: {
                            n12 = n5;
                            n11 = n10;
                            break;
                        }
                        case 4: {
                            n12 = 1;
                            n11 = 2;
                            break;
                        }
                        case 3: {
                            n12 = 2;
                            n11 = 4;
                            break;
                        }
                        case 2: {
                            n12 = 4;
                            n11 = n10;
                        }
                    }
                }
                n10 = n12;
                n13 = n12 + n11;
                n12 = n10;
                n15 = n11;
            }
            n11 = n6 == 1 ? 1 : 0;
            if ((n12 += n2) < n8) {
                n9 = n12 * n7;
                n5 = n9 + n4;
                n12 = n10 = n5 + n3;
                if (n9 + n7 < n10) {
                    n12 = n9 + n7;
                }
                n9 = i * n6 * gifFrame.iw;
                if (n11 != 0) {
                    for (n10 = n5; n10 < n12; ++n10) {
                        Boolean bl3;
                        n5 = nArray2[byArray[n9] & 0xFF];
                        if (n5 != 0) {
                            nArray[n10] = n5;
                            bl3 = bl2;
                        } else {
                            bl3 = bl2;
                            if (bl) {
                                bl3 = bl2;
                                if (bl2 == null) {
                                    bl3 = true;
                                }
                            }
                        }
                        n9 += n6;
                        bl2 = bl3;
                    }
                } else {
                    n10 = n9;
                    n11 = n12;
                    for (int j = n5; j < n11; ++j) {
                        int n16 = this.averageColorsNear(n10, (n12 - n5) * n6 + n9, gifFrame.iw);
                        if (n16 != 0) {
                            nArray[j] = n16;
                        } else if (bl && bl2 == null) {
                            bl2 = true;
                        }
                        n10 += n6;
                    }
                }
            }
            n5 = n13;
            n9 = n14;
            n10 = n15;
        }
        if (this.isFirstFrameTransparent == null) {
            boolean bl4 = bl2 == null ? false : bl2;
            this.isFirstFrameTransparent = bl4;
        }
    }

    private void copyIntoScratchFast(GifFrame object) {
        int[] nArray = this.mainScratch;
        int n = ((GifFrame)object).ih;
        int n2 = ((GifFrame)object).iy;
        int n3 = ((GifFrame)object).iw;
        int n4 = ((GifFrame)object).ix;
        boolean bl = this.framePointer == 0;
        int n5 = this.downsampledWidth;
        byte[] byArray = this.mainPixels;
        int[] nArray2 = this.act;
        int n6 = -1;
        for (int i = 0; i < n; ++i) {
            int n7;
            int n8 = (i + n2) * n5;
            int n9 = n8 + n4;
            int n10 = n7 = n9 + n3;
            if (n8 + n5 < n7) {
                n10 = n8 + n5;
            }
            n7 = ((GifFrame)object).iw * i;
            while (n9 < n10) {
                int n11 = byArray[n7];
                int n12 = n11 & 0xFF;
                n8 = n6;
                if (n12 != n6) {
                    n8 = nArray2[n12];
                    if (n8 != 0) {
                        nArray[n9] = n8;
                        n8 = n6;
                    } else {
                        n8 = n11;
                    }
                }
                ++n7;
                ++n9;
                n6 = n8;
            }
        }
        object = this.isFirstFrameTransparent;
        boolean bl2 = object != null && ((Boolean)object).booleanValue() || this.isFirstFrameTransparent == null && bl && n6 != -1;
        this.isFirstFrameTransparent = bl2;
    }

    private void decodeBitmapData(GifFrame object) {
        int n;
        int n2;
        int n3;
        if (object != null) {
            this.rawData.position(((GifFrame)object).bufferFrameStart);
        }
        if (object == null) {
            n3 = this.header.width;
            n2 = this.header.height;
        } else {
            n3 = ((GifFrame)object).iw;
            n2 = ((GifFrame)object).ih;
        }
        int n4 = n3 * n2;
        object = this.mainPixels;
        if (object == null || ((Object)object).length < n4) {
            this.mainPixels = this.bitmapProvider.obtainByteArray(n4);
        }
        byte[] byArray = this.mainPixels;
        if (this.prefix == null) {
            this.prefix = new short[4096];
        }
        short[] sArray = this.prefix;
        if (this.suffix == null) {
            this.suffix = new byte[4096];
        }
        byte[] byArray2 = this.suffix;
        if (this.pixelStack == null) {
            this.pixelStack = new byte[4097];
        }
        byte[] byArray3 = this.pixelStack;
        int n5 = this.readByte();
        int n6 = 1 << n5;
        int n7 = n6 + 2;
        int n8 = -1;
        int n9 = n5 + 1;
        int n10 = (1 << n9) - 1;
        n2 = 0;
        while (true) {
            n = 0;
            if (n2 >= n6) break;
            sArray[n2] = 0;
            byArray2[n2] = (byte)n2;
            ++n2;
        }
        object = this.block;
        int n11 = 0;
        int n12 = 0;
        n2 = 0;
        int n13 = 0;
        int n14 = 0;
        int n15 = 0;
        n3 = 0;
        block1: while (n3 < n4) {
            int n16;
            if (n13 == 0) {
                n13 = this.readBlock();
                if (n13 <= 0) {
                    this.status = 3;
                    break;
                }
                n11 = 0;
            }
            n15 += (object[n11] & 0xFF) << n14;
            int n17 = n11 + 1;
            int n18 = n13 - 1;
            n11 = n14 + 8;
            n14 = n2;
            n13 = n12;
            n12 = n10;
            n2 = n8;
            n10 = n7;
            n7 = n;
            n8 = n11;
            while (n8 >= n9) {
                n11 = n15 & n12;
                n15 >>= n9;
                n8 -= n9;
                if (n11 == n6) {
                    n9 = n5 + 1;
                    n12 = (1 << n9) - 1;
                    n10 = n6 + 2;
                    n2 = -1;
                    continue;
                }
                if (n11 == n6 + 1) {
                    n16 = n8;
                    n = n7;
                    n7 = n10;
                    n8 = n2;
                    n10 = n12;
                    n11 = n17;
                    n12 = n13;
                    n2 = n14;
                    n13 = n18;
                    n14 = n16;
                    continue block1;
                }
                if (n2 == -1) {
                    byArray[n7] = byArray2[n11];
                    ++n7;
                    ++n3;
                    n2 = n11;
                    n14 = n11;
                    continue;
                }
                if (n11 >= n10) {
                    byArray3[n13] = (byte)n14;
                    n14 = n13 + 1;
                    n13 = n2;
                } else {
                    n14 = n13;
                    n13 = n11;
                }
                while (n13 >= n6) {
                    byArray3[n14] = byArray2[n13];
                    ++n14;
                    n13 = sArray[n13];
                }
                int n19 = byArray2[n13] & 0xFF;
                byArray[n7] = (byte)n19;
                ++n7;
                ++n3;
                while (n14 > 0) {
                    byArray[n7] = byArray3[--n14];
                    ++n7;
                    ++n3;
                }
                n13 = n10;
                n = n9;
                n16 = n12;
                if (n10 < 4096) {
                    sArray[n10] = (short)n2;
                    byArray2[n10] = (byte)n19;
                    n2 = n10 + 1;
                    if ((n2 & n12) == 0) {
                        n13 = n2;
                        n = n9;
                        n16 = n12;
                        if (n2 < 4096) {
                            n = n9 + 1;
                            n16 = n12 + n2;
                            n13 = n2;
                        }
                    } else {
                        n16 = n12;
                        n = n9;
                        n13 = n2;
                    }
                }
                n2 = n11;
                n11 = n19;
                n10 = n13;
                n9 = n;
                n12 = n16;
                n13 = n14;
                n14 = n11;
            }
            n16 = n8;
            n = n7;
            n7 = n10;
            n8 = n2;
            n10 = n12;
            n11 = n17;
            n12 = n13;
            n2 = n14;
            n13 = n18;
            n14 = n16;
        }
        Arrays.fill(byArray, n, n4, (byte)0);
    }

    private GifHeaderParser getHeaderParser() {
        if (this.parser == null) {
            this.parser = new GifHeaderParser();
        }
        return this.parser;
    }

    private Bitmap getNextBitmap() {
        Boolean bl = this.isFirstFrameTransparent;
        bl = bl != null && !bl.booleanValue() ? this.bitmapConfig : Bitmap.Config.ARGB_8888;
        bl = this.bitmapProvider.obtain(this.downsampledWidth, this.downsampledHeight, (Bitmap.Config)bl);
        bl.setHasAlpha(true);
        return bl;
    }

    private int readBlock() {
        int n = this.readByte();
        if (n <= 0) {
            return n;
        }
        ByteBuffer byteBuffer = this.rawData;
        byteBuffer.get(this.block, 0, Math.min(n, byteBuffer.remaining()));
        return n;
    }

    private int readByte() {
        return this.rawData.get() & 0xFF;
    }

    private Bitmap setPixels(GifFrame gifFrame, GifFrame gifFrame2) {
        int n;
        int[] nArray = this.mainScratch;
        if (gifFrame2 == null) {
            Bitmap bitmap = this.previousImage;
            if (bitmap != null) {
                this.bitmapProvider.release(bitmap);
            }
            this.previousImage = null;
            Arrays.fill(nArray, 0);
        }
        if (gifFrame2 != null && gifFrame2.dispose == 3 && this.previousImage == null) {
            Arrays.fill(nArray, 0);
        }
        if (gifFrame2 != null && gifFrame2.dispose > 0) {
            if (gifFrame2.dispose == 2) {
                int n2;
                int n3;
                n = 0;
                if (!gifFrame.transparency) {
                    n = n3 = this.header.bgColor;
                    if (gifFrame.lct != null) {
                        n = n3;
                        if (this.header.bgIndex == gifFrame.transIndex) {
                            n = 0;
                        }
                    }
                }
                int n4 = gifFrame2.ih / this.sampleSize;
                n3 = gifFrame2.iy / this.sampleSize;
                int n5 = gifFrame2.iw / this.sampleSize;
                int n6 = gifFrame2.ix / this.sampleSize;
                int n7 = this.downsampledWidth;
                for (n3 = n2 = n3 * n7 + n6; n3 < n7 * n4 + n2; n3 += this.downsampledWidth) {
                    for (n6 = n3; n6 < n3 + n5; ++n6) {
                        nArray[n6] = n;
                    }
                }
            } else if (gifFrame2.dispose == 3 && (gifFrame2 = this.previousImage) != null) {
                n = this.downsampledWidth;
                gifFrame2.getPixels(nArray, 0, n, 0, 0, n, this.downsampledHeight);
            }
        }
        this.decodeBitmapData(gifFrame);
        if (!gifFrame.interlace && this.sampleSize == 1) {
            this.copyIntoScratchFast(gifFrame);
        } else {
            this.copyCopyIntoScratchRobust(gifFrame);
        }
        if (this.savePrevious && (gifFrame.dispose == 0 || gifFrame.dispose == 1)) {
            if (this.previousImage == null) {
                this.previousImage = this.getNextBitmap();
            }
            gifFrame = this.previousImage;
            n = this.downsampledWidth;
            gifFrame.setPixels(nArray, 0, n, 0, 0, n, this.downsampledHeight);
        }
        gifFrame = this.getNextBitmap();
        n = this.downsampledWidth;
        gifFrame.setPixels(nArray, 0, n, 0, 0, n, this.downsampledHeight);
        return gifFrame;
    }

    @Override
    public void advance() {
        this.framePointer = (this.framePointer + 1) % this.header.frameCount;
    }

    @Override
    public void clear() {
        this.header = null;
        Object object = this.mainPixels;
        if (object != null) {
            this.bitmapProvider.release((byte[])object);
        }
        if ((object = (Object)this.mainScratch) != null) {
            this.bitmapProvider.release((int[])object);
        }
        if ((object = (Object)this.previousImage) != null) {
            this.bitmapProvider.release((Bitmap)object);
        }
        this.previousImage = null;
        this.rawData = null;
        this.isFirstFrameTransparent = null;
        object = this.block;
        if (object != null) {
            this.bitmapProvider.release((byte[])object);
        }
    }

    @Override
    public int getByteSize() {
        return this.rawData.limit() + this.mainPixels.length + this.mainScratch.length * 4;
    }

    @Override
    public int getCurrentFrameIndex() {
        return this.framePointer;
    }

    @Override
    public ByteBuffer getData() {
        return this.rawData;
    }

    @Override
    public int getDelay(int n) {
        int n2;
        int n3 = n2 = -1;
        if (n >= 0) {
            n3 = n2;
            if (n < this.header.frameCount) {
                n3 = this.header.frames.get((int)n).delay;
            }
        }
        return n3;
    }

    @Override
    public int getFrameCount() {
        return this.header.frameCount;
    }

    @Override
    public int getHeight() {
        return this.header.height;
    }

    @Override
    @Deprecated
    public int getLoopCount() {
        if (this.header.loopCount == -1) {
            return 1;
        }
        return this.header.loopCount;
    }

    @Override
    public int getNetscapeLoopCount() {
        return this.header.loopCount;
    }

    @Override
    public int getNextDelay() {
        int n;
        if (this.header.frameCount > 0 && (n = this.framePointer) >= 0) {
            return this.getDelay(n);
        }
        return 0;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public Bitmap getNextFrame() {
        synchronized (this) {
            int n;
            Object object;
            Object object2;
            if (this.header.frameCount <= 0 || this.framePointer < 0) {
                object2 = TAG;
                if (Log.isLoggable((String)object2, (int)3)) {
                    object = new StringBuilder();
                    ((StringBuilder)object).append("Unable to decode frame, frameCount=");
                    ((StringBuilder)object).append(this.header.frameCount);
                    ((StringBuilder)object).append(", framePointer=");
                    ((StringBuilder)object).append(this.framePointer);
                    Log.d((String)object2, (String)((StringBuilder)object).toString());
                }
                this.status = 1;
            }
            if ((n = this.status) != 1 && n != 2) {
                this.status = 0;
                if (this.block == null) {
                    this.block = this.bitmapProvider.obtainByteArray(255);
                }
                GifFrame gifFrame = this.header.frames.get(this.framePointer);
                object = null;
                n = this.framePointer - 1;
                if (n >= 0) {
                    object = this.header.frames.get(n);
                }
                object2 = gifFrame.lct != null ? (Object)gifFrame.lct : (Object)this.header.gct;
                this.act = (int[])object2;
                if (object2 == null) {
                    object2 = TAG;
                    if (Log.isLoggable((String)object2, (int)3)) {
                        object = new StringBuilder();
                        ((StringBuilder)object).append("No valid color table found for frame #");
                        ((StringBuilder)object).append(this.framePointer);
                        Log.d((String)object2, (String)((StringBuilder)object).toString());
                    }
                    this.status = 1;
                    return null;
                }
                if (!gifFrame.transparency) return this.setPixels(gifFrame, (GifFrame)object);
                object2 = this.act;
                System.arraycopy(object2, 0, this.pct, 0, ((Object)object2).length);
                object2 = this.pct;
                this.act = (int[])object2;
                object2[gifFrame.transIndex] = false;
                if (gifFrame.dispose != 2) return this.setPixels(gifFrame, (GifFrame)object);
                if (this.framePointer != 0) return this.setPixels(gifFrame, (GifFrame)object);
                this.isFirstFrameTransparent = true;
                return this.setPixels(gifFrame, (GifFrame)object);
            }
            object2 = TAG;
            if (!Log.isLoggable((String)object2, (int)3)) return null;
            object = new StringBuilder();
            ((StringBuilder)object).append("Unable to decode frame, status=");
            ((StringBuilder)object).append(this.status);
            Log.d((String)object2, (String)((StringBuilder)object).toString());
            return null;
        }
    }

    @Override
    public int getStatus() {
        return this.status;
    }

    @Override
    public int getTotalIterationCount() {
        if (this.header.loopCount == -1) {
            return 1;
        }
        if (this.header.loopCount == 0) {
            return 0;
        }
        return this.header.loopCount + 1;
    }

    @Override
    public int getWidth() {
        return this.header.width;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public int read(InputStream inputStream, int n) {
        if (inputStream != null) {
            n = n > 0 ? (n += 4096) : 16384;
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(n);
                byte[] byArray = new byte[16384];
                while ((n = inputStream.read(byArray, 0, byArray.length)) != -1) {
                    byteArrayOutputStream.write(byArray, 0, n);
                }
                byteArrayOutputStream.flush();
                this.read(byteArrayOutputStream.toByteArray());
            }
            catch (IOException iOException) {
                Log.w((String)TAG, (String)"Error reading data from stream", (Throwable)iOException);
            }
        } else {
            this.status = 2;
        }
        if (inputStream == null) return this.status;
        try {
            inputStream.close();
            return this.status;
        }
        catch (IOException iOException) {
            Log.w((String)TAG, (String)"Error closing stream", (Throwable)iOException);
            return this.status;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public int read(byte[] byArray) {
        synchronized (this) {
            GifHeader gifHeader;
            this.header = gifHeader = this.getHeaderParser().setData(byArray).parseHeader();
            if (byArray == null) return this.status;
            this.setData(gifHeader, byArray);
            return this.status;
        }
    }

    @Override
    public void resetFrameIndex() {
        this.framePointer = -1;
    }

    @Override
    public void setData(GifHeader gifHeader, ByteBuffer byteBuffer) {
        synchronized (this) {
            this.setData(gifHeader, byteBuffer, 1);
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void setData(GifHeader object, ByteBuffer object2, int n) {
        synchronized (this) {
            Throwable throwable2;
            if (n > 0) {
                try {
                    n = Integer.highestOneBit(n);
                    this.status = 0;
                    this.header = object;
                    this.framePointer = -1;
                    object2 = ((ByteBuffer)object2).asReadOnlyBuffer();
                    this.rawData = object2;
                    ((ByteBuffer)object2).position(0);
                    this.rawData.order(ByteOrder.LITTLE_ENDIAN);
                    this.savePrevious = false;
                    object2 = ((GifHeader)object).frames.iterator();
                    while (object2.hasNext()) {
                        if (((GifFrame)object2.next()).dispose != 3) continue;
                        this.savePrevious = true;
                        break;
                    }
                    this.sampleSize = n;
                    this.downsampledWidth = ((GifHeader)object).width / n;
                    this.downsampledHeight = ((GifHeader)object).height / n;
                    this.mainPixels = this.bitmapProvider.obtainByteArray(((GifHeader)object).width * ((GifHeader)object).height);
                    this.mainScratch = this.bitmapProvider.obtainIntArray(this.downsampledWidth * this.downsampledHeight);
                    return;
                }
                catch (Throwable throwable2) {}
            } else {
                object2 = new StringBuilder();
                ((StringBuilder)object2).append("Sample size must be >=0, not: ");
                ((StringBuilder)object2).append(n);
                object = new IllegalArgumentException(((StringBuilder)object2).toString());
                throw object;
            }
            throw throwable2;
        }
    }

    @Override
    public void setData(GifHeader gifHeader, byte[] byArray) {
        synchronized (this) {
            this.setData(gifHeader, ByteBuffer.wrap(byArray));
            return;
        }
    }

    @Override
    public void setDefaultBitmapConfig(Bitmap.Config config) {
        if (config != Bitmap.Config.ARGB_8888 && config != Bitmap.Config.RGB_565) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unsupported format: ");
            stringBuilder.append(config);
            stringBuilder.append(", must be one of ");
            stringBuilder.append(Bitmap.Config.ARGB_8888);
            stringBuilder.append(" or ");
            stringBuilder.append(Bitmap.Config.RGB_565);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        this.bitmapConfig = config;
    }
}

