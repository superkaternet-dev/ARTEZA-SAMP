/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.util;

public class MurmurHash3 {
    private MurmurHash3() {
    }

    public static int murmurhash3_x86_32(byte[] byArray, int n, int n2, int n3) {
        block6: {
            int n4;
            int n5 = (n2 & 0xFFFFFFFC) + n;
            while (n < n5) {
                n4 = (byArray[n] & 0xFF | (byArray[n + 1] & 0xFF) << 8 | (byArray[n + 2] & 0xFF) << 16 | byArray[n + 3] << 24) * -862048943;
                n3 ^= (n4 << 15 | n4 >>> 17) * 461845907;
                n3 = (n3 >>> 19 | n3 << 13) * 5 - 430675100;
                n += 4;
            }
            n4 = 0;
            n = 0;
            switch (n2 & 3) {
                default: {
                    break block6;
                }
                case 3: {
                    n = (byArray[n5 + 2] & 0xFF) << 16;
                }
                case 2: {
                    n |= (byArray[n5 + 1] & 0xFF) << 8;
                    break;
                }
                case 1: {
                    n = n4;
                }
            }
            n = (byArray[n5] & 0xFF | n) * -862048943;
            n3 ^= (n >>> 17 | n << 15) * 461845907;
        }
        n = n3 ^ n2;
        n = (n ^ n >>> 16) * -2048144789;
        n = (n ^ n >>> 13) * -1028477387;
        return n ^ n >>> 16;
    }
}

