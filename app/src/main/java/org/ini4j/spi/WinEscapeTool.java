/*
 * Decompiled with CFR 0.152.
 */
package org.ini4j.spi;

import org.ini4j.spi.EscapeTool;

public class WinEscapeTool
extends EscapeTool {
    private static final int ANSI_HEX_DIGITS = 2;
    private static final int ANSI_OCTAL_DIGITS = 3;
    private static final WinEscapeTool INSTANCE = new WinEscapeTool();
    private static final int OCTAL_RADIX = 8;

    public static WinEscapeTool getInstance() {
        return INSTANCE;
    }

    @Override
    void escapeBinary(StringBuilder stringBuilder, char c) {
        stringBuilder.append("\\x");
        stringBuilder.append(HEX[c >>> 4 & 0xF]);
        stringBuilder.append(HEX[c & 0xF]);
    }

    @Override
    int unescapeBinary(StringBuilder stringBuilder, char c, String string2, int n) {
        int n2 = n;
        if (c == 'x') {
            try {
                stringBuilder.append((char)Integer.parseInt(string2.substring(n, n + 2), 16));
                n2 = n + 2;
            }
            catch (Exception exception) {
                throw new IllegalArgumentException("Malformed \\xHH encoding.", exception);
            }
        }
        if (c == 'o') {
            try {
                stringBuilder.append((char)Integer.parseInt(string2.substring(n, n + 3), 8));
                n2 = n + 3;
            }
            catch (Exception exception) {
                throw new IllegalArgumentException("Malformed \\oOO encoding.", exception);
            }
        }
        return n2;
    }
}

