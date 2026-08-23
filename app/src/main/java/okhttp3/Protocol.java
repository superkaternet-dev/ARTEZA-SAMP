/*
 * Decompiled with CFR 0.152.
 */
package okhttp3;

import java.io.IOException;

public final class Protocol
extends Enum<Protocol> {
    private static final Protocol[] $VALUES;
    public static final /* enum */ Protocol HTTP_1_0;
    public static final /* enum */ Protocol HTTP_1_1;
    public static final /* enum */ Protocol HTTP_2;
    public static final /* enum */ Protocol SPDY_3;
    private final String protocol;

    static {
        Protocol protocol;
        Protocol protocol2;
        Protocol protocol3;
        Protocol protocol4;
        HTTP_1_0 = protocol4 = new Protocol("http/1.0");
        HTTP_1_1 = protocol3 = new Protocol("http/1.1");
        SPDY_3 = protocol2 = new Protocol("spdy/3.1");
        HTTP_2 = protocol = new Protocol("h2");
        $VALUES = new Protocol[]{protocol4, protocol3, protocol2, protocol};
    }

    private Protocol(String string3) {
        this.protocol = string3;
    }

    public static Protocol get(String string2) throws IOException {
        Object object = HTTP_1_0;
        if (string2.equals(((Protocol)((Object)object)).protocol)) {
            return object;
        }
        object = HTTP_1_1;
        if (string2.equals(((Protocol)((Object)object)).protocol)) {
            return object;
        }
        object = HTTP_2;
        if (string2.equals(((Protocol)((Object)object)).protocol)) {
            return object;
        }
        object = SPDY_3;
        if (string2.equals(((Protocol)((Object)object)).protocol)) {
            return object;
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("Unexpected protocol: ");
        ((StringBuilder)object).append(string2);
        throw new IOException(((StringBuilder)object).toString());
    }

    public static Protocol valueOf(String string2) {
        return Enum.valueOf(Protocol.class, string2);
    }

    public static Protocol[] values() {
        return (Protocol[])$VALUES.clone();
    }

    public String toString() {
        return this.protocol;
    }
}

