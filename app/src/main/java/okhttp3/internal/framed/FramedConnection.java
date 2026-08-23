/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.framed;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import okhttp3.Protocol;
import okhttp3.internal.NamedRunnable;
import okhttp3.internal.Platform;
import okhttp3.internal.Util;
import okhttp3.internal.framed.ErrorCode;
import okhttp3.internal.framed.FrameReader;
import okhttp3.internal.framed.FrameWriter;
import okhttp3.internal.framed.FramedStream;
import okhttp3.internal.framed.Header;
import okhttp3.internal.framed.HeadersMode;
import okhttp3.internal.framed.Http2;
import okhttp3.internal.framed.Ping;
import okhttp3.internal.framed.PushObserver;
import okhttp3.internal.framed.Settings;
import okhttp3.internal.framed.Spdy3;
import okhttp3.internal.framed.Variant;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ByteString;
import okio.Okio;

public final class FramedConnection
implements Closeable {
    static final boolean $assertionsDisabled = false;
    private static final int OKHTTP_CLIENT_WINDOW_SIZE = 0x1000000;
    private static final ExecutorService executor = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), Util.threadFactory("OkHttp FramedConnection", true));
    long bytesLeftInWriteWindow;
    final boolean client;
    private final Set<Integer> currentPushRequests;
    final FrameWriter frameWriter;
    private final String hostname;
    private long idleStartTimeNs;
    private int lastGoodStreamId;
    private final Listener listener;
    private int nextPingId;
    private int nextStreamId;
    Settings okHttpSettings;
    final Settings peerSettings;
    private Map<Integer, Ping> pings;
    final Protocol protocol;
    private final ExecutorService pushExecutor;
    private final PushObserver pushObserver;
    final Reader readerRunnable;
    private boolean receivedInitialPeerSettings;
    private boolean shutdown;
    final Socket socket;
    private final Map<Integer, FramedStream> streams;
    long unacknowledgedBytesRead;
    final Variant variant;

    private FramedConnection(Builder builder) throws IOException {
        Protocol protocol;
        block8: {
            boolean bl;
            Settings settings;
            block7: {
                block6: {
                    String string2;
                    this.streams = new HashMap<Integer, FramedStream>();
                    this.idleStartTimeNs = System.nanoTime();
                    this.unacknowledgedBytesRead = 0L;
                    this.okHttpSettings = new Settings();
                    this.peerSettings = settings = new Settings();
                    this.receivedInitialPeerSettings = false;
                    this.currentPushRequests = new LinkedHashSet<Integer>();
                    this.protocol = protocol = builder.protocol;
                    this.pushObserver = builder.pushObserver;
                    this.client = bl = builder.client;
                    this.listener = builder.listener;
                    boolean bl2 = builder.client;
                    int n = 2;
                    int n2 = bl2 ? 1 : 2;
                    this.nextStreamId = n2;
                    if (builder.client && protocol == Protocol.HTTP_2) {
                        this.nextStreamId += 2;
                    }
                    n2 = n;
                    if (builder.client) {
                        n2 = 1;
                    }
                    this.nextPingId = n2;
                    if (builder.client) {
                        this.okHttpSettings.set(7, 0, 0x1000000);
                    }
                    this.hostname = string2 = builder.hostname;
                    if (protocol != Protocol.HTTP_2) break block6;
                    this.variant = new Http2();
                    this.pushExecutor = new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), Util.threadFactory(Util.format("OkHttp %s Push Observer", string2), true));
                    settings.set(7, 0, 65535);
                    settings.set(5, 0, 16384);
                    break block7;
                }
                if (protocol != Protocol.SPDY_3) break block8;
                this.variant = new Spdy3();
                this.pushExecutor = null;
            }
            this.bytesLeftInWriteWindow = settings.getInitialWindowSize(65536);
            this.socket = builder.socket;
            this.frameWriter = this.variant.newWriter(builder.sink, bl);
            this.readerRunnable = new Reader(this, this.variant.newReader(builder.source, bl));
            return;
        }
        throw new AssertionError((Object)protocol);
    }

    static /* synthetic */ boolean access$1602(FramedConnection framedConnection, boolean bl) {
        framedConnection.shutdown = bl;
        return bl;
    }

    static /* synthetic */ int access$1702(FramedConnection framedConnection, int n) {
        framedConnection.lastGoodStreamId = n;
        return n;
    }

    static /* synthetic */ boolean access$2300(FramedConnection framedConnection) {
        return framedConnection.receivedInitialPeerSettings;
    }

    static /* synthetic */ boolean access$2302(FramedConnection framedConnection, boolean bl) {
        framedConnection.receivedInitialPeerSettings = bl;
        return bl;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void close(ErrorCode object, ErrorCode errorCode) throws IOException {
        block19: {
            int n;
            int n2;
            if (Thread.holdsLock(this)) {
                object = new AssertionError();
                throw object;
            }
            Object object2 = null;
            try {
                this.shutdown((ErrorCode)((Object)object));
                object = object2;
            }
            catch (IOException iOException) {
                // empty catch block
            }
            FramedStream[] framedStreamArray = null;
            Ping[] pingArray = null;
            synchronized (this) {
                boolean bl = this.streams.isEmpty();
                int n3 = 0;
                if (!bl) {
                    framedStreamArray = this.streams.values().toArray(new FramedStream[this.streams.size()]);
                    this.streams.clear();
                    this.setIdle(false);
                }
                if ((object2 = this.pings) != null) {
                    pingArray = object2.values().toArray(new Ping[this.pings.size()]);
                    this.pings = null;
                }
            }
            object2 = object;
            if (framedStreamArray != null) {
                n2 = framedStreamArray.length;
                n = 0;
                while (true) {
                    block18: {
                        object2 = object;
                        if (n >= n2) break;
                        object2 = framedStreamArray[n];
                        try {
                            ((FramedStream)object2).close(errorCode);
                            object2 = object;
                        }
                        catch (IOException iOException) {
                            object2 = object;
                            if (object == null) break block18;
                            object2 = iOException;
                        }
                    }
                    ++n;
                    object = object2;
                }
            }
            if (pingArray != null) {
                n2 = pingArray.length;
                for (n = n3; n < n2; ++n) {
                    pingArray[n].cancel();
                }
            }
            try {
                this.frameWriter.close();
                object = object2;
            }
            catch (IOException iOException) {
                object = object2;
                if (object2 != null) break block19;
                object = iOException;
            }
        }
        try {
            this.socket.close();
        }
        catch (IOException iOException) {
            // empty catch block
        }
        if (object != null) throw object;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private FramedStream newStream(int var1_1, List<Header> var2_2, boolean var3_7, boolean var4_8) throws IOException {
        block13: {
            var7_7 = var3_5 ^ 1;
            var4_6 ^= 1;
            var8_8 = this.frameWriter;
            synchronized (var8_8) {
                block15: {
                    synchronized (this) {
                        if (this.shutdown) ** break block14
                        var6_9 = this.nextStreamId;
                        this.nextStreamId = var6_9 + 2;
                        var9_10 = new FramedStream(var6_9, this, var7_7, var4_6, (List<Header>)var2_2);
                        var5_11 = var3_5 == false || this.bytesLeftInWriteWindow == 0L || var9_10.bytesLeftInWriteWindow == 0L;
                        if (var9_10.isOpen()) {
                            this.streams.put(var6_9, var9_10);
                            this.setIdle(false);
                        }
                        if (var1_1 != 0) break block15;
                    }
                    this.frameWriter.synStream(var7_7, var4_6, var6_9, var1_1, (List<Header>)var2_2);
                    ** GOTO lbl23
                }
                if (this.client) ** break block16
                var10_12 = this.frameWriter;
                var10_12.pushPromise(var1_1, var6_9, (List<Header>)var2_2);
lbl23:
                // 2 sources

                // MONITOREXIT @DISABLED, blocks:[3, 7] lbl23 : MonitorExitStatement: MONITOREXIT : var8_8
                if (!var5_11) break block13;
            }
            this.frameWriter.flush();
        }
        return var9_10;
        {
            try {
                var2_2 = new IllegalArgumentException("client streams shouldn't have associated stream IDs");
                throw var2_2;
            }
lbl32:
            // 3 sources

            catch (Throwable var2_4) {}
            {
                var2_2 = new IOException("shutdown");
                throw var2_2;
                {
                    catch (Throwable var2_3) {}
                    // MONITOREXIT @DISABLED, blocks:[6, 7, 8] lbl34 : MonitorExitStatement: MONITOREXIT : this
                    ** try [egrp 6[TRYBLOCK] [12 : 225->235)] { 
lbl40:
                    // 1 sources

                    throw var2_3;
                }
            }
            throw var2_4;
        }
    }

    private void pushDataLater(int n, BufferedSource object, int n2, boolean bl) throws IOException {
        Buffer buffer = new Buffer();
        object.require(n2);
        object.read(buffer, n2);
        if (buffer.size() == (long)n2) {
            this.pushExecutor.execute(new NamedRunnable(this, "OkHttp %s Push Data[%s]", new Object[]{this.hostname, n}, n, buffer, n2, bl){
                final FramedConnection this$0;
                final Buffer val$buffer;
                final int val$byteCount;
                final boolean val$inFinished;
                final int val$streamId;
                {
                    this.this$0 = framedConnection;
                    this.val$streamId = n;
                    this.val$buffer = buffer;
                    this.val$byteCount = n2;
                    this.val$inFinished = bl;
                    super(string2, objectArray);
                }

                /*
                 * Enabled aggressive block sorting
                 * Enabled unnecessary exception pruning
                 * Enabled aggressive exception aggregation
                 */
                @Override
                public void execute() {
                    try {
                        boolean bl = this.this$0.pushObserver.onData(this.val$streamId, this.val$buffer, this.val$byteCount, this.val$inFinished);
                        if (bl) {
                            this.this$0.frameWriter.rstStream(this.val$streamId, ErrorCode.CANCEL);
                        }
                        if (!bl) {
                            if (!this.val$inFinished) return;
                        }
                        FramedConnection framedConnection = this.this$0;
                        synchronized (framedConnection) {
                            this.this$0.currentPushRequests.remove(this.val$streamId);
                        }
                    }
                    catch (IOException iOException) {
                        // empty catch block
                        return;
                    }
                }
            });
            return;
        }
        object = new StringBuilder();
        ((StringBuilder)object).append(buffer.size());
        ((StringBuilder)object).append(" != ");
        ((StringBuilder)object).append(n2);
        throw new IOException(((StringBuilder)object).toString());
    }

    private void pushHeadersLater(int n, List<Header> list, boolean bl) {
        this.pushExecutor.execute(new NamedRunnable(this, "OkHttp %s Push Headers[%s]", new Object[]{this.hostname, n}, n, list, bl){
            final FramedConnection this$0;
            final boolean val$inFinished;
            final List val$requestHeaders;
            final int val$streamId;
            {
                this.this$0 = framedConnection;
                this.val$streamId = n;
                this.val$requestHeaders = list;
                this.val$inFinished = bl;
                super(string2, objectArray);
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public void execute() {
                boolean bl = this.this$0.pushObserver.onHeaders(this.val$streamId, this.val$requestHeaders, this.val$inFinished);
                if (bl) {
                    try {
                        this.this$0.frameWriter.rstStream(this.val$streamId, ErrorCode.CANCEL);
                    }
                    catch (IOException iOException) {
                        return;
                    }
                }
                if (!bl) {
                    if (!this.val$inFinished) return;
                }
                FramedConnection framedConnection = this.this$0;
                synchronized (framedConnection) {
                    this.this$0.currentPushRequests.remove(this.val$streamId);
                    return;
                }
            }
        });
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void pushRequestLater(int n, List<Header> list) {
        synchronized (this) {
            if (this.currentPushRequests.contains(n)) {
                this.writeSynResetLater(n, ErrorCode.PROTOCOL_ERROR);
                return;
            }
            this.currentPushRequests.add(n);
        }
        this.pushExecutor.execute(new NamedRunnable(this, "OkHttp %s Push Request[%s]", new Object[]{this.hostname, n}, n, list){
            final FramedConnection this$0;
            final List val$requestHeaders;
            final int val$streamId;
            {
                this.this$0 = framedConnection;
                this.val$streamId = n;
                this.val$requestHeaders = list;
                super(string2, objectArray);
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public void execute() {
                if (!this.this$0.pushObserver.onRequest(this.val$streamId, this.val$requestHeaders)) return;
                try {
                    this.this$0.frameWriter.rstStream(this.val$streamId, ErrorCode.CANCEL);
                    FramedConnection framedConnection = this.this$0;
                    synchronized (framedConnection) {
                        this.this$0.currentPushRequests.remove(this.val$streamId);
                    }
                }
                catch (IOException iOException) {
                    return;
                }
            }
        });
    }

    private void pushResetLater(int n, ErrorCode errorCode) {
        this.pushExecutor.execute(new NamedRunnable(this, "OkHttp %s Push Reset[%s]", new Object[]{this.hostname, n}, n, errorCode){
            final FramedConnection this$0;
            final ErrorCode val$errorCode;
            final int val$streamId;
            {
                this.this$0 = framedConnection;
                this.val$streamId = n;
                this.val$errorCode = errorCode;
                super(string2, objectArray);
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public void execute() {
                this.this$0.pushObserver.onReset(this.val$streamId, this.val$errorCode);
                FramedConnection framedConnection = this.this$0;
                synchronized (framedConnection) {
                    this.this$0.currentPushRequests.remove(this.val$streamId);
                    return;
                }
            }
        });
    }

    private boolean pushedStream(int n) {
        boolean bl = this.protocol == Protocol.HTTP_2 && n != 0 && (n & 1) == 0;
        return bl;
    }

    private Ping removePing(int n) {
        synchronized (this) {
            Object object;
            block6: {
                block5: {
                    object = this.pings;
                    if (object == null) break block5;
                    object = object.remove(n);
                    break block6;
                }
                object = null;
            }
            return object;
            finally {
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void setIdle(boolean bl) {
        synchronized (this) {
            Throwable throwable2;
            block5: {
                long l;
                block4: {
                    if (bl) {
                        try {
                            l = System.nanoTime();
                            break block4;
                        }
                        catch (Throwable throwable2) {
                            break block5;
                        }
                    }
                    l = Long.MAX_VALUE;
                }
                this.idleStartTimeNs = l;
                return;
            }
            throw throwable2;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void writePing(boolean bl, int n, int n2, Ping ping) throws IOException {
        FrameWriter frameWriter = this.frameWriter;
        synchronized (frameWriter) {
            if (ping != null) {
                ping.send();
            }
            this.frameWriter.ping(bl, n, n2);
            return;
        }
    }

    private void writePingLater(boolean bl, int n, int n2, Ping ping) {
        executor.execute(new NamedRunnable(this, "OkHttp %s ping %08x%08x", new Object[]{this.hostname, n, n2}, bl, n, n2, ping){
            final FramedConnection this$0;
            final int val$payload1;
            final int val$payload2;
            final Ping val$ping;
            final boolean val$reply;
            {
                this.this$0 = framedConnection;
                this.val$reply = bl;
                this.val$payload1 = n;
                this.val$payload2 = n2;
                this.val$ping = ping;
                super(string2, objectArray);
            }

            @Override
            public void execute() {
                try {
                    this.this$0.writePing(this.val$reply, this.val$payload1, this.val$payload2, this.val$ping);
                }
                catch (IOException iOException) {
                    // empty catch block
                }
            }
        });
    }

    void addBytesToWriteWindow(long l) {
        this.bytesLeftInWriteWindow += l;
        if (l > 0L) {
            this.notifyAll();
        }
    }

    @Override
    public void close() throws IOException {
        this.close(ErrorCode.NO_ERROR, ErrorCode.CANCEL);
    }

    public void flush() throws IOException {
        this.frameWriter.flush();
    }

    public long getIdleStartTimeNs() {
        synchronized (this) {
            long l = this.idleStartTimeNs;
            return l;
        }
    }

    public Protocol getProtocol() {
        return this.protocol;
    }

    FramedStream getStream(int n) {
        synchronized (this) {
            FramedStream framedStream = this.streams.get(n);
            return framedStream;
        }
    }

    public boolean isIdle() {
        synchronized (this) {
            long l = this.idleStartTimeNs;
            boolean bl = l != Long.MAX_VALUE;
            return bl;
        }
    }

    public int maxConcurrentStreams() {
        synchronized (this) {
            int n = this.peerSettings.getMaxConcurrentStreams(Integer.MAX_VALUE);
            return n;
        }
    }

    public FramedStream newStream(List<Header> list, boolean bl, boolean bl2) throws IOException {
        return this.newStream(0, list, bl, bl2);
    }

    public int openStreamCount() {
        synchronized (this) {
            int n = this.streams.size();
            return n;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public Ping ping() throws IOException {
        int n;
        Object object = new Ping();
        synchronized (this) {
            if (this.shutdown) {
                object = new IOException("shutdown");
                throw object;
            }
            n = this.nextPingId;
            this.nextPingId = n + 2;
            if (this.pings == null) {
                HashMap<Integer, Ping> hashMap = new HashMap<Integer, Ping>();
                this.pings = hashMap;
            }
            this.pings.put(n, (Ping)object);
        }
        this.writePing(false, n, 1330343787, (Ping)object);
        return object;
    }

    public FramedStream pushStream(int n, List<Header> list, boolean bl) throws IOException {
        if (!this.client) {
            if (this.protocol == Protocol.HTTP_2) {
                return this.newStream(n, list, bl, false);
            }
            throw new IllegalStateException("protocol != HTTP_2");
        }
        throw new IllegalStateException("Client cannot push requests.");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    FramedStream removeStream(int n) {
        synchronized (this) {
            FramedStream framedStream = this.streams.remove(n);
            if (framedStream != null && this.streams.isEmpty()) {
                this.setIdle(true);
            }
            this.notifyAll();
            return framedStream;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void setSettings(Settings object) throws IOException {
        FrameWriter frameWriter = this.frameWriter;
        synchronized (frameWriter) {
            synchronized (this) {
                if (!this.shutdown) {
                    this.okHttpSettings.merge((Settings)object);
                    this.frameWriter.settings((Settings)object);
                    return;
                }
                object = new IOException("shutdown");
                throw object;
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void shutdown(ErrorCode errorCode) throws IOException {
        FrameWriter frameWriter = this.frameWriter;
        synchronized (frameWriter) {
            int n;
            synchronized (this) {
                if (this.shutdown) {
                    return;
                }
                this.shutdown = true;
                n = this.lastGoodStreamId;
            }
            this.frameWriter.goAway(n, errorCode, Util.EMPTY_BYTE_ARRAY);
            return;
        }
    }

    public void start() throws IOException {
        this.start(true);
    }

    void start(boolean bl) throws IOException {
        if (bl) {
            this.frameWriter.connectionPreface();
            this.frameWriter.settings(this.okHttpSettings);
            int n = this.okHttpSettings.getInitialWindowSize(65536);
            if (n != 65536) {
                this.frameWriter.windowUpdate(0, n - 65536);
            }
        }
        new Thread(this.readerRunnable).start();
    }

    /*
     * Exception decompiling
     */
    public void writeData(int var1_1, boolean var2_2, Buffer var3_3, long var4_7) throws IOException {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Back jump on a try block [egrp 3[TRYBLOCK] [7 : 174->186)] java.lang.Throwable
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.insertExceptionBlocks(Op02WithProcessedDataAndRefs.java:2283)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:415)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    void writeSynReply(int n, boolean bl, List<Header> list) throws IOException {
        this.frameWriter.synReply(bl, n, list);
    }

    void writeSynReset(int n, ErrorCode errorCode) throws IOException {
        this.frameWriter.rstStream(n, errorCode);
    }

    void writeSynResetLater(int n, ErrorCode errorCode) {
        executor.submit(new NamedRunnable(this, "OkHttp %s stream %d", new Object[]{this.hostname, n}, n, errorCode){
            final FramedConnection this$0;
            final ErrorCode val$errorCode;
            final int val$streamId;
            {
                this.this$0 = framedConnection;
                this.val$streamId = n;
                this.val$errorCode = errorCode;
                super(string2, objectArray);
            }

            @Override
            public void execute() {
                try {
                    this.this$0.writeSynReset(this.val$streamId, this.val$errorCode);
                }
                catch (IOException iOException) {
                    // empty catch block
                }
            }
        });
    }

    void writeWindowUpdateLater(int n, long l) {
        executor.execute(new NamedRunnable(this, "OkHttp Window Update %s stream %d", new Object[]{this.hostname, n}, n, l){
            final FramedConnection this$0;
            final int val$streamId;
            final long val$unacknowledgedBytesRead;
            {
                this.this$0 = framedConnection;
                this.val$streamId = n;
                this.val$unacknowledgedBytesRead = l;
                super(string2, objectArray);
            }

            @Override
            public void execute() {
                try {
                    this.this$0.frameWriter.windowUpdate(this.val$streamId, this.val$unacknowledgedBytesRead);
                }
                catch (IOException iOException) {
                    // empty catch block
                }
            }
        });
    }

    public static class Builder {
        private boolean client;
        private String hostname;
        private Listener listener = Listener.REFUSE_INCOMING_STREAMS;
        private Protocol protocol = Protocol.SPDY_3;
        private PushObserver pushObserver = PushObserver.CANCEL;
        private BufferedSink sink;
        private Socket socket;
        private BufferedSource source;

        public Builder(boolean bl) throws IOException {
            this.client = bl;
        }

        public FramedConnection build() throws IOException {
            return new FramedConnection(this);
        }

        public Builder listener(Listener listener) {
            this.listener = listener;
            return this;
        }

        public Builder protocol(Protocol protocol) {
            this.protocol = protocol;
            return this;
        }

        public Builder pushObserver(PushObserver pushObserver) {
            this.pushObserver = pushObserver;
            return this;
        }

        public Builder socket(Socket socket) throws IOException {
            return this.socket(socket, ((InetSocketAddress)socket.getRemoteSocketAddress()).getHostName(), Okio.buffer(Okio.source(socket)), Okio.buffer(Okio.sink(socket)));
        }

        public Builder socket(Socket socket, String string2, BufferedSource bufferedSource, BufferedSink bufferedSink) {
            this.socket = socket;
            this.hostname = string2;
            this.source = bufferedSource;
            this.sink = bufferedSink;
            return this;
        }
    }

    public static abstract class Listener {
        public static final Listener REFUSE_INCOMING_STREAMS = new Listener(){

            @Override
            public void onStream(FramedStream framedStream) throws IOException {
                framedStream.close(ErrorCode.REFUSED_STREAM);
            }
        };

        public void onSettings(FramedConnection framedConnection) {
        }

        public abstract void onStream(FramedStream var1) throws IOException;
    }

    class Reader
    extends NamedRunnable
    implements FrameReader.Handler {
        final FrameReader frameReader;
        final FramedConnection this$0;

        private Reader(FramedConnection framedConnection, FrameReader frameReader) {
            this.this$0 = framedConnection;
            super("OkHttp %s", framedConnection.hostname);
            this.frameReader = frameReader;
        }

        private void ackSettingsLater(Settings settings) {
            executor.execute(new NamedRunnable(this, "OkHttp %s ACK Settings", new Object[]{this.this$0.hostname}, settings){
                final Reader this$1;
                final Settings val$peerSettings;
                {
                    this.this$1 = reader;
                    this.val$peerSettings = settings;
                    super(string2, objectArray);
                }

                @Override
                public void execute() {
                    try {
                        this.this$1.this$0.frameWriter.ackSettings(this.val$peerSettings);
                    }
                    catch (IOException iOException) {
                        // empty catch block
                    }
                }
            });
        }

        @Override
        public void ackSettings() {
        }

        @Override
        public void alternateService(int n, String string2, ByteString byteString, String string3, int n2, long l) {
        }

        @Override
        public void data(boolean bl, int n, BufferedSource bufferedSource, int n2) throws IOException {
            if (this.this$0.pushedStream(n)) {
                this.this$0.pushDataLater(n, bufferedSource, n2, bl);
                return;
            }
            FramedStream framedStream = this.this$0.getStream(n);
            if (framedStream == null) {
                this.this$0.writeSynResetLater(n, ErrorCode.INVALID_STREAM);
                bufferedSource.skip(n2);
                return;
            }
            framedStream.receiveData(bufferedSource, n2);
            if (bl) {
                framedStream.receiveFin();
            }
        }

        /*
         * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
         * Loose catch block
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        protected void execute() {
            Throwable throwable2222222;
            ErrorCode errorCode;
            ErrorCode errorCode2;
            block12: {
                block13: {
                    ErrorCode errorCode3 = ErrorCode.INTERNAL_ERROR;
                    errorCode2 = ErrorCode.INTERNAL_ERROR;
                    errorCode = errorCode3;
                    ErrorCode errorCode4 = errorCode3;
                    if (!this.this$0.client) {
                        errorCode = errorCode3;
                        errorCode4 = errorCode3;
                        this.frameReader.readConnectionPreface();
                    }
                    do {
                        errorCode = errorCode3;
                        errorCode4 = errorCode3;
                    } while (this.frameReader.nextFrame(this));
                    errorCode = errorCode3;
                    errorCode4 = errorCode3;
                    errorCode = errorCode3 = ErrorCode.NO_ERROR;
                    errorCode4 = errorCode3;
                    ErrorCode errorCode5 = ErrorCode.CANCEL;
                    {
                        catch (Throwable throwable2222222) {
                            break block12;
                        }
                        catch (IOException iOException) {}
                        errorCode = errorCode4;
                        {
                            errorCode = errorCode4 = ErrorCode.PROTOCOL_ERROR;
                            errorCode3 = ErrorCode.PROTOCOL_ERROR;
                        }
                        try {
                            this.this$0.close(errorCode4, errorCode3);
                        }
                        catch (IOException iOException) {
                            // empty catch block
                        }
                        break block13;
                    }
                    try {
                        this.this$0.close(errorCode3, errorCode5);
                    }
                    catch (IOException iOException) {}
                }
                Util.closeQuietly(this.frameReader);
                return;
            }
            try {
                this.this$0.close(errorCode, errorCode2);
            }
            catch (IOException iOException) {
                // empty catch block
            }
            Util.closeQuietly(this.frameReader);
            throw throwable2222222;
        }

        /*
         * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
         * Loose catch block
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void goAway(int n, ErrorCode framedStreamArray, ByteString object) {
            ((ByteString)object).size();
            object = this.this$0;
            synchronized (object) {
                framedStreamArray = this.this$0.streams.values().toArray(new FramedStream[this.this$0.streams.size()]);
                FramedConnection.access$1602(this.this$0, true);
                {
                    catch (Throwable throwable) {}
                    {
                        throw throwable;
                    }
                }
            }
            int n2 = framedStreamArray.length;
            int n3 = 0;
            while (n3 < n2) {
                object = framedStreamArray[n3];
                if (((FramedStream)object).getId() > n && ((FramedStream)object).isLocallyInitiated()) {
                    ((FramedStream)object).receiveRstStream(ErrorCode.REFUSED_STREAM);
                    this.this$0.removeStream(((FramedStream)object).getId());
                }
                ++n3;
            }
        }

        /*
         * WARNING - void declaration
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Converted monitor instructions to comments
         * Lifted jumps to return sites
         */
        @Override
        public void headers(boolean bl, boolean bl2, int n, int n2, List<Header> object, HeadersMode object2) {
            void var6_6;
            if (this.this$0.pushedStream(n)) {
                this.this$0.pushHeadersLater(n, (List)object, bl2);
                return;
            }
            FramedConnection framedConnection = this.this$0;
            // MONITORENTER : framedConnection
            if (this.this$0.shutdown) {
                // MONITOREXIT : framedConnection
                return;
            }
            Object object3 = this.this$0.getStream(n);
            if (object3 == null) {
                if (var6_6.failIfStreamAbsent()) {
                    this.this$0.writeSynResetLater(n, ErrorCode.INVALID_STREAM);
                    // MONITOREXIT : framedConnection
                    return;
                }
                if (n <= this.this$0.lastGoodStreamId) {
                    // MONITOREXIT : framedConnection
                    return;
                }
                if (n % 2 == this.this$0.nextStreamId % 2) {
                    // MONITOREXIT : framedConnection
                    return;
                }
                FramedStream framedStream = new FramedStream(n, this.this$0, bl, bl2, (List<Header>)object);
                FramedConnection.access$1702(this.this$0, n);
                this.this$0.streams.put(n, framedStream);
                object = executor;
                object3 = new NamedRunnable(this, "OkHttp %s stream %d", new Object[]{this.this$0.hostname, n}, framedStream){
                    final Reader this$1;
                    final FramedStream val$newStream;
                    {
                        this.this$1 = reader;
                        this.val$newStream = framedStream;
                        super(string2, objectArray);
                    }

                    @Override
                    public void execute() {
                        try {
                            this.this$1.this$0.listener.onStream(this.val$newStream);
                        }
                        catch (IOException iOException) {
                            Platform platform = Platform.get();
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("FramedConnection.Listener failure for ");
                            stringBuilder.append(this.this$1.this$0.hostname);
                            platform.log(4, stringBuilder.toString(), iOException);
                            try {
                                this.val$newStream.close(ErrorCode.PROTOCOL_ERROR);
                            }
                            catch (IOException iOException2) {
                                // empty catch block
                            }
                        }
                    }
                };
                object.execute((Runnable)object3);
                // MONITOREXIT : framedConnection
                return;
            }
            // MONITOREXIT : framedConnection
            if (var6_6.failIfStreamPresent()) {
                ((FramedStream)object3).closeLater(ErrorCode.PROTOCOL_ERROR);
                this.this$0.removeStream(n);
                return;
            }
            ((FramedStream)object3).receiveHeaders((List<Header>)object, (HeadersMode)var6_6);
            if (!bl2) return;
            ((FramedStream)object3).receiveFin();
        }

        @Override
        public void ping(boolean bl, int n, int n2) {
            if (bl) {
                Ping ping = this.this$0.removePing(n);
                if (ping != null) {
                    ping.receive();
                }
            } else {
                this.this$0.writePingLater(true, n, n2, null);
            }
        }

        @Override
        public void priority(int n, int n2, int n3, boolean bl) {
        }

        @Override
        public void pushPromise(int n, int n2, List<Header> list) {
            this.this$0.pushRequestLater(n2, list);
        }

        @Override
        public void rstStream(int n, ErrorCode errorCode) {
            if (this.this$0.pushedStream(n)) {
                this.this$0.pushResetLater(n, errorCode);
                return;
            }
            FramedStream framedStream = this.this$0.removeStream(n);
            if (framedStream != null) {
                framedStream.receiveRstStream(errorCode);
            }
        }

        /*
         * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void settings(boolean var1_1, Settings var2_2) {
            var7_4 = 0L;
            var9_5 = null;
            var10_6 = this.this$0;
            synchronized (var10_6) {
                var3_7 = this.this$0.peerSettings.getInitialWindowSize(65536);
                if (var1_1) {
                    this.this$0.peerSettings.clear();
                }
                this.this$0.peerSettings.merge((Settings)var2_2 /* !! */ );
                if (this.this$0.getProtocol() == Protocol.HTTP_2) {
                    this.ackSettingsLater((Settings)var2_2 /* !! */ );
                }
                var4_8 = this.this$0.peerSettings.getInitialWindowSize(65536);
                var5_9 = var7_4;
                var2_2 /* !! */  = var9_5;
                if (var4_8 != -1) {
                    var5_9 = var7_4;
                    var2_2 /* !! */  = var9_5;
                    if (var4_8 != var3_7) {
                    }
                }
                ** GOTO lbl34
                {
                    catch (Throwable var2_3) {}
                    {
                        throw var2_3;
                    }
                }
                var7_4 = var4_8 - var3_7;
                {
                    if (!FramedConnection.access$2300(this.this$0)) {
                        this.this$0.addBytesToWriteWindow(var7_4);
                        FramedConnection.access$2302(this.this$0, true);
                    }
                    var5_9 = var7_4;
                    var2_2 /* !! */  = var9_5;
                    if (!FramedConnection.access$1900(this.this$0).isEmpty()) {
                        var2_2 /* !! */  = FramedConnection.access$1900(this.this$0).values().toArray(new FramedStream[FramedConnection.access$1900(this.this$0).size()]);
                        var5_9 = var7_4;
                    }
lbl34:
                    // 4 sources

                    var12_10 = FramedConnection.access$2100();
                    var9_5 = FramedConnection.access$1100(this.this$0);
                    var3_7 = 0;
                    var11_11 = new NamedRunnable(this, "OkHttp %s settings", new Object[]{var9_5}){
                        final Reader this$1;
                        {
                            this.this$1 = reader;
                            super(string2, objectArray);
                        }

                        @Override
                        public void execute() {
                            this.this$1.this$0.listener.onSettings(this.this$1.this$0);
                        }
                    };
                    var12_10.execute(var11_11);
                    // MONITOREXIT @DISABLED, blocks:[1, 5] lbl38 : MonitorExitStatement: MONITOREXIT : var10_6
                    if (var2_2 /* !! */  == null) return;
                    if (var5_9 == 0L) return;
                }
            }
            var4_8 = var2_2 /* !! */ .length;
            while (var3_7 < var4_8) {
                var9_5 = var2_2 /* !! */ [var3_7];
                synchronized (var9_5) {
                    var9_5.addBytesToWriteWindow(var5_9);
                }
                ++var3_7;
            }
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void windowUpdate(int n, long l) {
            if (n == 0) {
                FramedConnection framedConnection = this.this$0;
                synchronized (framedConnection) {
                    FramedConnection framedConnection2 = this.this$0;
                    framedConnection2.bytesLeftInWriteWindow += l;
                    this.this$0.notifyAll();
                    return;
                }
            }
            FramedStream framedStream = this.this$0.getStream(n);
            if (framedStream == null) return;
            synchronized (framedStream) {
                framedStream.addBytesToWriteWindow(l);
                return;
            }
        }
    }
}

