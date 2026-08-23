/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.tubesock;

public class WebSocketMessage {
    private byte[] byteMessage;
    private byte opcode;
    private String stringMessage;

    public WebSocketMessage(String string2) {
        this.stringMessage = string2;
        this.opcode = 1;
    }

    public WebSocketMessage(byte[] byArray) {
        this.byteMessage = byArray;
        this.opcode = (byte)2;
    }

    public byte[] getBytes() {
        return this.byteMessage;
    }

    public String getText() {
        return this.stringMessage;
    }

    public boolean isBinary() {
        boolean bl = this.opcode == 2;
        return bl;
    }

    public boolean isText() {
        byte by = this.opcode;
        boolean bl = true;
        if (by != 1) {
            bl = false;
        }
        return bl;
    }
}

