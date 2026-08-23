/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.emulators;

public final class EmulatedServiceSettings {
    private final String host;
    private final int port;

    public EmulatedServiceSettings(String string2, int n) {
        this.host = string2;
        this.port = n;
    }

    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }
}

