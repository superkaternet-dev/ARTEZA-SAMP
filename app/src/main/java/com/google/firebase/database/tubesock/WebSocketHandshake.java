/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Base64
 */
package com.google.firebase.database.tubesock;

import android.util.Base64;
import com.google.firebase.database.tubesock.WebSocketException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

class WebSocketHandshake {
    private static final String WEBSOCKET_VERSION = "13";
    private Map<String, String> extraHeaders = null;
    private String nonce = null;
    private String protocol = null;
    private URI url = null;

    public WebSocketHandshake(URI uRI, String string2, Map<String, String> map) {
        this.url = uRI;
        this.protocol = string2;
        this.extraHeaders = map;
        this.nonce = this.createNonce();
    }

    private String createNonce() {
        byte[] byArray = new byte[16];
        for (int i = 0; i < 16; ++i) {
            byArray[i] = (byte)this.rand(0, 255);
        }
        return Base64.encodeToString((byte[])byArray, (int)2);
    }

    private String generateHeader(LinkedHashMap<String, String> linkedHashMap) {
        String string2 = new String();
        for (String string3 : linkedHashMap.keySet()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(string2);
            stringBuilder.append(string3);
            stringBuilder.append(": ");
            stringBuilder.append(linkedHashMap.get(string3));
            stringBuilder.append("\r\n");
            string2 = stringBuilder.toString();
        }
        return string2;
    }

    private int rand(int n, int n2) {
        double d = Math.random();
        double d2 = n2;
        Double.isNaN(d2);
        double d3 = n;
        Double.isNaN(d3);
        return (int)(d * d2 + d3);
    }

    public byte[] getHandshake() {
        CharSequence charSequence = this.url.getPath();
        Object object = this.url.getQuery();
        Object object2 = new StringBuilder();
        ((StringBuilder)object2).append((String)charSequence);
        if (object == null) {
            object = "";
        } else {
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append("?");
            ((StringBuilder)charSequence).append((String)object);
            object = ((StringBuilder)charSequence).toString();
        }
        ((StringBuilder)object2).append((String)object);
        charSequence = ((StringBuilder)object2).toString();
        object = object2 = this.url.getHost();
        if (this.url.getPort() != -1) {
            object = new StringBuilder();
            ((StringBuilder)object).append((String)object2);
            ((StringBuilder)object).append(":");
            ((StringBuilder)object).append(this.url.getPort());
            object = ((StringBuilder)object).toString();
        }
        object2 = new LinkedHashMap();
        ((HashMap)object2).put("Host", object);
        ((HashMap)object2).put("Upgrade", "websocket");
        ((HashMap)object2).put("Connection", "Upgrade");
        ((HashMap)object2).put("Sec-WebSocket-Version", WEBSOCKET_VERSION);
        ((HashMap)object2).put("Sec-WebSocket-Key", this.nonce);
        object = this.protocol;
        if (object != null) {
            ((HashMap)object2).put("Sec-WebSocket-Protocol", object);
        }
        if ((object = this.extraHeaders) != null) {
            for (String string2 : object.keySet()) {
                if (((HashMap)object2).containsKey(string2)) continue;
                ((HashMap)object2).put(string2, this.extraHeaders.get(string2));
            }
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("GET ");
        ((StringBuilder)object).append((String)charSequence);
        ((StringBuilder)object).append(" HTTP/1.1\r\n");
        object = ((StringBuilder)object).toString();
        charSequence = new StringBuilder();
        ((StringBuilder)charSequence).append((String)object);
        ((StringBuilder)charSequence).append(this.generateHeader((LinkedHashMap<String, String>)object2));
        object = ((StringBuilder)charSequence).toString();
        object2 = new StringBuilder();
        ((StringBuilder)object2).append((String)object);
        ((StringBuilder)object2).append("\r\n");
        object = ((StringBuilder)object2).toString().getBytes(Charset.defaultCharset());
        object2 = new byte[((Object)object).length];
        System.arraycopy(object, 0, object2, 0, ((Object)object).length);
        return object2;
    }

    public void verifyServerHandshakeHeaders(HashMap<String, String> hashMap) {
        if ("websocket".equals(hashMap.get("upgrade"))) {
            if ("upgrade".equals(hashMap.get("connection"))) {
                return;
            }
            throw new WebSocketException("connection failed: missing header field in server handshake: Connection");
        }
        throw new WebSocketException("connection failed: missing header field in server handshake: Upgrade");
    }

    public void verifyServerStatusLine(String charSequence) {
        int n = Integer.parseInt(((String)charSequence).substring(9, 12));
        if (n != 407) {
            if (n != 404) {
                if (n == 101) {
                    return;
                }
                charSequence = new StringBuilder();
                ((StringBuilder)charSequence).append("connection failed: unknown status code ");
                ((StringBuilder)charSequence).append(n);
                throw new WebSocketException(((StringBuilder)charSequence).toString());
            }
            throw new WebSocketException("connection failed: 404 not found");
        }
        throw new WebSocketException("connection failed: proxy authentication not supported");
    }
}

