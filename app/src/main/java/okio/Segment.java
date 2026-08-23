/*
 * Decompiled with CFR 0.152.
 */
package okio;

import okio.SegmentPool;

final class Segment {
    static final int SHARE_MINIMUM = 1024;
    static final int SIZE = 8192;
    final byte[] data;
    int limit;
    Segment next;
    boolean owner;
    int pos;
    Segment prev;
    boolean shared;

    Segment() {
        this.data = new byte[8192];
        this.owner = true;
        this.shared = false;
    }

    Segment(Segment segment) {
        this(segment.data, segment.pos, segment.limit);
        segment.shared = true;
    }

    Segment(byte[] byArray, int n, int n2) {
        this.data = byArray;
        this.pos = n;
        this.limit = n2;
        this.owner = false;
        this.shared = true;
    }

    public void compact() {
        Segment segment = this.prev;
        if (segment != this) {
            if (!segment.owner) {
                return;
            }
            int n = this.limit - this.pos;
            int n2 = segment.limit;
            int n3 = segment.shared ? 0 : segment.pos;
            if (n > 8192 - n2 + n3) {
                return;
            }
            this.writeTo(segment, n);
            this.pop();
            SegmentPool.recycle(this);
            return;
        }
        throw new IllegalStateException();
    }

    public Segment pop() {
        Segment segment = this.next;
        Segment segment2 = segment != this ? segment : null;
        Segment segment3 = this.prev;
        segment3.next = segment;
        this.next.prev = segment3;
        this.next = null;
        this.prev = null;
        return segment2;
    }

    public Segment push(Segment segment) {
        segment.prev = this;
        segment.next = this.next;
        this.next.prev = segment;
        this.next = segment;
        return segment;
    }

    public Segment split(int n) {
        if (n > 0 && n <= this.limit - this.pos) {
            Segment segment;
            if (n >= 1024) {
                segment = new Segment(this);
            } else {
                segment = SegmentPool.take();
                System.arraycopy(this.data, this.pos, segment.data, 0, n);
            }
            segment.limit = segment.pos + n;
            this.pos += n;
            this.prev.push(segment);
            return segment;
        }
        throw new IllegalArgumentException();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void writeTo(Segment segment, int n) {
        if (!segment.owner) throw new IllegalArgumentException();
        int n2 = segment.limit;
        if (n2 + n > 8192) {
            if (segment.shared) throw new IllegalArgumentException();
            int n3 = segment.pos;
            if (n2 + n - n3 > 8192) throw new IllegalArgumentException();
            byte[] byArray = segment.data;
            System.arraycopy(byArray, n3, byArray, 0, n2 - n3);
            segment.limit -= segment.pos;
            segment.pos = 0;
        }
        System.arraycopy(this.data, this.pos, segment.data, segment.limit, n);
        segment.limit += n;
        this.pos += n;
    }
}

