/*
 * Decompiled with CFR 0.152.
 */
package okhttp3;

import java.net.Socket;
import okhttp3.Handshake;
import okhttp3.Protocol;
import okhttp3.Route;

public interface Connection {
    public Handshake handshake();

    public Protocol protocol();

    public Route route();

    public Socket socket();
}

