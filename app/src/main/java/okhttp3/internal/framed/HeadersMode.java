/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.framed;

public final class HeadersMode
extends Enum<HeadersMode> {
    private static final HeadersMode[] $VALUES;
    public static final /* enum */ HeadersMode HTTP_20_HEADERS;
    public static final /* enum */ HeadersMode SPDY_HEADERS;
    public static final /* enum */ HeadersMode SPDY_REPLY;
    public static final /* enum */ HeadersMode SPDY_SYN_STREAM;

    static {
        HeadersMode headersMode;
        HeadersMode headersMode2;
        HeadersMode headersMode3;
        HeadersMode headersMode4;
        SPDY_SYN_STREAM = headersMode4 = new HeadersMode();
        SPDY_REPLY = headersMode3 = new HeadersMode();
        SPDY_HEADERS = headersMode2 = new HeadersMode();
        HTTP_20_HEADERS = headersMode = new HeadersMode();
        $VALUES = new HeadersMode[]{headersMode4, headersMode3, headersMode2, headersMode};
    }

    public static HeadersMode valueOf(String string2) {
        return Enum.valueOf(HeadersMode.class, string2);
    }

    public static HeadersMode[] values() {
        return (HeadersMode[])$VALUES.clone();
    }

    public boolean failIfHeadersAbsent() {
        boolean bl = this == SPDY_HEADERS;
        return bl;
    }

    public boolean failIfHeadersPresent() {
        boolean bl = this == SPDY_REPLY;
        return bl;
    }

    public boolean failIfStreamAbsent() {
        boolean bl = this == SPDY_REPLY || this == SPDY_HEADERS;
        return bl;
    }

    public boolean failIfStreamPresent() {
        boolean bl = this == SPDY_SYN_STREAM;
        return bl;
    }
}

