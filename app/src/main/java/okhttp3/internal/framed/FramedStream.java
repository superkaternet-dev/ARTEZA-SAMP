/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.framed;

import java.io.EOFException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.internal.framed.ErrorCode;
import okhttp3.internal.framed.FramedConnection;
import okhttp3.internal.framed.Header;
import okhttp3.internal.framed.HeadersMode;
import okhttp3.internal.framed.StreamResetException;
import okio.AsyncTimeout;
import okio.Buffer;
import okio.BufferedSource;
import okio.Sink;
import okio.Source;
import okio.Timeout;

public final class FramedStream {
    static final boolean $assertionsDisabled = false;
    long bytesLeftInWriteWindow;
    private final FramedConnection connection;
    private ErrorCode errorCode = null;
    private final int id;
    private final StreamTimeout readTimeout = new StreamTimeout(this);
    private final List<Header> requestHeaders;
    private List<Header> responseHeaders;
    final FramedDataSink sink;
    private final FramedDataSource source;
    long unacknowledgedBytesRead = 0L;
    private final StreamTimeout writeTimeout = new StreamTimeout(this);

    FramedStream(int n, FramedConnection closeable, boolean bl, boolean bl2, List<Header> list) {
        if (closeable != null) {
            if (list != null) {
                FramedDataSource framedDataSource;
                this.id = n;
                this.connection = closeable;
                this.bytesLeftInWriteWindow = closeable.peerSettings.getInitialWindowSize(65536);
                this.source = framedDataSource = new FramedDataSource(this, closeable.okHttpSettings.getInitialWindowSize(65536));
                closeable = new FramedDataSink(this);
                this.sink = closeable;
                FramedDataSource.access$102(framedDataSource, bl2);
                FramedDataSink.access$202((FramedDataSink)closeable, bl);
                this.requestHeaders = list;
                return;
            }
            throw new NullPointerException("requestHeaders == null");
        }
        throw new NullPointerException("connection == null");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    private void cancelStreamIfNecessary() throws IOException {
        if (Thread.holdsLock(this)) throw new AssertionError();
        // MONITORENTER : this
        boolean bl = !this.source.finished && this.source.closed && (this.sink.finished || this.sink.closed);
        boolean bl2 = this.isOpen();
        // MONITOREXIT : this
        if (bl) {
            this.close(ErrorCode.CANCEL);
            return;
        }
        if (bl2) return;
        this.connection.removeStream(this.id);
    }

    private void checkOutNotClosed() throws IOException {
        if (!this.sink.closed) {
            if (!this.sink.finished) {
                if (this.errorCode == null) {
                    return;
                }
                throw new StreamResetException(this.errorCode);
            }
            throw new IOException("stream finished");
        }
        throw new IOException("stream closed");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private boolean closeInternal(ErrorCode errorCode) {
        if (Thread.holdsLock(this)) {
            throw new AssertionError();
        }
        synchronized (this) {
            if (this.errorCode != null) {
                return false;
            }
            if (this.source.finished && this.sink.finished) {
                return false;
            }
            this.errorCode = errorCode;
            this.notifyAll();
        }
        this.connection.removeStream(this.id);
        return true;
    }

    private void waitForIo() throws InterruptedIOException {
        try {
            this.wait();
            return;
        }
        catch (InterruptedException interruptedException) {
            throw new InterruptedIOException();
        }
    }

    void addBytesToWriteWindow(long l) {
        this.bytesLeftInWriteWindow += l;
        if (l > 0L) {
            this.notifyAll();
        }
    }

    public void close(ErrorCode errorCode) throws IOException {
        if (!this.closeInternal(errorCode)) {
            return;
        }
        this.connection.writeSynReset(this.id, errorCode);
    }

    public void closeLater(ErrorCode errorCode) {
        if (!this.closeInternal(errorCode)) {
            return;
        }
        this.connection.writeSynResetLater(this.id, errorCode);
    }

    public FramedConnection getConnection() {
        return this.connection;
    }

    public ErrorCode getErrorCode() {
        synchronized (this) {
            ErrorCode errorCode = this.errorCode;
            return errorCode;
        }
    }

    public int getId() {
        return this.id;
    }

    public List<Header> getRequestHeaders() {
        return this.requestHeaders;
    }

    /*
     * Loose catch block
     */
    public List<Header> getResponseHeaders() throws IOException {
        synchronized (this) {
            Object object;
            block11: {
                block12: {
                    this.readTimeout.enter();
                    while (true) {
                        object = this.responseHeaders;
                        if (object != null) break;
                        try {
                            if (this.errorCode != null) break;
                            this.waitForIo();
                        }
                        catch (Throwable throwable) {
                            break block11;
                        }
                    }
                    try {
                        this.readTimeout.exitAndThrowIfTimedOut();
                        object = this.responseHeaders;
                        if (object == null) break block12;
                    }
                    catch (Throwable throwable) {}
                    {
                        throw throwable;
                    }
                    return object;
                }
                object = new StreamResetException(this.errorCode);
                throw object;
                catch (Throwable throwable) {
                    // empty catch block
                }
            }
            this.readTimeout.exitAndThrowIfTimedOut();
            throw object;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public Sink getSink() {
        synchronized (this) {
            if (this.responseHeaders == null && !this.isLocallyInitiated()) {
                IllegalStateException illegalStateException = new IllegalStateException("reply before requesting the sink");
                throw illegalStateException;
            }
            return this.sink;
        }
    }

    public Source getSource() {
        return this.source;
    }

    public boolean isLocallyInitiated() {
        int n = this.id;
        boolean bl = true;
        boolean bl2 = (n & 1) == 1;
        bl2 = this.connection.client == bl2 ? bl : false;
        return bl2;
    }

    public boolean isOpen() {
        synchronized (this) {
            block5: {
                Object object;
                block4: {
                    object = this.errorCode;
                    if (object == null) break block4;
                    return false;
                }
                if (!this.source.finished && !this.source.closed || !this.sink.finished && !this.sink.closed || (object = this.responseHeaders) == null) break block5;
                return false;
            }
            return true;
        }
    }

    public Timeout readTimeout() {
        return this.readTimeout;
    }

    void receiveData(BufferedSource bufferedSource, int n) throws IOException {
        if (!Thread.holdsLock(this)) {
            this.source.receive(bufferedSource, n);
            return;
        }
        throw new AssertionError();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    void receiveFin() {
        if (Thread.holdsLock(this)) throw new AssertionError();
        // MONITORENTER : this
        FramedDataSource.access$102(this.source, true);
        boolean bl = this.isOpen();
        this.notifyAll();
        // MONITOREXIT : this
        if (bl) return;
        this.connection.removeStream(this.id);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    void receiveHeaders(List<Header> object, HeadersMode object2) {
        if (Thread.holdsLock(this)) throw new AssertionError();
        Object var4_3 = null;
        boolean bl = true;
        // MONITORENTER : this
        if (this.responseHeaders == null) {
            if (((HeadersMode)((Object)object2)).failIfHeadersAbsent()) {
                object = ErrorCode.PROTOCOL_ERROR;
            } else {
                this.responseHeaders = object;
                bl = this.isOpen();
                this.notifyAll();
                object = var4_3;
            }
        } else if (((HeadersMode)((Object)object2)).failIfHeadersPresent()) {
            object = ErrorCode.STREAM_IN_USE;
        } else {
            object2 = new ArrayList();
            object2.addAll(this.responseHeaders);
            object2.addAll(object);
            this.responseHeaders = object2;
            object = var4_3;
        }
        // MONITOREXIT : this
        if (object != null) {
            this.closeLater((ErrorCode)((Object)object));
            return;
        }
        if (bl) return;
        this.connection.removeStream(this.id);
    }

    void receiveRstStream(ErrorCode errorCode) {
        synchronized (this) {
            if (this.errorCode == null) {
                this.errorCode = errorCode;
                this.notifyAll();
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void reply(List<Header> object, boolean bl) throws IOException {
        if (Thread.holdsLock(this)) {
            throw new AssertionError();
        }
        boolean bl2 = false;
        synchronized (this) {
            Throwable throwable2;
            block9: {
                if (object != null) {
                    block8: {
                        try {
                            if (this.responseHeaders != null) break block8;
                            this.responseHeaders = object;
                            if (!bl) {
                                FramedDataSink.access$202(this.sink, true);
                                bl2 = true;
                            }
                            // MONITOREXIT @DISABLED, blocks:[0, 2, 7] lbl13 : MonitorExitStatement: MONITOREXIT : this
                        }
                        catch (Throwable throwable2) {
                            break block9;
                        }
                        this.connection.writeSynReply(this.id, bl2, (List<Header>)object);
                        if (bl2) {
                            this.connection.flush();
                        }
                        return;
                    }
                    object = new IllegalStateException("reply already sent");
                    throw object;
                }
                object = new NullPointerException("responseHeaders == null");
                throw object;
            }
            throw throwable2;
        }
    }

    public Timeout writeTimeout() {
        return this.writeTimeout;
    }

    final class FramedDataSink
    implements Sink {
        static final boolean $assertionsDisabled = false;
        private static final long EMIT_BUFFER_SIZE = 16384L;
        private boolean closed;
        private boolean finished;
        private final Buffer sendBuffer;
        final FramedStream this$0;

        FramedDataSink(FramedStream framedStream) {
            this.this$0 = framedStream;
            this.sendBuffer = new Buffer();
        }

        static /* synthetic */ boolean access$202(FramedDataSink framedDataSink, boolean bl) {
            framedDataSink.finished = bl;
            return bl;
        }

        /*
         * Loose catch block
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        private void emitDataFrame(boolean bl) throws IOException {
            long l;
            Object object = this.this$0;
            synchronized (object) {
                this.this$0.writeTimeout.enter();
                while (this.this$0.bytesLeftInWriteWindow <= 0L && !this.finished && !this.closed && this.this$0.errorCode == null) {
                    this.this$0.waitForIo();
                }
                this.this$0.writeTimeout.exitAndThrowIfTimedOut();
                this.this$0.checkOutNotClosed();
                l = Math.min(this.this$0.bytesLeftInWriteWindow, this.sendBuffer.size());
                FramedStream framedStream = this.this$0;
                framedStream.bytesLeftInWriteWindow -= l;
            }
            this.this$0.writeTimeout.enter();
            try {
                object = this.this$0.connection;
                int n = this.this$0.id;
                bl = bl && l == this.sendBuffer.size();
                ((FramedConnection)object).writeData(n, bl, this.sendBuffer, l);
                return;
            }
            finally {
                this.this$0.writeTimeout.exitAndThrowIfTimedOut();
            }
            {
                catch (Throwable throwable) {
                    this.this$0.writeTimeout.exitAndThrowIfTimedOut();
                    throw throwable;
                }
            }
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Converted monitor instructions to comments
         * Lifted jumps to return sites
         */
        @Override
        public void close() throws IOException {
            if (Thread.holdsLock(this.this$0)) {
                AssertionError assertionError = new AssertionError();
                throw assertionError;
            }
            FramedStream framedStream = this.this$0;
            // MONITORENTER : framedStream
            if (this.closed) {
                // MONITOREXIT : framedStream
                return;
            }
            // MONITOREXIT : framedStream
            if (!this.this$0.sink.finished) {
                if (this.sendBuffer.size() > 0L) {
                    while (this.sendBuffer.size() > 0L) {
                        this.emitDataFrame(true);
                    }
                } else {
                    this.this$0.connection.writeData(this.this$0.id, true, null, 0L);
                }
            }
            framedStream = this.this$0;
            // MONITORENTER : framedStream
            this.closed = true;
            // MONITOREXIT : framedStream
            this.this$0.connection.flush();
            this.this$0.cancelStreamIfNecessary();
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void flush() throws IOException {
            if (Thread.holdsLock(this.this$0)) {
                AssertionError assertionError = new AssertionError();
                throw assertionError;
            }
            FramedStream framedStream = this.this$0;
            synchronized (framedStream) {
                this.this$0.checkOutNotClosed();
            }
            while (this.sendBuffer.size() > 0L) {
                this.emitDataFrame(false);
                this.this$0.connection.flush();
            }
        }

        @Override
        public Timeout timeout() {
            return this.this$0.writeTimeout;
        }

        @Override
        public void write(Buffer object, long l) throws IOException {
            if (!Thread.holdsLock(this.this$0)) {
                this.sendBuffer.write((Buffer)object, l);
                while (this.sendBuffer.size() >= 16384L) {
                    this.emitDataFrame(false);
                }
                return;
            }
            object = new AssertionError();
            throw object;
        }
    }

    private final class FramedDataSource
    implements Source {
        static final boolean $assertionsDisabled = false;
        private boolean closed;
        private boolean finished;
        private final long maxByteCount;
        private final Buffer readBuffer;
        private final Buffer receiveBuffer;
        final FramedStream this$0;

        private FramedDataSource(FramedStream framedStream, long l) {
            this.this$0 = framedStream;
            this.receiveBuffer = new Buffer();
            this.readBuffer = new Buffer();
            this.maxByteCount = l;
        }

        static /* synthetic */ boolean access$102(FramedDataSource framedDataSource, boolean bl) {
            framedDataSource.finished = bl;
            return bl;
        }

        private void checkNotClosed() throws IOException {
            if (!this.closed) {
                if (this.this$0.errorCode == null) {
                    return;
                }
                throw new StreamResetException(this.this$0.errorCode);
            }
            throw new IOException("stream closed");
        }

        private void waitUntilReadable() throws IOException {
            this.this$0.readTimeout.enter();
            try {
                while (this.readBuffer.size() == 0L && !this.finished && !this.closed && this.this$0.errorCode == null) {
                    this.this$0.waitForIo();
                }
            }
            catch (Throwable throwable) {
                this.this$0.readTimeout.exitAndThrowIfTimedOut();
                throw throwable;
            }
            this.this$0.readTimeout.exitAndThrowIfTimedOut();
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void close() throws IOException {
            FramedStream framedStream = this.this$0;
            synchronized (framedStream) {
                this.closed = true;
                this.readBuffer.clear();
                this.this$0.notifyAll();
            }
            this.this$0.cancelStreamIfNecessary();
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public long read(Buffer object, long l) throws IOException {
            if (l < 0L) {
                object = new StringBuilder();
                ((StringBuilder)object).append("byteCount < 0: ");
                ((StringBuilder)object).append(l);
                throw new IllegalArgumentException(((StringBuilder)object).toString());
            }
            Object object2 = this.this$0;
            synchronized (object2) {
                this.waitUntilReadable();
                this.checkNotClosed();
                if (this.readBuffer.size() == 0L) {
                    return -1L;
                }
                Buffer buffer = this.readBuffer;
                l = buffer.read((Buffer)object, Math.min(l, buffer.size()));
                object = this.this$0;
                ((FramedStream)object).unacknowledgedBytesRead += l;
                if (this.this$0.unacknowledgedBytesRead >= (long)(((FramedStream)this.this$0).connection.okHttpSettings.getInitialWindowSize(65536) / 2)) {
                    this.this$0.connection.writeWindowUpdateLater(this.this$0.id, this.this$0.unacknowledgedBytesRead);
                    this.this$0.unacknowledgedBytesRead = 0L;
                }
            }
            object = this.this$0.connection;
            synchronized (object) {
                object2 = this.this$0.connection;
                ((FramedConnection)object2).unacknowledgedBytesRead += l;
                if (((FramedStream)this.this$0).connection.unacknowledgedBytesRead >= (long)(((FramedStream)this.this$0).connection.okHttpSettings.getInitialWindowSize(65536) / 2)) {
                    this.this$0.connection.writeWindowUpdateLater(0, ((FramedStream)this.this$0).connection.unacknowledgedBytesRead);
                    ((FramedStream)this.this$0).connection.unacknowledgedBytesRead = 0L;
                }
                return l;
            }
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Converted monitor instructions to comments
         * Lifted jumps to return sites
         */
        void receive(BufferedSource object, long l) throws IOException {
            if (Thread.holdsLock(this.this$0)) {
                object = new AssertionError();
                throw object;
            }
            while (l > 0L) {
                FramedStream framedStream = this.this$0;
                // MONITORENTER : framedStream
                boolean bl = this.finished;
                long l2 = this.readBuffer.size();
                long l3 = this.maxByteCount;
                boolean bl2 = true;
                boolean bl3 = l2 + l > l3;
                // MONITOREXIT : framedStream
                if (bl3) {
                    object.skip(l);
                    this.this$0.closeLater(ErrorCode.FLOW_CONTROL_ERROR);
                    return;
                }
                if (bl) {
                    object.skip(l);
                    return;
                }
                l3 = object.read(this.receiveBuffer, l);
                if (l3 == -1L) throw new EOFException();
                framedStream = this.this$0;
                // MONITORENTER : framedStream
                bl3 = this.readBuffer.size() == 0L ? bl2 : false;
                this.readBuffer.writeAll(this.receiveBuffer);
                if (bl3) {
                    this.this$0.notifyAll();
                }
                // MONITOREXIT : framedStream
                l -= l3;
            }
        }

        @Override
        public Timeout timeout() {
            return this.this$0.readTimeout;
        }
    }

    class StreamTimeout
    extends AsyncTimeout {
        final FramedStream this$0;

        StreamTimeout(FramedStream framedStream) {
            this.this$0 = framedStream;
        }

        public void exitAndThrowIfTimedOut() throws IOException {
            if (!this.exit()) {
                return;
            }
            throw this.newTimeoutException(null);
        }

        @Override
        protected IOException newTimeoutException(IOException iOException) {
            SocketTimeoutException socketTimeoutException = new SocketTimeoutException("timeout");
            if (iOException != null) {
                socketTimeoutException.initCause(iOException);
            }
            return socketTimeoutException;
        }

        @Override
        protected void timedOut() {
            this.this$0.closeLater(ErrorCode.CANCEL);
        }
    }
}

