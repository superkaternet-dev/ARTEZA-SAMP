/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.Parcelable
 */
package androidx.versionedparcelable;

import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcelable;
import androidx.collection.ArrayMap;
import androidx.versionedparcelable.VersionedParcel;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.nio.charset.Charset;

class VersionedParcelStream
extends VersionedParcel {
    private static final int TYPE_BOOLEAN = 5;
    private static final int TYPE_BOOLEAN_ARRAY = 6;
    private static final int TYPE_DOUBLE = 7;
    private static final int TYPE_DOUBLE_ARRAY = 8;
    private static final int TYPE_FLOAT = 13;
    private static final int TYPE_FLOAT_ARRAY = 14;
    private static final int TYPE_INT = 9;
    private static final int TYPE_INT_ARRAY = 10;
    private static final int TYPE_LONG = 11;
    private static final int TYPE_LONG_ARRAY = 12;
    private static final int TYPE_NULL = 0;
    private static final int TYPE_STRING = 3;
    private static final int TYPE_STRING_ARRAY = 4;
    private static final int TYPE_SUB_BUNDLE = 1;
    private static final int TYPE_SUB_PERSISTABLE_BUNDLE = 2;
    private static final Charset UTF_16 = Charset.forName("UTF-16");
    int mCount = 0;
    private DataInputStream mCurrentInput;
    private DataOutputStream mCurrentOutput;
    private FieldBuffer mFieldBuffer;
    private int mFieldId = -1;
    int mFieldSize = -1;
    private boolean mIgnoreParcelables;
    private final DataInputStream mMasterInput;
    private final DataOutputStream mMasterOutput;

    public VersionedParcelStream(InputStream inputStream, OutputStream outputStream) {
        this(inputStream, outputStream, new ArrayMap<String, Method>(), new ArrayMap<String, Method>(), new ArrayMap<String, Class>());
    }

    private VersionedParcelStream(InputStream inputStream, OutputStream outputStream, ArrayMap<String, Method> object, ArrayMap<String, Method> arrayMap, ArrayMap<String, Class> arrayMap2) {
        super((ArrayMap<String, Method>)object, arrayMap, arrayMap2);
        object = null;
        inputStream = inputStream != null ? new DataInputStream(new FilterInputStream(this, inputStream){
            final VersionedParcelStream this$0;
            {
                this.this$0 = versionedParcelStream;
                super(inputStream);
            }

            @Override
            public int read() throws IOException {
                if (this.this$0.mFieldSize != -1 && this.this$0.mCount >= this.this$0.mFieldSize) {
                    throw new IOException();
                }
                int n = super.read();
                VersionedParcelStream versionedParcelStream = this.this$0;
                ++versionedParcelStream.mCount;
                return n;
            }

            @Override
            public int read(byte[] object, int n, int n2) throws IOException {
                if (this.this$0.mFieldSize != -1 && this.this$0.mCount >= this.this$0.mFieldSize) {
                    throw new IOException();
                }
                if ((n = super.read((byte[])object, n, n2)) > 0) {
                    object = this.this$0;
                    object.mCount += n;
                }
                return n;
            }

            @Override
            public long skip(long l) throws IOException {
                if (this.this$0.mFieldSize != -1 && this.this$0.mCount >= this.this$0.mFieldSize) {
                    throw new IOException();
                }
                if ((l = super.skip(l)) > 0L) {
                    VersionedParcelStream versionedParcelStream = this.this$0;
                    versionedParcelStream.mCount += (int)l;
                }
                return l;
            }
        }) : null;
        this.mMasterInput = inputStream;
        if (outputStream != null) {
            object = new DataOutputStream(outputStream);
        }
        this.mMasterOutput = object;
        this.mCurrentInput = inputStream;
        this.mCurrentOutput = object;
    }

    private void readObject(int n, String charSequence, Bundle bundle) {
        switch (n) {
            default: {
                charSequence = new StringBuilder();
                ((StringBuilder)charSequence).append("Unknown type ");
                ((StringBuilder)charSequence).append(n);
                throw new RuntimeException(((StringBuilder)charSequence).toString());
            }
            case 14: {
                bundle.putFloatArray((String)charSequence, this.readFloatArray());
                break;
            }
            case 13: {
                bundle.putFloat((String)charSequence, this.readFloat());
                break;
            }
            case 12: {
                bundle.putLongArray((String)charSequence, this.readLongArray());
                break;
            }
            case 11: {
                bundle.putLong((String)charSequence, this.readLong());
                break;
            }
            case 10: {
                bundle.putIntArray((String)charSequence, this.readIntArray());
                break;
            }
            case 9: {
                bundle.putInt((String)charSequence, this.readInt());
                break;
            }
            case 8: {
                bundle.putDoubleArray((String)charSequence, this.readDoubleArray());
                break;
            }
            case 7: {
                bundle.putDouble((String)charSequence, this.readDouble());
                break;
            }
            case 6: {
                bundle.putBooleanArray((String)charSequence, this.readBooleanArray());
                break;
            }
            case 5: {
                bundle.putBoolean((String)charSequence, this.readBoolean());
                break;
            }
            case 4: {
                bundle.putStringArray((String)charSequence, this.readArray(new String[0]));
                break;
            }
            case 3: {
                bundle.putString((String)charSequence, this.readString());
                break;
            }
            case 2: {
                bundle.putBundle((String)charSequence, this.readBundle());
                break;
            }
            case 1: {
                bundle.putBundle((String)charSequence, this.readBundle());
                break;
            }
            case 0: {
                bundle.putParcelable((String)charSequence, null);
            }
        }
    }

    private void writeObject(Object object) {
        block16: {
            block3: {
                block15: {
                    block14: {
                        block13: {
                            block12: {
                                block11: {
                                    block10: {
                                        block9: {
                                            block8: {
                                                block7: {
                                                    block6: {
                                                        block5: {
                                                            block4: {
                                                                block2: {
                                                                    if (object != null) break block2;
                                                                    this.writeInt(0);
                                                                    break block3;
                                                                }
                                                                if (!(object instanceof Bundle)) break block4;
                                                                this.writeInt(1);
                                                                this.writeBundle((Bundle)object);
                                                                break block3;
                                                            }
                                                            if (!(object instanceof String)) break block5;
                                                            this.writeInt(3);
                                                            this.writeString((String)object);
                                                            break block3;
                                                        }
                                                        if (!(object instanceof String[])) break block6;
                                                        this.writeInt(4);
                                                        this.writeArray((String[])object);
                                                        break block3;
                                                    }
                                                    if (!(object instanceof Boolean)) break block7;
                                                    this.writeInt(5);
                                                    this.writeBoolean((Boolean)object);
                                                    break block3;
                                                }
                                                if (!(object instanceof boolean[])) break block8;
                                                this.writeInt(6);
                                                this.writeBooleanArray((boolean[])object);
                                                break block3;
                                            }
                                            if (!(object instanceof Double)) break block9;
                                            this.writeInt(7);
                                            this.writeDouble((Double)object);
                                            break block3;
                                        }
                                        if (!(object instanceof double[])) break block10;
                                        this.writeInt(8);
                                        this.writeDoubleArray((double[])object);
                                        break block3;
                                    }
                                    if (!(object instanceof Integer)) break block11;
                                    this.writeInt(9);
                                    this.writeInt((Integer)object);
                                    break block3;
                                }
                                if (!(object instanceof int[])) break block12;
                                this.writeInt(10);
                                this.writeIntArray((int[])object);
                                break block3;
                            }
                            if (!(object instanceof Long)) break block13;
                            this.writeInt(11);
                            this.writeLong((Long)object);
                            break block3;
                        }
                        if (!(object instanceof long[])) break block14;
                        this.writeInt(12);
                        this.writeLongArray((long[])object);
                        break block3;
                    }
                    if (!(object instanceof Float)) break block15;
                    this.writeInt(13);
                    this.writeFloat(((Float)object).floatValue());
                    break block3;
                }
                if (!(object instanceof float[])) break block16;
                this.writeInt(14);
                this.writeFloatArray((float[])object);
            }
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unsupported type ");
        stringBuilder.append(object.getClass());
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    @Override
    public void closeField() {
        FieldBuffer fieldBuffer = this.mFieldBuffer;
        if (fieldBuffer != null) {
            try {
                if (fieldBuffer.mOutput.size() != 0) {
                    this.mFieldBuffer.flushField();
                }
                this.mFieldBuffer = null;
            }
            catch (IOException iOException) {
                throw new VersionedParcel.ParcelException(iOException);
            }
        }
    }

    @Override
    protected VersionedParcel createSubParcel() {
        return new VersionedParcelStream(this.mCurrentInput, this.mCurrentOutput, this.mReadCache, this.mWriteCache, this.mParcelizerCache);
    }

    @Override
    public boolean isStream() {
        return true;
    }

    @Override
    public boolean readBoolean() {
        try {
            boolean bl = this.mCurrentInput.readBoolean();
            return bl;
        }
        catch (IOException iOException) {
            throw new VersionedParcel.ParcelException(iOException);
        }
    }

    @Override
    public Bundle readBundle() {
        int n = this.readInt();
        if (n < 0) {
            return null;
        }
        Bundle bundle = new Bundle();
        for (int i = 0; i < n; ++i) {
            String string2 = this.readString();
            this.readObject(this.readInt(), string2, bundle);
        }
        return bundle;
    }

    @Override
    public byte[] readByteArray() {
        block3: {
            int n;
            try {
                n = this.mCurrentInput.readInt();
                if (n <= 0) break block3;
            }
            catch (IOException iOException) {
                throw new VersionedParcel.ParcelException(iOException);
            }
            byte[] byArray = new byte[n];
            this.mCurrentInput.readFully(byArray);
            return byArray;
        }
        return null;
    }

    @Override
    protected CharSequence readCharSequence() {
        return null;
    }

    @Override
    public double readDouble() {
        try {
            double d = this.mCurrentInput.readDouble();
            return d;
        }
        catch (IOException iOException) {
            throw new VersionedParcel.ParcelException(iOException);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public boolean readField(int n) {
        try {
            while (true) {
                int n2;
                if ((n2 = this.mFieldId) == n) {
                    return true;
                }
                if (String.valueOf(n2).compareTo(String.valueOf(n)) > 0) {
                    return false;
                }
                int n3 = this.mCount;
                n2 = this.mFieldSize;
                if (n3 < n2) {
                    this.mMasterInput.skip(n2 - n3);
                }
                this.mFieldSize = -1;
                int n4 = this.mMasterInput.readInt();
                this.mCount = 0;
                n2 = n3 = n4 & 0xFFFF;
                if (n3 == 65535) {
                    n2 = this.mMasterInput.readInt();
                }
                this.mFieldId = 0xFFFF & n4 >> 16;
                this.mFieldSize = n2;
            }
        }
        catch (IOException iOException) {
            return false;
        }
    }

    @Override
    public float readFloat() {
        try {
            float f = this.mCurrentInput.readFloat();
            return f;
        }
        catch (IOException iOException) {
            throw new VersionedParcel.ParcelException(iOException);
        }
    }

    @Override
    public int readInt() {
        try {
            int n = this.mCurrentInput.readInt();
            return n;
        }
        catch (IOException iOException) {
            throw new VersionedParcel.ParcelException(iOException);
        }
    }

    @Override
    public long readLong() {
        try {
            long l = this.mCurrentInput.readLong();
            return l;
        }
        catch (IOException iOException) {
            throw new VersionedParcel.ParcelException(iOException);
        }
    }

    @Override
    public <T extends Parcelable> T readParcelable() {
        return null;
    }

    @Override
    public String readString() {
        block3: {
            int n;
            try {
                n = this.mCurrentInput.readInt();
                if (n <= 0) break block3;
            }
            catch (IOException iOException) {
                throw new VersionedParcel.ParcelException(iOException);
            }
            Object object = new byte[n];
            this.mCurrentInput.readFully((byte[])object);
            object = new String((byte[])object, UTF_16);
            return object;
        }
        return null;
    }

    @Override
    public IBinder readStrongBinder() {
        return null;
    }

    @Override
    public void setOutputField(int n) {
        FieldBuffer fieldBuffer;
        this.closeField();
        this.mFieldBuffer = fieldBuffer = new FieldBuffer(n, this.mMasterOutput);
        this.mCurrentOutput = fieldBuffer.mDataStream;
    }

    @Override
    public void setSerializationFlags(boolean bl, boolean bl2) {
        if (bl) {
            this.mIgnoreParcelables = bl2;
            return;
        }
        throw new RuntimeException("Serialization of this object is not allowed");
    }

    @Override
    public void writeBoolean(boolean bl) {
        try {
            this.mCurrentOutput.writeBoolean(bl);
            return;
        }
        catch (IOException iOException) {
            throw new VersionedParcel.ParcelException(iOException);
        }
    }

    /*
     * Unable to fully structure code
     */
    @Override
    public void writeBundle(Bundle var1_1) {
        if (var1_1 == null) ** GOTO lbl12
        try {
            block3: {
                var2_4 = var1_1.keySet();
                this.mCurrentOutput.writeInt(var2_4.size());
                var2_4 = var2_4.iterator();
                while (var2_4.hasNext()) {
                    var3_5 = (String)var2_4.next();
                    this.writeString(var3_5);
                    this.writeObject(var1_1.get(var3_5));
                }
                break block3;
lbl12:
                // 1 sources

                this.mCurrentOutput.writeInt(-1);
            }
            return;
        }
        catch (IOException var1_2) {
            var1_3 = new VersionedParcel.ParcelException(var1_2);
            throw var1_3;
        }
    }

    /*
     * Unable to fully structure code
     */
    @Override
    public void writeByteArray(byte[] var1_1) {
        if (var1_1 == null) ** GOTO lbl6
        try {
            block2: {
                this.mCurrentOutput.writeInt(var1_1.length);
                this.mCurrentOutput.write(var1_1);
                break block2;
lbl6:
                // 1 sources

                this.mCurrentOutput.writeInt(-1);
            }
            return;
        }
        catch (IOException var1_2) {
            throw new VersionedParcel.ParcelException(var1_2);
        }
    }

    /*
     * Unable to fully structure code
     */
    @Override
    public void writeByteArray(byte[] var1_1, int var2_3, int var3_4) {
        if (var1_1 == null) ** GOTO lbl6
        try {
            block2: {
                this.mCurrentOutput.writeInt(var3_4);
                this.mCurrentOutput.write(var1_1, var2_3, var3_4);
                break block2;
lbl6:
                // 1 sources

                this.mCurrentOutput.writeInt(-1);
            }
            return;
        }
        catch (IOException var1_2) {
            throw new VersionedParcel.ParcelException(var1_2);
        }
    }

    @Override
    protected void writeCharSequence(CharSequence charSequence) {
        if (this.mIgnoreParcelables) {
            return;
        }
        throw new RuntimeException("CharSequence cannot be written to an OutputStream");
    }

    @Override
    public void writeDouble(double d) {
        try {
            this.mCurrentOutput.writeDouble(d);
            return;
        }
        catch (IOException iOException) {
            throw new VersionedParcel.ParcelException(iOException);
        }
    }

    @Override
    public void writeFloat(float f) {
        try {
            this.mCurrentOutput.writeFloat(f);
            return;
        }
        catch (IOException iOException) {
            throw new VersionedParcel.ParcelException(iOException);
        }
    }

    @Override
    public void writeInt(int n) {
        try {
            this.mCurrentOutput.writeInt(n);
            return;
        }
        catch (IOException iOException) {
            throw new VersionedParcel.ParcelException(iOException);
        }
    }

    @Override
    public void writeLong(long l) {
        try {
            this.mCurrentOutput.writeLong(l);
            return;
        }
        catch (IOException iOException) {
            throw new VersionedParcel.ParcelException(iOException);
        }
    }

    @Override
    public void writeParcelable(Parcelable parcelable) {
        if (this.mIgnoreParcelables) {
            return;
        }
        throw new RuntimeException("Parcelables cannot be written to an OutputStream");
    }

    /*
     * Unable to fully structure code
     */
    @Override
    public void writeString(String var1_1) {
        if (var1_1 == null) ** GOTO lbl7
        try {
            block2: {
                var1_1 = var1_1.getBytes(VersionedParcelStream.UTF_16);
                this.mCurrentOutput.writeInt(((Object)var1_1).length);
                this.mCurrentOutput.write((byte[])var1_1);
                break block2;
lbl7:
                // 1 sources

                this.mCurrentOutput.writeInt(-1);
            }
            return;
        }
        catch (IOException var1_2) {
            throw new VersionedParcel.ParcelException(var1_2);
        }
    }

    @Override
    public void writeStrongBinder(IBinder iBinder) {
        if (this.mIgnoreParcelables) {
            return;
        }
        throw new RuntimeException("Binders cannot be written to an OutputStream");
    }

    @Override
    public void writeStrongInterface(IInterface iInterface) {
        if (this.mIgnoreParcelables) {
            return;
        }
        throw new RuntimeException("Binders cannot be written to an OutputStream");
    }

    private static class FieldBuffer {
        final DataOutputStream mDataStream;
        private final int mFieldId;
        final ByteArrayOutputStream mOutput;
        private final DataOutputStream mTarget;

        FieldBuffer(int n, DataOutputStream dataOutputStream) {
            ByteArrayOutputStream byteArrayOutputStream;
            this.mOutput = byteArrayOutputStream = new ByteArrayOutputStream();
            this.mDataStream = new DataOutputStream(byteArrayOutputStream);
            this.mFieldId = n;
            this.mTarget = dataOutputStream;
        }

        void flushField() throws IOException {
            this.mDataStream.flush();
            int n = this.mOutput.size();
            int n2 = this.mFieldId;
            int n3 = n >= 65535 ? 65535 : n;
            this.mTarget.writeInt(n2 << 16 | n3);
            if (n >= 65535) {
                this.mTarget.writeInt(n);
            }
            this.mOutput.writeTo(this.mTarget);
        }
    }
}

