/*
 * Decompiled with CFR 0.152.
 */
package okio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import okio.AsyncTimeout;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.RealBufferedSink;
import okio.RealBufferedSource;
import okio.Segment;
import okio.SegmentPool;
import okio.Sink;
import okio.Source;
import okio.Timeout;
import okio.Util;

public final class Okio {
    static final Logger logger = Logger.getLogger(Okio.class.getName());

    private Okio() {
    }

    public static Sink appendingSink(File file) throws FileNotFoundException {
        if (file != null) {
            return Okio.sink(new FileOutputStream(file, true));
        }
        throw new IllegalArgumentException("file == null");
    }

    public static BufferedSink buffer(Sink sink) {
        if (sink != null) {
            return new RealBufferedSink(sink);
        }
        throw new IllegalArgumentException("sink == null");
    }

    public static BufferedSource buffer(Source source) {
        if (source != null) {
            return new RealBufferedSource(source);
        }
        throw new IllegalArgumentException("source == null");
    }

    static boolean isAndroidGetsocknameError(AssertionError assertionError) {
        boolean bl = ((Throwable)((Object)assertionError)).getCause() != null && ((Throwable)((Object)assertionError)).getMessage() != null && ((Throwable)((Object)assertionError)).getMessage().contains("getsockname failed");
        return bl;
    }

    public static Sink sink(File file) throws FileNotFoundException {
        if (file != null) {
            return Okio.sink(new FileOutputStream(file));
        }
        throw new IllegalArgumentException("file == null");
    }

    public static Sink sink(OutputStream outputStream) {
        return Okio.sink(outputStream, new Timeout());
    }

    private static Sink sink(OutputStream outputStream, Timeout timeout) {
        if (outputStream != null) {
            if (timeout != null) {
                return new Sink(timeout, outputStream){
                    final OutputStream val$out;
                    final Timeout val$timeout;
                    {
                        this.val$timeout = timeout;
                        this.val$out = outputStream;
                    }

                    @Override
                    public void close() throws IOException {
                        this.val$out.close();
                    }

                    @Override
                    public void flush() throws IOException {
                        this.val$out.flush();
                    }

                    @Override
                    public Timeout timeout() {
                        return this.val$timeout;
                    }

                    public String toString() {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("sink(");
                        stringBuilder.append(this.val$out);
                        stringBuilder.append(")");
                        return stringBuilder.toString();
                    }

                    @Override
                    public void write(Buffer buffer, long l) throws IOException {
                        Util.checkOffsetAndCount(buffer.size, 0L, l);
                        while (l > 0L) {
                            this.val$timeout.throwIfReached();
                            Segment segment = buffer.head;
                            int n = (int)Math.min(l, (long)(segment.limit - segment.pos));
                            this.val$out.write(segment.data, segment.pos, n);
                            segment.pos += n;
                            l -= (long)n;
                            buffer.size -= (long)n;
                            if (segment.pos != segment.limit) continue;
                            buffer.head = segment.pop();
                            SegmentPool.recycle(segment);
                        }
                    }
                };
            }
            throw new IllegalArgumentException("timeout == null");
        }
        throw new IllegalArgumentException("out == null");
    }

    public static Sink sink(Socket socket) throws IOException {
        if (socket != null) {
            AsyncTimeout asyncTimeout = Okio.timeout(socket);
            return asyncTimeout.sink(Okio.sink(socket.getOutputStream(), asyncTimeout));
        }
        throw new IllegalArgumentException("socket == null");
    }

    public static Sink sink(Path path, OpenOption ... openOptionArray) throws IOException {
        if (path != null) {
            return Okio.sink(Files.newOutputStream(path, openOptionArray));
        }
        throw new IllegalArgumentException("path == null");
    }

    public static Source source(File file) throws FileNotFoundException {
        if (file != null) {
            return Okio.source(new FileInputStream(file));
        }
        throw new IllegalArgumentException("file == null");
    }

    public static Source source(InputStream inputStream) {
        return Okio.source(inputStream, new Timeout());
    }

    private static Source source(InputStream inputStream, Timeout timeout) {
        if (inputStream != null) {
            if (timeout != null) {
                return new Source(timeout, inputStream){
                    final InputStream val$in;
                    final Timeout val$timeout;
                    {
                        this.val$timeout = timeout;
                        this.val$in = inputStream;
                    }

                    @Override
                    public void close() throws IOException {
                        this.val$in.close();
                    }

                    @Override
                    public long read(Buffer object, long l) throws IOException {
                        if (l >= 0L) {
                            int n;
                            block6: {
                                if (l == 0L) {
                                    return 0L;
                                }
                                try {
                                    this.val$timeout.throwIfReached();
                                    Segment segment = ((Buffer)object).writableSegment(1);
                                    n = (int)Math.min(l, (long)(8192 - segment.limit));
                                    n = this.val$in.read(segment.data, segment.limit, n);
                                    if (n != -1) break block6;
                                    return -1L;
                                }
                                catch (AssertionError assertionError) {
                                    if (Okio.isAndroidGetsocknameError(assertionError)) {
                                        throw new IOException((Throwable)((Object)assertionError));
                                    }
                                    throw assertionError;
                                }
                            }
                            segment.limit += n;
                            ((Buffer)object).size += (long)n;
                            return n;
                        }
                        object = new StringBuilder();
                        ((StringBuilder)object).append("byteCount < 0: ");
                        ((StringBuilder)object).append(l);
                        throw new IllegalArgumentException(((StringBuilder)object).toString());
                    }

                    @Override
                    public Timeout timeout() {
                        return this.val$timeout;
                    }

                    public String toString() {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("source(");
                        stringBuilder.append(this.val$in);
                        stringBuilder.append(")");
                        return stringBuilder.toString();
                    }
                };
            }
            throw new IllegalArgumentException("timeout == null");
        }
        throw new IllegalArgumentException("in == null");
    }

    public static Source source(Socket socket) throws IOException {
        if (socket != null) {
            AsyncTimeout asyncTimeout = Okio.timeout(socket);
            return asyncTimeout.source(Okio.source(socket.getInputStream(), asyncTimeout));
        }
        throw new IllegalArgumentException("socket == null");
    }

    public static Source source(Path path, OpenOption ... openOptionArray) throws IOException {
        if (path != null) {
            return Okio.source(Files.newInputStream(path, openOptionArray));
        }
        throw new IllegalArgumentException("path == null");
    }

    private static AsyncTimeout timeout(Socket socket) {
        return new AsyncTimeout(socket){
            final Socket val$socket;
            {
                this.val$socket = socket;
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
                try {
                    this.val$socket.close();
                }
                catch (AssertionError assertionError) {
                    if (Okio.isAndroidGetsocknameError(assertionError)) {
                        Logger logger = Okio.logger;
                        Level level = Level.WARNING;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Failed to close timed out socket ");
                        stringBuilder.append(this.val$socket);
                        logger.log(level, stringBuilder.toString(), (Throwable)((Object)assertionError));
                    }
                    throw assertionError;
                }
                catch (Exception exception) {
                    Logger logger = Okio.logger;
                    Level level = Level.WARNING;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Failed to close timed out socket ");
                    stringBuilder.append(this.val$socket);
                    logger.log(level, stringBuilder.toString(), exception);
                }
            }
        };
    }
}

