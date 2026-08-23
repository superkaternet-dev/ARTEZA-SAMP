/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.http;

import java.io.EOFException;
import java.io.IOException;
import java.net.ProtocolException;
import java.util.concurrent.TimeUnit;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.Internal;
import okhttp3.internal.Util;
import okhttp3.internal.http.HttpEngine;
import okhttp3.internal.http.HttpStream;
import okhttp3.internal.http.OkHeaders;
import okhttp3.internal.http.RealResponseBody;
import okhttp3.internal.http.RequestLine;
import okhttp3.internal.http.RetryableSink;
import okhttp3.internal.http.StatusLine;
import okhttp3.internal.http.StreamAllocation;
import okhttp3.internal.io.RealConnection;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ForwardingTimeout;
import okio.Okio;
import okio.Sink;
import okio.Source;
import okio.Timeout;

public final class Http1xStream
implements HttpStream {
    private static final int STATE_CLOSED = 6;
    private static final int STATE_IDLE = 0;
    private static final int STATE_OPEN_REQUEST_BODY = 1;
    private static final int STATE_OPEN_RESPONSE_BODY = 4;
    private static final int STATE_READING_RESPONSE_BODY = 5;
    private static final int STATE_READ_RESPONSE_HEADERS = 3;
    private static final int STATE_WRITING_REQUEST_BODY = 2;
    private HttpEngine httpEngine;
    private final BufferedSink sink;
    private final BufferedSource source;
    private int state = 0;
    private final StreamAllocation streamAllocation;

    public Http1xStream(StreamAllocation streamAllocation, BufferedSource bufferedSource, BufferedSink bufferedSink) {
        this.streamAllocation = streamAllocation;
        this.source = bufferedSource;
        this.sink = bufferedSink;
    }

    static /* synthetic */ int access$502(Http1xStream http1xStream, int n) {
        http1xStream.state = n;
        return n;
    }

    private void detachTimeout(ForwardingTimeout forwardingTimeout) {
        Timeout timeout = forwardingTimeout.delegate();
        forwardingTimeout.setDelegate(Timeout.NONE);
        timeout.clearDeadline();
        timeout.clearTimeout();
    }

    private Source getTransferStream(Response response) throws IOException {
        if (!HttpEngine.hasBody(response)) {
            return this.newFixedLengthSource(0L);
        }
        if ("chunked".equalsIgnoreCase(response.header("Transfer-Encoding"))) {
            return this.newChunkedSource(this.httpEngine);
        }
        long l = OkHeaders.contentLength(response);
        if (l != -1L) {
            return this.newFixedLengthSource(l);
        }
        return this.newUnknownLengthSource();
    }

    @Override
    public void cancel() {
        RealConnection realConnection = this.streamAllocation.connection();
        if (realConnection != null) {
            realConnection.cancel();
        }
    }

    @Override
    public Sink createRequestBody(Request request, long l) throws IOException {
        if ("chunked".equalsIgnoreCase(request.header("Transfer-Encoding"))) {
            return this.newChunkedSink();
        }
        if (l != -1L) {
            return this.newFixedLengthSink(l);
        }
        throw new IllegalStateException("Cannot stream a request body without chunked encoding or a known content length!");
    }

    @Override
    public void finishRequest() throws IOException {
        this.sink.flush();
    }

    public boolean isClosed() {
        boolean bl = this.state == 6;
        return bl;
    }

    public Sink newChunkedSink() {
        if (this.state == 1) {
            this.state = 2;
            return new ChunkedSink(this);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("state: ");
        stringBuilder.append(this.state);
        throw new IllegalStateException(stringBuilder.toString());
    }

    public Source newChunkedSource(HttpEngine object) throws IOException {
        if (this.state == 4) {
            this.state = 5;
            return new ChunkedSource(this, (HttpEngine)object);
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("state: ");
        ((StringBuilder)object).append(this.state);
        throw new IllegalStateException(((StringBuilder)object).toString());
    }

    public Sink newFixedLengthSink(long l) {
        if (this.state == 1) {
            this.state = 2;
            return new FixedLengthSink(this, l);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("state: ");
        stringBuilder.append(this.state);
        throw new IllegalStateException(stringBuilder.toString());
    }

    public Source newFixedLengthSource(long l) throws IOException {
        if (this.state == 4) {
            this.state = 5;
            return new FixedLengthSource(this, l);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("state: ");
        stringBuilder.append(this.state);
        throw new IllegalStateException(stringBuilder.toString());
    }

    public Source newUnknownLengthSource() throws IOException {
        if (this.state == 4) {
            StreamAllocation streamAllocation = this.streamAllocation;
            if (streamAllocation != null) {
                this.state = 5;
                streamAllocation.noNewStreams();
                return new UnknownLengthSource(this);
            }
            throw new IllegalStateException("streamAllocation == null");
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("state: ");
        stringBuilder.append(this.state);
        throw new IllegalStateException(stringBuilder.toString());
    }

    @Override
    public ResponseBody openResponseBody(Response response) throws IOException {
        Source source = this.getTransferStream(response);
        return new RealResponseBody(response.headers(), Okio.buffer(source));
    }

    public Headers readHeaders() throws IOException {
        String string2;
        Headers.Builder builder = new Headers.Builder();
        while ((string2 = this.source.readUtf8LineStrict()).length() != 0) {
            Internal.instance.addLenient(builder, string2);
        }
        return builder.build();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public Response.Builder readResponse() throws IOException {
        Object object;
        int n = this.state;
        if (n != 1 && n != 3) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("state: ");
            stringBuilder.append(this.state);
            throw new IllegalStateException(stringBuilder.toString());
        }
        try {
            StatusLine statusLine;
            do {
                statusLine = StatusLine.parse(this.source.readUtf8LineStrict());
                object = new Response.Builder();
                object = ((Response.Builder)object).protocol(statusLine.protocol).code(statusLine.code).message(statusLine.message).headers(this.readHeaders());
            } while (statusLine.code == 100);
            this.state = 4;
            return object;
        }
        catch (EOFException eOFException) {
            object = new StringBuilder();
            ((StringBuilder)object).append("unexpected end of stream on ");
            ((StringBuilder)object).append(this.streamAllocation);
            object = new IOException(((StringBuilder)object).toString());
            ((Throwable)object).initCause(eOFException);
            throw object;
        }
    }

    @Override
    public Response.Builder readResponseHeaders() throws IOException {
        return this.readResponse();
    }

    @Override
    public void setHttpEngine(HttpEngine httpEngine) {
        this.httpEngine = httpEngine;
    }

    public void writeRequest(Headers object, String string2) throws IOException {
        if (this.state == 0) {
            this.sink.writeUtf8(string2).writeUtf8("\r\n");
            int n = ((Headers)object).size();
            for (int i = 0; i < n; ++i) {
                this.sink.writeUtf8(((Headers)object).name(i)).writeUtf8(": ").writeUtf8(((Headers)object).value(i)).writeUtf8("\r\n");
            }
            this.sink.writeUtf8("\r\n");
            this.state = 1;
            return;
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("state: ");
        ((StringBuilder)object).append(this.state);
        object = new IllegalStateException(((StringBuilder)object).toString());
        throw object;
    }

    @Override
    public void writeRequestBody(RetryableSink object) throws IOException {
        if (this.state == 1) {
            this.state = 3;
            ((RetryableSink)object).writeToSocket(this.sink);
            return;
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("state: ");
        ((StringBuilder)object).append(this.state);
        throw new IllegalStateException(((StringBuilder)object).toString());
    }

    @Override
    public void writeRequestHeaders(Request request) throws IOException {
        this.httpEngine.writingRequestHeaders();
        String string2 = RequestLine.get(request, this.httpEngine.getConnection().route().proxy().type());
        this.writeRequest(request.headers(), string2);
    }

    private abstract class AbstractSource
    implements Source {
        protected boolean closed;
        final Http1xStream this$0;
        protected final ForwardingTimeout timeout;

        private AbstractSource(Http1xStream http1xStream) {
            this.this$0 = http1xStream;
            this.timeout = new ForwardingTimeout(http1xStream.source.timeout());
        }

        protected final void endOfInput(boolean bl) throws IOException {
            if (this.this$0.state == 6) {
                return;
            }
            if (this.this$0.state == 5) {
                this.this$0.detachTimeout(this.timeout);
                Http1xStream.access$502(this.this$0, 6);
                if (this.this$0.streamAllocation != null) {
                    this.this$0.streamAllocation.streamFinished(bl ^ true, this.this$0);
                }
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("state: ");
            stringBuilder.append(this.this$0.state);
            throw new IllegalStateException(stringBuilder.toString());
        }

        @Override
        public Timeout timeout() {
            return this.timeout;
        }
    }

    private final class ChunkedSink
    implements Sink {
        private boolean closed;
        final Http1xStream this$0;
        private final ForwardingTimeout timeout;

        private ChunkedSink(Http1xStream http1xStream) {
            this.this$0 = http1xStream;
            this.timeout = new ForwardingTimeout(http1xStream.sink.timeout());
        }

        @Override
        public void close() throws IOException {
            synchronized (this) {
                block4: {
                    boolean bl = this.closed;
                    if (!bl) break block4;
                    return;
                }
                this.closed = true;
                this.this$0.sink.writeUtf8("0\r\n\r\n");
                this.this$0.detachTimeout(this.timeout);
                Http1xStream.access$502(this.this$0, 3);
                return;
            }
        }

        @Override
        public void flush() throws IOException {
            synchronized (this) {
                block4: {
                    boolean bl = this.closed;
                    if (!bl) break block4;
                    return;
                }
                this.this$0.sink.flush();
                return;
            }
        }

        @Override
        public Timeout timeout() {
            return this.timeout;
        }

        @Override
        public void write(Buffer buffer, long l) throws IOException {
            if (!this.closed) {
                if (l == 0L) {
                    return;
                }
                this.this$0.sink.writeHexadecimalUnsignedLong(l);
                this.this$0.sink.writeUtf8("\r\n");
                this.this$0.sink.write(buffer, l);
                this.this$0.sink.writeUtf8("\r\n");
                return;
            }
            throw new IllegalStateException("closed");
        }
    }

    private class ChunkedSource
    extends AbstractSource {
        private static final long NO_CHUNK_YET = -1L;
        private long bytesRemainingInChunk;
        private boolean hasMoreChunks;
        private final HttpEngine httpEngine;
        final Http1xStream this$0;

        ChunkedSource(Http1xStream http1xStream, HttpEngine httpEngine) throws IOException {
            this.this$0 = http1xStream;
            super(http1xStream);
            this.bytesRemainingInChunk = -1L;
            this.hasMoreChunks = true;
            this.httpEngine = httpEngine;
        }

        private void readChunkSize() throws IOException {
            String string2;
            block4: {
                block5: {
                    if (this.bytesRemainingInChunk != -1L) {
                        this.this$0.source.readUtf8LineStrict();
                    }
                    try {
                        boolean bl;
                        this.bytesRemainingInChunk = this.this$0.source.readHexadecimalUnsignedLong();
                        string2 = this.this$0.source.readUtf8LineStrict().trim();
                        if (this.bytesRemainingInChunk < 0L || !string2.isEmpty() && !(bl = string2.startsWith(";"))) break block4;
                        if (this.bytesRemainingInChunk != 0L) break block5;
                        this.hasMoreChunks = false;
                    }
                    catch (NumberFormatException numberFormatException) {
                        throw new ProtocolException(numberFormatException.getMessage());
                    }
                    this.httpEngine.receiveHeaders(this.this$0.readHeaders());
                    this.endOfInput(true);
                }
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("expected chunk size and optional extensions but was \"");
            stringBuilder.append(this.bytesRemainingInChunk);
            stringBuilder.append(string2);
            stringBuilder.append("\"");
            ProtocolException protocolException = new ProtocolException(stringBuilder.toString());
            throw protocolException;
        }

        @Override
        public void close() throws IOException {
            if (this.closed) {
                return;
            }
            if (this.hasMoreChunks && !Util.discard(this, 100, TimeUnit.MILLISECONDS)) {
                this.endOfInput(false);
            }
            this.closed = true;
        }

        @Override
        public long read(Buffer object, long l) throws IOException {
            if (l >= 0L) {
                if (!this.closed) {
                    if (!this.hasMoreChunks) {
                        return -1L;
                    }
                    long l2 = this.bytesRemainingInChunk;
                    if (l2 == 0L || l2 == -1L) {
                        this.readChunkSize();
                        if (!this.hasMoreChunks) {
                            return -1L;
                        }
                    }
                    if ((l = this.this$0.source.read((Buffer)object, Math.min(l, this.bytesRemainingInChunk))) != -1L) {
                        this.bytesRemainingInChunk -= l;
                        return l;
                    }
                    this.endOfInput(false);
                    throw new ProtocolException("unexpected end of stream");
                }
                throw new IllegalStateException("closed");
            }
            object = new StringBuilder();
            ((StringBuilder)object).append("byteCount < 0: ");
            ((StringBuilder)object).append(l);
            throw new IllegalArgumentException(((StringBuilder)object).toString());
        }
    }

    private final class FixedLengthSink
    implements Sink {
        private long bytesRemaining;
        private boolean closed;
        final Http1xStream this$0;
        private final ForwardingTimeout timeout;

        private FixedLengthSink(Http1xStream http1xStream, long l) {
            this.this$0 = http1xStream;
            this.timeout = new ForwardingTimeout(http1xStream.sink.timeout());
            this.bytesRemaining = l;
        }

        @Override
        public void close() throws IOException {
            if (this.closed) {
                return;
            }
            this.closed = true;
            if (this.bytesRemaining <= 0L) {
                this.this$0.detachTimeout(this.timeout);
                Http1xStream.access$502(this.this$0, 3);
                return;
            }
            throw new ProtocolException("unexpected end of stream");
        }

        @Override
        public void flush() throws IOException {
            if (this.closed) {
                return;
            }
            this.this$0.sink.flush();
        }

        @Override
        public Timeout timeout() {
            return this.timeout;
        }

        @Override
        public void write(Buffer object, long l) throws IOException {
            if (!this.closed) {
                Util.checkOffsetAndCount(((Buffer)object).size(), 0L, l);
                if (l <= this.bytesRemaining) {
                    this.this$0.sink.write((Buffer)object, l);
                    this.bytesRemaining -= l;
                    return;
                }
                object = new StringBuilder();
                ((StringBuilder)object).append("expected ");
                ((StringBuilder)object).append(this.bytesRemaining);
                ((StringBuilder)object).append(" bytes but received ");
                ((StringBuilder)object).append(l);
                throw new ProtocolException(((StringBuilder)object).toString());
            }
            throw new IllegalStateException("closed");
        }
    }

    private class FixedLengthSource
    extends AbstractSource {
        private long bytesRemaining;
        final Http1xStream this$0;

        public FixedLengthSource(Http1xStream http1xStream, long l) throws IOException {
            this.this$0 = http1xStream;
            super(http1xStream);
            this.bytesRemaining = l;
            if (l == 0L) {
                this.endOfInput(true);
            }
        }

        @Override
        public void close() throws IOException {
            if (this.closed) {
                return;
            }
            if (this.bytesRemaining != 0L && !Util.discard(this, 100, TimeUnit.MILLISECONDS)) {
                this.endOfInput(false);
            }
            this.closed = true;
        }

        @Override
        public long read(Buffer object, long l) throws IOException {
            if (l >= 0L) {
                if (!this.closed) {
                    if (this.bytesRemaining == 0L) {
                        return -1L;
                    }
                    long l2 = this.this$0.source.read((Buffer)object, Math.min(this.bytesRemaining, l));
                    if (l2 != -1L) {
                        this.bytesRemaining = l = this.bytesRemaining - l2;
                        if (l == 0L) {
                            this.endOfInput(true);
                        }
                        return l2;
                    }
                    this.endOfInput(false);
                    throw new ProtocolException("unexpected end of stream");
                }
                throw new IllegalStateException("closed");
            }
            object = new StringBuilder();
            ((StringBuilder)object).append("byteCount < 0: ");
            ((StringBuilder)object).append(l);
            throw new IllegalArgumentException(((StringBuilder)object).toString());
        }
    }

    private class UnknownLengthSource
    extends AbstractSource {
        private boolean inputExhausted;
        final Http1xStream this$0;

        private UnknownLengthSource(Http1xStream http1xStream) {
            this.this$0 = http1xStream;
            super(http1xStream);
        }

        @Override
        public void close() throws IOException {
            if (this.closed) {
                return;
            }
            if (!this.inputExhausted) {
                this.endOfInput(false);
            }
            this.closed = true;
        }

        @Override
        public long read(Buffer object, long l) throws IOException {
            if (l >= 0L) {
                if (!this.closed) {
                    if (this.inputExhausted) {
                        return -1L;
                    }
                    l = this.this$0.source.read((Buffer)object, l);
                    if (l == -1L) {
                        this.inputExhausted = true;
                        this.endOfInput(true);
                        return -1L;
                    }
                    return l;
                }
                throw new IllegalStateException("closed");
            }
            object = new StringBuilder();
            ((StringBuilder)object).append("byteCount < 0: ");
            ((StringBuilder)object).append(l);
            throw new IllegalArgumentException(((StringBuilder)object).toString());
        }
    }
}

