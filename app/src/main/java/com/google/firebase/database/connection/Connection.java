/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.connection;

import com.google.firebase.database.connection.ConnectionContext;
import com.google.firebase.database.connection.HostInfo;
import com.google.firebase.database.connection.WebsocketConnection;
import com.google.firebase.database.logging.LogWrapper;
import com.google.firebase.database.logging.Logger;
import java.util.HashMap;
import java.util.Map;

class Connection
implements WebsocketConnection.Delegate {
    private static final String REQUEST_PAYLOAD = "d";
    private static final String REQUEST_TYPE = "t";
    private static final String REQUEST_TYPE_DATA = "d";
    private static final String SERVER_CONTROL_MESSAGE = "c";
    private static final String SERVER_CONTROL_MESSAGE_DATA = "d";
    private static final String SERVER_CONTROL_MESSAGE_HELLO = "h";
    private static final String SERVER_CONTROL_MESSAGE_RESET = "r";
    private static final String SERVER_CONTROL_MESSAGE_SHUTDOWN = "s";
    private static final String SERVER_CONTROL_MESSAGE_TYPE = "t";
    private static final String SERVER_DATA_MESSAGE = "d";
    private static final String SERVER_ENVELOPE_DATA = "d";
    private static final String SERVER_ENVELOPE_TYPE = "t";
    private static final String SERVER_HELLO_HOST = "h";
    private static final String SERVER_HELLO_SESSION_ID = "s";
    private static final String SERVER_HELLO_TIMESTAMP = "ts";
    private static long connectionIds = 0L;
    private WebsocketConnection conn;
    private Delegate delegate;
    private HostInfo hostInfo;
    private final LogWrapper logger;
    private State state;

    public Connection(ConnectionContext connectionContext, HostInfo hostInfo, String string2, Delegate object, String string3, String string4) {
        long l = connectionIds;
        connectionIds = 1L + l;
        this.hostInfo = hostInfo;
        this.delegate = object;
        Logger logger = connectionContext.getLogger();
        object = new StringBuilder();
        ((StringBuilder)object).append("conn_");
        ((StringBuilder)object).append(l);
        this.logger = new LogWrapper(logger, "Connection", ((StringBuilder)object).toString());
        this.state = State.REALTIME_CONNECTING;
        this.conn = new WebsocketConnection(connectionContext, hostInfo, string2, string4, this, string3);
    }

    private void onConnectionReady(long l, String string2) {
        if (this.logger.logsDebug()) {
            this.logger.debug("realtime connection established", new Object[0]);
        }
        this.state = State.REALTIME_CONNECTED;
        this.delegate.onReady(l, string2);
    }

    private void onConnectionShutdown(String string2) {
        if (this.logger.logsDebug()) {
            this.logger.debug("Connection shutdown command received. Shutting down...", new Object[0]);
        }
        this.delegate.onKill(string2);
        this.close();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void onControlMessage(Map<String, Object> object) {
        Object object2;
        Object object3;
        if (this.logger.logsDebug()) {
            object3 = this.logger;
            object2 = new StringBuilder();
            ((StringBuilder)object2).append("Got control message: ");
            ((StringBuilder)object2).append(object.toString());
            ((LogWrapper)object3).debug(((StringBuilder)object2).toString(), new Object[0]);
        }
        try {
            object3 = (String)object.get("t");
            if (object3 != null) {
                boolean bl = ((String)object3).equals("s");
                if (bl) {
                    this.onConnectionShutdown((String)object.get("d"));
                    return;
                }
                if (((String)object3).equals(SERVER_CONTROL_MESSAGE_RESET)) {
                    this.onReset((String)object.get("d"));
                    return;
                }
                if (((String)object3).equals("h")) {
                    this.onHandshake((Map)object.get("d"));
                    return;
                }
                if (!this.logger.logsDebug()) return;
                object2 = this.logger;
                object = new StringBuilder();
                ((StringBuilder)object).append("Ignoring unknown control message: ");
                ((StringBuilder)object).append((String)object3);
                ((LogWrapper)object2).debug(((StringBuilder)object).toString(), new Object[0]);
                return;
            }
            if (this.logger.logsDebug()) {
                object2 = this.logger;
                object3 = new StringBuilder();
                ((StringBuilder)object3).append("Got invalid control message: ");
                ((StringBuilder)object3).append(object.toString());
                ((LogWrapper)object2).debug(((StringBuilder)object3).toString(), new Object[0]);
            }
            this.close();
            return;
        }
        catch (ClassCastException classCastException) {
            if (this.logger.logsDebug()) {
                object = this.logger;
                object3 = new StringBuilder();
                ((StringBuilder)object3).append("Failed to parse control message: ");
                ((StringBuilder)object3).append(classCastException.toString());
                ((LogWrapper)object).debug(((StringBuilder)object3).toString(), new Object[0]);
            }
            this.close();
        }
    }

    private void onDataMessage(Map<String, Object> map) {
        if (this.logger.logsDebug()) {
            LogWrapper logWrapper = this.logger;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("received data message: ");
            stringBuilder.append(map.toString());
            logWrapper.debug(stringBuilder.toString(), new Object[0]);
        }
        this.delegate.onDataMessage(map);
    }

    private void onHandshake(Map<String, Object> object) {
        long l = (Long)object.get(SERVER_HELLO_TIMESTAMP);
        String string2 = (String)object.get("h");
        this.delegate.onCacheHost(string2);
        object = (String)object.get("s");
        if (this.state == State.REALTIME_CONNECTING) {
            this.conn.start();
            this.onConnectionReady(l, (String)object);
        }
    }

    private void onReset(String string2) {
        if (this.logger.logsDebug()) {
            LogWrapper logWrapper = this.logger;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Got a reset; killing connection to ");
            stringBuilder.append(this.hostInfo.getHost());
            stringBuilder.append("; Updating internalHost to ");
            stringBuilder.append(string2);
            logWrapper.debug(stringBuilder.toString(), new Object[0]);
        }
        this.delegate.onCacheHost(string2);
        this.close(DisconnectReason.SERVER_RESET);
    }

    private void sendData(Map<String, Object> map, boolean bl) {
        if (this.state != State.REALTIME_CONNECTED) {
            this.logger.debug("Tried to send on an unconnected connection", new Object[0]);
        } else {
            if (bl) {
                this.logger.debug("Sending data (contents hidden)", new Object[0]);
            } else {
                this.logger.debug("Sending data: %s", map);
            }
            this.conn.send(map);
        }
    }

    public void close() {
        this.close(DisconnectReason.OTHER);
    }

    public void close(DisconnectReason disconnectReason) {
        if (this.state != State.REALTIME_DISCONNECTED) {
            if (this.logger.logsDebug()) {
                this.logger.debug("closing realtime connection", new Object[0]);
            }
            this.state = State.REALTIME_DISCONNECTED;
            WebsocketConnection websocketConnection = this.conn;
            if (websocketConnection != null) {
                websocketConnection.close();
                this.conn = null;
            }
            this.delegate.onDisconnect(disconnectReason);
        }
    }

    public void injectConnectionFailure() {
        this.close();
    }

    @Override
    public void onDisconnect(boolean bl) {
        this.conn = null;
        if (!bl && this.state == State.REALTIME_CONNECTING) {
            if (this.logger.logsDebug()) {
                this.logger.debug("Realtime connection failed", new Object[0]);
            }
        } else if (this.logger.logsDebug()) {
            this.logger.debug("Realtime connection lost", new Object[0]);
        }
        this.close();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void onMessage(Map<String, Object> object) {
        try {
            Object object2 = (String)object.get("t");
            if (object2 != null) {
                if (((String)object2).equals("d")) {
                    this.onDataMessage((Map)object.get("d"));
                    return;
                }
                if (((String)object2).equals(SERVER_CONTROL_MESSAGE)) {
                    this.onControlMessage((Map)object.get("d"));
                    return;
                }
                if (!this.logger.logsDebug()) return;
                LogWrapper logWrapper = this.logger;
                object = new StringBuilder();
                ((StringBuilder)object).append("Ignoring unknown server message type: ");
                ((StringBuilder)object).append((String)object2);
                logWrapper.debug(((StringBuilder)object).toString(), new Object[0]);
                return;
            }
            if (this.logger.logsDebug()) {
                object2 = this.logger;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to parse server message: missing message type:");
                stringBuilder.append(object.toString());
                ((LogWrapper)object2).debug(stringBuilder.toString(), new Object[0]);
            }
            this.close();
            return;
        }
        catch (ClassCastException classCastException) {
            if (this.logger.logsDebug()) {
                LogWrapper logWrapper = this.logger;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to parse server message: ");
                stringBuilder.append(classCastException.toString());
                logWrapper.debug(stringBuilder.toString(), new Object[0]);
            }
            this.close();
        }
    }

    public void open() {
        if (this.logger.logsDebug()) {
            this.logger.debug("Opening a connection", new Object[0]);
        }
        this.conn.open();
    }

    public void sendRequest(Map<String, Object> map, boolean bl) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("t", "d");
        hashMap.put("d", map);
        this.sendData(hashMap, bl);
    }

    public static interface Delegate {
        public void onCacheHost(String var1);

        public void onDataMessage(Map<String, Object> var1);

        public void onDisconnect(DisconnectReason var1);

        public void onKill(String var1);

        public void onReady(long var1, String var3);
    }

    public static final class DisconnectReason
    extends Enum<DisconnectReason> {
        private static final DisconnectReason[] $VALUES;
        public static final /* enum */ DisconnectReason OTHER;
        public static final /* enum */ DisconnectReason SERVER_RESET;

        static {
            DisconnectReason disconnectReason;
            DisconnectReason disconnectReason2;
            SERVER_RESET = disconnectReason2 = new DisconnectReason();
            OTHER = disconnectReason = new DisconnectReason();
            $VALUES = new DisconnectReason[]{disconnectReason2, disconnectReason};
        }

        public static DisconnectReason valueOf(String string2) {
            return Enum.valueOf(DisconnectReason.class, string2);
        }

        public static DisconnectReason[] values() {
            return (DisconnectReason[])$VALUES.clone();
        }
    }

    private static final class State
    extends Enum<State> {
        private static final State[] $VALUES;
        public static final /* enum */ State REALTIME_CONNECTED;
        public static final /* enum */ State REALTIME_CONNECTING;
        public static final /* enum */ State REALTIME_DISCONNECTED;

        static {
            State state;
            State state2;
            State state3;
            REALTIME_CONNECTING = state3 = new State();
            REALTIME_CONNECTED = state2 = new State();
            REALTIME_DISCONNECTED = state = new State();
            $VALUES = new State[]{state3, state2, state};
        }

        public static State valueOf(String string2) {
            return Enum.valueOf(State.class, string2);
        }

        public static State[] values() {
            return (State[])$VALUES.clone();
        }
    }
}

