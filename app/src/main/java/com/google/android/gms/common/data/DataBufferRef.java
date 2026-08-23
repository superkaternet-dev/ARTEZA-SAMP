/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.database.CharArrayBuffer
 *  android.net.Uri
 */
package com.google.android.gms.common.data;

import android.database.CharArrayBuffer;
import android.net.Uri;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.Preconditions;

public abstract class DataBufferRef {
    protected final DataHolder mDataHolder;
    protected int mDataRow;
    private int zaa;

    public DataBufferRef(DataHolder dataHolder, int n) {
        this.mDataHolder = Preconditions.checkNotNull(dataHolder);
        this.zaa(n);
    }

    protected void copyToBuffer(String string2, CharArrayBuffer charArrayBuffer) {
        this.mDataHolder.zac(string2, this.mDataRow, this.zaa, charArrayBuffer);
    }

    public boolean equals(Object object) {
        if (object instanceof DataBufferRef) {
            object = (DataBufferRef)object;
            if (Objects.equal(((DataBufferRef)object).mDataRow, this.mDataRow) && Objects.equal(((DataBufferRef)object).zaa, this.zaa) && ((DataBufferRef)object).mDataHolder == this.mDataHolder) {
                return true;
            }
        }
        return false;
    }

    protected boolean getBoolean(String string2) {
        return this.mDataHolder.getBoolean(string2, this.mDataRow, this.zaa);
    }

    protected byte[] getByteArray(String string2) {
        return this.mDataHolder.getByteArray(string2, this.mDataRow, this.zaa);
    }

    protected int getDataRow() {
        return this.mDataRow;
    }

    protected double getDouble(String string2) {
        return this.mDataHolder.zaa(string2, this.mDataRow, this.zaa);
    }

    protected float getFloat(String string2) {
        return this.mDataHolder.zab(string2, this.mDataRow, this.zaa);
    }

    protected int getInteger(String string2) {
        return this.mDataHolder.getInteger(string2, this.mDataRow, this.zaa);
    }

    protected long getLong(String string2) {
        return this.mDataHolder.getLong(string2, this.mDataRow, this.zaa);
    }

    protected String getString(String string2) {
        return this.mDataHolder.getString(string2, this.mDataRow, this.zaa);
    }

    public boolean hasColumn(String string2) {
        return this.mDataHolder.hasColumn(string2);
    }

    protected boolean hasNull(String string2) {
        return this.mDataHolder.hasNull(string2, this.mDataRow, this.zaa);
    }

    public int hashCode() {
        return Objects.hashCode(this.mDataRow, this.zaa, this.mDataHolder);
    }

    public boolean isDataValid() {
        return !this.mDataHolder.isClosed();
    }

    protected Uri parseUri(String string2) {
        if ((string2 = this.mDataHolder.getString(string2, this.mDataRow, this.zaa)) == null) {
            return null;
        }
        return Uri.parse((String)string2);
    }

    protected final void zaa(int n) {
        boolean bl = false;
        if (n >= 0 && n < this.mDataHolder.getCount()) {
            bl = true;
        }
        Preconditions.checkState(bl);
        this.mDataRow = n;
        this.zaa = this.mDataHolder.getWindowIndex(n);
    }
}

