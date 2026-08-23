/*
 * Decompiled with CFR 0.152.
 */
package okio;

import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.Deflater;
import okio.Buffer;
import okio.BufferedSink;
import okio.DeflaterSink;
import okio.Okio;
import okio.Segment;
import okio.Sink;
import okio.Timeout;
import okio.Util;

public final class GzipSink
implements Sink {
    private boolean closed;
    private final CRC32 crc = new CRC32();
    private final Deflater deflater;
    private final DeflaterSink deflaterSink;
    private final BufferedSink sink;

    public GzipSink(Sink sink) {
        if (sink != null) {
            Deflater deflater;
            this.deflater = deflater = new Deflater(-1, true);
            sink = Okio.buffer(sink);
            this.sink = sink;
            this.deflaterSink = new DeflaterSink((BufferedSink)sink, deflater);
            this.writeHeader();
            return;
        }
        throw new IllegalArgumentException("sink == null");
    }

    private void updateCrc(Buffer object, long l) {
        object = ((Buffer)object).head;
        while (l > 0L) {
            int n = (int)Math.min(l, (long)(((Segment)object).limit - ((Segment)object).pos));
            this.crc.update(((Segment)object).data, ((Segment)object).pos, n);
            l -= (long)n;
            object = ((Segment)object).next;
        }
    }

    private void writeFooter() throws IOException {
        this.sink.writeIntLe((int)this.crc.getValue());
        this.sink.writeIntLe(this.deflater.getTotalIn());
    }

    private void writeHeader() {
        Buffer buffer = this.sink.buffer();
        buffer.writeShort(8075);
        buffer.writeByte(8);
        buffer.writeByte(0);
        buffer.writeInt(0);
        buffer.writeByte(0);
        buffer.writeByte(0);
    }

    @Override
    public void close() throws IOException {
        Throwable throwable;
        block9: {
            Throwable throwable2;
            block8: {
                if (this.closed) {
                    return;
                }
                throwable = null;
                try {
                    this.deflaterSink.finishDeflate();
                    this.writeFooter();
                }
                catch (Throwable throwable3) {
                    // empty catch block
                }
                try {
                    this.deflater.end();
                    throwable2 = throwable;
                }
                catch (Throwable throwable4) {
                    throwable2 = throwable;
                    if (throwable != null) break block8;
                    throwable2 = throwable4;
                }
            }
            try {
                this.sink.close();
                throwable = throwable2;
            }
            catch (Throwable throwable5) {
                throwable = throwable2;
                if (throwable2 != null) break block9;
                throwable = throwable5;
            }
        }
        this.closed = true;
        if (throwable != null) {
            Util.sneakyRethrow(throwable);
        }
    }

    @Override
    public void flush() throws IOException {
        this.deflaterSink.flush();
    }

    @Override
    public Timeout timeout() {
        return this.sink.timeout();
    }

    @Override
    public void write(Buffer object, long l) throws IOException {
        if (l >= 0L) {
            if (l == 0L) {
                return;
            }
            this.updateCrc((Buffer)object, l);
            this.deflaterSink.write((Buffer)object, l);
            return;
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("byteCount < 0: ");
        ((StringBuilder)object).append(l);
        throw new IllegalArgumentException(((StringBuilder)object).toString());
    }
}

