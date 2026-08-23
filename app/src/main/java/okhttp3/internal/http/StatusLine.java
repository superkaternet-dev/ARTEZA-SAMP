/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.http;

import java.io.IOException;
import java.net.ProtocolException;
import okhttp3.Protocol;
import okhttp3.Response;

public final class StatusLine {
    public static final int HTTP_CONTINUE = 100;
    public static final int HTTP_PERM_REDIRECT = 308;
    public static final int HTTP_TEMP_REDIRECT = 307;
    public final int code;
    public final String message;
    public final Protocol protocol;

    public StatusLine(Protocol protocol, int n, String string2) {
        this.protocol = protocol;
        this.code = n;
        this.message = string2;
    }

    public static StatusLine get(Response response) {
        return new StatusLine(response.protocol(), response.code(), response.message());
    }

    public static StatusLine parse(String string2) throws IOException {
        block14: {
            Object object;
            int n;
            int n2;
            block12: {
                block9: {
                    block10: {
                        block13: {
                            block11: {
                                if (!string2.startsWith("HTTP/1.")) break block9;
                                if (string2.length() < 9 || string2.charAt(8) != ' ') break block10;
                                n2 = string2.charAt(7) - 48;
                                n = 9;
                                if (n2 != 0) break block11;
                                object = Protocol.HTTP_1_0;
                                break block12;
                            }
                            if (n2 != 1) break block13;
                            object = Protocol.HTTP_1_1;
                            break block12;
                        }
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Unexpected status line: ");
                        stringBuilder.append(string2);
                        throw new ProtocolException(stringBuilder.toString());
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unexpected status line: ");
                    stringBuilder.append(string2);
                    throw new ProtocolException(stringBuilder.toString());
                }
                if (!string2.startsWith("ICY ")) break block14;
                object = Protocol.HTTP_1_0;
                n = 4;
            }
            if (string2.length() >= n + 3) {
                String string3;
                try {
                    n2 = Integer.parseInt(string2.substring(n, n + 3));
                    string3 = "";
                }
                catch (NumberFormatException numberFormatException) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unexpected status line: ");
                    stringBuilder.append(string2);
                    throw new ProtocolException(stringBuilder.toString());
                }
                if (string2.length() > n + 3) {
                    if (string2.charAt(n + 3) == ' ') {
                        string3 = string2.substring(n + 4);
                    } else {
                        object = new StringBuilder();
                        ((StringBuilder)object).append("Unexpected status line: ");
                        ((StringBuilder)object).append(string2);
                        throw new ProtocolException(((StringBuilder)object).toString());
                    }
                }
                return new StatusLine((Protocol)((Object)object), n2, string3);
            }
            object = new StringBuilder();
            ((StringBuilder)object).append("Unexpected status line: ");
            ((StringBuilder)object).append(string2);
            throw new ProtocolException(((StringBuilder)object).toString());
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unexpected status line: ");
        stringBuilder.append(string2);
        throw new ProtocolException(stringBuilder.toString());
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        String string2 = this.protocol == Protocol.HTTP_1_0 ? "HTTP/1.0" : "HTTP/1.1";
        stringBuilder.append(string2);
        stringBuilder.append(' ');
        stringBuilder.append(this.code);
        if (this.message != null) {
            stringBuilder.append(' ');
            stringBuilder.append(this.message);
        }
        return stringBuilder.toString();
    }
}

