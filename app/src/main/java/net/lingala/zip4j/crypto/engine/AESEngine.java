/*
 * Decompiled with CFR 0.152.
 */
package net.lingala.zip4j.crypto.engine;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import net.lingala.zip4j.exception.ZipException;

public class AESEngine {
    private static final byte[] S = new byte[]{99, 124, 119, 123, -14, 107, 111, -59, 48, 1, 103, 43, -2, -41, -85, 118, -54, -126, -55, 125, -6, 89, 71, -16, -83, -44, -94, -81, -100, -92, 114, -64, -73, -3, -109, 38, 54, 63, -9, -52, 52, -91, -27, -15, 113, -40, 49, 21, 4, -57, 35, -61, 24, -106, 5, -102, 7, 18, -128, -30, -21, 39, -78, 117, 9, -125, 44, 26, 27, 110, 90, -96, 82, 59, -42, -77, 41, -29, 47, -124, 83, -47, 0, -19, 32, -4, -79, 91, 106, -53, -66, 57, 74, 76, 88, -49, -48, -17, -86, -5, 67, 77, 51, -123, 69, -7, 2, 127, 80, 60, -97, -88, 81, -93, 64, -113, -110, -99, 56, -11, -68, -74, -38, 33, 16, -1, -13, -46, -51, 12, 19, -20, 95, -105, 68, 23, -60, -89, 126, 61, 100, 93, 25, 115, 96, -127, 79, -36, 34, 42, -112, -120, 70, -18, -72, 20, -34, 94, 11, -37, -32, 50, 58, 10, 73, 6, 36, 92, -62, -45, -84, 98, -111, -107, -28, 121, -25, -56, 55, 109, -115, -43, 78, -87, 108, 86, -12, -22, 101, 122, -82, 8, -70, 120, 37, 46, 28, -90, -76, -58, -24, -35, 116, 31, 75, -67, -117, -118, 112, 62, -75, 102, 72, 3, -10, 14, 97, 53, 87, -71, -122, -63, 29, -98, -31, -8, -104, 17, 105, -39, -114, -108, -101, 30, -121, -23, -50, 85, 40, -33, -116, -95, -119, 13, -65, -26, 66, 104, 65, -103, 45, 15, -80, 84, -69, 22};
    private static final int[] T0;
    private static final int[] rcon;
    private int C0;
    private int C1;
    private int C2;
    private int C3;
    private int rounds;
    private int[][] workingKey = null;

    static {
        rcon = new int[]{1, 2, 4, 8, 16, 32, 64, 128, 27, 54, 108, 216, 171, 77, 154, 47, 94, 188, 99, 198, 151, 53, 106, 212, 179, 125, 250, 239, 197, 145};
        T0 = AESEngine.$d2j$hex$da5a0ceb$decode_I("c66363a5f87c7c84ee777799f67b7b8dfff2f20dd66b6bbdde6f6fb191c5c5546030305002010103ce6767a9562b2b7de7fefe19b5d7d7624dababe6ec76769a8fcaca451f82829d89c9c940fa7d7d87effafa15b25959eb8e4747c9fbf0f00b41adadecb3d4d4675fa2a2fd45afafea239c9cbf53a4a4f7e47272969bc0c05b75b7b7c2e1fdfd1c3d9393ae4c26266a6c36365a7e3f3f41f5f7f70283cccc4f6834345c51a5a5f4d1e5e534f9f1f108e2717193abd8d873623131532a15153f0804040c95c7c752462323659dc3c35e30181828379696a10a05050f2f9a9ab50e070709241212361b80809bdfe2e23dcdebeb264e2727697fb2b2cdea75759f1209091b1d83839e582c2c74341a1a2e361b1b2ddc6e6eb2b45a5aee5ba0a0fba45252f6763b3b4db7d6d6617db3b3ce5229297bdde3e33e5e2f2f7113848497a65353f5b9d1d16800000000c1eded2c40202060e3fcfc1f79b1b1c8b65b5bedd46a6abe8dcbcb4667bebed97239394b944a4ade984c4cd4b05858e885cfcf4abbd0d06bc5efef2a4faaaae5edfbfb16864343c59a4d4dd766333355118585948a4545cfe9f9f91004020206fe7f7f81a05050f0783c3c44259f9fba4ba8a8e3a25151f35da3a3fe804040c0058f8f8a3f9292ad219d9dbc70383848f1f5f50463bcbcdf77b6b6c1afdada754221216320101030e5ffff1afdf3f30ebfd2d26d81cdcd4c180c0c1426131335c3ecec2fbe5f5fe1359797a2884444cc2e17173993c4c45755a7a7f2fc7e7e827a3d3d47c86464acba5d5de73219192be6737395c06060a0198181989e4f4fd1a3dcdc7f44222266542a2a7e3b9090ab0b8888838c4646cac7eeee296bb8b8d32814143ca7dede79bc5e5ee2160b0b1daddbdb76dbe0e03b64323256743a3a4e140a0a1e924949db0c06060a4824246cb85c5ce49fc2c25dbdd3d36e43acacefc46262a6399191a8319595a4d3e4e437f279798bd5e7e7328bc8c8436e373759da6d6db7018d8d8cb1d5d5649c4e4ed249a9a9e0d86c6cb4ac5656faf3f4f407cfeaea25ca6565aff47a7a8e47aeaee9100808186fbabad5f07878884a25256f5c2e2e72381c1c2457a6a6f173b4b4c797c6c651cbe8e823a1dddd7ce874749c3e1f1f21964b4bdd61bdbddc0d8b8b860f8a8a85e07070907c3e3e4271b5b5c4cc6666aa904848d806030305f7f6f6011c0e0e12c26161a36a35355fae5757f969b9b9d01786869199c1c1583a1d1d27279e9eb9d9e1e138ebf8f8132b9898b322111133d26969bba9d9d970078e8e89339494a72d9b9bb63c1e1e2215878792c9e9e92087cece49aa5555ff50282878a5dfdf7a038c8c8f59a1a1f8098989801a0d0d1765bfbfdad7e6e631844242c6d06868b8824141c3299999b05a2d2d771e0f0f117bb0b0cba85454fc6dbbbbd62c16163a");
    }

    public AESEngine(byte[] byArray) throws ZipException {
        this.init(byArray);
    }

    private final void encryptBlock(int[][] nArray) {
        int n;
        int n2;
        int n3;
        int n4;
        int n5;
        int n6;
        int n7;
        int n8;
        Object[] objectArray;
        this.C0 ^= nArray[0][0];
        this.C1 ^= nArray[0][1];
        this.C2 ^= nArray[0][2];
        this.C3 ^= nArray[0][3];
        int n9 = 1;
        while (n9 < this.rounds - 1) {
            objectArray = T0;
            n8 = objectArray[this.C0 & 0xFF] ^ this.shift(objectArray[this.C1 >> 8 & 0xFF], 24) ^ this.shift(objectArray[this.C2 >> 16 & 0xFF], 16) ^ this.shift(objectArray[this.C3 >> 24 & 0xFF], 8) ^ nArray[n9][0];
            n7 = objectArray[this.C1 & 0xFF] ^ this.shift(objectArray[this.C2 >> 8 & 0xFF], 24) ^ this.shift(objectArray[this.C3 >> 16 & 0xFF], 16) ^ this.shift(objectArray[this.C0 >> 24 & 0xFF], 8) ^ nArray[n9][1];
            n6 = objectArray[this.C2 & 0xFF] ^ this.shift(objectArray[this.C3 >> 8 & 0xFF], 24) ^ this.shift(objectArray[this.C0 >> 16 & 0xFF], 16) ^ this.shift(objectArray[this.C1 >> 24 & 0xFF], 8) ^ nArray[n9][2];
            n5 = objectArray[this.C3 & 0xFF];
            n4 = this.shift(objectArray[this.C0 >> 8 & 0xFF], 24);
            n3 = this.shift(objectArray[this.C1 >> 16 & 0xFF], 16);
            n2 = this.shift(objectArray[this.C2 >> 24 & 0xFF], 8);
            n = n9 + 1;
            n9 = nArray[n9][3] ^ (n5 ^ n4 ^ n3 ^ n2);
            this.C0 = objectArray[n8 & 0xFF] ^ this.shift(objectArray[n7 >> 8 & 0xFF], 24) ^ this.shift(objectArray[n6 >> 16 & 0xFF], 16) ^ this.shift(objectArray[n9 >> 24 & 0xFF], 8) ^ nArray[n][0];
            this.C1 = objectArray[n7 & 0xFF] ^ this.shift(objectArray[n6 >> 8 & 0xFF], 24) ^ this.shift(objectArray[n9 >> 16 & 0xFF], 16) ^ this.shift(objectArray[n8 >> 24 & 0xFF], 8) ^ nArray[n][1];
            this.C2 = objectArray[n6 & 0xFF] ^ this.shift(objectArray[n9 >> 8 & 0xFF], 24) ^ this.shift(objectArray[n8 >> 16 & 0xFF], 16) ^ this.shift(objectArray[n7 >> 24 & 0xFF], 8) ^ nArray[n][2];
            n9 = objectArray[n9 & 0xFF];
            n8 = this.shift(objectArray[n8 >> 8 & 0xFF], 24);
            n7 = this.shift(objectArray[n7 >> 16 & 0xFF], 16);
            this.C3 = this.shift(objectArray[n6 >> 24 & 0xFF], 8) ^ (n7 ^ (n8 ^ n9)) ^ nArray[n][3];
            n9 = n + 1;
        }
        objectArray = T0;
        n7 = objectArray[this.C0 & 0xFF] ^ this.shift(objectArray[this.C1 >> 8 & 0xFF], 24) ^ this.shift(objectArray[this.C2 >> 16 & 0xFF], 16) ^ this.shift(objectArray[this.C3 >> 24 & 0xFF], 8) ^ nArray[n9][0];
        n = objectArray[this.C1 & 0xFF] ^ this.shift(objectArray[this.C2 >> 8 & 0xFF], 24) ^ this.shift(objectArray[this.C3 >> 16 & 0xFF], 16) ^ this.shift(objectArray[this.C0 >> 24 & 0xFF], 8) ^ nArray[n9][1];
        n6 = objectArray[this.C2 & 0xFF] ^ this.shift(objectArray[this.C3 >> 8 & 0xFF], 24) ^ this.shift(objectArray[this.C0 >> 16 & 0xFF], 16) ^ this.shift(objectArray[this.C1 >> 24 & 0xFF], 8) ^ nArray[n9][2];
        n2 = objectArray[this.C3 & 0xFF];
        n5 = this.shift(objectArray[this.C0 >> 8 & 0xFF], 24);
        n3 = this.shift(objectArray[this.C1 >> 16 & 0xFF], 16);
        n4 = this.shift(objectArray[this.C2 >> 24 & 0xFF], 8);
        n8 = n9 + 1;
        n4 = nArray[n9][3] ^ (n4 ^ (n2 ^ n5 ^ n3));
        objectArray = S;
        n2 = objectArray[n7 & 0xFF];
        n5 = objectArray[n >> 8 & 0xFF];
        n3 = objectArray[n6 >> 16 & 0xFF];
        n9 = objectArray[n4 >> 24 & 0xFF];
        this.C0 = nArray[n8][0] ^ (n2 & 0xFF ^ (n5 & 0xFF) << 8 ^ (n3 & 0xFF) << 16 ^ n9 << 24);
        this.C1 = objectArray[n & 0xFF] & 0xFF ^ (objectArray[n6 >> 8 & 0xFF] & 0xFF) << 8 ^ (objectArray[n4 >> 16 & 0xFF] & 0xFF) << 16 ^ objectArray[n7 >> 24 & 0xFF] << 24 ^ nArray[n8][1];
        this.C2 = objectArray[n6 & 0xFF] & 0xFF ^ (objectArray[n4 >> 8 & 0xFF] & 0xFF) << 8 ^ (objectArray[n7 >> 16 & 0xFF] & 0xFF) << 16 ^ objectArray[n >> 24 & 0xFF] << 24 ^ nArray[n8][2];
        this.C3 = objectArray[n4 & 0xFF] & 0xFF ^ (objectArray[n7 >> 8 & 0xFF] & 0xFF) << 8 ^ (objectArray[n >> 16 & 0xFF] & 0xFF) << 16 ^ objectArray[n6 >> 24 & 0xFF] << 24 ^ nArray[n8][3];
    }

    private int[][] generateWorkingKey(byte[] object) throws ZipException {
        int n = ((byte[])object).length / 4;
        if ((n == 4 || n == 6 || n == 8) && n * 4 == ((byte[])object).length) {
            int n2;
            this.rounds = n2 = n + 6;
            int[][] nArray = new int[n2 + 1][4];
            n2 = 0;
            int n3 = 0;
            while (n3 < ((byte[])object).length) {
                nArray[n2 >> 2][n2 & 3] = object[n3] & 0xFF | (object[n3 + 1] & 0xFF) << 8 | (object[n3 + 2] & 0xFF) << 16 | object[n3 + 3] << 24;
                n3 += 4;
                ++n2;
            }
            int n4 = this.rounds;
            for (n3 = n; n3 < n4 + 1 << 2; ++n3) {
                int n5 = nArray[n3 - 1 >> 2][n3 - 1 & 3];
                if (n3 % n == 0) {
                    n2 = this.subWord(this.shift(n5, 8)) ^ rcon[n3 / n - 1];
                } else {
                    n2 = n5;
                    if (n > 6) {
                        n2 = n5;
                        if (n3 % n == 4) {
                            n2 = this.subWord(n5);
                        }
                    }
                }
                nArray[n3 >> 2][n3 & 3] = nArray[n3 - n >> 2][n3 - n & 3] ^ n2;
            }
            return nArray;
        }
        object = new ZipException("invalid key length (not 128/192/256)");
        throw object;
    }

    private int shift(int n, int n2) {
        return n >>> n2 | n << -n2;
    }

    private final void stateIn(byte[] byArray, int n) {
        int n2;
        int n3 = n + 1;
        this.C0 = n2 = byArray[n] & 0xFF;
        n = n3 + 1;
        this.C0 = n2 |= (byArray[n3] & 0xFF) << 8;
        n3 = n + 1;
        this.C0 = n2 |= (byArray[n] & 0xFF) << 16;
        n = n3 + 1;
        this.C0 = n2 | byArray[n3] << 24;
        n3 = n + 1;
        this.C1 = n2 = byArray[n] & 0xFF;
        n = n3 + 1;
        this.C1 = n2 = (byArray[n3] & 0xFF) << 8 | n2;
        n3 = n + 1;
        this.C1 = n2 |= (byArray[n] & 0xFF) << 16;
        n = n3 + 1;
        this.C1 = n2 | byArray[n3] << 24;
        n3 = n + 1;
        this.C2 = n2 = byArray[n] & 0xFF;
        n = n3 + 1;
        this.C2 = n2 = (byArray[n3] & 0xFF) << 8 | n2;
        n3 = n + 1;
        this.C2 = n2 |= (byArray[n] & 0xFF) << 16;
        n = n3 + 1;
        this.C2 = n2 | byArray[n3] << 24;
        n3 = n + 1;
        this.C3 = n2 = byArray[n] & 0xFF;
        n = n3 + 1;
        this.C3 = n2 = (byArray[n3] & 0xFF) << 8 | n2;
        n3 = n + 1;
        this.C3 = n = n2 | (byArray[n] & 0xFF) << 16;
        this.C3 = n | byArray[n3] << 24;
    }

    private final void stateOut(byte[] byArray, int n) {
        int n2 = n + 1;
        int n3 = this.C0;
        byArray[n] = (byte)n3;
        int n4 = n2 + 1;
        byArray[n2] = (byte)(n3 >> 8);
        n = n4 + 1;
        byArray[n4] = (byte)(n3 >> 16);
        n4 = n + 1;
        byArray[n] = (byte)(n3 >> 24);
        n2 = n4 + 1;
        n = this.C1;
        byArray[n4] = (byte)n;
        n3 = n2 + 1;
        byArray[n2] = (byte)(n >> 8);
        n4 = n3 + 1;
        byArray[n3] = (byte)(n >> 16);
        n3 = n4 + 1;
        byArray[n4] = (byte)(n >> 24);
        n4 = n3 + 1;
        n = this.C2;
        byArray[n3] = (byte)n;
        n3 = n4 + 1;
        byArray[n4] = (byte)(n >> 8);
        n4 = n3 + 1;
        byArray[n3] = (byte)(n >> 16);
        n3 = n4 + 1;
        byArray[n4] = (byte)(n >> 24);
        n4 = n3 + 1;
        n = this.C3;
        byArray[n3] = (byte)n;
        n3 = n4 + 1;
        byArray[n4] = (byte)(n >> 8);
        n4 = n3 + 1;
        byArray[n3] = (byte)(n >> 16);
        byArray[n4] = (byte)(n >> 24);
    }

    private int subWord(int n) {
        byte[] byArray = S;
        byte by = byArray[n & 0xFF];
        byte by2 = byArray[n >> 8 & 0xFF];
        byte by3 = byArray[n >> 16 & 0xFF];
        return byArray[n >> 24 & 0xFF] << 24 | (by & 0xFF | (by2 & 0xFF) << 8 | (by3 & 0xFF) << 16);
    }

    public void init(byte[] byArray) throws ZipException {
        this.workingKey = this.generateWorkingKey(byArray);
    }

    public int processBlock(byte[] byArray, int n, byte[] byArray2, int n2) throws ZipException {
        if (this.workingKey != null) {
            if (n + 16 <= byArray.length) {
                if (n2 + 16 <= byArray2.length) {
                    this.stateIn(byArray, n);
                    this.encryptBlock(this.workingKey);
                    this.stateOut(byArray2, n2);
                    return 16;
                }
                throw new ZipException("output buffer too short");
            }
            throw new ZipException("input buffer too short");
        }
        throw new ZipException("AES engine not initialised");
    }

    public int processBlock(byte[] byArray, byte[] byArray2) throws ZipException {
        return this.processBlock(byArray, 0, byArray2, 0);
    }

    private static long[] $d2j$hex$da5a0ceb$decode_J(String src) {
        byte[] d = AESEngine.$d2j$hex$da5a0ceb$decode_B(src);
        ByteBuffer b = ByteBuffer.wrap(d);
        b.order(ByteOrder.LITTLE_ENDIAN);
        LongBuffer s = b.asLongBuffer();
        long[] data = new long[d.length / 8];
        s.get(data);
        return data;
    }

    private static int[] $d2j$hex$da5a0ceb$decode_I(String src) {
        byte[] d = AESEngine.$d2j$hex$da5a0ceb$decode_B(src);
        ByteBuffer b = ByteBuffer.wrap(d);
        b.order(ByteOrder.LITTLE_ENDIAN);
        IntBuffer s = b.asIntBuffer();
        int[] data = new int[d.length / 4];
        s.get(data);
        return data;
    }

    private static short[] $d2j$hex$da5a0ceb$decode_S(String src) {
        byte[] d = AESEngine.$d2j$hex$da5a0ceb$decode_B(src);
        ByteBuffer b = ByteBuffer.wrap(d);
        b.order(ByteOrder.LITTLE_ENDIAN);
        ShortBuffer s = b.asShortBuffer();
        short[] data = new short[d.length / 2];
        s.get(data);
        return data;
    }

    private static byte[] $d2j$hex$da5a0ceb$decode_B(String src) {
        char[] d = src.toCharArray();
        byte[] ret = new byte[src.length() / 2];
        for (int i = 0; i < ret.length; ++i) {
            int ll;
            int hh;
            char h = d[2 * i];
            char l = d[2 * i + 1];
            if (h >= '0' && h <= '9') {
                hh = h - 48;
            } else if (h >= 'a' && h <= 'f') {
                hh = h - 97 + 10;
            } else if (h >= 'A' && h <= 'F') {
                hh = h - 65 + 10;
            } else {
                throw new RuntimeException();
            }
            if (l >= '0' && l <= '9') {
                ll = l - 48;
            } else if (l >= 'a' && l <= 'f') {
                ll = l - 97 + 10;
            } else if (l >= 'A' && l <= 'F') {
                ll = l - 65 + 10;
            } else {
                throw new RuntimeException();
            }
            ret[i] = (byte)(hh << 4 | ll);
        }
        return ret;
    }
}

