/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable$Creator
 *  android.util.SparseArray
 */
package com.google.android.gms.common.server.converter;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.server.converter.zac;
import com.google.android.gms.common.server.converter.zad;
import com.google.android.gms.common.server.response.FastJsonResponse;
import java.util.ArrayList;
import java.util.HashMap;

public final class StringToIntConverter
extends AbstractSafeParcelable
implements FastJsonResponse.FieldConverter<String, Integer> {
    public static final Parcelable.Creator<StringToIntConverter> CREATOR = new zad();
    final int zaa;
    private final HashMap<String, Integer> zab;
    private final SparseArray<String> zac;

    public StringToIntConverter() {
        this.zaa = 1;
        this.zab = new HashMap();
        this.zac = new SparseArray();
    }

    StringToIntConverter(int n, ArrayList<zac> arrayList) {
        this.zaa = n;
        this.zab = new HashMap();
        this.zac = new SparseArray();
        int n2 = arrayList.size();
        for (n = 0; n < n2; ++n) {
            zac zac2 = (zac)arrayList.get(n);
            this.add(zac2.zab, zac2.zac);
        }
    }

    public StringToIntConverter add(String string2, int n) {
        this.zab.put(string2, n);
        this.zac.put(n, (Object)string2);
        return this;
    }

    public final void writeToParcel(Parcel parcel, int n) {
        n = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.zaa);
        ArrayList<zac> arrayList = new ArrayList<zac>();
        for (String string2 : this.zab.keySet()) {
            arrayList.add(new zac(string2, this.zab.get(string2)));
        }
        SafeParcelWriter.writeTypedList(parcel, 2, arrayList, false);
        SafeParcelWriter.finishObjectHeader(parcel, n);
    }

    @Override
    public final int zaa() {
        return 7;
    }

    @Override
    public final int zab() {
        return 0;
    }
}

