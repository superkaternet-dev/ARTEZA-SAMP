/*
 * Decompiled with CFR 0.152.
 */
package okio;

import java.io.EOFException;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import okio.Buffer;
import okio.BufferedSource;
import okio.Okio;
import okio.Segment;
import okio.SegmentPool;
import okio.Source;
import okio.Timeout;

public final class InflaterSource
implements Source {
    private int bufferBytesHeldByInflater;
    private boolean closed;
    private final Inflater inflater;
    private final BufferedSource source;

    InflaterSource(BufferedSource bufferedSource, Inflater inflater) {
        if (bufferedSource != null) {
            if (inflater != null) {
                this.source = bufferedSource;
                this.inflater = inflater;
                return;
            }
            throw new IllegalArgumentException("inflater == null");
        }
        throw new IllegalArgumentException("source == null");
    }

    public InflaterSource(Source source, Inflater inflater) {
        this(Okio.buffer(source), inflater);
    }

    private void releaseInflatedBytes() throws IOException {
        int n = this.bufferBytesHeldByInflater;
        if (n == 0) {
            return;
        }
        this.bufferBytesHeldByInflater -= (n -= this.inflater.getRemaining());
        this.source.skip(n);
    }

    @Override
    public void close() throws IOException {
        if (this.closed) {
            return;
        }
        this.inflater.end();
        this.closed = true;
        this.source.close();
    }

    /*
     * Unable to fully structure code
     */
    @Override
    public long read(Buffer var1_1, long var2_3) throws IOException {
        block9: {
            block10: {
                if (var2_3 < 0L) break block9;
                if (this.closed) break block10;
                if (var2_3 == 0L) {
                    return 0L;
                }
                while (true) {
                    block8: {
                        var5_5 = this.refill();
                        var6_6 = var1_1.writableSegment(1);
                        var4_4 = this.inflater.inflate(var6_6.data, var6_6.limit, 8192 - var6_6.limit);
                        if (var4_4 <= 0) break block8;
                        var6_6.limit += var4_4;
                        var1_1.size += (long)var4_4;
                        return var4_4;
                    }
                    if (this.inflater.finished() || this.inflater.needsDictionary()) ** GOTO lbl22
                    if (!var5_5) continue;
                    break;
                }
                try {
                    var1_1 = new EOFException("source exhausted prematurely");
                    throw var1_1;
lbl22:
                    // 1 sources

                    this.releaseInflatedBytes();
                    if (var6_6.pos == var6_6.limit) {
                        var1_1.head = var6_6.pop();
                        SegmentPool.recycle(var6_6);
                    }
                    return -1L;
                }
                catch (DataFormatException var1_2) {
                    throw new IOException(var1_2);
                }
            }
            throw new IllegalStateException("closed");
        }
        var1_1 = new StringBuilder();
        var1_1.append("byteCount < 0: ");
        var1_1.append(var2_3);
        var1_1 = new IllegalArgumentException(var1_1.toString());
        throw var1_1;
    }

    public boolean refill() throws IOException {
        if (!this.inflater.needsInput()) {
            return false;
        }
        this.releaseInflatedBytes();
        if (this.inflater.getRemaining() == 0) {
            if (this.source.exhausted()) {
                return true;
            }
            Segment segment = this.source.buffer().head;
            this.bufferBytesHeldByInflater = segment.limit - segment.pos;
            this.inflater.setInput(segment.data, segment.pos, this.bufferBytesHeldByInflater);
            return false;
        }
        throw new IllegalStateException("?");
    }

    @Override
    public Timeout timeout() {
        return this.source.timeout();
    }
}

