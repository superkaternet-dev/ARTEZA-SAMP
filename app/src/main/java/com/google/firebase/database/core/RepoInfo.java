/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core;

import com.google.firebase.emulators.EmulatedServiceSettings;
import java.net.URI;

public final class RepoInfo {
    private static final String LAST_SESSION_ID_PARAM = "ls";
    private static final String VERSION_PARAM = "v";
    public String host;
    public String internalHost;
    public String namespace;
    public boolean secure;

    public void applyEmulatorSettings(EmulatedServiceSettings object) {
        if (object == null) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(((EmulatedServiceSettings)object).getHost());
        stringBuilder.append(":");
        stringBuilder.append(((EmulatedServiceSettings)object).getPort());
        this.host = object = stringBuilder.toString();
        this.internalHost = object;
        this.secure = false;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object != null && this.getClass() == object.getClass()) {
            object = (RepoInfo)object;
            if (this.secure != ((RepoInfo)object).secure) {
                return false;
            }
            if (!this.host.equals(((RepoInfo)object).host)) {
                return false;
            }
            return this.namespace.equals(((RepoInfo)object).namespace);
        }
        return false;
    }

    public URI getConnectionURL(String string2) {
        CharSequence charSequence = this.secure ? "wss" : "ws";
        CharSequence charSequence2 = new StringBuilder();
        charSequence2.append((String)charSequence);
        charSequence2.append("://");
        charSequence2.append(this.internalHost);
        charSequence2.append("/.ws?ns=");
        charSequence2.append(this.namespace);
        charSequence2.append("&");
        charSequence2.append(VERSION_PARAM);
        charSequence2.append("=");
        charSequence2.append("5");
        charSequence2 = charSequence2.toString();
        charSequence = charSequence2;
        if (string2 != null) {
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append((String)charSequence2);
            ((StringBuilder)charSequence).append("&ls=");
            ((StringBuilder)charSequence).append(string2);
            charSequence = ((StringBuilder)charSequence).toString();
        }
        return URI.create((String)charSequence);
    }

    public int hashCode() {
        return (this.host.hashCode() * 31 + this.secure) * 31 + this.namespace.hashCode();
    }

    public boolean isCacheableHost() {
        return this.internalHost.startsWith("s-");
    }

    public boolean isCustomHost() {
        boolean bl = !this.host.contains(".firebaseio.com") && !this.host.contains(".firebaseio-demo.com");
        return bl;
    }

    public boolean isDemoHost() {
        return this.host.contains(".firebaseio-demo.com");
    }

    public boolean isSecure() {
        return this.secure;
    }

    public String toDebugString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(host=");
        stringBuilder.append(this.host);
        stringBuilder.append(", secure=");
        stringBuilder.append(this.secure);
        stringBuilder.append(", ns=");
        stringBuilder.append(this.namespace);
        stringBuilder.append(" internal=");
        stringBuilder.append(this.internalHost);
        stringBuilder.append(")");
        return stringBuilder.toString();
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

