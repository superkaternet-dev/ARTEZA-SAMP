/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ContentValues
 *  android.os.Parcel
 *  android.os.Parcelable$Creator
 */
package com.google.android.gms.common.data;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.data.AbstractDataBuffer;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class DataBufferSafeParcelable<T extends SafeParcelable>
extends AbstractDataBuffer<T> {
    private static final String[] zaa = new String[]{"data"};
    private final Parcelable.Creator<T> zab;

    public DataBufferSafeParcelable(DataHolder dataHolder, Parcelable.Creator<T> creator) {
        super(dataHolder);
        this.zab = creator;
    }

    public static <T extends SafeParcelable> void addValue(DataHolder.Builder builder, T object) {
        Parcel parcel = Parcel.obtain();
        object.writeToParcel(parcel, 0);
        object = new ContentValues();
        object.put("data", parcel.marshall());
        builder.withRow((ContentValues)object);
        parcel.recycle();
    }

    public static DataHolder.Builder buildDataHolder() {
        return DataHolder.builder(zaa);
    }

    @Override
    public T get(int n) {
        DataHolder dataHolder = Preconditions.checkNotNull(this.mDataHolder);
        Object object = dataHolder.getByteArray("data", n, dataHolder.getWindowIndex(n));
        dataHolder = Parcel.obtain();
        dataHolder.unmarshall((byte[])object, 0, ((byte[])object).length);
        dataHolder.setDataPosition(0);
        object = (SafeParcelable)this.zab.createFromParcel((Parcel)dataHolder);
        dataHolder.recycle();
        return (T)object;
    }
}

