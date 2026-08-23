/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.connection;

import java.net.URI;

public class HostInfo {
    private static final String LAST_SESSION_ID_PARAM = "ls";
    private static final String VERSION_PARAM = "v";
    private final String host;
    private final String namespace;
    private final boolean secure;

    public HostInfo(String string2, String string3, boolean bl) {
        this.host = string2;
        this.namespace = string3;
        this.secure = bl;
    }

    public static URI getConnectionUrl(String charSequence, boolean bl, String string2, String string3) {
        String string4 = bl ? "wss" : "ws";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(string4);
        stringBuilder.append("://");
        stringBuilder.append((String)charSequence);
        stringBuilder.append("/.ws?ns=");
        stringBuilder.append(string2);
        stringBuilder.append("&");
        stringBuilder.append(VERSION_PARAM);
        stringBuilder.append("=");
        stringBuilder.append("5");
        string2 = stringBuilder.toString();
        charSequence = string2;
        if (string3 != null) {
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append(string2);
            ((StringBuilder)charSequence).append("&ls=");
            ((StringBuilder)charSequence).append(string3);
            charSequence = ((StringBuilder)charSequence).toString();
        }
        return URI.create((String)charSequence);
    }

    public String getHost() {
        return this.host;
    }

    public String getNamespace() {
        return this.namespace;
    }

    public boolean isSecure() {
        return this.secure;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("http");
        String string2 = this.secure ? "s" : "";
        stringBuilder.append(string2);
        stringBuilder.append("://");
        stringBuilder.append(this.host);
        return stringBuilder.toString();
    }
}

