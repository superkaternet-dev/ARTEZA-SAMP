/*
 * Decompiled with CFR 0.152.
 */
package org.ini4j.spi;

import org.ini4j.spi.ServiceFinder;

public class EscapeTool {
    private static final char ASCII_MAX = '~';
    private static final char ASCII_MIN = ' ';
    static final char DOUBLE_QUOTE = '\"';
    private static final String ESCAPEABLE_CHARS = "\\\t\n\f\b\r:=";
    private static final char ESCAPE_CHAR = '\\';
    private static final String ESCAPE_LETTERS = "\\tnfbr:=";
    static final char[] HEX = "0123456789abcdef".toCharArray();
    static final int HEX_DIGIT_1_OFFSET = 12;
    static final int HEX_DIGIT_2_OFFSET = 8;
    static final int HEX_DIGIT_3_OFFSET = 4;
    static final int HEX_DIGIT_MASK = 15;
    static final int HEX_RADIX = 16;
    private static final EscapeTool INSTANCE = ServiceFinder.findService(EscapeTool.class);
    private static final int UNICODE_HEX_DIGITS = 4;

    public static EscapeTool getInstance() {
        return INSTANCE;
    }

    public String escape(String string2) {
        int n = string2.length();
        StringBuilder stringBuilder = new StringBuilder(n * 2);
        for (int i = 0; i < n; ++i) {
            char c = string2.charAt(i);
            int n2 = ESCAPEABLE_CHARS.indexOf(c);
            if (n2 >= 0) {
                stringBuilder.append('\\');
                stringBuilder.append(ESCAPE_LETTERS.charAt(n2));
                continue;
            }
            if (c >= ' ' && c <= '~') {
                stringBuilder.append(c);
                continue;
            }
            this.escapeBinary(stringBuilder, c);
        }
        return stringBuilder.toString();
    }

    void escapeBinary(StringBuilder stringBuilder, char c) {
        stringBuilder.append("\\u");
        char[] cArray = HEX;
        stringBuilder.append(cArray[c >>> 12 & 0xF]);
        stringBuilder.append(cArray[c >>> 8 & 0xF]);
        stringBuilder.append(cArray[c >>> 4 & 0xF]);
        stringBuilder.append(cArray[c & 0xF]);
    }

    public String quote(String string2) {
        String string3 = string2;
        CharSequence charSequence = string3;
        if (string2 != null) {
            charSequence = string3;
            if (string2.length() != 0) {
                charSequence = new StringBuilder();
                ((StringBuilder)charSequence).append('\"');
                for (int i = 0; i < string2.length(); ++i) {
                    char c = string2.charAt(i);
                    if (c == '\\' || c == '\"') {
                        ((StringBuilder)charSequence).append('\\');
                    }
                    ((StringBuilder)charSequence).append(c);
                }
                ((StringBuilder)charSequence).append('\"');
                charSequence = ((StringBuilder)charSequence).toString();
            }
        }
        return charSequence;
    }

    public String unescape(String string2) {
        int n = string2.length();
        StringBuilder stringBuilder = new StringBuilder(n);
        int n2 = 0;
        while (n2 < n) {
            int n3 = n2 + 1;
            char c = string2.charAt(n2);
            if (c == '\\') {
                n2 = n3 + 1;
                c = string2.charAt(n3);
                if ((n3 = this.unescapeBinary(stringBuilder, c, string2, n2)) == n2) {
                    n3 = ESCAPE_LETTERS.indexOf(c);
                    if (n3 >= 0) {
                        c = ESCAPEABLE_CHARS.charAt(n3);
                    }
                    stringBuilder.append(c);
                    continue;
                }
                n2 = n3;
                continue;
            }
            stringBuilder.append(c);
            n2 = n3;
        }
        return stringBuilder.toString();
    }

    int unescapeBinary(StringBuilder stringBuilder, char c, String string2, int n) {
        int n2 = n;
        if (c == 'u') {
            try {
                stringBuilder.append((char)Integer.parseInt(string2.substring(n, n + 4), 16));
                n2 = n + 4;
            }
            catch (Exception exception) {
                throw new IllegalArgumentException("Malformed \\uxxxx encoding.", exception);
            }
        }
        return n2;
    }

    public String unquote(String string2) {
        StringBuilder stringBuilder = new StringBuilder();
        boolean bl = false;
        for (int i = 1; i < string2.length() - 1; ++i) {
            char c = string2.charAt(i);
            boolean bl2 = bl;
            if (c == '\\') {
                if (!bl) {
                    bl = true;
                    continue;
                }
                bl2 = false;
            }
            stringBuilder.append(c);
            bl = bl2;
        }
        return stringBuilder.toString();
    }
}

