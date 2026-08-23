/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.util;

import com.google.android.gms.common.internal.Objects;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public final class ArrayUtils {
    private ArrayUtils() {
    }

    public static <T> T[] concat(T[] ... TArray) {
        if (TArray.length != 0) {
            int n;
            int n2 = 0;
            for (n = 0; n < TArray.length; ++n) {
                n2 += TArray[n].length;
            }
            T[] TArray2 = Arrays.copyOf(TArray[0], n2);
            n2 = TArray[0].length;
            for (n = 1; n < TArray.length; ++n) {
                T[] TArray3 = TArray[n];
                int n3 = TArray3.length;
                System.arraycopy(TArray3, 0, TArray2, n2, n3);
                n2 += n3;
            }
            return TArray2;
        }
        return (Object[])Array.newInstance(TArray.getClass(), 0);
    }

    public static byte[] concatByteArrays(byte[] ... byArray) {
        if (byArray.length != 0) {
            int n;
            int n2 = 0;
            for (n = 0; n < byArray.length; ++n) {
                n2 += byArray[n].length;
            }
            byte[] byArray2 = Arrays.copyOf(byArray[0], n2);
            n = byArray[0].length;
            for (n2 = 1; n2 < byArray.length; ++n2) {
                byte[] byArray3 = byArray[n2];
                int n3 = byArray3.length;
                System.arraycopy(byArray3, 0, byArray2, n, n3);
                n += n3;
            }
            return byArray2;
        }
        return new byte[0];
    }

    public static boolean contains(int[] nArray, int n) {
        if (nArray == null) {
            return false;
        }
        int n2 = nArray.length;
        for (int i = 0; i < n2; ++i) {
            if (nArray[i] != n) continue;
            return true;
        }
        return false;
    }

    public static <T> boolean contains(T[] TArray, T t) {
        int n = TArray != null ? TArray.length : 0;
        for (int i = 0; i < n; ++i) {
            if (!Objects.equal(TArray[i], t)) continue;
            if (i < 0) break;
            return true;
        }
        return false;
    }

    public static <T> ArrayList<T> newArrayList() {
        return new ArrayList();
    }

    public static <T> T[] removeAll(T[] objectArray, T ... TArray) {
        block10: {
            Object[] objectArray2;
            int n;
            block13: {
                block12: {
                    block11: {
                        int n2;
                        Object var6_2 = null;
                        if (objectArray == null) {
                            return null;
                        }
                        if (TArray == null || (n = TArray.length) == 0) break block10;
                        objectArray2 = TArray.getClass().getComponentType();
                        int n3 = objectArray.length;
                        objectArray2 = (Object[])Array.newInstance(objectArray2, n3);
                        if (n == 1) {
                            n = 0;
                            for (int i = 0; i < n3; ++i) {
                                T t = objectArray[i];
                                n2 = n;
                                if (!Objects.equal(TArray[0], t)) {
                                    objectArray2[n] = t;
                                    n2 = n + 1;
                                }
                                n = n2;
                            }
                        } else {
                            n = 0;
                            for (n2 = 0; n2 < n3; ++n2) {
                                T t = objectArray[n2];
                                int n4 = n;
                                if (!ArrayUtils.contains(TArray, t)) {
                                    objectArray2[n] = t;
                                    n4 = n + 1;
                                }
                                n = n4;
                            }
                        }
                        if (objectArray2 != null) break block11;
                        objectArray = var6_2;
                        break block12;
                    }
                    if (n != objectArray2.length) break block13;
                    objectArray = objectArray2;
                }
                return objectArray;
            }
            return Arrays.copyOf(objectArray2, n);
        }
        return Arrays.copyOf(objectArray, objectArray.length);
    }

    public static <T> ArrayList<T> toArrayList(T[] TArray) {
        int n = TArray.length;
        ArrayList<T> arrayList = new ArrayList<T>(n);
        for (int i = 0; i < n; ++i) {
            arrayList.add(TArray[i]);
        }
        return arrayList;
    }

    public static int[] toPrimitiveArray(Collection<Integer> object) {
        int n = 0;
        if (object != null && object.size() != 0) {
            int[] nArray = new int[object.size()];
            object = object.iterator();
            while (object.hasNext()) {
                nArray[n] = (Integer)object.next();
                ++n;
            }
            return nArray;
        }
        return new int[0];
    }

    public static Integer[] toWrapperArray(int[] nArray) {
        if (nArray == null) {
            return null;
        }
        int n = nArray.length;
        Integer[] integerArray = new Integer[n];
        for (int i = 0; i < n; ++i) {
            integerArray[i] = nArray[i];
        }
        return integerArray;
    }

    public static void writeArray(StringBuilder stringBuilder, double[] dArray) {
        int n = dArray.length;
        for (int i = 0; i < n; ++i) {
            if (i != 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append(Double.toString(dArray[i]));
        }
    }

    public static void writeArray(StringBuilder stringBuilder, float[] fArray) {
        int n = fArray.length;
        for (int i = 0; i < n; ++i) {
            if (i != 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append(Float.toString(fArray[i]));
        }
    }

    public static void writeArray(StringBuilder stringBuilder, int[] nArray) {
        int n = nArray.length;
        for (int i = 0; i < n; ++i) {
            if (i != 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append(Integer.toString(nArray[i]));
        }
    }

    public static void writeArray(StringBuilder stringBuilder, long[] lArray) {
        int n = lArray.length;
        for (int i = 0; i < n; ++i) {
            if (i != 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append(Long.toString(lArray[i]));
        }
    }

    public static <T> void writeArray(StringBuilder stringBuilder, T[] TArray) {
        int n = TArray.length;
        for (int i = 0; i < n; ++i) {
            if (i != 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append(TArray[i]);
        }
    }

    public static void writeArray(StringBuilder stringBuilder, boolean[] blArray) {
        int n = blArray.length;
        for (int i = 0; i < n; ++i) {
            if (i != 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append(Boolean.toString(blArray[i]));
        }
    }

    public static void writeStringArray(StringBuilder stringBuilder, String[] stringArray) {
        int n = stringArray.length;
        for (int i = 0; i < n; ++i) {
            if (i != 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append("\"");
            stringBuilder.append(stringArray[i]);
            stringBuilder.append("\"");
        }
    }
}

