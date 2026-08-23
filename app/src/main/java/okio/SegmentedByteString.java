/*
 * Decompiled with CFR 0.152.
 */
package okio;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import okio.Buffer;
import okio.ByteString;
import okio.Segment;
import okio.Util;

final class SegmentedByteString
extends ByteString {
    final transient int[] directory;
    final transient byte[][] segments;

    SegmentedByteString(Buffer object, int n) {
        super(null);
        Util.checkOffsetAndCount(((Buffer)object).size, 0L, n);
        int n2 = 0;
        int n3 = 0;
        Object object2 = ((Buffer)object).head;
        while (n2 < n) {
            if (((Segment)object2).limit != ((Segment)object2).pos) {
                n2 += ((Segment)object2).limit - ((Segment)object2).pos;
                ++n3;
                object2 = ((Segment)object2).next;
                continue;
            }
            throw new AssertionError((Object)"s.limit == s.pos");
        }
        this.segments = new byte[n3][];
        this.directory = new int[n3 * 2];
        n3 = 0;
        n2 = 0;
        object = ((Buffer)object).head;
        while (n3 < n) {
            int n4;
            this.segments[n2] = ((Segment)object).data;
            n3 = n4 = n3 + (((Segment)object).limit - ((Segment)object).pos);
            if (n4 > n) {
                n3 = n;
            }
            object2 = this.directory;
            object2[n2] = n3;
            object2[this.segments.length + n2] = ((Segment)object).pos;
            ((Segment)object).shared = true;
            ++n2;
            object = ((Segment)object).next;
        }
    }

    private int segment(int n) {
        if ((n = Arrays.binarySearch(this.directory, 0, this.segments.length, n + 1)) < 0) {
            n ^= 0xFFFFFFFF;
        }
        return n;
    }

    private ByteString toByteString() {
        return new ByteString(this.toByteArray());
    }

    private Object writeReplace() {
        return this.toByteString();
    }

    @Override
    public ByteBuffer asByteBuffer() {
        return ByteBuffer.wrap(this.toByteArray()).asReadOnlyBuffer();
    }

    @Override
    public String base64() {
        return this.toByteString().base64();
    }

    @Override
    public String base64Url() {
        return this.toByteString().base64Url();
    }

    @Override
    public boolean equals(Object object) {
        boolean bl = true;
        if (object == this) {
            return true;
        }
        if (!(object instanceof ByteString) || ((ByteString)object).size() != this.size() || !this.rangeEquals(0, (ByteString)object, 0, this.size())) {
            bl = false;
        }
        return bl;
    }

    @Override
    public byte getByte(int n) {
        Util.checkOffsetAndCount(this.directory[this.segments.length - 1], n, 1L);
        int n2 = this.segment(n);
        int n3 = n2 == 0 ? 0 : this.directory[n2 - 1];
        int[] nArray = this.directory;
        byte[][] byArray = this.segments;
        int n4 = nArray[byArray.length + n2];
        return byArray[n2][n - n3 + n4];
    }

    @Override
    public int hashCode() {
        int n = this.hashCode;
        if (n != 0) {
            return n;
        }
        int n2 = 1;
        int n3 = 0;
        int n4 = this.segments.length;
        for (n = 0; n < n4; ++n) {
            byte[] byArray = this.segments[n];
            int[] nArray = this.directory;
            int n5 = nArray[n4 + n];
            int n6 = nArray[n];
            for (int i = n5; i < n5 + (n6 - n3); ++i) {
                n2 = n2 * 31 + byArray[i];
            }
            n3 = n6;
        }
        this.hashCode = n2;
        return n2;
    }

    @Override
    public String hex() {
        return this.toByteString().hex();
    }

    @Override
    public int indexOf(byte[] byArray, int n) {
        return this.toByteString().indexOf(byArray, n);
    }

    @Override
    byte[] internalArray() {
        return this.toByteArray();
    }

    @Override
    public int lastIndexOf(byte[] byArray, int n) {
        return this.toByteString().lastIndexOf(byArray, n);
    }

    @Override
    public ByteString md5() {
        return this.toByteString().md5();
    }

    @Override
    public boolean rangeEquals(int n, ByteString byteString, int n2, int n3) {
        if (n >= 0 && n <= this.size() - n3) {
            int n4 = this.segment(n);
            int n5 = n;
            n = n4;
            while (n3 > 0) {
                int n6;
                int[] nArray;
                int n7;
                byte[][] byArray = this.segments;
                n4 = n == 0 ? 0 : this.directory[n - 1];
                if (!byteString.rangeEquals(n2, byArray[n], n5 - n4 + (n7 = (nArray = this.directory)[byArray.length + n]), n6 = Math.min(n3, n4 + (this.directory[n] - n4) - n5))) {
                    return false;
                }
                n5 += n6;
                n2 += n6;
                n3 -= n6;
                ++n;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean rangeEquals(int n, byte[] byArray, int n2, int n3) {
        if (n >= 0 && n <= this.size() - n3 && n2 >= 0 && n2 <= byArray.length - n3) {
            int n4 = this.segment(n);
            int n5 = n;
            n = n4;
            while (n3 > 0) {
                int n6;
                int[] nArray;
                int n7;
                byte[][] byArray2 = this.segments;
                n4 = n == 0 ? 0 : this.directory[n - 1];
                if (!Util.arrayRangeEquals(byArray2[n], n5 - n4 + (n7 = (nArray = this.directory)[byArray2.length + n]), byArray, n2, n6 = Math.min(n3, n4 + (this.directory[n] - n4) - n5))) {
                    return false;
                }
                n5 += n6;
                n2 += n6;
                n3 -= n6;
                ++n;
            }
            return true;
        }
        return false;
    }

    @Override
    public ByteString sha256() {
        return this.toByteString().sha256();
    }

    @Override
    public int size() {
        return this.directory[this.segments.length - 1];
    }

    @Override
    public ByteString substring(int n) {
        return this.toByteString().substring(n);
    }

    @Override
    public ByteString substring(int n, int n2) {
        return this.toByteString().substring(n, n2);
    }

    @Override
    public ByteString toAsciiLowercase() {
        return this.toByteString().toAsciiLowercase();
    }

    @Override
    public ByteString toAsciiUppercase() {
        return this.toByteString().toAsciiUppercase();
    }

    @Override
    public byte[] toByteArray() {
        Object[] objectArray = this.directory;
        Object object = this.segments;
        objectArray = new byte[objectArray[((byte[][])object).length - 1]];
        Object object2 = 0;
        int n = ((byte[][])object).length;
        for (int i = 0; i < n; ++i) {
            object = this.directory;
            byte[] byArray = object[n + i];
            byte[] byArray2 = object[i];
            System.arraycopy(this.segments[i], (int)byArray, objectArray, object2, (int)(byArray2 - object2));
            object2 = byArray2;
        }
        return objectArray;
    }

    @Override
    public String toString() {
        return this.toByteString().toString();
    }

    @Override
    public String utf8() {
        return this.toByteString().utf8();
    }

    @Override
    public void write(OutputStream object) throws IOException {
        if (object != null) {
            int n = 0;
            int n2 = this.segments.length;
            for (int i = 0; i < n2; ++i) {
                int[] nArray = this.directory;
                int n3 = nArray[n2 + i];
                int n4 = nArray[i];
                ((OutputStream)object).write(this.segments[i], n3, n4 - n);
                n = n4;
            }
            return;
        }
        object = new IllegalArgumentException("out == null");
        throw object;
    }

    @Override
    void write(Buffer buffer) {
        int n = 0;
        int n2 = this.segments.length;
        for (int i = 0; i < n2; ++i) {
            Object object = this.directory;
            int n3 = object[n2 + i];
            int n4 = object[i];
            object = new Segment(this.segments[i], n3, n3 + n4 - n);
            if (buffer.head == null) {
                object.prev = object;
                object.next = object;
                buffer.head = object;
            } else {
                buffer.head.prev.push((Segment)object);
            }
            n = n4;
        }
        buffer.size += (long)n;
    }
}

