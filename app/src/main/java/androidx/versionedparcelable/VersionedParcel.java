/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.BadParcelableException
 *  android.os.Bundle
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.NetworkOnMainThreadException
 *  android.os.Parcelable
 *  android.util.Size
 *  android.util.SizeF
 *  android.util.SparseBooleanArray
 */
package androidx.versionedparcelable;

import android.os.BadParcelableException;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.NetworkOnMainThreadException;
import android.os.Parcelable;
import android.util.Size;
import android.util.SizeF;
import android.util.SparseBooleanArray;
import androidx.collection.ArrayMap;
import androidx.collection.ArraySet;
import androidx.versionedparcelable.VersionedParcelable;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class VersionedParcel {
    private static final int EX_BAD_PARCELABLE = -2;
    private static final int EX_ILLEGAL_ARGUMENT = -3;
    private static final int EX_ILLEGAL_STATE = -5;
    private static final int EX_NETWORK_MAIN_THREAD = -6;
    private static final int EX_NULL_POINTER = -4;
    private static final int EX_PARCELABLE = -9;
    private static final int EX_SECURITY = -1;
    private static final int EX_UNSUPPORTED_OPERATION = -7;
    private static final String TAG = "VersionedParcel";
    private static final int TYPE_BINDER = 5;
    private static final int TYPE_FLOAT = 8;
    private static final int TYPE_INTEGER = 7;
    private static final int TYPE_PARCELABLE = 2;
    private static final int TYPE_SERIALIZABLE = 3;
    private static final int TYPE_STRING = 4;
    private static final int TYPE_VERSIONED_PARCELABLE = 1;
    protected final ArrayMap<String, Class> mParcelizerCache;
    protected final ArrayMap<String, Method> mReadCache;
    protected final ArrayMap<String, Method> mWriteCache;

    public VersionedParcel(ArrayMap<String, Method> arrayMap, ArrayMap<String, Method> arrayMap2, ArrayMap<String, Class> arrayMap3) {
        this.mReadCache = arrayMap;
        this.mWriteCache = arrayMap2;
        this.mParcelizerCache = arrayMap3;
    }

    private Exception createException(int n, String string2) {
        switch (n) {
            default: {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unknown exception code: ");
                stringBuilder.append(n);
                stringBuilder.append(" msg ");
                stringBuilder.append(string2);
                return new RuntimeException(stringBuilder.toString());
            }
            case -1: {
                return new SecurityException(string2);
            }
            case -2: {
                return new BadParcelableException(string2);
            }
            case -3: {
                return new IllegalArgumentException(string2);
            }
            case -4: {
                return new NullPointerException(string2);
            }
            case -5: {
                return new IllegalStateException(string2);
            }
            case -6: {
                return new NetworkOnMainThreadException();
            }
            case -7: {
                return new UnsupportedOperationException(string2);
            }
            case -9: 
        }
        return (Exception)this.readParcelable();
    }

    private Class findParcelClass(Class<? extends VersionedParcelable> clazz) throws ClassNotFoundException {
        Class<?> clazz2;
        Class<?> clazz3 = clazz2 = (Class<?>)this.mParcelizerCache.get(clazz.getName());
        if (clazz2 == null) {
            clazz3 = Class.forName(String.format("%s.%sParcelizer", clazz.getPackage().getName(), clazz.getSimpleName()), false, clazz.getClassLoader());
            this.mParcelizerCache.put(clazz.getName(), clazz3);
        }
        return clazz3;
    }

    private Method getReadMethod(String string2) throws IllegalAccessException, NoSuchMethodException, ClassNotFoundException {
        Method method;
        Method method2 = method = (Method)this.mReadCache.get(string2);
        if (method == null) {
            System.currentTimeMillis();
            method2 = Class.forName(string2, true, VersionedParcel.class.getClassLoader()).getDeclaredMethod("read", VersionedParcel.class);
            this.mReadCache.put(string2, method2);
        }
        return method2;
    }

    protected static Throwable getRootCause(Throwable throwable) {
        while (throwable.getCause() != null) {
            throwable = throwable.getCause();
        }
        return throwable;
    }

    private <T> int getType(T t) {
        if (t instanceof String) {
            return 4;
        }
        if (t instanceof Parcelable) {
            return 2;
        }
        if (t instanceof VersionedParcelable) {
            return 1;
        }
        if (t instanceof Serializable) {
            return 3;
        }
        if (t instanceof IBinder) {
            return 5;
        }
        if (t instanceof Integer) {
            return 7;
        }
        if (t instanceof Float) {
            return 8;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(t.getClass().getName());
        stringBuilder.append(" cannot be VersionedParcelled");
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    private Method getWriteMethod(Class clazz) throws IllegalAccessException, NoSuchMethodException, ClassNotFoundException {
        Method method = (Method)this.mWriteCache.get(clazz.getName());
        GenericDeclaration genericDeclaration = method;
        if (method == null) {
            genericDeclaration = this.findParcelClass(clazz);
            System.currentTimeMillis();
            genericDeclaration = ((Class)genericDeclaration).getDeclaredMethod("write", clazz, VersionedParcel.class);
            this.mWriteCache.put(clazz.getName(), (Method)genericDeclaration);
        }
        return genericDeclaration;
    }

    private <T, S extends Collection<T>> S readCollection(S s) {
        int n = this.readInt();
        if (n < 0) {
            return null;
        }
        if (n != 0) {
            int n2 = this.readInt();
            if (n < 0) {
                return null;
            }
            switch (n2) {
                default: {
                    break;
                }
                case 5: {
                    for (int i = n; i > 0; --i) {
                        s.add((IBinder)this.readStrongBinder());
                    }
                    break;
                }
                case 4: {
                    for (int i = n; i > 0; --i) {
                        s.add((String)this.readString());
                    }
                    break;
                }
                case 3: {
                    for (int i = n; i > 0; --i) {
                        s.add((Serializable)this.readSerializable());
                    }
                    break;
                }
                case 2: {
                    for (int i = n; i > 0; --i) {
                        s.add(this.readParcelable());
                    }
                    break;
                }
                case 1: {
                    while (n > 0) {
                        s.add(this.readVersionedParcelable());
                        --n;
                    }
                    break block0;
                }
            }
        }
        return s;
    }

    private Exception readException(int n, String string2) {
        return this.createException(n, string2);
    }

    private int readExceptionCode() {
        return this.readInt();
    }

    private <T> void writeCollection(Collection<T> iterator2) {
        if (iterator2 == null) {
            this.writeInt(-1);
            return;
        }
        int n = iterator2.size();
        this.writeInt(n);
        if (n > 0) {
            n = this.getType(iterator2.iterator().next());
            this.writeInt(n);
            switch (n) {
                default: {
                    break;
                }
                case 8: {
                    iterator2 = iterator2.iterator();
                    while (iterator2.hasNext()) {
                        this.writeFloat(((Float)iterator2.next()).floatValue());
                    }
                    break;
                }
                case 7: {
                    iterator2 = iterator2.iterator();
                    while (iterator2.hasNext()) {
                        this.writeInt((Integer)iterator2.next());
                    }
                    break;
                }
                case 5: {
                    iterator2 = iterator2.iterator();
                    while (iterator2.hasNext()) {
                        this.writeStrongBinder((IBinder)iterator2.next());
                    }
                    break;
                }
                case 4: {
                    iterator2 = iterator2.iterator();
                    while (iterator2.hasNext()) {
                        this.writeString((String)iterator2.next());
                    }
                    break;
                }
                case 3: {
                    iterator2 = iterator2.iterator();
                    while (iterator2.hasNext()) {
                        this.writeSerializable((Serializable)iterator2.next());
                    }
                    break;
                }
                case 2: {
                    iterator2 = iterator2.iterator();
                    while (iterator2.hasNext()) {
                        this.writeParcelable((Parcelable)iterator2.next());
                    }
                    break;
                }
                case 1: {
                    iterator2 = iterator2.iterator();
                    while (iterator2.hasNext()) {
                        this.writeVersionedParcelable((VersionedParcelable)iterator2.next());
                    }
                    break block0;
                }
            }
        }
    }

    private <T> void writeCollection(Collection<T> collection, int n) {
        this.setOutputField(n);
        this.writeCollection(collection);
    }

    private void writeSerializable(Serializable serializable) {
        if (serializable == null) {
            this.writeString(null);
            return;
        }
        String string2 = serializable.getClass().getName();
        this.writeString(string2);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(serializable);
            objectOutputStream.close();
            this.writeByteArray(byteArrayOutputStream.toByteArray());
            return;
        }
        catch (IOException iOException) {
            serializable = new StringBuilder();
            ((StringBuilder)serializable).append("VersionedParcelable encountered IOException writing serializable object (name = ");
            ((StringBuilder)serializable).append(string2);
            ((StringBuilder)serializable).append(")");
            throw new RuntimeException(((StringBuilder)serializable).toString(), iOException);
        }
    }

    private void writeVersionedParcelableCreator(VersionedParcelable versionedParcelable) {
        try {
            Class clazz = this.findParcelClass(versionedParcelable.getClass());
            this.writeString(clazz.getName());
            return;
        }
        catch (ClassNotFoundException classNotFoundException) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(versionedParcelable.getClass().getSimpleName());
            stringBuilder.append(" does not have a Parcelizer");
            throw new RuntimeException(stringBuilder.toString(), classNotFoundException);
        }
    }

    protected abstract void closeField();

    protected abstract VersionedParcel createSubParcel();

    public boolean isStream() {
        return false;
    }

    protected <T> T[] readArray(T[] TArray) {
        int n = this.readInt();
        if (n < 0) {
            return null;
        }
        ArrayList<Object> arrayList = new ArrayList<Object>(n);
        if (n != 0) {
            int n2 = this.readInt();
            if (n < 0) {
                return null;
            }
            switch (n2) {
                default: {
                    break;
                }
                case 5: {
                    for (int i = n; i > 0; --i) {
                        arrayList.add(this.readStrongBinder());
                    }
                    break;
                }
                case 4: {
                    for (int i = n; i > 0; --i) {
                        arrayList.add(this.readString());
                    }
                    break;
                }
                case 3: {
                    for (int i = n; i > 0; --i) {
                        arrayList.add(this.readSerializable());
                    }
                    break;
                }
                case 2: {
                    for (int i = n; i > 0; --i) {
                        arrayList.add(this.readParcelable());
                    }
                    break;
                }
                case 1: {
                    while (n > 0) {
                        arrayList.add(this.readVersionedParcelable());
                        --n;
                    }
                    break block0;
                }
            }
        }
        return arrayList.toArray(TArray);
    }

    public <T> T[] readArray(T[] TArray, int n) {
        if (!this.readField(n)) {
            return TArray;
        }
        return this.readArray(TArray);
    }

    protected abstract boolean readBoolean();

    public boolean readBoolean(boolean bl, int n) {
        if (!this.readField(n)) {
            return bl;
        }
        return this.readBoolean();
    }

    protected boolean[] readBooleanArray() {
        int n = this.readInt();
        if (n < 0) {
            return null;
        }
        boolean[] blArray = new boolean[n];
        for (int i = 0; i < n; ++i) {
            boolean bl = this.readInt() != 0;
            blArray[i] = bl;
        }
        return blArray;
    }

    public boolean[] readBooleanArray(boolean[] blArray, int n) {
        if (!this.readField(n)) {
            return blArray;
        }
        return this.readBooleanArray();
    }

    protected abstract Bundle readBundle();

    public Bundle readBundle(Bundle bundle, int n) {
        if (!this.readField(n)) {
            return bundle;
        }
        return this.readBundle();
    }

    public byte readByte(byte by, int n) {
        if (!this.readField(n)) {
            return by;
        }
        return (byte)(this.readInt() & 0xFF);
    }

    protected abstract byte[] readByteArray();

    public byte[] readByteArray(byte[] byArray, int n) {
        if (!this.readField(n)) {
            return byArray;
        }
        return this.readByteArray();
    }

    public char[] readCharArray(char[] cArray, int n) {
        if (!this.readField(n)) {
            return cArray;
        }
        int n2 = this.readInt();
        if (n2 < 0) {
            return null;
        }
        cArray = new char[n2];
        for (n = 0; n < n2; ++n) {
            cArray[n] = (char)this.readInt();
        }
        return cArray;
    }

    protected abstract CharSequence readCharSequence();

    public CharSequence readCharSequence(CharSequence charSequence, int n) {
        if (!this.readField(n)) {
            return charSequence;
        }
        return this.readCharSequence();
    }

    protected abstract double readDouble();

    public double readDouble(double d, int n) {
        if (!this.readField(n)) {
            return d;
        }
        return this.readDouble();
    }

    protected double[] readDoubleArray() {
        int n = this.readInt();
        if (n < 0) {
            return null;
        }
        double[] dArray = new double[n];
        for (int i = 0; i < n; ++i) {
            dArray[i] = this.readDouble();
        }
        return dArray;
    }

    public double[] readDoubleArray(double[] dArray, int n) {
        if (!this.readField(n)) {
            return dArray;
        }
        return this.readDoubleArray();
    }

    public Exception readException(Exception exception, int n) {
        if (!this.readField(n)) {
            return exception;
        }
        n = this.readExceptionCode();
        if (n != 0) {
            return this.readException(n, this.readString());
        }
        return exception;
    }

    protected abstract boolean readField(int var1);

    protected abstract float readFloat();

    public float readFloat(float f, int n) {
        if (!this.readField(n)) {
            return f;
        }
        return this.readFloat();
    }

    protected float[] readFloatArray() {
        int n = this.readInt();
        if (n < 0) {
            return null;
        }
        float[] fArray = new float[n];
        for (int i = 0; i < n; ++i) {
            fArray[i] = this.readFloat();
        }
        return fArray;
    }

    public float[] readFloatArray(float[] fArray, int n) {
        if (!this.readField(n)) {
            return fArray;
        }
        return this.readFloatArray();
    }

    protected <T extends VersionedParcelable> T readFromParcel(String object, VersionedParcel versionedParcel) {
        try {
            object = (VersionedParcelable)this.getReadMethod((String)object).invoke(null, versionedParcel);
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new RuntimeException("VersionedParcel encountered ClassNotFoundException", classNotFoundException);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            throw new RuntimeException("VersionedParcel encountered NoSuchMethodException", noSuchMethodException);
        }
        catch (InvocationTargetException invocationTargetException) {
            if (invocationTargetException.getCause() instanceof RuntimeException) {
                throw (RuntimeException)invocationTargetException.getCause();
            }
            throw new RuntimeException("VersionedParcel encountered InvocationTargetException", invocationTargetException);
        }
        catch (IllegalAccessException illegalAccessException) {
            throw new RuntimeException("VersionedParcel encountered IllegalAccessException", illegalAccessException);
        }
        return (T)object;
    }

    protected abstract int readInt();

    public int readInt(int n, int n2) {
        if (!this.readField(n2)) {
            return n;
        }
        return this.readInt();
    }

    protected int[] readIntArray() {
        int n = this.readInt();
        if (n < 0) {
            return null;
        }
        int[] nArray = new int[n];
        for (int i = 0; i < n; ++i) {
            nArray[i] = this.readInt();
        }
        return nArray;
    }

    public int[] readIntArray(int[] nArray, int n) {
        if (!this.readField(n)) {
            return nArray;
        }
        return this.readIntArray();
    }

    public <T> List<T> readList(List<T> list, int n) {
        if (!this.readField(n)) {
            return list;
        }
        return this.readCollection(new ArrayList());
    }

    protected abstract long readLong();

    public long readLong(long l, int n) {
        if (!this.readField(n)) {
            return l;
        }
        return this.readLong();
    }

    protected long[] readLongArray() {
        int n = this.readInt();
        if (n < 0) {
            return null;
        }
        long[] lArray = new long[n];
        for (int i = 0; i < n; ++i) {
            lArray[i] = this.readLong();
        }
        return lArray;
    }

    public long[] readLongArray(long[] lArray, int n) {
        if (!this.readField(n)) {
            return lArray;
        }
        return this.readLongArray();
    }

    public <K, V> Map<K, V> readMap(Map<K, V> object, int n) {
        if (!this.readField(n)) {
            return object;
        }
        int n2 = this.readInt();
        if (n2 < 0) {
            return null;
        }
        ArrayMap arrayMap = new ArrayMap();
        if (n2 == 0) {
            return arrayMap;
        }
        object = new ArrayList();
        ArrayList arrayList = new ArrayList();
        this.readCollection(object);
        this.readCollection(arrayList);
        for (n = 0; n < n2; ++n) {
            arrayMap.put(object.get(n), arrayList.get(n));
        }
        return arrayMap;
    }

    protected abstract <T extends Parcelable> T readParcelable();

    public <T extends Parcelable> T readParcelable(T t, int n) {
        if (!this.readField(n)) {
            return t;
        }
        return this.readParcelable();
    }

    protected Serializable readSerializable() {
        String string2 = this.readString();
        if (string2 == null) {
            return null;
        }
        Object object = new ByteArrayInputStream(this.readByteArray());
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(this, (InputStream)object){
                final VersionedParcel this$0;
                {
                    this.this$0 = versionedParcel;
                    super(inputStream);
                }

                @Override
                protected Class<?> resolveClass(ObjectStreamClass objectStreamClass) throws IOException, ClassNotFoundException {
                    Class<?> clazz = Class.forName(objectStreamClass.getName(), false, this.getClass().getClassLoader());
                    if (clazz != null) {
                        return clazz;
                    }
                    return super.resolveClass(objectStreamClass);
                }
            };
            object = (Serializable)objectInputStream.readObject();
            return object;
        }
        catch (ClassNotFoundException classNotFoundException) {
            object = new StringBuilder();
            ((StringBuilder)object).append("VersionedParcelable encountered ClassNotFoundException reading a Serializable object (name = ");
            ((StringBuilder)object).append(string2);
            ((StringBuilder)object).append(")");
            throw new RuntimeException(((StringBuilder)object).toString(), classNotFoundException);
        }
        catch (IOException iOException) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("VersionedParcelable encountered IOException reading a Serializable object (name = ");
            stringBuilder.append(string2);
            stringBuilder.append(")");
            throw new RuntimeException(stringBuilder.toString(), iOException);
        }
    }

    public <T> Set<T> readSet(Set<T> set, int n) {
        if (!this.readField(n)) {
            return set;
        }
        return this.readCollection(new ArraySet());
    }

    public Size readSize(Size size, int n) {
        if (!this.readField(n)) {
            return size;
        }
        if (this.readBoolean()) {
            return new Size(this.readInt(), this.readInt());
        }
        return null;
    }

    public SizeF readSizeF(SizeF sizeF, int n) {
        if (!this.readField(n)) {
            return sizeF;
        }
        if (this.readBoolean()) {
            return new SizeF(this.readFloat(), this.readFloat());
        }
        return null;
    }

    public SparseBooleanArray readSparseBooleanArray(SparseBooleanArray sparseBooleanArray, int n) {
        if (!this.readField(n)) {
            return sparseBooleanArray;
        }
        int n2 = this.readInt();
        if (n2 < 0) {
            return null;
        }
        sparseBooleanArray = new SparseBooleanArray(n2);
        for (n = 0; n < n2; ++n) {
            sparseBooleanArray.put(this.readInt(), this.readBoolean());
        }
        return sparseBooleanArray;
    }

    protected abstract String readString();

    public String readString(String string2, int n) {
        if (!this.readField(n)) {
            return string2;
        }
        return this.readString();
    }

    protected abstract IBinder readStrongBinder();

    public IBinder readStrongBinder(IBinder iBinder, int n) {
        if (!this.readField(n)) {
            return iBinder;
        }
        return this.readStrongBinder();
    }

    protected <T extends VersionedParcelable> T readVersionedParcelable() {
        String string2 = this.readString();
        if (string2 == null) {
            return null;
        }
        return this.readFromParcel(string2, this.createSubParcel());
    }

    public <T extends VersionedParcelable> T readVersionedParcelable(T t, int n) {
        if (!this.readField(n)) {
            return t;
        }
        return this.readVersionedParcelable();
    }

    protected abstract void setOutputField(int var1);

    public void setSerializationFlags(boolean bl, boolean bl2) {
    }

    protected <T> void writeArray(T[] TArray) {
        if (TArray == null) {
            this.writeInt(-1);
            return;
        }
        int n = TArray.length;
        this.writeInt(n);
        if (n > 0) {
            int n2 = this.getType(TArray[0]);
            this.writeInt(n2);
            switch (n2) {
                default: {
                    break;
                }
                case 5: {
                    for (int i = 0; i < n; ++i) {
                        this.writeStrongBinder((IBinder)TArray[i]);
                    }
                    break;
                }
                case 4: {
                    for (int i = 0; i < n; ++i) {
                        this.writeString((String)TArray[i]);
                    }
                    break;
                }
                case 3: {
                    for (int i = 0; i < n; ++i) {
                        this.writeSerializable((Serializable)TArray[i]);
                    }
                    break;
                }
                case 2: {
                    for (int i = 0; i < n; ++i) {
                        this.writeParcelable((Parcelable)TArray[i]);
                    }
                    break;
                }
                case 1: {
                    for (int i = 0; i < n; ++i) {
                        this.writeVersionedParcelable((VersionedParcelable)TArray[i]);
                    }
                }
            }
        }
    }

    public <T> void writeArray(T[] TArray, int n) {
        this.setOutputField(n);
        this.writeArray(TArray);
    }

    protected abstract void writeBoolean(boolean var1);

    public void writeBoolean(boolean bl, int n) {
        this.setOutputField(n);
        this.writeBoolean(bl);
    }

    protected void writeBooleanArray(boolean[] blArray) {
        if (blArray != null) {
            int n = blArray.length;
            this.writeInt(n);
            for (int i = 0; i < n; ++i) {
                this.writeInt(blArray[i]);
            }
        } else {
            this.writeInt(-1);
        }
    }

    public void writeBooleanArray(boolean[] blArray, int n) {
        this.setOutputField(n);
        this.writeBooleanArray(blArray);
    }

    protected abstract void writeBundle(Bundle var1);

    public void writeBundle(Bundle bundle, int n) {
        this.setOutputField(n);
        this.writeBundle(bundle);
    }

    public void writeByte(byte by, int n) {
        this.setOutputField(n);
        this.writeInt(by);
    }

    protected abstract void writeByteArray(byte[] var1);

    public void writeByteArray(byte[] byArray, int n) {
        this.setOutputField(n);
        this.writeByteArray(byArray);
    }

    protected abstract void writeByteArray(byte[] var1, int var2, int var3);

    public void writeByteArray(byte[] byArray, int n, int n2, int n3) {
        this.setOutputField(n3);
        this.writeByteArray(byArray, n, n2);
    }

    public void writeCharArray(char[] cArray, int n) {
        this.setOutputField(n);
        if (cArray != null) {
            int n2 = cArray.length;
            this.writeInt(n2);
            for (n = 0; n < n2; ++n) {
                this.writeInt(cArray[n]);
            }
        } else {
            this.writeInt(-1);
        }
    }

    protected abstract void writeCharSequence(CharSequence var1);

    public void writeCharSequence(CharSequence charSequence, int n) {
        this.setOutputField(n);
        this.writeCharSequence(charSequence);
    }

    protected abstract void writeDouble(double var1);

    public void writeDouble(double d, int n) {
        this.setOutputField(n);
        this.writeDouble(d);
    }

    protected void writeDoubleArray(double[] dArray) {
        if (dArray != null) {
            int n = dArray.length;
            this.writeInt(n);
            for (int i = 0; i < n; ++i) {
                this.writeDouble(dArray[i]);
            }
        } else {
            this.writeInt(-1);
        }
    }

    public void writeDoubleArray(double[] dArray, int n) {
        this.setOutputField(n);
        this.writeDoubleArray(dArray);
    }

    public void writeException(Exception exception, int n) {
        this.setOutputField(n);
        if (exception == null) {
            this.writeNoException();
            return;
        }
        n = 0;
        if (exception instanceof Parcelable && exception.getClass().getClassLoader() == Parcelable.class.getClassLoader()) {
            n = -9;
        } else if (exception instanceof SecurityException) {
            n = -1;
        } else if (exception instanceof BadParcelableException) {
            n = -2;
        } else if (exception instanceof IllegalArgumentException) {
            n = -3;
        } else if (exception instanceof NullPointerException) {
            n = -4;
        } else if (exception instanceof IllegalStateException) {
            n = -5;
        } else if (exception instanceof NetworkOnMainThreadException) {
            n = -6;
        } else if (exception instanceof UnsupportedOperationException) {
            n = -7;
        }
        this.writeInt(n);
        if (n == 0) {
            if (exception instanceof RuntimeException) {
                throw (RuntimeException)exception;
            }
            throw new RuntimeException(exception);
        }
        this.writeString(exception.getMessage());
        switch (n) {
            default: {
                break;
            }
            case -9: {
                this.writeParcelable((Parcelable)exception);
            }
        }
    }

    protected abstract void writeFloat(float var1);

    public void writeFloat(float f, int n) {
        this.setOutputField(n);
        this.writeFloat(f);
    }

    protected void writeFloatArray(float[] fArray) {
        if (fArray != null) {
            int n = fArray.length;
            this.writeInt(n);
            for (int i = 0; i < n; ++i) {
                this.writeFloat(fArray[i]);
            }
        } else {
            this.writeInt(-1);
        }
    }

    public void writeFloatArray(float[] fArray, int n) {
        this.setOutputField(n);
        this.writeFloatArray(fArray);
    }

    protected abstract void writeInt(int var1);

    public void writeInt(int n, int n2) {
        this.setOutputField(n2);
        this.writeInt(n);
    }

    protected void writeIntArray(int[] nArray) {
        if (nArray != null) {
            int n = nArray.length;
            this.writeInt(n);
            for (int i = 0; i < n; ++i) {
                this.writeInt(nArray[i]);
            }
        } else {
            this.writeInt(-1);
        }
    }

    public void writeIntArray(int[] nArray, int n) {
        this.setOutputField(n);
        this.writeIntArray(nArray);
    }

    public <T> void writeList(List<T> list, int n) {
        this.writeCollection(list, n);
    }

    protected abstract void writeLong(long var1);

    public void writeLong(long l, int n) {
        this.setOutputField(n);
        this.writeLong(l);
    }

    protected void writeLongArray(long[] lArray) {
        if (lArray != null) {
            int n = lArray.length;
            this.writeInt(n);
            for (int i = 0; i < n; ++i) {
                this.writeLong(lArray[i]);
            }
        } else {
            this.writeInt(-1);
        }
    }

    public void writeLongArray(long[] lArray, int n) {
        this.setOutputField(n);
        this.writeLongArray(lArray);
    }

    public <K, V> void writeMap(Map<K, V> object2, int n) {
        int n2;
        this.setOutputField(n2);
        if (object2 == null) {
            this.writeInt(-1);
            return;
        }
        n2 = object2.size();
        this.writeInt(n2);
        if (n2 == 0) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (Map.Entry entry : object2.entrySet()) {
            arrayList.add(entry.getKey());
            arrayList2.add(entry.getValue());
        }
        this.writeCollection(arrayList);
        this.writeCollection(arrayList2);
    }

    protected void writeNoException() {
        this.writeInt(0);
    }

    protected abstract void writeParcelable(Parcelable var1);

    public void writeParcelable(Parcelable parcelable, int n) {
        this.setOutputField(n);
        this.writeParcelable(parcelable);
    }

    public void writeSerializable(Serializable serializable, int n) {
        this.setOutputField(n);
        this.writeSerializable(serializable);
    }

    public <T> void writeSet(Set<T> set, int n) {
        this.writeCollection(set, n);
    }

    public void writeSize(Size size, int n) {
        this.setOutputField(n);
        boolean bl = size != null;
        this.writeBoolean(bl);
        if (size != null) {
            this.writeInt(size.getWidth());
            this.writeInt(size.getHeight());
        }
    }

    public void writeSizeF(SizeF sizeF, int n) {
        this.setOutputField(n);
        boolean bl = sizeF != null;
        this.writeBoolean(bl);
        if (sizeF != null) {
            this.writeFloat(sizeF.getWidth());
            this.writeFloat(sizeF.getHeight());
        }
    }

    public void writeSparseBooleanArray(SparseBooleanArray sparseBooleanArray, int n) {
        this.setOutputField(n);
        if (sparseBooleanArray == null) {
            this.writeInt(-1);
            return;
        }
        int n2 = sparseBooleanArray.size();
        this.writeInt(n2);
        for (n = 0; n < n2; ++n) {
            this.writeInt(sparseBooleanArray.keyAt(n));
            this.writeBoolean(sparseBooleanArray.valueAt(n));
        }
    }

    protected abstract void writeString(String var1);

    public void writeString(String string2, int n) {
        this.setOutputField(n);
        this.writeString(string2);
    }

    protected abstract void writeStrongBinder(IBinder var1);

    public void writeStrongBinder(IBinder iBinder, int n) {
        this.setOutputField(n);
        this.writeStrongBinder(iBinder);
    }

    protected abstract void writeStrongInterface(IInterface var1);

    public void writeStrongInterface(IInterface iInterface, int n) {
        this.setOutputField(n);
        this.writeStrongInterface(iInterface);
    }

    protected <T extends VersionedParcelable> void writeToParcel(T t, VersionedParcel versionedParcel) {
        try {
            this.getWriteMethod(t.getClass()).invoke(null, t, versionedParcel);
            return;
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new RuntimeException("VersionedParcel encountered ClassNotFoundException", classNotFoundException);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            throw new RuntimeException("VersionedParcel encountered NoSuchMethodException", noSuchMethodException);
        }
        catch (InvocationTargetException invocationTargetException) {
            if (invocationTargetException.getCause() instanceof RuntimeException) {
                throw (RuntimeException)invocationTargetException.getCause();
            }
            throw new RuntimeException("VersionedParcel encountered InvocationTargetException", invocationTargetException);
        }
        catch (IllegalAccessException illegalAccessException) {
            throw new RuntimeException("VersionedParcel encountered IllegalAccessException", illegalAccessException);
        }
    }

    protected void writeVersionedParcelable(VersionedParcelable versionedParcelable) {
        if (versionedParcelable == null) {
            this.writeString(null);
            return;
        }
        this.writeVersionedParcelableCreator(versionedParcelable);
        VersionedParcel versionedParcel = this.createSubParcel();
        this.writeToParcel(versionedParcelable, versionedParcel);
        versionedParcel.closeField();
    }

    public void writeVersionedParcelable(VersionedParcelable versionedParcelable, int n) {
        this.setOutputField(n);
        this.writeVersionedParcelable(versionedParcelable);
    }

    public static class ParcelException
    extends RuntimeException {
        public ParcelException(Throwable throwable) {
            super(throwable);
        }
    }
}

