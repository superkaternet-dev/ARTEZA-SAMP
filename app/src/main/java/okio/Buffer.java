/*
 * Decompiled with CFR 0.152.
 */
package okio;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ByteString;
import okio.Options;
import okio.Segment;
import okio.SegmentPool;
import okio.SegmentedByteString;
import okio.Sink;
import okio.Source;
import okio.Timeout;
import okio.Util;

public final class Buffer
implements BufferedSource,
BufferedSink,
Cloneable {
    private static final byte[] DIGITS = new byte[]{48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102};
    static final int REPLACEMENT_CHARACTER = 65533;
    Segment head;
    long size;

    private ByteString digest(String object) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance((String)object);
            messageDigest.update(this.head.data, this.head.pos, this.head.limit - this.head.pos);
            object = this.head.next;
            while (object != this.head) {
                messageDigest.update(((Segment)object).data, ((Segment)object).pos, ((Segment)object).limit - ((Segment)object).pos);
                object = ((Segment)object).next;
            }
            object = ByteString.of(messageDigest.digest());
            return object;
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            AssertionError assertionError = new AssertionError();
            throw assertionError;
        }
    }

    private boolean rangeEquals(Segment segment, int n, ByteString byteString, int n2, int n3) {
        int n4 = segment.limit;
        byte[] byArray = segment.data;
        while (n2 < n3) {
            int n5 = n4;
            Segment segment2 = segment;
            int n6 = n;
            if (n == n4) {
                segment2 = segment.next;
                byArray = segment2.data;
                n6 = segment2.pos;
                n5 = segment2.limit;
            }
            if (byArray[n6] != byteString.getByte(n2)) {
                return false;
            }
            n = n6 + 1;
            ++n2;
            n4 = n5;
            segment = segment2;
        }
        return true;
    }

    private void readFrom(InputStream object, long l, boolean bl) throws IOException {
        if (object != null) {
            while (true) {
                if (l <= 0L && !bl) {
                    return;
                }
                Segment segment = this.writableSegment(1);
                int n = (int)Math.min(l, (long)(8192 - segment.limit));
                if ((n = ((InputStream)object).read(segment.data, segment.limit, n)) == -1) {
                    if (bl) {
                        return;
                    }
                    throw new EOFException();
                }
                segment.limit += n;
                this.size += (long)n;
                l -= (long)n;
            }
        }
        object = new IllegalArgumentException("in == null");
        throw object;
    }

    @Override
    public Buffer buffer() {
        return this;
    }

    public void clear() {
        try {
            this.skip(this.size);
            return;
        }
        catch (EOFException eOFException) {
            throw new AssertionError((Object)eOFException);
        }
    }

    public Buffer clone() {
        Segment segment;
        Buffer buffer = new Buffer();
        if (this.size == 0L) {
            return buffer;
        }
        buffer.head = segment = new Segment(this.head);
        segment.prev = segment;
        segment.next = segment;
        segment = this.head.next;
        while (segment != this.head) {
            buffer.head.prev.push(new Segment(segment));
            segment = segment.next;
        }
        buffer.size = this.size;
        return buffer;
    }

    @Override
    public void close() {
    }

    public long completeSegmentByteCount() {
        long l = this.size;
        if (l == 0L) {
            return 0L;
        }
        Segment segment = this.head.prev;
        long l2 = l;
        if (segment.limit < 8192) {
            l2 = l;
            if (segment.owner) {
                l2 = l - (long)(segment.limit - segment.pos);
            }
        }
        return l2;
    }

    public Buffer copyTo(OutputStream outputStream) throws IOException {
        return this.copyTo(outputStream, 0L, this.size);
    }

    public Buffer copyTo(OutputStream object, long l, long l2) throws IOException {
        if (object != null) {
            int n;
            long l3;
            Segment segment;
            Util.checkOffsetAndCount(this.size, l, l2);
            if (l2 == 0L) {
                return this;
            }
            Segment segment2 = this.head;
            while (true) {
                segment = segment2;
                l3 = l;
                if (l < (long)(segment2.limit - segment2.pos)) break;
                l -= (long)(segment2.limit - segment2.pos);
                segment2 = segment2.next;
            }
            for (long i = l2; i > 0L; i -= (long)n) {
                int n2 = (int)((long)segment.pos + l3);
                n = (int)Math.min((long)(segment.limit - n2), i);
                ((OutputStream)object).write(segment.data, n2, n);
                l3 = 0L;
                segment = segment.next;
            }
            return this;
        }
        object = new IllegalArgumentException("out == null");
        throw object;
    }

    public Buffer copyTo(Buffer object, long l, long l2) {
        if (object != null) {
            Segment segment;
            long l3;
            Segment segment2;
            Util.checkOffsetAndCount(this.size, l, l2);
            if (l2 == 0L) {
                return this;
            }
            ((Buffer)object).size += l2;
            Segment segment3 = this.head;
            while (true) {
                segment2 = segment3;
                l3 = l;
                if (l < (long)(segment3.limit - segment3.pos)) break;
                l -= (long)(segment3.limit - segment3.pos);
                segment3 = segment3.next;
            }
            for (long i = l2; i > 0L; i -= (long)(segment.limit - segment.pos)) {
                segment = new Segment(segment2);
                segment.pos = (int)((long)segment.pos + l3);
                segment.limit = Math.min(segment.pos + (int)i, segment.limit);
                segment3 = ((Buffer)object).head;
                if (segment3 == null) {
                    segment.prev = segment;
                    segment.next = segment;
                    ((Buffer)object).head = segment;
                } else {
                    segment3.prev.push(segment);
                }
                l3 = 0L;
                segment2 = segment2.next;
            }
            return this;
        }
        object = new IllegalArgumentException("out == null");
        throw object;
    }

    @Override
    public BufferedSink emit() {
        return this;
    }

    @Override
    public Buffer emitCompleteSegments() {
        return this;
    }

    public boolean equals(Object object) {
        long l;
        if (this == object) {
            return true;
        }
        if (!(object instanceof Buffer)) {
            return false;
        }
        object = (Buffer)object;
        long l2 = this.size;
        if (l2 != ((Buffer)object).size) {
            return false;
        }
        if (l2 == 0L) {
            return true;
        }
        Segment segment = this.head;
        object = ((Buffer)object).head;
        int n = segment.pos;
        int n2 = ((Segment)object).pos;
        for (l2 = 0L; l2 < this.size; l2 += l) {
            l = Math.min(segment.limit - n, ((Segment)object).limit - n2);
            int n3 = 0;
            while ((long)n3 < l) {
                if (segment.data[n] != ((Segment)object).data[n2]) {
                    return false;
                }
                ++n3;
                ++n;
                ++n2;
            }
            Segment segment2 = segment;
            n3 = n;
            if (n == segment.limit) {
                segment2 = segment.next;
                n3 = segment2.pos;
            }
            Object object2 = object;
            int n4 = n2;
            if (n2 == ((Segment)object).limit) {
                object2 = ((Segment)object).next;
                n4 = ((Segment)object2).pos;
            }
            segment = segment2;
            object = object2;
            n = n3;
            n2 = n4;
        }
        return true;
    }

    @Override
    public boolean exhausted() {
        boolean bl = this.size == 0L;
        return bl;
    }

    @Override
    public void flush() {
    }

    public byte getByte(long l) {
        Util.checkOffsetAndCount(this.size, l, 1L);
        Segment segment = this.head;
        int n;
        while (l >= (long)(n = segment.limit - segment.pos)) {
            l -= (long)n;
            segment = segment.next;
        }
        return segment.data[segment.pos + (int)l];
    }

    public int hashCode() {
        Segment segment = this.head;
        if (segment == null) {
            return 0;
        }
        int n = 1;
        do {
            int n2 = segment.limit;
            for (int i = segment.pos; i < n2; ++i) {
                n = n * 31 + segment.data[i];
            }
        } while ((segment = segment.next) != this.head);
        return n;
    }

    @Override
    public long indexOf(byte by) {
        return this.indexOf(by, 0L);
    }

    @Override
    public long indexOf(byte by, long l) {
        if (l >= 0L) {
            long l2;
            long l3;
            Segment segment;
            long l4;
            Object object = this.head;
            if (object == null) {
                return -1L;
            }
            if (this.size - l < l) {
                l4 = this.size;
                while (true) {
                    segment = object;
                    l3 = l4;
                    l2 = l;
                    if (l4 > l) {
                        object = ((Segment)object).prev;
                        l4 -= (long)(((Segment)object).limit - ((Segment)object).pos);
                        continue;
                    }
                    break;
                }
            } else {
                l3 = 0L;
                while (true) {
                    l4 = (long)(((Segment)object).limit - ((Segment)object).pos) + l3;
                    segment = object;
                    l2 = l;
                    if (l4 >= l) break;
                    object = ((Segment)object).next;
                    l3 = l4;
                }
            }
            while (l3 < this.size) {
                object = segment.data;
                int n = segment.limit;
                for (int i = (int)((long)segment.pos + l2 - l3); i < n; ++i) {
                    if (object[i] != by) continue;
                    return (long)(i - segment.pos) + l3;
                }
                l2 = l3 += (long)(segment.limit - segment.pos);
                segment = segment.next;
            }
            return -1L;
        }
        IllegalArgumentException illegalArgumentException = new IllegalArgumentException("fromIndex < 0");
        throw illegalArgumentException;
    }

    @Override
    public long indexOf(ByteString byteString) throws IOException {
        return this.indexOf(byteString, 0L);
    }

    @Override
    public long indexOf(ByteString serializable, long l) throws IOException {
        if (((ByteString)serializable).size() != 0) {
            if (l >= 0L) {
                long l2;
                Segment segment;
                long l3;
                Object object = this.head;
                if (object == null) {
                    return -1L;
                }
                if (this.size - l < l) {
                    l3 = this.size;
                    while (true) {
                        segment = object;
                        l2 = l3;
                        if (l3 > l) {
                            object = ((Segment)object).prev;
                            l3 -= (long)(((Segment)object).limit - ((Segment)object).pos);
                            continue;
                        }
                        break;
                    }
                } else {
                    l2 = 0L;
                    while (true) {
                        l3 = (long)(((Segment)object).limit - ((Segment)object).pos) + l2;
                        segment = object;
                        if (l3 >= l) break;
                        object = ((Segment)object).next;
                        l2 = l3;
                    }
                }
                byte by = ((ByteString)serializable).getByte(0);
                int n = ((ByteString)serializable).size();
                l3 = 1L + (this.size - (long)n);
                while (l2 < l3) {
                    object = segment.data;
                    int n2 = (int)Math.min((long)segment.limit, (long)segment.pos + l3 - l2);
                    for (int i = (int)((long)segment.pos + l - l2); i < n2; ++i) {
                        if (object[i] != by || !this.rangeEquals(segment, i + 1, (ByteString)serializable, 1, n)) continue;
                        return (long)(i - segment.pos) + l2;
                    }
                    l = l2 += (long)(segment.limit - segment.pos);
                    segment = segment.next;
                }
                return -1L;
            }
            throw new IllegalArgumentException("fromIndex < 0");
        }
        serializable = new IllegalArgumentException("bytes is empty");
        throw serializable;
    }

    @Override
    public long indexOfElement(ByteString byteString) {
        return this.indexOfElement(byteString, 0L);
    }

    @Override
    public long indexOfElement(ByteString object, long l) {
        if (l >= 0L) {
            long l2;
            Segment segment;
            long l3;
            Object object2 = this.head;
            if (object2 == null) {
                return -1L;
            }
            if (this.size - l < l) {
                l3 = this.size;
                while (true) {
                    segment = object2;
                    l2 = l3;
                    if (l3 > l) {
                        object2 = ((Segment)object2).prev;
                        l3 -= (long)(((Segment)object2).limit - ((Segment)object2).pos);
                        continue;
                    }
                    break;
                }
            } else {
                l2 = 0L;
                while (true) {
                    l3 = (long)(((Segment)object2).limit - ((Segment)object2).pos) + l2;
                    segment = object2;
                    if (l3 >= l) break;
                    object2 = ((Segment)object2).next;
                    l2 = l3;
                }
            }
            if (((ByteString)object).size() == 2) {
                byte by = ((ByteString)object).getByte(0);
                byte by2 = ((ByteString)object).getByte(1);
                while (l2 < this.size) {
                    object = segment.data;
                    int n = segment.limit;
                    for (int i = (int)((long)segment.pos + l - l2); i < n; ++i) {
                        Object object3 = object[i];
                        if (object3 != by && object3 != by2) {
                            continue;
                        }
                        return (long)(i - segment.pos) + l2;
                    }
                    l = l2 += (long)(segment.limit - segment.pos);
                    segment = segment.next;
                }
            } else {
                object = ((ByteString)object).internalArray();
                while (l2 < this.size) {
                    object2 = segment.data;
                    int n = (int)((long)segment.pos + l - l2);
                    int n2 = segment.limit;
                    while (true) {
                        if (n >= n2) break;
                        Object object4 = object2[n];
                        int n3 = ((Object)object).length;
                        for (int i = 0; i < n3; ++i) {
                            if (object4 != object[i]) continue;
                            return (long)(n - segment.pos) + l2;
                        }
                        ++n;
                    }
                    l = l2 += (long)(segment.limit - segment.pos);
                    segment = segment.next;
                }
            }
            return -1L;
        }
        object = new IllegalArgumentException("fromIndex < 0");
        throw object;
    }

    @Override
    public InputStream inputStream() {
        return new InputStream(this){
            final Buffer this$0;
            {
                this.this$0 = buffer;
            }

            @Override
            public int available() {
                return (int)Math.min(this.this$0.size, Integer.MAX_VALUE);
            }

            @Override
            public void close() {
            }

            @Override
            public int read() {
                if (this.this$0.size > 0L) {
                    return this.this$0.readByte() & 0xFF;
                }
                return -1;
            }

            @Override
            public int read(byte[] byArray, int n, int n2) {
                return this.this$0.read(byArray, n, n2);
            }

            public String toString() {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(this.this$0);
                stringBuilder.append(".inputStream()");
                return stringBuilder.toString();
            }
        };
    }

    public ByteString md5() {
        return this.digest("MD5");
    }

    @Override
    public OutputStream outputStream() {
        return new OutputStream(this){
            final Buffer this$0;
            {
                this.this$0 = buffer;
            }

            @Override
            public void close() {
            }

            @Override
            public void flush() {
            }

            public String toString() {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(this);
                stringBuilder.append(".outputStream()");
                return stringBuilder.toString();
            }

            @Override
            public void write(int n) {
                this.this$0.writeByte((byte)n);
            }

            @Override
            public void write(byte[] byArray, int n, int n2) {
                this.this$0.write(byArray, n, n2);
            }
        };
    }

    boolean rangeEquals(long l, ByteString byteString) {
        int n = byteString.size();
        if (this.size - l < (long)n) {
            return false;
        }
        for (int i = 0; i < n; ++i) {
            if (this.getByte((long)i + l) == byteString.getByte(i)) continue;
            return false;
        }
        return true;
    }

    @Override
    public int read(byte[] byArray) {
        return this.read(byArray, 0, byArray.length);
    }

    @Override
    public int read(byte[] byArray, int n, int n2) {
        Util.checkOffsetAndCount(byArray.length, n, n2);
        Segment segment = this.head;
        if (segment == null) {
            return -1;
        }
        n2 = Math.min(n2, segment.limit - segment.pos);
        System.arraycopy(segment.data, segment.pos, byArray, n, n2);
        segment.pos += n2;
        this.size -= (long)n2;
        if (segment.pos == segment.limit) {
            this.head = segment.pop();
            SegmentPool.recycle(segment);
        }
        return n2;
    }

    @Override
    public long read(Buffer object, long l) {
        if (object != null) {
            if (l >= 0L) {
                long l2 = this.size;
                if (l2 == 0L) {
                    return -1L;
                }
                long l3 = l;
                if (l > l2) {
                    l3 = this.size;
                }
                ((Buffer)object).write(this, l3);
                return l3;
            }
            object = new StringBuilder();
            ((StringBuilder)object).append("byteCount < 0: ");
            ((StringBuilder)object).append(l);
            throw new IllegalArgumentException(((StringBuilder)object).toString());
        }
        throw new IllegalArgumentException("sink == null");
    }

    @Override
    public long readAll(Sink sink) throws IOException {
        long l = this.size;
        if (l > 0L) {
            sink.write(this, l);
        }
        return l;
    }

    @Override
    public byte readByte() {
        if (this.size != 0L) {
            Segment segment = this.head;
            int n = segment.pos;
            int n2 = segment.limit;
            byte[] byArray = segment.data;
            int n3 = n + 1;
            byte by = byArray[n];
            --this.size;
            if (n3 == n2) {
                this.head = segment.pop();
                SegmentPool.recycle(segment);
            } else {
                segment.pos = n3;
            }
            return by;
        }
        throw new IllegalStateException("size == 0");
    }

    @Override
    public byte[] readByteArray() {
        try {
            byte[] byArray = this.readByteArray(this.size);
            return byArray;
        }
        catch (EOFException eOFException) {
            throw new AssertionError((Object)eOFException);
        }
    }

    @Override
    public byte[] readByteArray(long l) throws EOFException {
        Util.checkOffsetAndCount(this.size, 0L, l);
        if (l <= Integer.MAX_VALUE) {
            byte[] byArray = new byte[(int)l];
            this.readFully(byArray);
            return byArray;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("byteCount > Integer.MAX_VALUE: ");
        stringBuilder.append(l);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    @Override
    public ByteString readByteString() {
        return new ByteString(this.readByteArray());
    }

    @Override
    public ByteString readByteString(long l) throws EOFException {
        return new ByteString(this.readByteArray(l));
    }

    @Override
    public long readDecimalLong() {
        block8: {
            if (this.size == 0L) break block8;
            long l = 0L;
            int n = 0;
            boolean bl = false;
            int n2 = 0;
            long l2 = -922337203685477580L;
            long l3 = -7L;
            do {
                Object object = this.head;
                Object object2 = ((Segment)object).data;
                int n3 = ((Segment)object).pos;
                int n4 = ((Segment)object).limit;
                while (n3 < n4) {
                    byte by;
                    block12: {
                        block11: {
                            int n5;
                            block9: {
                                block10: {
                                    by = object2[n3];
                                    if (by < 48 || by > 57) break block9;
                                    n5 = 48 - by;
                                    if (l < l2 || l == l2 && (long)n5 < l3) break block10;
                                    l = l * 10L + (long)n5;
                                    n5 = n2;
                                    break block11;
                                }
                                object = new Buffer().writeDecimalLong(l).writeByte(by);
                                if (!bl) {
                                    ((Buffer)object).readByte();
                                }
                                object2 = new StringBuilder();
                                ((StringBuilder)object2).append("Number too large: ");
                                ((StringBuilder)object2).append(((Buffer)object).readUtf8());
                                throw new NumberFormatException(((StringBuilder)object2).toString());
                            }
                            n5 = n2;
                            if (by != 45 || n != 0) break block12;
                            bl = true;
                            --l3;
                        }
                        ++n3;
                        ++n;
                        continue;
                    }
                    if (n != 0) {
                        n2 = 1;
                        break;
                    }
                    object2 = new StringBuilder();
                    ((StringBuilder)object2).append("Expected leading [0-9] or '-' character but was 0x");
                    ((StringBuilder)object2).append(Integer.toHexString(by));
                    throw new NumberFormatException(((StringBuilder)object2).toString());
                }
                if (n3 == n4) {
                    this.head = ((Segment)object).pop();
                    SegmentPool.recycle((Segment)object);
                    continue;
                }
                ((Segment)object).pos = n3;
            } while (n2 == 0 && this.head != null);
            this.size -= (long)n;
            l2 = bl ? l : -l;
            return l2;
        }
        IllegalStateException illegalStateException = new IllegalStateException("size == 0");
        throw illegalStateException;
    }

    public Buffer readFrom(InputStream inputStream) throws IOException {
        this.readFrom(inputStream, Long.MAX_VALUE, true);
        return this;
    }

    public Buffer readFrom(InputStream object, long l) throws IOException {
        if (l >= 0L) {
            this.readFrom((InputStream)object, l, false);
            return this;
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("byteCount < 0: ");
        ((StringBuilder)object).append(l);
        throw new IllegalArgumentException(((StringBuilder)object).toString());
    }

    @Override
    public void readFully(Buffer buffer, long l) throws EOFException {
        long l2 = this.size;
        if (l2 >= l) {
            buffer.write(this, l);
            return;
        }
        buffer.write(this, l2);
        throw new EOFException();
    }

    @Override
    public void readFully(byte[] byArray) throws EOFException {
        int n;
        for (int i = 0; i < byArray.length; i += n) {
            n = this.read(byArray, i, byArray.length - i);
            if (n != -1) {
                continue;
            }
            throw new EOFException();
        }
    }

    @Override
    public long readHexadecimalUnsignedLong() {
        if (this.size != 0L) {
            long l;
            int n;
            long l2 = 0L;
            int n2 = 0;
            byte by = 0;
            do {
                byte by2;
                int n3;
                int n4;
                Object object;
                block11: {
                    Object object2;
                    block12: {
                        object = this.head;
                        object2 = ((Segment)object).data;
                        n4 = ((Segment)object).pos;
                        n3 = ((Segment)object).limit;
                        n = n2;
                        l = l2;
                        while (true) {
                            by2 = by;
                            if (n4 >= n3) break block11;
                            by2 = object2[n4];
                            if (by2 >= 48 && by2 <= 57) {
                                n2 = by2 - 48;
                            } else if (by2 >= 97 && by2 <= 102) {
                                n2 = by2 - 97 + 10;
                            } else {
                                if (by2 < 65 || by2 > 70) break block12;
                                n2 = by2 - 65 + 10;
                            }
                            if ((0xF000000000000000L & l) != 0L) break;
                            l = l << 4 | (long)n2;
                            ++n4;
                            ++n;
                        }
                        object = new Buffer().writeHexadecimalUnsignedLong(l).writeByte(by2);
                        object2 = new StringBuilder();
                        ((StringBuilder)object2).append("Number too large: ");
                        ((StringBuilder)object2).append(((Buffer)object).readUtf8());
                        throw new NumberFormatException(((StringBuilder)object2).toString());
                    }
                    if (n != 0) {
                        by2 = 1;
                    } else {
                        object2 = new StringBuilder();
                        ((StringBuilder)object2).append("Expected leading [0-9a-fA-F] character but was 0x");
                        ((StringBuilder)object2).append(Integer.toHexString(by2));
                        throw new NumberFormatException(((StringBuilder)object2).toString());
                    }
                }
                if (n4 == n3) {
                    this.head = ((Segment)object).pop();
                    SegmentPool.recycle((Segment)object);
                } else {
                    ((Segment)object).pos = n4;
                }
                if (by2 != 0) break;
                l2 = l;
                n2 = n;
                by = by2;
            } while (this.head != null);
            this.size -= (long)n;
            return l;
        }
        IllegalStateException illegalStateException = new IllegalStateException("size == 0");
        throw illegalStateException;
    }

    @Override
    public int readInt() {
        if (this.size >= 4L) {
            Segment segment = this.head;
            int n = segment.limit;
            int n2 = segment.pos;
            if (n - n2 < 4) {
                return (this.readByte() & 0xFF) << 24 | (this.readByte() & 0xFF) << 16 | (this.readByte() & 0xFF) << 8 | this.readByte() & 0xFF;
            }
            byte[] byArray = segment.data;
            int n3 = n2 + 1;
            n2 = byArray[n2];
            int n4 = n3 + 1;
            n3 = byArray[n3];
            int n5 = n4 + 1;
            n4 = byArray[n4];
            int n6 = n5 + 1;
            n5 = byArray[n5];
            this.size -= 4L;
            if (n6 == n) {
                this.head = segment.pop();
                SegmentPool.recycle(segment);
            } else {
                segment.pos = n6;
            }
            return (n2 & 0xFF) << 24 | (n3 & 0xFF) << 16 | (n4 & 0xFF) << 8 | n5 & 0xFF;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("size < 4: ");
        stringBuilder.append(this.size);
        throw new IllegalStateException(stringBuilder.toString());
    }

    @Override
    public int readIntLe() {
        return Util.reverseBytesInt(this.readInt());
    }

    @Override
    public long readLong() {
        if (this.size >= 8L) {
            Segment segment = this.head;
            int n = segment.limit;
            int n2 = segment.pos;
            if (n - n2 < 8) {
                return ((long)this.readInt() & 0xFFFFFFFFL) << 32 | (long)this.readInt() & 0xFFFFFFFFL;
            }
            byte[] byArray = segment.data;
            int n3 = n2 + 1;
            long l = byArray[n2];
            n2 = n3 + 1;
            long l2 = byArray[n3];
            n3 = n2 + 1;
            long l3 = byArray[n2];
            int n4 = n3 + 1;
            long l4 = byArray[n3];
            n2 = n4 + 1;
            long l5 = byArray[n4];
            n3 = n2 + 1;
            long l6 = byArray[n2];
            n2 = n3 + 1;
            long l7 = byArray[n3];
            n3 = n2 + 1;
            long l8 = byArray[n2];
            this.size -= 8L;
            if (n3 == n) {
                this.head = segment.pop();
                SegmentPool.recycle(segment);
            } else {
                segment.pos = n3;
            }
            return (l7 & 0xFFL) << 8 | ((l & 0xFFL) << 56 | (l2 & 0xFFL) << 48 | (l3 & 0xFFL) << 40 | (l4 & 0xFFL) << 32 | (l5 & 0xFFL) << 24 | (l6 & 0xFFL) << 16) | l8 & 0xFFL;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("size < 8: ");
        stringBuilder.append(this.size);
        throw new IllegalStateException(stringBuilder.toString());
    }

    @Override
    public long readLongLe() {
        return Util.reverseBytesLong(this.readLong());
    }

    @Override
    public short readShort() {
        if (this.size >= 2L) {
            Segment segment = this.head;
            int n = segment.limit;
            int n2 = segment.pos;
            if (n - n2 < 2) {
                return (short)((this.readByte() & 0xFF) << 8 | this.readByte() & 0xFF);
            }
            byte[] byArray = segment.data;
            int n3 = n2 + 1;
            byte by = byArray[n2];
            n2 = n3 + 1;
            n3 = byArray[n3];
            this.size -= 2L;
            if (n2 == n) {
                this.head = segment.pop();
                SegmentPool.recycle(segment);
            } else {
                segment.pos = n2;
            }
            return (short)((by & 0xFF) << 8 | n3 & 0xFF);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("size < 2: ");
        stringBuilder.append(this.size);
        throw new IllegalStateException(stringBuilder.toString());
    }

    @Override
    public short readShortLe() {
        return Util.reverseBytesShort(this.readShort());
    }

    @Override
    public String readString(long l, Charset object) throws EOFException {
        Util.checkOffsetAndCount(this.size, 0L, l);
        if (object != null) {
            if (l <= Integer.MAX_VALUE) {
                if (l == 0L) {
                    return "";
                }
                Segment segment = this.head;
                if ((long)segment.pos + l > (long)segment.limit) {
                    return new String(this.readByteArray(l), (Charset)object);
                }
                object = new String(segment.data, segment.pos, (int)l, (Charset)object);
                segment.pos = (int)((long)segment.pos + l);
                this.size -= l;
                if (segment.pos == segment.limit) {
                    this.head = segment.pop();
                    SegmentPool.recycle(segment);
                }
                return object;
            }
            object = new StringBuilder();
            ((StringBuilder)object).append("byteCount > Integer.MAX_VALUE: ");
            ((StringBuilder)object).append(l);
            throw new IllegalArgumentException(((StringBuilder)object).toString());
        }
        throw new IllegalArgumentException("charset == null");
    }

    @Override
    public String readString(Charset object) {
        try {
            object = this.readString(this.size, (Charset)object);
            return object;
        }
        catch (EOFException eOFException) {
            throw new AssertionError((Object)eOFException);
        }
    }

    @Override
    public String readUtf8() {
        try {
            String string2 = this.readString(this.size, Util.UTF_8);
            return string2;
        }
        catch (EOFException eOFException) {
            throw new AssertionError((Object)eOFException);
        }
    }

    @Override
    public String readUtf8(long l) throws EOFException {
        return this.readString(l, Util.UTF_8);
    }

    @Override
    public int readUtf8CodePoint() throws EOFException {
        block9: {
            block14: {
                int n;
                int n2;
                int n3;
                int n4;
                block11: {
                    block13: {
                        block12: {
                            block10: {
                                if (this.size == 0L) break block9;
                                n4 = this.getByte(0L);
                                if ((n4 & 0x80) != 0) break block10;
                                n3 = n4 & 0x7F;
                                n2 = 1;
                                n = 0;
                                break block11;
                            }
                            if ((n4 & 0xE0) != 192) break block12;
                            n3 = n4 & 0x1F;
                            n2 = 2;
                            n = 128;
                            break block11;
                        }
                        if ((n4 & 0xF0) != 224) break block13;
                        n3 = n4 & 0xF;
                        n2 = 3;
                        n = 2048;
                        break block11;
                    }
                    if ((n4 & 0xF8) != 240) break block14;
                    n3 = n4 & 7;
                    n2 = 4;
                    n = 65536;
                }
                if (this.size >= (long)n2) {
                    for (n4 = 1; n4 < n2; ++n4) {
                        byte by = this.getByte(n4);
                        if ((by & 0xC0) == 128) {
                            n3 = n3 << 6 | by & 0x3F;
                            continue;
                        }
                        this.skip(n4);
                        return 65533;
                    }
                    this.skip(n2);
                    if (n3 > 0x10FFFF) {
                        return 65533;
                    }
                    if (n3 >= 55296 && n3 <= 57343) {
                        return 65533;
                    }
                    if (n3 < n) {
                        return 65533;
                    }
                    return n3;
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("size < ");
                stringBuilder.append(n2);
                stringBuilder.append(": ");
                stringBuilder.append(this.size);
                stringBuilder.append(" (to read code point prefixed 0x");
                stringBuilder.append(Integer.toHexString(n4));
                stringBuilder.append(")");
                throw new EOFException(stringBuilder.toString());
            }
            this.skip(1L);
            return 65533;
        }
        EOFException eOFException = new EOFException();
        throw eOFException;
    }

    @Override
    public String readUtf8Line() throws EOFException {
        long l = this.indexOf((byte)10);
        if (l == -1L) {
            l = this.size;
            String string2 = l != 0L ? this.readUtf8(l) : null;
            return string2;
        }
        return this.readUtf8Line(l);
    }

    String readUtf8Line(long l) throws EOFException {
        if (l > 0L && this.getByte(l - 1L) == 13) {
            String string2 = this.readUtf8(l - 1L);
            this.skip(2L);
            return string2;
        }
        String string3 = this.readUtf8(l);
        this.skip(1L);
        return string3;
    }

    @Override
    public String readUtf8LineStrict() throws EOFException {
        long l = this.indexOf((byte)10);
        if (l != -1L) {
            return this.readUtf8Line(l);
        }
        Buffer buffer = new Buffer();
        this.copyTo(buffer, 0L, Math.min(32L, this.size));
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\\n not found: size=");
        stringBuilder.append(this.size());
        stringBuilder.append(" content=");
        stringBuilder.append(buffer.readByteString().hex());
        stringBuilder.append("\u2026");
        throw new EOFException(stringBuilder.toString());
    }

    @Override
    public boolean request(long l) {
        boolean bl = this.size >= l;
        return bl;
    }

    @Override
    public void require(long l) throws EOFException {
        if (this.size >= l) {
            return;
        }
        throw new EOFException();
    }

    List<Integer> segmentSizes() {
        if (this.head == null) {
            return Collections.emptyList();
        }
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        arrayList.add(this.head.limit - this.head.pos);
        Segment segment = this.head.next;
        while (segment != this.head) {
            arrayList.add(segment.limit - segment.pos);
            segment = segment.next;
        }
        return arrayList;
    }

    @Override
    public int select(Options byteStringArray) {
        Segment segment = this.head;
        if (segment == null) {
            return byteStringArray.indexOf(ByteString.EMPTY);
        }
        byteStringArray = byteStringArray.byteStrings;
        int n = byteStringArray.length;
        for (int i = 0; i < n; ++i) {
            ByteString byteString = byteStringArray[i];
            if (this.size < (long)byteString.size() || !this.rangeEquals(segment, segment.pos, byteString, 0, byteString.size())) continue;
            try {
                this.skip(byteString.size());
                return i;
            }
            catch (EOFException eOFException) {
                throw new AssertionError((Object)eOFException);
            }
        }
        return -1;
    }

    int selectPrefix(Options object) {
        Segment segment = this.head;
        ByteString[] byteStringArray = ((Options)object).byteStrings;
        int n = byteStringArray.length;
        for (int i = 0; i < n; ++i) {
            object = byteStringArray[i];
            int n2 = (int)Math.min(this.size, (long)((ByteString)object).size());
            if (n2 != 0 && !this.rangeEquals(segment, segment.pos, (ByteString)object, 0, n2)) {
                continue;
            }
            return i;
        }
        return -1;
    }

    public ByteString sha1() {
        return this.digest("SHA-1");
    }

    public ByteString sha256() {
        return this.digest("SHA-256");
    }

    public long size() {
        return this.size;
    }

    @Override
    public void skip(long l) throws EOFException {
        while (l > 0L) {
            Segment segment = this.head;
            if (segment != null) {
                int n = (int)Math.min(l, (long)(segment.limit - this.head.pos));
                this.size -= (long)n;
                l -= (long)n;
                segment = this.head;
                segment.pos += n;
                if (this.head.pos != this.head.limit) continue;
                segment = this.head;
                this.head = segment.pop();
                SegmentPool.recycle(segment);
                continue;
            }
            throw new EOFException();
        }
    }

    public ByteString snapshot() {
        long l = this.size;
        if (l <= Integer.MAX_VALUE) {
            return this.snapshot((int)l);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("size > Integer.MAX_VALUE: ");
        stringBuilder.append(this.size);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public ByteString snapshot(int n) {
        if (n == 0) {
            return ByteString.EMPTY;
        }
        return new SegmentedByteString(this, n);
    }

    @Override
    public Timeout timeout() {
        return Timeout.NONE;
    }

    public String toString() {
        return this.snapshot().toString();
    }

    Segment writableSegment(int n) {
        block4: {
            Segment segment;
            block6: {
                Segment segment2;
                block5: {
                    if (n < 1 || n > 8192) break block4;
                    segment = this.head;
                    if (segment == null) {
                        this.head = segment = SegmentPool.take();
                        segment.prev = segment;
                        segment.next = segment;
                        return segment;
                    }
                    segment2 = segment.prev;
                    if (segment2.limit + n > 8192) break block5;
                    segment = segment2;
                    if (segment2.owner) break block6;
                }
                segment = segment2.push(SegmentPool.take());
            }
            return segment;
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Buffer write(ByteString byteString) {
        if (byteString != null) {
            byteString.write(this);
            return this;
        }
        throw new IllegalArgumentException("byteString == null");
    }

    @Override
    public Buffer write(byte[] byArray) {
        if (byArray != null) {
            return this.write(byArray, 0, byArray.length);
        }
        throw new IllegalArgumentException("source == null");
    }

    @Override
    public Buffer write(byte[] object, int n, int n2) {
        if (object != null) {
            Util.checkOffsetAndCount(((byte[])object).length, n, n2);
            int n3 = n + n2;
            while (n < n3) {
                Segment segment = this.writableSegment(1);
                int n4 = Math.min(n3 - n, 8192 - segment.limit);
                System.arraycopy(object, n, segment.data, segment.limit, n4);
                n += n4;
                segment.limit += n4;
            }
            this.size += (long)n2;
            return this;
        }
        object = new IllegalArgumentException("source == null");
        throw object;
    }

    @Override
    public BufferedSink write(Source source, long l) throws IOException {
        while (l > 0L) {
            long l2 = source.read(this, l);
            if (l2 != -1L) {
                l -= l2;
                continue;
            }
            throw new EOFException();
        }
        return this;
    }

    @Override
    public void write(Buffer object, long l) {
        if (object != null) {
            if (object != this) {
                Util.checkOffsetAndCount(((Buffer)object).size, 0L, l);
                while (l > 0L) {
                    long l2;
                    Segment segment;
                    if (l < (long)(((Buffer)object).head.limit - ((Buffer)object).head.pos)) {
                        int n;
                        segment = this.head;
                        segment = segment != null ? segment.prev : null;
                        if (segment != null && segment.owner && (l2 = (long)segment.limit) + l - (long)(n = segment.shared ? 0 : segment.pos) <= 8192L) {
                            ((Buffer)object).head.writeTo(segment, (int)l);
                            ((Buffer)object).size -= l;
                            this.size += l;
                            return;
                        }
                        ((Buffer)object).head = ((Buffer)object).head.split((int)l);
                    }
                    segment = ((Buffer)object).head;
                    l2 = segment.limit - segment.pos;
                    ((Buffer)object).head = segment.pop();
                    Segment segment2 = this.head;
                    if (segment2 == null) {
                        this.head = segment;
                        segment.prev = segment;
                        segment.next = segment;
                    } else {
                        segment2.prev.push(segment).compact();
                    }
                    ((Buffer)object).size -= l2;
                    this.size += l2;
                    l -= l2;
                }
                return;
            }
            throw new IllegalArgumentException("source == this");
        }
        object = new IllegalArgumentException("source == null");
        throw object;
    }

    @Override
    public long writeAll(Source object) throws IOException {
        if (object != null) {
            long l;
            long l2 = 0L;
            while ((l = object.read(this, 8192L)) != -1L) {
                l2 += l;
            }
            return l2;
        }
        object = new IllegalArgumentException("source == null");
        throw object;
    }

    @Override
    public Buffer writeByte(int n) {
        Segment segment = this.writableSegment(1);
        byte[] byArray = segment.data;
        int n2 = segment.limit;
        segment.limit = n2 + 1;
        byArray[n2] = (byte)n;
        ++this.size;
        return this;
    }

    @Override
    public Buffer writeDecimalLong(long l) {
        if (l == 0L) {
            return this.writeByte(48);
        }
        boolean bl = false;
        long l2 = l;
        if (l < 0L) {
            l2 = -l;
            if (l2 < 0L) {
                return this.writeUtf8("-9223372036854775808");
            }
            bl = true;
        }
        int n = l2 < 100000000L ? (l2 < 10000L ? (l2 < 100L ? (l2 < 10L ? 1 : 2) : (l2 < 1000L ? 3 : 4)) : (l2 < 1000000L ? (l2 < 100000L ? 5 : 6) : (l2 < 10000000L ? 7 : 8))) : (l2 < 1000000000000L ? (l2 < 10000000000L ? (l2 < 1000000000L ? 9 : 10) : (l2 < 100000000000L ? 11 : 12)) : (l2 < 1000000000000000L ? (l2 < 10000000000000L ? 13 : (l2 < 100000000000000L ? 14 : 15)) : (l2 < 100000000000000000L ? (l2 < 10000000000000000L ? 16 : 17) : (l2 < 1000000000000000000L ? 18 : 19))));
        int n2 = n;
        if (bl) {
            n2 = n + 1;
        }
        Segment segment = this.writableSegment(n2);
        byte[] byArray = segment.data;
        n = segment.limit + n2;
        while (l2 != 0L) {
            int n3 = (int)(l2 % 10L);
            byArray[--n] = DIGITS[n3];
            l2 /= 10L;
        }
        if (bl) {
            byArray[n - 1] = 45;
        }
        segment.limit += n2;
        this.size += (long)n2;
        return this;
    }

    @Override
    public Buffer writeHexadecimalUnsignedLong(long l) {
        if (l == 0L) {
            return this.writeByte(48);
        }
        int n = Long.numberOfTrailingZeros(Long.highestOneBit(l)) / 4 + 1;
        Segment segment = this.writableSegment(n);
        byte[] byArray = segment.data;
        int n2 = segment.limit;
        for (int i = segment.limit + n - 1; i >= n2; --i) {
            byArray[i] = DIGITS[(int)(0xFL & l)];
            l >>>= 4;
        }
        segment.limit += n;
        this.size += (long)n;
        return this;
    }

    @Override
    public Buffer writeInt(int n) {
        Segment segment = this.writableSegment(4);
        byte[] byArray = segment.data;
        int n2 = segment.limit;
        int n3 = n2 + 1;
        byArray[n2] = (byte)(n >>> 24 & 0xFF);
        n2 = n3 + 1;
        byArray[n3] = (byte)(n >>> 16 & 0xFF);
        n3 = n2 + 1;
        byArray[n2] = (byte)(n >>> 8 & 0xFF);
        byArray[n3] = (byte)(n & 0xFF);
        segment.limit = n3 + 1;
        this.size += 4L;
        return this;
    }

    @Override
    public Buffer writeIntLe(int n) {
        return this.writeInt(Util.reverseBytesInt(n));
    }

    @Override
    public Buffer writeLong(long l) {
        Segment segment = this.writableSegment(8);
        byte[] byArray = segment.data;
        int n = segment.limit;
        int n2 = n + 1;
        byArray[n] = (byte)(l >>> 56 & 0xFFL);
        int n3 = n2 + 1;
        byArray[n2] = (byte)(l >>> 48 & 0xFFL);
        n = n3 + 1;
        byArray[n3] = (byte)(l >>> 40 & 0xFFL);
        n2 = n + 1;
        byArray[n] = (byte)(l >>> 32 & 0xFFL);
        n = n2 + 1;
        byArray[n2] = (byte)(l >>> 24 & 0xFFL);
        n2 = n + 1;
        byArray[n] = (byte)(l >>> 16 & 0xFFL);
        n = n2 + 1;
        byArray[n2] = (byte)(l >>> 8 & 0xFFL);
        byArray[n] = (byte)(l & 0xFFL);
        segment.limit = n + 1;
        this.size += 8L;
        return this;
    }

    @Override
    public Buffer writeLongLe(long l) {
        return this.writeLong(Util.reverseBytesLong(l));
    }

    @Override
    public Buffer writeShort(int n) {
        Segment segment = this.writableSegment(2);
        byte[] byArray = segment.data;
        int n2 = segment.limit;
        int n3 = n2 + 1;
        byArray[n2] = (byte)(n >>> 8 & 0xFF);
        byArray[n3] = (byte)(n & 0xFF);
        segment.limit = n3 + 1;
        this.size += 2L;
        return this;
    }

    @Override
    public Buffer writeShortLe(int n) {
        return this.writeShort(Util.reverseBytesShort((short)n));
    }

    @Override
    public Buffer writeString(String object, int n, int n2, Charset comparable) {
        if (object != null) {
            if (n >= 0) {
                if (n2 >= n) {
                    if (n2 <= ((String)object).length()) {
                        if (comparable != null) {
                            if (((Charset)comparable).equals(Util.UTF_8)) {
                                return this.writeUtf8((String)object);
                            }
                            object = ((String)object).substring(n, n2).getBytes((Charset)comparable);
                            return this.write((byte[])object, 0, ((Object)object).length);
                        }
                        throw new IllegalArgumentException("charset == null");
                    }
                    comparable = new StringBuilder();
                    ((StringBuilder)comparable).append("endIndex > string.length: ");
                    ((StringBuilder)comparable).append(n2);
                    ((StringBuilder)comparable).append(" > ");
                    ((StringBuilder)comparable).append(((String)object).length());
                    throw new IllegalArgumentException(((StringBuilder)comparable).toString());
                }
                object = new StringBuilder();
                ((StringBuilder)object).append("endIndex < beginIndex: ");
                ((StringBuilder)object).append(n2);
                ((StringBuilder)object).append(" < ");
                ((StringBuilder)object).append(n);
                throw new IllegalArgumentException(((StringBuilder)object).toString());
            }
            object = new StringBuilder();
            ((StringBuilder)object).append("beginIndex < 0: ");
            ((StringBuilder)object).append(n);
            throw new IllegalAccessError(((StringBuilder)object).toString());
        }
        throw new IllegalArgumentException("string == null");
    }

    @Override
    public Buffer writeString(String string2, Charset charset) {
        return this.writeString(string2, 0, string2.length(), charset);
    }

    public Buffer writeTo(OutputStream outputStream) throws IOException {
        return this.writeTo(outputStream, this.size);
    }

    public Buffer writeTo(OutputStream object, long l) throws IOException {
        if (object != null) {
            Util.checkOffsetAndCount(this.size, 0L, l);
            Segment segment = this.head;
            while (l > 0L) {
                int n = (int)Math.min(l, (long)(segment.limit - segment.pos));
                ((OutputStream)object).write(segment.data, segment.pos, n);
                segment.pos += n;
                this.size -= (long)n;
                l -= (long)n;
                Segment segment2 = segment;
                if (segment.pos == segment.limit) {
                    Segment segment3;
                    segment2 = segment3 = segment.pop();
                    this.head = segment3;
                    SegmentPool.recycle(segment);
                }
                segment = segment2;
            }
            return this;
        }
        object = new IllegalArgumentException("out == null");
        throw object;
    }

    @Override
    public Buffer writeUtf8(String string2) {
        return this.writeUtf8(string2, 0, string2.length());
    }

    @Override
    public Buffer writeUtf8(String object, int n, int n2) {
        if (object != null) {
            if (n >= 0) {
                if (n2 >= n) {
                    if (n2 <= ((String)object).length()) {
                        while (n < n2) {
                            int n3;
                            char c = ((String)object).charAt(n);
                            if (c < '\u0080') {
                                Segment segment = this.writableSegment(1);
                                byte[] byArray = segment.data;
                                int n4 = segment.limit - n;
                                int n5 = Math.min(n2, 8192 - n4);
                                n3 = n + 1;
                                byArray[n + n4] = (byte)c;
                                for (n = n3; n < n5 && (n3 = (int)((String)object).charAt(n)) < 128; ++n) {
                                    byArray[n + n4] = (byte)n3;
                                }
                                n3 = n + n4 - segment.limit;
                                segment.limit += n3;
                                this.size += (long)n3;
                                continue;
                            }
                            if (c < '\u0800') {
                                this.writeByte(c >> 6 | 0xC0);
                                this.writeByte(0x80 | c & 0x3F);
                                ++n;
                                continue;
                            }
                            if (c >= '\ud800' && c <= '\udfff') {
                                n3 = n + 1 < n2 ? ((String)object).charAt(n + 1) : 0;
                                if (c <= '\udbff' && n3 >= 56320 && n3 <= 57343) {
                                    n3 = ((0xFFFF27FF & c) << 10 | 0xFFFF23FF & n3) + 65536;
                                    this.writeByte(n3 >> 18 | 0xF0);
                                    this.writeByte(n3 >> 12 & 0x3F | 0x80);
                                    this.writeByte(n3 >> 6 & 0x3F | 0x80);
                                    this.writeByte(0x80 | n3 & 0x3F);
                                    n += 2;
                                    continue;
                                }
                                this.writeByte(63);
                                ++n;
                                continue;
                            }
                            this.writeByte(c >> 12 | 0xE0);
                            this.writeByte(c >> 6 & 0x3F | 0x80);
                            this.writeByte(0x80 | c & 0x3F);
                            ++n;
                        }
                        return this;
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("endIndex > string.length: ");
                    stringBuilder.append(n2);
                    stringBuilder.append(" > ");
                    stringBuilder.append(((String)object).length());
                    throw new IllegalArgumentException(stringBuilder.toString());
                }
                object = new StringBuilder();
                ((StringBuilder)object).append("endIndex < beginIndex: ");
                ((StringBuilder)object).append(n2);
                ((StringBuilder)object).append(" < ");
                ((StringBuilder)object).append(n);
                throw new IllegalArgumentException(((StringBuilder)object).toString());
            }
            object = new StringBuilder();
            ((StringBuilder)object).append("beginIndex < 0: ");
            ((StringBuilder)object).append(n);
            throw new IllegalAccessError(((StringBuilder)object).toString());
        }
        object = new IllegalArgumentException("string == null");
        throw object;
    }

    @Override
    public Buffer writeUtf8CodePoint(int n) {
        block8: {
            block5: {
                block7: {
                    block6: {
                        block4: {
                            if (n >= 128) break block4;
                            this.writeByte(n);
                            break block5;
                        }
                        if (n >= 2048) break block6;
                        this.writeByte(n >> 6 | 0xC0);
                        this.writeByte(0x80 | n & 0x3F);
                        break block5;
                    }
                    if (n >= 65536) break block7;
                    if (n >= 55296 && n <= 57343) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Unexpected code point: ");
                        stringBuilder.append(Integer.toHexString(n));
                        throw new IllegalArgumentException(stringBuilder.toString());
                    }
                    this.writeByte(n >> 12 | 0xE0);
                    this.writeByte(n >> 6 & 0x3F | 0x80);
                    this.writeByte(0x80 | n & 0x3F);
                    break block5;
                }
                if (n > 0x10FFFF) break block8;
                this.writeByte(n >> 18 | 0xF0);
                this.writeByte(n >> 12 & 0x3F | 0x80);
                this.writeByte(n >> 6 & 0x3F | 0x80);
                this.writeByte(0x80 | n & 0x3F);
            }
            return this;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unexpected code point: ");
        stringBuilder.append(Integer.toHexString(n));
        throw new IllegalArgumentException(stringBuilder.toString());
    }
}

