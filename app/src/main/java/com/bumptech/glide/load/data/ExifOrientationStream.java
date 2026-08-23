/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.load.data;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class ExifOrientationStream
extends FilterInputStream {
    private static final byte[] EXIF_SEGMENT;
    private static final int ORIENTATION_POSITION;
    private static final int SEGMENT_LENGTH;
    private static final int SEGMENT_START_POSITION = 2;
    private final byte orientation;
    private int position;

    static {
        int n;
        byte[] byArray;
        byte[] byArray2 = byArray = new byte[29];
        byArray[0] = -1;
        byArray2[1] = -31;
        byArray2[2] = 0;
        byArray2[3] = 28;
        byArray2[4] = 69;
        byArray2[5] = 120;
        byArray2[6] = 105;
        byArray2[7] = 102;
        byArray2[8] = 0;
        byArray2[9] = 0;
        byArray2[10] = 77;
        byArray2[11] = 77;
        byArray2[12] = 0;
        byArray2[13] = 0;
        byArray2[14] = 0;
        byArray2[15] = 0;
        byArray2[16] = 0;
        byArray2[17] = 8;
        byArray2[18] = 0;
        byArray2[19] = 1;
        byArray2[20] = 1;
        byArray2[21] = 18;
        byArray2[22] = 0;
        byArray2[23] = 2;
        byArray2[24] = 0;
        byArray2[25] = 0;
        byArray2[26] = 0;
        byArray2[27] = 1;
        byArray2[28] = 0;
        EXIF_SEGMENT = byArray;
        SEGMENT_LENGTH = n = byArray.length;
        ORIENTATION_POSITION = n + 2;
    }

    public ExifOrientationStream(InputStream object, int n) {
        super((InputStream)object);
        if (n >= -1 && n <= 8) {
            this.orientation = (byte)n;
            return;
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("Cannot add invalid orientation: ");
        ((StringBuilder)object).append(n);
        throw new IllegalArgumentException(((StringBuilder)object).toString());
    }

    @Override
    public void mark(int n) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean markSupported() {
        return false;
    }

    @Override
    public int read() throws IOException {
        int n;
        int n2 = this.position;
        n = n2 >= 2 && n2 <= (n = ORIENTATION_POSITION) ? (n2 == n ? (int)this.orientation : EXIF_SEGMENT[n2 - 2] & 0xFF) : super.read();
        if (n != -1) {
            ++this.position;
        }
        return n;
    }

    @Override
    public int read(byte[] byArray, int n, int n2) throws IOException {
        int n3 = this.position;
        int n4 = ORIENTATION_POSITION;
        if (n3 > n4) {
            n = super.read(byArray, n, n2);
        } else if (n3 == n4) {
            byArray[n] = this.orientation;
            n = 1;
        } else if (n3 < 2) {
            n = super.read(byArray, n, 2 - n3);
        } else {
            n2 = Math.min(n4 - n3, n2);
            System.arraycopy(EXIF_SEGMENT, this.position - 2, byArray, n, n2);
            n = n2;
        }
        if (n > 0) {
            this.position += n;
        }
        return n;
    }

    @Override
    public void reset() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long skip(long l) throws IOException {
        if ((l = super.skip(l)) > 0L) {
            this.position = (int)((long)this.position + l);
        }
        return l;
    }
}

