/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.tubesock;

import com.google.firebase.database.tubesock.MessageBuilderFactory;
import com.google.firebase.database.tubesock.WebSocket;
import com.google.firebase.database.tubesock.WebSocketEventHandler;
import com.google.firebase.database.tubesock.WebSocketException;
import com.google.firebase.database.tubesock.WebSocketMessage;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;

class WebSocketReceiver {
    private WebSocketEventHandler eventHandler = null;
    private DataInputStream input = null;
    private byte[] inputHeader = new byte[112];
    private MessageBuilderFactory.Builder pendingBuilder;
    private volatile boolean stop = false;
    private WebSocket websocket = null;

    WebSocketReceiver(WebSocket webSocket) {
        this.websocket = webSocket;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private void appendBytes(boolean bl, byte by, byte[] object) {
        if (by == 9) {
            if (!bl) throw new WebSocketException("PING must not fragment across frames");
            this.handlePing((byte[])object);
            return;
        } else {
            MessageBuilderFactory.Builder builder = this.pendingBuilder;
            if (builder != null && by != 0) {
                throw new WebSocketException("Failed to continue outstanding frame");
            }
            if (builder == null && by == 0) {
                throw new WebSocketException("Received continuing frame, but there's nothing to continue");
            }
            if (builder == null) {
                this.pendingBuilder = MessageBuilderFactory.builder(by);
            }
            if (!this.pendingBuilder.appendBytes((byte[])object)) throw new WebSocketException("Failed to decode frame");
            if (!bl) return;
            WebSocketMessage webSocketMessage = this.pendingBuilder.toMessage();
            this.pendingBuilder = null;
            if (webSocketMessage == null) throw new WebSocketException("Failed to decode whole message");
            this.eventHandler.onMessage(webSocketMessage);
        }
    }

    private void handleError(WebSocketException webSocketException) {
        this.stopit();
        this.websocket.handleReceiverError(webSocketException);
    }

    private void handlePing(byte[] byArray) {
        if (byArray.length <= 125) {
            this.websocket.pong(byArray);
            return;
        }
        throw new WebSocketException("PING frame too long");
    }

    private long parseLong(byte[] byArray, int n) {
        return ((long)byArray[n + 0] << 56) + ((long)(byArray[n + 1] & 0xFF) << 48) + ((long)(byArray[n + 2] & 0xFF) << 40) + ((long)(byArray[n + 3] & 0xFF) << 32) + ((long)(byArray[n + 4] & 0xFF) << 24) + (long)((byArray[n + 5] & 0xFF) << 16) + (long)((byArray[n + 6] & 0xFF) << 8) + (long)((byArray[n + 7] & 0xFF) << 0);
    }

    private int read(byte[] byArray, int n, int n2) throws IOException {
        this.input.readFully(byArray, n, n2);
        return n2;
    }

    boolean isRunning() {
        return this.stop ^ true;
    }

    /*
     * Unable to fully structure code
     */
    void run() {
        this.eventHandler = this.websocket.getEventHandler();
        while (!this.stop) {
            block10: {
                block12: {
                    block11: {
                        var3_3 = 0 + this.read(this.inputHeader, 0, 1);
                        var7_6 = this.inputHeader;
                        var4_4 = (var7_6[0] & 128) != 0;
                        var2_2 = (var7_6[0] & 112) != 0 ? 1 : 0;
                        if (var2_2 != 0) ** GOTO lbl52
                        var1_1 = (byte)(var7_6[0] & 15);
                        var2_2 = var3_3 + this.read((byte[])var7_6, var3_3, 1);
                        var7_6 = this.inputHeader;
                        var3_3 = var7_6[1];
                        var5_5 = 0L;
                        if (var3_3 >= 126) break block11;
                        var5_5 = var3_3;
                        ** GOTO lbl31
                    }
                    if (var3_3 != 126) break block12;
                    this.read((byte[])var7_6, var2_2, 2);
                    var7_6 = this.inputHeader;
                    var5_5 = (long)(var7_6[2] & 255) << 8 | (long)(var7_6[3] & 255);
                    ** GOTO lbl31
                }
                if (var3_3 != 127) ** GOTO lbl31
                var3_3 = this.read((byte[])var7_6, var2_2, 8);
                var5_5 = this.parseLong(this.inputHeader, var2_2 + var3_3 - 8);
lbl31:
                // 4 sources

                var7_6 = new byte[(int)var5_5];
                this.read((byte[])var7_6, 0, (int)var5_5);
                if (var1_1 != 8) break block10;
                this.websocket.onCloseOpReceived();
            }
            if (var1_1 == 10) continue;
            if (var1_1 == 1 || var1_1 == 2 || var1_1 == 9 || var1_1 == 0) ** GOTO lbl50
            try {
                var7_6 = new StringBuilder;
                var7_6();
                var7_6.append("Unsupported opcode: ");
                var7_6.append(var1_1);
                var8_10 = new WebSocketException(var7_6.toString());
                throw var8_10;
lbl50:
                // 1 sources

                this.appendBytes(var4_4, var1_1, (byte[])var7_6);
                continue;
lbl52:
                // 1 sources

                var7_6 = new WebSocketException;
                var7_6("Invalid frame received");
                throw var7_6;
            }
            catch (WebSocketException var7_7) {
                this.handleError(var7_7);
            }
            catch (IOException var7_8) {
                this.handleError(new WebSocketException("IO Error", var7_8));
            }
            catch (SocketTimeoutException var7_9) {}
        }
    }

    void setInput(DataInputStream dataInputStream) {
        this.input = dataInputStream;
    }

    void stopit() {
        this.stop = true;
    }
}

