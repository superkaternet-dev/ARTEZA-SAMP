/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.framed;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.Protocol;
import okhttp3.internal.Util;
import okhttp3.internal.framed.ErrorCode;
import okhttp3.internal.framed.FrameReader;
import okhttp3.internal.framed.FrameWriter;
import okhttp3.internal.framed.Header;
import okhttp3.internal.framed.HeadersMode;
import okhttp3.internal.framed.Hpack;
import okhttp3.internal.framed.Settings;
import okhttp3.internal.framed.Variant;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ByteString;
import okio.Source;
import okio.Timeout;

public final class Http2
implements Variant {
    private static final ByteString CONNECTION_PREFACE;
    static final byte FLAG_ACK = 1;
    static final byte FLAG_COMPRESSED = 32;
    static final byte FLAG_END_HEADERS = 4;
    static final byte FLAG_END_PUSH_PROMISE = 4;
    static final byte FLAG_END_STREAM = 1;
    static final byte FLAG_NONE = 0;
    static final byte FLAG_PADDED = 8;
    static final byte FLAG_PRIORITY = 32;
    static final int INITIAL_MAX_FRAME_SIZE = 16384;
    static final byte TYPE_CONTINUATION = 9;
    static final byte TYPE_DATA = 0;
    static final byte TYPE_GOAWAY = 7;
    static final byte TYPE_HEADERS = 1;
    static final byte TYPE_PING = 6;
    static final byte TYPE_PRIORITY = 2;
    static final byte TYPE_PUSH_PROMISE = 5;
    static final byte TYPE_RST_STREAM = 3;
    static final byte TYPE_SETTINGS = 4;
    static final byte TYPE_WINDOW_UPDATE = 8;
    private static final Logger logger;

    static {
        logger = Logger.getLogger(FrameLogger.class.getName());
        CONNECTION_PREFACE = ByteString.encodeUtf8("PRI * HTTP/2.0\r\n\r\nSM\r\n\r\n");
    }

    private static IllegalArgumentException illegalArgument(String string2, Object ... objectArray) {
        throw new IllegalArgumentException(Util.format(string2, objectArray));
    }

    private static IOException ioException(String string2, Object ... objectArray) throws IOException {
        throw new IOException(Util.format(string2, objectArray));
    }

    private static int lengthWithoutPadding(int n, byte by, short s) throws IOException {
        int n2 = n;
        if ((by & 8) != 0) {
            n2 = n - 1;
        }
        if (s <= n2) {
            return (short)(n2 - s);
        }
        throw Http2.ioException("PROTOCOL_ERROR padding %s > remaining length %s", s, n2);
    }

    private static int readMedium(BufferedSource bufferedSource) throws IOException {
        return (bufferedSource.readByte() & 0xFF) << 16 | (bufferedSource.readByte() & 0xFF) << 8 | bufferedSource.readByte() & 0xFF;
    }

    private static void writeMedium(BufferedSink bufferedSink, int n) throws IOException {
        bufferedSink.writeByte(n >>> 16 & 0xFF);
        bufferedSink.writeByte(n >>> 8 & 0xFF);
        bufferedSink.writeByte(n & 0xFF);
    }

    @Override
    public Protocol getProtocol() {
        return Protocol.HTTP_2;
    }

    @Override
    public FrameReader newReader(BufferedSource bufferedSource, boolean bl) {
        return new Reader(bufferedSource, 4096, bl);
    }

    @Override
    public FrameWriter newWriter(BufferedSink bufferedSink, boolean bl) {
        return new Writer(bufferedSink, bl);
    }

    static final class ContinuationSource
    implements Source {
        byte flags;
        int left;
        int length;
        short padding;
        private final BufferedSource source;
        int streamId;

        public ContinuationSource(BufferedSource bufferedSource) {
            this.source = bufferedSource;
        }

        private void readContinuationHeader() throws IOException {
            int n;
            int n2 = this.streamId;
            this.left = n = Http2.readMedium(this.source);
            this.length = n;
            byte by = (byte)(this.source.readByte() & 0xFF);
            this.flags = (byte)(this.source.readByte() & 0xFF);
            if (logger.isLoggable(Level.FINE)) {
                logger.fine(FrameLogger.formatHeader(true, this.streamId, this.length, by, this.flags));
            }
            this.streamId = n = this.source.readInt() & Integer.MAX_VALUE;
            if (by == 9) {
                if (n == n2) {
                    return;
                }
                throw Http2.ioException("TYPE_CONTINUATION streamId changed", new Object[0]);
            }
            throw Http2.ioException("%s != TYPE_CONTINUATION", new Object[]{by});
        }

        @Override
        public void close() throws IOException {
        }

        @Override
        public long read(Buffer buffer, long l) throws IOException {
            int n;
            while ((n = this.left) == 0) {
                this.source.skip(this.padding);
                this.padding = 0;
                if ((this.flags & 4) != 0) {
                    return -1L;
                }
                this.readContinuationHeader();
            }
            if ((l = this.source.read(buffer, Math.min(l, (long)n))) == -1L) {
                return -1L;
            }
            this.left = (int)((long)this.left - l);
            return l;
        }

        @Override
        public Timeout timeout() {
            return this.source.timeout();
        }
    }

    static final class FrameLogger {
        private static final String[] BINARY;
        private static final String[] FLAGS;
        private static final String[] TYPES;

        static {
            Object[] objectArray;
            Object object;
            Object[] objectArray2;
            int n;
            TYPES = new String[]{"DATA", "HEADERS", "PRIORITY", "RST_STREAM", "SETTINGS", "PUSH_PROMISE", "PING", "GOAWAY", "WINDOW_UPDATE", "CONTINUATION"};
            FLAGS = new String[64];
            BINARY = new String[256];
            for (n = 0; n < (objectArray2 = BINARY).length; ++n) {
                objectArray2[n] = Util.format("%8s", Integer.toBinaryString(n)).replace(' ', '0');
            }
            Object object2 = FLAGS;
            object2[0] = "";
            object2[1] = "END_STREAM";
            objectArray2 = new int[1];
            objectArray2[0] = (String)true;
            object2[8] = "PADDED";
            int n2 = objectArray2.length;
            for (n = 0; n < n2; ++n) {
                object = objectArray2[n];
                objectArray = FLAGS;
                object2 = new StringBuilder();
                ((StringBuilder)object2).append(objectArray[object]);
                ((StringBuilder)object2).append("|PADDED");
                objectArray[object | 8] = ((StringBuilder)object2).toString();
            }
            object2 = FLAGS;
            object2[4] = "END_HEADERS";
            object2[32] = "PRIORITY";
            object2[36] = "END_HEADERS|PRIORITY";
            Object[] objectArray3 = objectArray = (Object[])new int[3];
            objectArray[0] = (String)4;
            objectArray3[1] = (String)32;
            objectArray3[2] = (String)36;
            object = objectArray.length;
            for (n = 0; n < object; ++n) {
                String string2 = objectArray[n];
                int n3 = objectArray2.length;
                for (n2 = 0; n2 < n3; ++n2) {
                    String string3 = objectArray2[n2];
                    object2 = FLAGS;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append((String)object2[string3]);
                    stringBuilder.append('|');
                    stringBuilder.append((String)object2[string2]);
                    object2[string3 | string2] = stringBuilder.toString();
                    stringBuilder = new StringBuilder();
                    stringBuilder.append((String)object2[string3]);
                    stringBuilder.append('|');
                    stringBuilder.append((String)object2[string2]);
                    stringBuilder.append("|PADDED");
                    object2[string3 | string2 | 8] = stringBuilder.toString();
                }
            }
            for (n = 0; n < (objectArray2 = FLAGS).length; ++n) {
                if (objectArray2[n] != null) continue;
                objectArray2[n] = BINARY[n];
            }
        }

        FrameLogger() {
        }

        static String formatFlags(byte by, byte by2) {
            Object object;
            if (by2 == 0) {
                return "";
            }
            switch (by) {
                default: {
                    object = FLAGS;
                    object = by2 < ((String[])object).length ? object[by2] : BINARY[by2];
                }
                case 4: 
                case 6: {
                    String string2 = by2 == 1 ? "ACK" : BINARY[by2];
                    return string2;
                }
                case 2: 
                case 3: 
                case 7: 
                case 8: {
                    return BINARY[by2];
                }
            }
            if (by == 5 && (by2 & 4) != 0) {
                return ((String)object).replace("HEADERS", "PUSH_PROMISE");
            }
            if (by == 0 && (by2 & 0x20) != 0) {
                return ((String)object).replace("PRIORITY", "COMPRESSED");
            }
            return object;
        }

        static String formatHeader(boolean bl, int n, int n2, byte by, byte by2) {
            Object object = TYPES;
            object = by < ((String[])object).length ? object[by] : Util.format("0x%02x", by);
            String string2 = FrameLogger.formatFlags(by, by2);
            String string3 = bl ? "<<" : ">>";
            return Util.format("%s 0x%08x %5d %-13s %s", string3, n, n2, object, string2);
        }
    }

    static final class Reader
    implements FrameReader {
        private final boolean client;
        private final ContinuationSource continuation;
        final Hpack.Reader hpackReader;
        private final BufferedSource source;

        Reader(BufferedSource source, int n, boolean bl) {
            this.source = source;
            this.client = bl;
            source = new ContinuationSource((BufferedSource)source);
            this.continuation = source;
            this.hpackReader = new Hpack.Reader(n, source);
        }

        private void readData(FrameReader.Handler handler, int n, byte by, int n2) throws IOException {
            boolean bl = true;
            short s = 0;
            boolean bl2 = (by & 1) != 0;
            if ((by & 0x20) == 0) {
                bl = false;
            }
            if (!bl) {
                if ((by & 8) != 0) {
                    s = (short)(this.source.readByte() & 0xFF);
                }
                n = Http2.lengthWithoutPadding(n, by, s);
                handler.data(bl2, n2, this.source, n);
                this.source.skip(s);
                return;
            }
            throw Http2.ioException("PROTOCOL_ERROR: FLAG_COMPRESSED without SETTINGS_COMPRESS_DATA", new Object[0]);
        }

        private void readGoAway(FrameReader.Handler handler, int n, byte by, int n2) throws IOException {
            if (n >= 8) {
                if (n2 == 0) {
                    n2 = this.source.readInt();
                    by = (byte)this.source.readInt();
                    n -= 8;
                    ErrorCode errorCode = ErrorCode.fromHttp2(by);
                    if (errorCode != null) {
                        ByteString byteString = ByteString.EMPTY;
                        if (n > 0) {
                            byteString = this.source.readByteString(n);
                        }
                        handler.goAway(n2, errorCode, byteString);
                        return;
                    }
                    throw Http2.ioException("TYPE_GOAWAY unexpected error code: %d", new Object[]{(int)by});
                }
                throw Http2.ioException("TYPE_GOAWAY streamId != 0", new Object[0]);
            }
            throw Http2.ioException("TYPE_GOAWAY length < 8: %s", new Object[]{n});
        }

        private List<Header> readHeaderBlock(int n, short s, byte by, int n2) throws IOException {
            ContinuationSource continuationSource = this.continuation;
            continuationSource.left = n;
            continuationSource.length = n;
            this.continuation.padding = s;
            this.continuation.flags = by;
            this.continuation.streamId = n2;
            this.hpackReader.readHeaders();
            return this.hpackReader.getAndResetHeaderList();
        }

        private void readHeaders(FrameReader.Handler handler, int n, byte by, int n2) throws IOException {
            short s = 0;
            if (n2 != 0) {
                boolean bl = (by & 1) != 0;
                if ((by & 8) != 0) {
                    s = (short)(this.source.readByte() & 0xFF);
                }
                int n3 = n;
                if ((by & 0x20) != 0) {
                    this.readPriority(handler, n2);
                    n3 = n - 5;
                }
                handler.headers(false, bl, n2, -1, this.readHeaderBlock(Http2.lengthWithoutPadding(n3, by, s), s, by, n2), HeadersMode.HTTP_20_HEADERS);
                return;
            }
            throw Http2.ioException("PROTOCOL_ERROR: TYPE_HEADERS streamId == 0", new Object[0]);
        }

        private void readPing(FrameReader.Handler handler, int n, byte by, int n2) throws IOException {
            boolean bl = false;
            if (n == 8) {
                if (n2 == 0) {
                    n = this.source.readInt();
                    n2 = this.source.readInt();
                    if ((by & 1) != 0) {
                        bl = true;
                    }
                    handler.ping(bl, n, n2);
                    return;
                }
                throw Http2.ioException("TYPE_PING streamId != 0", new Object[0]);
            }
            throw Http2.ioException("TYPE_PING length != 8: %s", new Object[]{n});
        }

        private void readPriority(FrameReader.Handler handler, int n) throws IOException {
            int n2 = this.source.readInt();
            boolean bl = (Integer.MIN_VALUE & n2) != 0;
            handler.priority(n, Integer.MAX_VALUE & n2, (this.source.readByte() & 0xFF) + 1, bl);
        }

        private void readPriority(FrameReader.Handler handler, int n, byte by, int n2) throws IOException {
            if (n == 5) {
                if (n2 != 0) {
                    this.readPriority(handler, n2);
                    return;
                }
                throw Http2.ioException("TYPE_PRIORITY streamId == 0", new Object[0]);
            }
            throw Http2.ioException("TYPE_PRIORITY length: %d != 5", new Object[]{n});
        }

        private void readPushPromise(FrameReader.Handler handler, int n, byte by, int n2) throws IOException {
            short s = 0;
            if (n2 != 0) {
                if ((by & 8) != 0) {
                    s = (short)(this.source.readByte() & 0xFF);
                }
                handler.pushPromise(n2, this.source.readInt() & Integer.MAX_VALUE, this.readHeaderBlock(Http2.lengthWithoutPadding(n - 4, by, s), s, by, n2));
                return;
            }
            throw Http2.ioException("PROTOCOL_ERROR: TYPE_PUSH_PROMISE streamId == 0", new Object[0]);
        }

        private void readRstStream(FrameReader.Handler handler, int n, byte by, int n2) throws IOException {
            if (n == 4) {
                if (n2 != 0) {
                    n = this.source.readInt();
                    ErrorCode errorCode = ErrorCode.fromHttp2(n);
                    if (errorCode != null) {
                        handler.rstStream(n2, errorCode);
                        return;
                    }
                    throw Http2.ioException("TYPE_RST_STREAM unexpected error code: %d", new Object[]{n});
                }
                throw Http2.ioException("TYPE_RST_STREAM streamId == 0", new Object[0]);
            }
            throw Http2.ioException("TYPE_RST_STREAM length: %d != 4", new Object[]{n});
        }

        private void readSettings(FrameReader.Handler object, int n, byte by, int n2) throws IOException {
            if (n2 == 0) {
                if ((by & 1) != 0) {
                    if (n == 0) {
                        object.ackSettings();
                        return;
                    }
                    throw Http2.ioException("FRAME_SIZE_ERROR ack frame should be empty!", new Object[0]);
                }
                if (n % 6 == 0) {
                    Settings settings = new Settings();
                    for (n2 = 0; n2 < n; n2 += 6) {
                        short s = this.source.readShort();
                        int n3 = this.source.readInt();
                        switch (s) {
                            default: {
                                by = (byte)s;
                                break;
                            }
                            case 6: {
                                by = (byte)s;
                                break;
                            }
                            case 5: {
                                if (n3 >= 16384 && n3 <= 0xFFFFFF) {
                                    by = (byte)s;
                                    break;
                                }
                                throw Http2.ioException("PROTOCOL_ERROR SETTINGS_MAX_FRAME_SIZE: %s", new Object[]{n3});
                            }
                            case 4: {
                                by = (byte)7;
                                if (n3 >= 0) break;
                                throw Http2.ioException("PROTOCOL_ERROR SETTINGS_INITIAL_WINDOW_SIZE > 2^31 - 1", new Object[0]);
                            }
                            case 3: {
                                by = (byte)4;
                                break;
                            }
                            case 2: {
                                by = (byte)s;
                                if (n3 == 0) break;
                                if (n3 == 1) {
                                    by = (byte)s;
                                    break;
                                }
                                throw Http2.ioException("PROTOCOL_ERROR SETTINGS_ENABLE_PUSH != 0 or 1", new Object[0]);
                            }
                            case 1: {
                                by = (byte)s;
                            }
                        }
                        settings.set(by, 0, n3);
                    }
                    object.settings(false, settings);
                    if (settings.getHeaderTableSize() >= 0) {
                        this.hpackReader.headerTableSizeSetting(settings.getHeaderTableSize());
                    }
                    return;
                }
                throw Http2.ioException("TYPE_SETTINGS length %% 6 != 0: %s", new Object[]{n});
            }
            object = Http2.ioException("TYPE_SETTINGS streamId != 0", new Object[0]);
            throw object;
        }

        private void readWindowUpdate(FrameReader.Handler handler, int n, byte by, int n2) throws IOException {
            if (n == 4) {
                long l = (long)this.source.readInt() & Integer.MAX_VALUE;
                if (l != 0L) {
                    handler.windowUpdate(n2, l);
                    return;
                }
                throw Http2.ioException("windowSizeIncrement was 0", new Object[]{l});
            }
            throw Http2.ioException("TYPE_WINDOW_UPDATE length !=4: %s", new Object[]{n});
        }

        @Override
        public void close() throws IOException {
            this.source.close();
        }

        @Override
        public boolean nextFrame(FrameReader.Handler handler) throws IOException {
            try {
                this.source.require(9L);
            }
            catch (IOException iOException) {
                return false;
            }
            int n = Http2.readMedium(this.source);
            if (n >= 0 && n <= 16384) {
                byte by = (byte)(this.source.readByte() & 0xFF);
                byte by2 = (byte)(this.source.readByte() & 0xFF);
                int n2 = this.source.readInt() & Integer.MAX_VALUE;
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine(FrameLogger.formatHeader(true, n2, n, by, by2));
                }
                switch (by) {
                    default: {
                        this.source.skip(n);
                        break;
                    }
                    case 8: {
                        this.readWindowUpdate(handler, n, by2, n2);
                        break;
                    }
                    case 7: {
                        this.readGoAway(handler, n, by2, n2);
                        break;
                    }
                    case 6: {
                        this.readPing(handler, n, by2, n2);
                        break;
                    }
                    case 5: {
                        this.readPushPromise(handler, n, by2, n2);
                        break;
                    }
                    case 4: {
                        this.readSettings(handler, n, by2, n2);
                        break;
                    }
                    case 3: {
                        this.readRstStream(handler, n, by2, n2);
                        break;
                    }
                    case 2: {
                        this.readPriority(handler, n, by2, n2);
                        break;
                    }
                    case 1: {
                        this.readHeaders(handler, n, by2, n2);
                        break;
                    }
                    case 0: {
                        this.readData(handler, n, by2, n2);
                    }
                }
                return true;
            }
            throw Http2.ioException("FRAME_SIZE_ERROR: %s", new Object[]{n});
        }

        @Override
        public void readConnectionPreface() throws IOException {
            if (this.client) {
                return;
            }
            ByteString byteString = this.source.readByteString(CONNECTION_PREFACE.size());
            if (logger.isLoggable(Level.FINE)) {
                logger.fine(Util.format("<< CONNECTION %s", byteString.hex()));
            }
            if (CONNECTION_PREFACE.equals(byteString)) {
                return;
            }
            throw Http2.ioException("Expected a connection header but was %s", new Object[]{byteString.utf8()});
        }
    }

    static final class Writer
    implements FrameWriter {
        private final boolean client;
        private boolean closed;
        private final Buffer hpackBuffer;
        private final Hpack.Writer hpackWriter;
        private int maxFrameSize;
        private final BufferedSink sink;

        Writer(BufferedSink bufferedSink, boolean bl) {
            this.sink = bufferedSink;
            this.client = bl;
            bufferedSink = new Buffer();
            this.hpackBuffer = bufferedSink;
            this.hpackWriter = new Hpack.Writer((Buffer)bufferedSink);
            this.maxFrameSize = 16384;
        }

        private void writeContinuationFrames(int n, long l) throws IOException {
            while (l > 0L) {
                int n2;
                byte by = (l -= (long)(n2 = (int)Math.min((long)this.maxFrameSize, l))) == 0L ? (byte)4 : 0;
                this.frameHeader(n, n2, (byte)9, by);
                this.sink.write(this.hpackBuffer, (long)n2);
            }
        }

        @Override
        public void ackSettings(Settings object) throws IOException {
            synchronized (this) {
                if (!this.closed) {
                    this.maxFrameSize = ((Settings)object).getMaxFrameSize(this.maxFrameSize);
                    this.frameHeader(0, 0, (byte)4, (byte)1);
                    this.sink.flush();
                    return;
                }
                object = new IOException("closed");
                throw object;
            }
        }

        @Override
        public void close() throws IOException {
            synchronized (this) {
                this.closed = true;
                this.sink.close();
                return;
            }
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void connectionPreface() throws IOException {
            synchronized (this) {
                if (this.closed) {
                    IOException iOException = new IOException("closed");
                    throw iOException;
                }
                boolean bl = this.client;
                if (!bl) {
                    return;
                }
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine(Util.format(">> CONNECTION %s", CONNECTION_PREFACE.hex()));
                }
                this.sink.write(CONNECTION_PREFACE.toByteArray());
                this.sink.flush();
                return;
            }
        }

        /*
         * WARNING - void declaration
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void data(boolean bl, int n, Buffer object, int n2) throws IOException {
            synchronized (this) {
                void var4_4;
                if (this.closed) {
                    object = new IOException("closed");
                    throw object;
                }
                byte by = 0;
                if (bl) {
                    by = (byte)(0 | 1);
                }
                this.dataFrame(n, by, (Buffer)object, (int)var4_4);
                return;
            }
        }

        void dataFrame(int n, byte by, Buffer buffer, int n2) throws IOException {
            this.frameHeader(n, n2, (byte)0, by);
            if (n2 > 0) {
                this.sink.write(buffer, (long)n2);
            }
        }

        @Override
        public void flush() throws IOException {
            synchronized (this) {
                if (!this.closed) {
                    this.sink.flush();
                    return;
                }
                IOException iOException = new IOException("closed");
                throw iOException;
            }
        }

        void frameHeader(int n, int n2, byte by, byte by2) throws IOException {
            int n3;
            if (logger.isLoggable(Level.FINE)) {
                logger.fine(FrameLogger.formatHeader(false, n, n2, by, by2));
            }
            if (n2 <= (n3 = this.maxFrameSize)) {
                if ((Integer.MIN_VALUE & n) == 0) {
                    Http2.writeMedium(this.sink, n2);
                    this.sink.writeByte(by & 0xFF);
                    this.sink.writeByte(by2 & 0xFF);
                    this.sink.writeInt(Integer.MAX_VALUE & n);
                    return;
                }
                throw Http2.illegalArgument("reserved bit set: %s", new Object[]{n});
            }
            throw Http2.illegalArgument("FRAME_SIZE_ERROR length > %d: %d", new Object[]{n3, n2});
        }

        @Override
        public void goAway(int n, ErrorCode object, byte[] byArray) throws IOException {
            synchronized (this) {
                if (!this.closed) {
                    if (object.httpCode != -1) {
                        this.frameHeader(0, byArray.length + 8, (byte)7, (byte)0);
                        this.sink.writeInt(n);
                        this.sink.writeInt(object.httpCode);
                        if (byArray.length > 0) {
                            this.sink.write(byArray);
                        }
                        this.sink.flush();
                        return;
                    }
                    throw Http2.illegalArgument("errorCode.httpCode == -1", new Object[0]);
                }
                object = new IOException("closed");
                throw object;
            }
        }

        @Override
        public void headers(int n, List<Header> object) throws IOException {
            synchronized (this) {
                if (!this.closed) {
                    this.headers(false, n, (List<Header>)object);
                    return;
                }
                object = new IOException("closed");
                throw object;
            }
        }

        void headers(boolean bl, int n, List<Header> list) throws IOException {
            if (!this.closed) {
                this.hpackWriter.writeHeaders(list);
                long l = this.hpackBuffer.size();
                int n2 = (int)Math.min((long)this.maxFrameSize, l);
                byte by = l == (long)n2 ? (byte)4 : 0;
                byte by2 = by;
                if (bl) {
                    by2 = (byte)(by | 1);
                }
                this.frameHeader(n, n2, (byte)1, by2);
                this.sink.write(this.hpackBuffer, (long)n2);
                if (l > (long)n2) {
                    this.writeContinuationFrames(n, l - (long)n2);
                }
                return;
            }
            throw new IOException("closed");
        }

        @Override
        public int maxDataLength() {
            return this.maxFrameSize;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void ping(boolean bl, int n, int n2) throws IOException {
            synchronized (this) {
                if (this.closed) {
                    IOException iOException = new IOException("closed");
                    throw iOException;
                }
                byte by = bl ? (byte)1 : 0;
                this.frameHeader(0, 8, (byte)6, by);
                this.sink.writeInt(n);
                this.sink.writeInt(n2);
                this.sink.flush();
                return;
            }
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void pushPromise(int n, int n2, List<Header> object) throws IOException {
            synchronized (this) {
                if (this.closed) {
                    object = new IOException("closed");
                    throw object;
                }
                this.hpackWriter.writeHeaders((List<Header>)object);
                long l = this.hpackBuffer.size();
                int n3 = this.maxFrameSize;
                byte by = 4;
                n3 = (int)Math.min((long)(n3 - 4), l);
                if (l != (long)n3) {
                    by = 0;
                }
                this.frameHeader(n, n3 + 4, (byte)5, by);
                this.sink.writeInt(Integer.MAX_VALUE & n2);
                this.sink.write(this.hpackBuffer, (long)n3);
                if (l > (long)n3) {
                    this.writeContinuationFrames(n, l - (long)n3);
                }
                return;
            }
        }

        @Override
        public void rstStream(int n, ErrorCode object) throws IOException {
            synchronized (this) {
                if (!this.closed) {
                    if (object.httpCode != -1) {
                        this.frameHeader(n, 4, (byte)3, (byte)0);
                        this.sink.writeInt(object.httpCode);
                        this.sink.flush();
                        return;
                    }
                    object = new IllegalArgumentException();
                    throw object;
                }
                object = new IOException("closed");
                throw object;
            }
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void settings(Settings object) throws IOException {
            synchronized (this) {
                if (!this.closed) {
                    this.frameHeader(0, ((Settings)object).size() * 6, (byte)4, (byte)0);
                } else {
                    object = new IOException("closed");
                    throw object;
                }
                for (int i = 0; i < 10; ++i) {
                    int n;
                    if (!((Settings)object).isSet(i)) continue;
                    int n2 = i;
                    if (n2 == 4) {
                        n = 3;
                    } else {
                        n = n2;
                        if (n2 == 7) {
                            n = 4;
                        }
                    }
                    this.sink.writeShort(n);
                    this.sink.writeInt(((Settings)object).get(i));
                }
                this.sink.flush();
                return;
            }
        }

        @Override
        public void synReply(boolean bl, int n, List<Header> object) throws IOException {
            synchronized (this) {
                if (!this.closed) {
                    this.headers(bl, n, (List<Header>)object);
                    return;
                }
                object = new IOException("closed");
                throw object;
            }
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void synStream(boolean bl, boolean bl2, int n, int n2, List<Header> object) throws IOException {
            synchronized (this) {
                Throwable throwable2;
                if (!bl2) {
                    try {
                        if (!this.closed) {
                            this.headers(bl, n, (List<Header>)object);
                            return;
                        }
                        object = new IOException("closed");
                        throw object;
                    }
                    catch (Throwable throwable2) {}
                } else {
                    object = new UnsupportedOperationException();
                    throw object;
                }
                throw throwable2;
            }
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void windowUpdate(int n, long l) throws IOException {
            synchronized (this) {
                if (this.closed) {
                    IOException iOException = new IOException("closed");
                    throw iOException;
                }
                if (l != 0L && l <= Integer.MAX_VALUE) {
                    this.frameHeader(n, 4, (byte)8, (byte)0);
                    this.sink.writeInt((int)l);
                    this.sink.flush();
                    return;
                }
                throw Http2.illegalArgument("windowSizeIncrement == 0 || windowSizeIncrement > 0x7fffffffL: %s", new Object[]{l});
            }
        }
    }
}

