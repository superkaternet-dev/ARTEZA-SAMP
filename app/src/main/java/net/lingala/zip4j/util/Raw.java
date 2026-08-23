/*
 * Decompiled with CFR 0.152.
 */
package net.lingala.zip4j.util;

import java.io.DataInput;
import java.io.IOException;
import net.lingala.zip4j.exception.ZipException;

public class Raw {
    public static byte bitArrayToByte(int[] object) throws ZipException {
        if (object != null) {
            if (((int[])object).length == 8) {
                if (Raw.checkBits(object)) {
                    int n = 0;
                    for (int i = 0; i < ((int[])object).length; ++i) {
                        double d = n;
                        double d2 = Math.pow(2.0, i);
                        double d3 = object[i];
                        Double.isNaN(d3);
                        Double.isNaN(d);
                        n = (int)(d + d2 * d3);
                    }
                    return (byte)n;
                }
                throw new ZipException("invalid bits provided, bits contain other values than 0 or 1");
            }
            throw new ZipException("invalid bit array length, cannot calculate byte");
        }
        object = new ZipException("bit array is null, cannot calculate byte from bits");
        throw object;
    }

    private static boolean checkBits(int[] nArray) {
        for (int i = 0; i < nArray.length; ++i) {
            if (nArray[i] == 0 || nArray[i] == 1) continue;
            return false;
        }
        return true;
    }

    public static byte[] convertCharArrayToByteArray(char[] object) {
        if (object != null) {
            byte[] byArray = new byte[((char[])object).length];
            for (int i = 0; i < ((char[])object).length; ++i) {
                byArray[i] = (byte)object[i];
            }
            return byArray;
        }
        object = new NullPointerException();
        throw object;
    }

    public static void prepareBuffAESIVBytes(byte[] byArray, int n, int n2) {
        byArray[0] = (byte)n;
        byArray[1] = (byte)(n >> 8);
        byArray[2] = (byte)(n >> 16);
        byArray[3] = (byte)(n >> 24);
        byArray[4] = 0;
        byArray[5] = 0;
        byArray[6] = 0;
        byArray[7] = 0;
        byArray[8] = 0;
        byArray[9] = 0;
        byArray[10] = 0;
        byArray[11] = 0;
        byArray[12] = 0;
        byArray[13] = 0;
        byArray[14] = 0;
        byArray[15] = 0;
    }

    public static int readIntLittleEndian(byte[] byArray, int n) {
        return byArray[n] & 0xFF | (byArray[n + 1] & 0xFF) << 8 | (byArray[n + 2] & 0xFF | (byArray[n + 3] & 0xFF) << 8) << 16;
    }

    public static int readLeInt(DataInput dataInput, byte[] byArray) throws ZipException {
        try {
            dataInput.readFully(byArray, 0, 4);
        }
        catch (IOException iOException) {
            throw new ZipException(iOException);
        }
        return byArray[0] & 0xFF | (byArray[1] & 0xFF) << 8 | (byArray[2] & 0xFF | (byArray[3] & 0xFF) << 8) << 16;
    }

    public static long readLongLittleEndian(byte[] byArray, int n) {
        return (((((((0L | (long)(byArray[n + 7] & 0xFF)) << 8 | (long)(byArray[n + 6] & 0xFF)) << 8 | (long)(byArray[n + 5] & 0xFF)) << 8 | (long)(byArray[n + 4] & 0xFF)) << 8 | (long)(byArray[n + 3] & 0xFF)) << 8 | (long)(byArray[n + 2] & 0xFF)) << 8 | (long)(byArray[n + 1] & 0xFF)) << 8 | (long)(byArray[n] & 0xFF);
    }

    public static final short readShortBigEndian(byte[] byArray, int n) {
        short s = (short)((short)(byArray[n] & 0xFF | 0) << 8);
        return (short)(byArray[n + 1] & 0xFF | s);
    }

    public static int readShortLittleEndian(byte[] byArray, int n) {
        return byArray[n] & 0xFF | (byArray[n + 1] & 0xFF) << 8;
    }

    public static byte[] toByteArray(int n) {
        return new byte[]{(byte)n, (byte)(n >> 8), (byte)(n >> 16), (byte)(n >> 24)};
    }

    public static byte[] toByteArray(int n, int n2) {
        byte[] byArray = new byte[n2];
        byte[] byArray2 = Raw.toByteArray(n);
        for (n = 0; n < byArray2.length && n < n2; ++n) {
            byArray[n] = byArray2[n];
        }
        return byArray;
    }

    public static final void writeIntLittleEndian(byte[] byArray, int n, int n2) {
        byArray[n + 3] = (byte)(n2 >>> 24);
        byArray[n + 2] = (byte)(n2 >>> 16);
        byArray[n + 1] = (byte)(n2 >>> 8);
        byArray[n] = (byte)(n2 & 0xFF);
    }

    public static void writeLongLittleEndian(byte[] byArray, int n, long l) {
        byArray[n + 7] = (byte)(l >>> 56);
        byArray[n + 6] = (byte)(l >>> 48);
        byArray[n + 5] = (byte)(l >>> 40);
        byArray[n + 4] = (byte)(l >>> 32);
        byArray[n + 3] = (byte)(l >>> 24);
        byArray[n + 2] = (byte)(l >>> 16);
        byArray[n + 1] = (byte)(l >>> 8);
        byArray[n] = (byte)(0xFFL & l);
    }

    public static final void writeShortLittleEndian(byte[] byArray, int n, short s) {
        byArray[n + 1] = (byte)(s >>> 8);
        byArray[n] = (byte)(s & 0xFF);
    }
}

