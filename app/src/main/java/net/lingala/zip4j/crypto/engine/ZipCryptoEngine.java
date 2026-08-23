/*
 * Decompiled with CFR 0.152.
 */
package net.lingala.zip4j.crypto.engine;

public class ZipCryptoEngine {
    private static final int[] CRC_TABLE = new int[256];
    private final int[] keys = new int[3];

    static {
        for (int i = 0; i < 256; ++i) {
            int n = i;
            for (int j = 0; j < 8; ++j) {
                if ((n & 1) == 1) {
                    n = n >>> 1 ^ 0xEDB88320;
                    continue;
                }
                n >>>= 1;
            }
            ZipCryptoEngine.CRC_TABLE[i] = n;
        }
    }

    private int crc32(int n, byte by) {
        return n >>> 8 ^ CRC_TABLE[(n ^ by) & 0xFF];
    }

    public byte decryptByte() {
        int n = this.keys[2] | 2;
        return (byte)((n ^ 1) * n >>> 8);
    }

    public void initKeys(char[] cArray) {
        int[] nArray = this.keys;
        nArray[0] = 305419896;
        nArray[1] = 591751049;
        nArray[2] = 878082192;
        for (int i = 0; i < cArray.length; ++i) {
            this.updateKeys((byte)(cArray[i] & 0xFF));
        }
    }

    public void updateKeys(byte by) {
        int[] nArray = this.keys;
        nArray[0] = this.crc32(nArray[0], by);
        nArray = this.keys;
        nArray[1] = nArray[1] + (nArray[0] & 0xFF);
        nArray[1] = nArray[1] * 134775813 + 1;
        nArray[2] = this.crc32(nArray[2], (byte)(nArray[1] >> 24));
    }
}

