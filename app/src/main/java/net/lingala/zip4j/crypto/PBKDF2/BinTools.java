/*
 * Decompiled with CFR 0.152.
 */
package net.lingala.zip4j.crypto.PBKDF2;

class BinTools {
    public static final String hex = "0123456789ABCDEF";

    BinTools() {
    }

    public static String bin2hex(byte[] byArray) {
        if (byArray == null) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer(byArray.length * 2);
        for (int i = 0; i < byArray.length; ++i) {
            int n = (byArray[i] + 256) % 256;
            stringBuffer.append(hex.charAt(n / 16 & 0xF));
            stringBuffer.append(hex.charAt(n % 16 & 0xF));
        }
        return stringBuffer.toString();
    }

    public static int hex2bin(char c) {
        if (c >= '0' && c <= '9') {
            return c - 48;
        }
        if (c >= 'A' && c <= 'F') {
            return c - 65 + 10;
        }
        if (c >= 'a' && c <= 'f') {
            return c - 97 + 10;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Input string may only contain hex digits, but found '");
        stringBuilder.append(c);
        stringBuilder.append("'");
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public static byte[] hex2bin(String object) {
        CharSequence charSequence = object;
        if (object == null) {
            charSequence = "";
        } else if (((String)object).length() % 2 != 0) {
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append("0");
            ((StringBuilder)charSequence).append((String)object);
            charSequence = ((StringBuilder)charSequence).toString();
        }
        object = new byte[((String)charSequence).length() / 2];
        int n = 0;
        int n2 = 0;
        while (n < ((String)charSequence).length()) {
            int n3 = n + 1;
            char c = ((String)charSequence).charAt(n);
            char c2 = ((String)charSequence).charAt(n3);
            object[n2] = (byte)(BinTools.hex2bin(c) * 16 + BinTools.hex2bin(c2));
            ++n2;
            n = n3 + 1;
        }
        return object;
    }
}

