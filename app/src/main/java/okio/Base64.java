/*
 * Decompiled with CFR 0.152.
 */
package okio;

import java.io.UnsupportedEncodingException;

final class Base64 {
    private static final byte[] MAP = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};
    private static final byte[] URL_MAP = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 45, 95};

    private Base64() {
    }

    public static byte[] decode(String object) {
        int n;
        int n2;
        int n3;
        for (n3 = ((String)object).length(); n3 > 0 && ((n2 = ((String)object).charAt(n3 - 1)) == 61 || n2 == 10 || n2 == 13 || n2 == 32 || n2 == 9); --n3) {
        }
        byte[] byArray = new byte[(int)((long)n3 * 6L / 8L)];
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        for (n = 0; n < n3; ++n) {
            int n7;
            int n8;
            block14: {
                block9: {
                    block12: {
                        block13: {
                            block15: {
                                char c;
                                block11: {
                                    block10: {
                                        block8: {
                                            c = ((String)object).charAt(n);
                                            if (c < 'A' || c > 'Z') break block8;
                                            n2 = c - 65;
                                            break block9;
                                        }
                                        if (c < 'a' || c > 'z') break block10;
                                        n2 = c - 71;
                                        break block9;
                                    }
                                    if (c < '0' || c > '9') break block11;
                                    n2 = c + 4;
                                    break block9;
                                }
                                if (c == '+' || c == '-') break block12;
                                if (c == '/' || c == '_') break block13;
                                n8 = n4;
                                n7 = n5;
                                n2 = n6;
                                if (c == '\n') break block14;
                                n8 = n4;
                                n7 = n5;
                                n2 = n6;
                                if (c == '\r') break block14;
                                n8 = n4;
                                n7 = n5;
                                n2 = n6;
                                if (c == ' ') break block14;
                                if (c != '\t') break block15;
                                n8 = n4;
                                n7 = n5;
                                n2 = n6;
                                break block14;
                            }
                            return null;
                        }
                        n2 = 63;
                        break block9;
                    }
                    n2 = 62;
                }
                n6 = n6 << 6 | (byte)n2;
                n8 = n4;
                n7 = ++n5;
                n2 = n6;
                if (n5 % 4 == 0) {
                    n2 = n4 + 1;
                    byArray[n4] = (byte)(n6 >> 16);
                    n4 = n2 + 1;
                    byArray[n2] = (byte)(n6 >> 8);
                    byArray[n4] = (byte)n6;
                    n8 = n4 + 1;
                    n2 = n6;
                    n7 = n5;
                }
            }
            n4 = n8;
            n5 = n7;
            n6 = n2;
        }
        n3 = n5 % 4;
        if (n3 == 1) {
            return null;
        }
        if (n3 == 2) {
            byArray[n4] = (byte)(n6 << 12 >> 16);
            n2 = n4 + 1;
        } else {
            n2 = n4;
            if (n3 == 3) {
                n3 = n6 << 6;
                n = n4 + 1;
                byArray[n4] = (byte)(n3 >> 16);
                n2 = n + 1;
                byArray[n] = (byte)(n3 >> 8);
            }
        }
        if (n2 == byArray.length) {
            return byArray;
        }
        object = new byte[n2];
        System.arraycopy(byArray, 0, object, 0, n2);
        return object;
    }

    public static String encode(byte[] byArray) {
        return Base64.encode(byArray, MAP);
    }

    private static String encode(byte[] object, byte[] byArray) {
        int n;
        byte[] byArray2 = new byte[(((byte[])object).length + 2) * 4 / 3];
        int n2 = 0;
        int n3 = ((byte[])object).length - ((byte[])object).length % 3;
        for (n = 0; n < n3; n += 3) {
            int n4 = n2 + 1;
            byArray2[n2] = byArray[(object[n] & 0xFF) >> 2];
            n2 = n4 + 1;
            byArray2[n4] = byArray[(object[n] & 3) << 4 | (object[n + 1] & 0xFF) >> 4];
            n4 = n2 + 1;
            byArray2[n2] = byArray[(object[n + 1] & 0xF) << 2 | (object[n + 2] & 0xFF) >> 6];
            n2 = n4 + 1;
            byArray2[n4] = byArray[object[n + 2] & 0x3F];
        }
        switch (((byte[])object).length % 3) {
            default: {
                break;
            }
            case 2: {
                n = n2 + 1;
                byArray2[n2] = byArray[(object[n3] & 0xFF) >> 2];
                n2 = n + 1;
                byArray2[n] = byArray[(object[n3] & 3) << 4 | (object[n3 + 1] & 0xFF) >> 4];
                n = n2 + 1;
                byArray2[n2] = byArray[(object[n3 + 1] & 0xF) << 2];
                n2 = n + 1;
                byArray2[n] = 61;
                break;
            }
            case 1: {
                n = n2 + 1;
                byArray2[n2] = byArray[(object[n3] & 0xFF) >> 2];
                n2 = n + 1;
                byArray2[n] = byArray[(object[n3] & 3) << 4];
                n = n2 + 1;
                byArray2[n2] = 61;
                n2 = n + 1;
                byArray2[n] = 61;
            }
        }
        try {
            object = new String(byArray2, 0, n2, "US-ASCII");
            return object;
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            AssertionError assertionError = new AssertionError((Object)unsupportedEncodingException);
            throw assertionError;
        }
    }

    public static String encodeUrl(byte[] byArray) {
        return Base64.encode(byArray, URL_MAP);
    }
}

