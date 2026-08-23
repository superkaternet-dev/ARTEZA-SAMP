/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.http;

public final class HeaderParser {
    private HeaderParser() {
    }

    public static int parseSeconds(String string2, int n) {
        long l;
        block3: {
            try {
                l = Long.parseLong(string2);
                if (l > Integer.MAX_VALUE) {
                    return Integer.MAX_VALUE;
                }
                if (l >= 0L) break block3;
                return 0;
            }
            catch (NumberFormatException numberFormatException) {
                return n;
            }
        }
        return (int)l;
    }

    public static int skipUntil(String string2, int n, String string3) {
        while (n < string2.length() && string3.indexOf(string2.charAt(n)) == -1) {
            ++n;
        }
        return n;
    }

    public static int skipWhitespace(String string2, int n) {
        char c;
        while (n < string2.length() && ((c = string2.charAt(n)) == ' ' || c == '\t')) {
            ++n;
        }
        return n;
    }
}

