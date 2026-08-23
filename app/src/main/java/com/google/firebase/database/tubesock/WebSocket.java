/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.net.SSLCertificateSocketFactory
 *  android.net.SSLSessionCache
 */
package com.google.firebase.database.tubesock;

import android.net.SSLCertificateSocketFactory;
import android.net.SSLSessionCache;
import com.google.firebase.database.connection.ConnectionContext;
import com.google.firebase.database.logging.LogWrapper;
import com.google.firebase.database.logging.Logger;
import com.google.firebase.database.tubesock.ThreadInitializer;
import com.google.firebase.database.tubesock.WebSocketEventHandler;
import com.google.firebase.database.tubesock.WebSocketException;
import com.google.firebase.database.tubesock.WebSocketHandshake;
import com.google.firebase.database.tubesock.WebSocketReceiver;
import com.google.firebase.database.tubesock.WebSocketWriter;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocket;

public class WebSocket {
    static final byte OPCODE_BINARY = 2;
    static final byte OPCODE_CLOSE = 8;
    static final byte OPCODE_NONE = 0;
    static final byte OPCODE_PING = 9;
    static final byte OPCODE_PONG = 10;
    static final byte OPCODE_TEXT = 1;
    private static final int SSL_HANDSHAKE_TIMEOUT_MS = 60000;
    private static final String THREAD_BASE_NAME = "TubeSock";
    private static final Charset UTF8;
    private static final AtomicInteger clientCount;
    private static ThreadInitializer intializer;
    private static ThreadFactory threadFactory;
    private final int clientId;
    private WebSocketEventHandler eventHandler = null;
    private final WebSocketHandshake handshake;
    private final Thread innerThread;
    private final LogWrapper logger;
    private final WebSocketReceiver receiver;
    private volatile Socket socket = null;
    private final String sslCacheDirectory;
    private volatile State state = State.NONE;
    private final URI url;
    private final WebSocketWriter writer;

    static {
        clientCount = new AtomicInteger(0);
        UTF8 = Charset.forName("UTF-8");
        threadFactory = Executors.defaultThreadFactory();
        intializer = new ThreadInitializer(){

            @Override
            public void setName(Thread thread2, String string2) {
                thread2.setName(string2);
            }
        };
    }

    public WebSocket(ConnectionContext connectionContext, URI uRI) {
        this(connectionContext, uRI, null);
    }

    public WebSocket(ConnectionContext connectionContext, URI uRI, String string2) {
        this(connectionContext, uRI, string2, null);
    }

    public WebSocket(ConnectionContext object, URI uRI, String string2, Map<String, String> map) {
        int n;
        this.clientId = n = clientCount.incrementAndGet();
        this.innerThread = WebSocket.getThreadFactory().newThread(new Runnable(this){
            final WebSocket this$0;
            {
                this.this$0 = webSocket;
            }

            @Override
            public void run() {
                this.this$0.runReader();
            }
        });
        this.url = uRI;
        this.sslCacheDirectory = ((ConnectionContext)object).getSslCacheDirectory();
        Logger logger = ((ConnectionContext)object).getLogger();
        object = new StringBuilder();
        ((StringBuilder)object).append("sk_");
        ((StringBuilder)object).append(n);
        this.logger = new LogWrapper(logger, "WebSocket", ((StringBuilder)object).toString());
        this.handshake = new WebSocketHandshake(uRI, string2, map);
        this.receiver = new WebSocketReceiver(this);
        this.writer = new WebSocketWriter(this, THREAD_BASE_NAME, n);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void closeSocket() {
        synchronized (this) {
            Object object = this.state;
            State state = State.DISCONNECTED;
            if (object == state) {
                return;
            }
            this.receiver.stopit();
            this.writer.stopIt();
            object = this.socket;
            if (object != null) {
                try {
                    this.socket.close();
                }
                catch (Exception exception) {
                    object = this.eventHandler;
                    WebSocketException webSocketException = new WebSocketException("Failed to close", exception);
                    object.onError(webSocketException);
                }
            }
            this.state = State.DISCONNECTED;
            this.eventHandler.onClose();
            return;
        }
    }

    private Socket createSocket() {
        Object object;
        block14: {
            StringBuilder stringBuilder;
            String string2;
            block13: {
                object = this.url.getScheme();
                string2 = this.url.getHost();
                int n = this.url.getPort();
                if (object != null && ((String)object).equals("ws")) {
                    int n2 = n;
                    if (n == -1) {
                        n2 = 80;
                    }
                    try {
                        object = new Socket(string2, n2);
                    }
                    catch (IOException iOException) {
                        object = new StringBuilder();
                        ((StringBuilder)object).append("error while creating socket to ");
                        ((StringBuilder)object).append(this.url);
                        throw new WebSocketException(((StringBuilder)object).toString(), iOException);
                    }
                    catch (UnknownHostException unknownHostException) {
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("unknown host: ");
                        stringBuilder2.append(string2);
                        throw new WebSocketException(stringBuilder2.toString(), unknownHostException);
                    }
                }
                if (object == null || !((String)object).equals("wss")) break block14;
                int n3 = n;
                if (n == -1) {
                    n3 = 443;
                }
                stringBuilder = null;
                object = null;
                try {
                    if (this.sslCacheDirectory != null) {
                        File file = new File(this.sslCacheDirectory);
                        object = new SSLSessionCache(file);
                    }
                }
                catch (IOException iOException) {
                    this.logger.debug("Failed to initialize SSL session cache", iOException, new Object[0]);
                    object = stringBuilder;
                }
                object = (SSLSocket)SSLCertificateSocketFactory.getDefault((int)60000, (SSLSessionCache)object).createSocket(string2, n3);
                if (!HttpsURLConnection.getDefaultHostnameVerifier().verify(string2, ((SSLSocket)object).getSession())) break block13;
                return object;
            }
            try {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Error while verifying secure socket to ");
                stringBuilder.append(this.url);
                object = new WebSocketException(stringBuilder.toString());
                throw object;
            }
            catch (IOException iOException) {
                object = new StringBuilder();
                ((StringBuilder)object).append("error while creating secure socket to ");
                ((StringBuilder)object).append(this.url);
                throw new WebSocketException(((StringBuilder)object).toString(), iOException);
            }
            catch (UnknownHostException unknownHostException) {
                object = new StringBuilder();
                ((StringBuilder)object).append("unknown host: ");
                ((StringBuilder)object).append(string2);
                throw new WebSocketException(((StringBuilder)object).toString(), unknownHostException);
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("unsupported protocol: ");
        stringBuilder.append((String)object);
        throw new WebSocketException(stringBuilder.toString());
    }

    static ThreadInitializer getIntializer() {
        return intializer;
    }

    static ThreadFactory getThreadFactory() {
        return threadFactory;
    }

    /*
     * Exception decompiling
     */
    private void runReader() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Back jump on a try block [egrp 12[TRYBLOCK] [32 : 477->479)] java.lang.Throwable
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

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private void send(byte by, byte[] object) {
        synchronized (this) {
            if (this.state != State.CONNECTED) {
                object = this.eventHandler;
                WebSocketException webSocketException = new WebSocketException("error while sending data: not connected");
                object.onError(webSocketException);
                return;
            }
            try {
                this.writer.send(by, true, (byte[])object);
                return;
            }
            catch (IOException iOException) {
                WebSocketEventHandler webSocketEventHandler = this.eventHandler;
                WebSocketException webSocketException = new WebSocketException("Failed to send frame", iOException);
                webSocketEventHandler.onError(webSocketException);
                this.close();
                return;
            }
        }
    }

    private void sendCloseHandshake() {
        try {
            this.state = State.DISCONNECTING;
            this.writer.stopIt();
            this.writer.send((byte)8, true, new byte[0]);
        }
        catch (IOException iOException) {
            this.eventHandler.onError(new WebSocketException("Failed to send close frame", iOException));
        }
    }

    public static void setThreadFactory(ThreadFactory threadFactory, ThreadInitializer threadInitializer) {
        WebSocket.threadFactory = threadFactory;
        intializer = threadInitializer;
    }

    public void blockClose() throws InterruptedException {
        if (this.writer.getInnerThread().getState() != Thread.State.NEW) {
            this.writer.getInnerThread().join();
        }
        this.getInnerThread().join();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void close() {
        synchronized (this) {
            int n = 3.$SwitchMap$com$google$firebase$database$tubesock$WebSocket$State[this.state.ordinal()];
            switch (n) {
                default: {
                    return;
                }
                case 5: {
                    return;
                }
                case 4: {
                    return;
                }
                case 3: {
                    this.sendCloseHandshake();
                    return;
                }
                case 2: {
                    this.closeSocket();
                    return;
                }
                case 1: 
            }
            this.state = State.DISCONNECTED;
            return;
        }
    }

    public void connect() {
        synchronized (this) {
            if (this.state != State.NONE) {
                WebSocketEventHandler webSocketEventHandler = this.eventHandler;
                WebSocketException webSocketException = new WebSocketException("connect() already called");
                webSocketEventHandler.onError(webSocketException);
                this.close();
                return;
            }
            ThreadInitializer threadInitializer = WebSocket.getIntializer();
            Thread thread2 = this.getInnerThread();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("TubeSockReader-");
            stringBuilder.append(this.clientId);
            threadInitializer.setName(thread2, stringBuilder.toString());
            this.state = State.CONNECTING;
            this.getInnerThread().start();
            return;
        }
    }

    WebSocketEventHandler getEventHandler() {
        return this.eventHandler;
    }

    Thread getInnerThread() {
        return this.innerThread;
    }

    void handleReceiverError(WebSocketException webSocketException) {
        this.eventHandler.onError(webSocketException);
        if (this.state == State.CONNECTED) {
            this.close();
        }
        this.closeSocket();
    }

    void onCloseOpReceived() {
        this.closeSocket();
    }

    void pong(byte[] byArray) {
        synchronized (this) {
            this.send((byte)10, byArray);
            return;
        }
    }

    public void send(String string2) {
        synchronized (this) {
            this.send((byte)1, string2.getBytes(UTF8));
            return;
        }
    }

    public void send(byte[] byArray) {
        synchronized (this) {
            this.send((byte)2, byArray);
            return;
        }
    }

    public void setEventHandler(WebSocketEventHandler webSocketEventHandler) {
        this.eventHandler = webSocketEventHandler;
    }

    private static final class State
    extends Enum<State> {
        private static final State[] $VALUES;
        public static final /* enum */ State CONNECTED;
        public static final /* enum */ State CONNECTING;
        public static final /* enum */ State DISCONNECTED;
        public static final /* enum */ State DISCONNECTING;
        public static final /* enum */ State NONE;

        static {
            State state;
            State state2;
            State state3;
            State state4;
            State state5;
            NONE = state5 = new State();
            CONNECTING = state4 = new State();
            CONNECTED = state3 = new State();
            DISCONNECTING = state2 = new State();
            DISCONNECTED = state = new State();
            $VALUES = new State[]{state5, state4, state3, state2, state};
        }

        public static State valueOf(String string2) {
            return Enum.valueOf(State.class, string2);
        }

        public static State[] values() {
            return (State[])$VALUES.clone();
        }
    }
}

