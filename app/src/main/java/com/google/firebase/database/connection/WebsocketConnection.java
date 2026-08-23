/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.connection;

import com.google.firebase.database.connection.ConnectionContext;
import com.google.firebase.database.connection.HostInfo;
import com.google.firebase.database.connection.util.StringListReader;
import com.google.firebase.database.logging.LogWrapper;
import com.google.firebase.database.logging.Logger;
import com.google.firebase.database.tubesock.WebSocket;
import com.google.firebase.database.tubesock.WebSocketEventHandler;
import com.google.firebase.database.tubesock.WebSocketException;
import com.google.firebase.database.tubesock.WebSocketMessage;
import com.google.firebase.database.util.JsonMapper;
import java.io.EOFException;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

class WebsocketConnection {
    private static final long CONNECT_TIMEOUT_MS = 30000L;
    private static final long KEEP_ALIVE_TIMEOUT_MS = 45000L;
    private static final int MAX_FRAME_SIZE = 16384;
    private static long connectionId = 0L;
    private WSClient conn;
    private ScheduledFuture<?> connectTimeout;
    private final ConnectionContext connectionContext;
    private Delegate delegate;
    private boolean everConnected = false;
    private final ScheduledExecutorService executorService;
    private StringListReader frameReader;
    private boolean isClosed = false;
    private ScheduledFuture<?> keepAlive;
    private final LogWrapper logger;
    private long totalFrames = 0L;

    public WebsocketConnection(ConnectionContext object, HostInfo hostInfo, String string2, String string3, Delegate object2, String string4) {
        this.connectionContext = object;
        this.executorService = ((ConnectionContext)object).getExecutorService();
        this.delegate = object2;
        long l = connectionId;
        connectionId = 1L + l;
        object2 = ((ConnectionContext)object).getLogger();
        object = new StringBuilder();
        ((StringBuilder)object).append("ws_");
        ((StringBuilder)object).append(l);
        this.logger = new LogWrapper((Logger)object2, "WebSocket", ((StringBuilder)object).toString());
        this.conn = this.createConnection(hostInfo, string2, string3, string4);
    }

    static /* synthetic */ boolean access$102(WebsocketConnection websocketConnection, boolean bl) {
        websocketConnection.everConnected = bl;
        return bl;
    }

    private void appendFrame(String object) {
        long l;
        this.frameReader.addString((String)object);
        this.totalFrames = l = this.totalFrames - 1L;
        if (l == 0L) {
            try {
                this.frameReader.freeze();
                Map<String, Object> map = JsonMapper.parseJson(this.frameReader.toString());
                this.frameReader = null;
                if (this.logger.logsDebug()) {
                    object = this.logger;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("handleIncomingFrame complete frame: ");
                    stringBuilder.append(map);
                    ((LogWrapper)object).debug(stringBuilder.toString(), new Object[0]);
                }
                this.delegate.onMessage(map);
            }
            catch (ClassCastException classCastException) {
                LogWrapper logWrapper = this.logger;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Error parsing frame (cast error): ");
                stringBuilder.append(this.frameReader.toString());
                logWrapper.error(stringBuilder.toString(), classCastException);
                this.close();
                this.shutdown();
            }
            catch (IOException iOException) {
                LogWrapper logWrapper = this.logger;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Error parsing frame: ");
                stringBuilder.append(this.frameReader.toString());
                logWrapper.error(stringBuilder.toString(), iOException);
                this.close();
                this.shutdown();
            }
        }
    }

    private void closeIfNeverConnected() {
        if (!this.everConnected && !this.isClosed) {
            if (this.logger.logsDebug()) {
                this.logger.debug("timed out on connect", new Object[0]);
            }
            this.conn.close();
        }
    }

    private WSClient createConnection(HostInfo object, String object2, String string2, String string3) {
        if (object2 == null) {
            object2 = ((HostInfo)object).getHost();
        }
        object2 = HostInfo.getConnectionUrl((String)object2, ((HostInfo)object).isSecure(), ((HostInfo)object).getNamespace(), string3);
        object = new HashMap();
        object.put("User-Agent", this.connectionContext.getUserAgent());
        object.put("X-Firebase-GMPID", this.connectionContext.getApplicationId());
        object.put("X-Firebase-AppCheck", string2);
        return new WSClientTubesock(this, new WebSocket(this.connectionContext, (URI)object2, null, (Map<String, String>)object));
    }

    private String extractFrameCount(String string2) {
        if (string2.length() <= 6) {
            block4: {
                int n = Integer.parseInt(string2);
                if (n <= 0) break block4;
                try {
                    this.handleNewFrameCount(n);
                }
                catch (NumberFormatException numberFormatException) {
                    // empty catch block
                }
            }
            return null;
        }
        this.handleNewFrameCount(1);
        return string2;
    }

    private void handleIncomingFrame(String string2) {
        if (!this.isClosed) {
            this.resetKeepAlive();
            if (this.isBuffering()) {
                this.appendFrame(string2);
            } else if ((string2 = this.extractFrameCount(string2)) != null) {
                this.appendFrame(string2);
            }
        }
    }

    private void handleNewFrameCount(int n) {
        this.totalFrames = n;
        this.frameReader = new StringListReader();
        if (this.logger.logsDebug()) {
            LogWrapper logWrapper = this.logger;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("HandleNewFrameCount: ");
            stringBuilder.append(this.totalFrames);
            logWrapper.debug(stringBuilder.toString(), new Object[0]);
        }
    }

    private boolean isBuffering() {
        boolean bl = this.frameReader != null;
        return bl;
    }

    private Runnable nop() {
        return new Runnable(this){
            final WebsocketConnection this$0;
            {
                this.this$0 = websocketConnection;
            }

            @Override
            public void run() {
                if (this.this$0.conn != null) {
                    this.this$0.conn.send("0");
                    this.this$0.resetKeepAlive();
                }
            }
        };
    }

    private void onClosed() {
        if (!this.isClosed) {
            if (this.logger.logsDebug()) {
                this.logger.debug("closing itself", new Object[0]);
            }
            this.shutdown();
        }
        this.conn = null;
        ScheduledFuture<?> scheduledFuture = this.keepAlive;
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
        }
    }

    private void resetKeepAlive() {
        if (!this.isClosed) {
            ScheduledFuture<?> scheduledFuture = this.keepAlive;
            if (scheduledFuture != null) {
                scheduledFuture.cancel(false);
                if (this.logger.logsDebug()) {
                    scheduledFuture = this.logger;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Reset keepAlive. Remaining: ");
                    stringBuilder.append(this.keepAlive.getDelay(TimeUnit.MILLISECONDS));
                    ((LogWrapper)((Object)scheduledFuture)).debug(stringBuilder.toString(), new Object[0]);
                }
            } else if (this.logger.logsDebug()) {
                this.logger.debug("Reset keepAlive", new Object[0]);
            }
            this.keepAlive = this.executorService.schedule(this.nop(), 45000L, TimeUnit.MILLISECONDS);
        }
    }

    private void shutdown() {
        this.isClosed = true;
        this.delegate.onDisconnect(this.everConnected);
    }

    private static String[] splitIntoFrames(String string2, int n) {
        if (string2.length() <= n) {
            return new String[]{string2};
        }
        ArrayList<String> arrayList = new ArrayList<String>();
        for (int i = 0; i < string2.length(); i += n) {
            arrayList.add(string2.substring(i, Math.min(i + n, string2.length())));
        }
        return arrayList.toArray(new String[arrayList.size()]);
    }

    public void close() {
        if (this.logger.logsDebug()) {
            this.logger.debug("websocket is being closed", new Object[0]);
        }
        this.isClosed = true;
        this.conn.close();
        ScheduledFuture<?> scheduledFuture = this.connectTimeout;
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
        }
        if ((scheduledFuture = this.keepAlive) != null) {
            scheduledFuture.cancel(true);
        }
    }

    public void open() {
        this.conn.connect();
        this.connectTimeout = this.executorService.schedule(new Runnable(this){
            final WebsocketConnection this$0;
            {
                this.this$0 = websocketConnection;
            }

            @Override
            public void run() {
                this.this$0.closeIfNeverConnected();
            }
        }, 30000L, TimeUnit.MILLISECONDS);
    }

    public void send(Map<String, Object> map) {
        int n;
        Object object;
        Object object2;
        this.resetKeepAlive();
        try {
            object2 = WebsocketConnection.splitIntoFrames(JsonMapper.serializeJson(map), 16384);
            if (((String[])object2).length > 1) {
                WSClient wSClient = this.conn;
                object = new StringBuilder();
                ((StringBuilder)object).append("");
                ((StringBuilder)object).append(((Object)object2).length);
                wSClient.send(((StringBuilder)object).toString());
            }
            n = 0;
        }
        catch (IOException iOException) {
            object = this.logger;
            object2 = new StringBuilder();
            ((StringBuilder)object2).append("Failed to serialize message: ");
            ((StringBuilder)object2).append(map.toString());
            ((LogWrapper)object).error(((StringBuilder)object2).toString(), iOException);
            this.shutdown();
        }
        while (true) {
            if (n < ((Object)object2).length) {
                this.conn.send((String)object2[n]);
                ++n;
                continue;
            }
            break;
        }
    }

    public void start() {
    }

    public static interface Delegate {
        public void onDisconnect(boolean var1);

        public void onMessage(Map<String, Object> var1);
    }

    private static interface WSClient {
        public void close();

        public void connect();

        public void send(String var1);
    }

    private class WSClientTubesock
    implements WSClient,
    WebSocketEventHandler {
        final WebsocketConnection this$0;
        private WebSocket ws;

        private WSClientTubesock(WebsocketConnection websocketConnection, WebSocket webSocket) {
            this.this$0 = websocketConnection;
            this.ws = webSocket;
            webSocket.setEventHandler(this);
        }

        private void shutdown() {
            this.ws.close();
            try {
                this.ws.blockClose();
            }
            catch (InterruptedException interruptedException) {
                this.this$0.logger.error("Interrupted while shutting down websocket threads", interruptedException);
            }
        }

        @Override
        public void close() {
            this.ws.close();
        }

        @Override
        public void connect() {
            try {
                this.ws.connect();
            }
            catch (WebSocketException webSocketException) {
                if (this.this$0.logger.logsDebug()) {
                    this.this$0.logger.debug("Error connecting", webSocketException, new Object[0]);
                }
                this.shutdown();
            }
        }

        @Override
        public void onClose() {
            this.this$0.executorService.execute(new Runnable(this){
                final WSClientTubesock this$1;
                {
                    this.this$1 = wSClientTubesock;
                }

                @Override
                public void run() {
                    if (this.this$1.this$0.logger.logsDebug()) {
                        this.this$1.this$0.logger.debug("closed", new Object[0]);
                    }
                    this.this$1.this$0.onClosed();
                }
            });
        }

        @Override
        public void onError(WebSocketException webSocketException) {
            this.this$0.executorService.execute(new Runnable(this, webSocketException){
                final WSClientTubesock this$1;
                final WebSocketException val$e;
                {
                    this.this$1 = wSClientTubesock;
                    this.val$e = webSocketException;
                }

                @Override
                public void run() {
                    if (this.val$e.getCause() != null && this.val$e.getCause() instanceof EOFException) {
                        this.this$1.this$0.logger.debug("WebSocket reached EOF.", new Object[0]);
                    } else {
                        this.this$1.this$0.logger.debug("WebSocket error.", this.val$e, new Object[0]);
                    }
                    this.this$1.this$0.onClosed();
                }
            });
        }

        @Override
        public void onLogMessage(String string2) {
            if (this.this$0.logger.logsDebug()) {
                LogWrapper logWrapper = this.this$0.logger;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Tubesock: ");
                stringBuilder.append(string2);
                logWrapper.debug(stringBuilder.toString(), new Object[0]);
            }
        }

        @Override
        public void onMessage(WebSocketMessage object) {
            object = ((WebSocketMessage)object).getText();
            if (this.this$0.logger.logsDebug()) {
                LogWrapper logWrapper = this.this$0.logger;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("ws message: ");
                stringBuilder.append((String)object);
                logWrapper.debug(stringBuilder.toString(), new Object[0]);
            }
            this.this$0.executorService.execute(new Runnable(this, (String)object){
                final WSClientTubesock this$1;
                final String val$str;
                {
                    this.this$1 = wSClientTubesock;
                    this.val$str = string2;
                }

                @Override
                public void run() {
                    this.this$1.this$0.handleIncomingFrame(this.val$str);
                }
            });
        }

        @Override
        public void onOpen() {
            this.this$0.executorService.execute(new Runnable(this){
                final WSClientTubesock this$1;
                {
                    this.this$1 = wSClientTubesock;
                }

                @Override
                public void run() {
                    this.this$1.this$0.connectTimeout.cancel(false);
                    WebsocketConnection.access$102(this.this$1.this$0, true);
                    if (this.this$1.this$0.logger.logsDebug()) {
                        this.this$1.this$0.logger.debug("websocket opened", new Object[0]);
                    }
                    this.this$1.this$0.resetKeepAlive();
                }
            });
        }

        @Override
        public void send(String string2) {
            this.ws.send(string2);
        }
    }
}

