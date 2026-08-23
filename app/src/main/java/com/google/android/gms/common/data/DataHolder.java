/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ContentValues
 *  android.database.CharArrayBuffer
 *  android.database.Cursor
 *  android.database.CursorIndexOutOfBoundsException
 *  android.database.CursorWindow
 *  android.os.Bundle
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.util.Log
 */
package com.google.android.gms.common.data;

import android.content.ContentValues;
import android.database.CharArrayBuffer;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.CursorWindow;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import com.google.android.gms.common.data.zab;
import com.google.android.gms.common.data.zac;
import com.google.android.gms.common.data.zad;
import com.google.android.gms.common.data.zae;
import com.google.android.gms.common.data.zaf;
import com.google.android.gms.common.internal.Asserts;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.sqlite.CursorWrapper;
import java.io.Closeable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class DataHolder
extends AbstractSafeParcelable
implements Closeable {
    public static final Parcelable.Creator<DataHolder> CREATOR = new zaf();
    private static final Builder zaf = new zab(new String[0], null);
    final int zaa;
    Bundle zab;
    int[] zac;
    int zad;
    boolean zae;
    private final String[] zag;
    private final CursorWindow[] zah;
    private final int zai;
    private final Bundle zaj;
    private boolean zak;

    DataHolder(int n, String[] stringArray, CursorWindow[] cursorWindowArray, int n2, Bundle bundle) {
        this.zae = false;
        this.zak = true;
        this.zaa = n;
        this.zag = stringArray;
        this.zah = cursorWindowArray;
        this.zai = n2;
        this.zaj = bundle;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public DataHolder(Cursor cursor, int n, Bundle bundle) {
        CursorWrapper cursorWrapper = new CursorWrapper(cursor);
        String[] stringArray = cursorWrapper.getColumnNames();
        ArrayList<Cursor> arrayList = new ArrayList<Cursor>();
        try {
            int n2;
            int n3 = cursorWrapper.getCount();
            cursor = cursorWrapper.getWindow();
            if (cursor != null) {
                if (cursor.getStartPosition() == 0) {
                    cursor.acquireReference();
                    cursorWrapper.setWindow(null);
                    arrayList.add(cursor);
                    n2 = cursor.getNumRows();
                } else {
                    n2 = 0;
                }
            } else {
                n2 = 0;
            }
            while (n2 < n3 && cursorWrapper.moveToPosition(n2)) {
                cursor = cursorWrapper.getWindow();
                if (cursor != null) {
                    cursor.acquireReference();
                    cursorWrapper.setWindow(null);
                } else {
                    super(false);
                    cursor.setStartPosition(n2);
                    cursorWrapper.fillWindow(n2, (CursorWindow)cursor);
                }
                if (cursor.getNumRows() == 0) {
                    break;
                }
                arrayList.add(cursor);
                n2 = cursor.getStartPosition();
                int n4 = cursor.getNumRows();
                n2 += n4;
            }
        }
        finally {
            cursorWrapper.close();
        }
        this(stringArray, arrayList.toArray(new CursorWindow[arrayList.size()]), n, bundle);
    }

    private DataHolder(Builder builder, int n, Bundle bundle) {
        this(builder.zaa, DataHolder.zaf(builder, -1), n, null);
    }

    /* synthetic */ DataHolder(Builder builder, int n, Bundle bundle, int n2, zae zae2) {
        this(builder.zaa, DataHolder.zaf(builder, -1), n, bundle);
    }

    /* synthetic */ DataHolder(Builder builder, int n, Bundle bundle, zae zae2) {
        this(builder, n, null);
    }

    public DataHolder(String[] stringArray, CursorWindow[] cursorWindowArray, int n, Bundle bundle) {
        this.zae = false;
        this.zak = true;
        this.zaa = 1;
        this.zag = Preconditions.checkNotNull(stringArray);
        this.zah = Preconditions.checkNotNull(cursorWindowArray);
        this.zai = n;
        this.zaj = bundle;
        this.zad();
    }

    public static Builder builder(String[] stringArray) {
        return new Builder(stringArray, null, null);
    }

    public static DataHolder empty(int n) {
        return new DataHolder(zaf, n, null);
    }

    private final void zae(String string2, int n) {
        Bundle bundle = this.zab;
        if (bundle != null && bundle.containsKey(string2)) {
            if (!this.isClosed()) {
                if (n >= 0 && n < this.zad) {
                    return;
                }
                throw new CursorIndexOutOfBoundsException(n, this.zad);
            }
            throw new IllegalArgumentException("Buffer is closed.");
        }
        string2 = (string2 = String.valueOf(string2)).length() != 0 ? "No such column: ".concat(string2) : new String("No such column: ");
        throw new IllegalArgumentException(string2);
    }

    /*
     * Unable to fully structure code
     */
    private static CursorWindow[] zaf(Builder var0, int var1_2) {
        var1_2 = Builder.zac((Builder)var0).length;
        var4_3 = 0;
        if (var1_2 == 0) {
            return new CursorWindow[0];
        }
        var12_4 = Builder.zab((Builder)var0);
        var5_5 = var12_4.size();
        var9_6 = new CursorWindow(false);
        var11_7 = new ArrayList<Object>();
        var11_7.add(var9_6);
        var9_6.setNumColumns(Builder.zac((Builder)var0).length);
        var2_8 = 0;
        for (var1_2 = 0; var1_2 < var5_5; ++var1_2) {
            block24: {
                block23: {
                    var6_10 = var9_6.allocRow();
                    if (var6_10) ** GOTO lbl42
                    var9_6 = new StringBuilder(72);
                    var9_6.append("Allocating additional cursor window for large data set (row ");
                    var9_6.append(var1_2);
                    var9_6.append(")");
                    Log.d((String)"DataHolder", (String)var9_6.toString());
                    var10_12 = new CursorWindow(false);
                    var10_12.setStartPosition(var1_2);
                    var10_12.setNumColumns(Builder.zac((Builder)var0).length);
                    var11_7.add(var10_12);
                    var9_6 = var10_12;
                    if (!var10_12.allocRow()) {
                        Log.e((String)"DataHolder", (String)"Unable to allocate row to hold data.");
                        var11_7.remove(var10_12);
                        return var11_7.toArray(new CursorWindow[var11_7.size()]);
                    }
lbl42:
                    // 3 sources

                    var14_14 = (Map)var12_4.get(var1_2);
                    var3_9 = 0;
                    var6_10 = true;
                    while (true) {
                        block25: {
                            if (var3_9 >= Builder.zac((Builder)var0).length) break block23;
                            if (!var6_10) break block24;
                            var10_12 = Builder.zac((Builder)var0)[var3_9];
                            var13_13 = var14_14.get(var10_12);
                            if (var13_13 != null) ** GOTO lbl56
                            var6_10 = var9_6.putNull(var1_2, var3_9);
                            break block25;
lbl56:
                            // 1 sources

                            if (var13_13 instanceof String) {
                                var6_10 = var9_6.putString((String)var13_13, var1_2, var3_9);
                                break block25;
                            }
                            if (var13_13 instanceof Long) {
                                var6_10 = var9_6.putLong(((Long)var13_13).longValue(), var1_2, var3_9);
                                break block25;
                            }
                            if (var13_13 instanceof Integer) {
                                var6_10 = var9_6.putLong((long)((Integer)var13_13).intValue(), var1_2, var3_9);
                                break block25;
                            }
                            if (!(var13_13 instanceof Boolean)) ** GOTO lbl70
                            var7_11 = true != (Boolean)var13_13 ? 0L : 1L;
                            var6_10 = var9_6.putLong(var7_11, var1_2, var3_9);
                            break block25;
lbl70:
                            // 1 sources

                            if (var13_13 instanceof byte[]) {
                                var6_10 = var9_6.putBlob((byte[])var13_13, var1_2, var3_9);
                                break block25;
                            }
                            if (var13_13 instanceof Double) {
                                var6_10 = var9_6.putDouble(((Double)var13_13).doubleValue(), var1_2, var3_9);
                                break block25;
                            }
                            if (!(var13_13 instanceof Float)) break;
                            var6_10 = var9_6.putDouble((double)((Float)var13_13).floatValue(), var1_2, var3_9);
                        }
                        ++var3_9;
                    }
                    try {
                        var9_6 = var13_13.toString();
                        var1_2 = String.valueOf(var10_12).length();
                        var2_8 = var9_6.length();
                        var12_4 = new StringBuilder(var1_2 + 32 + var2_8);
                        var12_4.append("Unsupported object for column ");
                        var12_4.append((String)var10_12);
                        var12_4.append(": ");
                        var12_4.append((String)var9_6);
                        var0 = new IllegalArgumentException(var12_4.toString());
                        throw var0;
                    }
                    catch (RuntimeException var0_1) {
                        var2_8 = var11_7.size();
                        for (var1_2 = var4_3; var1_2 < var2_8; ++var1_2) {
                            ((CursorWindow)var11_7.get(var1_2)).close();
                        }
                        throw var0_1;
                    }
                }
                if (var6_10) {
                    var2_8 = 0;
                    continue;
                }
            }
            if (var2_8 == 0) {
                var10_12 = new StringBuilder(74);
                var10_12.append("Couldn't populate window data for row ");
                var10_12.append(var1_2);
                var10_12.append(" - allocating new window.");
                Log.d((String)"DataHolder", (String)var10_12.toString());
                var9_6.freeLastRow();
                var9_6 = new CursorWindow(false);
                var9_6.setStartPosition(var1_2);
                var9_6.setNumColumns(Builder.zac((Builder)var0).length);
                var11_7.add(var9_6);
                --var1_2;
                var2_8 = 1;
                continue;
            }
            var0 = new zad("Could not add the value to a new CursorWindow. The size of value may be larger than what a CursorWindow can handle.");
            throw var0;
        }
        return var11_7.toArray(new CursorWindow[var11_7.size()]);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void close() {
        synchronized (this) {
            CursorWindow[] cursorWindowArray;
            if (this.zae) return;
            this.zae = true;
            for (int i = 0; i < (cursorWindowArray = this.zah).length; ++i) {
                cursorWindowArray[i].close();
            }
            return;
        }
    }

    protected final void finalize() throws Throwable {
        try {
            if (this.zak && this.zah.length > 0 && !this.isClosed()) {
                this.close();
                String string2 = this.toString();
                int n = String.valueOf(string2).length();
                StringBuilder stringBuilder = new StringBuilder(n + 178);
                stringBuilder.append("Internal data leak within a DataBuffer object detected!  Be sure to explicitly call release() on all DataBuffer extending objects when you are done with them. (internal object: ");
                stringBuilder.append(string2);
                stringBuilder.append(")");
                Log.e((String)"DataBuffer", (String)stringBuilder.toString());
            }
            return;
        }
        finally {
            super.finalize();
        }
    }

    public boolean getBoolean(String string2, int n, int n2) {
        this.zae(string2, n);
        return Long.valueOf(this.zah[n2].getLong(n, this.zab.getInt(string2))) == 1L;
    }

    public byte[] getByteArray(String string2, int n, int n2) {
        this.zae(string2, n);
        return this.zah[n2].getBlob(n, this.zab.getInt(string2));
    }

    public int getCount() {
        return this.zad;
    }

    public int getInteger(String string2, int n, int n2) {
        this.zae(string2, n);
        return this.zah[n2].getInt(n, this.zab.getInt(string2));
    }

    public long getLong(String string2, int n, int n2) {
        this.zae(string2, n);
        return this.zah[n2].getLong(n, this.zab.getInt(string2));
    }

    public Bundle getMetadata() {
        return this.zaj;
    }

    public int getStatusCode() {
        return this.zai;
    }

    public String getString(String string2, int n, int n2) {
        this.zae(string2, n);
        return this.zah[n2].getString(n, this.zab.getInt(string2));
    }

    public int getWindowIndex(int n) {
        int n2;
        int n3;
        block2: {
            int[] nArray;
            boolean bl = n >= 0 && n < this.zad;
            Preconditions.checkState(bl);
            for (n3 = 0; n3 < (n2 = (nArray = this.zac).length); ++n3) {
                if (n >= nArray[n3]) continue;
                n = n3 - 1;
                break block2;
            }
            n = n3;
        }
        n3 = n;
        if (n == n2) {
            n3 = n - 1;
        }
        return n3;
    }

    public boolean hasColumn(String string2) {
        return this.zab.containsKey(string2);
    }

    public boolean hasNull(String string2, int n, int n2) {
        this.zae(string2, n);
        return this.zah[n2].isNull(n, this.zab.getInt(string2));
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean isClosed() {
        synchronized (this) {
            return this.zae;
        }
    }

    public final void writeToParcel(Parcel parcel, int n) {
        int n2 = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeStringArray(parcel, 1, this.zag, false);
        SafeParcelWriter.writeTypedArray((Parcel)parcel, (int)2, (Parcelable[])this.zah, (int)n, (boolean)false);
        SafeParcelWriter.writeInt(parcel, 3, this.getStatusCode());
        SafeParcelWriter.writeBundle(parcel, 4, this.getMetadata(), false);
        SafeParcelWriter.writeInt(parcel, 1000, this.zaa);
        SafeParcelWriter.finishObjectHeader(parcel, n2);
        if ((n & 1) != 0) {
            this.close();
        }
    }

    public final double zaa(String string2, int n, int n2) {
        this.zae(string2, n);
        return this.zah[n2].getDouble(n, this.zab.getInt(string2));
    }

    public final float zab(String string2, int n, int n2) {
        this.zae(string2, n);
        return this.zah[n2].getFloat(n, this.zab.getInt(string2));
    }

    public final void zac(String string2, int n, int n2, CharArrayBuffer charArrayBuffer) {
        this.zae(string2, n);
        this.zah[n2].copyStringToBuffer(n, this.zab.getInt(string2), charArrayBuffer);
    }

    public final void zad() {
        String[] stringArray;
        int n;
        this.zab = new Bundle();
        int n2 = 0;
        for (n = 0; n < (stringArray = this.zag).length; ++n) {
            this.zab.putInt(stringArray[n], n);
        }
        this.zac = new int[this.zah.length];
        int n3 = 0;
        for (n = n2; n < (stringArray = this.zah).length; ++n) {
            this.zac[n] = n3;
            n2 = stringArray[n].getStartPosition();
            n3 += this.zah[n].getNumRows() - (n3 - n2);
        }
        this.zad = n3;
    }

    public static class Builder {
        private final String[] zaa;
        private final ArrayList<HashMap<String, Object>> zab;
        private final HashMap<Object, Integer> zac;

        /* synthetic */ Builder(String[] stringArray, String string2, zac zac2) {
            this.zaa = Preconditions.checkNotNull(stringArray);
            this.zab = new ArrayList();
            this.zac = new HashMap();
        }

        static /* bridge */ /* synthetic */ ArrayList zab(Builder builder) {
            return builder.zab;
        }

        public DataHolder build(int n) {
            return new DataHolder(this, n, null, null);
        }

        public DataHolder build(int n, Bundle bundle) {
            return new DataHolder(this, n, bundle, -1, null);
        }

        public Builder withRow(ContentValues object2) {
            Asserts.checkNotNull(object2);
            HashMap<String, Object> hashMap = new HashMap<String, Object>(object2.size());
            for (Object object2 : object2.valueSet()) {
                hashMap.put((String)object2.getKey(), object2.getValue());
            }
            return this.zaa(hashMap);
        }

        public Builder zaa(HashMap<String, Object> hashMap) {
            Asserts.checkNotNull(hashMap);
            this.zab.add(hashMap);
            return this;
        }
    }
}

