/*
 * Decompiled with CFR 0.152.
 */
package okio;

import okio.Segment;

final class SegmentPool {
    static final long MAX_SIZE = 65536L;
    static long byteCount;
    static Segment next;

    private SegmentPool() {
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    static void recycle(Segment segment) {
        if (segment.next == null && segment.prev == null) {
            if (segment.shared) {
                return;
            }
            synchronized (SegmentPool.class) {
                long l = byteCount;
                if (l + 8192L > 65536L) {
                    return;
                }
                byteCount = l + 8192L;
                segment.next = next;
                segment.limit = 0;
                segment.pos = 0;
                next = segment;
                return;
            }
        }
        throw new IllegalArgumentException();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    static Segment take() {
        synchronized (SegmentPool.class) {
            Segment segment = next;
            if (segment != null) {
                next = segment.next;
                segment.next = null;
                byteCount -= 8192L;
                return segment;
            }
            return new Segment();
        }
    }
}

