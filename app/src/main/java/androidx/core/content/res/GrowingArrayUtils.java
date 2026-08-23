/*
 * Decompiled with CFR 0.152.
 */
package androidx.core.content.res;

import java.lang.reflect.Array;

final class GrowingArrayUtils {
    private GrowingArrayUtils() {
    }

    public static int[] append(int[] nArray, int n, int n2) {
        int[] nArray2 = nArray;
        if (n + 1 > nArray.length) {
            nArray2 = new int[GrowingArrayUtils.growSize(n)];
            System.arraycopy(nArray, 0, nArray2, 0, n);
        }
        nArray2[n] = n2;
        return nArray2;
    }

    public static long[] append(long[] lArray, int n, long l) {
        long[] lArray2 = lArray;
        if (n + 1 > lArray.length) {
            lArray2 = new long[GrowingArrayUtils.growSize(n)];
            System.arraycopy(lArray, 0, lArray2, 0, n);
        }
        lArray2[n] = l;
        return lArray2;
    }

    public static <T> T[] append(T[] TArray, int n, T t) {
        Object[] objectArray = TArray;
        if (n + 1 > TArray.length) {
            objectArray = (Object[])Array.newInstance(TArray.getClass().getComponentType(), GrowingArrayUtils.growSize(n));
            System.arraycopy(TArray, 0, objectArray, 0, n);
        }
        objectArray[n] = t;
        return objectArray;
    }

    public static boolean[] append(boolean[] blArray, int n, boolean bl) {
        boolean[] blArray2 = blArray;
        if (n + 1 > blArray.length) {
            blArray2 = new boolean[GrowingArrayUtils.growSize(n)];
            System.arraycopy(blArray, 0, blArray2, 0, n);
        }
        blArray2[n] = bl;
        return blArray2;
    }

    public static int growSize(int n) {
        n = n <= 4 ? 8 : (n *= 2);
        return n;
    }

    public static int[] insert(int[] nArray, int n, int n2, int n3) {
        if (n + 1 <= nArray.length) {
            System.arraycopy(nArray, n2, nArray, n2 + 1, n - n2);
            nArray[n2] = n3;
            return nArray;
        }
        int[] nArray2 = new int[GrowingArrayUtils.growSize(n)];
        System.arraycopy(nArray, 0, nArray2, 0, n2);
        nArray2[n2] = n3;
        System.arraycopy(nArray, n2, nArray2, n2 + 1, nArray.length - n2);
        return nArray2;
    }

    public static long[] insert(long[] lArray, int n, int n2, long l) {
        if (n + 1 <= lArray.length) {
            System.arraycopy(lArray, n2, lArray, n2 + 1, n - n2);
            lArray[n2] = l;
            return lArray;
        }
        long[] lArray2 = new long[GrowingArrayUtils.growSize(n)];
        System.arraycopy(lArray, 0, lArray2, 0, n2);
        lArray2[n2] = l;
        System.arraycopy(lArray, n2, lArray2, n2 + 1, lArray.length - n2);
        return lArray2;
    }

    public static <T> T[] insert(T[] TArray, int n, int n2, T t) {
        if (n + 1 <= TArray.length) {
            System.arraycopy(TArray, n2, TArray, n2 + 1, n - n2);
            TArray[n2] = t;
            return TArray;
        }
        Object[] objectArray = (Object[])Array.newInstance(TArray.getClass().getComponentType(), GrowingArrayUtils.growSize(n));
        System.arraycopy(TArray, 0, objectArray, 0, n2);
        objectArray[n2] = t;
        System.arraycopy(TArray, n2, objectArray, n2 + 1, TArray.length - n2);
        return objectArray;
    }

    public static boolean[] insert(boolean[] blArray, int n, int n2, boolean bl) {
        if (n + 1 <= blArray.length) {
            System.arraycopy(blArray, n2, blArray, n2 + 1, n - n2);
            blArray[n2] = bl;
            return blArray;
        }
        boolean[] blArray2 = new boolean[GrowingArrayUtils.growSize(n)];
        System.arraycopy(blArray, 0, blArray2, 0, n2);
        blArray2[n2] = bl;
        System.arraycopy(blArray, n2, blArray2, n2 + 1, blArray.length - n2);
        return blArray2;
    }
}

