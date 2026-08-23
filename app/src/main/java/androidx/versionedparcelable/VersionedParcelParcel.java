/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.text.TextUtils
 *  android.util.SparseIntArray
 */
package androidx.versionedparcelable;

import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.SparseIntArray;
import androidx.collection.ArrayMap;
import androidx.versionedparcelable.VersionedParcel;
import java.lang.reflect.Method;

class VersionedParcelParcel
extends VersionedParcel {
    private static final boolean DEBUG = false;
    private static final String TAG = "VersionedParcelParcel";
    private int mCurrentField = -1;
    private final int mEnd;
    private int mFieldId = -1;
    private int mNextRead = 0;
    private final int mOffset;
    private final Parcel mParcel;
    private final SparseIntArray mPositionLookup = new SparseIntArray();
    private final String mPrefix;

    VersionedParcelParcel(Parcel parcel) {
        this(parcel, parcel.dataPosition(), parcel.dataSize(), "", new ArrayMap<String, Method>(), new ArrayMap<String, Method>(), new ArrayMap<String, Class>());
    }

    private VersionedParcelParcel(Parcel parcel, int n, int n2, String string2, ArrayMap<String, Method> arrayMap, ArrayMap<String, Method> arrayMap2, ArrayMap<String, Class> arrayMap3) {
        super(arrayMap, arrayMap2, arrayMap3);
        this.mParcel = parcel;
        this.mOffset = n;
        this.mEnd = n2;
        this.mNextRead = n;
        this.mPrefix = string2;
    }

    @Override
    public void closeField() {
        int n = this.mCurrentField;
        if (n >= 0) {
            int n2 = this.mPositionLookup.get(n);
            n = this.mParcel.dataPosition();
            this.mParcel.setDataPosition(n2);
            this.mParcel.writeInt(n - n2);
            this.mParcel.setDataPosition(n);
        }
    }

    @Override
    protected VersionedParcel createSubParcel() {
        int n;
        Parcel parcel = this.mParcel;
        int n2 = parcel.dataPosition();
        int n3 = n = this.mNextRead;
        if (n == this.mOffset) {
            n3 = this.mEnd;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.mPrefix);
        stringBuilder.append("  ");
        return new VersionedParcelParcel(parcel, n2, n3, stringBuilder.toString(), this.mReadCache, this.mWriteCache, this.mParcelizerCache);
    }

    @Override
    public boolean readBoolean() {
        boolean bl = this.mParcel.readInt() != 0;
        return bl;
    }

    @Override
    public Bundle readBundle() {
        return this.mParcel.readBundle(this.getClass().getClassLoader());
    }

    @Override
    public byte[] readByteArray() {
        int n = this.mParcel.readInt();
        if (n < 0) {
            return null;
        }
        byte[] byArray = new byte[n];
        this.mParcel.readByteArray(byArray);
        return byArray;
    }

    @Override
    protected CharSequence readCharSequence() {
        return (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(this.mParcel);
    }

    @Override
    public double readDouble() {
        return this.mParcel.readDouble();
    }

    @Override
    public boolean readField(int n) {
        boolean bl;
        while (true) {
            int n2 = this.mNextRead;
            int n3 = this.mEnd;
            bl = true;
            if (n2 >= n3) break;
            n3 = this.mFieldId;
            if (n3 == n) {
                return true;
            }
            if (String.valueOf(n3).compareTo(String.valueOf(n)) > 0) {
                return false;
            }
            this.mParcel.setDataPosition(this.mNextRead);
            n3 = this.mParcel.readInt();
            this.mFieldId = this.mParcel.readInt();
            this.mNextRead += n3;
        }
        if (this.mFieldId != n) {
            bl = false;
        }
        return bl;
    }

    @Override
    public float readFloat() {
        return this.mParcel.readFloat();
    }

    @Override
    public int readInt() {
        return this.mParcel.readInt();
    }

    @Override
    public long readLong() {
        return this.mParcel.readLong();
    }

    @Override
    public <T extends Parcelable> T readParcelable() {
        return (T)this.mParcel.readParcelable(this.getClass().getClassLoader());
    }

    @Override
    public String readString() {
        return this.mParcel.readString();
    }

    @Override
    public IBinder readStrongBinder() {
        return this.mParcel.readStrongBinder();
    }

    @Override
    public void setOutputField(int n) {
        this.closeField();
        this.mCurrentField = n;
        this.mPositionLookup.put(n, this.mParcel.dataPosition());
        this.writeInt(0);
        this.writeInt(n);
    }

    @Override
    public void writeBoolean(boolean bl) {
        this.mParcel.writeInt(bl ? 1 : 0);
    }

    @Override
    public void writeBundle(Bundle bundle) {
        this.mParcel.writeBundle(bundle);
    }

    @Override
    public void writeByteArray(byte[] byArray) {
        if (byArray != null) {
            this.mParcel.writeInt(byArray.length);
            this.mParcel.writeByteArray(byArray);
        } else {
            this.mParcel.writeInt(-1);
        }
    }

    @Override
    public void writeByteArray(byte[] byArray, int n, int n2) {
        if (byArray != null) {
            this.mParcel.writeInt(byArray.length);
            this.mParcel.writeByteArray(byArray, n, n2);
        } else {
            this.mParcel.writeInt(-1);
        }
    }

    @Override
    protected void writeCharSequence(CharSequence charSequence) {
        TextUtils.writeToParcel((CharSequence)charSequence, (Parcel)this.mParcel, (int)0);
    }

    @Override
    public void writeDouble(double d) {
        this.mParcel.writeDouble(d);
    }

    @Override
    public void writeFloat(float f) {
        this.mParcel.writeFloat(f);
    }

    @Override
    public void writeInt(int n) {
        this.mParcel.writeInt(n);
    }

    @Override
    public void writeLong(long l) {
        this.mParcel.writeLong(l);
    }

    @Override
    public void writeParcelable(Parcelable parcelable) {
        this.mParcel.writeParcelable(parcelable, 0);
    }

    @Override
    public void writeString(String string2) {
        this.mParcel.writeString(string2);
    }

    @Override
    public void writeStrongBinder(IBinder iBinder) {
        this.mParcel.writeStrongBinder(iBinder);
    }

    @Override
    public void writeStrongInterface(IInterface iInterface) {
        this.mParcel.writeStrongInterface(iInterface);
    }
}

