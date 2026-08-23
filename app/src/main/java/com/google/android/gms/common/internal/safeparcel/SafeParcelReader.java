/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.PendingIntent
 *  android.os.Bundle
 *  android.os.IBinder
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.util.SparseArray
 *  android.util.SparseBooleanArray
 *  android.util.SparseIntArray
 *  android.util.SparseLongArray
 */
package com.google.android.gms.common.internal.safeparcel;

import android.app.PendingIntent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.util.SparseLongArray;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class SafeParcelReader {
    private SafeParcelReader() {
    }

    public static BigDecimal createBigDecimal(Parcel parcel, int n) {
        int n2 = SafeParcelReader.readSize(parcel, n);
        n = parcel.dataPosition();
        if (n2 == 0) {
            return null;
        }
        byte[] byArray = parcel.createByteArray();
        int n3 = parcel.readInt();
        parcel.setDataPosition(n + n2);
        return new BigDecimal(new BigInteger(byArray), n3);
    }

    public static BigDecimal[] createBigDecimalArray(Parcel parcel, int n) {
        int n2 = SafeParcelReader.readSize(parcel, n);
        int n3 = parcel.dataPosition();
        if (n2 == 0) {
            return null;
        }
        int n4 = parcel.readInt();
        BigDecimal[] bigDecimalArray = new BigDecimal[n4];
        for (n = 0; n < n4; ++n) {
            byte[] byArray = parcel.createByteArray();
            int n5 = parcel.readInt();
            bigDecimalArray[n] = new BigDecimal(new BigInteger(byArray), n5);
        }
        parcel.setDataPosition(n3 + n2);
        return bigDecimalArray;
    }

    public static BigInteger createBigInteger(Parcel parcel, int n) {
        n = SafeParcelReader.readSize(parcel, n);
        int n2 = parcel.dataPosition();
        if (n == 0) {
            return null;
        }
        byte[] byArray = parcel.createByteArray();
        parcel.setDataPosition(n2 + n);
        return new BigInteger(byArray);
    }

    public static BigInteger[] createBigIntegerArray(Parcel parcel, int n) {
        int n2 = SafeParcelReader.readSize(parcel, n);
        int n3 = parcel.dataPosition();
        if (n2 == 0) {
            return null;
        }
        int n4 = parcel.readInt();
        BigInteger[] bigIntegerArray = new BigInteger[n4];
        for (n = 0; n < n4; ++n) {
            bigIntegerArray[n] = new BigInteger(parcel.createByteArray());
        }
        parcel.setDataPosition(n3 + n2);
        return bigIntegerArray;
    }

    public static boolean[] createBooleanArray(Parcel parcel, int n) {
        n = SafeParcelReader.readSize(parcel, n);
        int n2 = parcel.dataPosition();
        if (n == 0) {
            return null;
        }
        boolean[] blArray = parcel.createBooleanArray();
        parcel.setDataPosition(n2 + n);
        return blArray;
    }

    public static ArrayList<Boolean> createBooleanList(Parcel parcel, int n) {
        int n2 = SafeParcelReader.readSize(parcel, n);
        int n3 = parcel.dataPosition();
        if (n2 == 0) {
            return null;
        }
        ArrayList<Boolean> arrayList = new ArrayList<Boolean>();
        int n4 = parcel.readInt();
        for (n = 0; n < n4; ++n) {
            boolean bl = parcel.readInt() != 0;
            arrayList.add(bl);
        }
        parcel.setDataPosition(n3 + n2);
        return arrayList;
    }

    public static Bundle createBundle(Parcel parcel, int n) {
        int n2 = SafeParcelReader.readSize(parcel, n);
        n = parcel.dataPosition();
        if (n2 == 0) {
            return null;
        }
        Bundle bundle = parcel.readBundle();
        parcel.setDataPosition(n + n2);
        return bundle;
    }

    public static byte[] createByteArray(Parcel parcel, int n) {
        n = SafeParcelReader.readSize(parcel, n);
        int n2 = parcel.dataPosition();
        if (n == 0) {
            return null;
        }
        byte[] byArray = parcel.createByteArray();
        parcel.setDataPosition(n2 + n);
        return byArray;
    }

    public static byte[][] createByteArrayArray(Parcel parcel, int n) {
        int n2 = SafeParcelReader.readSize(parcel, n);
        int n3 = parcel.dataPosition();
        if (n2 == 0) {
            return null;
        }
        int n4 = parcel.readInt();
        byte[][] byArrayArray = new byte[n4][];
        for (n = 0; n < n4; ++n) {
            byArrayArray[n] = parcel.createByteArray();
        }
        parcel.setDataPosition(n3 + n2);
        return byArrayArray;
    }

    public static SparseArray<byte[]> createByteArraySparseArray(Parcel parcel, int n) {
        int n2 = SafeParcelReader.readSize(parcel, n);
        int n3 = parcel.dataPosition();
        if (n2 == 0) {
            return null;
        }
        int n4 = parcel.readInt();
        SparseArray sparseArray = new SparseArray(n4);
        for (n = 0; n < n4; ++n) {
            sparseArray.append(parcel.readInt(), (Object)parcel.createByteArray());
        }
        parcel.setDataPosition(n3 + n2);
        return sparseArray;
    }

    public static char[] createCharArray(Parcel parcel, int n) {
        int n2 = SafeParcelReader.readSize(parcel, n);
        n = parcel.dataPosition();
        if (n2 == 0) {
            return null;
        }
        char[] cArray = parcel.createCharArray();
        parcel.setDataPosition(n + n2);
        return cArray;
    }

    public static double[] createDoubleArray(Parcel parcel, int n) {
        n = SafeParcelReader.readSize(parcel, n);
        int n2 = parcel.dataPosition();
        if (n == 0) {
            return null;
        }
        double[] dArray = parcel.createDoubleArray();
        parcel.setDataPosition(n2 + n);
        return dArray;
    }

    public static ArrayList<Double> createDoubleList(Parcel parcel, int n) {
        int n2 = SafeParcelReader.readSize(parcel, n);
        int n3 = parcel.dataPosition();
        if (n2 == 0) {
            return null;
        }
        ArrayList<Double> arrayList = new ArrayList<Double>();
        int n4 = parcel.readInt();
        for (n = 0; n < n4; ++n) {
            arrayList.add(parcel.readDouble());
        }
        parcel.setDataPosition(n3 + n2);
        return arrayList;
    }

    public static SparseArray<Double> createDoubleSparseArray(Parcel parcel, int n) {
        int n2 = SafeParcelReader.readSize(parcel, n);
        int n3 = parcel.dataPosition();
        if (n2 == 0) {
            return null;
        }
        SparseArray sparseArray = new SparseArray();
        int n4 = parcel.readInt();
        for (n = 0; n < n4; ++n) {
            sparseArray.append(parcel.readInt(), (Object)parcel.readDouble());
        }
        parcel.setDataPosition(n3 + n2);
        return sparseArray;
    }

    public static float[] createFloatArray(Parcel parcel, int n) {
        int n2 = SafeParcelReader.readSize(parcel, n);
        n = parcel.dataPosition();
        if (n2 == 0) {
            return null;
        }
        float[] fArray = parcel.createFloatArray();
        parcel.setDataPosition(n + n2);
        return fArray;
    }

    public static ArrayList<Float> createFloatList(Parcel parcel, int n) {
        int n2 = SafeParcelReader.readSize(parcel, n);
        int n3 = parcel.dataPosition();
        if (n2 == 0) {
            return null;
        }
        ArrayList<Float> arrayList = new ArrayList<Float>();
        int n4 = parcel.readInt();
        for (n = 0; n < n4; ++n) {
            arrayList.add(Float.valueOf(parcel.readFloat()));
        }
        parcel.setDataPosition(n3 + n2);
        return arrayList;
    }

    public static SparseArray<Float> createFloatSparseArray(Parcel parcel, int n) {
        int n2 = SafeParcelReader.readSize(parcel, n);
        int n3 = parcel.dataPosition();
        if (n2 == 0) {
            return null;
        }
        SparseArray sparseArray = new SparseArray();
        int n4 = parcel.readInt();
        for (n = 0; n < n4; ++n) {
            sparseArray.append(parcel.readInt(), (Object)Float.valueOf(parcel.readFloat()));
        }
        parcel.setDataPosition(n3 + n2);
        return sparseArray;
    }

    public static IBinder[] createIBinderArray(Parcel parcel, int n) {
        n = SafeParcelReader.readSize(parcel, n);
        int n2 = parcel.dataPosition();
        if (n == 0) {
            return null;
        }
        IBinder[] iBinderArray = parcel.createBinderArray();
        parcel.setDataPosition(n2 + n);
        return iBinderArray;
    }

    public static ArrayList<IBinder> createIBinderList(Parcel parcel, int n) {
        int n2 = SafeParcelReader.readSize(parcel, n);
        n = parcel.dataPosition();
        if (n2 == 0) {
            return null;
        }
        ArrayList arrayList = parcel.createBinderArrayList();
        parcel.setDataPosition(n + n2);
        return arrayList;
    }

    public static SparseArray<IBinder> createIBinderSparseArray(Parcel parcel, int n) {
        int n2 = SafeParcelReader.readSize(parcel, n);
        int n3 = parcel.dataPosition();
        if (n2 == 0) {
            return null;
        }
        int n4 = parcel.readInt();
        SparseArray sparseArray = new SparseArray(n4);
        for (n = 0; n < n4; ++n) {
            sparseArray.append(parcel.readInt(), (Object)parcel.readStrongBinder());
        }
        parcel.setDataPosition(n3 + n2);
        return sparseArray;
    }

    public static int[] createIntArray(Parcel parcel, int n) {
        n = SafeParcelReader.readSize(parcel, n);
        int n2 = parcel.dataPosition();
        if (n == 0) {
            return null;
        }
        int[] nArray = parcel.createIntArray();
        parcel.setDataPosition(n2 + n);
        return nArray;
    }

    public static ArrayList<Integer> createIntegerList(Parcel parcel, int n) {
        int n2 = SafeParcelReader.readSize(parcel, n);
        int n3 = parcel.dataPosition();
        if (n2 == 0) {
            return null;
        }
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        int n4 = parcel.readInt();
        for (n = 0; n < n4; ++n) {
            arrayList.add(parcel.readInt());
        }
        parcel.setDataPosition(n3 + n2);
        return arrayList;
    }

    public static long[] createLongArray(Parcel parcel, int n) {
        n = SafeParcelReader.readSize(parcel, n);
        int n2 = parcel.dataPosition();
        if (n == 0) {
            return null;
        }
        long[] lArray = parcel.createLongArray();
        parcel.setDataPosition(n2 + n);
        return lArray;
    }

    public static ArrayList<Long> createLongList(Parcel parcel, int n) {
        int n2 = SafeParcelReader.readSize(parcel, n);
        int n3 = parcel.dataPosition();
        if (n2 == 0) {
            return null;
        }
        ArrayList<Long> arrayList = new ArrayList<Long>();
        int n4 = parcel.readInt();
        for (n = 0; n < n4; ++n) {
            arrayList.add(parcel.readLong());
        }
        parcel.setDataPosition(n3 + n2);
        return arrayList;
    }

    public static Parcel createParcel(Parcel parcel, int n) {
        int n2 = SafeParcelReader.readSize(parcel, n);
        n = parcel.dataPosition();
        if (n2 == 0) {
            return null;
        }
        Parcel parcel2 = Parcel.obtain();
        parcel2.appendFrom(parcel, n, n2);
        parcel.setDataPosition(n + n2);
        return parcel2;
    }

    public static Parcel[] createParcelArray(Parcel parcel, int n) {
        int n2 = SafeParcelReader.readSize(parcel, n);
        int n3 = parcel.dataPosition();
        if (n2 == 0) {
            return null;
        }
        int n4 = parcel.readInt();
        Parcel[] parcelArray = new Parcel[n4];
        for (n = 0; n < n4; ++n) {
            int n5 = parcel.readInt();
            if (n5 != 0) {
                int n6 = parcel.dataPosition();
                Parcel parcel2 = Parcel.obtain();
                parcel2.appendFrom(parcel, n6, n5);
                parcelArray[n] = parcel2;
                parcel.setDataPosition(n6 + n5);
                continue;
            }
            parcelArray[n] = null;
        }
        parcel.setDataPosition(n3 + n2);
        return parcelArray;
    }

    public static ArrayList<Parcel> createParcelList(Parcel parcel, int n) {
        int n2 = SafeParcelReader.readSize(parcel, n);
        int n3 = parcel.dataPosition();
        if (n2 == 0) {
            return null;
        }
        int n4 = parcel.readInt();
        ArrayList<Parcel> arrayList = new ArrayList<Parcel>();
        for (n = 0; n < n4; ++n) {
            int n5 = parcel.readInt();
            if (n5 != 0) {
                int n6 = parcel.dataPosition();
                Parcel parcel2 = Parcel.obtain();
                parcel2.appendFrom(parcel, n6, n5);
                arrayList.add(parcel2);
                parcel.setDataPosition(n6 + n5);
                continue;
            }
            arrayList.add(null);
        }
        parcel.setDataPosition(n3 + n2);
        return arrayList;
    }

    public static SparseArray<Parcel> createParcelSparseArray(Parcel parcel, int n) {
        int n2 = SafeParcelReader.readSize(parcel, n);
        int n3 = parcel.dataPosition();
        if (n2 == 0) {
            return null;
        }
        int n4 = parcel.readInt();
        SparseArray sparseArray = new SparseArray();
        for (n = 0; n < n4; ++n) {
            int n5 = parcel.readInt();
            int n6 = parcel.readInt();
            if (n6 != 0) {
                int n7 = parcel.dataPosition();
                Parcel parcel2 = Parcel.obtain();
                parcel2.appendFrom(parcel, n7, n6);
                sparseArray.append(n5, (Object)parcel2);
                parcel.setDataPosition(n7 + n6);
                continue;
            }
            sparseArray.append(n5, null);
        }
        parcel.setDataPosition(n3 + n2);
        return sparseArray;
    }

    public static <T extends Parcelable> T createParcelable(Parcel parcel, int n, Parcelable.Creator<T> parcelable) {
        int n2 = SafeParcelReader.readSize(parcel, n);
        n = parcel.dataPosition();
        if (n2 == 0) {
            return null;
        }
        parcelable = (Parcelable)parcelable.createFromParcel(parcel);
        parcel.setDataPosition(n + n2);
        return (T)parcelable;
    }

    public static SparseBooleanArray createSparseBooleanArray(Parcel parcel, int n) {
        int n2 = SafeParcelReader.readSize(parcel, n);
        n = parcel.dataPosition();
        if (n2 == 0) {
            return null;
        }
        SparseBooleanArray sparseBooleanArray = parcel.readSparseBooleanArray();
        parcel.setDataPosition(n + n2);
        return sparseBooleanArray;
    }

    public static SparseIntArray createSparseIntArray(Parcel parcel, int n) {
        int n2 = SafeParcelReader.readSize(parcel, n);
        int n3 = parcel.dataPosition();
        if (n2 == 0) {
            return null;
        }
        SparseIntArray sparseIntArray = new SparseIntArray();
        int n4 = parcel.readInt();
        for (n = 0; n < n4; ++n) {
            sparseIntArray.append(parcel.readInt(), parcel.readInt());
        }
        parcel.setDataPosition(n3 + n2);
        return sparseIntArray;
    }

    public static SparseLongArray createSparseLongArray(Parcel parcel, int n) {
        int n2 = SafeParcelReader.readSize(parcel, n);
        int n3 = parcel.dataPosition();
        if (n2 == 0) {
            return null;
        }
        SparseLongArray sparseLongArray = new SparseLongArray();
        int n4 = parcel.readInt();
        for (n = 0; n < n4; ++n) {
            sparseLongArray.append(parcel.readInt(), parcel.readLong());
        }
        parcel.setDataPosition(n3 + n2);
        return sparseLongArray;
    }

    public static String createString(Parcel parcel, int n) {
        int n2 = SafeParcelReader.readSize(parcel, n);
        n = parcel.dataPosition();
        if (n2 == 0) {
            return null;
        }
        String string2 = parcel.readString();
        parcel.setDataPosition(n + n2);
        return string2;
    }

    public static String[] createStringArray(Parcel parcel, int n) {
        n = SafeParcelReader.readSize(parcel, n);
        int n2 = parcel.dataPosition();
        if (n == 0) {
            return null;
        }
        String[] stringArray = parcel.createStringArray();
        parcel.setDataPosition(n2 + n);
        return stringArray;
    }

    public static ArrayList<String> createStringList(Parcel parcel, int n) {
        int n2 = SafeParcelReader.readSize(parcel, n);
        n = parcel.dataPosition();
        if (n2 == 0) {
            return null;
        }
        ArrayList arrayList = parcel.createStringArrayList();
        parcel.setDataPosition(n + n2);
        return arrayList;
    }

    public static SparseArray<String> createStringSparseArray(Parcel parcel, int n) {
        int n2 = SafeParcelReader.readSize(parcel, n);
        int n3 = parcel.dataPosition();
        if (n2 == 0) {
            return null;
        }
        SparseArray sparseArray = new SparseArray();
        int n4 = parcel.readInt();
        for (n = 0; n < n4; ++n) {
            sparseArray.append(parcel.readInt(), (Object)parcel.readString());
        }
        parcel.setDataPosition(n3 + n2);
        return sparseArray;
    }

    public static <T> T[] createTypedArray(Parcel parcel, int n, Parcelable.Creator<T> objectArray) {
        int n2 = SafeParcelReader.readSize(parcel, n);
        n = parcel.dataPosition();
        if (n2 == 0) {
            return null;
        }
        objectArray = parcel.createTypedArray(objectArray);
        parcel.setDataPosition(n + n2);
        return objectArray;
    }

    public static <T> ArrayList<T> createTypedList(Parcel parcel, int n, Parcelable.Creator<T> object) {
        n = SafeParcelReader.readSize(parcel, n);
        int n2 = parcel.dataPosition();
        if (n == 0) {
            return null;
        }
        object = parcel.createTypedArrayList(object);
        parcel.setDataPosition(n2 + n);
        return object;
    }

    public static <T> SparseArray<T> createTypedSparseArray(Parcel parcel, int n, Parcelable.Creator<T> creator) {
        int n2 = SafeParcelReader.readSize(parcel, n);
        int n3 = parcel.dataPosition();
        if (n2 == 0) {
            return null;
        }
        int n4 = parcel.readInt();
        SparseArray sparseArray = new SparseArray();
        for (n = 0; n < n4; ++n) {
            int n5 = parcel.readInt();
            Object object = parcel.readInt() != 0 ? creator.createFromParcel(parcel) : null;
            sparseArray.append(n5, object);
        }
        parcel.setDataPosition(n3 + n2);
        return sparseArray;
    }

    public static void ensureAtEnd(Parcel parcel, int n) {
        if (parcel.dataPosition() == n) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder(37);
        stringBuilder.append("Overread allowed size end=");
        stringBuilder.append(n);
        throw new ParseException(stringBuilder.toString(), parcel);
    }

    public static int getFieldId(int n) {
        return (char)n;
    }

    public static boolean readBoolean(Parcel parcel, int n) {
        SafeParcelReader.zzb(parcel, n, 4);
        return parcel.readInt() != 0;
    }

    public static Boolean readBooleanObject(Parcel parcel, int n) {
        int n2 = SafeParcelReader.readSize(parcel, n);
        if (n2 == 0) {
            return null;
        }
        SafeParcelReader.zza(parcel, n, n2, 4);
        boolean bl = parcel.readInt() != 0;
        return bl;
    }

    public static byte readByte(Parcel parcel, int n) {
        SafeParcelReader.zzb(parcel, n, 4);
        return (byte)parcel.readInt();
    }

    public static char readChar(Parcel parcel, int n) {
        SafeParcelReader.zzb(parcel, n, 4);
        return (char)parcel.readInt();
    }

    public static double readDouble(Parcel parcel, int n) {
        SafeParcelReader.zzb(parcel, n, 8);
        return parcel.readDouble();
    }

    public static Double readDoubleObject(Parcel parcel, int n) {
        int n2 = SafeParcelReader.readSize(parcel, n);
        if (n2 == 0) {
            return null;
        }
        SafeParcelReader.zza(parcel, n, n2, 8);
        return parcel.readDouble();
    }

    public static float readFloat(Parcel parcel, int n) {
        SafeParcelReader.zzb(parcel, n, 4);
        return parcel.readFloat();
    }

    public static Float readFloatObject(Parcel parcel, int n) {
        int n2 = SafeParcelReader.readSize(parcel, n);
        if (n2 == 0) {
            return null;
        }
        SafeParcelReader.zza(parcel, n, n2, 4);
        return Float.valueOf(parcel.readFloat());
    }

    public static int readHeader(Parcel parcel) {
        return parcel.readInt();
    }

    public static IBinder readIBinder(Parcel parcel, int n) {
        int n2 = SafeParcelReader.readSize(parcel, n);
        n = parcel.dataPosition();
        if (n2 == 0) {
            return null;
        }
        IBinder iBinder = parcel.readStrongBinder();
        parcel.setDataPosition(n + n2);
        return iBinder;
    }

    public static int readInt(Parcel parcel, int n) {
        SafeParcelReader.zzb(parcel, n, 4);
        return parcel.readInt();
    }

    public static Integer readIntegerObject(Parcel parcel, int n) {
        int n2 = SafeParcelReader.readSize(parcel, n);
        if (n2 == 0) {
            return null;
        }
        SafeParcelReader.zza(parcel, n, n2, 4);
        return parcel.readInt();
    }

    public static void readList(Parcel parcel, int n, List list, ClassLoader classLoader) {
        int n2 = SafeParcelReader.readSize(parcel, n);
        n = parcel.dataPosition();
        if (n2 == 0) {
            return;
        }
        parcel.readList(list, classLoader);
        parcel.setDataPosition(n + n2);
    }

    public static long readLong(Parcel parcel, int n) {
        SafeParcelReader.zzb(parcel, n, 8);
        return parcel.readLong();
    }

    public static Long readLongObject(Parcel parcel, int n) {
        int n2 = SafeParcelReader.readSize(parcel, n);
        if (n2 == 0) {
            return null;
        }
        SafeParcelReader.zza(parcel, n, n2, 8);
        return parcel.readLong();
    }

    public static PendingIntent readPendingIntent(Parcel parcel, int n) {
        n = SafeParcelReader.readSize(parcel, n);
        int n2 = parcel.dataPosition();
        if (n == 0) {
            return null;
        }
        PendingIntent pendingIntent = PendingIntent.readPendingIntentOrNullFromParcel((Parcel)parcel);
        parcel.setDataPosition(n2 + n);
        return pendingIntent;
    }

    public static short readShort(Parcel parcel, int n) {
        SafeParcelReader.zzb(parcel, n, 4);
        return (short)parcel.readInt();
    }

    public static int readSize(Parcel parcel, int n) {
        if ((n & 0xFFFF0000) != -65536) {
            return (char)(n >> 16);
        }
        return parcel.readInt();
    }

    public static void skipUnknownField(Parcel parcel, int n) {
        n = SafeParcelReader.readSize(parcel, n);
        parcel.setDataPosition(parcel.dataPosition() + n);
    }

    public static int validateObjectHeader(Parcel parcel) {
        int n = SafeParcelReader.readHeader(parcel);
        int n2 = SafeParcelReader.readSize(parcel, n);
        int n3 = parcel.dataPosition();
        if (SafeParcelReader.getFieldId(n) != 20293) {
            String string2 = String.valueOf(Integer.toHexString(n));
            string2 = string2.length() != 0 ? "Expected object header. Got 0x".concat(string2) : new String("Expected object header. Got 0x");
            throw new ParseException(string2, parcel);
        }
        n = n2 + n3;
        if (n >= n3 && n <= parcel.dataSize()) {
            return n;
        }
        StringBuilder stringBuilder = new StringBuilder(54);
        stringBuilder.append("Size read is invalid start=");
        stringBuilder.append(n3);
        stringBuilder.append(" end=");
        stringBuilder.append(n);
        throw new ParseException(stringBuilder.toString(), parcel);
    }

    private static void zza(Parcel parcel, int n, int n2, int n3) {
        if (n2 == n3) {
            return;
        }
        String string2 = Integer.toHexString(n2);
        StringBuilder stringBuilder = new StringBuilder(String.valueOf(string2).length() + 46);
        stringBuilder.append("Expected size ");
        stringBuilder.append(n3);
        stringBuilder.append(" got ");
        stringBuilder.append(n2);
        stringBuilder.append(" (0x");
        stringBuilder.append(string2);
        stringBuilder.append(")");
        throw new ParseException(stringBuilder.toString(), parcel);
    }

    private static void zzb(Parcel parcel, int n, int n2) {
        if ((n = SafeParcelReader.readSize(parcel, n)) == n2) {
            return;
        }
        String string2 = Integer.toHexString(n);
        StringBuilder stringBuilder = new StringBuilder(String.valueOf(string2).length() + 46);
        stringBuilder.append("Expected size ");
        stringBuilder.append(n2);
        stringBuilder.append(" got ");
        stringBuilder.append(n);
        stringBuilder.append(" (0x");
        stringBuilder.append(string2);
        stringBuilder.append(")");
        throw new ParseException(stringBuilder.toString(), parcel);
    }

    public static class ParseException
    extends RuntimeException {
        public ParseException(String string2, Parcel object) {
            int n = object.dataPosition();
            int n2 = object.dataSize();
            object = new StringBuilder(String.valueOf(string2).length() + 41);
            ((StringBuilder)object).append(string2);
            ((StringBuilder)object).append(" Parcel: pos=");
            ((StringBuilder)object).append(n);
            ((StringBuilder)object).append(" size=");
            ((StringBuilder)object).append(n2);
            super(((StringBuilder)object).toString());
        }
    }
}

