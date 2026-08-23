/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.tubesock;

import com.google.firebase.database.tubesock.WebSocketException;
import com.google.firebase.database.tubesock.WebSocketMessage;

public interface WebSocketEventHandler {
    public void onClose();

    public void onError(WebSocketException var1);

    public void onLogMessage(String var1);

    public void onMessage(WebSocketMessage var1);

    public void onOpen();
}

