/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package com.bumptech.glide.gifdecoder;

import android.util.Log;
import com.bumptech.glide.gifdecoder.GifFrame;
import com.bumptech.glide.gifdecoder.GifHeader;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class GifHeaderParser {
    static final int DEFAULT_FRAME_DELAY = 10;
    private static final int DESCRIPTOR_MASK_INTERLACE_FLAG = 64;
    private static final int DESCRIPTOR_MASK_LCT_FLAG = 128;
    private static final int DESCRIPTOR_MASK_LCT_SIZE = 7;
    private static final int EXTENSION_INTRODUCER = 33;
    private static final int GCE_DISPOSAL_METHOD_SHIFT = 2;
    private static final int GCE_MASK_DISPOSAL_METHOD = 28;
    private static final int GCE_MASK_TRANSPARENT_COLOR_FLAG = 1;
    private static final int IMAGE_SEPARATOR = 44;
    private static final int LABEL_APPLICATION_EXTENSION = 255;
    private static final int LABEL_COMMENT_EXTENSION = 254;
    private static final int LABEL_GRAPHIC_CONTROL_EXTENSION = 249;
    private static final int LABEL_PLAIN_TEXT_EXTENSION = 1;
    private static final int LSD_MASK_GCT_FLAG = 128;
    private static final int LSD_MASK_GCT_SIZE = 7;
    private static final int MASK_INT_LOWEST_BYTE = 255;
    private static final int MAX_BLOCK_SIZE = 256;
    static final int MIN_FRAME_DELAY = 2;
    private static final String TAG = "GifHeaderParser";
    private static final int TRAILER = 59;
    private final byte[] block = new byte[256];
    private int blockSize = 0;
    private GifHeader header;
    private ByteBuffer rawData;

    private boolean err() {
        boolean bl = this.header.status != 0;
        return bl;
    }

    private int read() {
        int n = 0;
        try {
            byte by = this.rawData.get();
            n = by & 0xFF;
        }
        catch (Exception exception) {
            this.header.status = 1;
        }
        return n;
    }

    private void readBitmap() {
        this.header.currentFrame.ix = this.readShort();
        this.header.currentFrame.iy = this.readShort();
        this.header.currentFrame.iw = this.readShort();
        this.header.currentFrame.ih = this.readShort();
        int n = this.read();
        boolean bl = false;
        boolean bl2 = (n & 0x80) != 0;
        int n2 = (int)Math.pow(2.0, (n & 7) + 1);
        Object object = this.header.currentFrame;
        if ((n & 0x40) != 0) {
            bl = true;
        }
        ((GifFrame)object).interlace = bl;
        this.header.currentFrame.lct = (int[])(bl2 ? this.readColorTable(n2) : null);
        this.header.currentFrame.bufferFrameStart = this.rawData.position();
        this.skipImageData();
        if (this.err()) {
            return;
        }
        object = this.header;
        ++((GifHeader)object).frameCount;
        this.header.frames.add(this.header.currentFrame);
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void readBlock() {
        int n;
        this.blockSize = n = this.read();
        int n2 = 0;
        if (n <= 0) return;
        n = 0;
        while (true) {
            int n3 = n;
            try {
                n = this.blockSize;
                if (n2 >= n) return;
                n3 = n -= n2;
            }
            catch (Exception exception) {
                if (Log.isLoggable((String)TAG, (int)3)) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Error Reading Block n: ");
                    stringBuilder.append(n2);
                    stringBuilder.append(" count: ");
                    stringBuilder.append(n3);
                    stringBuilder.append(" blockSize: ");
                    stringBuilder.append(this.blockSize);
                    Log.d((String)TAG, (String)stringBuilder.toString(), (Throwable)exception);
                }
                this.header.status = 1;
                return;
            }
            {
                this.rawData.get(this.block, n2, n);
                n2 += n;
                continue;
            }
            break;
        }
    }

    private int[] readColorTable(int n) {
        int n2;
        int[] nArray = null;
        byte[] byArray = new byte[n * 3];
        int[] nArray2 = nArray;
        this.rawData.get(byArray);
        nArray2 = nArray;
        try {
            nArray = new int[256];
            n2 = 0;
        }
        catch (BufferUnderflowException bufferUnderflowException) {
            if (Log.isLoggable((String)TAG, (int)3)) {
                Log.d((String)TAG, (String)"Format Error Reading Color Table", (Throwable)bufferUnderflowException);
            }
            this.header.status = 1;
        }
        for (int i = 0; i < n; ++i) {
            int n3 = n2 + 1;
            n2 = byArray[n2];
            int n4 = n3 + 1;
            nArray[i] = 0xFF000000 | (n2 & 0xFF) << 16 | (byArray[n3] & 0xFF) << 8 | byArray[n4] & 0xFF;
            n2 = n4 + 1;
        }
        nArray2 = nArray;
        return nArray2;
    }

    private void readContents() {
        this.readContents(Integer.MAX_VALUE);
    }

    private void readContents(int n) {
        boolean bl = false;
        block11: while (!bl && !this.err() && this.header.frameCount <= n) {
            switch (this.read()) {
                default: {
                    this.header.status = 1;
                    continue block11;
                }
                case 59: {
                    bl = true;
                    continue block11;
                }
                case 44: {
                    if (this.header.currentFrame == null) {
                        this.header.currentFrame = new GifFrame();
                    }
                    this.readBitmap();
                    continue block11;
                }
                case 33: 
            }
            switch (this.read()) {
                default: {
                    this.skip();
                    continue block11;
                }
                case 255: {
                    this.readBlock();
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < 11; ++i) {
                        stringBuilder.append((char)this.block[i]);
                    }
                    if (stringBuilder.toString().equals("NETSCAPE2.0")) {
                        this.readNetscapeExt();
                        continue block11;
                    }
                    this.skip();
                    continue block11;
                }
                case 254: {
                    this.skip();
                    continue block11;
                }
                case 249: {
                    this.header.currentFrame = new GifFrame();
                    this.readGraphicControlExt();
                    continue block11;
                }
                case 1: 
            }
            this.skip();
        }
    }

    private void readGraphicControlExt() {
        this.read();
        int n = this.read();
        int n2 = this.header.currentFrame.dispose = (n & 0x1C) >> 2;
        boolean bl = true;
        if (n2 == 0) {
            this.header.currentFrame.dispose = 1;
        }
        GifFrame gifFrame = this.header.currentFrame;
        if ((n & 1) == 0) {
            bl = false;
        }
        gifFrame.transparency = bl;
        n = n2 = this.readShort();
        if (n2 < 2) {
            n = 10;
        }
        this.header.currentFrame.delay = n * 10;
        this.header.currentFrame.transIndex = this.read();
        this.read();
    }

    private void readHeader() {
        Object object = new StringBuilder();
        for (int i = 0; i < 6; ++i) {
            ((StringBuilder)object).append((char)this.read());
        }
        if (!((StringBuilder)object).toString().startsWith("GIF")) {
            this.header.status = 1;
            return;
        }
        this.readLSD();
        if (this.header.gctFlag && !this.err()) {
            object = this.header;
            ((GifHeader)object).gct = this.readColorTable(((GifHeader)object).gctSize);
            object = this.header;
            ((GifHeader)object).bgColor = ((GifHeader)object).gct[this.header.bgIndex];
        }
    }

    private void readLSD() {
        this.header.width = this.readShort();
        this.header.height = this.readShort();
        int n = this.read();
        GifHeader gifHeader = this.header;
        boolean bl = (n & 0x80) != 0;
        gifHeader.gctFlag = bl;
        this.header.gctSize = (int)Math.pow(2.0, (n & 7) + 1);
        this.header.bgIndex = this.read();
        this.header.pixelAspect = this.read();
    }

    private void readNetscapeExt() {
        do {
            this.readBlock();
            byte[] byArray = this.block;
            if (byArray[0] != 1) continue;
            byte by = byArray[1];
            byte by2 = byArray[2];
            this.header.loopCount = (by2 & 0xFF) << 8 | by & 0xFF;
        } while (this.blockSize > 0 && !this.err());
    }

    private int readShort() {
        return this.rawData.getShort();
    }

    private void reset() {
        this.rawData = null;
        Arrays.fill(this.block, (byte)0);
        this.header = new GifHeader();
        this.blockSize = 0;
    }

    private void skip() {
        int n;
        do {
            n = this.read();
            int n2 = Math.min(this.rawData.position() + n, this.rawData.limit());
            this.rawData.position(n2);
        } while (n > 0);
    }

    private void skipImageData() {
        this.read();
        this.skip();
    }

    public void clear() {
        this.rawData = null;
        this.header = null;
    }

    public boolean isAnimated() {
        this.readHeader();
        if (!this.err()) {
            this.readContents(2);
        }
        int n = this.header.frameCount;
        boolean bl = true;
        if (n <= 1) {
            bl = false;
        }
        return bl;
    }

    public GifHeader parseHeader() {
        if (this.rawData != null) {
            if (this.err()) {
                return this.header;
            }
            this.readHeader();
            if (!this.err()) {
                this.readContents();
                if (this.header.frameCount < 0) {
                    this.header.status = 1;
                }
            }
            return this.header;
        }
        throw new IllegalStateException("You must call setData() before parseHeader()");
    }

    public GifHeaderParser setData(ByteBuffer byteBuffer) {
        this.reset();
        this.rawData = byteBuffer = byteBuffer.asReadOnlyBuffer();
        byteBuffer.position(0);
        this.rawData.order(ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public GifHeaderParser setData(byte[] byArray) {
        if (byArray != null) {
            this.setData(ByteBuffer.wrap(byArray));
        } else {
            this.rawData = null;
            this.header.status = 2;
        }
        return this;
    }
}

